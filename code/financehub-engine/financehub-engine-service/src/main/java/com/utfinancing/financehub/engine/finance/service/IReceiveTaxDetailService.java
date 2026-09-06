package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.ReceiveTaxDetailDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ReceiveTaxDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ReceiveTaxDetailVO;
import com.utfinancing.financehub.engine.finance.entity.ReceiveTaxDetailEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2024-03-20
 * @Description : ReceiveTaxDetail服务类接口
 * @Modified :
 */
public interface IReceiveTaxDetailService extends IService<ReceiveTaxDetailEntity> {

    Long saveReceiveTaxDetail(ReceiveTaxDetailDTO dto);

    Long updateReceiveTaxDetail(Long id, ReceiveTaxDetailDTO dto);

    ReceiveTaxDetailDTO getReceiveTaxDetailDTOById(Long id);

    IPage<ReceiveTaxDetailVO> selectPage(ReceiveTaxDetailQueryDTO queryDTO);

    /**
     * 上传发票明细
     * @param file
     * @return
     */
    Boolean importFile(MultipartFile file);
}
