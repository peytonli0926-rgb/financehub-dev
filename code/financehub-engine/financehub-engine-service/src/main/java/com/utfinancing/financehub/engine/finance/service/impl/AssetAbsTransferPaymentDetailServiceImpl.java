package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.model.dto.AssetAbsTransferPaymentDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.AssetAbsTransferPaymentDetailDTO;
import com.utfinancing.financehub.engine.finance.model.vo.AssetAbsTransferPaymentDetailVO;
import com.utfinancing.financehub.engine.finance.entity.AssetAbsTransferPaymentDetailEntity;
import com.utfinancing.financehub.engine.finance.mapper.AssetAbsTransferPaymentDetailMapper;
import com.utfinancing.financehub.engine.finance.service.IAssetAbsTransferPaymentDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author : bruyang
 * @Date : Create in 2024-03-20
 * @Description :  AssetAbsTransferPaymentDetail服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class AssetAbsTransferPaymentDetailServiceImpl extends ServiceImpl<AssetAbsTransferPaymentDetailMapper, AssetAbsTransferPaymentDetailEntity> implements IAssetAbsTransferPaymentDetailService {

    private final AssetAbsTransferPaymentDetailMapper assetAbsTransferPaymentDetailMapper;

    @Override
    public Long saveAssetAbsTransferPaymentDetail(AssetAbsTransferPaymentDetailDTO dto) {
        AssetAbsTransferPaymentDetailEntity entity = BeanUtil.copyProperties(dto, AssetAbsTransferPaymentDetailEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateAssetAbsTransferPaymentDetail(Long id, AssetAbsTransferPaymentDetailDTO dto) {
        AssetAbsTransferPaymentDetailEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public AssetAbsTransferPaymentDetailDTO getAssetAbsTransferPaymentDetailDTOById(Long id) {
        AssetAbsTransferPaymentDetailEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, AssetAbsTransferPaymentDetailDTO.class);
    }

    @Override
    public IPage<AssetAbsTransferPaymentDetailVO> selectPage(AssetAbsTransferPaymentDetailQueryDTO queryDTO) {
        LambdaQueryWrapper<AssetAbsTransferPaymentDetailEntity> queryWrapper = Wrappers.<AssetAbsTransferPaymentDetailEntity>lambdaQuery();
        if (ObjectUtil.isNotNull(queryDTO.getAssetAbsTransferPaymentId())) {
            queryWrapper.eq(AssetAbsTransferPaymentDetailEntity::getAssetAbsTransferPaymentId,queryDTO.getAssetAbsTransferPaymentId());
        }
        //这里注入查询条件
        IPage<AssetAbsTransferPaymentDetailEntity> entityIPage = assetAbsTransferPaymentDetailMapper.selectPage(new Page<AssetAbsTransferPaymentDetailEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, AssetAbsTransferPaymentDetailVO.class);
    }

    @Override
    public Boolean removeBatcheByDetailId(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Boolean.TRUE;
        }
        List<AssetAbsTransferPaymentDetailEntity> detailEntityList = this.lambdaQuery().in(AssetAbsTransferPaymentDetailEntity::getAssetAbsTransferPaymentId,idList).list();
        if (CollectionUtil.isEmpty(detailEntityList)) {
            return Boolean.TRUE;
        }
        return this.removeBatchByIds(detailEntityList.stream().map(AssetAbsTransferPaymentDetailEntity::getId).collect(Collectors.toList()));
    }

    @Override
    public List<AssetAbsTransferPaymentDetailVO> selectPaymentDetailList(AssetAbsTransferPaymentDetailQueryDTO queryDTO) {
        return assetAbsTransferPaymentDetailMapper.selectPaymentDetailList(queryDTO);
    }

}

