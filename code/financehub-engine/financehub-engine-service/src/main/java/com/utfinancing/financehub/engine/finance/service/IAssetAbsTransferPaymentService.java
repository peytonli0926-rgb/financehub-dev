package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.AssetAbsRedeemDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.AssetAbsTransferPaymentDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.AssetAbsTransferPaymentQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.AssetAbsTransferPaymentDTO;
import com.utfinancing.financehub.engine.finance.model.vo.*;
import com.utfinancing.financehub.engine.finance.entity.AssetAbsTransferPaymentEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2024-03-20
 * @Description : AssetAbsTransferPayment服务类接口
 * @Modified :
 */
public interface IAssetAbsTransferPaymentService extends IService<AssetAbsTransferPaymentEntity> {

    Long saveAssetAbsTransferPayment(AssetAbsTransferPaymentDTO dto);

    Long updateAssetAbsTransferPayment(Long id, AssetAbsTransferPaymentDTO dto);

    AssetAbsTransferPaymentDTO getAssetAbsTransferPaymentDTOById(Long id);

    IPage<AssetAbsTransferPaymentVO> selectPage(AssetAbsTransferPaymentQueryDTO queryDTO);

    Boolean deleteByIds(List<Long> idList);

    /**
     * 批量撤回
     * @param idList
     * @return
     */
    Boolean withdraw(List<Long> idList);

    Boolean importTemplate(MultipartFile file);

    IPage<AssetAbsTransferPaymentDetailVO> detailPage(AssetAbsTransferPaymentDetailQueryDTO queryDTO);

    /**
     * 批量提交
     * @param idList
     * @return
     */
    Boolean submit(List<Long> idList);

    /**
     * 批量生成凭证
     * @param idList
     * @param isSubmit
     * @return
     */
    Boolean generateVoucher(List<Long> idList,String isSubmit);

    List<AssetAbsTransferPaymentSummarExcelVO> selectPaymentList(List<Long> idList);

    List<AssetAbsTransferPaymentDetailExcelVO> selectPaymentDetailList(List<Long> idList);

    Boolean updateProcessStatus(CommonApproveDTO approveDTO);
}
