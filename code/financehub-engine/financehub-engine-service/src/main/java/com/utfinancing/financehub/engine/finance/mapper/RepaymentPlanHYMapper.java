package com.utfinancing.financehub.engine.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.entity.RepaymentPlanHYEntity;
import com.utfinancing.financehub.engine.finance.model.dto.GetProcessDataDTO;
import com.utfinancing.financehub.engine.finance.model.dto.OutstandingAmountCashFlowSumDTO;
import com.utfinancing.financehub.engine.finance.model.dto.OutstandingAmountCashFlowSumInputDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 偿还计划测算表 Mapper 接口
 * </p>
 *
 * @author hzhao
 * @since 2023-09-12
 */
public interface RepaymentPlanHYMapper extends BaseMapper<RepaymentPlanHYEntity> {

    /**
     * 取得恒运待处理数据总量
     */
    public Integer getDataCount();

    /**
     * 根据合同分页，取得合同的所有期初数据
     */
    public List<RepaymentPlanHYEntity> selectHyInitDataByPage(GetProcessDataDTO params);

    /**
     * 删除创建的初始化现金流
     */
    public int outstandingAmountInitCashFlowDel();

    /**
     * 汇总未偿还现金流总和
     */
    public List<OutstandingAmountCashFlowSumDTO> outstandingAmountCashFlowSum(@Param("params") OutstandingAmountCashFlowSumInputDTO params);

    /**
     * 取得initDate前一条数据
     */
    public List<RepaymentPlanHYEntity> selectMaxDateDateByInitDate(@Param("initDate") String initDate);

    /**
     * 保存合同在2023-01-01之前到期的偿还计划数据
     */
    public void saveExpirationOfContractData();

    /**
     * 更新无效数据
     */
    public void updNoVliadData();
}
