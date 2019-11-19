package com.csu.micrometer.dao;

public class Message {

    long userId;

    String content;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Message{" +
                "userId=" + userId +
                ", content='" + content + '\'' +
                '}';
    }
}
