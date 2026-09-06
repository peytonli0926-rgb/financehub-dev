package com.utfinancing.financehub.engine.finance.service;

import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.engine.finance.entity.NonConfirmCollectionSumEntity;
import com.utfinancing.financehub.engine.finance.model.dto.FundEbankTransactionDataQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.FundEbankTransactionDataDTO;
import com.utfinancing.financehub.engine.finance.model.dto.SelectFundEbankTransactionDataByConDTO;
import com.utfinancing.financehub.engine.finance.model.vo.FundEbankTransactionDataVO;
import com.utfinancing.financehub.engine.finance.entity.FundEbankTransactionDataEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-17
 * @Description : FundEbankTransactionData服务类接口
 * @Modified :
 */
public interface IFundEbankTransactionDataService extends IService<FundEbankTransactionDataEntity> {

    Long saveFundEbankTransactionData(FundEbankTransactionDataDTO dto);

    void saveRawData(JSONArray jsonArray);

    Long updateFundEbankTransactionData(Long id, FundEbankTransactionDataDTO dto);

    FundEbankTransactionDataDTO getFundEbankTransactionDataDTOById(Long id);

    IPage<FundEbankTransactionDataVO> selectPage(FundEbankTransactionDataQueryDTO queryDTO);

    void ebankTransactionGenerateVoucher(List<FundEbankTransactionDataEntity> ebankTransactionDataEntityList);

    Boolean transactionGenerateVoucher(FundEbankTransactionDataQueryDTO queryDTO);

    Boolean testGenerateVoucherByDay(FundEbankTransactionDataQueryDTO queryDTO);

    /**
     * 根据条件查询资金系统收款数据
     */
    List<FundEbankTransactionDataEntity> selectFundEbankTransactionDataByCon(SelectFundEbankTransactionDataByConDTO params);

    void generateMappingVoucher();
}
