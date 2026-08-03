package com.kuang.geoslopefix.controller;

import com.kuang.geoslopefix.mapper.DataSlopeGeologyMapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/debug")
public class TestController {

    @Resource
    private DataSlopeGeologyMapper mapper;

    @GetMapping("/fix")
    public String debugFix() {
        // 完整复制Navicat查到的ID，一字不差！
        String id = "47bbed4410c74c01bcbab26dd795f1fa";
        String targetText = "自然斜坡、涉水斜坡";
        int row = mapper.updateSlopeType(id, targetText);
        System.out.println("id=["+id+"]");
        System.out.println("newText=["+targetText+"]");
        return "执行完毕，影响行数：" + row;
    }
    }
