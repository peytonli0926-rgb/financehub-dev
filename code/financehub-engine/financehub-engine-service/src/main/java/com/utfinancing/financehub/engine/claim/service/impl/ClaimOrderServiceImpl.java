package com.utfinancing.financehub.engine.claim.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.claim.entity.*;
import com.utfinancing.financehub.engine.claim.model.dto.*;
import com.utfinancing.financehub.engine.claim.model.vo.ClaimOrderVO;
import com.utfinancing.financehub.engine.claim.mapper.ClaimOrderMapper;
import com.utfinancing.financehub.engine.claim.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : lixin
 * @Date : Create in 2023-10-23
 * @Description :  ClaimOrder服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class ClaimOrderServiceImpl extends ServiceImpl<ClaimOrderMapper, ClaimOrderEntity> implements IClaimOrderService {

    private final ClaimOrderMapper claimOrderMapper;
    private final IClaimOrderDetailService claimOrderDetailService;
    private final IClaimOrderInvoiceService claimOrderInvoiceService;
    private final IClaimOrderSpecialService claimOrderSpecialService;
    private final IClaimOrderPaymentService claimOrderPaymentService;

    @Override
    public Long saveClaimOrder(ClaimOrderDTO dto) {
        ClaimOrderEntity entity = BeanUtil.copyProperties(dto, ClaimOrderEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateClaimOrder(Long id, ClaimOrderDTO dto) {
        ClaimOrderEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public ClaimOrderDTO getClaimOrderDTOById(Long id) {
        ClaimOrderEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, ClaimOrderDTO.class);
    }

    @Override
    public IPage<ClaimOrderVO> selectPage(ClaimOrderQueryDTO queryDTO) {
        LambdaQueryWrapper<ClaimOrderEntity> queryWrapper = Wrappers.<ClaimOrderEntity>lambdaQuery();
        //这里注入查询条件
        IPage<ClaimOrderEntity> entityIPage = claimOrderMapper.selectPage(new Page<ClaimOrderEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, ClaimOrderVO.class);
    }

    @Override
    public Long saveOrderTransactionData(ClaimOrderSaveDTO saveDTO) {
        ClaimOrderEntity orderEntity = BeanUtil.copyProperties(saveDTO, ClaimOrderEntity.class);
        this.save(orderEntity);
        if (CollectionUtil.isNotEmpty(saveDTO.getClaimDetailList())){
            List<ClaimOrderDetailEntity> detailEntityList = ListBeanUtil.copyList(saveDTO.getClaimDetailList(), ClaimOrderDetailEntity.class);
            for (ClaimOrderDetailEntity e : detailEntityList) {
                e.setClaimOrderId(orderEntity.getId());
                e.setEmployeeNo(saveDTO.getReqUserNo());
            }
            claimOrderDetailService.saveBatch(detailEntityList);
        }
        if (CollectionUtil.isNotEmpty(saveDTO.getInvoiceList())){
            List<ClaimOrderInvoiceEntity> invoiceEntityList = ListBeanUtil.copyList(saveDTO.getInvoiceList(), ClaimOrderInvoiceEntity.class);
            invoiceEntityList.forEach(e -> e.setClaimOrderId(orderEntity.getId()));
            claimOrderInvoiceService.saveBatch(invoiceEntityList);
        }
        if (CollectionUtil.isNotEmpty(saveDTO.getSpecialExpenseList())){
            List<ClaimOrderSpecialEntity> specialEntityList = ListBeanUtil.copyList(saveDTO.getSpecialExpenseList(), ClaimOrderSpecialEntity.class);
            specialEntityList.forEach(e -> e.setClaimOrderId(orderEntity.getId()));
            claimOrderSpecialService.saveBatch(specialEntityList);
        }
        if (CollectionUtil.isNotEmpty(saveDTO.getPaymentList())){
            List<ClaimOrderPaymentEntity> paymentEntityList = ListBeanUtil.copyList(saveDTO.getPaymentList(), ClaimOrderPaymentEntity.class);
            paymentEntityList.forEach(e -> e.setClaimOrderId(orderEntity.getId()));
            claimOrderPaymentService.saveBatch(paymentEntityList);
        }
        return orderEntity.getId();
    }

    @Override
    public List<ClaimOrderVO> selectByCondition(ClaimOrderQueryDTO queryDTO) {
        return claimOrderMapper.selectByCondition(queryDTO);
    }

}

