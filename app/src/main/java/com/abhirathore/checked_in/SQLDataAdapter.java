package com.abhirathore.checked_in;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SQLDataAdapter extends RecyclerView.Adapter<SQLDataAdapter.ViewHolder> {

    // variable for our array list and context
    private ArrayList<SQLDataModel> sqlDataModelArrayList;
    private Context context;

    // constructor
    public SQLDataAdapter(ArrayList<SQLDataModel> sqlDataModelArrayListl, Context context) {
        this.sqlDataModelArrayList = sqlDataModelArrayListl;
        this.context = context;
    }


    @NonNull
    @Override
    public SQLDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booked_history_layout, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull SQLDataAdapter.ViewHolder holder, int position) {
        SQLDataModel model=sqlDataModelArrayList.get(position);
        holder.id_tv.setText(model.getId());
        holder.h_name_tv.setText(model.getHotel_n());
        holder.c_name_tv.setText(model.getCust_name());
        holder.date_tv.setText(model.getDate_v());
        holder.time_tv.setText(model.getTime_v());
        holder.days_tv.setText(model.getDays_v());
        holder.price_tv.setText(model.getPrice_v());
        holder.status_tv.setText(model.getStatus_v());
    }

    @Override
    public int getItemCount() {
        return sqlDataModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // creating variables for our text views.
        private TextView id_tv, h_name_tv, c_name_tv, date_tv,time_tv,days_tv,price_tv,status_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views
            id_tv=itemView.findViewById(R.id.id_value);
            h_name_tv=itemView.findViewById(R.id.Hotel_Name);
            c_name_tv=itemView.findViewById(R.id.CustomerName);
            date_tv=itemView.findViewById(R.id.Date_value);
            time_tv=itemView.findViewById(R.id.Time_value);
            days_tv=itemView.findViewById(R.id.Days_value);
            price_tv=itemView.findViewById(R.id.Price_value);
            status_tv=itemView.findViewById(R.id.Status_value);
        }
    }
}
