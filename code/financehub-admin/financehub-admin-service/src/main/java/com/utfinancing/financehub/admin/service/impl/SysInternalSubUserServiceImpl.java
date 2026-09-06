package com.utfinancing.financehub.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.admin.model.dto.SysInternalSubUserQueryDTO;
import com.utfinancing.financehub.admin.model.dto.SysInternalSubUserDTO;
import com.utfinancing.financehub.admin.model.vo.SysInternalSubUserVO;
import com.utfinancing.financehub.admin.entity.SysInternalSubUserEntity;
import com.utfinancing.financehub.admin.mapper.SysInternalSubUserMapper;
import com.utfinancing.financehub.admin.service.ISysInternalSubUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : bruyang
 * @Date : Create in 2023-11-17
 * @Description :  SysInternalSubUser服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class SysInternalSubUserServiceImpl extends ServiceImpl<SysInternalSubUserMapper, SysInternalSubUserEntity> implements ISysInternalSubUserService {

    private final SysInternalSubUserMapper sysInternalSubUserMapper;

    @Override
    public Long saveSysInternalSubUser(SysInternalSubUserDTO dto) {
        SysInternalSubUserEntity entity = BeanUtil.copyProperties(dto, SysInternalSubUserEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateSysInternalSubUser(Long id, SysInternalSubUserDTO dto) {
        SysInternalSubUserEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public SysInternalSubUserDTO getSysInternalSubUserDTOById(Long id) {
        SysInternalSubUserEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, SysInternalSubUserDTO.class);
    }

    @Override
    public IPage<SysInternalSubUserVO> selectPage(SysInternalSubUserQueryDTO queryDTO) {
        Page page = new Page(queryDTO.getPageNum(),queryDTO.getPageSize());
        IPage<SysInternalSubUserVO> entityIPage = sysInternalSubUserMapper.selectPageByCondition(page, queryDTO);
        return entityIPage;
    }

}

