package com.utfinancing.financehub.engine.rule.service;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.vo.ContractTransactionVO;
import com.utfinancing.financehub.engine.rule.model.dto.InterfaceDataQueryDTO;
import com.utfinancing.financehub.engine.rule.model.dto.InterfaceDataDTO;
import com.utfinancing.financehub.engine.rule.model.vo.InterfaceDataVO;
import com.utfinancing.financehub.engine.rule.entity.InterfaceDataEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.verification.model.dto.VerificationPaybackQueryDTO;
import com.utfinancing.financehub.engine.verification.model.vo.VerificationPaybackDetailsVO;
import com.utfinancing.financehub.engine.verification.model.vo.VerificationPaybackVO;

import java.util.List;
import java.util.Map;

/**
 * @Author : lixin
 * @Date : Create in 2023-09-14
 * @Description : InterfaceData服务类接口
 * @Modified :
 */
public interface IInterfaceDataService extends IService<InterfaceDataEntity> {

    Long saveInterfaceData(InterfaceDataDTO dto);

    Long updateInterfaceData(Long id, InterfaceDataDTO dto);

    InterfaceDataDTO getInterfaceDataDTOById(Long id);

//    IPage<InterfaceDataVO> selectPage(InterfaceDataQueryDTO queryDTO);

    /**
     * 租赁业务接口查询
     * @param queryDTO
     * @return
     */
    JSONObject selectPage(InterfaceDataQueryDTO queryDTO);

    /**
     * 保存接口数据
     */
    InterfaceDataDTO saveInterfaceDataFromMap(Map<String, Object> dataMap);

    /**
     * 核销回款分页接口
     * @param queryDTO
     * @return
     */
    IPage<VerificationPaybackVO> selectPaybackPage(VerificationPaybackQueryDTO queryDTO);

    /**
     * 获取接口数据
     * @param interfaceDataDTO
     * @return
     */
    List<InterfaceDataEntity> selectByCondition(InterfaceDataDTO interfaceDataDTO);

    /**
     * 核销回款详情分页接口
     * @param queryDTO
     * @return
     */
    IPage<VerificationPaybackDetailsVO> selectPaybackDetailsPage(VerificationPaybackQueryDTO queryDTO);

    List<VerificationPaybackDetailsVO> selectPayBackAmount(VerificationPaybackQueryDTO queryDTO);

    /**
     * 核销回款列表查询
     * @param queryDTO
     * @return
     */
    List<VerificationPaybackDetailsVO> listPaybackDetails(VerificationPaybackQueryDTO queryDTO);


    public List<InterfaceDataEntity> selectEbankSerialNumberNotNullData();

    IPage<ContractTransactionVO> contractTransactionByPage(InterfaceDataQueryDTO queryDTO);

    /**
     * 查询过去一年起租的合同信息
     */
    public List<InterfaceDataEntity> queryOnHireContract();

    /**
     * 核销回款分页接口
     * @param queryDTO
     * @return
     */
    List<VerificationPaybackVO> selectPaybackList(VerificationPaybackQueryDTO queryDTO);

    List<VerificationPaybackDetailsVO> selectPaybackDetailsList(VerificationPaybackQueryDTO queryDTO);
}
