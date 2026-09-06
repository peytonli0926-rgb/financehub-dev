package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.RentRegisterDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.RentRegisterDetailDTO;
import com.utfinancing.financehub.engine.finance.model.vo.RentRegisterDetailExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.RentRegisterDetailVO;
import com.utfinancing.financehub.engine.finance.entity.RentRegisterDetailEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-08
 * @Description : RentRegisterDetail服务类接口
 * @Modified :
 */
public interface IRentRegisterDetailService extends IService<RentRegisterDetailEntity> {

    Long saveRentRegisterDetail(RentRegisterDetailDTO dto);

    Long updateRentRegisterDetail(Long id, RentRegisterDetailDTO dto);

    RentRegisterDetailDTO getRentRegisterDetailDTOById(Long id);

    IPage<RentRegisterDetailVO> selectPage(RentRegisterDetailQueryDTO queryDTO);

    List<RentRegisterDetailExcelVO> selectList(RentRegisterDetailQueryDTO queryDTO);

    /**
     * 计算金额
     * @param contractCodeList
     */
    void calculateAmount(List<String> contractCodeList);

    /**
     * 根据合同编号查询
     * @param contractCodeList
     * @return
     */
    List<RentRegisterDetailVO> selectByContractCodeList(List<String> contractCodeList);

    /**
     * 根据出租登记id删除租金计划
     * @param id
     */
    void removeByRentRegisterId(Long id);
}
