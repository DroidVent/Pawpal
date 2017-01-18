package com.org.pawpal.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hp-pc on 18-01-2017.
 */
public class GetThreadMessageResponse {
    private String code;
    private String message;
    @SerializedName("data")
    private ThreadMessageResponse response;
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

    public ThreadMessageResponse getResponse() {
        return response;
    }

    public void setResponse(ThreadMessageResponse response) {
        this.response = response;
    }
}
