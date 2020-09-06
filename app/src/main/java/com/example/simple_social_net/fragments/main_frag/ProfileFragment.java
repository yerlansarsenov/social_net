package com.example.simple_social_net.fragments.main_frag;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simple_social_net.MainActivity;
import com.example.simple_social_net.MyApp;
import com.example.simple_social_net.R;
import com.example.simple_social_net.backend.APIService;
import com.example.simple_social_net.fragments.ImagesFragment;
import com.example.simple_social_net.models.Friends;
import com.example.simple_social_net.models.Images;
import com.example.simple_social_net.models.Users;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class ProfileFragment extends Fragment {

    Users profileUser;
    List<Friends> friends;
    private boolean isCurr;
    private String avaUrl;
    private String curIdString;
    private String idString;
    public ProfileFragment(String curId, String id) {
        this.curIdString = curId;
        this.idString = id;
        isCurr = false;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    private void checkData() {
        if (curIdString.equals(idString)){
            isCurr = true;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        checkData();
        Activity activity = getActivity();
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ImageView ava = view.findViewById(R.id.ava_prof_frag);
        TextView name = view.findViewById(R.id.name_prof_frag);
        TextView last = view.findViewById(R.id.last_prof_frag);
        //Picasso.get().load(R.drawable.place).placeholder(R.drawable.place).into(ava);
        Button addBtn = view.findViewById(R.id.addtofr_prof_frag);
        Button chatBtn = view.findViewById(R.id.chat_prof_frag);
        chatBtn.setVisibility(View.INVISIBLE);
        addBtn.setVisibility(View.INVISIBLE);
        Button friendsBtn = view.findViewById(R.id.friends_prof_frag);
        Button postBtn = view.findViewById(R.id.posts_prof_frag);
        Button editBtn = view.findViewById(R.id.edit_prof_frag);
        Retrofit retrofit = MyApp.getInstance().getRetrofit();
        APIService apiService = MyApp.getInstance().getApiService();
        if (!curIdString.equals(idString)){
            apiService.getFriendsByUserId(curIdString).enqueue(new Callback<List<Friends>>() {
                @Override
                public void onResponse(Call<List<Friends>> call, Response<List<Friends>> response) {
                    if (response.isSuccessful()){
                        boolean alreadyFriends = false;
                        for (Friends friend : response.body()){
                            if (friend.getFriendId().equals(idString)){
                                alreadyFriends = true;
                                break;
                            }
                        }
                        if (alreadyFriends){
                            addBtn.setVisibility(View.INVISIBLE);
                            chatBtn.setVisibility(View.VISIBLE);
                        } else{
                            addBtn.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Friends>> call, Throwable t) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity.getBaseContext(), "Server Connection Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).editFrag();
            }
        });
        friendsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).friendsFrag(idString);
            }
        });
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).postFrag(idString);
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Friends friends = new Friends();
                friends.setUserId(curIdString);
                friends.setFriendId(idString);
                apiService.addToFriends(friends).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            Friends friends1 = new Friends();
                            friends1.setUserId(idString);
                            friends1.setFriendId(curIdString);
                            apiService.addToFriends(friends1).enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful()){
                                        addBtn.setVisibility(View.INVISIBLE);
                                        chatBtn.setVisibility(View.VISIBLE);
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
            }
        });
        apiService.getUserById(idString).enqueue(new Callback<List<Users>>() {
            @Override
            public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {
                profileUser = response.body().get(0);
                name.setText(profileUser.getFirstName());
                last.setText(profileUser.getLastName());
                apiService.getAllImages().enqueue(new Callback<List<Images>>() {
                    @Override
                    public void onResponse(Call<List<Images>> call, Response<List<Images>> response) {
                        for(Images im : response.body()){
                            if (im.getName().equals(profileUser.getAvaName())){
                                avaUrl = im.getUrl();
                                break;
                            }
                        }
                        Log.e("url", avaUrl);
                        Picasso.get()
                                .load(avaUrl)
                                .placeholder(R.drawable.place)
                                .into(ava);
                    }

                    @Override
                    public void onFailure(Call<List<Images>> call, Throwable t) {
                        Log.e("onFail", t.getMessage());
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
        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).chatDetailFrag(idString);
            }
        });

        if (isCurr){
            addBtn.setVisibility(View.INVISIBLE);
        } else{
            editBtn.setVisibility(View.INVISIBLE);
        }
        return view;
    }


    /*private void getCurrentUser() {
        Retrofit retrofit = MyApp.getInstance().getRetrofit();
        APIService apiService = MyApp.getInstance().getApiService();
        Call<List<Users>> call = apiService.getUserById(idString);
        try {
            Response<List<Users>> response = call.execute();
            profileUser = response.body().get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

}
