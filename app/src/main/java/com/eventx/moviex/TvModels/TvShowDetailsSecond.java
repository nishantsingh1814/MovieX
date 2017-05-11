package com.eventx.moviex.TvModels;

import com.eventx.moviex.LoginAccount.AccountState;
import com.eventx.moviex.LoginAccount.AccountStateSecond;

import java.util.ArrayList;

/**
 * Created by Nishant on 4/29/2017.
 */

public class TvShowDetailsSecond {
    private ArrayList<TvGenre> genres;
    private String overview;
    private String poster_path;
    private String name;
    private String first_air_date;
    private float vote_average;
    private long vote_count;
    private int number_of_seasons;
    private AccountStateSecond account_states;

    public AccountStateSecond getAccount_states() {
        return account_states;
    }

    public void setAccount_states(AccountStateSecond account_states) {
        this.account_states = account_states;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    private String backdrop_path;

    public int getNumber_of_seasons() {
        return number_of_seasons;
    }

    public void setNumber_of_seasons(int number_of_seasons) {
        this.number_of_seasons = number_of_seasons;
    }

    public ArrayList<TvGenre> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<TvGenre> genres) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirst_air_date() {
        return first_air_date;
    }

    public void setFirst_air_date(String first_air_date) {
        this.first_air_date = first_air_date;
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
}
