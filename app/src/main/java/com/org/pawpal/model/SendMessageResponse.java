package com.org.pawpal.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hp-pc on 15-01-2017.
 */
public class SendMessageResponse {
    private String code;
    private String message;
    @SerializedName("data")
    private ThreadMessage threadMessage;
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

    public ThreadMessage getThreadMessage() {
        return threadMessage;
    }

    public void setThreadMessage(ThreadMessage threadMessage) {
        this.threadMessage = threadMessage;
    }
}
