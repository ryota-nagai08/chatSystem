package com.example.myapp.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventDto {
    private int id;
    private String title;
    private LocalDateTime start;
    private LocalDateTime end;
    private String description;
}
