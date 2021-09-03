package com.tinatest.line_bot.service;

import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.tinatest.line_bot.dto.KingInfo;
import com.tinatest.line_bot.model.common.Common;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ScheduledTaskService {

    private static final SimpleDateFormat tFormat = new SimpleDateFormat("HH:mm:ss");
    private static final String FIRE = String.valueOf(Character.toChars(Integer.decode("0x1000A4")));
    private static List<String> needUpdates;
    private static Message message;
    private static String msg;

    @Autowired
    private LineageService lineageService;

    @Autowired
    private LineBotService lineBotService;

    @Autowired
    private UserService userService;

    @Autowired
    private LineNotifyService lineNotifyService;

    @Value("${line.bot.push.enable:false}")
    private String pushEnable;

    @PostConstruct
    public void init(){
        log.error(String.format(">>>>>>>>>> 推播功能 : %s <<<<<<<<<<", (BooleanUtils.toBoolean(pushEnable) ? "開啟" : "關閉")));
        updateKingInfoList();
        checkTask();
        test();
    }

    @Scheduled(cron=  "0 */50 * ? * *")
    public void updateKingInfoList() {
        log.warn("================> Query DB to update KingInfoList");
        lineageService.updateKingInfoList();
    }

    @Scheduled(cron=  "0 */10 * ? * *")
    public void test() {
        if (!CollectionUtils.isEmpty(userService.getUserNotifyList())) {
            lineNotifyService.sendMessages(userService.getUserNotifyList(), "TEST 每10分鐘一次的推播~", true);
        } else {
            log.info("[TEST] 沒有使用者需要通知 ! ");
        }

    }

    @Scheduled(cron=  "0 */3 * ? * *")
    public void checkTask() {
        Date now = new Date();

        if (CollectionUtils.isEmpty(userService.getUserNotifyList())) {
            log.info("沒有使用者需要通知 ! ================> checkTask Date:" + now);
            return;
        }
        boolean kingWillAppear = false;
        message = null;
        msg = "";
        needUpdates = new ArrayList<>();
        for (KingInfo kingInfo: lineageService.getKingInfoList()) {
            if (kingInfo.getNextAppear() != null) {
                int min = (int) ((kingInfo.getNextAppear().getTime() - now.getTime()) / (1000*60));
                if (min > 0 && min < 5) {
                    log.info(String.format("%s --> 下次出現時間: %s",kingInfo.getName(), Common.sdFormat.format(kingInfo.getNextAppear())));
                    String randomStr = kingInfo.isRandom() ? "隨" : "必";
                    String missStr = getMissStr(kingInfo.getMissCount());
                    msg = msg + String.format("\n\n%s[%s]-%s(%s)\n%s%s重生時間 : %s", Common.DEVIL,
                            kingInfo.getName(), kingInfo.getLocation(), randomStr, missStr,
                            Common.CLOCK, tFormat.format(kingInfo.getNextAppear()));
                    kingWillAppear = true;
                } else if (min < -30) {
                    needUpdates.add(kingInfo.getId());
                }
            }
        }
        if (needUpdates.size() > 0) {
            lineageService.updateNextAppear(needUpdates, now);
        }
        if (kingWillAppear && BooleanUtils.toBoolean(pushEnable)) {
            goPushMessage();
        }
    }

    private String getMissStr(Integer missCount) {
        return (missCount == null || missCount == 0) ? "" : Common.CRY + "您已錯過 : " + missCount + "次了\n";
    }

    private void goPushMessage() {
        // 推播訊息 兩種選擇: 1. 用LINE Bot push message   2. 使用LINE Notify
//            1.
//            message = new TextMessage(FIRE + "出王通知" + msg);
//            lineBotService.pushMsg(userService.getUserInfoList(), message);
//            2.
        lineNotifyService.sendMessages(userService.getUserInfoList(), FIRE + "出王通知" + msg, true);
    }
}