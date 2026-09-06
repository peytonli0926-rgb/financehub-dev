package com.utfinancing.financehub.engine.verification.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.CheckPageQueryDTO;
import com.utfinancing.financehub.engine.verification.model.dto.VerificationCheckDTO;
import com.utfinancing.financehub.engine.verification.model.dto.VerificationDetailsQueryDTO;
import com.utfinancing.financehub.engine.verification.model.dto.VerificationDetailsDTO;
import com.utfinancing.financehub.engine.verification.model.vo.VerificationDetailsVO;
import com.utfinancing.financehub.engine.verification.entity.VerificationDetailsEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2023-10-12
 * @Description : VerificationDetails服务类接口
 * @Modified :
 */
public interface IVerificationDetailsService extends IService<VerificationDetailsEntity> {

    Long saveVerificationDetails(VerificationDetailsDTO dto);

    Long updateVerificationDetails(Long id, VerificationDetailsDTO dto);

    VerificationDetailsDTO getVerificationDetailsDTOById(Long id);

    IPage<VerificationDetailsVO> selectPage(VerificationDetailsQueryDTO queryDTO);

    Boolean updateBatchByVerificationIdList(List<Long> idList);

    Boolean deleteById(Long id);

    List<VerificationDetailsVO> listByCondition(VerificationDetailsQueryDTO queryDTO);

    Long importDetailInfos(MultipartFile file, Long verificationId);

    void calculateFinancialExpenseAmount (VerificationDetailsEntity verificationDetailsEntity);

    IPage<VerificationCheckDTO> checkDataPage(CheckPageQueryDTO queryDTO);

}
