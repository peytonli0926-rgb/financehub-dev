package com.utfinancing.financehub.engine.dw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.dw.entity.DwsBzHetjyjgxxDEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author robjiang
 * @since 2024-02-19
 */
public interface DwsBzHetjyjgxxDMapper extends BaseMapper<DwsBzHetjyjgxxDEntity> {

    public List<DwsBzHetjyjgxxDEntity> selectQicqmzfByContractCode(@Param("contractCodeList") List<String> contractCodeList);
}
