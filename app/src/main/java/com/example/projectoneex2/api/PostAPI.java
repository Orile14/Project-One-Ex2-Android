package com.example.projectoneex2.api;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.projectoneex2.Comment;
import com.example.projectoneex2.FeedActivity;
import com.example.projectoneex2.Friend;
import com.example.projectoneex2.ImagePost;
import com.example.projectoneex2.MyApplication;
import com.example.projectoneex2.R;
import com.example.projectoneex2.Request;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Class for managing API requests related to posts

public class PostAPI {

    Retrofit retrofit;

    WebServiceAPI webServiceAPI;

    // Constructor to initialize the API

    public PostAPI() {
        // Create a new Retrofit instance
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // Create a new instance of the WebServiceAPI interface
        webServiceAPI = retrofit.create(WebServiceAPI.class);

    }
    // Method to create a new post
    public void create(ImagePost post, String token) {
        // Create a new JSON object to hold the post data
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault());
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("postOwnerID", post.getAuthor());
        jsonObject1.addProperty("content", post.getContent());
        jsonObject1.addProperty("img", post.getUserpic());
        jsonObject1.addProperty("date", dateFormat.format(System.currentTimeMillis()));
        jsonObject1.add("comments", new JsonArray()); // Empty array for comments
        jsonObject1.add("likesID", new JsonArray()); // Empty array for likes
        // formatting toke
        String tokenWithoutPrefix = token.substring(10);
        String tokenWithoutSuffix = tokenWithoutPrefix.substring(0, tokenWithoutPrefix.length() - 2);
        Call<ResponseBody> call = webServiceAPI.createPost("Bearer " + tokenWithoutSuffix, jsonObject1,FeedActivity.userId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }
    // Method to create a new comment
    public void createComment(String postID, String token, Comment comment, MutableLiveData<List<ImagePost>> postListData) {
        // Create a new JSON object to hold the comment data
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("content", comment.getContent());
        // formatting token
        String tokenWithoutPrefix = token.substring(10);
        String tokenWithoutSuffix = tokenWithoutPrefix.substring(0, tokenWithoutPrefix.length() - 2);
        Call<ResponseBody> call = webServiceAPI.createComment("Bearer " + tokenWithoutSuffix, postID, jsonObject1);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }
    // Method to get the list of posts
    public void get(MutableLiveData<List<ImagePost>> postListData, String token) {
        // Formatting token
        String tokenWithoutPrefix = token.substring(10);
        String tokenWithoutSuffix = tokenWithoutPrefix.substring(0, tokenWithoutPrefix.length() - 2);
        Call<ResponseBody> call = webServiceAPI.getPosts("Bearer " + tokenWithoutSuffix);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBodyString = response.body().string();
                        JSONArray jsonArray = new JSONArray(responseBodyString);
                        List<ImagePost> posts = new ArrayList<>();
                        // Iterate through the JSON array and get the posts details
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String ID = jsonObject.getString("_id");
                            String author = jsonObject.getString("nick");
                            String content = jsonObject.getString("content");
                            String time = jsonObject.getString("date");
                            String AuthorId = jsonObject.getString("postOwnerID");
                            String img = jsonObject.getString("img");
                            String profilePic = jsonObject.getString("profilePic");
                            ImagePost new_post = new ImagePost(author, content, img, profilePic, time, ID);
                            new_post.setPostOwnerID(AuthorId);
                            JSONArray commentsArray = jsonObject.getJSONArray("comments");

                            for (int j = 0; j < commentsArray.length(); j++) {
                                JSONObject commentObject = commentsArray.getJSONObject(j);
                                String authorID = commentObject.getString("nickname");
                                String commentContent = commentObject.getString("content");
                                String commentDate = commentObject.getString("date");
                                String pic = commentObject.getString("profilePic");
                                Comment comment = new Comment(authorID, commentContent, commentDate);
                                comment.setAuthorPic(pic);
                                comment.setId(commentObject.getString("_id"));

                                if (commentObject.has("likes")) {
                                    JSONArray likeArray = commentObject.getJSONArray("likes");
                                    int a = likeArray.length();
                                    comment.setLikes(a);
                                }
                                comment.setCommentOwnerID(commentObject.getString("commentOwnerID"));
                                new_post.addComment(comment);
                            }

                            JSONArray likeArray = jsonObject.getJSONArray("likesID");
                            int j = likeArray.length();
                            new_post.setLikes(j);
                            posts.add(new_post);
                        }
                        // Set the list of posts in the MutableLiveData object
                        postListData.postValue(posts);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        // Handle error
                    }
                } else {
                    // Response not successful, handle error
                    Log.e("API Error", "Response not successful: " + response.code());
                    // Handle error
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Log the error
                Log.e("API Error", "Request failed: " + t.getMessage());
                // Handle error
            }
        });
    }
    // Method to like a post
    public void like(ImagePost post, String token) {
        //formatting token
        String tokenWithoutPrefix = token.substring(10);
        String tokenWithoutSuffix = tokenWithoutPrefix.substring(0,
                tokenWithoutPrefix.length() - 2);

        Call<ResponseBody> call = webServiceAPI.likePost("Bearer " + tokenWithoutSuffix, post.get_id());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                response.body();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }
    // Method to like a comment
    public void CommentLike(String postID, String commentID, String token) {
        // formatting token
        String tokenWithoutPrefix = token.substring(10);
        String tokenWithoutSuffix = tokenWithoutPrefix.substring(0, tokenWithoutPrefix.length() - 2);
        Call<ResponseBody> call = webServiceAPI.commentLike("Bearer " + tokenWithoutSuffix, postID, commentID);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {;
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });

    }
    // Method to delete a post
    public void delete(ImagePost post, String token) {
        //formatting token
        String tokenWithoutPrefix = token.substring(10);
        String tokenWithoutSuffix = tokenWithoutPrefix.substring(0, tokenWithoutPrefix.length() - 2);
        Call<ResponseBody> call = webServiceAPI.deletePost("Bearer " + tokenWithoutSuffix,FeedActivity.userId, post.get_id());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }
    // Method to delete a comment
    public void deleteComment(String postID, String commentID, String token) {
        //formatting token
        String tokenWithoutPrefix = token.substring(10);
        String tokenWithoutSuffix = tokenWithoutPrefix.substring(0, tokenWithoutPrefix.length() - 2);
        Call<ResponseBody> call = webServiceAPI.commentDelete("Bearer " + tokenWithoutSuffix, postID, commentID);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                response.body();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    // Method to get the user ID
    public void getUserId(String token) {
        //formatting token
        String tokenWithoutPrefix = token.substring(10);
        String tokenWithoutSuffix = tokenWithoutPrefix.substring(0, tokenWithoutPrefix.length() - 2);
        Call<ResponseBody> call = webServiceAPI.getUserId("Bearer " + tokenWithoutSuffix);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    // Get the response body as a string
                    String responseBodyString = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseBodyString);
                    FeedActivity.userId = jsonObject.getString("ownerId");
                } catch (IOException | JSONException e) {
                      e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }
    // Method to edit a comment
    public void editComment(String postId, String userId, String updatedContent, String token) {
        // Create a new JSON object to hold the updated comment content
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("content", updatedContent);
        // formatting token
        String tokenWithoutPrefix = token.substring(10);
        String tokenWithoutSuffix = tokenWithoutPrefix.substring(0, tokenWithoutPrefix.length() - 2);
        Call<ResponseBody> call = webServiceAPI.commentEdit("Bearer " + tokenWithoutSuffix, jsonObject1, postId, userId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                response.body();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });

    }
    // Method to get the user posts
    public void getProfilePosts(String currentId, MutableLiveData postListData, String token) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // formatting token
                String tokenWithoutPrefix = token.substring(10);
                String tokenWithoutSuffix = tokenWithoutPrefix.substring(0, tokenWithoutPrefix.length() - 2);
                Call<ResponseBody> call = webServiceAPI.getUserPosts("Bearer " + tokenWithoutSuffix, currentId);
                try {
                    // Synchronously execute the request
                    Response<ResponseBody> response = call.execute();
                    if (response.isSuccessful()) {
                        // Response successful, process it
                        String responseBodyString;
                        responseBodyString = response.body().string();
                        JSONObject jsonObject1 = new JSONObject(responseBodyString);
                        JSONArray jsonArray = jsonObject1.getJSONArray("posts");
                        List<ImagePost> posts = new ArrayList<>();
                        // Iterate through the JSON array
                        for (int i = 0; i < jsonArray.length(); i++) {
                            // Get each JSON object from the array
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            // Extract data from JSON object
                            String ID = jsonObject.getString("_id");
                            String content = jsonObject.getString("content");
                            String time = jsonObject.getString("date");
                            String AuthorId = jsonObject.getString("postOwnerID");
                            String img = jsonObject.getString("img");
                            String profilePic = jsonObject.getString("profilePic");
                            ImagePost new_post = new ImagePost(FeedActivity.currentNickname, content, img, profilePic, time, ID);
                            new_post.setPostOwnerID(AuthorId);
                            JSONArray commentsArray = jsonObject.getJSONArray("comments");
                            for (int j = 0; j < commentsArray.length(); j++) {
                                JSONObject commentObject = commentsArray.getJSONObject(j);
                                String authorID = commentObject.getString("_id");
                                String commentContent = commentObject.getString("content");
                                String commentDate = commentObject.getString("date");

                                Comment comment = new Comment(authorID, commentContent, commentDate);
                                if (commentObject.has("likes")) {
                                    JSONArray likeArray = commentObject.getJSONArray("likes");
                                    int a = likeArray.length();
                                    comment.setLikes(a);
                                    comment.setId(commentObject.getString("_id"));
                                    comment.setCommentOwnerID(commentObject.getString("commentOwnerID"));

                                }
                                // Add comment to post
                                new_post.addComment(comment);
                            }
                            JSONArray likeArray = jsonObject.getJSONArray("likesID");
                            int j = likeArray.length();
                            new_post.setLikes(j);
                            // Add the Post object to the list
                            posts.add(new_post);
                        }
                        // Set the list of posts in the MutableLiveData object
                        postListData.postValue(posts);

                    } else {
                        // Response not successful, handle error
                        // Log the error
                        Log.e("API Error", "Response not successful: " + response.code());
                        // Exit the application or handle the error as required
                    }
                } catch (IOException e) {
                    e.printStackTrace(); // Log the error
                    // Exit the application or handle the error as required
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
    // Method to get the user friends
    public void getFriends(MutableLiveData friendsListData,String token, String id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // formatting token
                String tokenWithoutPrefix = token.substring(10);
                String tokenWithoutSuffix = tokenWithoutPrefix.substring(0, tokenWithoutPrefix.length() - 2);
                Call<ResponseBody> call = webServiceAPI.getFriends("Bearer " + tokenWithoutSuffix, id);
                try {
                    // Synchronously execute the request
                    Response<ResponseBody> response = call.execute();
                    if (response.isSuccessful()) {
                        // Response successful, process it
                        String responseBodyString;
                        responseBodyString = response.body().string();
                        JSONObject jsonObject1 = new JSONObject(responseBodyString);
                        JSONArray jsonArray = jsonObject1.getJSONArray("friends");
                        List<Friend> friends = new ArrayList<>();
                        // Iterate through the JSON array
                        for (int i = 0; i < jsonArray.length(); i++) {
                            // Get each JSON object from the array
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            // Extract data from JSON object
                            String ID = jsonObject.getString("id");
                            String img = jsonObject.getString("img");
                            String profilePic = jsonObject.getString("nick");
                            Friend friend = new Friend(profilePic, img);
                            friends.add(friend);
                        }
                        friendsListData.postValue(friends);
                    } else {
                        // Response not successful, handle error
                        // Log the error
                        Log.e("API Error", "Response not successful: " + response.code());
                        // Exit the application or handle the error as required
                    }
                } catch (IOException e) {
                    e.printStackTrace(); // Log the error
                    // Exit the application or handle the error as required
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

    }
    // Method to send a friend request
    public void sendFriendRequest(String token, String userId) {
        String tokenWithoutPrefix = token.substring(10);
        // formatting token
        String tokenWithoutSuffix = tokenWithoutPrefix.substring(0, tokenWithoutPrefix.length() - 2);
        Call<ResponseBody> call = webServiceAPI.sendFriendRequest("Bearer " + tokenWithoutSuffix, userId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    // Method to get the friend requests
    public void getReq( MutableLiveData friendRequestListData, String token) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // formatting token
                String tokenWithoutPrefix = token.substring(10);
                String tokenWithoutSuffix = tokenWithoutPrefix.substring(0, tokenWithoutPrefix.length() - 2);
                Call<ResponseBody> call = webServiceAPI.getFriendRequest("Bearer " + tokenWithoutSuffix);
                try {
                    // Synchronously execute the request
                    Response<ResponseBody> response = call.execute();
                    if (response.isSuccessful()) {
                        // Response successful, process it
                        String responseBodyString;
                        responseBodyString = response.body().string();
                        JSONObject jsonObject1 = new JSONObject(responseBodyString);
                        JSONArray jsonArray = jsonObject1.getJSONArray("friendsRequest");
                        List<Request> requests = new ArrayList<>();
                        // Iterate through the JSON array
                        for (int i = 0; i < jsonArray.length(); i++) {
                            // Get each JSON object from the array
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            // Extract data from JSON object
                            String ID = jsonObject.getString("id");
                            String img = jsonObject.getString("img");
                            String profilePic = jsonObject.getString("nick");
                            Request request = new Request(profilePic, ID, img);
                            requests.add(request);
                        }
                        friendRequestListData.postValue(requests);
                    } else {
                        // Response not successful, handle error
                        // Log the error
                        Log.e("API Error", "Response not successful: " + response.code());
                        // Exit the application or handle the error as required
                    }
                } catch (IOException e) {
                    e.printStackTrace(); // Log the error
                    // Exit the application or handle the error as required
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

    }
    // Method to approve a friend request
    public void editPost(ImagePost post, String token) {
        //formatting token
        String tokenWithoutPrefix = token.substring(10);
        String tokenWithoutSuffix = tokenWithoutPrefix.substring(0, tokenWithoutPrefix.length() - 2);
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("content", post.getContent());
        jsonObject1.addProperty("image", post.getUserpic());
        Call<ResponseBody> call = webServiceAPI.editPost("Bearer " + tokenWithoutSuffix, jsonObject1, post.getPostOwnerID(),
                post.get_id());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}