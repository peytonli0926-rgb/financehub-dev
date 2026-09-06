package com.utfinancing.financehub.engine.contractstatusupdate.service;

import com.utfinancing.financehub.engine.approve.entity.ApproveEntity;
import com.utfinancing.financehub.engine.finance.entity.ContractStatusRecordEntity;
import com.utfinancing.financehub.engine.finance.entity.RecyclingEquipmentInEntity;
import com.utfinancing.financehub.engine.finance.entity.RecyclingEquipmentOutDetailEntity;
import com.utfinancing.financehub.engine.verification.entity.VerificationDetailsEntity;

import java.util.List;

public interface IContractStatusUpdateService {
    /**
     * 我的单据-审批_更新合同财务状态
     * @param approveEntityList
     * @return
     */
    boolean updateContractStatus(List<ApproveEntity> approveEntityList);
    /**
     * 坏账核销更新合同表财务合同状态
     * @param entityList
     * @return
     */
    boolean updateHZHXContractStatus(List<VerificationDetailsEntity> entityList);
    /**
     * 特殊合同更新合同表财务合同状态
     * @param entityList
     * @return
     */
    boolean updateTSZTContractStatus(List<ContractStatusRecordEntity> entityList);
    /**
     * 回收设备财务入库更新合同表财务合同状态
     * @param entityList
     * @return
     */
    boolean updateHSSBCWRKContractStatus(List<RecyclingEquipmentInEntity> entityList);

    /**
     * 回收设备财务出库更新合同表财务合同状态
     * @param entityList
     * @return
     */
    boolean updateHSSBCWCKContractStatus(List<RecyclingEquipmentOutDetailEntity> entityList);

}
