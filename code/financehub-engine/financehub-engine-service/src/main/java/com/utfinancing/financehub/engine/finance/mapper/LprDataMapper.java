package com.utfinancing.financehub.engine.finance.mapper;

import com.utfinancing.financehub.engine.finance.entity.LprDataEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * lpr数据表 Mapper 接口
 * </p>
 *
 * @author hzhao
 * @since 2023-10-17
 */
public interface LprDataMapper extends BaseMapper<LprDataEntity> {
    int deleteAll();
}
