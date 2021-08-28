package com.tinatest.line_bot.service;

import com.linecorp.bot.model.Broadcast;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.tinatest.line_bot.dto.KingInfo;
import com.tinatest.line_bot.model.common.Common;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
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

    @Scheduled(cron=  "0 */50 * ? * *")
    public void updateKingInfoList() {
        log.warn("================> Query DB to update KingInfoList");
        lineageService.updateKingInfoList();
    }

    @Scheduled(cron=  "0 */3 * ? * *")
    public void checkTask() {
        Date now = new Date();
//        log.info("================> checkTask Date:" + now);
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
                    msg = msg + String.format("\n\n [%s]-%s(%s)\n 重生時間: %s",
                            kingInfo.getName(), kingInfo.getLocation(), randomStr, tFormat.format(kingInfo.getNextAppear()));
                    kingWillAppear = true;
                } else if (min < -30) {
                    needUpdates.add(kingInfo.getId());
                }
            }
        }
        if (needUpdates.size() > 0) {
            lineageService.updateNextAppear(needUpdates, now);
        }
        if (kingWillAppear) {
            message = new TextMessage(FIRE + "出王通知" + FIRE +"" + msg);
            lineBotService.pushMsg(lineageService.getUserInfoList(), message);
//            Broadcast broadcast = new Broadcast(message);
//            client.broadcast(broadcast);
        }
    }
}