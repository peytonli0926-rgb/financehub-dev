package com.utfinancing.financehub.engine.finance.mapper;

import com.utfinancing.financehub.engine.finance.entity.CheckAccountDetailResultEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.dto.CheckAccountDetailResultDTO;
import com.utfinancing.financehub.engine.finance.model.dto.CheckAccountDetailResultQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.CheckAccountDetailResultVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 科目余额与明细余额对账结果实时表 Mapper 接口
 * </p>
 *
 * @author jnc
 * @since 2024-03-20
 */
public interface CheckAccountDetailResultMapper extends BaseMapper<CheckAccountDetailResultEntity> {
    void clearTableData();

    void queryAndSaveCheckResultLatestDtoByParam(@Param("param")CheckAccountDetailResultQueryDTO param, @Param("list") List<Map<String, Object>> paramList);

    List<CheckAccountDetailResultEntity> queryCheckResultDto(@Param("param") CheckAccountDetailResultQueryDTO param, @Param("list") List<Map<String, Object>> paramList);
}
