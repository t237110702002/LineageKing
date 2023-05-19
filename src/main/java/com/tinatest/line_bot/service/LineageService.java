package com.tinatest.line_bot.service;

import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.FlexComponent;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.component.Text.TextWeight;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.container.Bubble.BubbleSize;
import com.linecorp.bot.model.message.flex.container.BubbleStyles;
import com.linecorp.bot.model.message.flex.container.BubbleStyles.BlockStyle;
import com.linecorp.bot.model.message.flex.unit.FlexAlign;
import com.linecorp.bot.model.message.flex.unit.FlexDirection;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import com.tinatest.line_bot.dto.KingInfo;
import com.tinatest.line_bot.dto.KingInfoRequest;
import com.tinatest.line_bot.model.KingShortNameEntity;
import com.tinatest.line_bot.model.KingShortNameRepository;
import com.tinatest.line_bot.model.LineageKingInfoEntity;
import com.tinatest.line_bot.model.LineageKingInfoRepository;
import com.tinatest.line_bot.model.common.Common;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LineageService {

    private static Map<String, KingInfo> database = new LinkedHashMap<>();
    private static List<KingInfo> kingInfoList;

    @Autowired
    private LineageKingInfoRepository lineageKingInfoRepository;

    @Autowired
    private KingShortNameRepository kingShortNameRepository;

    public String createData(String id, String name, String location, String period, String lastAppear, String random) {
        if (StringUtils.isBlank(lastAppear)) {
            System.out.println("lastAppear is empty");
            return "lastAppear 沒有填";
        }
        Date lastAppearDate = null;

        try {
            lastAppearDate = Common.sdFormat.parse(lastAppear);
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        KingInfo kingInfo = database.get(id);
        Optional<LineageKingInfoEntity> kingInfoEntity = lineageKingInfoRepository.findById(id);

        if (!kingInfoEntity.isPresent()) {
            int size = getAllKings().size();
            String newId = String.valueOf(size + 1);
            LineageKingInfoEntity entity = new LineageKingInfoEntity();
            entity.setId(newId);
            entity.setKingName(name);
            entity.setLocation(location);
            entity.setPeriodMin(Integer.getInteger(period));
            entity.setRandom(Boolean.getBoolean(random));
            entity.setLastAppear(lastAppearDate);
            entity.setNextAppear(getNextAppear(entity.getPeriodMin(),  lastAppearDate));
            entity.setUpdateDate(new Date());
            lineageKingInfoRepository.save(entity);
//            database.put(newId, new KingInfo(newId, name, location, Integer.getInteger(period),
//                    Boolean.getBoolean(random), lastAppearDate, getNextAppear(kingInfo.getPeriod(),  lastAppearDate)));

            return "新增一筆: " + name;

        } else {
            LineageKingInfoEntity entity = kingInfoEntity.get();
            entity.setLastAppear(lastAppearDate);
            entity.setNextAppear(getNextAppear(entity.getPeriodMin(),  lastAppearDate));
            entity.setUpdateDate(new Date());
//            database.put(id, kingInfo);
            lineageKingInfoRepository.save(entity);
            return "修改一筆: " + entity.getKingName();
        }
    }

    public List<KingInfo> getAllKings() {
        List<KingInfo> all = new LinkedList<>();
        lineageKingInfoRepository.findAllByOrderByNextAppear().forEach(e-> all.add(transform(e)));
        return all;
    }

    public String goUpdate(List<KingInfoRequest> kingInfoList) {

        kingInfoList.forEach(kingInfo -> {
            LineageKingInfoEntity king = getKingByName(kingInfo.getName());
            try {
                if (kingInfo.getLastAppear() != null) {
                    king.setLastAppear(Common.sdFormat.parse(kingInfo.getLastAppear()));
                }
                if (kingInfo.getNextAppear() != null) {
                    king.setNextAppear(Common.sdFormat.parse(kingInfo.getNextAppear()));
                } else if (king.getLastAppear() != null) {
                    king.setNextAppear(getNextAppear(king.getPeriodMin(),  king.getLastAppear()));
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Parse date fail!");
            }
            king.setUpdateDate(new Date());
            lineageKingInfoRepository.save(king);
        });
        return "ok";
    }

    private Box getFlex(LineageKingInfoEntity kingInfo) {

        String randomStr = kingInfo.getRandom() ? "隨" : "必";
        String nextAppearStr = kingInfo.getNextAppear() == null ? "  -----  " : Common.timeOnlyFormat.format(kingInfo.getNextAppear());
        String missStr = (kingInfo.getMissCount() == null || kingInfo.getMissCount() == 0) ? "    " : "【過" + kingInfo.getMissCount() + "】";
        String name = String.format("%s-%s(%s)", kingInfo.getKingName(), kingInfo.getLocation(), randomStr);

        return Box.builder()
                .height("25px")
                .layout(FlexLayout.HORIZONTAL)
                .contents(Text.builder().text(nextAppearStr).flex(3).size("xs").weight(TextWeight.BOLD).build(),
                        Text.builder().text(name).flex(6).size("xs").build(),
                        Text.builder().text(missStr).flex(3).size("xs").color("#DD5555").align(FlexAlign.END).build())
                .build();

    }

    private FlexMessage getAppearTable(List<FlexComponent> flexComponents) {

        FlexDirection direction = FlexDirection.LTR;
        BubbleStyles styles = BubbleStyles.builder().footer(BlockStyle.builder().backgroundColor("#639594").build()).build();

        Box header = Box.builder()
                .layout(FlexLayout.VERTICAL)
                .content(Box.builder()
                        .layout(FlexLayout.HORIZONTAL)
                        .contents(Text.builder().text("出王重生表").color("#FFFFFF").weight(TextWeight.BOLD).build())
                        .build())
                .backgroundColor("#639594").build();

        Box body = Box.builder()
                .layout(FlexLayout.VERTICAL)
                .contents(flexComponents)
                .build();
//
//        Box footer = Box.builder().layout(FlexLayout.VERTICAL).build();
//        BubbleSize size;
//        Action action;

        Bubble bubble = Bubble.builder().direction(direction).header(header).body(body).styles(styles).build();
        return FlexMessage.builder().altText("出王重生表").contents(bubble).build();

//        client.pushMessage(new PushMessage("Ud62a356eedbea86f5231532bae38da4c", flexMessage));
    }

    public String command_k(String receivedMessage) {
        String[] strings = StringUtils.split(receivedMessage, " ");
        if (strings.length < 2 || StringUtils.isBlank(strings[1])) {
            return Common.ALERT + "查無此王！";
        }
        LineageKingInfoEntity king = getKingByName(strings[1]);
        if (king == null) {
            return Common.ALERT + "查無此王 : " + strings[1];
        }
        Date now = new Date();
        Date killingDate = null;
        if (strings.length == 2) {
            killingDate = now;
        } else if (strings.length == 3) {
            Date numStrToDate = numStrToDate(strings[2].trim(), now);
            if (numStrToDate == null) {
                return Common.ALERT + "時間錯誤";
            }
            killingDate = numStrToDate;
        }

        king.setLastAppear(killingDate);
        king.setNextAppear(getNextAppear(king.getPeriodMin(),  killingDate));
        king.setMissCount(0);
        king.setUpdateDate(new Date());
        lineageKingInfoRepository.save(king);
        updateKingInfoList();
        return getOneKingInfoStr(transform(king));
    }

    public String command_kr(String receivedMessage) {
        String[] strings = StringUtils.split(receivedMessage, " ");
        if (strings.length < 2 || StringUtils.isBlank(strings[1])) {
            return Common.ALERT + "查無此王！";
        }
        LineageKingInfoEntity king = getKingByName(strings[1]);
        if (king == null) {
            return Common.ALERT + "查無此王 : " + strings[1];
        }
        Date now = new Date();
        Date nextAppear = null;
        if (strings.length == 2) {
            nextAppear = now;
        } else if (strings.length == 3) {
            Date numStrToDate = numStrToDate(strings[2].trim(), now);
            if (numStrToDate == null) {
                return Common.ALERT + "時間錯誤";
            }
            nextAppear = numStrToDate;

        }
        king.setLastAppear(null);
        king.setNextAppear(nextAppear);
        king.setUpdateDate(new Date());
        king.setMissCount(0);
        lineageKingInfoRepository.save(king);
        updateKingInfoList();
        return getOneKingInfoStr(transform(king));
    }

    public Message command_kb() {
        List<LineageKingInfoEntity> result = lineageKingInfoRepository.getAppearFromNow(new Date());
        if (CollectionUtils.isEmpty(result)) {
            return new TextMessage(Common.ALERT + "無出王資訊" +Common.PENCIL);
        } else {
            int size = result.size() < 10 ? result.size() : 10;
            List<FlexComponent> flexComponents = new ArrayList<>();
            for (int i = 0; i < size; i++) {
//                        messages = messages + getKingsInfoStrForTen(result.get(i)); // just text msg
                flexComponents.add(getFlex(result.get(i)));
            }
            return getAppearTable(flexComponents);
        }
    }

    public String command_ky(String receivedMessage) {
        String[] strings = StringUtils.split(receivedMessage, " ");
        LineageKingInfoEntity king = getKingByName(strings[1]);
        if (king == null) {
            return Common.ALERT + "查無此王 : " + strings[1];
        }
        Date now = new Date();
        Date killingDate = null;
        if (strings.length == 2) {
            killingDate = now;
        } else if (strings.length == 3) {
            Date numStrToDate = numStrToDate(strings[2].trim(), now);
            if (numStrToDate == null) {
                return Common.ALERT + "時間錯誤";
            }
            killingDate = numStrToDate;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(killingDate);
        cal.add(Calendar.DATE, -1);

        king.setLastAppear(cal.getTime());
        king.setNextAppear(getNextAppear(king.getPeriodMin(),  cal.getTime()));
        king.setMissCount(0);
        king.setUpdateDate(new Date());
        lineageKingInfoRepository.save(king);
        updateKingInfoList();
        return getOneKingInfoStr(transform(king));
    }

    public String command_clear(String receivedMessage) {
        String[] strings = StringUtils.split(receivedMessage, " ");
        LineageKingInfoEntity king = getKingByName(strings[1].trim());
        king.setLastAppear(null);
        king.setNextAppear(null);
        king.setUpdateDate(new Date());
        lineageKingInfoRepository.save(king);
        updateKingInfoList();
        return getOneKingInfoStr(transform(king));
    }

    public String command_clearAll() {
        try {
            Iterable<LineageKingInfoEntity> all = lineageKingInfoRepository.findAll();
            all.forEach(k -> {
                k.setLastAppear(null);
                k.setNextAppear(null);
                k.setMissCount(0);
                k.setUpdateDate(new Date());
            });
            lineageKingInfoRepository.saveAll(all);
            updateKingInfoList();
            return Common.ALERT + "清除成功！";
        } catch (Exception e) {
            log.error(e.getMessage());
            return Common.ALERT + "清除失敗！";
        }
    }

    public String command_kbAll() {
        List<KingInfo> allKings = getAllKings();
        String result = "";
        for (KingInfo kingInfo: allKings) {
            result = result + getKingsInfoStrForTen(kingInfo);
        }
        return result;
    }

    public String command_tag(String receivedMessage) {
        String[] strings = StringUtils.split(receivedMessage, " ");

        List<KingShortNameEntity> byKingName = kingShortNameRepository.findByKingNameOrShortName(strings[1].trim(), strings[1].trim());
        List<String> list = byKingName.stream().map(KingShortNameEntity::getShortName).collect(Collectors.toList());
        return String.format("[%s] : %s", strings[1].trim(),  StringUtils.join(list, ","));
    }

    public String command_addTag(String receivedMessage) {
        String[] strings = StringUtils.split(receivedMessage, " ");

        String kingName = strings[2].trim();
        LineageKingInfoEntity king = getKingByName(kingName);
        if (king == null) {
            return String.format(Common.ALERT + "查無此王 : %s", kingName);
        }
        List<String> list = new ArrayList<>();
        for(int i = 3; i<strings.length; i++) {
            list.add(strings[i]);
            KingShortNameEntity entity = new KingShortNameEntity();
            entity.setShortName(strings[i].trim());
            entity.setKingName(kingName);
            entity.setUpdateDate(new Date());
            kingShortNameRepository.save(entity);
        }
        return String.format(Common.PENCIL + "新增[%s]的簡稱: %s", kingName, StringUtils.join(list, ","));
    }

    public String command_default(String receivedMessage) {
        LineageKingInfoEntity kingInfoEntity = getKingByName(receivedMessage);
        if (kingInfoEntity != null) {
            return getOneKingInfoStr(transform(kingInfoEntity));
        }
        return "";
    }

    private Date getNextAppear(int period, Date lastAppear) {
        Calendar c = Calendar.getInstance();
        c.setTime(lastAppear);
        c.add(Calendar.MINUTE, period);
        return c.getTime();
    }

    private String getKingsInfoStrForTen(KingInfo kingInfo) {
        String randomStr = kingInfo.isRandom() ? "隨" : "必";
        String nextAppearStr = kingInfo.getNextAppear() == null ? "  -----  " : Common.timeOnlyFormat.format(kingInfo.getNextAppear());
        String missStr = (kingInfo.getMissCount() == null || kingInfo.getMissCount() == 0) ? "" : "【過" + kingInfo.getMissCount() + "】";
        return String.format("%s %s [%s]-%s(%s)%s \n\n", Common.FIRE, nextAppearStr, kingInfo.getName(),
                kingInfo.getLocation(), randomStr, missStr);
    }

    private String getOneKingInfoStr(KingInfo kingInfo) {
        String randomStr = kingInfo.isRandom() ? "隨" : "必";
        String lastAppearStr = kingInfo.getLastAppear() == null ? " ----- " : Common.sdFormat.format(kingInfo.getLastAppear());
        String nextAppearStr = kingInfo.getNextAppear() == null ? " ----- " : Common.sdFormat.format(kingInfo.getNextAppear());
        String missStr = (kingInfo.getMissCount() == null || kingInfo.getMissCount() == 0) ? "" : "(過" + kingInfo.getMissCount() + ")";
        return String.format("%s [%s]-%s(%s) \n死亡時間:%s\n重生時間:%s", Common.DEVIL,
                kingInfo.getName(), kingInfo.getLocation(), randomStr, lastAppearStr, nextAppearStr);
    }

    public void updateNextAppear(List<String> ids, Date now) {
        Iterable<LineageKingInfoEntity> kings = lineageKingInfoRepository.findAllById(ids);
        for (LineageKingInfoEntity king: kings) {
            Date nextAppearTemp = king.getNextAppear();
            Integer miss = king.getMissCount() == null ? 0 : king.getMissCount();
            king.setLastAppear(nextAppearTemp);
            king.setNextAppear(getNextAppear(king.getPeriodMin(), nextAppearTemp));
            king.setMissCount(miss + 1);
            king.setUpdateDate(now);
            lineageKingInfoRepository.save(king);
        }
        updateKingInfoList();
    }

    private KingInfo transform(LineageKingInfoEntity lineageKingInfoEntity) {
        KingInfo kingInfo = new KingInfo();
        kingInfo.setId(lineageKingInfoEntity.getId());
        kingInfo.setName(lineageKingInfoEntity.getKingName());
        kingInfo.setLocation(lineageKingInfoEntity.getLocation());
        kingInfo.setPeriod(lineageKingInfoEntity.getPeriodMin());
        kingInfo.setLastAppear(lineageKingInfoEntity.getLastAppear());
        kingInfo.setNextAppear(lineageKingInfoEntity.getNextAppear());
        kingInfo.setRandom(lineageKingInfoEntity.getRandom());
        kingInfo.setMissCount(lineageKingInfoEntity.getMissCount());
        return kingInfo;
    }

    private Date numStrToDate(String numStr, Date now) {
        String allTime = (numStr.trim().length() == 4) ? numStr.trim() + "00" : numStr.trim();
        try {
            return Common.formatter.parse(String.format("%s %s",Common.dateFormat.format(now), allTime));
        } catch (ParseException e) {
            log.warn(e.getMessage());
        }
        return null;
    }

    private KingInfo findKingByName(String name) {
        return transform(getKingByName(name));
    }

    private LineageKingInfoEntity getKingByName(String name) {
        List<KingShortNameEntity> byShortName = kingShortNameRepository.findByKingNameOrShortName(name, name);
        String kingName = CollectionUtils.isEmpty(byShortName) ? name : byShortName.get(0).getKingName();
        LineageKingInfoEntity byKingName = lineageKingInfoRepository.findByKingName(kingName);
        if (byKingName != null) {
            return byKingName;
        }
        return null;
    }

    private List<KingInfo> getSorted(List<KingInfo> kingInfos) {
        Collections.sort(kingInfos);
        return kingInfos;
    }

    public void updateKingInfoList() {
        kingInfoList = getAllKings();
    }


    public List<KingInfo> getKingInfoList() {
        return kingInfoList;
    }
//
//    public void setKingInfoList(List<KingInfo> kingInfoList) {
//        this.kingInfoList = kingInfoList;
//    }

    public void localData() {

//        database.put("1", new KingInfo("1", "佩爾利斯", "蜜蜂蜂窩", 180, true, null, null));
//        database.put("2", new KingInfo("2","巴實那", "荒原南部", 240, true,null, null));
//        database.put("3", new KingInfo("3","潘納洛德", "哥肯花園", 300, true,null, null));
//        database.put("4", new KingInfo("4","采爾圖巴", "采爾圖巴營帳", 360, true,null, null));
//        database.put("5", new KingInfo("5","坦佛斯特", "太平間", 360, true,null, null));
//        database.put("6", new KingInfo("6","魔圖拉", "掠奪者野營", 360, true,null, null));
//        database.put("7", new KingInfo("7","安庫拉", "狄恩牧草地", 360, true,null, null));
//        database.put("8", new KingInfo("8","布賴卡", "布賴卡巢穴", 360, true,null, null));
//        database.put("9", new KingInfo("9","巨蟻女王", "螞蟻3樓", 360, true,null, null));
//        database.put("10", new KingInfo("10","特論巴", "血之沼澤", 420, true,null, null));
//
//        database.put("11", new KingInfo("11","雷比魯", "孢子擴散地", 420, true, null, null));
//        database.put("12", new KingInfo("12","史坦", "巨人的痕跡", 420, false,null, null));
//        database.put("13", new KingInfo("13","被汙染的克魯瑪", "克塔3樓", 480, false,null, null));
//        database.put("14", new KingInfo("14","提米特利斯", "芙羅蘭開墾地", 480, false,null, null));
//        database.put("15", new KingInfo("15","提米妮爾", "提米妮爾巢穴", 480, false,null, null));
//        database.put("16", new KingInfo("16","塔金", "里多蜥蜴人部落", 480, true,null, null));
//        database.put("17", new KingInfo("17","克魯瑪", "克魯瑪沼澤地", 480, false,null, null));
//        database.put("18", new KingInfo("18","卡雷斯", "塔諾峽谷", 540, true,null, null));
//        database.put("19", new KingInfo("19","貝希莫斯", "龍之谷北部", 540, false,null, null));
//        database.put("20", new KingInfo("20","塔拉金", "叛亂軍根據地", 600, false,null, null));
//
//        database.put("21", new KingInfo("21","核心基座", "克塔7樓", 600, true, null, null));
//        database.put("22", new KingInfo("22","梅杜莎", "梅杜莎的庭院", 600, false,null, null));
//        database.put("23", new KingInfo("23","卡坦", "克塔6樓", 600, false,null, null));
//        database.put("24", new KingInfo("24","沙勒卡", "德魯蜥蜴人棲息地", 600, false,null, null));
//        database.put("25", new KingInfo("25","凱索思", "絕望廢墟", 600, true,null, null));
//        database.put("26", new KingInfo("26","風王", "", 720, false,null, null));
//        database.put("27", new KingInfo("27","賽魯", "帕格立歐祭壇", 720, true,null, null));
//        database.put("28", new KingInfo("28","黑色蕾爾莉", "死亡迴廊", 720, false,null, null));
//        database.put("29", new KingInfo("29","薩班", "螞蟻2樓", 720, false,null, null));
//        database.put("30", new KingInfo("30","瓦柏", "山賊城寨", 720, true,null, null));
//
//        database.put("31", new KingInfo("31","猛龍獸", "龍洞6樓", 720, true, null, null));
//        database.put("32", new KingInfo("32","潘柴特", "狄恩丘陵地", 720, false,null, null));
//        database.put("33", new KingInfo("33","暗王", "", 720, false,null, null));
//        database.put("34", new KingInfo("34","寇倫", "象牙塔2樓", 720, false,null, null));
//        database.put("35", new KingInfo("35","水王", "", 1080, false,null, null));
//        database.put("36", new KingInfo("36","地王", "", 1080, false,null, null));
//        database.put("37", new KingInfo("37","奧爾芬", "奧爾芬之巢", 1440, true,null, null));

//        Date now = new Date();
//        database.values().stream().forEach(kingInfo -> {
//            LineageKingInfoEntity entity = new LineageKingInfoEntity();
//            entity.setId(kingInfo.getId());
//            entity.setKingName(kingInfo.getName());
//            entity.setLocation(kingInfo.getLocation());
//            entity.setRandom(kingInfo.isRandom());
//            entity.setPeriodMin(kingInfo.getPeriod());
//            entity.setUpdateDate(now);
//            lineageKingInfoRepository.save(entity);
//        });
    }

}
