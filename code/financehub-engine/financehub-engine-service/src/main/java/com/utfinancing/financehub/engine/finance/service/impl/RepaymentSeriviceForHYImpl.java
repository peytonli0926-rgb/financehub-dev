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
import com.utfinancing.financehub.engine.finance.mapper.RepaymentPlanHYMapper;
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


@Service(value = "hy")
public class RepaymentSeriviceForHYImpl extends RepaymentAbstractService<RepaymentPlanHYMapper, RepaymentPlanHYEntity>
        implements IRepaymentService<RepaymentPlanHYEntity> {

    @Resource
    private RepaymentPlanHYMapper repaymentPlanHYMapper;

    @Resource(name = "hyAsync")
    private IRepaymentAsyncSerivice repaymentAsyncSerivice;

    @Override
    protected void outstandingAmountInitCashFlowDel() {
        repaymentPlanHYMapper.outstandingAmountInitCashFlowDel();
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
        return repaymentPlanHYMapper.outstandingAmountCashFlowSum(inputDTO);
    }

    /**
     * 创建初始化的现金流数据
     */
    @Override
    protected void createInitCashFlowData(List<OutstandingAmountCashFlowSumDTO> cashFlowSumDTOList, DataInitDTO params) {
        List<RepaymentPlanHYEntity> maxDateDateByInitDateList = repaymentPlanHYMapper.
                selectMaxDateDateByInitDate(params.getInitDate());
        Map<String, RepaymentPlanHYEntity> maxDateDateByInitDateMap = maxDateDateByInitDateList.stream().collect(
                Collectors.toMap(RepaymentPlanHYEntity::getContractCode, (e) -> e));

        List<RepaymentPlanHYEntity> repaymentPlanHYEntityList = new ArrayList<>();
        for (int i = 0; i < cashFlowSumDTOList.size(); i++) {
            RepaymentPlanHYEntity repaymentPlanHYEntity = new RepaymentPlanHYEntity();
            // 将2023-11-30之前最后一条数据的复制到当前临时现金流数据中
            OutstandingAmountCashFlowSumDTO dto = cashFlowSumDTOList.get(i);
            RepaymentPlanHYEntity source = maxDateDateByInitDateMap.get(dto.getContractCode());
            if (source == null) {
                continue;
            }
            BeanUtils.copyProperties(source, repaymentPlanHYEntity);
            repaymentPlanHYEntity.setId(IdWorker.getId());
            repaymentPlanHYEntity.setContractCode(dto.getContractCode());
            repaymentPlanHYEntity.setCashFlow(dto.getCashflowInit());
            repaymentPlanHYEntity.setPeriods(-1);
            try {
                repaymentPlanHYEntity.setPlanDate(DateUtils.parseDate(params.getInitDate(), DateUtils.YYYY_MM_DD));
            } catch (ParseException ex) {
                repaymentPlanHYEntity.setPlanDate(new Date());
            }
            repaymentPlanHYEntityList.add(repaymentPlanHYEntity);
        }
        super.saveBatch(repaymentPlanHYEntityList);

//        LambdaUpdateWrapper<RepaymentPlanHYEntity> wrapper = new LambdaUpdateWrapper<>();
//        wrapper.set(RepaymentPlanHYEntity::getDelFlag, YesOrNoEnum.YES.getCode());
//        wrapper.eq(RepaymentPlanHYEntity::getCashFlow, 0);
//        wrapper.and(w-> w.eq(RepaymentPlanHYEntity::getRentAmount, 0).or().isNull(RepaymentPlanHYEntity::getRentAmount));
//        wrapper.and(e->e.eq(RepaymentPlanHYEntity::getPrincipalAmount, 0).or().isNull(RepaymentPlanHYEntity::getPrincipalAmount));
//        wrapper.and(e->e.eq(RepaymentPlanHYEntity::getInterestAmount, 0).or().isNull(RepaymentPlanHYEntity::getInterestAmount));
//        this.update(wrapper);

        // 将无效数据逻辑删除
        repaymentPlanHYMapper.updNoVliadData();
    }

    @Override
    protected Integer getDataCount(String sytemCode) {
        return repaymentPlanHYMapper.getDataCount();
    }

    /**
     * 取得处理的服务类
     */
    @Override
    protected IRepaymentAsyncSerivice getRepaymentAsyncSerivice() {
        return repaymentAsyncSerivice;
    }


    /**
     * 保存合同在2023-01-01之前到期的偿还计划数据
     */
    protected void saveExpirationOfContractData() {
        repaymentPlanHYMapper.saveExpirationOfContractData();
    }

    /**
     * 根据合同取得偿还计划
     *
     * @param contractCode
     */
    @Override
    public List<RepaymentPlanEntity> selectRepaymentByContract(String contractCode) {
        LambdaQueryWrapper<RepaymentPlanHYEntity> wrapper = new LambdaQueryWrapper();
        wrapper.eq(RepaymentPlanHYEntity::getContractCode, contractCode);
        wrapper.eq(RepaymentPlanHYEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        wrapper.orderByAsc(RepaymentPlanHYEntity::getPlanDate);
        List<RepaymentPlanHYEntity> repaymentPlanHYEntityList = repaymentPlanHYMapper.selectList(wrapper);
        if (repaymentPlanHYEntityList == null || repaymentPlanHYEntityList.isEmpty()) {
            return new ArrayList<>();
        }
        List<RepaymentPlanEntity> result = BeanUtil.copyToList(repaymentPlanHYEntityList, RepaymentPlanEntity.class);
        return result;
    }
}
