package com.tinatest.line_bot.controller;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import com.tinatest.line_bot.service.LineBotService;

//@LineMessageHandler
public class MessageHandler {
//
//    private final LineBotService lineBotService;
//
//    public MessageHandler(LineBotService lineBotService) {
//        super();
//        this.lineBotService = lineBotService;
//    }
//
//    @EventMapping
//    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws IOException, ExecutionException, InterruptedException {
//        System.out.println("event: " + event);
//        BotApiResponse response = lineBotService.reply(event);
//        System.out.println("Sent messages: " + response);
//    }
//
//    @EventMapping
//    public void defaultMessageEvent(Event event) {
//        System.out.println("event: " + event);
//    }

}