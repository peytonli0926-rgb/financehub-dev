package com.utfinancing.financehub.engine.finance.mapper;

import com.utfinancing.financehub.engine.finance.entity.CheckAccountKingdeeResultEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.dto.CheckAccountKingdeeResultDTO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 金蝶科目余额与中台科目余额对账实时表 Mapper 接口
 * </p>
 *
 * @author jnc
 * @since 2024-03-27
 */
public interface CheckAccountKingdeeResultMapper extends BaseMapper<CheckAccountKingdeeResultEntity> {

    void queryAndSaveCheckKingdee(@Param("param") CheckAccountKingdeeResultDTO param);

    void clearTableData();
}
