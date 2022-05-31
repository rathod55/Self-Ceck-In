package com.abhirathore.checked_in;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;


public class Payment_Activity extends AppCompatActivity  {
TextView hotel_name, hotel_price,user_name,days,date_and_time;
String name,h_price,c_name,n_days,token,id_d;
Spinner payment_spinner;
String[] payment;
CardView cardView;
Button confirm_payment;
ProgressDialog progressDialog;
FirebaseDatabase firebaseDatabase;
DatabaseReference databaseReference,databaseReference1;
Query query;
String uri;
FirebaseStorage firebaseStorage;
StorageReference storageReference;
public int total,x;
String total_rooms;
String booked_rooms;
String final_book;
String date,time;
String url;
EditText card_number,card_date,card_cvc;
String hotel_location;
//handling paymeny styles

    private static final int CARD_NUMBER_TOTAL_SYMBOLS = 19; // size of pattern 0000-0000-0000-0000
    private static final int CARD_NUMBER_TOTAL_DIGITS = 16; // max numbers of digits in pattern: 0000 x 4
    private static final int CARD_NUMBER_DIVIDER_MODULO = 5; // means divider position is every 5th symbol beginning with 1
    private static final int CARD_NUMBER_DIVIDER_POSITION = CARD_NUMBER_DIVIDER_MODULO - 1; // means divider position is every 4th symbol beginning with 0
    private static final char CARD_NUMBER_DIVIDER = '-';

    private static final int CARD_DATE_TOTAL_SYMBOLS = 5; // size of pattern MM/YY
    private static final int CARD_DATE_TOTAL_DIGITS = 4; // max numbers of digits in pattern: MM + YY
    private static final int CARD_DATE_DIVIDER_MODULO = 3; // means divider position is every 3rd symbol beginning with 1
    private static final int CARD_DATE_DIVIDER_POSITION = CARD_DATE_DIVIDER_MODULO - 1; // means divider position is every 2nd symbol beginning with 0
    private static final char CARD_DATE_DIVIDER = '/';

