package com.utfinancing.financehub.etl.financial.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.etl.financial.entity.VoucherDimensionValueEntity;
import com.utfinancing.financehub.etl.financial.mapper.VoucherDimensionValueMapper;
import com.utfinancing.financehub.etl.financial.service.IVoucherDimensionValueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : robjiang
 * @Date : Create in 2024-02-29
 * @Description :  VoucherDimensionValue服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
public class VoucherDimensionValueServiceImpl extends ServiceImpl<VoucherDimensionValueMapper,
        VoucherDimensionValueEntity> implements IVoucherDimensionValueService {

    private final VoucherDimensionValueMapper voucherDimensionValueMapper;

    /**
     * 根据条件查询VoucherDimensionValueEntity数据
     */
    public List<VoucherDimensionValueEntity> getVoucherDimensionValueListByCondition(VoucherDimensionValueEntity params) {
        LambdaQueryWrapper<VoucherDimensionValueEntity> wrapper = new LambdaQueryWrapper();
        if (StringUtils.isNotEmpty(params.getOrgId())) {
            wrapper.eq(VoucherDimensionValueEntity::getOrgId, params.getOrgId());
        }
        if (StringUtils.isNotEmpty(params.getSourceSystem())) {
            wrapper.eq(VoucherDimensionValueEntity::getSourceSystem, params.getSourceSystem());
        }
        if (StringUtils.isNotEmpty(params.getAccountCode())) {
            wrapper.eq(VoucherDimensionValueEntity::getAccountCode, params.getAccountCode());
        }
        return voucherDimensionValueMapper.selectList(wrapper);
    }

    /**
     * 取得业务主键
     */
    public String getBusinessKey(VoucherDimensionValueEntity params) {
        StringBuffer businessKey = new StringBuffer();
        if (StringUtils.isNotEmpty(params.getOrgId())) {
            businessKey.append(params.getOrgId()).append("|");
        }
        if (StringUtils.isNotEmpty(params.getAccountCode())) {
            businessKey.append(params.getAccountCode()).append("|");
        }
        if (StringUtils.isNotEmpty(params.getSourceSystem())) {
            businessKey.append(params.getSourceSystem()).append("|");
        }
        return businessKey.deleteCharAt(businessKey.length() - 1).toString();
    }
}

