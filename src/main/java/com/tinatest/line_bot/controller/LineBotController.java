package com.tinatest.line_bot.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@Slf4j
public class LineBotController {


    @GetMapping
    @ResponseBody
    public ResponseEntity defaultMed() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return ResponseEntity.ok("Hello welcome to my app : LineageKing :)   "+ now.format(formatter));
    }


//    @PostMapping("/push")
//    public ResponseEntity printHello(MessageEvent<TextMessageContent> event) {
//
//        System.out.println("event: " + event);
////        BotApiResponse response = lineBotService.reply(event);
////
////        final LineMessagingClient client = LineMessagingClient
////                .builder(ACCESS_TOKEN)
////                .build();
//
//        TextMessage textMessage = new TextMessage("hello");
//        PushMessage pushMessage = new PushMessage("userId", textMessage);
//        BotApiResponse botApiResponse;
//        try {
//            botApiResponse = lineBotService.reply(event);
//            System.out.println("Sent messages: " + botApiResponse);
//
//        } catch (InterruptedException | ExecutionException | IOException e) {
//            e.printStackTrace();
//        }
//        return ResponseEntity.ok("Call back success!");
//    }


}
