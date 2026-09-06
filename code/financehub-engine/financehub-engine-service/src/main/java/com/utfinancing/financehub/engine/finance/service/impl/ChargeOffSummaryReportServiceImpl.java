package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.model.dto.ChargeOffSummaryReportQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ChargeOffSummaryReportDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ChargeOffSummaryReportVO;
import com.utfinancing.financehub.engine.finance.entity.ChargeOffSummaryReportEntity;
import com.utfinancing.financehub.engine.finance.mapper.ChargeOffSummaryReportMapper;
import com.utfinancing.financehub.engine.finance.service.IChargeOffSummaryReportService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : bruyang
 * @Date : Create in 2024-07-26
 * @Description :  ChargeOffSummaryReport服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class ChargeOffSummaryReportServiceImpl extends ServiceImpl<ChargeOffSummaryReportMapper, ChargeOffSummaryReportEntity> implements IChargeOffSummaryReportService {

    private final ChargeOffSummaryReportMapper chargeOffSummaryReportMapper;

    @Override
    public Long saveChargeOffSummaryReport(ChargeOffSummaryReportDTO dto) {
        ChargeOffSummaryReportEntity entity = BeanUtil.copyProperties(dto, ChargeOffSummaryReportEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateChargeOffSummaryReport(Long id, ChargeOffSummaryReportDTO dto) {
        ChargeOffSummaryReportEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public ChargeOffSummaryReportDTO getChargeOffSummaryReportDTOById(Long id) {
        ChargeOffSummaryReportEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, ChargeOffSummaryReportDTO.class);
    }

    @Override
    public IPage<ChargeOffSummaryReportVO> selectPage(ChargeOffSummaryReportQueryDTO queryDTO) {
        LambdaQueryWrapper<ChargeOffSummaryReportEntity> queryWrapper = Wrappers.<ChargeOffSummaryReportEntity>lambdaQuery();
        //这里注入查询条件
//        IPage<ChargeOffSummaryReportEntity> entityIPage = chargeOffSummaryReportMapper.selectPage(new Page<ChargeOffSummaryReportEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
//        return ListBeanUtil.copyPage(entityIPage, ChargeOffSummaryReportVO.class);
        Page page = new Page(queryDTO.getPageNum(), queryDTO.getPageSize());
       return chargeOffSummaryReportMapper.summaryPage(page, queryDTO);
    }

    @Override
    public IPage<ChargeOffSummaryReportVO> summaryDetailPage(ChargeOffSummaryReportQueryDTO queryDTO) {
        Page page = new Page(queryDTO.getPageNum(), queryDTO.getPageSize());
        return chargeOffSummaryReportMapper.summaryDetailPage(page, queryDTO);
    }


}

