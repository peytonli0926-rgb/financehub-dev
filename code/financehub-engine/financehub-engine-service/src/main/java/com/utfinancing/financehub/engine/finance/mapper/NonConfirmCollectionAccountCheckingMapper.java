package com.utfinancing.financehub.engine.finance.mapper;

import com.utfinancing.financehub.engine.finance.entity.NonConfirmCollectionAccountCheckingEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.dto.AccountCheckingExportDTO;
import com.utfinancing.financehub.engine.finance.model.dto.SelectNonConfirmAccountCheckingDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author robjiang
 * @since 2024-04-22
 */
public interface NonConfirmCollectionAccountCheckingMapper extends BaseMapper<NonConfirmCollectionAccountCheckingEntity> {

    /**
     * 对账表导出
     */
    public List<AccountCheckingExportDTO> accountingCheckingDataExport(
            @Param("params") SelectNonConfirmAccountCheckingDTO params);

    /**
     * 对账表历史数据导出
     */
    public List<AccountCheckingExportDTO> accountingCheckingHistroyDataExport(
            @Param("params") SelectNonConfirmAccountCheckingDTO params);

    /**
     * 对账表总数查询
     */
    public Integer selectNonConfirmAccountCheckingCount(@Param("params") SelectNonConfirmAccountCheckingDTO params);

    /**
     * 对账表分页查询
     */
    public List<NonConfirmCollectionAccountCheckingEntity> selectNonConfirmAccountChecking(
            @Param("params") SelectNonConfirmAccountCheckingDTO params);

    /**
     * 历史对账表总数查询
     */
    public Integer selectLastAccountCheckingCount(@Param("params") SelectNonConfirmAccountCheckingDTO params);

    /**
     * 历史对账表分页查询
     */
    public List<NonConfirmCollectionAccountCheckingEntity> selectLastAccountChecking(
            @Param("params") SelectNonConfirmAccountCheckingDTO params);

    /**
     * 通过业务主键取得对账表信息
     */
    public List<NonConfirmCollectionAccountCheckingEntity> selectAccountCheckingByBusKey(@Param("businessKeyList") List<String> businessKeyList);

    /**
     * 批量上传更新
     */
    public int batchModifyUploadSql();
}
