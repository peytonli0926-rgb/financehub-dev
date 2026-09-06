package com.utfinancing.financehub.etl.financial.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Maps;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.etl.easold.model.dto.EasVoucherDTO;
import com.utfinancing.financehub.etl.enums.Eas2SystemCodeSystemEnum;
import com.utfinancing.financehub.etl.financial.entity.VoucherEntity;
import com.utfinancing.financehub.etl.financial.entity.VoucherEntryEntity;
import com.utfinancing.financehub.etl.financial.entity.VoucherToEasResultEntity;
import com.utfinancing.financehub.etl.financial.model.dto.SendEas2ResultQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.SendEas2ResultDTO;
import com.utfinancing.financehub.etl.financial.model.vo.SendEas2ResultVO;
import com.utfinancing.financehub.etl.financial.entity.SendEas2ResultEntity;
import com.utfinancing.financehub.etl.financial.mapper.SendEas2ResultMapper;
import com.utfinancing.financehub.etl.financial.service.ISendEas2ResultService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.etl.financial.service.IVoucherEntryService;
import com.utfinancing.financehub.etl.financial.service.IVoucherService;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @Author : bruyang
 * @Date : Create in 2024-04-10
 * @Description :  SendEas2Result服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class SendEas2ResultServiceImpl extends ServiceImpl<SendEas2ResultMapper, SendEas2ResultEntity> implements ISendEas2ResultService {

    private final SendEas2ResultMapper sendEas2ResultMapper;

    @Resource
    private IVoucherService iVoucherService;

    @Resource
    private IVoucherEntryService iVoucherEntryService;

    @Override
    public Long saveSendEas2Result(SendEas2ResultDTO dto) {
        SendEas2ResultEntity entity = BeanUtil.copyProperties(dto, SendEas2ResultEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateSendEas2Result(Long id, SendEas2ResultDTO dto) {
        SendEas2ResultEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public SendEas2ResultDTO getSendEas2ResultDTOById(Long id) {
        SendEas2ResultEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, SendEas2ResultDTO.class);
    }

    @Override
    public IPage<SendEas2ResultVO> selectPage(SendEas2ResultQueryDTO queryDTO) {
        LambdaQueryWrapper<SendEas2ResultEntity> queryWrapper = Wrappers.<SendEas2ResultEntity>lambdaQuery();
        //这里注入查询条件
        IPage<SendEas2ResultEntity> entityIPage = sendEas2ResultMapper.selectPage(new Page<SendEas2ResultEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, SendEas2ResultVO.class);
    }

    @Async
    @Override
    public void saveFidSendEas2Result(List<EasVoucherDTO> easVoucherDTOList, Map resultMap,String systemCode,String batchUuid) {
        Map sucs = (Map) resultMap.get("sucs");
        Map error = (Map) resultMap.get("errs");
        int i=2;
        List<SendEas2ResultEntity> resultEntityList = Lists.newArrayList();
        //获取错误的fid
        List<String> errorFidList = Lists.newArrayList();
        for (EasVoucherDTO entity : easVoucherDTOList) {
            if (!error.isEmpty() && error.containsKey(String.valueOf(i))) {
                errorFidList.add(entity.getFid());
            }
            i++;
        }
        //本业务的凭证号和FID的对应关系
        Map<String, List<EasVoucherDTO>> voucherNumFidMap = easVoucherDTOList.stream()
                .collect(Collectors.groupingBy(
                        EasVoucherDTO::getVoucherNumber));
        //获取成功记录的voucherNum
        List<String> voucherNumList = Lists.newArrayList();
        Map<String,String> voucherNumEasIdMap = Maps.newHashMap();
        if (!sucs.isEmpty()) {
            List<VoucherToEasResultEntity> voucherToEasResultList = new ArrayList<>();
            for(Object key : sucs.keySet()) {
                Map map = (Map) sucs.get(key);
                if (null != map.get("voucherNumber")) {
                    voucherNumList.add(map.get("voucherNumber").toString());
                    String number = map.get("number").toString();
                    voucherNumEasIdMap.put(map.get("voucherNumber").toString(),map.get("id").toString().concat("|").
                            concat(number));
                }
            }
        }
        List<SendEas2ResultEntity> entityList = Lists.newArrayList();
        Map<Long,String> voucherIdEasIdMap = Maps.newHashMap();
        //只记录成功的记录
        for(Map.Entry<String, List<EasVoucherDTO>> entry : voucherNumFidMap.entrySet()) {
             if (voucherNumList.contains(entry.getKey())) {
                 entry.getValue().stream().forEach(v ->{
                     if (!errorFidList.contains(v.getFid())) {
                         SendEas2ResultEntity resultEntity = new SendEas2ResultEntity();
                         resultEntity.setFid(v.getFid());
                         resultEntity.setPrimaryKey(v.getPrimaryKey());
                         resultEntity.setBatchUuid(batchUuid);
                         resultEntity.setIsSuccess("sucs");
                         resultEntity.setSystemCode(systemCode);
                         String easVoucherId = voucherNumEasIdMap.get(v.getVoucherNumber());
                         if ((systemCode.equals(Eas2SystemCodeSystemEnum.FINHUB.getCode())
                                 || systemCode.equals(Eas2SystemCodeSystemEnum.FINHUB_ENTRY.getCode()))
                                 && StringUtils.isNotEmpty(resultEntity.getPrimaryKey())) {
                             List<Long> voucherIdList= Arrays.stream(resultEntity.getPrimaryKey().split(",")).map(Long::parseLong).collect(Collectors.toList());
                             voucherIdList.forEach(u -> {
                                 voucherIdEasIdMap.put(u,easVoucherId);
                             });
                         }
                         entityList.add(resultEntity);
                     }
                 });
             } else {
                 // 失败的数据更新状态为0
//                 entry.getValue().stream().forEach(v ->{
//                     List<Long> voucherIdList= Arrays.stream(v.getPrimaryKey().split(",")).
//                             map(Long::parseLong).collect(Collectors.toList());
//                     if (systemCode.equals(Eas2SystemCodeSystemEnum.FINHUB.getCode()) && !voucherIdList.isEmpty()) {
//                         iVoucherService.lambdaUpdate().
//                                 set(VoucherEntity::getIsSendKingdee, "0").
//                                 in(VoucherEntity::getId,voucherIdList).update();
//                     } else if (systemCode.equals(Eas2SystemCodeSystemEnum.FINHUB_ENTRY.getCode()) && !voucherIdList.isEmpty()) {
//                         iVoucherEntryService.lambdaUpdate().
//                                 set(VoucherEntryEntity::getIsSendKingdee,"0").
//                                 in(VoucherEntryEntity::getId, voucherIdList).update();
//                     }
//                 });
             }
        }
        log.info("保存数据条数：{}",entityList.size());
        saveBatch(entityList);
        //异步更新数据中台数据需要更新凭证表状态为已传送金蝶
        log.info("更新中台凭证id数量：{}",voucherIdEasIdMap.size());
        if (systemCode.equals(Eas2SystemCodeSystemEnum.FINHUB.getCode()) && !voucherIdEasIdMap.isEmpty()) {
           log.info("更新中台凭证表开始，更新数量：{}",voucherIdEasIdMap.size());
            CompletableFuture.runAsync(() -> {
                for (Map.Entry<Long, String> entry : voucherIdEasIdMap.entrySet()) {
                    String[] easArrays = entry.getValue().split("\\|");//easVoucherId|voucherNumber
                    iVoucherService.lambdaUpdate().set(VoucherEntity::getVoucherStatus,"4").
                            set(VoucherEntity::getEasVoucherId,easArrays[0]).
                            set(VoucherEntity::getEasVoucherNumber,easArrays[1]).
                            set(VoucherEntity::getIsSendKingdee, "1").
                            eq(VoucherEntity::getId,entry.getKey()).update();
                }

            });
        } else if (systemCode.equals(Eas2SystemCodeSystemEnum.FINHUB_ENTRY.getCode()) && !voucherIdEasIdMap.isEmpty()) {
            log.info("更新中台凭证分录开始，更新数量：{}",voucherIdEasIdMap.size());
            CompletableFuture.runAsync(() -> {
                for (Map.Entry<Long, String> entry : voucherIdEasIdMap.entrySet()) {
                    String[] easArrays = entry.getValue().split("\\|");//easVoucherId|voucherNumber
                    iVoucherEntryService.lambdaUpdate().set(VoucherEntryEntity::getIsSendKingdee,"1").
                            set(VoucherEntryEntity::getEasVoucherId,easArrays[0]).
                            set(VoucherEntryEntity::getEasVoucherNumber,easArrays[1]).
                            eq(VoucherEntryEntity::getId,entry.getKey()).update();
                }
            });
        }
    }

}

