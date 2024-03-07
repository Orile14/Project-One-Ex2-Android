package com.example.projectoneex2.repositoy;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.projectoneex2.User;
import com.example.projectoneex2.api.UserAPI;
//repository for the users
public class UsersRepository {
    private String username;
    private String password;
    private UserToken userToken;
    private LoginValue loginValue;
    private UserAPI userApi;
    //constructor
    public UsersRepository() {
        this.userToken = new UserToken();
        this.loginValue = new LoginValue();
        userApi = new UserAPI();
    }
    //method to get the user
    public void editUser(User a, String token, String userId) {
        userApi.editUser(a,token,userId);
    }
    //method to delete the account
    public void deleteAccount(String token) {
        userApi.deleteAccount(token);
    }
    //method to get the user
    class UserToken extends MutableLiveData<String> {
        public UserToken() {
            super();
            setValue(new String());
        }
        @Override
        protected void onActive() {
            super.onActive();
            UserAPI userAPI = new UserAPI();
            userAPI.get(this,username,password);
        }
    }
    //method to get the user
    class LoginValue extends MutableLiveData<String> {
        public LoginValue() {
            super();
            setValue(null);
        }
        @Override
        protected void onActive() {
            super.onActive();
            UserAPI userAPI = new UserAPI();
            userAPI.get(this,username,password);
        }
    }
    //method to get the token
    public LiveData<String> getToken(String username, String password) {
        this.username =username;
        this.password = password;
        userToken.onActive();
        return userToken;
    }
    //method to login
    public LoginValue Login(String username, String password) {
        this.username =username;
        this.password = password;
        userApi.getLogin(username,password);
        return loginValue;
    }
    //method to create the user
    public void create(User user) {
        userApi.create(user);
    }



}
