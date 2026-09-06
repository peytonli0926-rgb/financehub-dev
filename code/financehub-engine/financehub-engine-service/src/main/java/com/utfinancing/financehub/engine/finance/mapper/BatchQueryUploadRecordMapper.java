package com.utfinancing.financehub.engine.finance.mapper;

import com.utfinancing.financehub.engine.finance.entity.BatchQueryUploadRecordEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.dto.BatchQueryUploadQueryDTO;
import com.utfinancing.financehub.engine.hthx.base.MyBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 批量查询上传记录表 Mapper 接口
 * </p>
 *
 * @author jnc
 * @since 2024-05-14
 */
@Mapper
public interface BatchQueryUploadRecordMapper extends MyBaseMapper<BatchQueryUploadRecordEntity> {

    List<Map<String, String>> selectBatchQueryContractBalanceInfo(@Param("param") BatchQueryUploadQueryDTO queryDTO);

    Map<String, Long> selectBatchQueryContractBalanceInfoSize(@Param("param") BatchQueryUploadQueryDTO queryDTO);

    List<Map<String, String>> selectBatchQueryAssistBalanceInfo(@Param("param") BatchQueryUploadQueryDTO queryDTO);

    List<Map<String, String>> selectBatchQueryUploadList(@Param("param") BatchQueryUploadQueryDTO queryDTO);
}
