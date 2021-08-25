package com.tinatest.line_bot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Service
public class ScheduledTaskService {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

//    @Scheduled(cron="/* cronの記述 */", zone = "Asia/Tokyo")
//    public void executeAlarm() {
//        try {
//            //プッシュする処理を呼び出す
//            pushAlarm();
//        } catch (URISyntaxException e) {
//            log.error("{}", e);
//        }
//        log.info("cron executed : " + sdf.format(new Date()));
//    }
}