package com.utfinancing.financehub.engine.finance.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.entity.PayVatEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.dto.PayVatDTO;
import com.utfinancing.financehub.engine.finance.model.dto.PayVatQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.PayVatTaxRateDTO;
import com.utfinancing.financehub.engine.finance.model.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 应交增值税 Mapper 接口
 * </p>
 *
 * @author bruyang
 * @since 2024-03-20
 */
public interface PayVatMapper extends BaseMapper<PayVatEntity> {

    /**
     * 获取合同数据放入应交增值税表
     *
     * @return
     */
    List<PayVatDTO> payVatGetContract(@Param("taxRateDTO") PayVatTaxRateDTO taxRateDTO,
                                      @Param("queryPeriodCode") Integer queryPeriodCode,
                                      @Param("reportDate") String reportDate);

    /**
     * 分页查询
     *
     * @param page
     * @param queryDTO
     * @return
     */
    IPage<PayVatVO> selectPageByMapper(Page page, @Param("queryDTO") PayVatQueryDTO queryDTO);

    /**
     * 查询
     *
     * @param queryDTO
     * @return
     */
    List<PayVatVO> selectPageByMapper(@Param("queryDTO") PayVatQueryDTO queryDTO);

    /**
     * 根据合同编码和开票主体查询余额
     *
     * @param payVatIdList
     * @return
     */
    List<ContractBalanceVO> selectContractBalanceByContractCodeAndOrgId(@Param("payVatIdList") List<Long> payVatIdList);

    /**
     * 根据合同编码和开票主体查询最新余额
     *
     * @param payVatIdList
     * @return
     */
    List<ContractBalanceLatestVO> selectContractBalanceLatestByContractCodeAndOrgId(@Param("payVatIdList") List<Long> payVatIdList);

    /**
     * 根据合同编码和开票主体查询偿还计划
     *
     * @param payVatIdList
     * @return
     */
    List<RepaymentPlanVO> selectRepaymentPlanByContractCodeAndOrgId(@Param("payVatIdList") List<Long> payVatIdList);

    /**
     * 根据合同编号，签约主体，查询其他业务系统对账数据
     * @param contractCodeList
     * @param orgIdList
     * @return
     */
    List<PayVatCheckDataVO> selectCheckData(@Param("contractCodeList") List<String> contractCodeList, @Param("orgIdList") List<String> orgIdList);
}
