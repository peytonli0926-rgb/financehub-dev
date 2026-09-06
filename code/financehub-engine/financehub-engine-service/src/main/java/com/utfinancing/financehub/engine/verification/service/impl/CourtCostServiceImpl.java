package com.utfinancing.financehub.engine.verification.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.common.utils.MapUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.approve.model.dto.ApproveDTO;
import com.utfinancing.financehub.engine.approve.service.IApproveService;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.entity.ContractBalanceEntity;
import com.utfinancing.financehub.engine.finance.entity.ContractEntity;
import com.utfinancing.financehub.engine.finance.model.dto.ContractQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.OrgCompanyQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherDTO;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherEntryQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ContractVO;
import com.utfinancing.financehub.engine.finance.model.vo.OrgCompanyVO;
import com.utfinancing.financehub.engine.finance.model.vo.VoucherEntryVO;
import com.utfinancing.financehub.engine.finance.service.*;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import com.utfinancing.financehub.engine.rule.model.dto.ExecuteCommonDTO;
import com.utfinancing.financehub.engine.rule.model.vo.VoucherInfoVO;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import com.utfinancing.financehub.engine.verification.entity.CourtCostDetailsEntity;
import com.utfinancing.financehub.engine.verification.entity.VerificationDetailsEntity;
import com.utfinancing.financehub.engine.verification.entity.VerificationEntity;
import com.utfinancing.financehub.engine.verification.model.dto.*;
import com.utfinancing.financehub.engine.verification.model.vo.CourtCostDetailsVO;
import com.utfinancing.financehub.engine.verification.model.vo.CourtCostReportFormVO;
import com.utfinancing.financehub.engine.verification.model.vo.CourtCostVO;
import com.utfinancing.financehub.engine.verification.entity.CourtCostEntity;
import com.utfinancing.financehub.engine.verification.mapper.CourtCostMapper;
import com.utfinancing.financehub.engine.verification.service.ICourtCostDetailsService;
import com.utfinancing.financehub.engine.verification.service.ICourtCostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author : bruyang
 * @Date : Create in 2023-10-23
 * @Description :  CourtCost服务实现类
 * @Modified :
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class CourtCostServiceImpl extends ServiceImpl<CourtCostMapper, CourtCostEntity> implements ICourtCostService {

    private final CourtCostMapper courtCostMapper;

    @Resource
    private IContractService iContractService;

    @Resource
    private IContractBalanceService iContractBalanceService;

    @Resource
    private ICourtCostDetailsService iCourtCostDetailsService;

    @Resource
    private IRuleService iRuleService;

    @Resource
    private IVoucherEntryService iVoucherEntryService;

    @Resource
    private IOrgCompanyService iOrgCompanyService;

    @Resource
    private IApproveService iApproveService;

    @Value("${approve.url.courtCost-url:null}")
    private String approveUrl;

    private IVoucherService iVoucherService;

    @Override
    public Long saveCourtCost(CourtCostDTO dto) {
        CourtCostEntity entity = BeanUtil.copyProperties(dto, CourtCostEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateCourtCost(Long id, CourtCostDTO dto) {
        CourtCostEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public CourtCostDTO getCourtCostDTOById(Long id) {
        CourtCostEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, CourtCostDTO.class);
    }

    @Override
    public IPage<CourtCostVO> selectPage(CourtCostQueryDTO queryDTO) {
        LambdaQueryWrapper<CourtCostEntity> queryWrapper = Wrappers.<CourtCostEntity>lambdaQuery();
        if (ObjectUtil.isNotEmpty(queryDTO.getAccountDate())) {
            queryWrapper.eq(CourtCostEntity::getAccountDate, queryDTO.getAccountDate());
        }
        if (StringUtils.isNotEmpty(queryDTO.getProcessStatus())) {
            queryWrapper.eq(CourtCostEntity::getProcessStatus, queryDTO.getProcessStatus());
        }
        if (ObjectUtil.isNotEmpty(queryDTO.getStartAccountDate())) {
            queryWrapper.ge(CourtCostEntity::getAccountDate,queryDTO.getStartAccountDate());
        }
        if (ObjectUtil.isNotEmpty(queryDTO.getEndAccountDate())) {
            queryWrapper.le(CourtCostEntity::getAccountDate, queryDTO.getEndAccountDate());
        }
        if (CollectionUtils.isNotEmpty(queryDTO.getProcessStatusList())) {
            queryWrapper.in(CourtCostEntity::getProcessStatus, queryDTO.getProcessStatusList());
        }
        IPage<CourtCostEntity> entityIPage = courtCostMapper.selectPage(new Page<CourtCostEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        IPage<CourtCostVO> courtCostVOIPage = ListBeanUtil.copyPage(entityIPage, CourtCostVO.class);
        courtCostVOIPage.getRecords().forEach(v -> {
            v.setBatchType(BatchTypeEnum.SSF.getCode());
        });
        return courtCostVOIPage;
    }

    @Override
    public Boolean deleteByIds(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请至少勾选一条数据删除");
        }
        List<CourtCostEntity> courtCostEntityList = this.listByIds(idList);
        courtCostEntityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("只有处理状态为已录入的才可以删除");
            }
        });
        this.removeBatchByIds(idList);
        //删除凭证
        batchDeleteVoucher(idList);
        return iCourtCostDetailsService.removeBatchByCourtCostIdList(idList);
    }

    @Override
    public Boolean submit(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请至少勾选一条数据提交");
        }
        List<CourtCostEntity> courtCostEntityList = this.listByIds(idList);
        List<ApproveDTO> approveDTOList = Lists.newArrayList();
        courtCostEntityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("只有处理状态为已录入的才可以提交");
            }
            v.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
            ApproveDTO approveDTO = new ApproveDTO();
            approveDTO.setDocumentId(v.getId());
            approveDTO.setDocumentType(BatchTypeEnum.SSF.getCode());
            approveDTO.setUrl(approveUrl+v.getId());
            approveDTOList.add(approveDTO);
        });
        //发送审核
        Map<Long,Long> processInstantIdMap = iApproveService.submit(approveDTOList);
        courtCostEntityList.stream().forEach(v -> {
            if (null!=processInstantIdMap && processInstantIdMap.containsKey(v.getId())) {
                v.setProcessInstanceId(processInstantIdMap.get(v.getId()));
            }
        });
        generateVoucher(idList, YesOrNoEnum.YES.getCode());
        return this.updateBatchById(courtCostEntityList);
    }

    @Override
    public Boolean withdraw(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请至少勾选一条数据撤回");
        }
        List<CourtCostEntity> courtCostEntityList = this.listByIds(idList);
        courtCostEntityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.SUBMITTED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("只有处理状态为已提交的才可以撤回");
            }
            v.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
            v.setIsGenerateVoucher("0");
        });
        iApproveService.withdraw(courtCostEntityList.stream().map(CourtCostEntity::getProcessInstanceId).collect(Collectors.toList()));
        //删除凭证
        batchDeleteVoucher(idList);
        return this.updateBatchById(courtCostEntityList);
    }

    @Override
    public Boolean importFile(MultipartFile file) {
        try {
            ExcelUtil<CourtCostDetailsExcelDTO> util = new ExcelUtil<CourtCostDetailsExcelDTO>(CourtCostDetailsExcelDTO.class);
            List<CourtCostDetailsExcelDTO> courtCostDetailsExcelDTOList = util.importExcel(file.getInputStream());
            //获取签约主体信息
            Map<String,String> orgIdByOrgNameMap = getOrgIdOrgName();
            courtCostDetailsExcelDTOList.stream().forEach(v -> {
                //获取签约主体信息
               if (orgIdByOrgNameMap.containsKey(v.getOrgName())) {
                   v.setOrgId(orgIdByOrgNameMap.get(v.getOrgName()));
               }
            });
            checkData(courtCostDetailsExcelDTOList);
            LocalDateTime accountDate = DateUtil.toLocalDateTime(courtCostDetailsExcelDTOList.get(0).getAccountDate());
            CourtCostDTO courtCostDTO = new CourtCostDTO();
            courtCostDTO.setAccountDate(accountDate);
            courtCostDTO.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
            Long courtCostId = saveCourtCost(courtCostDTO);
            List<CourtCostDetailsEntity> detailsEntityList = Lists.newArrayList();
            courtCostDetailsExcelDTOList.stream().forEach(v -> {
                CourtCostDetailsEntity detailsEntity = new CourtCostDetailsEntity();
                BeanUtil.copyProperties(v, detailsEntity);
                detailsEntity.setCourtCostId(courtCostId);
                detailsEntity.setAccountDate(accountDate);
                detailsEntityList.add(detailsEntity);
            });
            iCourtCostDetailsService.saveBatch(detailsEntityList);
            //刷新诉讼费转费用
            refreshVourchAndAmount(courtCostId);
        } catch (Exception e) {
            throw new ServiceException("上传文件失败，失败原因:"+e.getMessage());
        }
        return Boolean.TRUE;
    }

    @Override
    public List<CourtCostDetailsVO> listByCondition(CourtCostDetailsQueryDTO queryDTO) {
        if (CollectionUtils.isEmpty(queryDTO.getCourtCostIdList())) {
            throw new ServiceException("请至少勾选一条数据下载");
        }
        Map<String,String> orgNameByOrgIdMap = getOrgNameOrgId();
        List<CourtCostDetailsVO> detailsVOList = iCourtCostDetailsService.listByCondition(queryDTO);
        detailsVOList.stream().forEach(v -> {
            if (orgNameByOrgIdMap.containsKey(v.getOrgId())) {
                v.setOrgName(orgNameByOrgIdMap.get(v.getOrgId()));
            }
        });
        return detailsVOList;
    }

    public void checkData(List<CourtCostDetailsExcelDTO> courtCostDetailsExcelDTOList){
        if (CollectionUtils.isEmpty(courtCostDetailsExcelDTOList)) {
            throw new ServiceException("没数据可供上传");
        }
        //获取系统所有的合同编号
        List<String> contractList = courtCostDetailsExcelDTOList.stream().map(CourtCostDetailsExcelDTO::getContractCode).collect(Collectors.toList());
        List<String> orgIdList = courtCostDetailsExcelDTOList.stream().map(CourtCostDetailsExcelDTO::getOrgId).collect(Collectors.toList());
        ContractQueryDTO queryDTO = new ContractQueryDTO();
        queryDTO.setContractCodeList(contractList);
        queryDTO.setOrgIds(orgIdList);
        Map<String,List<ContractVO>> contractCodeMap =  iContractService.allContractList(queryDTO).stream().collect(Collectors.groupingBy(v->v.getContractCode()+"_"+v.getOrgId()));
        Date accountDate = courtCostDetailsExcelDTOList.get(0).getAccountDate();
        courtCostDetailsExcelDTOList.stream().forEach(v -> {
            if (ObjectUtil.isNull(v.getAccountDate())) {
                throw new ServiceException("记账日期不可以为空");
            }
            if (StringUtils.isEmpty(v.getOrgName())) {
                throw new ServiceException("签约实体不可以为空");
            }
            if (StringUtils.isEmpty(v.getOrgId())) {
                throw new ServiceException("签约实体在系统中不存在");
            }
            if (StringUtils.isEmpty(v.getContractCode())) {
                throw new ServiceException("合同编号不可以为空");
            }
            if (StringUtils.isEmpty(v.getCostCenter())) {
                throw new ServiceException("成本中心不可以为空");
            }
            if (ObjectUtil.isNull(v.getTransgerCostAmount())) {
                throw new ServiceException("诉讼费转费用不可以为空");
            }
            if (accountDate.compareTo(v.getAccountDate())!=0) {
                throw new ServiceException("记账日期存在不一致，不能导入");
            }
            if (!contractCodeMap.containsKey(v.getContractCode()+"_"+v.getOrgId())) {
                throw new ServiceException("合同编号+签约主体在系统中不存在");
            }
            //校验转费用金额大于应收诉讼费金额
            ContractVO contractEntity = contractCodeMap.get(v.getContractCode()+"_"+v.getOrgId()).get(0);
            Map<String, Object> balanceMap = iContractBalanceService.getLastBalanceMap(contractEntity.getBusinessCode(),contractEntity.getClientCode(),contractEntity.getContractCode(),contractEntity.getOrgId(),null);
            if (null != balanceMap && !balanceMap.isEmpty()) {
                ContractBalanceEntity contractBalanceEntity = BeanUtil.toBean(balanceMap, ContractBalanceEntity.class);
                //receivable_litigation_expenses_balance
                if (v.getTransgerCostAmount().compareTo(contractBalanceEntity.getReceivableLitigationExpensesBalance())>0) {
                    throw new ServiceException("转费用金额不可以大于诉讼费余额");
                }
            }
        });
    }

    public void refreshVourchAndAmount(Long id) {
        CourtCostEntity courtCostEntity = this.getById(id);
        //获取详情信息
        CourtCostDetailsQueryDTO queryDTO = new CourtCostDetailsQueryDTO();
        queryDTO.setCourtCostId(id);
        List<CourtCostDetailsVO> detailsVOList = iCourtCostDetailsService.listByCondition(queryDTO);
        final BigDecimal[] transgerCostAmount = {BigDecimal.ZERO};
        if (CollectionUtils.isNotEmpty(detailsVOList)) {
            detailsVOList.stream().forEach(v -> {
                if (ObjectUtil.isNotNull(v.getTransgerCostAmount())) {
                    transgerCostAmount[0] = transgerCostAmount[0].add(v.getTransgerCostAmount());
                }
            });
        } else {
            courtCostEntity.setIsGenerateVoucher("0");
        }
        courtCostEntity.setTransgerCostAmount(transgerCostAmount[0]);
        this.updateById(courtCostEntity);
    }

    @Override
    public IPage<CourtCostDetailsVO> selectDetailPage(CourtCostDetailsQueryDTO queryDTO) {
        return iCourtCostDetailsService.selectPage(queryDTO);
    }

    @Override
    public Boolean generateVoucher(List<Long> idList,String isSubmit) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请至少勾选一条数据");
        }
        List<CourtCostEntity> courtCostEntityList = this.listByIds(idList);
        courtCostEntityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("只有处理状态为已录入才可以生成凭证");
            }
        });
        //获取详情信息
        CourtCostDetailsQueryDTO queryDTO = new CourtCostDetailsQueryDTO();
        queryDTO.setCourtCostIdList(idList);
        List<CourtCostDetailsVO> detailsVOList = iCourtCostDetailsService.listByCondition(queryDTO);
        if (CollectionUtils.isEmpty(detailsVOList)) {
            throw new ServiceException("不存在数据生成凭证");
        }
        //取诉讼费记账日期对应合同表合同更新日期最近的那条合同的合同状态
        //获取所有的合同信息
        List<ContractEntity> contractEntityList = iContractService.lambdaQuery().
                in(ContractEntity::getContractCode, detailsVOList.stream().map(CourtCostDetailsVO::getContractCode).collect(Collectors.toList())).in(ContractEntity::getOrgId, detailsVOList.stream().map(CourtCostDetailsVO::getOrgId).collect(Collectors.toList())).list();
        Map<String,List<ContractEntity>> contractMap = contractEntityList.stream().collect(Collectors.groupingBy(v -> v.getContractCode()+"-"+v.getOrgId()));
        List<Long> courtCostList = detailsVOList.stream().map(CourtCostDetailsVO::getCourtCostId).distinct().collect(Collectors.toList());
        //按照诉讼费id分组
        Map<Long, List<CourtCostDetailsVO>> courtCostDetailsMap = detailsVOList.stream().collect(Collectors.groupingBy(CourtCostDetailsVO::getCourtCostId));
        List<Map<String,Object>> voucherMapList = Lists.newArrayList();
        for (Map.Entry<Long, List<CourtCostDetailsVO>> entry : courtCostDetailsMap.entrySet()) {
            List<CourtCostDetailsVO> detailsVOS = entry.getValue();
            detailsVOS.stream().forEach(v -> {
                String key = v.getContractCode()+"-"+v.getOrgId();
                if (contractMap.containsKey(key)) {
                    ContractEntity contractEntity = getLatestFinancialDateContract(contractMap.get(key), v);
                    ExecuteCommonDTO executeCommonDTO = new ExecuteCommonDTO();
                    executeCommonDTO.setSystemCode(SystemEnum.CWZT.getCode());
                    executeCommonDTO.setSystemName(SystemEnum.CWZT.getDesc());
                    executeCommonDTO.setBusinessCode("ZLYW");
                    executeCommonDTO.setBusinessName("租赁");
                    executeCommonDTO.setSceneCode(SceneEnum.SSF.getCode());
                    executeCommonDTO.setSceneName(SceneEnum.SSF.name());
                    executeCommonDTO.setOrderId(v.getId().toString());
                    executeCommonDTO.setBusinessDate(new Date());
                    executeCommonDTO.setContractCode(v.getContractCode());
                    executeCommonDTO.setOrgId(v.getOrgId());
                    executeCommonDTO.setFinancialContractStatus(contractEntity.getFinancialContractStatus());
                    executeCommonDTO.setCurrencyType(contractEntity.getCurrencyType());
                    executeCommonDTO.setAccountDate(v.getAccountDate());
                    executeCommonDTO.setBatchId(entry.getKey());
                    executeCommonDTO.setBatchType(BatchTypeEnum.SSF.getCode());
                    executeCommonDTO.setFinanceDate(DateUtil.toLocalDateTime(v.getAccountDate()));
                    executeCommonDTO.setIsSubmit(isSubmit);
                    Map<String, Object> dataMap = BeanUtil.beanToMap(executeCommonDTO);
                    //诉讼费收回-收回是手工凭证 litigationExpenseReceipt
                    //诉讼费支付-魔方系统的数据 litigationExpensePayment
                    //诉讼费转费用
                    dataMap.put("litigationExpenseTransfer", ObjectUtil.isNotNull(v.getTransgerCostAmount()) ? v.getTransgerCostAmount() : BigDecimal.ZERO);
                    dataMap.put("costCentre",v.getCostCenter());//成本中心
                    log.info("生成凭证参数：{}", JSON.toJSONString(dataMap));
                    voucherMapList.add(dataMap);
                }
            });
        }
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
            if (CollectionUtils.isNotEmpty(infoVO.getVoucherDTOList())) {
                voucherIds = infoVO.getVoucherDTOList().stream().map(VoucherDTO::getId).map(String::valueOf).collect(Collectors.toList()).stream().collect(Collectors.joining(","));
            }
            iCourtCostDetailsService.lambdaUpdate().set(CourtCostDetailsEntity::getVoucherId, voucherIds).eq(CourtCostDetailsEntity::getId,Long.parseLong(infoVO.getOrderId())).update();
        }
        LambdaUpdateChainWrapper<CourtCostEntity> costEntityUpdateWrapper =  this.lambdaUpdate().set(CourtCostEntity::getIsGenerateVoucher, "1").in(CourtCostEntity::getId, courtCostList);
        return costEntityUpdateWrapper.update();
    }

    @Override
    public IPage<CourtCostReportFormVO> selectReportFormPage(CourtCostDetailsQueryDTO queryDTO) {
        IPage<CourtCostReportFormVO> reportFormVOIPage = iCourtCostDetailsService.selectReportFormPage(queryDTO);
        if (CollectionUtils.isEmpty(reportFormVOIPage.getRecords())) {
            return reportFormVOIPage;
        }
        List<CourtCostReportFormVO> costDetailsVOList = reportFormVOIPage.getRecords();
        getReportFormData(costDetailsVOList);
        return reportFormVOIPage;
    }

    @Override
    public List<CourtCostReportFormVO> reportFormDetails(CourtCostDetailsQueryDTO queryDTO) {
        List<CourtCostReportFormVO> courtCostReportFormVOList = iCourtCostDetailsService.selectReportFormDetails(queryDTO);
        getReportFormData(courtCostReportFormVOList);
        return courtCostReportFormVOList;
    }

    @Override
    public Boolean updateProcessStatus(CommonApproveDTO commonApproveDTO) {
        if (StringUtils.isEmpty(commonApproveDTO.getDocumentStatus())) {
            throw new ServiceException("诉讼费状态不可以为空");
        }
        CourtCostEntity courtCostEntity = this.getById(commonApproveDTO.getDocumentId());
        if (null == courtCostEntity) {
            throw new ServiceException("诉讼费数据不存在");
        }
        String processStatus = courtCostEntity.getProcessStatus();
        if (ProcessStatusEnum.REVIEWED.getCode().equals(commonApproveDTO.getDocumentStatus())) {
            processStatus = ProcessStatusEnum.REVIEWED.getCode();
            // TODO: 11/01/2024  待传送金蝶
        } else if (ProcessStatusEnum.REJECTED.getCode().equals(commonApproveDTO.getDocumentStatus())) {
            processStatus = ProcessStatusEnum.REJECTED.getCode();
        }
        courtCostEntity.setProcessStatus(processStatus);
        return this.updateById(courtCostEntity);
    }

    public void getReportFormData(List<CourtCostReportFormVO> costDetailsVOList){
        Map<String,String> orgNameByOrgIdMap = getOrgNameOrgId();
        costDetailsVOList.forEach(v -> {
            if (orgNameByOrgIdMap.containsKey(v.getOrgId())) {
                v.setOrgName(orgNameByOrgIdMap.get(v.getOrgId()));
            }
            //获取诉讼费转费用金额
            final BigDecimal[] transgerCostAmount = {BigDecimal.ZERO};
            //获取合同余额表对应的期初余额 receivable_litigation_expenses_balance
            final BigDecimal[] receivableLitigationExpensesBalance = {BigDecimal.ZERO};
            //期末余额 = （应收诉讼费科目-期初余额+（应收诉讼费科目-诉讼费支付）-（应收诉讼费科目-诉讼费收回）-应收诉讼费科目-诉讼费转费用）
            BigDecimal litigationExpensesEndBalance = BigDecimal.ZERO;
            //应收诉讼费科目-诉讼费支付
            final BigDecimal[] receivableRentPay = {BigDecimal.ZERO};
            //应收诉讼费科目-诉讼费收回
            final BigDecimal[] receivableRentRecover = {BigDecimal.ZERO};
            //管理费用-诉讼费科目-支付金额
            final BigDecimal[] litigationExpensesPayTotal = {BigDecimal.ZERO};
            //管理费用-诉讼费科目-收回金额
            final BigDecimal[] litigationExpensesRecoverTotal = {BigDecimal.ZERO};
            //代收款项科目-支付金额
            final BigDecimal[] receivableRentPayTotal = {BigDecimal.ZERO};
            //代收款项科目-收回金额
            final BigDecimal[] receivableRentRecoverTotal = {BigDecimal.ZERO};
            //应付未付款
            final BigDecimal[] payableUnpaid = {BigDecimal.ZERO};
            //获取凭证明细数据
            VoucherEntryQueryDTO queryDTO = new VoucherEntryQueryDTO();
            queryDTO.setContractCode(v.getContractCode());
            queryDTO.setOrgId(v.getOrgId());
            if (ObjectUtil.isNotNull(v.getVoucherId())) {
                queryDTO.setVoucherId(v.getVoucherId());
            }
            List<VoucherEntryVO> voucherEntryVOList = iVoucherEntryService.selectCourtCostByCondition(queryDTO);
            voucherEntryVOList.stream().forEach(e -> {
                if (e.getOrderId().contains("_")) {
                    payableUnpaid[0] =  payableUnpaid[0].add(null == e.getCreditAmount() ? BigDecimal.ZERO : e.getCreditAmount());
                } else {
                    //获取转费用合计
                    if (BatchTypeEnum.SSF.getCode().equals(e.getBatchType())) {
                        if ("1221.04".equals(e.getAccountCode())) {
                            transgerCostAmount[0] = transgerCostAmount[0].add(null == e.getCreditAmount() ? BigDecimal.ZERO : e.getCreditAmount());
                        }
                    } else if (BatchTypeEnum.SSFZF.getCode().equals(e.getBatchType())) {
                        //获取 应收诉讼费科目-诉讼费支付receivableRentPay
                        if ("1221.04".equals(e.getAccountCode())) {
                            receivableRentPay[0] = receivableRentPay[0].add(null == e.getCreditAmount() ? BigDecimal.ZERO : e.getCreditAmount());
                        } else if ("6602.27".equals(e.getAccountCode())) {
                            //管理费用-诉讼费科目-支付金额
                            litigationExpensesPayTotal[0] = litigationExpensesPayTotal[0].add(null == e.getDebitAmount() ? BigDecimal.ZERO : e.getDebitAmount());
                        } else if ("2241.09".equals(e.getAccountCode())) {
                            //代收款项科目-支付金额
                            receivableRentPayTotal[0] = receivableRentPayTotal[0].add(null == e.getDebitAmount() ? BigDecimal.ZERO : e.getDebitAmount());
                        }
                    }
                }
            });
            //获取合同余额表对应的期初余额
            List<ContractBalanceEntity> contractBalanceEntityList = iContractBalanceService.lambdaQuery().eq(ContractBalanceEntity::getContractCode,v.getContractCode())
                    .eq(ContractBalanceEntity::getOrgId,v.getOrgId()).eq(ObjectUtil.isNotNull(v.getAccountDate()),ContractBalanceEntity::getBusinessDate,v.getAccountDate())
                    .eq(ObjectUtil.isNotNull(v.getVoucherId()),ContractBalanceEntity::getVoucherId,v.getVoucherId()).list();
            if (CollectionUtils.isNotEmpty(contractBalanceEntityList)) {
                contractBalanceEntityList.stream().forEach(balanceEntity -> {
                    receivableLitigationExpensesBalance[0] = receivableLitigationExpensesBalance[0].add(!NumberUtil.equals(balanceEntity.getReceivableLitigationExpensesBalance(), 0) ? balanceEntity.getReceivableLitigationExpensesBalance(): BigDecimal.ZERO);
                });
            }
            litigationExpensesEndBalance = receivableLitigationExpensesBalance[0].add(receivableRentPay[0]).subtract(receivableRentRecover[0]).subtract(transgerCostAmount[0]);
            v.setLitigationExpensesBalance(receivableLitigationExpensesBalance[0]);
            v.setReceivableRentRecover(receivableRentRecover[0]);
            v.setLitigationExpensesPayTotal(litigationExpensesPayTotal[0]);
            v.setLitigationExpensesRecoverTotal(litigationExpensesRecoverTotal[0]);
            v.setReceivableRentPayTotal(receivableRentPayTotal[0]);
            v.setReceivableRentRecoverTotal(receivableRentRecoverTotal[0]);
            v.setLitigationExpensesEndBalance(litigationExpensesEndBalance);
            v.setReceivableRentPay(receivableRentPay[0]);
            v.setPayableUnpaid(payableUnpaid[0]);
            v.setTransgerCostAmount(transgerCostAmount[0]);
        });

    }
    public ContractEntity getLatestFinancialDateContract(List<ContractEntity> contractEntityList, CourtCostDetailsVO detailsVO){
        //过滤合同状态变更日期小于记账日期，取其中最大值的合同 暂时不这样做修改时间2023-11-21 改为取最新的那个合同状态
        Optional<ContractEntity> entityOptional = contractEntityList.stream().max(Comparator.comparing(ContractEntity::getFinancialContractStatusUpdateTime));
        return entityOptional.isPresent()?entityOptional.get():null;
    }

    private Map<String,String> getOrgIdOrgName(){
        Map<String,String> orgIdAndNameMap = Maps.newHashMap();
        List<OrgCompanyVO> orgCompanyVOList =  iOrgCompanyService.selectByCondition(new OrgCompanyQueryDTO());
        if (CollectionUtils.isNotEmpty(orgCompanyVOList)) {
            orgIdAndNameMap = orgCompanyVOList.stream().collect(Collectors.toMap(OrgCompanyVO::getOrgName,OrgCompanyVO::getOrgId, (k1,k2)->k2));
        }
        return orgIdAndNameMap;
    }

    private Map<String,String> getOrgNameOrgId(){
        Map<String,String> orgNameAndIdMap = Maps.newHashMap();
        List<OrgCompanyVO> orgCompanyVOList =  iOrgCompanyService.selectByCondition(new OrgCompanyQueryDTO());
        if (CollectionUtils.isNotEmpty(orgCompanyVOList)) {
            orgNameAndIdMap = orgCompanyVOList.stream().collect(Collectors.toMap(OrgCompanyVO::getOrgId,OrgCompanyVO::getOrgName, (k1,k2)->k2));
        }
        return orgNameAndIdMap;
    }

    public void batchDeleteVoucher(List<Long> ids){
        //获取所有的凭证Id
        List<CourtCostDetailsEntity> detailsEntityList = iCourtCostDetailsService.lambdaQuery().in(CourtCostDetailsEntity::getCourtCostId, ids).list();
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
        iCourtCostDetailsService.updateBatchById(detailsEntityList);
    }

}

