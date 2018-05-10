package com.example.player.viewmodel;

import com.example.player.client.MoviesClient;
import com.example.player.model.Example;
import com.example.player.model.Result;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;

/**
 * Created by linke_000 on 17/08/2017.
 */
public class FeedViewModel {
    private static final String TAG = "ViewModelTAG_";
    private MoviesClient moviesClient;

    private int page;
    private String apiKey;
    private BehaviorSubject<List<PostViewModel>> postSubject = BehaviorSubject.create(new ArrayList<>());
    private BehaviorSubject<Boolean> isLoadingSubject        = BehaviorSubject.create(false);

    List<PostViewModel> currentList;

    @Inject
    public FeedViewModel(MoviesClient moviesClient) {
        this.moviesClient = moviesClient;
        this.apiKey       = "be6fd15f2a967853814cce71eafabab9";
        this.page         = 1;
        currentList       = new ArrayList<PostViewModel>();
    }

    public Observable<List<PostViewModel>> loadMorePosts() {
        // Don't try and load if we're already loading
        if (isLoadingSubject.getValue()) {
            return Observable.empty();
        }

        isLoadingSubject.onNext(true);

        return moviesClient
                .getTop(apiKey, page)
                // Safe to cast to RedditListing, as this is always returned from top posts
                .cast(Example.class)
                // Store the page, so we can use it to get the next page of posts is a subsequent load
                .doOnNext(new Action1<Example>() {
                    @Override
                    public void call(Example example) {
                        page = example.getPage();
                        if (page < example.getTotalResults()) {
                            page++;
                        }
                    }
                })
                // Flatten into observable of Results
                .map(Example::getResults)
                .flatMapIterable(list -> list)
                .filter(object -> object instanceof Result)
                // Transform model to viewmodel
                .map(link -> new PostViewModel((Result) link))
                // Merge viewmodels into a single list to be emitted
                .toList()
                // Concatenate the new posts to the current posts list, then emit it via the post subject
                .doOnNext(list -> {
                    List<PostViewModel> fullList = new ArrayList<>(postSubject.getValue());
                    fullList.addAll(list);

                    currentList.addAll(list);

                    postSubject.onNext(fullList);
                })
                .doOnTerminate(() -> isLoadingSubject.onNext(false));
    }

    public Observable<List<PostViewModel>> postsObservable() {
        return postSubject.asObservable();
    }

    public Observable<Boolean> isLoadingObservable() {
        return isLoadingSubject.asObservable();
    }

    public String getSelectedTitle(int position) {
        if (currentList != null && !currentList.isEmpty() && currentList.size() > position) {
            return currentList.get(position).getTitle();
        } else {
            return "Empty: " + position + "_" + currentList.size();
        }
    }

}