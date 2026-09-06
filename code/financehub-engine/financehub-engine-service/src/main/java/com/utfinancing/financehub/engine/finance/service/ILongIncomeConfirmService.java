package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.LongIncomeConfirmQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.LongIncomeConfirmDTO;
import com.utfinancing.financehub.engine.finance.model.vo.LongIncomeConfirmVO;
import com.utfinancing.financehub.engine.finance.entity.LongIncomeConfirmEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;

import java.util.List;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-15
 * @Description : LongIncomeConfirm服务类接口
 * @Modified :
 */
public interface ILongIncomeConfirmService extends IService<LongIncomeConfirmEntity> {

    Long saveLongIncomeConfirm(LongIncomeConfirmDTO dto);

    Long updateLongIncomeConfirm(Long id, LongIncomeConfirmDTO dto);

    LongIncomeConfirmDTO getLongIncomeConfirmDTOById(Long id);

    IPage<LongIncomeConfirmVO> selectPage(LongIncomeConfirmQueryDTO queryDTO);

    List<LongIncomeConfirmVO> selectList(LongIncomeConfirmQueryDTO queryDTO);

    Boolean generateVoucher(List<Long> ids, String code);

    Boolean submit(List<Long> ids);

    Boolean withdraw(List<Long> ids);

    void deleteByLongNumberAndContractCode(String longReceivableNumber, String contractCode);

    void updateProcessStatus(CommonApproveDTO approveDTO);

    void batchDeleteVoucher(List<Long> ids);
}
