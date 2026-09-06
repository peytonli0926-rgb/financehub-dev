package com.utfinancing.financehub.etl.financial.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.etl.financial.entity.ReportPeriodSyncRecordEntity;
import com.utfinancing.financehub.etl.financial.mapper.ReportPeriodSyncRecordMapper;
import com.utfinancing.financehub.etl.financial.service.IReportPeriodSyncRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : jnc
 * @Date : Create in 2024-04-10
 * @Description :  ReportPeriodSyncRecord服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class ReportPeriodSyncRecordServiceImpl extends ServiceImpl<ReportPeriodSyncRecordMapper, ReportPeriodSyncRecordEntity> implements IReportPeriodSyncRecordService {

    private final ReportPeriodSyncRecordMapper reportPeriodSyncRecordMapper;

}

