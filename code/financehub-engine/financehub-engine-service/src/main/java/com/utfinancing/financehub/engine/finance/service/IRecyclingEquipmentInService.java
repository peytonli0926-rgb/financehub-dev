package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.RecyclingEquipmentInQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.RecyclingEquipmentInDTO;
import com.utfinancing.financehub.engine.finance.model.vo.RecyclingEquipmentInCheckVO;
import com.utfinancing.financehub.engine.finance.model.vo.RecyclingEquipmentInVO;
import com.utfinancing.financehub.engine.finance.entity.RecyclingEquipmentInEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author : jnc
 * @Date : Create in 2024-03-07
 * @Description : RecyclingEquipmentIn服务类接口
 * @Modified :
 */
public interface IRecyclingEquipmentInService extends IService<RecyclingEquipmentInEntity> {

    Long saveRecyclingEquipmentIn(RecyclingEquipmentInDTO dto);

    Long updateRecyclingEquipmentIn(Long id, RecyclingEquipmentInDTO dto);

    RecyclingEquipmentInDTO getRecyclingEquipmentInDTOById(Long id);

    IPage<RecyclingEquipmentInVO> selectPage(RecyclingEquipmentInQueryDTO queryDTO);

    Boolean importTemplate(MultipartFile file);

    Boolean submit(List<Long> ids);

    Boolean generateVoucher(List<Long> ids, String isSubmit);

    Boolean withdraw(List<Long> ids);

    List<RecyclingEquipmentInVO> listByCondition(RecyclingEquipmentInQueryDTO queryDTO);

    Boolean removeSummaryAndDetail(List<Long> ids);

    List<RecyclingEquipmentInCheckVO> checkRemainBalance(Long id);

    void updateProcessStatus(CommonApproveDTO approveDTO);
}
