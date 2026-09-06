package com.utfinancing.financehub.engine.finance.mapper;

import com.utfinancing.financehub.engine.finance.entity.ServiceFeePlanEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.vo.ServiceFeeAllocationExcelVo;
import com.utfinancing.financehub.engine.finance.model.vo.ServiceFeePlanVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 服务费计划表 Mapper 接口
 * </p>
 *
 * @author wenbin
 * @since 2024-05-29
 */
public interface ServiceFeePlanMapper extends BaseMapper<ServiceFeePlanEntity> {

    void physicalDeleteByContractCode(@Param("contractCode") String contractCode);

    List<ServiceFeePlanVO> getExportData(@Param("contractCode") String contractCode);

    List<ServiceFeeAllocationExcelVo> selectServiceFeeAllocationExcelList(@Param("contractCodes")List<String> planQueryWrapper);
}
