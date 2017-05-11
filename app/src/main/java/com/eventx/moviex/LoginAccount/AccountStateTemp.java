package com.eventx.moviex.LoginAccount;

/**
 * Created by Nishant on 4/29/2017.
 */

public class AccountStateTemp {
    private boolean favorite;
    private boolean watchlist;

    public boolean isWatchlist() {
        return watchlist;
    }

    public void setWatchlist(boolean watchlist) {
        this.watchlist = watchlist;
    }

    public boolean isFavorite() {

        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
