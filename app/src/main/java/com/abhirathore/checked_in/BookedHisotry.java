package com.abhirathore.checked_in;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class BookedHisotry extends AppCompatActivity {

    // creating variables for our array list,
    // dbhandler, adapter and recycler view.
    private ArrayList<SQLDataModel> sqlDataModelArrayList;
    private DBHandler dbHandler;
    private SQLDataAdapter sqlDataAdapter;
    private RecyclerView boooked_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_hisotry);


        // initializing our all variables.
        sqlDataModelArrayList = new ArrayList<>();
        dbHandler = new DBHandler(BookedHisotry.this);

        // getting our course array
        // list from db handler class.
        sqlDataModelArrayList = dbHandler.sqlDataModelArrayList();

        // on below line passing our array lost to our adapter class.
        sqlDataAdapter = new SQLDataAdapter(sqlDataModelArrayList, BookedHisotry.this);
        boooked_view = findViewById(R.id.recycler_view2);

        // setting layout manager for our recycler view.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BookedHisotry.this, RecyclerView.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        boooked_view.setLayoutManager(linearLayoutManager);

        // setting our adapter to recycler view.
        boooked_view.setAdapter(sqlDataAdapter);
    }
}
