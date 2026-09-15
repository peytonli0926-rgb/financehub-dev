package com.utfinancing.financehub.engine.scene.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.common.redis.service.RedisService;
import com.utfinancing.financehub.engine.constants.RedisConstant;
import com.utfinancing.financehub.engine.enums.AssistFlagEnum;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.entity.BankAccountEntity;
import com.utfinancing.financehub.engine.finance.service.IBankAccountService;
import com.utfinancing.financehub.engine.rule.constant.RuleConstant;
import com.utfinancing.financehub.engine.scene.model.dto.AccountQueryDTO;
import com.utfinancing.financehub.engine.scene.model.dto.AccountDTO;
import com.utfinancing.financehub.engine.scene.model.dto.AccountSaveDTO;
import com.utfinancing.financehub.engine.scene.model.vo.AccountVO;
import com.utfinancing.financehub.engine.scene.entity.AccountEntity;
import com.utfinancing.financehub.engine.scene.mapper.AccountMapper;
import com.utfinancing.financehub.engine.scene.service.IAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author : lixin
 * @Date : Create in 2023-08-25
 * @Description :  Account服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class AccountServiceImpl extends ServiceImpl<AccountMapper, AccountEntity> implements IAccountService {

    private final AccountMapper accountMapper;

    private final IBankAccountService iBankAccountService;
    private final RedisService redisService;

    @Override
    public Long saveAccount(AccountSaveDTO dto) {
        long rows = this.count(Wrappers.<AccountEntity>lambdaQuery()
                .eq(AccountEntity::getBusinessCode, dto.getBusinessCode())
                .eq(AccountEntity::getFundType, dto.getFundType()));
        if (rows > 0){
            throw new ServiceException(StrUtil.format("业务编码：[{}], 金额类型：[{}] 已存在", dto.getBusinessCode(), dto.getFundType()));
        }
        AccountEntity entity = BeanUtil.copyProperties(dto, AccountEntity.class);
        entity.setAssistFlags(dto.getAssistFlags());
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateAccount(Long id, AccountSaveDTO dto) {
        AccountEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        long rows = this.count(Wrappers.<AccountEntity>lambdaQuery()
                .eq(AccountEntity::getBusinessCode, dto.getBusinessCode())
                .eq(AccountEntity::getFundType, dto.getFundType())
                .ne(AccountEntity::getId, id));
        if (rows > 0){
            throw new ServiceException(StrUtil.format("业务编码：[{}], 金额类型：[{}] 已存在", dto.getBusinessCode(), dto.getFundType()));
        }
        entity.setAssistFlags(dto.getAssistFlags());
        entity.updateById();
        return id;
    }

    @Override
    public AccountDTO getAccountDTOById(Long id) {
        AccountEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, AccountDTO.class);
    }

    @Override
    public IPage<AccountVO> selectPage(AccountQueryDTO queryDTO) {
        LambdaQueryWrapper<AccountEntity> queryWrapper = Wrappers.<AccountEntity>lambdaQuery();
        queryWrapper.eq(StringUtils.isNotBlank(queryDTO.getBusinessName()), AccountEntity::getBusinessName, queryDTO.getBusinessName());
        queryWrapper.like(StringUtils.isNotBlank(queryDTO.getAccountCode()), AccountEntity::getAccountCode, queryDTO.getAccountCode());
        queryWrapper.like(StringUtils.isNotBlank(queryDTO.getAccountName()), AccountEntity::getAccountName, queryDTO.getAccountName());
        queryWrapper.eq(StringUtils.isNotBlank(queryDTO.getFundType()), AccountEntity::getFundType, queryDTO.getFundType());
        queryWrapper.eq(StringUtils.isNotBlank(queryDTO.getAccountCategory()), AccountEntity::getAccountCategory, queryDTO.getAccountCategory());
        queryWrapper.eq(StringUtils.isNotBlank(queryDTO.getDebitCreditType()), AccountEntity::getDebitCreditType, queryDTO.getDebitCreditType());
        queryWrapper.eq(StringUtils.isNotBlank(queryDTO.getSettlementType()), AccountEntity::getSettlementType, queryDTO.getSettlementType());
        String assistFlags = "";
        if (CollectionUtils.isNotEmpty(queryDTO.getAssistFlags())) {
            assistFlags = queryDTO.getAssistFlags().stream()
                    .map(s -> "'" + s + "'")
                    .collect(Collectors.joining(", "));
            queryWrapper.apply("STRING_TO_ARRAY(assist_flags, ',') @> ARRAY["+ assistFlags +"]");
        }
        queryWrapper.orderByDesc(AccountEntity::getCreateTime);
        IPage<AccountEntity> entityIPage = accountMapper.selectPage(new Page<AccountEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        IPage<AccountVO> accountVoIPage = ListBeanUtil.copyPage(entityIPage, AccountVO.class);
        if (CollectionUtils.isNotEmpty(entityIPage.getRecords())){
            Map<Long, AccountEntity> entityMap = entityIPage.getRecords().stream().collect(Collectors.toMap(e->e.getId(), e->e));
            for (AccountVO vo: accountVoIPage.getRecords()){
                vo.setAssistFlags(entityMap.get(vo.getId()).getAssistFlags());
            }
        }
        return accountVoIPage;
    }

    public Map<String, AccountEntity> selectAllAccountToRedis() {
        LambdaQueryWrapper<AccountEntity> queryWrapper = Wrappers.<AccountEntity>lambdaQuery();
        queryWrapper.eq(AccountEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        List<AccountEntity> accountEntityList = accountMapper.selectList(queryWrapper);

        if (accountEntityList == null || accountEntityList.isEmpty()) {
            redisService.setCacheObject(RedisConstant.ACCOUNT_DATA, new HashMap<>(), RedisConstant.TIME_OUT, TimeUnit.MINUTES);
            return new HashMap<>();
        } else {
            Map<String, AccountEntity> accountEntityMap = accountEntityList.stream().collect(
                    Collectors.toMap(e->getBusKey(e), (e)->e, (a, b)->b));
            redisService.setCacheObject(RedisConstant.ACCOUNT_DATA, accountEntityMap, RedisConstant.TIME_OUT, TimeUnit.MINUTES);
            return accountEntityMap;
        }
    }

    public Map<String, AccountEntity> getAccountEntityMapFromRedis() {
        Map<String, AccountEntity> accountEntityMap = redisService.getCacheObject(RedisConstant.ACCOUNT_DATA);
        if (accountEntityMap == null || accountEntityMap.keySet().size() == 0) {
            return this.selectAllAccountToRedis();
        } else {
            return accountEntityMap;
        }
    }

    public AccountDTO getAccountByFundTypeFromRedis(String businessCode, String fundType) {
        Map<String, AccountEntity> accountEntityMap = getAccountEntityMapFromRedis();
        AccountEntity accountParam = new AccountEntity();
        accountParam.setBusinessCode(businessCode);
        accountParam.setFundType(fundType);
        AccountEntity entity = accountEntityMap.get(getBusKey(accountParam));

        // Business-specific mappings take precedence. Shared subjects are
        // configured under the default business and act as a common fallback.
        if (entity == null && !RuleConstant.DEFAULT_BUSINESS_CODE.equals(businessCode)) {
            accountParam.setBusinessCode(RuleConstant.DEFAULT_BUSINESS_CODE);
            entity = accountEntityMap.get(getBusKey(accountParam));
        }

        if (entity == null){
            throw new ServiceException(StrUtil.format("科目不存在[{}-{}]", businessCode, fundType));
        }
        return BeanUtil.copyProperties(entity, AccountDTO.class);
    }

    @Override
    public AccountDTO getAccountByFundTypeFromRedisStrict(String fundType, String accountingBusinessCode) {
        if (StrUtil.isBlank(fundType)) {
            throw new ServiceException("金额类型不能为空");
        }
        if (StrUtil.isBlank(accountingBusinessCode)) {
            throw new ServiceException(StrUtil.format("金额类型[{}]缺少核算业务类型，无法匹配科目", fundType));
        }
        AccountEntity accountParam = new AccountEntity();
        accountParam.setFundType(fundType);
        accountParam.setBusinessCode(accountingBusinessCode);
        AccountEntity entity = getAccountEntityMapFromRedis().get(getBusKey(accountParam));
        if (entity == null) {
            throw new ServiceException(StrUtil.format(
                    "科目配置不完整：金额类型[{}]、核算业务类型[{}]未配置对应科目",
                    fundType, accountingBusinessCode));
        }
        return BeanUtil.copyProperties(entity, AccountDTO.class);
    }

    @Override
    public String getFundTypeString(List<String> accountCodeList) {
        return accountMapper.getFundTypeString(accountCodeList);
    }

    private String getBusKey(AccountEntity entity) {
        StringBuffer result = new StringBuffer(entity.getBusinessCode());
        result.append("\\|").append(entity.getFundType());
        return result.toString();
    }

    @Override
    public List<AccountVO> selectList(AccountQueryDTO queryDTO) {
        LambdaQueryWrapper<AccountEntity> queryWrapper = Wrappers.<AccountEntity>lambdaQuery();
        queryWrapper.eq(StringUtils.isNotBlank(queryDTO.getBusinessName()), AccountEntity::getBusinessName, queryDTO.getBusinessName());
        queryWrapper.like(StringUtils.isNotBlank(queryDTO.getAccountCode()), AccountEntity::getAccountCode, queryDTO.getAccountCode());
        queryWrapper.like(StringUtils.isNotBlank(queryDTO.getAccountName()), AccountEntity::getAccountName, queryDTO.getAccountName());
        queryWrapper.in(CollectionUtils.isNotEmpty(queryDTO.getAccountCodeList()), AccountEntity::getAccountCode, queryDTO.getAccountCodeList());
        queryWrapper.in(CollectionUtils.isNotEmpty(queryDTO.getAccountNameList()), AccountEntity::getAccountName, queryDTO.getAccountNameList());
        queryWrapper.eq(StringUtils.isNotBlank(queryDTO.getFundType()), AccountEntity::getFundType, queryDTO.getFundType());
        queryWrapper.eq(StringUtils.isNotBlank(queryDTO.getAccountCategory()), AccountEntity::getAccountCategory, queryDTO.getAccountCategory());
        List<AccountEntity> entityIPage = accountMapper.selectList(queryWrapper);
        return ListBeanUtil.copyList(entityIPage, AccountVO.class);
    }

    @Override
    public AccountDTO getAccountByFundType(String businessCode, String fundType) {
        if (StrUtil.isBlank(businessCode)){
            businessCode = RuleConstant.DEFAULT_BUSINESS_CODE;
        }
        AccountEntity entity = this.getOne(Wrappers.<AccountEntity>lambdaQuery()
                .eq(AccountEntity::getBusinessCode, businessCode)
                .eq(AccountEntity::getFundType, fundType), false);
        if (entity == null && !RuleConstant.DEFAULT_BUSINESS_CODE.equals(businessCode)) {
            entity = this.getOne(Wrappers.<AccountEntity>lambdaQuery()
                    .eq(AccountEntity::getBusinessCode, RuleConstant.DEFAULT_BUSINESS_CODE)
                    .eq(AccountEntity::getFundType, fundType), false);
        }
        if (entity == null){
            throw new ServiceException(StrUtil.format("科目不存在[{}-{}]", businessCode, fundType));
        }
        return BeanUtil.copyProperties(entity, AccountDTO.class);
    }

    @Override
    public AccountDTO getAccountByFundTypeStrict(String fundType, String accountingBusinessCode) {
        if (StrUtil.isBlank(fundType)) {
            throw new ServiceException("金额类型不能为空");
        }
        if (StrUtil.isBlank(accountingBusinessCode)) {
            throw new ServiceException(StrUtil.format("金额类型[{}]缺少核算业务类型，无法匹配科目", fundType));
        }
        AccountEntity entity = this.getOne(Wrappers.<AccountEntity>lambdaQuery()
                .eq(AccountEntity::getFundType, fundType)
                .eq(AccountEntity::getBusinessCode, accountingBusinessCode), false);
        if (entity == null) {
            throw new ServiceException(StrUtil.format(
                    "科目配置不完整：金额类型[{}]、核算业务类型[{}]未配置对应科目",
                    fundType, accountingBusinessCode));
        }
        return BeanUtil.copyProperties(entity, AccountDTO.class);
    }

    @Override
    public List<AccountVO> queryAllAccount() {
        List<AccountVO> accountVOList = Lists.newArrayList();
        QueryWrapper<AccountEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("distinct account_code, account_name,assist_flags").orderByAsc("account_code");
        List<AccountEntity> accountEntityList = this.list(queryWrapper);
        //包含银行科目
        QueryWrapper<BankAccountEntity> bankAccountEntityQueryWrapper = new QueryWrapper<>();
        bankAccountEntityQueryWrapper.select("distinct account_code, account_name").orderByAsc("account_code");
        List<BankAccountEntity> bankAccountList = iBankAccountService.list(bankAccountEntityQueryWrapper);
        if (CollectionUtils.isNotEmpty(accountEntityList)) {
            accountVOList = ListBeanUtil.copyList(accountEntityList, AccountVO.class);
        }
        if (CollectionUtils.isNotEmpty(bankAccountList)) {
            accountVOList.addAll(ListBeanUtil.copyList(bankAccountList, AccountVO.class));
        }
        return accountVOList.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public AccountDTO getOneAccountByCode(String accountCode) {
        List<AccountEntity> entityList = this.lambdaQuery().eq(AccountEntity::getAccountCode,accountCode).list();
        AccountEntity entity = new AccountEntity();
        if (CollectionUtils.isNotEmpty(entityList)) {
            entity = entityList.get(0);
        }
        return BeanUtil.copyProperties(entity, AccountDTO.class);
    }

}

