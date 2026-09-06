package com.utfinancing.financehub.engine.finance.mapper;

import com.utfinancing.financehub.engine.finance.entity.AccountBalanceEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.dto.AccountBalanceQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.AccountBalanceVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 科目余额表 Mapper 接口
 * </p>
 *
 * @author lixin
 * @since 2023-09-11
 */
public interface AccountBalanceMapper extends BaseMapper<AccountBalanceEntity> {

    List<AccountBalanceVO> selectAccountBefore(@Param("param") AccountBalanceQueryDTO param);
}
