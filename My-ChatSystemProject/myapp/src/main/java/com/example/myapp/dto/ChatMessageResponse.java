package com.example.myapp.dto;

import java.time.LocalDateTime;

import com.example.myapp.entity.ChatMessage;

import lombok.Data;

@Data
public class ChatMessageResponse {
    private Integer id;
    private Integer roomId;
    private Integer userId;
    private String username;
    private String content;
    private String imageUrl;
    private LocalDateTime sentAt;

    public ChatMessageResponse(ChatMessage message) {
        this.id = message.getId();
        this.roomId = message.getRoomId();
        this.userId = message.getUserId();
        this.username = message.getUser().getName();
        this.content = message.getContent();
        this.imageUrl = message.getImageUrl();
        this.sentAt = message.getSentAt();
    }
}
