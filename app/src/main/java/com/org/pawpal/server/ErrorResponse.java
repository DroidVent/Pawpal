package com.org.pawpal.server;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hp-pc on 22-12-2016.
 */

public class ErrorResponse {
    @SerializedName("code")
    int errorCode;

    @SerializedName("message")
    String msg;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
