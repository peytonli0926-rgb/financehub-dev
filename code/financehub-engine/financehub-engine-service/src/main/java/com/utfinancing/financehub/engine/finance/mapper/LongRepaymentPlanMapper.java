package com.utfinancing.financehub.engine.finance.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.entity.LongRepaymentPlanEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.dto.LongRepaymentPlanQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.LongRepaymentPlanVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 长期应收款-偿还计划 Mapper 接口
 * </p>
 *
 * @author wenbin
 * @since 2024-04-12
 */
public interface LongRepaymentPlanMapper extends BaseMapper<LongRepaymentPlanEntity> {

    IPage<LongRepaymentPlanVO> selectPageByMapper(Page page, @Param("queryDTO") LongRepaymentPlanQueryDTO queryDTO);

    List<LongRepaymentPlanVO> selectPageByMapper(@Param("queryDTO") LongRepaymentPlanQueryDTO queryDTO);
}
