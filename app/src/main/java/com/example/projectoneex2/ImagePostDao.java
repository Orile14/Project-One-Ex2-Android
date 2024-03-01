package com.example.projectoneex2;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ImagePostDao {
    @Query("SELECT * FROM image_posts")
    List<ImagePost> index();
    @Query("SELECT * FROM image_posts WHERE id = :id")
    ImagePost show(int id);

    @Insert
    void insertImagePost(List<ImagePost> imagePosts);
    @Delete
    void deleteImagePost(ImagePost...imagePosts);
    @Update
    void updateImagePost(ImagePost...imagePosts);
    @Query("DELETE FROM image_posts")
    void deleteAllRecords();
}