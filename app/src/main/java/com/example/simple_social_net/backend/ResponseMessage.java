package com.example.simple_social_net.backend;

public class ResponseMessage {
    public String textMessage;
    boolean isCurUser;

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public boolean isCurUser() {
        return isCurUser;
    }

    public void setCurUser(boolean curUser) {
        isCurUser = curUser;
    }
}
