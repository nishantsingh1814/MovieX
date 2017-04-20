package com.eventx.moviex.MovieModels;

import com.eventx.moviex.TvModels.Episodes;

import java.util.ArrayList;

/**
 * Created by Nishant on 3/29/2017.
 */

public class EpisodeResults {
    ArrayList<Episodes> episodes;

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    String poster_path;

    public ArrayList<Episodes> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(ArrayList<Episodes> episodes) {
        this.episodes = episodes;
    }
}
