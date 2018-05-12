package com.example.player.view;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.player.DemoApplication;
import com.example.player.R;
import com.example.player.model.MapsModuleListener;

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
        showList();
    }

    private void showMap() {
        if (mapFragment != null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.container, mapFragment, MapFragment.TAG);
            fragmentTransaction.commit();
        }
    }

    private void showList() {
        if (searchResultsFragment != null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.container, searchResultsFragment, SearchResultsFragment.TAG);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onMapsSearchOpen() {
        showMap();
    }

    @Override
    public void onMapsSearchClosed() {
        showList();
    }

    @Override
    public void onPoiSearchTermEntered(String searchTerm) {

    }
}