package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.entity.TailDifferenceAdjustmentEntity;
import com.utfinancing.financehub.engine.finance.mapper.TailDifferenceAdjustmentMapper;
import com.utfinancing.financehub.engine.finance.model.dto.TailDifferenceAdjustmentDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.TailDifferenceAdjustmentDetailDTO;
import com.utfinancing.financehub.engine.finance.model.dto.TailDifferenceAdjustmentQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.TailDifferenceAdjustmentDetailVO;
import com.utfinancing.financehub.engine.finance.entity.TailDifferenceAdjustmentDetailEntity;
import com.utfinancing.financehub.engine.finance.mapper.TailDifferenceAdjustmentDetailMapper;
import com.utfinancing.financehub.engine.finance.service.ITailDifferenceAdjustmentDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.verification.model.dto.CourtCostDetailsQueryDTO;
import com.utfinancing.financehub.engine.verification.model.vo.CourtCostDetailsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author : bruyang
 * @Date : Create in 2024-03-05
 * @Description :  TailDifferenceAdjustmentDetail服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class TailDifferenceAdjustmentDetailServiceImpl extends ServiceImpl<TailDifferenceAdjustmentDetailMapper, TailDifferenceAdjustmentDetailEntity> implements ITailDifferenceAdjustmentDetailService {

    private final TailDifferenceAdjustmentDetailMapper tailDifferenceAdjustmentDetailMapper;
    @Override
    public Long saveTailDifferenceAdjustmentDetail(TailDifferenceAdjustmentDetailDTO dto) {
        TailDifferenceAdjustmentDetailEntity entity = BeanUtil.copyProperties(dto, TailDifferenceAdjustmentDetailEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateTailDifferenceAdjustmentDetail(Long id, TailDifferenceAdjustmentDetailDTO dto) {
        TailDifferenceAdjustmentDetailEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public TailDifferenceAdjustmentDetailDTO getTailDifferenceAdjustmentDetailDTOById(Long id) {
        TailDifferenceAdjustmentDetailEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, TailDifferenceAdjustmentDetailDTO.class);
    }

    @Override
    public IPage<TailDifferenceAdjustmentDetailVO> selectPage(TailDifferenceAdjustmentDetailQueryDTO queryDTO) {
        LambdaQueryWrapper<TailDifferenceAdjustmentDetailEntity> queryWrapper = getQueryWrapper(queryDTO);
        //这里注入查询条件
//        IPage<TailDifferenceAdjustmentDetailEntity> entityIPage = tailDifferenceAdjustmentDetailMapper.selectPage(new Page<TailDifferenceAdjustmentDetailEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return tailDifferenceAdjustmentDetailMapper.selectPageByParams(new Page<TailDifferenceAdjustmentDetailVO>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryDTO);
    }

    @Override
    public Boolean removeBatcheByDetailId(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Boolean.TRUE;
        }
        List<TailDifferenceAdjustmentDetailEntity> detailEntityList = this.lambdaQuery().in(TailDifferenceAdjustmentDetailEntity::getTailDifferenceAdjustmentId,idList).list();
        if (CollectionUtil.isEmpty(detailEntityList)) {
            return Boolean.TRUE;
        }
        return this.removeBatchByIds(detailEntityList.stream().map(TailDifferenceAdjustmentDetailEntity::getId).collect(Collectors.toList()));
    }

    @Override
    public List<TailDifferenceAdjustmentDetailVO> selectByCondition(TailDifferenceAdjustmentDetailQueryDTO queryDTO) {
        LambdaQueryWrapper<TailDifferenceAdjustmentDetailEntity> queryWrapper = getQueryWrapper(queryDTO);
        return BeanUtil.copyToList(this.list(queryWrapper),TailDifferenceAdjustmentDetailVO.class);
    }

    @Override
    public boolean initInsertData(TailDifferenceAdjustmentQueryDTO queryDTO) {
        return tailDifferenceAdjustmentDetailMapper.initInsertData(queryDTO);
    }

    @Override
    public List<TailDifferenceAdjustmentEntity> selectAllOrgIdAccountCode() {
        return tailDifferenceAdjustmentDetailMapper.selectAllOrgIdAccountCode();
    }

    @Override
    public List<TailDifferenceAdjustmentDetailVO> selectByParams(TailDifferenceAdjustmentDetailQueryDTO queryDTO) {
        return tailDifferenceAdjustmentDetailMapper.selectByParams(queryDTO);
    }

    @Override
    public void generateContractBalanceTempData(TailDifferenceAdjustmentQueryDTO queryDTO) {
        tailDifferenceAdjustmentDetailMapper.generateContractBalanceTempData(queryDTO);
    }

    @Override
    public void truncateContractBalanceTempData() {
        tailDifferenceAdjustmentDetailMapper.truncateContractBalanceTempData();
    }

    public LambdaQueryWrapper<TailDifferenceAdjustmentDetailEntity> getQueryWrapper(TailDifferenceAdjustmentDetailQueryDTO queryDTO){
        LambdaQueryWrapper<TailDifferenceAdjustmentDetailEntity> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(queryDTO.getAccountCode())) {
            queryWrapper.eq(TailDifferenceAdjustmentDetailEntity::getAccountCode,queryDTO.getAccountCode());
        }
        if (StringUtils.isNotEmpty(queryDTO.getAccountName())) {
            queryWrapper.eq(TailDifferenceAdjustmentDetailEntity::getAccountName,queryDTO.getAccountName());
        }
        if (CollectionUtils.isNotEmpty(queryDTO.getOrgIdList())) {
            queryWrapper.in(TailDifferenceAdjustmentDetailEntity::getOrgId,queryDTO.getOrgIdList());
        }
        if (ObjectUtil.isNotNull(queryDTO.getAccountDate())) {
            queryWrapper.apply("to_char(account_date,'YYYY-MM-DD')={0}",queryDTO.getAccountDate());
        }
        if (ObjectUtil.isNotNull(queryDTO.getBusinessDate())) {
            queryWrapper.apply("to_char(business_date,'YYYY-MM-DD')={0}",queryDTO.getBusinessDate());
        }
        if (ObjectUtil.isNotNull(queryDTO.getTailDifferenceAdjustmentId())) {
            queryWrapper.eq(TailDifferenceAdjustmentDetailEntity::getTailDifferenceAdjustmentId,queryDTO.getTailDifferenceAdjustmentId());
        }
        if (ObjectUtil.isNotNull(queryDTO.getAccountBalance())) {
            queryWrapper.eq(TailDifferenceAdjustmentDetailEntity::getAccountBalance, queryDTO.getAccountBalance());
        }
        if (ObjectUtil.isNotNull(queryDTO.getMinAccountBalance())) {
            queryWrapper.ge(TailDifferenceAdjustmentDetailEntity::getAccountBalance, queryDTO.getMinAccountBalance());
        }
        if (ObjectUtil.isNotNull(queryDTO.getMinAccountBalance())) {
            queryWrapper.le(TailDifferenceAdjustmentDetailEntity::getAccountBalance, queryDTO.getMaxAccountBalance());
        }
        if (StringUtils.isNotEmpty(queryDTO.getContractCode())) {
            queryWrapper.like(TailDifferenceAdjustmentDetailEntity::getContractCode,queryDTO.getContractCode());
        }
        queryWrapper.orderByDesc(TailDifferenceAdjustmentDetailEntity::getCreateTime);
        return queryWrapper;
    }

}

