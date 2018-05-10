package com.example.player.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.player.DemoApplication;
import com.example.player.R;
import com.example.player.model.User;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "TAG_";

    @Inject
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((DemoApplication) getApplication()).demoComponent().inject(this);

        if (user != null) {
            user.setUserName("raul");
            user.setPassword("password");
            Log.d(TAG, "onCreate: " + user.toString());
        }

    }
}