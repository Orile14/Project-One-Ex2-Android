package com.example.projectoneex2.repositoy;

import androidx.lifecycle.MutableLiveData;

import com.example.projectoneex2.AppDB;
import com.example.projectoneex2.FeedActivity;
import com.example.projectoneex2.FriendsRequest;
import com.example.projectoneex2.ImagePost;
import com.example.projectoneex2.ImagePostDao;
import com.example.projectoneex2.Request;
import com.example.projectoneex2.User;
import com.example.projectoneex2.api.PostAPI;
import com.example.projectoneex2.api.UserAPI;

import java.util.List;

public class FriendRequestRepository {
    private FriendRequestListData requestListData;
    private PostAPI postApi;
    private String token;

    public FriendRequestRepository() {
        this.requestListData = new FriendRequestListData();
    }


    public void getReq() {
        PostAPI postAPI = new PostAPI();
    }

    public void setToken(String token) {
        this.token = token;
    }


    public void declineRequest(String token, String userId, String friendId) {
        UserAPI userAPI = new UserAPI();
        userAPI.declineRequest(token,userId,friendId);
    }

    public void approveRequest(String token, String id, String userId) {
        UserAPI userAPI = new UserAPI();
        userAPI.approveRequest(token,id,userId);
    }

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

    public MutableLiveData<List<Request>> getAllReq() {
        return requestListData;
    }
}

