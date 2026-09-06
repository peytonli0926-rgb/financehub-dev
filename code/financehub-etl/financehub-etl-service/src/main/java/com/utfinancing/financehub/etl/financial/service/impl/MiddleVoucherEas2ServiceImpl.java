package com.utfinancing.financehub.etl.financial.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.google.common.collect.Maps;
import com.utfinancing.financehub.common.core.enums.FundPaymentTypeEnum;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.common.core.utils.SpringUtils;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.etl.easold.model.dto.EasVoucherDTO;
import com.utfinancing.financehub.etl.enums.DataExecutionTaskStatusEnum;
import com.utfinancing.financehub.etl.enums.Eas2SystemCodeSystemEnum;
import com.utfinancing.financehub.etl.enums.ExecutionTaskSystemEnum;
import com.utfinancing.financehub.etl.financial.entity.ClientEntity;
import com.utfinancing.financehub.etl.financial.entity.KingdeeHybVoucherEntity;
import com.utfinancing.financehub.etl.financial.entity.KingdeeMiddleVoucherEntity;
import com.utfinancing.financehub.etl.financial.entity.SendEas2ResultEntity;
import com.utfinancing.financehub.etl.financial.model.dto.DataExecutionTaskDTO;
import com.utfinancing.financehub.etl.financial.service.*;
import com.utfinancing.financehub.etl.financial.util.CommonDateUtil;
import com.utfinancing.financehub.etl.kingdee.service.IKingdeeEasService;
import com.utfinancing.financehub.etl.middle.service.IEasVoucherHeadService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * <ul>
 * <li>Project : financehub-etl</li>
 * <li>ClassName : com.utfinancing.financehub.etl.financial.service.impl.MiddleVoucherEas2ServiceImpl</li>
 * <li>CreateTime : 2024/04/07 17:18</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@Slf4j
@Service
public class MiddleVoucherEas2ServiceImpl implements MiddleVoucherEas2Service {
    @Resource
    private IVoucherToEasRecordService iVoucherToEasRecordService;
    @Resource
    private IDataExecutionTaskService dataExecutionTaskService;
    @Resource
    private VoucherTransactionService voucherTransactionService;

    @Resource
    private IEasVoucherHeadService iEasVoucherHeadService;

    @Resource
    private IVoucherService iVoucherService;

    @Resource
    private ISendEas2ResultService iSendEas2ResultService;

    //金蝶Service
    @Resource
    private IKingdeeEasService kingdeeEasService;

    @Resource
    private IKingdeeHybVoucherService iKingdeeHybVoucherService;

    @Resource
    private IKingdeeMiddleVoucherService iKingdeeMiddleVoucherService;

    @Resource
    private IClientService iClientService;

    @Async
    @Override
    public Boolean syncVoucherToEas2ByPeriodCodes(String periodCodes) throws Exception {
        List<String> periodCodeList = StrUtil.split(periodCodes, "|");
        for (String periodCodeStr : periodCodeList) {
            log.info("金蝶中间表同步凭证到Eas2,periodCode:{}开始", periodCodeStr);
            List<String> dayList = DateUtils.getDayListOfMonth(periodCodeStr);
            CountDownLatch countDownLatch = new CountDownLatch(dayList.size());
            for (String day : dayList) {
                MiddleVoucherEas2Service middleVoucherEas2Service = SpringUtils.getBean(MiddleVoucherEas2Service.class);
                middleVoucherEas2Service.syncVoucherToEas2ByVoucherDate2(day, countDownLatch);
            }
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.info("金蝶中间表按天同步凭证到Eas2,periodCode:{}同步完成", periodCodeStr);
        }
        return Boolean.TRUE;
    }

    @Async
    @Override
    public Boolean syncStageVoucherToEas2ByPeriodCodes(String periodCodes) throws Exception {
        List<String> periodCodeList = StrUtil.split(periodCodes, "|");
        for (String periodCodeStr : periodCodeList) {
            log.info("金蝶中间表同步凭证到Eas2,periodCode:{}开始", periodCodeStr);
            List<String> dayList = DateUtils.getDayListOfMonth(periodCodeStr);
            CountDownLatch countDownLatch = new CountDownLatch(dayList.size());
            for (String day : dayList) {
                MiddleVoucherEas2Service middleVoucherEas2Service = SpringUtils.getBean(MiddleVoucherEas2Service.class);
                middleVoucherEas2Service.syncStageVoucherToEas2ByVoucherDate2(day, countDownLatch);
            }
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.info("金蝶中间表按天同步凭证到Eas2,periodCode:{}同步完成", periodCodeStr);
        }
        return Boolean.TRUE;
    }

