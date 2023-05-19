package com.tinatest.line_bot.service;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.Broadcast;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.action.URIAction;
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
import com.linecorp.bot.model.message.flex.component.Button;
import com.linecorp.bot.model.message.flex.component.Button.ButtonHeight;
import com.linecorp.bot.model.message.flex.component.Button.ButtonStyle;
import com.linecorp.bot.model.message.flex.component.Image;
import com.linecorp.bot.model.message.flex.component.Image.ImageAspectMode;
import com.linecorp.bot.model.message.flex.component.Image.ImageSize;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.component.Text.TextWeight;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.container.Bubble.BubbleSize;
import com.linecorp.bot.model.message.flex.container.BubbleStyles;
import com.linecorp.bot.model.message.flex.container.BubbleStyles.BlockStyle;
import com.linecorp.bot.model.message.flex.unit.FlexDirection;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import com.linecorp.bot.model.message.flex.unit.FlexMarginSize;
import com.linecorp.bot.model.response.BotApiResponse;
import com.tinatest.line_bot.model.common.Common;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
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

    @Autowired
    private LineNotifyService lineNotifyService;

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

    public boolean followEvent(String userId, String replyToken) {
        return initialCreate(userId, replyToken);
    }

    public boolean joinEvent(String senderId, String replyToken) {
       return initialCreate(senderId, replyToken);
    }

    private boolean initialCreate(String userId, String replyToken) {
        try {
            userService.createUser(userId);
            log.info("=============> 建立user:" + userId);
            String authLink = lineNotifyService.generateAuthLink(userId);
            log.info("-----authLink----->" + authLink);
            replyText(replyToken, String.format("Hi 我是天堂打王小幫手！請先點擊下方連結完成連動並繳費，完成後告知管理員代碼(%s)以啟用通知功能！%s\n %s ",
                    getCode(userId), Common.SMILE, authLink));
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public void reply(MessageEvent<TextMessageContent> event) throws InterruptedException {
        String userIdEvent = event.getSource().getUserId();
        String senderIdEvent = event.getSource().getSenderId();
        String receivedMessageOri = event.getMessage().getText();
        String receivedMessage = receivedMessageOri.toLowerCase();
        receivedMessage = receivedMessage.trim();
        String replyToken = event.getReplyToken();
        Message msg= new TextMessage(" ");

        if (receivedMessage.startsWith("push:")) {
            if (userService.isUserAdmin(userIdEvent)) {
                String pushMsg = StringUtils.substringAfter(receivedMessage, "push:").trim();
                Broadcast broadcast = new Broadcast(new TextMessage(pushMsg));
                client.broadcast(broadcast);
            } else {
                msg = new TextMessage("您無此權限！");
            }

        } else if (receivedMessage.startsWith("add admin")) {
            if (StringUtils.equals(senderIdEvent, "Ud62a356eedbea86f5231532bae38da4c")) {
                String code = StringUtils.substringAfter(receivedMessage, "add admin").trim();
                String userId = userService.addAdmin(code);
                if (userId != null) {
                    msg = new TextMessage("新增成功: " + code);
                    client.pushMessage(new PushMessage(userId, new TextMessage(Common.ALERT + "您已升級為管理員！\n" + Common.COMMAND_ADMIN)));
                } else {
                    msg = new TextMessage("新增失敗: " + code);
                }
            } else {
                msg = new TextMessage("您無此權限！");
            }
        } else if (receivedMessage.startsWith("activate")) {
            if (userService.isUserAdmin(userIdEvent)) {
                String code = StringUtils.substringAfter(receivedMessage, "activate").trim();
                String userId = userService.activateUser(code);
                if (userId != null) {
                    msg = new TextMessage("啟用成功: " + code);
                    client.pushMessage(new PushMessage(userId, new TextMessage(Common.ALERT + "小幫手啟用成功! 請使用help查看指令集!")));
                } else {
                    msg = new TextMessage("啟用失敗: " + code);
                }
            } else {
                msg = new TextMessage("您無此權限！");
            }
        } else if (receivedMessage.startsWith("cool")) {
            if (StringUtils.equals(userIdEvent, "Ud62a356eedbea86f5231532bae38da4c")) {
                msg = coolMsg();
            } else if (StringUtils.equals(userIdEvent, "U4d62b305e6b5c038adb81259e02f33e1")) {
                msg = coolMsgForEric();
            } else {
                msg = new TextMessage("您無此權限！");
            }
        } else if (receivedMessage.startsWith("add user")) {
            String result = userService.addUserProfile(userIdEvent, receivedMessageOri);
            msg = new TextMessage(result);

        } else if (receivedMessage.startsWith("show user")) {
            String result = userService.getUserInfoAllList();
            msg = new TextMessage(result);

        } else {
            if (userService.isUserApproved(senderIdEvent)) {
                msg = getMsg(receivedMessage, userIdEvent, senderIdEvent);
            } else {
                msg = new TextMessage(Common.ALERT + "抱歉！小幫手功能尚未啟用！");
            }
        }
        try {
            client.replyMessage(new ReplyMessage(replyToken, msg)).get();
        } catch (ExecutionException e) {
            log.error("receivedMessageOri  =====> " + receivedMessageOri);
        }
    }

    public Message getMsg(String receivedMessage, String userId, String senderId) {
        String keyword = String.format(receivedMessage);

        Message resultMsg = new TextMessage(" ");
        String messages = "";

        if (receivedMessage.startsWith("k ")) {
            keyword = "k";
        } else if (receivedMessage.startsWith("clear all")) {
            keyword = "clear all";
        } else if (receivedMessage.startsWith("clear ")) {
            keyword = "clear";
        } else if (receivedMessage.startsWith("add tag")) {
            keyword = "add tag";
        } else if (receivedMessage.startsWith("kr ")) {
            keyword = "kr";
        } else if (receivedMessage.startsWith("tag ")) {
            keyword = "tag";
        } else if (receivedMessage.startsWith("ky ")) {
            keyword = "ky";
        }

        switch (keyword) {
            case "hi":
            case "嗨":
            case "你好":
            case "hello":
            case "hey":
//                messages = Common.HI + "Hi Hi Hi 我是Tina小幫手";
                resultMsg = helloMsg();
                break;
            case "...":
                messages = "...";
                break;
            case "enable":
                messages = userService.updateUserNotify(senderId, true);
                break;
            case "disable":
                messages = userService.updateUserNotify(senderId, false);
                break;
            case "clear":
                messages = lineageService.command_clear(receivedMessage);
                break;
            case "clear all":
                if (userService.isUserAdmin(userId)) {
                    messages = lineageService.command_clearAll();
                } else {
                    messages = Common.ALERT + "您無此權限！";
                }
                break;
            case "add tag":
                messages = lineageService.command_addTag(receivedMessage);
                break;
            case "tag":
                messages = lineageService.command_tag(receivedMessage);
                break;
            case "k":
                messages = lineageService.command_k(receivedMessage);
                break;
            case "kr":
                messages = lineageService.command_kr(receivedMessage);
                break;
            case "ky":
                messages = lineageService.command_ky(receivedMessage);
                break;
            case "kb all":
                messages = lineageService.command_kbAll();
                break;
            case "kb":
                resultMsg = lineageService.command_kb();
                break;
            case "help":
                messages = Common.COMMAND;
                break;
            default:
                messages = lineageService.command_default(receivedMessage);
                break;
        }
        if (StringUtils.isNoneEmpty(messages)) {
            return new TextMessage(messages);
        }
        return resultMsg;
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

        Bubble bubble = Bubble.builder().direction(direction).body(body).styles(styles).build();
        FlexMessage flexMessage = FlexMessage.builder().altText("test").contents(bubble).build();
        client.pushMessage(new PushMessage("Ud62a356eedbea86f5231532bae38da4c", flexMessage));
    }

    public FlexMessage helloMsg() {

        FlexDirection direction = FlexDirection.LTR;

        BubbleStyles styles = BubbleStyles.builder().footer(BlockStyle.builder().backgroundColor("#639594").build()).build();

        Image hero = Image.builder().url(URI.create("https://i.pinimg.com/originals/39/5b/9d/395b9d6aa2f79c77f86bbdc9840e0248.jpg"))
                .size(ImageSize.FULL_WIDTH).aspectRatio(25, 19).build();

        Box header = Box.builder()
                .layout(FlexLayout.VERTICAL).build();

        Box body = Box.builder()
                .layout(FlexLayout.VERTICAL)
                .contents(Text.builder().text("Hi！ 我是打王小幫手 \uD83D\uDC99  ").weight(TextWeight.BOLD).size("xl").build(),
                        Text.builder().text("讓你不會錯過打王時間喔~").margin(FlexMarginSize.XL).build())
                .build();

        URIAction action = new URIAction("Tina's IG", URI.create("https://www.instagram.com/tinayenxx/"), null);

        Box footer = Box.builder()
                .layout(FlexLayout.VERTICAL)
                .content(Button.builder().height(ButtonHeight.MEDIUM).style(ButtonStyle.LINK).action(action).build())
                .backgroundColor("#ffffb3")
                .build();
        BubbleSize size;

        Bubble bubble = Bubble.builder().direction(direction).hero(hero).body(body).build();
        return FlexMessage.builder().altText("Welcome Message").contents(bubble).build();
    }

    public FlexMessage coolMsgForEric() {

        FlexDirection direction = FlexDirection.LTR;

        BubbleStyles styles = BubbleStyles.builder().footer(BlockStyle.builder().backgroundColor("#639594").build()).build();

        Image hero = Image.builder().url(URI.create("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQezre2Ubu4g8XpFprmNfyiNUL0yUAmeQNYXw&usqp=CAU"))
                .aspectMode(ImageAspectMode.Cover).aspectRatio(61, 20).size(ImageSize.FULL_WIDTH).build();


        Image img1 = Image.builder().url(URI.create("https://miro.medium.com/max/1400/1*0pVt2nv81SAUIWoP7OcHow.jpeg")).build();

        Image img2 = Image.builder().url(URI.create("https://i.pinimg.com/564x/bc/c8/86/bcc886a85593c2cd4b1944494f74a6c7.jpg")).build();


        Box header = Box.builder()
                .layout(FlexLayout.VERTICAL).build();


        Box images = Box.builder()
                .layout(FlexLayout.HORIZONTAL)
                .contents(img1, img2)
                .build();

        Box body = Box.builder()
                .layout(FlexLayout.VERTICAL)
                .contents(Text.builder().text("Hey Eric！\uD83D\uDC99 ").weight(TextWeight.BOLD).size("xl").build(),
                        Text.builder().text("✨ 酷酷的東西 ✨").margin(FlexMarginSize.XL).build(),
                        images)
                .build();

        URIAction action = new URIAction("Fantasy Baseball", URI.create("https://baseball.fantasysports.yahoo.com/"), null);
        URIAction action2 = new URIAction("Fantasy Football", URI.create("https://football.fantasysports.yahoo.com/"), null);

        Box footer = Box.builder()
                .layout(FlexLayout.VERTICAL)
                .contents(
                        Button.builder().height(ButtonHeight.SMALL).style(ButtonStyle.PRIMARY).action(action).color("#d8cea6").build(),
                        Button.builder().height(ButtonHeight.SMALL).style(ButtonStyle.PRIMARY).action(action2).color("#d8cea6").build()
                )
                .backgroundColor("#778ca6")
                .spacing(FlexMarginSize.SM)
                .build();
        BubbleSize size;

        Bubble bubble = Bubble.builder().direction(direction).hero(hero).body(body).footer(footer).build();
        return FlexMessage.builder().altText("Welcome Message").contents(bubble).build();
    }

    public FlexMessage coolMsg() {

        FlexDirection direction = FlexDirection.LTR;

        BubbleStyles styles = BubbleStyles.builder().footer(BlockStyle.builder().backgroundColor("#639594").build()).build();

        Image hero = Image.builder().url(URI.create("https://i.pinimg.com/originals/39/5b/9d/395b9d6aa2f79c77f86bbdc9840e0248.jpg"))
                .size(ImageSize.FULL_WIDTH).aspectRatio(25, 19).build();

        Box header = Box.builder()
                .layout(FlexLayout.VERTICAL).build();

        Box body = Box.builder()
                .layout(FlexLayout.VERTICAL)
                .contents(Text.builder().text("酷酷的東西 \uD83D\uDC99  \uD83D\uDE0F").weight(TextWeight.BOLD).size("xl").build(),
                        Text.builder().text("✨ 網拍逛起來 ✨").margin(FlexMarginSize.XL).build())
                .build();

        URIAction action = new URIAction("Tina's IG", URI.create("https://www.instagram.com/tinayenxx/"), null);
        URIAction action2 = new URIAction("SWEESA", URI.create("https://www.sweesa.com/Shop/"), null);
        URIAction action3 = new URIAction("WEAR to EAT", URI.create("https://www.weartoeat.com.tw/"), null);

        Box footer = Box.builder()
                .layout(FlexLayout.VERTICAL)
                .contents(Button.builder().height(ButtonHeight.SMALL).style(ButtonStyle.PRIMARY).action(action).color("#5F7197").build(),
                        Button.builder().height(ButtonHeight.SMALL).style(ButtonStyle.PRIMARY).action(action2).color("#5F7197").build(),
                        Button.builder().height(ButtonHeight.SMALL).style(ButtonStyle.PRIMARY).action(action3).color("#5F7197").build())
                .backgroundColor("#ffffb3")
                .spacing(FlexMarginSize.SM)
                .build();
        BubbleSize size;

        Bubble bubble = Bubble.builder().direction(direction).hero(hero).body(body).footer(footer).build();
        return FlexMessage.builder().altText("Welcome Message").contents(bubble).build();
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

    private String getCode(String id) {  // 用於啟用的代碼
        return StringUtils.substring(id, 1, 7);
    }

}
