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
    public static PostViewModel getPoiByUid(String uuId) {
        List<PostViewModel> postViewModels = PostViewModel.find(PostViewModel.class, "uid=?", uuId);
        if (postViewModels != null && !postViewModels.isEmpty()) {
            return postViewModels.get(0);
        } else {
            return null;
        }
    }

    @WorkerThread
    @Insert(onConflict = REPLACE)
    public static void insertLastSearch(List<PostViewModel> postViewModelList) {
        if (postViewModelList != null && !postViewModelList.isEmpty()) {
            for (PostViewModel postViewModel : postViewModelList) {
                PostViewModel existingPoi = getPoiByUid(postViewModel.getUid());
                if (existingPoi == null) { // Poi doesnt exist, save it
                    Log.d("TAG", "_ppp insertLastSearch: " + postViewModel.getNameOfPlace());
                    PostViewModel.save(postViewModel);
                } else { // else update it
                    Log.d("TAG", "_ppp Update data: " + postViewModel.getNameOfPlace());
                    existingPoi.setHomePage(postViewModel.getHomePage());
                    existingPoi.setCategoryOfPlace(postViewModel.getCategoryOfPlace());
                    existingPoi.setIconExtension(postViewModel.getIconExtension());
                    existingPoi.setIconUrl(postViewModel.getIconUrl());
                    existingPoi.setLatLn(postViewModel.getLatLn());
                    existingPoi.setNameOfPlace(postViewModel.getNameOfPlace());
                    existingPoi.save();
                }
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