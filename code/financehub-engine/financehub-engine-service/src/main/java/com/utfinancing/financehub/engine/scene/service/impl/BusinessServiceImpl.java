package com.utfinancing.financehub.engine.scene.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.utfinancing.financehub.common.redis.service.RedisService;
import com.utfinancing.financehub.engine.constants.RedisConstant;
import com.utfinancing.financehub.engine.scene.entity.BusinessEntity;
import com.utfinancing.financehub.engine.scene.mapper.BusinessMapper;
import com.utfinancing.financehub.engine.scene.model.dto.BusinessDTO;
import com.utfinancing.financehub.engine.scene.model.dto.BusinessQueryDTO;
import com.utfinancing.financehub.engine.scene.model.dto.BusinessSaveDTO;
import com.utfinancing.financehub.engine.scene.model.vo.BusinessVO;
import com.utfinancing.financehub.engine.scene.service.IBusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author : hzhao
 * @Date : Create in 2023-08-30
 * @Description :  Business服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class BusinessServiceImpl extends ServiceImpl<BusinessMapper, BusinessEntity> implements IBusinessService {

    private final BusinessMapper businessMapper;
    private final RedisService redisService;

    @Override
    public Long saveBusiness(BusinessSaveDTO dto) {
        BusinessEntity entity = BeanUtil.copyProperties(dto, BusinessEntity.class);
        this.save(entity);
        redisService.deleteObject(RedisConstant.V_ALL_BUSINESS);
        return entity.getId();
    }

    @Override
    public Long updateBusiness(Long id, BusinessDTO dto) {
        BusinessEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        redisService.deleteObject(String.format(RedisConstant.V_BUSINESS_CODE_KEY,dto.getBusinessCode()));
        redisService.deleteObject(RedisConstant.V_ALL_BUSINESS);
        return id;
    }

    @Override
    public BusinessDTO getBusinessDTOById(Long id) {
        BusinessEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, BusinessDTO.class);
    }

    @Override
    public IPage<BusinessVO> selectPage(BusinessQueryDTO queryDTO) {
        LambdaQueryWrapper<BusinessEntity> queryWrapper = Wrappers.<BusinessEntity>lambdaQuery();
        //这里注入查询条件
        IPage<BusinessEntity> entityIPage = businessMapper.selectPage(new Page<BusinessEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, BusinessVO.class);
    }

    @Override
    public List<BusinessVO> selectAll(BusinessQueryDTO queryDTO) {
        LambdaQueryWrapper<BusinessEntity> queryWrapper = Wrappers.<BusinessEntity>lambdaQuery();
        //这里注入查询条件
        List<BusinessEntity> businessEntities = getBaseMapper().selectList(queryWrapper);
        return ListBeanUtil.copyList(businessEntities, BusinessVO.class);
    }

    @Override
    public void addSceneCountByBusinessId(Long id) {
        BusinessEntity entity = this.getById(id);
        LambdaUpdateWrapper<BusinessEntity> updateChainWrapper = new LambdaUpdateWrapper<>();
        updateChainWrapper
                .eq(BusinessEntity::getId, id)
                .set(BusinessEntity::getSceneCount, entity.getSceneCount() + 1);
        this.update(updateChainWrapper);
    }

    @Override
    public void substractSceneCountByBusinessId(Long id) {
        BusinessEntity entity = this.getById(id);
        LambdaUpdateWrapper<BusinessEntity> updateChainWrapper = new LambdaUpdateWrapper<>();
        updateChainWrapper
                .eq(BusinessEntity::getId, id)
                .set(BusinessEntity::getSceneCount, entity.getSceneCount() - 1);
        this.update(updateChainWrapper);
    }

    @Override
    public Map<String, BusinessDTO> getMapByCode(List<String> businessCodeList) {
        List<BusinessEntity> entityList = this.list(Wrappers.<BusinessEntity>lambdaQuery()
                .in(BusinessEntity::getBusinessCode, businessCodeList));
        List<BusinessDTO> dtoList = ListBeanUtil.copyList(entityList, BusinessDTO.class);
        Map<String, BusinessDTO> dtoMap = dtoList.stream().collect(Collectors.toMap(e->e.getBusinessCode(), e->e));
        return dtoMap;
    }

    @Override
    public BusinessDTO getBusinessByCode(String businessCode) {
        BusinessEntity entity = this.getOne(Wrappers.<BusinessEntity>lambdaQuery().eq(BusinessEntity::getBusinessCode, businessCode));
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, BusinessDTO.class);
    }

    @Override
    public List<BusinessDTO> queryAll() {
        List<BusinessEntity> entityList = this.list();
        return ListBeanUtil.copyList(entityList, BusinessDTO.class);
    }


}

