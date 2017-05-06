package com.eventx.moviex.MovieModels;

import com.eventx.moviex.LoginAccount.AccountState;

import java.util.ArrayList;

/**
 * Created by Nishant on 3/27/2017.
 */

public class MovieDetails {
    private ArrayList<MovieGenre> genres;
    private String overview;
    private String poster_path;
    private String title;
    private String release_date;
    private int runtime;
    private String tagline;
    private float vote_average;
    private String backdrop_path;
    private AccountState account_states;

    public AccountState getAccount_states() {
        return account_states;
    }

    public void setAccount_states(AccountState account_states) {
        this.account_states = account_states;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public ArrayList<MovieGenre> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<MovieGenre> genres) {
        this.genres = genres;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public float getVote_average() {
        return vote_average;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }

    public long getVote_count() {
        return vote_count;
    }

    public void setVote_count(long vote_count) {
        this.vote_count = vote_count;
    }

    private long vote_count;
}
