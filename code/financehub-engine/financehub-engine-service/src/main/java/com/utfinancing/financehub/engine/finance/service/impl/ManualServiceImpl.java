package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.utfinancing.financehub.admin.api.RemoteDictService;
import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.common.core.constant.CacheConstants;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.common.redis.service.RedisService;
import com.utfinancing.financehub.engine.approve.model.dto.ApproveDTO;
import com.utfinancing.financehub.engine.approve.service.IApproveService;
import com.utfinancing.financehub.engine.constants.Constants;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.ManualVO;
import com.utfinancing.financehub.engine.finance.mapper.ManualMapper;
import com.utfinancing.financehub.engine.finance.model.vo.ManualVoucherExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.ManualVoucherVO;
import com.utfinancing.financehub.engine.finance.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import com.utfinancing.financehub.engine.rule.entity.InterfaceDataEntity;
import com.utfinancing.financehub.engine.rule.service.IInterfaceDataService;
import com.utfinancing.financehub.engine.rule.service.impl.LocalSegmentVoucherGenerator;
import com.utfinancing.financehub.engine.scene.entity.AccountEntity;
import com.utfinancing.financehub.engine.scene.entity.BusinessEntity;
import com.utfinancing.financehub.engine.scene.entity.SceneEntity;
import com.utfinancing.financehub.engine.scene.service.IAccountService;
import com.utfinancing.financehub.engine.scene.service.IBusinessService;
import com.utfinancing.financehub.engine.scene.service.ISceneService;
import com.utfinancing.financehub.engine.utils.CommonDateUtils;
import com.utfinancing.financehub.engine.utils.PeriodCodeUtil;
import com.utfinancing.financehub.engine.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @Author : bruyang
 * @Date : Create in 2024-01-10
 * @Description :  Manual服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class ManualServiceImpl extends ServiceImpl<ManualMapper, ManualEntity> implements IManualService {

    private final ManualMapper manualMapper;
    @Resource
    private final IApproveService iApproveService;
    @Resource
    private final IContractService iContractService;
    @Resource
    private final IOrgCompanyService iOrgCompanyService;
    @Resource
    private final IAccountService iAccountService;
    @Resource
    private final IManualVoucherService iManualVoucherService;
    @Resource
    private final ISceneService iSceneService;
    @Resource
    private final ICurrencyService iCurrencyService;
    @Resource
    private final IClientService iClientService;
    @Resource
    private final IVoucherService iVoucherService;
    @Resource
    private final IContractBalanceService iContractBalanceService;

    public static final String MANUAL_VOUCHER = "manualVoucher";
    public static final String NON_CONFIRM_COLLECTION_SECOND_DETAIL = "nonConfirmCollectionSecondDetail";

    //过期时间：1个月
    private static final long REDIS_VOUCHER_NUM_EXPIRE = 3600L * 24 * 30;

    private final RedisService redisService;

    @Resource
    private final IBusinessService iBusinessService;

    @Value("${approve.url.manual-url:null}")
    private String approveUrl;

    @Resource
    private IBankAccountService iBankAccountService;

    @Resource
    private IFundSystemBalanceService iFundSystemBalanceService;

    @Resource
    private IInvoiceClaimService invoiceClaimService;

    @Resource
    private IContractBalanceLatestService iContractBalanceLatestService;

    @Resource
    private RemoteDictService remoteDictService;

    @Resource
    private IInterfaceDataService iInterfaceDataService;

    // 注入新的本地号段生成器
    @Autowired
    private LocalSegmentVoucherGenerator localSegmentVoucherGenerator;

    @Override
    public Long saveManual(ManualDTO dto) {
        //校验
        if (ObjectUtils.isNotNull(dto.getVoucherDate())) {
            dto.setPeriodCode(PeriodCodeUtil.periodCodeByDate(dto.getVoucherDate()));
        }
        checkSaveData(dto);
        ManualEntity entity = BeanUtil.copyProperties(dto, ManualEntity.class);
        entity.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
        entity.setVoucherNum(generateVoucherNum(entity.getVoucherType(),entity.getVoucherDate()));
        entity.setPeriodCode(PeriodCodeUtil.periodCodeByLocalDateTime(entity.getVoucherDate()));
        this.save(entity);
        //保存详情信息
        List<ManualVoucherEntity> voucherEntityList = BeanUtil.copyToList(dto.getManualVoucherDTOList(), ManualVoucherEntity.class);
        voucherEntityList.stream().forEach(v -> {
            v.setManualId(entity.getId());
            v.setPreparerName(UserUtils.getStaffName());
        });
        iManualVoucherService.saveBatch(voucherEntityList);
        //数据来源-开票认领
        if (BatchTypeEnum.KJFP.getCode().equals(dto.getSourceFrom())) {
            InvoiceClaimEntity invoiceClaimEntity = invoiceClaimService.getById(dto.getExternalId());
            invoiceClaimEntity.setManualId(entity.getId());
            invoiceClaimEntity.setProcessStatus("1");
            invoiceClaimService.updateById(invoiceClaimEntity);
        }
        return entity.getId();
    }

    @Override
    public Long updateManual(Long id, ManualDTO dto) {
        checkSaveData(dto);
        ManualEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        //保存详情信息
        List<ManualVoucherEntity> voucherEntityList = BeanUtil.copyToList(dto.getManualVoucherDTOList(), ManualVoucherEntity.class);
        voucherEntityList.forEach(v -> {
            v.setManualId(entity.getId());
            v.setCreditAmount(null==v.getCreditAmount()?BigDecimal.ZERO:v.getCreditAmount());
            v.setDebitAmount(null==v.getDebitAmount()?BigDecimal.ZERO:v.getDebitAmount());
            v.setVoucherSummary(StringUtils.isEmpty(v.getVoucherSummary())?"":v.getVoucherSummary());
            v.setIsRelatedOtherCustomer(StringUtils.isEmpty(v.getIsRelatedOtherCustomer())?YesOrNoEnum.NO.getCode():v.getIsRelatedOtherCustomer());
            v.setAccountCode(StringUtils.isEmpty(v.getAccountCode())?"":v.getAccountCode());
            v.setAccountName(StringUtils.isEmpty(v.getAccountName())?"":v.getAccountName());
            if (StringUtils.isEmpty(v.getClientCode())) {
               v.setClientCode("");
               v.setClientName("");
            }
            if (StringUtils.isEmpty(v.getLoansContractCode())) {
                v.setLoansContractCode("");
                v.setLoansContractCodeName("");
            }
            if (StringUtils.isEmpty(v.getBankNo())) {
                v.setBankNo("");
                v.setBankNoName("");
            }
        });
        iManualVoucherService.updateBatchById(voucherEntityList);
        return id;
    }

    @Override
    public ManualDTO getManualDTOById(Long id) {
        ManualEntity entity = this.getById(id);
        if (entity == null) return null;
        //获取手工凭证详情信息
        List<ManualVoucherEntity> manualVoucherEntityList = iManualVoucherService.lambdaQuery().eq(ManualVoucherEntity::getManualId,id).list();
        ManualDTO manualDTO = BeanUtil.copyProperties(entity, ManualDTO.class);
        List<ManualVoucherExcelVO> manualVoucherExcelVOList = BeanUtil.copyToList(manualVoucherEntityList,ManualVoucherExcelVO.class);
        manualDTO.setManualVoucherDTOList(manualVoucherExcelVOList);
        return manualDTO;
    }

    @Override
    public IPage<ManualVO> selectPage(ManualQueryDTO queryDTO) {
        LambdaQueryWrapper<ManualEntity> queryWrapper = Wrappers.<ManualEntity>lambdaQuery();
        //这里注入查询条件
        if (StringUtils.isNotEmpty(queryDTO.getOrgId())) {
            queryWrapper.eq(ManualEntity::getOrgId,queryDTO.getOrgId());
        }
        if (ObjectUtils.isNotNull(queryDTO.getStartVoucherDate())) {
            queryWrapper.ge(ManualEntity::getVoucherDate, queryDTO.getStartVoucherDate());
        }
        if (ObjectUtils.isNotNull(queryDTO.getEndVoucherDate())) {
            queryWrapper.lt(ManualEntity::getVoucherDate, DateUtil.offsetDay(queryDTO.getEndVoucherDate(), 1));
        }
        if (ObjectUtils.isNotNull(queryDTO.getStartBusinessDate())) {
            queryWrapper.ge(ManualEntity::getBusinessDate, queryDTO.getStartBusinessDate());
        }
        if (ObjectUtils.isNotNull(queryDTO.getEndBusinessDate())) {
            queryWrapper.lt(ManualEntity::getBusinessDate, DateUtil.offsetDay(queryDTO.getEndBusinessDate(), 1));
        }
        if (ObjectUtils.isNotNull(queryDTO.getPeriodCode())) {
            queryWrapper.eq(ManualEntity::getPeriodCode, queryDTO.getPeriodCode());
        }
        if (StringUtils.isNotEmpty(queryDTO.getCurrencyCode())) {
            queryWrapper.eq(ManualEntity::getCurrencyCode, queryDTO.getCurrencyCode());
        }
        if (StringUtils.isNotEmpty(queryDTO.getBusinessCode())) {
            queryWrapper.like(ManualEntity::getBusinessCode,queryDTO.getBusinessCode());
        }
        if (StringUtils.isNotEmpty(queryDTO.getBusinessName())) {
            queryWrapper.like(ManualEntity::getBusinessName,queryDTO.getBusinessName());
        }
        if (StringUtils.isNotEmpty(queryDTO.getVoucherType())) {
            queryWrapper.eq(ManualEntity::getVoucherType,queryDTO.getVoucherType());
        }
        if (ObjectUtils.isNotNull(queryDTO.getVoucherNum())) {
            queryWrapper.eq(ManualEntity::getVoucherNum,queryDTO.getVoucherNum());
        }
        if (ObjectUtils.isNotNull(queryDTO.getId())) {
            queryWrapper.eq(ManualEntity::getId,queryDTO.getId());
        }
        if (StringUtils.isNotEmpty(queryDTO.getSceneName())) {
            queryWrapper.eq(ManualEntity::getSceneName,queryDTO.getSceneName());
        }
        if (StringUtils.isNotEmpty(queryDTO.getSceneCode())) {
            queryWrapper.eq(ManualEntity::getSceneCode,queryDTO.getSceneCode());
        }
        if (StringUtils.isNotEmpty(queryDTO.getSubSceneType())) {
            queryWrapper.eq(ManualEntity::getSubSceneType,queryDTO.getSubSceneType());
        }
        if (StringUtils.isNotEmpty(queryDTO.getProcessStatus())) {
            queryWrapper.eq(ManualEntity::getProcessStatus,queryDTO.getProcessStatus());
        }
        if (CollectionUtils.isNotEmpty(queryDTO.getProcessStatusList())) {
            queryWrapper.in(ManualEntity::getProcessStatus,queryDTO.getProcessStatusList());
        }
        if (CollectionUtils.isNotEmpty(queryDTO.getOrgIdList())) {
            queryWrapper.in(ManualEntity::getOrgId,queryDTO.getOrgIdList());
        }
        if (CollectionUtils.isNotEmpty(queryDTO.getIdList())) {
            queryWrapper.in(ManualEntity::getId,queryDTO.getIdList());
        }
        queryWrapper.orderByDesc(ManualEntity::getCreateTime);
        //这里注入查询条件
        IPage<ManualEntity> entityIPage = manualMapper.selectPage(new Page<ManualEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, ManualVO.class);
    }

    @Override
    public Boolean withdraw(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请至少选择一条数据撤回");
        }
        List<ManualEntity> manualEntityList = this.listByIds(idList);
        manualEntityList.forEach(v -> {
            if (!ProcessStatusEnum.SUBMITTED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("只有已提交状态才可以撤回");
            }
            v.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
        });
        iApproveService.withdraw(manualEntityList.stream().map(ManualEntity::getProcessInstanceId).collect(Collectors.toList()));
        //撤回的时候需要将金额都改为负数
//        manualEntityList.forEach(v -> {
//            generateVoucher(v.getId(),UserUtils.getStaffName(),UserUtils.getStaffName(),Boolean.TRUE);
//        });
        //iVoucherService.updateStatusByBatch(Lists.newArrayList(idList),BatchTypeEnum.SGPZ.getCode(),ProcessStatusEnum.ENTERED.getCode(),"","");
        iVoucherService.withdrawOnlyForManualVoucher(Lists.newArrayList(idList),BatchTypeEnum.SGPZ.getCode(),ProcessStatusEnum.ENTERED.getCode(),"","");
        //撤回的时候需要将手工凭证删除，不然凭证查询界面会有重复的数据
        //iVoucherService.deleteByBatchIdList(Lists.newArrayList(idList),BatchTypeEnum.SGPZ.getCode());
        return this.updateBatchById(manualEntityList);
    }

    @Override
    public Boolean submit(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请至少选择一条数据撤回");
        }
        List<ApproveDTO> approveDTOList = Lists.newArrayList();
        List<ManualEntity> manualEntityList = this.listByIds(idList);
        manualEntityList.forEach(v -> {
            if (!(ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus())
                    || ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus()))) {
                throw new ServiceException("处理状态为已录入/已拒绝的才可以提交");
            }
            v.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
            ApproveDTO approveDTO = new ApproveDTO();
            approveDTO.setDocumentId(v.getId());
            approveDTO.setDocumentType(BatchTypeEnum.SGPZ.getCode());
            approveDTO.setUrl(approveUrl+v.getId());
            approveDTOList.add(approveDTO);
        });
        Map<Long,Long> processInstantIdMap = iApproveService.submit(approveDTOList);
        manualEntityList.forEach(v -> {
            if (null!=processInstantIdMap && processInstantIdMap.containsKey(v.getId())) {
                v.setProcessInstanceId(processInstantIdMap.get(v.getId()));
            }
            generateVoucher(v.getId(),v.getCreateUserName(),v.getCreateBy(),Boolean.FALSE,MANUAL_VOUCHER);
        });
        this.updateBatchById(manualEntityList);
        return Boolean.TRUE;
    }

    @Override
    public Boolean updateStatus(CommonApproveDTO approveDTO) {
        ManualEntity manualEntity = this.getById(approveDTO.getDocumentId());
        if (null == manualEntity) {
            throw new ServiceException("手工凭证数据不存在");
        }
        try {
            String processStatus = manualEntity.getProcessStatus();
            if (ProcessStatusEnum.REVIEWED.getCode().equals(approveDTO.getDocumentStatus())) {
                processStatus = ProcessStatusEnum.REVIEWED.getCode();
                //复核通过之后生成凭证
                //generateVoucher(manualEntity.getId(),approveDTO.getApproverName(),approveDTO.getApproverNum(),Boolean.FALSE);
                //复核通过之后 更新状态为已复核，复核人
            } else if (ProcessStatusEnum.REJECTED.getCode().equals(approveDTO.getDocumentStatus())) {
                processStatus = ProcessStatusEnum.REJECTED.getCode();
            }
            iVoucherService.updateStatusByBatch(Lists.newArrayList(manualEntity.getId()),BatchTypeEnum.SGPZ.getCode(),processStatus,approveDTO.getApproverNum(),approveDTO.getApproverName());
            manualEntity.setRecheckUserNo(approveDTO.getApproverNum());
            manualEntity.setRecheckUserName(approveDTO.getApproverName());
            manualEntity.setProcessStatus(processStatus);
            manualEntity.setErrorInfo("");
        } catch (Exception e) {
            log.info("手工凭证复核报错，报错原因：{}",e.getMessage());
            String errorInfo = e.getMessage();
            if (StringUtils.isNotEmpty(e.getMessage()) && e.getMessage().length()>1000) {
                errorInfo = e.getMessage().substring(0,1000);
            }
            if (StringUtils.isEmpty(e.getMessage())) {
                errorInfo = "复核失败";
            }
            manualEntity.setErrorInfo(errorInfo);
        }
        return this.updateById(manualEntity);
    }

    @Override
    public String importTemplate(MultipartFile file) {
        String resultString = "";
        ExcelUtil<ManualVoucherExcelVO> util = new ExcelUtil<ManualVoucherExcelVO>(ManualVoucherExcelVO.class);
        try {
            List<ManualVoucherExcelVO> manualVoucherExcelVOList = util.importExcel(file.getInputStream());
            //转换签约主体为编码
            Map<String,String> orgIdMap = Maps.newHashMap();
            List<String> orgIdList = manualVoucherExcelVOList.stream().map(ManualVoucherExcelVO::getOrgId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(orgIdList)) {
                orgIdMap = iOrgCompanyService.lambdaQuery().in(OrgCompanyEntity::getOrgName,orgIdList).list().stream().collect(HashMap::new,(h,v)->h.put(v.getOrgName(),v.getOrgId()),HashMap::putAll);
            }
            Map<String, String> finalOrgIdMap = orgIdMap;
            //根据场景名称获取场景编码
            Map<String,String> sceneMap = getAllSceneMap();
            //根据获取币种名称获取币种编码
            Map<String,String> currencyMap = getAllCurrency();
            //获取voucherType
            List<SysDictData> sysDictDataList = remoteDictService.listDictData(DictTypeEnum.SYS_VOUCHER_TYPE.getCode()).getData();
            Map<String,String> voucherTypeMap = Maps.newHashMap();
            if (CollectionUtils.isNotEmpty(sysDictDataList)) {
                voucherTypeMap =  sysDictDataList.stream().collect(HashMap::new,(map,item)->map.put(item.getDictLabel(),item.getDictValue()),HashMap::putAll);
            }

            // 细分场景编码
            List<SysDictData> subSceneDictDataList = remoteDictService.listDictData(DictTypeEnum.SYS_SUB_SCENE_TYPE.getCode()).getData();
            Map<String,String> subSceneTypeMap = Maps.newHashMap();
            if (CollectionUtils.isNotEmpty(subSceneDictDataList)) {
                subSceneTypeMap =  subSceneDictDataList.stream().collect(HashMap::new,(map,item)->map.put(item.getDictLabel(),item.getDictValue()),HashMap::putAll);
            }

            Map<String, String> finalVoucherTypeMap = voucherTypeMap;
            //导入的银行编码转为银行账号
            Map<String,List<BankAccountEntity>> bankAccountMap = iBankAccountService.list().stream().collect(Collectors.groupingBy(v->v.getOrgId()+"-"+v.getAccountCode()+"-"+v.getBankAccountCode()));
            Map<String, String> finalSubSceneTypeMap = subSceneTypeMap;
            manualVoucherExcelVOList.stream().forEach(v -> {
                if (finalOrgIdMap.containsKey(v.getOrgId())) {
                    v.setOrgId(finalOrgIdMap.get(v.getOrgId()));
                }
                String key = v.getOrgId()+"-"+v.getAccountCode()+"-"+v.getBankNo();
                if (sceneMap.containsKey(v.getSceneName())) {
                    v.setSceneCode(sceneMap.get(v.getSceneName()));
                }
                if (currencyMap.containsKey(v.getCurrencyCode())) {
                    v.setCurrencyCode(currencyMap.get(v.getCurrencyCode()));
                }
                if (ObjectUtils.isNotNull(v.getVoucherDate())) {
                    v.setPeriodCode(PeriodCodeUtil.periodCodeByDate(v.getVoucherDate()));
                }
                if (finalVoucherTypeMap.containsKey(v.getVoucherType())) {
                    v.setVoucherType(finalVoucherTypeMap.get(v.getVoucherType()));
                }
                if (YesOrNoEnum.YES.getDesc().equals(v.getIsRelatedOtherCustomer())
                    || YesOrNoEnum.YES.getCode().equals(v.getIsRelatedOtherCustomer())) {
                    v.setIsRelatedOtherCustomer(YesOrNoEnum.YES.getCode());
                } else {
                    v.setIsRelatedOtherCustomer(YesOrNoEnum.NO.getCode());
                }
                if (bankAccountMap.containsKey(key)) {
                    v.setBankNo(bankAccountMap.get(key).get(0).getBankAccountNumber());
                }
                if (finalSubSceneTypeMap.containsKey(v.getSubSceneType())) {
                    v.setSubSceneType(finalSubSceneTypeMap.get(v.getSubSceneType()));
                }
            });
            resultString = checkData(manualVoucherExcelVOList);
            //按照签约主体+会计期间+业务日期+记账日期+币种分组+凭证类型+场景编码+细分场景+汇率+业务编码+批次号 按照index 升序排序
            Map<String,List<ManualVoucherExcelVO>> voucherEntityMap = manualVoucherExcelVOList.stream().sorted(Comparator.comparing(ManualVoucherExcelVO::getIndex)).collect(Collectors.groupingBy(v->v.getOrgId()+"_"+v.getPeriodCode()+"_"+v.getBusinessDate()
                    +"_"+v.getVoucherDate()+"_"+v.getCurrencyCode() + "_"+v.getVoucherType()
                    +"_"+v.getSceneCode()+"_"+v.getSubSceneType()
                    +"_"+v.getRate()
                    +"_"+v.getBusinessCode()
                    +"_"+v.getBatchId()));
//            if (voucherEntityMap.size()>1) {
//                throw new ServiceException("签约主体+会计期间+业务日期+记账日期+币种+凭证类型+摘要内容+场景编码+细分场景+附件数量+汇率+业务编码存在不一致的情况，不可以导入");
//            }
            for(Map.Entry<String, List<ManualVoucherExcelVO>> entry : voucherEntityMap.entrySet()) {
                List<ManualVoucherExcelVO> manualVoucherExcelList = entry.getValue();
                ManualVoucherExcelVO voucherExcelEntity = manualVoucherExcelList.get(0);
                ManualEntity manualEntity = BeanUtil.copyProperties(voucherExcelEntity,ManualEntity.class);
                manualEntity.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
                //校验借方发生额和贷方发生额一致
                final BigDecimal[] debitAmount = {BigDecimal.ZERO};
                final BigDecimal[] creditAmount = {BigDecimal.ZERO};
                manualVoucherExcelList.stream().forEach(e -> {
                    debitAmount[0] = debitAmount[0].add(null == e.getDebitAmount() ? BigDecimal.ZERO : e.getDebitAmount());
                    creditAmount[0] = creditAmount[0].add(null == e.getCreditAmount() ? BigDecimal.ZERO : e.getCreditAmount());
                });
                if (debitAmount[0].compareTo(creditAmount[0])!=0) {
                    throw new ServiceException("借方发生额和贷方发生额不一致，不能导入");
                }
                //生成凭证号
               manualEntity.setVoucherNum(generateVoucherNum(manualEntity.getVoucherType(),manualEntity.getVoucherDate()));
               manualEntity.setPeriodCode(PeriodCodeUtil.periodCodeByLocalDateTime(manualEntity.getVoucherDate()));
               manualEntity.setCreateUserName(UserUtils.getStaffName());
               save(manualEntity);
               //保存详情
               List<ManualVoucherEntity> manualVoucherList = BeanUtil.copyToList(manualVoucherExcelList, ManualVoucherEntity.class);
               manualVoucherList.stream().forEach(s -> {
                   s.setManualId(manualEntity.getId());
                   s.setPreparerName(UserUtils.getStaffName());
                   //辅助帐摘要内容为空则取摘要内容
                   if (StringUtils.isEmpty(s.getSubsidiaryAccount())) {
                      s.setSubsidiaryAccount(s.getVoucherSummary());
                   }
               });
               iManualVoucherService.saveBatch(manualVoucherList);
            }
        } catch (Exception exception) {
            throw new ServiceException("导入失败,失败原因："+exception.getMessage());
        }
        return resultString;
    }

    @Override
    public Boolean deleteByIds(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请至少选择一条数据删除");
        }
        List<ManualEntity> manualEntityList = this.listByIds(idList);
        manualEntityList.forEach(v -> {
            if (!(ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus())
                    || ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus()))) {
                throw new ServiceException("处理状态为已录入/已拒绝的才可以删除");
            }
        });
        //删除详情信息
        List<Long> voucherIdList = iManualVoucherService.lambdaQuery().in(ManualVoucherEntity::getManualId, idList).list().stream().map(ManualVoucherEntity::getId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(voucherIdList)) {
            iManualVoucherService.removeByIds(voucherIdList);
        }
        //按照数据来源分组
        Map<String,List<ManualEntity>> externalListMap =manualEntityList.stream().filter(v-> StringUtils.isNotEmpty(v.getSourceFrom())).collect(Collectors.toList()).stream().collect(Collectors.groupingBy(ManualEntity::getSourceFrom));
        if (!externalListMap.isEmpty()) {
            for (Map.Entry<String, List<ManualEntity>> entry : externalListMap.entrySet()) {
                List<Long> sourceFromIdList = entry.getValue().stream().map(ManualEntity::getSourceId).collect(Collectors.toList());
                if (ManualSourceFrom.KJFP.getCode().equals(entry.getKey())) {
                    List<Long> manualIdList = entry.getValue().stream().map(ManualEntity::getId).collect(Collectors.toList());
                    LambdaUpdateChainWrapper<InvoiceClaimEntity> invoiceEntityUpdateWrapper =  invoiceClaimService.lambdaUpdate().set(InvoiceClaimEntity::getProcessStatus, "0").in(InvoiceClaimEntity::getManualId, manualIdList);
                    invoiceEntityUpdateWrapper.update();
                } else if (ManualSourceFrom.PZ.getCode().equals(entry.getKey())) {
                    iVoucherService.lambdaUpdate().set(VoucherEntity::getIsWriteOff,YesOrNoEnum.NO.getCode()).in(VoucherEntity::getId,sourceFromIdList).update();
                } else if (ManualSourceFrom.SGPZ.getCode().equals(entry.getKey())) {
                    this.lambdaUpdate().set(ManualEntity::getIsWriteOff,YesOrNoEnum.NO.getCode()).in(ManualEntity::getId,sourceFromIdList).update();
                }
            }
        }
        return this.removeByIds(idList);
    }

    @Override
    public List<ManualVoucherVO> getManualVoucherById(Long id) {
        List<ManualVoucherEntity> voucherEntityList = iManualVoucherService.lambdaQuery().eq(ManualVoucherEntity::getManualId, id).list();
        return BeanUtil.copyToList(voucherEntityList, ManualVoucherVO.class);
    }

    public String checkData(List<ManualVoucherExcelVO> manualVoucherExcelVOList) {
        //校验业务场景
        List<String> sceneList = manualVoucherExcelVOList.stream().map(ManualVoucherExcelVO::getSceneCode).collect(Collectors.toList());
        Map<String,String> sysSceneMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(sceneList)) {
            List<SceneEntity> sceneEntityList = iSceneService.lambdaQuery().in(SceneEntity::getSceneCode, sceneList).list();
            if (CollectionUtils.isNotEmpty(sceneEntityList)) {
                sysSceneMap = sceneEntityList.stream().collect(HashMap::new,(h,v)->h.put(v.getSceneCode(), v.getSceneName()),HashMap::putAll);
            }
        }
        //校验币种
        List<String> currencyList = manualVoucherExcelVOList.stream().map(ManualVoucherExcelVO::getCurrencyCode).collect(Collectors.toList());
        List<String> sysCurrencyList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(currencyList)) {
            List<CurrencyEntity> currencyEntityList = iCurrencyService.lambdaQuery().in(CurrencyEntity::getCurrencyCode, currencyList).list();
            if (CollectionUtils.isNotEmpty(currencyEntityList)) {
                sysCurrencyList = currencyEntityList.stream().map(CurrencyEntity::getCurrencyCode).collect(Collectors.toList());
            }
        }
        List<String> contractCodeList = manualVoucherExcelVOList.stream().map(ManualVoucherExcelVO::getContractCode).collect(Collectors.toList());
        List<String> orgIdList = manualVoucherExcelVOList.stream().map(ManualVoucherExcelVO::getOrgId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(orgIdList)) {
            throw new ServiceException("签约主体不可以为空");
        }
        Map<String,String> contractCodeMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(contractCodeList)) {
            List<ContractEntity> contractEntityList = iContractService.lambdaQuery().in(ContractEntity::getContractCode,contractCodeList).list();
            if (CollectionUtils.isNotEmpty(contractEntityList)) {
                contractCodeMap = contractEntityList.stream().collect(HashMap::new,(h,v)->h.put(v.getContractCode(),v.getContractName()),HashMap::putAll);
            }
        }
        Map<String,String> orgIdMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(orgIdList)) {
            orgIdMap = iOrgCompanyService.lambdaQuery().in(OrgCompanyEntity::getOrgId,orgIdList).list().stream().collect(HashMap::new,(h,v)->h.put(v.getOrgId(),v.getOrgName()),HashMap::putAll);
        }
        //判断科目是否存在
        List<String> accountCodeList = manualVoucherExcelVOList.stream().map(ManualVoucherExcelVO::getAccountCode).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(accountCodeList)) {
            throw new ServiceException("科目不可以为空");
        }
        Map<String, List<AccountEntity>> accountEntityMap = iAccountService.list().stream().collect(Collectors.groupingBy(v -> v.getBusinessCode()+"-"+v.getAccountCode()));
        //银行科目编码
        Map<String,List<BankAccountEntity>> bankAccountMap = iBankAccountService.list().stream().collect(Collectors.groupingBy(v->v.getOrgId()+"-"+v.getAccountCode()+"-"+v.getBankAccountNumber()));
        //初始化客户编码 如果客户编码没有填写，填写了客户类型则按照客户类型+合同编码去查最新余额表，多条数据取任意一个
        String clientTypeString = initClientCode(manualVoucherExcelVOList);
        //客户编码
        List<String> clientCodeList = manualVoucherExcelVOList.stream().map(ManualVoucherExcelVO::getClientCode).collect(Collectors.toList());
        Map<String,String> clientCodeMap= Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(clientCodeList)) {
            List<ClientEntity> clientEntityList = iClientService.lambdaQuery().in(ClientEntity::getClientCode,clientCodeList).list();
            if (CollectionUtils.isNotEmpty(clientEntityList)) {
                clientCodeMap = clientEntityList.stream().collect(HashMap::new,(h,v)->h.put(v.getClientCode(),v.getClientName()),HashMap::putAll);
            }
        }
        //业务编码
        Map<String,String> businessMap = new HashMap<>();
        List<String> businessCodes = manualVoucherExcelVOList.stream().map(ManualVoucherExcelVO::getBusinessCode).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(businessCodes)) {
           List<BusinessEntity> businessEntityList = iBusinessService.lambdaQuery().in(BusinessEntity::getBusinessCode,businessCodes).list();
           if (CollectionUtils.isNotEmpty(businessEntityList)) {
               businessMap = businessEntityList.stream().collect(HashMap::new,(h,v)->h.put(v.getBusinessCode(),v.getBusinessName()),HashMap::putAll);
           }
        }

        List<String> finalSysCurrencyList = sysCurrencyList;
        Map<String, String> finalSysSceneMap = sysSceneMap;
        Map<String, String> finalClientCodeMap = clientCodeMap;
        Map<String, String> finalContractCodeMap = contractCodeMap;
        Map<String, String> finalBusinessMap = businessMap;
        Map<String, String> finalOrgIdMap = orgIdMap;
        //获取业务编码，如果是资金系统的不需要校验维度
        String businessCode = manualVoucherExcelVOList.get(0).getBusinessCode();
        AtomicInteger i = new AtomicInteger(1);
        manualVoucherExcelVOList.stream().forEach(v -> {
            v.setIndex(i.get());
            if (StringUtils.isEmpty(v.getOrgId())) {
                throw new ServiceException("签约主体不可以为空");
            }
            if (ObjectUtils.isNull(v.getPeriodCode())) {
                throw new ServiceException("会计期间不可以为空");
            }
            if (ObjectUtils.isNull(v.getBusinessDate())) {
                throw new ServiceException("业务日期不可以为空");
            }
            if (ObjectUtils.isNull(v.getVoucherDate())) {
                throw new ServiceException("财务日期不可以为空");
            }
            if (StringUtils.isEmpty(v.getCurrencyCode())) {
                throw new ServiceException("币种不可以为空");
            }
            if (StringUtils.isEmpty(v.getAccountCode())) {
                throw new ServiceException("科目编码不可以为空");
            }
            if (StringUtils.isEmpty(v.getSceneCode())) {
                throw new ServiceException("业务场景不可以为空");
            }
            if (StringUtils.isEmpty(v.getBusinessCode())) {
                throw new ServiceException("业务编码不可以为空");
            }
            if (StringUtils.isEmpty(v.getBatchId())) {
                throw new ServiceException("凭证批次号不可以为空");
            }
            if (!finalOrgIdMap.containsKey(v.getOrgId())) {
                throw new ServiceException("签约主体在系统中不存在");
            }
            if (!finalSysSceneMap.containsKey(v.getSceneCode())) {
                throw new ServiceException("业务场景在系统中不存在");
            }
            if (!finalSysCurrencyList.contains(v.getCurrencyCode())) {
                throw new ServiceException("币种在系统中不存在");
            }
//            if (Constants.RECEIVABLE_UNCONFIRM_RECEIPT.equals(v.getAccountCode())) {
//               throw new ServiceException("请在未确认收款模块进行认领动作");
//            }
            String busAccountCode = v.getBusinessCode()+"-"+v.getAccountCode();
            String bankAccountCode = v.getOrgId()+"-"+v.getAccountCode()+"-"+v.getBankNo();
            if (!accountEntityMap.containsKey(v.getBusinessCode()+"-"+v.getAccountCode()) && !bankAccountMap.containsKey(bankAccountCode)) {
                throw new ServiceException("科目编码+业务编码或者科目编码+签约主体+银行账号在系统中不存在，不可以导入");
            }
            if (accountEntityMap.containsKey(v.getBusinessCode()+"-"+v.getAccountCode())) {
                //根据科目查询维度，维度确定合同编码和客户编码是否必填,如果分录设置了字段isRelatedOtherCustomer=1 则不需要校验客户是否需要必填
                AccountEntity accountEntity = accountEntityMap.get(busAccountCode).get(0);
                v.setAccountName(accountEntity.getAccountName());
                List<String> assistFlagList = null == accountEntity.getAssistFlags() ? Lists.newArrayList() : accountEntity.getAssistFlags();
                if (YesOrNoEnum.NO.getCode().equals(v.getIsRelatedOtherCustomer()) && assistFlagList.contains(AssistFlagEnum.CLIENT.getCode())) {
                    if (StringUtils.isEmpty(v.getClientCode())) {
                        throw new ServiceException("客户编码不可以为空");
                    }
                    if (!finalClientCodeMap.containsKey(v.getClientCode())) {
                        throw new ServiceException("客户编码在系统中不存在");
                    }
                }
                if (assistFlagList.contains(AssistFlagEnum.CONTRACT.getCode())){
                    if (StringUtils.isEmpty(v.getContractCode())) {
                        throw new ServiceException("合同编码不可以为空");
                    }
                    if (!finalContractCodeMap.containsKey(v.getContractCode())) {
                        throw new ServiceException("合同编码在系统中不存在");
                    }
                }
                if (assistFlagList.contains(AssistFlagEnum.BANK_NO.getCode())) {
                    if (StringUtils.isEmpty(v.getBankNo())) {
                        throw new ServiceException("银行账号不可以为空");
                    }
                    if (!bankAccountMap.containsKey(bankAccountCode)) {
                        throw new ServiceException("银行账号在系统中不存在");
                    }
                }
                if (assistFlagList.contains(AssistFlagEnum.BILL_CONTRACT.getCode())) {
                    if (StringUtils.isEmpty(v.getLoansContractCode())) {
                        throw new ServiceException("借款合同编号不可以为空");
                    }
                }
            }
            if (bankAccountMap.containsKey(bankAccountCode)) {
                v.setAccountName(bankAccountMap.get(bankAccountCode).get(0).getAccountName());
            }
            //一条记录只能是借方金额或贷方金额
            if ((ObjectUtils.isNotNull(v.getCreditAmount()) && v.getCreditAmount().compareTo(BigDecimal.ZERO)!=0)
                    && ObjectUtils.isNotNull(v.getDebitAmount()) && v.getDebitAmount().compareTo(BigDecimal.ZERO)!=0) {
                throw new ServiceException("一条记录贷方金额，借方金额只能二选一");
            }
            if (ObjectUtils.isNull(v.getCreditAmount()) && ObjectUtils.isNull(v.getDebitAmount()) ) {
                throw new ServiceException("一条记录贷方金额，借方金额必须有一个有值");
            }
            if (finalContractCodeMap.containsKey(v.getContractCode())) {
                v.setContractName(finalContractCodeMap.get(v.getContractCode()));
            }
            if (finalClientCodeMap.containsKey(v.getClientCode())) {
                v.setClientName(finalClientCodeMap.get(v.getClientCode()));
            }
            if (!finalBusinessMap.containsKey(v.getBusinessCode())) {
                throw new ServiceException("业务编码在系统中不存在");
            }
            v.setSceneName(finalSysSceneMap.get(v.getSceneCode()));
            v.setBusinessName(finalBusinessMap.get(v.getBusinessCode()));
            i.getAndIncrement();
        });
        //校验isRelatedOtherCustomer=1时借贷金额是否相同
