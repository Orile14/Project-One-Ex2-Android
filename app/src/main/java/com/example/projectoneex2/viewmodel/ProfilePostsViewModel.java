package com.example.projectoneex2.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.projectoneex2.ImagePost;
import com.example.projectoneex2.Request;
import com.example.projectoneex2.repositoy.ProfilePostsRepositry;

import java.util.List;

// ViewModel class responsible for managing the data of posts
public class ProfilePostsViewModel extends ViewModel {

    private ProfilePostsRepositry mRepository; // Repository instance for handling post data
    private MutableLiveData<List<ImagePost>> posts; // LiveData object containing a list of image posts

    // Method to retrieve the LiveData object containing the list of image posts
    public MutableLiveData<List<ImagePost>> get() {
        return posts;
    }

    public ProfilePostsViewModel() {
        mRepository = new ProfilePostsRepositry();
        posts = mRepository.getAllProfile();
    }

    public MutableLiveData<List<ImagePost>> getProfilePosts(String token) {
        if (posts == null) {
            posts = new MutableLiveData<>();
        }
        return posts;
    }


    public void setToken(String token) {
        mRepository.setToken(token);
    }

    public void sendFriendRequest(String token, String currentId) {
        mRepository.sendFriendRequest(token, currentId);
    }

}