package com.utfinancing.financehub.admin.service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.admin.api.model.SysInternalRoleOrg;
import com.utfinancing.financehub.admin.api.model.SysInternalRoleOrgQuery;
import com.utfinancing.financehub.admin.entity.SysInternalRoleOrgEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.admin.model.vo.SysInternalRoleOrgVO;

import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2023-11-16
 * @Description : SysInternalRoleOrg服务类接口
 * @Modified :
 */
public interface ISysInternalRoleOrgService extends IService<SysInternalRoleOrgEntity> {

    Long saveSysInternalRoleOrg(SysInternalRoleOrg dto);

    Long updateSysInternalRoleOrg(Long id, SysInternalRoleOrg dto);

    SysInternalRoleOrg getSysInternalRoleOrgDTOById(Long id);

    IPage<SysInternalRoleOrgVO> selectPage(SysInternalRoleOrgQuery queryDTO);

    List<String> getOrgByUserCode (String userCode);

}
