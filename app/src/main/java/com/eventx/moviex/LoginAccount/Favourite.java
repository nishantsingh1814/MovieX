package com.eventx.moviex.LoginAccount;

/**
 * Created by Nishant on 4/24/2017.
 */

public class Favourite {
    private String media_type;
    private boolean favorite;
    private long media_id;

    public Favourite(String media_type, boolean favorite, long media_id) {
        this.media_type = media_type;
        this.favorite = favorite;
        this.media_id = media_id;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public long getMedia_id() {
        return media_id;
    }

    public void setMedia_id(long media_id) {
        this.media_id = media_id;
    }
}
