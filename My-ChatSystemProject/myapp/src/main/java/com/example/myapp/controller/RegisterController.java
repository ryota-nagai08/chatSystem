package com.example.myapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.myapp.entity.User;
import com.example.myapp.repository.UserRepository;



@Controller
public class RegisterController {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 新規登録画面表示
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {

        model.addAttribute("user", new User());
        return "register";
    }
    
    // ユーザー新規登録処理
    @PostMapping("/register")
    public String processRegistration(@ModelAttribute("user") User user, Model model) {
        if(userRepository.findByName(user.getName()).isPresent()){
            model.addAttribute("error", "このユーザー名は既に使用されています");
            return "register";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if(user.getRole() == null || user.getRole().isEmpty()){
            user.setRole("USER");
        }
        userRepository.save(user);
        return "redirect:/login?registered";
    }
}
