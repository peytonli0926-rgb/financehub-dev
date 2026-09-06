package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.entity.RepaymentPlanEntity;
import com.utfinancing.financehub.engine.finance.entity.RepaymentPlanYYEntity;
import com.utfinancing.financehub.engine.finance.mapper.RepaymentPlanYYMapper;
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


@Service(value = "yy")
public class RepaymentSeriviceForYYImpl extends RepaymentAbstractService<RepaymentPlanYYMapper, RepaymentPlanYYEntity>
        implements IRepaymentService<RepaymentPlanYYEntity> {

    @Resource
    private RepaymentPlanYYMapper repaymentPlanYYMapper;

    @Resource(name = "yyAsync")
    private IRepaymentAsyncSerivice repaymentAsyncSerivice;

    @Override
    protected Integer getDataCount(String sytemCode) {
        return repaymentPlanYYMapper.getDataCount();
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
        repaymentPlanYYMapper.outstandingAmountInitCashFlowDel();
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
        return repaymentPlanYYMapper.outstandingAmountCashFlowSum(inputDTO);
    }

    /**
     * 创建初始化的现金流数据
     */
    @Override
    protected void createInitCashFlowData(
            List<OutstandingAmountCashFlowSumDTO> cashFlowSumDTOList, DataInitDTO params) {
        List<RepaymentPlanYYEntity> maxDateDateByInitDateList = repaymentPlanYYMapper.
                selectMaxDateDateByInitDate(params.getInitDate());
        Map<String, RepaymentPlanYYEntity> maxDateDateByInitDateMap = maxDateDateByInitDateList.stream().collect(
                Collectors.toMap(RepaymentPlanYYEntity::getContractCode, (e) -> e));

        List<RepaymentPlanYYEntity> repaymentPlanYYEntities = new ArrayList<>();
        for (int i = 0; i < cashFlowSumDTOList.size(); i++) {
            RepaymentPlanYYEntity repaymentPlanYYEntity = new RepaymentPlanYYEntity();
            // 将2023-11-30之前最后一条数据的复制到当前临时现金流数据中
            OutstandingAmountCashFlowSumDTO dto = cashFlowSumDTOList.get(i);
            RepaymentPlanYYEntity source = maxDateDateByInitDateMap.get(dto.getContractCode());
            if (source == null) {
                continue;
            }
            BeanUtils.copyProperties(source, repaymentPlanYYEntity);
            repaymentPlanYYEntity.setId(IdWorker.getId());
            repaymentPlanYYEntity.setContractCode(dto.getContractCode());
            repaymentPlanYYEntity.setCashFlow(dto.getCashflowInit());
            repaymentPlanYYEntity.setSystemCode(params.getSystemCode());
            repaymentPlanYYEntity.setPeriods(-1);
            try {
                repaymentPlanYYEntity.setPlanDate(DateUtils.parseDate(params.getInitDate(), DateUtils.YYYY_MM_DD));
            } catch (ParseException ex) {
                repaymentPlanYYEntity.setPlanDate(new Date());
            }
            repaymentPlanYYEntities.add(repaymentPlanYYEntity);
        }
        super.saveBatch(repaymentPlanYYEntities);

        // 将无效数据逻辑删除
        repaymentPlanYYMapper.updNoVliadData();
    }


    /**
     * 保存合同在2023-01-01之前到期的偿还计划数据
     */
    protected void saveExpirationOfContractData() {
        repaymentPlanYYMapper.saveExpirationOfContractData();
    }

    /**
     * 根据合同取得偿还计划
     *
     * @param contractCode
     */
    @Override
    public List<RepaymentPlanEntity> selectRepaymentByContract(String contractCode) {
        LambdaQueryWrapper<RepaymentPlanYYEntity> wrapper = new LambdaQueryWrapper();
        wrapper.eq(RepaymentPlanYYEntity::getContractCode, contractCode);
        wrapper.eq(RepaymentPlanYYEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        wrapper.orderByAsc(RepaymentPlanYYEntity::getPlanDate);
        List<RepaymentPlanYYEntity> repaymentPlanPLEntityList = repaymentPlanYYMapper.selectList(wrapper);
        if (repaymentPlanPLEntityList == null || repaymentPlanPLEntityList.isEmpty()) {
            return new ArrayList<>();
        }
        List<RepaymentPlanEntity> result = BeanUtil.copyToList(repaymentPlanPLEntityList, RepaymentPlanEntity.class);
        return result;
    }
}
