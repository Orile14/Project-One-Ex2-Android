package com.example.projectoneex2.repositoy;

import androidx.lifecycle.MutableLiveData;

import com.example.projectoneex2.AppDB;
import com.example.projectoneex2.FeedActivity;
import com.example.projectoneex2.Friend;
import com.example.projectoneex2.FriendsRequest;
import com.example.projectoneex2.ImagePost;
import com.example.projectoneex2.ImagePostDao;
import com.example.projectoneex2.Request;
import com.example.projectoneex2.User;
import com.example.projectoneex2.api.PostAPI;
import com.example.projectoneex2.api.UserAPI;

import java.util.List;

public class FriendsRepository {
    private FriendsListData friendsListData;
    private PostAPI postApi;
    private String token;

    public FriendsRepository() {
        this.friendsListData = new FriendsListData();
    }


    public void getFriends() {
        PostAPI postAPI = new PostAPI();
    }

    public void setToken(String token) {
        this.token = token;
    }


    class FriendsListData extends MutableLiveData<List<Friend>> {
        public FriendsListData() {
            super();
        }

        @Override
        protected void onActive() {
            super.onActive();
            PostAPI postAPI = new PostAPI();
            postAPI.getFriends( this, token,FeedActivity.currentId );
        }
    }

    public MutableLiveData<List<Friend>> getAllReq() {
        return friendsListData;
    }
}

