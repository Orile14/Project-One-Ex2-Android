package com.example.projectoneex2;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
//interface to access the image post database
@Dao
public interface ImagePostDao {
    @Query("SELECT * FROM image_posts LIMIT 5")
    List<ImagePost> index();
    @Query("SELECT * FROM image_posts WHERE daoID = :id LIMIT 5")
    ImagePost show(int id);
    //method to get each user different post on the database
    @Query("SELECT * FROM image_posts WHERE daoID = :id ")
    List<ImagePost> indexById(String id);
    @Insert
    void insertImagePost(List<ImagePost> imagePosts);
    @Delete
    void deleteImagePost(ImagePost...imagePosts);
    @Update
    void updateImagePost(ImagePost...imagePosts);
    @Query("DELETE FROM image_posts")
    void deleteAllRecords();
    //method to delete all the post of a user before an insertion
    @Query("DELETE FROM image_posts  WHERE daoID = :id")
    void deleteAllRecords(String id);
}