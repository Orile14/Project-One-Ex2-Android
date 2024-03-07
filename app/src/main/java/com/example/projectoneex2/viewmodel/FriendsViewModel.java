package com.example.projectoneex2.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.example.projectoneex2.Friend;
import com.example.projectoneex2.Request;
import com.example.projectoneex2.repositoy.FriendRequestRepository;
import com.example.projectoneex2.repositoy.FriendsRepository;

import java.util.List;

public class FriendsViewModel extends androidx.lifecycle.ViewModel {
    private FriendsRepository mRepository; // Repository instance for handling post data
    private MutableLiveData<List<Friend>> friends; // LiveData object containing a list of image posts

    // Method to retrieve the LiveData object containing the list of image posts
    public MutableLiveData<List<Friend>> get() {
        return friends;
    }

    public FriendsViewModel() {
        mRepository = new FriendsRepository();
        friends = mRepository.getAllReq();
    }

    public MutableLiveData<List<Friend>> getFriends(String token) {
        if (friends == null) {
            friends= new MutableLiveData<>();
        }
        return friends;
    }


    public void setToken(String token) {
        mRepository.setToken(token);
    }

}
