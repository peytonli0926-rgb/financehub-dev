package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.NonConfirmCollectionAccountCheckingVO;
import com.utfinancing.financehub.engine.finance.entity.NonConfirmCollectionAccountCheckingEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : robjiang
 * @Date : Create in 2024-04-22
 * @Description : NonConfirmCollectionAccountChecking服务类接口
 * @Modified :
 */
public interface INonConfirmCollectionAccountCheckingService extends IService<NonConfirmCollectionAccountCheckingEntity> {

    Long saveNonConfirmCollectionAccountChecking(NonConfirmCollectionAccountCheckingDTO dto);

    Long updateNonConfirmCollectionAccountChecking(Long id, NonConfirmCollectionAccountCheckingDTO dto);

    NonConfirmCollectionAccountCheckingDTO getNonConfirmCollectionAccountCheckingDTOById(Long id);

    IPage<NonConfirmCollectionAccountCheckingVO> selectPage(NonConfirmCollectionAccountCheckingQueryDTO queryDTO);

    /**
     * 查询对账导出数据
     * @return
     */
    public List<AccountCheckingExportDTO> queryExportData(QueryNonConfirmAccountCheckingInputDTO params);

    /**
     * 分页查询未确认收款的对账表数据
     */
    public IPage<QueryNonConfirmAccountCheckingOutputDTO> queryNonConfirmAccountChecking(
            QueryNonConfirmAccountCheckingInputDTO params);


    /**
     * 确认对账
     */
    public String confirmAccountingChecking(ConfirmAccountingCheckingDTO params);


    /**
     * 批量修改上传
     */
    public String batchModifyUpload(List<BatchModifyTemplateDTO> list);


    /**
     * 回租结果上传
     */
    public String nonLeaseResultUpload(List<UploadNonLeaseResultTemplateDTO> list, String isWaring);
}
