package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.finance.entity.VoucherAmountInitEntity;
import com.utfinancing.financehub.engine.finance.model.dto.TGLVoucherInitDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @Author : robjiang
 * @Date : Create in 2024-01-10
 * @Description : VoucherAmountInit服务类接口
 * @Modified :
 */
public interface IVoucherAmountInitService extends IService<VoucherAmountInitEntity> {

    /**
     * 根据合同查询收益数据列表
     */
    public Map<String, List<VoucherAmountInitEntity>> getVoucherAmountByContract(Set<String> contractCodeList);

    /**
     * 取得金蝶每月的现金流数据
     */
    public Map<String, BigDecimal> getVoucherAmountInit(String startPeriod, String endPeriod, String contractCode);

    /**
     * 根据合同和期数取得收益数据
     */
    public List<VoucherAmountInitEntity> getVoucherAmountByPeriod(String period, String contractCode);

}
