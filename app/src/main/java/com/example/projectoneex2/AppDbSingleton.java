package com.example.projectoneex2;

import android.content.Context;
import androidx.room.Room;

import com.example.projectoneex2.AppDB;

public class AppDbSingleton {
    private static AppDB instance;

    public static synchronized AppDB getInstance(Context context) {
        if (instance == null) {
            // Create or access the database using Room.databaseBuilder()
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDB.class, "app-database")
                    .build();
        }
        return instance;
    }
}
