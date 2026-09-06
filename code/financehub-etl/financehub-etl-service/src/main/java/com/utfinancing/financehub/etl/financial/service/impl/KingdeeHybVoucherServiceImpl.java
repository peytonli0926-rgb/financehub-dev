package com.utfinancing.financehub.etl.financial.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.etl.financial.model.dto.KingdeeHybVoucherQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.KingdeeHybVoucherDTO;
import com.utfinancing.financehub.etl.financial.model.vo.KingdeeHybVoucherVO;
import com.utfinancing.financehub.etl.financial.entity.KingdeeHybVoucherEntity;
import com.utfinancing.financehub.etl.financial.mapper.KingdeeHybVoucherMapper;
import com.utfinancing.financehub.etl.financial.service.IKingdeeHybVoucherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : bruyang
 * @Date : Create in 2024-07-01
 * @Description :  KingdeeHybVoucher服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class KingdeeHybVoucherServiceImpl extends ServiceImpl<KingdeeHybVoucherMapper, KingdeeHybVoucherEntity> implements IKingdeeHybVoucherService {

    private final KingdeeHybVoucherMapper kingdeeHybVoucherMapper;

    @Override
    public Long saveKingdeeHybVoucher(KingdeeHybVoucherDTO dto) {
        KingdeeHybVoucherEntity entity = BeanUtil.copyProperties(dto, KingdeeHybVoucherEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateKingdeeHybVoucher(Long id, KingdeeHybVoucherDTO dto) {
        KingdeeHybVoucherEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public KingdeeHybVoucherDTO getKingdeeHybVoucherDTOById(Long id) {
        KingdeeHybVoucherEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, KingdeeHybVoucherDTO.class);
    }

    @Override
    public IPage<KingdeeHybVoucherVO> selectPage(KingdeeHybVoucherQueryDTO queryDTO) {
        LambdaQueryWrapper<KingdeeHybVoucherEntity> queryWrapper = Wrappers.<KingdeeHybVoucherEntity>lambdaQuery();
        //这里注入查询条件
        IPage<KingdeeHybVoucherEntity> entityIPage = kingdeeHybVoucherMapper.selectPage(new Page<KingdeeHybVoucherEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, KingdeeHybVoucherVO.class);
    }

}

