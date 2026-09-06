package com.utfinancing.financehub.engine.dw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.dw.entity.DwCmFarkhxxDEntity;

import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-12-14
 * @Description : DwCmFarkhxxD服务类接口
 * @Modified :
 */
public interface IDwCmFarkhxxDService extends IService<DwCmFarkhxxDEntity> {


    List<DwCmFarkhxxDEntity> selectByClientCode(String clientCode);
}
