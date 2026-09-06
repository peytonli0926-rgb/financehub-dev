package com.utfinancing.financehub.engine.finance.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.entity.NonConfirmCollectionSumEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 未确认收款汇总表 Mapper 接口
 * </p>
 *
 * @author robjiang
 * @since 2024-03-22
 */
public interface NonConfirmCollectionSumMapper extends BaseMapper<NonConfirmCollectionSumEntity> {


    /**
     * 未确认收款汇总信息查询
     */
    public Page<SelectNonConfirmCollectionSumByPageDTO> selectNonConfirmCollectionSumByPage(IPage pageQuery,
                        @Param("params") NonConfirmCollectionSumQueryDTO params);

    /**
     * 未确认收款汇总-数据总计
     */
    public Integer selectNonConfirmCollectionSumCount(@Param("params") NonConfirmCollectionSumQueryDTO params);

    /**
     * 未确认收款汇总信息查询
     */
    public List<SelectNonConfirmCollectionSumByPageDTO> selectNonConfirmCollectionSumByPage(
            @Param("params") NonConfirmCollectionSumQueryDTO params);

    /**
     * 修改网银编号查询
     */
    public List<BusinessClaimRepaymentRecordDTO> selectClaimRecordForModifyEbank(
            @Param("params") SelectClaimRecordForModifyEbankDTO params);

    /**
     * 未确认收款下载表数据查询
     */
    public List<SelectDownloadDataDTO> selectDownloadData(@Param("params") QueryThirdDetailPageDataDTO params);
}
