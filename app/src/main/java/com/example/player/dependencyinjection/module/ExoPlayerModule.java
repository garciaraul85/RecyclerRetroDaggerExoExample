package com.example.player.dependencyinjection.module;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;

import com.example.player.BuildConfig;
import com.example.player.client.FourSquaresClient;
import com.example.player.deserializer.DateDeserializer;
import com.example.player.deserializer.ObjectJsonDeserializer;
import com.example.player.model.BaseObject;
import com.example.player.view.MapFragment;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

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

    @Provides
    public FourSquaresClient provideMoviesClient() {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(BaseObject.class, new ObjectJsonDeserializer())
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .create();

        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            okHttpClientBuilder.addInterceptor(interceptor);
        }
        OkHttpClient client = okHttpClientBuilder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.foursquare.com")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(rxAdapter)
                .build();

        return retrofit.create(FourSquaresClient.class);
    }

    @Provides //scope is not necessary for parameters stored within the module
    public Context context() {
        return context;
    }

    @Provides
    public MapFragment providesMapFragment() {
        MapFragment mapFragment = new MapFragment();
        return mapFragment;
    }

}