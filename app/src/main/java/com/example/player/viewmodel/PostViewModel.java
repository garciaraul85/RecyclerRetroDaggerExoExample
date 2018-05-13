package com.example.player.viewmodel;

import com.example.player.model.Result;
import com.example.player.model.Venue;
import com.google.android.gms.maps.model.LatLng;

import lombok.Data;
import lombok.NonNull;

/**
 * Created by linke_000 on 17/08/2017.
 */
@Data
public class PostViewModel {

    private String id;
    private String nameOfPlace;
    private String categoryOfPlace;
    private String iconUrl;
    private String iconExtension = ".png";
    private String homePage;
    private LatLng latLng;
    private double distanceToCenter;
    private boolean favorite;

    public PostViewModel(@NonNull Venue venue) {
        this.nameOfPlace = venue.getName();

        if (venue.getId() != null) {
            this.id = venue.getId();
        }

        if (venue.getCategories() != null && !venue.getCategories().isEmpty()) {
            this.categoryOfPlace = venue.getCategories().get(0).getName();
            if (venue.getCategories().get(0).getIcon() != null &&
                    venue.getCategories().get(0).getIcon().getPrefix() != null &&
                    venue.getCategories().get(0).getIcon().getSuffix() != null)
                this.iconUrl = venue.getCategories().get(0).getIcon().getPrefix();
                this.iconExtension = venue.getCategories().get(0).getIcon().getSuffix();
        }

        if (venue.getDelivery() != null && venue.getDelivery().getUrl() != null) {
            this.homePage = venue.getDelivery().getUrl();
        }

        if (venue.getLocation() != null) {
            this.latLng = new LatLng(venue.getLocation().getLat(), venue.getLocation().getLng());
        }

    }

    public String getNameOfPlace() {
        return nameOfPlace;
    }

    public void setNameOfPlace(String nameOfPlace) {
        this.nameOfPlace = nameOfPlace;
    }

    public String getCategoryOfPlace() {
        return categoryOfPlace;
    }

    public void setCategoryOfPlace(String categoryOfPlace) {
        this.categoryOfPlace = categoryOfPlace;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getIconExtension() {
        return iconExtension;
    }

    public void setIconExtension(String iconExtension) {
        this.iconExtension = iconExtension;
    }

    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getDistanceToCenter() {
        return distanceToCenter;
    }

    public void setDistanceToCenter(double distanceToCenter) {
        this.distanceToCenter = distanceToCenter;
    }
}