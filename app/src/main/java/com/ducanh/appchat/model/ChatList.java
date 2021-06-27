package com.ducanh.appchat.model;

public class ChatList {
    private String id;

    public ChatList() {
    }

    public ChatList(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
