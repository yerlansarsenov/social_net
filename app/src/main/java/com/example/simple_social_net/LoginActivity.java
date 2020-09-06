package com.example.simple_social_net;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.simple_social_net.fragments.login_frag.LoginFragment;
import com.example.simple_social_net.fragments.login_frag.RegisterFragment;

public class LoginActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String LOGIN = "login";
    public static final String ID = "id";
    private String loginString;
    private String idString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        checkData();
        if (!loginString.isEmpty()){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        loginFragment();
    }

    public void loginFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.login_frame, new LoginFragment()).commitAllowingStateLoss();
    }

    public void signinFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.login_frame, new RegisterFragment()).commitAllowingStateLoss();
    }

    private void checkData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        loginString = sharedPreferences.getString(LOGIN, "");
        idString = sharedPreferences.getString(ID, "");
    }

    public void loginSuccessfully(String id, String login) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOGIN, login);
        editor.putString(ID, id);
        editor.apply();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }
}
