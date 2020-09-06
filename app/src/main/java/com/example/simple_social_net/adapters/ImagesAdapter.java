package com.example.simple_social_net.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simple_social_net.MyApp;
import com.example.simple_social_net.R;
import com.example.simple_social_net.backend.APIService;
import com.example.simple_social_net.models.Images;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImagesViewHolder> {
    List<Images> images;
    Activity activity;
    OnChoose onChoose;
    @NonNull
    @Override
    public ImagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_images,parent, false);
        return new ImagesViewHolder(view, onChoose);
    }

    public ImagesAdapter(List<Images> images, Activity activity, OnChoose onChoose) {
        this.images = new ArrayList<>();
        this.activity = activity;
        this.onChoose = onChoose;
    }

    public void setImages(List<Images> images){
        this.images = images;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesViewHolder holder, int position) {
        Picasso.get()
                .load(images.get(position).getUrl())
                .placeholder(R.drawable.place)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ImagesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        OnChoose onChoose;
        public ImagesViewHolder(@NonNull View itemView, OnChoose onChoose) {
            super(itemView);
            this.onChoose = onChoose;
            imageView = itemView.findViewById(R.id.image_image_rec);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onChoose.onTouch(getAdapterPosition());
        }
    }

    public interface OnChoose{
        void onTouch(int position);
    }
}
