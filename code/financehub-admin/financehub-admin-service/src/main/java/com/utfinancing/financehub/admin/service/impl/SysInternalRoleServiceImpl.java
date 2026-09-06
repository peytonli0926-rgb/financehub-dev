package com.utfinancing.financehub.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.utfinancing.financehub.admin.api.model.SysInternalRole;
import com.utfinancing.financehub.admin.api.model.SysInternalRoleQuery;
import com.utfinancing.financehub.admin.model.vo.SysInternalRoleVO;
import com.utfinancing.financehub.admin.service.ISysInternalRoleOrgService;
import com.utfinancing.financehub.admin.service.ISysInternalRoleUserService;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.admin.entity.SysInternalRoleEntity;
import com.utfinancing.financehub.admin.mapper.SysInternalRoleMapper;
import com.utfinancing.financehub.admin.service.ISysInternalRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2023-11-16
 * @Description :  SysInternalRole服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class SysInternalRoleServiceImpl extends ServiceImpl<SysInternalRoleMapper, SysInternalRoleEntity> implements ISysInternalRoleService {

    private final SysInternalRoleMapper sysInternalRoleMapper;

    @Override
    public Long saveSysInternalRole(SysInternalRole dto) {
        SysInternalRoleEntity entity = BeanUtil.copyProperties(dto, SysInternalRoleEntity.class);
        checkData(dto);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateSysInternalRole(Long id, SysInternalRole dto) {
        checkData(dto);
        SysInternalRoleEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public SysInternalRole getSysInternalRoleDTOById(Long id) {
        SysInternalRoleEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, SysInternalRole.class);
    }

    @Override
    public IPage<SysInternalRoleVO> selectPage(SysInternalRoleQuery queryDTO) {
        LambdaQueryWrapper<SysInternalRoleEntity> queryWrapper = Wrappers.<SysInternalRoleEntity>lambdaQuery();
        //这里注入查询条件
        if (StringUtils.isNotEmpty(queryDTO.getRoleCode())) {
            queryWrapper.like(SysInternalRoleEntity::getRoleCode, queryDTO.getRoleCode());
        }
        if (StringUtils.isNotEmpty(queryDTO.getRoleName())) {
            queryWrapper.like(SysInternalRoleEntity::getRoleName, queryDTO.getRoleName());
        }
        if (StringUtils.isNotEmpty(queryDTO.getStatus())) {
            queryWrapper.like(SysInternalRoleEntity::getStatus, queryDTO.getStatus());
        }
        queryWrapper.orderByDesc(SysInternalRoleEntity::getCreateTime);
        IPage<SysInternalRoleEntity> entityIPage = sysInternalRoleMapper.selectPage(new Page<SysInternalRoleEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, SysInternalRoleVO.class);
    }

    @Override
    public List<SysInternalRoleVO> selectUserRoleList(SysInternalRoleQuery queryDTO) {
        return sysInternalRoleMapper.selectUserRoleList(queryDTO);
    }

    @Override
    public Boolean removeByRoleId(Long id) {
        //判断角色下是否分配了人和数据权限如果存在则不可以删除角色
        Boolean isExistOrg = sysInternalRoleMapper.orgListByRoleId(id).isEmpty();
        Boolean isExistUser = sysInternalRoleMapper.userListByRoleId(id).isEmpty();
        if (!isExistOrg && !isExistUser) {
            throw new ServiceException("角色下已经分配了数据权限和用户不可以删除");
        } else if (!isExistOrg) {
            throw new ServiceException("角色下已经分配了数据权限不可以删除");
        } else if (!isExistUser) {
            throw new ServiceException("角色下已经分配了用户不可以删除");
        }
        return this.removeById(id);
    }

    public void checkData(SysInternalRole dto){
        if (StringUtils.isEmpty(dto.getRoleCode())) {
            throw new ServiceException("角色编码不能为空");
        }
        if (StringUtils.isEmpty(dto.getRoleName())) {
            throw new ServiceException("角色名称不能为空");
        }
        //校验角色编码是否存在
        if (isExistRoleCode(dto.getRoleCode(),dto.getId())) {
            throw new ServiceException("角色编码已经存在不能重复保存");
        }
    }

    public Boolean isExistRoleCode(String roleCode, Long id) {
        return lambdaQuery().eq(SysInternalRoleEntity::getRoleCode, roleCode).notIn(ObjectUtil.isNotNull(id),SysInternalRoleEntity::getId, id).exists();
    }

}

