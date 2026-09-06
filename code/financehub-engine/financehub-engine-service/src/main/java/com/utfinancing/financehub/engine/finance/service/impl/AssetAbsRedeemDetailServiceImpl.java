package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.entity.OutTableContractDetailEntity;
import com.utfinancing.financehub.engine.finance.model.dto.AssetAbsRedeemDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.AssetAbsRedeemDetailDTO;
import com.utfinancing.financehub.engine.finance.model.vo.AssetAbsRedeemDetailVO;
import com.utfinancing.financehub.engine.finance.entity.AssetAbsRedeemDetailEntity;
import com.utfinancing.financehub.engine.finance.mapper.AssetAbsRedeemDetailMapper;
import com.utfinancing.financehub.engine.finance.service.IAssetAbsRedeemDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author : bruyang
 * @Date : Create in 2024-03-15
 * @Description :  AssetAbsRedeemDetail服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class AssetAbsRedeemDetailServiceImpl extends ServiceImpl<AssetAbsRedeemDetailMapper, AssetAbsRedeemDetailEntity> implements IAssetAbsRedeemDetailService {

    private final AssetAbsRedeemDetailMapper assetAbsRedeemDetailMapper;

    @Override
    public Long saveAssetAbsRedeemDetail(AssetAbsRedeemDetailDTO dto) {
        AssetAbsRedeemDetailEntity entity = BeanUtil.copyProperties(dto, AssetAbsRedeemDetailEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateAssetAbsRedeemDetail(Long id, AssetAbsRedeemDetailDTO dto) {
        AssetAbsRedeemDetailEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public AssetAbsRedeemDetailDTO getAssetAbsRedeemDetailDTOById(Long id) {
        AssetAbsRedeemDetailEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, AssetAbsRedeemDetailDTO.class);
    }

    @Override
    public IPage<AssetAbsRedeemDetailVO> selectPage(AssetAbsRedeemDetailQueryDTO queryDTO) {
        LambdaQueryWrapper<AssetAbsRedeemDetailEntity> queryWrapper = getQueryWrapper(queryDTO);
        //这里注入查询条件
        IPage<AssetAbsRedeemDetailEntity> entityIPage = assetAbsRedeemDetailMapper.selectPage(new Page<AssetAbsRedeemDetailEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, AssetAbsRedeemDetailVO.class);
    }

    @Override
    public Boolean removeBatcheByDetailId(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Boolean.TRUE;
        }
        List<AssetAbsRedeemDetailEntity> detailEntityList = this.lambdaQuery().in(AssetAbsRedeemDetailEntity::getAssetAbsRedeemId,idList).list();
        if (CollectionUtil.isEmpty(detailEntityList)) {
            return Boolean.TRUE;
        }
        return this.removeBatchByIds(detailEntityList.stream().map(AssetAbsRedeemDetailEntity::getId).collect(Collectors.toList()));
    }

    @Override
    public List<AssetAbsRedeemDetailVO> selectByCondition(AssetAbsRedeemDetailQueryDTO queryDTO) {
        return BeanUtil.copyToList(this.list(getQueryWrapper(queryDTO)), AssetAbsRedeemDetailVO.class);
    }

    public LambdaQueryWrapper<AssetAbsRedeemDetailEntity> getQueryWrapper(AssetAbsRedeemDetailQueryDTO queryDTO){
        LambdaQueryWrapper<AssetAbsRedeemDetailEntity> queryWrapper = Wrappers.<AssetAbsRedeemDetailEntity>lambdaQuery();
        if (ObjectUtils.isNotNull(queryDTO.getAssetAbsRedeemId())) {
            queryWrapper.eq(AssetAbsRedeemDetailEntity::getAssetAbsRedeemId,queryDTO.getAssetAbsRedeemId());
        }
        if (StringUtils.isNotEmpty(queryDTO.getContractCode())) {
            queryWrapper.like(AssetAbsRedeemDetailEntity::getContractCode,queryDTO.getContractCode());
        }
        if (CollectionUtil.isNotEmpty(queryDTO.getAssetAbsRedeemIdList())) {
            queryWrapper.in(AssetAbsRedeemDetailEntity::getAssetAbsRedeemId,queryDTO.getAssetAbsRedeemIdList());
        }
        queryWrapper.orderByDesc(AssetAbsRedeemDetailEntity::getCreateTime);
        return queryWrapper;
    }

}

