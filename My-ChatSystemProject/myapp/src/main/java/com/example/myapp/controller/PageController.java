package com.example.myapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.myapp.config.CustomUserDetails;
import com.example.myapp.entity.ChatRoom;
import com.example.myapp.entity.Event;
import com.example.myapp.entity.User;
import com.example.myapp.repository.ChatRoomRepository;
import com.example.myapp.repository.EventRepository;
import com.example.myapp.repository.UserRepository;
import com.example.myapp.service.MyUserDetailsService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;








@Controller
public class PageController {

    @Autowired
    private MyUserDetailsService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ChatRoomRepository roomRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/manager")
    public String showManagerPage() {
        return "manager";
    }

    @GetMapping("/top")
    public String showTopPage() {
        return "top";
    }
    
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }
    
    // ユーザーホーム
    @GetMapping("/home")
    public String showHomePage(Authentication authentication, Model model) {

        if(authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof CustomUserDetails principal){
            User user = principal.getUser();
            model.addAttribute("user", user);
        }else{
            model.addAttribute("user", "No User");
        }
        // チャットルーム一覧
        List<ChatRoom> rooms = roomRepository.findAll();
        model.addAttribute("rooms", rooms);
        return "user/home";
    }
    
    // 管理者ホーム
    @GetMapping("/admin")
    public String showAdminHome(Authentication authentication, Model model) {
        if(authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof CustomUserDetails principal){
            User user = principal.getUser();
            model.addAttribute("user", user);
        }else{
            model.addAttribute("user", "No User");
        }
        return "manage/admin-home";
    }

    // マイページ表示
    @GetMapping("/mypage")
    public String getMethodName() {
        return "my-page";
    }

    // マイイベントリスト
    @GetMapping("/mylist")
    public String showMyEventList(@AuthenticationPrincipal CustomUserDetails userDetails, Authentication authentication,Model model) {
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow();
        List<Event> myList = user.getJoinedEvents();
        model.addAttribute("eventlist", myList);
        return "my-list";
    }
    
    // ユーザー一覧ページ
    @GetMapping("/userlist")
    public String showUserList(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "user-list";
    }
    
    // ユーザー削除処理
    @PostMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") int id) {

        List<Event> createdEvents = eventRepository.findByCreatedById(id);
        eventRepository.deleteAll(createdEvents);

        List<ChatRoom> createdChatRooms = roomRepository.findByCreatedById(id);
        roomRepository.deleteAll(createdChatRooms);

        userRepository.deleteById(id);
        return "redirect:/userlist";
    }

    // ユーザー退会処理
    @PostMapping("/user/leave/{id}")
    public String withdrawUser(@PathVariable("id") int id,
                                @AuthenticationPrincipal CustomUserDetails userDetails,
                                HttpServletRequest request) {
    List<Event> createdEvents = eventRepository.findByCreatedById(id);
    eventRepository.deleteAll(createdEvents);

    List<ChatRoom> createdChatRooms = roomRepository.findByCreatedById(id);
    roomRepository.deleteAll(createdChatRooms);

    userRepository.deleteById(id);

    HttpSession session = request.getSession(false);
    if (session != null) {
        session.invalidate();
    }
    return "redirect:/login?withdrawn";
}


    // パスワード変更画面表示
    @GetMapping("/changepassword")
    public String showChangePasswordView() {
        return "change-password";
    }
    
    // パスワード変更処理
    @PostMapping("/changepassword")
    public String changePassword(@RequestParam("new-pass") String newPassword,
                                @RequestParam("confirm-pass") String confirmPassword,
                                @AuthenticationPrincipal CustomUserDetails userDetails,
                                Model model) {
        if (newPassword == null || confirmPassword == null ||
            newPassword.isBlank() || confirmPassword.isBlank()) {
            model.addAttribute("error", "パスワードをすべて入力してください。");
            return "change-password";
        }

        if(newPassword.equals(confirmPassword)){
            userService.updatePassword(userDetails.getUser().getId(), passwordEncoder.encode(newPassword));
            model.addAttribute("message", "パスワードを変更しました");
            return "change-password";
        }else{
            model.addAttribute("error", "パスワードが一致しません");
            return "change-password";
        }
    }
}
