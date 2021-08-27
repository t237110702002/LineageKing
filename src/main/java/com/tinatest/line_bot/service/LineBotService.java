package com.tinatest.line_bot.service;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.Broadcast;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.StickerMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.StickerMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import com.tinatest.line_bot.dto.KingInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class LineBotService {

    private static final String ACCESS_TOKEN = "yXg2XLMonW2Odz62Jc8bOW5TW5aYEOwP4yAJmRk53dp9BAV4RC+nunO5IAqXMSCK/8z7WcYUe655wZpjl2FttMkH3KZ4CLMjZhqDy8snZCtkJCWFCHQcCMWiQSariPJiU5InRcF75xmFeowOdyAM7AdB04t89/1O/w1cDnyilFU=";

    private final LineMessagingClient client = LineMessagingClient
            .builder(ACCESS_TOKEN)
            .build();

    private static final SimpleDateFormat tFormat = new SimpleDateFormat("HH:mm:ss");

    private static Message message;
    private static String msg;

    @Autowired
    private LineageService lineageService;

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

    public BotApiResponse reply(MessageEvent<TextMessageContent> event) throws IOException, ExecutionException, InterruptedException {

        String receivedMessage = event.getMessage().getText();
        String replyToken = event.getReplyToken();

        if (receivedMessage.startsWith("push:")) {
            String pushMsg = StringUtils.substringAfter(receivedMessage, "push:");

            Broadcast broadcast = new Broadcast(new TextMessage(pushMsg));
            return client.broadcast(broadcast).get();
        }
        return client.replyMessage(new ReplyMessage(replyToken, new TextMessage(lineageService.getMsg(receivedMessage)))).get();
    }

    public void handleSticker(String replyToken, StickerMessageContent content) {
        client.replyMessage(new ReplyMessage(replyToken, new StickerMessage(
                        content.getPackageId(), content.getStickerId())));
    }

    @Scheduled(cron=  "0 */2 * ? * *")
    public void checkTask() {
        Date now = new Date();
        log.info("================> checkTask Date:" + now);
        boolean kingWillAppear = false;
        List<KingInfo> allKings = lineageService.getAllKings();
        message = null;
        msg = "";
        for (KingInfo kingInfo: allKings) {
            if (kingInfo.getNextAppear() != null) {
                int min = (int) ((kingInfo.getNextAppear().getTime() - now.getTime()) / (1000*60));
//                System.out.println(String.format("%s --> 下次出現時間: %s",kingInfo.getName(), sdFormat.format(kingInfo.getNextAppear())));
                if (min < 5 && min > 0) {
                    kingWillAppear = true;
                    String randomStr = kingInfo.isRandom() ? "隨" : "必";
                    msg = msg + String.format("[%s]-%s(%s)\n 重生時間: %s\n\n",
                            kingInfo.getName(), kingInfo.getLocation(), randomStr, tFormat.format(kingInfo.getNextAppear()));
                }
            }
        }
        if (kingWillAppear) {
            message = new TextMessage("<<出王通知>>\n" + msg);

            Broadcast broadcast = new Broadcast(message);
            client.broadcast(broadcast);
        }
    }

    public void replyText(String replyToken, String message) {
        client.replyMessage(new ReplyMessage(replyToken, new TextMessage(message)));
    }

}
