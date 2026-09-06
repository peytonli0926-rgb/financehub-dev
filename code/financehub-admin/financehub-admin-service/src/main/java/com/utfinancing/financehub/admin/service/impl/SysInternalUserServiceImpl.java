package com.utfinancing.financehub.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.utfinancing.financehub.admin.api.model.SysInternalRoleQuery;
import com.utfinancing.financehub.admin.api.model.SysInternalUser;
import com.utfinancing.financehub.admin.entity.SysInternalSubUserEntity;
import com.utfinancing.financehub.admin.model.dto.SysInternalUserDTO;
import com.utfinancing.financehub.admin.model.dto.SysInternalUserQueryDTO;
import com.utfinancing.financehub.admin.model.vo.SysInternalRoleVO;
import com.utfinancing.financehub.admin.model.vo.SysInternalUserVO;
import com.utfinancing.financehub.admin.service.ISysInternalRoleService;
import com.utfinancing.financehub.admin.service.ISysInternalSubUserService;
import com.utfinancing.financehub.common.core.enums.SystemRoleEnums;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.admin.entity.SysInternalUserEntity;
import com.utfinancing.financehub.admin.mapper.SysInternalUserMapper;
import com.utfinancing.financehub.admin.service.ISysInternalUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author : bruyang
 * @Date : Create in 2023-11-17
 * @Description :  SysInternalUser服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class SysInternalUserServiceImpl extends ServiceImpl<SysInternalUserMapper, SysInternalUserEntity> implements ISysInternalUserService {

    private final SysInternalUserMapper sysInternalUserMapper;

    @Resource
    private ISysInternalSubUserService iSysInternalSubUserService;

    @Resource
    private ISysInternalRoleService iSysInternalRoleService;

    @Override
    public Long saveSysInternalUser(SysInternalUserDTO dto) {
        checkData(dto);
        //判断用户是否已经存在
        SysInternalUserEntity entity = this.getOne(Wrappers.<SysInternalUserEntity>lambdaQuery().eq(SysInternalUserEntity::getUserCode,dto.getUserCode()));
        if (null == entity) {
            entity = BeanUtil.copyProperties(dto, SysInternalUserEntity.class);
            this.save(entity);
        }
        return entity.getId();
    }

    @Override
    public Long updateSysInternalUser(Long id, SysInternalUserDTO dto) {
        checkData(dto);
        SysInternalUserEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public SysInternalUserDTO getSysInternalUserDTOById(Long id) {
        SysInternalUserEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, SysInternalUserDTO.class);
    }

    @Override
    public IPage<SysInternalUserVO> selectPage(SysInternalUserQueryDTO queryDTO) {
        QueryWrapper<SysInternalUserEntity> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(queryDTO.getUserName())) {
            queryWrapper.lambda().like(SysInternalUserEntity::getUserName, queryDTO.getUserName());
        }
        if (StringUtils.isNotEmpty(queryDTO.getUserCode())) {
            queryWrapper.lambda().like(SysInternalUserEntity::getUserCode, queryDTO.getUserCode());
        }
        IPage<SysInternalUserEntity> entityIPage = sysInternalUserMapper.selectPage(new Page<SysInternalUserEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        //查询用户对应的角色
        Map<Long, List<SysInternalRoleVO>> userRoleMap = Maps.newHashMap();
        List<SysInternalRoleVO> userRoleList = iSysInternalRoleService.selectUserRoleList(new SysInternalRoleQuery());
        if (CollectionUtils.isNotEmpty(userRoleList)) {
            userRoleMap = userRoleList.stream().collect(Collectors.groupingBy(SysInternalRoleVO::getSysUserId));
        }
        //获取下级用户信息
        Map<Long, List<SysInternalUserVO>> subMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(entityIPage.getRecords())) {
            List<Long> partentIdList = entityIPage.getRecords().stream().map(SysInternalUserEntity::getId).collect(Collectors.toList());
            List<SysInternalSubUserEntity> subUserEntityList = iSysInternalSubUserService.lambdaQuery().in(SysInternalSubUserEntity::getParentUserId,partentIdList).list();
            if (CollectionUtils.isNotEmpty(subUserEntityList)) {
            //按照父级分组
            Map<Long,List<Long>> partentIdSubListIdMap = subUserEntityList.stream().collect(Collectors.groupingBy(SysInternalSubUserEntity::getParentUserId, Collectors.mapping(SysInternalSubUserEntity::getSubUserId, Collectors.toList())));
            //获取下级用户信息
            List<Long> subIdList = Lists.newArrayList();
            for(Map.Entry<Long, List<Long>> entry:partentIdSubListIdMap.entrySet()){
                subIdList.addAll(entry.getValue());
            }
            Map<Long,SysInternalUserEntity> subUserMap =  this.lambdaQuery().in(SysInternalUserEntity::getId, subIdList).list().stream().collect(Collectors.toMap(SysInternalUserEntity::getId, Function.identity(), (k1, k2)->k2));
            for(Map.Entry<Long, List<Long>> entry : partentIdSubListIdMap.entrySet()) {
                List<SysInternalUserEntity> subUserList = Lists.newArrayList();
                entry.getValue().stream().forEach(v -> {
                    if (subUserMap.containsKey(v)) {
                        subUserList.add(subUserMap.get(v));
                    }
                });
                subMap.put(entry.getKey(), ListBeanUtil.copyList(subUserList, SysInternalUserVO.class));
            }
            }
        }
        IPage<SysInternalUserVO> resultPage = ListBeanUtil.copyPage(entityIPage,SysInternalUserVO.class);
        Map<Long, List<SysInternalRoleVO>> finalUserRoleMap = userRoleMap;
        resultPage.getRecords().stream().forEach(v -> {
            if (subMap.containsKey(v.getId())) {
                v.setSubUserList(subMap.get(v.getId()));
            }
            if (finalUserRoleMap.containsKey(v.getId())) {
                v.setRoleList(finalUserRoleMap.get(v.getId()));
            }
        });
        return resultPage;
    }

    @Override
    public List<SysInternalUserVO> listByCondition(SysInternalUserQueryDTO queryDTO) {
        QueryWrapper<SysInternalUserEntity> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(queryDTO.getUserName())) {
            queryWrapper.lambda().like(SysInternalUserEntity::getUserName, queryDTO.getUserName());
        }
        if (StringUtils.isNotEmpty(queryDTO.getUserCode())) {
            queryWrapper.lambda().like(SysInternalUserEntity::getUserCode, queryDTO.getUserCode());
        }
        return ListBeanUtil.copyList(list(queryWrapper), SysInternalUserVO.class);
    }

    @Override
    public List<SysInternalUser> getReviewSubUserByUserCode(String userCode) {
        SysInternalUserDTO sysInternalUserDTO = new SysInternalUserDTO();
        // 查询下级用户不加角色限制
        // sysInternalUserDTO.setRoleCode(SystemRoleEnums.REVIEW_CODE.getCode());
        sysInternalUserDTO.setUserCode(userCode);
        return sysInternalUserMapper.getReviewSubUser(sysInternalUserDTO);
    }

    public void checkData(SysInternalUserDTO dto) {
        if (StringUtils.isEmpty(dto.getUserCode())) {
            throw new ServiceException("用户编码不可以为空");
        }
        if (StringUtils.isEmpty(dto.getUserName())) {
            throw new ServiceException("用户名称不可以为空");
        }
        if (ObjectUtil.isNull(dto.getRoleId())) {
            throw new ServiceException("角色Id不可以为空");
        }
        if (isExistUser(dto)) {
            throw new ServiceException("用户已经存在不可重复添加");
        }
    }

    public Boolean isExistUser(SysInternalUserDTO dto){
        SysInternalUserDTO queryDto = new SysInternalUserDTO();
        queryDto.setUserCode(dto.getUserCode());
        queryDto.setRoleId(dto.getRoleId());
        List<SysInternalUserVO> internalUserVOList = sysInternalUserMapper.selectByCondition(queryDto);
        if (CollectionUtils.isEmpty(internalUserVOList)) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

}

