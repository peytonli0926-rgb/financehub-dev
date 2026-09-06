package com.utfinancing.financehub.engine.claim.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.claim.model.dto.ClaimOrderInvoiceQueryDTO;
import com.utfinancing.financehub.engine.claim.model.dto.ClaimOrderInvoiceDTO;
import com.utfinancing.financehub.engine.claim.model.vo.ClaimOrderInvoiceVO;
import com.utfinancing.financehub.engine.claim.entity.ClaimOrderInvoiceEntity;
import com.utfinancing.financehub.engine.claim.mapper.ClaimOrderInvoiceMapper;
import com.utfinancing.financehub.engine.claim.service.IClaimOrderInvoiceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : lixin
 * @Date : Create in 2023-10-23
 * @Description :  ClaimOrderInvoice服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class ClaimOrderInvoiceServiceImpl extends ServiceImpl<ClaimOrderInvoiceMapper, ClaimOrderInvoiceEntity> implements IClaimOrderInvoiceService {

    private final ClaimOrderInvoiceMapper claimOrderInvoiceMapper;

    @Override
    public Long saveClaimOrderInvoice(ClaimOrderInvoiceDTO dto) {
        ClaimOrderInvoiceEntity entity = BeanUtil.copyProperties(dto, ClaimOrderInvoiceEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateClaimOrderInvoice(Long id, ClaimOrderInvoiceDTO dto) {
        ClaimOrderInvoiceEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public ClaimOrderInvoiceDTO getClaimOrderInvoiceDTOById(Long id) {
        ClaimOrderInvoiceEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, ClaimOrderInvoiceDTO.class);
    }

    @Override
    public IPage<ClaimOrderInvoiceVO> selectPage(ClaimOrderInvoiceQueryDTO queryDTO) {
        LambdaQueryWrapper<ClaimOrderInvoiceEntity> queryWrapper = Wrappers.<ClaimOrderInvoiceEntity>lambdaQuery();
        //这里注入查询条件
        IPage<ClaimOrderInvoiceEntity> entityIPage = claimOrderInvoiceMapper.selectPage(new Page<ClaimOrderInvoiceEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, ClaimOrderInvoiceVO.class);
    }

}

