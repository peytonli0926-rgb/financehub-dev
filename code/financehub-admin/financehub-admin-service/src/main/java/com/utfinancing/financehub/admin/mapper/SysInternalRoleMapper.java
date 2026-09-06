package com.utfinancing.financehub.admin.mapper;

import com.utfinancing.financehub.admin.api.model.SysInternalRoleQuery;
import com.utfinancing.financehub.admin.entity.SysInternalRoleEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.admin.model.vo.SysInternalRoleVO;
import org.apache.ibatis.annotations.Param;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

/**
 * <p>
 * 中台内部角色表 Mapper 接口
 * </p>
 *
 * @author bruyang
 * @since 2023-11-16
 */
public interface SysInternalRoleMapper extends BaseMapper<SysInternalRoleEntity> {

    List<SysInternalRoleVO> selectUserRoleList(@Param("condition") SysInternalRoleQuery queryDTO);

    List<String> orgListByRoleId(@Param("id") Long id);

    List<String> userListByRoleId(@Param("id") Long id);
}
