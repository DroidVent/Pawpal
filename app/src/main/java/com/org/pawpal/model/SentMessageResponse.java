package com.org.pawpal.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by hp-pc on 17-01-2017.
 */
public class SentMessageResponse {
    @SerializedName("messages")
    private ArrayList<Message> messages;

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }
}
