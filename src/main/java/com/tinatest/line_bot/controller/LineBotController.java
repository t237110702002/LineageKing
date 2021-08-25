package com.tinatest.line_bot.controller;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import com.tinatest.line_bot.service.LineBotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@Slf4j
public class LineBotController {

    private static final String ACCESS_TOKEN = "yXg2XLMonW2Odz62Jc8bOW5TW5aYEOwP4yAJmRk53dp9BAV4RC+nunO5IAqXMSCK/8z7WcYUe655wZpjl2FttMkH3KZ4CLMjZhqDy8snZCtkJCWFCHQcCMWiQSariPJiU5InRcF75xmFeowOdyAM7AdB04t89/1O/w1cDnyilFU=";

    @Autowired
    private LineBotService lineBotService;

    @PostMapping("/push")
    public ResponseEntity printHello(MessageEvent<TextMessageContent> event) {

        System.out.println("event: " + event);
//        BotApiResponse response = lineBotService.reply(event);
//
//        final LineMessagingClient client = LineMessagingClient
//                .builder(ACCESS_TOKEN)
//                .build();

        TextMessage textMessage = new TextMessage("hello");
        PushMessage pushMessage = new PushMessage("userId", textMessage);
        BotApiResponse botApiResponse;
        try {
            botApiResponse = lineBotService.reply(event);
            System.out.println("Sent messages: " + botApiResponse);

        } catch (InterruptedException | ExecutionException | IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("Call back success!");
    }


}
