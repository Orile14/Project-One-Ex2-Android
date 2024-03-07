package com.example.projectoneex2.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.projectoneex2.Comment;
import com.example.projectoneex2.ImagePost;
import com.example.projectoneex2.repositoy.PostsRepository;

import java.util.List;

// ViewModel class responsible for managing the data of posts
public class PostsViewModel extends ViewModel {

    private PostsRepository mRepository;
    private MutableLiveData<List<ImagePost>> posts;

    // Method to retrieve the LiveData object containing the list of image posts
    public MutableLiveData<List<ImagePost>> get() {
        return posts;
    }
    // Method to set the token
    public PostsViewModel() {
        mRepository = new PostsRepository();
        posts = mRepository.getAll();
    }
    // Method to set the token
    public void setToken(String token) {
        mRepository.setToken(token);
    }
    // Method to get the posts
    public MutableLiveData<List<ImagePost>> getPosts(String token) {
        if (posts == null) {
            posts = new MutableLiveData<>();
        }
        return posts;
    }

    // Method to add a new image post to the repository
    public void add(ImagePost post,String token) {
        mRepository.add(post,token);
    }
    // Method to like a post
    public void like(ImagePost post,String token) {
        mRepository.like(post,token);
    }
    // Method to like a comment
    public void commentLike(String postID,String commentID,String token) {
        mRepository.commentLike(postID,commentID,token);
    }
    // Method to add a comment
    public void addComment(String id, Comment updatedContent, String token) {
        mRepository.addComment(id,updatedContent,token);
    }
    // Method to delete an existing image post from the repository
    public void delete(ImagePost imagePost, String token) {
       mRepository.delete(imagePost,token);
    }
    // Method to delete an existing comment from the repository
    public void deleteComment(String postID, String commentID, String token) {
        mRepository.deleteComment(postID,commentID,token);
    }
    // Method to edit an existing comment from the repository
    public void editComment(String id, String id1, String updatedContent, String token) {
        mRepository.editComment(id,id1,updatedContent,token);
    }
    // Method to get the user id
    public void getUserId(String token) {
        mRepository.getUserId(token);
    }
    // Method to reload the data from the repository
    public void reload(String token) {
        mRepository.reload();
    }
    // Method to edit an existing image post from the repository

    public void editPost(ImagePost post, String token) {
        mRepository.editPost(post,token);
    }
}
