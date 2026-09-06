package com.utfinancing.financehub.etl.financial.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.etl.financial.model.dto.VoucherTypeQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.VoucherTypeDTO;
import com.utfinancing.financehub.etl.financial.model.vo.VoucherTypeVO;
import com.utfinancing.financehub.etl.financial.entity.VoucherTypeEntity;
import com.utfinancing.financehub.etl.financial.mapper.VoucherTypeMapper;
import com.utfinancing.financehub.etl.financial.service.IVoucherTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
 * @Description :  VoucherType服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class VoucherTypeServiceImpl extends ServiceImpl<VoucherTypeMapper, VoucherTypeEntity> implements IVoucherTypeService {

    private final VoucherTypeMapper voucherTypeMapper;

    @Override
    public Long saveVoucherType(VoucherTypeDTO dto) {
        VoucherTypeEntity entity = BeanUtil.copyProperties(dto, VoucherTypeEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateVoucherType(Long id, VoucherTypeDTO dto) {
        VoucherTypeEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public VoucherTypeDTO getVoucherTypeDTOById(Long id) {
        VoucherTypeEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, VoucherTypeDTO.class);
    }

    @Override
    public IPage<VoucherTypeVO> selectPage(VoucherTypeQueryDTO queryDTO) {
        LambdaQueryWrapper<VoucherTypeEntity> queryWrapper = Wrappers.<VoucherTypeEntity>lambdaQuery();
        //这里注入查询条件
        IPage<VoucherTypeEntity> entityIPage = voucherTypeMapper.selectPage(new Page<VoucherTypeEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, VoucherTypeVO.class);
    }

}

