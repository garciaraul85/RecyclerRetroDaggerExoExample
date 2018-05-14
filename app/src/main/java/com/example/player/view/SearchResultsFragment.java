package com.example.player.view;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.player.DemoApplication;
import com.example.player.R;
import com.example.player.model.MapsModuleListener;
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
public class SearchResultsFragment extends Fragment implements LifecycleOwner {

    public static final String TAG = "SearchResultsFragment";
    RecyclerView postList;

    private LifecycleRegistry mLifecycleRegistry;

    MenuItem loadingMenuItem;

    //@Inject
    FeedViewModel viewModel;

    private PostAdapter postAdapter;
    private LinearLayoutManager postListLayoutManager;

    private EditText poiSearchEditText;
    private FloatingActionButton showMapButton;
    private ImageView poiSearchClear;
    private Handler searchHandler = new Handler();

    /** Hold active loading observable subscriptions, so that they can be unsubscribed from when the activity is destroyed */
    private CompositeSubscription subscriptions;

    private MapsModuleListener fragmentListener;

    private String currentSearch = "";

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    public SearchResultsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (fragmentListener == null) {
            try {
                fragmentListener = (MapsModuleListener) context;
            } catch (ClassCastException e) {
                throw new ClassCastException("Activity must implement MapsModuleListener.");
            }
        }
        Log.d(TAG, "_yyy onAttach: ");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLifecycleRegistry = new LifecycleRegistry(this);
        mLifecycleRegistry.markState(Lifecycle.State.CREATED);
        Log.d(TAG, "_yyy onCreate: ");
    }

    @Override
    public void onStart() {
        super.onStart();
        mLifecycleRegistry.markState(Lifecycle.State.STARTED);
        Log.d(TAG, "_yyy onStart: ");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(FeedViewModel.class);
        Log.d(TAG, "_yyy onActivityCreated: ");

        showFab();
        manageSearchList();
        initBindings();

        // Initial page load
        search(currentSearch);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "_yyy onCreateView: ");
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search_results, container, false);

        setupUI(rootView);

        ((DemoApplication) getActivity().getApplication()).appComponent().inject(this);

        subscriptions = new CompositeSubscription();
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "_yyy onDestroy: ");
        subscriptions.unsubscribe();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "_yyy onResume: ");

        if (viewModel != null && viewModel.getQuery() != null && !viewModel.getQuery().equals("")) {
            search(viewModel.getQuery());
        }
    }

    @UiThread
    private void setupUI(@NonNull View rootView) {
        loadingMenuItem  = rootView.findViewById(R.id.progress);

        postList          = rootView.findViewById(R.id.post_list);
        poiSearchEditText = rootView.findViewById(R.id.poi_search_edit_text);

        poiSearchClear = rootView.findViewById(R.id.poi_search_clear);
        showMapButton  = rootView.findViewById(R.id.button_show_map);

        manageSearchListeners();
    }

    Runnable searchRunnable = new Runnable() {
        @Override
        public void run() {
            if (fragmentListener != null) {
                fragmentListener.onPoiSearchTermEntered(poiSearchEditText.getText().toString());
            }
        }
    };

    int cont;
    private void manageSearchList() {
        postListLayoutManager = new LinearLayoutManager(getContext());
        postList.setLayoutManager(postListLayoutManager);

        postList.addOnItemTouchListener(new RecyclerTouchListener(getContext(),
                postList, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //Values are passing to activity & to fragment as well
                Log.d(TAG, "_yyy: " + cont);
                String uuid = viewModel.getSelectedTitle(position);
                Intent intent = new Intent(getContext(), PlayerActivity.class);
                intent.putExtra("uuid", uuid);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }

        }));

        postAdapter = new PostAdapter(getActivity().getApplicationContext());
        postList.setAdapter(postAdapter);
    }

    public void manageSearchListeners() {
        poiSearchEditText.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (!TextUtils.isEmpty(s)) {
                            currentSearch = s.toString();
                            Log.d(TAG, "onTextChanged: " + currentSearch);
                            search(currentSearch);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        searchHandler.removeCallbacks(searchRunnable);
                        if (poiSearchEditText.getText().toString().length() > 0) {
                            Log.d(TAG, "afterTextChanged: ");
                            searchHandler.postDelayed(searchRunnable, 1000);
                        }
                        if (postAdapter != null) {
                            postAdapter.notifyDataSetChanged();
                        }
                    }
                }
        );

        poiSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //Clear focus here from edittext
                    poiSearchEditText.clearFocus();
                    fragmentListener.onPoiSearchTermEntered(poiSearchEditText.getText().toString());
                    hideSoftKeyboard();
                }
                return false;
            }
        });

        poiSearchEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getActivity().
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
            }
        });

        poiSearchClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                poiSearchEditText.setText("");
                search("");
            }
        });
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    private void initBindings() {
        Log.d(TAG, "_bbb initBindings: ");
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
                infiniteScrollObservable.subscribe(x -> loadNextPage(currentSearch))
        );

    }

    public boolean isSearchAdded() {
        return isAdded();
    }

    private void search(String query) {
        Log.d(TAG, "_bbb loadNextPage: " + query);
        if (viewModel.getCurrentList() != null && !viewModel.getCurrentList().isEmpty()) {
            Log.d(TAG, "_xxxsearch: " + viewModel.getCurrentList().get(0).getNameOfPlace());
        }

        try {
            subscriptions.add(viewModel.loadMorePosts(query).subscribe());
        } catch (NullPointerException e) {}

        if (postAdapter != null) {
            postAdapter.notifyDataSetChanged();
        }
    }

    private void loadNextPage(String query) {
        Log.d(TAG, "_bbbloadNextPage: " + query);
        try {
            subscriptions.add(viewModel.loadMorePosts(query).subscribe());
        } catch (NullPointerException e) {}

    }

    private void setIsLoading(boolean isLoading) {
        if (loadingMenuItem != null) {
            loadingMenuItem.setVisible(isLoading);
        }
    }

    private void showFab() {
        final Observer<Boolean> showFabObserver = showFab -> {
            if (showMapButton != null) {
                if (showFab != null && showFab) {
                    showMapButton.setVisibility(View.VISIBLE);
                } else {
                    showMapButton.setVisibility(View.GONE);
                }
            }
        };

        viewModel.getShowResultsMapFab().observe(this, showFabObserver);

        // Show the list in a map format
        showMapButton.setOnClickListener(v -> {
            if (fragmentListener != null) {
                hideSoftKeyboard();
                fragmentListener.onMapsSearchOpen(viewModel.getCurrentList());
            }
        });
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }

}