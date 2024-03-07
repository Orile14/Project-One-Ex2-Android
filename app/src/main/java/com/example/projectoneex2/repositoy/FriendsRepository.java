package com.example.projectoneex2.repositoy;

import androidx.lifecycle.MutableLiveData;

import com.example.projectoneex2.FeedActivity;
import com.example.projectoneex2.Friend;
import com.example.projectoneex2.api.PostAPI;

import java.util.List;
//repository for the friends
public class FriendsRepository {
    private FriendsListData friendsListData;
    private String token;
    //constructor
    public FriendsRepository() {
        this.friendsListData = new FriendsListData();
    }
    //method to get the friends
    public void getFriends() {
        PostAPI postAPI = new PostAPI();
    }
    //method to set the token
    public void setToken(String token) {
        this.token = token;
    }
    //method to get the friends list
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
    //method to get the friends list
    public MutableLiveData<List<Friend>> getAllReq() {
        return friendsListData;
    }
}

