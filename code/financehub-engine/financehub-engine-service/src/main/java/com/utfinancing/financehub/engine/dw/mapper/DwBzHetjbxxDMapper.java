package com.utfinancing.financehub.engine.dw.mapper;

import com.utfinancing.financehub.engine.dw.entity.DwBzHetjbxxDEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.dw.entity.DwsBzHetjyjgxxDEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 数仓-合同基本信息 Mapper 接口
 * </p>
 *
 * @author lixin
 * @since 2023-12-14
 */
public interface DwBzHetjbxxDMapper extends BaseMapper<DwBzHetjbxxDEntity> {

    public List<DwBzHetjbxxDEntity> selectBzhetjbxxByContractCode(@Param("contractCodeList") List<String> contractCodeList);
}
