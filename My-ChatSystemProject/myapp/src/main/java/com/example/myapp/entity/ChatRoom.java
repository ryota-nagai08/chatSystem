package com.example.myapp.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="chat_rooms")
@Data
public class ChatRoom {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(unique=true, nullable=false)
    private String name;

    @ManyToOne
    @JoinColumn(name="created_by")
    private User createdBy;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy="room", cascade=CascadeType.ALL, orphanRemoval= true)
    List<ChatMessage> messages = new ArrayList<>();

    @OneToMany(mappedBy="room", cascade=CascadeType.ALL, orphanRemoval= true)
    List<ChatRoomMember> members = new ArrayList<>();

    @PrePersist
    public void onPrePersist(){
        this.createdAt = LocalDateTime.now();
    }
}
