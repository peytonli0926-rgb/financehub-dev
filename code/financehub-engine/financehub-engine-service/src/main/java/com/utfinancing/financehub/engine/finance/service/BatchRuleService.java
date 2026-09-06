package com.utfinancing.financehub.engine.finance.service;

import com.utfinancing.financehub.engine.rule.model.vo.VoucherInfoVO;

import java.util.List;
import java.util.Map;

public interface BatchRuleService {

    public List<VoucherInfoVO> executeRule(List<Map<String, Object>> voucherMapList, Map<String, Object> params);
}
