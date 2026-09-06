package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.model.dto.DwsBzHetjyjgfyxDQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.DwsBzHetjyjgfyxDDTO;
import com.utfinancing.financehub.engine.finance.model.vo.DwsBzHetjyjgfyxDVO;
import com.utfinancing.financehub.engine.finance.entity.DwsBzHetjyjgfyxDEntity;
import com.utfinancing.financehub.engine.finance.mapper.DwsBzHetjyjgfyxDMapper;
import com.utfinancing.financehub.engine.finance.service.IDwsBzHetjyjgfyxDService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author : robjiang
 * @Date : Create in 2024-05-08
 * @Description :  DwsBzHetjyjgfyxD服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class DwsBzHetjyjgfyxDServiceImpl extends ServiceImpl<DwsBzHetjyjgfyxDMapper, DwsBzHetjyjgfyxDEntity>
        implements IDwsBzHetjyjgfyxDService {

    private final DwsBzHetjyjgfyxDMapper dwsBzHetjyjgfyxDMapper;

    public Map<String, BigDecimal> selectHetjyjgfyxByContractCode(List<String> contractCodeList) {
        List<DwsBzHetjyjgfyxDEntity> dwsBzHetjyjgfyxDEntityList = dwsBzHetjyjgfyxDMapper.
                selectHetjyjgfyxByContractCode(contractCodeList);
        if (dwsBzHetjyjgfyxDEntityList == null || dwsBzHetjyjgfyxDEntityList.isEmpty()) {
            return new HashMap<>();
        }

        return dwsBzHetjyjgfyxDEntityList.stream().collect(
                Collectors.toMap(
                    e -> this.getBusinessKey(e.getVcHetbh(), e.getVcFeiylx()), 
                    e -> new BigDecimal(e.getDecJiashj() != null ? e.getDecJiashj() : "0"), 
                    (existing, replacement) -> existing.add(replacement)
                ));
    }

    /**
     * 业务主键取得
     */
    public String getBusinessKey(String contractCode, String amountType) {
        return new StringBuffer(contractCode).append("_").append(amountType).toString();
    }
}

