package com.utfinancing.financehub.engine.approve.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.approve.model.dto.ApproveQueryDTO;
import com.utfinancing.financehub.engine.approve.model.dto.ApproveDTO;
import com.utfinancing.financehub.engine.approve.model.vo.ApproveVO;
import com.utfinancing.financehub.engine.approve.entity.ApproveEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Map;

/**
 * @Author : bruyang
 * @Date : Create in 2024-01-08
 * @Description : Approve服务类接口
 * @Modified :
 */
public interface IApproveService extends IService<ApproveEntity> {

    Long saveApprove(ApproveDTO dto);

    Long updateApprove(Long id, ApproveDTO dto);

    ApproveDTO getApproveDTOById(Long id);

    IPage<ApproveVO> selectPage(ApproveQueryDTO queryDTO);

    IPage<ApproveVO> todoApproveByPage(ApproveQueryDTO queryDTO);

    IPage<ApproveVO> myDocumentByPage(ApproveQueryDTO queryDTO);

    IPage<ApproveVO> approvedByPage(ApproveQueryDTO queryDTO);

    Boolean pass(List<Long> idList);

    Boolean refuse(List<Long> idList,String remark);

    Boolean returnReviewed(List<Long> idList,String remark);

    public Boolean recall(List<Long> idList,String remark);

    Map<Long,Long> submit(List<ApproveDTO> approveDTOList);

    Boolean withdraw(List<Long> idList);

    Boolean passAll();

    Boolean refuseAll();
}
