package com.abhirathore.checked_in;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class Show_Hotels extends AppCompatActivity {
FirebaseDatabase firebaseDatabase;
DatabaseReference databaseReference;
RecyclerView recyclerView;
LinearLayoutManager linearLayoutManager;
String location_name;
ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__hotels);

        location_name=getIntent().getExtras().getString("Hotel_Location");
        recyclerView=findViewById(R.id.recycler_view);
        progressDialog=new ProgressDialog(Show_Hotels.this);
        progressDialog.setTitle("Searching");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        firebaseDatabase=FirebaseDatabase.getInstance();

        linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        databaseReference=firebaseDatabase.getReference("hotel/"+location_name);

        FirebaseRecyclerAdapter<Hotel_Model, Hotel_Viewholder> firebaseRecyclerAdapter =
                        new FirebaseRecyclerAdapter<Hotel_Model, Hotel_Viewholder>(
                                Hotel_Model.class,
                                R.layout.hotel_model,
                                Hotel_Viewholder.class,
                                databaseReference

                        ) {
                            @Override
                            protected void populateViewHolder(Hotel_Viewholder viewHolder, Hotel_Model model, int position) {
                                viewHolder.setDetails(getApplicationContext(), model.getImage(), model.getName(), model.getRating(),model.getPrice());
                                Toast.makeText(getApplicationContext(),"Connection with Database Succesfull",Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public Hotel_Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {

                                Hotel_Viewholder viewHolder = super.onCreateViewHolder(parent, viewType);

                                viewHolder.setOnClickListener(new Hotel_Viewholder.ClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {

                                        String token = getItem(position).getToken();
                                        String img_address=getItem(position).getImage();
                                        String hotel_name=getItem(position).getName();
                                        String hotel_price=getItem(position).getPrice();

                                        Intent intent = new Intent(Show_Hotels.this, Hotel_Details.class);
                                        intent.putExtra("token", token);
                                        intent.putExtra("image_address",img_address);
                                        intent.putExtra("hotel_name",hotel_name);
                                        intent.putExtra("price",hotel_price);
                                        intent.putExtra("hotel_location",location_name);
                                        startActivity(intent);


                                    }

                                    @Override
                                    public void onItemLongClick(View view, int position) {

                                    }
                                });

                                return viewHolder;
                            }

                        };




        recyclerView.setAdapter(firebaseRecyclerAdapter);
        progressDialog.dismiss();
    }
    }
