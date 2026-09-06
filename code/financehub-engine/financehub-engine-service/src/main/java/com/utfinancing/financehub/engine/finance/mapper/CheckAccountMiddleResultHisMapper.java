package com.utfinancing.financehub.engine.finance.mapper;

import com.utfinancing.financehub.engine.finance.entity.CheckAccountMiddleResultHisEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.dto.CheckAccountMiddleResultDTO;
import com.utfinancing.financehub.engine.finance.model.dto.CheckAccountMiddleResultHisDTO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 金蝶中间表与中台科目发生额对账历史表 Mapper 接口
 * </p>
 *
 * @author jnc
 * @since 2024-03-27
 */
public interface CheckAccountMiddleResultHisMapper extends BaseMapper<CheckAccountMiddleResultHisEntity> {

    void saveResultToHis();

    void clearHisTableData(Integer periodCode);

    void queryAndSaveCheckMiddle(@Param("param") CheckAccountMiddleResultHisDTO param);
}
