package com.utfinancing.financehub.etl.financial.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.etl.financial.model.dto.VoucherEntryQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.VoucherEntryDTO;
import com.utfinancing.financehub.etl.financial.model.vo.VoucherEntryVO;
import com.utfinancing.financehub.etl.financial.entity.VoucherEntryEntity;
import com.utfinancing.financehub.etl.financial.mapper.VoucherEntryMapper;
import com.utfinancing.financehub.etl.financial.service.IVoucherEntryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : lixin
 * @Date : Create in 2023-11-16
 * @Description :  VoucherEntry服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class VoucherEntryServiceImpl extends ServiceImpl<VoucherEntryMapper, VoucherEntryEntity> implements IVoucherEntryService {

    private final VoucherEntryMapper voucherEntryMapper;

    @Override
    public Long saveVoucherEntry(VoucherEntryDTO dto) {
        VoucherEntryEntity entity = BeanUtil.copyProperties(dto, VoucherEntryEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateVoucherEntry(Long id, VoucherEntryDTO dto) {
        VoucherEntryEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public VoucherEntryDTO getVoucherEntryDTOById(Long id) {
        VoucherEntryEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, VoucherEntryDTO.class);
    }

    @Override
    public IPage<VoucherEntryVO> selectPage(VoucherEntryQueryDTO queryDTO) {
        LambdaQueryWrapper<VoucherEntryEntity> queryWrapper = Wrappers.<VoucherEntryEntity>lambdaQuery();
        //这里注入查询条件
        IPage<VoucherEntryEntity> entityIPage = voucherEntryMapper.selectPage(new Page<VoucherEntryEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, VoucherEntryVO.class);
    }

}

