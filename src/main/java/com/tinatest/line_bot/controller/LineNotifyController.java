package com.tinatest.line_bot.controller;

import com.tinatest.line_bot.service.LineNotifyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LineNotifyController {

    @Autowired
    private LineNotifyService lineNotifyService;

    @GetMapping(value = "/callback/notify")
    @ResponseBody
    public String notifyCallback(HttpServletRequest req, @RequestParam String code, @RequestParam String state) {

        assert(StringUtils.equals(req.getHeader("referer"), "https://notify-bot.line.me/"));
        lineNotifyService.callBack(code, state);
        return  "恭喜完成 LINE Notify 連動！請關閉此視窗。";
    }

    @GetMapping(value = "/notify/auth")
    @ResponseBody
    public String notifyAuth(HttpServletRequest req, @RequestParam String userId) {

        String authLink = lineNotifyService.generateAuthLink(userId);

        return  authLink;
    }

    @GetMapping(value = "/notify/test")
    @ResponseBody
    public void notifyTest(HttpServletRequest req) {
        lineNotifyService.sendMessage("CBMtxYQgXGFHGabgdTAu7hakElQKf3LCoCHAB85VYIl", "test", true);
    }

}
