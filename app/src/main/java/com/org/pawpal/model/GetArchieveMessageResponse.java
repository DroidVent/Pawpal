package com.org.pawpal.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hp-pc on 26-01-2017.
 */
public class GetArchieveMessageResponse {
    private String code;
    private String message;
    @SerializedName("data")
    private ArchieveMessageResponse archieveMessageResponse;

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

    public ArchieveMessageResponse getArchieveMessageResponse() {
        return archieveMessageResponse;
    }

    public void setArchieveMessageResponse(ArchieveMessageResponse archieveMessageResponse) {
        this.archieveMessageResponse = archieveMessageResponse;
    }
}
