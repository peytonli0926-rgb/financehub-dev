package com.utfinancing.financehub.etl.financial.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.etl.financial.model.dto.CheckCommonDataQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.CheckCommonDataDTO;
import com.utfinancing.financehub.etl.financial.model.vo.CheckCommonDataVO;
import com.utfinancing.financehub.etl.financial.entity.CheckCommonDataEntity;
import com.utfinancing.financehub.etl.financial.mapper.CheckCommonDataMapper;
import com.utfinancing.financehub.etl.financial.service.ICheckCommonDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : jnc
 * @Date : Create in 2024-03-30
 * @Description :  CheckCommonData服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class CheckCommonDataServiceImpl extends ServiceImpl<CheckCommonDataMapper, CheckCommonDataEntity> implements ICheckCommonDataService {

    private final CheckCommonDataMapper checkCommonDataMapper;

    @Override
    public void clearTableData(String executeDateCode, String sqlMark, Integer periodCode) {
        checkCommonDataMapper.clearTableData(executeDateCode, sqlMark, periodCode);

    }
}

