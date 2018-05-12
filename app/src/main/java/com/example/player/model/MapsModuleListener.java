package com.example.player.model;

public interface MapsModuleListener {
    void onMapsSearchOpen();

    void onMapsSearchClosed();

    void onPoiSearchTermEntered(String searchTerm);

}