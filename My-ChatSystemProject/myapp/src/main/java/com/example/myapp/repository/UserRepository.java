package com.example.myapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.myapp.entity.User;

@Repository
public interface  UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByName(String name);
}
