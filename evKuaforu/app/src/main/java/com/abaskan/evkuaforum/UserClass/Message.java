package com.abaskan.evkuaforum.UserClass;

public class Message {
    private String userId;
    private String barberId;
    private String message;
    private String date;
    private String from;

    public Message() {
    }

    public Message(String userId, String barberId, String message, String date, String from) {
        this.userId = userId;
        this.barberId = barberId;
        this.message = message;
        this.date = date;
        this.from = from;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBarberId() {
        return barberId;
    }

    public void setBarberId(String barberId) {
        this.barberId = barberId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
