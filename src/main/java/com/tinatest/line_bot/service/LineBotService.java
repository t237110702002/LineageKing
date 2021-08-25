package com.tinatest.line_bot.service;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class LineBotService {

    private static final String ACCESS_TOKEN = "yXg2XLMonW2Odz62Jc8bOW5TW5aYEOwP4yAJmRk53dp9BAV4RC+nunO5IAqXMSCK/8z7WcYUe655wZpjl2FttMkH3KZ4CLMjZhqDy8snZCtkJCWFCHQcCMWiQSariPJiU5InRcF75xmFeowOdyAM7AdB04t89/1O/w1cDnyilFU=";


    public void pushMessage(String userId) {
        final LineMessagingClient client = LineMessagingClient
                .builder(ACCESS_TOKEN)
                .build();

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
}
