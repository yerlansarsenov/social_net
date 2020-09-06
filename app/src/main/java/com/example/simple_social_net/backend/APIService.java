package com.example.simple_social_net.backend;

import com.example.simple_social_net.models.Friends;
import com.example.simple_social_net.models.Images;
import com.example.simple_social_net.models.Messages;
import com.example.simple_social_net.models.Posts;
import com.example.simple_social_net.models.Users;

import java.util.List;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIService {
    @GET("users.php/api/users")
    Call<List<Users>> getAllUsers();

    @POST("users.php/api/user/add")
    Call<ResponseBody> addUser(@Body Users user);

    @GET("users.php/api/users/{id}")
    Call<List<Users>> getUserById(@Path("id") String id);

    @GET("images.php/api/images")
    Call<List<Images>> getAllImages();

    @GET("friends.php/api/friends/{user_id}")
    Call<List<Friends>> getFriendsByUserId(@Path("user_id") String user_id);

    @POST("friends.php/api/friends/add")
    Call<ResponseBody> addToFriends(@Body Friends friends);

    @GET("posts.php/api/posts/{user_id}")
    Call<List<Posts>> getPostsByUserId(@Path("user_id") String user_id);

    @GET("posts.php/api/posts")
    Call<List<Posts>> getAllPosts();

    @POST("posts.php/api/posts/add")
    Call<ResponseBody> addPost(@Body Posts post);

    @GET("messages.php/api/messages/{from_id}/{to_id}")
    Call<List<Messages>> getMessagesByFromTo(@Path("from_id") String from_id, @Path("to_id") String to_id);

    @POST("messages.php/api/messages/add")
    Call<ResponseBody> addToMessages(@Body Messages messages);

    @PUT("users.php/api/user/update/{id}")
    Call<ResponseBody> updateUserById(@Path("id") String id, @Body Users user);

}
