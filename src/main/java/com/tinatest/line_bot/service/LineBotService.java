package com.tinatest.line_bot.service;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.Broadcast;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class LineBotService {

    private static final String ACCESS_TOKEN = "yXg2XLMonW2Odz62Jc8bOW5TW5aYEOwP4yAJmRk53dp9BAV4RC+nunO5IAqXMSCK/8z7WcYUe655wZpjl2FttMkH3KZ4CLMjZhqDy8snZCtkJCWFCHQcCMWiQSariPJiU5InRcF75xmFeowOdyAM7AdB04t89/1O/w1cDnyilFU=";

    private final LineMessagingClient client = LineMessagingClient
            .builder(ACCESS_TOKEN)
            .build();

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

        if (receivedMessage.equals("push")) {
            Broadcast broadcast = new Broadcast(new TextMessage("推播測試喔喔喔喔喔喔"));
            return client.broadcast(broadcast).get();
        }


        List<Message> messages = null;
        switch (receivedMessage) {
            case "Hi":
            case "hi":
            case "嗨":
            case "你好":
                messages = Arrays.asList(new TextMessage("HI HI HI 我是Tina小幫手"));
                break;
            default:
                messages = Arrays.asList(new TextMessage("測試預設訊息"));
                break;
        }
        return client.replyMessage(new ReplyMessage(replyToken, messages)).get();
    }

//    public BotApiResponse push(MessageEvent<TextMessageContent> event) throws IOException, ExecutionException, InterruptedException {
//
//        String[] userIds = {"U848d0fb8269d111a96875ae3cb365ba6"};
//
//        String receivedMessage = event.getMessage().getText();
//        String replyToken = event.getReplyToken();
//
//    }

}
