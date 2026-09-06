package com.utfinancing.financehub.engine.finance.service;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.FundBusinessSystemEbankMappingQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.FundBusinessSystemEbankMappingDTO;
import com.utfinancing.financehub.engine.finance.model.dto.SystemBankMappingReconciliationDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.SystemBankMappingReconciliationQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.FundBusinessSystemEbankMappingVO;
import com.utfinancing.financehub.engine.finance.entity.FundBusinessSystemEbankMappingEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.finance.model.vo.SystemBankMappingReconciliationDetailVO;
import com.utfinancing.financehub.engine.finance.model.vo.SystemBankMappingReconciliationNoVO;
import com.utfinancing.financehub.engine.finance.model.vo.SystemBankMappingReconciliationVO;

import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2023-12-21
 * @Description : FundBusinessSystemEbankMapping服务类接口
 * @Modified :
 */
public interface IFundBusinessSystemEbankMappingService extends IService<FundBusinessSystemEbankMappingEntity> {

    Long saveFundBusinessSystemEbankMapping(FundBusinessSystemEbankMappingDTO dto);

    /**
     * 根据条件查询业务系统和资金系统网银编号映射关系
     */
    public List<FundBusinessSystemEbankMappingEntity> selectFundBusinessSystemEbankMappingByCon(
            FundBusinessSystemEbankMappingEntity entity);

    Long updateFundBusinessSystemEbankMapping(Long id, FundBusinessSystemEbankMappingDTO dto);

    FundBusinessSystemEbankMappingDTO getFundBusinessSystemEbankMappingDTOById(Long id);

    IPage<FundBusinessSystemEbankMappingVO> selectPage(FundBusinessSystemEbankMappingQueryDTO queryDTO);

    /**
     * 保存资金系统和业务系统网银编号映射数据
     * @param ebankMappingDTO
     * @return
     */
    Boolean saveEbankMapping(List<FundBusinessSystemEbankMappingDTO> ebankMappingDTO);

    /**
     * 关联网银收款信息入库到未确认收款汇总表
     */
    public void selectNonConfirmCollectionFromFundSystem();


    /**
     * 根据批扣号查询资金系统网银编号映射关系
     */
    public FundBusinessSystemEbankMappingEntity selectFundBusinessSystemEbankMappingBySerialNumber(
            String ebankSerialNumber);

    IPage<SystemBankMappingReconciliationVO> systemBankMappingReconciliationFilter(SystemBankMappingReconciliationQueryDTO dto);

    IPage<SystemBankMappingReconciliationDetailVO> systemBankMappingReconciliationDetailByMatchNumber(SystemBankMappingReconciliationDetailQueryDTO dto);

    IPage<SystemBankMappingReconciliationNoVO> systemBankMappingReconciliationNoVO(BaseQueryDTO dto);
}
