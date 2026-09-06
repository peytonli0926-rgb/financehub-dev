package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.model.dto.OutstandingAmountInitQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.OutstandingAmountInitDTO;
import com.utfinancing.financehub.engine.finance.model.dto.SelectEndBalForInputDTO;
import com.utfinancing.financehub.engine.finance.model.vo.OutstandingAmountInitVO;
import com.utfinancing.financehub.engine.finance.entity.OutstandingAmountInitEntity;
import com.utfinancing.financehub.engine.finance.mapper.OutstandingAmountInitMapper;
import com.utfinancing.financehub.engine.finance.service.IOutstandingAmountInitService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author : robjiang
 * @Date : Create in 2025-05-15
 * @Description :  OutstandingAmountInit服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class OutstandingAmountInitServiceImpl extends ServiceImpl<OutstandingAmountInitMapper, OutstandingAmountInitEntity>
        implements IOutstandingAmountInitService {

    private final OutstandingAmountInitMapper outstandingAmountInitMapper;

    @Override
    public Long saveOutstandingAmountInit(OutstandingAmountInitDTO dto) {
        OutstandingAmountInitEntity entity = BeanUtil.copyProperties(dto, OutstandingAmountInitEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateOutstandingAmountInit(Long id, OutstandingAmountInitDTO dto) {
        OutstandingAmountInitEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public OutstandingAmountInitDTO getOutstandingAmountInitDTOById(Long id) {
        OutstandingAmountInitEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, OutstandingAmountInitDTO.class);
    }

    @Override
    public IPage<OutstandingAmountInitVO> selectPage(OutstandingAmountInitQueryDTO queryDTO) {
        LambdaQueryWrapper<OutstandingAmountInitEntity> queryWrapper = Wrappers.<OutstandingAmountInitEntity>lambdaQuery();
        //这里注入查询条件
        IPage<OutstandingAmountInitEntity> entityIPage = outstandingAmountInitMapper.selectPage(new Page<OutstandingAmountInitEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, OutstandingAmountInitVO.class);
    }

    /**
     * 查询合同的未实现收益总额
     */
    public Map<String, BigDecimal> selectEndBalFor(List<String> contractCodeList) {
        SelectEndBalForInputDTO params = new SelectEndBalForInputDTO();
        params.setContractList(contractCodeList);
        List<OutstandingAmountInitEntity> result = outstandingAmountInitMapper.selectEndBalFor(params);
        if (result == null || result.isEmpty()) {
            return new HashMap<>();
        }

        return result.stream().collect(Collectors.toMap(
                OutstandingAmountInitEntity::getContractCode, OutstandingAmountInitEntity::getEndBalanceFor, (a,b)->b));
    }

}

