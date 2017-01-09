package com.org.pawpal.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by hp-pc on 26-12-2016.
 */

public class FilterPal  implements Parcelable{
    private String pet_size;
    private String host_period;
    private String frequency;
    private String profile_id;
    private String lat;
    private String lng;
    private int page;
    private String last_profile_id;

    private List<String> activities;

    protected FilterPal(Parcel in) {
        pet_size = in.readString();
        host_period = in.readString();
        frequency = in.readString();
        profile_id = in.readString();
        lat = in.readString();
        lng = in.readString();
        activities = in.createStringArrayList();
    }
    public FilterPal()
    {

    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getLast_profile_id() {
        return last_profile_id;
    }

    public void setLast_profile_id(String last_profile_id) {
        this.last_profile_id = last_profile_id;
    }

    public static final Creator<FilterPal> CREATOR = new Creator<FilterPal>() {
        @Override
        public FilterPal createFromParcel(Parcel in) {
            return new FilterPal(in);
        }

        @Override
        public FilterPal[] newArray(int size) {
            return new FilterPal[size];
        }
    };

    public List<String> getActivities() {
        return activities;
    }

    public void setActivities(List<String> activities) {
        this.activities = activities;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getProfile_id() {
        return profile_id;
    }

    public void setProfile_id(String profile_id) {
        this.profile_id = profile_id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getPet_size() {
        return pet_size;
    }

    public void setPet_size(String pet_size) {
        this.pet_size = pet_size;
    }

    public String getHost_period() {
        return host_period;
    }

    public void setHost_period(String host_period) {
        this.host_period = host_period;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(pet_size);
        parcel.writeString(host_period);
        parcel.writeString(frequency);
        parcel.writeString(profile_id);
        parcel.writeString(lat);
        parcel.writeString(lng);
        parcel.writeStringList(activities);
    }
}
