package com.tinatest.line_bot.service;

import com.tinatest.line_bot.dto.KingInfo;
import com.tinatest.line_bot.dto.KingInfoRequest;
import com.tinatest.line_bot.model.KingShortNameEntity;
import com.tinatest.line_bot.model.KingShortNameRepository;
import com.tinatest.line_bot.model.LineageKingInfoEntity;
import com.tinatest.line_bot.model.LineageKingInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    private static final SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat timeOnlyFormat = new SimpleDateFormat("HH:mm:ss");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(String.format("yyyy-MM-dd"));
    private static final SimpleDateFormat formatter = new SimpleDateFormat(String.format("yyyy-MM-dd HHmmss"));

    @Autowired
    private LineageKingInfoRepository lineageKingInfoRepository;

    @Autowired
    private KingShortNameRepository kingShortNameRepository;

    @PostConstruct
    public void init() {
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


    public String createData(String id, String name, String location, String period, String lastAppear, String random) {
        if (StringUtils.isBlank(lastAppear)) {
            System.out.println("lastAppear is empty");
            return "lastAppear 沒有填";
        }
        Date lastAppearDate = null;

        try {
            lastAppearDate = sdFormat.parse(lastAppear);
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

    private Date getNextAppear(int period, Date lastAppear) {
        Calendar c = Calendar.getInstance();
        c.setTime(lastAppear);
        c.add(Calendar.MINUTE, period);
        return c.getTime();
    }

//    public Map<String, KingInfo> getDatabase() {
//        return database;
//    }

    public List<KingInfo> getAllKings() {
        List<KingInfo> all = new LinkedList<>();
        lineageKingInfoRepository.findAll().forEach(e-> all.add(transform(e)));
        return all;
    }

    public String goUpdate(List<KingInfoRequest> kingInfoList) {

        kingInfoList.forEach(kingInfo -> {
            LineageKingInfoEntity king = getKingByName(kingInfo.getName());
            try {
                if (kingInfo.getLastAppear() != null) {
                    king.setLastAppear(sdFormat.parse(kingInfo.getLastAppear()));
                }
                if (kingInfo.getNextAppear() != null) {
                    king.setNextAppear(sdFormat.parse(kingInfo.getNextAppear()));
                } else if (king.getLastAppear() != null) {
                    king.setNextAppear(getNextAppear(king.getPeriodMin(),  king.getLastAppear()));
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Parse date fail!");
            }
//            database.put(king.getId(), king);
            king.setUpdateDate(new Date());
            lineageKingInfoRepository.save(king);
        });
        return "ok";
    }

    public String getMsg(String receivedMessage) {

        receivedMessage = receivedMessage.toLowerCase();
        receivedMessage = receivedMessage.trim();
        String keyword = new String(receivedMessage);

//        Map<String, KingInfo> database = getDatabase();
        List<KingInfo> allKings = getAllKings();
        String messages = "";

        if (receivedMessage.startsWith("k ")) {
            keyword = "k";
        }
        if (receivedMessage.startsWith("clear ")) {
            keyword = "clear";
        }
        if (receivedMessage.startsWith("add tag")) {
            keyword = "add tag";
        }


        switch (keyword) {
            case "hi":
            case "嗨":
            case "你好":
            case "hello":
                messages = "Hi Hi Hi 我是Tina小幫手";
                break;
            case "clear":
                messages = command_clear(receivedMessage);
                break;
            case "add tag":
                messages = command_addTag(receivedMessage);
                break;
            case "k":
                String[] strings = StringUtils.split(receivedMessage, " ");
                Date now = new Date();
                Date killingDate = null;
                if (strings.length == 2) {
                    killingDate = now;
                } else if (strings.length == 3) {

                    String allTime = (strings[2].length() == 4) ? strings[2].trim() + "00" : strings[2];

                    try {
                        killingDate = formatter.parse(String.format("%s %s",dateFormat.format(now), allTime));
                    } catch (ParseException e) {
                        messages = "時間錯誤";
                        break;
                    }
                }

                LineageKingInfoEntity king = getKingByName(strings[1]);
                if (king == null) {
                    messages = "查無此王 : " + strings[1];
                    break;
                }

                king.setLastAppear(killingDate);
                king.setNextAppear(getNextAppear(king.getPeriodMin(),  killingDate));
                king.setUpdateDate(new Date());
//                database.put(king.getId(), king);
                lineageKingInfoRepository.save(king);
                messages = getOneKingInfoStr(transform(king));
                break;
            case "kb all":
                List<KingInfo> sorted = getSorted(allKings);
                for (KingInfo kingInfo: sorted) {
                    messages = messages + getKingsInfoStrForTen(kingInfo);
                }
                break;
            case "kb":
                List<KingInfo> result = allKings.stream().filter(k -> k.getNextAppear() != null).collect(Collectors.toList());
                List<KingInfo> sortedForTen = getSorted(result);
                int size = sortedForTen.size() < 10 ? sortedForTen.size() : 10;
                for (int i = 0; i<size; i++) {
                    messages = messages + getKingsInfoStrForTen(sortedForTen.get(i));
                }
                break;
            default:
                boolean find = false;
                for (KingInfo kingInfo: allKings) {
                    if (receivedMessage.equals(kingInfo.getName())) {
                        messages = getOneKingInfoStr(kingInfo);
                        find = true;
                    }
                }
                if (!find) {
                    messages = "指令集: \n" +
                            "(red check mark)kb: 列出近10筆王重生表  \n\n" +
                            "(red check mark)kb all: 列出所王重生表  \n\n" +
                            "(red check mark)k [王名稱]: 設定死亡時間(當下) \n\n" +
                            "(red check mark)k [王名稱] [死亡時間]: 設定[死亡時間]，時間規則24小時制[hhmm]或[hhmmss] \n\n" +
                            "(red check mark)clear [王名稱]: 清除死亡時間和重生時間 \n\n" +
                            "(red check mark)add tag [名稱] [簡稱]";
                }
                break;
        }
        return messages;
    }

    private List<KingInfo> getSorted(List<KingInfo> kingInfos) {
         Collections.sort(kingInfos);
        return kingInfos;
    }

    private String command_clear(String receivedMessage) {
        String[] strings = StringUtils.split(receivedMessage, " ");
        LineageKingInfoEntity king = getKingByName(strings[1].trim());
        king.setLastAppear(null);
        king.setNextAppear(null);
        king.setUpdateDate(new Date());
        lineageKingInfoRepository.save(king);
        return getOneKingInfoStr(transform(king));
    }

    private String command_addTag(String receivedMessage) {
        String[] strings = StringUtils.split(receivedMessage, " ");

        String kingName = strings[2].trim();
        KingInfo king = findKingByName(kingName);
        if (king == null) {
            return String.format("查無此王 : %s", kingName);
        }

        KingShortNameEntity entity = new KingShortNameEntity();
        entity.setShortName(strings[3].trim());
        entity.setKingName(kingName);
        entity.setUpdateDate(new Date());
        kingShortNameRepository.save(entity);

        return String.format("新增[%s]的簡稱: %s", kingName, strings[3]);
    }


//    private String getKingsInfoStr(KingInfo kingInfo) {
//        String randomStr = kingInfo.isRandom() ? "隨" : "必";
//        String lastAppearStr = kingInfo.getLastAppear() == null ? "  -----  " : timeOnlyFormat.format(kingInfo.getLastAppear());
//        String nextAppearStr = kingInfo.getNextAppear() == null ? "  -----  " : timeOnlyFormat.format(kingInfo.getNextAppear());
//       return String.format("%s [%s]-%s(%s) 死亡時間:%s 重生時間:%s \n",
//                kingInfo.getName(), kingInfo.getLocation(), randomStr, lastAppearStr, nextAppearStr);
//    }

    private String getKingsInfoStrForTen(KingInfo kingInfo) {
        String randomStr = kingInfo.isRandom() ? "隨" : "必";
        String nextAppearStr = kingInfo.getNextAppear() == null ? "  -----  " : timeOnlyFormat.format(kingInfo.getNextAppear());
        return String.format("%s [%s]-%s(%s) \n\n", nextAppearStr, kingInfo.getName(), kingInfo.getLocation(), randomStr);
    }

    private String getOneKingInfoStr(KingInfo kingInfo) {
        String randomStr = kingInfo.isRandom() ? "隨" : "必";
        String lastAppearStr = kingInfo.getLastAppear() == null ? " ----- " : sdFormat.format(kingInfo.getLastAppear());
        String nextAppearStr = kingInfo.getNextAppear() == null ? " ----- " : sdFormat.format(kingInfo.getNextAppear());
        return String.format("[%s]-%s(%s) \n死亡時間:%s\n重生時間:%s",
                kingInfo.getName(), kingInfo.getLocation(), randomStr, lastAppearStr, nextAppearStr);
    }

    private KingInfo findKingByName(String name) {
        return transform(getKingByName(name));
    }

    private LineageKingInfoEntity getKingByName(String name) {
        LineageKingInfoEntity byKingName = lineageKingInfoRepository.findByKingName(name);
        if (byKingName != null) {
            return byKingName;
        }
        KingShortNameEntity byShortName = kingShortNameRepository.findByShortName(name);
        if (byShortName == null) {
            return null;
        }
        return getKingByName(byShortName.getKingName());
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
        return kingInfo;
    }

}
