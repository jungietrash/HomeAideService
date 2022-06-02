package com.homeaide.post.bookingv3.adaptorsfragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.homeaide.post.bookingv3.booking.Orders;
import com.homeaide.post.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterOrderItem extends RecyclerView.Adapter<AdapterOrderItem.ItemViewHolder> {

    List<Orders> arr;
    OnItemClickListener onItemClickListener;

    public AdapterOrderItem(List<Orders> arr) {
        this.arr = arr;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder
                (LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        String imageUriText = null;

        Orders listings = arr.get(position);
        holder.orderName.setText(listings.getItemName());

        imageUriText = listings.getImageUrl();
        Picasso.get()
                .load(imageUriText)
                .into(holder.orderPhoto);
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    public interface  OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener = listener;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{

        TextView orderName;
        ImageView orderPhoto;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            orderName = itemView.findViewById(R.id.tv_orderName);
            orderPhoto = itemView.findViewById(R.id.iv_orderPhoto);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onItemClickListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            onItemClickListener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }
}
