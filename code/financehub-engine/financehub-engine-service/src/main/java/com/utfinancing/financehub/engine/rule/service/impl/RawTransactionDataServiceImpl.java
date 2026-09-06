package com.utfinancing.financehub.engine.rule.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.enums.RawMessageStatusEnum;
import com.utfinancing.financehub.engine.enums.SceneEnum;
import com.utfinancing.financehub.engine.enums.ServiceFeeScenceDescEnum;
import com.utfinancing.financehub.engine.enums.SystemEnum;
import com.utfinancing.financehub.engine.finance.entity.ContractEntity;
import com.utfinancing.financehub.engine.finance.mapper.ContractMapper;
import com.utfinancing.financehub.engine.finance.service.IContractService;
import com.utfinancing.financehub.engine.rule.constant.RuleConstant;
import com.utfinancing.financehub.engine.rule.entity.RawTransactionDataEntity;
import com.utfinancing.financehub.engine.rule.model.dto.RawTransactionDataQueryDTO;
import com.utfinancing.financehub.engine.rule.model.dto.RawTransactionDataDTO;
import com.utfinancing.financehub.engine.rule.model.vo.RawTransactionDataDuplicateVo;
import com.utfinancing.financehub.engine.rule.model.vo.RawTransactionDataVO;
import com.utfinancing.financehub.engine.rule.mapper.RawTransactionDataMapper;
import com.utfinancing.financehub.engine.rule.service.IRawTransactionDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @Author : lixin
 * @Date : Create in 2023-10-16
 * @Description :  RawTransationData服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class RawTransactionDataServiceImpl extends ServiceImpl<RawTransactionDataMapper, RawTransactionDataEntity> implements IRawTransactionDataService {

    private final RawTransactionDataMapper rawTransationDataMapper;
    @Autowired
    private IContractService contractService;

    @Override
    public Long saveRawTransationData(RawTransactionDataDTO dto) {
        RawTransactionDataEntity entity = BeanUtil.copyProperties(dto, RawTransactionDataEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateRawTransationData(Long id, RawTransactionDataDTO dto) {
        RawTransactionDataEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public Boolean updateStatus(Long id, String status, String errorInfo) {
        RawTransactionDataEntity entity = new RawTransactionDataEntity();
        entity.setId(id);
        entity.setMessageStatus(status);
        entity.setErrorInfo(errorInfo);
        return this.updateById(entity);
    }

    @Override
    public RawTransactionDataDTO getRawTransationDataDTOById(Long id) {
        RawTransactionDataEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, RawTransactionDataDTO.class);
    }

    @Override
    public IPage<RawTransactionDataVO> selectPage(RawTransactionDataQueryDTO queryDTO) {
        LambdaQueryWrapper<RawTransactionDataEntity> queryWrapper = Wrappers.<RawTransactionDataEntity>lambdaQuery();
        queryWrapper
                .eq(StrUtil.isNotBlank(queryDTO.getSystemCode()), RawTransactionDataEntity::getSystemCode, queryDTO.getSystemCode())
                .like(StrUtil.isNotBlank(queryDTO.getOrderId()), RawTransactionDataEntity::getOrderId, queryDTO.getOrderId())
                .like(StrUtil.isNotBlank(queryDTO.getMessageId()), RawTransactionDataEntity::getMessageId, queryDTO.getMessageId())
                .eq(StrUtil.isNotBlank(queryDTO.getBusinessCode()), RawTransactionDataEntity::getBusinessCode, queryDTO.getBusinessCode())
                .eq(StrUtil.isNotBlank(queryDTO.getOrgId()), RawTransactionDataEntity::getOrgId, queryDTO.getOrgId())
                .eq(StrUtil.isNotBlank(queryDTO.getSceneCode()), RawTransactionDataEntity::getSceneCode, queryDTO.getSceneCode())
                .like(StrUtil.isNotBlank(queryDTO.getContractCode()), RawTransactionDataEntity::getContractCode, queryDTO.getContractCode())
                .eq(StrUtil.isNotBlank(queryDTO.getContractStatus()), RawTransactionDataEntity::getContractStatus, queryDTO.getContractStatus())
                .eq(StrUtil.isNotBlank(queryDTO.getMessageStatus()), RawTransactionDataEntity::getMessageStatus, queryDTO.getMessageStatus())
                .like(StrUtil.isNotBlank(queryDTO.getErrorInfo()), RawTransactionDataEntity::getErrorInfo, queryDTO.getErrorInfo())
                .orderByDesc(RawTransactionDataEntity::getCreateTime);
        IPage<RawTransactionDataEntity> entityIPage = rawTransationDataMapper.selectPage(new Page<RawTransactionDataEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, RawTransactionDataVO.class);
    }

    @Override
    public RawTransactionDataEntity saveRawData(Map<String, Object> dataMap) {
        RawTransactionDataEntity entity = BeanUtil.copyProperties(dataMap, RawTransactionDataEntity.class);
        entity.setMessageContent(JSONObject.parseObject(JSONObject.toJSONString(dataMap)));
        entity.setMessageStatus(RawMessageStatusEnum.NOT_EXECUTE.getCode());
        this.save(entity);
        return entity;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public RawTransactionDataEntity saveRawData(String messageId, JSONObject jsonData) {
        RawTransactionDataEntity entity = BeanUtil.copyProperties(jsonData, RawTransactionDataEntity.class);
        entity.setMessageId(messageId);
        entity.setMessageContent(jsonData);
        entity.setMessageStatus(RawMessageStatusEnum.NOT_EXECUTE.getCode());
        LocalDateTime businessDate = null;
        String businessDateStr = jsonData.getString(RuleConstant.FIELD_BUSINESS_DATE);
        if (StrUtil.isBlank(businessDateStr)) {
            businessDate = LocalDateTimeUtil.now();
        } else {
            businessDate = DateUtils.parseLocalDateTime(businessDateStr);
        }
        entity.setBusinessDate(businessDate);
        //保存合同信息
        ContractEntity contractEntity = new ContractEntity();
        contractEntity.setContractCode(entity.getMessageContent().getString("contractCode"));
        if (ServiceFeeScenceDescEnum.getSignDesc().contains(entity.getSceneCode())) {
            //应收服务费
            BigDecimal receivableServiceAmount = entity.getMessageContent().getBigDecimal("receivableServiceAmount");
            if (BigDecimal.ZERO.compareTo(receivableServiceAmount) != 0) contractEntity.setReceivableServiceAmount(entity.getMessageContent().getBigDecimal("receivableServiceAmount"));
        }
        if (ServiceFeeScenceDescEnum.ZXFWFSK.getDesc().equals(entity.getSceneCode())
                || (SystemEnum.XWXT.getCode().equals(entity.getSystemCode()) && ServiceFeeScenceDescEnum.HL.getDesc().equals(entity.getSceneCode()))) {
            // 实收服务费
            BigDecimal receiveServiceAmount = entity.getMessageContent().getBigDecimal("receiveServiceAmount");
            if (BigDecimal.ZERO.compareTo(receiveServiceAmount) != 0) contractEntity.setActualServiceAmount(receiveServiceAmount);
        }
        contractService.updateByContractCode(contractEntity);
        this.save(entity);
        return entity;
    }

    @Override
    public List<RawTransactionDataDuplicateVo> getDuplicateData(String systemCode, List<String> ignoreRepeatDataSceneCode) {
        return rawTransationDataMapper.getDuplicateData(systemCode, ignoreRepeatDataSceneCode);
    }

}

