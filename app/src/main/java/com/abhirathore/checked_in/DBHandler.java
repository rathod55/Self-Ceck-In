/* Database to Store Booked Room History for
User that will be displayed inbooking history activity */

package com.abhirathore.checked_in;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    private final static String DB_NAME="bookinghistory";
    private final static String TABLE_NAME="booked";
    private final static int DB_VERSION=1;
    private final static String ID_COL="id";
    private final static String H_NAME="hotel";
    private final static String C_NAME="name";
    private final static String DATE="date";
    private final static String TIME="time";
    private final static String DAYS="days";
    private final static String PRICE="price";
    private final static String STATUS="status";

    public DBHandler( Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " TEXT, "
                + H_NAME + " TEXT,"
                + C_NAME + " TEXT,"
                + DATE + " TEXT,"
                + TIME + " TEXT,"
                + DAYS + " TEXT,"
                + PRICE + " TEXT,"
                + STATUS + " TEXT)";
        db.execSQL(query);
    }

    public void addDetails(String b_id, String hotel_name,String customer_name, String date, String time, String days, String price, String status)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(ID_COL,b_id);
        contentValues.put(H_NAME,hotel_name);
        contentValues.put(C_NAME,customer_name);
        contentValues.put(DATE,date);
        contentValues.put(TIME,time);
        contentValues.put(DAYS,days);
        contentValues.put(PRICE,price);
        contentValues.put(STATUS,status);

        db.insert(TABLE_NAME,null,contentValues);
        db.close();
    }


    // we have created a new method for reading all the courses.
    public ArrayList<SQLDataModel> sqlDataModelArrayList() {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        // on below line we are creating a new array list.
        ArrayList<SQLDataModel> courseModalArrayList = new ArrayList<>();

        // moving our cursor to first position.
        if (cursorCourses.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                courseModalArrayList.add(new SQLDataModel(cursorCourses.getString(0),
                        cursorCourses.getString(1),
                        cursorCourses.getString(2),
                        cursorCourses.getString(3),
                        cursorCourses.getString(4),
                        cursorCourses.getString(5),
                        cursorCourses.getString(6),
                        cursorCourses.getString(7)));
            } while (cursorCourses.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursorCourses.close();
        return courseModalArrayList;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
