package com.example.projectoneex2.repositoy;

import androidx.lifecycle.MutableLiveData;

import com.example.projectoneex2.AppDB;
import com.example.projectoneex2.FeedActivity;
import com.example.projectoneex2.ImagePost;
import com.example.projectoneex2.ImagePostDao;
import com.example.projectoneex2.api.PostAPI;

import java.util.List;

public class ProfilePostsRepositry {
    private ProfilePostListData postListData;

    private PostAPI postApi;
    AppDB db;
    ImagePostDao dao;
    private String token;
    public ProfilePostsRepositry() {
        this.postListData = new ProfilePostListData();

        db = FeedActivity.db;
        dao = db.imagePostDao();
//        this.postApi = postApi;
    }


    public void getProfilePosts() {
        PostAPI postAPI = new PostAPI();
    }

    public void setToken(String token) {
        this.token= token;
    }

    public void sendFriendRequest(String token, String currentId) {
        PostAPI postAPI = new PostAPI();
        postAPI.sendFriendRequest(token,currentId);
    }

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

    public MutableLiveData<List<ImagePost>> getAllProfile() {

        return postListData;
    }
    public void add(ImagePost post,String token) {
        PostAPI postAPI = new PostAPI();
        postAPI.create(post,token);
        postAPI.get(postListData,token);
    }



}
