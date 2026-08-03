package com.kuang.geoslopefix.service.impl;

import com.kuang.geoslopefix.entity.DataSlopeGeology;
import com.kuang.geoslopefix.mapper.DataSlopeGeologyMapper;
import com.kuang.geoslopefix.service.SlopeTypeFixService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 斜坡类型标准化清洗业务实现类
 * 核心功能：对斜坡原始文本进行脏数据清洗、格式统一、规范标准化
 */
@Service // Spring注解：标识当前类为业务层Bean，自动交给Spring容器管理
public class SlopeTypeFixServiceImpl implements SlopeTypeFixService {

    @Resource // 依赖注入，注入Mybatis Mapper，用于数据库交互
    private DataSlopeGeologyMapper dataSlopeGeologyMapper;

    /**
     * 正则模式：匹配无分隔拼接文本
     * 案例：自然斜坡不涉水斜坡 → 捕获两组内容，后续插入顿号
     * 分组1：自然斜坡 | 人工边坡
     * 分组2：涉水斜坡 | 不涉水斜坡
     */
    private static final Pattern COMBINE_PATTERN = Pattern.compile("(自然斜坡|人工边坡)(涉水斜坡|不涉水斜坡);");
    /**
     * 合法格式校验正则
     * 允许格式：
     * 1. 单一值：自然斜坡 / 人工边坡 / 涉水斜坡 / 不涉水斜坡
     * 2. 组合值：自然斜坡、涉水斜坡  人工边坡、不涉水斜坡（只能两组，中文顿号分隔）
     */
    private static final Pattern LEGAL_PATTERN = Pattern.compile("^(自然斜坡|人工边坡|涉水斜坡|不涉水斜坡)$|^(自然斜坡|人工边坡)、(涉水斜坡|不涉水斜坡)$");

    /**
     * 斜坡类型文本标准化核心方法
     * @param originText 原始脏文本
     * @return 清洗后标准化斜坡类型字符串
     */
    @Override
    public String standardSlopeType(String originText) {
        // 判空防护：原始字符串null或者全空白，直接原值返回，不处理
        if (originText == null || originText.isBlank()) {
            return originText;
        }
        // 复制原始文本，后续所有操作基于副本，不改动入参
        String text = originText;

        // 1.清除所有空白字符（空格、制表符、换行、全角空格）
        text = text.replaceAll("\\s+", "");

        // ========== 【层级1：最高优先级 固定完整长脏数据，不可下移】 ==========
        // 规则：完整长句式优先匹配替换，防止后续短文本替换破坏原始字符串
        text = text.replace("自然、不涉水斜坡", "自然斜坡、不涉水斜坡");
        text = text.replace("自然、涉水斜坡", "自然斜坡、涉水斜坡");
        text = text.replace("人工、涉水斜坡", "人工边坡、涉水斜坡");

        // ========== 【层级2：错别字修正】 ==========
        text = text.replace("斜波", "斜坡");         //同音错别字
        text = text.replace("滑坡", "斜坡");         //录入错误
        text = text.replace("鞋破", "斜坡");         //拼音录入错误
        text = text.replace("不涉水鞋破", "不涉水斜坡");
        text = text.replace("部涉水斜坡", "不涉水斜坡"); //形近字错误
        text =text.replace("不涉水坡","不涉水斜坡");    //缺字补齐

        // ========== 【层级3：通用别名替换（新增！解决：自然坡、人工坡）】 ==========
        // 重要位置说明：不能放在正则后面，防止短别名匹配异常
        text = text.replace("自然坡", "自然斜坡");
        text = text.replace("人工坡", "人工边坡");

        // ========== 【层级4：统一各类分隔符转为中文顿号 、】 ==========
        // 匹配英文逗号、中文逗号、点号、斜杠等所有分隔符号，统一替换为标准中文顿号
        text = text.replaceAll("[,，､.。/]", "、");

        // ========== 【层级5：正则自动插入顿号 自然斜坡不涉水斜坡 → 自然斜坡、不涉水斜坡】 ==========
        Matcher matcher = COMBINE_PATTERN.matcher(text); // 创建正则匹配器
        StringBuffer sb = new StringBuffer(); // 字符串缓冲区，用于正则替换
        // 循环查找所有匹配到的拼接文本
        while (matcher.find()) {
            // $1代表第一捕获组，$2第二捕获组，中间插入顿号
            matcher.appendReplacement(sb, "$1、$2");
        }
        matcher.appendTail(sb); // 将剩余未匹配字符串追加到缓冲区末尾
        text = sb.toString(); // 替换完成结果赋值

        // ========== 【层级6：清理连续顿号、末尾多余顿号】 ==========
        text = text.replaceAll("、+", "、"); //多个连续顿号合并成单个顿号
        text = text.replaceAll("、$", "");   //移除字符串末尾多余顿号

        return text; // 返回清洗完成文本
    }

