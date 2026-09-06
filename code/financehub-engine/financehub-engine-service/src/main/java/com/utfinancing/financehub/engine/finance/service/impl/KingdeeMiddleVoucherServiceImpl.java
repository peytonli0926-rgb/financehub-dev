package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.model.dto.KingdeeMiddleVoucherQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.KingdeeMiddleVoucherDTO;
import com.utfinancing.financehub.engine.finance.model.vo.KingdeeMiddleVoucherVO;
import com.utfinancing.financehub.engine.finance.entity.KingdeeMiddleVoucherEntity;
import com.utfinancing.financehub.engine.finance.mapper.KingdeeMiddleVoucherMapper;
import com.utfinancing.financehub.engine.finance.service.IKingdeeMiddleVoucherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : bruyang
 * @Date : Create in 2024-07-04
 * @Description :  KingdeeMiddleVoucher服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class KingdeeMiddleVoucherServiceImpl extends ServiceImpl<KingdeeMiddleVoucherMapper, KingdeeMiddleVoucherEntity> implements IKingdeeMiddleVoucherService {

    private final KingdeeMiddleVoucherMapper kingdeeMiddleVoucherMapper;

    @Override
    public Long saveKingdeeMiddleVoucher(KingdeeMiddleVoucherDTO dto) {
        KingdeeMiddleVoucherEntity entity = BeanUtil.copyProperties(dto, KingdeeMiddleVoucherEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateKingdeeMiddleVoucher(Long id, KingdeeMiddleVoucherDTO dto) {
        KingdeeMiddleVoucherEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public KingdeeMiddleVoucherDTO getKingdeeMiddleVoucherDTOById(Long id) {
        KingdeeMiddleVoucherEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, KingdeeMiddleVoucherDTO.class);
    }

    @Override
    public IPage<KingdeeMiddleVoucherVO> selectPage(KingdeeMiddleVoucherQueryDTO queryDTO) {
        LambdaQueryWrapper<KingdeeMiddleVoucherEntity> queryWrapper = Wrappers.<KingdeeMiddleVoucherEntity>lambdaQuery();
        //这里注入查询条件
        IPage<KingdeeMiddleVoucherEntity> entityIPage = kingdeeMiddleVoucherMapper.selectPage(new Page<KingdeeMiddleVoucherEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, KingdeeMiddleVoucherVO.class);
    }

}

