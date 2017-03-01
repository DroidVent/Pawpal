package com.org.pawpal.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hp-pc on 16-02-2017.
 */
public class LoginFb {
    private String email;
    @SerializedName("fb_id")
    private String fbId;
    @SerializedName("id_token")
    private String token;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
