package com.org.pawpal.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hp-pc on 17-12-2016.
 */
public class PostProfile implements Parcelable {
    @SerializedName("pet_size")
    private String pet_size = "";

    @SerializedName("profile_id")
    private String profile_id = "";

    @SerializedName("pet_gender")
    private String pet_gender = "";

    @SerializedName("pet_dob")
    private String pet_dob = "";

    @SerializedName("period")
    private String period = "";

    @SerializedName("frequency")
    private String frequency = "";

    @SerializedName("description")
    private String description = "";

    @SerializedName("pet_breed")
    private String pet_breed = "";

    @SerializedName("activities")
    private String activities;

    @SerializedName("profileImages")
    private String profileImages;

    private List<UserImages> userImages;
    public PostProfile()
    {

    }

    protected PostProfile(Parcel in) {
        pet_size = in.readString();
        profile_id = in.readString();
        pet_gender = in.readString();
        pet_dob = in.readString();
        period = in.readString();
        frequency = in.readString();
        description = in.readString();
        pet_breed = in.readString();
        activities = in.readString();
        profileImages = in.readString();
        userImages = in.createTypedArrayList(UserImages.CREATOR);
    }

    public static final Creator<PostProfile> CREATOR = new Creator<PostProfile>() {
        @Override
        public PostProfile createFromParcel(Parcel in) {
            return new PostProfile(in);
        }

        @Override
        public PostProfile[] newArray(int size) {
            return new PostProfile[size];
        }
    };

    public String getPetSize() {
        return pet_size;
    }

    public void setPetSize(String petSize) {
        this.pet_size = petSize;
    }


    public String getpet_gender() {
        return pet_gender;
    }

    public void setpet_gender(String pet_gender) {
        this.pet_gender = pet_gender;
    }

    public String getpet_dob() {
        return pet_dob;
    }

    public void setpet_dob(String pet_dob) {
        this.pet_dob = pet_dob;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPet_breed() {
        return pet_breed;
    }

    public void setPet_breed(String pet_breed) {
        this.pet_breed = pet_breed;
    }

    public String getactivities() {
        return activities;
    }

    public void setactivities(String activities) {
        this.activities = activities;
    }

    public String getUserImages() {
        return profileImages;
    }

    public void setUserImages(String userImages) {
        this.profileImages = userImages;
    }
    public List<UserImages> getProfileImages()
    {
        return userImages;
    }
    public String getProfileId() {
        return profile_id;
    }

    public void setProfileId(String profileId) {
        this.profile_id = profileId;
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
        parcel.writeString(pet_size);
        parcel.writeString(profile_id);
        parcel.writeString(pet_gender);
        parcel.writeString(pet_dob);
        parcel.writeString(period);
        parcel.writeString(frequency);
        parcel.writeString(description);
        parcel.writeString(pet_breed);
        parcel.writeString(activities);
        parcel.writeString(profileImages);
        parcel.writeTypedList(userImages);
    }
}
