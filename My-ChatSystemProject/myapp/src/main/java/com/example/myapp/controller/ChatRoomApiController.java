package com.example.myapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.myapp.config.CustomUserDetails;
import com.example.myapp.dto.ChatRoomRequest;
import com.example.myapp.dto.ChatRoomResponse;
import com.example.myapp.entity.ChatRoom;
import com.example.myapp.entity.User;
import com.example.myapp.service.ChatRoomService;

@RestController
@RequestMapping("/api/chatrooms")
public class ChatRoomApiController {

    @Autowired
    private ChatRoomService roomService;

    @PostMapping
    public ChatRoomResponse create(@RequestBody ChatRoomRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        User user = userDetails.getUser();
        ChatRoom savedChatRoom = roomService.createRoom(request.getName(), user);

        return new ChatRoomResponse(savedChatRoom);
    }
}
