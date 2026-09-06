package com.utfinancing.financehub.etl.middle.mapper;

import com.utfinancing.financehub.etl.easold.model.dto.EasVoucherDTO;
import com.utfinancing.financehub.etl.easold.model.dto.QueryEas1VoucherInputDTO;
import com.utfinancing.financehub.etl.financial.entity.KingdeeMiddleVoucherEntity;
import com.utfinancing.financehub.etl.middle.entity.EasVoucherHeadEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.etl.middle.model.dto.MidVoucherEntryDTO;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * EAS凭证头 Mapper 接口
 * </p>
 *
 * @author lixin
 * @since 2023-11-28
 */
public interface EasVoucherHeadMapper extends BaseMapper<EasVoucherHeadEntity> {

    List<EasVoucherHeadEntity> selectMidVoucherHeaderByDate(@Param("periodYear")String periodYear, @Param("periodMonth")String periodMonth, @Param("voucherDate")String voucherDate);

    List<MidVoucherEntryDTO> selectMidVoucherEntryByDate(@Param("periodYear")Integer periodYear,
                                                         @Param("periodMonth")Integer periodMonth,
                                                         @Param("voucherDate")String voucherDate);

    List<Map<String, Object>> selectMidVoucherAsstactByDate(@Param("periodYear")Integer periodYear,
                                            @Param("periodMonth")Integer periodMonth,
                                            @Param("voucherDate")String voucherDate);

    List<Map<String, Object>> selectMidVoucherIncloudAsscateByDate(@Param("periodYear")Integer periodYear,
                                            @Param("periodMonth")Integer periodMonth,
                                            @Param("voucherDate")String voucherDate);



    List<Map<String, Object>> selectMiddleData(@Param("statement")String statement);

    List<EasVoucherDTO> selectMiddleEasVoucher(@Param("periodYear")Integer periodYear,
                                               @Param("periodMonth")Integer periodMonth,
                                               @Param("voucherDate")String voucherDate);

    List<EasVoucherDTO> selectStageMiddleEasVoucher(@Param("periodYear")Integer periodYear,
                                               @Param("periodMonth")Integer periodMonth,
                                               @Param("voucherDate")String voucherDate);


    List<EasVoucherDTO> selectStageZJXTMiddleEasVoucher(@Param("periodYear")Integer periodYear,
                                                    @Param("periodMonth")Integer periodMonth,
                                                    @Param("voucherDate")String voucherDate);

    List<KingdeeMiddleVoucherEntity> selectKingdeeMiddleGaxdVoucher(@Param("year") Integer year, @Param("month") Integer month, @Param("voucherDate")String voucherDate);



}
