package com.eventx.moviex.LoginAccount;

/**
 * Created by Nishant on 4/24/2017.
 */

public class WatchList {
    private String media_type;
    private long media_id;
    private boolean watchlist;

    public String getMedia_type() {
        return media_type;
    }

    public WatchList(String media_type, long media_id, boolean watchlist) {
        this.media_type = media_type;
        this.media_id = media_id;
        this.watchlist = watchlist;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public long getMedia_id() {
        return media_id;
    }

    public void setMedia_id(long media_id) {
        this.media_id = media_id;
    }

    public boolean isWatchlist() {
        return watchlist;
    }

    public void setWatchlist(boolean watchlist) {
        this.watchlist = watchlist;
    }
}
