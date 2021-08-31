package com.tinatest.line_bot.service;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.Broadcast;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.JoinEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.StickerMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.StickerMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.container.Bubble.BubbleSize;
import com.linecorp.bot.model.message.flex.container.BubbleStyles;
import com.linecorp.bot.model.message.flex.container.BubbleStyles.BlockStyle;
import com.linecorp.bot.model.message.flex.unit.FlexDirection;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import com.linecorp.bot.model.response.BotApiResponse;
import com.tinatest.line_bot.model.common.Common;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;
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

//    @PostConstruct
//    public void init(){
////        pushPicMsg();
//    }

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
        Message msg;
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
                msg = new TextMessage("啟用成功: " + code);
                client.pushMessage(new PushMessage(code, new TextMessage(Common.ALERT + "小幫手啟用成功! 請使用help查看指令集!")));
            } else {
                msg = new TextMessage("啟用失敗: " + code);
            }
        } else if (StringUtils.equalsIgnoreCase(receivedMessage, "enable")) {
            msg = new TextMessage(userService.updateUserNotify(event.getSource().getSenderId(), true));
        } else if (StringUtils.equalsIgnoreCase(receivedMessage, "disable")) {
            msg = new TextMessage(userService.updateUserNotify(event.getSource().getSenderId(), false));
        } else {
            msg = lineageService.getMsg(receivedMessage);
        }
//        return client.replyMessage(new ReplyMessage(replyToken, msg)).get();
        return client.pushMessage(new PushMessage(replyToken, msg)).get();
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


    public void pushPicMsg() {

//        URI uri = URI.create("https://img.technews.tw/wp-content/uploads/2019/08/21165024/Spider-Man-624x375.jpg");
//        ButtonsTemplate buttonsTemplate = new ButtonsTemplate(uri, "出王重生表", "123\n456\n789", null);
        FlexDirection direction = FlexDirection.LTR;

        BubbleStyles styles = BubbleStyles.builder().footer(BlockStyle.builder().backgroundColor("#639594").build()).build();

        Box header = Box.builder().layout(FlexLayout.BASELINE).content(Text.builder().text("出王重生表").build()).backgroundColor("#639594").build();

        Box body = Box.builder().layout(FlexLayout.VERTICAL).contents(Text.builder().text("15:25:00 王的名稱A").build(), Text.builder().text("21:36:40 王的名稱B").build()).build();

        Box footer = Box.builder().layout(FlexLayout.VERTICAL).build();

        BubbleSize size;

        Action action;

        Bubble bubble = Bubble.builder().direction(direction).header(header).body(body).styles(styles).build();
        FlexMessage flexMessage = FlexMessage.builder().altText("test").contents(bubble).build();
        client.pushMessage(new PushMessage("Ud62a356eedbea86f5231532bae38da4c", flexMessage));
    }

    public FlexMessage pushPicMsg(String kingName, String lastAppear, String nextAppear) {

        FlexDirection direction = FlexDirection.LTR;

        BubbleStyles styles = BubbleStyles.builder().footer(BlockStyle.builder().backgroundColor("#639594").build()).build();

        Box header = Box.builder()
                .layout(FlexLayout.VERTICAL)
                .content(Box.builder()
                        .layout(FlexLayout.HORIZONTAL)
                        .contents(Text.builder().text("擊殺").color("#FFFFFF").build(), Text.builder().text(kingName).color("#FFFFFF").build())
                        .build())
                .backgroundColor("#639594").build();

        Box body = Box.builder()
                .layout(FlexLayout.VERTICAL)
                .contents(Text.builder().text(lastAppear).build(), Text.builder().text(nextAppear).build())
                .build();

        Box footer = Box.builder().layout(FlexLayout.VERTICAL).build();
        BubbleSize size;
        Action action;

        Bubble bubble = Bubble.builder().direction(direction).header(header).body(body).styles(styles).build();
       return FlexMessage.builder().altText("test").contents(bubble).build();

//        client.pushMessage(new PushMessage("Ud62a356eedbea86f5231532bae38da4c", flexMessage));
    }

}
