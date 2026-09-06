package com.utfinancing.financehub.engine.rule.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.enums.MqErrorMessageStatusEnum;
import com.utfinancing.financehub.engine.rule.model.dto.MqErrorMessageQueryDTO;
import com.utfinancing.financehub.engine.rule.model.dto.MqErrorMessageDTO;
import com.utfinancing.financehub.engine.rule.model.vo.MqErrorMessageVO;
import com.utfinancing.financehub.engine.rule.entity.MqErrorMessageEntity;
import com.utfinancing.financehub.engine.rule.mapper.MqErrorMessageMapper;
import com.utfinancing.financehub.engine.rule.service.IMqErrorMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : lixin
 * @Date : Create in 2024-01-08
 * @Description :  MqErrorMessage服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class MqErrorMessageServiceImpl extends ServiceImpl<MqErrorMessageMapper, MqErrorMessageEntity> implements IMqErrorMessageService {

    private final MqErrorMessageMapper mqErrorMessageMapper;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public Long saveMqErrorMessage(MqErrorMessageDTO dto) {
        MqErrorMessageEntity entity = BeanUtil.copyProperties(dto, MqErrorMessageEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateMqErrorMessage(Long id, MqErrorMessageDTO dto) {
        MqErrorMessageEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public MqErrorMessageDTO getMqErrorMessageDTOById(Long id) {
        MqErrorMessageEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, MqErrorMessageDTO.class);
    }

    @Override
    public Boolean rePushMessage(List<Long> messageIdList) {
        if (CollectionUtil.isEmpty(messageIdList)){
            throw new ServiceException("请选择需要推送的消息");
        }
        List<MqErrorMessageEntity> entityList = this.list(Wrappers.<MqErrorMessageEntity>lambdaQuery()
                .in(MqErrorMessageEntity::getId, messageIdList)
                .eq(MqErrorMessageEntity::getStatus, MqErrorMessageStatusEnum.NOT_PROCESS.getCode()));
        for (MqErrorMessageEntity entity: entityList){
            if (StrUtil.isNotBlank(entity.getOriginalExchange()) && StrUtil.isNotBlank(entity.getOriginalRoutingKey())){
                rabbitTemplate.convertAndSend(entity.getOriginalExchange(), entity.getOriginalRoutingKey(), entity.getMessageBody());
                entity.setStatus(MqErrorMessageStatusEnum.REPUSH.getCode());
                entity.updateById();
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public IPage<MqErrorMessageVO> selectPage(MqErrorMessageQueryDTO queryDTO) {
        LambdaQueryWrapper<MqErrorMessageEntity> queryWrapper = Wrappers.<MqErrorMessageEntity>lambdaQuery();
        queryWrapper.eq(StrUtil.isNotBlank(queryDTO.getStatus()), MqErrorMessageEntity::getStatus, queryDTO.getStatus());
        queryWrapper.eq(StrUtil.isNotBlank(queryDTO.getOriginalRoutingKey()), MqErrorMessageEntity::getOriginalRoutingKey, queryDTO.getOriginalRoutingKey());
        queryWrapper.like(StrUtil.isNotBlank(queryDTO.getExceptionMessage()), MqErrorMessageEntity::getExceptionMessage, queryDTO.getExceptionMessage());
        IPage<MqErrorMessageEntity> entityIPage = mqErrorMessageMapper.selectPage(new Page<MqErrorMessageEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, MqErrorMessageVO.class);
    }

}

