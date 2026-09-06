package com.utfinancing.financehub.engine.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.entity.ServiceFeeDetailsEntity;
import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeeDetailsQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ServiceFeeDetailsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 服务费分摊表详情 Mapper 接口
 * </p>
 *
 * @author hzhao
 * @since 2023-11-07
 */
public interface ServiceFeeDetailsMapper extends BaseMapper<ServiceFeeDetailsEntity> {
    List<ServiceFeeDetailsVO> selectServiceFeeInfo(Page page, @Param("param") ServiceFeeDetailsQueryDTO param);

    List<ServiceFeeDetailsVO> selectDetailList( @Param("param") ServiceFeeDetailsQueryDTO queryDTO);
}
