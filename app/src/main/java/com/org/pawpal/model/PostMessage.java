package com.org.pawpal.model;

/**
 * Created by hp-pc on 15-01-2017.
 */

public class PostMessage {
    private String user_profile_id;
    private String message_text;
    private String profile_id;

    public String getProfile_id() {
        return profile_id;
    }

    public void setProfile_id(String profile_id) {
        this.profile_id = profile_id;
    }

    public String getMessage_text() {
        return message_text;
    }

    public void setMessage_text(String message_text) {
        this.message_text = message_text;
    }

    public String getUser_profile_id() {
        return user_profile_id;
    }

    public void setUser_profile_id(String user_profile_id) {
        this.user_profile_id = user_profile_id;
    }
}
