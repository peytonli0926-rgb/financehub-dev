package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.model.dto.FundBusinessSystemEbankPcAmountQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.FundBusinessSystemEbankPcAmountDTO;
import com.utfinancing.financehub.engine.finance.model.vo.FundBusinessSystemEbankPcAmountVO;
import com.utfinancing.financehub.engine.finance.entity.FundBusinessSystemEbankPcAmountEntity;
import com.utfinancing.financehub.engine.finance.mapper.FundBusinessSystemEbankPcAmountMapper;
import com.utfinancing.financehub.engine.finance.service.IFundBusinessSystemEbankPcAmountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : bruyang
 * @Date : Create in 2024-07-16
 * @Description :  FundBusinessSystemEbankPcAmount服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class FundBusinessSystemEbankPcAmountServiceImpl extends ServiceImpl<FundBusinessSystemEbankPcAmountMapper, FundBusinessSystemEbankPcAmountEntity> implements IFundBusinessSystemEbankPcAmountService {

    private final FundBusinessSystemEbankPcAmountMapper fundBusinessSystemEbankPcAmountMapper;

    @Override
    public Long saveFundBusinessSystemEbankPcAmount(FundBusinessSystemEbankPcAmountDTO dto) {
        FundBusinessSystemEbankPcAmountEntity entity = BeanUtil.copyProperties(dto, FundBusinessSystemEbankPcAmountEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateFundBusinessSystemEbankPcAmount(Long id, FundBusinessSystemEbankPcAmountDTO dto) {
        FundBusinessSystemEbankPcAmountEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public FundBusinessSystemEbankPcAmountDTO getFundBusinessSystemEbankPcAmountDTOById(Long id) {
        FundBusinessSystemEbankPcAmountEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, FundBusinessSystemEbankPcAmountDTO.class);
    }

    @Override
    public IPage<FundBusinessSystemEbankPcAmountVO> selectPage(FundBusinessSystemEbankPcAmountQueryDTO queryDTO) {
        LambdaQueryWrapper<FundBusinessSystemEbankPcAmountEntity> queryWrapper = Wrappers.<FundBusinessSystemEbankPcAmountEntity>lambdaQuery();
        //这里注入查询条件
        IPage<FundBusinessSystemEbankPcAmountEntity> entityIPage = fundBusinessSystemEbankPcAmountMapper.selectPage(new Page<FundBusinessSystemEbankPcAmountEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, FundBusinessSystemEbankPcAmountVO.class);
    }

}

