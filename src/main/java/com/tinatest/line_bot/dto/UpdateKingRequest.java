package com.tinatest.line_bot.dto;

import java.util.List;

public class UpdateKingRequest {

    private String token;
    private List<KingInfoRequest> kingInfoList;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<KingInfoRequest> getKingInfoList() {
        return kingInfoList;
    }

    public void setKingInfoList(List<KingInfoRequest> kingInfoList) {
        this.kingInfoList = kingInfoList;
    }
}
