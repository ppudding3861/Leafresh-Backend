package com.leafresh.backend.chat.model.dto;

import java.time.LocalDateTime;

public class ChatMessageDTO {

    private Integer id;
    private Integer senderId;
    private Integer recipientId;
    private String content;
    private LocalDateTime timestamp;

    public ChatMessageDTO() {
    }

    public ChatMessageDTO(Integer id, Integer senderId, Integer recipientId, String content, LocalDateTime timestamp) {
        this.id = id;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.content = content;
        this.timestamp = timestamp;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Integer getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Integer recipientId) {
        this.recipientId = recipientId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ChatMessageDTO{" +
                "id=" + id +
                ", senderId=" + senderId +
                ", recipientId=" + recipientId +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
