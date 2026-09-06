package com.utfinancing.financehub.engine.finance.mapper;

import com.utfinancing.financehub.engine.finance.entity.OutTableContractDetailEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.dto.OutTableContractDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.OutTableContractDetailVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 出表ABS合同详情 Mapper 接口
 * </p>
 *
 * @author bruyang
 * @since 2024-03-11
 */
public interface OutTableContractDetailMapper extends BaseMapper<OutTableContractDetailEntity> {

    List<OutTableContractDetailEntity> getOutTableContractDetailInfoByTaId(@Param("taId") Long taId);

    List<OutTableContractDetailVO> selectDetailsByParams(@Param("param") OutTableContractDetailQueryDTO queryDTO);
}
