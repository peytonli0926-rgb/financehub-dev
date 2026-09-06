package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.enums.MarginTypeEnum;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.MarginContractBalanceVO;
import com.utfinancing.financehub.engine.finance.entity.MarginContractBalanceEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;

import java.util.List;
import java.util.Map;

/**
 * @Author : hzhao
 * @Date : Create in 2023-09-24
 * @Description : MarginContractBalance服务类接口
 * @Modified :
 */
public interface IMarginContractBalanceService extends IService<MarginContractBalanceEntity> {

    Long saveMarginContractBalance(MarginContractBalanceDTO dto);
    Void saveMarginContractBalanceBatch(List<MarginContractBalanceSaveDTO> dto);

    Long updateMarginContractBalance(Long id, MarginContractBalanceDTO dto);

    MarginContractBalanceDTO getMarginContractBalanceDTOById(Long id);

    IPage<MarginContractBalanceVO> selectPage(MarginContractBalanceQueryDTO queryDTO);
    List<MarginContractBalanceVO> selectList(MarginContractBalanceQueryDTO queryDTO);

    IPage<MarginContractBalanceVO> selectSummaryPage(MarginContractBalanceQueryDTO queryDTO);

    List<MarginContractBalanceVO> enterList();

    Void generate(MarginContractBalanceQueryDTO queryDTO);
    void voucher(MarginContractBalanceCheckDTO checkDTO, MarginTypeEnum marginTypeEnum,String isSubmit);

    String submmit(MarginContractBalanceCheckDTO checkDTO);
    Void pass(MarginContractBalanceCheckDTO checkDTO);
    Void fail(MarginContractBalanceCheckDTO checkDTO);

    Void withdraw(MarginContractBalanceCheckDTO checkDTO);

    /**
     * 更新状态
     * @param commonApproveDTO
     * @return
     */
    Boolean updateProcessStatus(CommonApproveDTO commonApproveDTO);

    Map<String, String> export(MarginContractBalanceQueryDTO queryDTO);
}
