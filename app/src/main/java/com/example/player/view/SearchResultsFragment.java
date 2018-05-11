package com.example.player.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.player.DemoApplication;
import com.example.player.R;
import com.example.player.view.recycler.PostAdapter;
import com.example.player.view.recycler.RecyclerTouchListener;
import com.example.player.view.recycler.RecyclerViewClickListener;
import com.example.player.viewmodel.FeedViewModel;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchResultsFragment extends Fragment {

    public static final String TAG = "SearchResultsFragment";
    RecyclerView postList;

    MenuItem loadingMenuItem;

    @Inject
    FeedViewModel viewModel;

    private PostAdapter postAdapter;
    private LinearLayoutManager postListLayoutManager;

    /** Hold active loading observable subscriptions, so that they can be unsubscribed from when the activity is destroyed */
    private CompositeSubscription subscriptions;

    public SearchResultsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search_results, container, false);

        loadingMenuItem = (MenuItem) rootView.findViewById(R.id.progress);
        postList        = (RecyclerView) rootView.findViewById(R.id.post_list);

        ((DemoApplication) getActivity().getApplication()).appComponent().inject(this);

        subscriptions = new CompositeSubscription();
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        subscriptions.unsubscribe();
    }

    @Override
    public void onResume() {
        super.onResume();
        initViews();
        initBindings();

        // Initial page load
        loadNextPage();
    }

    private void initViews() {
        postListLayoutManager = new LinearLayoutManager(getContext());
        postList.setLayoutManager(postListLayoutManager);

        postList.addOnItemTouchListener(new RecyclerTouchListener(getContext(),
                postList, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //Values are passing to activity & to fragment as well
                startActivity(new Intent(getContext(), PlayerActivity.class));
            }

            @Override
            public void onLongClick(View view, int position) {

            }

        }));

        postAdapter = new PostAdapter();
        postList.setAdapter(postAdapter);
    }

    private void initBindings() {
        // Observable that emits when the RecyclerView is scrolled to the bottom
        Observable<Void> infiniteScrollObservable = Observable.create(subscriber -> {
            postList.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    subscriber.onNext(null);
                }
            });
        });

        subscriptions.addAll(
                // Bind list of posts to the RecyclerView
                viewModel.postsObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(postAdapter::setItems),

                // Bind loading status to show/hide loading spinner
                viewModel.isLoadingObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(this::setIsLoading),

                // Trigger next page load when RecyclerView is scrolled to the bottom
                infiniteScrollObservable.subscribe(x -> loadNextPage())
        );
    }

    private void loadNextPage() {
        subscriptions.add(viewModel.loadMorePosts().subscribe()
        );
    }

    private void setIsLoading(boolean isLoading) {
        if (loadingMenuItem != null) {
            loadingMenuItem.setVisible(isLoading);
        }
    }

}