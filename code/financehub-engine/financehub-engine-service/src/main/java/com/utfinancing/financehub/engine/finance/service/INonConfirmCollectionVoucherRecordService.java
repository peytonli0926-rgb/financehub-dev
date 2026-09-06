package com.utfinancing.financehub.engine.finance.service;

import com.utfinancing.financehub.engine.finance.entity.NonConfirmCollectionVoucherRecordEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author : robjiang
 * @Date : Create in 2024-04-25
 * @Description : NonConfirmCollectionVoucherRecord服务类接口
 * @Modified :
 */
public interface INonConfirmCollectionVoucherRecordService extends IService<NonConfirmCollectionVoucherRecordEntity> {


    /**
     * 根据明细ID查询凭证记录
     */
    public List<NonConfirmCollectionVoucherRecordEntity> selectByDetailId(String detailId);
}
