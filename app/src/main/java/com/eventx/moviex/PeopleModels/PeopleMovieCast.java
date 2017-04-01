package com.eventx.moviex.PeopleModels;

import com.eventx.moviex.MovieModels.Movie;

import java.util.ArrayList;

/**
 * Created by Nishant on 3/30/2017.
 */

public class PeopleMovieCast {
    ArrayList<MovieCredits> cast;

    public ArrayList<MovieCredits> getCast() {
        return cast;
    }

    public void setCast(ArrayList<MovieCredits> cast) {
        this.cast = cast;
    }
}
