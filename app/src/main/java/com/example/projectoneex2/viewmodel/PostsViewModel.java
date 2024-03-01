package com.example.projectoneex2.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.projectoneex2.Comment;
import com.example.projectoneex2.ImagePost;
import com.example.projectoneex2.User;
import com.example.projectoneex2.repositoy.PostsRepository;

import java.util.List;

// ViewModel class responsible for managing the data of posts
public class PostsViewModel extends ViewModel {

    private PostsRepository mRepository; // Repository instance for handling post data
    private MutableLiveData<List<ImagePost>> posts; // LiveData object containing a list of image posts

    // Method to retrieve the LiveData object containing the list of image posts
    public MutableLiveData<List<ImagePost>> get() {
        return posts;
    }

    public PostsViewModel() {
        mRepository = new PostsRepository();
        posts = mRepository.getAll();
    }
    public void setToken(String token) {
        mRepository.setToken(token);
    }

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
    public void like(ImagePost post,String token) {
        mRepository.like(post,token);
    }
    public void commentLike(String postID,String commentID,String token) {
        mRepository.commentLike(postID,commentID,token);
    }

    public void addComment(String id, Comment updatedContent, String token) {
        mRepository.addComment(id,updatedContent,token);
    }

    public void delete(ImagePost imagePost, String token) {
       mRepository.delete(imagePost,token);
    }

    public void deleteComment(String postID, String commentID, String token) {
        mRepository.deleteComment(postID,commentID,token);
    }

    public void editComment(String id, String id1, String updatedContent, String token) {
        mRepository.editComment(id,id1,updatedContent,token);
    }

    public void getUserId(String token) {
        mRepository.getUserId(token);
    }

    public void getFriends(String token, String id) {
         mRepository.getFriends(token,id);
    }


    // Method to delete an existing image post from the repository
//    public void delete(ImagePost post) {
//        mRepository.delete(post);
//    }

    // Method to reload the data from the repository
//    public void reload() {
//        mRepository.reload();
//    }
}
