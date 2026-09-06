package com.utfinancing.financehub.admin.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.admin.api.model.SysInternalRoleOrgQuery;
import com.utfinancing.financehub.admin.entity.SysInternalRoleOrgEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.admin.model.vo.SysInternalRoleOrgVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 中台内部角色和签约实体关联表 Mapper 接口
 * </p>
 *
 * @author bruyang
 * @since 2023-11-16
 */
public interface SysInternalRoleOrgMapper extends BaseMapper<SysInternalRoleOrgEntity> {

    IPage<SysInternalRoleOrgVO> selectPageByCondition(Page page, @Param("condition") SysInternalRoleOrgQuery condition);

    List<String> getOrgByUserCode(@Param("userCode") String userCode);
}
