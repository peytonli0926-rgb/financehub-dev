package com.utfinancing.financehub.engine.finance.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.entity.ServiceFeeDetailsNewEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeeDetailsQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ServiceFeeDetailsNewVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 服务费分摊表详情 Mapper 接口
 * </p>
 *
 * @author le
 * @since 2025-11-10
 */
public interface ServiceFeeDetailsNewMapper extends BaseMapper<ServiceFeeDetailsNewEntity> {

    List<ServiceFeeDetailsNewVO> selectDetailList(@Param("param") ServiceFeeDetailsQueryDTO queryDTO);

    List<ServiceFeeDetailsNewVO> selectServiceFeeInfo(Page page, @Param("param") ServiceFeeDetailsQueryDTO param);

    List<ServiceFeeDetailsNewEntity> getUnapportionedAndNoPlanData(@Param("preYearValue") Integer preYearValue, @Param("preMonthValue") Integer preMonthValue,
                                                                   @Param("allocateContractCodes") List<String> allocateContractCodes);

    List<ServiceFeeDetailsNewEntity> getUnapportionedAndNoPlanData1(@Param("date") String date, @Param("allocateContractCodes")List<String> allocateContractCodes);
}
