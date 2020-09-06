package com.example.simple_social_net.fragments.main_frag;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.simple_social_net.MainActivity;
import com.example.simple_social_net.R;
import com.example.simple_social_net.models.Users;

import java.util.List;

public class SearchFragment extends Fragment {

    public SearchFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText editText = view.findViewById(R.id.edit_search_frag);
        Button btn = view.findViewById(R.id.btn_search_frag);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search = editText.getText().toString();
                if (search.isEmpty()){
                    Toast.makeText(getContext(), "Please type what you searching for", Toast.LENGTH_SHORT).show();
                    return;
                }
                ((MainActivity)getActivity()).resultFrag(search);
            }
        });
    }
}
