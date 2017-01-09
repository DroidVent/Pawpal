package com.org.pawpal.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hp-pc on 30-12-2016.
 */

public class SearchPalResponse {
    private String code;
    private String message;
    private String pawl_count;
    @SerializedName("data")
    private List<SearchPal> searchPals;

    public String getCode() {
        return code;
    }

    public String getPawl_count() {
        return pawl_count;
    }

    public void setPawl_count(String pawl_count) {
        this.pawl_count = pawl_count;
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

    public List<SearchPal> getSearchPals() {
        return searchPals;
    }

    public void setSearchPals(List<SearchPal> searchPals) {
        this.searchPals = searchPals;
    }
}
