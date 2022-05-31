package com.abhirathore.checked_in;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Room_Details extends AppCompatActivity {
String room_no;
TextView room_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room__details);

        room_no=getIntent().getExtras().getString("room");

        room_text=(TextView)findViewById(R.id.room_number);
        room_text.setText(room_no);



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(Room_Details.this,HomeActivity.class));
    }
}
