package com.utfinancing.financehub.engine.approve.mapper;

import com.utfinancing.financehub.engine.approve.entity.ApproveEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 审批表 Mapper 接口
 * </p>
 *
 * @author bruyang
 * @since 2024-01-08
 */
public interface ApproveMapper extends BaseMapper<ApproveEntity> {

    int physicalDeleteByDocuments(@Param("documentIds") List<Long> documentIds,
                                  @Param("documentType") String documentType);
}
