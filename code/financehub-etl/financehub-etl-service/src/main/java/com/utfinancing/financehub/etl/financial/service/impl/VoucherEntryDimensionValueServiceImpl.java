package com.utfinancing.financehub.etl.financial.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.etl.financial.entity.VoucherDimensionValueEntity;
import com.utfinancing.financehub.etl.financial.model.dto.VoucherEntryDimensionValueQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.VoucherEntryDimensionValueDTO;
import com.utfinancing.financehub.etl.financial.model.vo.VoucherEntryDimensionValueVO;
import com.utfinancing.financehub.etl.financial.entity.VoucherEntryDimensionValueEntity;
import com.utfinancing.financehub.etl.financial.mapper.VoucherEntryDimensionValueMapper;
import com.utfinancing.financehub.etl.financial.service.IVoucherEntryDimensionValueService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : bruyang
 * @Date : Create in 2024-07-19
 * @Description :  VoucherEntryDimensionValue服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class VoucherEntryDimensionValueServiceImpl extends ServiceImpl<VoucherEntryDimensionValueMapper, VoucherEntryDimensionValueEntity> implements IVoucherEntryDimensionValueService {

    private final VoucherEntryDimensionValueMapper voucherEntryDimensionValueMapper;

    @Override
    public Long saveVoucherEntryDimensionValue(VoucherEntryDimensionValueDTO dto) {
        VoucherEntryDimensionValueEntity entity = BeanUtil.copyProperties(dto, VoucherEntryDimensionValueEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateVoucherEntryDimensionValue(Long id, VoucherEntryDimensionValueDTO dto) {
        VoucherEntryDimensionValueEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public VoucherEntryDimensionValueDTO getVoucherEntryDimensionValueDTOById(Long id) {
        VoucherEntryDimensionValueEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, VoucherEntryDimensionValueDTO.class);
    }

    @Override
    public IPage<VoucherEntryDimensionValueVO> selectPage(VoucherEntryDimensionValueQueryDTO queryDTO) {
        LambdaQueryWrapper<VoucherEntryDimensionValueEntity> queryWrapper = Wrappers.<VoucherEntryDimensionValueEntity>lambdaQuery();
        //这里注入查询条件
        IPage<VoucherEntryDimensionValueEntity> entityIPage = voucherEntryDimensionValueMapper.selectPage(new Page<VoucherEntryDimensionValueEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, VoucherEntryDimensionValueVO.class);
    }

    /**
     * 取得业务主键
     */
    @Override
    public String getBusinessKey(VoucherEntryDimensionValueEntity params) {
        StringBuffer businessKey = new StringBuffer();
        if (StringUtils.isNotEmpty(params.getOrgId())) {
            businessKey.append(params.getOrgId()).append("|");
        }
        if (StringUtils.isNotEmpty(params.getAccountCode())) {
            businessKey.append(params.getAccountCode()).append("|");
        }
//        if (StringUtils.isNotEmpty(params.getSourceSystem())) {
//            businessKey.append(params.getSourceSystem()).append("|");
//        }
        return businessKey.deleteCharAt(businessKey.length() - 1).toString();
    }
}

