package com.utfinancing.financehub.engine.finance.mapper;

import com.utfinancing.financehub.engine.finance.entity.PostalStorageFeeDetailsEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.dto.PostalStorageFeeDetailsQueryDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 邮储手续费详情 Mapper 接口
 * </p>
 *
 * @author hzhao
 * @since 2023-11-17
 */
public interface PostalStorageFeeDetailsMapper extends BaseMapper<PostalStorageFeeDetailsEntity> {
        List<PostalStorageFeeDetailsEntity> listByContractCodes(@Param("param") PostalStorageFeeDetailsQueryDTO queryDTO);
}
