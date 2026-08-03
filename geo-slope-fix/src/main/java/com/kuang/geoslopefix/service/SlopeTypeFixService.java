package com.kuang.geoslopefix.service;

import java.util.List;
import java.util.Map;

public interface SlopeTypeFixService {
    String standardSlopeType(String originText);

    //预览清洗结果（只读，不修改数据库）
    List<Map<String, String>> previewFixData();
    //批量执行全部数据修复
    String batchFixAll();
    //根据主键单条修复
    boolean fixSingle(String id);
}