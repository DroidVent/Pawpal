package com.org.pawpal.model;

/**
 * Created by hp-pc on 18-01-2017.
 */
public class ThreadMessage {
    private String message_text;
    private String thread_id;
    private String created_date;
    private int is_own_msg;
    private String profile_image;

    public int getIs_own_msg() {
        return is_own_msg;
    }

    public void setIs_own_msg(int is_own_msg) {
        this.is_own_msg = is_own_msg;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getThread_id() {
        return thread_id;
    }

    public void setThread_id(String thread_id) {
        this.thread_id = thread_id;
    }

    public String getMessage_text() {
        return message_text;
    }

    public void setMessage_text(String message_text) {
        this.message_text = message_text;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }
}
