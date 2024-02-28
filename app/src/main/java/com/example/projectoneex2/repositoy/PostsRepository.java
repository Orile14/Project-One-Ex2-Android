package com.example.projectoneex2.repositoy;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.projectoneex2.Comment;
import com.example.projectoneex2.ImagePost;
import com.example.projectoneex2.api.PostAPI;

import java.util.LinkedList;
import java.util.List;

public class PostsRepository {
    private PostListData postListData;
    private PostAPI postApi;
    public PostsRepository() {
//        LocalDatabase db=LocalDatabase.getInstance();
//        dao=db.imagePostDao();
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

    class PostListData extends MutableLiveData<List<ImagePost>> {
        public PostListData() {
            super();
            setValue(new LinkedList<>());
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
