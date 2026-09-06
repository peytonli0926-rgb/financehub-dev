package com.utfinancing.financehub.engine.verification.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.verification.model.dto.CourtCostDetailsQueryDTO;
import com.utfinancing.financehub.engine.verification.model.dto.CourtCostDetailsDTO;
import com.utfinancing.financehub.engine.verification.model.vo.CourtCostDetailsVO;
import com.utfinancing.financehub.engine.verification.entity.CourtCostDetailsEntity;
import com.utfinancing.financehub.engine.verification.mapper.CourtCostDetailsMapper;
import com.utfinancing.financehub.engine.verification.model.vo.CourtCostReportFormVO;
import com.utfinancing.financehub.engine.verification.service.ICourtCostDetailsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author : bruyang
 * @Date : Create in 2023-10-23
 * @Description :  CourtCostDetails服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class CourtCostDetailsServiceImpl extends ServiceImpl<CourtCostDetailsMapper, CourtCostDetailsEntity> implements ICourtCostDetailsService {

    private final CourtCostDetailsMapper courtCostDetailsMapper;

    @Override
    public Long saveCourtCostDetails(CourtCostDetailsDTO dto) {
        CourtCostDetailsEntity entity = BeanUtil.copyProperties(dto, CourtCostDetailsEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateCourtCostDetails(Long id, CourtCostDetailsDTO dto) {
        CourtCostDetailsEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public CourtCostDetailsDTO getCourtCostDetailsDTOById(Long id) {
        CourtCostDetailsEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, CourtCostDetailsDTO.class);
    }

    @Override
    public IPage<CourtCostDetailsVO> selectPage(CourtCostDetailsQueryDTO queryDTO) {
        LambdaQueryWrapper<CourtCostDetailsEntity> queryWrapper = getQueryWrapper(queryDTO);
        //这里注入查询条件
        IPage<CourtCostDetailsEntity> entityIPage = courtCostDetailsMapper.selectPage(new Page<CourtCostDetailsEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, CourtCostDetailsVO.class);
    }

    @Override
    public List<CourtCostDetailsVO> listByCondition(CourtCostDetailsQueryDTO queryDTO) {
        return ListBeanUtil.copyList(list(getQueryWrapper(queryDTO)), CourtCostDetailsVO.class);
    }

    @Override
    public Boolean removeBatchByCourtCostIdList(List<Long> courtCostIdList) {
        CourtCostDetailsQueryDTO queryDTO = new CourtCostDetailsQueryDTO();
        queryDTO.setCourtCostIdList(courtCostIdList);
        List<CourtCostDetailsVO> costDetailsVOList = listByCondition(queryDTO);
        if (CollectionUtil.isEmpty(costDetailsVOList)) {
            return Boolean.TRUE;
        }
        return this.removeBatchByIds(costDetailsVOList.stream().map(CourtCostDetailsVO::getId).collect(Collectors.toList()));
    }

    @Override
    public IPage<CourtCostReportFormVO> selectReportFormPage(CourtCostDetailsQueryDTO queryDTO) {
        Page page = new Page(queryDTO.getPageNum(), queryDTO.getPageSize());
        return courtCostDetailsMapper.selectReportFormPage(page,queryDTO);
    }

    public LambdaQueryWrapper<CourtCostDetailsEntity> getQueryWrapper(CourtCostDetailsQueryDTO queryDTO){
        LambdaQueryWrapper<CourtCostDetailsEntity> queryWrapper = Wrappers.<CourtCostDetailsEntity>lambdaQuery();
        if (CollectionUtil.isNotEmpty(queryDTO.getCourtCostIdList())) {
            queryWrapper.in(CourtCostDetailsEntity::getCourtCostId, queryDTO.getCourtCostIdList());
        }
        if (ObjectUtil.isNotEmpty(queryDTO.getCourtCostId())) {
            queryWrapper.eq(CourtCostDetailsEntity::getCourtCostId, queryDTO.getCourtCostId());
        }
        if (StringUtils.isNotEmpty(queryDTO.getContractCode())) {
            queryWrapper.eq(CourtCostDetailsEntity::getContractCode, queryDTO.getContractCode());
        }
        if (StringUtils.isNotEmpty(queryDTO.getCostCenter())) {
            queryWrapper.eq(CourtCostDetailsEntity::getCostCenter, queryDTO.getCostCenter());
        }
        if (ObjectUtil.isNotEmpty(queryDTO.getAccountDate())) {
            queryWrapper.eq(CourtCostDetailsEntity::getAccountDate, queryDTO.getAccountDate());
        }
        if (ObjectUtil.isNotEmpty(queryDTO.getStartAccountDate())) {
            queryWrapper.ge(CourtCostDetailsEntity::getAccountDate, queryDTO.getStartAccountDate());
        }
        if (ObjectUtil.isNotEmpty(queryDTO.getEndAccountDate())) {
            queryWrapper.le(CourtCostDetailsEntity::getAccountDate, queryDTO.getEndAccountDate());
        }
        if (StringUtils.isNotEmpty(queryDTO.getOrgId())) {
            queryWrapper.eq(CourtCostDetailsEntity::getOrgId, queryDTO.getOrgId());
        }
        return queryWrapper;
    }

    @Override
    public List<CourtCostReportFormVO> selectReportFormDetails(CourtCostDetailsQueryDTO queryDTO) {
        return courtCostDetailsMapper.selectReportFormDetails(queryDTO);
    }

}

