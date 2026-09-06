package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferVO;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-22
 * @Description : ConvertTransfer服务类接口
 * @Modified :
 */
public interface IConvertTransferService extends IService<ConvertTransferEntity> {

    Boolean updateProcessStatus(CommonApproveDTO approveDTO);

    IPage<ConvertTransferVO> selectPage(ConvertTransferQueryDTO queryDTO);

    List<ConvertTransferExcelVO> selectList(ConvertTransferQueryDTO idList);

    Boolean importFile(MultipartFile file) throws IOException;

    Boolean generateVoucher(List<Long> ids);

    Boolean submit(List<Long> ids);

    Boolean withdraw(List<Long> ids);

    Boolean delete(List<Long> ids);

    List<String> batchList();
}
