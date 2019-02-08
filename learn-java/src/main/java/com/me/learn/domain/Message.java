package com.me.learn.domain;

public class Message {
    private final String message;
    private String source;

    public Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String completeMessage() {
        return String.format("%s from %s", message, source);
    }
}
