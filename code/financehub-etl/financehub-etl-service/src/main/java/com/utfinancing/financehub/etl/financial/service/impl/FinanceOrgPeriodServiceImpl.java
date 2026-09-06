package com.utfinancing.financehub.etl.financial.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.utfinancing.financehub.etl.financial.entity.AccountEntity;
import com.utfinancing.financehub.etl.financial.mapper.AccountMapper;
import com.utfinancing.financehub.etl.financial.mapper.FinanceOrgPeriodMapper;
import com.utfinancing.financehub.etl.financial.model.dto.AccountDTO;
import com.utfinancing.financehub.etl.financial.model.dto.AccountQueryDTO;
import com.utfinancing.financehub.etl.financial.model.vo.AccountVO;
import com.utfinancing.financehub.etl.financial.service.IAccountService;
import com.utfinancing.financehub.etl.financial.service.IFinanceOrgPeriodService;
import com.utfinancing.financehub.etl.kingdee.model.dto.OrgPeriodDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-16
 * @Description :  Account服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class FinanceOrgPeriodServiceImpl implements IFinanceOrgPeriodService {

    private final FinanceOrgPeriodMapper financeOrgPeriodMapper;


    @Override
    public List<OrgPeriodDTO> selectCurrentPeriodCodeByOrgIds(List<String> compareOrgIds) {
        return financeOrgPeriodMapper.selectCurrentPeriodCodeByOrgIds(compareOrgIds);
    }
}

