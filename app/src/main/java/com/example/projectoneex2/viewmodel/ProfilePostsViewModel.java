package com.example.projectoneex2.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.projectoneex2.ImagePost;
import com.example.projectoneex2.repositoy.ProfilePostsRepositry;

import java.util.List;

// ViewModel class responsible for managing the data of posts
public class ProfilePostsViewModel extends ViewModel {

    private ProfilePostsRepositry mRepository;
    private MutableLiveData<List<ImagePost>> posts;

    // Method to retrieve the LiveData object containing the list of image posts
    public MutableLiveData<List<ImagePost>> get() {
        return posts;
    }
    // Method to set the token
    public ProfilePostsViewModel() {
        mRepository = new ProfilePostsRepositry();
        posts = mRepository.getAllProfile();
    }
    // Method to set the token
    public MutableLiveData<List<ImagePost>> getProfilePosts(String token) {
        if (posts == null) {
            posts = new MutableLiveData<>();
        }
        return posts;
    }
    // Method to add a new image post to the repository
    public void setToken(String token) {
        mRepository.setToken(token);
    }
    // Method to add a new image post to the repository
    public void sendFriendRequest(String token, String currentId) {
        mRepository.sendFriendRequest(token, currentId);
    }

}