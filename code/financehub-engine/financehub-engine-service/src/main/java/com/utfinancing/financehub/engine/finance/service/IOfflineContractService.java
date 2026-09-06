package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.ContractStatusRecordVO;
import com.utfinancing.financehub.engine.finance.model.vo.OfflineContractVO;
import com.utfinancing.financehub.engine.finance.entity.OfflineContractEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;

import java.util.List;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-18
 * @Description : OfflineContract服务类接口
 * @Modified :
 */
public interface IOfflineContractService extends IService<OfflineContractEntity> {

    Long saveOfflineContract(OfflineContractDTO dto);

    Long updateOfflineContract(Long id, OfflineContractDTO dto);

    OfflineContractDTO getOfflineContractDTOById(Long id);

    IPage<OfflineContractVO> selectPage(OfflineContractQueryDTO queryDTO);
    List<OfflineContractVO> selectList(OfflineContractQueryDTO queryDTO);

    void deleteByIds(List<Long> ids);

    void importData(List<OfflineContractExcel> contractExcels, List<OfflineContractStructureExcel> contractStructureExcels, List<OfflineContractRepaymentPlanExcel> repaymentPlanExcels);

    Void submit(List<Long> ids);
    Void withdraw(List<Long> ids);
    Void pass(List<Long> ids);
    Void fail(List<Long> ids);

    Boolean voucher(List<Long> ids,String isSubmit);

    /**
     * 审批后修改状态
     * @param approveDTO
     */
    void updateProcessStatus(CommonApproveDTO approveDTO);

    void batchDeleteVoucher(List<Long> ids);
}
