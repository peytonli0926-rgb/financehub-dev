package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.RentIncomeConfirmQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.RentIncomeConfirmDTO;
import com.utfinancing.financehub.engine.finance.model.vo.RentIncomeConfirmVO;
import com.utfinancing.financehub.engine.finance.entity.RentIncomeConfirmEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;

import java.util.List;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-10
 * @Description : RentIncomeConfirm服务类接口
 * @Modified :
 */
public interface IRentIncomeConfirmService extends IService<RentIncomeConfirmEntity> {

    Long saveRentIncomeConfirm(RentIncomeConfirmDTO dto);

    Long updateRentIncomeConfirm(Long id, RentIncomeConfirmDTO dto);

    RentIncomeConfirmDTO getRentIncomeConfirmDTOById(Long id);

    IPage<RentIncomeConfirmVO> selectPage(RentIncomeConfirmQueryDTO queryDTO);

    /**
     * 根据合同号查询
     *
     * @param contractCode
     * @return
     */
    List<RentIncomeConfirmEntity> getByContractCode(String contractCode);

    /**
     * 根据合同编号删除数据
     *
     * @param contractCode
     */
    void deleteByContractCode(String contractCode);

    /**
     * 导出
     *
     * @param queryDTO
     * @return
     */
    List<RentIncomeConfirmVO> selectList(RentIncomeConfirmQueryDTO queryDTO);

    /**
     * 批量生成收入确认凭证
     *
     * @param ids
     * @param code
     * @return
     */
    Boolean generateVoucher(List<Long> ids, String code);

    /**
     * 批量生成结转凭证
     *
     * @param ids
     * @param code
     * @return
     */
    Boolean generateCarryForwardVoucher(List<Long> ids, String code);

    /**
     * 批量提交
     *
     * @param ids
     * @return
     */
    Boolean submit(List<Long> ids);

    /**
     * 批量撤回
     *
     * @param ids
     * @return
     */
    Boolean withdraw(List<Long> ids);

    void updateProcessStatus(CommonApproveDTO approveDTO);
}
