package com.org.pawpal.model;

/**
 * Created by hp-pc on 01-12-2016.
 */
public class Register {
    private String profile_type;
    private String name;
    private String nick_name;
    private String email;
    private String phone;
    private String city;
    private String country;
    private String makani_number;
    private String password;
    private String address;
    private String lat;
    private String lng;
    private String fb_id;

    public Register(String profile_type, String name, String nick_name, String email, String phone, String city, String country, String makani_number, String password, String address, String lat, String lng, String fb_id) {
        this.profile_type = profile_type;
        this.name = name;
        this.nick_name = nick_name;
        this.email = email;
        this.phone = phone;
        this.city = city;
        this.country = country;
        this.makani_number = makani_number;
        this.password = password;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.fb_id = fb_id;
    }

    public String getProfile_type() {
        return profile_type;
    }

    public void setProfile_type(String profile_type) {
        this.profile_type = profile_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getMakani_number() {
        return makani_number;
    }

    public void setMakani_number(String makani_number) {
        this.makani_number = makani_number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getFb_id() {
        return fb_id;
    }

    public void setFb_id(String fb_id) {
        this.fb_id = fb_id;
    }
}
