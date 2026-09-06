package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.model.dto.LeaseIncomeUploadRecordQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.LeaseIncomeUploadRecordDTO;
import com.utfinancing.financehub.engine.finance.model.vo.LeaseIncomeUploadRecordVO;
import com.utfinancing.financehub.engine.finance.entity.LeaseIncomeUploadRecordEntity;
import com.utfinancing.financehub.engine.finance.mapper.LeaseIncomeUploadRecordMapper;
import com.utfinancing.financehub.engine.finance.service.ILeaseIncomeUploadRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : robjiang
 * @Date : Create in 2025-11-20
 * @Description :  LeaseIncomeUploadRecord服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class LeaseIncomeUploadRecordServiceImpl extends ServiceImpl<LeaseIncomeUploadRecordMapper, LeaseIncomeUploadRecordEntity> implements ILeaseIncomeUploadRecordService {

    private final LeaseIncomeUploadRecordMapper leaseIncomeUploadRecordMapper;

    @Override
    public Long saveLeaseIncomeUploadRecord(LeaseIncomeUploadRecordDTO dto) {
        LeaseIncomeUploadRecordEntity entity = BeanUtil.copyProperties(dto, LeaseIncomeUploadRecordEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateLeaseIncomeUploadRecord(Long id, LeaseIncomeUploadRecordDTO dto) {
        LeaseIncomeUploadRecordEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public LeaseIncomeUploadRecordDTO getLeaseIncomeUploadRecordDTOById(Long id) {
        LeaseIncomeUploadRecordEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, LeaseIncomeUploadRecordDTO.class);
    }

    @Override
    public IPage<LeaseIncomeUploadRecordVO> selectPage(LeaseIncomeUploadRecordQueryDTO queryDTO) {
        LambdaQueryWrapper<LeaseIncomeUploadRecordEntity> queryWrapper = Wrappers.<LeaseIncomeUploadRecordEntity>lambdaQuery();
        //这里注入查询条件
        IPage<LeaseIncomeUploadRecordEntity> entityIPage = leaseIncomeUploadRecordMapper.selectPage(new Page<LeaseIncomeUploadRecordEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, LeaseIncomeUploadRecordVO.class);
    }

}

