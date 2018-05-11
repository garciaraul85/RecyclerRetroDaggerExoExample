package com.example.player.view.recycler;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.player.R;
import com.example.player.viewmodel.PostViewModel;
import com.squareup.picasso.Picasso;

import org.apache.commons.validator.routines.UrlValidator;

/**
 * Created by linke_000 on 17/08/2017.
 */
public class PostViewHolder extends RecyclerView.ViewHolder {
    private View view;
    private TextView nameTextView;
    private TextView categoryTextView;
    private ImageView iconPathImageView;

    public PostViewHolder(View view) {
        super(view);

        this.view = view;
        this.nameTextView = (TextView) view.findViewById(R.id.nameOfPlace);
        this.categoryTextView = (TextView) view.findViewById(R.id.categoryOfPlace);
        this.iconPathImageView = (ImageView)view.findViewById(R.id.iconUrl);
    }

    public void bind(PostViewModel viewModel) {
        nameTextView.setText(viewModel.getNameOfPlace());
        categoryTextView.setText(viewModel.getCategoryOfPlace());

        //UrlValidator urlValidator = new UrlValidator();
        String imagePath = viewModel.getIconUrl() + "32" + viewModel.getIconExtension();
        boolean hasThumbnail = viewModel.getIconUrl() != null;// && urlValidator.isValid(imagePath);

        // Show/hide the thumbnail if there is/isn't one
        iconPathImageView.setVisibility(hasThumbnail ? View.VISIBLE : View.GONE);

        // Load the thumbnail if there is one
        Log.d("Picasso", "bind: " + imagePath);
        if (hasThumbnail) {
            Glide.with(view.getContext())
                    .load(imagePath)
                    .placeholder(R.drawable.exo_controls_next)
                    .error(R.drawable.exo_controls_pause)
                    .into(iconPathImageView);
            //            Picasso.with(view.getContext()).load("https://ss3.4sqi.net/img/categories_v2/food/mexican_32.png").into(iconPathImageView);
        }
    }

}