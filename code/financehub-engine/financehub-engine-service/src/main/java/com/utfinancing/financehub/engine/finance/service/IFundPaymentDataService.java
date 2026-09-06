package com.utfinancing.financehub.engine.finance.service;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.FundEbankTransactionDataQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.FundPaymentDataQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.FundPaymentDataDTO;
import com.utfinancing.financehub.engine.finance.model.vo.FundPaymentDataVO;
import com.utfinancing.financehub.engine.finance.entity.FundPaymentDataEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-17
 * @Description : FundPaymentData服务类接口
 * @Modified :
 */
public interface IFundPaymentDataService extends IService<FundPaymentDataEntity> {

    Long saveFundPaymentData(FundPaymentDataDTO dto);

    FundPaymentDataEntity saveRawData(JSONObject jsonData);

    Long updateFundPaymentData(Long id, FundPaymentDataDTO dto);

    FundPaymentDataDTO getFundPaymentDataDTOById(Long id);

    IPage<FundPaymentDataVO> selectPage(FundPaymentDataQueryDTO queryDTO);

    void paymentGenerateVoucher(List<FundPaymentDataEntity> fundPaymentDataEntityList);

    Boolean payMentGenerateVoucher(FundPaymentDataQueryDTO queryDTO);

    Boolean testGenerateVoucher2ByDay(FundPaymentDataQueryDTO queryDTO);


}
