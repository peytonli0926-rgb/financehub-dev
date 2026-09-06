package com.utfinancing.financehub.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.admin.model.dto.SysInternalUserDTO;
import com.utfinancing.financehub.admin.service.ISysInternalUserService;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.admin.model.dto.SysInternalRoleUserQueryDTO;
import com.utfinancing.financehub.admin.model.dto.SysInternalRoleUserDTO;
import com.utfinancing.financehub.admin.model.vo.SysInternalRoleUserVO;
import com.utfinancing.financehub.admin.entity.SysInternalRoleUserEntity;
import com.utfinancing.financehub.admin.mapper.SysInternalRoleUserMapper;
import com.utfinancing.financehub.admin.service.ISysInternalRoleUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.util.List;
/**
 * @Author : bruyang
 * @Date : Create in 2023-11-17
 * @Description :  SysInternalRoleUser服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class SysInternalRoleUserServiceImpl extends ServiceImpl<SysInternalRoleUserMapper, SysInternalRoleUserEntity> implements ISysInternalRoleUserService {

    private final SysInternalRoleUserMapper sysInternalRoleUserMapper;

    @Resource
    private ISysInternalUserService iSysInternalUserService;

    @Override
    public Long saveSysInternalRoleUser(SysInternalRoleUserDTO dto) {
        SysInternalUserDTO userDTO = BeanUtil.copyProperties(dto,SysInternalUserDTO.class);
        //保存用户信息
        Long userId = iSysInternalUserService.saveSysInternalUser(userDTO);
        SysInternalRoleUserEntity entity = BeanUtil.copyProperties(dto, SysInternalRoleUserEntity.class);
        entity.setSysUserId(userId);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateSysInternalRoleUser(Long id, SysInternalRoleUserDTO dto) {
        SysInternalRoleUserEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public SysInternalRoleUserDTO getSysInternalRoleUserDTOById(Long id) {
        SysInternalRoleUserEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, SysInternalRoleUserDTO.class);
    }

    @Override
    public IPage<SysInternalRoleUserVO> selectPage(SysInternalRoleUserQueryDTO queryDTO) {
        Page page = new Page(queryDTO.getPageNum(),queryDTO.getPageSize());
        return sysInternalRoleUserMapper.selectPageByCondition(page, queryDTO);
    }

    @Override
    public Boolean removeUserById(Long id) {
        //删除关联表
        this.removeById(id);
        return this.removeById(id);
    }

}

