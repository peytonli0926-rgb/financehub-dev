package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.model.dto.PayableInsuranceDetailsQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.PayableInsuranceDetailsDTO;
import com.utfinancing.financehub.engine.finance.model.vo.PayableInsuranceDetailsVO;
import com.utfinancing.financehub.engine.finance.entity.PayableInsuranceDetailsEntity;
import com.utfinancing.financehub.engine.finance.mapper.PayableInsuranceDetailsMapper;
import com.utfinancing.financehub.engine.finance.service.IPayableInsuranceDetailsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : hzhao
 * @Date : Create in 2023-10-24
 * @Description :  PayableInsuranceDetails服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class PayableInsuranceDetailsServiceImpl extends ServiceImpl<PayableInsuranceDetailsMapper, PayableInsuranceDetailsEntity> implements IPayableInsuranceDetailsService {

    private final PayableInsuranceDetailsMapper payableInsuranceDetailsMapper;

    @Override
    public Long savePayableInsuranceDetails(PayableInsuranceDetailsDTO dto) {
        PayableInsuranceDetailsEntity entity = BeanUtil.copyProperties(dto, PayableInsuranceDetailsEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updatePayableInsuranceDetails(Long id, PayableInsuranceDetailsDTO dto) {
        PayableInsuranceDetailsEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public PayableInsuranceDetailsDTO getPayableInsuranceDetailsDTOById(Long id) {
        PayableInsuranceDetailsEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, PayableInsuranceDetailsDTO.class);
    }

    @Override
    public IPage<PayableInsuranceDetailsVO> selectPage(PayableInsuranceDetailsQueryDTO queryDTO) {
        LambdaQueryWrapper<PayableInsuranceDetailsEntity> queryWrapper = Wrappers.<PayableInsuranceDetailsEntity>lambdaQuery();
        //这里注入查询条件
        IPage<PayableInsuranceDetailsEntity> entityIPage = payableInsuranceDetailsMapper.selectPage(new Page<PayableInsuranceDetailsEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, PayableInsuranceDetailsVO.class);
    }

}

