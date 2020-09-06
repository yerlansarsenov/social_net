package com.example.simple_social_net.adapters;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simple_social_net.MainActivity;
import com.example.simple_social_net.MyApp;
import com.example.simple_social_net.R;
import com.example.simple_social_net.backend.APIService;
import com.example.simple_social_net.models.Friends;
import com.example.simple_social_net.models.Images;
import com.example.simple_social_net.models.Users;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder> {
    List<Friends> friends;
    Activity activity;
    public FriendsAdapter(List<Friends> users, Activity activity){
        this.friends = users;
        this.activity = activity;
    }

    public void setList(List<Friends> list){
        this.friends = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item, parent, false);
        return new FriendsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsViewHolder holder, int position) {
        Friends friend = friends.get(position);
        //
        APIService apiService = MyApp.getInstance().getApiService();
        apiService.getUserById(friend.getFriendId()).enqueue(new Callback<List<Users>>() {
            @Override
            public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {
                Users user = response.body().get(0);
                holder.nameView.setText(user.getFirstName() + " " + user.getLastName());
                apiService.getAllImages().enqueue(new Callback<List<Images>>() {
                    @Override
                    public void onResponse(Call<List<Images>> call, Response<List<Images>> response) {
                        for (Images image : response.body()){
                            if (image.getName().equals(user.getAvaName())){
                                Picasso.get()
                                        .load(image.getUrl())
                                        .placeholder(R.drawable.place)
                                        .into(holder.avaView);
                                break;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Images>> call, Throwable t) {
                        Log.e("recView", t.getMessage());
                        Toast.makeText(activity.getBaseContext(), "Server Connection Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Users>> call, Throwable t) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public class FriendsViewHolder extends RecyclerView.ViewHolder {
        public ImageView avaView;
        public TextView nameView;
        public FriendsViewHolder(@NonNull View itemView) {
            super(itemView);
            avaView = itemView.findViewById(R.id.ava_friend_rec);
            nameView = itemView.findViewById(R.id.name_friend_rec);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity)activity).profileFrag(friends.get(getAdapterPosition()).getFriendId());
                }
            });
        }

    }

}
