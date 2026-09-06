package com.utfinancing.financehub.engine.claim.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.claim.model.dto.ClaimOrderInvoiceQueryDTO;
import com.utfinancing.financehub.engine.claim.model.dto.ClaimOrderInvoiceDTO;
import com.utfinancing.financehub.engine.claim.model.vo.ClaimOrderInvoiceVO;
import com.utfinancing.financehub.engine.claim.entity.ClaimOrderInvoiceEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-10-23
 * @Description : ClaimOrderInvoice服务类接口
 * @Modified :
 */
public interface IClaimOrderInvoiceService extends IService<ClaimOrderInvoiceEntity> {

    Long saveClaimOrderInvoice(ClaimOrderInvoiceDTO dto);

    Long updateClaimOrderInvoice(Long id, ClaimOrderInvoiceDTO dto);

    ClaimOrderInvoiceDTO getClaimOrderInvoiceDTOById(Long id);

    IPage<ClaimOrderInvoiceVO> selectPage(ClaimOrderInvoiceQueryDTO queryDTO);

}
