package com.utfinancing.financehub.engine.rule.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.BeanUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.utfinancing.financehub.admin.api.RemoteDictService;
import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.common.core.constant.HttpStatus;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.redis.service.RedisService;
import com.utfinancing.financehub.engine.constants.RedisConstant;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.entity.ClientEntity;
import com.utfinancing.financehub.engine.finance.entity.ContractBalanceLatestEntity;
import com.utfinancing.financehub.engine.finance.model.dto.ContractDTO;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherDTO;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherEntrySaveDTO;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherSaveDTO;
import com.utfinancing.financehub.engine.finance.service.*;
import com.utfinancing.financehub.engine.rule.constant.RuleConstant;
import com.utfinancing.financehub.engine.rule.model.dto.InterfaceDataDTO;
import com.utfinancing.financehub.engine.rule.model.vo.VoucherInfoVO;
import com.utfinancing.financehub.engine.rule.service.IInterfaceDataService;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import com.utfinancing.financehub.engine.rule.util.RuleUtil;
import com.utfinancing.financehub.engine.scene.entity.AccountEntity;
import com.utfinancing.financehub.engine.scene.model.dto.BusinessDTO;
import com.utfinancing.financehub.engine.scene.model.dto.SceneDTO;
import com.utfinancing.financehub.engine.scene.model.dto.SceneRuleDTO;
import com.utfinancing.financehub.engine.scene.model.dto.TaxRateDTO;
import com.utfinancing.financehub.engine.scene.model.vo.BusinessVO;
import com.utfinancing.financehub.engine.scene.service.IAccountService;
import com.utfinancing.financehub.engine.scene.service.ISceneVoucherEntryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BatchRuleServiceImpl implements BatchRuleService {

    private final IRuleService ruleService;

    private final IInterfaceDataService interfaceDataService;

    private final IContractBalanceLatestService contractBalanceLatestService;

    private final IVoucherExtService voucherExtService;

    private final IAccountService accountService;
    private final IVoucherService voucherService;
    private final IContractBalanceService contractBalanceService;

    public List<VoucherInfoVO> executeRule(List<Map<String, Object>> voucherMapList, Map<String, Object> params) {
        List<VoucherInfoVO> result = new ArrayList<>();

        // 传递参数取得
        List<String> assistFlagsList = (List<String>) params.get("assistFlagsList");
        Map<String, ContractDTO> contractMap = (Map<String, ContractDTO>) params.get("contractMap");
        Map<String, ContractBalanceLatestEntity> contractBalanceLatestMap =
                (Map<String, ContractBalanceLatestEntity>) params.get("contractBalanceLatestEntityMap");
        Map<String, ClientEntity> clientEntitylist = (Map<String, ClientEntity>) params.get("clientEntitylist");

        for (int i = 0; i < voucherMapList.size(); i++) {
            Map<String, Object> dataMap = voucherMapList.get(i);

            // 结果对象
            VoucherInfoVO voucherInfoVO = new VoucherInfoVO();
            voucherInfoVO.setVoucherDTOList(new ArrayList<>());
            String orderId = MapUtil.getStr(dataMap, RuleConstant.FIELD_ORDER_ID);
            voucherInfoVO.setOrderId(orderId);

            //必填参数校验
            ruleService.validateRequiredField(dataMap);

            //获取场景编码
            String sceneCode = MapUtil.getStr(dataMap, RuleConstant.FIELD_SCENE_CODE);
            //缓存
            SceneDTO sceneDTO = ruleService.getRedisSceneDTOByCode(sceneCode);

            if (sceneDTO == null) {
                throw new ServiceException(StrUtil.format("场景[{}]不存在", sceneCode));
            }
            dataMap.put(RuleConstant.FIELD_SCENE_NAME, sceneDTO.getSceneName());

            // 取得余额信息
            Map<String, Object> lastBalanceMap = this.queryFullLastBalanceMap(dataMap, assistFlagsList, contractBalanceLatestMap);
            if (MapUtil.isNotEmpty(lastBalanceMap)){
                dataMap.putAll(lastBalanceMap);
            }

            // 取得合同信息
            String contractCode = MapUtil.getStr(dataMap, RuleConstant.FIELD_CONTRACT_CODE);
            String orgId = MapUtil.getStr(dataMap, RuleConstant.FIELD_ORG_ID);
            Map<String, Object> contractValueMap = getContractMap(contractMap, contractCode, orgId);
            if (MapUtil.isNotEmpty(contractValueMap)){
                dataMap.putAll(contractValueMap);
            }

            // 客户信息
            String clientCode = MapUtil.getStr(dataMap, RuleConstant.FIELD_CLIENT_CODE);
            Map<String, Object> clientMap = getClientMap(clientEntitylist, clientCode);
            if (MapUtil.isNotEmpty(clientMap)){
                dataMap.putAll(clientMap);
            }


            List<VoucherDTO> voucherDTOList = executeSingleInterface(sceneCode, dataMap, params);
            voucherInfoVO.getVoucherDTOList().addAll(voucherDTOList);
            result.add(voucherInfoVO);
        }
        return result;
    }

    private List<VoucherDTO> executeSingleInterface(String sceneCode, Map<String, Object> dataMap,
                                                    Map<String, Object> params){

        List<VoucherDTO> voucherDTOList = new ArrayList<>();

        String businessCode = MapUtil.getStr(dataMap, RuleConstant.FIELD_BUSINESS_CODE);
        String systemCode = MapUtil.getStr(dataMap, RuleConstant.FIELD_SYSTEM_CODE);
        if (businessCode == null){
            throw new ServiceException("业务编码[businessCode]不能为空");
        }

        BusinessDTO businessDTO = ruleService.getRedisBusinessByCode(businessCode);
        if (businessDTO == null){
            throw new ServiceException(StrUtil.format("业务编码[{}]不存在", businessCode));
        }
        String bankNo = "";
        String transactionType = "";

        //注入默认金额0
        ruleService.injectDefaultNumberFields(sceneCode, dataMap);

        //去掉空字符串的字段
        ruleService.cleanStringEmptyFields(dataMap);

        //业务编码
        List<BusinessVO> allBusinessList = ruleService.selectRedisBusinessAll();
        allBusinessList.forEach(e -> dataMap.put(e.getBusinessCode(), e.getBusinessCode()));

        //保存关联数据
        InterfaceDataDTO interfaceDataDTO = saveInterfaceData(dataMap);
        if (StringUtils.isNotEmpty(bankNo)) {
            interfaceDataDTO.setBankNo(bankNo);
        }

        //税率
        List<TaxRateDTO> taxRateDTOList = ruleService.queryRedisAllForEditor();
        for (TaxRateDTO taxRateDTO: taxRateDTOList){
            String name = "税率." + taxRateDTO.getBusinessCode()+"."+taxRateDTO.getFundType()+"."+taxRateDTO.getLeaseType();
            if (StrUtil.isNotBlank(taxRateDTO.getLeaseSubType())){
                name = name + "." + taxRateDTO.getLeaseSubType();
            }
            dataMap.put(name, taxRateDTO.getTaxRate());
        }

        //获取翻译后的规则
        List<SceneRuleDTO> ruleDTOList = getTranslateRule(sceneCode, interfaceDataDTO, dataMap, params);

        for (SceneRuleDTO ruleDTO: ruleDTOList){
            //执行规则，返回执行后的规则对象
            SceneRuleDTO resultRule = RuleUtil.executeRule(ruleDTO, dataMap);
//            log.info("规则执行结果对象: {}", JSONUtil.toJsonStr(resultRule));
            boolean ruleCondition = BooleanUtil.toBoolean(resultRule.getScriptCondition());
            if (ruleCondition){
                //根据规则执行结果生成凭证
                VoucherSaveDTO voucherSaveDTO = voucherExtService.generateVoucherFromRule(resultRule, interfaceDataDTO);
                //校验凭证是否配平
                validateVoucher(voucherSaveDTO);

                //入库
                VoucherDTO voucherDTO = voucherService.saveVoucherAndEntries(voucherSaveDTO);
                voucherDTOList.add(voucherDTO);


                if (!StrUtil.equals(voucherSaveDTO.getValidFlag(), VoucherValidFlagEnum.VALID.getCode())){
                    //如果凭证无效，则直接跳过，不扣减余额
                    continue;
                }

                //保存/更新合同余额表
                voucherDTO.setIsSubmit(MapUtil.getStr(dataMap, RuleConstant.IS_SUBMIT));
                contractBalanceService.saveContractBalanceFromVoucher(voucherDTO);
            }
        }
        return voucherDTOList;
    }

    private Map<String, Object> getClientMap(Map<String, ClientEntity> clientMap, String clientCode) {
        LambdaQueryWrapper<ClientEntity> lambdaQueryWrapper = Wrappers.<ClientEntity>lambdaQuery();
        lambdaQueryWrapper.eq(ClientEntity::getClientCode, clientCode);
        ClientEntity entity = clientMap.get(clientCode);
        if (entity == null) {
            return null;
        }
        Map<String, Object> oldMap = JSONObject.parseObject(JSONObject.toJSONString(entity), new TypeReference<Map<String, Object>>() {
        });
        Map<String, Object> newMap = new HashMap<>();
        Iterator<Map.Entry<String, Object>> it = oldMap.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String, Object> entry = it.next();
            newMap.put("client_" + entry.getKey(), entry.getValue());
        }
        return newMap;
    }

    private Map<String, Object> getContractMap(Map<String, ContractDTO> contractMap, String contractCode, String orgId) {
        ContractDTO contractEntity = contractMap.get(contractCode.concat("|").concat(
                StringUtils.isNotEmpty(orgId) ? orgId : ""));
        if (contractEntity == null) {
            return null;
        }
        Map<String, Object> oldMap = JSONObject.parseObject(JSONObject.toJSONString(contractEntity), new TypeReference<Map<String, Object>>() {
        });
        Map<String, Object> newMap = new HashMap<>();
        Iterator<Map.Entry<String, Object>> it = oldMap.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String, Object> entry = it.next();
            newMap.put("contract_" + entry.getKey(), entry.getValue());
        }
        return newMap;
    }

    private Map<String, Object> queryFullLastBalanceMap(Map<String, Object> dataMap, List<String> assistFlagsList,
                                                        Map<String, ContractBalanceLatestEntity> contractBalanceLatestMap) {
        Map<String, Object> fullBalanceMap = new HashMap<>();

        String businessCode = MapUtil.getStr(dataMap, RuleConstant.FIELD_BUSINESS_CODE);
        String clientCode = MapUtil.getStr(dataMap, RuleConstant.FIELD_CLIENT_CODE);
        String contractCode = MapUtil.getStr(dataMap, RuleConstant.FIELD_CONTRACT_CODE);
        Map<String, Object> defaultBalanceMap = this.getLastBalanceMap(businessCode, clientCode, contractCode, contractBalanceLatestMap);
        fullBalanceMap.putAll(defaultBalanceMap);
        //2.查询维度余额：根据场景的维度配置查询余额
        for (String assistFlag: assistFlagsList){
            Map<String, Object> assistBalanceMap = null;
            if (StrUtil.isBlank(assistFlag)){
                assistFlag = "none";
                assistBalanceMap = this.getLastBalanceMap(businessCode, null, null, contractBalanceLatestMap);
            }else{
                List<String> entryAssistsFlag = StrUtil.split(assistFlag, "_");
                String queryContractCode = null;
                String queryClientCode = null;
                if (CollectionUtil.contains(entryAssistsFlag, AssistFlagEnum.CLIENT.getCode())){
                    queryClientCode = clientCode;
                }
                if (CollectionUtil.contains(entryAssistsFlag, AssistFlagEnum.CONTRACT.getCode())){
                    queryContractCode = contractCode;
                }
                assistBalanceMap = getLastBalanceMap(businessCode, queryClientCode, queryContractCode, contractBalanceLatestMap);
            }
            //替换key
            fullBalanceMap.putAll(contractBalanceLatestService.replaceBalanceMapKey(assistBalanceMap, assistFlag));
        }
        return fullBalanceMap;
    }

    private Map<String, Object> getLastBalanceMap(String businessCode, String clientCode, String contractCode,
                                                  Map<String, ContractBalanceLatestEntity> contractBalanceLatestMap) {
        Map<String, Object> rowMap = new HashMap<>();

        String contractBalanceLatestBusKey = this.getContractBalanceLatestBusKey(businessCode, clientCode, contractCode);
        ContractBalanceLatestEntity contractBalanceLatestEntity = contractBalanceLatestMap.get(contractBalanceLatestBusKey);
        if (contractBalanceLatestEntity == null) {
            rowMap.put(ContractBalanceColumnsEnum.BUSINESS_CODE.getCode(), businessCode);
            rowMap.put(ContractBalanceColumnsEnum.CLIENT_CODE.getCode(), clientCode);
            rowMap.put(ContractBalanceColumnsEnum.CONTRACT_CODE.getCode(), contractCode);
            //其余余额字段补0
            contractBalanceLatestService.fillBalanceZero(rowMap);
            return rowMap;
        } else {
            rowMap.putAll(BeanUtils.beanToMap(contractBalanceLatestEntity));
        }

        //过滤发生额和其他字段，只保留余额字段
        for (Iterator<Map.Entry<String, Object>> it = rowMap.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, Object> item = it.next();
            if (!StrUtil.containsAny(item.getKey(), "_balance") && !StrUtil.equals(item.getKey(), "id")) {
                it.remove();
            }
        }
        rowMap.put(ContractBalanceColumnsEnum.BUSINESS_CODE.getCode(), businessCode);
        rowMap.put(ContractBalanceColumnsEnum.CLIENT_CODE.getCode(), clientCode);
        rowMap.put(ContractBalanceColumnsEnum.CONTRACT_CODE.getCode(), contractCode);
        return rowMap;
    }

    private String getContractBalanceLatestBusKey(String businessCode, String clientCode, String contractCode) {
        ContractBalanceLatestEntity entity = new ContractBalanceLatestEntity();
        entity.setContractCode(businessCode);
        entity.setBusinessCode(clientCode);
        entity.setClientCode(contractCode);
        return contractBalanceLatestService.getBusKey(entity);
    }

    private InterfaceDataDTO saveInterfaceData(Map<String, Object> dataMap) {
        //保存接口数据
        InterfaceDataDTO interfaceDataDTO = interfaceDataService.saveInterfaceDataFromMap(dataMap);
        return interfaceDataDTO;
    }


    public List<SceneRuleDTO> getTranslateRule(String sceneCode, InterfaceDataDTO interfaceDataDTO,
                                               Map<String, Object> dataMap, Map<String, Object> params) {
        //获取原始规则
        List<SceneRuleDTO> ruleDTOList = ruleService.getSceneRuleDTOByCode(sceneCode);

        Map<String, Object> ruleKeyMap = new HashMap<>();
        //接口字段
        Map<String, Object> sceneFieldsMap = (Map<String, Object>) params.get("sceneFieldsMap");
        ruleKeyMap.putAll(sceneFieldsMap);

        //金额类型参数
        R<List<SysDictData>> dictListR =  ruleService.listDictTypeData(DictTypeEnum.CASH_TYPE.getCode());
        if (dictListR.getCode() == HttpStatus.SUCCESS && CollectionUtil.isNotEmpty(dictListR.getData())) {
            for (SysDictData dictData : dictListR.getData()) {
                ruleKeyMap.put("金额类型参数表." + dictData.getDictLabel(), dictData.getDictValue());
                ruleKeyMap.put(dictData.getDictValue(), dictData.getDictValue());
            }
        }

        //合同余额表
        List<String> assistFlags = (List<String>) params.get("assistFlagsList");
        R<List<SysDictData>> contractBalanceListR = ruleService.listDictTypeData(DictTypeEnum.CASH_TYPE.getCode());
        if (contractBalanceListR.getCode() == HttpStatus.SUCCESS && CollectionUtil.isNotEmpty(contractBalanceListR.getData())) {
            for (String assistFlag: assistFlags){
                //维度合同余额
                if (StrUtil.isBlank(assistFlag)){
                    assistFlag = "none";
                }
                for (SysDictData dictData : contractBalanceListR.getData()) {
                    ruleKeyMap.put("科目余额表" + assistFlag + "." + dictData.getDictLabel() + "余额", dictData.getDictValue() + "_balance_"+assistFlag);
                }
            }
            //默认合同余额
            for (SysDictData dictData : contractBalanceListR.getData()) {
                ruleKeyMap.put("科目余额表." + dictData.getDictLabel() + "余额", dictData.getDictValue() + "_balance");
            }
        }

        //合同表
        R<List<SysDictData>> contractListR = ruleService.listDictTypeData(DictTypeEnum.CONTRACT_FIELDS.getCode());
        if (contractListR.getCode() == HttpStatus.SUCCESS && CollectionUtil.isNotEmpty(contractListR.getData())) {
            for (SysDictData dictData : contractListR.getData()) {
                ruleKeyMap.put("合同表." + dictData.getDictLabel(), "contract_" + dictData.getDictValue());
            }
        }

        //客户表
        R<List<SysDictData>> clientListR = ruleService.listDictTypeData(DictTypeEnum.CLIENT_FIELDS.getCode());
        if (clientListR.getCode() == HttpStatus.SUCCESS && CollectionUtil.isNotEmpty(clientListR.getData())) {
            for (SysDictData dictData : clientListR.getData()) {
                ruleKeyMap.put("客户表." + dictData.getDictLabel(), "client_" + dictData.getDictValue());
            }
        }

        //税率
        List<TaxRateDTO> taxRateDTOList = ruleService.queryRedisAllForEditor();
        for (TaxRateDTO taxRateDTO: taxRateDTOList){
            String name = "税率." + taxRateDTO.getBusinessCode()+"."+taxRateDTO.getFundType()+"."+taxRateDTO.getLeaseType();
            if (StrUtil.isNotBlank(taxRateDTO.getLeaseSubType())){
                name = name + "." + taxRateDTO.getLeaseSubType();
            }
            ruleKeyMap.put(name, name);
        }

        for (SceneRuleDTO ruleDTO: ruleDTOList){
            //翻译规则
            RuleUtil.translateRule(ruleDTO, ruleKeyMap, dataMap);
        }
        return ruleDTOList;
    }


    private void validateVoucher(VoucherSaveDTO voucherSaveDTO){
        List<VoucherEntrySaveDTO> allEntryList = voucherSaveDTO.getEntryList();
        if (CollectionUtil.isEmpty(allEntryList)){
            voucherSaveDTO.setValidFlag(VoucherValidFlagEnum.ENTRY_EMPTY.getCode());
            return;
        }

        Map<String, AccountEntity> accountEntityMap = accountService.getAccountEntityMapFromRedis();

        allEntryList.stream().forEach(v -> {
            AccountEntity entity = accountEntityMap.get(voucherSaveDTO.getBusinessCode().
                    concat("|").concat(v.getFundType()));

            if (entity != null) {
                v.setSettlementType(entity.getSettlementType());
            }
        });
        List<VoucherEntrySaveDTO> innerEntryList = allEntryList.stream().filter(v -> "intra".equals(v.getSettlementType())).collect(Collectors.toList());
        //校验是否平
        //借方总金额
        BigDecimal debitTotalAmount = innerEntryList.stream().filter(e->StrUtil.equals(DRCREnum.DR.getCode(), e.getDebitCreditType())).map(VoucherEntrySaveDTO::getDebitAmount).reduce(BigDecimal.ZERO, (a, b)-> NumberUtil.add(a, b)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal creditTotalAmount = innerEntryList.stream().filter(e->StrUtil.equals(DRCREnum.CR.getCode(), e.getDebitCreditType())).map(VoucherEntrySaveDTO::getCreditAmount).reduce(BigDecimal.ZERO, (a, b)->NumberUtil.add(a, b)).setScale(2, RoundingMode.HALF_UP);
        if (!NumberUtil.equals(debitTotalAmount, creditTotalAmount)){
            voucherSaveDTO.setValidFlag(VoucherValidFlagEnum.NOT_EQUALS.getCode());
        }
    }
}
