package com.eventx.moviex.TvModels;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Nishant on 3/29/2017.
 */

public class Episodes implements Serializable{
    private String air_date;
    private ArrayList<GuestStars> guest_stars;
    private int episode_number;
    private int season_number;

    public boolean isWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }

    private boolean watched;

    public int getEpisode_number() {
        return episode_number;
    }

    public void setEpisode_number(int episode_number) {
        this.episode_number = episode_number;
    }

    public int getSeason_number() {
        return season_number;
    }

    public void setSeason_number(int season_number) {
        this.season_number = season_number;
    }

    private String name;
    private String overview;
    private long id;
    private String still_path;
    private double vote_average;
    private long vote_count;

    public String getAir_date() {
        return air_date;
    }

    public void setAir_date(String air_date) {
        this.air_date = air_date;
    }

    public ArrayList<GuestStars> getGuest_stars() {
        return guest_stars;
    }

    public void setGuest_stars(ArrayList<GuestStars> guest_stars) {
        this.guest_stars = guest_stars;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStill_path() {
        return still_path;
    }

    public void setStill_path(String still_path) {
        this.still_path = still_path;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public long getVote_count() {
        return vote_count;
    }

    public void setVote_count(long vote_count) {
        this.vote_count = vote_count;
    }
}
