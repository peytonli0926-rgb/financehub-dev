package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.finance.entity.ContractHisEntity;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.ContractHisVO;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;

import java.util.List;

/**
 * @Author : hzhao
 * @Date : Create in 2023-11-01
 * @Description : ContractHis服务类接口
 * @Modified :
 */
public interface IContractHisService extends IService<ContractHisEntity> {

    Long saveContractHis(ContractHisDTO dto);

    Long updateContractHis(Long id, ContractHisDTO dto);

    ContractHisDTO getContractHisDTOById(Long id);
    ContractHisDTO getContractHisDTOByCode(String code);

    IPage<ContractHisVO> selectPage(ContractHisQueryDTO queryDTO);

    List<ContractHisVO> selectList(ContractHisQueryDTO queryDTO);

    void deleteByIds(List<Long> ids);

    void submit(List<Long> ids);

    void withdraw(List<Long> ids);

    void pass(List<Long> ids);

    void fail(List<Long> ids);

    Boolean voucher(List<Long> ids, String isSubmit);

    void importData(List<ContractHisExcel> contractExcels, List<ContractHisStructureExcel> contractStructureExcels, List<ContractHisRepaymentPlanExcel> repaymentPlanExcels);

    /**
     * 审批后修改状态
     * @param approveDTO
     */
    void updateProcessStatus(CommonApproveDTO approveDTO);
}
