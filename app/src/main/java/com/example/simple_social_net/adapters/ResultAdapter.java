package com.example.simple_social_net.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simple_social_net.MainActivity;
import com.example.simple_social_net.MyApp;
import com.example.simple_social_net.R;
import com.example.simple_social_net.backend.APIService;
import com.example.simple_social_net.models.Images;
import com.example.simple_social_net.models.Users;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder> {
    List<Users> users;
    Activity activity;
    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_item, parent, false);
        return new ResultViewHolder(v);
    }

    public void setUsers(List<Users> users){
        this.users = users;
        notifyDataSetChanged();
    }

    public ResultAdapter(List<Users> users, Activity activity){
        this.users = users;
        this.activity = activity;
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        Users user = users.get(position);
        holder.nameView.setText(user.getFirstName() + " " + user.getLastName());
        APIService apiService = MyApp.getInstance().getApiService();
        apiService.getAllImages().enqueue(new Callback<List<Images>>() {
            @Override
            public void onResponse(Call<List<Images>> call, Response<List<Images>> response) {
                if (response.isSuccessful()){
                    for (Images image : response.body()){
                        Log.e("user log", user.getLogin());
                        if (user.getAvaName().equals(image.getName())){
                            Picasso.get()
                                    .load(image.getUrl())
                                    .placeholder(R.drawable.place)
                                    .into(holder.avaView);
                            break;
                        }
                    }
                    Log.e("here it is", response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Images>> call, Throwable t) {
                Log.e("onFail((", t.getMessage());
                Toast.makeText(activity.getBaseContext(), "Server Connection Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ResultViewHolder extends RecyclerView.ViewHolder {
        public ImageView avaView;
        public TextView nameView;
        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            avaView = itemView.findViewById(R.id.ava_res_rec);
            nameView = itemView.findViewById(R.id.name_res_rec);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity)activity).profileFrag(users.get(getAdapterPosition()).getId());
                }
            });

        }
    }
}
