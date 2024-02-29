package com.example.projectoneex2.repositoy;

import androidx.lifecycle.MutableLiveData;

import com.example.projectoneex2.AppDB;
import com.example.projectoneex2.Comment;
import com.example.projectoneex2.FeedActivity;
import com.example.projectoneex2.ImagePost;
import com.example.projectoneex2.ImagePostDao;
import com.example.projectoneex2.api.PostAPI;

import java.util.List;

public class PostsRepository {
    private PostListData postListData;
    private PostAPI postApi;
    AppDB db;
    ImagePostDao dao;
    public PostsRepository() {
        db= FeedActivity.db;
        dao=db.imagePostDao();
        this.postListData = new PostListData();
//        this.postApi = postApi;
    }

    public void like(ImagePost post, String token) {
        PostAPI postAPI = new PostAPI();
        postAPI.like(post,token);
        postAPI.get(postListData);
    }

    public void commentLike(String postID, String commentID, String token) {
        PostAPI postAPI = new PostAPI();
        postAPI.CommentLike(postID,commentID,token);
        postAPI.get(postListData);

    }

    public void addComment(String id, Comment updatedContent, String token) {
        PostAPI postAPI = new PostAPI();
        postAPI.createComment(id,token,updatedContent,postListData);
        postAPI.get(postListData);
    }

    public void delete(ImagePost imagePost, String token) {
        PostAPI postAPI = new PostAPI();
        postAPI.delete(imagePost,token);
        postAPI.get(postListData);
    }

    public void deleteComment(String postID, String commentID, String token) {
        PostAPI postAPI = new PostAPI();
        postAPI.deleteComment(postID,commentID,token);
        postAPI.get(postListData);
    }

    class PostListData extends MutableLiveData<List<ImagePost>> {
        public PostListData() {
            super();
            setValue(dao.index());
        }
        @Override
        protected void onActive() {
            super.onActive();
//            new Thread(() -> {
//                postListData.postValue(dao.get());
//            }).start();
            PostAPI postAPI = new PostAPI();
            postAPI.get(this);
        }
    }
    public MutableLiveData<List<ImagePost>> getAll() {
        return postListData;
    }
    public void add(ImagePost post,String token) {
        PostAPI postAPI = new PostAPI();
        postAPI.create(post,token);
        postAPI.get(postListData);
    }
    public void realod() {
        PostAPI postAPI = new PostAPI();
        postApi.get(this.postListData);
    }


}
