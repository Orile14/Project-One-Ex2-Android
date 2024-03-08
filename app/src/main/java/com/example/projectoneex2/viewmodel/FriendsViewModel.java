package com.example.projectoneex2.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.example.projectoneex2.Friend;
import com.example.projectoneex2.repositoy.FriendsRepository;

import java.util.List;
//view model for the friends
public class FriendsViewModel extends androidx.lifecycle.ViewModel {
    private FriendsRepository mRepository;
    private MutableLiveData<List<Friend>> friends;

    // Method to retrieve the LiveData object containing the list of image posts
    public MutableLiveData<List<Friend>> get() {
        return friends;
    }
    // Method to set the token
    public FriendsViewModel() {
        mRepository = new FriendsRepository();
        friends = mRepository.getAllReq();
    }
    // Method to set the token
    public MutableLiveData<List<Friend>> getFriends(String token) {
        if (friends == null) {
            friends= new MutableLiveData<>();
        }
        return friends;
    }
    // Method to set the token
    public void setToken(String token) {
        mRepository.setToken(token);
    }

}
