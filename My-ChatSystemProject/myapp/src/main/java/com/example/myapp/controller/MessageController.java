package com.example.myapp.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.myapp.dto.ChatMessageResponse;
import com.example.myapp.service.MessageService;

@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;

    //掲示板ごとのメッセージを表示
    @GetMapping("/api/chatrooms/{roomId}/messages")
    public List<ChatMessageResponse> getMessages(@PathVariable Long roomId){
        return messageService.getChatMessagesByroomId(roomId)
                .stream()
                .map(ChatMessageResponse :: new)
                .collect(Collectors.toList());
    }
}
