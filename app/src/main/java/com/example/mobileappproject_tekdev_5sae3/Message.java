package com.example.mobileappproject_tekdev_5sae3;
public class Message {
    private long id;
    private String text;
    private String sender;

    public Message(long id, String text, String sender) {
        this.id = id;
        this.text = text;
        this.sender = sender;
    }

    // Getters et Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}

