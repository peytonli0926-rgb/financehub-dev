package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.engine.enums.SystemEnum;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.entity.RepaymentPlanEntity;
import com.utfinancing.financehub.engine.finance.entity.RepaymentPlanHYEntity;
import com.utfinancing.financehub.engine.finance.entity.RepaymentPlanPLEntity;
import com.utfinancing.financehub.engine.finance.entity.RepaymentPlanXWEntity;
import com.utfinancing.financehub.engine.finance.mapper.RepaymentPlanXWMapper;
import com.utfinancing.financehub.engine.finance.model.dto.DataInitDTO;
import com.utfinancing.financehub.engine.finance.model.dto.OutstandingAmountCashFlowSumDTO;
import com.utfinancing.financehub.engine.finance.model.dto.OutstandingAmountCashFlowSumInputDTO;
import com.utfinancing.financehub.engine.finance.service.IRepaymentAsyncSerivice;
import com.utfinancing.financehub.engine.finance.service.IRepaymentService;
import com.utfinancing.financehub.engine.hthx.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service(value = "xw")
@Slf4j
public class RepaymentSeriviceForXWImpl extends RepaymentAbstractService<RepaymentPlanXWMapper, RepaymentPlanXWEntity>
        implements IRepaymentService<RepaymentPlanXWEntity> {

    @Resource
    private RepaymentPlanXWMapper repaymentPlanXWMapper;

    @Resource(name = "xwAsync")
    private IRepaymentAsyncSerivice repaymentAsyncSerivice;

    @Override
    protected Integer getDataCount(String sytemCode) {
        return repaymentPlanXWMapper.getDataCount();
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
        repaymentPlanXWMapper.outstandingAmountInitCashFlowDel();
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
        return repaymentPlanXWMapper.outstandingAmountCashFlowSum(inputDTO);
    }

    /**
     * 创建初始化的现金流数据
     */
    @Override
    protected void createInitCashFlowData(
            List<OutstandingAmountCashFlowSumDTO> cashFlowSumDTOList, DataInitDTO params) {
        List<RepaymentPlanXWEntity> maxDateDateByInitDateList = repaymentPlanXWMapper.
                selectMaxDateDateByInitDate(params.getInitDate());
        Map<String, RepaymentPlanXWEntity> maxDateDateByInitDateMap = maxDateDateByInitDateList.stream().collect(
                Collectors.toMap(RepaymentPlanXWEntity::getContractCode, (e) -> e));

        List<RepaymentPlanXWEntity> repaymentPlanXWEntityList = new ArrayList<>();
        for (int i = 0; i < cashFlowSumDTOList.size(); i++) {
            RepaymentPlanXWEntity repaymentPlanXWEntity = new RepaymentPlanXWEntity();
            // 将2023-11-30之前最后一条数据的复制到当前临时现金流数据中
            OutstandingAmountCashFlowSumDTO dto = cashFlowSumDTOList.get(i);
            RepaymentPlanXWEntity source = maxDateDateByInitDateMap.get(dto.getContractCode());
            if (source == null) {
                continue;
            }
            BeanUtils.copyProperties(source, repaymentPlanXWEntity);
            repaymentPlanXWEntity.setId(IdWorker.getId());
            repaymentPlanXWEntity.setContractCode(dto.getContractCode());
            repaymentPlanXWEntity.setCashFlow(dto.getCashflowInit());
            repaymentPlanXWEntity.setSystemCode(params.getSystemCode());
            repaymentPlanXWEntity.setPeriods(-1);
            try {
                repaymentPlanXWEntity.setPlanDate(DateUtils.parseDate(params.getInitDate(), DateUtils.YYYY_MM_DD));
            } catch (ParseException ex) {
                repaymentPlanXWEntity.setPlanDate(new Date());
            }
            repaymentPlanXWEntityList.add(repaymentPlanXWEntity);
        }
        super.saveBatch(repaymentPlanXWEntityList);

//        LambdaUpdateWrapper<RepaymentPlanXWEntity> wrapper = new LambdaUpdateWrapper<>();
//        wrapper.set(RepaymentPlanXWEntity::getDelFlag, YesOrNoEnum.YES.getCode());
//        wrapper.eq(RepaymentPlanXWEntity::getCashFlow, 0);
//        wrapper.and(w-> w.eq(RepaymentPlanXWEntity::getRentAmount, 0).or().isNull(RepaymentPlanXWEntity::getRentAmount));
//        wrapper.and(e->e.eq(RepaymentPlanXWEntity::getPrincipalAmount, 0).or().isNull(RepaymentPlanXWEntity::getPrincipalAmount));
//        wrapper.and(e->e.eq(RepaymentPlanXWEntity::getInterestAmount, 0).or().isNull(RepaymentPlanXWEntity::getInterestAmount));
//        this.update(wrapper);

        // 将无效数据逻辑删除
        repaymentPlanXWMapper.updNoVliadData();
    }


    /**
     * 保存合同在2023-01-01之前到期的偿还计划数据
     */
    protected void saveExpirationOfContractData() {
        repaymentPlanXWMapper.saveExpirationOfContractData();
    }


    /**
     * 根据合同取得偿还计划
     *
     * @param contractCode
     */
    @Override
    public List<RepaymentPlanEntity> selectRepaymentByContract(String contractCode) {
        LambdaQueryWrapper<RepaymentPlanXWEntity> wrapper = new LambdaQueryWrapper();
        wrapper.eq(RepaymentPlanXWEntity::getContractCode, contractCode);
        wrapper.eq(RepaymentPlanXWEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        wrapper.orderByAsc(RepaymentPlanXWEntity::getPlanDate);
        List<RepaymentPlanXWEntity> repaymentPlanXWEntityList = repaymentPlanXWMapper.selectList(wrapper);
        if (repaymentPlanXWEntityList == null || repaymentPlanXWEntityList.isEmpty()) {
            return new ArrayList<>();
        }
        List<RepaymentPlanEntity> result = BeanUtil.copyToList(repaymentPlanXWEntityList, RepaymentPlanEntity.class);
        return result;
    }
}
