package com.utfinancing.financehub.engine.finance.mapper;

import com.utfinancing.financehub.engine.finance.entity.DwsBzHetjyjgfyxDEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author robjiang
 * @since 2024-05-08
 */
public interface DwsBzHetjyjgfyxDMapper extends BaseMapper<DwsBzHetjyjgfyxDEntity> {
    public List<DwsBzHetjyjgfyxDEntity> selectHetjyjgfyxByContractCode(@Param("contractCodeList") List<String> contractCodeList);
}
