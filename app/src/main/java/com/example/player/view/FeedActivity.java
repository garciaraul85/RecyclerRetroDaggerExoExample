package com.example.player.view;

import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.player.DemoApplication;
import com.example.player.R;
import com.example.player.model.MapsModuleListener;
import com.example.player.viewmodel.FeedViewModel;

import javax.inject.Inject;


public class FeedActivity extends AppCompatActivity implements MapsModuleListener {

    @Inject
    SearchResultsFragment searchResultsFragment;
    @Inject
    MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        ((DemoApplication) getApplication()).demoComponent().inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadList();
    }

    @UiThread
    private void loadMap() {
        if (getSupportFragmentManager().findFragmentByTag(MapFragment.TAG) == null && mapFragment != null && !mapFragment.isMapAdded()) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.container, mapFragment, MapFragment.TAG);
            fragmentTransaction.commit();
        }
    }

    @UiThread
    private void loadList() {
        if (getSupportFragmentManager().findFragmentByTag(SearchResultsFragment.TAG) == null && searchResultsFragment != null && !searchResultsFragment.isSearchAdded()) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.container, searchResultsFragment, SearchResultsFragment.TAG);
            fragmentTransaction.commit();
        }
    }

    @UiThread
    @Override
    public void onMapsSearchOpen() {
        loadMap();
    }

    @UiThread
    @Override
    public void onMapsSearchClosed() {
        loadList();
    }

    @Override
    public void onPoiSearchTermEntered(String searchTerm) {

    }
}