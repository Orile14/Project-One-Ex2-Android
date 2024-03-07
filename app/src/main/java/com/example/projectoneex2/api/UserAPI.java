package com.example.projectoneex2.api;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.projectoneex2.FeedActivity;
import com.example.projectoneex2.Login;
import com.example.projectoneex2.LoginRequest;
import com.example.projectoneex2.MyApplication;
import com.example.projectoneex2.R;
import com.example.projectoneex2.SignUp;
import com.example.projectoneex2.User;
import com.example.projectoneex2.editProfile;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//Class for managing API requests related to users
public class UserAPI {

    Retrofit retrofit;

    WebServiceAPI webServiceAPI;

    // Constructor to initialize the API
    public UserAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);

    }

    // Method to get a user's token
    public void get(MutableLiveData<String>  userToken, String password, String username) {
        // Execute the network request in a background thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                String token = null;
                // Create a JSON object to send as the request body
                String jsonString = "{username:" +password + ", password:" + username + "}";
                Gson gson = new Gson();
                JsonObject jsonObject1 = gson.fromJson(jsonString, JsonObject.class);
                Call<ResponseBody> call = webServiceAPI.getUser(jsonObject1);
                try {
                    Response<ResponseBody> response = call.execute();
                    if (response.isSuccessful()) {
                        // Request successful and the response has been returned
                        String responseBodyString = response.body().string();
                        userToken.postValue(responseBodyString);
                        Login.status = "success";

                    } else {
                        // Response not successful, handle error
                        userToken.postValue("failed");
                        Login.status = "failed";

                    }
                } catch (IOException e) {
                    e.printStackTrace(); // Log the error
                     // Exit the application or handle the error as required

                }
            }
        }).start();
    }
    // Method to get a user's login
    public void getLogin(String password, String username) {
        LoginRequest loginRequest = new LoginRequest(username, password);
        // Create a JSON object to send as the request body
        String jsonString = "{username:" + username + ", password:" + password + "}";
        Gson gson = new Gson();
        JsonObject jsonObject1 = gson.fromJson(jsonString, JsonObject.class);
        Call<ResponseBody> call = webServiceAPI.getLogin(jsonObject1);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        // Request successful and the response has been returned
                        String responseBodyString = response.body().string();
                        if (!responseBodyString.equals("null")) {
                            JSONObject jsonObject = new JSONObject(responseBodyString);
                            Login.nickname = jsonObject.getString("nick");
                            Login.profilePic = jsonObject.getString("profilepic");
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        // Handle the error as required
                    }
                } else {
                    // Response not successful, handle error
                    Log.e("API Error", "Response not successful: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    // Method to create a user
    public void create(User user) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Create a JSON object to send as the request body
                String jsonString = "{\"_id\": \"" + user.getId() + "\", \"username\": \"" +
                        user.getUsername() + "\", \"nick\": \"" + user.getNickname() + "\", " +
                        "\"password\": \"" + user.getPassword() + "\", \"img\": \"" + user.getProfile()
                        + "\", \"friends\": \"" + user.getFriends() + "\", \"posts\": \"" + user.getPosts()
                        + "\"}";
                Gson gson = new Gson();
                JsonObject jsonObject1 = gson.fromJson(jsonString, JsonObject.class);
                Call<ResponseBody> call = webServiceAPI.createUser(jsonObject1);
                try {
                    Response<ResponseBody> response = call.execute();
                    // Request successful and the response has been returned
                    if (response.code()==409){
                        SignUp.indicator.postValue("false");
                    }
                    if (response.isSuccessful()) {
                        // Request successful and the response has been returned
                        String responseBodyString = response.body().string();
                            SignUp.indicator.postValue("true");
                    } else {
                    }
                } catch (IOException e) {
                    e.printStackTrace(); // Log the error

                }
            }
            }).start();
    }

    // Method to get a user's posts
    public void declineRequest(String token, String userId, String friendId) {
        //format the token
        String tokenWithoutPrefix = token.substring(10);
        String tokenWithoutSuffix = tokenWithoutPrefix.substring(0, tokenWithoutPrefix.length() - 2);
        Call<ResponseBody> call = webServiceAPI.declineRequest("Bearer " + tokenWithoutSuffix, friendId, userId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    // Method to get a user's posts
    public void approveRequest(String token, String id, String userId) {
        //format the token
        String tokenWithoutPrefix = token.substring(10);
        String tokenWithoutSuffix = tokenWithoutPrefix.substring(0, tokenWithoutPrefix.length() - 2);
        Call<ResponseBody> call = webServiceAPI.approveRequest("Bearer " + tokenWithoutSuffix, userId, id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    // Method to edit a user's posts
    public void editUser(User user, String token, String userId) {
        // Create a JSON object to send as the request body
        String jsonString = "{\"_id\": \"" + user.getId() + "\", \"username\": \"" +
                user.getUsername() + "\", \"nick\": \"" + user.getNickname() + "\", " +
                "\"password\": \"" + user.getPassword() + "\", \"img\": \"" + user.getProfile()
                + "\", \"friends\": \"" + user.getFriends() + "\", \"posts\": \"" + user.getPosts()
                + "\"}";
        Gson gson = new Gson();
        JsonObject jsonObject1 = gson.fromJson(jsonString, JsonObject.class);
        String tokenWithoutPrefix = token.substring(10);
        // format the token
        String tokenWithoutSuffix = tokenWithoutPrefix.substring(0, tokenWithoutPrefix.length() - 2);
        Call<ResponseBody> call = webServiceAPI.editUser("Bearer " + tokenWithoutSuffix, userId, jsonObject1);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // Request successful and the response has been returned
                editProfile.indicatorE.postValue("true");
                if (response.code() != 200) {
                    // Response not successful, handle error
                    editProfile.indicatorE.postValue("false");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Response not successful, handle error
                editProfile.indicatorE.postValue("false");
                Log.d("Request Details", "URL: " + call.request().url());
                Log.d("Request Details", "Method: " + call.request().method());
                Log.d("Request Details", "Headers: " + call.request().headers());

            }
        });

    }
    // Method to delete a user's account
    public void deleteAccount(String token) {
        //format the token
        String tokenWithoutPrefix = token.substring(10);
        String tokenWithoutSuffix = tokenWithoutPrefix.substring(0, tokenWithoutPrefix.length() - 2);
        Call<ResponseBody> call = webServiceAPI.deleteAccount("Bearer " + tokenWithoutSuffix, FeedActivity.userId);
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
