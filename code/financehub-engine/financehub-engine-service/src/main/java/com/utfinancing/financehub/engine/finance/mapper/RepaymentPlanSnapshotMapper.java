package com.utfinancing.financehub.engine.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.entity.RepaymentPlanEntity;
import com.utfinancing.financehub.engine.finance.entity.RepaymentPlanSnapshotEntity;
import com.utfinancing.financehub.engine.finance.model.dto.RepaymentPlanQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.SelectNoRecaptureByContractDTO;
import com.utfinancing.financehub.engine.finance.model.vo.RepaymentPlanVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 偿还计划测算表 Mapper 接口
 * </p>
 *
 * @author hzhao
 * @since 2023-09-12
 */
public interface RepaymentPlanSnapshotMapper extends BaseMapper<RepaymentPlanSnapshotEntity> {


}
