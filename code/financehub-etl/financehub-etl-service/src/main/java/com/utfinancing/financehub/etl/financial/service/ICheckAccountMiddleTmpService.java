package com.utfinancing.financehub.etl.financial.service;

import com.baomidou.mybatisplus.core.metadata.IPage;


import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.etl.financial.entity.CheckAccountMiddleTmpEntity;

/**
 * @Author : jnc
 * @Date : Create in 2024-03-28
 * @Description : CheckAccountMiddleTmp服务类接口
 * @Modified :
 */
//@DS("master")
public interface ICheckAccountMiddleTmpService extends IService<CheckAccountMiddleTmpEntity> {

    void clearTableData();

}
