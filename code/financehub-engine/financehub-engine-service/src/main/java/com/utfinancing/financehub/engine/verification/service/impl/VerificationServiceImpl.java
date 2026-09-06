package com.utfinancing.financehub.engine.verification.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.nacos.common.utils.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.utfinancing.financehub.admin.api.RemoteDictService;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.common.security.utils.SecurityUtils;
import com.utfinancing.financehub.engine.approve.model.dto.ApproveDTO;
import com.utfinancing.financehub.engine.approve.service.IApproveService;
import com.utfinancing.financehub.engine.contractstatusupdate.service.IContractStatusUpdateService;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.*;
import com.utfinancing.financehub.engine.finance.service.*;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import com.utfinancing.financehub.engine.model.dto.CourtCostVerificationDTO;
import com.utfinancing.financehub.engine.rule.model.dto.ExecuteCommonDTO;
import com.utfinancing.financehub.engine.rule.model.vo.VoucherInfoVO;
import com.utfinancing.financehub.engine.rule.service.IInterfaceDataService;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import com.utfinancing.financehub.engine.utils.CommonDateUtils;
import com.utfinancing.financehub.engine.utils.ContractStatusUpdateUtils;
import com.utfinancing.financehub.engine.utils.PeriodCodeUtil;
import com.utfinancing.financehub.engine.utils.UserUtils;
import com.utfinancing.financehub.engine.verification.entity.VerificationDetailsEntity;
import com.utfinancing.financehub.engine.verification.model.dto.*;
import com.utfinancing.financehub.engine.verification.model.vo.VerificationDetailsVO;
import com.utfinancing.financehub.engine.verification.model.vo.VerificationPaybackDetailsVO;
import com.utfinancing.financehub.engine.verification.model.vo.VerificationPaybackVO;
import com.utfinancing.financehub.engine.verification.model.vo.VerificationVO;
import com.utfinancing.financehub.engine.verification.entity.VerificationEntity;
import com.utfinancing.financehub.engine.verification.mapper.VerificationMapper;
import com.utfinancing.financehub.engine.verification.service.IVerificationDetailsService;
import com.utfinancing.financehub.engine.verification.service.IVerificationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @Author : bruyang
 * @Date : Create in 2023-10-11
 * @Description :  Verification服务实现类
 * @Modified :
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class VerificationServiceImpl extends ServiceImpl<VerificationMapper, VerificationEntity> implements IVerificationService {

    private final VerificationMapper verificationMapper;

    @Resource
    private IContractService iContractService;

    @Resource
    private IVerificationDetailsService iVerificationDetailsService;

    @Resource
    private IContractBalanceService iContractBalanceService;

    @Resource
    private IRuleService iRuleService;

    @Resource
    private IInterfaceDataService interfaceDataService;

    @Resource
    private IVoucherService iVoucherService;

    @Resource
    private IOrgCompanyService iOrgCompanyService;

    @Resource
    private IApproveService iApproveService;

    @Value("${approve.url.verification-url:null}")
    private String approveUrl;

    @Resource
    private IOfflineContractService iOfflineContractService;

    @Resource
    private IContractBalanceLatestService iContractBalanceLatestService;

    @Resource
    private IContractStatusRecordService iContractStatusRecordService;

    @Resource
    private IBatchTaskService iBatchTaskService;

    @Value("${file.storage.basicpath.windows:null}")
    private String basicPathWindows;

    @Value("${file.storage.basicpath.linux:null}")
    private String basicPathLinux;

    private final IFileRecordService fileRecordService;

    @Value("${service.parth:null}")
    private String servicePath;

    @Autowired
    @Qualifier("asyncTaskExecutor")
    private ThreadPoolTaskExecutor asyncTaskExecutor;

//    @Resource
//    private RemoteDictService remoteDictService;
//    @Resource
//    private IContractMonthService iContractMonthService;
    @Resource
    private IContractStatusUpdateService contractStatusUpdateService;

    @Override
    public Long saveVerification(VerificationDTO dto) {
        VerificationEntity entity = BeanUtil.copyProperties(dto, VerificationEntity.class);
        entity.setCreateName(UserUtils.getStaffName());
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateVerification(Long id, VerificationDTO dto) {
        VerificationEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public VerificationDTO getVerificationDTOById(Long id) {
        VerificationEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, VerificationDTO.class);
    }

    @Override
    public IPage<VerificationVO> selectPage(VerificationQueryDTO queryDTO) {
        LambdaQueryWrapper<VerificationEntity> queryWrapper = Wrappers.<VerificationEntity>lambdaQuery();
        //这里注入查询条件
        if (ObjectUtil.isNotEmpty(queryDTO.getAccountDate())) {
            queryWrapper.eq(VerificationEntity::getAccountDate,queryDTO.getAccountDate());
        }
        if (StringUtils.isNotEmpty(queryDTO.getOrgId())) {
            queryWrapper.eq(VerificationEntity::getOrgId,queryDTO.getOrgId());
        }
        if (StringUtils.isNotEmpty(queryDTO.getVerificationStatus())) {
            queryWrapper.eq(VerificationEntity::getVerificationStatus,queryDTO.getVerificationStatus());
        }
        if (StringUtils.isNotEmpty(queryDTO.getProcessStatus())) {
            queryWrapper.eq(VerificationEntity::getProcessStatus,queryDTO.getProcessStatus());
        }
        if (CollectionUtils.isNotEmpty(queryDTO.getProcessStatusList())) {
            queryWrapper.in(VerificationEntity::getProcessStatus, queryDTO.getProcessStatusList());
        }
        if (ObjectUtil.isNotNull(queryDTO.getStartAccountDate())) {
            queryWrapper.ge(VerificationEntity::getAccountDate, queryDTO.getStartAccountDate());
        }
        if (ObjectUtil.isNotNull(queryDTO.getEndAccountDate())) {
            queryWrapper.le(VerificationEntity::getAccountDate, queryDTO.getEndAccountDate());
        }
        if (ObjectUtil.isNotNull(queryDTO.getId())) {
            queryWrapper.eq(VerificationEntity::getId, queryDTO.getId());
        }
        queryWrapper.orderByDesc(VerificationEntity::getAccountDate);
        IPage<VerificationEntity> entityIPage = verificationMapper.selectPage(new Page<VerificationEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        IPage<VerificationVO> resultPage = ListBeanUtil.copyPage(entityIPage, VerificationVO.class);
        resultPage.getRecords().stream().forEach(v -> {
            v.setBatchType(BatchTypeEnum.HZHX.getCode());
        });
        return resultPage;
    }

    @Override
    public Boolean deleteByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条数据删除");
        }
        List<VerificationEntity> verificationEntityList = verificationMapper.selectBatchIds(ids);
        verificationEntityList.stream().forEach(v -> {
              if (!(ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus())
              || ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus()))) {
                  throw new ServiceException("处理状态为已录入/已拒绝的才可以删除");
              }
        });
        //逻辑删除
        removeBatchByIds(ids);
        //获取所有的凭证Id
        List<VerificationDetailsEntity> detailsEntityList = iVerificationDetailsService.lambdaQuery().in(VerificationDetailsEntity::getVerificationId, ids).list();
        //逗号拆分
        List<Long> voucherIdList = Lists.newArrayList();
        detailsEntityList.stream().forEach(v -> {
            if (StringUtils.isNotEmpty(v.getVoucherId())) {
                voucherIdList.addAll(Arrays.stream(v.getVoucherId().split(",")).map(Long::parseLong).collect(Collectors.toList()));
            }
        });
        //删除核销明细表
        iVerificationDetailsService.updateBatchByVerificationIdList(ids);
        if (CollectionUtils.isNotEmpty(voucherIdList)) {
            iVoucherService.deleteByIdList(voucherIdList);
        }
        return Boolean.TRUE;
    }

    @Synchronized
    @Override
    public Boolean importTemplate(MultipartFile file) {
        try {
            ExcelUtil<VerificationExcelDTO> util = new ExcelUtil<VerificationExcelDTO>(VerificationExcelDTO.class);
            List<VerificationExcelDTO> verificationExcelDTOS = util.importExcel(file.getInputStream());
            verificationExcelDTOS = checkData(verificationExcelDTOS);
            //按照签约主体分组
            Map<String, List<VerificationExcelDTO>> verificationExcelMap = verificationExcelDTOS.stream().collect(Collectors.groupingBy(VerificationExcelDTO::getOrgId));
            for (Map.Entry<String, List<VerificationExcelDTO>> entry : verificationExcelMap.entrySet()) {
                List<VerificationExcelDTO> verificationExcelDTOList = entry.getValue();
                VerificationExcelDTO excelDTO = verificationExcelDTOList.get(0);
                //保存核销表
                VerificationDTO verificationDTO = new VerificationDTO();
                verificationDTO.setAccountDate(DateUtil.toLocalDateTime(excelDTO.getAccountDate()));
                verificationDTO.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
                verificationDTO.setOrgId(excelDTO.getOrgId());
                //判断系统中是否已存在当前导入日期,签约主体且状态为已录入的数据
                List<VerificationEntity> verificationEntityList = this.list(new LambdaQueryWrapper<VerificationEntity>().eq(VerificationEntity::getProcessStatus, verificationDTO.getProcessStatus())
                        .eq(VerificationEntity::getAccountDate, verificationDTO.getAccountDate())
                        .eq(VerificationEntity::getOrgId, verificationDTO.getOrgId())
                        .eq(VerificationEntity::getIsGenerateVoucher,"0"));
                Long verificationId;
                if (CollectionUtils.isNotEmpty(verificationEntityList)) {
                    verificationId = verificationEntityList.get(0).getId();
                } else {
                    verificationId = saveVerification(verificationDTO);
                }
                saveVericationDetails(verificationExcelDTOList, verificationId);
                //刷新财务核销敞口，补提拨备金额
                refreshVoucherAmount(verificationId);
            }
        } catch (ServiceException serviceException) {
            throw new ServiceException("导入核销数据失败，失败原因："+serviceException.getMessage());
        } catch (Exception exception) {
            throw new ServiceException("导入核销数据失败，失败原因："+exception.getMessage());
        }
        return Boolean.TRUE;
    }

    public List<VerificationExcelDTO> checkData(List<VerificationExcelDTO> verificationExcelDTOList) {
        if (CollectionUtils.isEmpty(verificationExcelDTOList)) {
            throw new ServiceException("没有数据需要上传");
        }
        List<VerificationExcelDTO> newVerificationExcelList = Lists.newArrayList();
        //合同签约主体关系
        Map<String,TreeSet<String>> contractAndOrgIdMap = new HashMap<>();
        //获取系统所有的合同编号
        List<String> contractCodeList = verificationExcelDTOList.stream().map(VerificationExcelDTO::getContractCode).collect(Collectors.toList());
        //获取系统所有的合同+签约主体信息
        ContractQueryDTO queryDTO = new ContractQueryDTO();
        queryDTO.setContractCodeList(contractCodeList);
        List<ContractVO> allContractCodeList = iContractService.allContractList(queryDTO);
        if (CollectionUtils.isNotEmpty(allContractCodeList)) {
            contractAndOrgIdMap = allContractCodeList.stream().collect(Collectors.groupingBy(ContractVO::getContractCode,Collectors.mapping(ContractVO::getOrgId,Collectors.toCollection(
                    () -> new TreeSet<>(String::compareTo)))));
        }
        //查询核销详情所有未生成凭证的数据
        List<VerificationDetailsEntity> detailsEntityList = iVerificationDetailsService.lambdaQuery().isNull(VerificationDetailsEntity::getVoucherId).list();
        Map<String,List<VerificationDetailsEntity>> detailMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(detailsEntityList)) {
            detailMap = detailsEntityList.stream().collect(Collectors.groupingBy(v -> v.getContractCode()+"_" + DateUtil.format(v.getAccountDate(), DateUtils.YYYY_MM_DD)));
        }
        Date accountDate = verificationExcelDTOList.get(0).getAccountDate();
        Map<String, TreeSet<String>> finalContractAndOrgIdMap = contractAndOrgIdMap;
        Map<String, List<VerificationDetailsEntity>> finalDetailMap = detailMap;
        verificationExcelDTOList.stream().forEach(v -> {
            if (StringUtils.isEmpty(v.getContractCode())) {
                throw new ServiceException("合同编码不可以为空");
            }
            if (ObjectUtil.isNull(v.getAccountDate())) {
                throw new ServiceException("记账日期不可以为空");
            }
            if (StringUtils.isEmpty(v.getFinancialContractStatus())) {
                throw new ServiceException("财务合同状态不可以为空");
            }
            if (accountDate.compareTo(v.getAccountDate())!=0) {
                throw new ServiceException("记账日期存在不一致，不能导入");
            }
            if (!finalContractAndOrgIdMap.containsKey(v.getContractCode())) {
                throw new ServiceException(String.format("合同号：%s，在系统中不存在",v.getContractCode()));
            }
            if (finalDetailMap.containsKey(v.getContractCode()+"_"+DateUtil.format(v.getAccountDate(), DateUtils.YYYY_MM_DD))) {
                throw new ServiceException(String.format("合同号：%s，记账日期：%s已经在系统中存在不可重复导入",v.getContractCode(),DateUtil.format(v.getAccountDate(), DateUtils.YYYY_MM_DD)));
            }
            finalContractAndOrgIdMap.get(v.getContractCode()).stream().forEach(f -> {
                VerificationExcelDTO excelDTO = BeanUtil.copyProperties(v, VerificationExcelDTO.class);
                excelDTO.setOrgId(f);
                newVerificationExcelList.add(excelDTO);
            });
        });
        return newVerificationExcelList;
    }

    public void saveVericationDetails(List<VerificationExcelDTO> verificationExcelDTOS, Long verificationId){
        //根据合同编号获取最新的合同科目余额表数据
        List<ContractEntity> newContractEntityList = Lists.newArrayList();
        List<VerificationDetailsEntity> verificationDetailsEntityList = Lists.newArrayList();
        List<String> contractCodeList = verificationExcelDTOS.stream().map(VerificationExcelDTO::getContractCode).collect(Collectors.toList());
        List<String> orgIdList = verificationExcelDTOS.stream().map(VerificationExcelDTO::getOrgId).collect(Collectors.toList());
        QueryWrapper<ContractEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(ContractEntity::getContractCode, contractCodeList);
        queryWrapper.lambda().in(ContractEntity::getOrgId, orgIdList);
        List<ContractEntity> contractEntityList = iContractService.list(queryWrapper);
        if (CollectionUtils.isNotEmpty(contractEntityList)) {
            newContractEntityList.addAll(contractEntityList);
        }
        //线下合同
        QueryWrapper<OfflineContractEntity> offQueryWrapper = new QueryWrapper<>();
        offQueryWrapper.lambda().in(OfflineContractEntity::getContractCode, contractCodeList);
        offQueryWrapper.lambda().in(OfflineContractEntity::getOrgId, orgIdList);
        offQueryWrapper.lambda().in(OfflineContractEntity::getProcessStatus,Lists.newArrayList(ProcessStatusEnum.REVIEWED.getCode(),ProcessStatusEnum.TO_KINGDEE.getCode()));
        List<OfflineContractEntity> offContractList = iOfflineContractService.list(offQueryWrapper);
        if (CollectionUtils.isNotEmpty(offContractList)) {
            offContractList.stream().forEach(o -> {
                ContractEntity contractEntity = BeanUtil.copyProperties(o,ContractEntity.class);
                newContractEntityList.add(contractEntity);
            });
        }
        LocalDateTime accountDate = DateUtil.toLocalDateTime(verificationExcelDTOS.get(0).getAccountDate());
        Map<String,String> finicalStatusMap = verificationExcelDTOS.stream().collect(Collectors.toMap(VerificationExcelDTO::getContractCode, VerificationExcelDTO::getFinancialContractStatus));
        newContractEntityList.stream().forEach(v -> {
            //获取签约主体+合同下的所有最新余额之和
            Map<String, Object> resultMap = getLastBalance(v.getOrgId(),v.getContractCode());
            Map<String,Object> dataMap = new HashMap<>();
            if (null!=resultMap) {
                for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
                    if (entry.getKey().contains("Balance")) {
                        dataMap.put(entry.getKey().substring(0, entry.getKey().lastIndexOf("Balance")), entry.getValue());
                    } else {
                        dataMap.put(entry.getKey(),entry.getValue());
                    }
                }
                VerificationDetailsEntity verificationDetailsEntity;
                verificationDetailsEntity = BeanUtil.toBean(dataMap, VerificationDetailsEntity.class);
                verificationDetailsEntity.setContractCode(v.getContractCode());
                if (finicalStatusMap.containsKey(verificationDetailsEntity.getContractCode())) {
                    verificationDetailsEntity.setFinancialContractStatus(finicalStatusMap.get(verificationDetailsEntity.getContractCode()));
                }
                verificationDetailsEntity.setAccountDate(accountDate);
                verificationDetailsEntity.setVerificationId(verificationId);
                verificationDetailsEntity.setOrgId(v.getOrgId());
                verificationDetailsEntity.setId(IdWorker.getId());
                verificationDetailsEntity.setClientName(v.getClientName());
                verificationDetailsEntity.setClientCode(v.getClientCode());
                verificationDetailsEntity.setCreateName(UserUtils.getStaffName());
                iVerificationDetailsService.calculateFinancialExpenseAmount(verificationDetailsEntity);
                //校验所有金额都为0则不入库
                if (isExistNonZero(verificationDetailsEntity)) {
                    verificationDetailsEntityList.add(verificationDetailsEntity);
                }
            }
        });
        if (CollectionUtils.isNotEmpty(verificationDetailsEntityList)) {
            iVerificationDetailsService.saveBatch(verificationDetailsEntityList);
        } else {
            this.removeById(verificationId);
        }
    }

    public Boolean isExistNonZero(VerificationDetailsEntity detailsEntity) {
        Boolean flag = Boolean.TRUE;
        if ((null==detailsEntity.getReceivableRent() || detailsEntity.getReceivableRent().compareTo(BigDecimal.ZERO)==0)
                && (null == detailsEntity.getReceivableResidualValue() || detailsEntity.getReceivableResidualValue().compareTo(BigDecimal.ZERO)==0)
                && (null == detailsEntity.getReceivableDownpayment() || detailsEntity.getReceivableDownpayment().compareTo(BigDecimal.ZERO)==0)
                && (null == detailsEntity.getReceivableCommission() || detailsEntity.getReceivableCommission().compareTo(BigDecimal.ZERO)==0)
                && (null == detailsEntity.getReceivableInsurance() || detailsEntity.getReceivableInsurance().compareTo(BigDecimal.ZERO)==0)
                && (null == detailsEntity.getReceivableOtherincome() || detailsEntity.getReceivableOtherincome().compareTo(BigDecimal.ZERO)==0)
                && (null == detailsEntity.getReceivableOuttax() || detailsEntity.getReceivableOuttax().compareTo(BigDecimal.ZERO)==0)
                && (null == detailsEntity.getUnrealizedRevenue() || detailsEntity.getUnrealizedRevenue().compareTo(BigDecimal.ZERO)==0)
                && (null == detailsEntity.getPayableDeviceEstimate() || detailsEntity.getPayableDeviceEstimate().compareTo(BigDecimal.ZERO)==0)
                && (null == detailsEntity.getPayableAgencyEstimate() || detailsEntity.getPayableAgencyEstimate().compareTo(BigDecimal.ZERO)==0)
                && (null == detailsEntity.getPayableVehicleEstimate() || detailsEntity.getPayableVehicleEstimate().compareTo(BigDecimal.ZERO)==0)
                && (null == detailsEntity.getPayableBandCostEstimate() || detailsEntity.getPayableBandCostEstimate().compareTo(BigDecimal.ZERO)==0)
                && (null == detailsEntity.getPayablePledgeEstimate() || detailsEntity.getPayablePledgeEstimate().compareTo(BigDecimal.ZERO)==0)
                && (null == detailsEntity.getPayableUnpledgeEstimate() || detailsEntity.getPayableUnpledgeEstimate().compareTo(BigDecimal.ZERO)==0)
                && (null == detailsEntity.getPayableOtherCostEstimate() || detailsEntity.getPayableOtherCostEstimate().compareTo(BigDecimal.ZERO)==0)
                && (null == detailsEntity.getDepreciationReserves() || detailsEntity.getDepreciationReserves().compareTo(BigDecimal.ZERO)==0)

        ) {
            flag = Boolean.FALSE;
        }
        return flag;
    }

    @Override
    public Boolean deleteDetailByDetailId(Long detailId) {
        //获取核销信息
        VerificationDetailsEntity verificationDetailsEntity = iVerificationDetailsService.getById(detailId);
        if (null == verificationDetailsEntity) {
            throw new ServiceException("核销详情信息不存在");
        }
        //获取核销状态
        VerificationDTO verificationVO = getVerificationDTOById(verificationDetailsEntity.getVerificationId());
        if (!(ProcessStatusEnum.ENTERED.getCode().equals(verificationVO.getProcessStatus())
                || ProcessStatusEnum.REJECTED.getCode().equals(verificationVO.getProcessStatus()))) {
            throw new ServiceException("只有处理状态为已录入/已拒绝的才可以删除");
        }
        if (ObjectUtil.isNotNull(verificationDetailsEntity.getVoucherId())) {
            iVoucherService.deleteByIdList(Arrays.stream(verificationDetailsEntity.getVoucherId().split(",")).map(Long::parseLong).collect(Collectors.toList()));
        }
        iVerificationDetailsService.deleteById(detailId);
        //刷新是否发送凭证字段
        refreshVoucherAmount(verificationDetailsEntity.getVerificationId());
        return Boolean.TRUE;
    }

    @Override
    public IPage<VerificationDetailsVO> selectDetailPage(VerificationDetailsQueryDTO queryDTO) {
        return iVerificationDetailsService.selectPage(queryDTO);
    }

    @Override
    public List<VerificationDetailsVO> listByCondition(VerificationDetailsQueryDTO queryDTO) {
        Map<String,String> orgIdAndNameMap = getOrgIdOrgName();
        List<VerificationDetailsVO> detailsVOList = iVerificationDetailsService.listByCondition(queryDTO);
        detailsVOList.stream().forEach(v -> {
            if (orgIdAndNameMap.containsKey(v.getOrgId())) {
                v.setOrgName(orgIdAndNameMap.get(v.getOrgId()));
            }
        });
        return detailsVOList;
    }

    @Transactional
    @Override
    public Long importDetailInfos(MultipartFile file, Long verificationId) {
        iVerificationDetailsService.importDetailInfos(file,verificationId);
        refreshVoucherAmount(verificationId);
        return verificationId;
    }

    @Override
    public Boolean submit(List<Long> idList) {
        //校验任务
        idList.forEach(v -> {
            Boolean isExistFlag = iBatchTaskService.isExistTask(v,BatchTypeEnum.HZHX.getCode());
            if (isExistFlag) {
                throw new ServiceException("存在任务正在执行，请稍后重试");
            }
        });
        //保存任务
        List<Long> taskIdList = Lists.newArrayList();
        idList.forEach(v -> {
            taskIdList.add(iBatchTaskService.saveBatchTask(BatchTaskDTO.builder().businessId(v).businessType(BatchTypeEnum.HZHX.getCode()).status("1").build()));
        });
        List<VerificationEntity> verificationEntityList = this.listByIds(idList);
        List<ApproveDTO> approveDTOList = Lists.newArrayList();
        verificationEntityList.stream().forEach(v -> {
            if (!(ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus())
                    || ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus()))
                    || !"1".equals(v.getIsGenerateVoucher())) {
                throw new ServiceException("只有处理状态为已录入/已拒绝并且已生成凭证的才可以提交");
            }
            v.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
            ApproveDTO approveDTO = new ApproveDTO();
            approveDTO.setDocumentId(v.getId());
            approveDTO.setDocumentType(BatchTypeEnum.HZHX.getCode());
            approveDTO.setUrl(approveUrl+v.getId());
            approveDTOList.add(approveDTO);
        });
        CompletableFuture.runAsync(()-> {
            batchDeleteVoucher(idList);
        //发送审核
        Map<Long,Long> processInstantIdMap = iApproveService.submit(approveDTOList);
        verificationEntityList.stream().forEach(v -> {
            if (null!=processInstantIdMap && processInstantIdMap.containsKey(v.getId())) {
                v.setProcessInstanceId(processInstantIdMap.get(v.getId()));
            }
        });
        //获取详情信息
        List<VerificationDetailsEntity> saveDetailsEntityList = iVerificationDetailsService.lambdaQuery().in(VerificationDetailsEntity::getVerificationId,idList).list();
        //提交生成凭证
        executeVoucher(saveDetailsEntityList,YesOrNoEnum.YES.getCode());
        //提交生成特殊合同
        saveSpecialContract(saveDetailsEntityList);
        updateBatchById(verificationEntityList);
        }).whenComplete((v, e) -> {
            // 执行成功，更新任务状态
            iBatchTaskService.updateBatchTask(taskIdList, "2");
        }).exceptionally(e -> {
            log.info("核销批量处理数据失败", e);
            iBatchTaskService.updateBatchTask(taskIdList, "3");
            return null;
        });;
        return Boolean.TRUE;
    }

    @Override
    public Boolean withdraw(List<Long> idList) {
        List<VerificationEntity> verificationEntityList = this.listByIds(idList);
        verificationEntityList.forEach(verificationEntity -> {
            if (!ProcessStatusEnum.SUBMITTED.getCode().equals(verificationEntity.getProcessStatus())) {
                throw new ServiceException("只有状态为已提交的才可以撤回");
            }
            verificationEntity.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
            verificationEntity.setIsGenerateVoucher("0");
        });
        iApproveService.withdraw(verificationEntityList.stream().map(VerificationEntity::getProcessInstanceId).collect(Collectors.toList()));
        //撤回后需要删除凭证
        //batchDeleteVoucher(idList);
        //撤回之后需要将合同状态的数据删除
        List<Long> detailList = iVerificationDetailsService.lambdaQuery().in(VerificationDetailsEntity::getVerificationId,idList).list().stream().map(VerificationDetailsEntity::getId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(detailList)) {
            iContractStatusRecordService.lambdaUpdate().set(ContractStatusRecordEntity::getDelFlag,"1").in(ContractStatusRecordEntity::getSourceFromId, detailList).eq(ContractStatusRecordEntity::getSourceFromType, BatchTypeEnum.HZHX.getCode()).update();
        }
        iVoucherService.updateStatusByBatch(idList,BatchTypeEnum.HZHX.getCode(), ProcessStatusEnum.ENTERED.getCode(),"","");
        return this.updateBatchById(verificationEntityList);
    }

    @Override
    public Boolean generateVoucher(List<Long> idList) {
        //校验任务
        idList.forEach(v -> {
            Boolean isExistFlag = iBatchTaskService.isExistTask(v,BatchTypeEnum.HZHX.getCode());
            if (isExistFlag) {
                throw new ServiceException("存在任务正在执行，请稍后重试");
            }
        });
        //保存任务
        List<Long> taskIdList = Lists.newArrayList();
        idList.forEach(v -> {
            taskIdList.add(iBatchTaskService.saveBatchTask(BatchTaskDTO.builder().businessId(v).businessType(BatchTypeEnum.HZHX.getCode()).status("1").build()));
        });
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请至少勾选一条生成凭证");
        }
        //获取核销信息
        List<VerificationEntity> verificationEntityList = this.listByIds(idList);
        verificationEntityList.stream().forEach(v -> {
            if (!(ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus())
                    || ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus()))
            ) {
                throw new ServiceException("只有处理状态为已录入/已拒绝才可以生成凭证");
            }
        });
        CompletableFuture.runAsync(()-> {
        //生成凭证前先删除凭证
        iVoucherService.deleteByBatchIdList(idList,BatchTypeEnum.HZHX.getCode());
        VerificationDetailsQueryDTO verificationDetailsQueryDTO = new VerificationDetailsQueryDTO();
        verificationDetailsQueryDTO.setVerificationIdList(idList);
        List<VerificationDetailsVO> verificationDetailsVOList = iVerificationDetailsService.listByCondition(verificationDetailsQueryDTO);
        Map<Long, List<VerificationDetailsVO>> verificationDetailsMap = verificationDetailsVOList.stream().collect(Collectors.groupingBy(VerificationDetailsVO::getVerificationId));
        //提交时按照财务核销状态 拆分为 正常核销和 亏损结清两条数据
        List<VerificationDetailsEntity> saveDetailsEntityList = Lists.newArrayList();
        for (Map.Entry<Long, List<VerificationDetailsVO>> entry : verificationDetailsMap.entrySet()){
            Map<String, List<VerificationDetailsVO>> detailMap = entry.getValue().stream().collect(Collectors.groupingBy(VerificationDetailsVO::getFinancialContractStatus));
            for (Map.Entry<String, List<VerificationDetailsVO>> detailEntry : detailMap.entrySet()) {
                VerificationEntity verificationEntity = new VerificationEntity();
                List<VerificationDetailsEntity> verificationDetailsEntityList = ListBeanUtil.copyList(detailEntry.getValue(),VerificationDetailsEntity.class, "id");
                verificationEntity.setAccountDate(detailEntry.getValue().get(0).getAccountDate());
                verificationEntity.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
                verificationEntity.setVerificationStatus(detailEntry.getKey());
                BigDecimal financialExpenseAmount = verificationDetailsEntityList.stream().filter(v-> ObjectUtil.isNotEmpty(v.getFinancialExpenseAmount()))
                        .map(VerificationDetailsEntity::getFinancialExpenseAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
                BigDecimal compensationProvisionAmount = verificationDetailsEntityList.stream().filter(v-> ObjectUtil.isNotEmpty(v.getCompensationProvisionAmount()))
                        .map(VerificationDetailsEntity::getCompensationProvisionAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
                verificationEntity.setFinancialExpenseAmount(financialExpenseAmount);
                verificationEntity.setCompensationProvisionAmount(compensationProvisionAmount);
                verificationEntity.setIsGenerateVoucher("1");
                verificationEntity.setOrgId(detailEntry.getValue().get(0).getOrgId());
                this.save(verificationEntity);
                verificationDetailsEntityList.stream().forEach(v -> {
                    v.setVerificationId(verificationEntity.getId());
                });
                iVerificationDetailsService.saveBatch(verificationDetailsEntityList);
                saveDetailsEntityList.addAll(verificationDetailsEntityList);
            }
        }
        //生成凭证
        executeVoucher(saveDetailsEntityList,YesOrNoEnum.NO.getCode());
        //删除拆分前的数据
        deleteByIds(idList);
        },asyncTaskExecutor).whenComplete((v, e) -> {
            // 执行成功，更新任务状态
          iBatchTaskService.updateBatchTask(taskIdList, "2");
        }).exceptionally(e -> {
            log.info("核销批量处理数据失败", e);
            iBatchTaskService.updateBatchTask(taskIdList, "3");
            return null;
        });
        return Boolean.TRUE;
    }

    public void refreshVoucherAmount(Long verificationId){
        VerificationEntity verificationEntity = this.getById(verificationId);
        if (null == verificationEntity) {
            return;
        }
        VerificationDetailsQueryDTO verificationDetailsQueryDTO = new VerificationDetailsQueryDTO();
        verificationDetailsQueryDTO.setVerificationId(verificationId);
        List<VerificationDetailsVO> verificationDetailsVOList = iVerificationDetailsService.listByCondition(verificationDetailsQueryDTO);
        final BigDecimal[] financialExpenseAmount = {BigDecimal.ZERO};
        final BigDecimal[] compensationProvisionAmount = {BigDecimal.ZERO};
        if (CollectionUtils.isNotEmpty(verificationDetailsVOList)) {
            verificationDetailsVOList.stream().forEach(v -> {
                if (ObjectUtil.isNotEmpty(v.getFinancialExpenseAmount())) {
                    financialExpenseAmount[0] = financialExpenseAmount[0].add(v.getFinancialExpenseAmount());
                }
                if (ObjectUtil.isNotEmpty(v.getCompensationProvisionAmount())) {
                    compensationProvisionAmount[0] = compensationProvisionAmount[0].add(v.getCompensationProvisionAmount());
                }
            });
        } else {
            verificationEntity.setIsGenerateVoucher("0");
        }
        verificationEntity.setFinancialExpenseAmount(financialExpenseAmount[0]);
        verificationEntity.setCompensationProvisionAmount(compensationProvisionAmount[0]);
        this.updateById(verificationEntity);
    }

    @Override
    public IPage<VerificationPaybackVO> selectPaybackPage(VerificationPaybackQueryDTO queryDTO) {
        //转换为会计期间
        Integer startPeriodCode = Integer.parseInt(queryDTO.getStartBusinessDate().replace("-", ""));
        Integer endPeriodCode = Integer.parseInt(queryDTO.getEndBusinessDate().replace("-", ""));
        queryDTO.setStartPeriodCode(startPeriodCode);
        queryDTO.setEndPeriodCode(endPeriodCode);
        IPage<VerificationPaybackVO> paybackVOIPage = interfaceDataService.selectPaybackPage(queryDTO);
//        List<VerificationPaybackVO> paybackVOList = paybackVOIPage.getRecords();
//        paybackVOList.stream().forEach(v -> {
//            //计算金额
//            VerificationPaybackQueryDTO paybackQueryDTO = new VerificationPaybackQueryDTO();
//            paybackQueryDTO.setOrgId(v.getOrgId());
//            paybackQueryDTO.setBusinessDate(v.getBusinessDate());
//            paybackQueryDTO.setPeriodCode(Integer.parseInt(v.getBusinessDate().replace("-", "")));
//            List<VerificationPaybackDetailsVO> detailsVOList = interfaceDataService.listPaybackDetails(paybackQueryDTO);
////            IPage<VerificationPaybackDetailsVO> iPage = selectPaybackDetailsPage(paybackQueryDTO);
//            final BigDecimal[] reversalProvisionAmount = {BigDecimal.ZERO};
//            final BigDecimal[] revenueFinance = {BigDecimal.ZERO};
//            if (null!=detailsVOList) {
//                detailsVOList.stream().forEach(s -> {
//                    reversalProvisionAmount[0] = reversalProvisionAmount[0].add(null==s.getDepreciationLossAmount()?BigDecimal.ZERO:s.getDepreciationLossAmount());
//                    revenueFinance[0] = revenueFinance[0].add(null==s.getLeaseRevenueAmount()?BigDecimal.ZERO:s.getLeaseRevenueAmount());
//                });
//            }
//            v.setRevenueFinance(revenueFinance[0]);
//            v.setReversalProvisionAmount(reversalProvisionAmount[0].multiply(new BigDecimal(-1)));
//        });
        return paybackVOIPage;
    }

    @Override
    public IPage<VerificationPaybackDetailsVO> selectPaybackDetailsPage(VerificationPaybackQueryDTO queryDTO) {
        queryDTO.setPeriodCode(Integer.parseInt(queryDTO.getBusinessDate().replace("-","")));
        queryDTO.setLastPeriodCode(PeriodCodeUtil.getLastMonthPeriodCode(queryDTO.getPeriodCode()));
        IPage<VerificationPaybackDetailsVO> interfaceDataEntityIPage = interfaceDataService.selectPaybackDetailsPage(queryDTO);
        if (CollectionUtils.isNotEmpty(interfaceDataEntityIPage.getRecords())) {
            List<VerificationPaybackDetailsVO> collect = interfaceDataEntityIPage.getRecords().stream().filter(v -> v.getDepreciationReservesAmount() == null).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(collect)) {

            }
            interfaceDataEntityIPage.getRecords().forEach(v -> {
                //缺省处理，chargeoff汇总表没有从核销表取值
                if (v.getDepreciationReservesAmount()==null){
                    BigDecimal maxDepreciationReserves = verificationMapper.getMaxDepreciationReservesByPeriodCode(v.getContractCode(), v.getOrgId(), queryDTO.getPeriodCode());
                    v.setDepreciationReservesAmount(maxDepreciationReserves);
                    v.setDepreciationAmount(Optional.ofNullable(v.getDepreciationReservesAmount()).orElse(BigDecimal.ZERO).subtract(Optional.ofNullable(v.getDepreciationLossAmount()).orElse(BigDecimal.ZERO)));
                }
                v.setIsAbnormal("0");
                v.setDepreciationAmount(null == v.getDepreciationAmount() ? BigDecimal.ZERO : v.getDepreciationAmount());
                if (v.getDepreciationAmount().compareTo(BigDecimal.ZERO) !=0) {
                    v.setIsAbnormal("1");
                }
            });
        }


        return interfaceDataEntityIPage;
    }

    @Override
    public List<VerificationPaybackDetailsVO> listPaybackByCondition(List<VerificationPaybackQueryDTO> queryDTOList) {
        if (CollectionUtils.isEmpty(queryDTOList)) {
            throw new ServiceException("请至少勾选一条数据");
        }
        Map<String,String> orgIdAndNameMap = getOrgIdOrgName();
        List<VerificationPaybackDetailsVO> list = Lists.newArrayList();
        queryDTOList.forEach(v -> {
            VerificationPaybackQueryDTO queryDTO = new VerificationPaybackQueryDTO();
            queryDTO.setPageNum(1);
            queryDTO.setPageSize(10000);
            queryDTO.setOrgId(v.getOrgId());
            queryDTO.setBusinessDate(v.getBusinessDate());
            IPage<VerificationPaybackDetailsVO> iPage = selectPaybackDetailsPage(queryDTO);
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                iPage.getRecords().forEach(s -> {
                    if (orgIdAndNameMap.containsKey(v.getOrgId())) {
                        s.setOrgName(orgIdAndNameMap.get(v.getOrgId()));
                    }
                });
                list.addAll(iPage.getRecords());
            }
        });
        return list;
    }

    @Override
    public VerificationPaybackDetailsVO verification(VerificationPaybackQueryDTO queryDTO) {
        VerificationPaybackDetailsVO detailsVO = new VerificationPaybackDetailsVO();
        List<String> greaterThanZeroList = Lists.newArrayList();
        List<String> greaterThanReservesList = Lists.newArrayList();
        List<VerificationPaybackDetailsVO> detailsVOList = selectPaybackDetailsList(queryDTO);
        detailsVOList.forEach(v -> {
            if (v.getDepreciationAmount().compareTo(BigDecimal.ZERO)>0) {
                greaterThanZeroList.add(v.getContractCode());
            }
            if (v.getDepreciationAmount().compareTo(Optional.ofNullable(v.getDepreciationReservesAmount()).orElse(BigDecimal.ZERO))>0) {
                greaterThanReservesList.add(v.getContractCode());
            }
        });
        detailsVO.setGreaterThanZeroList(greaterThanZeroList);
        detailsVO.setGreaterThanReservesList(greaterThanReservesList);
        return detailsVO;
    }

    @Override
    public Boolean updateProcessStatus(CommonApproveDTO commonApproveDTO) {
        if (StringUtils.isEmpty(commonApproveDTO.getDocumentStatus())) {
            throw new ServiceException("核销状态不可以为空");
        }
        VerificationEntity verificationEntity = this.getById(commonApproveDTO.getDocumentId());
        if (null == verificationEntity) {
            throw new ServiceException("核销数据不存在");
        }
        String processStatus = verificationEntity.getProcessStatus();
        if (ProcessStatusEnum.REVIEWED.getCode().equals(commonApproveDTO.getDocumentStatus())) {
            processStatus = ProcessStatusEnum.REVIEWED.getCode();
        } else if (ProcessStatusEnum.REJECTED.getCode().equals(commonApproveDTO.getDocumentStatus())) {
            processStatus = ProcessStatusEnum.REJECTED.getCode();
            //拒绝的时候需要删除特殊合同状态表数据
            List<Long> detailList = iVerificationDetailsService.lambdaQuery().eq(VerificationDetailsEntity::getVerificationId,verificationEntity.getId()).list().stream().map(VerificationDetailsEntity::getId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(detailList)) {
                iContractStatusRecordService.lambdaUpdate().set(ContractStatusRecordEntity::getDelFlag,"1").in(ContractStatusRecordEntity::getSourceFromId, detailList).eq(ContractStatusRecordEntity::getSourceFromType, BatchTypeEnum.HZHX.getCode()).update();
            }
        }
        verificationEntity.setProcessStatus(processStatus);
        //更新凭证表状态
        iVoucherService.updateStatusByBatch(Lists.newArrayList(verificationEntity.getId()),BatchTypeEnum.HZHX.getCode(), processStatus,commonApproveDTO.getApproverNum(),commonApproveDTO.getApproverName());
        return this.updateById(verificationEntity);
    }

    public void updateContract(VerificationDTO verificationDTO){
        //获取核销详情数据
        VerificationDetailsQueryDTO queryDTO = new VerificationDetailsQueryDTO();
        queryDTO.setVerificationIdList(verificationDTO.getIdList());
        List<VerificationDetailsVO> detailsVOList = iVerificationDetailsService.listByCondition(queryDTO);
        if (CollectionUtils.isEmpty(detailsVOList)) {
            return;
        }
        //过滤掉非亏损结清的数据
        detailsVOList = detailsVOList.stream().filter(v-> !FinancialContractStatusEnum.THREE.getCode().equals(v.getFinancialContractStatus())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(detailsVOList)) {
            return;
        }
        //按照合同编码+签约主体分组
        Map<String,List<VerificationDetailsVO>> detailMap =  detailsVOList.stream().collect(Collectors.groupingBy(v -> v.getContractCode()+"-" + v.getOrgId()));
        List<String> contractCodeList = detailsVOList.stream().map(VerificationDetailsVO::getContractCode).collect(Collectors.toList());
        List<String> orgIdList = detailsVOList.stream().map(VerificationDetailsVO::getOrgId).collect(Collectors.toList());
        //根据合同编码+签约主体查询所有的合同信息
        List<ContractEntity> contractEntityList = iContractService.lambdaQuery().in(ContractEntity::getContractCode,contractCodeList).in(ContractEntity::getOrgId,orgIdList).list();
        contractEntityList.stream().forEach(v -> {
            String key = v.getContractCode()+"-"+v.getOrgId();
            //根据合同编码+签约主体+客户编码获取最新的余额表信息，更新合同表销项税和租赁收益余额
            Map<String, Object> resultMap = iContractBalanceService.getLastBalanceMap(v.getBusinessCode(),v.getClientCode(),v.getContractCode(),v.getOrgId(),null);
            if (null != resultMap) {
                BigDecimal outtaxBalance = ObjectUtil.isNotNull(resultMap.get("outtaxBalance")) ? new BigDecimal(resultMap.get("outtaxBalance").toString()) : BigDecimal.ZERO;//销项税
                BigDecimal leaseRevenueBalance = ObjectUtil.isNotNull(resultMap.get("leaseRevenueBalance")) ? new BigDecimal(resultMap.get("leaseRevenueBalance").toString()) : BigDecimal.ZERO;///租赁收益余额
                v.setOuttaxBalance(outtaxBalance);
                v.setLeaseRevenueBalance(leaseRevenueBalance);
            }
            if (detailMap.containsKey(key)){
                v.setFinancialContractStatus(detailMap.get(key).get(0).getFinancialContractStatus());
                v.setFinancialContractStatusUpdateTime(new Date());
            }
        });
        iContractService.updateBatchById(contractEntityList);
    }

    @Override
    public Boolean updateContractAmount(List<CourtCostVerificationDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return Boolean.FALSE;
        }
        Map<String,List<CourtCostVerificationDTO>> detailMap =  dtoList.stream().collect(Collectors.groupingBy(v -> v.getContractCode()+"-" + v.getOrgId()));
        List<String> contractCodeList = dtoList.stream().map(CourtCostVerificationDTO::getContractCode).collect(Collectors.toList());
        List<String> orgIdList = dtoList.stream().map(CourtCostVerificationDTO::getOrgId).collect(Collectors.toList());
        //根据合同编码+签约主体查询所有的合同信息
        List<ContractEntity> contractEntityList = iContractService.lambdaQuery().in(ContractEntity::getContractCode,contractCodeList).in(ContractEntity::getOrgId,orgIdList).list();
        contractEntityList.stream().forEach(v -> {
            String key = v.getContractCode()+"-"+v.getOrgId();
            //根据合同编码+签约主体+客户编码获取最新的余额表信息，更新合同表销项税和租赁收益余额
            Map<String, Object> resultMap = iContractBalanceService.getLastBalanceMap(v.getBusinessCode(),v.getClientCode(),v.getContractCode(),v.getOrgId(),null);
            if (null != resultMap) {
                BigDecimal depreciationLossBalance = ObjectUtil.isNotNull(resultMap.get("depreciationLossBalance")) ? new BigDecimal(resultMap.get("depreciationLossBalance").toString()) : BigDecimal.ZERO;//转回拨备金额
                if (ObjectUtil.isNotNull(v.getDepreciationLossBalance())){
                    depreciationLossBalance = depreciationLossBalance.add(v.getDepreciationLossBalance());
                }
                v.setDepreciationLossBalance(depreciationLossBalance);
            }
        });
        if (CollectionUtils.isNotEmpty(contractEntityList)) {
            iContractService.updateBatchById(contractEntityList);
        }
        return Boolean.TRUE;
    }

    private Map<String,String> getOrgIdOrgName(){
        Map<String,String> orgIdAndNameMap = Maps.newHashMap();
        List<OrgCompanyVO> orgCompanyVOList =  iOrgCompanyService.selectByCondition(new OrgCompanyQueryDTO());
        if (CollectionUtils.isNotEmpty(orgCompanyVOList)) {
            orgIdAndNameMap = orgCompanyVOList.stream().collect(Collectors.toMap(OrgCompanyVO::getOrgId,OrgCompanyVO::getOrgName, (k1,k2)->k2));
        }
        return orgIdAndNameMap;
    }

    public Map<String,Object> getLastBalance(String orgId, String contractCode){
        Map<String,Object> newMap = Maps.newHashMap();
        List<ContractBalanceLatestEntity> latestEntityList = iContractBalanceLatestService.lambdaQuery()
                .eq(ContractBalanceLatestEntity::getOrgId,orgId).eq(ContractBalanceLatestEntity::getContractCode,contractCode).list();
        ContractBalanceLatestEntity newContractBalanceLatest = new ContractBalanceLatestEntity();
        if (CollectionUtils.isNotEmpty(latestEntityList)) {
            latestEntityList.forEach(v -> {
                Map<String,Object> latestMap = BeanUtil.beanToMap(v);
                for (Map.Entry<String, Object> entry : latestMap.entrySet()) {
                    if (entry.getKey().contains("Balance")) {
                        if (newMap.containsKey(entry.getKey())) {
                            BigDecimal balance = (null == newMap.get(entry.getKey()) ? BigDecimal.ZERO : new BigDecimal(newMap.get(entry.getKey()).toString())).add(null == entry.getValue() ? BigDecimal.ZERO : new BigDecimal(entry.getValue().toString()));
                            newMap.put(entry.getKey(), balance);
                        } else {
                            newMap.put(entry.getKey(),entry.getValue());
                        }
                    }
                }
            });
        }
        return newMap;
    }

    public void executeVoucher(List<VerificationDetailsEntity> entityList,String isSubmit){
        if (CollectionUtils.isEmpty(entityList)) {
            throw new ServiceException("没有数据需要生成凭证");
        }
        //提交时按照财务核销状态 拆分为 正常核销和 亏损结清两条数据
        List<Map<String,Object>> voucherMapList = Lists.newArrayList();
        List<Long> idList = Lists.newArrayList();
        //获取主表核销状态
        List<VerificationEntity> verificationEntityList = this.listByIds(entityList.stream().map(VerificationDetailsEntity::getVerificationId).distinct().collect(Collectors.toList()));
        Map<Long,String> verificationMap = verificationEntityList.stream().collect(HashMap::new,(map,item)->map.put(item.getId(),item.getVerificationStatus()),HashMap::putAll);
        //receivableOuttaxHzhx 合同+签约主体查最新余额表sum(receivable_outtax_balance)
        List<String> contractCodeList = entityList.stream().map(VerificationDetailsEntity::getContractCode).filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
        List<String> orgIdList = entityList.stream().map(VerificationDetailsEntity::getOrgId).filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
        List<ContractBalanceLatestEntity> latestEntityList = iContractBalanceLatestService.lambdaQuery().in(ContractBalanceLatestEntity::getContractCode,contractCodeList).in(ContractBalanceLatestEntity::getOrgId,orgIdList).list();
        Map<String,BigDecimal> receivableMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(latestEntityList)) {
            receivableMap = latestEntityList.stream().filter(v->null!=v.getReceivableOuttaxBalance()).collect(Collectors.groupingBy(v->v.getOrgId()+"-"+v.getContractCode(),Collectors.mapping(
                    ContractBalanceLatestEntity::getReceivableOuttaxBalance,
                    Collectors.reducing(
                            BigDecimal.ZERO,
                            BigDecimal::add
                    )
            )));
        }
        //生成凭证
        Map<String, BigDecimal> finalReceivableMap = receivableMap;
        entityList.stream().forEach(v -> {
            idList.add(v.getVerificationId());
            String key = v.getOrgId()+"-"+v.getContractCode();
            ExecuteCommonDTO executeCommonDTO = new ExecuteCommonDTO();
            executeCommonDTO.setSystemCode(SystemEnum.CWZT.getCode());
            executeCommonDTO.setSystemName(SystemEnum.CWZT.getDesc());
            executeCommonDTO.setBusinessCode(BusinessEnum.ZLYW.getCode());
            executeCommonDTO.setBusinessName(BusinessEnum.ZLYW.getDesc());
            executeCommonDTO.setSceneCode(SceneEnum.HZHX.getCode());
            executeCommonDTO.setSceneName(SceneEnum.HZHX.name());
            executeCommonDTO.setOrderId(v.getId().toString());
            executeCommonDTO.setBusinessDate(CommonDateUtils.parseLocalDateTimeToDate(v.getCreateTime()));
            executeCommonDTO.setBatchId(v.getVerificationId());
            executeCommonDTO.setBatchType(BatchTypeEnum.HZHX.getCode());
            executeCommonDTO.setFinanceDate(v.getAccountDate());
            executeCommonDTO.setClientCode(v.getClientCode());
            executeCommonDTO.setClientName(v.getClientName());
            executeCommonDTO.setCreateUserNo(v.getCreateBy());
            executeCommonDTO.setCreateUserName(v.getCreateName());
            executeCommonDTO.setIsSubmit(isSubmit);
            Map<String, Object> dataMap = BeanUtil.beanToMap(executeCommonDTO);
            dataMap.put("periodCode",CommonDateUtils.parseLocalDateTimeToYearMonth(v.getAccountDate()));
            BigDecimal receivableOuttaxHzhx = BigDecimal.ZERO;
            if (finalReceivableMap.containsKey(key)) {
                receivableOuttaxHzhx = finalReceivableMap.get(key);
            }
            dataMap.put("receivableOuttaxHzhx",receivableOuttaxHzhx);
            if (verificationMap.containsKey(v.getVerificationId())) {
                dataMap.put("verificationStatus", verificationMap.get(v.getVerificationId()));
            }
            //业务字段
            VerificationVoucherDTO voucherDTO = JSONObject.parseObject(JSONObject.toJSONString(v), VerificationVoucherDTO.class);
            Map<String, Object> verifDataMap = BeanUtil.beanToMap(voucherDTO);
            dataMap.putAll(verifDataMap);
            if (YesOrNoEnum.YES.getCode().equals(isSubmit)) {
                dataMap.put("financialContractStatusUpdateTime",v.getAccountDate());
            }
            log.info("生成凭证参数：{}", JSON.toJSONString(dataMap));
            voucherMapList.add(dataMap);
        });
        List<VoucherInfoVO> voucherResultList = iRuleService.batchExecuteRule(voucherMapList);
        for (VoucherInfoVO infoVO : voucherResultList) {
            String voucherIds = "";
            String errorInfo = "";
            if (StringUtils.isNotEmpty(infoVO.getErrorInfo())) {
                errorInfo = infoVO.getErrorInfo();
                if (infoVO.getErrorInfo().length()>2000) {
                    errorInfo = infoVO.getErrorInfo().substring(0, 2000);
                }
            }
            Integer periodCode = null;
            if (CollectionUtils.isNotEmpty(infoVO.getVoucherDTOList())) {
                 voucherIds = infoVO.getVoucherDTOList().stream().map(VoucherDTO::getId).map(String::valueOf).collect(Collectors.toList()).stream().collect(Collectors.joining(","));
                periodCode = infoVO.getVoucherDTOList().get(0).getPeriodCode();
            }
            iVerificationDetailsService.lambdaUpdate().set(VerificationDetailsEntity::getVoucherId, voucherIds).set(VerificationDetailsEntity::getPeriodCode,periodCode).set(VerificationDetailsEntity::getErrorInfo,errorInfo).eq(VerificationDetailsEntity::getId,Long.parseLong(infoVO.getOrderId())).update();
        }
        if (CollectionUtils.isNotEmpty(idList)) {
            updateIsGenerateVoucher(idList.stream().distinct().collect(Collectors.toList()), isSubmit);
        }
    }

    public void updateIsGenerateVoucher(List<Long> idList,String isSubmit){
        //获取详情信息
        Map<Long,List<VerificationDetailsEntity>> detailEntityMap = iVerificationDetailsService.lambdaQuery()
                .in(VerificationDetailsEntity::getVerificationId,idList).list().stream().collect(Collectors.groupingBy(VerificationDetailsEntity::getVerificationId));
        for (Map.Entry<Long, List<VerificationDetailsEntity>> entry : detailEntityMap.entrySet()) {
            boolean isExistEmpty = entry.getValue().stream().anyMatch(v -> StringUtils.isEmpty(v.getVoucherId()));
            String isGenerateVoucher = YesOrNoEnum.NO.getCode();
            if (!isExistEmpty) {
                isGenerateVoucher = YesOrNoEnum.YES.getCode();
            }
            this.lambdaUpdate().set(VerificationEntity::getIsGenerateVoucher, isGenerateVoucher).eq(VerificationEntity::getId,entry.getKey()).update();
        }
    }

    public void batchDeleteVoucher(List<Long> ids){
        //获取所有的凭证Id
        List<VerificationDetailsEntity> detailsEntityList = iVerificationDetailsService.lambdaQuery().in(VerificationDetailsEntity::getVerificationId, ids).list();
        //逗号拆分
        List<Long> voucherIdList = Lists.newArrayList();
        detailsEntityList.stream().forEach(v -> {
            if (StringUtils.isNotEmpty(v.getVoucherId())) {
                voucherIdList.addAll(Arrays.stream(v.getVoucherId().split(",")).map(Long::parseLong).collect(Collectors.toList()));
            }
            v.setVoucherId("");
        });
        if (CollectionUtils.isNotEmpty(voucherIdList)) {
            iVoucherService.deleteByIdList(voucherIdList);
        }
        iVerificationDetailsService.updateBatchById(detailsEntityList);
    }

    @Override
    public IPage<VerificationCheckDTO> checkPage(CheckPageQueryDTO queryDTO) {
        return iVerificationDetailsService.checkDataPage(queryDTO);
    }

    @Override
    public LocalDateTime getMaxAccountDate(String contractCode, String orgId) {
        return verificationMapper.getMaxAccountDate(contractCode,orgId);
    }

    @Override
    public BigDecimal getMaxDepreciationReserves(String contractCode, String orgId) {
        return verificationMapper.getMaxDepreciationReserves(contractCode,orgId);
    }

    public void saveSpecialContract( List<VerificationDetailsEntity> saveDetailsEntityList) {
        // 更新坏账核销合同状态
        contractStatusUpdateService.updateHZHXContractStatus(saveDetailsEntityList);

//        //过滤掉状态是非亏损结清的数据
//        List<VerificationDetailsEntity> newDetailList = saveDetailsEntityList.stream()
//                .filter(v ->!FinancialContractStatusEnum.THREE.getDesc().equals(v.getFinancialContractStatus())).collect(Collectors.toList());
//        if (CollectionUtils.isEmpty(newDetailList)) {
//            return;
//        }
//        List<ContractVO> contractVOList = Lists.newArrayList();
//        newDetailList.forEach(v -> {
//            ContractVO contractVO = BeanUtil.copyProperties(v,ContractVO.class);
//            contractVO.setSourceFromId(v.getId());
//            contractVO.setSourceFromType(BatchTypeEnum.HZHX.getCode());
//            contractVO.setFinancialContractStatusUpdateTime(CommonDateUtils.parseLocalDateTimeToDate(v.getAccountDate()));
//            contractVOList.add(contractVO);
//        });
//        iContractService.verificationSaveRecordList(contractVOList);
//        //更新合同的财务合同时间 按照财务合同状态分组
//        Map<String,List<VerificationDetailsEntity>> detailsEntityMap = newDetailList.stream().collect(Collectors.groupingBy(v->v.getFinancialContractStatus()));
//        for (Map.Entry<String, List<VerificationDetailsEntity>> entry : detailsEntityMap.entrySet()) {
//            LocalDateTime localDateTime = entry.getValue().get(0).getAccountDate();
//            iContractService.lambdaUpdate().set(ContractEntity::getFinancialContractStatus,entry.getKey())
//                    .set(ContractEntity::getFinancialContractStatusUpdateTime,localDateTime)
//                    .in(ContractEntity::getContractCode,entry.getValue().stream().map(VerificationDetailsEntity::getContractCode).filter(StringUtils::isNotEmpty).collect(Collectors.toList()))
//                    .eq(ContractEntity::getOrgId,entry.getValue().get(0).getOrgId()).update();
//        }
//        // 是否更新合同月表合同财务状态
//        if(ContractStatusUpdateUtils.isUpdateContractStatusEnable(remoteDictService, "HX")) {
//            for (Map.Entry<String, List<VerificationDetailsEntity>> entry : detailsEntityMap.entrySet()) {
//                LocalDateTime localDateTime = entry.getValue().get(0).getAccountDate();
//                iContractMonthService.lambdaUpdate().set(ContractMonthEntity::getFinancialContractStatus,entry.getKey())
//                        .set(ContractMonthEntity::getFinancialContractStatusUpdateTime,localDateTime)
//                        .in(ContractMonthEntity::getContractCode,entry.getValue().stream().map(VerificationDetailsEntity::getContractCode).filter(StringUtils::isNotEmpty).collect(Collectors.toList()))
//                        .eq(ContractMonthEntity::getOrgId,entry.getValue().get(0).getOrgId()).update();
//            }
//        }
    }

    @Override
    public Map<String, String> exportSummary(VerificationPaybackQueryDTO queryDTO) {
        //查询所有的汇总数再单条查明细
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fileName = "核销回款汇总_" + LocalDateTimeUtil.format(LocalDateTimeUtil.now(), "yyyyMMddHHmmss")+".xlsx";

        String filePath =getFilePath();
        FileRecordEntity record = new FileRecordEntity();
        record.setModuleName(ModuleEnum.VERIFICATION_SUMMARY.getCode());
        record.setBusinessScene(BusinessSceneEnum.VERIFICATION_SUMMARY.getCode());
        record.setFileLocation(filePath+fileName);
        record.setFileName(fileName);
        record.setExecuteStatus(CheckExecuteStatusEnum.INPROGRESS.getCode());
        record.setFileUploadBy(String.valueOf(SecurityUtils.getUserId()));
        fileRecordService.save(record);

        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            // 异步执行的任务
            queryAndWriteSpecialTable(queryDTO, fileName);
        }, asyncTaskExecutor).thenRun(() -> {
            FileRecordEntity tmp = new FileRecordEntity();
            tmp.setId(record.getId());
            tmp.setExecuteStatus(CheckExecuteStatusEnum.FINISH.getCode());
            tmp.setFileUploadTime(LocalDateTime.now());

            fileRecordService.updateById(tmp);
        });

        Map<String, String> map = Maps.newLinkedHashMap();
        map.put("fileName", fileName);
        map.put("location", filePath+fileName);
        map.put("servicePath", servicePath);
        return map;
    }

    private String getFilePath(){
        String osName = System.getProperties().getProperty("os.name");
        if(osName.toLowerCase().contains("windows")){
            return basicPathWindows+"verification"+ File.separator;
        }else if(osName.toLowerCase().contains("linux") || osName.toLowerCase().contains("unix")){
            return basicPathLinux+"verification"+File.separator;
        }
        return com.utfinancing.financehub.common.core.utils.StringUtils.EMPTY;
    }

    public void queryAndWriteSpecialTable(VerificationPaybackQueryDTO queryDTO, String fileName) {
        Map<String,String> orgIdAndNameMap = getOrgIdOrgName();
        List<VerificationPaybackDetailsVO> list = Lists.newArrayList();
        //查询所有的汇总页数据
        Integer startPeriodCode = Integer.parseInt(queryDTO.getStartBusinessDate().replace("-", ""));
        Integer endPeriodCode = Integer.parseInt(queryDTO.getEndBusinessDate().replace("-", ""));
        queryDTO.setStartPeriodCode(startPeriodCode);
        queryDTO.setEndPeriodCode(endPeriodCode);
        List<VerificationPaybackVO> paybackVOList = interfaceDataService.selectPaybackList(queryDTO);
        for (VerificationPaybackVO v : paybackVOList) {
            VerificationPaybackQueryDTO queryDetail = new VerificationPaybackQueryDTO();
            queryDetail.setOrgId(v.getOrgId());
            queryDetail.setBusinessDate(v.getBusinessDate());
            List<VerificationPaybackDetailsVO> detailsVOList = selectPaybackDetailsList(queryDetail);
            if (CollectionUtils.isNotEmpty(detailsVOList)) {
                detailsVOList.forEach(s -> {
                    if (orgIdAndNameMap.containsKey(v.getOrgId())) {
                        s.setOrgName(orgIdAndNameMap.get(v.getOrgId()));
                    }
                    if (YesOrNoEnum.YES.getCode().equals(s.getIsAbnormal())) {
                        s.setIsAbnormal(YesOrNoEnum.YES.getDesc());
                    } else {
                        s.setIsAbnormal(YesOrNoEnum.NO.getDesc());
                    }
                });
                list.addAll(detailsVOList);
            }
        }
        long l2 = System.currentTimeMillis();
        log.info("导出Excel数据 开始");
        ExcelWriter writer = cn.hutool.poi.excel.ExcelUtil.getWriter(getFilePath()+fileName);
        writer.addHeaderAlias("orgName", "签约主体名称");
        writer.addHeaderAlias("financialContractStatus", "财务合同状态");
        writer.addHeaderAlias("clientCode", "客户编码");
        writer.addHeaderAlias("clientName", "客户名称");
        writer.addHeaderAlias("contractCode", "合同编号");
        writer.addHeaderAlias("businessDate", "回款月份");
        writer.addHeaderAlias("receivableLeaseAmount", "应收租金");
        writer.addHeaderAlias("receivableLastAmount", "应收期末残值");
        writer.addHeaderAlias("receivableLastAmountd", "应收销项税");
        writer.addHeaderAlias("receivableFirstAmount", "应收首付款");
        writer.addHeaderAlias("receivableInsuranceAmount", "应收保险费");
        writer.addHeaderAlias("receivableProcedureAmount", "应收手续费");
        writer.addHeaderAlias("receiveOtherRevenues", "应收其他收入");
        writer.addHeaderAlias("receiveOtherRevenuesd", "未实现融资租赁收益");
        writer.addHeaderAlias("recycleDefaultInterestAmount", "应收罚息");
        writer.addHeaderAlias("receiveTerminateProcedureAmount", "应收变更手续费");
        writer.addHeaderAlias("receivableMarginAmount", "承租人保证金");
        writer.addHeaderAlias("leaseRevenueAmount", "确认收入金额");
        writer.addHeaderAlias("receivableOuttaxAmount", "计提税金");
        writer.addHeaderAlias("depreciationLossAmount", "拨备转回金额");
        writer.addHeaderAlias("depreciationReservesAmount", "回款前核销余额");
        writer.addHeaderAlias("depreciationAmount", "回款后核销余额");
        writer.addHeaderAlias("isAbnormal", "是否异常");
        writer.autoSizeColumnAll();
        writer.setOnlyAlias(true);
        writer.setColumnWidth(-1,20);
        writer.write(list, true);
        writer.close();
        long l3 = System.currentTimeMillis();
        log.info("导出Excel数据 结束，用时{} s", (l3-l2)/1000);
    }

    public List<VerificationPaybackDetailsVO> selectPaybackDetailsList(VerificationPaybackQueryDTO queryDTO) {
        queryDTO.setPeriodCode(Integer.parseInt(queryDTO.getBusinessDate().replace("-","")));
        queryDTO.setLastPeriodCode(PeriodCodeUtil.getLastMonthPeriodCode(queryDTO.getPeriodCode()));
        List<VerificationPaybackDetailsVO> interfaceDataEntityList = interfaceDataService.selectPaybackDetailsList(queryDTO);
        interfaceDataEntityList.forEach(v -> {
            //缺省处理，chargeoff汇总表没有从核销表取值
            if (v.getDepreciationReservesAmount()==null){
                BigDecimal maxDepreciationReserves = verificationMapper.getMaxDepreciationReservesByPeriodCode(v.getContractCode(), v.getOrgId(), queryDTO.getPeriodCode());
                v.setDepreciationReservesAmount(maxDepreciationReserves);
                v.setDepreciationAmount(Optional.ofNullable(v.getDepreciationReservesAmount()).orElse(BigDecimal.ZERO).subtract(Optional.ofNullable(v.getDepreciationLossAmount()).orElse(BigDecimal.ZERO)));
            }
            v.setIsAbnormal("0");
            v.setDepreciationAmount(null == v.getDepreciationAmount() ? BigDecimal.ZERO : v.getDepreciationAmount());
            if (v.getDepreciationAmount().compareTo(BigDecimal.ZERO) !=0) {
                v.setIsAbnormal("1");
            }
        });
        return interfaceDataEntityList;
    }
}

