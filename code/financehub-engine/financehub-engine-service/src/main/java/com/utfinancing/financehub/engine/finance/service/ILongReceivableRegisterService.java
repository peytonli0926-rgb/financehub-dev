package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.LongReceivableRegisterQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.LongReceivableRegisterDTO;
import com.utfinancing.financehub.engine.finance.model.vo.LongReceivableRegisterVO;
import com.utfinancing.financehub.engine.finance.entity.LongReceivableRegisterEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-11
 * @Description : LongReceivableRegister服务类接口
 * @Modified :
 */
public interface ILongReceivableRegisterService extends IService<LongReceivableRegisterEntity> {

    Long saveLongReceivableRegister(LongReceivableRegisterDTO dto);

    Long updateLongReceivableRegister(Long id, LongReceivableRegisterDTO dto);

    LongReceivableRegisterDTO getLongReceivableRegisterDTOById(Long id);

    IPage<LongReceivableRegisterVO> selectPage(LongReceivableRegisterQueryDTO queryDTO);

    List<LongReceivableRegisterVO> selectList(LongReceivableRegisterQueryDTO queryDTO);

    Boolean importFile(MultipartFile file);

    Boolean generateVoucher(List<Long> ids, String code);

    Boolean submit(List<Long> ids);

    Boolean withdraw(List<Long> ids);

    Boolean delete(List<Long> ids);

    Boolean apportion(List<Long> ids);

    void updateProcessStatus(CommonApproveDTO approveDTO);

    void batchDeleteVoucher(List<Long> ids);
}
