package com.example.simple_social_net.fragments.main_frag;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simple_social_net.MainActivity;
import com.example.simple_social_net.MyApp;
import com.example.simple_social_net.R;
import com.example.simple_social_net.backend.APIService;
import com.example.simple_social_net.models.Users;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomepageFragment extends Fragment {

    Users currentUser;
    String idString;
    public HomepageFragment(String id) {
        idString = id;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);

        RelativeLayout profile = view.findViewById(R.id.rel1_home_frag);
        RelativeLayout friends = view.findViewById(R.id.rel2_home_frag);
        RelativeLayout chats = view.findViewById(R.id.rel3_home_frag);
        RelativeLayout posts = view.findViewById(R.id.rel4_home_frag);
        Button extBtn = view.findViewById(R.id.ext_home_frag);
        Activity activity = getActivity();
        Retrofit retrofit = MyApp.getInstance().getRetrofit();
        APIService apiService = MyApp.getInstance().getApiService();
        apiService.getUserById(idString).enqueue(new Callback<List<Users>>() {
            @Override
            public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {
                currentUser = response.body().get(0);
            }

            @Override
            public void onFailure(Call<List<Users>> call, Throwable t) {
                Log.e("response","failed");
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity.getBaseContext(), "Server Connection Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        extBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).logOut();
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).profileFrag(idString);
            }
        });
        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).friendsFrag(idString);
            }
        });
        posts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).postFrag(idString);
            }
        });
        chats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).chatsFrag();
            }
        });
        return view;
    }


}
