package com.abhirathore.checked_in;
import android.database.DatabaseErrorHandler;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private Connection connection;


    private final String host = "database-1.ccocdplwulhl.us-east-1.rds.amazonaws.com";

    private final String database = "Hotel";
    private final int port = 5432;
    private final String user = "postgres";
    private final String pass = "Abhijeet";
    private String url = "jdbc:postgresql://%s:%d/%s";
    private final String INSERT = "INSERT INTO hotel_details (b_id,h_name,c_name,b_date,b_time,b_days,b_price,b_room,id_proof,photo_proof) VALUES (?,?,?,?,?,?,?,?,?,?)";
    private boolean status;

    public Database(String b_id,String hotel,String customer,String date,String time, String days, String price, String room, String idProof,String photoProof)
    {
        this.url = String.format(this.url, this.host, this.port, this.database);
        connect();
        try(PreparedStatement statement = connection.prepareStatement(INSERT)){

            statement.setEscapeProcessing(false);
            statement.setString(1,b_id);
            statement.setString(2,hotel);
            statement.setString(3,customer);
            statement.setString(4,date);
            statement.setString(5,time);
            statement.setString(6,days);
            statement.setString(7,price);
            statement.setString(8,room);
            statement.setString(9,idProof);
            statement.setString(10,photoProof);
            //statement.executeUpdate();
            if(statement.executeUpdate()<=0)
            {

            }

            Log.d("insertion","inserted succesfully");
        }
        catch(SQLException sql)
        {
            sql.getMessage();
        }

        //this.disconnect();
       Log.d("status","status");
    }

    private void connect()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    Class.forName("org.postgresql.Driver");
                    connection = DriverManager.getConnection(url, user, pass);
                    status = true;
                    Log.d("connected:", "Connected");
                }
                catch (Exception e)
                {
                    status = false;
                    System.out.print(e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try
        {
            thread.join();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            this.status = false;
        }
    }


    public Connection getExtraConnection()
    {
        Connection c = null;
        try
        {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(url, user, pass);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return c;
    }
}
