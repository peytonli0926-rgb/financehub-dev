package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.NonConfirmCollectionSecondDetailVO;
import com.utfinancing.financehub.engine.finance.entity.NonConfirmCollectionSecondDetailEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;

import java.util.Date;
import java.util.List;

/**
 * @Author : robjiang
 * @Date : Create in 2024-03-22
 * @Description : NonConfirmCollectionSecondDetail服务类接口
 * @Modified :
 */
public interface INonConfirmCollectionSecondDetailService extends IService<NonConfirmCollectionSecondDetailEntity> {

    Long saveNonConfirmCollectionSecondDetail(NonConfirmCollectionSecondDetailDTO dto);

    Long updateNonConfirmCollectionSecondDetail(Long id, NonConfirmCollectionSecondDetailDTO dto);

    NonConfirmCollectionSecondDetailDTO getNonConfirmCollectionSecondDetailDTOById(Long id);

    IPage<NonConfirmCollectionSecondDetailVO> selectPage(NonConfirmCollectionSecondDetailQueryDTO queryDTO);

    /**
     * 根据条件查询未确认收款明细记录
     */
    public List<NonConfirmCollectionSecondDetailEntity>  getNonConfirmCollectionDetailByCon(
            Long sumId, Date incomeDateOld);

    /**
     * 审核通过后处理
     */
    public void auditPass(CommonApproveDTO approveDTO);

    /**
     * 手工调整余额审核通过
     */
    public void auditPassForSGTZYE(CommonApproveDTO approveDTO);

    /**
     * 审核拒绝后处理
     */
    public void auditFailed(CommonApproveDTO approveDTO);

    /**
     * 审核拒绝后处理-手工调整余额审核
     */
    public void auditFailedForSGTZYE(CommonApproveDTO approveDTO);

    /**
     * 查询手工处理数据
     */
    public List<ManualProcessListDTO> queryManualProcess(QueryManualProcessDTO params);


    /**
     * 详情数据查询
     */
    public List<QueryDetailListDataDTO> queryDetailPageData(QueryDetailDataDTO params);


    /**
     * 第三层详情页面数据查询
     */
    public List<QueryThirdDetailPageListDataDTO> queryThirdDetailPageData(QueryThirdDetailPageDataDTO params);


    /**
     * 第三层详情页面数据查询
     */
    public IPage<QueryThirdDetailPageListDataDTO> queryThirdDetailDataByPage(QueryThirdDetailPageDataDTO params);


    /**
     * 撤回
     */
    public R<String> nonConfirmCollectionDetailRecall(NonConfirmCollectionDetailRecallInputDTO params);



    /**
     * 删除
     */
    public R<String> nonConfirmCollectionDetailDelete(NonConfirmCollectionDetailDeleteInputDTO params);

    /**
     * 未确认收款下载表数据查询
     */
    public List<SelectDownloadDataDTO> queryDownloadData(QueryThirdDetailPageDataDTO params);
}
