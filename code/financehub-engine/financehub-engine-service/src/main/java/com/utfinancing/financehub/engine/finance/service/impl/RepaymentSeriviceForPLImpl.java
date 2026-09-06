package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.entity.RepaymentPlanEntity;
import com.utfinancing.financehub.engine.finance.entity.RepaymentPlanHYEntity;
import com.utfinancing.financehub.engine.finance.entity.RepaymentPlanPLEntity;
import com.utfinancing.financehub.engine.finance.entity.RepaymentPlanXWEntity;
import com.utfinancing.financehub.engine.finance.mapper.RepaymentPlanPLMapper;
import com.utfinancing.financehub.engine.finance.model.dto.DataInitDTO;
import com.utfinancing.financehub.engine.finance.model.dto.OutstandingAmountCashFlowSumDTO;
import com.utfinancing.financehub.engine.finance.model.dto.OutstandingAmountCashFlowSumInputDTO;
import com.utfinancing.financehub.engine.finance.service.IRepaymentAsyncSerivice;
import com.utfinancing.financehub.engine.finance.service.IRepaymentService;
import com.utfinancing.financehub.engine.hthx.utils.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service(value = "pl")
public class RepaymentSeriviceForPLImpl extends RepaymentAbstractService<RepaymentPlanPLMapper, RepaymentPlanPLEntity>
        implements IRepaymentService<RepaymentPlanPLEntity> {

    @Resource
    private RepaymentPlanPLMapper repaymentPlanPLMapper;

    @Resource(name = "plAsync")
    private IRepaymentAsyncSerivice repaymentAsyncSerivice;

    @Override
    protected Integer getDataCount(String sytemCode) {
        return repaymentPlanPLMapper.getDataCount();
    }

    /**
     * 取得处理的服务类
     */
    @Override
    protected IRepaymentAsyncSerivice getRepaymentAsyncSerivice() {
        return repaymentAsyncSerivice;
    }

    @Override
    protected void outstandingAmountInitCashFlowDel() {
        repaymentPlanPLMapper.outstandingAmountInitCashFlowDel();
    }

    /**
     * 根据合同分组，取得指定日期之后的现金流汇总数据
     */
    @Override
    public List<OutstandingAmountCashFlowSumDTO> outstandingAmountCashFlowSum(String initDate, String contractCode) {
        OutstandingAmountCashFlowSumInputDTO inputDTO = new OutstandingAmountCashFlowSumInputDTO();
        inputDTO.setInitDate(initDate);
        if (StringUtils.isNotEmpty(contractCode)) {
            inputDTO.setContractCode(contractCode);
        }
        return repaymentPlanPLMapper.outstandingAmountCashFlowSum(inputDTO);
    }

    /**
     * 创建初始化的现金流数据
     */
    @Override
    protected void createInitCashFlowData(
            List<OutstandingAmountCashFlowSumDTO> cashFlowSumDTOList, DataInitDTO params) {
        List<RepaymentPlanPLEntity> maxDateDateByInitDateList = repaymentPlanPLMapper.
                selectMaxDateDateByInitDate(params.getInitDate());
        Map<String, RepaymentPlanPLEntity> maxDateDateByInitDateMap = maxDateDateByInitDateList.stream().collect(
                Collectors.toMap(RepaymentPlanPLEntity::getContractCode, (e) -> e));

        List<RepaymentPlanPLEntity> repaymentPlanPLEntityList = new ArrayList<>();
        for (int i = 0; i < cashFlowSumDTOList.size(); i++) {
            RepaymentPlanPLEntity repaymentPlanPLEntity = new RepaymentPlanPLEntity();
            // 将2023-11-30之前最后一条数据的复制到当前临时现金流数据中
            OutstandingAmountCashFlowSumDTO dto = cashFlowSumDTOList.get(i);
            RepaymentPlanPLEntity source = maxDateDateByInitDateMap.get(dto.getContractCode());
            if (source == null) {
                continue;
            }
            BeanUtils.copyProperties(source, repaymentPlanPLEntity);
            repaymentPlanPLEntity.setId(IdWorker.getId());
            repaymentPlanPLEntity.setContractCode(dto.getContractCode());
            repaymentPlanPLEntity.setCashFlow(dto.getCashflowInit());
            repaymentPlanPLEntity.setSystemCode(params.getSystemCode());
            repaymentPlanPLEntity.setPeriods(-1);
            try {
                repaymentPlanPLEntity.setPlanDate(DateUtils.parseDate(params.getInitDate(), DateUtils.YYYY_MM_DD));
            } catch (ParseException ex) {
                repaymentPlanPLEntity.setPlanDate(new Date());
            }
            repaymentPlanPLEntityList.add(repaymentPlanPLEntity);
        }
        super.saveBatch(repaymentPlanPLEntityList);

//        LambdaUpdateWrapper<RepaymentPlanPLEntity> wrapper = new LambdaUpdateWrapper<>();
//        wrapper.set(RepaymentPlanPLEntity::getDelFlag, YesOrNoEnum.YES.getCode());
//        wrapper.eq(RepaymentPlanPLEntity::getCashFlow, 0);
//        wrapper.and(w-> w.eq(RepaymentPlanPLEntity::getRentAmount, 0).or().isNull(RepaymentPlanPLEntity::getRentAmount));
//        wrapper.and(e->e.eq(RepaymentPlanPLEntity::getPrincipalAmount, 0).or().isNull(RepaymentPlanPLEntity::getPrincipalAmount));
//        wrapper.and(e->e.eq(RepaymentPlanPLEntity::getInterestAmount, 0).or().isNull(RepaymentPlanPLEntity::getInterestAmount));
//        this.update(wrapper);

        // 将无效数据逻辑删除
        repaymentPlanPLMapper.updNoVliadData();
    }


    /**
     * 保存合同在2023-01-01之前到期的偿还计划数据
     */
    protected void saveExpirationOfContractData() {
        repaymentPlanPLMapper.saveExpirationOfContractData();
    }

    /**
     * 根据合同取得偿还计划
     *
     * @param contractCode
     */
    @Override
    public List<RepaymentPlanEntity> selectRepaymentByContract(String contractCode) {
        LambdaQueryWrapper<RepaymentPlanPLEntity> wrapper = new LambdaQueryWrapper();
        wrapper.eq(RepaymentPlanPLEntity::getContractCode, contractCode);
        wrapper.eq(RepaymentPlanPLEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        wrapper.orderByAsc(RepaymentPlanPLEntity::getPlanDate);
        List<RepaymentPlanPLEntity> repaymentPlanPLEntityList = repaymentPlanPLMapper.selectList(wrapper);
        if (repaymentPlanPLEntityList == null || repaymentPlanPLEntityList.isEmpty()) {
            return new ArrayList<>();
        }
        List<RepaymentPlanEntity> result = BeanUtil.copyToList(repaymentPlanPLEntityList, RepaymentPlanEntity.class);
        return result;
    }
}
