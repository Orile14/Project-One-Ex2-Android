package com.example.projectoneex2.repositoy;

import androidx.lifecycle.MutableLiveData;

import com.example.projectoneex2.AppDB;
import com.example.projectoneex2.FeedActivity;
import com.example.projectoneex2.ImagePost;
import com.example.projectoneex2.ImagePostDao;
import com.example.projectoneex2.api.PostAPI;

import java.util.List;
//repository for the profile posts
public class ProfilePostsRepositry {
    private ProfilePostListData postListData;
    private AppDB db;
    private ImagePostDao dao;
    private String token;
    //constructor
    public ProfilePostsRepositry() {
        this.postListData = new ProfilePostListData();

        db = FeedActivity.db;
        dao = db.imagePostDao();
    }
    //method to get the profile posts
    public void getProfilePosts() {
        PostAPI postAPI = new PostAPI();
    }
    //method to set the token
    public void setToken(String token) {
        this.token= token;
    }
    //method to send the friend request
    public void sendFriendRequest(String token, String currentId) {
        PostAPI postAPI = new PostAPI();
        postAPI.sendFriendRequest(token,currentId);
    }
    //method to get the profile post list
    class ProfilePostListData extends MutableLiveData<List<ImagePost>> {
        public ProfilePostListData() {
            super();
        }
        @Override
        protected void onActive() {
            super.onActive();
            PostAPI postAPI = new PostAPI();
            postAPI.getProfilePosts(FeedActivity.currentId,this, token);
        }
    }
    //method to get the profile post list
    public MutableLiveData<List<ImagePost>> getAllProfile() {

        return postListData;
    }
    //method to add the post
    public void add(ImagePost post,String token) {
        PostAPI postAPI = new PostAPI();
        postAPI.create(post,token);
        postAPI.get(postListData,token);
    }



}
