package com.org.pawpal.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hp-pc on 02-03-2017.
 */
public class ProfileInfoResponse {
    private String code;
    private String message;
    @SerializedName("data")
    public ProfileInfo profileInfo;
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

    public ProfileInfo getProfileInfo() {
        return profileInfo;
    }

    public void setProfileInfo(ProfileInfo profileInfo) {
        this.profileInfo = profileInfo;
    }
}
