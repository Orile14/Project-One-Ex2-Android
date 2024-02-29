package com.example.projectoneex2.api;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.projectoneex2.Comment;
import com.example.projectoneex2.FeedActivity;
import com.example.projectoneex2.ImagePost;
import com.example.projectoneex2.MyApplication;
import com.example.projectoneex2.Profile;
import com.example.projectoneex2.R;
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

public class PostAPI {

    Retrofit retrofit;

    WebServiceAPI webServiceAPI;


    public PostAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);

    }

    public void create(ImagePost post, String token) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault());
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("postOwnerID", post.getAuthor());
        jsonObject1.addProperty("content", post.getContent());
        jsonObject1.addProperty("img", post.getUserpic());
        jsonObject1.addProperty("date", dateFormat.format(System.currentTimeMillis()));
        jsonObject1.add("comments", new JsonArray()); // Empty array for comments
        jsonObject1.add("likesID", new JsonArray()); // Empty array for likes
        // Assuming tokenString is your token with "Bearer" prefix
        String tokenWithoutPrefix = token.substring(10);
        // Assuming tokenString is your token
        String tokenWithoutSuffix = tokenWithoutPrefix.substring(0, tokenWithoutPrefix.length() - 2);
// Now you can use tokenWithoutSuffix
// Now you can use tokenWithoutPrefix in your Authorization header
        Call<ResponseBody> call = webServiceAPI.createPost("Bearer " + tokenWithoutSuffix, jsonObject1);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                response.body();
                Log.e("API Error", "Response not successful: " + response.code());
                Log.d("Request Details", "URL: " + call.request().url());
                Log.d("Request Details", "Method: " + call.request().method());
                Log.d("Request Details", "Headers: " + call.request().headers());
                Log.d("Request Details", "Body: " + jsonObject1);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Request Details", "URL: " + call.request().url());
                Log.d("Request Details", "Method: " + call.request().method());
                Log.d("Request Details", "Headers: " + call.request().headers());
                Log.d("Request Details", "Body: " + jsonObject1);
            }
        });
    }

    public void createComment(String postID, String token, Comment comment, MutableLiveData<List<ImagePost>> postListData) {
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("content", comment.getContent());
        String tokenWithoutPrefix = token.substring(10);
        String tokenWithoutSuffix = tokenWithoutPrefix.substring(0, tokenWithoutPrefix.length() - 2);
        Call<ResponseBody> call = webServiceAPI.createComment("Bearer " + tokenWithoutSuffix, postID, jsonObject1);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                response.body();
                Log.e("API Error", "Response not successful: " + response.code());
                Log.d("Request Details", "URL: " + call.request().url());
                Log.d("Request Details", "Method: " + call.request().method());
                Log.d("Request Details", "Headers: " + call.request().headers());
                Log.d("Request Details", "Body: " + jsonObject1);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Request Details", "URL: " + call.request().url());
                Log.d("Request Details", "Method: " + call.request().method());
                Log.d("Request Details", "Headers: " + call.request().headers());
                Log.d("Request Details", "Body: " + jsonObject1);
            }
        });
    }

    public void get(MutableLiveData<List<ImagePost>> postListData, String token) {
        // Execute the network request in a background thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Assuming tokenString is your token with "Bearer" prefix
                String tokenWithoutPrefix = token.substring(10);
                // Assuming tokenString is your token
                String tokenWithoutSuffix = tokenWithoutPrefix.substring(0, tokenWithoutPrefix.length() - 2);
                Call<ResponseBody> call = webServiceAPI.getPosts("Bearer " + tokenWithoutSuffix);
                try {

                    // Synchronously execute the request
                    Response<ResponseBody> response = call.execute();
                    if (response.isSuccessful()) {
                        // Response successful, process it
                        String responseBodyString;
                        responseBodyString = response.body().string();
                        JSONArray jsonArray = new JSONArray(responseBodyString);
                        List<ImagePost> posts = new ArrayList<>();
                        // Iterate through the JSON array
                        for (int i = 0; i < jsonArray.length(); i++) {
                            // Get each JSON object from the array
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            // Extract data from JSON object
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

    public void like(ImagePost post, String token) {

        // Assuming tokenString is your token with "Bearer" prefix
        String tokenWithoutPrefix = token.substring(10);
        // Assuming tokenString is your token
        String tokenWithoutSuffix = tokenWithoutPrefix.substring(0, tokenWithoutPrefix.length() - 2);

// Now you can use tokenWithoutSuffix


// Now you can use tokenWithoutPrefix in your Authorization header
        Call<ResponseBody> call = webServiceAPI.likePost("Bearer " + tokenWithoutSuffix, post.get_id());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                response.body();
                Log.e("API Error", "Response not successful: " + response.code());
                Log.d("Request Details", "URL: " + call.request().url());
                Log.d("Request Details", "Method: " + call.request().method());
                Log.d("Request Details", "Headers: " + call.request().headers());
                Log.d("Request Details", "Body: " + post.getId());

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Request Details", "URL: " + call.request().url());
                Log.d("Request Details", "Method: " + call.request().method());
                Log.d("Request Details", "Headers: " + call.request().headers());
                Log.d("Request Details", "Body: " + post.getId());
            }
        });
    }

    public void CommentLike(String postID, String commentID, String token) {
        // Assuming tokenString is your token with "Bearer" prefix
        String tokenWithoutPrefix = token.substring(10);
        // Assuming tokenString is your token
        String tokenWithoutSuffix = tokenWithoutPrefix.substring(0, tokenWithoutPrefix.length() - 2);
        Call<ResponseBody> call = webServiceAPI.commentLike("Bearer " + tokenWithoutSuffix, postID, commentID);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                response.body();
                Log.e("API Error", "Response not successful: " + response.code());
                Log.d("Request Details", "URL: " + call.request().url());
                Log.d("Request Details", "Method: " + call.request().method());
                Log.d("Request Details", "Headers: " + call.request().headers());
                Log.d("Request Details", "Body: " + postID + " " + commentID);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Request Details", "URL: " + call.request().url());
                Log.d("Request Details", "Method: " + call.request().method());
                Log.d("Request Details", "Headers: " + call.request().headers());

            }
        });

    }

    public void delete(ImagePost post, String token) {
        // Assuming tokenString is your token with "Bearer" prefix
        String tokenWithoutPrefix = token.substring(10);
        // Assuming tokenString is your token
        String tokenWithoutSuffix = tokenWithoutPrefix.substring(0, tokenWithoutPrefix.length() - 2);
        String s = post.get_id();
        Call<ResponseBody> call = webServiceAPI.deletePost("Bearer " + tokenWithoutSuffix, post.get_id());
        final Boolean[] indicator = new Boolean[1];
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("API Error", "Response not successful: " + response.code());
                Log.d("Request Details", "URL: " + call.request().url());
                Log.d("Request Details", "Method: " + call.request().method());
                Log.d("Request Details", "Headers: " + call.request().headers());
                Log.d("Request Details", "Body: " + post.getId());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Log.d("Request Details", "URL: " + call.request().url());
                Log.d("Request Details", "Method: " + call.request().method());
                Log.d("Request Details", "Headers: " + call.request().headers());
                Log.d("Request Details", "Body: " + post.getId());
            }
        });
    }

    public void deleteComment(String postID, String commentID, String token) {
        // Assuming tokenString is your token with "Bearer" prefix
        String tokenWithoutPrefix = token.substring(10);
        // Assuming tokenString is your token
        String tokenWithoutSuffix = tokenWithoutPrefix.substring(0, tokenWithoutPrefix.length() - 2);
        Call<ResponseBody> call = webServiceAPI.commentDelete("Bearer " + tokenWithoutSuffix, postID, commentID);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                response.body();
                Log.e("API Error", "Response not successful: " + response.code());
                Log.d("Request Details", "URL: " + call.request().url());
                Log.d("Request Details", "Method: " + call.request().method());
                Log.d("Request Details", "Headers: " + call.request().headers());
                Log.d("Request Details", "Body: " + postID + " " + commentID);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Request Details", "URL: " + call.request().url());
                Log.d("Request Details", "Method: " + call.request().method());
                Log.d("Request Details", "Headers: " + call.request().headers());

            }
        });
    }

    public void getUserId(String token) {
        String tokenWithoutPrefix = token.substring(10);
        // Assuming tokenString is your token
        String tokenWithoutSuffix = tokenWithoutPrefix.substring(0, tokenWithoutPrefix.length() - 2);
        Call<ResponseBody> call = webServiceAPI.getUserId("Bearer " + tokenWithoutSuffix);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String responseBodyString = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseBodyString);
                    String userId = jsonObject.getString("ownerId");
                    FeedActivity.userId = userId;
                    // Now you have the user ID, you can use it as needed
                    Log.d("User ID", "User ID: " + userId);
                } catch (IOException | JSONException e) {
                    throw new RuntimeException(e);
                }


                Log.e("API Error", "Response not successful: " + response.code());
                Log.d("Request Details", "URL: " + call.request().url());
                Log.d("Request Details", "Method: " + call.request().method());
                Log.d("Request Details", "Headers: " + call.request().headers());


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Request Details", "URL: " + call.request().url());
                Log.d("Request Details", "Method: " + call.request().method());
                Log.d("Request Details", "Headers: " + call.request().headers());

            }
        });
    }

    public void editComment(String id, String id1, String updatedContent, String token) {
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("content", updatedContent);
        // Assuming tokenString is your token with "Bearer" prefix
        String tokenWithoutPrefix = token.substring(10);
        // Assuming tokenString is your token
        String tokenWithoutSuffix = tokenWithoutPrefix.substring(0, tokenWithoutPrefix.length() - 2);
        Call<ResponseBody> call = webServiceAPI.commentEdit("Bearer " + tokenWithoutSuffix, jsonObject1, id, id1);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                response.body();
                Log.e("API Error", "Response not successful: " + response.code());
                Log.d("Request Details", "URL: " + call.request().url());
                Log.d("Request Details", "Method: " + call.request().method());
                Log.d("Request Details", "Headers: " + call.request().headers());
                Log.d("Request Details", "Body: " + id + " " + id1);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Request Details", "URL: " + call.request().url());
                Log.d("Request Details", "Method: " + call.request().method());
                Log.d("Request Details", "Headers: " + call.request().headers());

            }
        });

    }

    public void getProfilePosts(String currentId,MutableLiveData postListData, String token) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Assuming tokenString is your token with "Bearer" prefix
                String tokenWithoutPrefix = token.substring(10);
                // Assuming tokenString is your token
                String tokenWithoutSuffix = tokenWithoutPrefix.substring(0, tokenWithoutPrefix.length() - 2);
                Call<ResponseBody> call = webServiceAPI.getUserPosts("Bearer " + tokenWithoutSuffix,currentId);
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
}