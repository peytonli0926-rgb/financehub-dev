package com.utfinancing.financehub.engine.scene.mapper;

import com.utfinancing.financehub.engine.scene.entity.AccountEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 科目 Mapper 接口
 * </p>
 *
 * @author lixin
 * @since 2023-08-25
 */
public interface AccountMapper extends BaseMapper<AccountEntity> {

    String getFundTypeString(@Param("accountCodeList") List<String> accountCodeList);
}
