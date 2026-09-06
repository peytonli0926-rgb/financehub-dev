package com.utfinancing.financehub.engine.finance.mapper;

import com.utfinancing.financehub.engine.finance.entity.AccountAssistBalanceEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.dto.AccountAssistBalanceQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.AccountAssistCurrentBalanceSheetQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.AccountBalanceSheetQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.AccountAssistBalanceVO;
import com.utfinancing.financehub.engine.finance.model.vo.AccountAssistCurrentBalanceSheetVO;
import com.utfinancing.financehub.engine.finance.model.vo.AccountBalanceSheetVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 科目辅助帐余额表 Mapper 接口
 * </p>
 *
 * @author lixin
 * @since 2024-01-05
 */
public interface AccountAssistBalanceMapper extends BaseMapper<AccountAssistBalanceEntity> {

    Map<String, Long> selectCurrentBalanceSize(@Param("param") AccountAssistCurrentBalanceSheetQueryDTO queryDTO);

    List<AccountAssistCurrentBalanceSheetVO> selectCurrentBalance(@Param("param") AccountAssistCurrentBalanceSheetQueryDTO queryDTO);

    List<AccountAssistBalanceVO> selectAssistBalance(@Param("param") AccountAssistBalanceQueryDTO queryDTO);

    Map<String, Long> selectAssistBalanceSize(@Param("param") AccountAssistBalanceQueryDTO queryDTO);

    List<AccountBalanceSheetVO> selectAccountBalance(@Param("param")AccountBalanceSheetQueryDTO queryDTO);

    Map<String, Long> selectAccountBalanceSize(@Param("param")AccountBalanceSheetQueryDTO queryDTO);

    List<AccountBalanceSheetVO> selectAssistPart(@Param("param") AccountBalanceSheetQueryDTO queryDTO);

    List<AccountBalanceSheetVO> selectVoucherPart(@Param("param") AccountBalanceSheetQueryDTO queryDTO);

    List<AccountBalanceSheetVO> selectManualPart(@Param("param") AccountBalanceSheetQueryDTO queryDTO);

    List<AccountBalanceSheetVO> selectVoucherIdPart(@Param("param") AccountBalanceSheetQueryDTO queryDTOBak);

    Map<String, Long> selectAssistPartSize(@Param("param") AccountBalanceSheetQueryDTO queryDTO);

    Map<String, Long> selectVoucherPartSize(@Param("param") AccountBalanceSheetQueryDTO queryDTO);

    Map<String, Long> selectManualPartSize(@Param("param") AccountBalanceSheetQueryDTO queryDTO);

    List<AccountAssistBalanceVO> selectAccountAssistPart(@Param("param") AccountAssistBalanceQueryDTO queryDTO);

    Map<String, Long> selectAccountAssistPartSize(@Param("param") AccountAssistBalanceQueryDTO queryDTO);

    List<AccountAssistBalanceVO> selectAccountVoucherPart(@Param("param") AccountAssistBalanceQueryDTO queryDTO);

    Map<String, Long> selectAccountVoucherPartSize(@Param("param") AccountAssistBalanceQueryDTO queryDTO);

    List<AccountAssistBalanceVO> selectAccountManualPart(@Param("param") AccountAssistBalanceQueryDTO queryDTO);

    Map<String, Long> selectAccountManualPartSize(@Param("param") AccountAssistBalanceQueryDTO queryDTO);

    List<AccountAssistBalanceVO> selectAccountVoucherIdPart(@Param("param") AccountAssistBalanceQueryDTO queryDTOBak);

    List<AccountAssistBalanceVO> selectListByParams(@Param("param") AccountAssistBalanceQueryDTO queryDTO);

    List<AccountBalanceSheetVO> selectAccountAll(@Param("param")AccountBalanceSheetQueryDTO queryDTO);

    Map<String, Long> selectAccountAllSize(@Param("param")AccountBalanceSheetQueryDTO queryDTO);

    List<AccountAssistBalanceVO> selectAssistAll(@Param("param")AccountAssistBalanceQueryDTO queryDTO);

    Map<String, Long> selectAssistAllSize(@Param("param")AccountAssistBalanceQueryDTO queryDTO);
}
