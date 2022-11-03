package com.example.alpha1;

public class ChatLog {
    private String USER_NAME;
    private String MESSAGE;
    private String TIME;

    public ChatLog() {
        // Empty Constructor
    }

    public ChatLog(String USER_NAME, String MESSAGE,String TIME) {
        this.USER_NAME = USER_NAME;
        this.MESSAGE = MESSAGE;
        this.TIME = TIME;
    }

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
    }

    public String getMESSAGE() {
        return MESSAGE;
    }

    public void setMESSAGE(String MESSAGE) {
        this.MESSAGE = MESSAGE;
    }

    public String getTIME() {
        return TIME;
    }

    public void setTIME(String TIME) {
        this.TIME = TIME;
    }
}
