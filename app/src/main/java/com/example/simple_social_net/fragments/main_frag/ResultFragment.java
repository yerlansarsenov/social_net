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
import com.example.simple_social_net.adapters.ResultAdapter;
import com.example.simple_social_net.backend.APIService;
import com.example.simple_social_net.models.Users;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ResultFragment extends Fragment {

    String search;
    List<Users> users;
    public ResultFragment(String search) {
        this.search = search;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    public void setUsers(List<Users> list){
        this.users.clear();
        this.users.addAll(list);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Activity activity = getActivity();
        RecyclerView recyclerView = view.findViewById(R.id.resview_result_frag);
        recyclerView.setLayoutManager(new LinearLayoutManager((MainActivity)getActivity()));
        ResultAdapter resultAdapter = new ResultAdapter(users = new ArrayList<>(), getActivity());
        recyclerView.setAdapter(resultAdapter);
        APIService apiService = MyApp.getInstance().getApiService();
        apiService.getAllUsers().enqueue(new Callback<List<Users>>() {
            @Override
            public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {
                if (response.isSuccessful()){
                    List<Users> list = new ArrayList<>();
                    for (Users user : response.body()){
                        if (user.getLastName().contains(search) || user.getLogin().contains(search) || user.getFirstName().contains(search)){
                            list.add(user);
                        }
                    }
                    resultAdapter.setUsers(list);
                    setUsers(list);
                    Log.e("so im here", list.size() + "");
                } else{
                    Log.e("FragresponsenotSucc", response.message());
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity.getBaseContext(), "Server Connection Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Users>> call, Throwable t) {
                Log.e("FragFail", t.getMessage());
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
