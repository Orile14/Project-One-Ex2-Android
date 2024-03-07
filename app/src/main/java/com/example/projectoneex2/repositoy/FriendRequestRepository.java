package com.example.projectoneex2.repositoy;

import androidx.lifecycle.MutableLiveData;

import com.example.projectoneex2.Request;
import com.example.projectoneex2.api.PostAPI;
import com.example.projectoneex2.api.UserAPI;

import java.util.List;
//repository for the friend request
public class FriendRequestRepository {
    private FriendRequestListData requestListData;
    private String token;
    //constructor
    public FriendRequestRepository() {
        this.requestListData = new FriendRequestListData();
    }
    //method to get the request
    public void getReq() {
        PostAPI postAPI = new PostAPI();
    }
    //method to set the token
    public void setToken(String token) {
        this.token = token;
    }
    //method to decline the request
    public void declineRequest(String token, String userId, String friendId) {
        UserAPI userAPI = new UserAPI();
        userAPI.declineRequest(token,userId,friendId);
    }
    //method to approve the request
    public void approveRequest(String token, String id, String userId) {
        UserAPI userAPI = new UserAPI();
        userAPI.approveRequest(token,id,userId);
    }
    //method to get the request list
    class FriendRequestListData extends MutableLiveData<List<Request>> {
        public FriendRequestListData() {
            super();
        }

        @Override
        protected void onActive() {
            super.onActive();
            PostAPI postAPI = new PostAPI();
            postAPI.getReq( this, token);
        }
    }
    //method to get the request list
    public MutableLiveData<List<Request>> getAllReq() {
        return requestListData;
    }
}

