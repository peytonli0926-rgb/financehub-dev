package com.utfinancing.financehub.engine.finance.mapper;

import com.utfinancing.financehub.engine.finance.entity.CheckAccountKingdeeResultHisEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.dto.CheckAccountKingdeeResultDTO;
import com.utfinancing.financehub.engine.finance.model.dto.CheckAccountKingdeeResultHisDTO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 金蝶科目余额与中台科目余额对账历史表 Mapper 接口
 * </p>
 *
 * @author jnc
 * @since 2024-03-27
 */
public interface CheckAccountKingdeeResultHisMapper extends BaseMapper<CheckAccountKingdeeResultHisEntity> {
    void queryAndSaveCheckKingdee(@Param("param") CheckAccountKingdeeResultHisDTO param);

    void saveResultToHis();

    void clearHisTableData(Integer periodCode);
}
