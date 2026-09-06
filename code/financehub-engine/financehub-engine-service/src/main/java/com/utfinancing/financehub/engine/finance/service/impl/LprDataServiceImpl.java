package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson2.JSONArray;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.model.dto.LprDataQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.LprDataDTO;
import com.utfinancing.financehub.engine.finance.model.vo.LprDataVO;
import com.utfinancing.financehub.engine.finance.entity.LprDataEntity;
import com.utfinancing.financehub.engine.finance.mapper.LprDataMapper;
import com.utfinancing.financehub.engine.finance.service.ILprDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-17
 * @Description :  LprData服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class LprDataServiceImpl extends ServiceImpl<LprDataMapper, LprDataEntity> implements ILprDataService {

    private final LprDataMapper lprDataMapper;

    @Override
    public Long saveLprData(LprDataDTO dto) {
        LprDataEntity entity = BeanUtil.copyProperties(dto, LprDataEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public void saveRawData(JSONArray jsonArray) {
        getBaseMapper().deleteAll();
        List<LprDataEntity> entities = new ArrayList<>();
        jsonArray.forEach(e -> {
            LprDataEntity lprDataEntity = BeanUtil.copyProperties(e, LprDataEntity.class);
            entities.add(lprDataEntity);
        });
        this.saveBatch(entities);
    }

    @Override
    public Long updateLprData(Long id, LprDataDTO dto) {
        LprDataEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public LprDataDTO getLprDataDTOById(Long id) {
        LprDataEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, LprDataDTO.class);
    }

    @Override
    public IPage<LprDataVO> selectPage(LprDataQueryDTO queryDTO) {
        LambdaQueryWrapper<LprDataEntity> queryWrapper = Wrappers.<LprDataEntity>lambdaQuery();
        //这里注入查询条件
        IPage<LprDataEntity> entityIPage = lprDataMapper.selectPage(new Page<LprDataEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, LprDataVO.class);
    }

    @Override
    public List<LprDataVO> listOneYear() {
        LambdaQueryWrapper<LprDataEntity> queryWrapper = Wrappers.<LprDataEntity>lambdaQuery();
        queryWrapper.eq(LprDataEntity::getPeriod, "LPR一年期");
        queryWrapper.orderByDesc(LprDataEntity::getStartDate);
        //这里注入查询条件
        List<LprDataEntity> entities = lprDataMapper.selectList(queryWrapper);
        return this.convert(entities);
    }

    private List<LprDataVO> convert(List<LprDataEntity> entities) {
        List<LprDataVO> collect = entities.stream().map(e -> {
            LprDataVO lprDataVO = BeanUtil.copyProperties(e, LprDataVO.class);
            lprDataVO.setLprRate(NumberUtil.mul(lprDataVO.getLprRate(), 100));
            return lprDataVO;
        }).collect(Collectors.toList());
        return collect;
    }

}

