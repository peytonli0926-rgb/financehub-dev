package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.InvoiceClaimDTO;
import com.utfinancing.financehub.engine.finance.model.dto.PayVatQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.PayVatDTO;
import com.utfinancing.financehub.engine.finance.model.vo.PayVatVO;
import com.utfinancing.financehub.engine.finance.entity.PayVatEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;

import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2024-03-20
 * @Description : PayVat服务类接口
 * @Modified :
 */
public interface IPayVatService extends IService<PayVatEntity> {

    Long savePayVat(PayVatDTO dto);

    Long updatePayVat(Long id, PayVatDTO dto);

    PayVatDTO getPayVatDTOById(Long id);

    IPage<PayVatVO> selectPage(PayVatQueryDTO queryDTO);

    /**
     * 获取合同数据放入应交增值税表
     *
     * @return
     */
    String payVatGetContract(Integer period);

    /**
     * 查询
     *
     * @param queryDTO
     * @return
     */
    List<PayVatVO> selectList(PayVatQueryDTO queryDTO);

    /**
     * 批量生成凭证
     *
     * @param ids
     * @param code
     * @return
     */
    Boolean generateVoucher(List<Long> ids, String code);

    /**
     * 提交
     *
     * @param ids
     * @return
     */
    Boolean submit(List<Long> ids);

    /**
     * 撤回
     *
     * @param ids
     * @return
     */
    Boolean withdraw(List<Long> ids);

    /**
     * 修改备注
     *
     * @param dto
     * @return
     */
    void updateComments(InvoiceClaimDTO dto);

    /**
     * 审批修改单据状态
     *
     * @param approveDTO
     */
    void updateProcessStatus(CommonApproveDTO approveDTO);

    void batchDeleteVoucher(List<Long> ids);
}
