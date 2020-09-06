package com.example.simple_social_net.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.simple_social_net.MainActivity;
import com.example.simple_social_net.MyApp;
import com.example.simple_social_net.R;
import com.example.simple_social_net.adapters.ImagesAdapter;
import com.example.simple_social_net.backend.APIService;
import com.example.simple_social_net.models.Images;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImagesFragment extends DialogFragment implements ImagesAdapter.OnChoose {

    List<Images> images;
    public ImagesFragment() {

    }

    public void setImages(List<Images> images){
        this.images = images;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_images, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.rec_image_frag);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        ImagesAdapter adapter = new ImagesAdapter(images = new ArrayList<>(), getActivity(), this);
        recyclerView.setAdapter(adapter);

        APIService apiService = MyApp.getInstance().getApiService();
        apiService.getAllImages().enqueue(new Callback<List<Images>>() {
            @Override
            public void onResponse(Call<List<Images>> call, Response<List<Images>> response) {
                if (response.isSuccessful()){
                    setImages(response.body());
                    adapter.setImages(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Images>> call, Throwable t) {

            }
        });
    }


    @Override
    public void onTouch(int position) {
        String chosen = images.get(position).getName();
        ((MainActivity)getActivity()).onChoose(chosen);
        dismiss();
    }
}
