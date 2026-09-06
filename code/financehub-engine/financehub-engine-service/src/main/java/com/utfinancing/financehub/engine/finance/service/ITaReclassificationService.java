package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.TaReclassificationQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.TaReclassificationDTO;
import com.utfinancing.financehub.engine.finance.model.vo.TaReclassificationVO;
import com.utfinancing.financehub.engine.finance.entity.TaReclassificationEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author : wenbin
 * @Date : Create in 2024-05-24
 * @Description : TaReclassification服务类接口
 * @Modified :
 */
public interface ITaReclassificationService extends IService<TaReclassificationEntity> {

    Long saveTaReclassification(TaReclassificationDTO dto);

    Long updateTaReclassification(Long id, TaReclassificationDTO dto);

    TaReclassificationDTO getTaReclassificationDTOById(Long id);

    IPage<TaReclassificationVO> selectPage(TaReclassificationQueryDTO queryDTO);

    List<TaReclassificationVO> selectList(TaReclassificationQueryDTO queryDTO);

    Boolean generateVoucher(List<Long> ids, String code);

    Boolean importFile(MultipartFile file);

    void batchDeleteVoucher(List<Long> ids);

    Boolean submit(List<Long> ids);

    Boolean withdraw(List<Long> ids);

    Boolean delete(List<Long> ids);

    Boolean generateVoucherBatch(List<Long> ids, String isSubmit);

    Boolean submitBatch(List<Long> ids);

    void updateProcessStatus(CommonApproveDTO approveDTO);

    Boolean syncData(String businessDate);
}
