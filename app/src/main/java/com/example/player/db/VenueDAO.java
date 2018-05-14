package com.example.player.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.support.annotation.WorkerThread;

import com.example.player.viewmodel.PostViewModel;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface VenueDAO {

    @Query("SELECT * FROM PostViewModel ORDER BY nameOfPlace DESC")
    LiveData<List<PostViewModel>> getPois();

    @WorkerThread
    @Insert(onConflict = REPLACE)
    long insertLastSearch(List<PostViewModel> postViewModelList);

    @WorkerThread
    @Query("DELETE FROM PostViewModel WHERE favorite = 0")
    void clearLastSearch();

    @WorkerThread
    @Query("UPDATE PostViewModel SET favorite = :favorite  WHERE id = :id")
    void setFavorite(boolean favorite, String id);

    @WorkerThread
    @Query("UPDATE PostViewModel SET selected = :selected  WHERE id = :id")
    void setSelected(boolean selected, String id);

}