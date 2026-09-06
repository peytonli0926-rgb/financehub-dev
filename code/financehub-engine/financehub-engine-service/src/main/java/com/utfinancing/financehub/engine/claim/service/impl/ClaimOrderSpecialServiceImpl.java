package com.utfinancing.financehub.engine.claim.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.claim.model.dto.ClaimOrderSpecialQueryDTO;
import com.utfinancing.financehub.engine.claim.model.dto.ClaimOrderSpecialDTO;
import com.utfinancing.financehub.engine.claim.model.vo.ClaimOrderCostVo;
import com.utfinancing.financehub.engine.claim.model.vo.ClaimOrderSpecialVO;
import com.utfinancing.financehub.engine.claim.entity.ClaimOrderSpecialEntity;
import com.utfinancing.financehub.engine.claim.mapper.ClaimOrderSpecialMapper;
import com.utfinancing.financehub.engine.claim.service.IClaimOrderSpecialService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : lixin
 * @Date : Create in 2023-11-08
 * @Description :  ClaimOrderSpecial服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class ClaimOrderSpecialServiceImpl extends ServiceImpl<ClaimOrderSpecialMapper, ClaimOrderSpecialEntity> implements IClaimOrderSpecialService {

    private final ClaimOrderSpecialMapper claimOrderSpecialMapper;

    @Override
    public Long saveClaimOrderSpecial(ClaimOrderSpecialDTO dto) {
        ClaimOrderSpecialEntity entity = BeanUtil.copyProperties(dto, ClaimOrderSpecialEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateClaimOrderSpecial(Long id, ClaimOrderSpecialDTO dto) {
        ClaimOrderSpecialEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public ClaimOrderSpecialDTO getClaimOrderSpecialDTOById(Long id) {
        ClaimOrderSpecialEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, ClaimOrderSpecialDTO.class);
    }

    @Override
    public IPage<ClaimOrderSpecialVO> selectPage(ClaimOrderSpecialQueryDTO queryDTO) {
        LambdaQueryWrapper<ClaimOrderSpecialEntity> queryWrapper = Wrappers.<ClaimOrderSpecialEntity>lambdaQuery();
        //这里注入查询条件
        IPage<ClaimOrderSpecialEntity> entityIPage = claimOrderSpecialMapper.selectPage(new Page<ClaimOrderSpecialEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, ClaimOrderSpecialVO.class);
    }

    @Override
    public List<ClaimOrderSpecialVO> selectByCondition(ClaimOrderSpecialQueryDTO queryDTO) {
        return claimOrderSpecialMapper.selectByCondition(queryDTO);
    }

    @Override
    public List<ClaimOrderCostVo> statisticalCost(ClaimOrderSpecialQueryDTO queryDTO) {
        return claimOrderSpecialMapper.statisticalCost(queryDTO);
    }

    @Override
    public List<ClaimOrderSpecialVO> getOrderByContractCodeList(List<String> contractList) {
        return claimOrderSpecialMapper.getOrderByContractCodeList(contractList);
    }

}

