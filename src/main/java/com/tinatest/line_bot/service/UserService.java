package com.tinatest.line_bot.service;

import com.linecorp.bot.client.LineMessagingClient;
import com.tinatest.line_bot.dto.LineUserProfile;
import com.tinatest.line_bot.model.UserInfoEntity;
import com.tinatest.line_bot.model.UserInfoRepository;
import com.tinatest.line_bot.model.common.Common;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private static List<String> userInfoList;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private LineMessagingClient client;

    @Autowired
    private LineBotApiService lineBotApiService;

    @PostConstruct
    public void init() {
        updateUserInfoList();
    }

    public String updateUserNotify(String userId, boolean notify) {

        Optional<UserInfoEntity> userInfo = userInfoRepository.findById(userId);
        UserInfoEntity userInfoEntity;
        if (userInfo.isPresent()) {
            userInfoEntity = userInfo.get();
        } else {
            userInfoEntity = new UserInfoEntity();
            userInfoEntity.setUserId(userId);
            userInfoEntity.setApprove(false);
        }

        if (!userInfoEntity.getApprove()) {
            userInfoEntity.setUpdateDate(new Date());
            userInfoRepository.save(userInfoEntity);
            updateUserInfoList();
            return Common.CRY + " 抱歉! 您尚未繳費，通知功能無法開啟!";
        } else {
            userInfoEntity.setNotify(notify);
            userInfoEntity.setUpdateDate(new Date());
            userInfoRepository.save(userInfoEntity);
            updateUserInfoList();
            return notify ? Common.ALERT + "通知已開啟" : Common.ALERT + "通知已關閉";
        }
    }

    public String activateUser(String code) {

        UserInfoEntity userInfoEntity = userInfoRepository.findByUserIdLike(code);
        if (userInfoEntity == null) {
            log.warn("查無此user:" + code);
            return null;
        }
        try {
            userInfoEntity.setApprove(true);
            userInfoEntity.setNotify(false);
            userInfoEntity.setUpdateDate(new Date());
            userInfoRepository.save(userInfoEntity);
            updateUserInfoList();
            return userInfoEntity.getUserId();
        } catch (Exception e) {
            log.error("啟用時發生錯誤: " + e.getMessage());
            return null;
        }
    }

    public boolean updateUserToken(String userCode, String accessToken) {

        UserInfoEntity userInfoEntity = userInfoRepository.findByUserIdLike(userCode);
        if (userInfoEntity == null) {
            log.warn("查無此user:" + userCode);
            return false;
        }
        try {
            userInfoEntity.setAccessToken(accessToken);
            userInfoEntity.setUpdateDate(new Date());
            userInfoRepository.save(userInfoEntity);
            updateUserInfoList();
            return true;
        } catch (Exception e) {
            log.error("發生錯誤: " + e.getMessage());
            return false;
        }
    }

    public String addAdmin(String code) {

        UserInfoEntity userInfoEntity = userInfoRepository.findByUserIdLike(code);
        if (userInfoEntity == null) {
            log.warn("查無此user:" + code);
            return null;
        }
        try {
            userInfoEntity.setApprove(true);
            userInfoEntity.setAdmin(true);
            userInfoEntity.setUpdateDate(new Date());
            userInfoRepository.save(userInfoEntity);
            updateUserInfoList();
            return userInfoEntity.getUserId();
        } catch (Exception e) {
            log.error("新增管理員發生錯誤: " + e.getMessage());
            return null;
        }
    }

    public boolean isUserApproved(String userId) {  // 判斷使用者或群組是否已繳費
        Optional<UserInfoEntity> userInfo = userInfoRepository.findById(userId);
        if (userInfo.isPresent()) {
            return userInfo.get().getApprove();
        }
        return false;
    }

    public void createUser(String userId) {
        UserInfoEntity entity = userInfoRepository.findByUserId(userId);
        if (entity == null) {
            UserInfoEntity userInfoEntity = new UserInfoEntity();
            userInfoEntity.setUserId(userId);
            userInfoEntity.setNotify(false);
            userInfoEntity.setApprove(false);
            userInfoEntity.setAdmin(false);
            userInfoEntity.setUpdateDate(new Date());
            userInfoRepository.save(userInfoEntity);
            updateUserInfoList();
        }
    }

    public String addUserProfile(String userId, String receivedMessage) {
        String[] strings = StringUtils.split(receivedMessage, " ");
        if (strings.length != 4) {
            return Common.ALERT + "指令錯誤，請依照此格式 add user [LINE名稱] [遊戲名稱]";
        }
        LineUserProfile userProfile = lineBotApiService.getUserProfile(userId);
        if (!StringUtils.equals(userProfile.getDisplayName(), strings[2])) {
            return Common.ALERT + "非本人無法新增名冊！";
        }
        UserInfoEntity userInfoEntity = userInfoRepository.findByUserId(userId);
        if (userInfoEntity == null) {
            userInfoEntity = new UserInfoEntity();
            userInfoEntity.setUserId(userId);
            userInfoEntity.setNotify(false);
            userInfoEntity.setApprove(false);
            userInfoEntity.setAdmin(false);

        } else {
            userInfoEntity.setUserLineId(userProfile.getDisplayName());
            userInfoEntity.setUserGameId(strings[3]);
        }
        userInfoEntity.setUpdateDate(new Date());
        userInfoRepository.save(userInfoEntity);
        updateUserInfoList();
        return Common.ALERT + "新增成功！\nLINE名稱 : " + userProfile.getDisplayName() + "\n遊戲名稱 : " + strings[3];

    }

    public List<String> getUserNotifyList() {
        List<UserInfoEntity> userInfoEntities = userInfoRepository.findByNotifyTrueAndApproveTrueAndAccessTokenIsNotNull();
        return userInfoEntities.stream().map(UserInfoEntity::getAccessToken).collect(Collectors.toList());
    }

    public String getUserInfoAllList() {
        String msg ="【群組名冊】";
        List<UserInfoEntity> all = userInfoRepository.findByUserLineIdIsNotNull();
        if (CollectionUtils.isEmpty(all)) {
            return Common.ALERT + "無使用者名冊";
        }
        for (UserInfoEntity user : all) {
            msg = msg + String.format("\n" + Common.BLING + "%s --> %s", user.getUserLineId(), user.getUserGameId());
        }
        return msg;
    }

    public void updateUserInfoList() {
        userInfoList = getUserNotifyList();
    }


    public List<String> getUserInfoList() {
        return userInfoList;
    }

//    public boolean isUserAdmin(String userId) {
//        List<String> adminList = Arrays.asList("Ud62a356eedbea86f5231532bae38da4c");
//        return adminList.contains(userId);
//    }

    public boolean isUserAdmin(String userId) {
        if (StringUtils.equalsIgnoreCase(userId, "Ud62a356eedbea86f5231532bae38da4c")) {
            return true;
        }
        UserInfoEntity userInfoEntity = userInfoRepository.findByAdminAndUserId(true, userId);
        if (userInfoEntity != null) {
            return true;
        }
        return false;
    }
}
