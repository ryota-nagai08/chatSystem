package com.example.myapp.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String mail;

    private String password;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    private String role;

    // 中間テーブルevent_participantsを通してUserとEventを多対多で結びつける。
    @ManyToMany
    @JoinTable(
        name="event_participants",
        joinColumns= @JoinColumn(name="user_id"),
        inverseJoinColumns= @JoinColumn(name="event_id")
    )
    private List<Event> joinedEvents = new ArrayList<>();

    @PrePersist
    public void onPrePersist() {
    this.createdAt = LocalDateTime.now();
}

}