    private static final int CARD_CVC_TOTAL_SYMBOLS = 3;

private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_);

        //getting extras
        name=getIntent().getExtras().getString("hotel_name");
        h_price=getIntent().getExtras().getString("price");
        n_days=getIntent().getExtras().getString("days");
        c_name=getIntent().getExtras().getString("user_name");
        token=getIntent().getExtras().getString("token");
        uri=getIntent().getExtras().getString("user_image_uri");
        id_d=getIntent().getExtras().getString("id_details");
        date=getIntent().getExtras().getString("date");
        time=getIntent().getExtras().getString("time");
        hotel_location=getIntent().getExtras().getString("hotel_location");

        //initializing the textview
        hotel_name=(TextView)findViewById(R.id.hotel_name_set);
        hotel_price=(TextView)findViewById(R.id.price_set);
        user_name=(TextView)findViewById(R.id.hotel_customer_set);
        days=(TextView)findViewById(R.id.days_set);
        date_and_time=(TextView)findViewById(R.id.date_time_set);

        //Initializing the Edit Text
        card_number = (EditText) findViewById(R.id.cardNumberEditText);
        card_date = (EditText) findViewById(R.id.cardDateEditText);
        card_cvc = (EditText) findViewById(R.id.cardCVCEditText);

        setValues();

        //Initializing the Card VIew
        cardView=findViewById(R.id.card_view2);

        //Initializing the spinner
        payment_spinner=(Spinner)findViewById(R.id.payment_select_spinner);
        payment=getResources().getStringArray(R.array.payment_methods);
        ArrayAdapter<CharSequence> arrayAdapter=ArrayAdapter.createFromResource(this,R.array.payment_methods,android.R.layout.simple_list_item_1);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        payment_spinner.setAdapter(arrayAdapter);
        payment_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //Initializing the DB Handler to handle Database Operations
        dbHandler=new DBHandler(Payment_Activity.this);

        progressDialog=new ProgressDialog(this);
        confirm_payment=(Button) findViewById(R.id.confirm_payment);
        confirm_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToDatabase();

            }
        });

        //Handling Events for Card NUmber
        card_number.addTextChangedListener(new TextWatcher() {

            int len = 0;
            String string;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }


            @Override
            public void afterTextChanged(Editable s) {
                if (!isInputCorrect(s, CARD_NUMBER_TOTAL_SYMBOLS, CARD_NUMBER_DIVIDER_MODULO, CARD_NUMBER_DIVIDER)) {
                    s.replace(0, s.length(), buildCorrectString(getDigitArray(s, CARD_NUMBER_TOTAL_DIGITS), CARD_NUMBER_DIVIDER_POSITION, CARD_NUMBER_DIVIDER));
                }
            }

            private boolean isInputCorrect(Editable s, int totalSymbols, int dividerModulo, char divider) {
                boolean isCorrect = s.length() <= totalSymbols; // check size of entered string
                for (int i = 0; i < s.length(); i++) { // check that every element is right
                    if (i > 0 && (i + 1) % dividerModulo == 0) {
                        isCorrect &= divider == s.charAt(i);
                    } else {
                        isCorrect &= Character.isDigit(s.charAt(i));
                    }
                }
                return isCorrect;
            }

            private String buildCorrectString(char[] digits, int dividerPosition, char divider) {
                final StringBuilder formatted = new StringBuilder();

                for (int i = 0; i < digits.length; i++) {
                    if (digits[i] != 0) {
                        formatted.append(digits[i]);
                        if ((i > 0) && (i < (digits.length - 1)) && (((i + 1) % dividerPosition) == 0)) {
                            formatted.append(divider);
                        }
                    }
                }

                return formatted.toString();
            }

            private char[] getDigitArray(final Editable s, final int size) {
                char[] digits = new char[size];
                int index = 0;
                for (int i = 0; i < s.length() && index < size; i++) {
                    char current = s.charAt(i);
                    if (Character.isDigit(current)) {
                        digits[index] = current;
                        index++;
                    }
                }
                return digits;
            }
        });

        //Handling Events for Card Expiry
        card_date.addTextChangedListener(new TextWatcher() {
            int len = 0;
            String string;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!isInputCorrect(s, CARD_DATE_TOTAL_SYMBOLS, CARD_DATE_DIVIDER_MODULO, CARD_DATE_DIVIDER)) {
                    s.replace(0, s.length(), concatString(getDigitArray(s, CARD_DATE_TOTAL_DIGITS), CARD_DATE_DIVIDER_POSITION, CARD_DATE_DIVIDER));
                }

            }

            private boolean isInputCorrect(Editable s, int totalSymbols, int dividerModulo, char divider) {
                boolean isCorrect = s.length() <= totalSymbols; // check size of entered string
                for (int i = 0; i < s.length(); i++) { // check that every element is right
                    if (i > 0 && (i + 1) % dividerModulo == 0) {
                        isCorrect &= divider == s.charAt(i);
                    } else {
                        isCorrect &= Character.isDigit(s.charAt(i));
                    }
                }
                return isCorrect;
            }

            private String concatString(char[] digits, int dividerPosition, char divider) {
                final StringBuilder formatted = new StringBuilder();

                for (int i = 0; i < digits.length; i++) {
                    if (digits[i] != 0) {
                        formatted.append(digits[i]);
                        if ((i > 0) && (i < (digits.length - 1)) && (((i + 1) % dividerPosition) == 0)) {
                            formatted.append(divider);
                        }
                    }
                }

                return formatted.toString();
            }

            private char[] getDigitArray(final Editable s, final int size) {
                char[] digits = new char[size];
                int index = 0;
                for (int i = 0; i < s.length() && index < size; i++) {
                    char current = s.charAt(i);
                    if (Character.isDigit(current)) {
                        digits[index] = current;
                        index++;
                    }
                }
                return digits;
            }
        });


        //Handling Card CVC Events
        card_cvc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int len = 0;
                String string;

                if (s.length() > CARD_CVC_TOTAL_SYMBOLS) {
                    s.delete(CARD_CVC_TOTAL_SYMBOLS, s.length());
                }

            }


        });





    }
    void setValues()
    {
        hotel_name.setText(name);
        user_name.setText(c_name);
        days.setText(n_days);

        int days=Integer.valueOf(n_days);
        int price=Integer.valueOf(h_price);
        total=price*days;

        hotel_price.setText(total+" RS");
        date_and_time.setText(date+"   "+time);
    }

    void saveToDatabase()
    {
        progressDialog.setTitle("Confirming");
        progressDialog.show();
        firebaseDatabase=FirebaseDatabase.getInstance();
        String generateId=String.valueOf(System.currentTimeMillis());
        //databaseReference=firebaseDatabase.getReference("booked_history/"+generateId);

        //DatabaseReference id=databaseReference.child("booking_id");
//        id.setValue(generateId);
//
//        DatabaseReference hotel_name=databaseReference.child("hotel");
//        hotel_name.setValue(name);
//
//        DatabaseReference days=databaseReference.child("days");
//        days.setValue(n_days);
//
//        DatabaseReference customer_name=databaseReference.child("name");
//        customer_name.setValue(c_name);
//
//        DatabaseReference price_paid=databaseReference.child("price_paid");
//        price_paid.setValue(total+" RS");
//
//        DatabaseReference id_details=databaseReference.child("id_Details");
//        id_details.setValue(id_d);
//
//        DatabaseReference date_time=databaseReference.child("date_time");
        //String concatenate=date+time;
        //date_time.setValue(concatenate);

        //DatabaseReference profile_path=databaseReference.child("image");

        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        if(uri!=null)
        {
            StorageReference ref=storageReference.child("users/"+ UUID.randomUUID().toString());
            ref.putFile(Uri.parse(uri)).
                    addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                             url=taskSnapshot.getMetadata().getPath();
                            //profile_path.setValue(url);
                            //progressDialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast
                            .makeText(Payment_Activity.this,
                                    "Something went wrong " + e.getMessage(),
                                    Toast.LENGTH_SHORT)
                            .show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress
                            = (100.0
                            * snapshot.getBytesTransferred()
                            / snapshot.getTotalByteCount());
                    progressDialog.setMessage(
                            "Payment Successful "
                                    + (int)progress + "%");
                }
            });



        }

        databaseReference1=firebaseDatabase.getReference("hotel/"+hotel_location);
        query=databaseReference1.orderByChild("token").equalTo(token);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot1:snapshot.getChildren())
                {
                    total_rooms=dataSnapshot1.child("rooms").getValue().toString();
                    booked_rooms=dataSnapshot1.child("booked").getValue().toString();

                    //COnverting into Integer
                    int total=Integer.valueOf(total_rooms);
                    int book=Integer.valueOf(booked_rooms);

                    if(book>=0 && book<=total)
                    {
                        book=book+1;

                        if(book<=total)
                        {
                            x=book;
                            query.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    databaseReference1.child(snapshot.getKey()).child("booked").setValue(String.valueOf(x));
                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                }

                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                }

                                @Override
                                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            String concatRoomandStatus=(String.valueOf(x))+" booked";
                            dbHandler.addDetails(generateId,name,c_name,date,time,n_days,String.valueOf(total),concatRoomandStatus);

                            //adding data to Postgre sql database
                            try{
                                Database db = new Database(generateId,name,c_name,date,time,n_days,String.valueOf(total),concatRoomandStatus,id_d,uri);
                                //db.addDetails("pink", "Jk","fdjf","jkdj","jfdkjf","jfkdj","kfdlkf","jfkdjf");
                                Toast.makeText(getApplicationContext(),"Connection Succesfull",Toast.LENGTH_SHORT).show();
                            }catch(Exception e)
                            {
                               // Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                            }
                            //Database db=new Database();
                            final_book=String.valueOf(x);
                            Intent show_room=new Intent(Payment_Activity.this,Room_Details.class);
                            show_room.putExtra("room",final_book);
                            startActivity(show_room);
                            progressDialog.dismiss();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Sorry All Room are booked",Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Sorry All Room are booked",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

}
