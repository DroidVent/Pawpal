package com.org.pawpal.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by hp-pc on 18-01-2017.
 */
public class ThreadMessageResponse {
    @SerializedName("messages")
    private ArrayList<ThreadMessage> messages;

    @SerializedName("profile")
    private ThreadProfile profile;


    public ArrayList<ThreadMessage> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<ThreadMessage> messages) {
        this.messages = messages;
    }

    public ThreadProfile getProfile() {
        return profile;
    }

    public void setProfile(ThreadProfile profile) {
        this.profile = profile;
    }
}
