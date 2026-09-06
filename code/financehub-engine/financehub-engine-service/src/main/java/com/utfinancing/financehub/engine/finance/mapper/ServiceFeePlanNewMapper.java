package com.utfinancing.financehub.engine.finance.mapper;

import com.utfinancing.financehub.engine.finance.entity.ServiceFeePlanNewEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.vo.ServiceFeeAllocationExcelVo;
import com.utfinancing.financehub.engine.finance.model.vo.ServiceFeePlanNewVO;
import com.utfinancing.financehub.engine.finance.model.vo.ServiceFeePlanVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author le
 * @since 2025-11-10
 */
public interface ServiceFeePlanNewMapper extends BaseMapper<ServiceFeePlanNewEntity> {

    List<ServiceFeePlanNewVO> selectRepaymentPlanByContractCode(@Param("contractCode") String contractCode, @Param("serviceFeeNo")  String serviceFeeNo);


    List<ServiceFeeAllocationExcelVo> selectServiceFeeAllocationExcelList(@Param("contractCodes")List<String> contractCodes);
}
