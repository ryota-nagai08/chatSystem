package com.example.myapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.myapp.config.CustomUserDetails;
import com.example.myapp.entity.ChatRoom;
import com.example.myapp.entity.User;
import com.example.myapp.repository.ChatRoomRepository;


@Controller
public class ChatRoomController {

    @Autowired
    private ChatRoomRepository roomRepository;

    // 各ルームへの遷移画面表示
    @GetMapping("/home/chatrooms/{id}")
    public String showChatRoom(@PathVariable Integer id, Authentication authentication ,Model model) {
        if(authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof CustomUserDetails principal){
            User user = principal.getUser();
            model.addAttribute("user", user);
        }else{
            model.addAttribute("user", "No User");
        }
        // チャットルーム一覧
        List<ChatRoom> rooms = roomRepository.findAll();
        // 現在のルーム名
        ChatRoom currentRoom = roomRepository.findById(id).orElseThrow();

        model.addAttribute("rooms", rooms);
        model.addAttribute("currentRoom", currentRoom.getName());
        model.addAttribute("roomId", id);
        return "user/home";
    }

    @PostMapping("/delete/chatrooms/{id}")
    public String postMethodName(@PathVariable int id) {
        roomRepository.deleteById(id);
        return "redirect:/home";
    }
}