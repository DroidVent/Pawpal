package com.org.pawpal.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by hp-pc on 18-01-2017.
 */
public class ThreadProfile {
    @SerializedName("profile_created_at")
    private String profile_created_at;

    @SerializedName("distance")
    private String distance;

    @SerializedName("name")
    private String name;

    @SerializedName("profile_id")
    private String profile_id;

    @SerializedName("images")
    private ArrayList<UserImages> userImages;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getProfile_created_at() {
        return profile_created_at;
    }

    public void setProfile_created_at(String profile_created_at) {
        this.profile_created_at = profile_created_at;
    }

    public ArrayList<UserImages> getUserImages() {
        return userImages;
    }

    public void setUserImages(ArrayList<UserImages> userImages) {
        this.userImages = userImages;
    }
    public String getProfile_id() {
        return profile_id;
    }

    public void setProfile_id(String profile_id) {
        this.profile_id = profile_id;
    }
}
