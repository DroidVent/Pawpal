package com.org.pawpal.model;

import java.util.ArrayList;

/**
 * Created by hp-pc on 27-01-2017.
 */
public class LatestConversationResponse {
    private ArrayList<Message> messages;

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }
}
