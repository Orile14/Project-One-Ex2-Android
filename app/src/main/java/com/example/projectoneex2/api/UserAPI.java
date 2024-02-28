package com.example.projectoneex2.api;

import static java.lang.System.exit;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.projectoneex2.ImagePost;
import com.example.projectoneex2.LoginRequest;
import com.example.projectoneex2.MyApplication;
import com.example.projectoneex2.R;
import com.example.projectoneex2.User;
import com.example.projectoneex2.repositoy.UsersRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserAPI {

    Retrofit retrofit;

    WebServiceAPI webServiceAPI;


    public UserAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);

    }


    public void get(MutableLiveData<String>  userToken, String password, String username) {
        // Execute the network request in a background thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                String token = null;
                String jsonString = "{username:" +password + ", password:" + username + "}";
                Gson gson = new Gson();
                JsonObject jsonObject1 = gson.fromJson(jsonString, JsonObject.class);
                Call<ResponseBody> call = webServiceAPI.getUser(jsonObject1);
                try {
                    Response<ResponseBody> response = call.execute();
                    if (response.isSuccessful()) {
                        String responseBodyString = response.body().string();
                        userToken.postValue(responseBodyString);

                    } else {
                        userToken.postValue(null);
                    }
                } catch (IOException e) {
                    e.printStackTrace(); // Log the error
                    exit(1777); // Exit the application or handle the error as required

                }
            }
        }).start();
    }
    public void getLogin(MutableLiveData<Boolean>  login, String password, String username) {
        // Execute the network request in a background thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                String token = null;
                LoginRequest loginRequest = new LoginRequest(username, password);
                String jsonString = "{username:" + username + ", password:" + password + "}";
                Gson gson = new Gson();
                JsonObject jsonObject1 = gson.fromJson(jsonString, JsonObject.class);
                Call<ResponseBody> call = webServiceAPI.getLogin(jsonObject1);
                try {
                    Response<ResponseBody> response = call.execute();
                    if (response.isSuccessful()) {
                        String responseBodyString = response.body().string();
                        if (responseBodyString.equals("true")){
                            login.postValue(true);
                        }
                        Headers headers = response.headers();
                        for (int i = 0, size = headers.size(); i < size; i++) {
                            Log.d(headers.name(i), headers.value(i));
                        }
                        Log.e("API Error", "Response not successful: " + response.code());
                        Log.d("Request Details", "URL: " + call.request().url());
                        Log.d("Request Details", "Method: " + call.request().method());
                        Log.d("Request Details", "Headers: " + call.request().headers());
                        Log.d("Request Details", "Body: " + jsonObject1);
                    } else {
                        // Response not successful, handle error
                        // Log the error
                        Log.e("API Error", "Response not successful: " + response.code());
                        Headers headers = response.headers();
                        for (int i = 0, size = headers.size(); i < size; i++) {
                            Log.d(headers.name(i), headers.value(i));
                        }
                        Log.d("Request Details", "URL: " + call.request().url());
                        Log.d("Request Details", "Method: " + call.request().method());
                        Log.d("Request Details", "Headers: " + call.request().headers());
                        Log.d("Request Details", "Body: " + username);

                        // Exit the application or handle the error as required

                    }
                } catch (IOException e) {
                    e.printStackTrace(); // Log the error
                    exit(1777); // Exit the application or handle the error as required

                }

            }
        }).start();
    }


    public void create(User user) {
        new Thread(new Runnable() {
            @Override
            public void run() {

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
                    if (response.isSuccessful()) {
                        String responseBodyString = response.body().string();
                        Log.e("API Error", "Response not successful: " + response.code());
                        Log.d("Request Details", "URL: " + call.request().url());
                        Log.d("Request Details", "Method: " + call.request().method());
                        Log.d("Request Details", "Headers: " + call.request().headers());
                        Log.d("Request Details", "Body: " + jsonObject1);
                    } else {
                        Log.d("Request Details", "URL: " + call.request().url());
                        Log.d("Request Details", "Method: " + call.request().method());
                        Log.d("Request Details", "Headers: " + call.request().headers());
                        Log.d("Request Details", "Body: " + jsonObject1);
                    }
                } catch (IOException e) {
                    e.printStackTrace(); // Log the error
                    exit(1777); // Exit the application or handle the error as required

                }
            }
            }).start();
    }


    }
