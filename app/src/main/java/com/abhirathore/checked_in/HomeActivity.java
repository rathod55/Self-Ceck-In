package com.abhirathore.checked_in;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    Button search_button,history,logout;
    EditText location;
    String text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        cekSession();


        search_button = (Button) findViewById(R.id.search_btton);
        location = (EditText) findViewById(R.id.location_text);
        history=(Button)findViewById(R.id.History);
        logout=(Button)findViewById(R.id.logout_button);

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text = location.getText().toString();
                Intent show_hotels_intent = new Intent(HomeActivity.this, Show_Hotels.class);
                show_hotels_intent.putExtra("Hotel_Location", text.toLowerCase());
                startActivity(show_hotels_intent);

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPref.saveSharedSetting(HomeActivity.this,"CaptainCode","true");
                Intent logout=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(logout);
                Toast.makeText(getApplicationContext(),"Loged Out Succesfully", Toast.LENGTH_SHORT).show();
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,BookedHisotry.class));
            }
        });

    }
    public void cekSession()
    {
        Boolean Check=Boolean.valueOf(SharedPref.readSharedSetting(HomeActivity.this,"CaptainCode","true"));
        Intent introIntent = new Intent(HomeActivity.this,MainActivity.class);
        introIntent.putExtra("CaptainCode",Check);
        if(Check)
        {
            startActivity(introIntent);
            finish();
        }
    }
}


