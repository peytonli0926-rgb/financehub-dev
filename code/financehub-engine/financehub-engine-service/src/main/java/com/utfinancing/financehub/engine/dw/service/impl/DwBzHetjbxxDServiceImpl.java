package com.utfinancing.financehub.engine.dw.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.dw.entity.DwBzHetjbxxDEntity;
import com.utfinancing.financehub.engine.dw.entity.DwsBzHetjyjgxxDEntity;
import com.utfinancing.financehub.engine.dw.mapper.DwBzHetjbxxDMapper;
import com.utfinancing.financehub.engine.dw.model.dto.DwBzHetjbxxDDTO;
import com.utfinancing.financehub.engine.dw.service.IDwBzHetjbxxDService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author : lixin
 * @Date : Create in 2023-12-14
 * @Description :  DwBzHetjbxxD服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class DwBzHetjbxxDServiceImpl extends ServiceImpl<DwBzHetjbxxDMapper, DwBzHetjbxxDEntity> implements IDwBzHetjbxxDService {

    private final DwBzHetjbxxDMapper dwBzHetjbxxDMapper;

    @Override
    public DwBzHetjbxxDDTO getHtjbxxByCode(String contractCode) {
        DwBzHetjbxxDEntity entity = this.getOne(Wrappers.<DwBzHetjbxxDEntity>lambdaQuery()
                .eq(DwBzHetjbxxDEntity::getVcHetbh, contractCode), false);
        if (entity == null) {
            return null;
        }
        return BeanUtil.copyProperties(entity, DwBzHetjbxxDDTO.class);
    }

    /**
     * 根据合同编码取得DwBzHetjbxxD对象
     */
    public Map<String, DwBzHetjbxxDEntity> getDwBzHetjbxxDMapByContractCode(List<String> contractCodeList) {
        List<DwBzHetjbxxDEntity> dwBzHetjbxxDEntityList = dwBzHetjbxxDMapper.selectBzhetjbxxByContractCode(contractCodeList);
        if (dwBzHetjbxxDEntityList != null && !dwBzHetjbxxDEntityList.isEmpty()) {
            return dwBzHetjbxxDEntityList.stream().collect(
                    Collectors.toMap(e -> e.getVcHetbh(), (e) -> e, (a, b) -> b));
        } else {
            return new HashMap<>();
        }
    }
}

