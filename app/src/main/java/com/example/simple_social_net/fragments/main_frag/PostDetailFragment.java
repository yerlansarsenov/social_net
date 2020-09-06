package com.example.simple_social_net.fragments.main_frag;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class PostDetailFragment extends Fragment {

    String postId;

    public PostDetailFragment(String id) {
        postId = id;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Activity activity = getActivity();
        TextView titleView = view.findViewById(R.id.title_post_det_frag);
        TextView descView = view.findViewById(R.id.desc_post_det_frag);
        ImageView imageView = view.findViewById(R.id.photo_post_det_frag);
        APIService apiService = MyApp.getInstance().getApiService();
        apiService.getAllPosts().enqueue(new Callback<List<Posts>>() {
            @Override
            public void onResponse(Call<List<Posts>> call, Response<List<Posts>> response) {
                if (response.isSuccessful()){
                    for(Posts post : response.body()){
                        if (post.getId().equals(postId)){
                            titleView.setText(post.getTitle());
                            descView.setText(post.getDescription());
                            apiService.getAllImages().enqueue(new Callback<List<Images>>() {
                                @Override
                                public void onResponse(Call<List<Images>> call, Response<List<Images>> response) {
                                    if (response.isSuccessful()){
                                        for(Images image : response.body()){
                                            if (image.getName().equals(post.getMainPicName())){
                                                Picasso.get()
                                                        .load(image.getUrl())
                                                        .placeholder(R.drawable.place)
                                                        .into(imageView);
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<Images>> call, Throwable t) {
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(activity.getBaseContext(), "Server Connection Error", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Posts>> call, Throwable t) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity.getBaseContext(), "Server Connection Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
