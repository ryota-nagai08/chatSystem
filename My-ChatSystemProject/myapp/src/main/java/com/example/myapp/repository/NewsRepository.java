package com.example.myapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.myapp.entity.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer>{
    
}
