package com.example.simple_social_net.fragments.main_frag;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.simple_social_net.MainActivity;
import com.example.simple_social_net.MyApp;
import com.example.simple_social_net.R;
import com.example.simple_social_net.adapters.ChatsAdapter;
import com.example.simple_social_net.backend.APIService;
import com.example.simple_social_net.models.Friends;
import com.example.simple_social_net.models.Users;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatsFragment extends Fragment {

    List<Friends> friends;

    String curId;
    public ChatsFragment(String idString) {
        curId = idString;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.rec_chat_frag);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ChatsAdapter adapter = new ChatsAdapter(friends = new ArrayList<>(), ((MainActivity)getActivity()));
        Activity activity = getActivity();
        recyclerView.setAdapter(adapter);
        APIService apiService = MyApp.getInstance().getApiService();
        apiService.getFriendsByUserId(curId).enqueue(new Callback<List<Friends>>() {
            @Override
            public void onResponse(Call<List<Friends>> call, Response<List<Friends>> response) {
                if (response.isSuccessful()){
                    if (response.body().size() > 0){
                        //setFriends(friends);
                        adapter.setList(response.body());
                    } else{
                        Log.e("ali baba", "no friends?");

                    }
                } else{
                    Log.e("respons", "notSucc");
                }
            }

            @Override
            public void onFailure(Call<List<Friends>> call, Throwable t) {
                Log.e("friendNotFound", t.getMessage());
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
