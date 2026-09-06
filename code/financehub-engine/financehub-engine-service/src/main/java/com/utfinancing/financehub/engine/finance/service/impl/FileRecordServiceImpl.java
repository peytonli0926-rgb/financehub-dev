package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.entity.CheckAccountDetailResultHisEntity;
import com.utfinancing.financehub.engine.finance.entity.FileRecordEntity;
import com.utfinancing.financehub.engine.finance.mapper.FileRecordMapper;
import com.utfinancing.financehub.engine.finance.model.dto.FileRecordQueryDTO;
import com.utfinancing.financehub.engine.finance.service.IFileRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : jnc
 * @Date : Create in 2024-04-29
 * @Description :  FileRecord服务实现类
 * @Modified :
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class FileRecordServiceImpl extends ServiceImpl<FileRecordMapper, FileRecordEntity> implements IFileRecordService {

    private final FileRecordMapper fileRecordMapper;

    @Override
    public IPage<FileRecordEntity> selectFileListByModuleAndBusiness(FileRecordQueryDTO queryDTO) {
        LambdaQueryWrapper<FileRecordEntity> queryWrapper = Wrappers.<FileRecordEntity>lambdaQuery();
        //这里注入查询条件
        queryWrapper.eq(FileRecordEntity::getModuleName, queryDTO.getModuleName());
        queryWrapper.eq(FileRecordEntity::getBusinessScene, queryDTO.getBusinessScene());
        queryWrapper.in(FileRecordEntity::getExecuteStatus, queryDTO.getExecuteStatus());
        queryWrapper.orderByDesc(FileRecordEntity::getId);
        IPage<FileRecordEntity> entityIPage = fileRecordMapper.selectPage(new Page<FileRecordEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return entityIPage;
    }
}

