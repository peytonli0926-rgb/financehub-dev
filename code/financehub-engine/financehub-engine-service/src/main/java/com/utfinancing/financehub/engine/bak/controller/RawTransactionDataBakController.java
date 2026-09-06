package com.utfinancing.financehub.engine.bak.controller;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.redis.service.RedisService;
import com.utfinancing.financehub.engine.bak.entity.RawTransactionDataBakEntity;
import com.utfinancing.financehub.engine.bak.model.dto.RawTransactionDataBakQueryDTO;
import com.utfinancing.financehub.engine.bak.model.dto.RawTransactionDataBakDTO;
import com.utfinancing.financehub.engine.bak.model.vo.RawTransactionDataBakVO;
import com.utfinancing.financehub.engine.enums.RawMessageStatusEnum;
import com.utfinancing.financehub.engine.enums.VoucherValidFlagEnum;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherDTO;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import com.utfinancing.financehub.engine.scene.service.IFieldMappingService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.utfinancing.financehub.engine.bak.service.IRawTransactionDataBakService;


/**
 * @Author : lixin
 * @Date : Create in 2023-10-18
 * @Description :   RawTransactionDataBak控制器实现类
 * @Modified :
 */
@Api(tags = "临时测试")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/bak/raw-transaction-data-bak")
public class RawTransactionDataBakController {

    @Autowired
    private IRawTransactionDataBakService rawTransactionDataBakService;

    @Autowired
    private IFieldMappingService fieldMappingService;

    @Autowired
    private IRuleService ruleService;


    @PostMapping("/batchRun")
    @ApiOperation(value = "batchRun")
    public R<Long> save(@RequestParam("pageSize") int pageSize, @RequestParam("sceneCode")String sceneCode, @RequestParam("systemCode")String systemCode) {
        fieldMappingService.cleanCache();
        int page = 1;
        long start = System.currentTimeMillis();
        IPage<RawTransactionDataBakEntity> entityIPage = this.rawTransactionDataBakService.page(new Page<>(page, pageSize), Wrappers.<RawTransactionDataBakEntity>lambdaQuery()
                .eq(RawTransactionDataBakEntity::getMessageStatus, RawMessageStatusEnum.NOT_EXECUTE.getCode())
                .eq(RawTransactionDataBakEntity::getSceneCode, sceneCode)
                .eq(RawTransactionDataBakEntity::getSystemCode, systemCode)
                .orderByAsc(RawTransactionDataBakEntity::getCreateTime));
        int i = 1;
        for (RawTransactionDataBakEntity entity: entityIPage.getRecords()){
            try {
                JSONObject jsonObject = entity.getMessageContent();
                fieldMappingService.convertDataFromMapping(jsonObject);
                jsonObject.put("businessCode", "ZLYW");
                String orderId = "F2_" + jsonObject.getString("orderId");
                jsonObject.put("orderId", orderId);
                jsonObject.put("interfaceId",entity.getId());
                if (StrUtil.isBlank(jsonObject.getString("businessDate"))){
                    jsonObject.put("businessDate", LocalDateTimeUtil.format(entity.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                }
                List<VoucherDTO> voucherDTOList = ruleService.executeRule(jsonObject.to(new TypeReference<Map<String, Object>>() {}));
                if (CollectionUtils.isEmpty(voucherDTOList) || CollectionUtils.isEmpty(voucherDTOList.get(0).getEntryList())){
                    log.info("执行失败, num:{}, sceneCode:{}, orderId:{}", (i+"/"+pageSize), sceneCode, orderId);
                    entity.setErrorInfo("凭证行为空");
                    entity.setMessageStatus("FAILED");
                } else {
                    boolean hasFailed = false;
                    for (VoucherDTO voucherDTO: voucherDTOList){
                        if (StrUtil.equals(VoucherValidFlagEnum.NOT_EQUALS.getCode(), voucherDTO.getValidFlag())){
                            hasFailed = true;
                            break;
                        }
                    }
                    if (hasFailed){
                        log.info("执行失败, num:{}, sceneCode:{}, orderId:{}", (i+"/"+pageSize), sceneCode, orderId);
                        entity.setErrorInfo("借贷金额不平");
                        entity.setMessageStatus("FAILED");
                    } else{
                        log.info("执行成功, num:{}, sceneCode:{}, orderId:{}", (i+"/"+pageSize), sceneCode, orderId);
                        entity.setMessageStatus("SUCCESS");
                    }
                }
            } catch (Exception e){
                entity.setErrorInfo(e.getMessage());
                entity.setMessageStatus("FAILED");
            }finally {
                entity.updateById();
                i++;
            }
        }
        long cost = System.currentTimeMillis() - start;
        System.out.println("end cost: " + cost + "ms.");
        return R.ok(cost);
    }

    @PostMapping("/batchRunVoucher")
    @ApiOperation(value = "批量跑凭证-按系统和场景")
    public R<Boolean> save(@RequestParam("sceneCodes")String sceneCodes, @RequestParam("systemCode")String systemCode){
        return R.ok(rawTransactionDataBakService.batchRunVoucher(sceneCodes, systemCode));
    }
}



