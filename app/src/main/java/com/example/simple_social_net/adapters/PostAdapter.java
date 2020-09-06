package com.example.simple_social_net.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simple_social_net.MainActivity;
import com.example.simple_social_net.MyApp;
import com.example.simple_social_net.R;
import com.example.simple_social_net.backend.APIService;
import com.example.simple_social_net.models.Images;
import com.example.simple_social_net.models.Posts;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    List<Posts> posts;
    Activity activity;
    public PostAdapter(List<Posts> posts, Activity activity){
        this.posts = posts;
        this.activity = activity;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(view);
    }

    public void setPosts(List<Posts> posts){
        this.posts = posts;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Posts post = posts.get(position);
        APIService apiService = MyApp.getInstance().getApiService();
        holder.titleView.setText(post.getTitle());
        apiService.getAllImages().enqueue(new Callback<List<Images>>() {
            @Override
            public void onResponse(Call<List<Images>> call, Response<List<Images>> response) {
                if (response.isSuccessful()){
                    for (Images image : response.body()){
                        if (image.getName().equals(post.getMainPicName())){
                            Picasso.get()
                                    .load(image.getUrl())
                                    .placeholder(R.drawable.place)
                                    .into(holder.photoView);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Images>> call, Throwable t) {
                Toast.makeText(activity.getBaseContext(), "Server Connection Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        public TextView titleView;
        public ImageView photoView;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            photoView = itemView.findViewById(R.id.photo_post_rec);
            titleView = itemView.findViewById(R.id.title_post_rec);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity)activity).postDetailFrag(posts.get(getAdapterPosition()).getId());
                }
            });
        }
    }
}
