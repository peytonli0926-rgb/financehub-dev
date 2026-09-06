package com.utfinancing.financehub.engine.finance.service;

import com.utfinancing.financehub.engine.finance.entity.NonConfirmCollectionFileUploadRecordEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author : robjiang
 * @Date : Create in 2024-05-21
 * @Description : NonConfirmCollectionFileUploadRecord服务类接口
 * @Modified :
 */
public interface INonConfirmCollectionFileUploadRecordService extends IService<NonConfirmCollectionFileUploadRecordEntity> {

    /**
     * 取得最新记录
     */
    public NonConfirmCollectionFileUploadRecordEntity getLastRecord();

}
