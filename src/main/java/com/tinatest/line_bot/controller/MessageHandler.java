package com.tinatest.line_bot.controller;

import java.util.concurrent.ExecutionException;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.JoinEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.UnfollowEvent;
import com.linecorp.bot.model.event.UnknownEvent;
import com.linecorp.bot.model.event.message.StickerMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import com.tinatest.line_bot.model.common.Common;
import com.tinatest.line_bot.service.LineBotService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@LineMessageHandler
public class MessageHandler {

    private final LineBotService lineBotService;

    public MessageHandler(LineBotService lineBotService) {
        super();
        this.lineBotService = lineBotService;
    }

    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws ExecutionException, InterruptedException {
        lineBotService.reply(event);
    }

    @EventMapping
    public void handleStickerMessageEvent(MessageEvent<StickerMessageContent> event) {
        lineBotService.handleSticker(event.getReplyToken(), event.getMessage());
    }

    @EventMapping
    public void defaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }

    @EventMapping
    public void handleUnfollowEvent(UnfollowEvent event) {
        log.info("unfollowed this bot: {}", event);
    }

    @EventMapping
    public void handleUnknownEvent(UnknownEvent event) {
        log.info("Got an unknown event!!!!! : {}", event);
    }

    @EventMapping
    public void handleFollowEvent(FollowEvent event) {
        String replyToken = event.getReplyToken();
        String userId = event.getSource().getUserId();
        log.warn("----------->[FollowEvent] userId:" + userId);
        boolean success = lineBotService.followEvent(userId, replyToken);

    }

    @EventMapping
    public void handleJoinEvent(JoinEvent event) {
        String replyToken = event.getReplyToken();
        String senderId = event.getSource().getSenderId();
        log.warn("----------->[JoinEvent] senderId:" + senderId);
        boolean success = lineBotService.joinEvent(senderId, replyToken);
    }
}