package com.utfinancing.financehub.etl.kingdee.service;

import com.baomidou.dynamic.datasource.annotation.DS;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.etl.financial.entity.KingdeeContractBalanceEntity;
import com.utfinancing.financehub.etl.financial.entity.KingdeeVoucherEntity;
import com.utfinancing.financehub.etl.financial.entity.KingdeeVoucherEntryEntity;
import com.utfinancing.financehub.etl.kingdee.entity.TBdCustomer;
import com.utfinancing.financehub.etl.kingdee.model.dto.*;
import com.utfinancing.financehub.etl.kingdee.entity.TGlVoucherEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
 * @Description : TGlVoucher服务类接口
 * @Modified :
 */
@DS("slave_kingdee")
public interface ITGlVoucherService extends IService<TGlVoucherEntity> {

    List<ContractSumDTO> selectOneContract();

    List<KingdeeVoucherEntity> selectVoucherByPeriodAndDate(Integer periodCode,
                                                            String voucherDate);

    KingdeeVoucherEntity selectVoucherByEasId(String easVoucherId);

    List<KingdeeVoucherEntity> selectVoucherByPeriod(Integer periodCode);

    List<KingdeeVoucherEntryEntity> selectVoucherEntryByPeriodAndDate(String voucherId);

    List<KingdeeVoucherEntryEntity> selectVoucherEntryByPeriodCode(Integer periodCode, String voucherDate, Integer bizStatus,List<String> contractCodeList);

    List<KingdeeContractBalanceDTO> selectAccountBalanceList(String clientCode, String contractCode);


    List<KingdeeContractBalanceEntity> selectContractBalance(Integer periodCode,List<String> contractCodeList);
    List<KingdeeContractBalanceEntity> selectContractBalance80001(Integer periodCode,List<String> contractCodeList);

    List<KingdeeAssistBalanceDTO> selectKingdeeAssistBalanceList(Integer periodCode, String orgId);

    List<TBdCustomer> selectCustomerById(String clientCode);


    String exportKingdeeData(String statement,String columns);
}
