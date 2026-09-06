package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.BusinessClaimRepaymentRecordVO;
import com.utfinancing.financehub.engine.finance.entity.BusinessClaimRepaymentRecordEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author : robjiang
 * @Date : Create in 2024-03-21
 * @Description : BusinessClaimRepaymentRecord服务类接口
 * @Modified :
 */
public interface IBusinessClaimRepaymentRecordService extends IService<BusinessClaimRepaymentRecordEntity> {

    Long saveBusinessClaimRepaymentRecord(BusinessClaimRepaymentRecordDTO dto);

    Long updateBusinessClaimRepaymentRecord(Long id, BusinessClaimRepaymentRecordDTO dto);

    BusinessClaimRepaymentRecordDTO getBusinessClaimRepaymentRecordDTOById(Long id);

    IPage<BusinessClaimRepaymentRecordVO> selectPage(BusinessClaimRepaymentRecordQueryDTO queryDTO);

    public void extractClaimPaymentRecord();


    /**
     * 根据批扣流水号查询认领记录
     */
    public List<BusinessClaimRepaymentRecordEntity> selectClaimRecordByDeductBatchNo(String deductBatchNo,
            List<String> processStatusList, String contractCode, List<String> operationTypeList);


    /**
     * 根据未确认收款明细表id查询认领记录
     */
    public List<BusinessClaimRepaymentRecordEntity> selectClaimRecordBySecondDetailId(String nonConfirmSecondDetailId,
                                                                                      List<String> processStatusList);

    /**
     * 取最大batch no值
     */
    public int getMaxBatchNo(String ebankSerialNumber);

    /**
     * 取得已经认领金额
     */
    public BigDecimal getReadyClaimAmount(List<BusinessClaimRepaymentRecordEntity> businessClaimRepaymentRecordEntityList);

    /**
     * 更新状态
     */
    public boolean processStatusUpdate(Long detailId, String status);

    /**
     * 批量更新状态
     */
    public boolean processStatusBatchUpdate(List<Long> detailIds, String status);

    /**
     * 根据客户编码查询已经认领金额和到账金额总和
     */
    public SelectClaimByClientCodeDTO selectClaimByClientCode(String clientCode);

    /**
     * 修改入账日期查询
     */
    public List<SelectIncomeDateInfoOutputDTO> selectIncomeDateInfo(SelectIncomeDateInfoInputDTO params);

    /**
     * 根据合同及日期，在参数日期之后取得合同认领的金额
     */
    public BusinessClaimRepaymentRecordEntity queryClaimAmountByContract(String contractCode, Date date);
}
