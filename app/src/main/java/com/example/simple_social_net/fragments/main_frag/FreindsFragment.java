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
import android.widget.Button;
import android.widget.Toast;

import com.example.simple_social_net.MainActivity;
import com.example.simple_social_net.MyApp;
import com.example.simple_social_net.R;
import com.example.simple_social_net.adapters.FriendsAdapter;
import com.example.simple_social_net.backend.APIService;
import com.example.simple_social_net.models.Friends;
import com.example.simple_social_net.models.Users;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FreindsFragment extends Fragment {

    private String idString;
    public List<Friends> friendsThis;
    public FreindsFragment(String idString) {
        this.idString = idString;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_freinds, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recview_friend_frag);
        recyclerView.setLayoutManager(new LinearLayoutManager((MainActivity)getActivity()));
        FriendsAdapter friendsAdapter = new FriendsAdapter(friendsThis = new ArrayList<>(), ((MainActivity)getActivity()));
        Activity activity = getActivity();
        recyclerView.setAdapter(friendsAdapter);
        Button glob = view.findViewById(R.id.glob_friend_frag);
        glob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).searchFrag();
            }
        });
        APIService apiService = MyApp.getInstance().getApiService();
        apiService.getFriendsByUserId(idString).enqueue(new Callback<List<Friends>>() {
            @Override
            public void onResponse(Call<List<Friends>> call, Response<List<Friends>> response) {
                if (response.isSuccessful()){
                    if (response.body().size() > 0){
                        //setFriends(friends);
                        friendsAdapter.setList(response.body());
                    } else{
                        Log.e("ali baba", "no friends?");

                    }
                } else{
                    Log.e("respons", "notSucc");
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity.getBaseContext(), "Server Connection Error", Toast.LENGTH_SHORT).show();
                        }
                    });
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

    private void setFriends(List<Friends> friends) {
        this.friendsThis = friends;
    }


}
