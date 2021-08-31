package com.tinatest.line_bot.controller;

import com.google.gson.Gson;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.Message;
import com.tinatest.line_bot.dto.KingInfo;
import com.tinatest.line_bot.dto.KingInfoRequest;
import com.tinatest.line_bot.dto.UpdateKingRequest;
import com.tinatest.line_bot.service.LineageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;
import java.util.List;

@Controller
@Slf4j
public class LineageController {

    @Value("${admin.token}")
    private String adminToken;

    @Autowired
    private LineageService lineageService;

    @Autowired
    private LineMessagingClient client;

    @GetMapping(value = "/page")
    public String mainPage(HttpServletRequest request, HttpServletResponse response) {
        return "kingInfo";
    }

    @PostMapping(value = "/create")
    @ResponseBody
    public String inputInfo(HttpServletRequest req) {
        return null;
//        String id = req.getParameter("kingId");
//        String name = req.getParameter("name");
//        String location = req.getParameter("location");
//        String period = req.getParameter("period");
//        String random = req.getParameter("random");
//        String lastAppear = req.getParameter("lastAppear");
//
//        try {
//            String result = lineageService.createData(id, name, location, period, lastAppear, random);
//            return result;
//        } catch (Exception e) {
//            return "FAIL";
//        }
    }

    @GetMapping(value = "/kinglist")
    @Produces("application/json")
    @ResponseBody
    public ResponseEntity kingList(HttpServletRequest req) {
        List<KingInfo> allKings = lineageService.getAllKings();
        Gson gson = new Gson();
        String json = gson.toJson(allKings);
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType("application/json"))
                .body(json);
    }

    @PostMapping(value = "/update")
    @Produces("application/json")
    @ResponseBody
    public String updateList(@RequestBody UpdateKingRequest request) {
        if (!StringUtils.equals(request.getToken(), adminToken)) {
            return "AUTH FAIL";
        }
        return lineageService.goUpdate(request.getKingInfoList());
    }

    @PostMapping(value = "/test/command")
    @Produces("application/json")
    @ResponseBody
    public String command(HttpServletRequest req, @RequestParam String token, @RequestParam String command) {
        if (!token.equals(adminToken)) {
            return "AUTH FAIL";
        }
        Message msg = lineageService.getMsg(command);
        client.pushMessage(new PushMessage("Ud62a356eedbea86f5231532bae38da4c", msg));
        return msg.toString();
    }
}