    /**
     * 预览修正数据接口
     * 查询所有斜坡类型异常数据，只计算清洗结果，不修改数据库，用于前端预览校验
     * @return 原始值、修正值、是否符合规范标识列表
     */
    @Override
    public List<Map<String, String>> previewFixData() {
        // Mapper查询数据库，筛选所有斜坡类型存在异常的数据
        List<DataSlopeGeology> list = dataSlopeGeologyMapper.selectAllAbnormalSlopeType();
        // 封装返回结果集合
        //SpringBoot 特性：
        //List<Map<>> 自动序列化为前端能识别的 JSON 数组。
        List<Map<String, String>> result = new ArrayList<>();

        // 遍历每一条异常数据
        for (DataSlopeGeology item : list) {
            // 获取原始斜坡类型
            String origin = item.getSlopeType();
            // 空数据直接跳过，无需处理
            if (origin == null || origin.isBlank()) {
                continue;
            }
            // 调用标准化方法，计算清洗之后的值
            String fixed = standardSlopeType(origin);
            // 使用合法正则校验清洗后的文本是否满足规范格式
            boolean isMatch = LEGAL_PATTERN.matcher(fixed).matches();

            // 组装单行返回数据
            Map<String, String> map = new HashMap<>();
            map.put("id", item.getId());                     //主键ID
            map.put("originSlopeType", origin);              //原始斜坡类型
            map.put("fixedSlopeType", fixed);                //清洗后斜坡类型
            map.put("isMatchStandard", isMatch ? "是" : "否");//是否符合规范标识
            result.add(map);
        }
        return result;
    }

    /**
     * 批量修正所有异常斜坡类型数据
     * 先预览计算所有待修改数据，再执行数据库更新
     * @return 执行结果统计文本
     */
    @Override
    public String batchFixAll() {
        // 调用预览方法，获取全部待处理数据（复用清洗逻辑，保证预览和实际修改结果一致）
        List<Map<String, String>> previewList = previewFixData();
        int successCount = 0; // 更新成功条数
        int skipCount = 0;    //无需变更跳过条数

        // 循环逐条处理
        for (Map<String, String> row : previewList) {
            String id = row.get("id");
            String origin = row.get("originSlopeType");
            String target = row.get("fixedSlopeType");

            // 清洗前后文本完全一致，不需要更新，跳过执行SQL，减轻数据库压力
            if (origin.equals(target)) {
                skipCount++;
                continue;
            }
            // 执行数据库更新语句
            int affect = dataSlopeGeologyMapper.updateSlopeType(id, target);
            // affect>0代表数据库受影响行数大于0，更新成功
            if (affect > 0) {
                successCount++;
            }
        }
        // 返回汇总信息给前端展示
        return "批量修正执行完成：总计加载" + previewList.size() + "条，成功更新" + successCount + "条，无需变更跳过" + skipCount + "条";
    }

    /**
     * 单条修正接口（暂不启用）
     * @param id 数据主键
     * @return 不支持该方法
     */
    @Override
    public boolean fixSingle(String id) {
        // 抛出不支持操作异常，业务设计要求统一使用预览+批量模式，禁止单独修改
        throw new UnsupportedOperationException("使用预览+批量接口执行，单一修改接口暂不启用");
    }
}