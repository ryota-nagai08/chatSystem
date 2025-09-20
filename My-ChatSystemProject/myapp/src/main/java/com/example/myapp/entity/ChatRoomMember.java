package com.example.myapp.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="chat_room_members")
@Data
public class ChatRoomMember {

    @Id
    @Column(name="room_id")
    private Integer roomId;

    @Id
    @Column(name="user_id")
    private Integer userId;

    @Column(name="joined_at")
    private LocalDateTime joinedAt;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="room_id", insertable=false, updatable=false)
    private ChatRoom room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
}
