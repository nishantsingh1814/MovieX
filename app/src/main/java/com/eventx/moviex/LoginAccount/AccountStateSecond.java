package com.eventx.moviex.LoginAccount;

/**
 * Created by Nishant on 4/27/2017.
 */

public class AccountStateSecond {
    private boolean favorite;
    private boolean watchlist;


    private Rated rated;

    public Rated getRated() {
        return rated;
    }

    public void setRated(Rated rated) {
        this.rated = rated;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean isWatchlist() {
        return watchlist;
    }

    public void setWatchlist(boolean watchlist) {
        this.watchlist = watchlist;
    }
}
