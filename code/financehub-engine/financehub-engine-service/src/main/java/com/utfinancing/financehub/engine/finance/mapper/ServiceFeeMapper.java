package com.utfinancing.financehub.engine.finance.mapper;

import com.utfinancing.financehub.engine.finance.entity.ServiceFeeEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.vo.ServiceFeePlanVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 服务费分摊表 Mapper 接口
 * </p>
 *
 * @author hzhao
 * @since 2023-11-07
 */
public interface ServiceFeeMapper extends BaseMapper<ServiceFeeEntity> {

    List<ServiceFeePlanVO> selectRepaymentPlanByContractCode(@Param("contractCode") String contractCode,@Param("serviceFeeNo")  String serviceFeeNo);
}
