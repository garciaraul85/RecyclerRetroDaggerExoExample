package com.example.player.dependencyinjection.component;

import com.example.player.dependencyinjection.module.ApplicationModule;
import com.example.player.dependencyinjection.module.ClientModule;
import com.example.player.view.FeedActivity;
import com.example.player.view.SearchResultsFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by linke_000 on 17/08/2017.
 */
@Singleton
@Component(modules = {ApplicationModule.class, ClientModule.class})
public interface ApplicationComponent {
    void inject(FeedActivity activity);
    void inject(SearchResultsFragment activity);
}