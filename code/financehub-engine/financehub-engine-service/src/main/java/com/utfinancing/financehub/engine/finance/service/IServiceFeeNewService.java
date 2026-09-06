package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.ServiceFeeDetailsNewVO;
import com.utfinancing.financehub.engine.finance.model.vo.ServiceFeeNewVO;
import com.utfinancing.financehub.engine.finance.entity.ServiceFeeNewEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;

import java.util.List;

/**
 * @Author : le
 * @Date : Create in 2025-11-10
 * @Description : ServiceFeeNew服务类接口
 * @Modified :
 */
public interface IServiceFeeNewService extends IService<ServiceFeeNewEntity> {


    void submit(List<Long> ids);

    void withdraw(List<Long> ids);

    Boolean voucher(List<Long> ids, String isSubmit);

    void reversalVoucher(List<Long> ids);

    void deleteByIds(List<Long> ids);

    R importData(List<ServiceFeeImport> list);

    void batchDeleteVoucher(List<Long> ids);

    IPage<ServiceFeeNewVO> selectPage(ServiceFeeQueryDTO queryDTO);

    List<ServiceFeeDetailsNewVO> selectExportDetailList(ServiceFeeDetailsQueryDTO serviceFeeDetailsQueryDTO);

    void updateProcessStatus(CommonApproveDTO approveDTO);
}
