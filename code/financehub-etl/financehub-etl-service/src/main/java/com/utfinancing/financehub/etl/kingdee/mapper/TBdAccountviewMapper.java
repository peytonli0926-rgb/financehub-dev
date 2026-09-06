package com.utfinancing.financehub.etl.kingdee.mapper;

import com.utfinancing.financehub.etl.financial.model.dto.KingdeeAccountDTO;
import com.utfinancing.financehub.etl.kingdee.entity.TBdAccountviewEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lixin
 * @since 2023-11-12
 */
public interface TBdAccountviewMapper extends BaseMapper<TBdAccountviewEntity> {

    List<KingdeeAccountDTO> selectAllAccount();

}
