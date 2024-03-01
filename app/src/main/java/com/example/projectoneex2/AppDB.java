package com.example.projectoneex2;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ImagePost.class}, version =1)
public abstract class AppDB extends RoomDatabase {
    public abstract ImagePostDao imagePostDao();


}