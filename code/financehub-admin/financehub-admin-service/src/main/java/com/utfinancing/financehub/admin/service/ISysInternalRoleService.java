package com.utfinancing.financehub.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.admin.api.model.SysInternalRole;
import com.utfinancing.financehub.admin.api.model.SysInternalRoleQuery;
import com.utfinancing.financehub.admin.entity.SysInternalRoleEntity;
import com.utfinancing.financehub.admin.model.vo.SysInternalRoleUserVO;
import com.utfinancing.financehub.admin.model.vo.SysInternalRoleVO;

import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2023-11-16
 * @Description : SysInternalRole服务类接口
 * @Modified :
 */
public interface ISysInternalRoleService extends IService<SysInternalRoleEntity> {

    Long saveSysInternalRole(SysInternalRole dto);

    Long updateSysInternalRole(Long id, SysInternalRole dto);

    SysInternalRole getSysInternalRoleDTOById(Long id);

    IPage<SysInternalRoleVO> selectPage(SysInternalRoleQuery queryDTO);

    List<SysInternalRoleVO> selectUserRoleList(SysInternalRoleQuery queryDTO);

    Boolean removeByRoleId(Long id);

}
