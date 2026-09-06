package com.utfinancing.financehub.engine.rule.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.config.RabbitmqConfig;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherDTO;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherDetailDTO;
import com.utfinancing.financehub.engine.finance.service.ICloseAccountService;
import com.utfinancing.financehub.engine.model.vo.VoucherVO;
import com.utfinancing.financehub.engine.rule.model.dto.GenerateVoucherReqDTO;
import com.utfinancing.financehub.engine.rule.model.dto.ValidateSyntaxReqDTO;
import com.utfinancing.financehub.engine.rule.model.vo.VoucherInfoVO;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import com.utfinancing.financehub.engine.scene.model.dto.SceneRuleDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Api(tags = "业务数据接口")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/rule")
public class RuleController {

    private final IRuleService ruleService;
    private final RabbitTemplate rabbitTemplate;
    private final ICloseAccountService closeAccountService;

    @PostMapping("/execute")
    @ApiOperation(value = "发送转换后数据")
    public R<List<VoucherDTO>> execute(@Valid @RequestBody Map<String, Object> dataMap) {
        List<VoucherDTO> voucherDTOList = ruleService.executeRule(dataMap);
        return R.ok(voucherDTOList);
    }


    @PostMapping("/executeRawData")
    @ApiOperation(value = "发送原始数据")
    public R<Boolean> executeRawData(@Valid @RequestBody JSONObject jsonObject) {
        ruleService.execute(UUID.fastUUID().toString(true), jsonObject);
        return R.ok();
    }


    @PostMapping("/generateVoucher")
    @ApiOperation(value = "生成凭证-规则测试")
    public R<List<VoucherDetailDTO>> generateVoucher(@Valid @RequestBody GenerateVoucherReqDTO reqDTO) {
        List<VoucherDetailDTO> voucherDetailDTOList = ruleService.generateVoucher(reqDTO.getParam());
        return R.ok(voucherDetailDTOList);
    }

    @PostMapping("/validateSyntax")
    @ApiOperation(value = "语法校验")
    public R<Boolean> validateSyntax(@Valid @RequestBody ValidateSyntaxReqDTO reqDTO) {
        return R.ok(ruleService.validateSyntax(reqDTO.getScript()));
    }


    @PostMapping("/publishMQTransaction")
    @ApiOperation(value = "推送租赁交易数据")
    public R<SceneRuleDTO> publishMQTransaction(@Valid @RequestBody Map<String, Object> dataMap) {
        log.info("mq producer send message:{}", JSONObject.toJSONString(dataMap));
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_DIRECT_TRANSACTION_DATA, RabbitmqConfig.ROUTINGKEY_TRANSACTION_DATA, JSONObject.toJSONString(dataMap));
        return R.ok();
    }


    @PostMapping("/publishRepaymentPlan")
    @ApiOperation(value = "推送偿还计划数据")
    public R<SceneRuleDTO> publishRepaymentPlan(@Valid @RequestBody Map<String, Object> dataMap) {
        log.info("mq producer send message:{}", JSONObject.toJSONString(dataMap));
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_DIRECT_TRANSACTION_DATA, RabbitmqConfig.ROUTINGKEY_REPAYMENT_PLAN_DATA, JSONObject.toJSONString(dataMap));
        return R.ok();
    }

    @PostMapping("/executeRule")
    @ApiOperation(value = "生成凭证")
    public R<List<VoucherVO>> executeRule(@Valid @RequestBody Map<String, Object> dataMap) {
        List<VoucherDTO> voucherDTOList = ruleService.executeRule(dataMap);
        return R.ok(BeanUtil.copyToList(voucherDTOList, VoucherVO.class));
    }

    @PostMapping("/batchExecuteRule")
    @ApiOperation(value = "批量生成凭证")
    public R<List<VoucherInfoVO>> batchExecuteRule(@Valid @RequestBody List<Map<String, Object>> dataMap) {
        List<VoucherInfoVO> voucherMap = ruleService.batchExecuteRule(dataMap);
        return R.ok(voucherMap);
    }

    @PostMapping("/queryCurrentPeriodCode")
    @ApiOperation(value = "查询当前会计期间")
    public R<Integer> queryCurrentPeriodCode(@RequestParam String systemCode){
        return R.ok(closeAccountService.queryCurrentPeriodCode(systemCode));
    }
}
