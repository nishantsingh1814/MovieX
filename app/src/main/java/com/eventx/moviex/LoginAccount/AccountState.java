package com.eventx.moviex.LoginAccount;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Nishant on 4/24/2017.
 */

public class AccountState {
    private boolean favorite;
    private boolean watchlist;



    private boolean rated;

    public boolean isRated() {
        return rated;
    }

    public void setRated(boolean rated) {
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
