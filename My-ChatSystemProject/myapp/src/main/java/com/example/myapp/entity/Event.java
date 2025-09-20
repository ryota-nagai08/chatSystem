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
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Entity
@Table(name="events")
@Data
public class Event {
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    private String title;

    private String description;

    @Column(name="start_time")
    @NotNull
    private LocalDateTime startTime;

    @Column(name="end_time")
    @NotNull
    private LocalDateTime endTime;

    @ManyToOne
    @JoinColumn(name="created_by")
    private User createdBy;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    // UserエンティティのjoinedEventsとマッピング。多対多の設定。親はUser側（中間テーブルの管理や作成）。
    @ManyToMany(mappedBy = "joinedEvents")
    private List<User> participants = new ArrayList<>();


    // DB保存前に行う処理。登録日時と更新日時をセット。
    @PrePersist
    public void onPrePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // 更新時間を自動でセットする処理。
    @PreUpdate
    public void onPreUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}