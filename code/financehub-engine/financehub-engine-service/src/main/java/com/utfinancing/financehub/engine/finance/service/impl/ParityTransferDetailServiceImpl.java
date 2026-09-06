package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.entity.OutTableContractDetailEntity;
import com.utfinancing.financehub.engine.finance.model.dto.ParityTransferDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ParityTransferDetailDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ParityTransferDetailVO;
import com.utfinancing.financehub.engine.finance.entity.ParityTransferDetailEntity;
import com.utfinancing.financehub.engine.finance.mapper.ParityTransferDetailMapper;
import com.utfinancing.financehub.engine.finance.service.IParityTransferDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author : bruyang
 * @Date : Create in 2024-04-02
 * @Description :  ParityTransferDetail服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class ParityTransferDetailServiceImpl extends ServiceImpl<ParityTransferDetailMapper, ParityTransferDetailEntity> implements IParityTransferDetailService {

    private final ParityTransferDetailMapper parityTransferDetailMapper;

    @Override
    public Long saveParityTransferDetail(ParityTransferDetailDTO dto) {
        ParityTransferDetailEntity entity = BeanUtil.copyProperties(dto, ParityTransferDetailEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateParityTransferDetail(Long id, ParityTransferDetailDTO dto) {
        ParityTransferDetailEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public ParityTransferDetailDTO getParityTransferDetailDTOById(Long id) {
        ParityTransferDetailEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, ParityTransferDetailDTO.class);
    }

    @Override
    public IPage<ParityTransferDetailVO> selectPage(ParityTransferDetailQueryDTO queryDTO) {
        LambdaQueryWrapper<ParityTransferDetailEntity> queryWrapper = Wrappers.<ParityTransferDetailEntity>lambdaQuery();
        //这里注入查询条件
        if (ObjectUtils.isNotNull(queryDTO.getParityTransferId())) {
            queryWrapper.eq(ParityTransferDetailEntity::getParityTransferId,queryDTO.getParityTransferId());
        }
        queryWrapper.orderByDesc(ParityTransferDetailEntity::getCreateTime);
        IPage<ParityTransferDetailEntity> entityIPage = parityTransferDetailMapper.selectPage(new Page<ParityTransferDetailEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, ParityTransferDetailVO.class);
    }

    @Override
    public Boolean removeBatcheByDetailId(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Boolean.TRUE;
        }
        List<ParityTransferDetailEntity> detailEntityList = this.lambdaQuery().in(ParityTransferDetailEntity::getParityTransferId,idList).list();
        if (CollectionUtil.isEmpty(detailEntityList)) {
            return Boolean.TRUE;
        }
        return this.removeBatchByIds(detailEntityList.stream().map(ParityTransferDetailEntity::getId).collect(Collectors.toList()));
    }

    /**
     * 根据平价转让id 查询详情list
     * @param parityTransferId
     * @return
     */
    @Override
    public List<ParityTransferDetailEntity> getByParityTransferId(Long parityTransferId) {
        List<ParityTransferDetailEntity> list = list(new LambdaQueryWrapper<ParityTransferDetailEntity>().eq(ParityTransferDetailEntity::getParityTransferId, parityTransferId));
        return list;
    }

    /**
     * 根据批次号查询
     * @param batch
     * @return
     */
    @Override
    public List<ParityTransferDetailVO> getByBatch(String batch) {
        List<ParityTransferDetailEntity> list = list(new LambdaQueryWrapper<ParityTransferDetailEntity>().eq(ParityTransferDetailEntity::getBatch, batch));
        return ListBeanUtil.copyList(list, ParityTransferDetailVO.class);
    }

}

