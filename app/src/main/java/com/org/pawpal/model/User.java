package com.org.pawpal.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hp-pc on 24-11-2016.
 */

public class User {
    private int code;
    private String message;
    @SerializedName("data")
    private UserData userData;

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
