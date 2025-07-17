package com.demo.demo.entity.Chat;

public class ChatMessage {
    public enum MessageType {
        CHAT, JOIN, LEAVE
    }

    private MessageType type;
    private String sender;
    private String content;
    private String timestamp;
    private String avatarUrl;

    public ChatMessage() {}
    public ChatMessage(MessageType type, String sender, String content, String timestamp) {
        this.type = type;
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
    }

    // getters & setters
    public MessageType getType() { return type; }
    public void setType(MessageType type) { this.type = type; }
    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
}
