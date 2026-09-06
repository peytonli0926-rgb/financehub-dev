package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeeDetailsNewQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeeDetailsQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeeQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ServiceFeeDetailsNewVO;
import com.utfinancing.financehub.engine.finance.entity.ServiceFeeDetailsNewEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : le
 * @Date : Create in 2025-11-10
 * @Description : ServiceFeeDetailsNew服务类接口
 * @Modified :
 */
public interface IServiceFeeDetailsNewService extends IService<ServiceFeeDetailsNewEntity> {


    IPage<ServiceFeeDetailsNewVO> selectPage(ServiceFeeDetailsNewQueryDTO queryDTO);

    List<ServiceFeeDetailsNewEntity> selectLastPeriodData(ServiceFeeDetailsNewVO serviceFeeDetailsVO);

    IPage<ServiceFeeDetailsNewVO> selectDetailPage(ServiceFeeDetailsQueryDTO queryDTO);

    void updateVoucherStatus(List<Long> ids, String code, Integer periodCode);

    List<ServiceFeeDetailsNewEntity> getUnapportionedAndNoPlanData(ServiceFeeQueryDTO queryDTO, List<String> allocateContractCodes);
    List<ServiceFeeDetailsNewEntity> getUnapportionedAndNoPlanData(String date, List<String> allocateContractCodes);
}
