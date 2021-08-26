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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class LineBotService {

    private static final String ACCESS_TOKEN = "yXg2XLMonW2Odz62Jc8bOW5TW5aYEOwP4yAJmRk53dp9BAV4RC+nunO5IAqXMSCK/8z7WcYUe655wZpjl2FttMkH3KZ4CLMjZhqDy8snZCtkJCWFCHQcCMWiQSariPJiU5InRcF75xmFeowOdyAM7AdB04t89/1O/w1cDnyilFU=";

    private final LineMessagingClient client = LineMessagingClient
            .builder(ACCESS_TOKEN)
            .build();

    private final SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
        Map<String, KingInfo> database = lineageService.getDatabase();
        List<Message> messages = null;
        switch (receivedMessage.toLowerCase()) {
            case "hi":
            case "嗨":
            case "你好":
            case "hello":
                messages = Arrays.asList(new TextMessage("HI HI HI 我是Tina小幫手"));
                break;
            case "kb all":
                String msg = "";
                for (KingInfo kingInfo: database.values()) {
                    String randomStr = kingInfo.isRandom() ? "隨" : "必";
                    msg = msg + String.format("[%s]-%s(%s) 死亡時間:%s 重生時間:%s",
                                kingInfo.getName(), kingInfo.getLocation(), randomStr, sdFormat.format(kingInfo.getLastAppear()), sdFormat.format(kingInfo.getNextAppear()));
                }
                messages.add(new TextMessage(msg));
                break;
            default:
                for (KingInfo kingInfo: database.values()) {
                    if (receivedMessage.equals(kingInfo.getName())) {
                        String randomStr = kingInfo.isRandom() ? "隨" : "必";
                        messages.add(new TextMessage(String.format("[%s]-%s(%s) \n死亡時間:%s\n 重生時間:%s",
                                kingInfo.getName(), kingInfo.getLocation(), randomStr, sdFormat.format(kingInfo.getLastAppear()), sdFormat.format(kingInfo.getNextAppear()))));
                        break;
                    }

                }
                break;
        }
        return client.replyMessage(new ReplyMessage(replyToken, messages)).get();
    }

    public void handleSticker(String replyToken, StickerMessageContent content) {

        client.replyMessage(new ReplyMessage(replyToken, new StickerMessage(
                        content.getPackageId(), content.getStickerId())));
    }

//    public BotApiResponse push(MessageEvent<TextMessageContent> event) throws IOException, ExecutionException, InterruptedException {
//
//        String[] userIds = {"U848d0fb8269d111a96875ae3cb365ba6"};
//
//        String receivedMessage = event.getMessage().getText();
//        String replyToken = event.getReplyToken();
//
//    }
    @Scheduled(cron=  "0 */1 * ? * *")
    public void checkTask() {
        Date now = new Date();

        System.out.println("================> checkTask Date:" + now);
        boolean kingWillAppear = false;

        Map<String, KingInfo> database = lineageService.getDatabase();
        List<Message> pushMessages = new ArrayList<>();
        SimpleDateFormat tFormat = new SimpleDateFormat("HH:mm:ss");
        for (KingInfo kingInfo: database.values()) {
            if (kingInfo.getNextAppear() != null) {
                int min = (int) ((kingInfo.getNextAppear().getTime() - now.getTime()) / (1000*60));

//                System.out.println(String.format("%s --> 下次出現時間: %s",kingInfo.getName(), sdFormat.format(kingInfo.getNextAppear())));

                if (min < 5 && min > 0) {
                    kingWillAppear = true;
                    String msg = String.format("!!!提醒!!! [%s]-%s 預估出現時間: %s",
                            kingInfo.getName(), kingInfo.getLocation(), tFormat.format(kingInfo.getNextAppear()));
                    pushMessages.add(new TextMessage(msg));
                }
            }
        }

        if (kingWillAppear) {
            Broadcast broadcast = new Broadcast(pushMessages);
            client.broadcast(broadcast);
        }
    }

    public void replyText(String replyToken, String message) {
        client.replyMessage(new ReplyMessage(replyToken, new TextMessage(message)));
    }

}
