package com.example.player.view;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.location.Location;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

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

        showListButton = view.findViewById(R.id.button_show_list);

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


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;
        if (mMarkerPoints == null) {
            mMarkerPoints = new ArrayList<>();
        }

        jumpToDefaultLocation();

        for (PostViewModel viewModel : viewModel.getCurrentList()) {
            Log.d(TAG, "_xxx  Place: " + viewModel.getNameOfPlace());
        }
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }
}