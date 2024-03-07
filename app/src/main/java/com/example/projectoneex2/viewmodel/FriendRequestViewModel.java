package com.example.projectoneex2.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.example.projectoneex2.FriendsRequest;
import com.example.projectoneex2.ImagePost;
import com.example.projectoneex2.Request;
import com.example.projectoneex2.repositoy.FriendRequestRepository;
import com.example.projectoneex2.repositoy.ProfilePostsRepositry;

import java.util.List;

public class FriendRequestViewModel extends androidx.lifecycle.ViewModel {
    private FriendRequestRepository mRepository; // Repository instance for handling post data
    private MutableLiveData<List<Request>> requests; // LiveData object containing a list of image posts

    // Method to retrieve the LiveData object containing the list of image posts
    public MutableLiveData<List<Request>> get() {
        return requests;
    }

    public FriendRequestViewModel() {
        mRepository = new FriendRequestRepository();
       requests = mRepository.getAllReq();
    }

    public MutableLiveData<List<Request>> getRequests(String token) {
        if (requests == null) {
            requests= new MutableLiveData<>();
        }
        return requests;
    }


    public void setToken(String token) {
        mRepository.setToken(token);
    }

    public void declineRequest(String token,String userId,String friendId) {
        mRepository.declineRequest(token,userId,friendId);
    }

    public void approveRequest(String token, String id, String userId) {
        mRepository.approveRequest(token,id,userId);
    }
}
