package com.example.projectoneex2.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.projectoneex2.Login;
import com.example.projectoneex2.User;
import com.example.projectoneex2.repositoy.UsersRepository;

// ViewModel class responsible for managing the data of posts
public class UserViewModel extends ViewModel {

    private UsersRepository mRepository; // Repository instance for handling post data

    public LiveData<String> getToken(String username, String password) {
        return mRepository.getToken(username,password);
    }
    public LiveData<String> getLogin(String username, String password) {
        return mRepository.Login(username,password);
    }
    public void SignUp(User user) {
         mRepository.create(user);
    }

    public UserViewModel() {
        mRepository = new UsersRepository();
    }

    public void editUser(User a, String token, String userId) {
        mRepository.editUser(a,token,userId);
    }

    public void deleteAccount(String token) {
        mRepository.deleteAccount(token);
    }

}

