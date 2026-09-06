package com.utfinancing.financehub.engine.integration.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.integration.model.dto.ExternalVoucherEntryQueryDTO;
import com.utfinancing.financehub.engine.integration.model.dto.ExternalVoucherEntryDTO;
import com.utfinancing.financehub.engine.integration.model.vo.ExternalVoucherEntryVO;
import com.utfinancing.financehub.engine.integration.entity.ExternalVoucherEntryEntity;
import com.utfinancing.financehub.engine.integration.mapper.ExternalVoucherEntryMapper;
import com.utfinancing.financehub.engine.integration.service.IExternalVoucherEntryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : lixin
 * @Date : Create in 2023-10-31
 * @Description :  ExternalVoucherEntry服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class ExternalVoucherEntryServiceImpl extends ServiceImpl<ExternalVoucherEntryMapper, ExternalVoucherEntryEntity> implements IExternalVoucherEntryService {

    private final ExternalVoucherEntryMapper externalVoucherEntryMapper;

    @Override
    public Long saveExternalVoucherEntry(ExternalVoucherEntryDTO dto) {
        ExternalVoucherEntryEntity entity = BeanUtil.copyProperties(dto, ExternalVoucherEntryEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateExternalVoucherEntry(Long id, ExternalVoucherEntryDTO dto) {
        ExternalVoucherEntryEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public ExternalVoucherEntryDTO getExternalVoucherEntryDTOById(Long id) {
        ExternalVoucherEntryEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, ExternalVoucherEntryDTO.class);
    }

    @Override
    public IPage<ExternalVoucherEntryVO> selectPage(ExternalVoucherEntryQueryDTO queryDTO) {
        LambdaQueryWrapper<ExternalVoucherEntryEntity> queryWrapper = Wrappers.<ExternalVoucherEntryEntity>lambdaQuery();
        //这里注入查询条件
        IPage<ExternalVoucherEntryEntity> entityIPage = externalVoucherEntryMapper.selectPage(new Page<ExternalVoucherEntryEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, ExternalVoucherEntryVO.class);
    }

}

