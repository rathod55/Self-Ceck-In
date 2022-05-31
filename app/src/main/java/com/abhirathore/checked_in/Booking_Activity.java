package com.abhirathore.checked_in;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;


public class Booking_Activity extends AppCompatActivity {
    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "Booking_Activity";
    EditText full_name,age,days,getid,time;
    static EditText date;
    Spinner id_spinner;
    Button choose_image,clear,proceed;
    ImageView user_image;
    String hotel_name,token,price;
    public Uri selectedImageUri;
    String uri;
    String hotel_location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_);

        //getting extras from previous activity
        hotel_name=getIntent().getExtras().getString("hotel_name");
        token=getIntent().getExtras().getString("token");
        price=getIntent().getExtras().getString("price");
        hotel_location=getIntent().getExtras().getString("hotel_location");

        //Initialization of Edittext
        full_name=(EditText)findViewById(R.id.full_name_edittext);
        age=(EditText)findViewById(R.id.age_edittext);
        days=(EditText)findViewById(R.id.days_edittext);
        getid=(EditText)findViewById(R.id.get_id_number);
        date=(EditText)findViewById(R.id.date_edittext);
        time=(EditText)findViewById(R.id.time_edittext);

        //INitializing the Spinner
        id_spinner=(Spinner)findViewById(R.id.id_proof_spinner);

        //Initializing the Buttons
        choose_image=(Button)findViewById(R.id.choose_image_button);
        clear=(Button)findViewById(R.id.clear_values);
        proceed=(Button)findViewById(R.id.proceed);

        //Initializing the Image View
        user_image=(ImageView)findViewById(R.id.user_image);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearValues();
            }
        });

        choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        //settting the adapter for spinner to select id
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.id_proof, android.R.layout.simple_list_item_1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        id_spinner.setAdapter(adapter1);

        id_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getid.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                id_spinner.setPrompt("Select the id");
            }
        });

        //action of date selector;
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTime(v);
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(full_name.getText().equals("") || age.getText().equals("") || days.getText().equals("")|| getid.getText().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Enter all The Details",Toast.LENGTH_LONG).show();
                }
                else if(user_image.getDrawable()==null)
                {
                    Toast.makeText(getApplicationContext(),"PLease Select the Image",Toast.LENGTH_LONG).show();
                }
                else{
                    Intent payment_intent=new Intent(Booking_Activity.this, Payment_Activity.class);
                    payment_intent.putExtra("hotel_name",hotel_name);
                    payment_intent.putExtra("token",token);
                    payment_intent.putExtra("price",price);
                    payment_intent.putExtra("days",days.getText().toString());
                    payment_intent.putExtra("user_name",full_name.getText().toString());
                    payment_intent.putExtra("user_image_uri",uri);
                    payment_intent.putExtra("id_details",getid.getText().toString());
                    payment_intent.putExtra("date",date.getText().toString());
                    payment_intent.putExtra("time",time.getText().toString());
                    payment_intent.putExtra("hotel_location",hotel_location);
                    startActivity(payment_intent);
                }
            }
        });
    }

    public void getTime(View v)
    {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Booking_Activity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        time.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }




    //date picker fucnitonality
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new Booking_Activity.DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog datePickerDialog= new DatePickerDialog(getActivity(), this, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(c.getTime().getTime());
            Log.d("Picker timestamp",c.getTime().getTime()+"");
            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            date.setText(day + "/" + (month + 1) + "/" + year);
        }
    }

    //function to clear values of editext and set to default
    void clearValues()
    {
        full_name.setText("");
        age.setText("");
        days.setText("");
        getid.setText("");
        getid.setVisibility(View.GONE);
    }
    //function to choose image from the gallery;
    void chooseImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                // Get the url from data
                selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // Get the path from the Uri
                    String path = getPathFromURI(selectedImageUri);
                    Log.i(TAG, "Image Path : " + path);
                    // Set the image in ImageView
                    user_image.setImageURI(selectedImageUri);
                    uri=String.valueOf(selectedImageUri);
                }
            }
        }
    }
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }


}
