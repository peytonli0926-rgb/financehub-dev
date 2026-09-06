package com.utfinancing.financehub.engine.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.entity.CheckSqlEntity;
import com.utfinancing.financehub.engine.finance.model.dto.ReportLeaseTableQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ReportLeaseTableVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 对账对接其他业务系统sql表 Mapper 接口
 * </p>
 *
 * @author jnc
 * @since 2024-04-02
 */
public interface ReportCommonMapper {

    List<ReportLeaseTableVO> selectLeaseTable(@Param("param") ReportLeaseTableQueryDTO queryDTO);

    Map<String, Long> selectLeaseTableSize(@Param("param") ReportLeaseTableQueryDTO queryDTO);

    void createTempTableLeaseTable();

    void fillTempTableLeaseTable(@Param("param") ReportLeaseTableQueryDTO queryDTO);

    List<ReportLeaseTableVO> selectLeaseTableFromTempTable(@Param("param") ReportLeaseTableQueryDTO queryDTO);
}
