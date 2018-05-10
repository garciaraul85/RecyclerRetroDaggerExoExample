package com.example.player.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.player.R;
import com.example.player.dependencyinjection.component.DaggerPlayerActivityComponent;
import com.example.player.dependencyinjection.component.PlayerActivityComponent;
import com.example.player.dependencyinjection.module.ExoPlayerModule;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.HttpDataSource;

import javax.inject.Inject;

public class PlayerActivity extends AppCompatActivity {

    private PlayerActivityComponent component;

    @Inject
    Handler handler;

    @Inject
    DefaultBandwidthMeter bandwidthMeter;

    //@ViewById(R.id.mainSimpleExoPlayer)
    SimpleExoPlayerView simpleExoPlayerView;

    // Create player
    @Inject
    TrackSelection.Factory videoTrackSelectionFactory;
    @Inject
    TrackSelector trackSelector;
    @Inject
    LoadControl loadControl;
    @Inject
    SimpleExoPlayer player;

    // Prepare player
    @Inject
    String userAgent;
    @Inject
    HttpDataSource.Factory httpDataSourceFactory;
    @Inject
    DataSource.Factory dataSourceFactory;
    @Inject
    MediaSource videoSource;

    private static final String VIDEO_URI =
            "http://techslides.com/demos/sample-videos/small.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.mainSimpleExoPlayer);

        component =
                DaggerPlayerActivityComponent
                        .builder()
                        .exoPlayerModule(new ExoPlayerModule(PlayerActivity.this, VIDEO_URI))
                        .build();

        component.inject(this);

        attachPlayerView();
        preparePlayer();
    }

    // Set player to SimpleExoPlayerView
    public void attachPlayerView() {
        simpleExoPlayerView.setPlayer(player);
    }

    public void preparePlayer() {
        player.prepare(videoSource);
    }

    // Activity onStop, player must be release because of memory saving
    @Override
    public void onStop(){
        super.onStop();
        player.release();
    }

}