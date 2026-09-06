package com.utfinancing.financehub.engine.finance.service;

import com.utfinancing.financehub.engine.finance.entity.DwsBzHetjyjgfyxDEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Author : robjiang
 * @Date : Create in 2024-05-08
 * @Description : DwsBzHetjyjgfyxD服务类接口
 * @Modified :
 */
public interface IDwsBzHetjyjgfyxDService extends IService<DwsBzHetjyjgfyxDEntity> {
    public Map<String, BigDecimal> selectHetjyjgfyxByContractCode(List<String> contractCodeList);
    public String getBusinessKey(String contractCode, String amountType);
}
