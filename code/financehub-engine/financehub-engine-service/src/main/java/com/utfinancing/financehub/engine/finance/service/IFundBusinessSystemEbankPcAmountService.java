package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.FundBusinessSystemEbankPcAmountQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.FundBusinessSystemEbankPcAmountDTO;
import com.utfinancing.financehub.engine.finance.model.vo.FundBusinessSystemEbankPcAmountVO;
import com.utfinancing.financehub.engine.finance.entity.FundBusinessSystemEbankPcAmountEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2024-07-16
 * @Description : FundBusinessSystemEbankPcAmount服务类接口
 * @Modified :
 */
public interface IFundBusinessSystemEbankPcAmountService extends IService<FundBusinessSystemEbankPcAmountEntity> {

    Long saveFundBusinessSystemEbankPcAmount(FundBusinessSystemEbankPcAmountDTO dto);

    Long updateFundBusinessSystemEbankPcAmount(Long id, FundBusinessSystemEbankPcAmountDTO dto);

    FundBusinessSystemEbankPcAmountDTO getFundBusinessSystemEbankPcAmountDTOById(Long id);

    IPage<FundBusinessSystemEbankPcAmountVO> selectPage(FundBusinessSystemEbankPcAmountQueryDTO queryDTO);

}
