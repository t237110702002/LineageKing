package com.tinatest.line_bot.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tinatest.line_bot.dto.KingInfo;
import com.tinatest.line_bot.service.LineageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;
import java.util.Arrays;
import java.util.Map;

@Controller
@Slf4j
public class LineageController {


    @Autowired
    private LineageService lineageService;

    @GetMapping(value = "/page")
    public String mainPage(HttpServletRequest request, HttpServletResponse response) {

//        request.setAttribute("name", Arrays.asList("JJ", "王力宏"));
        return "kingInfo";
    }

    @PostMapping(value = "/create")
    @ResponseBody
    public String inputInfo(HttpServletRequest req) {
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String location = req.getParameter("location");
        String period = req.getParameter("period");
        String random = req.getParameter("random");
        String lastAppear = req.getParameter("lastAppear");

        try {
            lineageService.createData(id, name, location, period, lastAppear, random);
            return "OK";
        } catch (Exception e) {
            return "FAIL";
        }
    }

    @GetMapping(value = "/kinglist")
    @Produces("application/json")
    @ResponseBody
    public String kingList(HttpServletRequest req) {
        Map<String, KingInfo> database = lineageService.getDatabase();
        Gson gson = new Gson();
        String json = gson.toJson(database);
        return json;
    }
}
