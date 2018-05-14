package com.example.player.viewmodel;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.example.player.model.Venue;
import com.google.android.gms.maps.model.LatLng;
import com.orm.SugarRecord;

import lombok.Data;
import lombok.NonNull;

/**
 * Created by linke_000 on 17/08/2017.
 */
@Entity(indices = {@Index(value = {"id"},
        unique = true)})
@Data
public class PostViewModel extends SugarRecord {

    private String uid;
    private String nameOfPlace;
    private String categoryOfPlace;
    private String iconUrl;
    private String iconExtension = ".png";
    private String homePage;
    private String latLn;
    private LatLng latLng;
    private double distanceToCenter;
    private boolean favorite;
    private boolean selected;

    public PostViewModel() {

    }

    public PostViewModel(@NonNull Venue venue) {
        this.nameOfPlace = venue.getName();

        if (venue.getId() != null) {
            this.uid = venue.getId();
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
            this.latLn = venue.getLocation().getLat() + "," +  venue.getLocation().getLng();
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public double getDistanceToCenter() {
        return distanceToCenter;
    }

    public void setDistanceToCenter(double distanceToCenter) {
        this.distanceToCenter = distanceToCenter;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getLatLn() {
        return latLn;
    }

    public void setLatLn(String latLn) {
        this.latLn = latLn;
    }
}