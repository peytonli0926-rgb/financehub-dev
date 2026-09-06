package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.entity.NonConfirmCollectionSecondDetailEntity;
import com.utfinancing.financehub.engine.finance.model.dto.LeaseIncomeDetailsQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.LeaseIncomeDetailsDTO;
import com.utfinancing.financehub.engine.finance.model.vo.LeaseIncomeDetailsVO;
import com.utfinancing.financehub.engine.finance.entity.LeaseIncomeDetailsEntity;
import com.utfinancing.financehub.engine.finance.mapper.LeaseIncomeDetailsMapper;
import com.utfinancing.financehub.engine.finance.service.ILeaseIncomeDetailsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.util.Date;
import java.util.List;
/**
 * @Author : hzhao
 * @Date : Create in 2023-11-13
 * @Description :  LeaseIncomeDetails服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class LeaseIncomeDetailsServiceImpl extends ServiceImpl<LeaseIncomeDetailsMapper, LeaseIncomeDetailsEntity>
        implements ILeaseIncomeDetailsService {

    private final LeaseIncomeDetailsMapper leaseIncomeDetailsMapper;

    @Override
    public Long saveLeaseIncomeDetails(LeaseIncomeDetailsDTO dto) {
        LeaseIncomeDetailsEntity entity = BeanUtil.copyProperties(dto, LeaseIncomeDetailsEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateLeaseIncomeDetails(Long id, LeaseIncomeDetailsDTO dto) {
        LeaseIncomeDetailsEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public LeaseIncomeDetailsDTO getLeaseIncomeDetailsDTOById(Long id) {
        LeaseIncomeDetailsEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, LeaseIncomeDetailsDTO.class);
    }

    @Override
    public IPage<LeaseIncomeDetailsVO> selectPage(LeaseIncomeDetailsQueryDTO queryDTO) {
        LambdaQueryWrapper<LeaseIncomeDetailsEntity> queryWrapper = Wrappers.<LeaseIncomeDetailsEntity>lambdaQuery();
        //这里注入查询条件
        IPage<LeaseIncomeDetailsEntity> entityIPage = leaseIncomeDetailsMapper.selectPage(new Page<LeaseIncomeDetailsEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, LeaseIncomeDetailsVO.class);
    }

    @Override
    public List<LeaseIncomeDetailsVO> selectDetailsByCondition(LeaseIncomeDetailsQueryDTO queryDTO) {
        return leaseIncomeDetailsMapper.selectDetailsByCondition(queryDTO);
    }

    /**
     * 取得合同收益计提数据-根据月份
     */
    public LeaseIncomeDetailsEntity getLeaseIncomeDetailsByMonth(String contractCode, Date planDate) {
        LambdaQueryWrapper<LeaseIncomeDetailsEntity> wrapper = new LambdaQueryWrapper();
        wrapper.eq(LeaseIncomeDetailsEntity::getContractCode, contractCode);
        if (planDate != null) {
            String leaseDate = DateUtils.parseDateToStr( "yyyy-MM", planDate);
            wrapper.apply("date_format(business_date, '%Y-%m') = {0}", leaseDate);
        }
        wrapper.eq(LeaseIncomeDetailsEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        return leaseIncomeDetailsMapper.selectOne(wrapper);
    }

    /**
     * 收益计提结果校验
     */
    public void leaseIncomeVadation() {
        leaseIncomeDetailsMapper.leaseIncomeDataValidation();
    }
}

