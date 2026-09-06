package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.utfinancing.financehub.engine.constants.Constants;
import com.utfinancing.financehub.engine.enums.LeaseTypeEnum;
import com.utfinancing.financehub.engine.enums.SystemEnum;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.entity.RepaymentPlanExceldataEntity;
import com.utfinancing.financehub.engine.finance.mapper.RepaymentPlanExceldataMapper;
import com.utfinancing.financehub.engine.finance.model.dto.ContractDTO;
import com.utfinancing.financehub.engine.finance.service.IRepaymentPlanExceldataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.etl.model.dto.SelectReceiveRepaymentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author : robjiang
 * @Date : Create in 2024-01-23
 * @Description :  RepaymentPlanExceldata服务实现类
 * @Modified :
 */
@Service
@Transactional
public class RepaymentPlanExceldataServiceImpl extends ServiceImpl<RepaymentPlanExceldataMapper, RepaymentPlanExceldataEntity>
        implements IRepaymentPlanExceldataService {

    @Resource
    private RepaymentPlanExceldataMapper repaymentPlanExceldataMapper;

    /**
     * 保存最新的回笼数据
     */
    public void saveNewReceivedRepayment(List<SelectReceiveRepaymentDTO> receivedRepaymentDTOList) {
        if (receivedRepaymentDTOList == null || receivedRepaymentDTOList.isEmpty()) {
            return;
        }

        // 删除原有的回笼数据
        LambdaQueryWrapper<RepaymentPlanExceldataEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RepaymentPlanExceldataEntity::getContractCode, receivedRepaymentDTOList.get(0).getContractCode());
        this.remove(wrapper);

        // 保存最新的回笼数据
        List<RepaymentPlanExceldataEntity> repaymentPlanExceldataEntityList = BeanUtil.copyToList(
                receivedRepaymentDTOList, RepaymentPlanExceldataEntity.class);
        this.saveBatch(repaymentPlanExceldataEntityList);
    }

    /**
     * 回笼数据合并
     */
    public List<RepaymentPlanExceldataEntity> receivedRepaymentMerge(List<RepaymentPlanExceldataEntity> receivedRepaymentDTOList) {
        if (receivedRepaymentDTOList == null || receivedRepaymentDTOList.isEmpty()) {
            return new ArrayList<>();
        }

        List<RepaymentPlanExceldataEntity> result = new ArrayList<>();
        Map<String, List<RepaymentPlanExceldataEntity>> receivedRepaymentMap = receivedRepaymentDTOList.stream().
                collect(Collectors.groupingBy(e->e.getContractCode().concat("-").concat(String.valueOf(e.getPeriods()))));
        for (String key : receivedRepaymentMap.keySet()) {
            RepaymentPlanExceldataEntity entity = new RepaymentPlanExceldataEntity();
            List<RepaymentPlanExceldataEntity> curReceivedRepaymentList = receivedRepaymentMap.get(key);
            entity.setContractCode(curReceivedRepaymentList.get(0).getContractCode());
            entity.setPeriods(curReceivedRepaymentList.get(0).getPeriods());
            entity.setSystemCode(curReceivedRepaymentList.get(0).getSystemCode());
            entity.setActualRepaymentRentAmount(curReceivedRepaymentList.stream().
                    map(RepaymentPlanExceldataEntity::getActualRepaymentRentAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
            entity.setActualRepaymentPrincipalAmount(curReceivedRepaymentList.stream().
                    map(RepaymentPlanExceldataEntity::getActualRepaymentPrincipalAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
            entity.setActualRepaymentInteresAmount(curReceivedRepaymentList.stream().
                    map(RepaymentPlanExceldataEntity::getActualRepaymentInteresAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
            entity.setPlanDate(curReceivedRepaymentList.get(curReceivedRepaymentList.size() - 1).getPlanDate());
            result.add(entity);
        }
        return result;
    }

    /**
     * 回笼的总金额计算
     */
    public BigDecimal receivedAmountCompute(
            List<RepaymentPlanExceldataEntity> exceldataEntityList, ContractDTO contractDTO) {

        if (exceldataEntityList == null && exceldataEntityList.isEmpty()) {
            return BigDecimal.ZERO;
        }

        if (contractDTO == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal receivedPrincipalAmount = exceldataEntityList.stream().
                filter(e -> e.getActualRepaymentPrincipalAmount() != null).
                map(e -> e.getActualRepaymentPrincipalAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal receivedInteresAmount = exceldataEntityList.stream().
                filter(e -> e.getActualRepaymentInteresAmount() != null).
                map(e -> e.getActualRepaymentInteresAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);

        if (YesOrNoEnum.NO.getCode().equals(contractDTO.getIsChangeRepayment())) {
            // 回笼的金额除去税收
            BigDecimal receivedRentAmount = BigDecimal.ZERO;
            if (LeaseTypeEnum.DIRECT.getCode().equals(contractDTO.getLeaseType())) {
                receivedRentAmount = (receivedPrincipalAmount.add(receivedInteresAmount)).
                        divide(Constants.DIRECT_RATE, 2, RoundingMode.HALF_UP);
            } else {
                receivedRentAmount = receivedPrincipalAmount.add(receivedInteresAmount.
                        divide(Constants.LEASEBACK_RATE, 2, RoundingMode.HALF_UP));
            }
            return receivedRentAmount;
        } else {
            return receivedPrincipalAmount.add(receivedInteresAmount);
        }
    }


    /**
     * 取得合同回笼金额的总数
     */
    public Map<String, BigDecimal> getReceivedMoneyByContract(Set<String> contractCodeList,
                                                              Map<String, ContractDTO> contractDTOMap) {
        LambdaQueryWrapper<RepaymentPlanExceldataEntity> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(RepaymentPlanExceldataEntity::getContractCode, contractCodeList);
        queryWrapper.isNotNull(RepaymentPlanExceldataEntity::getActualRepaymentRentAmount);
        queryWrapper.orderByAsc(RepaymentPlanExceldataEntity::getContractCode);
        queryWrapper.orderByAsc(RepaymentPlanExceldataEntity::getPlanDate);
        List<RepaymentPlanExceldataEntity> exceldataEntityList = repaymentPlanExceldataMapper.selectList(queryWrapper);

        if (exceldataEntityList == null && exceldataEntityList.isEmpty()) {
            return new HashMap<>();
        }

        Map<String, BigDecimal> receivedMoneyMap = new HashMap<>();
        Map<String, List<RepaymentPlanExceldataEntity>> exceldataEntityMap = exceldataEntityList.stream().
                collect(Collectors.groupingBy(RepaymentPlanExceldataEntity::getContractCode));
        for (String key : exceldataEntityMap.keySet()) {
            ContractDTO contractDTO = contractDTOMap.get(key);
            if (contractDTO == null) {
                continue;
            }

            BigDecimal receivedPrincipalAmount = exceldataEntityMap.get(key).stream().
                    filter(e->e.getActualRepaymentPrincipalAmount() != null).
                    map(e->e.getActualRepaymentPrincipalAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal receivedInteresAmount = exceldataEntityMap.get(key).stream().
                    filter(e->e.getActualRepaymentInteresAmount() != null).
                    map(e->e.getActualRepaymentInteresAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);

            // 回笼的金额除去税收
            BigDecimal receivedRentAmount = BigDecimal.ZERO;
            if (LeaseTypeEnum.DIRECT.getCode().equals(contractDTO.getLeaseType())) {
                receivedRentAmount = (receivedPrincipalAmount.add(receivedInteresAmount)).
                        divide(Constants.DIRECT_RATE, 2, RoundingMode.HALF_UP);
            } else {
                receivedRentAmount = receivedPrincipalAmount.add(receivedInteresAmount.
                        divide(Constants.LEASEBACK_RATE, 2, RoundingMode.HALF_UP));
            }
            receivedMoneyMap.put(key, receivedRentAmount);
        }
        return receivedMoneyMap;
    }
}

