package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.model.dto.ImpairmentProvisionUploadTaskQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ImpairmentProvisionUploadTaskDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ImpairmentProvisionUploadTaskVO;
import com.utfinancing.financehub.engine.finance.entity.ImpairmentProvisionUploadTaskEntity;
import com.utfinancing.financehub.engine.finance.mapper.ImpairmentProvisionUploadTaskMapper;
import com.utfinancing.financehub.engine.finance.service.IImpairmentProvisionUploadTaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.time.Duration;
import java.util.List;
/**
 * @Author : wenbin
 * @Date : Create in 2024-04-28
 * @Description :  ImpairmentProvisionUploadTask服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class ImpairmentProvisionUploadTaskServiceImpl extends ServiceImpl<ImpairmentProvisionUploadTaskMapper, ImpairmentProvisionUploadTaskEntity> implements IImpairmentProvisionUploadTaskService {

    private final ImpairmentProvisionUploadTaskMapper impairmentProvisionUploadTaskMapper;

    @Override
    public Long saveImpairmentProvisionUploadTask(ImpairmentProvisionUploadTaskDTO dto) {
        ImpairmentProvisionUploadTaskEntity entity = BeanUtil.copyProperties(dto, ImpairmentProvisionUploadTaskEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateImpairmentProvisionUploadTask(Long id, ImpairmentProvisionUploadTaskDTO dto) {
        ImpairmentProvisionUploadTaskEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public ImpairmentProvisionUploadTaskDTO getImpairmentProvisionUploadTaskDTOById(Long id) {
        ImpairmentProvisionUploadTaskEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, ImpairmentProvisionUploadTaskDTO.class);
    }

    /**
     * @description:减值计提-首页列表-查询上传任务
     **/
    @Override
    public IPage<ImpairmentProvisionUploadTaskVO> selectPage(ImpairmentProvisionUploadTaskQueryDTO queryDTO) {
        LambdaQueryWrapper<ImpairmentProvisionUploadTaskEntity> queryWrapper = Wrappers.<ImpairmentProvisionUploadTaskEntity>lambdaQuery();
        //这里注入查询条件
        setQueryCondition(queryWrapper,queryDTO);
        IPage<ImpairmentProvisionUploadTaskEntity> entityIPage = impairmentProvisionUploadTaskMapper.selectPage(new Page<ImpairmentProvisionUploadTaskEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        IPage<ImpairmentProvisionUploadTaskVO> taskVOIPage = ListBeanUtil.copyPage(entityIPage, ImpairmentProvisionUploadTaskVO.class);
        taskVOIPage.getRecords();
        setData(taskVOIPage.getRecords());
        return taskVOIPage;
    }

    private void setData(List<ImpairmentProvisionUploadTaskVO> list) {
        if(CollUtil.isEmpty(list)){
            return;
        }
        list.stream().forEach(a->{
            if(ObjectUtil.isNotEmpty(a.getStartTime()) && ObjectUtil.isNotEmpty(a.getEndTime())) {
                // 计算任务耗时-时间差
                Duration duration = Duration.between(a.getStartTime(), a.getEndTime());
                long millis = duration.toMillis();
                a.setCostTime(millis);
            }
        });
    }

    private void setQueryCondition(LambdaQueryWrapper<ImpairmentProvisionUploadTaskEntity> queryWrapper, ImpairmentProvisionUploadTaskQueryDTO queryDTO) {
        if(ObjectUtil.isNotEmpty(queryDTO.getTaskType())){
            queryWrapper.eq(ImpairmentProvisionUploadTaskEntity::getTaskType,queryDTO.getTaskType());
        }
        if(ObjectUtil.isNotEmpty(queryDTO.getExcelType())){
            queryWrapper.eq(ImpairmentProvisionUploadTaskEntity::getExcelType,queryDTO.getExcelType());
        }
        if(ObjectUtil.isNotEmpty(queryDTO.getStatus())){
            queryWrapper.eq(ImpairmentProvisionUploadTaskEntity::getStatus,queryDTO.getStatus());
        }
        if(ObjectUtil.isNotEmpty(queryDTO.getUserName())){
            queryWrapper.like(ImpairmentProvisionUploadTaskEntity::getUserName,queryDTO.getUserName());
        }
        queryWrapper.orderByDesc(ImpairmentProvisionUploadTaskEntity::getId);
    }

}

