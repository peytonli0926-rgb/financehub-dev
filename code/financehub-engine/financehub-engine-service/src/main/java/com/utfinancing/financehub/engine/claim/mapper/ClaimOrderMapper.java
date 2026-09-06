package com.utfinancing.financehub.engine.claim.mapper;

import com.utfinancing.financehub.engine.claim.entity.ClaimOrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.claim.model.dto.ClaimOrderQueryDTO;
import com.utfinancing.financehub.engine.claim.model.vo.ClaimOrderVO;
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
public interface ClaimOrderMapper extends BaseMapper<ClaimOrderEntity> {

    List<ClaimOrderVO> selectByCondition(@Param("condition") ClaimOrderQueryDTO queryDTO);
}
