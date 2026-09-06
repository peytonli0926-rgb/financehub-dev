package com.utfinancing.financehub.engine.claim.mapper;

import com.utfinancing.financehub.engine.claim.entity.ClaimOrderDetailEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.claim.model.dto.ClaimOrderDetailQueryDTO;
import com.utfinancing.financehub.engine.claim.model.vo.ClaimOrderDetailVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 报销系统-报销明细表 Mapper 接口
 * </p>
 *
 * @author lixin
 * @since 2023-10-23
 */
public interface ClaimOrderDetailMapper extends BaseMapper<ClaimOrderDetailEntity> {

    List<ClaimOrderDetailVO> selectByCondition(@Param("condition") ClaimOrderDetailQueryDTO queryDTO);

}
