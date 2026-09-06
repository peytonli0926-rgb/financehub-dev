package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.FundBusinessSystemEbankAmountMappingQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.FundBusinessSystemEbankAmountMappingDTO;
import com.utfinancing.financehub.engine.finance.model.vo.FundBusinessSystemEbankAmountMappingVO;
import com.utfinancing.financehub.engine.finance.entity.FundBusinessSystemEbankAmountMappingEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2024-07-12
 * @Description : FundBusinessSystemEbankAmountMapping服务类接口
 * @Modified :
 */
public interface IFundBusinessSystemEbankAmountMappingService extends IService<FundBusinessSystemEbankAmountMappingEntity> {

    Long saveFundBusinessSystemEbankAmountMapping(FundBusinessSystemEbankAmountMappingDTO dto);

    Long updateFundBusinessSystemEbankAmountMapping(Long id, FundBusinessSystemEbankAmountMappingDTO dto);

    FundBusinessSystemEbankAmountMappingDTO getFundBusinessSystemEbankAmountMappingDTOById(Long id);

    IPage<FundBusinessSystemEbankAmountMappingVO> selectPage(FundBusinessSystemEbankAmountMappingQueryDTO queryDTO);

    void saveFundBusinessSystemEbankAmountMappingList(List<FundBusinessSystemEbankAmountMappingDTO> dtoList);

}
