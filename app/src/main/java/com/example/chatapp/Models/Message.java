package com.example.chatapp.Models;

public class Message {

    private String Message;
    private String messageTime;
    private String userName;
    private String userImage;


    public Message() {
    }

    public Message(String message, String messageTime, String userName, String userImage) {
        Message = message;
        this.messageTime = messageTime;
        this.userName = userName;
        this.userImage = userImage;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}
