package com.example.simple_social_net.fragments.main_frag;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simple_social_net.MyApp;
import com.example.simple_social_net.R;
import com.example.simple_social_net.adapters.ChatAdapter;
import com.example.simple_social_net.backend.APIService;
import com.example.simple_social_net.models.Messages;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChatDetailFragment extends Fragment {

    List<Messages> messages;
    String fromId;
    String toId;
    Activity activity = getActivity();
    ChatAdapter chatAdapter;
    RecyclerView recyclerView;
    public ChatDetailFragment(String from_id, String to_id) {
        fromId = from_id;
        toId = to_id;
    }

    public void setMessages(List<Messages> list){
        this.messages.clear();
        this.messages.addAll(list);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText input = view.findViewById(R.id.input_chat_detail_frag);
        recyclerView = view.findViewById(R.id.rec_chat_detail_frag);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chatAdapter = new ChatAdapter(messages = new ArrayList<>(), getActivity(), fromId);
        recyclerView.setAdapter(chatAdapter);
        APIService apiService = MyApp.getInstance().getApiService();
        apiService.getMessagesByFromTo(fromId, toId).enqueue(new Callback<List<Messages>>() {
            @Override
            public void onResponse(Call<List<Messages>> call, Response<List<Messages>> response) {
                if (response.isSuccessful()){
                    List<Messages> test = new ArrayList<>();
                    for(Messages message : response.body()){
                        if (!messages.contains(message)){
                            test.add(message);
                        }
                    }
                    apiService.getMessagesByFromTo(toId, fromId).enqueue(new Callback<List<Messages>>() {
                        @Override
                        public void onResponse(Call<List<Messages>> call, Response<List<Messages>> response) {
                            if (response.isSuccessful()){
                                for(Messages message : response.body()){
                                    if (!messages.contains(message)){
                                        test.add(message);
                                    }
                                }
                                setMessages(test);
                                messages.sort(new Comparator<Messages>() {
                                    @Override
                                    public int compare(Messages messages, Messages t1) {
                                        return Integer.parseInt(messages.getId()) - Integer.parseInt(t1.getId());
                                    }
                                });
                                chatAdapter.setMessages(messages);
//                                if (!isVis()){
//                                    recyclerView.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
//                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Messages>> call, Throwable t) {
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
            public void onFailure(Call<List<Messages>> call, Throwable t) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity.getBaseContext(), "Server Connection Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEND){
                    if (input.getText().toString().isEmpty()){
                        Toast.makeText(getContext(), "Empty message", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    Messages message = new Messages();
                    message.setFromId(fromId);
                    message.setToId(toId);
                    message.setText(input.getText().toString());
                    input.setText("");
                    APIService apiService = MyApp.getInstance().getApiService();
                    apiService.addToMessages(message).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()){
                                apiService.getMessagesByFromTo(fromId, toId).enqueue(new Callback<List<Messages>>() {
                                    @Override
                                    public void onResponse(Call<List<Messages>> call, Response<List<Messages>> response) {
                                        if (response.isSuccessful()){
                                            List<Messages> test = new ArrayList<>();
                                            for(Messages message : response.body()){
                                                if (!messages.contains(message)){
                                                    test.add(message);
                                                }
                                            }
                                            setMessages(test);
                                            apiService.getMessagesByFromTo(toId, fromId).enqueue(new Callback<List<Messages>>() {
                                                @Override
                                                public void onResponse(Call<List<Messages>> call, Response<List<Messages>> response) {
                                                    if (response.isSuccessful()){
                                                        for(Messages message : response.body()){
                                                            if (!messages.contains(message)){
                                                                test.add(message);
                                                            }
                                                        }
                                                        setMessages(test);
                                                        messages.sort(new Comparator<Messages>() {
                                                            @Override
                                                            public int compare(Messages messages, Messages t1) {
                                                                return Integer.parseInt(messages.getId()) - Integer.parseInt(t1.getId());
                                                            }
                                                        });
                                                        for(Messages m : messages){
                                                            Log.e(m.getId(), m.getText());
                                                        }
                                                        chatAdapter.setMessages(messages);
                                                        if (!messages.isEmpty() && !isVis()){
                                                            recyclerView.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<List<Messages>> call, Throwable t) {
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
                                    public void onFailure(Call<List<Messages>> call, Throwable t) {
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
                return true;
            }
        });
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                doIt();
            }
        }, 0, 5000);
    }

    public void doIt(){
        APIService apiService = MyApp.getInstance().getApiService();
        apiService.getMessagesByFromTo(fromId, toId).enqueue(new Callback<List<Messages>>() {
            @Override
            public void onResponse(Call<List<Messages>> call, Response<List<Messages>> response) {
                if (response.isSuccessful()){
                    List<Messages> test = new ArrayList<>();
                    for(Messages message : response.body()){
                        if (!messages.contains(message)){
                            test.add(message);
                        }
                    }
                    setMessages(test);
                    apiService.getMessagesByFromTo(toId, fromId).enqueue(new Callback<List<Messages>>() {
                        @Override
                        public void onResponse(Call<List<Messages>> call, Response<List<Messages>> response) {
                            if (response.isSuccessful()){
                                for(Messages message : response.body()){
                                    if (!messages.contains(message)){
                                        test.add(message);
                                    }
                                }
                                setMessages(test);
                                messages.sort(new Comparator<Messages>() {
                                    @Override
                                    public int compare(Messages messages, Messages t1) {
                                        return Integer.parseInt(messages.getId()) - Integer.parseInt(t1.getId());
                                    }
                                });
                                chatAdapter.setMessages(messages);
                                if (!messages.isEmpty() && !isVis()){
                                    recyclerView.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Messages>> call, Throwable t) {
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
            public void onFailure(Call<List<Messages>> call, Throwable t) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity.getBaseContext(), "Server Connection Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public boolean isVis(){
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int position = linearLayoutManager.findLastCompletelyVisibleItemPosition();
        int itemCnt = recyclerView.getAdapter().getItemCount();
        return (position >= itemCnt);
    }

}
