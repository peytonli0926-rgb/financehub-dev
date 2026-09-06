package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import com.utfinancing.financehub.common.core.enums.EnableFlagEnum;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.enums.ContractStatusEnum;
import com.utfinancing.financehub.engine.enums.FinancialContractStatusEnum;
import com.utfinancing.financehub.engine.enums.SceneEnum;
import com.utfinancing.financehub.engine.finance.mapper.ContractStatusRecordMapper;
import com.utfinancing.financehub.engine.finance.model.dto.ContractInterfaceTotalQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ContractInterfaceTotalDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ContractInterfaceTotalSaveDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ContractStatusRecordDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ContractInterfaceTotalVO;
import com.utfinancing.financehub.engine.finance.entity.ContractInterfaceTotalEntity;
import com.utfinancing.financehub.engine.finance.mapper.ContractInterfaceTotalMapper;
import com.utfinancing.financehub.engine.finance.service.IContractInterfaceTotalService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.finance.service.IContractStatusRecordService;
import com.utfinancing.financehub.engine.rule.constant.RuleConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.Map;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-09
 * @Description :  ContractInterfaceTotal服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class ContractInterfaceTotalServiceImpl extends ServiceImpl<ContractInterfaceTotalMapper, ContractInterfaceTotalEntity> implements IContractInterfaceTotalService {

    private final ContractInterfaceTotalMapper contractInterfaceTotalMapper;

    @Autowired
    private final IContractStatusRecordService contractStatusRecordService;

    @Override
    public Long saveContractInterfaceTotal(ContractInterfaceTotalSaveDTO dto) {
        ContractInterfaceTotalEntity entity = BeanUtil.copyProperties(dto, ContractInterfaceTotalEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateContractInterfaceTotal(Long id, ContractInterfaceTotalDTO dto) {
        ContractInterfaceTotalEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public Long updateEntityById(ContractInterfaceTotalDTO dto) {
        ContractInterfaceTotalEntity entity = BeanUtil.copyProperties(dto, ContractInterfaceTotalEntity.class);
        entity.updateById();
        return entity.getId();
    }

    @Override
    public ContractInterfaceTotalDTO getContractInterfaceTotalDTOById(Long id) {
        ContractInterfaceTotalEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, ContractInterfaceTotalDTO.class);
    }

    @Override
    public IPage<ContractInterfaceTotalVO> selectPage(ContractInterfaceTotalQueryDTO queryDTO) {
        LambdaQueryWrapper<ContractInterfaceTotalEntity> queryWrapper = Wrappers.<ContractInterfaceTotalEntity>lambdaQuery();
        //这里注入查询条件
        IPage<ContractInterfaceTotalEntity> entityIPage = contractInterfaceTotalMapper.selectPage(new Page<ContractInterfaceTotalEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, ContractInterfaceTotalVO.class);
    }

    @Override
    public ContractInterfaceTotalDTO getByContractCode(String contractCode) {
        ContractInterfaceTotalEntity entity = this.getOne(Wrappers.<ContractInterfaceTotalEntity>lambdaQuery()
                .eq(ContractInterfaceTotalEntity::getContractCode, contractCode)
                .eq(ContractInterfaceTotalEntity::getEnableFlag, EnableFlagEnum.ENABLE.getCode()));
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, ContractInterfaceTotalDTO.class);
    }

    @Override
    public void saveFromInterfaceData(Map<String, Object> dataMap) {
        String sceneCode = MapUtil.getStr(dataMap, RuleConstant.FIELD_SCENE_CODE);
        String contractStatus = MapUtil.getStr(dataMap, RuleConstant.FIELD_CONTRACT_STATUS);
        String contractCode = MapUtil.getStr(dataMap, RuleConstant.FIELD_CONTRACT_CODE);
        //租赁收款场景如果状态为财务出库 累计
        if (SceneEnum.ZLSK.getCode().equals(sceneCode)) {
            String orgId = MapUtil.getStr(dataMap, RuleConstant.FIELD_ORG_ID);
            ContractStatusRecordDTO contractStatusRecordDTO = contractStatusRecordService.getLastByContractCodeAndOrgId(contractCode, orgId);
            if (contractStatusRecordDTO!=null){
                contractStatus = contractStatusRecordDTO.getFinancialContractStatus();
            }
            if ("财务入库".equals(contractStatus)) {
                ContractInterfaceTotalSaveDTO saveDTO = BeanUtil.copyProperties(dataMap, ContractInterfaceTotalSaveDTO.class);
                ContractInterfaceTotalDTO oldDto = this.getByContractCode(contractCode);
                if (oldDto == null) {
                    //如果不存在，新增一条记录
                    this.saveContractInterfaceTotal(saveDTO);
                    return;
                }
                //如果存在，更新累计金额
                oldDto.setRecyclePrincipalAmount(NumberUtil.add(oldDto.getRecyclePrincipalAmount(), saveDTO.getRecyclePrincipalAmount()));
                oldDto.setRecycleInterestAmount(NumberUtil.add(oldDto.getRecycleInterestAmount(), saveDTO.getRecycleInterestAmount()));
                oldDto.setRecycleDefaultInterestAmount(NumberUtil.add(oldDto.getRecycleDefaultInterestAmount(), saveDTO.getRecycleDefaultInterestAmount()));
                oldDto.setReceiveFirstAmount(NumberUtil.add(oldDto.getReceiveFirstAmount(), saveDTO.getReceiveFirstAmount()));
                oldDto.setReceiveProcedureAmount(NumberUtil.add(oldDto.getReceiveProcedureAmount(), saveDTO.getReceiveProcedureAmount()));
                oldDto.setReceiveInsuranceAmount(NumberUtil.add(oldDto.getReceiveInsuranceAmount(), saveDTO.getReceiveInsuranceAmount()));
                oldDto.setReceiveServiceAmount(NumberUtil.add(oldDto.getReceiveServiceAmount(), saveDTO.getReceiveServiceAmount()));
                oldDto.setReceiveMarginAmount(NumberUtil.add(oldDto.getReceiveMarginAmount(), saveDTO.getReceiveMarginAmount()));
                oldDto.setReceiveRetainedPrice(NumberUtil.add(oldDto.getReceiveRetainedPrice(), saveDTO.getReceiveRetainedPrice()));
                oldDto.setReceiveOtherRevenues(NumberUtil.add(oldDto.getReceiveOtherRevenues(), saveDTO.getReceiveOtherRevenues()));
                oldDto.setReceiveFirmRebate(NumberUtil.add(oldDto.getReceiveFirmRebate(), saveDTO.getReceiveFirmRebate()));
                oldDto.setReceiveTerminateProcedureAmount(NumberUtil.add(oldDto.getReceiveTerminateProcedureAmount(), saveDTO.getReceiveTerminateProcedureAmount()));
                oldDto.setReceivePenal(NumberUtil.add(oldDto.getReceivePenal(), saveDTO.getReceivePenal()));
                oldDto.setReceiveGPS(NumberUtil.add(oldDto.getReceiveGPS(), saveDTO.getReceiveGPS()));
                oldDto.setReceiveInsuranceDifferAmount(NumberUtil.add(oldDto.getReceiveInsuranceDifferAmount(), saveDTO.getReceiveInsuranceDifferAmount()));
                oldDto.setReceiveRecycleCarAmount(NumberUtil.add(oldDto.getReceiveRecycleCarAmount(), saveDTO.getReceiveRecycleCarAmount()));
                this.updateEntityById(oldDto);
            }else {
                ContractInterfaceTotalDTO oldDto = this.getByContractCode(contractCode);
                if (oldDto != null) {
                    //如果存在，则更新为不可用
                    oldDto.setEnableFlag(EnableFlagEnum.DISABLE.getCode());
                    this.updateEntityById(oldDto);
                }
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(NumberUtil.add(BigDecimal.ONE, null));
    }

}

