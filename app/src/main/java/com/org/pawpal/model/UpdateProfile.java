package com.org.pawpal.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hp-pc on 21-12-2016.
 */
public class UpdateProfile {
    private String code;
    private String message;
    @SerializedName("data")
    private UpdateProfileData userData;

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

    public UpdateProfileData getUserData() {
        return userData;
    }

    public void setUserData(UpdateProfileData userData) {
        this.userData = userData;
    }
}
