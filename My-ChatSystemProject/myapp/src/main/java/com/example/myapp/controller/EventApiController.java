package com.example.myapp.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.myapp.config.CustomUserDetails;
import com.example.myapp.dto.EventDto;
import com.example.myapp.entity.Event;
import com.example.myapp.entity.User;
import com.example.myapp.repository.EventRepository;
import com.example.myapp.repository.UserRepository;

@RestController
@RequestMapping("/api/events")
public class EventApiController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    // イベント追加
    @PostMapping
    public Event addEvent(@RequestBody Event event, @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userRepository.findById(userDetails.getId()).orElseThrow();
        event.setCreatedBy(user);
        return eventRepository.save(event);
    }

    // イベント情報取得
    @GetMapping
    public List<EventDto> getEvents(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        List<Event> events = eventRepository.findByStartTimeBetween(start, end);
        return events.stream()
                .map(event -> new EventDto(
                        event.getId(),
                        event.getTitle(),
                        event.getStartTime(),
                        event.getEndTime(),
                        event.getDescription()
                ))
                .toList();
    }
}
