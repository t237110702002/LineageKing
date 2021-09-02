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
import com.tinatest.line_bot.service.LineNotifyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
@LineMessageHandler
public class MessageHandler {

    private final LineBotService lineBotService;

    private final LineNotifyService lineNotifyService;

    public MessageHandler(LineBotService lineBotService, LineNotifyService lineNotifyService) {
        super();
        this.lineBotService = lineBotService;
        this.lineNotifyService = lineNotifyService;
    }

    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws ExecutionException, InterruptedException {
//        System.out.println("event: " + event);
        BotApiResponse response = lineBotService.reply(event);
//        System.out.println("Sent messages: " + response);
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
        boolean success = lineBotService.followEvent(event);
        if (success) {
            String authLink = lineNotifyService.generateAuthLink(event.getSource().getUserId());
            String code = StringUtils.substring(event.getSource().getUserId(), 1, 7);
            lineBotService.replyText(replyToken, String.format("Hi~ 我是天堂打王小幫手! 請先點擊下方連結完成連動並繳費，完成後告知管理員啟用通知功能，謝謝! %s\n請告知管理員您的代碼 : %s \n%s",
                    Common.SMILE, code, authLink));
        }
    }

    @EventMapping
    public void handleJoinEvent(JoinEvent event) {
        String replyToken = event.getReplyToken();
        boolean success = lineBotService.joinEvent(event);
        if (success) {
            String authLink = lineNotifyService.generateAuthLink(event.getSource().getUserId());
            String code = StringUtils.substring(event.getSource().getSenderId(), 1, 7);
            log.warn("senderId:" + event.getSource().getSenderId());
            lineBotService.replyText(replyToken, String.format("Hi 我是天堂打王小幫手！請先點擊下方連結完成連動並繳費，完成後告知管理員代碼(%s)以啟用通知功能！%s\n %s ",
                    code, Common.SMILE, authLink));
        }

    }
}