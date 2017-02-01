package com.org.pawpal.model;

import java.util.ArrayList;

/**
 * Created by hp-pc on 27-01-2017.
 */
public class NewestPal {
    private ArrayList<UserImages> images;
    private String name;
    private String id;
    private String distance;

    public ArrayList<UserImages> getImages() {
        return images;
    }

    public void setImages(ArrayList<UserImages> images) {
        this.images = images;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

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
}
