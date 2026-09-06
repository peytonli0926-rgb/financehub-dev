package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.InternalTransferGenerateDTO;
import com.utfinancing.financehub.engine.finance.model.dto.InternalTransferQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.InternalTransferDTO;
import com.utfinancing.financehub.engine.finance.model.vo.InternalTransferVO;
import com.utfinancing.financehub.engine.finance.entity.InternalTransferEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-17
 * @Description : InternalTransfer服务类接口
 * @Modified :
 */
public interface IInternalTransferService extends IService<InternalTransferEntity> {

    Long saveInternalTransfer(InternalTransferDTO dto);

    Long updateInternalTransfer(Long id, InternalTransferDTO dto);

    InternalTransferDTO getInternalTransferDTOById(Long id);

    IPage<InternalTransferVO> selectPage(InternalTransferQueryDTO queryDTO);

    List<InternalTransferVO> selectList(InternalTransferQueryDTO queryDTO);

    Boolean importFile(MultipartFile file);

    Boolean generateVoucher(List<Long> ids, String code);

    Boolean submit(List<Long> ids);

    Boolean withdraw(List<Long> ids);

    Boolean delete(List<Long> ids);

    Boolean generatePayment(InternalTransferGenerateDTO queryDTO);

    void updateProcessStatus(CommonApproveDTO approveDTO);
}
