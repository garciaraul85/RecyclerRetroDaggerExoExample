package com.example.player;

import android.app.Application;

import com.example.player.db.AppDatabase;
import com.example.player.dependencyinjection.component.ApplicationComponent;
import com.example.player.dependencyinjection.component.DaggerApplicationComponent;
import com.example.player.dependencyinjection.component.DaggerUserComponent;
import com.example.player.dependencyinjection.component.DemoComponent;
import com.example.player.dependencyinjection.module.ApplicationModule;
import com.example.player.dependencyinjection.module.DemoModule;
import com.orm.SugarContext;


/**
 * Created by linke_000 on 17/08/2017.
 */
public class DemoApplication extends Application {
    private DemoComponent demoComponent;
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        demoComponent = createDemoComponent();
        applicationComponent = createAppComponent();
        SugarContext.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }

    public ApplicationComponent createAppComponent() {
        return DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent appComponent() {
        return applicationComponent;
    }

    public DemoComponent createDemoComponent() {
        return DaggerUserComponent
                .builder()
                .demoModule(new DemoModule())
                .build();
    }

    public DemoComponent demoComponent() {
        return demoComponent;
    }

}