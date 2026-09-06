package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.RecyclingEquipmentOutDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.RecyclingEquipmentOutDetailDTO;
import com.utfinancing.financehub.engine.finance.model.vo.RecyclingEquipmentInVO;
import com.utfinancing.financehub.engine.finance.model.vo.RecyclingEquipmentOutDetailVO;
import com.utfinancing.financehub.engine.finance.entity.RecyclingEquipmentOutDetailEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author : jnc
 * @Date : Create in 2024-03-07
 * @Description : RecyclingEquipmentOutDetail服务类接口
 * @Modified :
 */
public interface IRecyclingEquipmentOutDetailService extends IService<RecyclingEquipmentOutDetailEntity> {

    Long saveRecyclingEquipmentOutDetail(RecyclingEquipmentOutDetailDTO dto);

    Long updateRecyclingEquipmentOutDetail(Long id, RecyclingEquipmentOutDetailDTO dto);

    RecyclingEquipmentOutDetailDTO getRecyclingEquipmentOutDetailDTOById(Long id);

    IPage<RecyclingEquipmentOutDetailVO> selectPage(RecyclingEquipmentOutDetailQueryDTO queryDTO);

    Boolean importTemplate(MultipartFile file);

    Boolean submit(List<Long> ids);

    Boolean generateVoucher(List<Long> ids, String code);

    Boolean withdraw(List<Long> ids);

    List<RecyclingEquipmentOutDetailVO> listByCondition(RecyclingEquipmentOutDetailQueryDTO queryDTO);

    Boolean removeDetail(List<Long> ids);

    void updateProcessStatus(CommonApproveDTO approveDTO);
}
