package com.example.player.view;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.player.DemoApplication;
import com.example.player.R;
import com.example.player.model.MapsModuleListener;
import com.example.player.viewmodel.FeedViewModel;
import com.example.player.viewmodel.PostViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, LifecycleOwner {

    public static final String TAG = "MapFragment";
    // Google Map
    private GoogleMap mGoogleMap;
    private static final int DEFAULT_ZOOM = 12;
    // Coordinates Seattle (47.6062° N, 122.3321°W)  takes me to somewhere in Asia so I changed it to real Seattle Coordinates. ;)
    private final LatLng mDefaultLocation = new LatLng(47.608013, -122.335167);

    private ArrayList<LatLng> mMarkerPoints;

    private SupportMapFragment mapFragment;

    private MapsModuleListener fragmentListener;

    private FloatingActionButton showListButton;

    FeedViewModel viewModel;

    private LifecycleRegistry mLifecycleRegistry;
    private List<PostViewModel> postViewModelList;

    private String uuId;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(FeedViewModel.class);
    }

    public boolean isMapAdded() {
        return isAdded();
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
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLifecycleRegistry = new LifecycleRegistry(this);
        mLifecycleRegistry.markState(Lifecycle.State.CREATED);
    }

    @Override
    public void onStart() {
        super.onStart();
        mLifecycleRegistry.markState(Lifecycle.State.STARTED);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        ((DemoApplication) getActivity().getApplication()).appComponent().inject(this);

        uuId = getActivity().getIntent().getStringExtra("uuid");

        showListButton = view.findViewById(R.id.button_show_list);

        showListButton.setVisibility(View.GONE);

        // Show the list in a map format
        showListButton.setOnClickListener(v -> {
            if (fragmentListener != null) {
                fragmentListener.onMapsSearchClosed();
            }
        });

        initializeFragmentMap();
        return view;
    }

    /**
     * Function to load map.
     * */
    private void initializeFragmentMap() {
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
    }

    /**
     * In our case jump to Seaattle
     */
    private void jumpToDefaultLocation() {
        this.mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
    }

    private void jumpToPoiLocation(LatLng poiLocation) {
        this.mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(poiLocation, DEFAULT_ZOOM));
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;
        if (mMarkerPoints == null) {
            mMarkerPoints = new ArrayList<>();
        }

                // Print selected poi marker
        if (uuId != null) {
            final Observer<PostViewModel> postViewModelObserver = postViewModel -> {
                fragmentListener.onPoiSelected(postViewModel);

                if (postViewModel != null && postViewModel.getLatLn() != null) {
                    String[] latLn = postViewModel.getLatLn().split(",");

                    if (latLn != null && latLn.length > 0) {
                        LatLng poiLocation = new LatLng(Double.valueOf(latLn[0]), Double.valueOf(latLn[1]));

                        jumpToPoiLocation(poiLocation);
                        googleMap.addMarker(new MarkerOptions().position(
                                poiLocation).title(postViewModel.getNameOfPlace()));

                        googleMap.addMarker(new MarkerOptions().position(
                                mDefaultLocation).title(getString(R.string.def_location)));
                    }
                }
            };
            viewModel.getSelectedPoi(uuId).observe(this, postViewModelObserver);
        } else {

            jumpToDefaultLocation();
            showListButton.setVisibility(View.VISIBLE);

            // Print all pois markers
            if (this.postViewModelList != null && !this.postViewModelList.isEmpty()) {
                for (PostViewModel viewModel : this.postViewModelList) {
                    if (viewModel.getLatLng() != null && viewModel.getUid() != null) {
                        mMarkerPoints.add(viewModel.getLatLng());
                        googleMap.addMarker(new MarkerOptions().position(viewModel.getLatLng())
                                .title(viewModel.getNameOfPlace()));
                    }
                }
            }

            // Select poi marker
            googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Log.d(TAG, "_xxx onInfoWindowClick: " + marker.getTitle());
                    if (postViewModelList != null && !postViewModelList.isEmpty()) {
                        for (PostViewModel viewModel : postViewModelList) {
                            if (viewModel.getNameOfPlace() != null && marker.getTitle() != null && viewModel.getNameOfPlace().equals(marker.getTitle())) {
                                Log.d(TAG, "onInfoWindowClick: " + viewModel.getNameOfPlace());
                                break;
                            }
                        }
                    }
                }
            });
        }

    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }

    public void setPostViewModelList(List<PostViewModel> postViewModelList) {
        if (this.postViewModelList != null) {
            this.postViewModelList.clear();
        }
        if (postViewModelList == null) {
            postViewModelList = new ArrayList<>();
        }
        this.postViewModelList = postViewModelList;
    }
}