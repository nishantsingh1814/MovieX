package com.eventx.moviex.PeopleModels;

/**
 * Created by Nishant on 3/30/2017.
 */

public class PeopleInfo {
    private String biography;
    private String birthday;
    private String homepage;
    private long id;
    private String name;
    private String place_of_birth;
    private String profile_path;
    private String[] also_known_as;

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace_of_birth() {
        return place_of_birth;
    }

    public void setPlace_of_birth(String place_of_birth) {
        this.place_of_birth = place_of_birth;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
    }

    public String[] getAlso_known_As() {
        return also_known_as;
    }

    public void setAlso_known_As(String[] also_known_As) {
        this.also_known_as = also_known_As;
    }
}
