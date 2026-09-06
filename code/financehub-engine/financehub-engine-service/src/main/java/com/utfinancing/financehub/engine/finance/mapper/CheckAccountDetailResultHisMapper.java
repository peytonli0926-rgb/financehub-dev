package com.utfinancing.financehub.engine.finance.mapper;

import com.utfinancing.financehub.engine.finance.entity.CheckAccountDetailResultHisEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.dto.CheckAccountDetailResultHisQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.CheckAccountDetailResultQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.CheckAccountDetailResultHisVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 科目余额与明细余额对账结果历史表 Mapper 接口
 * </p>
 *
 * @author jnc
 * @since 2024-03-20
 */
public interface CheckAccountDetailResultHisMapper extends BaseMapper<CheckAccountDetailResultHisEntity> {

    public void saveResultToHis();

    void clearHisTableData(@Param("periodCode") Integer periodCode);

    void queryAndSaveCheckResultDtoByParam(@Param("param") CheckAccountDetailResultHisQueryDTO param, @Param("list") List<Map<String, Object>> paramList);

    void clearContractTmpTableData();

    void insertContractTmpTableData(Integer periodCode);

    List<CheckAccountDetailResultHisEntity> queryCheckResultDto(@Param("param") CheckAccountDetailResultHisQueryDTO param, @Param("list") List<Map<String, Object>> paramList);
}
