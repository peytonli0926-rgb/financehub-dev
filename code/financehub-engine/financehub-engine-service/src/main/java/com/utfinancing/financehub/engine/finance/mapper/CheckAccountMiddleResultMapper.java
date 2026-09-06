package com.utfinancing.financehub.engine.finance.mapper;

import com.utfinancing.financehub.engine.finance.entity.CheckAccountMiddleResultEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.dto.CheckAccountMiddleResultDTO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 金蝶中间表与中台科目发生额对账实时表 Mapper 接口
 * </p>
 *
 * @author jnc
 * @since 2024-03-27
 */
public interface CheckAccountMiddleResultMapper extends BaseMapper<CheckAccountMiddleResultEntity> {

    void clearTableData();

    void queryAndSaveCheckMiddle(@Param("param") CheckAccountMiddleResultDTO param);
}
