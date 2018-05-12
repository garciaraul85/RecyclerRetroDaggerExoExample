package com.example.player.dependencyinjection.module;

import com.example.player.view.MapFragment;
import com.example.player.view.SearchResultsFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created by linke_000 on 21/08/2017.
 */
@Module
public class DemoModule {

    @Provides
    public SearchResultsFragment providesSearchResultsFragment() {
        SearchResultsFragment searchResultsFragment = new SearchResultsFragment();
        return searchResultsFragment;
    }

    @Provides
    public MapFragment providesMapFragment() {
        MapFragment mapFragment = new MapFragment();
        return mapFragment;
    }

}