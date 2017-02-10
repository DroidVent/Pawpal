package com.org.pawpal.model;

import java.util.ArrayList;

/**
 * Created by hp-pc on 25-11-2016.
 */
public class UserData {
    private String user_id;
    private String profile_id;
    private String name;
    private String is_subscribed;
    private ArrayList<UserImages> images;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getProfile_id() {
        return profile_id;
    }

    public void setProfile_id(String profile_id) {
        this.profile_id = profile_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<UserImages> getImages() {
        return images;
    }

    public void setImages(ArrayList<UserImages> images) {
        this.images = images;
    }

    public String getIs_subscribed() {
        return is_subscribed;
    }

    public void setIs_subscribed(String is_subscribed) {
        this.is_subscribed = is_subscribed;
    }
}
