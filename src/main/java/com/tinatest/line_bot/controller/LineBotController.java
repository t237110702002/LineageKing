package com.tinatest.line_bot.controller;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@Slf4j
public class LineBotController {
    private static final String ACCESS_TOKEN = "yXg2XLMonW2Odz62Jc8bOW5TW5aYEOwP4yAJmRk53dp9BAV4RC+nunO5IAqXMSCK/8z7WcYUe655wZpjl2FttMkH3KZ4CLMjZhqDy8snZCtkJCWFCHQcCMWiQSariPJiU5InRcF75xmFeowOdyAM7AdB04t89/1O/w1cDnyilFU=";

    @GetMapping("/hello")
    public ResponseEntity printHello() {
        return ResponseEntity.ok("Hello welcome to my app : LineageKing :)");
    }

    @PostMapping("/callback")
    public ResponseEntity printHello(MessageEvent<TextMessageContent> event) {

        System.out.println(event.getMessage());

        System.out.println("hello");

        final LineMessagingClient client = LineMessagingClient
                .builder(ACCESS_TOKEN)
                .build();

        final TextMessage textMessage = new TextMessage("hello");
        final PushMessage pushMessage = new PushMessage(
                "userId", textMessage);

        final BotApiResponse botApiResponse;
        try {
            botApiResponse = client.pushMessage(pushMessage).get();
            System.out.println(botApiResponse);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("hello welcome to my app :");
    }



}
