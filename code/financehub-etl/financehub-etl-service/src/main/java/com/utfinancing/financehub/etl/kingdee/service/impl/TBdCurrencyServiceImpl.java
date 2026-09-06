package com.utfinancing.financehub.etl.kingdee.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.etl.kingdee.model.dto.TBdCurrencyQueryDTO;
import com.utfinancing.financehub.etl.kingdee.model.dto.TBdCurrencyDTO;
import com.utfinancing.financehub.etl.kingdee.model.vo.TBdCurrencyVO;
import com.utfinancing.financehub.etl.kingdee.entity.TBdCurrencyEntity;
import com.utfinancing.financehub.etl.kingdee.mapper.TBdCurrencyMapper;
import com.utfinancing.financehub.etl.kingdee.service.ITBdCurrencyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
 * @Description :  TBdCurrency服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
@DS("slave_kingdee")
public class TBdCurrencyServiceImpl extends ServiceImpl<TBdCurrencyMapper, TBdCurrencyEntity> implements ITBdCurrencyService {

    private final TBdCurrencyMapper tBdCurrencyMapper;


}

