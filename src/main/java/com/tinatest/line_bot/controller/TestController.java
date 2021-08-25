package com.tinatest.line_bot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@CrossOrigin("*")
@RequestMapping("/test")
@Controller
public class TestController {

    {
        log.info("init :\t" + this.getClass().getSimpleName());
    }


    @GetMapping
    public ResponseEntity printHello() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return ResponseEntity.ok("Hello welcome to my app : LineageKing :)   "+ now.format(formatter));
    }

}