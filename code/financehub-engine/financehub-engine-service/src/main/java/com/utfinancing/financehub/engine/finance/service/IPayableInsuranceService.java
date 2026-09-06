package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.PayableInsuranceDetailsVO;
import com.utfinancing.financehub.engine.finance.model.vo.PayableInsuranceVO;
import com.utfinancing.financehub.engine.finance.entity.PayableInsuranceEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;

import java.util.List;
import java.util.Map;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-24
 * @Description : PayableInsurance服务类接口
 * @Modified :
 */
public interface IPayableInsuranceService extends IService<PayableInsuranceEntity> {

    Long savePayableInsurance(PayableInsuranceDTO dto);

    Long updatePayableInsurance(Long id, PayableInsuranceDTO dto);

    PayableInsuranceDTO getPayableInsuranceDTOById(Long id);

    Void generate(PayableInsuranceQueryDTO queryDTO);

    Boolean voucher(List<Long> ids, String isSubmit);

    IPage<PayableInsuranceVO> selectPage(PayableInsuranceQueryDTO queryDTO);

    IPage<PayableInsuranceDetailsVO> selectDetailPage(PayableInsuranceDetailsQueryDTO queryDTO);

    List<PayableInsuranceDetailsVO> selectDetailList(PayableInsuranceQueryDTO queryDTO);
    void importData(List<PayableInsuranceDetailImport> list);
    Void submit(List<Long> ids);
    Void withdraw(List<Long> ids);

    Void pass(List<Long> ids);

    Void fail(List<Long> ids);

    void updateProcessStatus(CommonApproveDTO approveDTO);

    void batchDeleteVoucher(List<Long> ids);

    Map<String, String> export(PayableInsuranceQueryDTO queryDTO);



}
