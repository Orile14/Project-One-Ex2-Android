package com.example.projectoneex2.viewmodel;

import androidx.lifecycle.LiveData;

import com.example.projectoneex2.imagePost;
import com.example.projectoneex2.textPost;

import java.util.List;

public class TextViewModel {
    private TextViewModel mRepository;
    private LiveData<List<imagePost>> posts;

    public LiveData<List<imagePost>> get(){
        return posts;
    }
    public void add(textPost post){
        mRepository.add(post);
    }
    public void delete(textPost post){
        mRepository.delete(post);
    }
    public void reaload(){
        mRepository.reaload();
    }
}
