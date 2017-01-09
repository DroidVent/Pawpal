package com.org.pawpal.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hp-pc on 12-12-2016.
 */
public class UserImages implements Parcelable{
    private String url;

    protected UserImages(Parcel in) {
        url = in.readString();
    }
    public UserImages()
    {

    }

    public static final Creator<UserImages> CREATOR = new Creator<UserImages>() {
        @Override
        public UserImages createFromParcel(Parcel in) {
            return new UserImages(in);
        }

        @Override
        public UserImages[] newArray(int size) {
            return new UserImages[size];
        }
    };

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(url);
    }
}
