package com.example.simple_social_net.fragments.main_frag;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.simple_social_net.MainActivity;
import com.example.simple_social_net.MyApp;
import com.example.simple_social_net.R;
import com.example.simple_social_net.adapters.PostAdapter;
import com.example.simple_social_net.backend.APIService;
import com.example.simple_social_net.models.Posts;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PostsFragment extends Fragment {

    List<Posts> posts;
    String idString;
    String curIdString;
    private boolean isCurr;
    public PostsFragment(String curId, String id) {
        this.idString = id;
        curIdString = curId;
        isCurr = false;
    }

    private void checkData() {
        if (curIdString.equals(idString)){
            isCurr = true;
        }
    }

    private void setPosts(List<Posts> posts){
        this.posts = posts;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkData();
        Button btn = view.findViewById(R.id.btn_post_frag);
        if (!isCurr){
            btn.setVisibility(View.INVISIBLE);
        }
        Activity activity = getActivity();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)activity).postAddFrag();
            }
        });
        RecyclerView recyclerView = view.findViewById(R.id.rec_post_frag);
        recyclerView.setLayoutManager(new LinearLayoutManager((MainActivity)getActivity()));
        PostAdapter adapter = new PostAdapter(posts = new ArrayList<>(), ((MainActivity)getActivity()));
        recyclerView.setAdapter(adapter);
        APIService apiService = MyApp.getInstance().getApiService();
        apiService.getPostsByUserId(idString).enqueue(new Callback<List<Posts>>() {
            @Override
            public void onResponse(Call<List<Posts>> call, Response<List<Posts>> response) {
                if (response.isSuccessful()){
                    setPosts(response.body());
                    adapter.setPosts(response.body());
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
