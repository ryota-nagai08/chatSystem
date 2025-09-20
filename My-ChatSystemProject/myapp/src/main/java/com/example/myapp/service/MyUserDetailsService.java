package com.example.myapp.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.myapp.config.CustomUserDetails;
import com.example.myapp.entity.User;
import com.example.myapp.repository.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username){
        User user = userRepository.findByName(username)
                    .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりませんでした"));

        return new CustomUserDetails(user);
    }

    // パスワード更新処理
    public void updatePassword(int id, String encodedPassword){
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            user.setPassword(encodedPassword);
            userRepository.save(user);
        }
    }
}