package com.leafresh.backend.chat_with_spring.chat.model;

public class ChatMessageDTO {
    private Long id;
    private String name;
    private String message;
    private Long chatRoomId;
    private Long senderId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    @Override
    public String toString() {
        return "ChatMessageDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", message='" + message + '\'' +
                ", chatRoomId=" + chatRoomId +
                ", senderId=" + senderId +
                '}';
    }
}
