package com.utfinancing.financehub.etl.financial.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.SpringUtils;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.utfinancing.financehub.etl.commveh.service.ICommercialVehicleDataService;
import com.utfinancing.financehub.etl.enums.SystemEnum;
import com.utfinancing.financehub.etl.financial.entity.*;
import com.utfinancing.financehub.etl.financial.enums.ExecuteStatusEnum;
import com.utfinancing.financehub.etl.financial.enums.MarginStatusEnum;
import com.utfinancing.financehub.etl.financial.enums.YesOrNoEnum;
import com.utfinancing.financehub.etl.financial.mapper.BankAccountMapper;
import com.utfinancing.financehub.etl.financial.mapper.FinanceOrgPeriodMapper;
import com.utfinancing.financehub.etl.financial.mapper.ReportFinanceDataMapper;
import com.utfinancing.financehub.etl.financial.model.dto.BankAccountDTO;
import com.utfinancing.financehub.etl.financial.model.dto.BankAccountQueryDTO;
import com.utfinancing.financehub.etl.financial.model.vo.BankAccountVO;
import com.utfinancing.financehub.etl.financial.model.vo.OrgCompanyVO;
import com.utfinancing.financehub.etl.financial.service.*;
import com.utfinancing.financehub.etl.kingdee.model.dto.CurrencyExchangeRateDTO;
import com.utfinancing.financehub.etl.kingdee.model.dto.OrgPeriodDTO;
import com.utfinancing.financehub.etl.kingdee.service.IReportKingdeeDataService;
import com.utfinancing.financehub.etl.micro.service.IMicroDataService;
import com.utfinancing.financehub.etl.passveh.service.IPassengerVehicleDataService;
import com.utfinancing.financehub.etl.platform.service.IPlatformDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
 * @Description :  BankAccount服务实现类
 * @Modified :
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ReportFinanceDataServiceImpl implements IReportFinanceDataService {

    private final ReportFinanceDataMapper reportFinanceDataMapper;

    @Resource
    private IFinanceOrgPeriodService financeOrgPeriodService;


    @Resource
    private IReportKingdeeDataService reportKingdeeDataService;

    @Resource
    private IReportPeriodSyncRecordService reportPeriodSyncRecordService;

    @Resource
    private IEasExchangeRateService easExchangeRateService;

    @Resource
    private ITaReclassificationService taReclassificationService;

    @Resource
    private ITaReclassificationDetailService taReclassificationDetailService;

    @Resource
    private IPlatformDataService platformDataService;

    @Resource
    private IMicroDataService microDataService;

    @Resource
    private ICommercialVehicleDataService commercialVehicleDataService;

    @Resource
    private IPassengerVehicleDataService passengerVehicleDataService;

    @Resource
    private IOrgCompanyService orgCompanyService;

    @Resource
    private IAccountAssistBalanceService accountAssistBalanceService;


    private final List<String> compareOrgIds = Arrays.asList("01-C0001","02-C0001","30001","80001");

    @Override
    public boolean compareOrgPeriod() {
        List<OrgPeriodDTO> kingdeeOrgPeriodList = reportKingdeeDataService.selectCurrentPeriodCodeByOrgIds(compareOrgIds);
        List<OrgPeriodDTO> financeOrgPeriodList = financeOrgPeriodService.selectCurrentPeriodCodeByOrgIds(compareOrgIds);

        boolean compareFlag = true;
        if(CollectionUtil.isEmpty(kingdeeOrgPeriodList)){
            throw new ServiceException("kingdee获取机构对应账期失败");
        }
        if(CollectionUtil.isEmpty(financeOrgPeriodList)){
            throw new ServiceException("中台获取机构对应账期失败");
        }
        if(kingdeeOrgPeriodList.size()!=compareOrgIds.size()){
            throw new ServiceException("kingdee存在机构获取对应账期失败");
        }

        if(financeOrgPeriodList.size()!=compareOrgIds.size()){
            throw new ServiceException("中台存在机构获取对应账期失败");
        }

        for(OrgPeriodDTO financeDto : financeOrgPeriodList){
            if(!compareFlag)
                break;
            String orgId = financeDto.getOrgId();
            String currentPeriod = financeDto.getPeriodCode();
            Optional<OrgPeriodDTO> op = kingdeeOrgPeriodList.stream().filter(y-> StringUtils.equals(y.getOrgId(), orgId)).findFirst();
            if(op.isPresent()){
                OrgPeriodDTO kingdeeDto = op.get();
                if(!StringUtils.equals(kingdeeDto.getPeriodCode(), currentPeriod)){
                    compareFlag = false;
                }
            }else{
                compareFlag = false;
            }
        }
        if(!compareFlag){
            log.info("存在机构在Kingdee和中台的当前账期不一样");
            return compareFlag;
        }
        LocalDateTime kingdeePeriod = LocalDateTimeUtil.parse(kingdeeOrgPeriodList.get(0).getPeriodCode());
        Integer periodCode = Integer.valueOf(kingdeePeriod.minusMonths(1).format(DateTimeFormatter.ofPattern("yyyyMM")));
        ReportPeriodSyncRecordEntity record = reportPeriodSyncRecordService.getOne(new LambdaQueryWrapper<ReportPeriodSyncRecordEntity>()
                .eq(ReportPeriodSyncRecordEntity::getPeriodCode, periodCode).eq(ReportPeriodSyncRecordEntity::getExecuteStatus, ExecuteStatusEnum.FINISH.getCode()));
        if(record!=null){
            log.info("period code:"+periodCode+" 已经做过同步");
            return false;
        }
        ReportPeriodSyncRecordEntity newRecord = new ReportPeriodSyncRecordEntity();
        newRecord.setPeriodCode(periodCode);
        newRecord.setExecuteStatus(ExecuteStatusEnum.INPROGRESS.getCode());
        newRecord.setStartTime(LocalDateTime.now());
        newRecord.setDelFlag("0");
        newRecord.setCreateBy("system");
        newRecord.setCreateTime(LocalDateTime.now());
        newRecord.setUpdateBy("system");
        newRecord.setUpdateTime(LocalDateTime.now());
        reportPeriodSyncRecordService.save(newRecord);



        log.info("同步contract_balance period code:"+periodCode+"的最新数据到month表开始");
//        reportFinanceDataMapper.syncPeriodContractBalanceData(periodCode);
        this.executeContractMonthData(periodCode+"", "ALL");
        log.info("同步contract_balance period code:"+periodCode+"的最新数据到month表结束");


        accountAssistBalanceService.syncAssistBalancePeriodClose(periodCode);

        List<ReportPeriodSyncRecordEntity> list = reportPeriodSyncRecordService.list(
                new LambdaQueryWrapper<ReportPeriodSyncRecordEntity>()
                        .eq(ReportPeriodSyncRecordEntity::getExecuteStatus, ExecuteStatusEnum.INPROGRESS.getCode())
                        .orderByDesc(ReportPeriodSyncRecordEntity::getCreateBy));
        if(CollectionUtil.isNotEmpty(list)){
            ReportPeriodSyncRecordEntity latestRecord = list.get(0);
            latestRecord.setEndTime(LocalDateTime.now());
            latestRecord.setExecuteStatus(ExecuteStatusEnum.FINISH.getCode());
            latestRecord.setUpdateTime(LocalDateTime.now());
            reportPeriodSyncRecordService.update();
        }
        return compareFlag;
    }

    @Override
    public boolean fillContractReportFlag() {
        Integer periodCode = Integer.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMM")));
        reportFinanceDataMapper.removeContractReportFlag();
        reportFinanceDataMapper.fillContractReportFlagOne(periodCode);
        reportFinanceDataMapper.fillContractReportFlagRest();
        return true;
    }

    @Override
    public boolean syncKingdeeExchangeRate(String queryDate) {
        List<CurrencyExchangeRateDTO> kingdeeExchangeRateList = reportKingdeeDataService.selectExchangeRateByQueryDate(queryDate);
        if(CollectionUtil.isEmpty(kingdeeExchangeRateList)){
            throw new ServiceException("没找到金蝶queryDate:"+queryDate+" 对应的汇率");
        }
        List<EasExchangeRateEntity> financeExchangeRateList = Lists.newArrayList();
        kingdeeExchangeRateList.stream().forEach(r->{
            EasExchangeRateEntity exchangeRate = new EasExchangeRateEntity();
            exchangeRate.setExecuteDate(queryDate);
            exchangeRate.setSourceEasCode(r.getSourceEasCode());
            exchangeRate.setSourceEasName(r.getSourceEasName());
            exchangeRate.setTargetEasCode(r.getTargetEasCode());
            exchangeRate.setTargetEasName(r.getTargetEasName());
            exchangeRate.setExchangeRate(r.getExchangeRate());

            exchangeRate.setDelFlag("0");
            exchangeRate.setCreateBy("system");
            exchangeRate.setCreateTime(LocalDateTime.now());
            exchangeRate.setUpdateBy("system");
            exchangeRate.setUpdateTime(LocalDateTime.now());
            financeExchangeRateList.add(exchangeRate);
        });
        Optional<CurrencyExchangeRateDTO> rmbOp = kingdeeExchangeRateList.stream()
                .filter(r->StringUtils.equals(r.getTargetEasCode(), "BB01")&&
                        StringUtils.equals(r.getTargetEasName(), "人民币")&&
                        StringUtils.equals(r.getSourceEasCode(), "BB01")&&
                        StringUtils.equals(r.getSourceEasName(), "人民币")).findAny();
        if(!rmbOp.isPresent()){
            EasExchangeRateEntity rmbExchangeRate = new EasExchangeRateEntity();
            rmbExchangeRate.setExecuteDate(queryDate);
            rmbExchangeRate.setSourceEasCode("BB01");
            rmbExchangeRate.setSourceEasName("人民币");
            rmbExchangeRate.setTargetEasCode("BB01");
            rmbExchangeRate.setTargetEasName("人民币");
            rmbExchangeRate.setExchangeRate(BigDecimal.ONE);

            rmbExchangeRate.setDelFlag("0");
            rmbExchangeRate.setCreateBy("system");
            rmbExchangeRate.setCreateTime(LocalDateTime.now());
            rmbExchangeRate.setUpdateBy("system");
            rmbExchangeRate.setUpdateTime(LocalDateTime.now());
            financeExchangeRateList.add(rmbExchangeRate);
        }

        easExchangeRateService.saveBatch(financeExchangeRateList);
        return true;
    }

    //同步辅助月表
    @Override
    public boolean syncAssistantBalance(String queryDate) {
        List<EasExchangeRateEntity> exchangeRateList = easExchangeRateService.list(new LambdaQueryWrapper<EasExchangeRateEntity>().eq(EasExchangeRateEntity::getExecuteDate, queryDate));
        if(CollectionUtil.isEmpty(exchangeRateList)){
            throw new ServiceException("没找到queryDate:"+queryDate +"的汇率数据");
        }
        reportFinanceDataMapper.syncAssistantBalance(queryDate);
        return true;
    }

    //同步台账明细表
    @Override
    public boolean syncDetailBalance(String queryDate) {
        List<EasExchangeRateEntity> exchangeRateList = easExchangeRateService.list(new LambdaQueryWrapper<EasExchangeRateEntity>().eq(EasExchangeRateEntity::getExecuteDate, queryDate));
        if(CollectionUtil.isEmpty(exchangeRateList)){
            throw new ServiceException("没找到queryDate:"+queryDate +"的汇率数据");
        }
        reportFinanceDataMapper.syncDetailBalance(queryDate);
        return true;
    }

    @Override
    public String executeAssistantAndDetailBalanceData(String queryDate) {
        log.info("同步金蝶汇率 开始");
        this.syncKingdeeExchangeRate(queryDate);
        log.info("同步金蝶汇率 结束");
        log.info("同步辅助余额帐 开始");
        this.syncAssistantBalance(queryDate);
        log.info("同步辅助余额帐 结束");
        log.info("同步明细余额帐 开始");
        this.syncDetailBalance(queryDate);
        log.info("同步明细余额帐 结束");
        return "success";
    }

    @Override
    public String executeTaReclassificationData(String queryDate) {
        if(StringUtils.isEmpty(queryDate)){
            throw new ServiceException("同步时间queryDate不能为空");
        }
        LocalDateTime dateTime = LocalDateTimeUtil.parse(queryDate, "yyyy-MM-dd");
        String reclassificationMonth = LocalDateTimeUtil.format(dateTime, "yyyy-MM");

        LocalDateTime queryDateNextDay = dateTime.plusDays(1);
        String reclassificationMonthNextDay = LocalDateTimeUtil.format(queryDateNextDay, "yyyy-MM-dd");

        List<TaReclassificationEntity> tmpList = taReclassificationService.list(new LambdaQueryWrapper<TaReclassificationEntity>().apply("to_char(reclassification_month,'YYYY-MM') = {0} ", reclassificationMonth));
        if(CollectionUtil.isNotEmpty(tmpList)){
            throw new ServiceException("该月的业务系统报表已同步:"+reclassificationMonth);
        }
        List<TaReclassificationDetailEntity> totalList = Lists.newArrayList();
        Map<String, String> orgMap = getOrgNameOrgId();

        log.info("开始同步业务系统重分类数据 统一平台 开始");
        //统一平台
        List<TaReclassificationDetailEntity> platformList = platformDataService.getTaReclassificationDetailList(queryDate, orgMap);
        if(CollectionUtil.isNotEmpty(platformList)){
            totalList.addAll(platformList);
        }
        log.info("开始同步业务系统重分类数据 统一平台 结束,同步数据量:{}",platformList.size());

        log.info("开始同步业务系统重分类数据 小微 开始");
        //小微
        List<TaReclassificationDetailEntity> xwList = microDataService.getTaReclassificationDetailList(queryDate, orgMap);
        if(CollectionUtil.isNotEmpty(xwList)){
            totalList.addAll(xwList);
        }
        log.info("开始同步业务系统重分类数据 小微 结束,同步数据量:{}",xwList.size());

        log.info("开始同步业务系统重分类数据 乘用车 开始");
        //乘用车
        List<TaReclassificationDetailEntity> passVehList = passengerVehicleDataService.getTaReclassificationDetailList(queryDate, orgMap);
        if(CollectionUtil.isNotEmpty(passVehList)){
            totalList.addAll(passVehList);
        }
        log.info("开始同步业务系统重分类数据 乘用车 结束,同步数据量:{}",passVehList.size());

        log.info("开始同步业务系统重分类数据 商用车 开始");
        //商用车
        List<TaReclassificationDetailEntity> commVehList = commercialVehicleDataService.getTaReclassificationDetailList(queryDate, orgMap);
        if(CollectionUtil.isNotEmpty(commVehList)){
            totalList.addAll(commVehList);
        }
        log.info("开始同步业务系统重分类数据 商用车 结束,同步数据量:{}",commVehList.size());

        if(CollectionUtil.isEmpty(totalList)){
            throw new ServiceException("同步到的业务系统报表数量为0");
        }


        totalList.stream().forEach(i->{
//            i.setId(IdWorker.getId());
            i.setReclassificationMonth(dateTime);
        });

        TaReclassificationEntity summary = new TaReclassificationEntity();
        summary.setReclassificationMonth(dateTime);
        summary.setProcessStatus(MarginStatusEnum.ENTERED.getCode());
        summary.setIsGenerateVoucher(YesOrNoEnum.NO.getCode());
        summary.setAccountDate(dateTime);
        taReclassificationService.save(summary);
        log.info("TA重分类 汇总数据 保存完成");

        log.info("TA重分类 详细数据 保存 开始");
        taReclassificationDetailService.saveBatch(totalList);
        log.info("TA重分类 详细数据 保存 完成，保存数量:{}", totalList.size());

        List<TaReclassificationDetailEntity> typtList = taReclassificationDetailService.selectTYPTInfo(queryDate, reclassificationMonthNextDay, SystemEnum.TYPT.getCode());
        if(CollectionUtil.isNotEmpty(typtList)){
            log.info("TA重分类 特殊处理统一平台数据 开始");
            typtList.stream().forEach(i->{
                if(StringUtils.isNotEmpty(i.getBankOrgId())&&orgMap.containsKey(i.getBankOrgId())){
                    i.setBankOrgId(orgMap.get(i.getBankOrgId()));
                }
            });
            taReclassificationDetailService.updateBatchById(typtList);
            log.info("TA重分类 特殊处理统一平台数据 结束，处理数据量：{}",typtList.size());
        }

        String queryDateStr = queryDate+" 00:00:00.000";

        log.info("TA重分类 加载明细数据字段 开始");
        List<TaReclassificationDetailEntity> entityList = taReclassificationDetailService.selectDetailDataAndContractInfo(queryDate);
        log.info("TA重分类 加载明细数据字段 结束，加载数量：{}", entityList.stream());
        if(CollectionUtil.isNotEmpty(entityList)){
            log.info("TA重分类 更新明细数据字段 开始");
            entityList.stream().forEach(i->i.setTaReclassificationId(summary.getId()));
            taReclassificationDetailService.updateBatchById(entityList);
            log.info("TA重分类 更新明细数据字段 结束，更新数量：{}", entityList.size());
        }


        BigDecimal taReclassificationAmount = entityList.stream().map(TaReclassificationDetailEntity::getTaReclassificationAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        summary.setTaReclassificationAmount(taReclassificationAmount);
        taReclassificationService.updateById(summary);
        log.info("TA重分类 刷新汇总数据重分类金额 完成，任务结束");
        return "success";
    }

    public static void main(String[] args) {
        String date = "2024-02-29";
        String dateStr = date+" 00:00:00.000";
        System.out.println(dateStr);
    }

    private Map<String,String> getOrgNameOrgId(){
        Map<String,String> orgNameAndIdMap = Maps.newHashMap();

        LambdaQueryWrapper<OrgCompanyEntity> queryWrapper = Wrappers.<OrgCompanyEntity>lambdaQuery();
        List<OrgCompanyEntity> orgCompanyEntityList = orgCompanyService.list(queryWrapper).stream().distinct().collect(Collectors.toList());
        List<OrgCompanyVO> orgCompanyVOList =  ListBeanUtil.copyList(orgCompanyEntityList, OrgCompanyVO.class);
        if (CollectionUtils.isNotEmpty(orgCompanyVOList)) {
            orgNameAndIdMap = orgCompanyVOList.stream().collect(Collectors.toMap(OrgCompanyVO::getOrgName,OrgCompanyVO::getOrgId, (k1, k2)->k2));
        }
        return orgNameAndIdMap;
    }

    @Override
    public String executeKingdeeRateData(String queryDateStart, String queryDateEnd) {
        List<CurrencyExchangeRateDTO> kingdeeExchangeRateList = reportKingdeeDataService.selectExchangeRateByQueryDateStartAndEnd(queryDateStart, queryDateEnd);

        List<EasExchangeRateEntity> financeExchangeRateList = Lists.newArrayList();
        kingdeeExchangeRateList.stream().forEach(r->{
            EasExchangeRateEntity exchangeRate = new EasExchangeRateEntity();
            exchangeRate.setExecuteDate(r.getQueryDate());
            exchangeRate.setSourceEasCode(r.getSourceEasCode());
            exchangeRate.setSourceEasName(r.getSourceEasName());
            exchangeRate.setTargetEasCode(r.getTargetEasCode());
            exchangeRate.setTargetEasName(r.getTargetEasName());
            exchangeRate.setExchangeRate(r.getExchangeRate());

            exchangeRate.setDelFlag("0");
            exchangeRate.setCreateBy("system");
            exchangeRate.setCreateTime(LocalDateTime.now());
            exchangeRate.setUpdateBy("system");
            exchangeRate.setUpdateTime(LocalDateTime.now());
            financeExchangeRateList.add(exchangeRate);
        });

        easExchangeRateService.saveBatch(financeExchangeRateList);
        return "success";
    }

    @Override
//    @Async
    public String executeContractMonthData(String periodCodes, String orgIds) {
        List<String> periodCodeList = StrUtil.split(periodCodes, "|");
        List<String> orgIdList = Lists.newArrayList();
        if(StringUtils.isNotEmpty(orgIds)){
            orgIdList = StrUtil.split(orgIds, "|");
        }
        for (String periodCodeStr: periodCodeList) {
            CountDownLatch totalCountDownLatch = new CountDownLatch(1);

            IReportFinanceDataService reportFinanceDataService = SpringUtils.getBean(IReportFinanceDataService.class);
            reportFinanceDataService.executeContractBalanceMonthData(totalCountDownLatch, periodCodeStr, orgIdList);

            try {
                totalCountDownLatch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return "success";
    }

    @Override
    @Async
    public void executeContractBalanceMonthData(CountDownLatch totalCountDownLatch, String periodCodeStr, List<String> orgIdList) {
        String orgIdsStr = StringUtils.join(orgIdList, ",");
        log.info("固化合同余额月表 处理月份:{} 处理签约主体:{} 开始", periodCodeStr, orgIdsStr);
        Map<String, Object> param = Maps.newHashMap();
        param.put("periodCode", periodCodeStr);
        param.put("orgIdList", orgIdList);
        reportFinanceDataMapper.deleteIdTemp();
        reportFinanceDataMapper.fillIdTemp(param);
        reportFinanceDataMapper.syncContractBalanceMonthData(param);
        totalCountDownLatch.countDown();
        log.info("固化合同余额月表 处理月份:{} 处理签约主体:{} 结束", periodCodeStr, orgIdsStr);
    }

}

