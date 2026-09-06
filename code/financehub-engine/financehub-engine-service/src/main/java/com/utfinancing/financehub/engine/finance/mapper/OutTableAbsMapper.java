package com.utfinancing.financehub.engine.finance.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.entity.OutTableAbsEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.dto.OutTableAbsQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.OutTableContractDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.OutTableContractDetailVO;
import com.utfinancing.financehub.engine.finance.model.vo.OutTableRentPlanVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 出表ABS Mapper 接口
 * </p>
 *
 * @author bruyang
 * @since 2024-03-11
 */
public interface OutTableAbsMapper extends BaseMapper<OutTableAbsEntity> {

    List<OutTableContractDetailVO> selectByCondition(@Param("param") OutTableContractDetailQueryDTO queryDTO);

    IPage<OutTableRentPlanVO> selectRentPlanPage(Page page, @Param("param")OutTableAbsQueryDTO queryDTO);

    List<OutTableRentPlanVO> selectRentPlanList(@Param("param")OutTableAbsQueryDTO queryDTO);
}
