package com.example.player.viewmodel;

import com.example.player.model.Result;
import com.example.player.model.Venue;

import lombok.Data;
import lombok.NonNull;

/**
 * Created by linke_000 on 17/08/2017.
 */
@Data
public class PostViewModel {

    private String nameOfPlace;
    private String categoryOfPlace;
    private String iconUrl;
    private String iconExtension = ".png";
    private String homePage;
    private double latitude;
    private double longitude;
    private boolean favorite;

    public PostViewModel(@NonNull Venue venue) {
        this.nameOfPlace = venue.getName();

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
            this.latitude = venue.getLocation().getLat();
            this.longitude = venue.getLocation().getLng();
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
}