package com.org.pawpal.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hp-pc on 16-01-2017.
 */
public class GetSentMessageResponse {
    private String code;
    private String message;
    @SerializedName("data")
    private SentMessageResponse response;
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

    public SentMessageResponse getResponse() {
        return response;
    }

    public void setResponse(SentMessageResponse response) {
        this.response = response;
    }
}
