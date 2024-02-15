package com.example.projectoneex2.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.projectoneex2.ImagePost;

import java.util.List;

// ViewModel class responsible for managing the data of posts
public class PostsViewModel extends ViewModel {
    private PostsViewModel mRepository; // Repository instance for handling post data
    private LiveData<List<ImagePost>> posts; // LiveData object containing a list of image posts

    // Method to retrieve the LiveData object containing the list of image posts
    public LiveData<List<ImagePost>> get() {
        return posts;
    }

    // Method to add a new image post to the repository
    public void add(ImagePost post) {
        mRepository.add(post);
    }

    // Method to delete an existing image post from the repository
    public void delete(ImagePost post) {
        mRepository.delete(post);
    }

    // Method to reload the data from the repository
    public void reload() {
        mRepository.reload();
    }
}
