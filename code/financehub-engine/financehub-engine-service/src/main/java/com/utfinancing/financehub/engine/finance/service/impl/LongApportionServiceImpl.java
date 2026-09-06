package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.entity.LongReceivableRegisterEntity;
import com.utfinancing.financehub.engine.finance.entity.LongRepaymentPlanEntity;
import com.utfinancing.financehub.engine.finance.mapper.LongReceivableRegisterMapper;
import com.utfinancing.financehub.engine.finance.model.dto.LongApportionQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.LongApportionDTO;
import com.utfinancing.financehub.engine.finance.model.vo.LongApportionVO;
import com.utfinancing.financehub.engine.finance.entity.LongApportionEntity;
import com.utfinancing.financehub.engine.finance.mapper.LongApportionMapper;
import com.utfinancing.financehub.engine.finance.service.ILongApportionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-12
 * @Description :  LongApportion服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class LongApportionServiceImpl extends ServiceImpl<LongApportionMapper, LongApportionEntity> implements ILongApportionService {

    private final LongApportionMapper longApportionMapper;
    private final LongReceivableRegisterMapper longReceivableRegisterMapper;

    @Override
    public Long saveLongApportion(LongApportionDTO dto) {
        LongApportionEntity entity = BeanUtil.copyProperties(dto, LongApportionEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateLongApportion(Long id, LongApportionDTO dto) {
        LongApportionEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public LongApportionDTO getLongApportionDTOById(Long id) {
        LongApportionEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, LongApportionDTO.class);
    }

    @Override
    public IPage<LongApportionVO> selectPage(LongApportionQueryDTO queryDTO) {
        LambdaQueryWrapper<LongApportionEntity> queryWrapper = Wrappers.<LongApportionEntity>lambdaQuery();
        // 这里注入查询条件
        setQueryCondition(queryDTO, queryWrapper);
        IPage<LongApportionEntity> entityIPage = longApportionMapper.selectPage(new Page<LongApportionEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);

        return ListBeanUtil.copyPage(entityIPage, LongApportionVO.class);
    }

    /**
     * 设置查询条件
     *
     * @param queryDTO
     * @param queryWrapper
     */
    private void setQueryCondition(LongApportionQueryDTO queryDTO, LambdaQueryWrapper<LongApportionEntity> queryWrapper) {
        if(ObjectUtil.isNotEmpty(queryDTO.getLongRegisterIdList())){
            queryWrapper.in(LongApportionEntity::getLongRegisterId, queryDTO.getLongRegisterIdList());
        }
        if (CollectionUtils.isNotEmpty(queryDTO.getIdList())) {
            queryWrapper.in(LongApportionEntity::getId, queryDTO.getIdList());
        }
        queryWrapper.orderByAsc(LongApportionEntity::getLongReceivableNumber,LongApportionEntity::getContractCode,LongApportionEntity::getReceivableDate);
    }

    @Override
    public List<LongApportionVO> selectList(LongApportionQueryDTO queryDTO) {
        LambdaQueryWrapper<LongApportionEntity> queryWrapper = Wrappers.<LongApportionEntity>lambdaQuery();
        // 这里注入查询条件
        setQueryCondition(queryDTO, queryWrapper);
        List<LongApportionEntity> list = longApportionMapper.selectList(queryWrapper);
        return ListBeanUtil.copyList(list, LongApportionVO.class);
    }

    /**
     * 根据长期应收款id删除分摊表
     * @param longRegisterIdList
     */
    @Override
    public void removeByLongRegisterIdList(List<Long> longRegisterIdList) {
        if (CollUtil.isNotEmpty(longRegisterIdList)){
            remove(new LambdaUpdateWrapper<LongApportionEntity>().in(LongApportionEntity::getLongRegisterId,longRegisterIdList));
        }
    }

}

