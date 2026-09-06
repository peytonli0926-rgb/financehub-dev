package com.utfinancing.financehub.engine.claim.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.claim.model.dto.ClaimOrderPaymentQueryDTO;
import com.utfinancing.financehub.engine.claim.model.dto.ClaimOrderPaymentDTO;
import com.utfinancing.financehub.engine.claim.model.vo.ClaimOrderPaymentVO;
import com.utfinancing.financehub.engine.claim.entity.ClaimOrderPaymentEntity;
import com.utfinancing.financehub.engine.claim.mapper.ClaimOrderPaymentMapper;
import com.utfinancing.financehub.engine.claim.service.IClaimOrderPaymentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : lixin
 * @Date : Create in 2023-11-08
 * @Description :  ClaimOrderPayment服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class ClaimOrderPaymentServiceImpl extends ServiceImpl<ClaimOrderPaymentMapper, ClaimOrderPaymentEntity> implements IClaimOrderPaymentService {

    private final ClaimOrderPaymentMapper claimOrderPaymentMapper;

    @Override
    public Long saveClaimOrderPayment(ClaimOrderPaymentDTO dto) {
        ClaimOrderPaymentEntity entity = BeanUtil.copyProperties(dto, ClaimOrderPaymentEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateClaimOrderPayment(Long id, ClaimOrderPaymentDTO dto) {
        ClaimOrderPaymentEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public ClaimOrderPaymentDTO getClaimOrderPaymentDTOById(Long id) {
        ClaimOrderPaymentEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, ClaimOrderPaymentDTO.class);
    }

    @Override
    public IPage<ClaimOrderPaymentVO> selectPage(ClaimOrderPaymentQueryDTO queryDTO) {
        LambdaQueryWrapper<ClaimOrderPaymentEntity> queryWrapper = Wrappers.<ClaimOrderPaymentEntity>lambdaQuery();
        //这里注入查询条件
        IPage<ClaimOrderPaymentEntity> entityIPage = claimOrderPaymentMapper.selectPage(new Page<ClaimOrderPaymentEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, ClaimOrderPaymentVO.class);
    }

}

