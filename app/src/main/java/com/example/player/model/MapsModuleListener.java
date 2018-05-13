package com.example.player.model;

import com.example.player.viewmodel.PostViewModel;

import java.util.List;

public interface MapsModuleListener {
    void onMapsSearchOpen(List<PostViewModel> postViewModelList);

    void onMapsSearchClosed();

    void onPoiSearchTermEntered(String searchTerm);

}