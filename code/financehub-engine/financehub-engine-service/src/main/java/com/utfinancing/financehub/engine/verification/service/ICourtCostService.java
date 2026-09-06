package com.utfinancing.financehub.engine.verification.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import com.utfinancing.financehub.engine.verification.model.dto.CourtCostDetailsDTO;
import com.utfinancing.financehub.engine.verification.model.dto.CourtCostDetailsQueryDTO;
import com.utfinancing.financehub.engine.verification.model.dto.CourtCostQueryDTO;
import com.utfinancing.financehub.engine.verification.model.dto.CourtCostDTO;
import com.utfinancing.financehub.engine.verification.model.vo.CourtCostDetailsVO;
import com.utfinancing.financehub.engine.verification.model.vo.CourtCostReportFormVO;
import com.utfinancing.financehub.engine.verification.model.vo.CourtCostVO;
import com.utfinancing.financehub.engine.verification.entity.CourtCostEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2023-10-23
 * @Description : CourtCost服务类接口
 * @Modified :
 */
public interface ICourtCostService extends IService<CourtCostEntity> {

    Long saveCourtCost(CourtCostDTO dto);

    Long updateCourtCost(Long id, CourtCostDTO dto);

    CourtCostDTO getCourtCostDTOById(Long id);

    IPage<CourtCostVO> selectPage(CourtCostQueryDTO queryDTO);

    /**
     * 批量删除接口
     * @param idList 诉讼费id
     * @return
     */
    Boolean deleteByIds(List<Long> idList);

    /**
     * 批量提交
     * @param idList
     * @return
     */
    Boolean submit(List<Long> idList);

    /**
     * 批量撤回
     * @param idList
     * @return
     */
    Boolean withdraw(List<Long> idList);

    /**
     * 上传文件
     * @param file
     * @return
     */
    Boolean importFile(MultipartFile file);

    /**
     * 诉讼费详情
     * @param queryDTO
     * @return
     */
    List<CourtCostDetailsVO> listByCondition(CourtCostDetailsQueryDTO queryDTO);

    /**
     * 详情分页查询接口
     * @param queryDTO
     * @return
     */
    IPage<CourtCostDetailsVO> selectDetailPage(CourtCostDetailsQueryDTO queryDTO);

    /**
     * 批量生成凭证
     * @param idList
     * @param isSubmit
     * @return
     */
    Boolean generateVoucher(List<Long> idList,String isSubmit);


    /**
     * 诉讼费报表分页查询
     * @param queryDTO
     * @return
     */
    IPage<CourtCostReportFormVO> selectReportFormPage(CourtCostDetailsQueryDTO queryDTO);

    /**
     * 报表详情接口
     * @param queryDTO
     * @return
     */
    List<CourtCostReportFormVO> reportFormDetails(CourtCostDetailsQueryDTO queryDTO);

    /**
     * 更新诉讼费状态
     * @param commonApproveDTO
     * @return
     */
    Boolean updateProcessStatus(CommonApproveDTO commonApproveDTO);
}
