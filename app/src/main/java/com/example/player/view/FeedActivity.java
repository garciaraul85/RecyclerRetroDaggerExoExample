package com.example.player.view;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.player.DemoApplication;
import com.example.player.R;

import javax.inject.Inject;


public class FeedActivity extends AppCompatActivity {

    SearchResultsFragment searchResultsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchResultsFragment = new SearchResultsFragment();
        if (searchResultsFragment != null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.add(R.id.container, searchResultsFragment, SearchResultsFragment.TAG);
            fragmentTransaction.commit();
        }
    }


}