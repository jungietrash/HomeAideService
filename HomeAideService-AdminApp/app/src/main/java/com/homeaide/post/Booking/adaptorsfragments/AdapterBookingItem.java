package com.homeaide.post.Booking.adaptorsfragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.homeaide.post.Booking.booking.Booking;
import com.homeaide.post.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterBookingItem extends RecyclerView.Adapter<AdapterBookingItem.ItemViewHolder> {

    private List<Booking> arr;
    private OnItemClickListener onItemClickListener;

    public AdapterBookingItem(List<Booking> arr) {
        this.arr = arr;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder
                (LayoutInflater.from(parent.getContext()).inflate(R.layout.active_booking_viewpager, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        String imageUriText = null;


        Booking booking = arr.get(position);

        holder.tv_bookingName.setText(booking.getProjName());

        imageUriText = booking.getImageUrl();
        Picasso.get()
                .load(imageUriText)
                .into(holder.iv_bookingPhoto);

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
        TextView tv_bookingName, tv_bookingDay,tv_bookingDate, tv_bookingMonth, tv_bookingTime;
        ImageView iv_bookingPhoto;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_bookingName = itemView.findViewById(R.id.tv_bookingName);
            iv_bookingPhoto = itemView.findViewById(R.id.iv_bookingPhoto);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        onItemClickListener.onItemClick(position);
                    }

                }
            });


        }
    }

}