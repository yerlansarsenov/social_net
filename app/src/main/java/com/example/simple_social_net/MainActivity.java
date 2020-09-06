package com.example.simple_social_net;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.simple_social_net.backend.APIService;
import com.example.simple_social_net.fragments.main_frag.ChatDetailFragment;
import com.example.simple_social_net.fragments.main_frag.ChatsFragment;
import com.example.simple_social_net.fragments.main_frag.EditFragment;
import com.example.simple_social_net.fragments.main_frag.FreindsFragment;
import com.example.simple_social_net.fragments.main_frag.HomepageFragment;
import com.example.simple_social_net.fragments.main_frag.PostAddFragment;
import com.example.simple_social_net.fragments.main_frag.PostDetailFragment;
import com.example.simple_social_net.fragments.main_frag.PostsFragment;
import com.example.simple_social_net.fragments.main_frag.ProfileFragment;
import com.example.simple_social_net.fragments.main_frag.ResultFragment;
import com.example.simple_social_net.fragments.main_frag.SearchFragment;
import com.example.simple_social_net.models.Users;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String LOGIN = "login";
    public static final String ID = "id";
    public static final String CHOSEN_IMAGE = "chosenImage";
    private String loginString;
    private String idString;
    private Users currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkData();
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        if (loginString.isEmpty() && idString.isEmpty()){
            startActivity(new Intent(MainActivity.this,  LoginActivity.class));
            finish();
            return;
        }
        homeFrag();
    }

    public void homeFrag() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_main_act, new HomepageFragment(idString))
                .commit();
//        getResources().getString(R.string.app_name);
//        getText(R.string.chosen_photo);
//        getResources().getText(R.string.app_name);
//        getString(R.string.hello_blank_fragment);
    }

    public void profileFrag(String id) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_main_act, new ProfileFragment(idString, id))
                .addToBackStack(null)
                .commit();
    }


    public void friendsFrag(String idString) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_main_act, new FreindsFragment(idString))
                .addToBackStack(null)
                .commit();
    }

    public void searchFrag() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_main_act, new SearchFragment())
                .addToBackStack(null)
                .commit();
    }

    public void resultFrag(String search) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_main_act, new ResultFragment(search))
                .addToBackStack(null)
                .commit();
    }
    private void setUser(Users users) {
        currentUser = users;
    }

    public void postFrag(String id) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_main_act, new PostsFragment(idString, id))
                .addToBackStack(null)
                .commit();
    }

    public void postDetailFrag(String id) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_main_act, new PostDetailFragment(id))
                .addToBackStack(null)
                .commit();
    }

    public void chatsFrag() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_main_act, new ChatsFragment(idString))
                .addToBackStack(null)
                .commit();
    }

    public void chatDetailFrag(String friendId) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_main_act, new ChatDetailFragment(idString, friendId))
                .addToBackStack(null)
                .commit();
    }

    public void postAddFrag() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_main_act, new PostAddFragment(idString))
                .addToBackStack(null)
                .commit();
    }

    public void editFrag() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_main_act, new EditFragment(idString))
                .addToBackStack(null)
                .commit();
    }

    private void checkData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        loginString = sharedPreferences.getString(LOGIN, "");
        idString = sharedPreferences.getString(ID, "");
    }

    public void setData(String login, String id){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOGIN, login);
        editor.putString(ID, id);
        editor.apply();
    }

    public void logOut(){
        setData("", "");
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    public void onChoose(String name){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CHOSEN_IMAGE, name);
        editor.apply();
    }

    public String getChosenImage(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String ans = sharedPreferences.getString(CHOSEN_IMAGE, "not chosen yet");
        return ans;
    }


}
