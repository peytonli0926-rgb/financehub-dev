package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.nacos.common.utils.StringUtils;
import com.utfinancing.financehub.common.core.constant.CacheConstants;
import com.utfinancing.financehub.common.redis.service.RedisService;
import com.utfinancing.financehub.engine.enums.DRCREnum;
import com.utfinancing.financehub.engine.enums.SceneEnum;
import com.utfinancing.financehub.engine.enums.SystemEnum;
import com.utfinancing.financehub.engine.enums.VoucherValidFlagEnum;
import com.utfinancing.financehub.engine.enums.VoucherWayEnum;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherEntrySaveDTO;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherSaveDTO;
import com.utfinancing.financehub.engine.finance.service.IVoucherExtService;
import com.utfinancing.financehub.engine.rule.model.dto.InterfaceDataDTO;
import com.utfinancing.financehub.engine.rule.service.IPeriodCodeService;
import com.utfinancing.financehub.engine.scene.model.dto.AccountDTO;
import com.utfinancing.financehub.engine.scene.model.dto.SceneRuleDTO;
import com.utfinancing.financehub.engine.scene.model.dto.SceneVoucherConditionDTO;
import com.utfinancing.financehub.engine.scene.model.dto.SceneVoucherEntryDTO;
import com.utfinancing.financehub.engine.scene.service.IAccountService;
import com.utfinancing.financehub.engine.utils.AssistFlagUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class VoucherExtServiceImpl implements IVoucherExtService {

    private final IPeriodCodeService periodCodeService;
    private final RedisService redisService;

    private final IAccountService accountService;
    //过期时间：1个月
    private static final long REDIS_VOUCHER_NUM_EXPIRE = 3600L * 24 * 30;

    @Override
    public VoucherSaveDTO generateVoucherFromRule(SceneRuleDTO ruleDTO, InterfaceDataDTO interfaceDataDTO) {
        VoucherSaveDTO voucherSaveDTO = new VoucherSaveDTO();
        BeanUtil.copyProperties(interfaceDataDTO, voucherSaveDTO);
        voucherSaveDTO.setValidFlag(VoucherValidFlagEnum.VALID.getCode());
        voucherSaveDTO.setInterfaceDataId(interfaceDataDTO.getId());
        voucherSaveDTO.setSource(ruleDTO.getSource());//来源
        voucherSaveDTO.setVoucherType(ruleDTO.getVoucherType());//凭证类型
        voucherSaveDTO.setBusinessDate(DateUtil.toLocalDateTime(DateUtil.parse(ruleDTO.getBusinessDate())));//业务日期
        voucherSaveDTO.setCurrency(ruleDTO.getCurrency());//币种
        voucherSaveDTO.setDeptName(ruleDTO.getDeptName());//部门
        voucherSaveDTO.setVoucherSummary(ruleDTO.getVoucherSummary());//凭证摘要
        voucherSaveDTO.setSignCompany(ruleDTO.getCompany()); //公司
        voucherSaveDTO.setVoucherWay(VoucherWayEnum.AUTO.getCode());//自动生成
        voucherSaveDTO.setSubSceneType(ruleDTO.getSubSceneType());
        voucherSaveDTO.setOrgId(ruleDTO.getCompany()); //凭证的签约主体为规则配置的签约主体

        LocalDate voucherDate = periodCodeService.generateVoucherDate(interfaceDataDTO);
        voucherSaveDTO.setVoucherDate(voucherDate.atStartOfDay());//凭证日期
        voucherSaveDTO.setPeriodCode(NumberUtil.parseInt(LocalDateTimeUtil.format(voucherDate, "yyyyMM")));
        voucherSaveDTO.setVoucherNum(generateVoucherNum(ruleDTO.getVoucherType(), voucherDate.atStartOfDay()));

        List<VoucherEntrySaveDTO> entryList = new ArrayList<>();

        //凭证行
        List<SceneVoucherEntryDTO> sceneVoucherEntryDTOList = ruleDTO.getEntryList();
        for (SceneVoucherEntryDTO sceneVoucherEntryDTO: sceneVoucherEntryDTOList){
            for (SceneVoucherConditionDTO sceneVoucherConditionDTO: sceneVoucherEntryDTO.getConditionList()){
                boolean condition = BooleanUtil.toBoolean(sceneVoucherConditionDTO.getScriptCondition());
                if (condition){
                    //条件成立，则生成凭证行
                    VoucherEntrySaveDTO entrySaveDTO = mappingVoucherEntry(sceneVoucherEntryDTO, sceneVoucherConditionDTO, interfaceDataDTO);
                    entryList.add(entrySaveDTO);
                }
            }
        }
        voucherSaveDTO.setEntryList(entryList);
        return voucherSaveDTO;
    }


    private VoucherEntrySaveDTO mappingVoucherEntry(SceneVoucherEntryDTO sceneVoucherEntryDTO,
                                                    SceneVoucherConditionDTO sceneVoucherConditionDTO,
                                                    InterfaceDataDTO interfaceDataDTO) {
        VoucherEntrySaveDTO entrySaveDTO = new VoucherEntrySaveDTO();
        entrySaveDTO.setFundType(sceneVoucherEntryDTO.getFundType());
        entrySaveDTO.setRelateBankFlag(sceneVoucherEntryDTO.getRelateBankFlag());
        entrySaveDTO.setBankAccount(interfaceDataDTO.getBankNo());
        entrySaveDTO.setCashAttribute(sceneVoucherEntryDTO.getCashAttribute());
        entrySaveDTO.setVoucherSummary(sceneVoucherEntryDTO.getVoucherSummary());
        entrySaveDTO.setAssistFlags(sceneVoucherEntryDTO.getAssistFlags());
        BigDecimal voucherAmount = StringUtils.isNotEmpty(sceneVoucherConditionDTO.getScriptAmount()) ? new BigDecimal(sceneVoucherConditionDTO.getScriptAmount()) : BigDecimal.ZERO;
        if (DRCREnum.DR.getCode().equals(sceneVoucherConditionDTO.getDebitCreditType())){
            entrySaveDTO.setDebitAmount(voucherAmount);
        } else if (DRCREnum.CR.getCode().equals(sceneVoucherConditionDTO.getDebitCreditType())){
            entrySaveDTO.setCreditAmount(voucherAmount);
        }
        if (AssistFlagUtil.hasClientFlag(sceneVoucherEntryDTO.getAssistFlags())){
            entrySaveDTO.setClientCode(interfaceDataDTO.getClientCode());
        }
        if (AssistFlagUtil.hasContractFlag(sceneVoucherEntryDTO.getAssistFlags())){
            entrySaveDTO.setContractCode(interfaceDataDTO.getContractCode());
        }
        if (AssistFlagUtil.hasBillContractFlag(sceneVoucherEntryDTO.getAssistFlags())){
            entrySaveDTO.setBillContractCode(interfaceDataDTO.getBillContractCode());
        }
        entrySaveDTO.setDebitCreditType(sceneVoucherConditionDTO.getDebitCreditType());
        //查询科目编码及科目名称
        String fundType = sceneVoucherEntryDTO.getFundType();
        AccountDTO accountDTO;
        if (SceneEnum.HTQZ.getCode().equals(interfaceDataDTO.getSceneCode())) {
            accountDTO = accountService.getAccountByFundTypeFromRedisStrict(
                    fundType, interfaceDataDTO.getAccountingBusinessCode());
        } else {
            String accountBusinessCode = StringUtils.isNotEmpty(interfaceDataDTO.getAccountingBusinessCode())
                    ? interfaceDataDTO.getAccountingBusinessCode() : interfaceDataDTO.getBusinessCode();
            accountDTO = accountService.getAccountByFundTypeFromRedis(accountBusinessCode, fundType);
        }

        entrySaveDTO.setAccountCode(accountDTO.getAccountCode());
        entrySaveDTO.setAccountName(accountDTO.getAccountName());
        return entrySaveDTO;
    }

    private long generateVoucherNum(String voucherType, LocalDateTime dateTime){
        //redis key : finhub-年月-凭证类型编码
        String key = CacheConstants.COMMON_PREFIX+"_voucher_num_"+dateTime.getYear()+dateTime.getMonthValue()+"_"+voucherType;
        return redisService.generate(key, REDIS_VOUCHER_NUM_EXPIRE);
    }
}
