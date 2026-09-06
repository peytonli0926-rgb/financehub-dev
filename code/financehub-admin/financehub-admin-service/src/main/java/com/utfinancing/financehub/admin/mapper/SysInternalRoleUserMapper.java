package com.utfinancing.financehub.admin.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.admin.entity.SysInternalRoleUserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.admin.model.dto.SysInternalRoleUserQueryDTO;
import com.utfinancing.financehub.admin.model.vo.SysInternalRoleUserVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 中台内部角色和用户关联表 Mapper 接口
 * </p>
 *
 * @author bruyang
 * @since 2023-11-17
 */
public interface SysInternalRoleUserMapper extends BaseMapper<SysInternalRoleUserEntity> {

    IPage<SysInternalRoleUserVO> selectPageByCondition(Page page, @Param("condition") SysInternalRoleUserQueryDTO queryDTO);
}
