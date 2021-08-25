package com.tinatest.line_bot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class LineBotController {


    @GetMapping("/hello")
    public ResponseEntity printHello() {
        System.out.println("hello");
        return ResponseEntity.ok("hello welcome to my app :");
    }



}
