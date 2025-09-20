package com.example.myapp.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.myapp.entity.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer>{
    
    List<Event> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT e FROM Event e LEFT JOIN FETCH e.createdBy")
    List<Event> findAllWithCreatedBy();

    List<Event> findByCreatedById(int id);
}
