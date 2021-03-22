package com.example.chatapp.Models;

public class User {
    private String userId;
    private String userEmail;
    private String userName;
    private String userStatue;
    private String userPhoto;
    private String fullName;

    public User() {
    }

    public User(String userId, String userEmail, String userName, String userStatue, String userPhoto, String fullName) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.userName = userName;
        this.userStatue = userStatue;
        this.userPhoto = userPhoto;
        this.fullName = fullName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserStatue() {
        return userStatue;
    }

    public void setUserStatue(String userStatue) {
        this.userStatue = userStatue;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
