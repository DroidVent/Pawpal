package com.org.pawpal.model;

import java.util.ArrayList;

/**
 * Created by hp-pc on 16-01-2017.
 */
public class GetInboxMessageResponse {
    private String code;
    private String message;
    private ArrayList<Message> messages;
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }
}
