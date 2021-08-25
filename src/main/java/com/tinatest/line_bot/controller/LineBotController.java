package com.tinatest.line_bot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.beans.EventHandler;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class LineBotController {


    @GetMapping("/hello")
    @ResponseBody
    public ResponseEntity printHello() {
        System.out.println("hello");
        return ResponseEntity.ok("hello welcome to my app :");


    }



}
