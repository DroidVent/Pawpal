package com.org.pawpal.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hp-pc on 27-01-2017.
 */
public class GetLatestConversationResponse {
    private String code;
    private String message;
    @SerializedName("data")
    private LatestConversationResponse latestConversationResponse;

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

    public LatestConversationResponse getLatestConversationResponse() {
        return latestConversationResponse;
    }

    public void setLatestConversationResponse(LatestConversationResponse latestConversationResponse) {
        this.latestConversationResponse = latestConversationResponse;
    }
}
