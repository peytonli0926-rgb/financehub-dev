package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.utfinancing.financehub.engine.finance.model.dto.ReceiveTaxDetailDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ReceiveTaxDetailExcelDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ReceiveTaxDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ReceiveTaxDetailVO;
import com.utfinancing.financehub.engine.finance.entity.ReceiveTaxDetailEntity;
import com.utfinancing.financehub.engine.finance.mapper.ReceiveTaxDetailMapper;
import com.utfinancing.financehub.engine.finance.service.IReceiveTaxDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author : bruyang
 * @Date : Create in 2024-03-20
 * @Description :  ReceiveTaxDetail服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class ReceiveTaxDetailServiceImpl extends ServiceImpl<ReceiveTaxDetailMapper, ReceiveTaxDetailEntity> implements IReceiveTaxDetailService {

    private final ReceiveTaxDetailMapper receiveTaxDetailMapper;

    @Override
    public Long saveReceiveTaxDetail(ReceiveTaxDetailDTO dto) {
        ReceiveTaxDetailEntity entity = BeanUtil.copyProperties(dto, ReceiveTaxDetailEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateReceiveTaxDetail(Long id, ReceiveTaxDetailDTO dto) {
        ReceiveTaxDetailEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public ReceiveTaxDetailDTO getReceiveTaxDetailDTOById(Long id) {
        ReceiveTaxDetailEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, ReceiveTaxDetailDTO.class);
    }

    @Override
    public IPage<ReceiveTaxDetailVO> selectPage(ReceiveTaxDetailQueryDTO queryDTO) {
        LambdaQueryWrapper<ReceiveTaxDetailEntity> queryWrapper = Wrappers.<ReceiveTaxDetailEntity>lambdaQuery();
        // 这里注入查询条件
        IPage<ReceiveTaxDetailEntity> entityIPage = receiveTaxDetailMapper.selectPage(new Page<ReceiveTaxDetailEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, ReceiveTaxDetailVO.class);
    }

    /**
     * 上传发票明细
     *
     * @param file
     * @return
     */
    @Override
    public Boolean importFile(MultipartFile file) {
        try {
            ExcelUtil<ReceiveTaxDetailExcelDTO> util = new ExcelUtil<ReceiveTaxDetailExcelDTO>(ReceiveTaxDetailExcelDTO.class);
            List<ReceiveTaxDetailExcelDTO> receiveTaxDetailExcelDTOList = util.importExcel(file.getInputStream());

            checkData(receiveTaxDetailExcelDTOList);
            List<String> invoiceNoList = receiveTaxDetailExcelDTOList.stream().map(ReceiveTaxDetailExcelDTO::getInvoiceNumber).distinct().collect(Collectors.toList());

            List<ReceiveTaxDetailEntity> list = list(new LambdaQueryWrapper<ReceiveTaxDetailEntity>()
                    .in(ReceiveTaxDetailEntity::getInvoiceNumber, invoiceNoList));
            List<Long> existIdList = Lists.newArrayList();
            receiveTaxDetailExcelDTOList.stream().forEach(a -> {
                // 按发票号码、备注、商品名称、期数、金额维度，重复覆盖原数据
                List<ReceiveTaxDetailEntity> existList = list.stream().filter(b -> ObjectUtil.equals(a.getRemark(), b.getRemark())
                        && ObjectUtil.equals(a.getInvoiceNumber(), b.getInvoiceNumber())
                        && ObjectUtil.equals(a.getProductName(), b.getProductName())
                        && ObjectUtil.equals(a.getModel(), b.getModel())
                        && ObjectUtil.equals(a.getTotalAmount(), b.getTotalAmount())
                ).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(existList)) {
                    // 存在，则删除原数据
                    existIdList.addAll(existList.stream().map(ReceiveTaxDetailEntity::getId).collect(Collectors.toList()));
                }
            });
            // 删除已存在的数据
            removeBatchByIds(existIdList);
            // 批量保存
            saveBatch(BeanUtil.copyToList(receiveTaxDetailExcelDTOList, ReceiveTaxDetailEntity.class));
        } catch (Exception e) {
            throw new ServiceException("上传文件失败，失败原因:" + e.getMessage());
        }
        return Boolean.TRUE;
    }

    /**
     * 校验上传数据
     *
     * @param receiveTaxDetailExcelDTOList
     */
    private void checkData(List<ReceiveTaxDetailExcelDTO> receiveTaxDetailExcelDTOList) {
        if (CollectionUtils.isEmpty(receiveTaxDetailExcelDTOList)) {
            throw new ServiceException("文件无数据");
        }
        Set<String> unionSet = new HashSet<>();
        receiveTaxDetailExcelDTOList.stream().forEach(a -> {
            if (ObjectUtil.isNull(a.getRemark())) {
                throw new ServiceException("备注不可以为空");
            }
            if (ObjectUtil.isNull(a.getInvoiceNumber())) {
                throw new ServiceException("发票号码不可以为空");
            }
            if (ObjectUtil.isNull(a.getProductName())) {
                throw new ServiceException("商品名称不可以为空");
            }
            if (ObjectUtil.isNull(a.getTaxAmount())) {
                throw new ServiceException("税额不可以为空");
            }
            String uniqueIdentifier = a.getRemark() + a.getInvoiceNumber() + a.getProductName() + a.getModel()+a.getTotalAmount();
            if (!unionSet.add(uniqueIdentifier)) {
                throw new ServiceException("文件存在重复的备注[" + a.getRemark() + "]+发票号码[" + a.getInvoiceNumber() +
                        "]+商品名称[" + a.getProductName() + "]+期数[" + a.getModel() + "]+金额["+a.getTotalAmount()+"],请检查");
            }
        });
    }


}

