package com.utfinancing.financehub.engine.claim.mapper;

import com.utfinancing.financehub.engine.claim.entity.ClaimOrderSpecialEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.claim.model.dto.ClaimOrderSpecialQueryDTO;
import com.utfinancing.financehub.engine.claim.model.vo.ClaimOrderCostVo;
import com.utfinancing.financehub.engine.claim.model.vo.ClaimOrderSpecialVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 报销系统-报销单专项费明细 Mapper 接口
 * </p>
 *
 * @author lixin
 * @since 2023-11-08
 */
public interface ClaimOrderSpecialMapper extends BaseMapper<ClaimOrderSpecialEntity> {

    List<ClaimOrderSpecialVO> selectByCondition(@Param("condition") ClaimOrderSpecialQueryDTO queryDTO);

    List<ClaimOrderCostVo> statisticalCost(@Param("condition") ClaimOrderSpecialQueryDTO queryDTO);

    List<ClaimOrderSpecialVO> getOrderByContractCodeList(@Param("contractCodeList") List<String> contractCodeList);
}
