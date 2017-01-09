package com.org.pawpal.model;

import java.util.List;

/**
 * Created by hp-pc on 02-01-2017.
 */
public class Favorite {
    private String id;
    private String name;
    private String distance;
    private List<UserImages> images;

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

    public List<UserImages> getImages() {
        return images;
    }

    public void setImages(List<UserImages> images) {
        this.images = images;
    }
}
