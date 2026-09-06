package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.LeaseIncomeDetailsQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.LeaseIncomeDetailsDTO;
import com.utfinancing.financehub.engine.finance.model.vo.LeaseIncomeDetailsVO;
import com.utfinancing.financehub.engine.finance.entity.LeaseIncomeDetailsEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
 * @Author : hzhao
 * @Date : Create in 2023-11-13
 * @Description : LeaseIncomeDetails服务类接口
 * @Modified :
 */
public interface ILeaseIncomeDetailsService extends IService<LeaseIncomeDetailsEntity> {

    Long saveLeaseIncomeDetails(LeaseIncomeDetailsDTO dto);

    Long updateLeaseIncomeDetails(Long id, LeaseIncomeDetailsDTO dto);

    LeaseIncomeDetailsDTO getLeaseIncomeDetailsDTOById(Long id);

    IPage<LeaseIncomeDetailsVO> selectPage(LeaseIncomeDetailsQueryDTO queryDTO);

    List<LeaseIncomeDetailsVO> selectDetailsByCondition(LeaseIncomeDetailsQueryDTO queryDTO);

    /**
     * 取得合同收益计提数据-根据月份
     */
    public LeaseIncomeDetailsEntity getLeaseIncomeDetailsByMonth(String contractCode, Date planDate);
    public void leaseIncomeVadation();
}
