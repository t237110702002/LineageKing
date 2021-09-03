package com.tinatest.line_bot.controller;

import com.linecorp.bot.client.LineMessagingClient;
import com.tinatest.line_bot.service.LineBotService;
import com.tinatest.line_bot.service.LineNotifyService;
import com.tinatest.line_bot.service.LineageService;
import com.tinatest.line_bot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@CrossOrigin("*")
@RequestMapping("/test")
@RestController
public class TestController {

    {
        log.info("init :\t" + this.getClass().getSimpleName());
    }

    @Autowired
    private UserService userService;

    @Autowired
    private LineageService lineageService;

    @Autowired
    private LineMessagingClient client;

    @Autowired
    private LineBotService lineBotService;

    @Autowired
    private LineNotifyService lineNotifyService;

    @GetMapping
    public ResponseEntity printHello() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return ResponseEntity.ok("Hello welcome to my app : LineageKing :)   "+ now.format(formatter));
    }

    @GetMapping(value = "/notify/list")
    public List<String> test() {
        List<String> userNotifyList = userService.getUserNotifyList();
        return userNotifyList;
    }

}