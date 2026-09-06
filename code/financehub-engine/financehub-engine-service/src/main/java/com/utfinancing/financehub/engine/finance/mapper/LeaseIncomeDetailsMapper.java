package com.utfinancing.financehub.engine.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.entity.LeaseIncomeDetailsEntity;
import com.utfinancing.financehub.engine.finance.model.dto.LeaseIncomeDetailsQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.SelectDetailsByPageDTO;
import com.utfinancing.financehub.engine.finance.model.vo.LeaseIncomeDetailsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hzhao
 * @since 2023-11-13
 */
public interface LeaseIncomeDetailsMapper extends BaseMapper<LeaseIncomeDetailsEntity> {

    List<LeaseIncomeDetailsVO> selectLeaseIncomeInfo(Page page, @Param("param") LeaseIncomeDetailsQueryDTO queryDTO);
//    List<LeaseIncomeDetailsEntity> selectLeaseIncomeInfo(@Param("param") LeaseIncomeDetailsQueryDTO queryDTO);
    List<LeaseIncomeDetailsVO> selectDetailsByCondition(@Param("param") LeaseIncomeDetailsQueryDTO queryDTO);

    List<LeaseIncomeDetailsEntity> selectDetailsByPage(@Param("param") SelectDetailsByPageDTO queryDTO);

    /**
     * 收益计提数据校验
     */
    void leaseIncomeDataValidation();
}
