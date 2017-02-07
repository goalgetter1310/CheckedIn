package com.checkedin.model.response;

public class BaseModel {

    public static final int STATUS_SUCCESS = 0;
    public static final int STATUS_FALITURE = 1;
    public static final int STATUS_CHAT_MESSAGE = 2;

    private int status;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
