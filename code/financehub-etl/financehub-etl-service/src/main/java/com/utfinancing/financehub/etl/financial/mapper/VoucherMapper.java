package com.utfinancing.financehub.etl.financial.mapper;

import com.utfinancing.financehub.etl.easold.model.dto.EasVoucherDTO;
import com.utfinancing.financehub.etl.financial.entity.VoucherEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.etl.middle.model.dto.MidVoucherEntryDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lixin
 * @since 2023-11-16
 */
public interface VoucherMapper extends BaseMapper<VoucherEntity> {

    List<MidVoucherEntryDTO> selectFinhubVoucherEntryByDate(@Param("periodCode")Integer periodCode, @Param("voucherDate")String voucherDate);

    List<Map<String, Object>> selectFinhubData(@Param("statement")String statement);

    List<EasVoucherDTO> selectFinhubVoucherData(@Param("periodCode")Integer periodCode, @Param("voucherDate")String voucherDate);

    List<EasVoucherDTO> selectFinhubInterVoucherData(@Param("periodCode")Integer periodCode, @Param("voucherDate")String voucherDate);

    int updateVoucherNumForWYLSFK(@Param("periodCode")Integer periodCode, @Param("voucherDate")String voucherDate,@Param("isEntryFlag") boolean isEntryFlag);

    List<EasVoucherDTO> selectNoSummaryFinhubVoucherData(@Param("periodCode")Integer periodCode, @Param("voucherDate")String voucherDate,@Param("isEntryFlag") boolean isEntryFlag);

    List<EasVoucherDTO> selectNoSummaryFinhubVoucherDataForWYLSFK(@Param("periodCode")Integer periodCode, @Param("voucherDate")String voucherDate,@Param("isEntryFlag") boolean isEntryFlag);

    List<EasVoucherDTO> selectNoSummaryFinhubInterVoucherData(@Param("periodCode")Integer periodCode, @Param("voucherDate")String voucherDate,@Param("isEntryFlag") boolean isEntryFlag);


}

