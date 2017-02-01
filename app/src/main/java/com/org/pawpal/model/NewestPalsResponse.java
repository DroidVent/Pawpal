package com.org.pawpal.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by hp-pc on 27-01-2017.
 */

public class NewestPalsResponse {
    private String code;
    private String message;
    @SerializedName("data")
    private ArrayList<NewestPal> newestPal;

    public ArrayList<NewestPal> getNewestPal() {
        return newestPal;
    }

    public void setNewestPal(ArrayList<NewestPal> newestPal) {
        this.newestPal = newestPal;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
