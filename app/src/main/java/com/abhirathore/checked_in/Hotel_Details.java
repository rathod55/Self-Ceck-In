package com.abhirathore.checked_in;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Hotel_Details extends AppCompatActivity {

    ImageView image;
    TextView name;
    ListView listView;
    String image_address,hotel_name,hotel_token,price,hotel_location;
    String[] hotel_facilities;
    Button book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel__details);

        image_address=getIntent().getExtras().getString("image_address");
        hotel_name=getIntent().getExtras().getString("hotel_name");
        hotel_token=getIntent().getExtras().getString("token");
        price=getIntent().getExtras().getString("price");
        hotel_location=getIntent().getExtras().getString("hotel_location");

        image=(ImageView)findViewById(R.id.hotel_img);
        name=(TextView)findViewById(R.id.hotel_name);
        listView=(ListView)findViewById(R.id.list_view);
        book=(Button)findViewById(R.id.book_button);

        name.setText(hotel_name);
        Picasso.get().load(image_address).into(image);

        hotel_facilities=getResources().getStringArray(R.array.facilities);

        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,hotel_facilities);
        listView.setAdapter(adapter);

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent booking=new Intent(Hotel_Details.this,Booking_Activity.class);
                booking.putExtra("hotel_name",hotel_name);
                booking.putExtra("token",hotel_token);
                booking.putExtra("price",price);
                booking.putExtra("hotel_location",hotel_location);
                startActivity(booking);
            }
        });

    }
}
