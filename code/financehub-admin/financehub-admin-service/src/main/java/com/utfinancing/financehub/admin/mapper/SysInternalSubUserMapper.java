package com.utfinancing.financehub.admin.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.admin.entity.SysInternalSubUserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.admin.model.dto.SysInternalSubUserQueryDTO;
import com.utfinancing.financehub.admin.model.vo.SysInternalSubUserVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 中台内部用户上级和下级关联表 Mapper 接口
 * </p>
 *
 * @author bruyang
 * @since 2023-11-17
 */
public interface SysInternalSubUserMapper extends BaseMapper<SysInternalSubUserEntity> {

    IPage<SysInternalSubUserVO> selectPageByCondition(Page page, @Param("condition") SysInternalSubUserQueryDTO queryDTO);
}
