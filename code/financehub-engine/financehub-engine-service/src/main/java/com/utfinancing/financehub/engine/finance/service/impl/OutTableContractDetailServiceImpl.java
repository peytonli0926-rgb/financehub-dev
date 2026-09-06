package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.entity.TailDifferenceAdjustmentDetailEntity;
import com.utfinancing.financehub.engine.finance.model.dto.OutTableContractDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.OutTableContractDetailDTO;
import com.utfinancing.financehub.engine.finance.model.vo.OutTableContractDetailVO;
import com.utfinancing.financehub.engine.finance.entity.OutTableContractDetailEntity;
import com.utfinancing.financehub.engine.finance.mapper.OutTableContractDetailMapper;
import com.utfinancing.financehub.engine.finance.service.IContractBalanceService;
import com.utfinancing.financehub.engine.finance.service.IOutTableContractDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author : bruyang
 * @Date : Create in 2024-03-11
 * @Description :  OutTableContractDetail服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class OutTableContractDetailServiceImpl extends ServiceImpl<OutTableContractDetailMapper, OutTableContractDetailEntity> implements IOutTableContractDetailService {

    private final OutTableContractDetailMapper outTableContractDetailMapper;

    @Override
    public Long saveOutTableContractDetail(OutTableContractDetailDTO dto) {
        OutTableContractDetailEntity entity = BeanUtil.copyProperties(dto, OutTableContractDetailEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateOutTableContractDetail(Long id, OutTableContractDetailDTO dto) {
        OutTableContractDetailEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public OutTableContractDetailDTO getOutTableContractDetailDTOById(Long id) {
        OutTableContractDetailEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, OutTableContractDetailDTO.class);
    }

    @Override
    public IPage<OutTableContractDetailVO> selectPage(OutTableContractDetailQueryDTO queryDTO) {
        LambdaQueryWrapper<OutTableContractDetailEntity> queryWrapper = getQueryWrapper(queryDTO);
        //这里注入查询条件
        IPage<OutTableContractDetailEntity> entityIPage = outTableContractDetailMapper.selectPage(new Page<OutTableContractDetailEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, OutTableContractDetailVO.class);
    }

    @Override
    public Boolean removeBatcheByDetailId(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Boolean.TRUE;
        }
        List<OutTableContractDetailEntity> detailEntityList = this.lambdaQuery().in(OutTableContractDetailEntity::getOutTableAbsId,idList).list();
        if (CollectionUtil.isEmpty(detailEntityList)) {
            return Boolean.TRUE;
        }
        return this.removeBatchByIds(detailEntityList.stream().map(OutTableContractDetailEntity::getId).collect(Collectors.toList()));
    }

    @Override
    public List<OutTableContractDetailEntity> getOutTableContractDetailInfoByTaId(Long id) {
        return outTableContractDetailMapper.getOutTableContractDetailInfoByTaId(id);
    }

    @Override
    public List<OutTableContractDetailVO> selectDetailsByParams(OutTableContractDetailQueryDTO queryDTO) {
        return outTableContractDetailMapper.selectDetailsByParams(queryDTO);
    }

    public LambdaQueryWrapper<OutTableContractDetailEntity> getQueryWrapper(OutTableContractDetailQueryDTO queryDTO){
        LambdaQueryWrapper<OutTableContractDetailEntity> queryWrapper = Wrappers.<OutTableContractDetailEntity>lambdaQuery();
        if (StringUtils.isNotEmpty(queryDTO.getContractCode())) {
            queryWrapper.like(OutTableContractDetailEntity::getContractCode,queryDTO.getContractCode());
        }
        if (ObjectUtil.isNotNull(queryDTO.getOutTableAbsId())) {
            queryWrapper.eq(OutTableContractDetailEntity::getOutTableAbsId,queryDTO.getOutTableAbsId());
        }
        return queryWrapper;
    }

}

