package com.example.myapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.myapp.config.CustomUserDetails;
import com.example.myapp.entity.Event;
import com.example.myapp.entity.User;
import com.example.myapp.repository.EventRepository;
import com.example.myapp.repository.UserRepository;




@Controller
public class EventPageController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    // イベント削除処理
    @PostMapping("/event/delete/{id}")
    public String deleteEvent(@PathVariable("id") int id) {
        eventRepository.deleteById(id);
        return "redirect:/eventlist";
    }
    

    // イベントカレンダー表示
    @GetMapping("/eventcalendar")
    public String showEventCalendarPage() {
        return "event/event-register";
    }

    // イベント一覧表示
    @GetMapping("/eventlist")
    public String showEventList(@ModelAttribute Event event,
                                @AuthenticationPrincipal CustomUserDetails userDetails,
                                Model model) {
        List<Event> events = eventRepository.findAllWithCreatedBy();
        model.addAttribute("events",events);

    if (userDetails != null) {
        model.addAttribute("currentUser", userDetails.getUser());
    }
        return "event/event-list";
    }
    
    // イベント詳細表示
    @GetMapping("/eventdetail/{id}")
    public String showEventDetail(@PathVariable("id") int id, Model model) {
        Event event = eventRepository.findById(id).orElseThrow();
        model.addAttribute("event", event);
        return "event/event-detail";
    }

    // イベント出席
    @PostMapping("/event/join")
    public String joinEvent(@RequestParam("eventId")int eventId,
                            @AuthenticationPrincipal CustomUserDetails userDetails,
                            RedirectAttributes redirectAttributes) {

        Event event = eventRepository.findById(eventId).orElseThrow();
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow();

        // ユーザーの参加イベントに現在のイベントが含まれていない場合、追加。Eventエンティティには参加するユーザーを追加。
        if(!user.getJoinedEvents().contains(event)){
            user.getJoinedEvents().add(event);
            event.getParticipants().add(user);

            userRepository.save(user);
            eventRepository.save(event);
        }
        redirectAttributes.addFlashAttribute("joinMessage","出席登録が完了しました");
        return "redirect:/eventlist";
    }
    
    //イベント欠席
    @PostMapping("/event/leave")
    public String leaveEvent(@RequestParam("eventId")int eventId,
                            @AuthenticationPrincipal CustomUserDetails userDetails,
                            RedirectAttributes redirectAttributes) {

        Event event = eventRepository.findById(eventId).orElseThrow();
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow();
        
        // ユーザーの参加イベントに現在のイベントが含まれている場合、削除。Eventエンティティでは参加するユーザーを削除。
         if(user.getJoinedEvents().contains(event)){
            user.getJoinedEvents().remove(event);
            event.getParticipants().remove(user);

            userRepository.save(user);
            eventRepository.save(event);
        }
        redirectAttributes.addFlashAttribute("leaveMessage","欠席登録が完了しました");
        return "redirect:/eventlist";
    }
}

