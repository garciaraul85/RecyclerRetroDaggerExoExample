package com.example.player.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.example.player.viewmodel.PostViewModel;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public class SugarVenueDAO {

    @WorkerThread
    public static List<PostViewModel> getPois() {
        List<PostViewModel> postViewModels = PostViewModel.listAll(PostViewModel.class);
        return postViewModels;
    }

    @WorkerThread
    @Insert(onConflict = REPLACE)
    public static void insertLastSearch(List<PostViewModel> postViewModelList) {
        if (postViewModelList != null && !postViewModelList.isEmpty()) {
            for (PostViewModel postViewModel : postViewModelList) {
                PostViewModel.save(postViewModel);
            }
        }
    }

    @WorkerThread
    public static void clearLastSearch() {
        int res = PostViewModel.deleteAll(PostViewModel.class,"favorite=?", "0");
        Log.d("TAG", "clearLastSearch: " + res);
    }

    @WorkerThread
    public static void setFavorite(boolean favorite, int id) {
        PostViewModel postViewModel = PostViewModel.findById(PostViewModel.class, id);
        postViewModel.setFavorite(favorite);
        postViewModel.save();
    }

    @WorkerThread
    public static void setSelected(boolean selected, int id) {
        PostViewModel postViewModel = PostViewModel.findById(PostViewModel.class, id);
        postViewModel.setSelected(selected);
        postViewModel.save();
    }

}