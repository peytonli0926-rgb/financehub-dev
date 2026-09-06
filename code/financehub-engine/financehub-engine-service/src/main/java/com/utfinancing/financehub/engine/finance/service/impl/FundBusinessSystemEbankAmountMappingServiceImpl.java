package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.enums.RawMessageStatusEnum;
import com.utfinancing.financehub.engine.finance.entity.FundBusinessSystemEbankPcAmountEntity;
import com.utfinancing.financehub.engine.finance.entity.FundBusinessSystemEbankWyAmountEntity;
import com.utfinancing.financehub.engine.finance.model.dto.FundBusinessSystemEbankAmountMappingQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.FundBusinessSystemEbankAmountMappingDTO;
import com.utfinancing.financehub.engine.finance.model.dto.FundBusinessSystemEbankPcAmountDTO;
import com.utfinancing.financehub.engine.finance.model.dto.FundBusinessSystemEbankWyAmountDTO;
import com.utfinancing.financehub.engine.finance.model.vo.FundBusinessSystemEbankAmountMappingVO;
import com.utfinancing.financehub.engine.finance.entity.FundBusinessSystemEbankAmountMappingEntity;
import com.utfinancing.financehub.engine.finance.mapper.FundBusinessSystemEbankAmountMappingMapper;
import com.utfinancing.financehub.engine.finance.service.IFundBusinessSystemEbankAmountMappingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.finance.service.IFundBusinessSystemEbankPcAmountService;
import com.utfinancing.financehub.engine.finance.service.IFundBusinessSystemEbankWyAmountService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : bruyang
 * @Date : Create in 2024-07-12
 * @Description :  FundBusinessSystemEbankAmountMapping服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class FundBusinessSystemEbankAmountMappingServiceImpl extends ServiceImpl<FundBusinessSystemEbankAmountMappingMapper, FundBusinessSystemEbankAmountMappingEntity> implements IFundBusinessSystemEbankAmountMappingService {

    private final FundBusinessSystemEbankAmountMappingMapper fundBusinessSystemEbankAmountMappingMapper;

    private final IFundBusinessSystemEbankPcAmountService iFundBusinessSystemEbankPcAmountService;

    private final IFundBusinessSystemEbankWyAmountService iFundBusinessSystemEbankWyAmountService;

    @Override
    public Long saveFundBusinessSystemEbankAmountMapping(FundBusinessSystemEbankAmountMappingDTO dto) {
        FundBusinessSystemEbankAmountMappingEntity entity = BeanUtil.copyProperties(dto, FundBusinessSystemEbankAmountMappingEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateFundBusinessSystemEbankAmountMapping(Long id, FundBusinessSystemEbankAmountMappingDTO dto) {
        FundBusinessSystemEbankAmountMappingEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public FundBusinessSystemEbankAmountMappingDTO getFundBusinessSystemEbankAmountMappingDTOById(Long id) {
        FundBusinessSystemEbankAmountMappingEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, FundBusinessSystemEbankAmountMappingDTO.class);
    }

    @Override
    public IPage<FundBusinessSystemEbankAmountMappingVO> selectPage(FundBusinessSystemEbankAmountMappingQueryDTO queryDTO) {
        LambdaQueryWrapper<FundBusinessSystemEbankAmountMappingEntity> queryWrapper = Wrappers.<FundBusinessSystemEbankAmountMappingEntity>lambdaQuery();
        //这里注入查询条件
        IPage<FundBusinessSystemEbankAmountMappingEntity> entityIPage = fundBusinessSystemEbankAmountMappingMapper.selectPage(new Page<FundBusinessSystemEbankAmountMappingEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, FundBusinessSystemEbankAmountMappingVO.class);
    }

    @Override
    public void saveFundBusinessSystemEbankAmountMappingList(List<FundBusinessSystemEbankAmountMappingDTO> dtoList) {
        dtoList.forEach(v -> {
            FundBusinessSystemEbankAmountMappingEntity entity = BeanUtil.copyProperties(v,FundBusinessSystemEbankAmountMappingEntity.class);
            entity.setMqMessage(JSON.toJSONString(v));
            entity.setMessageStatus(RawMessageStatusEnum.NOT_EXECUTE.getCode());
            this.save(entity);
            //保存网银信息
            List<FundBusinessSystemEbankWyAmountDTO> wyAmountDTOList = v.getWyList();
            if (CollectionUtils.isNotEmpty(wyAmountDTOList)) {
                List<FundBusinessSystemEbankWyAmountEntity> wyAmountEntityList = Lists.newArrayList();
                for (FundBusinessSystemEbankWyAmountDTO w : wyAmountDTOList) {
                    FundBusinessSystemEbankWyAmountEntity wyAmountEntity = BeanUtil.copyProperties(w, FundBusinessSystemEbankWyAmountEntity.class);
                    wyAmountEntity.setEbankAmountId(entity.getId());
                    wyAmountEntity.setMatchNumber(entity.getMatchNumber());
                    wyAmountEntityList.add(wyAmountEntity);
                }
                iFundBusinessSystemEbankWyAmountService.saveBatch(wyAmountEntityList);
            }
            //保存批次信息
            List<FundBusinessSystemEbankPcAmountDTO> pcAmountDTOList = v.getPcList();
            if (CollectionUtils.isNotEmpty(pcAmountDTOList)) {
                List<FundBusinessSystemEbankPcAmountEntity> pcAmountEntityList = Lists.newArrayList();
                pcAmountDTOList.forEach(p -> {
                    FundBusinessSystemEbankPcAmountEntity pcAmountEntity = BeanUtil.copyProperties(p, FundBusinessSystemEbankPcAmountEntity.class);
                    pcAmountEntity.setEbankAmountId(entity.getId());
                    pcAmountEntity.setMatchNumber(entity.getMatchNumber());
                    pcAmountEntityList.add(pcAmountEntity);
                });
                iFundBusinessSystemEbankPcAmountService.saveBatch(pcAmountEntityList);
            }
        });
    }

}

