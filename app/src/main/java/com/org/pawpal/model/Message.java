package com.org.pawpal.model;

/**
 * Created by hp-pc on 14-01-2017.
 */
public class Message {

    private String reply;
    private int type;
    private String username;
    private int isStar;
    private int isArchieve;
    private String message_date;
    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getIsStar() {
        return isStar;
    }

    public void setIsStar(int isStar) {
        this.isStar = isStar;
    }

    public int getIsArchieve() {
        return isArchieve;
    }

    public void setIsArchieve(int isArchieve) {
        this.isArchieve = isArchieve;
    }

    public String getMessage_date() {
        return message_date;
    }

    public void setMessage_date(String message_date) {
        this.message_date = message_date;
    }
}
