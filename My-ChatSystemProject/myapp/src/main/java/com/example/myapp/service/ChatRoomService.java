package com.example.myapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.myapp.entity.ChatRoom;
import com.example.myapp.entity.User;
import com.example.myapp.repository.ChatRoomRepository;

@Service
public class ChatRoomService {
    
    @Autowired
    private ChatRoomRepository roomRepository;

    // 掲示板作成
    public ChatRoom createRoom(String name, User createdBy){
        ChatRoom room = new ChatRoom();
        room.setName(name);
        room.setCreatedBy(createdBy);
        return roomRepository.save(room);
    }
}
