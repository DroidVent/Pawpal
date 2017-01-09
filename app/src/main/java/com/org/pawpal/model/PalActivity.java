package com.org.pawpal.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hp-pc on 17-12-2016.
 */
public class PalActivity implements Parcelable{
    private String id;
    private String name;
    private boolean isChecked;


    public PalActivity(Parcel in) {
        id = in.readString();
        name = in.readString();
        isChecked = in.readByte() != 0;
    }

    public static final Creator<PalActivity> CREATOR = new Creator<PalActivity>() {
        @Override
        public PalActivity createFromParcel(Parcel in) {
            return new PalActivity(in);
        }

        @Override
        public PalActivity[] newArray(int size) {
            return new PalActivity[size];
        }
    };

    public PalActivity() {

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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeByte((byte) (isChecked ? 1 : 0));
    }
}
