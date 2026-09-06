package com.utfinancing.financehub.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.admin.model.dto.SysInternalRoleUserQueryDTO;
import com.utfinancing.financehub.admin.model.dto.SysInternalRoleUserDTO;
import com.utfinancing.financehub.admin.model.vo.SysInternalRoleUserVO;
import com.utfinancing.financehub.admin.entity.SysInternalRoleUserEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2023-11-17
 * @Description : SysInternalRoleUser服务类接口
 * @Modified :
 */
public interface ISysInternalRoleUserService extends IService<SysInternalRoleUserEntity> {

    Long saveSysInternalRoleUser(SysInternalRoleUserDTO dto);

    Long updateSysInternalRoleUser(Long id, SysInternalRoleUserDTO dto);

    SysInternalRoleUserDTO getSysInternalRoleUserDTOById(Long id);

    IPage<SysInternalRoleUserVO> selectPage(SysInternalRoleUserQueryDTO queryDTO);

    Boolean removeUserById(Long id);

}
