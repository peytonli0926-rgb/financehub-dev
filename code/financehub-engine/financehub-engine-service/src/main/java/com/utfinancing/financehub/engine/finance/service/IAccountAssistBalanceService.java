package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.entity.FileRecordEntity;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.AccountAssistBalanceVO;
import com.utfinancing.financehub.engine.finance.entity.AccountAssistBalanceEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.finance.model.vo.AccountAssistCurrentBalanceSheetVO;
import com.utfinancing.financehub.engine.finance.model.vo.AccountBalanceSheetVO;

import java.util.List;
import java.util.Map;

/**
 * @Author : lixin
 * @Date : Create in 2024-01-05
 * @Description : AccountAssistBalance服务类接口
 * @Modified :
 */
public interface IAccountAssistBalanceService extends IService<AccountAssistBalanceEntity> {

    IPage<AccountAssistBalanceVO> assistBalancePage(AccountAssistBalanceQueryDTO queryDTO);

    IPage<AccountBalanceSheetVO> accountBalancePage(AccountBalanceSheetQueryDTO queryDTO);


    void saveAccountBalanceByVoucherDTO(VoucherDTO voucherDTO);

    AccountAssistBalanceEntity getOneAccountAssistBalance(AccountAssistBalanceGetOneQueryDTO queryDTO);

    List<AccountBalanceSheetExcelExportDTO> listByCondition(AccountBalanceSheetQueryDTO queryDTO);

    IPage<AccountAssistCurrentBalanceSheetVO> currentBalancePage(AccountAssistCurrentBalanceSheetQueryDTO queryDTO);

    List<AccountAssistCurrentBalanceSheetVO> getCurrentBalanceList(AccountAssistCurrentBalanceSheetQueryDTO queryDTO);

    List<AccountAssistBalanceVO> getAssistBalanceData(AccountAssistBalanceQueryDTO queryDTO);

    Map<String, String> generateAssistBalanceExcel(AccountAssistBalanceQueryDTO queryDTO);

    IPage<FileRecordEntity> selectAssistBalanceFileList(FileRecordQueryDTO queryDTO);
}
