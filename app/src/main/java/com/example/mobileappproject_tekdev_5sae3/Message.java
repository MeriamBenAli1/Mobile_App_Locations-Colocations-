package com.example.mobileappproject_tekdev_5sae3;

public class Message {
    private long id;
    private String text;
    private String sender;
    private boolean isEdited;  // Champ pour indiquer si le message est modifié

    public Message(long id, String text, String sender) {
        this.id = id;
        this.text = text;
        this.sender = sender;
        this.isEdited = false;
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

    public boolean isEdited() {
        return isEdited;
    }

    public void setEdited(boolean edited) {
        isEdited = edited;
    }
}
