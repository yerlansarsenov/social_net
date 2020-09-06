package com.example.simple_social_net.fragments.main_frag;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simple_social_net.MainActivity;
import com.example.simple_social_net.MyApp;
import com.example.simple_social_net.R;
import com.example.simple_social_net.backend.APIService;
import com.example.simple_social_net.fragments.ImagesFragment;
import com.example.simple_social_net.models.Users;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditFragment extends Fragment {

    String curId;
    public Activity activity;
    public EditFragment(String idString) {
        curId = idString;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = ((MainActivity)getActivity());
        EditText nameView = view.findViewById(R.id.name_edit_frag);
        EditText lastView = view.findViewById(R.id.last_edit_frag);
        EditText loginView = view.findViewById(R.id.login_edit_frag);
        EditText oldPassView = view.findViewById(R.id.old_pass_edit_frag);
        EditText newPassView = view.findViewById(R.id.new_pass_edit_frag);
        TextView textView = view.findViewById(R.id.chosen_edit_frag);
        Button btn = view.findViewById(R.id.choose_edit_frag);
        Button btnEdit = view.findViewById(R.id.doit_edit_frag);
        APIService apiService = MyApp.getInstance().getApiService();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                ImagesFragment imagesFragment = new ImagesFragment();
                imagesFragment.show(fragmentManager, "Choose image");
                textView.setText(((MainActivity)activity).getChosenImage());
            }
        });
        apiService.getUserById(curId).enqueue(new Callback<List<Users>>() {
            @Override
            public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {
                if (response.isSuccessful()){
                    Users user = response.body().get(0);
                    nameView.setText(user.getFirstName());
                    lastView.setText(user.getLastName());
                    loginView.setText(user.getLogin());
                    btnEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String name = nameView.getText().toString();
                            String last = lastView.getText().toString();
                            String login = loginView.getText().toString();
                            String pass = newPassView.getText().toString();
                            String image = ((MainActivity)activity).getChosenImage();
                            if (name.isEmpty() || last.isEmpty() || login.isEmpty() || pass.isEmpty() || image.isEmpty()){
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(activity.getBaseContext(), "Please, feel all the gaps", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return;
                            }
                            if (image.equals("not chosen yet")){
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(activity.getBaseContext(), "Please, choose the image", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return;
                            }
                            if (!oldPassView.getText().toString().isEmpty() && !newPassView.getText().toString().isEmpty()){
                                if (oldPassView.getText().toString().equals(user.getPassword())){
                                    if (newPassView.getText().toString().length() > 3){
                                        apiService.getAllUsers().enqueue(new Callback<List<Users>>() {
                                            @Override
                                            public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {
                                                boolean canUpdate = true;
                                                if (response.isSuccessful()){
                                                    user.setFirstName(name);
                                                    user.setLastName(last);
                                                    user.setLogin(login);
                                                    String image = ((MainActivity)activity).getChosenImage();
                                                    user.setAvaName(image);
                                                    for(Users u : response.body()){
                                                        if (!user.getId().equals(u.getId()) && user.getLogin().equals(u.getLogin())){
                                                            activity.runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    Toast.makeText(activity.getBaseContext(), "This login is already exist", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                            canUpdate = false;
                                                            Log.e("users not eq", u.getLogin());
                                                        }
                                                        Log.e("users", u.getLogin());
                                                    }
                                                    if (canUpdate){
                                                        user.setPassword(pass);
                                                        apiService.updateUserById(user.getId(), user).enqueue(new Callback<ResponseBody>() {
                                                            @Override
                                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                                if (response.isSuccessful())
                                                                {
                                                                    ((MainActivity)activity).onChoose("not chosen yet");
                                                                    activity.runOnUiThread(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            Toast.makeText(activity.getBaseContext(), "User updated successfully", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });
                                                                    ((MainActivity)activity).profileFrag(user.getId());

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

                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<List<Users>> call, Throwable t) {
                                                activity.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(activity.getBaseContext(), "Server connection error", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        });
                                    } else{
                                        activity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(activity.getBaseContext(), "New password is not strong enough", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        return;
                                    }
                                } else{
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(activity.getBaseContext(), "Incorrect password verification", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    return;
                                }
                            } else{
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(activity.getBaseContext(), "Please, feel all the gaps", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
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
}
