package com.example.myapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.myapp.entity.News;
import com.example.myapp.repository.NewsRepository;
import com.example.myapp.repository.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;





@Controller
public class NewsController {
    
    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private UserRepository userRepository;

    // お知らせ登録画面表示
    @GetMapping("/newsregister")
    public String showNewsRegister() {
        return "news/news-register";
    }
    
    // お知らせ登録処理
    @PostMapping("/newsregister")
    public String postNewsRegister(@RequestParam("news-title") String title,
                                   @RequestParam("news-content") String content) {
        News news = new News();
        news.setTitle(title);
        news.setDescription(content);

        newsRepository.save(news);
        return "redirect:/newslist";
    }
    
    // お知らせ削除処理
    @PostMapping("/news/delete/{id}")
    public String deleteNews(@PathVariable("id") Integer id) {
        newsRepository.deleteById(id);
        return "redirect:/newslist";
    }
    

    // お知らせ一覧表示
    @GetMapping("/newslist")
    public String showNewsList(@ModelAttribute News news, Model model) {
        List<News> newsList = newsRepository.findAll();
        model.addAttribute("newsList", newsList);
        return "news/news-list";
    }

    // お知らせ詳細表示
    @GetMapping("/newsdetail/{id}")
    public String showNewsDetail(@PathVariable("id") int id, Model model) {
        News news = newsRepository.findById(id).orElseThrow();
        model.addAttribute("news", news);
        return "news/news-detail";
    }
}
