package com.example.player.viewmodel;

import com.example.player.model.Result;

import lombok.Data;

/**
 * Created by linke_000 on 17/08/2017.
 */
@Data
public class PostViewModel {

    private String posterPath;
    private String title;
    private String releaseDate;
    private Double popularity;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;

    public PostViewModel(Result result, String type) {
        this.title       = result.getTitle();
        this.releaseDate = result.getReleaseDate();
        this.posterPath  = result.getPosterPath();
        this.popularity  = result.getPopularity();
        this.type        = type;
    }

    public PostViewModel(Result result) {
        this.title       = result.getTitle();
        this.releaseDate = result.getReleaseDate();
        this.posterPath  = result.getPosterPath();
        this.popularity  = result.getPopularity();
    }

    public PostViewModel(String type) {
        this.type = type;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }
}