package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.NonConfirmCollectionSumVO;
import com.utfinancing.financehub.engine.finance.entity.NonConfirmCollectionSumEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @Author : robjiang
 * @Date : Create in 2024-03-22
 * @Description : NonConfirmCollectionSum服务类接口
 * @Modified :
 */
public interface INonConfirmCollectionSumService extends IService<NonConfirmCollectionSumEntity> {

    Long saveNonConfirmCollectionSum(NonConfirmCollectionSumDTO dto);

    Long updateNonConfirmCollectionSum(Long id, NonConfirmCollectionSumDTO dto);

    /**
     * 根据条件查询未确认收款记录
     */
    public List<NonConfirmCollectionSumEntity>  getNonConfirmCollectionSumDTOByCon(NonConfirmCollectionSumEntity entity);

    NonConfirmCollectionSumDTO getNonConfirmCollectionSumDTOById(Long id);

    IPage<NonConfirmCollectionSumVO> selectPage(NonConfirmCollectionSumQueryDTO queryDTO);

    /**
     * 根据批扣流水号列表查询未确认收款汇总记录
     */
    public Map<String, NonConfirmCollectionSumEntity> queryNonConfirmCollectionSumMap(String... ebankSerialNumberList);

    /**
     * 分页查询汇总表
     */
    public IPage<SelectNonConfirmCollectionSumByPageDTO> selectPageByCon(NonConfirmCollectionSumQueryDTO queryDTO);

    /**
     * 查询汇总表-为分页
     */
    public List<SelectNonConfirmCollectionSumByPageDTO> selectByCon(NonConfirmCollectionSumQueryDTO queryDTO);

    /**
     * 认领数据查询
     */
    public R<ClaimQueryResultDTO> claimQuery(ClaimQueryDTO queryDTO);

    /**
     * 认领确认
     */
    public R claimConfirm(ClaimConfirmDTO claimConfirmDTO, String confirmKey);

    /**
     * 批量认领
     */
    public R<String> batchClaimConfirm(MultipartFile file) throws Exception;

    /**
     * 冲销确认
     */
    public R<String> writeOffConfirm(WriteOffConfirmDTO writeOffConfirmDTO);


    /**
     * 修改网银编号查询
     */
    public R<ModifyEbankNoQueryDTO> modifyEbankNoQuery(ModifyEbankNoQueryDTO params);

    /**
     * 修改网银编号确认
     */
    public R<String> modifyEbankNoConfirm(ModifyEbankNoConfirmDTO params);

    /**
     * 批量修改入账日期
     */
    public void batchModifyIncomeDate(List<ModifyIncomeDateTemplateDownloadExcel> list);

    /**
     * 手工调整余额上传
     */
    public R<String> handsAdjustBalance(List<HandsAdjustBalanceTemplateDownloadExcel> list);


    /**
     * @description:未确认收款-汇总表-上传线下网银-确认上传
     * @author: zhangli.chen
     **/
    public R offlineOnlineBankManualUpload(List<HthxOfflineOnlineBankTemplateExcel> onlineBankTemplateExcelList, String confirmKey);

}
