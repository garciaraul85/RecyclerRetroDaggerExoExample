package com.example.player.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.example.player.model.Venue;
import com.example.player.viewmodel.PostViewModel;
import com.google.android.gms.maps.model.LatLng;

@Database(entities = {PostViewModel.class},
        exportSchema = false, version = 1)
@TypeConverters({LatLngConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    static final String DATABASE_NAME = "te2_core_db";

    public abstract Venue venueDAO();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}