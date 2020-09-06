package com.example.simple_social_net.fragments.login_frag;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.simple_social_net.LoginActivity;
import com.example.simple_social_net.MyApp;
import com.example.simple_social_net.R;
import com.example.simple_social_net.backend.APIService;
import com.example.simple_social_net.models.Users;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginFragment extends Fragment {


    List<Users> users;
    public LoginFragment() {

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        users = new ArrayList<>();
        Retrofit retrofit = MyApp.getInstance().getRetrofit();
        Activity activity = getActivity();
        APIService apiService = MyApp.getInstance().getApiService();
        apiService.getAllUsers().enqueue(new Callback<List<Users>>() {
            @Override
            public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {
                users = response.body();
            }

            @Override
            public void onFailure(Call<List<Users>> call, Throwable t) {
                Log.e("in Failure", users.size() + "");
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity.getBaseContext(), "Server Connection Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        EditText editLogin = view.findViewById(R.id.edit1_login_frag);
        EditText editPass = view.findViewById(R.id.edit2_login_frag);
        Button btnLog = view.findViewById(R.id.button1_login_frag);
        Button btnReg = view.findViewById(R.id.button2_login_frag);
        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String loginString = editLogin.getText().toString();
                String passString = editPass.getText().toString();
                if (users.size() != 0)
                {
                    for (Users crtUser : users) {
                        if (loginString.equals(crtUser.getLogin()) && passString.equals(crtUser.getPassword())) {
                            ((LoginActivity) getActivity()).loginSuccessfully(crtUser.getId(), crtUser.getLogin());
                            return;
                        }
                    }
                    Toast.makeText(getContext(), "Incorrect login or password", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(getContext(), "Server Connection Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LoginActivity)getActivity()).signinFragment();
            }
        });
        return view;
    }

}
