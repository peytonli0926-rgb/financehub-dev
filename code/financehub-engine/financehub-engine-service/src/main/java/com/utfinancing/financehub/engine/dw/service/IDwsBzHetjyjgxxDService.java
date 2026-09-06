package com.utfinancing.financehub.engine.dw.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.dw.entity.DwsBzHetjyjgxxDEntity;
import com.utfinancing.financehub.engine.finance.entity.ContractEntity;

import java.util.List;
import java.util.Map;

/**
 * @Author : robjiang
 * @Date : Create in 2024-02-19
 * @Description : DwsBzHetjyjgxxD服务类接口
 * @Modified :
 */
public interface IDwsBzHetjyjgxxDService extends IService<DwsBzHetjyjgxxDEntity> {

    /**
     * 根据合同编码取得DwsBzHetjyjgxxDEntity对象
     */
    public Map<String, DwsBzHetjyjgxxDEntity> getDwsBzHetjyjgxxDMapByContractCode(List<String> contractCodeList);

    /**
     * 设置合同的paymethod
     */
    public List<ContractEntity> setContractPayMethod(List<ContractEntity> contractEntityList);
}
