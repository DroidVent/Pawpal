package com.org.pawpal.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hp-pc on 17-12-2016.
 */

public class PalActivitiyResponse {
    private String code;
    @SerializedName("data")
    private List<PalActivity> palActivities;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<PalActivity> getPalActivities() {
        return palActivities;
    }

    public void setPalActivities(List<PalActivity> palActivities) {
        this.palActivities = palActivities;
    }
}
