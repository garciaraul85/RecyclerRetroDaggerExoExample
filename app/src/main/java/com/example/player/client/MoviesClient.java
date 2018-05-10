package com.example.player.client;

import com.example.player.model.BaseObject;
import com.example.player.model.Example;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by linke_000 on 17/08/2017.
 */
public interface MoviesClient {
    @GET("/3/movie/upcoming")
    Observable<Example> getTop(@Query("api_key") String apiKey, @Query("page") int limit);

    @GET("/3/movie/upcoming")
    Observable<BaseObject> getTop2(@Query("api_key") String apiKey, @Query("page") int limit);
}