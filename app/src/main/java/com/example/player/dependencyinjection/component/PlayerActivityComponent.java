package com.example.player.dependencyinjection.component;

import android.content.Context;

import com.example.player.dependencyinjection.module.ExoPlayerModule;
import com.example.player.dependencyinjection.tags.PerActivity;
import com.example.player.view.PlayerActivity;

import dagger.Component;

/**
 * Created by linke_000 on 17/08/2017.
 */
@PerActivity
@Component(modules = {ExoPlayerModule.class})
public interface PlayerActivityComponent {
    Context context();

    void inject(PlayerActivity activity);
}