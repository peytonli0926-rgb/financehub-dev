package com.utfinancing.financehub.engine.dw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.dw.entity.DwBzHetjbxxDEntity;
import com.utfinancing.financehub.engine.dw.model.dto.DwBzHetjbxxDDTO;

import java.util.List;
import java.util.Map;

/**
 * @Author : lixin
 * @Date : Create in 2023-12-14
 * @Description : DwBzHetjbxxD服务类接口
 * @Modified :
 */
public interface IDwBzHetjbxxDService extends IService<DwBzHetjbxxDEntity> {


    /**
     * 根据合同编号查询合同基本信息
     * @param contractCode
     * @return
     */
    DwBzHetjbxxDDTO getHtjbxxByCode(String contractCode);


    /**
     * 根据合同编码取得DwBzHetjbxxD对象
     */
    public Map<String, DwBzHetjbxxDEntity> getDwBzHetjbxxDMapByContractCode(List<String> contractCodeList);
}
