package com.example.simple_social_net.fragments.login_frag;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterFragment extends Fragment {

    List<Users> users;
    Retrofit retrofit;
    APIService apiService;
    public RegisterFragment() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        users = new ArrayList<>();
        retrofit = MyApp.getInstance().getRetrofit();
        apiService = MyApp.getInstance().getApiService();
        Activity activity = getActivity();
        apiService.getAllUsers().enqueue(new Callback<List<Users>>() {
            @Override
            public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {
                users = response.body();

            }

            @Override
            public void onFailure(Call<List<Users>> call, Throwable t) {
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
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        EditText nameEdit = view.findViewById(R.id.name_reg_frag);
        EditText lastEdit = view.findViewById(R.id.last_reg_frag);
        EditText loginEdit = view.findViewById(R.id.login_reg_frag);
        EditText passEdit = view.findViewById(R.id.pass_reg_frag);
        Button btnReg = view.findViewById(R.id.btn1_reg_frag);
        Button btnBack = view.findViewById(R.id.btn2_reg_frag);
        Activity activity = getActivity();
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEdit.getText().toString();
                String last = lastEdit.getText().toString();
                String login = loginEdit.getText().toString();
                String pass = passEdit.getText().toString();
                if (name.isEmpty() || last.isEmpty() || login.isEmpty() || pass.isEmpty()){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity().getBaseContext(), "Fill all gaps", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                for (Users checkU : users){
                    if (login.equals(checkU.getLogin())){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity().getBaseContext(), "Login is already registered", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }
                }
                if (pass.length() < 4){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity().getBaseContext(), "Password is not strong enough", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                Users crnt = new Users();
                crnt.setFirstName(name);
                crnt.setLastName(last);
                crnt.setLogin(login);
                crnt.setPassword(pass);
                crnt.setAvaName("first");
                //Call<ResponseBody> call = apiService.addUser(crnt);
                apiService.addUser(crnt).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            Log.e("user ", " is added");
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity.getBaseContext(), "User added", Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else{
                            Log.e("fuck","fuckkk");
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity.getBaseContext(), "Server Connection Error", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity.getBaseContext(), "Server Connection Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                ((LoginActivity)getActivity()).loginFragment();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LoginActivity)getActivity()).loginFragment();
            }
        });
        return view;
    }
}
