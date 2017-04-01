package com.eventx.moviex.TvModels;

import java.io.Serializable;

/**
 * Created by Nishant on 3/29/2017.
 */

public class GuestStars implements Serializable {
    private String name;
    private String profile_path;
    private String character;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private long id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }
}
