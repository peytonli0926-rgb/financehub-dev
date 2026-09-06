package com.utfinancing.financehub.etl.financial.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.etl.financial.model.dto.KingdeeContractBalanceQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.KingdeeContractBalanceDTO;
import com.utfinancing.financehub.etl.financial.model.vo.KingdeeContractBalanceVO;
import com.utfinancing.financehub.etl.financial.entity.KingdeeContractBalanceEntity;
import com.utfinancing.financehub.etl.financial.mapper.KingdeeContractBalanceMapper;
import com.utfinancing.financehub.etl.financial.service.IKingdeeContractBalanceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : lixin
 * @Date : Create in 2023-11-19
 * @Description :  KingdeeContractBalance服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class KingdeeContractBalanceServiceImpl extends ServiceImpl<KingdeeContractBalanceMapper, KingdeeContractBalanceEntity> implements IKingdeeContractBalanceService {

    private final KingdeeContractBalanceMapper kingdeeContractBalanceMapper;

    @Override
    public Long saveKingdeeContractBalance(KingdeeContractBalanceDTO dto) {
        KingdeeContractBalanceEntity entity = BeanUtil.copyProperties(dto, KingdeeContractBalanceEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateKingdeeContractBalance(Long id, KingdeeContractBalanceDTO dto) {
        KingdeeContractBalanceEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public KingdeeContractBalanceDTO getKingdeeContractBalanceDTOById(Long id) {
        KingdeeContractBalanceEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, KingdeeContractBalanceDTO.class);
    }

    @Override
    public IPage<KingdeeContractBalanceVO> selectPage(KingdeeContractBalanceQueryDTO queryDTO) {
        LambdaQueryWrapper<KingdeeContractBalanceEntity> queryWrapper = Wrappers.<KingdeeContractBalanceEntity>lambdaQuery();
        //这里注入查询条件
        IPage<KingdeeContractBalanceEntity> entityIPage = kingdeeContractBalanceMapper.selectPage(new Page<KingdeeContractBalanceEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, KingdeeContractBalanceVO.class);
    }

}

