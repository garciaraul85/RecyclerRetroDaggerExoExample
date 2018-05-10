package com.example.player.view.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.player.R;
import com.example.player.viewmodel.PostViewModel;
import com.squareup.picasso.Picasso;

import org.apache.commons.validator.routines.UrlValidator;

/**
 * Created by linke_000 on 17/08/2017.
 */
public class PostViewHolder extends RecyclerView.ViewHolder {
    private View view;
    private TextView titleTextView;
    private TextView releaseDateTextView;
    private TextView popularityTextView;
    private ImageView posterPathImageView;
    private final String mainImagePath = "https://image.tmdb.org/t/p/w185_and_h278_bestv2/";

    public PostViewHolder(View view) {
        super(view);

        this.view = view;
        this.titleTextView = (TextView) view.findViewById(R.id.title);
        this.releaseDateTextView = (TextView) view.findViewById(R.id.releaseDate);
        this.popularityTextView = (TextView) view.findViewById(R.id.popularity);
        this.posterPathImageView = (ImageView)view.findViewById(R.id.posterPath);
    }

    public void bind(PostViewModel viewModel) {
        titleTextView.setText(viewModel.getTitle());
        releaseDateTextView.setText(viewModel.getReleaseDate());
        popularityTextView.setText(viewModel.getPopularity().toString());

        UrlValidator urlValidator = new UrlValidator();
        String imagePath = mainImagePath + viewModel.getPosterPath();
        boolean hasThumbnail = viewModel.getPosterPath() != null;// && urlValidator.isValid(imagePath);

        // Show/hide the thumbnail if there is/isn't one
        posterPathImageView.setVisibility(hasThumbnail ? View.VISIBLE : View.GONE);

        // Load the thumbnail if there is one
        if (hasThumbnail) {
            Picasso.with(view.getContext()).load(imagePath).into(posterPathImageView);
        }
    }

}