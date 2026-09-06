package com.utfinancing.financehub.etl.financial.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.etl.financial.model.dto.ContractQueryInfoDTO;
import com.utfinancing.financehub.etl.financial.model.dto.InvoiceClaimQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.InvoiceClaimDTO;
import com.utfinancing.financehub.etl.financial.model.vo.ContractInvoiceClaimVO;
import com.utfinancing.financehub.etl.financial.model.vo.InvoiceClaimVO;
import com.utfinancing.financehub.etl.financial.entity.InvoiceClaimEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2023-11-10
 * @Description : InvoiceClaim服务类接口
 * @Modified :
 */
@DS("master")
public interface IInvoiceClaimService extends IService<InvoiceClaimEntity> {

    Long saveInvoiceClaim(InvoiceClaimDTO dto);

    Long updateInvoiceClaim(Long id, InvoiceClaimDTO dto);

    InvoiceClaimDTO getInvoiceClaimDTOById(Long id);

    IPage<InvoiceClaimVO> selectPage(InvoiceClaimQueryDTO queryDTO);

    List<InvoiceClaimVO> listByCondition(InvoiceClaimQueryDTO queryDTO);

    /**
     * 合同分页查询开票认领数据
     * @param queryDTO
     * @return
     */
    IPage<ContractInvoiceClaimVO> selectContractInvoiceByPage(ContractQueryInfoDTO queryDTO);

    /**
     * 合同查询开票认领List
     * @param queryDTO
     * @return
     */
    List<ContractInvoiceClaimVO> selectContractInvoiceList(ContractQueryInfoDTO queryDTO);

    /**
     * 根据合同编码查询开票认领信息
     * @param queryDTO
     * @return
     */
    List<ContractInvoiceClaimVO> selectContractInvoiceClaimByCondition(ContractQueryInfoDTO queryDTO);
}