//        List<ManualVoucherExcelVO> otherCustomerList =  manualVoucherExcelVOList.stream().filter(v ->YesOrNoEnum.YES.getCode().equals(v.getIsRelatedOtherCustomer())).collect(Collectors.toList());
//        checkDebitAndCreditAmount(otherCustomerList);
        return clientTypeString;
    }

    public void generateVoucher(Long id,String approveName,String approveNum,Boolean isNegativeFlag,String businessScence) {
        ManualEntity manualEntity = this.getById(id);
        //删除原凭证,手工凭证场景不需要先删
        if (!businessScence.equals(MANUAL_VOUCHER)){
            CompletableFuture.runAsync(()->{
                iVoucherService.deleteByBatchIdList(Lists.newArrayList(id),BatchTypeEnum.SGPZ.getCode());
            });
        }

        //是否是资金系统
        Boolean fundSystemFlag = Boolean.FALSE;
        String transactionType = "";
        if (SceneEnum.WYLSFK.getCode().equals(manualEntity.getSceneCode())|| SceneEnum.WYLSSK.getCode().equals(manualEntity.getSceneCode())) {
            fundSystemFlag = Boolean.TRUE;
            if (SceneEnum.WYLSSK.getCode().equals(manualEntity.getSceneCode())) {
                transactionType="collection";
            } else {
                transactionType="payment";
            }
        }
        //获取详情
        List<ManualVoucherEntity> voucherEntityList = iManualVoucherService.lambdaQuery().eq(ManualVoucherEntity::getManualId,id).list();
        VoucherSaveDTO voucherSaveDTO = new VoucherSaveDTO();
        voucherSaveDTO.setBatchId(manualEntity.getId());
        voucherSaveDTO.setBatchType(BatchTypeEnum.SGPZ.getCode());
        voucherSaveDTO.setBusinessCode(manualEntity.getBusinessCode());
        voucherSaveDTO.setBusinessName(manualEntity.getBusinessName());
        voucherSaveDTO.setCreateUserName(manualEntity.getCreateUserName());
        voucherSaveDTO.setCreateUserNo(manualEntity.getCreateBy());
        voucherSaveDTO.setCurrency(manualEntity.getCurrencyCode());
        voucherSaveDTO.setOrderId(manualEntity.getId().toString());
        voucherSaveDTO.setOrgId(manualEntity.getOrgId());
        voucherSaveDTO.setPeriodCode(manualEntity.getPeriodCode());
        voucherSaveDTO.setSceneCode(manualEntity.getSceneCode());
        voucherSaveDTO.setSceneName(manualEntity.getSceneName());
        voucherSaveDTO.setSource(SystemEnum.CWZT.getCode());
        voucherSaveDTO.setSubSceneType(manualEntity.getSubSceneType());
        voucherSaveDTO.setSystemCode(SystemEnum.CWZT.getCode());
        voucherSaveDTO.setSystemName(SystemEnum.CWZT.getDesc());
        voucherSaveDTO.setVoucherDate(manualEntity.getVoucherDate());
        voucherSaveDTO.setVoucherNum(manualEntity.getVoucherNum());
        voucherSaveDTO.setVoucherSummary(manualEntity.getVoucherSummary());
        voucherSaveDTO.setVoucherType(manualEntity.getVoucherType());
        voucherSaveDTO.setVoucherWay(VoucherWayEnum.MANUAL.getCode());
        voucherSaveDTO.setTransactionType(transactionType);
        voucherSaveDTO.setVoucherStatus(ProcessStatusEnum.SUBMITTED.getCode());
        voucherSaveDTO.setRecheckUserNo(approveNum);
        voucherSaveDTO.setRecheckUserName(approveName);
        voucherSaveDTO.setCreateUserName(manualEntity.getCreateUserName());
        voucherSaveDTO.setCreateUserNo(manualEntity.getCreateBy());
        voucherSaveDTO.setBusinessDate(manualEntity.getBusinessDate());
        voucherSaveDTO.setValidFlag(VoucherValidFlagEnum.VALID.getCode());
        //转dataMap
        Map<String,Object> dataMap = BeanUtil.beanToMap(voucherSaveDTO);
        // modify by zhangli.chen for 凭证反冲新增网银数据 on 20250612
        if(manualEntity!=null && manualEntity.getSourceId()!=null){
            VoucherEntity preVoucherEntity =  iVoucherService.getById(manualEntity.getSourceId());
            log.info("====>>ManualServiceImpl==>>generateVoucher==00==>>manualEntity.getSourceId():{},preVoucherEntity:{}"
                    ,manualEntity.getSourceId(),preVoucherEntity);
            if(preVoucherEntity!=null && preVoucherEntity.getInterfaceDataId()!=null){
                InterfaceDataEntity preInterfaceDataEntity = iInterfaceDataService.getById(preVoucherEntity.getInterfaceDataId());
                log.info("====>>ManualServiceImpl==>>generateVoucher==01==>>preVoucherEntity.getInterfaceDataId():{},preInterfaceDataEntity:{}"
                        ,preVoucherEntity.getInterfaceDataId(),preInterfaceDataEntity);
                if(preInterfaceDataEntity!=null && preInterfaceDataEntity.getInterfaceData()!=null){
                    log.info("====>>ManualServiceImpl==>>generateVoucher==02==>>ebankSerialNumber:{},ebankBatchNo:{}"
                            ,preInterfaceDataEntity.getEbankSerialNumber(),preInterfaceDataEntity.getEbankBatchNo());
                    dataMap.put("ebankSerialNumber",preInterfaceDataEntity.getEbankSerialNumber());
                    dataMap.put("ebankBatchNo",preInterfaceDataEntity.getEbankBatchNo());
                }
            }
        }
        // modify by zhangli.chen for 新增反冲金额参数 on 20250613
        if(voucherEntityList!=null){
            BigDecimal receiveUnconfirmed = calculateAccountAmount(voucherEntityList, Constants.RECEIVABLE_UNCONFIRM_RECEIPT);
            dataMap.put("receiveUnconfirmed",(receiveUnconfirmed != null ? receiveUnconfirmed.doubleValue() : 0.0));
        }
        InterfaceDataEntity entity = BeanUtil.copyProperties(dataMap, InterfaceDataEntity.class);
        entity.setInterfaceData(JSONObject.parseObject(JSONObject.toJSONString(dataMap)));
        iInterfaceDataService.save(entity);
        voucherSaveDTO.setInterfaceId(entity.getId());
        voucherSaveDTO.setInterfaceDataId(entity.getId());
        //保存凭证头和凭证行
        Boolean finalFundSystemFlag = fundSystemFlag;
        String finalTransactionType = transactionType;
        List<VoucherEntrySaveDTO> voucherSaveDTOList = Lists.newArrayList();
        voucherEntityList.forEach(v -> {
            //获取科目维度
            VoucherEntrySaveDTO voucherEntrySaveDTO = BeanUtil.copyProperties(v,VoucherEntrySaveDTO.class);
            voucherEntrySaveDTO.setAccountCode(v.getAccountCode());
            voucherEntrySaveDTO.setAccountName(v.getAccountName());
            voucherEntrySaveDTO.setCashAttribute(v.getCashFlowMarker());
            // 客户编码
            if(StringUtils.isEmpty(v.getClientCode())){
                voucherEntrySaveDTO.setClientCode(null);
            }else{
                voucherEntrySaveDTO.setClientCode(v.getClientCode());
            }
            // 客户名称
            if(StringUtils.isEmpty(v.getClientName())){
                voucherEntrySaveDTO.setClientName(null);
            }else{
                voucherEntrySaveDTO.setClientName(v.getClientName());
            }
            // 合同编号
            if(StringUtils.isEmpty(v.getContractCode())){
                voucherEntrySaveDTO.setContractCode(null);
            }else{
                voucherEntrySaveDTO.setContractCode(v.getContractCode());
            }
            // 开票合同编号
            if(StringUtils.isEmpty(v.getLoansContractCode())){
                voucherEntrySaveDTO.setBillContractCode(null);
            }else{
                voucherEntrySaveDTO.setBillContractCode(v.getLoansContractCode());
            }
            voucherEntrySaveDTO.setContractName(v.getContractName());
            voucherEntrySaveDTO.setBankAccount(v.getBankNo());
            voucherEntrySaveDTO.setCashAttribute(v.getCashFlowMarker());
            voucherEntrySaveDTO.setCreditAmount(v.getCreditAmount());
            voucherEntrySaveDTO.setDebitAmount(v.getDebitAmount());
            voucherEntrySaveDTO.setVoucherSummary(v.getVoucherSummary());
            if (ObjectUtils.isNotNull(v.getDebitAmount()) && (v.getDebitAmount().compareTo(BigDecimal.ZERO) != 0)) {
                voucherEntrySaveDTO.setDebitCreditType(DRCREnum.DR.getCode());
            } else if (ObjectUtils.isNotNull(v.getCreditAmount()) && (v.getCreditAmount().compareTo(BigDecimal.ZERO) != 0)) {
                voucherEntrySaveDTO.setDebitCreditType(DRCREnum.CR.getCode());
            }
            if (isNegativeFlag) {
                voucherEntrySaveDTO.setDebitAmount((null== voucherEntrySaveDTO.getDebitAmount() ? BigDecimal.ZERO : voucherEntrySaveDTO.getDebitAmount()).multiply(new BigDecimal(-1)));
                voucherEntrySaveDTO.setCreditAmount((null== voucherEntrySaveDTO.getCreditAmount() ? BigDecimal.ZERO : voucherEntrySaveDTO.getCreditAmount()).multiply(new BigDecimal(-1)));
            }
            if (StringUtils.isEmpty(voucherSaveDTO.getContractCode())){
                voucherSaveDTO.setContractCode(v.getContractCode());
            }
            if (StringUtils.isEmpty(voucherSaveDTO.getClientCode())){
                voucherSaveDTO.setClientCode(v.getClientCode());
            }
            if (StringUtils.isEmpty(voucherSaveDTO.getContractName())){
                voucherSaveDTO.setContractName(v.getContractName());
            }
            if (StringUtils.isEmpty(voucherSaveDTO.getClientName())){
                voucherSaveDTO.setClientName(v.getClientName());
            }
            if (!finalFundSystemFlag) {
                List<AccountEntity> accountEntityList = iAccountService.lambdaQuery().eq(AccountEntity::getAccountCode,v.getAccountCode()).eq(AccountEntity::getBusinessCode,manualEntity.getBusinessCode()).list();
                if (CollectionUtils.isEmpty(accountEntityList)) {
                    throw new ServiceException("科目编码不存在");
                }
                AccountEntity accountEntity = accountEntityList.get(0);
                voucherEntrySaveDTO.setAssistFlags(accountEntity.getAssistFlags());
                voucherEntrySaveDTO.setFundType(accountEntity.getFundType());
                voucherSaveDTOList.add(voucherEntrySaveDTO);
            } else {
                voucherEntrySaveDTO.setFundType("bank_deposits");
                voucherSaveDTOList.add(voucherEntrySaveDTO);
                voucherSaveDTO.setEntryList(Lists.newArrayList(voucherEntrySaveDTO));
                VoucherDTO voucherDTO = iVoucherService.saveVoucherAndEntries(voucherSaveDTO);
                voucherDTO.setBankNo(v.getBankNo());
                voucherDTO.setTransactionType(finalTransactionType);
                voucherDTO.setIsSubmit(YesOrNoEnum.YES.getCode());
                voucherDTO.setIsAutoVoucherFlag(Boolean.FALSE);
                if(StringUtils.isEmpty(voucherDTO.getClientCode())){
                    voucherDTO.setClientCode(null);
                }
                if(StringUtils.isEmpty(voucherDTO.getClientName())){
                    voucherDTO.setClientName(null);
                }
                iFundSystemBalanceService.saveFundSystemBalanceFromVoucher(voucherDTO);
            }
        });
        if (!fundSystemFlag) {
            voucherSaveDTO.setEntryList(voucherSaveDTOList);
            voucherSaveDTO.setIsSubmit(YesOrNoEnum.YES.getCode());
            VoucherDTO voucherDTO = iVoucherService.saveVoucherAndEntries(voucherSaveDTO);
            voucherDTO.setIsSubmit(YesOrNoEnum.YES.getCode());
            voucherDTO.setIsAutoVoucherFlag(Boolean.FALSE);
            if(StringUtils.isEmpty(voucherDTO.getClientCode())){
                voucherDTO.setClientCode(null);
            }
            if(StringUtils.isEmpty(voucherDTO.getClientName())){
                voucherDTO.setClientName(null);
            }
            iContractBalanceService.saveMonualContractBalanceFromVoucher(voucherDTO);
        }
    }

    public void deleteInterfaceData(String interFaceId){
        if (StringUtils.isNotEmpty(interFaceId)) {
            iInterfaceDataService.remove(Wrappers.<InterfaceDataEntity>lambdaQuery()
                    .eq(InterfaceDataEntity::getInterfaceId, interFaceId));
        }
    }

    public long generateVoucherNum(String voucherType, LocalDateTime dateTime){
        try {
            // ========== 使用新的本地号段预分配方案 ==========
            return localSegmentVoucherGenerator.generateVoucherNum(voucherType, dateTime);
        }catch (Exception e){
            log.error("==>>ManualServiceImpl.generateVoucherNum==>>error:{}",e.getMessage());
            //redis key : finhub-年月-凭证类型编码
            String key = CacheConstants.COMMON_PREFIX+"_voucher_num_"+dateTime.getYear()+dateTime.getMonthValue()+"_"+voucherType;
            return redisService.generate(key, REDIS_VOUCHER_NUM_EXPIRE);
        }
    }

    public void checkSaveData(ManualDTO dto) {
        List<ManualVoucherExcelVO> manualVoucherDTOList = dto.getManualVoucherDTOList();
        if (CollectionUtils.isEmpty(manualVoucherDTOList)) {
            throw new ServiceException("凭证行不可以为空");
        }
        manualVoucherDTOList.stream().forEach(v -> {
            v.setBusinessCode(dto.getBusinessCode());
            v.setBusinessDate(dto.getBusinessDate());
            v.setSceneCode(dto.getSceneCode());
            v.setSubSceneType(dto.getSubSceneType());
            v.setOrgId(dto.getOrgId());
            v.setPeriodCode(dto.getPeriodCode());
            v.setRate(dto.getRate());
            v.setVoucherDate(dto.getVoucherDate());
            v.setVoucherType(dto.getVoucherType());
            v.setVoucherSummary(dto.getVoucherSummary());
            v.setCurrencyCode(dto.getCurrencyCode());
            //和批量导入共用一套校验，故增加默认批次号
            v.setBatchId("1");
        });
        checkData(manualVoucherDTOList);
        //校验借方发生额和贷方发生额一致
        checkDebitAndCreditAmount(manualVoucherDTOList);
    }

    public void checkDebitAndCreditAmount(List<ManualVoucherExcelVO> manualVoucherDTOList){
        if (CollectionUtils.isEmpty(manualVoucherDTOList)) {
            return;
        }
        final BigDecimal[] debitAmount = {BigDecimal.ZERO};
        final BigDecimal[] creditAmount = {BigDecimal.ZERO};
        manualVoucherDTOList.forEach(e -> {
            debitAmount[0] = debitAmount[0].add(null == e.getDebitAmount() ? BigDecimal.ZERO : e.getDebitAmount());
            creditAmount[0] = creditAmount[0].add(null == e.getCreditAmount() ? BigDecimal.ZERO : e.getCreditAmount());
        });
        if (debitAmount[0].compareTo(creditAmount[0])!=0) {
            throw new ServiceException("借方发生额和贷方发生额不一致，不能导入");
        }
    }

    @Override
    public Boolean writeOff(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请至少勾选一条数据冲销");
        }
        List<ManualEntity> manualEntityList = this.listByIds(idList);
        List<SysDictData> sysDictDataList = remoteDictService.listDictData(DictTypeEnum.SYS_VOUCHER_TYPE.getCode()).getData();
        Map<String,String> voucherTypeMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(sysDictDataList)) {
            voucherTypeMap =  sysDictDataList.stream().collect(HashMap::new,(map,item)->map.put(item.getDictValue(),item.getDictLabel()),HashMap::putAll);
        }
        //冲销 只有复核状态复制，更新凭证行，记账日期，原始数据增加冲销功能，不可再次冲销，金额乘-1
        Map<String, String> finalVoucherTypeMap = voucherTypeMap;
        manualEntityList.stream().forEach(v -> {
            if ("1".equals(v.getIsWriteOff())) {
                throw new ServiceException("已经冲销的数据不可以再次冲销");
            }
            if (!ProcessStatusEnum.REVIEWED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("只有处理状态为已复核的才可以冲销");
            }
            v.setIsWriteOff("1");
            this.updateById(v);
            ManualEntity manualEntity = BeanUtil.copyProperties(v,ManualEntity.class);
            manualEntity.setId(IdWorker.getId());
            manualEntity.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
            manualEntity.setBusinessDate(LocalDateTime.now());
            manualEntity.setVoucherDate(LocalDateTime.now());
            manualEntity.setPeriodCode(PeriodCodeUtil.periodCodeByLocalDateTime(manualEntity.getVoucherDate()));
            manualEntity.setProcessInstanceId(null);
            manualEntity.setVoucherNum(generateVoucherNum(manualEntity.getVoucherType(),manualEntity.getVoucherDate()));
            manualEntity.setIsWriteOff("0");
            manualEntity.setCreateTime(LocalDateTime.now());
            manualEntity.setRecheckUserNo("");
            manualEntity.setRecheckUserName("");
            manualEntity.setSourceFrom(ManualSourceFrom.SGPZ.getCode());
            manualEntity.setSourceId(v.getId());
            manualEntity.setErrorInfo("");
            String voucherSummary = StringUtils.isEmpty(manualEntity.getVoucherSummary()) ? "" : manualEntity.getVoucherSummary();
            String accountDate = null == v.getVoucherDate() ? "" : DateUtil.format(v.getVoucherDate(),"yyyy-MM-dd");
            String voucherType = StringUtils.isEmpty(manualEntity.getVoucherType()) ? "" : manualEntity.getVoucherType();
            if (finalVoucherTypeMap.containsKey(voucherType)) {
                voucherType = finalVoucherTypeMap.get(voucherType);
            }
            //冲销+记账日期+凭证类型+原凭证号+原凭证摘要
            String summary = "冲销"+accountDate+"-"+voucherType+"-"+v.getVoucherNum()+"号-";
            //凭证行摘要= 冲销原凭证记账日期+原凭证号+原凭证行摘要
            String entrySummary =  "冲销"+accountDate+"-"+v.getVoucherNum()+"-";
            manualEntity.setVoucherSummary(summary+voucherSummary);
            this.save(manualEntity);
            //获取详情信息
            List<ManualVoucherEntity> voucherEntityList = iManualVoucherService.lambdaQuery().eq(ManualVoucherEntity::getManualId, v.getId()).list();
            List<ManualVoucherEntity> newManualVoucherEntityList = Lists.newArrayList();
            voucherEntityList.forEach(e -> {
                ManualVoucherEntity manualVoucherEntity = BeanUtil.copyProperties(e, ManualVoucherEntity.class);
                manualVoucherEntity.setId(null);
                manualVoucherEntity.setManualId(manualEntity.getId());
                manualVoucherEntity.setPeriodCode(manualEntity.getPeriodCode());
                manualVoucherEntity.setVoucherDate(manualEntity.getVoucherDate());
                manualVoucherEntity.setBusinessDate(manualEntity.getBusinessDate());
                manualVoucherEntity.setCreateTime(LocalDateTime.now());
                if (null != e.getCreditAmount()) {
                    manualVoucherEntity.setCreditAmount(new BigDecimal(-1).multiply((null == e.getCreditAmount() ? BigDecimal.ZERO : e.getCreditAmount())));
                }
                if (null != e.getDebitAmount()) {
                    manualVoucherEntity.setDebitAmount(new BigDecimal(-1).multiply((null == e.getDebitAmount() ? BigDecimal.ZERO : e.getDebitAmount())));
                }
                manualVoucherEntity.setVoucherSummary(entrySummary+e.getVoucherSummary());
                manualVoucherEntity.setSubsidiaryAccount(entrySummary+e.getSubsidiaryAccount());
                newManualVoucherEntityList.add(manualVoucherEntity);
            });
            iManualVoucherService.saveBatch(newManualVoucherEntityList);
        });
        return Boolean.TRUE;
    }

    @Override
    public Boolean copy(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请至少勾选一条数据复制");
        }
        List<ManualEntity> manualEntityList = this.listByIds(idList);
        //复制，记账日期，会计期间，凭证编码，状态改为已录入
        String userNo = UserUtils.getStaffCode();
        String userName = UserUtils.getStaffName();
        manualEntityList.stream().forEach(v -> {
            v.setIsWriteOff("0");
            ManualEntity manualEntity = BeanUtil.copyProperties(v,ManualEntity.class);
            manualEntity.setId(IdWorker.getId());
            manualEntity.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
            manualEntity.setBusinessDate(LocalDate.now().atStartOfDay());
            manualEntity.setVoucherDate(LocalDate.now().atStartOfDay());
            manualEntity.setPeriodCode(PeriodCodeUtil.periodCodeByLocalDateTime(manualEntity.getVoucherDate()));
            manualEntity.setProcessInstanceId(null);
            manualEntity.setVoucherNum(generateVoucherNum(manualEntity.getVoucherType(),manualEntity.getVoucherDate()));
            manualEntity.setCreateTime(LocalDateTime.now());
            manualEntity.setCreateBy(userNo);
            manualEntity.setCreateUserName(userName);
            manualEntity.setUpdateBy(userNo);
            manualEntity.setSourceFrom("");
            manualEntity.setSourceId(null);
            this.save(manualEntity);
            //获取详情信息
            List<ManualVoucherEntity> voucherEntityList = iManualVoucherService.lambdaQuery().eq(ManualVoucherEntity::getManualId, v.getId()).list();
            List<ManualVoucherEntity> newManualVoucherEntityList = Lists.newArrayList();
            voucherEntityList.stream().forEach(e -> {
                ManualVoucherEntity manualVoucherEntity = BeanUtil.copyProperties(e, ManualVoucherEntity.class);
                manualVoucherEntity.setId(IdWorker.getId());
                manualVoucherEntity.setManualId(manualEntity.getId());
                manualVoucherEntity.setPeriodCode(manualEntity.getPeriodCode());
                manualVoucherEntity.setVoucherDate(manualEntity.getVoucherDate());
                manualVoucherEntity.setBusinessDate(manualEntity.getBusinessDate());
                manualVoucherEntity.setCreateTime(LocalDateTime.now());
                manualVoucherEntity.setCreateBy(userNo);
                manualVoucherEntity.setUpdateBy(userNo);
                newManualVoucherEntityList.add(manualVoucherEntity);
            });
            iManualVoucherService.saveBatch(newManualVoucherEntityList);
        });
        return Boolean.TRUE;
    }

    @Override
    public String extenalDataCheck(ManualDTO dto) {
        AtomicReference<String> error = new AtomicReference<>("");
        //校验
        if (ObjectUtils.isNotNull(dto.getVoucherDate())) {
            dto.setPeriodCode(PeriodCodeUtil.periodCodeByDate(dto.getVoucherDate()));
        }
        //先调用公共的check方法
        checkSaveData(dto);
        if (BatchTypeEnum.KJFP.getCode().equals(dto.getSourceFrom())) {
            InvoiceClaimEntity invoiceClaimEntity =  invoiceClaimService.getById(dto.getExternalId());
            if (null!=invoiceClaimEntity.getManualId()) {
                throw new ServiceException("该开票数据已经生成手工凭证不可以再次生成");
            }
            //2221.01.05科目金额与税额不相等/合同编号与开票认领记录合同编号不一致，是否保存
            BigDecimal taxValue = null==invoiceClaimEntity.getTaxValue() ? BigDecimal.ZERO : invoiceClaimEntity.getTaxValue();
            String contractCode = StringUtils.isEmpty(invoiceClaimEntity.getContractCode()) ? "" : invoiceClaimEntity.getContractCode();
            AtomicReference<Boolean> isFlag = new AtomicReference<>(Boolean.FALSE);
            AtomicReference<Boolean> isContractFlag = new AtomicReference<>(Boolean.FALSE);
            //获取科目编码为2221.01.05的金额之和
            final BigDecimal[] amountTotal = {BigDecimal.ZERO};
            String amountError = "2221.01.05科目金额与税额不相等";

            dto.getManualVoucherDTOList().stream().forEach(v-> {
               BigDecimal amount = null == v.getDebitAmount() ? v.getCreditAmount() : v.getDebitAmount();
               if ("2221.01.05".equals(v.getAccountCode()) && null != amount) {
                   amountTotal[0] = amountTotal[0].add(amount);
                   isFlag.set(Boolean.TRUE);
               }
                String manualContractCode = StringUtils.isEmpty(v.getContractCode()) ? "" : v.getContractCode();
               if (contractCode.equals(manualContractCode)) {
                   isContractFlag.set(Boolean.TRUE);
               }
//                if(!contractCode.equals(manualContractCode) && !isContractFlag.get()) {
//                    String info ="合同编号与开票认领记录合同编号不一致";
//                    if (StringUtils.isNotEmpty(error.get())) {
//                        error.set(error.get()+"/"+ info);
//                    } else {
//                        error.set(error.get()+ info);
//                    }
//                    isContractFlag.set(Boolean.TRUE);
//                }
            });
            if (!isContractFlag.get()) {
                error.set("合同编号与开票认领记录合同编号不一致");
            }
            if (isFlag.get() && amountTotal[0].compareTo(taxValue)!=0) {
                if (StringUtils.isNotEmpty(error.get())) {
                    error.set(amountError+"/"+error.get());
                } else {
                    error.set(amountError);
                }
            }
        }
        if (StringUtils.isNotEmpty(error.get())) {
            error.set(error.get()+",是否保存");
        }
        return error.get();
    }

    public Map<String,String> getAllSceneMap() {
        return iSceneService.list().stream().collect(HashMap::new,(h,v)->h.put(v.getSceneName(),v.getSceneCode()),HashMap::putAll);
    }

    public Map<String,String> getAllCurrency() {
        return iCurrencyService.list().stream().collect(HashMap::new,(h,v)->h.put(v.getCurrenctName(),v.getCurrencyCode()),HashMap::putAll);
    }

    /**
     * 批量更新手工凭证的审核状态
     */
    public void updateStatusByids(List<String> ids, String status, String recheckUserNo, String recheckUserName) {
        if (ids == null || ids.isEmpty()) {
            return ;
        }
        LambdaUpdateWrapper<ManualEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ManualEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        updateWrapper.in(ManualEntity::getId, ids);
        updateWrapper.set(ManualEntity::getProcessStatus, status);
        updateWrapper.set(ManualEntity::getRecheckUserName, recheckUserName);
        updateWrapper.set(ManualEntity::getRecheckUserNo, recheckUserNo);
        this.update(updateWrapper);
    }

    public String initClientCode(List<ManualVoucherExcelVO> manualVoucherExcelVOList) {
        if (CollectionUtils.isEmpty(manualVoucherExcelVOList)) {
            throw new ServiceException("导入的文件为空");
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("以下合同客户类型存在多条客户编码：");
         //过滤出合同编码不为空客户编码为空，客户类型不为空的数据
         List<ManualVoucherExcelVO> excelVOList = manualVoucherExcelVOList.stream().filter(v -> StringUtils.isNotEmpty(v.getContractCode())
                 && StringUtils.isEmpty(v.getClientCode()) && StringUtils.isNotEmpty(v.getClientType())).collect(Collectors.toList());
         if (CollectionUtils.isEmpty(excelVOList)) {
             return "";
         }
        List<String> contractCodeList = excelVOList.stream().map(ManualVoucherExcelVO::getContractCode).distinct().collect(Collectors.toList());
        List<String> clientTypeList = excelVOList.stream().map(ManualVoucherExcelVO::getClientType).distinct().collect(Collectors.toList());
        AtomicReference<Boolean> flag = new AtomicReference<>(Boolean.FALSE);
        List<String> contractCodeExistList = Lists.newArrayList();
        //根据合同编码+客户类型获取latest中客户编码
        Map<String,List<ContractBalanceLatestEntity>> latestMap = iContractBalanceLatestService.lambdaQuery().in(ContractBalanceLatestEntity::getContractCode,contractCodeList).in(ContractBalanceLatestEntity::getClientType,clientTypeList).isNotNull(ContractBalanceLatestEntity::getClientCode).list().stream().collect(Collectors.groupingBy(v -> v.getContractCode()+"-"+v.getClientType()));
        manualVoucherExcelVOList.forEach(v -> {
            String key = v.getContractCode()+"-"+v.getClientType();
            if (latestMap.containsKey(key) && StringUtils.isEmpty(v.getClientCode())) {
                v.setClientCode(latestMap.get(key).get(0).getClientCode());
                if (latestMap.get(key).size()>1 && !contractCodeExistList.contains(v.getContractCode())) {
                    flag.set(Boolean.TRUE);
                    contractCodeExistList.add(v.getContractCode());
                    stringBuilder.append(v.getContractCode()).append(",");
                }
            }
        });
        if (!flag.get()) {
            stringBuilder.setLength(0);
        } else {
            stringBuilder.deleteCharAt(stringBuilder.length()-1);
        }
        return stringBuilder.toString();
    }

    @Override
    public String importTemplateCheck(MultipartFile file) {
        ExcelUtil<ManualVoucherExcelVO> util = new ExcelUtil<ManualVoucherExcelVO>(ManualVoucherExcelVO.class);
        try {
            List<ManualVoucherExcelVO> manualVoucherExcelVOList = util.importExcel(file.getInputStream());
            return initClientCode(manualVoucherExcelVOList);
        } catch (Exception e) {
            throw new ServiceException("导入校验失败，失败原因："+e.getMessage());
        }
    }

    /**
     * @description: 查询科目的金额值
     * @author: zhangli.chen
     **/
    public static BigDecimal calculateAccountAmount(List<ManualVoucherEntity> voucherEntityList,String accountCode) {
        return voucherEntityList.stream()
                .filter(entity -> entity != null && accountCode.equals(entity.getAccountCode()))
                .map(entity -> {
                    BigDecimal debit = entity.getDebitAmount() != null ? entity.getDebitAmount() : BigDecimal.ZERO;
                    BigDecimal credit = entity.getCreditAmount() != null ? entity.getCreditAmount() : BigDecimal.ZERO;
                    return debit.subtract(credit);
                }).reduce(BigDecimal.ZERO, BigDecimal::add);
    }


}

