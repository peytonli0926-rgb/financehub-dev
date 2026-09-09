package com.utfinancing.financehub.engine.rule.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.googlecode.aviator.AviatorEvaluator;
import com.utfinancing.financehub.engine.enums.AssistFlagEnum;
import com.utfinancing.financehub.engine.rule.constant.RuleConstant;
import com.utfinancing.financehub.engine.scene.model.dto.SceneDTO;
import com.utfinancing.financehub.engine.scene.model.dto.SceneRuleDTO;
import com.utfinancing.financehub.engine.scene.model.dto.SceneVoucherConditionDTO;
import com.utfinancing.financehub.engine.scene.model.dto.SceneVoucherEntryDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class RuleUtil {

    /**
     * 翻译规则，表达式 {字段名} ==》 {字段编码}
     */
    public static void translateRule(SceneRuleDTO ruleDTO, Map<String, Object> fieldsMap, Map<String, Object> dataMap){
        StringSubstitutor stringSubstitutor = new StringSubstitutor(fieldsMap);
        stringSubstitutor.setVariablePrefix("{");
        stringSubstitutor.setVariableSuffix("}");

        //凭证头的规则执行条件，需要确定余额维度
        ruleDTO.setScriptCondition(StrUtil.blankToDefault(replaceFields(stringSubstitutor,ruleDTO.getScriptCondition()), "true")); //翻译规则执行条件
        ruleDTO.setScriptCondition(executeScriptString(ruleDTO.getScriptCondition(), dataMap)); //知行规则执行条件
        //如果规则知行条件的结果不为true，就不翻译里面的内容内容了，直接跳出
        boolean ruleCondition = BooleanUtil.toBoolean(ruleDTO.getScriptCondition());
        //如果不为true，直接跳过
        if (!ruleCondition){
            return;
        }

        ruleDTO.setSource(replaceFields(stringSubstitutor,ruleDTO.getSource())); //来源
        ruleDTO.setCompany(replaceFields(stringSubstitutor,ruleDTO.getCompany())); //公司
        ruleDTO.setBusinessDate(replaceFields(stringSubstitutor,ruleDTO.getBusinessDate())); //业务日期
        ruleDTO.setCurrency(replaceFields(stringSubstitutor,ruleDTO.getCurrency())); //币种
        ruleDTO.setDeptName(replaceFields(stringSubstitutor,ruleDTO.getDeptName())); //部门
        ruleDTO.setVoucherSummary(replaceFields(stringSubstitutor,ruleDTO.getVoucherSummary())); //凭证摘要

        //凭证行配置
        List<SceneVoucherEntryDTO> voucherEntryDTOS = ruleDTO.getEntryList();
        for (SceneVoucherEntryDTO entryDTO: voucherEntryDTOS){
            entryDTO.setVoucherSummary(replaceFields(stringSubstitutor,entryDTO.getVoucherSummary())); //凭证摘要
            entryDTO.setBankAccount(replaceFields(stringSubstitutor,entryDTO.getBankAccount()));
            //规则判定条件
            List<SceneVoucherConditionDTO> conditionDTOList = entryDTO.getConditionList();
            for (SceneVoucherConditionDTO conditionDTO: conditionDTOList){
                conditionDTO.setScriptCondition(StrUtil.blankToDefault(replaceFields(stringSubstitutor,conditionDTO.getScriptCondition(), entryDTO.getAssistFlags()), "true"));//执行条件
                conditionDTO.setScriptAmount(replaceFields(stringSubstitutor,conditionDTO.getScriptAmount(), entryDTO.getAssistFlags()));//凭证金额
            }
        }

    }

    private static String replaceFields(StringSubstitutor stringSubstitutor, String script){
        if (StrUtil.isBlank(script)){
            return script;
        }
        script = StrUtil.replace(StrUtil.cleanBlank(script), "，", ",");

        return stringSubstitutor.replace(script);
    }

    //根据凭证维度替换合同余额表的字段key
    private static String replaceFields(StringSubstitutor stringSubstitutor, String script, List<String> assistFlags){
        if (StrUtil.isBlank(script)){
            return script;
        }
        script = StrUtil.replace(StrUtil.cleanBlank(script), "，", ",");
        String assistFlagSuffix = "none";
        //替换余额表维度字段
        if (CollectionUtil.isNotEmpty(assistFlags)){
            assistFlagSuffix = StrUtil.join("_", assistFlags);
        }
        script = StrUtil.replace(script, "科目余额表.", "科目余额表" + assistFlagSuffix +".");

        return stringSubstitutor.replace(script);
    }

    /**
     * 执行规则 表达式 {字段编码} ==> 字段值
     */
    public static SceneRuleDTO executeRule(SceneRuleDTO ruleDTO, Map<String, Object> dataMap){
        SceneRuleDTO resultRule = BeanUtil.copyProperties(ruleDTO, SceneRuleDTO.class);
        //单个属性
        resultRule.setScriptCondition(executeScriptString(ruleDTO.getScriptCondition(), dataMap)); //规则执行条件
        boolean ruleCondition = BooleanUtil.toBoolean(resultRule.getScriptCondition());
        //如果不为true，直接跳过
        if (!ruleCondition){
            return resultRule;
        }
        resultRule.setSource(executeScriptString(ruleDTO.getSource(), dataMap)); //来源
        resultRule.setCompany(executeScriptString(ruleDTO.getCompany(), dataMap)); //公司
        resultRule.setBusinessDate(executeScriptString(ruleDTO.getBusinessDate(), dataMap)); //业务日期
        resultRule.setCurrency(executeScriptString(ruleDTO.getCurrency(), dataMap)); //币种
        resultRule.setDeptName(executeScriptString(ruleDTO.getDeptName(), dataMap)); //部门
        resultRule.setVoucherSummary(executeScriptString(ruleDTO.getVoucherSummary(), dataMap)); //凭证摘要

        //凭证行配置
        List<SceneVoucherEntryDTO> voucherEntryDTOS = ruleDTO.getEntryList();
        List<SceneVoucherEntryDTO> resultEntryDtoList = new ArrayList<>();
        for (SceneVoucherEntryDTO entryDTO: voucherEntryDTOS){
            SceneVoucherEntryDTO resultEntry = BeanUtil.copyProperties(entryDTO, SceneVoucherEntryDTO.class);
            resultEntry.setVoucherSummary(executeScriptString(entryDTO.getVoucherSummary(), dataMap)); //凭证摘要
            resultEntry.setBankAccount(executeScriptString(entryDTO.getBankAccount(), dataMap));
            resultEntry.setAssistFlags(entryDTO.getAssistFlags());
            //凭证行判定条件
            List<SceneVoucherConditionDTO> conditionDTOList = entryDTO.getConditionList();
            List<SceneVoucherConditionDTO> resultConditionDTOList = new ArrayList<>();
            for (SceneVoucherConditionDTO conditionDTO: conditionDTOList){
                SceneVoucherConditionDTO resultConditionDto = BeanUtil.copyProperties(conditionDTO, SceneVoucherConditionDTO.class);
                resultConditionDto.setScriptCondition(executeScriptString(conditionDTO.getScriptCondition(), dataMap));
                resultConditionDto.setScriptAmount(executeScriptString(conditionDTO.getScriptAmount(), dataMap));
                resultConditionDTOList.add(resultConditionDto);
            }

            resultEntry.setConditionList(resultConditionDTOList);
            resultEntryDtoList.add(resultEntry);
        }
        resultRule.setEntryList(resultEntryDtoList);
        return resultRule;
    }


    private static String executeScriptString(String script, Map<String, Object> dataMap){
        if (StrUtil.isBlank(script)){
            return script;
        }
        String result = null;
        try {
            result = AviatorEvaluator.execute(StrUtil.cleanBlank(script), dataMap).toString();
        }catch (Exception e){
            //log.error("执行表达式异常. 表达式：{}", script);
        }
        return result;
    }

    private static List<String> getAssistFlagsByDataMap(Map<String, Object> dataMap){
        List voucherHeadAssistFlags = new ArrayList();
        if (ObjectUtil.isNotNull(dataMap.get(RuleConstant.FIELD_CLIENT_CODE))){
            voucherHeadAssistFlags.add(AssistFlagEnum.CLIENT.getCode());
        }
        if (ObjectUtil.isNotNull(dataMap.get(RuleConstant.FIELD_CONTRACT_CODE))){
            voucherHeadAssistFlags.add(AssistFlagEnum.CONTRACT.getCode());
        }
        if (ObjectUtil.isNotNull(dataMap.get(RuleConstant.FIELD_BILL_CONTRACT_CODE))){
            voucherHeadAssistFlags.add(AssistFlagEnum.BILL_CONTRACT.getCode());
        }
        return voucherHeadAssistFlags;
    }

    public static void main(String[] args) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("receivable_service_balance_0_1", 1000);
        String script = "receivable_service_balance_0_1 + 10";
        System.out.println(AviatorEvaluator.execute(script, dataMap));
    }


}
