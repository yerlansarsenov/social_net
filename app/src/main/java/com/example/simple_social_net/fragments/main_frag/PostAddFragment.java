package com.example.simple_social_net.fragments.main_frag;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.simple_social_net.MainActivity;
import com.example.simple_social_net.MyApp;
import com.example.simple_social_net.R;
import com.example.simple_social_net.backend.APIService;
import com.example.simple_social_net.fragments.ImagesFragment;
import com.example.simple_social_net.models.Posts;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PostAddFragment extends Fragment {

    String curId;
    Activity activity;
    public PostAddFragment(String idString) {
        curId = idString;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();
        EditText titleView = view.findViewById(R.id.title_post_add_frag);
        EditText descView = view.findViewById(R.id.desc_post_add_frag);
        Button chooseBtn = view.findViewById(R.id.choose_post_add_frag);
        Button addBtn = view.findViewById(R.id.add_post_add_frag);
        chooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                ImagesFragment imagesFragment = new ImagesFragment();
                imagesFragment.show(fragmentManager, "Choose image");
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String chosen = ((MainActivity)activity).getChosenImage();
                if (titleView.getText().toString().isEmpty() || descView.getText().toString().isEmpty()){
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity.getBaseContext(), "Please, fill all the gaps", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                if (chosen.equals("not chosen yet")){
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity.getBaseContext(), "Please, choose image", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                APIService apiService = MyApp.getInstance().getApiService();
                Posts post = new Posts();
                post.setTitle(titleView.getText().toString());
                post.setDescription(descView.getText().toString());
                post.setLikes("0");
                post.setUserId(curId);
                post.setMainPicName(chosen);
                apiService.addPost(post).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            ((MainActivity)activity).onChoose("not chosen yet");
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity.getBaseContext(), "Post added successfully", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else{
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity.getBaseContext(), "Server connection error", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity.getBaseContext(), "Server connection error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
    }
}
