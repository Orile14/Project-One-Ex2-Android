package com.example.projectoneex2;

import androidx.room.Database;
import androidx.room.RoomDatabase;


public abstract class AppDB extends RoomDatabase {
    public abstract ImagePostDao imagePostDao();

}
