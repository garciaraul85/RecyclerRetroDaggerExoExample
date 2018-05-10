package com.example.player.view.recycler;

import android.view.View;

/**
 * Created by linke_000 on 17/08/2017.
 */
public interface RecyclerViewClickListener {
    void onClick(View view, int position);
    void onLongClick(View view, int position);
}