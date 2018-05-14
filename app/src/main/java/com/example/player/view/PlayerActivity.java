package com.example.player.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.player.DemoApplication;
import com.example.player.R;
import com.example.player.model.MapsModuleListener;
import com.example.player.viewmodel.PostViewModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

public class PlayerActivity extends AppCompatActivity implements MapsModuleListener {

    private static final String TAG = "PlayerActivity";
    @Inject
    MapFragment mapFragment;

    private TextView categoryTextView;
    private TextView nameOfTextView;
    private TextView linkTextView;
    private ImageView categoryIconImageView;
    private FloatingActionButton favoritesFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        ((DemoApplication) getApplication()).demoComponent().inject(this);

        categoryTextView = (TextView) findViewById(R.id.category_text_view);
        nameOfTextView = (TextView) findViewById(R.id.name_text_view);
        linkTextView = (TextView) findViewById(R.id.link_text_view);
        categoryIconImageView = (ImageView) findViewById(R.id.category_icon_image_view);
        favoritesFloatingActionButton = (FloatingActionButton) findViewById(R.id.poi_fab);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMap();
    }

    @UiThread
    private void loadMap() {
        if (getSupportFragmentManager().findFragmentByTag(MapFragment.TAG) == null && mapFragment != null && !mapFragment.isMapAdded()) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.container, mapFragment, MapFragment.TAG);

            // Pass the Pois List into the map
            //mapFragment.setPostViewModelList(postViewModelList);
            fragmentTransaction.commit();
        }
    }


    @Override
    public void onMapsSearchOpen(List<PostViewModel> postViewModelList) {

    }

    @Override
    public void onMapsSearchClosed() {
    }

    @Override
    public void onPoiSearchTermEntered(String searchTerm) {

    }

    @Override
    public void onPoiSelected(PostViewModel poi) {
        Log.d(TAG, "onPoiSelected: " + poi);

        if (poi.getCategoryOfPlace() != null) {
            categoryTextView.setText(poi.getCategoryOfPlace());
        }
        if (poi.getNameOfPlace() != null) {
            nameOfTextView.setText(poi.getNameOfPlace());
        }

        if (poi.getHomePage() != null) {
            linkTextView.setText(poi.getHomePage());
            linkTextView.setOnClickListener(v -> {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(linkTextView.getText().toString()));
                startActivity(i);
            });
        } else {
            linkTextView.setText(R.string.no_page);
        }

        if (poi.isFavorite()) {
            favoritesFloatingActionButton.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorYellow), android.graphics.PorterDuff.Mode.MULTIPLY);
        } else {
            favoritesFloatingActionButton.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack), android.graphics.PorterDuff.Mode.MULTIPLY);
        }

        if (poi.getIconUrl() != null && poi.getIconExtension() != null) {
            Log.d(TAG, "icon: " + poi.getIconUrl() + "32" + poi.getIconExtension());
            Picasso.with(getApplicationContext()).load(poi.getIconUrl() + "32" + poi.getIconExtension()).into(categoryIconImageView, new Callback() {
                @Override
                public void onSuccess() {
                    Log.d("TAG", "onSuccess");
                }

                @Override
                public void onError() {
                    Log.d(TAG, "onError: ");
                }
            });
        }
    }
}