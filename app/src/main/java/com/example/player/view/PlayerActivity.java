package com.example.player.view;

import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.player.DemoApplication;
import com.example.player.R;
import com.example.player.dependencyinjection.component.DaggerApplicationComponent;
import com.example.player.dependencyinjection.component.DaggerDemoComponent;
import com.example.player.dependencyinjection.component.PlayerActivityComponent;
import com.example.player.dependencyinjection.module.ClientModule;
import com.example.player.dependencyinjection.module.ExoPlayerModule;
import com.example.player.model.MapsModuleListener;
import com.example.player.viewmodel.PostViewModel;

import java.util.List;

import javax.inject.Inject;

public class PlayerActivity extends AppCompatActivity implements MapsModuleListener {

    private PlayerActivityComponent component;

    @Inject
    MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        ((DemoApplication) getApplication()).demoComponent().inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMap();
    }

    @UiThread
    private void loadMap() {
        if (getSupportFragmentManager().findFragmentByTag(MapFragment.TAG) == null && mapFragment != null && !mapFragment.isMapAdded()) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.container, mapFragment, MapFragment.TAG);

            // Pass the Pois List into the map
            //mapFragment.setPostViewModelList(postViewModelList);
            fragmentTransaction.commit();
        }
    }


    @Override
    public void onMapsSearchOpen(List<PostViewModel> postViewModelList) {

    }

    @Override
    public void onMapsSearchClosed() {

    }

    @Override
    public void onPoiSearchTermEntered(String searchTerm) {

    }
}