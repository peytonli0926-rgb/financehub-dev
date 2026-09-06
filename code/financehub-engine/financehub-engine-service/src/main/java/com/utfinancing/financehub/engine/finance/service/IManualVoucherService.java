package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.ManualVoucherQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ManualVoucherDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ManualVoucherVO;
import com.utfinancing.financehub.engine.finance.entity.ManualVoucherEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2024-01-03
 * @Description : ManualVoucher服务类接口
 * @Modified :
 */
public interface IManualVoucherService extends IService<ManualVoucherEntity> {

    Long saveManualVoucher(ManualVoucherDTO dto);

    Long updateManualVoucher(Long id, ManualVoucherDTO dto);

    ManualVoucherDTO getManualVoucherDTOById(Long id);

    IPage<ManualVoucherVO> selectPage(ManualVoucherQueryDTO queryDTO);

}
