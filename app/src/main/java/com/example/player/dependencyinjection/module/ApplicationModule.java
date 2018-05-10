package com.example.player.dependencyinjection.module;

import com.example.player.DemoApplication;

import dagger.Module;

/**
 * Created by linke_000 on 17/08/2017.
 */
@Module
public class ApplicationModule {
    private DemoApplication application;

    public ApplicationModule(DemoApplication application) {
        this.application = application;
    }
}