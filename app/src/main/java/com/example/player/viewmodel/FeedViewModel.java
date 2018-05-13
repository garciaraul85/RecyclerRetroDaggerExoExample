package com.example.player.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.player.client.FourSquaresClient;
import com.example.player.model.Response;
import com.example.player.model.Result;
import com.example.player.model.Venue;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by linke_000 on 17/08/2017.
 * Hope you guys like ViewModel and Android architecture, its a greate google library that allows us to preserver data state as long for configuration changes in activities and fragments.
 */
public class FeedViewModel extends ViewModel {
    private static final String TAG = "ViewModelTAG_";
    private FourSquaresClient fourSquareClient;

    private String clientId;
    private String clientSecret;
    private String version;
    private final static String NEAR = "Seattle";
    private String query = "";
    private BehaviorSubject<List<PostViewModel>> postSubject = BehaviorSubject.create(new ArrayList<>());
    private BehaviorSubject<Boolean> isLoadingSubject       = BehaviorSubject.create(false);

    private List<PostViewModel> currentList;

    private MutableLiveData<Boolean> showResultsMapFab = new MutableLiveData<>();

    @Inject
    public FeedViewModel(FourSquaresClient fourSquareClient) {
        this.fourSquareClient = fourSquareClient;
        this.clientId = "JV0ODA1NXAIUKULYS5MNOVAGTP3WYT00IAM4P2N1ACQVDT53";
        this.clientSecret = "ZGNPF3YWIPXI2AQTMBF52Q5MPO2BUQO3HXSMG5VKV0FRBZMU";
        this.version = "20180511";
        currentList = new ArrayList<>();
    }

    public Observable<List<PostViewModel>> loadMorePosts(String query) {

        if (!this.query.equals(query)) {
            currentList.clear();
            this.query = query;

            Log.d(TAG, "loadMorePosts: " + query);
            // Don't try and load if we're already loading
            if (isLoadingSubject.getValue()) {
                return Observable.empty();
            }

            isLoadingSubject.onNext(true);

            return fourSquareClient
                    .getSearch(query, NEAR, clientId, clientSecret, version)
                    // Safe to cast to RedditListing, as this is always returned from top posts
                    .cast(Result.class)
                    // Flatten into observable of Results
                    .map(Result::getResponse)
                    .flatMapIterable(Response::getVenues)
                    .filter(Venue.class::isInstance)
                    // Transform model to viewmodel
                    .map(link -> new PostViewModel((Venue) link))
                    // Merge viewmodels into a single list to be emitted
                    .toList()
                    // Concatenate the new posts to the current posts list, then emit it via the post subject
                    .doOnNext(this::call)
                    .doOnTerminate(() -> isLoadingSubject.onNext(false));
        } else {
            Log.d(TAG, "_xxx return previous search: ");
            restoreList(currentList);
            return null;
        }
    }

    public Observable<List<PostViewModel>> postsObservable() {
        return postSubject.asObservable();
    }

    public Observable<Boolean> isLoadingObservable() {
        return isLoadingSubject.asObservable();
    }

    public String getSelectedTitle(int position) {
        if (currentList != null && !currentList.isEmpty() && currentList.size() > position) {
            return currentList.get(position).getCategoryOfPlace();
        } else {
            return "Empty: " + position + "_" + currentList.size();
        }
    }

    private void call(List<PostViewModel> list) {
        Log.d(TAG, "_xxx call: " + list.toString());

        List<PostViewModel> fullList = new ArrayList<>(postSubject.getValue());
        fullList.addAll(list);

        currentList.addAll(list);

        postSubject.onNext(list);
        showResultsMapFab.postValue(!list.isEmpty());
    }

    private void restoreList(List<PostViewModel> list) {
        Log.d(TAG, "_xxx call: " + list.toString());

        List<PostViewModel> fullList = new ArrayList<>(postSubject.getValue());
        fullList.addAll(list);

        postSubject.onNext(list);
        showResultsMapFab.postValue(!list.isEmpty());
    }

    public MutableLiveData<Boolean> getShowResultsMapFab() {
        return showResultsMapFab;
    }

    public List<PostViewModel> getCurrentList() {
        return currentList;
    }
}