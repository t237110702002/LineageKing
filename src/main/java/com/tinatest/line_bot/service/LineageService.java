package com.tinatest.line_bot.service;

import com.tinatest.line_bot.dto.KingInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class LineageService {

    private static Map<String, KingInfo> database = new LinkedHashMap<>();

    private final SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @PostConstruct
    public void init() {
        database.put("1", new KingInfo("佩爾利斯", "蜜蜂蜂窩", 180, true, null, null));
        database.put("2", new KingInfo("巴實那", "荒原南部", 240, true,null, null));
        database.put("3", new KingInfo("潘納洛德", "哥肯花園", 300, true,null, null));
        database.put("4", new KingInfo("采爾圖巴", "采爾圖巴營帳", 360, true,null, null));
        database.put("5", new KingInfo("坦佛斯特", "太平間", 360, true,null, null));
        database.put("6", new KingInfo("魔圖拉", "掠奪者野營", 360, true,null, null));
        database.put("7", new KingInfo("安庫拉", "狄恩牧草地", 360, true,null, null));
        database.put("8", new KingInfo("布賴卡", "布賴卡巢穴", 360, true,null, null));
        database.put("9", new KingInfo("巨蟻女王", "螞蟻3樓", 360, true,null, null));
        database.put("10", new KingInfo("特論巴", "血之沼澤", 420, true,null, null));

        database.put("11", new KingInfo("雷比魯", "孢子擴散地", 420, true, null, null));
        database.put("12", new KingInfo("史坦", "巨人的痕跡", 420, false,null, null));
        database.put("13", new KingInfo("被汙染的克魯瑪", "克塔3樓", 480, false,null, null));
        database.put("14", new KingInfo("提米特利斯", "芙羅蘭開墾地", 480, false,null, null));
        database.put("15", new KingInfo("提米妮爾", "提米妮爾巢穴", 480, false,null, null));
        database.put("16", new KingInfo("塔金", "里多蜥蜴人部落", 480, true,null, null));
        database.put("17", new KingInfo("克魯瑪", "克魯瑪沼澤地", 480, false,null, null));
        database.put("18", new KingInfo("卡雷斯", "塔諾峽谷", 540, true,null, null));
        database.put("19", new KingInfo("貝希莫斯", "龍之谷北部", 540, false,null, null));
        database.put("20", new KingInfo("塔拉金", "叛亂軍根據地", 600, false,null, null));

        database.put("21", new KingInfo("核心基座", "克塔7樓", 600, true, null, null));
        database.put("22", new KingInfo("梅杜莎", "梅杜莎的庭院", 600, false,null, null));
        database.put("23", new KingInfo("卡坦", "克塔6樓", 600, false,null, null));
        database.put("24", new KingInfo("沙勒卡", "德魯蜥蜴人棲息地", 600, false,null, null));
        database.put("25", new KingInfo("凱索思", "絕望廢墟", 600, true,null, null));
        database.put("26", new KingInfo("風王", "", 720, false,null, null));
        database.put("27", new KingInfo("賽魯", "帕格立歐祭壇", 720, true,null, null));
        database.put("28", new KingInfo("黑色蕾爾莉", "死亡迴廊", 720, false,null, null));
        database.put("29", new KingInfo("薩班", "螞蟻2樓", 720, false,null, null));
        database.put("30", new KingInfo("瓦柏", "山賊城寨", 720, true,null, null));

        database.put("31", new KingInfo("猛龍獸", "龍洞6樓", 720, true, null, null));
        database.put("32", new KingInfo("潘柴特", "狄恩丘陵地", 720, false,null, null));
        database.put("33", new KingInfo("暗王", "", 720, false,null, null));
        database.put("34", new KingInfo("寇倫", "象牙塔2樓", 720, false,null, null));
        database.put("35", new KingInfo("水王", "", 1080, false,null, null));
        database.put("36", new KingInfo("地王", "", 1080, false,null, null));
        database.put("37", new KingInfo("奧爾芬", "奧爾芬之巢", 1440, true,null, null));
    }


    public String createData(String id, String name, String location, String period, String lastAppear, String random) {
        if (StringUtils.isBlank(lastAppear)) {
            System.out.println("lastAppear is empty");
            return "lastAppear 沒有填";
        }
        if (database.size() >= 100) {
            System.out.println("database滿了喔! size:100");
            return "database滿了喔!";
        }
        Date lastAppearDate = null;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            lastAppearDate = formatter.parse(lastAppear);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        KingInfo kingInfo = database.get(id);
        if (kingInfo == null) {
            String newId = String.valueOf(database.size() + 1);
            database.put(newId, new KingInfo(name, location, Integer.getInteger(period), Boolean.getBoolean(random), lastAppearDate, getNextAppear(kingInfo.getPeriod(),  lastAppearDate)));

            return "新增一筆: " + name;

        } else {
            kingInfo.setLastAppear(lastAppearDate);
            kingInfo.setNextAppear(getNextAppear(kingInfo.getPeriod(),  lastAppearDate));
            database.put(id, kingInfo);
            return "修改一筆: " + kingInfo.getName();
        }
    }

    private Date getNextAppear(int period, Date lastAppear) {
        Calendar c = Calendar.getInstance();
        c.setTime(lastAppear);
        c.add(Calendar.MINUTE, period);
        return c.getTime();
    }

    public Map<String, KingInfo> getDatabase() {
        return database;
    }


    public String getMsg(String receivedMessage) {
        Map<String, KingInfo> database = getDatabase();
        String messages = "";
        switch (receivedMessage.toLowerCase()) {
            case "hi":
            case "嗨":
            case "你好":
            case "hello":
                messages = "HI HI HI 我是Tina小幫手";
                break;
            case "kb all":
                for (KingInfo kingInfo: database.values()) {
                    String randomStr = kingInfo.isRandom() ? "隨" : "必";
                    String lastAppearStr = kingInfo.getLastAppear() == null ? "無紀錄時間" : sdFormat.format(kingInfo.getLastAppear());
                    String nextAppearStr = kingInfo.getLastAppear() == null ? "無紀錄時間" : sdFormat.format(kingInfo.getNextAppear());


                    messages = messages + String.format("[%s]-%s(%s) 死亡時間:%s 重生時間:%s \n",
                            kingInfo.getName(), kingInfo.getLocation(), randomStr, lastAppearStr, nextAppearStr);
                }
            break;
            default:
                for (KingInfo kingInfo: database.values()) {
                    if (receivedMessage.equals(kingInfo.getName())) {
                        String randomStr = kingInfo.isRandom() ? "隨" : "必";
                        String lastAppearStr = kingInfo.getLastAppear() == null ? "無紀錄時間" : sdFormat.format(kingInfo.getLastAppear());
                        String nextAppearStr = kingInfo.getLastAppear() == null ? "無紀錄時間" : sdFormat.format(kingInfo.getNextAppear());
                        messages = messages + String.format("[%s]-%s(%s) \n死亡時間:%s\n重生時間:%s",
                                kingInfo.getName(), kingInfo.getLocation(), randomStr, lastAppearStr, nextAppearStr);
                        break;
                    }

                }
                break;
        }
        return messages;
    }

}
