package com.utfinancing.financehub.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.utfinancing.financehub.admin.api.model.SysInternalRoleOrg;
import com.utfinancing.financehub.admin.api.model.SysInternalRoleOrgQuery;
import com.utfinancing.financehub.admin.model.vo.SysInternalRoleOrgVO;
import com.utfinancing.financehub.admin.service.ISysInternalRoleService;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.admin.entity.SysInternalRoleOrgEntity;
import com.utfinancing.financehub.admin.mapper.SysInternalRoleOrgMapper;
import com.utfinancing.financehub.admin.service.ISysInternalRoleOrgService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2023-11-16
 * @Description :  SysInternalRoleOrg服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class SysInternalRoleOrgServiceImpl extends ServiceImpl<SysInternalRoleOrgMapper, SysInternalRoleOrgEntity> implements ISysInternalRoleOrgService {

    private final SysInternalRoleOrgMapper sysInternalRoleOrgMapper;

    @Override
    public Long saveSysInternalRoleOrg(SysInternalRoleOrg dto) {
        checkData(dto);
        SysInternalRoleOrgEntity entity = BeanUtil.copyProperties(dto, SysInternalRoleOrgEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateSysInternalRoleOrg(Long id, SysInternalRoleOrg dto) {
        dto.setId(id);
        checkData(dto);
        SysInternalRoleOrgEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public SysInternalRoleOrg getSysInternalRoleOrgDTOById(Long id) {
        SysInternalRoleOrgEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, SysInternalRoleOrg.class);
    }

    @Override
    public IPage<SysInternalRoleOrgVO> selectPage(SysInternalRoleOrgQuery queryDTO) {
        Page page = new Page(queryDTO.getPageNum(),queryDTO.getPageSize());
        return sysInternalRoleOrgMapper.selectPageByCondition(page, queryDTO);
    }

    @Override
    public List<String> getOrgByUserCode(String userCode) {
        return sysInternalRoleOrgMapper.getOrgByUserCode(userCode);
    }

    public void checkData(SysInternalRoleOrg dto){
        if (StringUtils.isEmpty(dto.getOrgId())) {
            throw new ServiceException("签约实体不能为空");
        }
        if (StringUtils.isEmpty(dto.getOrgName())) {
            throw new ServiceException("签约实体名称不能为空");
        }
        if (ObjectUtil.isNull(dto.getRoleId())) {
            throw new ServiceException("关联角色ID不可以为空");
        }
        if (isExistOrgId(dto.getRoleId(),dto.getOrgId())) {
            throw new ServiceException("签约实体已经存在不可重复保存");
        }
    }

    public Boolean isExistOrgId(Long roleId,String orgId){
        return lambdaQuery().eq(SysInternalRoleOrgEntity::getRoleId,roleId).eq(SysInternalRoleOrgEntity::getOrgId,orgId).exists();
    }

}

