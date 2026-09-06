package com.utfinancing.financehub.etl.financial.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.common.core.utils.SpringUtils;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.utfinancing.financehub.common.redis.service.RedisService;
import com.utfinancing.financehub.etl.financial.entity.*;
import com.utfinancing.financehub.etl.financial.enums.YesOrNoEnum;
import com.utfinancing.financehub.etl.financial.model.dto.BankAccountDTO;
import com.utfinancing.financehub.etl.financial.model.dto.KingdeeAccountDTO;
import com.utfinancing.financehub.etl.financial.model.dto.KingdeeVoucherEntryInnerDTO;
import com.utfinancing.financehub.etl.financial.model.dto.VoucherTypeDTO;
import com.utfinancing.financehub.etl.financial.service.*;
import com.utfinancing.financehub.etl.kingdee.entity.*;
import com.utfinancing.financehub.etl.kingdee.model.dto.ContractSumDTO;
import com.utfinancing.financehub.etl.kingdee.model.dto.KingdeeAssistBalanceDTO;
import com.utfinancing.financehub.etl.kingdee.model.dto.OptionDTO;
import com.utfinancing.financehub.etl.kingdee.service.*;
import com.utfinancing.financehub.etl.middle.entity.EasVoucherHeadEntity;
import com.utfinancing.financehub.etl.middle.service.IEasVoucherHeadService;
import com.utfinancing.financehub.etl.middle.util.SceneMappingUtil;
import com.utfinancing.financehub.etl.middle.util.VoucherTypeMapUtil;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * @Author : lixin
 * @Date : Create in 12/11/2023
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KingdeeDataSyncServiceImpl implements IKingdeeDataSyncService {

    //币种
    private final ICurrencyService currencyService;
    private final ITBdCurrencyService itBdCurrencyService;

    //签约主体
    private final IOrgCompanyService orgCompanyService;
    private final ITOrgCompanyService itOrgCompanyService;

    //会计期间
    private final IAccountPeriodService accountPeriodService;
    private final ITBdPeriodService itBdPeriodService;

    //银行账户
    private final IBankAccountService bankAccountService;
    private final ITBdAccountbanksService itBdAccountbanksService;

    //凭证类型
    private final IVoucherTypeService voucherTypeService;
    private final ITBdVouchertypesService itBdVouchertypesService;

    //会计科目
    private final IKingdeeAccountService kingdeeAccountService;
    private final ITBdAccountviewService itBdAccountviewService;

    //职员
    private final IKingdeePersonService kingdeePersonService;
    //金融机构
    private final IKingdeeBankService kingdeeBankService;
    //成本中心
    private final IKingdeeCostcenterService kingdeeCostcenterService;
    //自定义核算项目
    private final IKingdeeGeneralAsstService kingdeeGeneralAsstService;

    //金蝶Service
    private final IKingdeeEasService kingdeeEasService;

    //凭证
    private final IKingdeeVoucherService kingdeeVoucherService;
    private final IKingdeeVoucherEntryService kingdeeVoucherEntryService;
    private final ITGlVoucherService itGlVoucherService;
    private final IVoucherService voucherService;
    private final IVoucherEntryService voucherEntryService;

    //客户
    private final ITBdCustomerService itBdCustomerService;
    private final IEasBdCustomerService easBdCustomerService;


    //科目配置
    private final IAccountService accountService;

    //合同余额表
    private final IContractBalanceService contractBalanceService;
    private final IContractBalanceLatestService contractBalanceLatestService;
    private final IKingdeeContractBalanceService kingdeeContractBalanceService;

    //中间库凭证头
    private final IEasVoucherHeadService easVoucherHeadService;

    //合同
    private final IClientService clientService;

    //科目辅助帐余额表
    private final IAccountAssistBalanceService accountAssistBalanceService;

    private final RedisService redisService;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    private IAccountAssistBalanceTempService accountAssistBalanceTempService;

    @Resource
    private IAccountAssistBalanceTempFirstMonthService accountAssistBalanceTempFirstMonthService;

    //过期时间：1天
    private static final long REDIS_VOUCHER_NUM_EXPIRE = 3600L * 24;


    @Override
    public Boolean syncCurrencyAll() {
        //删除全部
        currencyService.remove(Wrappers.emptyWrapper());
        //查询金蝶
        LambdaQueryWrapper<TBdCurrencyEntity> lambdaQueryWrapper = Wrappers.<TBdCurrencyEntity>lambdaQuery();
        lambdaQueryWrapper.select(TBdCurrencyEntity::getFid, TBdCurrencyEntity::getFnameL2, TBdCurrencyEntity::getFisocode,
                TBdCurrencyEntity::getFnumber, TBdCurrencyEntity::getFdeletedstatus);
        List<TBdCurrencyEntity> easEntityList = itBdCurrencyService.list(lambdaQueryWrapper);
        List<CurrencyEntity> finhubEntityList = easEntityList.stream().map(e -> {
            CurrencyEntity finhubEntity = new CurrencyEntity();
            finhubEntity.setCurrencyCode(e.getFisocode());
            finhubEntity.setCurrenctName(e.getFnameL2());
            finhubEntity.setEasId(e.getFid());
            finhubEntity.setEasCode(e.getFnumber());
            finhubEntity.setEasStatus(StrUtil.toString(e.getFdeletedstatus()));
            return finhubEntity;
        }).collect(Collectors.toList());
        //入库
        currencyService.saveBatch(finhubEntityList);
        return Boolean.TRUE;
    }

    @Override
    public Boolean syncOrgCompanyAll() {
        //删除全部
        orgCompanyService.remove(Wrappers.emptyWrapper());
        //查询金蝶
        LambdaQueryWrapper<TOrgCompanyEntity> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.select(TOrgCompanyEntity::getFid, TOrgCompanyEntity::getFnumber, TOrgCompanyEntity::getFnameL2,
                TOrgCompanyEntity::getFlongnumber, TOrgCompanyEntity::getFdisplaynameL2);
        List<TOrgCompanyEntity> easEntityList = itOrgCompanyService.list(lambdaQueryWrapper);
        List<OrgCompanyEntity> finhubEntityList = easEntityList.stream().map(e -> {
            OrgCompanyEntity entity = new OrgCompanyEntity();
            entity.setEasFid(e.getFid());
            entity.setOrgId(e.getFnumber());
            entity.setOrgName(e.getFnameL2());
            entity.setOrgLongId(e.getFlongnumber());
            entity.setOrgLongName(e.getFdisplaynameL2());
            return entity;
        }).collect(Collectors.toList());
        //入库
        orgCompanyService.saveBatch(finhubEntityList);
        return Boolean.TRUE;
    }

    @Override
    public Boolean syncPeriodAll() {
        //删除全部
        accountPeriodService.remove(Wrappers.emptyWrapper());
        //查询金蝶
        LambdaQueryWrapper<TBdPeriodEntity> lambdaQueryWrapper = Wrappers.<TBdPeriodEntity>lambdaQuery();
        lambdaQueryWrapper.select(TBdPeriodEntity::getFid, TBdPeriodEntity::getFperiodyear, TBdPeriodEntity::getFperiodquarter,
                TBdPeriodEntity::getFperiodnumber, TBdPeriodEntity::getFbegindate, TBdPeriodEntity::getFenddate, TBdPeriodEntity::getFnumber,
                TBdPeriodEntity::getFdescriptionL2);
        List<TBdPeriodEntity> easEntityList = itBdPeriodService.list(lambdaQueryWrapper);
        List<AccountPeriodEntity> entityList = easEntityList.stream().map(e -> {
            AccountPeriodEntity entity = new AccountPeriodEntity();
            entity.setEasId(e.getFid());
            entity.setPeriodYear(e.getFperiodyear().intValue());
            entity.setPeriodQuarter(e.getFperiodquarter().intValue());
            entity.setPeriodNumber(e.getFperiodnumber().intValue());
            entity.setPeriodCode(e.getFnumber().intValue());
            entity.setPeriodName(e.getFdescriptionL2());
            entity.setBeginDate(e.getFbegindate());
            entity.setEndDate(e.getFenddate());
            return entity;
        }).collect(Collectors.toList());
        //批量入库
        accountPeriodService.saveBatch(entityList);
        return Boolean.TRUE;
    }

    @Override
    public Boolean syncBankAccountAll() {
        //删除全部
        bankAccountService.remove(Wrappers.emptyWrapper());
        //查询全部
        List<BankAccountDTO> bankAccountDTOList = itBdAccountbanksService.selectAllAccountBank();
        List<BankAccountEntity> entityList = ListBeanUtil.copyList(bankAccountDTOList, BankAccountEntity.class);
        bankAccountService.saveBatch(entityList);
        return Boolean.TRUE;
    }

    @Override
    public Boolean syncVoucherTypeAll() {
        //删除全部
        voucherTypeService.remove(Wrappers.emptyWrapper());
        //查询全部
        List<VoucherTypeDTO> voucherTypeDTOList = itBdVouchertypesService.selectAllVoucherTypes();
        List<VoucherTypeEntity> entityList = ListBeanUtil.copyList(voucherTypeDTOList, VoucherTypeEntity.class);
        voucherTypeService.saveBatch(entityList);
        return Boolean.TRUE;
    }

    @Override
    public Boolean syncAccountAll() {
        //删除全部
        kingdeeAccountService.remove(Wrappers.emptyWrapper());
        //查询全部
        List<KingdeeAccountDTO> kingdeeAccountDTOList = itBdAccountviewService.selectAllAccount();
        List<KingdeeAccountEntity> entityList = ListBeanUtil.copyList(kingdeeAccountDTOList, KingdeeAccountEntity.class);
        kingdeeAccountService.saveBatch(entityList);
        return Boolean.TRUE;
    }

    @Override
    public Boolean syncPersonAll() {
        //删除全部
        kingdeePersonService.remove(Wrappers.emptyWrapper());
        //查询全部
        List<OptionDTO> kingdeeDataList = kingdeeEasService.selectAllPerson();
        List<KingdeePersonEntity> entityList = ListBeanUtil.copyList(kingdeeDataList, KingdeePersonEntity.class);
        kingdeePersonService.saveBatch(entityList);
        return Boolean.TRUE;
    }

    @Override
    public Boolean syncBankAll() {
        //删除全部
        kingdeeBankService.remove(Wrappers.emptyWrapper());
        //查询全部
        List<OptionDTO> kingdeeDataList = kingdeeEasService.selectAllBank();
        List<KingdeeBankEntity> entityList = ListBeanUtil.copyList(kingdeeDataList, KingdeeBankEntity.class);
        kingdeeBankService.saveBatch(entityList);
        return Boolean.TRUE;
    }

    @Override
    public Boolean syncCostCenterAll() {
        //删除全部
        kingdeeCostcenterService.remove(Wrappers.emptyWrapper());
        //查询全部
        List<OptionDTO> kingdeeDataList = kingdeeEasService.selectAllCostCenter();
        List<KingdeeCostcenterEntity> entityList = ListBeanUtil.copyList(kingdeeDataList, KingdeeCostcenterEntity.class);
        kingdeeCostcenterService.saveBatch(entityList);
        return Boolean.TRUE;
    }

    @Override
    public Boolean syncGeneralAsstAll() {
        //删除全部
        kingdeeGeneralAsstService.remove(Wrappers.emptyWrapper());
        //查询全部
        List<OptionDTO> kingdeeDataList = kingdeeEasService.selectAllGeneralAsst();
        List<OptionDTO> kingdeeContractDataList = kingdeeEasService.selectContractData();
        kingdeeDataList.addAll(kingdeeContractDataList);
        List<KingdeeGeneralAsstEntity> entityList = ListBeanUtil.copyList(kingdeeDataList, KingdeeGeneralAsstEntity.class);
        kingdeeGeneralAsstService.saveBatch(entityList);
        return Boolean.TRUE;
    }

    @Override
    public Boolean syncOneContract() {
        List<ContractSumDTO> contractSumDTOList = itGlVoucherService.selectOneContract();
        log.info("contractSumDTOList: {}", JSONObject.toJSONString(contractSumDTOList));
        return Boolean.TRUE;
    }

    @Override
    public Boolean syncKingdeeCustomerAll() {
        //删除全部
        easBdCustomerService.remove(Wrappers.emptyWrapper());
        //导入
        List<TBdCustomerEntity> kingdeeEntityList = itBdCustomerService.list();
        List<EasBdCustomerEntity> finhubEntityList = ListBeanUtil.copyList(kingdeeEntityList, EasBdCustomerEntity.class);
        easBdCustomerService.saveBatch(finhubEntityList);
        return Boolean.TRUE;
    }


    @Override
    @Async
    public Long syncVoucherByDateV2(Integer periodCode, String voucherDate, CountDownLatch countDownLatch) {
        //查询
        List<KingdeeVoucherEntity> entityList = itGlVoucherService.selectVoucherByPeriodAndDate(periodCode, voucherDate);
        log.info("syncVoucherByDate queryList size:{}, periodCode:{}, voucherDate:{}", entityList.size(), periodCode, voucherDate);
        //分页执行
        List<List<KingdeeVoucherEntity>> pageEntityList = ListUtil.partition(entityList, 1000);
        for (List<KingdeeVoucherEntity> subList: pageEntityList){
            //批量查询中间库凭证头
            List<EasVoucherHeadEntity> headEntityList = easVoucherHeadService.selectMidVoucherHeaderByEasIds(subList.stream().map(e -> e.getEasId()).collect(Collectors.toList()));
            log.info("已查询到中间表凭证头， 总查询个数：{}， 查询到凭证头个数:{}.", subList.size(), headEntityList.size());
            Map<String, EasVoucherHeadEntity> headEntityMap = headEntityList.stream().collect(Collectors.toMap(e -> e.getEasbzcode(), e -> e, (k1, k2) -> k1));
            List<VoucherEntity> finhubVoucherEntityList = new ArrayList<>();
            for (KingdeeVoucherEntity kingdeeVoucherEntity: subList){
                VoucherEntity voucherEntity = new VoucherEntity();
                EasVoucherHeadEntity headEntity = headEntityMap.get(kingdeeVoucherEntity.getEasId());
                if (headEntity != null) {
                    voucherEntity.setSystemCode(headEntity.getSystem());
                    voucherEntity.setSource(headEntity.getSystem());
                    voucherEntity.setSceneCode(SceneMappingUtil.getMappingSceneCode(headEntity.getSystem(), headEntity.getModelname()));
                    voucherEntity.setContractCode(headEntity.getContractid());
                    voucherEntity.setVoucherSummary(headEntity.getDescription());
                    voucherEntity.setVoucherType(VoucherTypeMapUtil.getVoucherTypeCode(headEntity.getVouchertype()));
                } else {
                    voucherEntity.setSystemCode("EAS");
                    voucherEntity.setSource("EAS");
                    voucherEntity.setSceneCode("SGPZ");
                    voucherEntity.setVoucherSummary("期初数据导入");
                    voucherEntity.setVoucherType("03");
                }
                voucherEntity.setPeriodCode(periodCode);
                voucherEntity.setEasVoucherId(kingdeeVoucherEntity.getEasId());
                voucherEntity.setOrgId(kingdeeVoucherEntity.getOrgId());
                voucherEntity.setSignCompany(kingdeeVoucherEntity.getOrgId());
                voucherEntity.setBusinessDate(kingdeeVoucherEntity.getBusinessDate());
                voucherEntity.setVoucherDate(kingdeeVoucherEntity.getVoucherDate());
                voucherEntity.setVoucherNum(null);
                voucherEntity.setCurrency(kingdeeVoucherEntity.getCurrencyCode());
                finhubVoucherEntityList.add(voucherEntity);
            }
            voucherService.saveBatch(finhubVoucherEntityList);
        }
        countDownLatch.countDown();
        log.info("凭证头同步完成， periodCode:{}, voucherDate:{}", periodCode, voucherDate);
        return new Integer(entityList.size()).longValue();
    }



    @Override
    @Async
    public Long syncVoucherByDate(Integer periodCode, String voucherDate) {
        long start = System.currentTimeMillis();
        log.info("syncVoucherByDate start, periodCode:{}, voucherDate:{}", periodCode, voucherDate);
        //删除全部
        kingdeeVoucherService.remove(Wrappers.<KingdeeVoucherEntity>lambdaQuery()
                .eq(KingdeeVoucherEntity::getPeriodCode, periodCode)
                .eq(KingdeeVoucherEntity::getVoucherDate, LocalDateTimeUtil.parse(voucherDate, "yyyy-MM-dd")));
        log.info("syncVoucherByDate remove data done, periodCode:{}, voucherDate:{}", periodCode, voucherDate);
        //查询
        List<KingdeeVoucherEntity> entityList = itGlVoucherService.selectVoucherByPeriodAndDate(periodCode, voucherDate);
        log.info("syncVoucherByDate queryList size:{}, periodCode:{}, voucherDate:{}", entityList.size(), periodCode, voucherDate);
        kingdeeVoucherService.saveBatch(entityList);
        log.info("syncVoucherByDate end. cost:{}ms, periodCode:{}, voucherDate:{}", (System.currentTimeMillis() - start), periodCode, voucherDate);
        return new Integer(entityList.size()).longValue();
    }

    @Override
    public Boolean syncVoucherByPeriod(String periodCode) {
        List<String> dayList = DateUtils.getDayListOfMonth(periodCode);
        List<String> contractCodeList = Lists.newArrayList();
        for (String day : dayList) {
            IKingdeeDataSyncService kingdeeDataSyncService = SpringUtils.getBean(IKingdeeDataSyncService.class);
            kingdeeDataSyncService.syncVoucherByDate(NumberUtil.parseInt(periodCode), day);
        }
        return Boolean.TRUE;
    }

    @Override
    @Async
    public Boolean syncFinhubVoucherHeadV2(String periodCodes) {
        List<String> contractCodeList = Lists.newArrayList();
        List<String> periodCodeList = StrUtil.split(periodCodes, "|");
        for (String periodCodeStr : periodCodeList) {
            List<String> dayList = DateUtils.getDayListOfMonth(periodCodeStr);
            Integer periodCode = NumberUtil.parseInt(periodCodeStr);
            CountDownLatch countDownLatch = new CountDownLatch(dayList.size());
            for (String day : dayList) {
                IKingdeeDataSyncService kingdeeDataSyncService = SpringUtils.getBean(IKingdeeDataSyncService.class);
                kingdeeDataSyncService.syncVoucherByDateV2(periodCode, day, countDownLatch);
            }
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return Boolean.TRUE;
    }

    @Override
    @Async
    public Boolean syncFinhubVoucherHeadByDayV2(Integer periodCode, String voucherDate) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        IKingdeeDataSyncService kingdeeDataSyncService = SpringUtils.getBean(IKingdeeDataSyncService.class);
        kingdeeDataSyncService.syncVoucherByDateV2(periodCode, voucherDate, countDownLatch);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public Boolean syncKingdeeVoucherEntryByPeriod(String periodCode) {
        List<String> dayList = DateUtils.getDayListOfMonth(periodCode);
        for (String day : dayList) {
            IKingdeeDataSyncService kingdeeDataSyncService = SpringUtils.getBean(IKingdeeDataSyncService.class);
            kingdeeDataSyncService.syncVoucherEntryByPeriod(NumberUtil.parseInt(periodCode), day);
        }
        return Boolean.TRUE;
    }

    @Override
    @Async
    public Long syncVoucherEntryByPeriod(Integer periodCode, String voucherDate) {
        //查询凭证头
        List<KingdeeVoucherEntity> voucherEntityList = kingdeeVoucherService.list(Wrappers.<KingdeeVoucherEntity>lambdaQuery()
                .select(KingdeeVoucherEntity::getEasId)
                .eq(KingdeeVoucherEntity::getPeriodCode, periodCode)
                .eq(KingdeeVoucherEntity::getVoucherDate, voucherDate));
        log.info("syncVoucherEntryByPeriod queryVoucher size:{}, periodCode:{}, voucherDate:{}", voucherEntityList.size(), periodCode, voucherDate);
        for (KingdeeVoucherEntity voucherEntity : voucherEntityList) {
            String voucherId = voucherEntity.getEasId();
            //先清除
            kingdeeVoucherEntryService.remove(Wrappers.<KingdeeVoucherEntryEntity>lambdaQuery().eq(KingdeeVoucherEntryEntity::getVoucherEasId, voucherId));
            //从金蝶查询
            List<KingdeeVoucherEntryEntity> voucherEntryEntityList = itGlVoucherService.selectVoucherEntryByPeriodAndDate(voucherId);
            kingdeeVoucherEntryService.saveBatch(voucherEntryEntityList);
            log.info("syncVoucherEntryByPeriod saveVoucherEntry done size:{}, voucherId:{}", voucherEntryEntityList.size(), voucherId);
        }
        log.info("syncVoucherEntryByPeriod end size:{}, periodCode:{}, voucherDate:{}", voucherEntityList.size(), periodCode, voucherDate);
        return new Long(voucherEntityList.size());
    }


    @Override
    @Async
    public Long syncFinhubVoucherBalance(String periodCodes,String contractCodes) {
        List<String> contractCodeList = Lists.newArrayList();
        if (StringUtils.isNotEmpty(contractCodes)) {
            contractCodeList = Arrays.asList(contractCodes.split(","));
        }
        log.info("合同：{}",JSON.toJSON(contractCodeList));
        //1.查询科目配置
        List<AccountEntity> accountEntityList = accountService.list(Wrappers.<AccountEntity>lambdaQuery());
        Map<String, AccountEntity> accountFundMap = accountEntityList.stream().collect(Collectors.toMap(e -> e.getAccountCode(), e -> e, (e1,e2)->e1));
        for (String periodCodeStr: StrUtil.split(periodCodes, "|")){
            Integer periodCode = NumberUtil.parseInt(periodCodeStr);
            String voucherDate = periodCodeStr.substring(0,4) + "-" + periodCodeStr.substring(4) + "-" + "01";
            //2.查询凭证
            List<KingdeeVoucherEntryInnerDTO> voucherEntryDTOList = kingdeeVoucherService.selectKingdeeVoucherEntrySumByPeriod(periodCode,contractCodeList);
            log.info("查询到所有数据 periodCode:{}, size:{}", periodCode, voucherEntryDTOList.size());
            //3.生成凭证头
            Map<String, List<KingdeeVoucherEntryInnerDTO>> voucherEntryDTOMap = voucherEntryDTOList.stream().collect(Collectors.groupingBy(e -> {
                return e.getOrgId()+"|" + e.getContractCode()+ "|" + e.getClientCode() + "|" + e.getBillContractCode();
            }));
            Map<Integer, List<String>> voucherEntryKeyMap = voucherEntryDTOMap.keySet().stream().collect(Collectors.groupingBy(e -> Math.abs(e.hashCode()) % 50));
            CountDownLatch countDownLatch = new CountDownLatch(voucherEntryKeyMap.size());
            for (Map.Entry<Integer, List<String>> keyEntry : voucherEntryKeyMap.entrySet()) {
                List<String> groupKeyList = keyEntry.getValue();
                log.info("分组结果 periodCode:{},hashKey:{}, dataSize:{}", periodCode, keyEntry.getKey(), groupKeyList.size());
                IKingdeeDataSyncService kingdeeDataSyncService = SpringUtils.getBean(IKingdeeDataSyncService.class);
                kingdeeDataSyncService.asyncFinHubVoucherBalance(countDownLatch, voucherEntryDTOMap, groupKeyList, periodCode, voucherDate, accountFundMap, accountEntityList);
            }
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.info("期间同步完成 periodCode:{}, size:{}", periodCode, voucherEntryDTOList.size());
        }
        return new Long(1);
    }

    @Override
    @Async
    public Boolean syncKingdeeContractBalance(Integer periodCode,String contractCodes) {
        log.info("KingdeeDataSyncServiceImpl.syncKingdeeContractBalance start ,periodCode:{},contractCodes:{}", periodCode,contractCodes);
        List<String> contractCodeList = Lists.newArrayList();
        if (StringUtils.isNotEmpty(contractCodes)) {
            contractCodeList.addAll(Arrays.asList(contractCodes.split(",")));
        }
        List<AccountEntity> accountEntityList = accountService.list(Wrappers.<AccountEntity>lambdaQuery());
        Map<String, AccountEntity> accountFundMap = accountEntityList.stream().collect(Collectors.toMap(e -> e.getAccountCode(), e -> e, (e1,e2)->e1));
        long start = System.currentTimeMillis();
        List<KingdeeContractBalanceEntity> entityList = itGlVoucherService.selectContractBalance(periodCode,contractCodeList);
        log.info("原始数据查询完成 period:{}, size:{}", periodCode, entityList.size());
        //分组
        Map<String, List<KingdeeContractBalanceEntity>> mapEntityList = entityList.stream().collect(Collectors.groupingBy(e -> {
            return e.getOrgId() + "|" + e.getContractCode() + "|" + e.getClientCode() + "|" + e.getBillContractCode();
        }));
        log.info("原始数据分组完成，开始逐行同步");
        int i = 1;
        double totalSize = NumberUtil.toDouble(mapEntityList.size());
        for (Map.Entry<String, List<KingdeeContractBalanceEntity>> entityMapEntry : mapEntityList.entrySet()) {
            try {
                String key = entityMapEntry.getKey();
                List<String> keyList = StrUtil.split(key, "|");
                String orgId = keyList.get(0);
                String contractCode = keyList.get(1);
                String clientCode = keyList.get(2);
                String billContractCode = keyList.get(3);
                Map<String, Object> contractBalanceRowMap = new HashMap<>();

                contractBalanceRowMap.put("system_code", "EAS");
                contractBalanceRowMap.put("business_date", LocalDateTimeUtil.parse("2018-12-31", "yyyy-MM-dd"));
                contractBalanceRowMap.put("voucher_date", LocalDateTimeUtil.parse("2018-12-31", "yyyy-MM-dd"));
                contractBalanceRowMap.put("scene_code", "SGPZ");
                contractBalanceRowMap.put("org_id", orgId);
                contractBalanceRowMap.put("contract_code", StrUtil.equals(contractCode, "null") ? null : contractCode);
                contractBalanceRowMap.put("client_code", StrUtil.equals(clientCode, "null") ? null : clientCode);
                contractBalanceRowMap.put("bill_contract_code", StrUtil.equals(billContractCode, "null") ? null : billContractCode);
                contractBalanceRowMap.put("period_code", periodCode);
                String businceeCode = "ZLYW";
                //根据科目排序
                List<KingdeeContractBalanceEntity> accountBalanceList = ListUtil.sortByProperty(entityMapEntry.getValue(), "accountCode");
                boolean insertFlage = false;
                for (KingdeeContractBalanceEntity kingdeeBalanceDTO : accountBalanceList) {
                    AccountEntity accountEntity = accountFundMap.get(kingdeeBalanceDTO.getAccountCode());
                    if (accountEntity != null) {
                        insertFlage = true;
                        String balanceType = accountEntity.getFundType() + "_balance";
                        String amountType = accountEntity.getFundType() + "_amount";
                        BigDecimal accountBalance = StringUtils.isEmpty(kingdeeBalanceDTO.getAccountBalance()) ? BigDecimal.ZERO : new BigDecimal(kingdeeBalanceDTO.getAccountBalance());
                        //CR 贷方，DR 借方
                        if ("CR".equals(accountEntity.getDebitCreditType())) {
                            accountBalance = NumberUtil.mul(accountBalance, -1);
                        }
                        if (contractBalanceRowMap.containsKey(balanceType)) {
                            accountBalance = (null == contractBalanceRowMap.get(balanceType) ? BigDecimal.ZERO : new BigDecimal(contractBalanceRowMap.get(balanceType).toString())).add(accountBalance);
                        }
                        contractBalanceRowMap.put(balanceType, accountBalance);
                        contractBalanceRowMap.put(amountType, accountBalance);
                        if (!contractBalanceRowMap.containsKey("business_code") && !ListUtil.toList("1122.07", "1122.06", "1221.04").contains(accountEntity.getAccountCode())) {
                            businceeCode = accountEntity.getBusinessCode();
                        }
                    }
                }
                contractBalanceRowMap.put("business_code", businceeCode);

                if (insertFlage){
                    //加入期初数据
                    contractBalanceService.insertMap(contractBalanceRowMap);
                    contractBalanceLatestService.insertMap(contractBalanceRowMap);
                }else{
                    log.info("找不到任何科目，跳过, key:{}", key);
                }
                log.info("单合同数据同步完成, key:{}, percent:{}", key, NumberUtil.formatPercent(i / totalSize, 2));
                i++;
            } catch (Exception e) {
                log.info("单笔合同异常, key:{}, errorMessage:{}", entityMapEntry.getKey(), e.getMessage());
            }
        }
        long end = System.currentTimeMillis();
        log.info("end period:{}, size:{},cost:{}", periodCode, entityList.size(), (end - start));
        return Boolean.TRUE;
    }

//    @Override
//    @Async
//    public Boolean syncKingdeeAssistBalance(String periodCodes, String orgId) {
//        List<String> periodCodeList = StrUtil.split(periodCodes, "|");
//        for (String periodCodeStr: periodCodeList){
//            Integer periodCode = NumberUtil.parseInt(periodCodeStr);
//            log.info("同步金蝶辅助帐余额表 开始, periodCode:{}, orgId:{}", periodCode, orgId);
//            List<KingdeeAssistBalanceDTO> kingdeeAssistBalanceList = itGlVoucherService.selectKingdeeAssistBalanceList(periodCode, orgId);
//            log.info("同步金蝶辅助帐余额表 查询到数据  periodCode:{}, orgId:{}, size:{}", periodCode, orgId, kingdeeAssistBalanceList.size());
//            //查询科目
//            List<AccountEntity> accountEntityList = accountService.list(Wrappers.<AccountEntity>lambdaQuery());
//            Map<String, AccountEntity> accountMap = accountEntityList.stream().collect(Collectors.toMap(e -> e.getAccountCode(), e -> e, (e1,e2)->e1));
//            List<AccountAssistBalanceEntity> accountAssistBalanceEntityList = kingdeeAssistBalanceList.stream().map(e->{
//                AccountEntity accountEntity = accountMap.get(e.getAccountCode());
//                if (accountEntity == null) return null;
//
//                AccountAssistBalanceEntity entity = BeanUtil.copyProperties(e, AccountAssistBalanceEntity.class);
//                //余额
//                if ("DR".equals(accountEntity.getDebitCreditType())){
//                    entity.setMonthBeginDebitBalance(e.getBeginBalance());
//                    entity.setMonthEndDebitBalance(e.getEndBalance());
//                }else{
//                    entity.setMonthBeginCreditBalance(e.getBeginBalance());
//                    entity.setMonthEndCreditBalance(e.getEndBalance());
//                }
//                //发生额
//                entity.setMonthDebitAmount(e.getDebitAmount());
//                entity.setMonthCreditAmount(e.getCreditAmount());
//                entity.setYearDebitAmount(e.getYearDebitAmount());
//                entity.setYearCreditAmount(e.getYearCreditAmount());
//                return entity;
//            }).filter(e-> ObjectUtil.isNotNull(e)).collect(Collectors.toList());
//            log.info("同步金蝶辅助帐余额表 开始保存入库  periodCode:{}, orgId:{}, 转换后数据量:{}", periodCode, orgId, accountAssistBalanceEntityList.size());
//            List<List<AccountAssistBalanceEntity>> entityPage = ListUtil.partition(accountAssistBalanceEntityList, 500);
//            IKingdeeDataSyncService kingdeeDataSyncService = SpringUtils.getBean(IKingdeeDataSyncService.class);
//            int pageCount = entityPage.size();
//            CountDownLatch countDownLatch = new CountDownLatch(pageCount);
//            log.info("同步金蝶辅助帐余额表 分组完成  periodCode:{}, orgId:{}, pageCount:{}", periodCode, orgId, pageCount);
//            int i = 1;
//            for (List<AccountAssistBalanceEntity> entityList: entityPage){
//                kingdeeDataSyncService.batchSaveAccountAssistBalance(countDownLatch, entityList, NumberUtil.formatPercent(i/Double.valueOf(Integer.valueOf(pageCount).toString()),2), periodCode);
//                i++;
//            }
//            try {
//                countDownLatch.await();
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//            log.info("同步金蝶辅助帐余额表 期间完成  periodCode:{}, orgId:{}, 转换后数据量:{}", periodCode, orgId, accountAssistBalanceEntityList.size());
//        }
//        return Boolean.TRUE;
//    }

    @Override
    @Async
    public Boolean syncKingdeeAssistBalance(String periodCodes, String orgId) {
        List<String> periodCodeList = StrUtil.split(periodCodes, "|");

        for (String periodCodeStr: periodCodeList){
            CountDownLatch totalCountDownLatch = new CountDownLatch(1);

            IKingdeeDataSyncService kingdeeDataSyncService = SpringUtils.getBean(IKingdeeDataSyncService.class);
            int size = kingdeeDataSyncService.executeMonth(totalCountDownLatch, periodCodeStr, orgId);
            log.info("同步金蝶辅助帐余额表 期间完成  periodCode:{}, orgId:{}, 转换后数据量:{}", periodCodeStr, orgId, size);

            try {
                totalCountDownLatch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return Boolean.TRUE;
    }

    @Override
    public int executeMonth(CountDownLatch totalCountDownLatch, String periodCodeStr, String orgId) {
        Integer periodCode = NumberUtil.parseInt(periodCodeStr);
        log.info("同步金蝶辅助帐余额表 开始, periodCode:{}, orgId:{}", periodCode, orgId);

        List<KingdeeAssistBalanceDTO> kingdeeAssistBalanceList = itGlVoucherService.selectKingdeeAssistBalanceList(periodCode, orgId);
        log.info("同步金蝶辅助帐余额表 查询到数据  periodCode:{}, orgId:{}, size:{}", periodCode, orgId, kingdeeAssistBalanceList.size());
        //查询科目
        List<AccountEntity> accountEntityList = accountService.list(Wrappers.<AccountEntity>lambdaQuery());
        Map<String, AccountEntity> accountMap = accountEntityList.stream().collect(Collectors.toMap(e -> e.getAccountCode(), e -> e, (e1,e2)->e1));
        boolean isFirstMonth = periodCodeStr.endsWith("01");

        //一月份数据进tempFirstMonth表
        if(isFirstMonth){
            accountAssistBalanceTempFirstMonthService.deleteTable();
            List<AccountAssistBalanceTempFirstMonthEntity> accountAssistBalanceEntityList = kingdeeAssistBalanceList.stream().map(e->{
                AccountEntity accountEntity = accountMap.get(e.getAccountCode());
                if (accountEntity == null) return null;

                AccountAssistBalanceTempFirstMonthEntity entity = BeanUtil.copyProperties(e, AccountAssistBalanceTempFirstMonthEntity.class);
                //余额
                if ("DR".equals(accountEntity.getDebitCreditType())){
                    entity.setMonthBeginDebitBalance(e.getBeginBalance());
                    entity.setMonthEndDebitBalance(e.getEndBalance());
                }else{
                    entity.setMonthBeginCreditBalance(NumberUtil.mul(e.getBeginBalance(), new BigDecimal(-1)));//*-1
                    entity.setMonthEndCreditBalance(NumberUtil.mul(e.getEndBalance(), new BigDecimal(-1)));//*-1
                }
                //发生额
                entity.setMonthDebitAmount(e.getDebitAmount());
                entity.setMonthCreditAmount(e.getCreditAmount());
                entity.setYearDebitAmount(e.getYearDebitAmount());
                entity.setYearCreditAmount(e.getYearCreditAmount());

                entity.setYearBeginDebitBalance(entity.getMonthBeginDebitBalance());
                entity.setYearBeginCreditBalance(entity.getMonthBeginCreditBalance());
                entity.setDelFlag(YesOrNoEnum.NO.getCode());
                return entity;
            }).filter(e-> ObjectUtil.isNotNull(e)).collect(Collectors.toList());
            log.info("同步金蝶辅助帐余额表 同步到eg_account_assist_balance_temp_fist_month 开始保存入库  periodCode:{}, orgId:{}, 转换后数据量:{}", periodCode, orgId, accountAssistBalanceEntityList.size());
            List<List<AccountAssistBalanceTempFirstMonthEntity>> entityPage = ListUtil.partition(accountAssistBalanceEntityList, 500);
//                    IKingdeeDataSyncService kingdeeDataSyncService = SpringUtils.getBean(IKingdeeDataSyncService.class);
            int pageCount = entityPage.size();
            CountDownLatch countDownLatch = new CountDownLatch(pageCount);
            log.info("同步金蝶辅助帐余额表 同步到eg_account_assist_balance_temp_fist_month 分组完成  periodCode:{}, orgId:{}, pageCount:{}", periodCode, orgId, pageCount);
            int i = 1;
            for (List<AccountAssistBalanceTempFirstMonthEntity> entityList: entityPage){
//                kingdeeDataSyncService.batchSaveAccountAssistBalance(countDownLatch, entityList, NumberUtil.formatPercent(i/Double.valueOf(Integer.valueOf(pageCount).toString()),2), periodCode);
                accountAssistBalanceTempFirstMonthService.batchSaveAccountAssistBalance(countDownLatch, entityList, NumberUtil.formatPercent(i/Double.valueOf(Integer.valueOf(pageCount).toString()),2), periodCode);
                i++;
            }
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        accountAssistBalanceTempService.deleteTable();
        List<AccountAssistBalanceTempEntity> accountAssistBalanceEntityList = kingdeeAssistBalanceList.stream().map(e->{
            AccountEntity accountEntity = accountMap.get(e.getAccountCode());
            if (accountEntity == null) return null;

            AccountAssistBalanceTempEntity entity = BeanUtil.copyProperties(e, AccountAssistBalanceTempEntity.class);
            //余额
            if ("DR".equals(accountEntity.getDebitCreditType())){
                entity.setMonthBeginDebitBalance(e.getBeginBalance());
                entity.setMonthEndDebitBalance(e.getEndBalance());
            }else{
                entity.setMonthBeginCreditBalance(NumberUtil.mul(e.getBeginBalance(), new BigDecimal(-1)));//*-1
                entity.setMonthEndCreditBalance(NumberUtil.mul(e.getEndBalance(), new BigDecimal(-1)));//*-1
            }
            //发生额
            entity.setMonthDebitAmount(e.getDebitAmount());
            entity.setMonthCreditAmount(e.getCreditAmount());
            entity.setYearDebitAmount(e.getYearDebitAmount());
            entity.setYearCreditAmount(e.getYearCreditAmount());
            entity.setDelFlag(YesOrNoEnum.NO.getCode());
            return entity;
        }).filter(e-> ObjectUtil.isNotNull(e)).collect(Collectors.toList());
        log.info("同步金蝶辅助帐余额表 同步到eg_account_assist_balance_temp 开始保存入库  periodCode:{}, orgId:{}, 转换后数据量:{}", periodCode, orgId, accountAssistBalanceEntityList.size());
        List<List<AccountAssistBalanceTempEntity>> entityPage = ListUtil.partition(accountAssistBalanceEntityList, 500);
//                IKingdeeDataSyncService kingdeeDataSyncService = SpringUtils.getBean(IKingdeeDataSyncService.class);
        int pageCount = entityPage.size();
        CountDownLatch countDownLatch2 = new CountDownLatch(pageCount);
        log.info("同步金蝶辅助帐余额表 同步到eg_account_assist_balance_temp 分组完成  periodCode:{}, orgId:{}, pageCount:{}", periodCode, orgId, pageCount);
        int i = 1;
        for (List<AccountAssistBalanceTempEntity> entityList: entityPage){
//                    kingdeeDataSyncService.batchSaveAccountAssistBalance(countDownLatch, entityList, NumberUtil.formatPercent(i/Double.valueOf(Integer.valueOf(pageCount).toString()),2), periodCode);
            accountAssistBalanceTempService.batchSaveAccountAssistBalance(countDownLatch2, entityList, NumberUtil.formatPercent(i/Double.valueOf(Integer.valueOf(pageCount).toString()),2), periodCode);
            i++;
        }
        try {
            countDownLatch2.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("同步金蝶辅助帐余额表 批量修改数据的年初余额 开始  periodCode:{}, orgId:{}", periodCode, orgId);
        accountAssistBalanceTempService.batchUpdateYearBeginDataByFirstMonth(periodCode);
        log.info("同步金蝶辅助帐余额表 批量修改数据的年初余额 结束  periodCode:{}, orgId:{}", periodCode, orgId);
        log.info("同步金蝶辅助帐余额表 同步临时表数据到eg_account_assist_balance 开始  periodCode:{}, orgId:{}", periodCode, orgId);
        accountAssistBalanceTempService.syncIntoAccountAssist(periodCode);
        log.info("同步金蝶辅助帐余额表 同步临时表数据到eg_account_assist_balance 结束  periodCode:{}, orgId:{}", periodCode, orgId);

//            List<AccountAssistBalanceEntity> accountAssistBalanceEntityList = kingdeeAssistBalanceList.stream().map(e->{
//                AccountEntity accountEntity = accountMap.get(e.getAccountCode());
//                if (accountEntity == null) return null;
//
//                AccountAssistBalanceEntity entity = BeanUtil.copyProperties(e, AccountAssistBalanceEntity.class);
//                //余额
//                if ("DR".equals(accountEntity.getDebitCreditType())){
//                    entity.setMonthBeginDebitBalance(e.getBeginBalance());
//                    entity.setMonthEndDebitBalance(e.getEndBalance());
//                }else{
//                    entity.setMonthBeginCreditBalance(NumberUtil.mul(e.getBeginBalance(), new BigDecimal(-1)));//*-1
//                    entity.setMonthEndCreditBalance(NumberUtil.mul(e.getEndBalance(), new BigDecimal(-1)));//*-1
//                }
//                //发生额
//                entity.setMonthDebitAmount(e.getDebitAmount());
//                entity.setMonthCreditAmount(e.getCreditAmount());
//                entity.setYearDebitAmount(e.getYearDebitAmount());
//                entity.setYearCreditAmount(e.getYearCreditAmount());
//                return entity;
//            }).filter(e-> ObjectUtil.isNotNull(e)).collect(Collectors.toList());
//            log.info("同步金蝶辅助帐余额表 开始保存入库  periodCode:{}, orgId:{}, 转换后数据量:{}", periodCode, orgId, accountAssistBalanceEntityList.size());
//            List<List<AccountAssistBalanceEntity>> entityPage = ListUtil.partition(accountAssistBalanceEntityList, 500);
//            IKingdeeDataSyncService kingdeeDataSyncService = SpringUtils.getBean(IKingdeeDataSyncService.class);
//            int pageCount = entityPage.size();
//            CountDownLatch countDownLatch = new CountDownLatch(pageCount);
//            log.info("同步金蝶辅助帐余额表 分组完成  periodCode:{}, orgId:{}, pageCount:{}", periodCode, orgId, pageCount);
//            int i = 1;
//            for (List<AccountAssistBalanceEntity> entityList: entityPage){
//                kingdeeDataSyncService.batchSaveAccountAssistBalance(countDownLatch, entityList, NumberUtil.formatPercent(i/Double.valueOf(Integer.valueOf(pageCount).toString()),2), periodCode);
//                i++;
//            }
//            try {
//                countDownLatch.await();
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
        totalCountDownLatch.countDown();
        return kingdeeAssistBalanceList.size();
//        System.out.println(periodCodeStr);
//        return 0;
    }


    @Override
    @Async
    public void batchSaveAccountAssistBalance(CountDownLatch countDownLatch, List<AccountAssistBalanceEntity> accountAssistBalanceEntityList, String percent, Integer periodCode) {
        accountAssistBalanceService.saveBatch(accountAssistBalanceEntityList);
        log.info("同步金蝶辅助帐余额表 单页保存完成.periodCode:{}, 完成比例:{}", periodCode, percent);
        countDownLatch.countDown();
    }



    @Override
    @Async
    public Boolean saveKingdeeAccountBalance(Integer periodCode,String contractCodes) {
        List<String> contractCodeList = Lists.newArrayList();
        if (StringUtils.isNotEmpty(contractCodes)) {
            contractCodeList.addAll(Arrays.asList(contractCodes.split(",")));
        }
        log.info("KingdeeDataSyncServiceImpl.saveKingdeeAccountBalance start ,periodCode:{}", periodCode);
        List<KingdeeContractBalanceEntity> entityList = itGlVoucherService.selectContractBalance(periodCode,contractCodeList);
        log.info("原始数据查询完成 period:{}, size:{}", periodCode, entityList.size());
        kingdeeContractBalanceService.saveBatch(entityList);
        log.info("保存完成, period:{}, size:{}", periodCode, entityList.size());
        return Boolean.TRUE;
    }

    @Override
    public Boolean syncKingdeeContractBalance80001(Integer periodCode,String contractCodes) {
        log.info("KingdeeDataSyncServiceImpl.syncKingdeeContractBalance start ,periodCode:{},contractCodes:{}", periodCode,contractCodes);
        List<AccountEntity> accountEntityList = accountService.list(Wrappers.<AccountEntity>lambdaQuery());
        Map<String, AccountEntity> accountFundMap = accountEntityList.stream().collect(Collectors.toMap(e -> e.getAccountCode(), e -> e, (e1,e2)->e1));
        long start = System.currentTimeMillis();
        List<String> contractCodeList = Lists.newArrayList();
        if (StringUtils.isNotEmpty(contractCodes)) {
            contractCodeList.addAll(Arrays.asList(contractCodes.split(",")));
        }
        log.info("合同号：{}", JSON.toJSON(contractCodeList));
        List<KingdeeContractBalanceEntity> entityList = itGlVoucherService.selectContractBalance80001(periodCode,contractCodeList);
        log.info("原始数据查询完成 period:{}, size:{}", periodCode, entityList.size());
        //分组
        Map<String, List<KingdeeContractBalanceEntity>> mapEntityList = entityList.stream().collect(Collectors.groupingBy(e -> {
            return e.getOrgId() + "|" + e.getContractCode() + "|" + e.getClientCode() + "|" + e.getBillContractCode();
        }));
        log.info("原始数据分组完成，开始逐行同步");
        int i = 1;
        double totalSize = NumberUtil.toDouble(mapEntityList.size());
        for (Map.Entry<String, List<KingdeeContractBalanceEntity>> entityMapEntry : mapEntityList.entrySet()) {
            try {
                String key = entityMapEntry.getKey();
                List<String> keyList = StrUtil.split(key, "|");
                String orgId = keyList.get(0);
                String contractCode = keyList.get(1);
                String clientCode = keyList.get(2);
                String billContractCode = keyList.get(3);
                Map<String, Object> contractBalanceRowMap = new HashMap<>();

                contractBalanceRowMap.put("system_code", "EAS");
                contractBalanceRowMap.put("business_date", LocalDateTimeUtil.parse("2020-09-30", "yyyy-MM-dd"));
                contractBalanceRowMap.put("voucher_date", LocalDateTimeUtil.parse("2020-09-30", "yyyy-MM-dd"));
                contractBalanceRowMap.put("scene_code", "SGPZ");
                contractBalanceRowMap.put("org_id", orgId);
                contractBalanceRowMap.put("contract_code", StrUtil.equals(contractCode, "null") ? null : contractCode);
                contractBalanceRowMap.put("client_code", StrUtil.equals(clientCode, "null") ? null : clientCode);
                contractBalanceRowMap.put("bill_contract_code", StrUtil.equals(billContractCode, "null") ? null : billContractCode);
                contractBalanceRowMap.put("period_code", periodCode);
                String businceeCode = "ZLYW";

                //根据科目排序
                List<KingdeeContractBalanceEntity> accountBalanceList = ListUtil.sortByProperty(entityMapEntry.getValue(), "accountCode");
                boolean insertFlage = false;
                for (KingdeeContractBalanceEntity kingdeeBalanceDTO : accountBalanceList) {
                    AccountEntity accountEntity = accountFundMap.get(kingdeeBalanceDTO.getAccountCode());
                    if (accountEntity != null) {
                        insertFlage = true;
                        String balanceType = accountEntity.getFundType() + "_balance";
                        String amountType = accountEntity.getFundType() + "_amount";
                        BigDecimal accountBalance = StringUtils.isEmpty(kingdeeBalanceDTO.getAccountBalance()) ? BigDecimal.ZERO : new BigDecimal(kingdeeBalanceDTO.getAccountBalance());
                        //CR 贷方，DR 借方
                        if ("CR".equals(accountEntity.getDebitCreditType())) {
                            accountBalance = NumberUtil.mul(accountBalance, -1);
                        }
                        if (contractBalanceRowMap.containsKey(balanceType)) {
                            accountBalance = (null == contractBalanceRowMap.get(balanceType) ? BigDecimal.ZERO : new BigDecimal(contractBalanceRowMap.get(balanceType).toString())).add(accountBalance);
                        }
                        contractBalanceRowMap.put(balanceType, accountBalance);
                        contractBalanceRowMap.put(amountType, accountBalance);
                        if (!contractBalanceRowMap.containsKey("business_code") && !ListUtil.toList("1122.07", "1122.06", "1221.04").contains(accountEntity.getAccountCode())) {
                            businceeCode = accountEntity.getBusinessCode();
                        }
                    }
                }
                contractBalanceRowMap.put("business_code", businceeCode);
                if (insertFlage){
                    //加入期初数据
                    contractBalanceService.insertMap(contractBalanceRowMap);
                    contractBalanceLatestService.insertMap(contractBalanceRowMap);
                }else{
                    log.info("找不到任何科目，跳过, key:{}", key);
                }
                log.info("单合同数据同步完成, key:{}, percent:{}", key, NumberUtil.formatPercent(i / totalSize, 2));
                i++;
            } catch (Exception e) {
                log.info("单笔合同异常, key:{}, errorMessage:{}", entityMapEntry.getKey(), e.getMessage());
            }
        }
        long end = System.currentTimeMillis();
        log.info("end period:{}, size:{},cost:{}", periodCode, entityList.size(), (end - start));
        return Boolean.TRUE;
    }

    @Override
    @Async
    public void asyncFinHubVoucherBalance(CountDownLatch countDownLatch, Map<String, List<KingdeeVoucherEntryInnerDTO>> voucherEntryDTOMap, List<String> keyList, Integer periodCode, String voucherDate, Map<String, AccountEntity> accountFundMap, List<AccountEntity> accountEntityList) {
        int count = 1;
        double totalSize = NumberUtil.toDouble(keyList.size());
        log.info("asyncFinHubVoucherBalance start, keySize:{}", totalSize);
        LocalDateTime voucherDateTime = LocalDateTimeUtil.parse(voucherDate, "yyyy-MM-dd");
        String businessCode = "ZLYW";
        for (String key : keyList) {
            try {
                //获取一个作为凭证头
                List<KingdeeVoucherEntryInnerDTO> voucherEntryInnerDTOList = voucherEntryDTOMap.get(key);
                KingdeeVoucherEntryInnerDTO innerDTO = voucherEntryInnerDTOList.get(0);
                VoucherEntity voucherEntity = new VoucherEntity();
//                AccountEntity voucherAccountEntry = accountFundMap.get(innerDTO.getAccountCode());
//                if (voucherAccountEntry != null){
//                    businessCode = voucherAccountEntry.getBusinessCode();
//                }
                businessCode = getBusinessCode(voucherEntryInnerDTOList, accountFundMap);
                voucherEntity.setBusinessCode(businessCode);
//                voucherEntity.setBusinessName("租赁业务");
                voucherEntity.setSystemCode("EAS");
                voucherEntity.setSource("EAS");
                voucherEntity.setSceneCode("SGPZ");
                voucherEntity.setSceneName("手工凭证");
                voucherEntity.setContractCode(innerDTO.getContractCode());
//                voucherEntity.setContractName(innerDTO.getContractName());
                voucherEntity.setClientCode(innerDTO.getClientCode());
//                voucherEntity.setClientName(innerDTO.getClientName());
                voucherEntity.setOrgId(innerDTO.getOrgId());
//                voucherEntity.setOrgName(innerDTO.getOrgName());
                voucherEntity.setVoucherType("03");
                voucherEntity.setSignCompany(innerDTO.getOrgId());

                voucherEntity.setBusinessDate(voucherDateTime);
                voucherEntity.setVoucherDate(voucherDateTime);
                voucherEntity.setVoucherNum(null);
                voucherEntity.setCurrency(innerDTO.getCurrencyCode());
                voucherEntity.setVoucherSummary("期初数据导入");
                voucherEntity.setPeriodCode(periodCode);

                List<VoucherEntryEntity> voucherEntryEntityList = new ArrayList<>();
                Map<String, Object> fundTypeMap = new HashMap<>();
                for (KingdeeVoucherEntryInnerDTO entryInnerDTO : voucherEntryInnerDTOList) {
                    AccountEntity accountEntity = accountFundMap.get(entryInnerDTO.getAccountCode());
                    if (accountEntity == null) {
                        log.error("not found fundType! contractCode:{}, accountCode:{}", entryInnerDTO.getContractCode(), entryInnerDTO.getAccountCode());
                        continue;
                    }
                    VoucherEntryEntity entryEntity = new VoucherEntryEntity();
//                    entryEntity.setRelateBankFlag("1");
                    entryEntity.setClientCode(entryInnerDTO.getClientCode());
//                    entryEntity.setClientName(entryInnerDTO.getClientName());
                    if (StrUtil.isNotBlank(entryInnerDTO.getContractCode())) {
                        entryEntity.setContractFlag("1");
                    }
                    if (StrUtil.isNotBlank(entryInnerDTO.getClientCode())) {
                        entryEntity.setClientFlag("1");
                    }
                    entryEntity.setContractCode(entryInnerDTO.getContractCode());
//                    entryEntity.setContractName(entryInnerDTO.getContractName());
                    entryEntity.setAccountCode(entryInnerDTO.getAccountCode());
                    entryEntity.setAccountName(entryInnerDTO.getAccountName());
                    entryEntity.setDebitCreditType(entryInnerDTO.getDebitCreditType());
                    entryEntity.setAccountPeriod(periodCode);
                    entryEntity.setPeriodCode(periodCode);
                    entryEntity.setDebitAmount(entryInnerDTO.getDebitAmount());
                    entryEntity.setCreditAmount(entryInnerDTO.getCreditAmount());
                    entryEntity.setBillContractCode(entryInnerDTO.getBillContractCode());

                    BigDecimal finalAmount = null;
                    if ("DR".equals(accountEntity.getDebitCreditType())) {
                        //余额方向为借： 余额 = 借 - 贷
                        //余额方向为贷： 余额 = 贷 - 借
                        finalAmount = NumberUtil.sub(entryInnerDTO.getDebitAmount(), entryInnerDTO.getCreditAmount());
                    } else {
                        finalAmount = NumberUtil.sub(entryInnerDTO.getCreditAmount(), entryInnerDTO.getDebitAmount());
                    }
                    entryEntity.setFundType(accountEntity.getFundType());
                    if (fundTypeMap.containsKey(accountEntity.getFundType())) {
                        finalAmount = (null == fundTypeMap.get(accountEntity.getFundType()) ? BigDecimal.ZERO : new BigDecimal(fundTypeMap.get(accountEntity.getFundType()).toString())).add(finalAmount);
                    }
                    fundTypeMap.put(accountEntity.getFundType(), finalAmount);
                    voucherEntryEntityList.add(entryEntity);
                }
                if (CollectionUtils.isNotEmpty(voucherEntryEntityList)) {
                    voucherService.save(voucherEntity);
                    voucherEntryEntityList.forEach(e -> e.setVoucherId(voucherEntity.getId()));
                    voucherEntryService.saveBatch(voucherEntryEntityList);
                    contractBalanceService.insertContractBalance(
                            "SGPZ",
                            "EAS",
                            voucherEntity.getId(),
                            businessCode,
                            accountEntityList,
                            fundTypeMap,
                            contractBalanceLatestService.getLastBalanceMap(businessCode, innerDTO.getOrgId(), innerDTO.getClientCode(), innerDTO.getContractCode(), innerDTO.getBillContractCode()),
                            innerDTO.getOrgId(),
                            innerDTO.getContractCode(),
                            innerDTO.getClientCode(),
                            voucherDate, null,
                            periodCode, innerDTO.getBillContractCode());
                    log.info("complete:{}", NumberUtil.formatPercent(count / totalSize, 2));
                } else {
                    log.error("voucher has no entry, voucherEntryKey:{} ", key);
                }
                //滚动新增/更新合同余额表
                count++;
            } catch (Exception e) {
                log.info("error. contractCode:{}", key, e);
            }
        }
        countDownLatch.countDown();
        log.info("complete all , totalSize:{}", totalSize);
    }


    private long generateVoucherNum(String voucherType, LocalDateTime dateTime) {
        //redis key : finhub-年月-凭证类型编码
        String key = "_voucher_num_" + dateTime.getYear() + dateTime.getMonthValue() + "_" + voucherType;
        return redisService.generate(key, REDIS_VOUCHER_NUM_EXPIRE);
    }

    private String getBusinessCode(List<KingdeeVoucherEntryInnerDTO> voucherEntryInnerDTOList, Map<String, AccountEntity> accountFundMap) {
        String businessCode = "ZLYW";
        for (KingdeeVoucherEntryInnerDTO innerDTO : voucherEntryInnerDTOList) {
            AccountEntity accountEntity = accountFundMap.get(innerDTO.getAccountCode());
            if (accountEntity != null && !ListUtil.toList("1122.07", "1122.06", "1221.04").contains(accountEntity.getAccountCode())) {
                businessCode = accountEntity.getBusinessCode();
                break;
            }
        }
        return businessCode;
    }

    private String getBusinessCodeForSignle(List<KingdeeVoucherEntryEntity> entryEntityList, Map<String, AccountEntity> accountFundMap) {
        String businessCode = "ZLYW";
        for (KingdeeVoucherEntryEntity innerDTO : entryEntityList) {
            AccountEntity accountEntity = accountFundMap.get(innerDTO.getAccountCode());
            if (accountEntity != null && !ListUtil.toList("1122.07", "1122.06", "1221.04").contains(accountEntity.getAccountCode())) {
                businessCode = accountEntity.getBusinessCode();
                break;
            }
        }
        return businessCode;
    }

    @Override
    @Async
    public Boolean syncFinHubVoucherDetail(Integer periodCode) {
        //1.查询金蝶凭证
        List<KingdeeVoucherEntity> kingdeeVoucherEntityList = itGlVoucherService.selectVoucherByPeriod(periodCode);
        log.info("查询单月凭证完成，总数量:{}", kingdeeVoucherEntityList.size());

        //2.查询科目配置
        List<AccountEntity> accountEntityList = accountService.list(Wrappers.<AccountEntity>lambdaQuery());
        Map<String, AccountEntity> accountFundMap = accountEntityList.stream().collect(Collectors.toMap(e -> e.getAccountCode(), e -> e, (e1,e2)->e1));

        //3.分组并行
        int pageSize = 1000;
        List<List<KingdeeVoucherEntity>> pageList = ListUtil.partition(kingdeeVoucherEntityList, pageSize);
        log.info("分组完成，每组凭证个数：{}, 分组个数:{}", pageSize, pageList.size());
        for (int i = 0; i < pageList.size(); i++) {
            List<KingdeeVoucherEntity> subList = pageList.get(i);
//            IKingdeeDataSyncService kingdeeDataSyncService = SpringUtils.getBean(IKingdeeDataSyncService.class);
            this.asyncFinHubVoucherDetail(subList, accountFundMap, accountEntityList, periodCode, false);
        }
        return Boolean.TRUE;
    }

    @Override
    @Async
    public Boolean syncFinHubVoucherDetailV2(String periodCodes,String contractCodes) {
        log.info("开始同步， periodCodes:{},合同编码：{}", periodCodes,contractCodes);
        List<String> contractCodeList = Lists.newArrayList();
        if (StringUtils.isNotEmpty(contractCodes)) {
            contractCodeList = StrUtil.split(contractCodes, ",");
        }
        log.info("合同编码：{}",JSON.toJSON(contractCodeList));
        List<String> periodCodeList = StrUtil.split(periodCodes, "|");
        //查询科目配置
        List<AccountEntity> accountEntityList = accountService.list(Wrappers.<AccountEntity>lambdaQuery());
        Map<String, AccountEntity> accountFundMap = accountEntityList.stream().collect(Collectors.toMap(e -> e.getAccountCode(), e -> e, (e1,e2)->e1));
        for (String periodCodeStr : periodCodeList) {
            Integer periodCode = NumberUtil.parseInt(periodCodeStr);
            List<String> dayList = DateUtils.getDayListOfMonth(periodCodeStr);
            for (String voucherDate: dayList){
                //1.查询金蝶所有凭证分录
                List<KingdeeVoucherEntryEntity> kingdeeVoucherEntryEntityList = itGlVoucherService.selectVoucherEntryByPeriodCode(periodCode, voucherDate, 5,contractCodeList);
                log.info("查询到所有金蝶凭证分录，periodCode:{}, voucherDate:{}, size:{}", periodCode, voucherDate, kingdeeVoucherEntryEntityList.size());
                //2.按合同编号分组
                Map<String, List<KingdeeVoucherEntryEntity>> contractEntryListMap = kingdeeVoucherEntryEntityList.stream().collect(Collectors.groupingBy(
                        e -> e.getOrgId() + "|" + e.getContractCode() + "|" + e.getClientCode() + "|" + e.getBillContractCode()
                ));
                log.info("按机构、合同、客户分组完成，periodCode:{}, voucherDate:{}, 分组后size:{}", periodCode, voucherDate, contractEntryListMap.size());
                //3. 根据key的hashCode进行分组
                Map<Integer, List<String>> voucherEntryKeyMap = contractEntryListMap.keySet().stream().collect(Collectors.groupingBy(e -> Math.abs(e.hashCode()) % 50));
                CountDownLatch countDownLatch = new CountDownLatch(voucherEntryKeyMap.size());
                for (Map.Entry<Integer, List<String>> keyEntry : voucherEntryKeyMap.entrySet()) {
                    List<String> groupKeyList = keyEntry.getValue();
                    log.info("分组结果 hashKey:{}, dataSize:{}", keyEntry.getKey(), groupKeyList.size());
                    IKingdeeDataSyncService kingdeeDataSyncService = SpringUtils.getBean(IKingdeeDataSyncService.class);
                    kingdeeDataSyncService.asyncFinHubVoucherBalanceDetailV2(countDownLatch, contractEntryListMap, groupKeyList, accountFundMap, accountEntityList, periodCode);
                }
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                log.info("按天凭证同步完成，periodCode:{}, voucherDate:{}, size:{}", periodCode, voucherDate, kingdeeVoucherEntryEntityList.size());
            }
        }
        return Boolean.TRUE;
    }

    @Override
    @Async
    public Boolean syncFinHubVoucherDetailByDayV2(Integer periodCode, String voucherDate,String contractCodes) {
        log.info("开始同步， periodCode:{}, voucherDate:{}", periodCode, voucherDate);
        List<String> contractCodeList = Lists.newArrayList();
        if (StringUtils.isNotEmpty(contractCodes)) {
            contractCodeList = Arrays.asList(contractCodes.split(","));
        }
        //查询科目配置
        List<AccountEntity> accountEntityList = accountService.list(Wrappers.<AccountEntity>lambdaQuery());
        Map<String, AccountEntity> accountFundMap = accountEntityList.stream().collect(Collectors.toMap(e -> e.getAccountCode(), e -> e, (e1,e2)->e1));
        //1.查询金蝶所有凭证分录
        List<KingdeeVoucherEntryEntity> kingdeeVoucherEntryEntityList = itGlVoucherService.selectVoucherEntryByPeriodCode(periodCode, voucherDate, null,contractCodeList);
        log.info("查询到所有金蝶凭证分录，periodCode:{}, voucherDate:{}, size:{}", periodCode, voucherDate, kingdeeVoucherEntryEntityList.size());
        //2.按合同编号分组
        Map<String, List<KingdeeVoucherEntryEntity>> contractEntryListMap = kingdeeVoucherEntryEntityList.stream().collect(Collectors.groupingBy(
                e -> e.getOrgId() + "|" + e.getContractCode() + "|" + e.getClientCode() + "|" + e.getBillContractCode()
        ));
        log.info("按机构、合同、客户分组完成，periodCode:{}, voucherDate:{}, 分组后size:{}", periodCode, voucherDate, contractEntryListMap.size());
        //3. 根据key的hashCode进行分组
        Map<Integer, List<String>> voucherEntryKeyMap = contractEntryListMap.keySet().stream().collect(Collectors.groupingBy(e -> Math.abs(e.hashCode()) % 50));
        CountDownLatch countDownLatch = new CountDownLatch(voucherEntryKeyMap.size());
        for (Map.Entry<Integer, List<String>> keyEntry : voucherEntryKeyMap.entrySet()) {
            List<String> groupKeyList = keyEntry.getValue();
            log.info("分组结果 hashKey:{}, dataSize:{}", keyEntry.getKey(), groupKeyList.size());
            IKingdeeDataSyncService kingdeeDataSyncService = SpringUtils.getBean(IKingdeeDataSyncService.class);
            kingdeeDataSyncService.asyncFinHubVoucherBalanceDetailV2(countDownLatch, contractEntryListMap, groupKeyList, accountFundMap, accountEntityList, periodCode);
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("按天凭证同步完成，periodCode:{}, voucherDate:{}, size:{}", periodCode, voucherDate, kingdeeVoucherEntryEntityList.size());
        return Boolean.TRUE;
    }

    @Override
    @Async
    public Boolean syncFinHubVoucherDetailFromMiddleTable(String voucherDate,CountDownLatch countDownLatch) {
        //查询科目配置
        List<AccountEntity> accountEntityList = accountService.list(Wrappers.<AccountEntity>lambdaQuery());
        Map<String, AccountEntity> accountFundMap = accountEntityList.stream().collect(Collectors.toMap(e -> e.getAccountCode(), e -> e, (e1,e2)->e1));
        //1.查询中间表的数据
        List<EasVoucherHeadEntity> middleHeaderList = easVoucherHeadService.selectMidVoucherHeaderByDate(voucherDate);
        double totalSize = middleHeaderList.size();
        log.info("开始同步中间表凭证， voucherDate:{}, size:{}", voucherDate, middleHeaderList.size());
        int i = 1;
        for (EasVoucherHeadEntity headEntity: middleHeaderList){
            //2.根据中间表的凭证ID查询金蝶凭证头
            KingdeeVoucherEntity kingdeeVoucherEntity = itGlVoucherService.selectVoucherByEasId(headEntity.getEasbzcode());
            if (kingdeeVoucherEntity == null){
                log.info("查询到金蝶凭证头为空，跳过， easVoucherId:{}", headEntity.getEasbzcode());
                continue;
            }
            //3.根据中间表的凭证ID查询金蝶凭证分录
            List<KingdeeVoucherEntryEntity> voucherEntryEntityList = itGlVoucherService.selectVoucherEntryByPeriodAndDate(kingdeeVoucherEntity.getEasId());
            if (CollectionUtils.isEmpty(voucherEntryEntityList)){
                log.info("查询到金蝶凭证分录为空，跳过， easVoucherId:{}", headEntity.getEasbzcode());
                continue;
            }
            VoucherEntity voucherEntity = new VoucherEntity();
            String businessCode = getBusinessCodeForSignle(voucherEntryEntityList, accountFundMap);
            voucherEntity.setBusinessCode(businessCode);
            voucherEntity.setId(IdWorker.getId());
            String systemCode = headEntity.getSystem();
            voucherEntity.setSystemCode(systemCode);
            voucherEntity.setSource(headEntity.getSystem());
            voucherEntity.setSceneCode(SceneMappingUtil.getMappingSceneCode(headEntity.getSystem(), headEntity.getModelname()));
            voucherEntity.setContractCode(headEntity.getContractid());
            voucherEntity.setVoucherSummary(headEntity.getDescription());
            voucherEntity.setVoucherType(VoucherTypeMapUtil.getVoucherTypeCode(headEntity.getVouchertype()));
            voucherEntity.setOrgId(kingdeeVoucherEntity.getOrgId());
            voucherEntity.setSignCompany(kingdeeVoucherEntity.getOrgId());
            voucherEntity.setBusinessDate(kingdeeVoucherEntity.getBusinessDate());
            voucherEntity.setVoucherDate(kingdeeVoucherEntity.getVoucherDate());
            voucherEntity.setVoucherNum(null);
            voucherEntity.setCurrency(kingdeeVoucherEntity.getCurrencyCode());
            voucherEntity.setEasVoucherId(kingdeeVoucherEntity.getEasId());
            voucherEntity.setPeriodCode(kingdeeVoucherEntity.getPeriodCode());

            //4.保存财务中台凭证头
            voucherService.save(voucherEntity);

            //按合同编号分组
            Map<String, List<KingdeeVoucherEntryEntity>> contractEntryListMap = voucherEntryEntityList.stream().collect(Collectors.groupingBy(
                    e -> e.getOrgId() + "|" + e.getContractCode() + "|" + e.getClientCode() + "|" + e.getBillContractCode()
            ));
            for (Map.Entry<String, List<KingdeeVoucherEntryEntity>> contractEntryListMapEntry : contractEntryListMap.entrySet()) {
                List<String> keyList = StrUtil.split(contractEntryListMapEntry.getKey(), "|");
                String orgId = keyList.get(0);
                String contractCode = keyList.get(1);
                String clientCode = keyList.get(2);
                String billContractCode = keyList.get(3);

                String easVoucherId = kingdeeVoucherEntity.getEasId();
                Map<String, Object> fundTypeMap = new HashMap<>();
                List<VoucherEntryEntity> finhubEntryList = new ArrayList<>();

                for (KingdeeVoucherEntryEntity voucherEntryEntity : contractEntryListMapEntry.getValue()) {
                    AccountEntity accountEntity = accountFundMap.get(voucherEntryEntity.getAccountCode());
                    VoucherEntryEntity entryEntity = new VoucherEntryEntity();
                    entryEntity.setVoucherId(voucherEntity.getId());
                    entryEntity.setPeriodCode(voucherEntryEntity.getPeriodCode());
                    entryEntity.setContractCode(voucherEntryEntity.getContractCode());
                    entryEntity.setBillContractCode(voucherEntryEntity.getBillContractCode());
                    entryEntity.setClientCode(voucherEntryEntity.getClientCode());
                    entryEntity.setAccountCode(voucherEntryEntity.getAccountCode());
                    entryEntity.setAccountName(voucherEntryEntity.getAccountName());
                    if (StrUtil.isNotBlank(voucherEntryEntity.getContractCode())) {
                        entryEntity.setContractFlag("1");
                    }
                    if (StrUtil.isNotBlank(voucherEntryEntity.getClientCode())) {
                        entryEntity.setClientFlag("1");
                    }
                    entryEntity.setAccountPeriod(voucherEntryEntity.getPeriodCode());
                    entryEntity.setPeriodCode(voucherEntryEntity.getPeriodCode());
                    entryEntity.setDebitCreditType(voucherEntryEntity.getDebitCreditType());
                    entryEntity.setCreditAmount(voucherEntryEntity.getCreditAmount());
                    entryEntity.setDebitAmount(voucherEntryEntity.getDebitAmount());

                    entryEntity.setEasVoucherId(easVoucherId);
                    if (accountEntity != null) {
                        BigDecimal finalAmount = null;
                        if ("DR".equals(accountEntity.getDebitCreditType())) {
                            //余额 = 借方金额 - 贷方金额
                            finalAmount = NumberUtil.sub(voucherEntryEntity.getDebitAmount(), voucherEntryEntity.getCreditAmount());
                        } else {
                            finalAmount = NumberUtil.sub(voucherEntryEntity.getCreditAmount(), voucherEntryEntity.getDebitAmount());
                        }
                        entryEntity.setFundType(accountEntity.getFundType());
                        if (fundTypeMap.containsKey(accountEntity.getFundType())) {
                            fundTypeMap.put(accountEntity.getFundType(), NumberUtil.add(MapUtil.get(fundTypeMap, accountEntity.getFundType(), BigDecimal.class), finalAmount));
                        } else {
                            fundTypeMap.put(accountEntity.getFundType(), finalAmount);
                        }
                    }
                    finhubEntryList.add(entryEntity);
                }
                voucherEntryService.saveBatch(finhubEntryList);//批量保存凭证行
                String balanceContractCode = StrUtil.equals(contractCode, "null") ? null : contractCode;
                String balanceClientCode = StrUtil.equals(clientCode, "null") ? null : clientCode;
                String balanceBillContractCode = StrUtil.equals(billContractCode, "null") ? null : billContractCode;
                if (fundTypeMap.size() > 0) {
                    Map<String, Object> lastBalanceMap = contractBalanceLatestService.getLastBalanceMap(businessCode, headEntity.getOrgnumber(),  balanceClientCode, balanceContractCode, balanceBillContractCode);
                    contractBalanceService.insertContractBalance(
                            SceneMappingUtil.getMappingSceneCode(headEntity.getSystem(), headEntity.getModelname()),
                            headEntity.getSystem(),
                            voucherEntity.getId(),
                            businessCode,
                            accountEntityList,
                            fundTypeMap,
                            lastBalanceMap,
                            orgId,
                            balanceContractCode,
                            balanceClientCode,
                            LocalDateTimeUtil.format(headEntity.getFinancialdate(), "yyyy-MM-dd"),
                            easVoucherId,
                            kingdeeVoucherEntity.getPeriodCode(), balanceBillContractCode);
                }
            }
            log.info("单凭证保存成功，凭证ID：{}, 进度:{} ", kingdeeVoucherEntity.getEasId(), NumberUtil.formatPercent(i/totalSize, 2));
            i++;
        }
        if (null != countDownLatch) {
            countDownLatch.countDown();
        }
        log.info("同步中间表凭证结束， voucherDate:{}", voucherDate);
        return true;
    }

    @Async
    @Override
    public Boolean syncFinHubVoucherDetailFromMiddleTablePeriodCodeV3(String periodCodes) {
        List<String> periodCodeList = StrUtil.split(periodCodes, "|");
        for (String periodCodeStr : periodCodeList) {
            List<String> dayList = DateUtils.getDayListOfMonth(periodCodeStr);
            Integer periodCode = NumberUtil.parseInt(periodCodeStr);
            CountDownLatch countDownLatch = new CountDownLatch(dayList.size());
            for (String day : dayList) {
                IKingdeeDataSyncService kingdeeDataSyncService = SpringUtils.getBean(IKingdeeDataSyncService.class);
                kingdeeDataSyncService.syncFinHubVoucherDetailFromMiddleTable(day, countDownLatch);
            }
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return Boolean.TRUE;
    }

    @Override
    @Async
    public Boolean syncKingdeeClientNameAll() {
        //1.查询所有合同
        List<ClientEntity> clientEntityList = clientService.selectAllClient();
        log.info("查询到所有客户, size:{}", clientEntityList.size());
        double totalSize = clientEntityList.size();
        int i = 1;
        for (ClientEntity entity: clientEntityList){
            //2.查询金蝶合同数据
            List<TBdCustomer> customerList = itGlVoucherService.selectCustomerById(entity.getClientCode());
            if (CollectionUtils.isNotEmpty(customerList)){
                TBdCustomer customer = customerList.get(0);
                entity.setClientName(customer.getFnameL2());
                clientService.updateById(entity);
            }
            log.info("单客户同步完成， clientCode:{}, 进度：{}", entity.getClientCode(), NumberUtil.formatPercent(i/totalSize, 2));
            i++;
        }
        return Boolean.TRUE;
    }

    @Override
    public void asyncFinHubVoucherDetail(List<KingdeeVoucherEntity> voucherEntityList, Map<String, AccountEntity> accountFundMap, List<AccountEntity> accountEntityList, Integer periodCode, boolean isLastPage) {
        double totalSize = voucherEntityList.size();
        int i = 1;
        //1.查询中间表凭证头
        List<EasVoucherHeadEntity> headEntityList = easVoucherHeadService.selectMidVoucherHeaderByEasIds(voucherEntityList.stream().map(e -> e.getEasId()).collect(Collectors.toList()));
        log.info("已查询到中间表凭证头， 总查询个数：{}， 查询到凭证头个数:{}.", totalSize, headEntityList.size());
        Map<String, EasVoucherHeadEntity> headEntityMap = headEntityList.stream().collect(Collectors.toMap(e -> e.getEasbzcode(), e -> e, (k1, k2) -> k1));
        for (KingdeeVoucherEntity kingdeeVoucherEntity : voucherEntityList) {
            List<VoucherEntryEntity> finhubEntryList = new ArrayList<>();
            try {
                EasVoucherHeadEntity headEntity = headEntityMap.get(kingdeeVoucherEntity.getEasId());
                //2.查询凭证行
                List<KingdeeVoucherEntryEntity> voucherEntryEntityList = itGlVoucherService.selectVoucherEntryByPeriodAndDate(kingdeeVoucherEntity.getEasId());
                //3.构造凭证头
                VoucherEntity voucherEntity = new VoucherEntity();
                String businessCode = getBusinessCodeForSignle(voucherEntryEntityList, accountFundMap);
//                voucherEntity.setBusinessCode(businessCode);
                voucherEntity.setId(IdWorker.getId());
                String systemCode = "EAS";
                if (headEntity != null) {
                    voucherEntity.setSystemCode(headEntity.getSystem());
                    voucherEntity.setSource(headEntity.getSystem());
                    voucherEntity.setSceneCode(SceneMappingUtil.getMappingSceneCode(headEntity.getSystem(), headEntity.getModelname()));
                    voucherEntity.setContractCode(headEntity.getContractid());
                    voucherEntity.setVoucherSummary(headEntity.getDescription());
                    voucherEntity.setVoucherType(VoucherTypeMapUtil.getVoucherTypeCode(headEntity.getVouchertype()));
                    systemCode = headEntity.getSystem();
                } else {
                    voucherEntity.setSystemCode("EAS");
                    voucherEntity.setSource("EAS");
                    voucherEntity.setSceneCode("SGPZ");
                    voucherEntity.setVoucherSummary("期初数据导入");
                    voucherEntity.setVoucherType("03");
                }
                voucherEntity.setOrgId(kingdeeVoucherEntity.getOrgId());
                voucherEntity.setSignCompany(kingdeeVoucherEntity.getOrgId());
                voucherEntity.setBusinessDate(kingdeeVoucherEntity.getBusinessDate());
                voucherEntity.setVoucherDate(kingdeeVoucherEntity.getVoucherDate());
                voucherEntity.setVoucherNum(null);
                voucherEntity.setCurrency("CNY");
                Map<String, Object> fundTypeMap = new HashMap<>();
                //分组
                Map<String, List<KingdeeVoucherEntryEntity>> entryEntityMap = voucherEntryEntityList.stream().collect(Collectors.groupingBy(e -> {
                    String contractCode = e.getContractCode();
                    String clientCode = e.getClientCode();
                    return (StrUtil.isBlank(contractCode) ? "null" : contractCode) + "_" + (StrUtil.isBlank(clientCode) ? "null" : clientCode);
                }));
                CountDownLatch countDownLatch = new CountDownLatch(entryEntityMap.size());
                for (Map.Entry<String, List<KingdeeVoucherEntryEntity>> entryEntityEntry : entryEntityMap.entrySet()) {
                    IKingdeeDataSyncService kingdeeDataSyncService = SpringUtils.getBean(IKingdeeDataSyncService.class);
                    //异步执行
                    kingdeeDataSyncService.executeFinhubVoucherEntry(entryEntityEntry,
                            countDownLatch,
                            accountFundMap,
                            voucherEntity,
                            finhubEntryList,
                            businessCode,
                            systemCode,
                            accountEntityList,
                            kingdeeVoucherEntity.getOrgId(),
                            kingdeeVoucherEntity.getVoucherDate(),
                            periodCode);
                }
                countDownLatch.await();
                voucherService.save(voucherEntity);
                //批量保存
                voucherEntryService.saveBatch(finhubEntryList);
                log.info("同步单个凭证完成, voucherId:{}, 凭证个数:{}, 完成比例：{}", kingdeeVoucherEntity.getEasId(), entryEntityMap.size(), NumberUtil.formatPercent(i / totalSize, 2));
                i++;
            } catch (Exception e) {
                log.error("同步凭证异常, voucherId:{}", kingdeeVoucherEntity.getEasId(), e);
            }
        }
        log.info("凭证批次同步完成, size:{}, periodCode:{}, isLastPage:{}", voucherEntityList.size(), periodCode, isLastPage);
//        if (isLastPage){
//            //如果是最后一笔，则调用下一笔
//            Map<String, Object> httpParamMap = new HashMap<>();
//            Integer nextPeriod = PeriodCodeUtil.getNextPeriodCode(periodCode);
//            httpParamMap.put("periodCode", nextPeriod);
//            String result = HttpUtil.post("http://localhost:8203/financial/kingdee/syncFinHubVoucherDetail", httpParamMap, 3600 * 1000);
//            log.info("已触发下一个会计期间, 当前期间:{}, 下一个期间:{}, result:{}", periodCode, nextPeriod, result);
//        }
    }

    @Async
    @Override
    public void asyncFinHubVoucherBalanceDetailV2(CountDownLatch countDownLatch, Map<String, List<KingdeeVoucherEntryEntity>> contractEntryListMap, List<String> groupKeyList, Map<String, AccountEntity> accountFundMap, List<AccountEntity> accountEntityList, Integer periodCode) {
        double totalSize = groupKeyList.size();
        //循环遍历key集合
        int i = 1;
        for (String key : groupKeyList) {
            List<String> keyList = StrUtil.split(key, "|");
            String orgId = keyList.get(0);
            String contractCode = keyList.get(1);
            String clientCode = keyList.get(2);
            String billContractCode = keyList.get(3);
            if (StrUtil.equals(contractCode, "null") && StrUtil.equals(clientCode, "null")){
                log.info("客户和合同都为空，跳过, periodCode:{}", periodCode);
                continue;
            }
            //拿到合同对应的凭证行
            List<KingdeeVoucherEntryEntity> entryEntityList = contractEntryListMap.get(key);
            //凭证行里面，再根据金蝶voucherId分组, 同一个
            Map<String, List<KingdeeVoucherEntryEntity>> easIdVoucherEntryMap = entryEntityList.stream().collect(Collectors.groupingBy(KingdeeVoucherEntryEntity::getVoucherEasId));

            for (Map.Entry<String, List<KingdeeVoucherEntryEntity>> voucherEntyMapEntry : easIdVoucherEntryMap.entrySet()) {
                String easVoucherId = voucherEntyMapEntry.getKey();
                Map<String, Object> fundTypeMap = new HashMap<>();
                List<VoucherEntryEntity> finhubEntryList = new ArrayList<>();
                String businessCode = getBusinessCodeForSignle(voucherEntyMapEntry.getValue(), accountFundMap);
                for (KingdeeVoucherEntryEntity voucherEntryEntity : voucherEntyMapEntry.getValue()) {
                    AccountEntity accountEntity = accountFundMap.get(voucherEntryEntity.getAccountCode());
                    VoucherEntryEntity entryEntity = new VoucherEntryEntity();
                    entryEntity.setPeriodCode(periodCode);
                    entryEntity.setContractCode(voucherEntryEntity.getContractCode());
                    entryEntity.setBillContractCode(voucherEntryEntity.getBillContractCode());
                    entryEntity.setClientCode(voucherEntryEntity.getClientCode());
                    entryEntity.setAccountCode(voucherEntryEntity.getAccountCode());
                    entryEntity.setAccountName(voucherEntryEntity.getAccountName());
                    if (StrUtil.isNotBlank(voucherEntryEntity.getContractCode())) {
                        entryEntity.setContractFlag("1");
                    }
                    if (StrUtil.isNotBlank(voucherEntryEntity.getClientCode())) {
                        entryEntity.setClientFlag("1");
                    }
                    entryEntity.setAccountPeriod(voucherEntryEntity.getPeriodCode());
                    entryEntity.setPeriodCode(voucherEntryEntity.getPeriodCode());
                    entryEntity.setDebitCreditType(voucherEntryEntity.getDebitCreditType());
                    entryEntity.setCreditAmount(voucherEntryEntity.getCreditAmount());
                    entryEntity.setDebitAmount(voucherEntryEntity.getDebitAmount());

                    entryEntity.setEasVoucherId(easVoucherId);
                    if (accountEntity != null) {
                        BigDecimal finalAmount = null;
                        if ("DR".equals(accountEntity.getDebitCreditType())) {
                            //余额 = 借方金额 - 贷方金额
                            finalAmount = NumberUtil.sub(voucherEntryEntity.getDebitAmount(), voucherEntryEntity.getCreditAmount());
                        } else {
                            finalAmount = NumberUtil.sub(voucherEntryEntity.getCreditAmount(), voucherEntryEntity.getDebitAmount());
                        }
                        entryEntity.setFundType(accountEntity.getFundType());
                        if (fundTypeMap.containsKey(accountEntity.getFundType())) {
                            fundTypeMap.put(accountEntity.getFundType(), NumberUtil.add(MapUtil.get(fundTypeMap, accountEntity.getFundType(), BigDecimal.class), finalAmount));
                        } else {
                            fundTypeMap.put(accountEntity.getFundType(), finalAmount);
                        }
                    }
                    finhubEntryList.add(entryEntity);
                }
                voucherEntryService.saveBatch(finhubEntryList);//批量保存凭证行
                String balanceContractCode = StrUtil.equals(contractCode, "null") ? null : contractCode;
                String balanceClientCode = StrUtil.equals(clientCode, "null") ? null : clientCode;
                String balanceBillContractCode = StrUtil.equals(billContractCode, "null") ? null : billContractCode;
                if (fundTypeMap.size() > 0) {
                    Map<String, Object> lastBalanceMap = contractBalanceLatestService.getLastBalanceMap(businessCode, orgId,  balanceClientCode, balanceContractCode, balanceBillContractCode);
                    contractBalanceService.insertContractBalance(
                            null,
                            null,
                            null,
                            businessCode,
                            accountEntityList,
                            fundTypeMap,
                            lastBalanceMap,
                            orgId,
                            balanceContractCode,
                            balanceClientCode,
                            null,
                            easVoucherId,
                            periodCode, balanceBillContractCode);
                }
            }
            log.info("单笔合同完成，key:{}, 凭证行个数：{}, 比例：{}", key, entryEntityList.size(), NumberUtil.formatPercent(i/totalSize, 2));
            i++;
        }
        countDownLatch.countDown();
    }


    @Override
    @Async
    public void executeFinhubVoucherEntry(Map.Entry<String, List<KingdeeVoucherEntryEntity>> entryEntityEntry,
                                          CountDownLatch countDownLatch,
                                          Map<String, AccountEntity> accountFundMap,
                                          VoucherEntity voucherEntity,
                                          List<VoucherEntryEntity> finhubEntryList,
                                          String businessCode,
                                          String systemCode,
                                          List<AccountEntity> accountEntityList,
                                          String orgId,
                                          LocalDateTime voucherDate,
                                          Integer periodCode) {
        Map<String, Object> fundTypeMap = new HashMap<>();
        String key = entryEntityEntry.getKey();
        String contractCode = StrUtil.split(key, '_').get(0);
        String clientCode = StrUtil.split(key, '_').get(1);
        if ("null".equals(contractCode)) {
            contractCode = null;
        }
        if ("null".equals(clientCode)) {
            clientCode = null;
        }
        for (KingdeeVoucherEntryEntity voucherEntryEntity : entryEntityEntry.getValue()) {
            AccountEntity accountEntity = accountFundMap.get(voucherEntryEntity.getAccountCode());
            VoucherEntryEntity entryEntity = new VoucherEntryEntity();
            entryEntity.setVoucherId(voucherEntity.getId());
            entryEntity.setContractCode(voucherEntryEntity.getContractCode());
            entryEntity.setClientCode(voucherEntryEntity.getClientCode());
            entryEntity.setAccountName(voucherEntryEntity.getAccountName());
            if (StrUtil.isNotBlank(voucherEntryEntity.getContractCode())) {
                entryEntity.setContractFlag("1");
            }
            if (StrUtil.isNotBlank(voucherEntryEntity.getClientCode())) {
                entryEntity.setClientFlag("1");
            }
            entryEntity.setAccountPeriod(voucherEntryEntity.getPeriodCode());
            entryEntity.setPeriodCode(voucherEntryEntity.getPeriodCode());
            entryEntity.setDebitCreditType(voucherEntryEntity.getDebitCreditType());
            entryEntity.setCreditAmount(voucherEntryEntity.getCreditAmount());
            entryEntity.setDebitAmount(voucherEntryEntity.getDebitAmount());


            if (accountEntity != null) {
                BigDecimal finalAmount = null;
                if ("DR".equals(accountEntity.getDebitCreditType())) {
                    //余额 = 借方金额 - 贷方金额
                    finalAmount = NumberUtil.sub(voucherEntryEntity.getDebitAmount(), voucherEntryEntity.getCreditAmount());
                } else {
                    finalAmount = NumberUtil.sub(voucherEntryEntity.getCreditAmount(), voucherEntryEntity.getDebitAmount());
                }
                entryEntity.setFundType(accountEntity.getFundType());
                fundTypeMap.put(accountEntity.getFundType(), finalAmount);
            }
            finhubEntryList.add(entryEntity);
        }
        if (fundTypeMap.size() > 0) {
            Map<String, Object> lastBalanceMap = contractBalanceLatestService.getLastBalanceMap(businessCode, orgId, clientCode, contractCode, null);
            contractBalanceService.insertContractBalance(
                    voucherEntity.getSceneCode(),
                    systemCode,
                    voucherEntity.getId(),
                    businessCode,
                    accountEntityList,
                    fundTypeMap,
                    lastBalanceMap,
                    orgId,
                    contractCode,
                    clientCode,
                    LocalDateTimeUtil.format(voucherDate, "yyyy-MM-dd"), null, periodCode, null);
        }
        countDownLatch.countDown();
    }

}
