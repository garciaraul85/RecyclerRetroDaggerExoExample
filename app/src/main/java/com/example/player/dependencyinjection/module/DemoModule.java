package com.example.player.dependencyinjection.module;

import com.example.player.model.User;

import dagger.Module;
import dagger.Provides;

/**
 * Created by linke_000 on 21/08/2017.
 */
@Module
public class DemoModule {

    @Provides
    public User providesUser() {
        User user = new User();
        return user;
    }

}