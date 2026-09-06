package com.utfinancing.financehub.etl.financial.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.etl.financial.model.dto.KingdeeVoucherEntryQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.KingdeeVoucherEntryDTO;
import com.utfinancing.financehub.etl.financial.model.vo.KingdeeVoucherEntryVO;
import com.utfinancing.financehub.etl.financial.entity.KingdeeVoucherEntryEntity;
import com.utfinancing.financehub.etl.financial.mapper.KingdeeVoucherEntryMapper;
import com.utfinancing.financehub.etl.financial.service.IKingdeeVoucherEntryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : lixin
 * @Date : Create in 2023-11-15
 * @Description :  KingdeeVoucherEntry服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class KingdeeVoucherEntryServiceImpl extends ServiceImpl<KingdeeVoucherEntryMapper, KingdeeVoucherEntryEntity> implements IKingdeeVoucherEntryService {

    private final KingdeeVoucherEntryMapper kingdeeVoucherEntryMapper;

    @Override
    public Long saveKingdeeVoucherEntry(KingdeeVoucherEntryDTO dto) {
        KingdeeVoucherEntryEntity entity = BeanUtil.copyProperties(dto, KingdeeVoucherEntryEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateKingdeeVoucherEntry(Long id, KingdeeVoucherEntryDTO dto) {
        KingdeeVoucherEntryEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public KingdeeVoucherEntryDTO getKingdeeVoucherEntryDTOById(Long id) {
        KingdeeVoucherEntryEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, KingdeeVoucherEntryDTO.class);
    }

    @Override
    public IPage<KingdeeVoucherEntryVO> selectPage(KingdeeVoucherEntryQueryDTO queryDTO) {
        LambdaQueryWrapper<KingdeeVoucherEntryEntity> queryWrapper = Wrappers.<KingdeeVoucherEntryEntity>lambdaQuery();
        //这里注入查询条件
        IPage<KingdeeVoucherEntryEntity> entityIPage = kingdeeVoucherEntryMapper.selectPage(new Page<KingdeeVoucherEntryEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, KingdeeVoucherEntryVO.class);
    }

}

