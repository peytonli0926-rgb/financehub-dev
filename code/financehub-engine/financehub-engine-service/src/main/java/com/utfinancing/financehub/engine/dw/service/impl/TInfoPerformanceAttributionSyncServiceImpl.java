package com.utfinancing.financehub.engine.dw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.utfinancing.financehub.engine.dw.entity.TInfoPerformanceAttributionSyncEntity;
import com.utfinancing.financehub.engine.dw.mapper.TInfoPerformanceAttributionSyncMapper;
import com.utfinancing.financehub.engine.dw.model.dto.TInfoPerformanceAttributionSyncQueryDTO;
import com.utfinancing.financehub.engine.dw.service.ITInfoPerformanceAttributionSyncService;
import com.utfinancing.financehub.engine.finance.entity.ContractEntity;
import com.utfinancing.financehub.engine.finance.model.vo.ContractVO;
import com.utfinancing.financehub.engine.finance.model.vo.TInfoPerformanceAttributionSyncVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * @Author : lixin
 * @Date : Create in 2023-12-14
 * @Description :  TInfoPerformanceAttributionSync服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class TInfoPerformanceAttributionSyncServiceImpl extends ServiceImpl<TInfoPerformanceAttributionSyncMapper, TInfoPerformanceAttributionSyncEntity> implements ITInfoPerformanceAttributionSyncService {

    private final TInfoPerformanceAttributionSyncMapper tInfoPerformanceAttributionSyncMapper;


    @Override
    public IPage<TInfoPerformanceAttributionSyncVO> selectPage(TInfoPerformanceAttributionSyncQueryDTO queryDTO) {
        LambdaQueryWrapper<TInfoPerformanceAttributionSyncEntity> queryWrapper = Wrappers.<TInfoPerformanceAttributionSyncEntity>lambdaQuery();
        //这里注入查询条件
        if (StringUtils.isNotEmpty(queryDTO.getContractNo())) {
            queryWrapper.eq(TInfoPerformanceAttributionSyncEntity::getContractNo,queryDTO.getContractNo());
        }
        IPage<TInfoPerformanceAttributionSyncEntity> entityIPage = tInfoPerformanceAttributionSyncMapper.selectPage(new Page<TInfoPerformanceAttributionSyncEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, TInfoPerformanceAttributionSyncVO.class);
    }
}

