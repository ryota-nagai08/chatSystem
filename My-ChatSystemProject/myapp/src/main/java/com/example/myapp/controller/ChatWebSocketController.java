package com.example.myapp.controller;

import java.security.Principal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import com.example.myapp.config.CustomUserDetails;
import com.example.myapp.dto.ChatMessageResponse;
import com.example.myapp.entity.ChatMessage;
import com.example.myapp.service.MessageService;

@Controller
public class ChatWebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageService messageService;

    @MessageMapping("/chat/send")
    public void receiveMessage(@Payload ChatMessage message, Principal principal) {
        if (principal instanceof Authentication auth &&
                auth.getPrincipal() instanceof CustomUserDetails userDetails &&
                userDetails.getUser() != null) {

            message.setUserId(userDetails.getUser().getId());
            message.setUser(userDetails.getUser());
            message.setSentAt(LocalDateTime.now());

            ChatMessage saved = messageService.saveMessage(message);
            ChatMessageResponse response = new ChatMessageResponse(saved);
            messagingTemplate.convertAndSend("/topic/chat/" + response.getRoomId(), response);
        } else {
            // ログ出力すると調査に役立つ
            System.err.println("未認証または不正なユーザー情報。WebSocketメッセージ受信拒否");
        }
    }
}
