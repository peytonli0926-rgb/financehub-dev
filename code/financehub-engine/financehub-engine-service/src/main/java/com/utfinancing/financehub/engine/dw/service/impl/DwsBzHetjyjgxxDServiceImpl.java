package com.utfinancing.financehub.engine.dw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.dw.entity.DwsBzHetjyjgxxDEntity;
import com.utfinancing.financehub.engine.dw.mapper.DwsBzHetjyjgxxDMapper;
import com.utfinancing.financehub.engine.dw.service.IDwsBzHetjyjgxxDService;
import com.utfinancing.financehub.engine.finance.entity.ContractEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author : robjiang
 * @Date : Create in 2024-02-19
 * @Description :  DwsBzHetjyjgxxD服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class DwsBzHetjyjgxxDServiceImpl extends ServiceImpl<DwsBzHetjyjgxxDMapper, DwsBzHetjyjgxxDEntity>
        implements IDwsBzHetjyjgxxDService {

    private final DwsBzHetjyjgxxDMapper dwsBzHetjyjgxxDMapper;


    /**
     * 根据合同编码取得DwsBzHetjyjgxxDEntity对象
     */
    public Map<String, DwsBzHetjyjgxxDEntity> getDwsBzHetjyjgxxDMapByContractCode(List<String> contractCodeList) {
        List<DwsBzHetjyjgxxDEntity> dwsBzHetjyjgxxDEntityList = dwsBzHetjyjgxxDMapper.
                selectQicqmzfByContractCode(contractCodeList);
        if (dwsBzHetjyjgxxDEntityList != null && !dwsBzHetjyjgxxDEntityList.isEmpty()) {
            return dwsBzHetjyjgxxDEntityList.stream().collect(
                    Collectors.toMap(e -> e.getVcHetbh(), (e) -> e, (a, b) -> b));
        } else {
            return new HashMap<>();
        }
    }


    /**
     * 设置合同的paymethod
     */
    public List<ContractEntity> setContractPayMethod(List<ContractEntity> contractEntityList) {
        if (contractEntityList == null || contractEntityList.isEmpty()) {
            return new ArrayList<>();
        }

        List<String> contractCodeList = contractEntityList.stream().map(e -> e.getContractCode()).
                collect(Collectors.toList());
        Map<String, DwsBzHetjyjgxxDEntity> DwsBzHetjyjgxxDListMap = this.getDwsBzHetjyjgxxDMapByContractCode(
                contractCodeList);

        for (ContractEntity entity : contractEntityList) {
            DwsBzHetjyjgxxDEntity dwsBzHetjyjgxxDEntity = DwsBzHetjyjgxxDListMap.get(entity.getContractCode());
            if (dwsBzHetjyjgxxDEntity != null) {
                entity.setPayMethod(dwsBzHetjyjgxxDEntity.getVcQicqmzf());
            } else {
                entity.setPayMethod("期末");
            }
        }
        return contractEntityList;
    }
}

