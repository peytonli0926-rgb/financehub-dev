package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.constant.ExceptionConstant;
import com.utfinancing.financehub.engine.finance.entity.ContractEntity;
import com.utfinancing.financehub.engine.finance.mapper.ContractMapper;
import com.utfinancing.financehub.engine.finance.model.dto.ReceiveTaxQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ReceiveTaxDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ReceiveTaxVO;
import com.utfinancing.financehub.engine.finance.entity.ReceiveTaxEntity;
import com.utfinancing.financehub.engine.finance.mapper.ReceiveTaxMapper;
import com.utfinancing.financehub.engine.finance.service.IReceiveTaxService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author : bruyang
 * @Date : Create in 2024-03-20
 * @Description :  ReceiveTax服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class ReceiveTaxServiceImpl extends ServiceImpl<ReceiveTaxMapper, ReceiveTaxEntity> implements IReceiveTaxService {

    private final ReceiveTaxMapper receiveTaxMapper;
    private final ContractMapper contractMapper;

    @Override
    public Long saveReceiveTax(ReceiveTaxDTO dto) {
        ReceiveTaxEntity entity = BeanUtil.copyProperties(dto, ReceiveTaxEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateReceiveTax(Long id, ReceiveTaxDTO dto) {
        ReceiveTaxEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public ReceiveTaxDTO getReceiveTaxDTOById(Long id) {
        ReceiveTaxEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, ReceiveTaxDTO.class);
    }

    /**
     * 分页查询
     *
     * @param queryDTO
     * @return
     */
    @Override
    public IPage<ReceiveTaxVO> selectPage(ReceiveTaxQueryDTO queryDTO) {
        Page page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        IPage<ReceiveTaxVO> receiveTaxVOIPage = receiveTaxMapper.selectPageByMapper(page, queryDTO);
        List<ReceiveTaxVO> receiveTaxVOList = receiveTaxVOIPage.getRecords();
        //设置数据
        setReceiveData(receiveTaxVOList);
        return receiveTaxVOIPage;
    }

    @Override
    public List<ReceiveTaxVO> selectList(ReceiveTaxQueryDTO queryDTO) {
        List<ReceiveTaxVO> receiveTaxVOList = receiveTaxMapper.selectPageByMapper(queryDTO);
        //设置数据
        setReceiveData(receiveTaxVOList);
        return receiveTaxVOList;
    }

    /**
     * 设置数据
     *
     * @param receiveTaxVOList
     */
    private void setReceiveData(List<ReceiveTaxVO> receiveTaxVOList) {
        if (CollectionUtils.isEmpty(receiveTaxVOList)) {
            return;
        }

        List<String> contractCodeList = receiveTaxVOList.stream().map(ReceiveTaxVO::getContractCode).collect(Collectors.toList());
        Map<String, List<ContractEntity>> contractCodeMap = contractMapper.selectList(new LambdaQueryWrapper<ContractEntity>().in(ContractEntity::getContractCode, contractCodeList))
                .stream().collect(Collectors.groupingBy(ContractEntity::getContractCode));
        receiveTaxVOList.stream().forEach(a -> {
            if (ObjectUtil.equal("否", a.getIsInvoiced()) && CollectionUtils.isNotEmpty(contractCodeMap)) {
                // 当是否开票为否时，取合同表tax_rate（按合同维度查，存在多条则取有值的最新入库那条）
                List<ContractEntity> list = contractCodeMap.get(a.getContractCode());
                if(CollectionUtils.isNotEmpty(list)) {
                    ContractEntity contractEntity = list.stream().filter(o->ObjectUtil.isNotEmpty(o.getTaxRate())).max(Comparator.comparing(ContractEntity::getId)).orElse(null);
                    if (ObjectUtil.isNotEmpty(contractEntity)) {
                        a.setTaxRate(contractEntity.getTaxRate());
                    }
                }
            }
            if(!NumberUtil.equals(a.getTaxValue(),a.getInvoiceDetailTax())){
                //对比税额、开票明细数据，若不相等则提示差异
                a.setDifferenceSituation(ExceptionConstant.EXCEPTION_TAX_DETAIL_DIFFERENT);
            }

        });
    }

}

