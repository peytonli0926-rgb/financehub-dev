package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.entity.FundEbankTransactionDataEntity;
import com.utfinancing.financehub.engine.finance.model.dto.FundBusinessSystemEbankWyAmountQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.FundBusinessSystemEbankWyAmountDTO;
import com.utfinancing.financehub.engine.finance.model.dto.SelectFundEbankTransactionDataByConDTO;
import com.utfinancing.financehub.engine.finance.model.dto.SelectNonConfirmCollectionDataDTO;
import com.utfinancing.financehub.engine.finance.model.vo.FundBusinessSystemEbankWyAmountVO;
import com.utfinancing.financehub.engine.finance.entity.FundBusinessSystemEbankWyAmountEntity;
import com.utfinancing.financehub.engine.finance.mapper.FundBusinessSystemEbankWyAmountMapper;
import com.utfinancing.financehub.engine.finance.service.IFundBusinessSystemEbankWyAmountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * @Author : bruyang
 * @Date : Create in 2024-07-16
 * @Description :  FundBusinessSystemEbankWyAmount服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class FundBusinessSystemEbankWyAmountServiceImpl extends ServiceImpl<FundBusinessSystemEbankWyAmountMapper,
        FundBusinessSystemEbankWyAmountEntity> implements IFundBusinessSystemEbankWyAmountService {

    private final FundBusinessSystemEbankWyAmountMapper fundBusinessSystemEbankWyAmountMapper;

    @Override
    public Long saveFundBusinessSystemEbankWyAmount(FundBusinessSystemEbankWyAmountDTO dto) {
        FundBusinessSystemEbankWyAmountEntity entity = BeanUtil.copyProperties(dto, FundBusinessSystemEbankWyAmountEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateFundBusinessSystemEbankWyAmount(Long id, FundBusinessSystemEbankWyAmountDTO dto) {
        FundBusinessSystemEbankWyAmountEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public FundBusinessSystemEbankWyAmountDTO getFundBusinessSystemEbankWyAmountDTOById(Long id) {
        FundBusinessSystemEbankWyAmountEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, FundBusinessSystemEbankWyAmountDTO.class);
    }

    @Override
    public IPage<FundBusinessSystemEbankWyAmountVO> selectPage(FundBusinessSystemEbankWyAmountQueryDTO queryDTO) {
        LambdaQueryWrapper<FundBusinessSystemEbankWyAmountEntity> queryWrapper = Wrappers.<FundBusinessSystemEbankWyAmountEntity>lambdaQuery();
        //这里注入查询条件
        IPage<FundBusinessSystemEbankWyAmountEntity> entityIPage = fundBusinessSystemEbankWyAmountMapper.
                selectPage(new Page<FundBusinessSystemEbankWyAmountEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, FundBusinessSystemEbankWyAmountVO.class);
    }

    /**
     * 根据条件查询资金系统收款数据
     */
    public List<FundBusinessSystemEbankWyAmountEntity> selectFundEbankTransactionDataByCon(
            SelectNonConfirmCollectionDataDTO dto) {
        List<String> ebankNumberList = new ArrayList<>();
        if (StringUtils.isNotEmpty(dto.getEbankNumber())) {
            ebankNumberList = Arrays.asList(dto.getEbankNumber().split(","));
        } else {
            return new ArrayList<>();
        }

        LambdaQueryWrapper<FundBusinessSystemEbankWyAmountEntity> wrapper = new LambdaQueryWrapper();
        wrapper.in(FundBusinessSystemEbankWyAmountEntity::getEbankNumber, ebankNumberList);
        wrapper.eq(FundBusinessSystemEbankWyAmountEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        return fundBusinessSystemEbankWyAmountMapper.selectList(wrapper);
    }

    /**
     * 根据match number查询任一网银数据
     */
    public FundBusinessSystemEbankWyAmountEntity selectFundEbankTransactionDataByCon(String matchNumber) {
        if (StringUtils.isEmpty(matchNumber)) {
            return new FundBusinessSystemEbankWyAmountEntity();
        }
        LambdaQueryWrapper<FundBusinessSystemEbankWyAmountEntity> wrapper = new LambdaQueryWrapper();
        wrapper.in(FundBusinessSystemEbankWyAmountEntity::getMatchNumber, matchNumber);
        wrapper.eq(FundBusinessSystemEbankWyAmountEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        List<FundBusinessSystemEbankWyAmountEntity> list = fundBusinessSystemEbankWyAmountMapper.selectList(wrapper);
        if (list == null || list.isEmpty()) {
            return new FundBusinessSystemEbankWyAmountEntity();
        } else {
            return list.get(0);
        }
    }
}

