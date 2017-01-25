package com.org.pawpal.model;

import java.util.ArrayList;

/**
 * Created by hp-pc on 17-01-2017.
 */
public class Message {
    private String message_text;
    private String thread_id;
    private String created_date;
    private String name;
    private int isFav;
    private int is_archive;
    private int profile_id;
    private int other_user_profile_id;
    private ArrayList<UserImages> images;

    public int getProfile_id() {
        return profile_id;
    }

    public void setProfile_id(int profile_id) {
        this.profile_id = profile_id;
    }

    public String getMessage_text() {
        return message_text;
    }

    public void setMessage_text(String message_text) {
        this.message_text = message_text;
    }

    public String getThread_id() {
        return thread_id;
    }

    public void setThread_id(String thread_id) {
        this.thread_id = thread_id;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
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

    public int getIsFav() {
        return isFav;
    }

    public void setIsFav(int isFav) {
        this.isFav = isFav;
    }

    public int getOther_user_profile_id() {
        return other_user_profile_id;
    }

    public void setOther_user_profile_id(int other_user_profile) {
        this.other_user_profile_id = other_user_profile;
    }

    public int getIs_archive() {
        return is_archive;
    }

    public void setIs_archive(int is_archive) {
        this.is_archive = is_archive;
    }
}
