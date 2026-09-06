package com.utfinancing.financehub.engine.contractstatusupdate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.admin.api.RemoteDictService;
import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.approve.entity.ApproveEntity;
import com.utfinancing.financehub.engine.contractstatusupdate.entity.SysDictTypeEntity;
import com.utfinancing.financehub.engine.contractstatusupdate.service.IContractStatusUpdateService;
import com.utfinancing.financehub.engine.contractstatusupdate.service.ISysDictTypeService;
import com.utfinancing.financehub.engine.enums.BatchTypeEnum;
import com.utfinancing.financehub.engine.enums.DictTypeEnum;
import com.utfinancing.financehub.engine.enums.FinancialContractStatusEnum;
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.mapper.ContractStatusRecordMapper;
import com.utfinancing.financehub.engine.finance.mapper.RecyclingEquipmentInDetailMapper;
import com.utfinancing.financehub.engine.finance.mapper.RecyclingEquipmentOutDetailMapper;
import com.utfinancing.financehub.engine.finance.model.vo.ContractVO;
import com.utfinancing.financehub.engine.finance.service.IContractMonthService;
import com.utfinancing.financehub.engine.finance.service.IContractService;
import com.utfinancing.financehub.engine.utils.CommonDateUtils;
import com.utfinancing.financehub.engine.verification.entity.VerificationDetailsEntity;
import com.utfinancing.financehub.engine.verification.service.IVerificationDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ContractStatusUpdateServiceImpl implements IContractStatusUpdateService {

    @Resource
    private IVerificationDetailsService iVerificationDetailsService;
    @Resource
    private IContractService iContractService;
    @Resource
    private IContractMonthService iContractMonthService;
    @Resource
    private RemoteDictService remoteDictService;
    @Resource
    private ContractStatusRecordMapper contractStatusRecordMapper;
    @Resource
    private ISysDictTypeService iSysDictTypeService;
    @Resource
    private RecyclingEquipmentInDetailMapper recyclingEquipmentInDetailMapper;
    @Resource
    private RecyclingEquipmentOutDetailMapper recyclingEquipmentOutDetailMapper;


    /**
     * 我的单据-审批_更新合同财务状态
     *
     * @param approveEntityList
     * @return
     */
    @Override
    public boolean updateContractStatus(List<ApproveEntity> approveEntityList) {
        List<Long> HZHXIdList = new ArrayList<>();
        List<Long> TSHTIdList = new ArrayList<>();
        approveEntityList.forEach(v -> {
            if (BatchTypeEnum.HZHX.getCode().equals(v.getDocumentType())) {
                HZHXIdList.add(v.getDocumentId());
            } else if (BatchTypeEnum.TSHT.getCode().equals(v.getDocumentType())) {
                TSHTIdList.add(v.getDocumentId());
            }
        });
        // 坏账核销更新合同表财务合同状态
        if(CollectionUtils.isNotEmpty(HZHXIdList))
            this.updateHZHXStatus(HZHXIdList);
        // 特殊合同更新合同表财务合同状态
        if(CollectionUtils.isNotEmpty(TSHTIdList))
            this.updateTSHTStatus(TSHTIdList);
        return true;
    }

    /**
     * 坏账核销更新合同表财务合同状态
     * @param entityList
     * @return
     */
    @Override
    public boolean updateHZHXContractStatus(List<VerificationDetailsEntity> entityList) {
        List<Long> HZHXIdList = new ArrayList<>();
        entityList.forEach(v -> {
            HZHXIdList.add(v.getVerificationId());
        });
        // 坏账核销更新合同表财务合同状态
        if(CollectionUtils.isNotEmpty(HZHXIdList))
            this.updateHZHXStatus(HZHXIdList);
        return true;
    }

    /**
     * 坏账核销更新合同月表合同财务状态实际操作方法
     *
     * @param idList
     * @return
     */
    private boolean updateHZHXStatus(List<Long> idList) {
        log.info("坏账核销更新合同月表合同财务状态开始...");
        //获取详情信息
        List<VerificationDetailsEntity> saveDetailsEntityList = iVerificationDetailsService.lambdaQuery().in(VerificationDetailsEntity::getVerificationId, idList).list();

        //过滤掉状态是非亏损结清的数据
        List<VerificationDetailsEntity> newDetailList = saveDetailsEntityList.stream()
                .filter(v -> !FinancialContractStatusEnum.THREE.getDesc().equals(v.getFinancialContractStatus())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(newDetailList)) {
            return false;
        }
        List<ContractVO> contractVOList = Lists.newArrayList();
        newDetailList.forEach(v -> {
            ContractVO contractVO = BeanUtil.copyProperties(v, ContractVO.class);
            contractVO.setSourceFromId(v.getId());
            contractVO.setSourceFromType(BatchTypeEnum.HZHX.getCode());
            contractVO.setFinancialContractStatusUpdateTime(CommonDateUtils.parseLocalDateTimeToDate(v.getAccountDate()));
            contractVOList.add(contractVO);
        });
        iContractService.verificationSaveRecordList(contractVOList);
        //更新合同的财务合同时间 按照财务合同状态分组
        Map<String, List<VerificationDetailsEntity>> detailsEntityMap = newDetailList.stream().collect(Collectors.groupingBy(v->v.getOrgId().concat("|").concat(v.getFinancialContractStatus())));
        for (Map.Entry<String, List<VerificationDetailsEntity>> entry : detailsEntityMap.entrySet()) {
            LocalDateTime localDateTime = entry.getValue().get(0).getAccountDate();
            iContractService.lambdaUpdate().set(ContractEntity::getFinancialContractStatus, entry.getKey().split("\\|")[1])
                    .set(ContractEntity::getFinancialContractStatusUpdateTime, localDateTime)
                    .in(ContractEntity::getContractCode, entry.getValue().stream().map(VerificationDetailsEntity::getContractCode).filter(StringUtils::isNotEmpty).collect(Collectors.toList()))
                    .eq(ContractEntity::getOrgId, entry.getValue().get(0).getOrgId()).update();

        }
        // 是否更新合同月表合同财务状态
        int count = 0;
        if (this.isUpdateContractStatusEnable(BatchTypeEnum.HZHX.getCode())) {
            for (Map.Entry<String, List<VerificationDetailsEntity>> entry : detailsEntityMap.entrySet()) {
                count = count + entry.getValue().size();
                LocalDateTime localDateTime = entry.getValue().get(0).getAccountDate();
                iContractMonthService.lambdaUpdate().set(ContractMonthEntity::getFinancialContractStatus, entry.getKey().split("\\|")[1])
                        .set(ContractMonthEntity::getFinancialContractStatusUpdateTime, localDateTime)
                        .in(ContractMonthEntity::getContractCode, entry.getValue().stream().map(VerificationDetailsEntity::getContractCode).filter(StringUtils::isNotEmpty).collect(Collectors.toList()))
                        .eq(ContractMonthEntity::getOrgId, entry.getValue().get(0).getOrgId()).update();

            }
        } else {
            log.info("是否更新开关关闭，不允许修改合同月表财务状态");
        }
        log.info("坏账核销更新合同月表合同财务状态完成，共更新 {} 条。", count);
        return true;
    }


    /**
     * 特殊合同更新合同表财务合同状态
     * @param entityList
     * @return
     */
    @Override
    public boolean updateTSZTContractStatus(List<ContractStatusRecordEntity> entityList) {
        List<Long> TSHTIdList = new ArrayList<>();
        entityList.forEach(v -> {
            TSHTIdList.add(v.getId());
        });
        // 特殊合同更新合同表财务合同状态
        if(CollectionUtils.isNotEmpty(TSHTIdList))
            this.updateTSHTStatus(TSHTIdList);
        return true;
    }

    /**
     * 特殊合同更新合同月表合同财务状态实际操作方法
     *
     * @param idList
     * @return
     */
    private boolean updateTSHTStatus(List<Long> idList) {
        log.info("特殊合同更新合同月表合同财务状态开始...");
        List<ContractStatusRecordEntity> recordEntityList = ChainWrappers.lambdaQueryChain(contractStatusRecordMapper, ContractStatusRecordEntity.class).
                in(ContractStatusRecordEntity::getId, idList).list();
        recordEntityList.forEach(v -> {
            CompletableFuture.runAsync(() -> {
                iContractService.lambdaUpdate().set(ContractEntity::getFinancialContractStatus, v.getFinancialContractStatus()).set(ContractEntity::getFinancialContractStatusUpdateTime, v.getFinancialContractStatusUpdateTime())
                        .eq(ContractEntity::getContractCode, v.getContractCode()).eq(ContractEntity::getOrgId, v.getOrgId()).update();
            });
        });
        // 是否更新合同月表合同财务状态
        if (this.isUpdateContractStatusEnable(BatchTypeEnum.TSHT.getCode())) {
            recordEntityList.forEach(v -> {
                CompletableFuture.runAsync(() -> {
                    iContractMonthService.lambdaUpdate().set(ContractMonthEntity::getFinancialContractStatus, v.getFinancialContractStatus()).set(ContractMonthEntity::getFinancialContractStatusUpdateTime, v.getFinancialContractStatusUpdateTime())
                            .eq(ContractMonthEntity::getContractCode, v.getContractCode()).eq(ContractMonthEntity::getOrgId, v.getOrgId()).update();
                });
            });
        } else {
            log.info("是否更新开关关闭，不允许修改合同月表财务状态");
        }
        log.info("特殊合同更新合同月表合同财务状态完成，共更新 {} 条。", recordEntityList.size());
        return true;
    }

    /**
     * 回收设备财务入库更新合同表财务合同状态
     *
     * @param entityList
     * @return
     */
    @Override
    public boolean updateHSSBCWRKContractStatus(List<RecyclingEquipmentInEntity> entityList) {
        List<Long> HSSBCWRKIdList = new ArrayList<>();
        entityList.forEach(v -> {
            HSSBCWRKIdList.add(v.getId());
        });
        // 特殊合同更新合同表财务合同状态
        if(CollectionUtils.isNotEmpty(HSSBCWRKIdList))
            this.updateHSSBCWRKIdListStatus(HSSBCWRKIdList);
        return true;
    }

    /**
     * 回收设备财务入库更新合同表财务合同状态实际操作方法
     *
     * @param idList
     * @return
     */
    private boolean updateHSSBCWRKIdListStatus(List<Long> idList) {
        log.info("设备财务入库更新合同月表合同财务状态开始...");
        List<RecyclingEquipmentInDetailEntity> detailEntityList = ChainWrappers.lambdaQueryChain(recyclingEquipmentInDetailMapper, RecyclingEquipmentInDetailEntity.class).
                in(RecyclingEquipmentInDetailEntity::getRecycleId, idList).list();
        detailEntityList.forEach(v -> {
            CompletableFuture.runAsync(() -> {
                iContractService.lambdaUpdate().set(ContractEntity::getFinancialContractStatus, v.getFinancialContractStatus()).set(ContractEntity::getFinancialContractStatusUpdateTime, this.parseDateStringToDate(v.getInboundDate()))
                        .eq(ContractEntity::getContractCode, v.getContractCode()).eq(ContractEntity::getOrgId, v.getOrgId()).update();
            });
        });
        // 是否更新合同月表合同财务状态
        if (this.isUpdateContractStatusEnable(BatchTypeEnum.HSSBCWRK.getCode())) {
            detailEntityList.forEach(v -> {
                CompletableFuture.runAsync(() -> {
                    iContractMonthService.lambdaUpdate().set(ContractMonthEntity::getFinancialContractStatus, v.getFinancialContractStatus()).set(ContractMonthEntity::getFinancialContractStatusUpdateTime, this.parseDateStringToDate(v.getInboundDate()))
                            .eq(ContractMonthEntity::getContractCode, v.getContractCode()).eq(ContractMonthEntity::getOrgId, v.getOrgId()).update();
                });
            });
        } else {
            log.info("是否更新开关关闭，不允许修改合同月表财务状态");
        }
        log.info("设备财务入库更新合同月表合同财务状态完成，共更新 {} 条。", detailEntityList.size());
        return true;
    }

    /**
     * 回收设备财务出库更新合同表财务合同状态
     *
     * @param entityList
     * @return
     */
    @Override
    public boolean updateHSSBCWCKContractStatus(List<RecyclingEquipmentOutDetailEntity> entityList) {
        List<Long> HSSBCWCKIdList = new ArrayList<>();
        entityList.forEach(v -> {
            HSSBCWCKIdList.add(v.getId());
        });
        // 批量更新合同表财务合同状态
        if(CollectionUtils.isNotEmpty(HSSBCWCKIdList))
            this.updateHSSBCWCKIdListStatus(HSSBCWCKIdList);
        return true;
    }

    /**
     * 回收设备财务出库更新合同表财务合同状态实际操作方法
     *
     * @param idList
     * @return
     */
    private boolean updateHSSBCWCKIdListStatus(List<Long> idList) {
        log.info("设备财务出库更新合同月表合同财务状态开始...");
        List<RecyclingEquipmentOutDetailEntity> detailEntityList = ChainWrappers.lambdaQueryChain(recyclingEquipmentOutDetailMapper, RecyclingEquipmentOutDetailEntity.class).
                in(RecyclingEquipmentOutDetailEntity::getId, idList).list();
        detailEntityList.forEach(v -> {
            CompletableFuture.runAsync(() -> {
                iContractService.lambdaUpdate().set(ContractEntity::getFinancialContractStatus, null).set(ContractEntity::getFinancialContractStatusUpdateTime, this.parseDateStringToDate(v.getOutboundDate()))
                        .eq(ContractEntity::getContractCode, v.getContractCode()).eq(ContractEntity::getOrgId, v.getOrgId()).update();
            });
        });
        // 是否更新合同月表合同财务状态
        if (this.isUpdateContractStatusEnable(BatchTypeEnum.HSSBCWCK.getCode())) {
            detailEntityList.forEach(v -> {
                CompletableFuture.runAsync(() -> {
                    iContractMonthService.lambdaUpdate().set(ContractMonthEntity::getFinancialContractStatus, null).set(ContractMonthEntity::getFinancialContractStatusUpdateTime, this.parseDateStringToDate(v.getOutboundDate()))
                            .eq(ContractMonthEntity::getContractCode, v.getContractCode()).eq(ContractMonthEntity::getOrgId, v.getOrgId()).update();
                });
            });
        } else {
            log.info("是否更新开关关闭，不允许修改合同月表财务状态");
        }
        log.info("设备财务出库更新合同月表合同财务状态完成，共更新 {} 条。", detailEntityList.size());
        return true;
    }


    /**
     * 检查参数设置是否开启修改合同月表合同状态
     *
     * @param key
     * @return
     */
    private boolean isUpdateContractStatusEnable(String key) {
        if (key == null) {
            return false;
        }
        try {
            List<SysDictTypeEntity> dictTypeEntityList =iSysDictTypeService.lambdaQuery().eq(SysDictTypeEntity::getDictType, DictTypeEnum.UPDATE_FINANCIAL_CONTRACT_STATUS.getCode()).list();
            if(CollectionUtils.isNotEmpty(dictTypeEntityList)) {
                SysDictTypeEntity dictTypeEntity = dictTypeEntityList.get(0);
                if(dictTypeEntity.getStatus() == null)
                    return false;
                if(StringUtils.equals("1", dictTypeEntity.getStatus()))
                    return false;
            } else {
                return false;
            }
            R<List<SysDictData>> statusR = remoteDictService.listDictData(DictTypeEnum.UPDATE_FINANCIAL_CONTRACT_STATUS.getCode());
            if (statusR == null || statusR.getData() == null) {
                return false;
            }
            Map<String, SysDictData> statusMap = statusR.getData().stream()
                    .filter(e -> e.getDictValue() != null) // 过滤掉dictValue为null的条目
                    .collect(Collectors.toMap(e -> e.getDictValue(), e -> e));
            if(statusMap.get(key) == null)
                return false;
            // 获取字典对象
            SysDictData sysDictData = statusMap.get(key);
            if(sysDictData.getStatus() == null)
                return false;
            if(!StringUtils.equals("0", sysDictData.getStatus()))
                return false;
            return "Y".equals(sysDictData.getDictLabel());
        } catch (Exception e) {
            // 处理异常，如日志记录
            log.error("检查系统是否开启失败", e);
            return false;
        }
    }

    /**
     * 日期转换方法
     *
     * @param dateString
     * @return
     */
    private Date parseDateStringToDate(String dateString){
        // 1. 定义可能的日期时间格式
        List<DateTimeFormatter> FORMATTERS = new ArrayList<>();
        // 带时间的格式
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
        // 仅日期的格式
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        // 尝试用每种格式解析
        // 2. 遍历格式，尝试解析
        ZoneId zoneId =  ZoneId.systemDefault();
        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                // 情况1：字符串带时间，直接解析为LocalDateTime
                LocalDateTime localDateTime = LocalDateTime.parse(dateString, formatter);
                // 绑定时区 -> 转换为Instant（时间戳）-> 桥接为Date
                return Date.from(localDateTime.atZone(zoneId).toInstant());
            } catch (DateTimeParseException e1) {
                // 情况2：字符串仅日期，先解析为LocalDate，再补充00:00:00时间
                try {
                    LocalDate localDate = LocalDate.parse(dateString, formatter);
                    // 绑定时区 -> 转换为Instant -> 桥接为Date
                    return Date.from(localDate.atStartOfDay(zoneId).toInstant());
                } catch (DateTimeParseException e2) {
                    // 该格式不匹配，继续尝试下一种
                    continue;
                }
            }
        }
        // 所有格式都失败，抛出异常
        throw new DateTimeParseException("无法解析字符串: " + dateString + "，请检查格式是否匹配", dateString, 0);
    }
}
