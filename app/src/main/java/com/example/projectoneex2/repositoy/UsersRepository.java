package com.example.projectoneex2.repositoy;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.projectoneex2.User;
import com.example.projectoneex2.api.UserAPI;

public class UsersRepository {
    private String username;
    private String password;
    private UserToken userToken;
    private LoginValue loginValue;
    private UserAPI userApi;
    public UsersRepository() {
//        LocalDatabase db=LocalDatabase.getInstance();
//        dao=db.imagePostDao();
        this.userToken = new UserToken();
        this.loginValue = new LoginValue();
//       this.postApi = postApi;
        userApi = new UserAPI();
    }

    public void editUser(User a, String token, String userId) {
        userApi.editUser(a,token,userId);
    }

    public void deleteAccount(String token) {
        userApi.deleteAccount(token);
    }

    class UserToken extends MutableLiveData<String> {
        public UserToken() {
            super();
            setValue(new String());
        }
        @Override
        protected void onActive() {
            super.onActive();
//            new Thread(() -> {
//                postListData.postValue(dao.get());
//            }).start();
            UserAPI userAPI = new UserAPI();
            userAPI.get(this,username,password);
        }
    }
    class LoginValue extends MutableLiveData<String> {
        public LoginValue() {
            super();
            setValue(null);
        }
        @Override
        protected void onActive() {
            super.onActive();
//            new Thread(() -> {
//                postListData.postValue(dao.get());
//            }).start();
            UserAPI userAPI = new UserAPI();
            userAPI.get(this,username,password);
        }
    }
//    class SignUpSucces extends MutableLiveData<String> {
//        public SignUpSucces() {
//            super();
//            setValue(new String());
//        }
//        @Override
//        protected void onActive() {
//            super.onActive();
////            new Thread(() -> {
////                postListData.postValue(dao.get());
////            }).start();
//            UserAPI userAPI = new UserAPI();
//            userAPI.get(this,username,password);
//        }
//    }
    public LiveData<String> getToken(String username, String password) {
        this.username =username;
        this.password = password;
        userToken.onActive();
        return userToken;
    }
    public LoginValue Login(String username, String password) {
        this.username =username;
        this.password = password;
        userApi.getLogin(username,password);
        return loginValue;
    }
    public void create(User user) {
        userApi.create(user);
    }



}
