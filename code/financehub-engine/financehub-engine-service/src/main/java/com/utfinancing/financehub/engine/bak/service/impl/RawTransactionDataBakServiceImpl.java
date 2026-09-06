package com.utfinancing.financehub.engine.bak.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.bak.model.dto.RawTransactionDataBakQueryDTO;
import com.utfinancing.financehub.engine.bak.model.dto.RawTransactionDataBakDTO;
import com.utfinancing.financehub.engine.bak.model.vo.RawTransactionDataBakVO;
import com.utfinancing.financehub.engine.bak.entity.RawTransactionDataBakEntity;
import com.utfinancing.financehub.engine.bak.mapper.RawTransactionDataBakMapper;
import com.utfinancing.financehub.engine.bak.service.IRawTransactionDataBakService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.enums.RawMessageStatusEnum;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherDTO;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import com.utfinancing.financehub.engine.scene.service.IFieldMappingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
import java.util.Map;

/**
 * @Author : lixin
 * @Date : Create in 2023-10-18
 * @Description :  RawTransactionDataBak服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class RawTransactionDataBakServiceImpl extends ServiceImpl<RawTransactionDataBakMapper, RawTransactionDataBakEntity> implements IRawTransactionDataBakService {

    private final RawTransactionDataBakMapper rawTransactionDataBakMapper;
    private final IFieldMappingService fieldMappingService;
    private final IRuleService ruleService;

    @Override
    public Long saveRawTransactionDataBak(RawTransactionDataBakDTO dto) {
        RawTransactionDataBakEntity entity = BeanUtil.copyProperties(dto, RawTransactionDataBakEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateRawTransactionDataBak(Long id, RawTransactionDataBakDTO dto) {
        RawTransactionDataBakEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public RawTransactionDataBakDTO getRawTransactionDataBakDTOById(Long id) {
        RawTransactionDataBakEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, RawTransactionDataBakDTO.class);
    }

    @Override
    public IPage<RawTransactionDataBakVO> selectPage(RawTransactionDataBakQueryDTO queryDTO) {
        LambdaQueryWrapper<RawTransactionDataBakEntity> queryWrapper = Wrappers.<RawTransactionDataBakEntity>lambdaQuery();
        //这里注入查询条件
        IPage<RawTransactionDataBakEntity> entityIPage = rawTransactionDataBakMapper.selectPage(new Page<RawTransactionDataBakEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, RawTransactionDataBakVO.class);
    }

    @Override
    @Async
    public Boolean batchRunVoucher(String sceneCodes, String systemCode) {
        log.info("按系统开始执行， systemCode:{}, sceneCodes:{}", systemCode, sceneCodes);
        fieldMappingService.cleanCache();
        List<String> sceneCodeList = StrUtil.split(sceneCodes, "|");
        for (String sceneCode: sceneCodeList){
            try {
                List<RawTransactionDataBakEntity> entityList = this.list(Wrappers.<RawTransactionDataBakEntity>lambdaQuery()
                        .eq(RawTransactionDataBakEntity::getMessageStatus, RawMessageStatusEnum.NOT_EXECUTE.getCode())
                        .eq(RawTransactionDataBakEntity::getSceneCode, sceneCode)
                        .eq(RawTransactionDataBakEntity::getSystemCode, systemCode)
                        .orderByAsc(RawTransactionDataBakEntity::getCreateTime));
                log.info("单场景开始执行， systemCode:{}, sceneCode:{}, size:{}", systemCode, sceneCode, entityList.size());
                for (RawTransactionDataBakEntity entity: entityList){
                    JSONObject jsonObject = entity.getMessageContent();
                    fieldMappingService.convertDataFromMapping(jsonObject);
                    jsonObject.put("businessCode", "ZLYW");
                    List<VoucherDTO> voucherDTOList = ruleService.executeRule(jsonObject.to(new TypeReference<Map<String, Object>>() {}));
                    if (CollectionUtils.isNotEmpty(voucherDTOList) && CollectionUtils.isNotEmpty(voucherDTOList.get(0).getEntryList())){
                        //成功
                        entity.setMessageStatus("SUCCESS");
                    }else{
                        entity.setMessageStatus("FAILED");
                    }
                    entity.updateById();
                }
                log.info("单场景执行完成， systemCode:{}, sceneCode:{}, size:{}", systemCode, sceneCode, entityList.size());
            }catch (Exception e){
                log.error("单场景执行异常, systemCode:{}, sceneCode:{}", systemCode, sceneCode, e);
            }
        }
        log.info("按系统执行完成， systemCode:{}, sceneCodes:{}", systemCode, sceneCodes);
        return Boolean.TRUE;
    }

}

