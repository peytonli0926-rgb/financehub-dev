package com.utfinancing.financehub.etl.financial.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.etl.financial.model.dto.AccountPeriodQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.AccountPeriodDTO;
import com.utfinancing.financehub.etl.financial.model.vo.AccountPeriodVO;
import com.utfinancing.financehub.etl.financial.entity.AccountPeriodEntity;
import com.utfinancing.financehub.etl.financial.mapper.AccountPeriodMapper;
import com.utfinancing.financehub.etl.financial.service.IAccountPeriodService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
 * @Description :  AccountPeriod服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class AccountPeriodServiceImpl extends ServiceImpl<AccountPeriodMapper, AccountPeriodEntity> implements IAccountPeriodService {

    private final AccountPeriodMapper accountPeriodMapper;


}

