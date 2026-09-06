package com.utfinancing.financehub.engine.finance.mapper;

import cn.hutool.core.date.DateTime;
import com.utfinancing.financehub.engine.finance.entity.AccountAssistBalanceEntity;
import com.utfinancing.financehub.engine.finance.entity.PayableInsuranceDetailsEntity;
import com.utfinancing.financehub.engine.finance.entity.PayableInsuranceEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.dto.AccountAssistBalanceQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.PayableInsuranceQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.AccountAssistBalanceVO;
import com.utfinancing.financehub.engine.finance.model.vo.PayableInsuranceDetailsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 应付保险费表 Mapper 接口
 * </p>
 *
 * @author hzhao
 * @since 2023-10-24
 */
public interface PayableInsuranceMapper extends BaseMapper<PayableInsuranceEntity> {

    List<PayableInsuranceDetailsVO> selectPayableInsuranceList(@Param("param") PayableInsuranceQueryDTO queryDTO);


    List<PayableInsuranceDetailsEntity> getPayableInsuranceDetails(@Param("param")AccountAssistBalanceQueryDTO balanceQueryDTO);
}
