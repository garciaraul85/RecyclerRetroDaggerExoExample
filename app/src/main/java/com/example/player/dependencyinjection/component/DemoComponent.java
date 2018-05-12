package com.example.player.dependencyinjection.component;

import com.example.player.dependencyinjection.module.DemoModule;
import com.example.player.dependencyinjection.tags.PerActivity;
import com.example.player.view.FeedActivity;

import dagger.Component;

/**
 * Created by linke_000 on 21/08/2017.
 */
@PerActivity
@Component(modules = { DemoModule.class })
public interface DemoComponent {
    void inject(FeedActivity activity);
}