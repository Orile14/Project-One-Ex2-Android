package com.example.projectoneex2.repositoy;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.projectoneex2.AppDB;
import com.example.projectoneex2.Comment;
import com.example.projectoneex2.FeedActivity;
import com.example.projectoneex2.ImagePost;
import com.example.projectoneex2.ImagePostDao;
import com.example.projectoneex2.User;
import com.example.projectoneex2.api.PostAPI;

import java.util.List;

public class PostsRepository {
    private PostListData postListData;

    private PostAPI postAPI;
    private String token;
    AppDB db;
    ImagePostDao dao;
    public PostsRepository() {
        this.postListData = new PostListData();

        db = FeedActivity.db;
        dao = db.imagePostDao();
        this.postAPI = new PostAPI();
    }


    public void like(ImagePost post, String token) {
        PostAPI postAPI = new PostAPI();
        postAPI.like(post,token);
        postAPI.get(postListData,token);
    }

    public void commentLike(String postID, String commentID, String token) {

        postAPI.CommentLike(postID,commentID,token);
        postAPI.get(postListData,token);

    }

    public void addComment(String id, Comment updatedContent, String token) {

        postAPI.createComment(id,token,updatedContent,postListData);
        postAPI.get(postListData,token);
    }

    public void delete(ImagePost imagePost, String token) {

        postAPI.delete(imagePost,token);
        postAPI.get(postListData,token);
    }

    public void deleteComment(String postID, String commentID, String token) {

        postAPI.deleteComment(postID,commentID,token);
        postAPI.get(postListData,token);
    }
    public void setToken(String token) {
       this.token= token;
    }

    public void editComment(String id, String id1, String updatedContent, String token) {

        postAPI.editComment(id,id1,updatedContent,token);
        postAPI.get(postListData,token);
    }

    public void getUserId(String token) {
        PostAPI postAPI = new PostAPI();
        postAPI.getUserId(token);
    }

    public void reload() {
        postAPI.get(postListData,token);
    }

    public void editPost(ImagePost post, String token) {
        postAPI.editPost(post,token);
        postAPI.get(postListData,token);
    }


    class PostListData extends MutableLiveData<List<ImagePost>> {
        public PostListData() {
            super();
        }
        @Override
        protected void onActive() {
            super.onActive();
            postAPI.get(this,token);
        }
    }

    public MutableLiveData<List<ImagePost>> getAll() {
        if (token!=null) {
            postAPI.get(postListData, token);
        }
        return postListData;
    }
    public void add(ImagePost post,String token) {
        postAPI.create(post,token);
        postAPI.get(postListData,token);
    }
    public void realod() {
        PostAPI postAPI = new PostAPI();
        postAPI.get(postListData,token);
    }


}
