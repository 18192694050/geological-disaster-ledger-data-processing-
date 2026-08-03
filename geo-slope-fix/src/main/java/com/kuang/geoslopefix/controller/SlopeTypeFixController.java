package com.kuang.geoslopefix.controller;

import com.kuang.geoslopefix.service.SlopeTypeFixService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/slopeFix")
public class SlopeTypeFixController {

    @Resource
    private SlopeTypeFixService slopeTypeFixService;

    /**
     * 预览转换结果【只计算，不修改数据库！必须优先预览】
     */
    @GetMapping("/preview")
    public List<Map<String, String>> preview() {
        return slopeTypeFixService.previewFixData();
    }

    /**
     * 执行批量更新
     */
    @GetMapping("/batch")
    public String batch() {
        return slopeTypeFixService.batchFixAll();
    }
}