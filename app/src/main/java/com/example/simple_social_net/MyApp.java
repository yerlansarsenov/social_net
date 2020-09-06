package com.example.simple_social_net;

import android.app.Application;

import com.example.simple_social_net.backend.APIService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyApp extends Application {
    public static MyApp instance;
    private Retrofit retrofit;
    private static final String URL = "http://192.168.0.189/socialnet/src/routes/";
    private APIService apiService;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Gson gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        apiService = retrofit.create(APIService.class);
    }

    public static MyApp getInstance(){
        return instance;
    }
    public Retrofit getRetrofit(){
        return retrofit;
    }
    public APIService getApiService(){
        return apiService;
    }
}
