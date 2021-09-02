package com.tinatest.line_bot.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "user_info")
public class UserInfoEntity {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "notify")
    private Boolean notify;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "approve")  //是否可以啟用功能
    private Boolean approve;

    @Column(name = "admin")  //是否為管理員
    private Boolean admin;

    @Column(name = "user_line_id") //使用者LINE的id
    private String userLineId;

    @Column(name = "user_game_id") //使用者遊戲中的id
    private String userGameId;

    @Column(name = "access_token") //LINE Notify access_token
    private String accessToken;

    @Column(name = "update_date")
    private Date updateDate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getNotify() {
        return notify;
    }

    public void setNotify(Boolean notify) {
        this.notify = notify;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getApprove() {
        return approve;
    }

    public void setApprove(Boolean approve) {
        this.approve = approve;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public String getUserLineId() {
        return userLineId;
    }

    public void setUserLineId(String userLineId) {
        this.userLineId = userLineId;
    }

    public String getUserGameId() {
        return userGameId;
    }

    public void setUserGameId(String userGameId) {
        this.userGameId = userGameId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
