package com.abhirathore.checked_in;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
public class Hotel_Viewholder extends RecyclerView.ViewHolder {

    View mView;
    public Hotel_Viewholder(@NonNull View itemView) {
        super(itemView);
        mView=itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        });
        //item long click
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemLongClick(view, getAdapterPosition());
                return true;
            }
        });
    }
    public void setDetails(Context context, String image, String name, String rating, String price)
    {
        TextView hotel_name=mView.findViewById(R.id.hotel_name_view);
        RatingBar hotel_rating=mView.findViewById(R.id.rating_bar);
        ImageView hotel_image=mView.findViewById(R.id.hotel_image);
        TextView hotel_price=mView.findViewById(R.id.price2);

        hotel_name.setText(name);
        Picasso.get().load(image).into(hotel_image);
        float rating_value=Float.valueOf(rating);
        hotel_rating.setRating(rating_value);

        //int price_of_hotel=Integer.valueOf(price);
        hotel_price.setText(price);
    }
    private Hotel_Viewholder.ClickListener mClickListener;
    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View  view, int position);
    }
    public void setOnClickListener(Hotel_Viewholder.ClickListener clickListener){
        mClickListener = clickListener;
    }


}
