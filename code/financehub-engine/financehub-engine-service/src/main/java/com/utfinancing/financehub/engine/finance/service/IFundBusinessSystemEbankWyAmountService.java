package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.FundBusinessSystemEbankWyAmountQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.FundBusinessSystemEbankWyAmountDTO;
import com.utfinancing.financehub.engine.finance.model.dto.SelectNonConfirmCollectionDataDTO;
import com.utfinancing.financehub.engine.finance.model.vo.FundBusinessSystemEbankWyAmountVO;
import com.utfinancing.financehub.engine.finance.entity.FundBusinessSystemEbankWyAmountEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2024-07-16
 * @Description : FundBusinessSystemEbankWyAmount服务类接口
 * @Modified :
 */
public interface IFundBusinessSystemEbankWyAmountService extends IService<FundBusinessSystemEbankWyAmountEntity> {

    Long saveFundBusinessSystemEbankWyAmount(FundBusinessSystemEbankWyAmountDTO dto);

    Long updateFundBusinessSystemEbankWyAmount(Long id, FundBusinessSystemEbankWyAmountDTO dto);

    FundBusinessSystemEbankWyAmountDTO getFundBusinessSystemEbankWyAmountDTOById(Long id);

    IPage<FundBusinessSystemEbankWyAmountVO> selectPage(FundBusinessSystemEbankWyAmountQueryDTO queryDTO);

    /**
     * 根据条件查询资金系统收款数据
     */
    public List<FundBusinessSystemEbankWyAmountEntity> selectFundEbankTransactionDataByCon(
            SelectNonConfirmCollectionDataDTO dto);

    /**
     * 根据match number查询网银数据
     */
    public FundBusinessSystemEbankWyAmountEntity selectFundEbankTransactionDataByCon(String matchNumber);
}
