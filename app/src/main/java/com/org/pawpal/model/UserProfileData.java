package com.org.pawpal.model;

import java.util.List;

/**
 * Created by hp-pc on 12-12-2016.
 */
public class UserProfileData {
    private String profile_id;
    private String name;
    private String pet_gender;
    private String pet_breed;
    private String pet_dob;
    private String pet_size;
    private String period;
    private String frequency;
    private String description;
    private List<PalActivity> activities;
    private List<UserImages> images;

    public String getProfile_id() {
        return profile_id;
    }

    public void setProfile_id(String profile_id) {
        this.profile_id = profile_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPet_gender() {
        return pet_gender;
    }

    public void setPet_gender(String pet_gender) {
        this.pet_gender = pet_gender;
    }

    public String getPet_breed() {
        return pet_breed;
    }

    public void setPet_breed(String pet_breed) {
        this.pet_breed = pet_breed;
    }

    public String getPet_dob() {
        return pet_dob;
    }

    public void setPet_dob(String pet_dob) {
        this.pet_dob = pet_dob;
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

    public List<PalActivity> getActivities() {
        return activities;
    }

    public void setActivities(List<PalActivity> activities) {
        this.activities = activities;
    }

    public List<UserImages> getImages() {
        return images;
    }

    public void setImages(List<UserImages> images) {
        this.images = images;
    }
}
