package com.tinatest.line_bot.model.common;

import java.text.SimpleDateFormat;

public class Common {

    public static final SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat timeOnlyFormat = new SimpleDateFormat("HH:mm:ss");
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat(String.format("yyyy-MM-dd"));
    public static final SimpleDateFormat formatter = new SimpleDateFormat(String.format("yyyy-MM-dd HHmmss"));

    public static final String DEVIL = String.valueOf(Character.toChars(Integer.decode("0x100024")));
    public static final String FIRE = String.valueOf(Character.toChars(Integer.decode("0x1000A4")));
    public static final String BLING = String.valueOf(Character.toChars(Integer.decode("0x10002D")));
    public static final String ALERT = String.valueOf(Character.toChars(Integer.decode("0x100035")));
    public static final String PENCIL = String.valueOf(Character.toChars(Integer.decode("0x100041")));
    public static final String HI = String.valueOf(Character.toChars(Integer.decode("0x10007F")));
    public static final String CLOCK = String.valueOf(Character.toChars(Integer.decode("0x100071")));
    public static final String CRY = String.valueOf(Character.toChars(Integer.decode("0x100094")));
    public static final String SMILE = String.valueOf(Character.toChars(Integer.decode("0x100090")));

    public static final String COMMAND = "[指令集] \n" +
            BLING+ "enable/disable: 開啟/關閉通知  \n\n" +
            BLING+ "kb: 列出近10筆王重生表  \n\n" +
            BLING+ "kb all: 列出所王重生表  \n\n" +
            BLING+ "k [王名稱]: 設定死亡時間(當下) \n\n" +
            BLING+ "k [王名稱] [死亡時間]: 設定指定死亡時間\n\n" +
            BLING+ "add tag [王名稱] [簡稱]: 新增王的簡稱 \n\n" +
            BLING+ "tag [王名稱]: 列出王的所有簡稱 \n\n" +
            BLING+ "kr [王名稱] [重生時間]: 直接指定重生時間 \n\n" +
            BLING+ "ky [王名稱] [死亡時間]: 指定前一天死亡時間(跨日時使用) \n\n" +
            BLING+ "clear [王名稱]: 清除死亡時間和重生時間 \n\n" +
            ALERT+"時間設定規則24小時制[hhmm]或[hhmmss]"
            ;
    public static final String COMMAND_ADMIN = "[管理員特殊指令集] \n" +
            BLING+ "push: [訊息]: 推播訊息給所有人  \n\n" +
            BLING+ "activate [代碼]: 幫使用者或群組啟用功能(當小幫手被加入好友或加入群組會提供一組代碼)  \n\n" +
            BLING+ "clear all: 清除所有死亡時間與重生時間  \n\n"
            ;

}
