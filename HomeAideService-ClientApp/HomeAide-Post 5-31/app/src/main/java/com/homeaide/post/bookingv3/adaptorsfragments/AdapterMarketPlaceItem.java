package com.homeaide.post.bookingv3.adaptorsfragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.homeaide.post.bookingv3.booking.Listings;
import com.homeaide.post.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterMarketPlaceItem extends RecyclerView.Adapter<AdapterMarketPlaceItem.ItemViewHolder> {

    List<Listings> arr;
    OnItemClickListener onItemClickListener;

    public AdapterMarketPlaceItem(List<Listings> arr) {
        this.arr = arr;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder
                (LayoutInflater.from(parent.getContext()).inflate(R.layout.item_marketplace,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        String imageUriText = null;

        Listings listings = arr.get(position);
        holder.tv_itemName.setText(listings.getListName());
        holder.tv_itemRatings.setText(listings.getRatings());
        holder.tv_price.setText(listings.getListPrice());

        imageUriText = listings.getImageUrl();

        Picasso.get().load(imageUriText)
                .into(holder.iv_listingImage);
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

    public class ItemViewHolder extends RecyclerView.ViewHolder {


        TextView tv_itemName, tv_itemRatings, tv_price;
        ImageView iv_listingImage;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_price = itemView.findViewById(R.id.tv_price);
            tv_itemName = itemView.findViewById(R.id.tv_itemName);
            tv_itemRatings = itemView.findViewById(R.id.tv_itemRatings);
            iv_listingImage = itemView.findViewById(R.id.iv_listingImage);

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
