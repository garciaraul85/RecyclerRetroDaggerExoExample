package com.example.player.dependencyinjection.module;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;

import com.example.player.view.MapFragment;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;

import dagger.Module;
import dagger.Provides;

/**
 * Created by linke_000 on 18/08/2017.
 */
@Module
public class ExoPlayerModule {
    private final Context context;
    private String videoUri;

    public ExoPlayerModule(Context context, String videoUri) {
        this.context  = context;
        this.videoUri = videoUri;
    }

    @Provides //scope is not necessary for parameters stored within the module
    public Context context() {
        return context;
    }

    @Provides
    public Handler provideMoviesClient() {
        Handler handler = new Handler();
        return handler;
    }

    @Provides
    public DefaultBandwidthMeter providesDefaultBandwidthMeter() {
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        return bandwidthMeter;
    }

    // Create player

    @Provides
    public TrackSelection.Factory providesVideoTrackSelection(
            DefaultBandwidthMeter bandwidthMeter) {
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
        return videoTrackSelectionFactory;
    }

    @Provides
    public TrackSelector providesTrackSelector(TrackSelection.Factory videoTrackSelectionFactory) {
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        return trackSelector;
    }

    @Provides
    public LoadControl providesLoadControl() {
        LoadControl loadControl = new DefaultLoadControl();
        return loadControl;
    }

    @Provides
    public SimpleExoPlayer providesExoPlayer(
            Context context, TrackSelector trackSelector, LoadControl loadControl) {
        SimpleExoPlayer player = ExoPlayerFactory.
                newSimpleInstance(context, trackSelector, loadControl);
        return player;
    }

    // Prepare player
    @Provides
    public String providesUserAgent(Context context) {
        return Util.getUserAgent(context, "AppName");
    }

    @Provides
    public HttpDataSource.Factory providesHttpDataSource(
            String userAgent, DefaultBandwidthMeter bandwidthMeter) {
        DefaultHttpDataSourceFactory factory =
                new DefaultHttpDataSourceFactory(userAgent, bandwidthMeter);
        return factory;
    }

    @Provides
    public DataSource.Factory providesDataSource(
            Context context, DefaultBandwidthMeter bandwidthMeter,
            HttpDataSource.Factory httpDataSource) {
        DefaultDataSourceFactory factory = new DefaultDataSourceFactory(
                context, bandwidthMeter, httpDataSource);
        return factory;
    }

    @Provides
    public MediaSource providesMediaSource(DataSource.Factory dataSourceFactory,
                                           Handler mainHandler) {
        Uri uri = Uri.parse(videoUri);
        ExtractorMediaSource extractorMediaSource = new ExtractorMediaSource(
                uri, dataSourceFactory, new DefaultExtractorsFactory(), mainHandler, null);
        return extractorMediaSource;
    }

    @Provides
    public MapFragment providesMapFragment() {
        MapFragment mapFragment = new MapFragment();
        return mapFragment;
    }

}