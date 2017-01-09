package com.org.pawpal.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hp-pc on 12-12-2016.
 */
public class Profile {
    private String code;
    private String message;
    @SerializedName("data")
    private UserProfileData userData;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserProfileData getUserData() {
        return userData;
    }

    public void setUserData(UserProfileData userData) {
        this.userData = userData;
    }
}
