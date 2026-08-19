package com.kuang.geoslopefix.util;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SlopeDataCleanUtil{
    //正则常量定义
    private static final Pattern TEST_COMBIN_PATTERN
            = Pattern.compile("(自然斜坡|人工边坡)(涉水斜坡|不涉水斜坡)");

    //私有构造，禁止实例化工具类
    private SlopeDataCleanUtil(){}

    public static String standardSlopeType(String originText) {
        if (originText == null || originText.isBlank()) {
            return originText;
        }
        String text=originText;
        //1.清除空白字符
        text=text.replaceAll("\\s+","");

        //2.优先替换长句式
        text=text.replace("自然、不涉水斜坡","自然斜坡、不涉水斜坡");
        text=text.replace("自然、涉水斜坡","自然斜坡、涉水斜坡");
        text=text.replace("人工、不涉水斜坡","人工边坡、不涉水斜坡");
        text=text.replace("人工、涉水斜坡","人工边坡、涉水斜坡");
        text = text.replace("部涉水斜坡", "不涉水斜坡");
        text =text.replace("不涉水坡","不涉水斜坡");

        //3.通用别名替换
        text=text.replace("自然坡","自然斜坡");
        text = text.replace("人工坡", "人工边坡");

        //=====4.正则粘连插入顿号【位置正确】=====
        Matcher matcher= TEST_COMBIN_PATTERN.matcher(text);
        StringBuffer sb=new StringBuffer();
        while (matcher.find()){
            matcher.appendReplacement(sb,"$1、$2");
        }
        matcher.appendTail(sb);
        text=sb.toString();

        //5.统一各类符号转为中文顿号
        text = text.replaceAll("[,，､.。/]", "、");

        //6.清理连续顿号、末尾多余顿号
        text = text.replaceAll("、+", "、");
        text = text.replaceAll("、$", "");

        return text;
    }
}