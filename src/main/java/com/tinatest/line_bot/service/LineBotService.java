package com.tinatest.line_bot.service;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.Broadcast;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.JoinEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.StickerMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.StickerMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import com.tinatest.line_bot.model.common.Common;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class LineBotService {

    @Autowired
    private LineMessagingClient client;

    @Autowired
    private LineageService lineageService;

    @Autowired
    private UserService userService;

    public void pushMessage(String userId) {
        final TextMessage textMessage = new TextMessage("hello");
        final PushMessage pushMessage = new PushMessage(
                "userId", textMessage);

        final BotApiResponse botApiResponse;
        try {
            botApiResponse = client.pushMessage(pushMessage).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return;
        }
        System.out.println(botApiResponse);
    }

    public boolean followEvent(FollowEvent event) {
        try {
            userService.createUser(event.getSource().getSenderId());
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public boolean joinEvent(JoinEvent event) {
        try {
            userService.createUser(event.getSource().getSenderId());
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public BotApiResponse reply(MessageEvent<TextMessageContent> event) throws  ExecutionException, InterruptedException {
        String receivedMessage = event.getMessage().getText();
        String replyToken = event.getReplyToken();
        String msg;
        receivedMessage = receivedMessage.toLowerCase();
        receivedMessage = receivedMessage.trim();

        if (receivedMessage.startsWith("push:") && userService.isUserAdmin(event.getSource().getUserId())) {
            String pushMsg = StringUtils.substringAfter(receivedMessage, "push:").trim();
            Broadcast broadcast = new Broadcast(new TextMessage(pushMsg));
            return client.broadcast(broadcast).get();
        } else if (receivedMessage.startsWith("activate:") && userService.isUserAdmin(event.getSource().getUserId())) {
            String code = StringUtils.substringAfter(receivedMessage, "activate:").trim();
            boolean success = userService.activateUser(code);
            if (success) {
                msg = "啟用成功: " + code;
                client.pushMessage(new PushMessage(code, new TextMessage(Common.ALERT + "小幫手啟用成功! 請使用help查看指令集!")));
            } else {
                msg = "啟用失敗: " + code;
            }
        } else if (StringUtils.equalsIgnoreCase(receivedMessage, "enable")) {
            msg = userService.updateUserNotify(event.getSource().getSenderId(), true);
        } else if (StringUtils.equalsIgnoreCase(receivedMessage, "disable")) {
            msg = userService.updateUserNotify(event.getSource().getSenderId(), false);
        } else {
            msg = lineageService.getMsg(receivedMessage);
        }
        return client.replyMessage(new ReplyMessage(replyToken, new TextMessage(msg))).get();
    }

    public void handleSticker(String replyToken, StickerMessageContent content) {
        client.replyMessage(new ReplyMessage(replyToken, new StickerMessage(
                        content.getPackageId(), content.getStickerId())));
    }

    public void replyText(String replyToken, String message) {
        client.replyMessage(new ReplyMessage(replyToken, new TextMessage(message)));
    }

    public void pushMsg(List<String> ids, Message message) {
        ids.forEach(id -> client.pushMessage(new PushMessage(id, message)));
    }

}
