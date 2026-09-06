package com.utfinancing.financehub.etl.financial.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.etl.financial.entity.VoucherDimensionValueEntity;

import java.util.List;


/**
 * @Author : robjiang
 * @Date : Create in 2024-02-29
 * @Description : VoucherDimensionValue服务类接口
 * @Modified :
 */
public interface IVoucherDimensionValueService extends IService<VoucherDimensionValueEntity> {

    /**
     * 根据条件查询VoucherDimensionValueEntity数据
     */
    public List<VoucherDimensionValueEntity> getVoucherDimensionValueListByCondition(VoucherDimensionValueEntity params);

    /**
     * 取得业务主键
     */
    public String getBusinessKey(VoucherDimensionValueEntity params);
}
