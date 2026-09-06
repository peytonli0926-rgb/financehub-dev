package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.TaOtherPayableQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.TaOtherPayableDTO;
import com.utfinancing.financehub.engine.finance.model.vo.TaOtherPayableVO;
import com.utfinancing.financehub.engine.finance.entity.TaOtherPayableEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author : wenbin
 * @Date : Create in 2024-05-22
 * @Description : TaOtherPayable服务类接口
 * @Modified :
 */
public interface ITaOtherPayableService extends IService<TaOtherPayableEntity> {

    Long saveTaOtherPayable(TaOtherPayableDTO dto);

    Long updateTaOtherPayable(Long id, TaOtherPayableDTO dto);

    TaOtherPayableDTO getTaOtherPayableDTOById(Long id);

    IPage<TaOtherPayableVO> selectPage(TaOtherPayableQueryDTO queryDTO);

    List<TaOtherPayableVO> selectList(TaOtherPayableQueryDTO queryDTO);

    Boolean generateVoucher(List<Long> ids, String code);

    Boolean importFile(MultipartFile file);

    Boolean submit(List<Long> ids);

    Boolean withdraw(List<Long> ids);

    Boolean delete(List<Long> ids);

    void batchDeleteVoucher(List<Long> ids);

    void updateProcessStatus(CommonApproveDTO approveDTO);
}
