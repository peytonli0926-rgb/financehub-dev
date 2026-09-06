package com.utfinancing.financehub.etl.financial.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.etl.financial.entity.RepaymentPlanPLEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RepaymentPlanPLMapper extends BaseMapper<RepaymentPlanPLEntity> {

    /**
     * 删除偿还计划
     */
    public void delRepaymentPlan(@Param("contractCodeList") List<String> contractCodeList);
}
