package com.utfinancing.financehub.engine.finance.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.base.MPJBaseMapper;
import com.utfinancing.financehub.engine.finance.entity.VoucherEntity;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.VoucherDetailExportDTO;
import com.utfinancing.financehub.engine.finance.model.vo.VoucherExportVo;
import com.utfinancing.financehub.engine.finance.model.vo.VoucherVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 凭证表 Mapper 接口
 * </p>
 *
 * @author lixin
 * @since 2023-09-01
 */
public interface VoucherMapper extends MPJBaseMapper<VoucherEntity> {

    IPage<VoucherVO> voucherNumberPage(Page page, @Param("param") VoucherQueryDTO queryWrapper);



    IPage<VoucherDetailDTO> voucherManualPage(Page page, @Param("param") VoucherQueryDTO queryDTO);

    List<VoucherDetailExportDTO> selectAllVoucerDetails(@Param("param") VoucherQueryDTO queryDTO);

    /**
     * 查询应付保险费相关凭证金额
     * @param queryDTO
     * @return
     */
    List<VoucherDetailDTO> getInsuranceAmount(@Param("queryDTO") VoucherQueryDTO queryDTO);

    IPage<VoucherDetailDTO> summaryByPage(Page page, @Param("param") VoucherQueryDTO queryDTO);
    List<VoucherExportVo> summaryExport(@Param("param") VoucherQueryDTO queryDTO);

    String getClientCodeByClientName(@Param("clientName") String clientName);

    List<VoucherEntity> selectVoucherEntity(@Param("param") VoucherQueryDTO queryDTO);

    Integer payablesReportQueryCount(@Param("param") PayablesReportQueryInputDTO queryDTO);

    /**
     * 凭证报表查询
     */
    List<PayablesReportQueryOutputDTO> payablesReportQuery(@Param("param") PayablesReportQueryInputDTO queryDTO);

    /**
     * 付款详情查询
     * @param queryDTO
     * @return
     */
    List<PaymentDetailQueryOutputDTO> paymentDetailQuery(@Param("param") PaymentDetailQueryInputDTO queryDTO);

    /**
     * 凭证详情查询
     */
    List<VoucherDetailQueryDTOOutput> voucherDetailQuery(@Param("param") VoucherDetailQueryDTOInput queryDTO);
}
