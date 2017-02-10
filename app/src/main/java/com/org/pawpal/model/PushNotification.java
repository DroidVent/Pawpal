package com.org.pawpal.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hp-pc on 07-02-2017.
 */
public class PushNotification implements Parcelable{
    private String message;
    private String profile_id;
    private String thread_id;
    private String title;
    public PushNotification()
    {

    }
    protected PushNotification(Parcel in) {
        message = in.readString();
        profile_id = in.readString();
        thread_id = in.readString();
        title = in.readString();
    }

    public static final Creator<PushNotification> CREATOR = new Creator<PushNotification>() {
        @Override
        public PushNotification createFromParcel(Parcel in) {
            return new PushNotification(in);
        }

        @Override
        public PushNotification[] newArray(int size) {
            return new PushNotification[size];
        }
    };

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getProfile_id() {
        return profile_id;
    }

    public void setProfile_id(String profile_id) {
        this.profile_id = profile_id;
    }

    public String getThread_id() {
        return thread_id;
    }

    public void setThread_id(String thread_id) {
        this.thread_id = thread_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(message);
        parcel.writeString(profile_id);
        parcel.writeString(thread_id);
        parcel.writeString(title);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
