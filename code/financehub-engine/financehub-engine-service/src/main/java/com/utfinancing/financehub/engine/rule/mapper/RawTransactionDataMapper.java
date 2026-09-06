package com.utfinancing.financehub.engine.rule.mapper;

import com.alibaba.fastjson2.JSONObject;
import com.utfinancing.financehub.engine.rule.entity.RawTransactionDataEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.rule.model.vo.RawTransactionDataDuplicateVo;
import com.utfinancing.financehub.engine.rule.model.vo.RawTransactionDataQueryVO;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.session.ResultHandler;

import java.util.List;

/**
 * <p>
 * 业务系统原始交易数据 Mapper 接口
 * </p>
 *
 * @author lixin
 * @since 2023-10-16
 */
public interface RawTransactionDataMapper extends BaseMapper<RawTransactionDataEntity> {

    @Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = 10000)
    void fetchTodoRawTransactionData(ResultHandler<RawTransactionDataQueryVO> handler, @Param("systemCode") String systemCode);

    JSONObject getTodoRawTransactionDataInfo(@Param("systemCode") String systemCode);

    List<RawTransactionDataDuplicateVo> getDuplicateData(@Param("systemCode") String systemCode, @Param("sceneCodes") List<String> ignoreRepeatDataSceneCode);
}