    @Async
    @Override
    public Boolean syncVoucherToEas2ByVoucherDate(String voucherDate) {
        //1.查询是否有正在执行的任务
        String systemCode = ExecutionTaskSystemEnum.MIDDLE_VOUCEHR_EAS2.getCode();
        DataExecutionTaskDTO taskDTO = dataExecutionTaskService.getRunningTaskBySystem(systemCode);
        if (taskDTO != null) {
            log.info("存在正在执行的任务，本次任务跳过, systemCode:{}", systemCode);
            return Boolean.TRUE;
        }
        String periodYear = StrUtil.sub(voucherDate, 0, 4);
        String periodMonth = StrUtil.sub(voucherDate, 5, 7);
        log.info("金蝶中间表同步凭证到Eas2,voucherDate:{}开始", voucherDate);
        List<EasVoucherDTO> easVoucherDTOList = iEasVoucherHeadService.selectMiddleEasVoucher(Integer.parseInt(periodYear), Integer.parseInt(periodMonth), voucherDate);
        log.info("查询过账金蝶中间表同步凭证到Eas2,结束");
        log.info("查询暂存金蝶中间表SC,EAS同步凭证到Eas2,voucherDate:{}开始", voucherDate);
        List<EasVoucherDTO> stageVoucherDTOList = iEasVoucherHeadService.selectStageMiddleEasVoucher(Integer.parseInt(periodYear), Integer.parseInt(periodMonth), voucherDate);
        log.info("查询暂存金蝶中间表SC,EAS同步凭证到Eas2结束");
        log.info("查询暂存金蝶中间表ZJXT同步凭证到Eas2,voucherDate:{}开始", voucherDate);
        List<EasVoucherDTO> zjxtVoucherDTOList = iEasVoucherHeadService.selectStageZJXTMiddleEasVoucher(Integer.parseInt(periodYear), Integer.parseInt(periodMonth), voucherDate);
        log.info("查询暂存金蝶中间表ZJXT同步凭证到Eas2结束");
        //汇总新的数据
        List<EasVoucherDTO> allEasVoucherList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(easVoucherDTOList)) {
            log.info("查询过账金蝶中间表同步凭证到Eas2,size{}", easVoucherDTOList.size());
            allEasVoucherList.addAll(easVoucherDTOList);
        }
        if (CollectionUtils.isNotEmpty(stageVoucherDTOList)) {
            log.info("查询暂存金蝶中间表SC,EAS同步凭证到Eas2,size{}", stageVoucherDTOList.size());
            allEasVoucherList.addAll(stageVoucherDTOList);
        }
        if (CollectionUtils.isNotEmpty(zjxtVoucherDTOList)) {
            log.info("查询暂存金蝶中间表ZJXT同步凭证到Eas2,size{}", zjxtVoucherDTOList.size());
            allEasVoucherList.addAll(zjxtVoucherDTOList);
        }
        if (CollectionUtils.isEmpty(allEasVoucherList)) {
            log.info("金蝶中间表同步凭证到Eas2,voucherDate:{}无数据同步", voucherDate);
            return Boolean.TRUE;
        }
        Map<String, List<EasVoucherDTO>> voucherMap = allEasVoucherList.stream().collect(Collectors.groupingBy(EasVoucherDTO::getIsStage));
        Long taskId = null;
        int successCount = 0;
        int failedCount = 0;
        String isSuccess = DataExecutionTaskStatusEnum.SUCCESS.getCode();
        ;
        try {
            List<String> existFidList = getMiddleEasRecordList(Eas2SystemCodeSystemEnum.KINGDEE_MIDDLE.getCode());
            LocalDateTime voucherLocalDateTime = CommonDateUtil.parseStringToLocalDateTime(voucherDate);
            taskId = dataExecutionTaskService.createNewTask(systemCode, DataExecutionTaskStatusEnum.RUNNING.getCode(), allEasVoucherList.size(), voucherLocalDateTime, voucherLocalDateTime);
            for (Map.Entry<String, List<EasVoucherDTO>> entry : voucherMap.entrySet()) {
                List<EasVoucherDTO> newEasVoucherList = Lists.newArrayList();
                List<EasVoucherDTO> oldEasVoucherList = entry.getValue();
                log.info("数据大小：size:{}", oldEasVoucherList.size());
                for (EasVoucherDTO voucherDTO : oldEasVoucherList) {
                    if (existFidList.contains(voucherDTO.getFid())) {
                        continue;
                    }
                    newEasVoucherList.add(voucherDTO);
                }
                if (CollectionUtils.isEmpty(newEasVoucherList)) {
                    log.info("传输的数据为空");
                    continue;
                }
                log.info("金蝶中间表同步凭证到Eas2,voucherDate:{}开始，发送数据条数{}", voucherDate, newEasVoucherList.size());
                voucherTransactionService.middleVoucherToEas2(newEasVoucherList, Eas2SystemCodeSystemEnum.KINGDEE_MIDDLE.getCode());
                //按照voucherNum 分组传输
//                Map<String,List<EasVoucherDTO>> easVoucherMap = newEasVoucherList.stream().collect(Collectors.groupingBy(EasVoucherDTO::getVoucherNumber));
//                for (Map.Entry<String, List<EasVoucherDTO>> easKey : easVoucherMap.entrySet()) {
//                    int i = easKey.getValue().size();
//                    voucherTransactionService.middleVoucherToEas2(easKey.getValue(), Eas2SystemCodeSystemEnum.KINGDEE_MIDDLE.getCode());
//                    log.info("金蝶中间表传输进度：{}",NumberUtil.formatPercent(i/totalSize, 2));
//                }
                successCount = newEasVoucherList.size();
            }
        } catch (Exception e) {
            log.info("金蝶中间库发送会计期间{}数据失败,失败原因：{}", voucherDate, e.getMessage());
            failedCount = allEasVoucherList.size();
            isSuccess = DataExecutionTaskStatusEnum.FAILED.getCode();
        } finally {
            if (null != taskId) {
                dataExecutionTaskService.finishedTask(taskId, isSuccess, successCount, failedCount);
            }
        }
        log.info("金蝶中间表同步凭证到Eas2,voucherDate:{}结束", voucherDate);
        return isSuccess.equals(DataExecutionTaskStatusEnum.SUCCESS.getCode()) ? Boolean.TRUE : Boolean.FALSE;
    }

    public List<String> getMiddleEasRecordList(String systemCode) {
        List<SendEas2ResultEntity> resultEntityList = iSendEas2ResultService.lambdaQuery().eq(SendEas2ResultEntity::getSystemCode, systemCode).eq(SendEas2ResultEntity::getIsSuccess, "sucs").list();
        List<String> resultList = Lists.newArrayList();
        resultEntityList.stream().forEach(v -> {
            resultList.addAll(Arrays.stream(v.getFid().split(",")).collect(Collectors.toList()));
        });
        return resultList;
    }

    @Async
    public void syncVoucherToEas2ByVoucherDate2(String voucherDate, CountDownLatch countDownLatch) throws Exception {
        log.info("金蝶中间表同步凭证到Eas2,voucherDate:{}开始", voucherDate);
        String periodYear = StrUtil.sub(voucherDate, 0, 4);
        String periodMonth = StrUtil.sub(voucherDate, 5, 7);
        List<EasVoucherDTO> easVoucherDTOList = iEasVoucherHeadService.selectMiddleEasVoucher(Integer.parseInt(periodYear), Integer.parseInt(periodMonth), voucherDate);
        log.info("金蝶中间表同步凭证到Eas2,同步数据量：{}", easVoucherDTOList.size());
        voucherTransactionService.middleVoucherToEas2(easVoucherDTOList, Eas2SystemCodeSystemEnum.KINGDEE_MIDDLE.getCode());
        countDownLatch.countDown();
        log.info("金蝶中间表同步凭证到Eas2,voucherDate:{}结束", voucherDate);
    }

    @Async
    public void syncStageVoucherToEas2ByVoucherDate2(String voucherDate, CountDownLatch countDownLatch) throws Exception {
        log.info("金蝶中间表暂存数据同步凭证到Eas2,voucherDate:{}开始", voucherDate);
        String periodYear = StrUtil.sub(voucherDate, 0, 4);
        String periodMonth = StrUtil.sub(voucherDate, 5, 7);
        List<EasVoucherDTO> easVoucherDTOList = iEasVoucherHeadService.selectStageMiddleEasVoucher(Integer.parseInt(periodYear), Integer.parseInt(periodMonth), voucherDate);
        log.info("金蝶中间表暂存数据同步凭证到Eas2,同步数据量：{}", easVoucherDTOList.size());
        voucherTransactionService.middleVoucherToEas2(easVoucherDTOList, Eas2SystemCodeSystemEnum.KINGDEE_MIDDLE.getCode());
        countDownLatch.countDown();
        log.info("金蝶中间表同步凭证到Eas2,voucherDate:{}结束", voucherDate);
    }

    @Async
    @Override
    public Boolean syncFinhubVoucherToEas2ByVoucherDate(String voucherDate) {
        String periodYear = StrUtil.sub(voucherDate, 0, 4);
        String periodMonth = StrUtil.sub(voucherDate, 5, 7);
        Integer periodCode = Integer.parseInt(periodYear + periodMonth);
        log.info("中台同步凭证（暂存，复核，过账）到Eas2,voucherDate:{}开始", voucherDate);
        //外部系统数据
        List<EasVoucherDTO> easVoucherDTOList = iVoucherService.selectFinhubVoucherData(periodCode, voucherDate);
        //按照是否暂存字段分组
        if (CollectionUtils.isNotEmpty(easVoucherDTOList)) {
            log.info("中台同步凭证（暂存，复核，过账）到Eas2,voucherDate{},数据总数：{}", voucherDate, easVoucherDTOList.size());
        }
        Map<String, List<EasVoucherDTO>> easVoucherMap = easVoucherDTOList.stream().collect(Collectors.groupingBy(EasVoucherDTO::getIsStage));
        try {
            for (Map.Entry<String, List<EasVoucherDTO>> entry : easVoucherMap.entrySet()) {
                List<EasVoucherDTO> oldEasVoucherList = entry.getValue();
                //每一组的voucherNum需要一致
                //org_id,system_code,scene_code,voucher_date,business_date, 创建人，复核人，凭证类型这些再分一次组
                Map<String, List<EasVoucherDTO>> voucherDTOMap = oldEasVoucherList.stream().collect(Collectors.groupingBy(v -> v.getCompanyNumber()
                        + "-" + v.getSystemCode() + "-" + v.getSceneCode() + "-" + v.getCreator() + "-" + v.getPoster()
                        + "-" + v.getPeriodNumber() + "-" + v.getPeriodYear() + v.getVoucherType()));
                for (Map.Entry<String, List<EasVoucherDTO>> entryKey : voucherDTOMap.entrySet()) {
                    String voucherNum = UUID.randomUUID().toString();
                    entryKey.getValue().stream().forEach(e -> {
                        e.setVoucherNumber(voucherNum);
                        e.setFid(UUID.randomUUID().toString());
                        e.setImportKey(e.getSystemCode() + e.getVoucherNumber());
                        //判断数据状态
                        if ("1".equals(e.getIsStage())) {
                            e.setDataStatus("暂存");
                        } else if ("true".equals(e.getIsCheck())) {
                            e.setDataStatus("复核");
                        } else {
                            e.setDataStatus("过账");
                        }
                    });
                }
                LocalDateTime voucherLocalDateTime = CommonDateUtil.parseStringToLocalDateTime(voucherDate);
                log.info("是否暂存数据:{}，中台同步凭证到Eas2,voucherDate:{}开始，发送数据条数{}", entry.getKey(), voucherDate, oldEasVoucherList.size());
                voucherTransactionService.middleVoucherToEas2(oldEasVoucherList, Eas2SystemCodeSystemEnum.FINHUB.getCode());
            }
        } catch (Exception e) {
            log.info("中台（暂存，复核，过账）发送会计期间{}数据失败,失败原因：{}", voucherDate, e.getMessage());
        }
        log.info("中台（暂存，复核，过账）同步凭证到Eas2,voucherDate:{}结束", voucherDate);
        return Boolean.TRUE;
    }

    @Async
    @Override
    public Boolean syncSubmitFinhubVoucherToEas2ByVoucherDate(String voucherDate) {
        String periodYear = StrUtil.sub(voucherDate, 0, 4);
        String periodMonth = StrUtil.sub(voucherDate, 5, 7);
        Integer periodCode = Integer.parseInt(periodYear + periodMonth);
        log.info("中台同步凭证（提交）到Eas2,voucherDate:{}开始", voucherDate);
        //中台自己生成的凭证
        List<EasVoucherDTO> finhubVoucherDTOList = iVoucherService.selectFinhubInterVoucherData(periodCode, voucherDate);
        //按照是否暂存字段分组
        if (CollectionUtils.isNotEmpty(finhubVoucherDTOList)) {
            log.info("中台同步凭证（提交）到Eas2,voucherDate{},数据总数：{}", voucherDate, finhubVoucherDTOList.size());
        }
        try {
            LocalDateTime voucherLocalDateTime = CommonDateUtil.parseStringToLocalDateTime(voucherDate);
            //分组按照：公司+期间+年+月+凭证类型
            //每一组的voucherNum需要一致
            Map<String, List<EasVoucherDTO>> voucherDTOMap = finhubVoucherDTOList.stream().collect(Collectors.groupingBy(v -> v.getCompanyNumber()
                    + "-" + v.getSystemCode() + "-" + v.getSceneCode() + "-" + v.getCreator() + "-" + v.getPoster()
                    + "-" + v.getPeriodNumber() + "-" + v.getPeriodYear() + v.getVoucherType()));
            for (Map.Entry<String, List<EasVoucherDTO>> entryKey : voucherDTOMap.entrySet()) {
                String voucherNum = UUID.randomUUID().toString();
                entryKey.getValue().stream().forEach(e -> {
                    e.setVoucherNumber(voucherNum);
                    e.setFid(UUID.randomUUID().toString());
                    e.setImportKey(e.getSystemCode() + e.getVoucherNumber());
                });
            }
            log.info("中台同步凭证（提交）到Eas2,voucherDate:{}开始，发送数据条数{}", voucherDate, finhubVoucherDTOList.size());
            voucherTransactionService.middleVoucherToEas2(finhubVoucherDTOList, Eas2SystemCodeSystemEnum.FINHUB.getCode());
        } catch (Exception e) {
            log.info("中台（提交）发送会计期间{}数据失败,失败原因：{}", voucherDate, e.getMessage());
        }
        log.info("中台（提交）同步凭证到Eas2,voucherDate:{}结束", voucherDate);
        return Boolean.TRUE;
    }

    @Override
    public Boolean syncFinhubVoucherToEas2ByPeriodCodes(String periodCodes) throws Exception {
        List<String> periodCodeList = StrUtil.split(periodCodes, "|");
        for (String periodCodeStr : periodCodeList) {
            log.info("中台暂存，复核，过账同步凭证到Eas2,periodCode:{}开始", periodCodeStr);
            List<String> dayList = DateUtils.getDayListOfMonth(periodCodeStr);
            CountDownLatch countDownLatch = new CountDownLatch(dayList.size());
            for (String day : dayList) {
                MiddleVoucherEas2Service middleVoucherEas2Service = SpringUtils.getBean(MiddleVoucherEas2Service.class);
                middleVoucherEas2Service.syncFinhubVoucherToEas2ByVoucherDate2(day, countDownLatch);
            }
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.info("金蝶中间表按天同步凭证到Eas2,periodCode:{}同步完成", periodCodeStr);
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean syncSubmitFinhubVoucherToEas2ByPeriodCodes(String periodCodes) throws Exception {
        List<String> periodCodeList = StrUtil.split(periodCodes, "|");
        for (String periodCodeStr : periodCodeList) {
            log.info("中台提交同步凭证到Eas2,periodCode:{}开始", periodCodeStr);
            List<String> dayList = DateUtils.getDayListOfMonth(periodCodeStr);
            CountDownLatch countDownLatch = new CountDownLatch(dayList.size());
            for (String day : dayList) {
                MiddleVoucherEas2Service middleVoucherEas2Service = SpringUtils.getBean(MiddleVoucherEas2Service.class);
                middleVoucherEas2Service.syncSubmitFinhubVoucherToEas2ByVoucherDate2(day, countDownLatch);
            }
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.info("中台提交按天同步凭证到Eas2,periodCode:{}同步完成", periodCodeStr);
        }
        return Boolean.TRUE;
    }

    @Override
    public void syncFinhubVoucherToEas2ByVoucherDate2(String voucherDate, CountDownLatch countDownLatch) throws Exception {
        log.info("中台提交同步凭证(暂存，复核，过账)同步凭证到Eas2,voucherDate:{}开始", voucherDate);
        syncFinhubVoucherToEas2ByVoucherDate(voucherDate);
        countDownLatch.countDown();
        log.info("金蝶中间表同步凭证(暂存，复核，过账)到Eas2,voucherDate:{}结束", voucherDate);
    }

    @Override
    public void syncSubmitFinhubVoucherToEas2ByVoucherDate2(String voucherDate, CountDownLatch countDownLatch) throws Exception {
        log.info("中台提交同步凭证（提交）同步凭证到Eas2,voucherDate:{}开始", voucherDate);
        syncSubmitFinhubVoucherToEas2ByVoucherDate(voucherDate);
        countDownLatch.countDown();
        log.info("中台提交同步凭证（提交）同步凭证到Eas2,voucherDate:{}结束", voucherDate);
    }

    @Async
    @Override
    public Boolean syncNoSummaryFinhubVoucherToEas2ByVoucherDate(String voucherDate, boolean isEntryFlag) {
        String periodYear = StrUtil.sub(voucherDate, 0, 4);
        String periodMonth = StrUtil.sub(voucherDate, 5, 7);
        Integer periodCode = Integer.parseInt(periodYear + periodMonth);
        log.info("不汇总中台同步凭证（暂存，复核，过账）到Eas2,voucherDate:{}开始", voucherDate);
        //外部系统数据
        List<EasVoucherDTO> easVoucherDTOList = iVoucherService.selectNoSummaryFinhubVoucherData(periodCode, voucherDate, isEntryFlag);
        //按照是否暂存字段分组
        if (CollectionUtils.isNotEmpty(easVoucherDTOList)) {
            log.info("不汇总中台同步凭证（暂存，复核，过账）到Eas2,voucherDate{},数据总数：{}", voucherDate, easVoucherDTOList.size());
        }
        //处理特殊场景数据
        easVoucherDTOList = handlingSepcialSceneData(easVoucherDTOList);
        Map<String, List<EasVoucherDTO>> easVoucherMap = easVoucherDTOList.stream().collect(Collectors.groupingBy(EasVoucherDTO::getIsStage));
        try {
            for (Map.Entry<String, List<EasVoucherDTO>> entry : easVoucherMap.entrySet()) {
                List<EasVoucherDTO> oldEasVoucherList = entry.getValue();
                //每一组的voucherNum需要一致
                //org_id,system_code,scene_code,voucher_date,business_date, 创建人，复核人，凭证类型这些再分一次组
                Map<String, List<EasVoucherDTO>> voucherDTOMap = oldEasVoucherList.stream().collect(Collectors.groupingBy(EasVoucherDTO::getFid));
                for (Map.Entry<String, List<EasVoucherDTO>> entryKey : voucherDTOMap.entrySet()) {
                    String voucherNum = UUID.randomUUID().toString();
                    entryKey.getValue().forEach(e -> {
                        if (!StringUtils.equals(e.getVoucherNumber(), e.getEvVoucherSummary())) {
                            e.setVoucherNumber(voucherNum);
                        }
//                        e.setFid(UUID.randomUUID().toString());
                        e.setImportKey(e.getSystemCode() + e.getVoucherNumber());
                        //判断数据状态
                        if ("1".equals(e.getIsStage())) {
                            e.setDataStatus("暂存");
                        } else if ("true".equals(e.getIsCheck())) {
                            e.setDataStatus("复核");
                        } else {
                            e.setDataStatus("过账");
                        }
                    });
                }
                LocalDateTime voucherLocalDateTime = CommonDateUtil.parseStringToLocalDateTime(voucherDate);
                log.info("不汇总是否暂存数据:{}，中台同步凭证到Eas2,voucherDate:{}开始，发送数据条数{}", entry.getKey(), voucherDate, oldEasVoucherList.size());
                String source = isEntryFlag ? Eas2SystemCodeSystemEnum.FINHUB_ENTRY.getCode() : Eas2SystemCodeSystemEnum.FINHUB.getCode();
                voucherTransactionService.middleVoucherToEas2(oldEasVoucherList, source);
            }
        } catch (Exception e) {
            log.info("不汇总中台（暂存，复核，过账）发送会计期间{}数据失败,失败原因：{}", voucherDate, e.getMessage());
        }
        log.info("不汇总中台（暂存，复核，过账）同步凭证到Eas2,voucherDate:{}结束", voucherDate);
        return Boolean.TRUE;
    }

    @Override
    public Boolean syncNoSummaryFinhubVoucherToEas2ByPeriodCodes(String periodCodes) throws Exception {
        List<String> periodCodeList = StrUtil.split(periodCodes, "|");
        for (String periodCodeStr : periodCodeList) {
            log.info("不汇总中台暂存，复核，过账同步凭证到Eas2,periodCode:{}开始", periodCodeStr);
            List<String> dayList = DateUtils.getDayListOfMonth(periodCodeStr);
            CountDownLatch countDownLatch = new CountDownLatch(dayList.size());
            for (String day : dayList) {
                MiddleVoucherEas2Service middleVoucherEas2Service = SpringUtils.getBean(MiddleVoucherEas2Service.class);
                middleVoucherEas2Service.syncFinhubVoucherToEas2ByVoucherDate2(day, countDownLatch);
            }
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.info("金蝶中间表按天同步凭证到Eas2,periodCode:{}同步完成", periodCodeStr);
        }
        return Boolean.TRUE;
    }

    @Override
    public void syncNoSummaryFinhubVoucherToEas2ByVoucherDate2(String voucherDate, CountDownLatch countDownLatch, boolean isEntryFlag) {
        log.info("不汇总中台提交同步凭证(暂存，复核，过账)同步凭证到Eas2,voucherDate:{}开始", voucherDate);
        syncNoSummaryFinhubVoucherToEas2ByVoucherDate(voucherDate, isEntryFlag);
        countDownLatch.countDown();
        log.info("不汇总金蝶中间表同步凭证(暂存，复核，过账)到Eas2,voucherDate:{}结束", voucherDate);
    }

    @Async
    @Override
    public Boolean syncNoSummarySubmitFinhubVoucherToEas2ByVoucherDate(String voucherDate, boolean isEntryFlag) {
        String periodYear = StrUtil.sub(voucherDate, 0, 4);
        String periodMonth = StrUtil.sub(voucherDate, 5, 7);
        Integer periodCode = Integer.parseInt(periodYear + periodMonth);
        log.info("不汇总中台同步凭证（提交）到Eas2,voucherDate:{}开始", voucherDate);
        //中台自己生成的凭证
        List<EasVoucherDTO> finhubVoucherDTOList = iVoucherService.selectNoSummaryFinhubInterVoucherData(periodCode, voucherDate, isEntryFlag);
        //按照是否暂存字段分组
        if (CollectionUtils.isNotEmpty(finhubVoucherDTOList)) {
            log.info("不汇总中台同步凭证（提交）到Eas2,voucherDate{},数据总数：{}", voucherDate, finhubVoucherDTOList.size());
        }
        finhubVoucherDTOList = handlingSepcialSceneData(finhubVoucherDTOList);
        try {
            Map<String, List<EasVoucherDTO>> finhubVoucherMap = finhubVoucherDTOList.stream().collect(Collectors.groupingBy(EasVoucherDTO::getIsStage));
            LocalDateTime voucherLocalDateTime = CommonDateUtil.parseStringToLocalDateTime(voucherDate);
            //分组按照：公司+期间+年+月+凭证类型
            //每一组的voucherNum需要一致
            for (Map.Entry<String, List<EasVoucherDTO>> entry : finhubVoucherMap.entrySet()) {
                List<EasVoucherDTO> newFinhubDtoList = entry.getValue();
                Map<String, List<EasVoucherDTO>> voucherDTOMap = newFinhubDtoList.stream().collect(Collectors.groupingBy(EasVoucherDTO::getFid));
                for (Map.Entry<String, List<EasVoucherDTO>> entryKey : voucherDTOMap.entrySet()) {
                    String voucherNum = UUID.randomUUID().toString();
                    entryKey.getValue().forEach(e -> {
                        e.setVoucherNumber(voucherNum);
//                        e.setFid(UUID.randomUUID().toString());
                        e.setImportKey(e.getSystemCode() + e.getVoucherNumber());
                        //判断数据状态
                        if ("1".equals(e.getIsStage())) {
                            e.setDataStatus("暂存");
                        } else if ("true".equals(e.getIsCheck())) {
                            e.setDataStatus("复核");
                        } else {
                            e.setDataStatus("过账");
                        }
                    });
                }
                log.info("不汇总中台同步凭证（提交）到Eas2,voucherDate:{}开始，发送数据条数{}", voucherDate, newFinhubDtoList.size());
                String source = isEntryFlag ? Eas2SystemCodeSystemEnum.FINHUB_ENTRY.getCode() : Eas2SystemCodeSystemEnum.FINHUB.getCode();
                voucherTransactionService.middleVoucherToEas2(newFinhubDtoList, source);
            }
        } catch (Exception e) {
            log.info("不汇总中台（提交）发送会计期间{}数据失败,失败原因：{}", voucherDate, e.getMessage());
        }
        log.info("不汇总中台（提交）同步凭证到Eas2,voucherDate:{}结束", voucherDate);
        return Boolean.TRUE;
    }

    @Override
    public Boolean syncNoSummarySubmitFinhubVoucherToEas2ByPeriodCodes(String periodCodes) throws Exception {
        List<String> periodCodeList = StrUtil.split(periodCodes, "|");
        for (String periodCodeStr : periodCodeList) {
            log.info("不汇总中台提交同步凭证到Eas2,periodCode:{}开始", periodCodeStr);
            List<String> dayList = DateUtils.getDayListOfMonth(periodCodeStr);
            CountDownLatch countDownLatch = new CountDownLatch(dayList.size());
            for (String day : dayList) {
                MiddleVoucherEas2Service middleVoucherEas2Service = SpringUtils.getBean(MiddleVoucherEas2Service.class);
                middleVoucherEas2Service.syncNoSummarySubmitFinhubVoucherToEas2ByVoucherDate2(day, countDownLatch, false);
            }
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.info("不汇总中台提交按天同步凭证到Eas2,periodCode:{}同步完成", periodCodeStr);
        }
        return Boolean.TRUE;
    }

    @Override
    public void syncNoSummarySubmitFinhubVoucherToEas2ByVoucherDate2(String voucherDate, CountDownLatch countDownLatch, boolean isEntryFlag) {
        log.info("不汇总中台提交同步凭证（提交）同步凭证到Eas2,voucherDate:{}开始", voucherDate);
        syncNoSummarySubmitFinhubVoucherToEas2ByVoucherDate(voucherDate, isEntryFlag);
        countDownLatch.countDown();
        log.info("不汇总中台提交同步凭证（提交）同步凭证到Eas2,voucherDate:{}结束", voucherDate);
    }

    @Override
    public boolean syncKingDeeVoucherByVoucherDate(String voucherDate) {
        log.info("同步金蝶恒运宝凭证数据到中台开始日期：{}", voucherDate);
        String periodYear = StrUtil.sub(voucherDate, 0, 4);
        String periodMonth = StrUtil.sub(voucherDate, 5, 7);
        String periodCode = periodYear + periodMonth;
        LocalDateTime voucherLocalDate = DateUtils.parseLocalDateTime(voucherDate);
        List<KingdeeHybVoucherEntity> voucherList = kingdeeEasService.selectKingdeeHybVoucher(periodCode, voucherDate);
        log.info("同步金蝶恒运宝凭证数据到中台结束，同步数据：{}条", voucherList.size());
        if (CollectionUtils.isEmpty(voucherList)) {
            return Boolean.TRUE;
        }
        List<KingdeeHybVoucherEntity> newVoucherEntityList = Lists.newArrayList();
        List<KingdeeHybVoucherEntity> oldVoucherEntity = iKingdeeHybVoucherService.lambdaQuery().eq(KingdeeHybVoucherEntity::getPeriodCode, periodCode).eq(KingdeeHybVoucherEntity::getVoucherDate, voucherLocalDate).list();
        //去重
        List<String> fidList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(oldVoucherEntity)) {
            fidList = oldVoucherEntity.stream().map(KingdeeHybVoucherEntity::getFid).distinct().collect(Collectors.toList());
        }
        for (KingdeeHybVoucherEntity kingdeeHybVoucherEntity : voucherList) {
            if (fidList.contains(kingdeeHybVoucherEntity.getFid())) {
                continue;
            }
            kingdeeHybVoucherEntity.setVoucherDate(voucherLocalDate);
            newVoucherEntityList.add(kingdeeHybVoucherEntity);
        }
        log.info("同步金蝶恒运宝凭证数据到中台去重同步数据：{}条", newVoucherEntityList.size());
        if (CollectionUtils.isEmpty(newVoucherEntityList)) {
            return Boolean.TRUE;
        }
        iKingdeeHybVoucherService.saveBatch(newVoucherEntityList);
        return Boolean.TRUE;
    }

    @Override
    public boolean syncKingDeeMiddleVoucherByVoucherDate(String voucherDate) {
        log.info("同步金蝶贵安，现代物流凭证数据到中台开始日期：{}", voucherDate);
        String periodYear = StrUtil.sub(voucherDate, 0, 4);
        String periodMonth = StrUtil.sub(voucherDate, 5, 7);
        String periodCode = periodYear + periodMonth;
        LocalDateTime voucherLocalDate = DateUtils.parseLocalDateTime(voucherDate);
        List<KingdeeMiddleVoucherEntity> voucherList = iEasVoucherHeadService.selectKingdeeMiddleGaxdVoucher(Integer.parseInt(periodYear), Integer.parseInt(periodMonth), voucherDate);
        log.info("同步金蝶贵安，现代物流凭证数据到中台结束，同步数据：{}条", voucherList.size());
        if (CollectionUtils.isEmpty(voucherList)) {
            return Boolean.TRUE;
        }
        List<KingdeeMiddleVoucherEntity> newVoucherEntityList = Lists.newArrayList();
        List<KingdeeMiddleVoucherEntity> oldVoucherEntity = iKingdeeMiddleVoucherService.lambdaQuery().eq(KingdeeMiddleVoucherEntity::getVoucherDate, voucherLocalDate).list();
        //去重
        List<String> easbzCodeList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(oldVoucherEntity)) {
            easbzCodeList = oldVoucherEntity.stream().map(KingdeeMiddleVoucherEntity::getEasbzCode).distinct().collect(Collectors.toList());
        }
        Map<String, List<KingdeeMiddleVoucherEntity>> voucherMap = voucherList.stream().collect(Collectors.groupingBy(KingdeeMiddleVoucherEntity::getEasbzCode));
        for (Map.Entry<String, List<KingdeeMiddleVoucherEntity>> entry : voucherMap.entrySet()) {
            Long batchId = IdWorker.getId();
            for (KingdeeMiddleVoucherEntity voucherEntity : entry.getValue()) {
                if (easbzCodeList.contains(voucherEntity.getEasbzCode())) {
                    continue;
                }
                voucherEntity.setVoucherDate(voucherLocalDate);
                voucherEntity.setBatchId(batchId);
                newVoucherEntityList.add(voucherEntity);
            }
        }
        log.info("同步金蝶贵安，现代物流凭证数据到中台去重同步数据：{}条", newVoucherEntityList.size());
        if (CollectionUtils.isEmpty(newVoucherEntityList)) {
            return Boolean.TRUE;
        }
        iKingdeeMiddleVoucherService.saveBatch(newVoucherEntityList);
        return Boolean.TRUE;
    }

    @Async
    @Override
    public void syncFinhubVoucherEntryToEas2ByPeriodCodes(String periodCodes) {
        List<String> periodCodeList = StrUtil.split(periodCodes, "|");
        log.info("同步中台凭证分录到金蝶开始，参数：{}", periodCodes);
        for (String periodCodeStr : periodCodeList) {
            List<String> dayList = DateUtils.getDayListOfMonth(periodCodeStr);
            CountDownLatch countDownLatch = new CountDownLatch(dayList.size());
            for (String day : dayList) {
                //外部系统数据
                log.info("发送外部系统数据开始");
                MiddleVoucherEas2Service middleVoucherEas2Service = SpringUtils.getBean(MiddleVoucherEas2Service.class);
                middleVoucherEas2Service.syncNoSummaryFinhubVoucherToEas2ByVoucherDate2(day, countDownLatch, true);
                //中台内部数据
                log.info("发送中台内部系统数据开始");
                middleVoucherEas2Service.syncNoSummarySubmitFinhubVoucherToEas2ByVoucherDate2(day, countDownLatch, true);
            }
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
        log.info("同步中台凭证分录到金蝶结束");
    }

    @Async
    @Override
    public void  syncFinhubVoucherEntryToEas2ByVoucherDate(String voucherDate) {
        log.info("发送外部系统数据开始");
        syncNoSummaryFinhubVoucherToEas2ByVoucherDate(voucherDate, true);
        log.info("发送外部系统数据结束");
        log.info("发送中台内部数据开始");
        syncNoSummarySubmitFinhubVoucherToEas2ByVoucherDate(voucherDate, true);
        log.info("发送中台内部数据结束");
    }

    @Override
    public void syncAllFinhubVoucherToEas2ByVoucherDate(String voucherDate) {
        log.info("汇总同步中台（暂存，复核，过账）凭证表到EAS2系统-按照会计时间(yyyy-MM-dd)同步");
        syncFinhubVoucherToEas2ByVoucherDate(voucherDate);
        log.info("汇总同步中台（提交）凭证表到EAS2系统-按照会计时间(yyyy-MM-dd)同步");
        syncSubmitFinhubVoucherToEas2ByVoucherDate(voucherDate);
        log.info("不汇总同步中台（暂存，复核，过账）凭证表到EAS2系统-按照会计时间(yyyy-MM-dd)同步");
        syncNoSummaryFinhubVoucherToEas2ByVoucherDate(voucherDate, false);
        log.info("不汇总同步中台（提交）凭证表到EAS2系统-按照会计期间同步");
        syncNoSummarySubmitFinhubVoucherToEas2ByVoucherDate(voucherDate, false);
        log.info("同步全部汇总数据结束=============");
    }

    public List<EasVoucherDTO> handlingSepcialSceneData(List<EasVoucherDTO> easVoucherDTOList) {
        /**
         * 1.场景为'HTQZ'，按合同号+科目+摘要+客户合并凭证；
         * 2.场景in('ZLSK','RKCZ')且细分场景in('1','18')，即含过渡科目的凭证，需按interface_id+科目+摘要+合同+客户合并凭证
         * 3.租赁付款，付款类型为保证金支出、渠道费支出时根据付款表的批次号合并凭证
         */
        List<EasVoucherDTO> newEasVoucherList = Lists.newArrayList();
        List<EasVoucherDTO> htqzVoucherList = Lists.newArrayList();
        List<EasVoucherDTO> zlrkVoucherList = Lists.newArrayList();
        List<EasVoucherDTO> zlfkMergeVoucherList = Lists.newArrayList();
        easVoucherDTOList.forEach(v -> {
            v.setUuid(UUID.randomUUID().toString());
            if ("HTQZ".equals(v.getSceneCode())) {
                htqzVoucherList.add(v);
            } else if (("ZLSK".equals(v.getSceneCode()) || "RKCZ".equals(v.getSceneCode())
                    || "WYLSSK".equals(v.getSceneCode())
            )
                    && ("1".equals(v.getSubSceneType()) || "18".equals(v.getSubSceneType()) || "网银流水收款".equals(v.getSubSceneType()))) {
                zlrkVoucherList.add(v);
            } else if ("WYLSFK".equals(v.getSceneCode()) && (StringUtils.equalsAny(v.getBusinessOperation(), FundPaymentTypeEnum.PAYMENT_TYPE_202.getCode(), FundPaymentTypeEnum.PAYMENT_TYPE_213.getCode()))) {
                zlfkMergeVoucherList.add(v);
            } else {
                newEasVoucherList.add(v);
            }
        });
        if (CollectionUtils.isNotEmpty(htqzVoucherList)) {
            newEasVoucherList.addAll(mergeVoucehr(htqzVoucherList, false));
        }
        if (CollectionUtils.isNotEmpty(zlrkVoucherList)) {
            newEasVoucherList.addAll(mergeVoucehr(zlrkVoucherList, true));
        }
        if (CollectionUtils.isNotEmpty(zlfkMergeVoucherList)) {
            newEasVoucherList.addAll(zlfkMergeVoucher(zlfkMergeVoucherList));
        }
        //uuid 相同的过滤掉
        List<EasVoucherDTO> fileterUuidList = Lists.newArrayList();
        List<String> uuidList = Lists.newArrayList();
        for (EasVoucherDTO voucherDTO : newEasVoucherList) {
            if (!uuidList.contains(voucherDTO.getUuid())) {
                fileterUuidList.add(voucherDTO);
                uuidList.add(voucherDTO.getUuid());
            }
        }
        //校验同一个fid 组内数据借贷是否相同，不同直接过滤掉
        List<EasVoucherDTO> filterVoucherList = Lists.newArrayList();
        Map<String, List<EasVoucherDTO>> voucherGroup = fileterUuidList.stream().collect(Collectors.groupingBy(EasVoucherDTO::getFid));
        for (Map.Entry<String, List<EasVoucherDTO>> entry : voucherGroup.entrySet()) {
            BigDecimal debitAmountTotal = BigDecimal.ZERO;
            BigDecimal creditAmountTotal = BigDecimal.ZERO;
            for (EasVoucherDTO v : entry.getValue()) {
                if (null != v.getDebitAmount()) {
                    debitAmountTotal = debitAmountTotal.add(v.getDebitAmount());
                }
                if (null != v.getCreditAmount()) {
                    creditAmountTotal = creditAmountTotal.add(v.getCreditAmount());
                }
            }
            if (debitAmountTotal.compareTo(creditAmountTotal) != 0) {
                log.info("fid:{} 借方金额：{}，贷方金额：{} 不相等，过滤掉", entry.getKey(), debitAmountTotal, creditAmountTotal);
                continue;
            }
            filterVoucherList.addAll(entry.getValue());
        }
        //获取不为空的客户编码
        List<String> clientCodeList = filterVoucherList.stream().map(EasVoucherDTO::getAsstActNumber2).filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
        Map<String, String> clientMap = getClientInfoMap(clientCodeList);
        filterVoucherList.forEach(v -> {
            if (StringUtils.isNotEmpty(v.getAsstActNumber2()) && clientMap.containsKey(v.getAsstActNumber2())) {
                v.setAsstActName2(null == clientMap.get(v.getAsstActNumber2()) ? "" : clientMap.get(v.getAsstActNumber2()));
            }
        });
        //新数组按照 签约主体，日期，类型，凭证编号排序
        return filterVoucherList.stream().sorted(Comparator.comparing(EasVoucherDTO::getCompanyNumber, Comparator.nullsLast(String::compareTo))
                .thenComparing(EasVoucherDTO::getBookedDate, Comparator.nullsLast(String::compareTo))
                .thenComparing(EasVoucherDTO::getVoucherType, Comparator.nullsLast(String::compareTo))
                .thenComparing(EasVoucherDTO::getVoucherNumber, Comparator.nullsLast(String::compareTo))
        ).collect(Collectors.toList());
    }

    private Collection<? extends EasVoucherDTO> zlfkMergeVoucher(List<EasVoucherDTO> zlrkVoucherList) {

        List<EasVoucherDTO> result = Lists.newArrayList();
        zlrkVoucherList.stream().collect(Collectors.groupingBy(EasVoucherDTO::getZjBatchNo)).forEach((batchNo, datas) -> {
            if (datas.size() == 1) {
                result.addAll(datas);
            }
            List<EasVoucherDTO> drs = datas.stream().filter(v -> v.getEntryDC() == 1).collect(Collectors.toList());
            String fid = drs.get(0).getFid();
            List<EasVoucherDTO> collect = drs.stream().peek(v -> v.setFid(fid)).collect(Collectors.toList());
            result.addAll(collect);
            List<EasVoucherDTO> crs = datas.stream().filter(v -> v.getEntryDC() == 0).collect(Collectors.toList());
            Optional<BigDecimal> crSum = crs.stream().map(EasVoucherDTO::getCreditAmount).reduce(BigDecimal::add);
            Optional<BigDecimal> oriSum = crs.stream().map(EasVoucherDTO::getOriginalAmount).reduce(BigDecimal::add);
            EasVoucherDTO mergeVo = crs.get(0);
            mergeVo.setFid(fid);
            mergeVo.setPrimaryKey(crs.stream().map(EasVoucherDTO::getPrimaryKey).collect(Collectors.joining(",")));
            mergeVo.setCreditAmount(crSum.get());
            mergeVo.setOriginalAmount(oriSum.get());
            result.add(mergeVo);
        });

        return result;
    }

    public List<EasVoucherDTO> mergeVoucehr(List<EasVoucherDTO> easVoucherDTOList, boolean isZlrkFlag) {
        List<EasVoucherDTO> mergeHtqzList = Lists.newArrayList();
        Map<String, List<EasVoucherDTO>> htqzMap = easVoucherDTOList.stream().collect(Collectors.groupingBy(v -> getKey(v, isZlrkFlag)));
        //按照fid分组
        Map<String, List<EasVoucherDTO>> fidMap = easVoucherDTOList.stream().collect(Collectors.groupingBy(EasVoucherDTO::getFid));
        //获取分组相同的数据
        Map<String, List<String>> sameMap = Maps.newHashMap();
        for (Map.Entry<String, List<EasVoucherDTO>> dto : htqzMap.entrySet()) {
            if (dto.getValue().size() > 1) {
                String key = dto.getValue().get(0).getFid();
                List<String> valuesList = Lists.newArrayList();
                //先校验是否已经存在
                for (EasVoucherDTO voucherDTO : dto.getValue()) {
                    if (sameMap.containsKey(voucherDTO.getFid())) {
                        key = voucherDTO.getFid();
                        break;
                    }
                }
                if (sameMap.containsKey(key)) {
                    valuesList = sameMap.get(key);
                }
                for (EasVoucherDTO v : dto.getValue()) {
                    if (!key.equals(v.getFid())) {
                        valuesList.add(v.getFid());
                    }
                }
                sameMap.put(key, valuesList);
            }
        }
        //分组相同的合并到一个凭证
        List<EasVoucherDTO> newEasList = Lists.newArrayList();
        //存在合并的凭证fid
        List<String> existGroupFidList = Lists.newArrayList();
        for (Map.Entry<String, List<String>> entry : sameMap.entrySet()) {
            List<EasVoucherDTO> mainDtoList = fidMap.get(entry.getKey());
            newEasList.addAll(mainDtoList);
            for (String s : entry.getValue()) {
                List<EasVoucherDTO> dtoList = fidMap.get(s);
                dtoList.forEach(v -> {
                            v.setFid(entry.getKey());
                            v.setVoucherNumber(mainDtoList.get(0).getVoucherNumber());
                        }
                );
                newEasList.addAll(dtoList);
            }
            existGroupFidList.add(entry.getKey());
            existGroupFidList.addAll(entry.getValue());
        }
        easVoucherDTOList.forEach(v -> {
            if (!existGroupFidList.contains(v.getFid())) {
                mergeHtqzList.add(v);
            }
        });
        //凭证里分录相同的再合并到一个分录上,再次分组 只有zlsk 需要将分组一致的再次合并金额相加
        if (CollectionUtils.isNotEmpty(newEasList)) {
            if (isZlrkFlag) {
                mergeHtqzList.addAll(newEasList);
            } else {
                Map<String, List<EasVoucherDTO>> newVoucherMap = newEasList.stream().collect(Collectors.groupingBy(v -> getKey(v, isZlrkFlag)));
                for (Map.Entry<String, List<EasVoucherDTO>> entry : newVoucherMap.entrySet()) {
                    EasVoucherDTO newDto = entry.getValue().get(0);
                    //金额相加
                    for (EasVoucherDTO dtos : entry.getValue()) {
                        if (newDto.getUuid().equals(dtos.getUuid())) {
                            continue;
                        }
                        BigDecimal amount = BigDecimal.ZERO;
                        if (newDto.getEntryDC() == 1) {
                            if (null != dtos.getDebitAmount()) {
                                amount = dtos.getDebitAmount();
                                newDto.setDebitAmount((null != newDto.getDebitAmount() ? newDto.getDebitAmount() : BigDecimal.ZERO).add(amount));
                            }
                        } else {
                            if (null != dtos.getCreditAmount()) {
                                amount = dtos.getCreditAmount();
                                newDto.setCreditAmount((null != newDto.getCreditAmount() ? newDto.getCreditAmount() : BigDecimal.ZERO).add(amount));
                            }
                        }
                        //originalAmount
                        newDto.setOriginalAmount((null != newDto.getOriginalAmount() ? newDto.getOriginalAmount() : BigDecimal.ZERO).add(amount));
                        //拼接PrimeryKey
                        newDto.setPrimaryKey(newDto.getPrimaryKey() + "," + dtos.getPrimaryKey());
                    }
                    mergeHtqzList.add(newDto);
                }
            }
        }
        return mergeHtqzList;
    }

    public String getKey(EasVoucherDTO v, boolean isZlrkFlag) {
        String key = v.getSystemCode() + "-" + v.getSceneCode() + "-"
                + v.getCompanyNumber();
        if (isZlrkFlag) {
            key = key + "-" + v.getInterfaceId();
        } else {
            key = key + "-" + v.getAsstActNumber1() + "-" + v.getAccountNumber() + "-" + v.getVoucherAbstract()
                    + "-" + v.getAsstActNumber2() + "-" + v.getEntryDC();
        }
        return key;

    }

    public Map<String, String> getClientInfoMap(List<String> clientCodeList) {
        Map<String, String> map = Maps.newHashMap();
        if (CollectionUtils.isEmpty(clientCodeList)) {
            return map;
        }
        List<ClientEntity> clientEntityList = iClientService.lambdaQuery().in(ClientEntity::getClientCode, clientCodeList).list();
        if (CollectionUtils.isNotEmpty(clientEntityList)) {
            map = clientEntityList.stream().collect(HashMap::new, (h, item) -> h.put(item.getClientCode(), item.getClientName()), HashMap::putAll);
        }
        return map;
    }
}
