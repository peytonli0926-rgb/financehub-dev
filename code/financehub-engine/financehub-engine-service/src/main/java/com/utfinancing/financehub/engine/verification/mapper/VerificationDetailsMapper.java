package com.utfinancing.financehub.engine.verification.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.model.dto.CheckPageQueryDTO;
import com.utfinancing.financehub.engine.verification.entity.VerificationDetailsEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.verification.model.dto.VerificationCheckDTO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author bruyang
 * @since 2023-10-12
 */
public interface VerificationDetailsMapper extends BaseMapper<VerificationDetailsEntity> {

    IPage<VerificationCheckDTO> checkDataPage(Page page, @Param("param") CheckPageQueryDTO queryDTO);

}
