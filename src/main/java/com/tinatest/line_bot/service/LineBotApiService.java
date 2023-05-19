package com.tinatest.line_bot.service;

import com.google.gson.Gson;
import com.tinatest.line_bot.dto.LineUserProfile;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import java.net.URL;

import java.util.stream.Collectors;


@Service
@Slf4j
public class LineBotApiService {

    @Value("${line.bot.channelToken}")
    private String channelAccessToken;

    public LineUserProfile getUserProfile(String userId) {
        String resp = getConnect(userId);
        return new Gson().fromJson(resp, LineUserProfile.class);
    }

    public String getConnect(String userId) {
        String result = null;
        try {
            URL url = new URL("https://api.line.me/v2/bot/profile/" + userId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.addRequestProperty("Authorization", "Bearer " + channelAccessToken);
            connection.setDoOutput(true);
            connection.connect();
            int status = connection.getResponseCode();
            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    return sb.toString();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
