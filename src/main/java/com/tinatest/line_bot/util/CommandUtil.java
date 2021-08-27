package com.tinatest.line_bot.util;

public class CommandUtil {

    public static String getAll() {
        return "指令集: \n" +
                "(red check mark)kb: 列出近10筆王重生表  \n\n" +
                "(red check mark)kb all: 列出所王重生表  \n\n" +
                "(red check mark)k [王名稱]: 設定死亡時間(當下) \n\n" +
                "(red check mark)k [王名稱] [死亡時間]: 設定[死亡時間]，時間規則24小時制[hhmm]或[hhmmss] \n\n" +
                "(red check mark)clear [王名稱]: 清除死亡時間和重生時間 \n\n" +
                "(red check mark)add tag [名稱] [簡稱]";
    }
}
