package com.utfinancing.financehub.engine.finance.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.entity.CostChannelFeeEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.dto.CostChannelFeeQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.CostChannelFeeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 成本类支付-经销商服务费、外部渠道费，海通渠道费 Mapper 接口
 * </p>
 *
 * @author bruyang
 * @since 2023-12-13
 */
public interface CostChannelFeeMapper extends BaseMapper<CostChannelFeeEntity> {

    IPage<CostChannelFeeVO> selectPageByCondition(Page page, @Param("param") CostChannelFeeQueryDTO queryDTO);

    List<CostChannelFeeVO> selectByCondition(@Param("param") CostChannelFeeQueryDTO queryDTO);

}
