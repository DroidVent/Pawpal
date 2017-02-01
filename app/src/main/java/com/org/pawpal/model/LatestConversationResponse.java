package com.org.pawpal.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by hp-pc on 27-01-2017.
 */
public class LatestConversationResponse {
    private ArrayList<Message> messages;
    @SerializedName("message_count")
    private String messageCount;
    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public String getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(String messageCount) {
        this.messageCount = messageCount;
    }
}
