package com.kuang.geoslopefix.service;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SlopeTypeTest2{
    //正则常量定义
    private static final Pattern TEST_COMBIN_PATTERN
            = Pattern.compile("(自然斜坡|人工边坡)(涉水斜坡|不涉水斜坡)");

    //改成static，main方便直接调用
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
        text=text.replace("人工、涉水坡","人工边坡、涉水斜坡");
        text = text.replace("部涉水斜坡", "不涉水斜坡");
        text =text.replace("不涉水坡","不涉水斜坡");

        //新增错别字修复
        text=text.replace("斜波","斜坡");
        text=text.replace("滑坡","斜坡");

        //3.通用别名替换
        text=text.replace("自然坡","自然斜坡");
        text = text.replace("人工坡", "人工边坡");

        //4.正则粘连插入顿号
        Matcher matcher= TEST_COMBIN_PATTERN.matcher(text);
        StringBuilder sb=new StringBuilder();
        while (matcher.find()){
            matcher.appendReplacement(sb,"$1、$2");
        }
        matcher.appendTail(sb);
        text=sb.toString();

        //5.统一顿号
        text = text.replaceAll("[,，､.。/]", "、");

        //6.清理连续顿号、末尾多余顿号
        text = text.replaceAll("、+", "、");
        text = text.replaceAll("、$", "");

        return text;
    }

    //main方法放在类内部！！
    public static void main(String[]args){
        //修正：String[] 数组定义
        String[] testArr = new String[]{
                "自然斜波不涉水坡",
                "自然斜坡不涉水斜坡",
                "人工、涉水坡",
                "自然、不涉水斜坡",
                "人工边坡涉水斜坡",
                "自然斜波、不涉水坡",
                "自然斜坡不涉水斜坡",
                "人工边坡、涉水斜波",
                "自然滑坡、涉水斜坡",
                "自然、不涉水斜坡"
        };
        for (String origin : testArr){
            String result = standardSlopeType(origin);
            System.out.println("原始："+origin+" → 标准化后："+result);
        }
    }
}