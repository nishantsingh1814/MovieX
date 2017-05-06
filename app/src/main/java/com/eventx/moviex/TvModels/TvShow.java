package com.eventx.moviex.TvModels;

import com.google.gson.annotations.SerializedName;

import static android.R.attr.rating;

/**
 * Created by Nishant on 3/27/2017.
 */

public class TvShow {
    private String name;
    private double vote_average;
    private String overview;
    private String poster_path;
    private float rating;

    public float getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    @SerializedName("id")
    private long tvId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public long getTvId() {
        return tvId;
    }

    public void setTvId(long tvId) {
        this.tvId = tvId;
    }
}
