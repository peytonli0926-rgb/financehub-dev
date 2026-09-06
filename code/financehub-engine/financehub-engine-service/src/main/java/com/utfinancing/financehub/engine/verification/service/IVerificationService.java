package com.utfinancing.financehub.engine.verification.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.CheckPageQueryDTO;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import com.utfinancing.financehub.engine.model.dto.CourtCostVerificationDTO;
import com.utfinancing.financehub.engine.verification.model.dto.*;
import com.utfinancing.financehub.engine.verification.model.vo.VerificationDetailsVO;
import com.utfinancing.financehub.engine.verification.model.vo.VerificationPaybackDetailsVO;
import com.utfinancing.financehub.engine.verification.model.vo.VerificationPaybackVO;
import com.utfinancing.financehub.engine.verification.model.vo.VerificationVO;
import com.utfinancing.financehub.engine.verification.entity.VerificationEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @Author : bruyang
 * @Date : Create in 2023-10-11
 * @Description : Verification服务类接口
 * @Modified :
 */
public interface IVerificationService extends IService<VerificationEntity> {

    Long saveVerification(VerificationDTO dto);

    Long updateVerification(Long id, VerificationDTO dto);

    VerificationDTO getVerificationDTOById(Long id);

    IPage<VerificationVO> selectPage(VerificationQueryDTO queryDTO);

    Boolean deleteByIds(List<Long> ids);

    Boolean importTemplate(MultipartFile file);

    Boolean deleteDetailByDetailId(Long detailId);

    IPage<VerificationDetailsVO> selectDetailPage(VerificationDetailsQueryDTO queryDTO);

    List<VerificationDetailsVO> listByCondition(VerificationDetailsQueryDTO queryDTO);

    Long importDetailInfos(MultipartFile file, Long verificationId);

    Boolean submit(List<Long> idList);

    Boolean withdraw(List<Long> idList);

    Boolean generateVoucher(List<Long> idList);

    /**
     * 核销回款分页接口
     * @param queryDTO 参数
     * @return
     */
    IPage<VerificationPaybackVO> selectPaybackPage(VerificationPaybackQueryDTO queryDTO);

    /**
     * 核销回款详情分页接口
     * @param queryDTO 参数
     * @return
     */
    IPage<VerificationPaybackDetailsVO> selectPaybackDetailsPage(VerificationPaybackQueryDTO queryDTO);

    /**
     * 批量查询核销回款详情信息
     * @param queryDTOList
     * @return
     */
    List<VerificationPaybackDetailsVO> listPaybackByCondition(List<VerificationPaybackQueryDTO> queryDTOList);

    /**
     * 核销回款校验
     * @return
     */
    VerificationPaybackDetailsVO verification(VerificationPaybackQueryDTO queryDTO);

    /**
     * 更新核销状态
     * @param commonApproveDTO
     * @return
     */
    Boolean updateProcessStatus(CommonApproveDTO commonApproveDTO);

    /**
     * 更新合同表转回拨备金额
     * @param dtoList
     * @return
     */
    Boolean updateContractAmount(List<CourtCostVerificationDTO> dtoList);

    IPage<VerificationCheckDTO> checkPage(CheckPageQueryDTO queryDTO);

    //根据合同编码+签约主体获取最大的记账日期
    LocalDateTime getMaxAccountDate(String contractCode,String orgId);

    //根据合同编码+签约主体获取最大拨备转回金额
    BigDecimal getMaxDepreciationReserves(String contractCode, String orgId);

    Map<String, String> exportSummary(VerificationPaybackQueryDTO queryDTO);
}
