package com.example.projectoneex2.api;
import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface WebServiceAPI {
    @GET("users/{id}/posts")
    Call<ResponseBody> getUserPosts(@Header("Authorization") String token, @Path("id") String id);
    @GET("users/{id}/friends")
    Call<ResponseBody> getFriends(@Header("Authorization") String token, @Path("id") String id);
    @GET("posts/")
    Call<ResponseBody> getPosts(@Header("Authorization") String token);
    @Headers("Content-Type: application/json")
    @POST("tokens")
    Call<ResponseBody> getUser(@Body JsonObject username);
    @Headers("Content-Type: application/json")
    @POST("users/login")
    Call<ResponseBody> getLogin(@Body JsonObject username);
    @Headers("Content-Type: application/json")
    @POST("users/")
    Call<ResponseBody> createUser(@Body JsonObject user);
    @Headers("Content-Type: application/json")
    @PATCH("posts/comment/edit/{id}/{commentId}") // Corrected URL format
    Call<ResponseBody> commentEdit(
            @Header("Authorization") String token,@Body JsonObject updateContent,
            @Path("id") String postId,
            @Path("commentId") String commentId
    );
    @Headers("Content-Type: application/json")
    @POST("users/{id}/posts")
    Call<ResponseBody> createPost(@Header("Authorization") String token, @Body JsonObject post,@Path("id") String id);
    @PUT("posts/like/{id}") // Using {id} instead of :id
    Call<ResponseBody> likePost(
            @Header("Authorization") String token,
            @Path("id") String postId // Using @Path to inject the id into the URL
    );
    @PUT("posts/comment/like/{id}/{commentId}") // Corrected URL format
    Call<ResponseBody> commentLike(
            @Header("Authorization") String token,
            @Path("id") String postId,
            @Path("commentId") String commentId
    );
    @Headers("Content-Type: application/json")
    @POST("posts/comment/{id}")
    Call<ResponseBody> createComment(@Header("Authorization") String token,
                                     @Path("id") String postId,
                                     @Body JsonObject post);


    @DELETE("users/{userId}/posts/{id}")
    Call<ResponseBody> deletePost(@Header("Authorization") String token, @Path("userId") String id, @Path("id") String postId);
    @DELETE("posts/comment/{id}/{commentId}") // Corrected URL format
    Call<ResponseBody> commentDelete(
            @Header("Authorization") String token,
            @Path("id") String postId,
            @Path("commentId") String commentId
    );
    @POST("users/getID")
    Call<ResponseBody> getUserId(@Header("Authorization") String token);
    @POST("users/{id}/friends")
    Call<ResponseBody> sendFriendRequest(@Header("Authorization") String token, @Path("id") String currentId);
    @GET("users/req")
    Call<ResponseBody> getFriendRequest(@Header("Authorization") String token);
    @DELETE("users/{id}/friends/{fid}")
    Call<ResponseBody> declineRequest(@Header("Authorization") String token, @Path("id") String userId, @Path("fid") String friendId);
    @PATCH("users/{id}/friends/{fid}")
    Call<ResponseBody> approveRequest(@Header("Authorization") String token, @Path("id") String userId, @Path("fid") String friendId);
    @Headers("Content-Type: application/json")
    @PUT("users/{id}")
    Call<ResponseBody> editUser(@Header("Authorization") String token, @Path("id") String userId, @Body JsonObject user);
    @DELETE("users/{id}")
    Call<ResponseBody> deleteAccount(@Header("Authorization") String token, @Path("id") String userId);
    @Headers("Content-Type: application/json")
    @PATCH("users/{id}/posts/{postId}")
    Call<ResponseBody> editPost(@Header("Authorization") String token, @Body JsonObject post, @Path("id") String userId, @Path("postId") String postID);
}
