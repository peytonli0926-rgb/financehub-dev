package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.ServiceFeeDetailsVO;
import com.utfinancing.financehub.engine.finance.model.vo.ServiceFeePlanVO;
import com.utfinancing.financehub.engine.finance.model.vo.ServiceFeeVO;
import com.utfinancing.financehub.engine.finance.entity.ServiceFeeEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;

import java.util.List;

/**
 * @Author : hzhao
 * @Date : Create in 2023-11-07
 * @Description : ServiceFee服务类接口
 * @Modified :
 */
public interface IServiceFeeService extends IService<ServiceFeeEntity> {

    Long saveServiceFee(ServiceFeeDTO dto);

    Long updateServiceFee(Long id, ServiceFeeDTO dto);

    ServiceFeeDTO getServiceFeeDTOById(Long id);

    IPage<ServiceFeeVO> selectPage(ServiceFeeQueryDTO queryDTO);

    IPage<ServiceFeeDetailsVO> selectDetailPage(ServiceFeeDetailsQueryDTO queryDTO);

    List<ServiceFeeDetailsVO> selectExportDetailList(ServiceFeeDetailsQueryDTO queryDTO);

    List<ServiceFeePlanVO > selectDetailPlanList(ServiceFeeDetailsQueryDTO queryDTO);

    void deleteByIds(List<Long> ids);

    void submit(List<Long> ids);

    void withdraw(List<Long> ids);

    Boolean voucher(List<Long> ids, String isSubmit);

    void reversalVoucher(List<Long> ids);

    R importData(List<ServiceFeeImport> list);

    R measurement(ServiceFeeQueryDTO queryDTO);

    void updateProcessStatus(CommonApproveDTO approveDTO);

    void batchDeleteVoucher(List<Long> ids);
}
