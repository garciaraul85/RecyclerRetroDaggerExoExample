package com.example.player.view.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.player.R;
import com.example.player.viewmodel.PostViewModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by linke_000 on 17/08/2017.
 */
public class PostAdapter extends RecyclerView.Adapter<PostViewHolder> {

    private static final String TAG = "PostAdapterTAG_";
    private List<PostViewModel> items = new ArrayList<>();

    public PostAdapter() {
        setHasStableIds(true);
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public PostViewModel getItem(int position) {
        return items.get(position);
    }


    public void setItems(List<PostViewModel> items) {
        if (items == null) {
            return;
        }

        this.items = new ArrayList<>(items);
        notifyDataSetChanged();
    }
}