package com.example.android.movies.domain;

import java.io.Serializable;

/**
 * Created by mupac_000 on 08-02-17.
 */

public class Movie implements Serializable {
    private String originalTitle;
    private String thumbnail;
    private String synopsis;
    private double rating;
    private String realiseDate;

    public Movie(String originalTitle,String thumbnail, String synopsis, String rating, String realiseDate)
    {
        this.originalTitle = originalTitle;
        this.thumbnail = "http://image.tmdb.org/t/p/w185/"+thumbnail;
        this.synopsis = synopsis;
        this.rating = Double.valueOf(rating);
        this.realiseDate = realiseDate;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getRealiseDate() {
        return realiseDate;
    }

    public void setRealiseDate(String realiseDate) {
        this.realiseDate = realiseDate;
    }




}
