package com.homeaide.post.bookingv3.adaptorsfragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.homeaide.post.bookingv3.booking.Projects;
import com.homeaide.post.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterInstallerItem extends RecyclerView.Adapter<AdapterInstallerItem.ItemViewHolder> {

    List<Projects> arr;
    OnItemClickListener onItemClickListener;

    public AdapterInstallerItem() {
    }

    public AdapterInstallerItem(List<Projects> arr) {
        this.arr = arr;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder
                (LayoutInflater.from(parent.getContext()).inflate(R.layout.item_installer,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        String imageUriText = null;

        Projects project = arr.get(position);
        holder.projName.setText(project.getProjName());
        holder.projPrice.setText(project.getPrice() +" - "+ project.getRatings() + "stars");
        holder.projDesc.setText(project.getProjAddress());

        imageUriText = project.getImageUrl();

        Picasso.get().load(imageUriText)
                .into(holder.projectImage);

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

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView projName, projRatings, projPrice,projDesc;
        ImageView projectImage;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            projectImage = itemView.findViewById(R.id.iv_projectPhoto);
            projName = itemView.findViewById(R.id.tv_projName);
            projPrice = itemView.findViewById(R.id.tv_price);
            projDesc = itemView.findViewById(R.id.shortDesc);

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
