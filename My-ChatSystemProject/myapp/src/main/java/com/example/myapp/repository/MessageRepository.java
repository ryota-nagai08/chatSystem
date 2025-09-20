package com.example.myapp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.myapp.entity.ChatMessage;

public interface MessageRepository extends JpaRepository<ChatMessage, Long>{
    List<ChatMessage> findByRoomId(Long roomId);
}
