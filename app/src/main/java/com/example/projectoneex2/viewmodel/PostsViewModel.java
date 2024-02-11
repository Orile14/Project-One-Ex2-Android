package com.example.projectoneex2.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.projectoneex2.imagePost;

import java.util.List;

public class PostsViewModel extends ViewModel {
    private PostsViewModel mRepository;
    private LiveData<List<imagePost>> posts;

    public LiveData<List<imagePost>> get(){
        return posts;
    }
    public void add(imagePost post){
                mRepository.add(post);
    }
    public void delete(imagePost post){
        mRepository.delete(post);
   }
    public void reaload(){
        mRepository.reaload();
    }
}
