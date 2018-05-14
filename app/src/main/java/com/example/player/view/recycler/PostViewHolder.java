package com.example.player.view.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.player.R;
import com.example.player.util.MapsUtil;
import com.example.player.viewmodel.PostViewModel;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import org.apache.commons.validator.routines.UrlValidator;

/**
 * Created by linke_000 on 17/08/2017.
 */
public class PostViewHolder extends RecyclerView.ViewHolder {
    private View view;
    private TextView nameTextView;
    private TextView categoryTextView;
    private TextView distanceTextView;
    private ImageView iconPathImageView;
    private ImageView favoriteImageView;
    private final LatLng mDefaultLocation = new LatLng(47.608013, -122.335167);
    private Context context;

    public PostViewHolder(View view, Context context) {
        super(view);

        this.context = context;
        this.view = view;
        this.nameTextView = view.findViewById(R.id.nameOfPlace);
        this.categoryTextView = view.findViewById(R.id.categoryOfPlace);
        this.distanceTextView = view.findViewById(R.id.distance);
        this.iconPathImageView = view.findViewById(R.id.iconUrl);
        this.favoriteImageView = view.findViewById(R.id.favoriteImageView);
    }

    public void bind(PostViewModel viewModel) {
        Log.d("TAG", "IS FAV: " + viewModel.isFavorite() + ", = " + viewModel.getNameOfPlace() + ", " + viewModel.getId());
        nameTextView.setText(viewModel.getNameOfPlace());
        categoryTextView.setText(viewModel.getCategoryOfPlace());
        if (viewModel.getLatLn() != null) {

            String[] latLn = viewModel.getLatLn().split(",");
            if (latLn.length > 0) {
                LatLng latLng = new LatLng(Double.valueOf(latLn[0]), Double.valueOf(latLn[1]));
                double distance = MapsUtil.CalculationByDistance(mDefaultLocation, latLng);
                distanceTextView.setText(String.format("%s%s%s", context.getString(R.string.distance), distance, context.getString(R.string.distance_mes)));
            }
        }

        //UrlValidator urlValidator = new UrlValidator();
        String imagePath = viewModel.getIconUrl() + "bg_88" + viewModel.getIconExtension();
        boolean hasThumbnail = viewModel.getIconUrl() != null;// && urlValidator.isValid(imagePath);

        // Show/hide the thumbnail if there is/isn't one
        iconPathImageView.setVisibility(hasThumbnail ? View.VISIBLE : View.GONE);

        // Shows or hides the favorite icon
        favoriteImageView.setVisibility(viewModel.isFavorite() ? View.VISIBLE : View.GONE);

        // Load the thumbnail if there is one
        if (hasThumbnail) {
            Glide.with(view.getContext())
                    .load(imagePath)
                    .placeholder(R.drawable.exo_controls_next)
                    .error(R.drawable.exo_controls_pause)
                    .into(iconPathImageView);
        }
    }

}