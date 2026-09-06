package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeeDetailsQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeeDetailsDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ServiceFeeDetailsVO;
import com.utfinancing.financehub.engine.finance.entity.ServiceFeeDetailsEntity;
import com.utfinancing.financehub.engine.finance.mapper.ServiceFeeDetailsMapper;
import com.utfinancing.financehub.engine.finance.service.IOrgCompanyService;
import com.utfinancing.financehub.engine.finance.service.IServiceFeeDetailsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.finance.service.IVoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author : hzhao
 * @Date : Create in 2023-11-07
 * @Description :  ServiceFeeDetails服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class ServiceFeeDetailsServiceImpl extends ServiceImpl<ServiceFeeDetailsMapper, ServiceFeeDetailsEntity> implements IServiceFeeDetailsService {

    private final ServiceFeeDetailsMapper serviceFeeDetailsMapper;
    private final IOrgCompanyService orgCompanyService;
    private final IVoucherService voucherService;
    @Override
    public Long saveServiceFeeDetails(ServiceFeeDetailsDTO dto) {
        ServiceFeeDetailsEntity entity = BeanUtil.copyProperties(dto, ServiceFeeDetailsEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateServiceFeeDetails(Long id, ServiceFeeDetailsDTO dto) {
        ServiceFeeDetailsEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public ServiceFeeDetailsDTO getServiceFeeDetailsDTOById(Long id) {
        ServiceFeeDetailsEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, ServiceFeeDetailsDTO.class);
    }

    @Override
    public IPage<ServiceFeeDetailsVO> selectPage(ServiceFeeDetailsQueryDTO queryDTO) {
        LambdaQueryWrapper<ServiceFeeDetailsEntity> queryWrapper = Wrappers.<ServiceFeeDetailsEntity>lambdaQuery();
        //这里注入查询条件
        IPage<ServiceFeeDetailsEntity> entityIPage = serviceFeeDetailsMapper.selectPage(new Page<ServiceFeeDetailsEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, ServiceFeeDetailsVO.class);
    }

    @Override
    public List<ServiceFeeDetailsEntity> selectLastPeriodData(ServiceFeeDetailsVO serviceFeeDetailsVO) {
           LambdaQueryWrapper<ServiceFeeDetailsEntity> queryWrapper = Wrappers.<ServiceFeeDetailsEntity>lambdaQuery();
        queryWrapper.select(ServiceFeeDetailsEntity::getServiceFeeAllocationNoTax,ServiceFeeDetailsEntity::getServiceFeeAllocationTaxIncluded);
        queryWrapper.eq(ServiceFeeDetailsEntity::getContractCode,serviceFeeDetailsVO.getContractCode());
        queryWrapper.eq(ServiceFeeDetailsEntity::getOrgId,serviceFeeDetailsVO.getOrgId());
        queryWrapper.eq(ServiceFeeDetailsEntity::getServiceFeeNo,serviceFeeDetailsVO.getServiceFeeNo());
        queryWrapper.eq(ServiceFeeDetailsEntity::getServiceOrgId,serviceFeeDetailsVO.getServiceOrgId());
        String lastMonth = DateUtil.format(DateUtil.offsetMonth(serviceFeeDetailsVO.getBusinessDate(), -1), "yyyy-MM");
        queryWrapper.apply("TO_CHAR(business_date, 'YYYY-MM') <= {0}", lastMonth);
        queryWrapper.orderByDesc(ServiceFeeDetailsEntity::getBusinessDate);
        queryWrapper.last("limit 1");
        return serviceFeeDetailsMapper.selectList(queryWrapper);
    }

    @Override
    public void updateVoucherStatus(List<Long> ids, String voucherStatus, Integer periodCode) {
        List<ServiceFeeDetailsEntity> serviceFeeDetailsEntities = this.list(new LambdaQueryWrapper<ServiceFeeDetailsEntity>().in(ServiceFeeDetailsEntity::getServiceFeeId,ids));
        List<String> voucherIds = serviceFeeDetailsEntities.stream()
                .map(ServiceFeeDetailsEntity::getVoucherId)
                .filter(Objects::nonNull).collect(Collectors.toList());

        voucherService.updateStatusBatchByIds(voucherIds,voucherStatus,periodCode);
    }

}

