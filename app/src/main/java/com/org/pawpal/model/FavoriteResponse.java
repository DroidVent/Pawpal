package com.org.pawpal.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hp-pc on 02-01-2017.
 */

public class FavoriteResponse {
    private String code;
    private String message;
    @SerializedName("data")
    private List<Favorite> favorites;
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

    public List<Favorite> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<Favorite> favorites) {
        this.favorites = favorites;
    }
}
