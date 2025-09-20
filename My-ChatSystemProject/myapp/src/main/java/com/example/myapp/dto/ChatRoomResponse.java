package com.example.myapp.dto;

import com.example.myapp.entity.ChatRoom;

import lombok.Data;

@Data
public class ChatRoomResponse {
    private Integer id;
    private String name;
    private String createdByName;

    public ChatRoomResponse(ChatRoom room) {
        this.id = room.getId();
        this.name = room.getName();
        this.createdByName = room.getCreatedBy().getName();
    }
}
