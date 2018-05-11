package com.example.player.client;

import com.example.player.model.Result;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by linke_000 on 11/05/2018.
 */
public interface FourSquaresClient {
    @GET("/v2/venues/search")
    Observable<Result> getSearch(@Query("query") String query, @Query("near") String near, @Query("client_id") String clientId, @Query("client_secret") String clientSecret, @Query("v") String version);

}