package com.example.myapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.myapp.entity.ChatMessage;
import com.example.myapp.repository.MessageRepository;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    // 掲示板内のメッセージを取得
    public List<ChatMessage> getChatMessagesByroomId(Long roomId){
        return messageRepository.findByRoomId(roomId);
    }

    public ChatMessage saveMessage(ChatMessage message) {
        return messageRepository.save(message);
    }
}
