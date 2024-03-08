package com.example.projectoneex2.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.example.projectoneex2.Request;
import com.example.projectoneex2.repositoy.FriendRequestRepository;

import java.util.List;
//view model for the friend request
public class FriendRequestViewModel extends androidx.lifecycle.ViewModel {
    private FriendRequestRepository mRepository;
    private MutableLiveData<List<Request>> requests;

    // Method to retrieve the LiveData object containing the list of image posts
    public MutableLiveData<List<Request>> get() {
        return requests;
    }
    // Method to set the token
    public FriendRequestViewModel() {
        mRepository = new FriendRequestRepository();
       requests = mRepository.getAllReq();
    }
    // Method to set the token
    public MutableLiveData<List<Request>> getRequests(String token) {
        if (requests == null) {
            requests= new MutableLiveData<>();
        }
        return requests;
    }
    // Method to set the token
    public void setToken(String token) {
        mRepository.setToken(token);
    }
    // Method to decline the request
    public void declineRequest(String token,String userId,String friendId) {
        mRepository.declineRequest(token,userId,friendId);
    }
    // Method to approve the request
    public void approveRequest(String token, String id, String userId) {
        mRepository.approveRequest(token,id,userId);
    }
}
