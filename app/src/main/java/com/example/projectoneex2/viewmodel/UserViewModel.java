package com.example.projectoneex2.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.projectoneex2.User;
import com.example.projectoneex2.repositoy.UsersRepository;

// ViewModel class responsible for managing the data of posts
public class UserViewModel extends ViewModel {

    private UsersRepository mRepository;
    // Method to retrieve the LiveData object containing the list of image posts
    public LiveData<String> getToken(String username, String password) {
        return mRepository.getToken(username,password);
    }
    // Method to retrieve the LiveData object containing the list of image posts
    public LiveData<String> getLogin(String username, String password) {
        return mRepository.Login(username,password);
    }
    // Method to retrieve the LiveData object containing the list of image posts
    public void SignUp(User user) {
         mRepository.create(user);
    }
    // Method to retrieve the LiveData object containing the list of image posts
    public UserViewModel() {
        mRepository = new UsersRepository();
    }
    // Method to retrieve the LiveData object containing the list of image posts
    public void editUser(User a, String token, String userId) {
        mRepository.editUser(a,token,userId);
    }
    // Method to retrieve the LiveData object containing the list of image posts
    public void deleteAccount(String token) {
        mRepository.deleteAccount(token);
    }

}

