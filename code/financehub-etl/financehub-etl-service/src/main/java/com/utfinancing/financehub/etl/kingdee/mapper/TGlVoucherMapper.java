package com.utfinancing.financehub.etl.kingdee.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.etl.financial.entity.KingdeeContractBalanceEntity;
import com.utfinancing.financehub.etl.financial.entity.KingdeeVoucherEntity;
import com.utfinancing.financehub.etl.financial.entity.KingdeeVoucherEntryEntity;
import com.utfinancing.financehub.etl.kingdee.entity.TBdCustomer;
import com.utfinancing.financehub.etl.kingdee.entity.TGlVoucherEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.etl.kingdee.model.dto.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lixin
 * @since 2023-11-12
 */
public interface TGlVoucherMapper extends BaseMapper<TGlVoucherEntity> {

    List<ContractSumDTO> selectOneContract(Page<ContractSumDTO> contractSumDTOPage);

    List<KingdeeVoucherEntity> selectVoucherByPeriodAndDate(@Param("periodCode")Integer periodCode,
                                                            @Param("voucherDate")String voucherDate);

    KingdeeVoucherEntity selectVoucherByEasVoucherId(@Param("easVoucherId")String easVoucherId);


    List<KingdeeVoucherEntity> selectVoucherByPeriod(@Param("periodCode")Integer periodCode);


    List<KingdeeVoucherEntryEntity> selectVoucherEntryByPeriodAndDate(@Param("voucherId")String voucherId);

    List<KingdeeVoucherEntryEntity> selectVoucherEntryByPeriodCode(@Param("periodCode")Integer periodCode, @Param("voucherDate")String voucherDate,@Param("bizStatus") Integer bizStatus,@Param("contractCodeList")List<String> contractCodeList);


    List<KingdeeContractBalanceDTO> selectAccountBalanceList(@Param("clientCode")String clientCode, @Param("contractCode")String contractCode);

    List<KingdeeContractBalanceEntity> selectContractBalance(@Param("periodCode")Integer periodCode,@Param("contractCodeList")List<String> contractCodeList);

    List<KingdeeContractBalanceEntity> selectContractBalance80001(@Param("periodCode")Integer periodCode,@Param("contractCodeList")List<String> contractCodeList);

    List<KingdeeAssistBalanceDTO> selectKingdeeAssistBalanceList(@Param("periodCode")Integer periodCode, @Param("orgId")String orgId);

    List<TBdCustomer> selectCustomerById(@Param("clientCode")String clientCode);

    List<Map<String, Object>> selectKingdeeData(@Param("statement")String statement);
}
