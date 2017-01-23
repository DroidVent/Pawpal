package com.org.pawpal.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hp-pc on 30-12-2016.
 */
public class SearchPal implements Parcelable{
    private String id;
    private String name;
    private String distance;
    private String profile_created_at;
    private String last_login;
    private String pet_size;
    private String period;
    private String description;
    private String profile_type;
    @SerializedName("activities")
    private List<PalActivity> palActivities;
    @SerializedName("images")
    private List<UserImages> userImages;

    protected SearchPal(Parcel in) {
        id = in.readString();
        name = in.readString();
        distance = in.readString();
        profile_created_at = in.readString();
        last_login = in.readString();
        pet_size = in.readString();
        period = in.readString();
        description = in.readString();
        palActivities = in.createTypedArrayList(PalActivity.CREATOR);
        userImages = in.createTypedArrayList(UserImages.CREATOR);
        profile_type = in.readString();
    }
    public SearchPal()
    {

    }

    public static final Creator<SearchPal> CREATOR = new Creator<SearchPal>() {
        @Override
        public SearchPal createFromParcel(Parcel in) {
            return new SearchPal(in);
        }

        @Override
        public SearchPal[] newArray(int size) {
            return new SearchPal[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getProfile_created_at() {
        return profile_created_at;
    }

    public void setProfile_created_at(String profile_created_at) {
        this.profile_created_at = profile_created_at;
    }

    public String getLast_login() {
        return last_login;
    }

    public void setLast_login(String last_login) {
        this.last_login = last_login;
    }

    public String getPet_size() {
        return pet_size;
    }

    public void setPet_size(String pet_size) {
        this.pet_size = pet_size;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PalActivity> getPalActivities() {
        return palActivities;
    }

    public void setPalActivities(List<PalActivity> palActivities) {
        this.palActivities = palActivities;
    }

    public List<UserImages> getUserImages() {
        return userImages;
    }

    public void setUserImages(List<UserImages> userImages) {
        this.userImages = userImages;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(distance);
        parcel.writeString(profile_created_at);
        parcel.writeString(last_login);
        parcel.writeString(pet_size);
        parcel.writeString(period);
        parcel.writeString(description);
        parcel.writeTypedList(palActivities);
        parcel.writeTypedList(userImages);
        parcel.writeString(profile_type);
    }

    public String getProfile_type() {
        return profile_type;
    }

    public void setProfile_type(String profile_type) {
        this.profile_type = profile_type;
    }
}
