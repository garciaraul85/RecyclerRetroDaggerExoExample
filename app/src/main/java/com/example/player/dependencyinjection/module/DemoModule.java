package com.example.player.dependencyinjection.module;

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

}