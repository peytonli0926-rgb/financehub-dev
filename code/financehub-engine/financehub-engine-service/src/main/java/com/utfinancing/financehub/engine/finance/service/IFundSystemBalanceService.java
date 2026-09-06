package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.FundSystemBalanceQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.FundSystemBalanceDTO;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherDTO;
import com.utfinancing.financehub.engine.finance.model.vo.FundSystemBalanceVO;
import com.utfinancing.financehub.engine.finance.entity.FundSystemBalanceEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2023-12-05
 * @Description : FundSystemBalance服务类接口
 * @Modified :
 */
public interface IFundSystemBalanceService extends IService<FundSystemBalanceEntity> {

    Long saveFundSystemBalance(FundSystemBalanceDTO dto);

    Long updateFundSystemBalance(Long id, FundSystemBalanceDTO dto);

    FundSystemBalanceDTO getFundSystemBalanceDTOById(Long id);

    IPage<FundSystemBalanceVO> selectPage(FundSystemBalanceQueryDTO queryDTO);

    Boolean saveFundSystemBalanceFromVoucher(VoucherDTO voucherDTO);

}
