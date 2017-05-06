package com.eventx.moviex.MovieModels;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Nishant on 3/27/2017.
 */

public class Movie implements Serializable{
    private String title;
    private String poster_path;
    private double vote_average;
    private String release_date;

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    private float rating;

    public float getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    private String overview;

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    @SerializedName("id")
    private long movieId;

    public Movie(String title, String poster_path, long movieId) {
        this.title = title;
        this.poster_path = poster_path;
        this.movieId = movieId;
    }



    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }
}
