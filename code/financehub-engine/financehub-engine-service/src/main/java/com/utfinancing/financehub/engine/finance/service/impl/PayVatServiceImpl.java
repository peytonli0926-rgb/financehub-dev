package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.approve.model.dto.ApproveDTO;
import com.utfinancing.financehub.engine.approve.service.IApproveService;
import com.utfinancing.financehub.engine.constants.Constants;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.constant.ExceptionConstant;
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.mapper.ContractBalanceMapper;
import com.utfinancing.financehub.engine.finance.mapper.ContractMapper;
import com.utfinancing.financehub.engine.finance.mapper.InvoiceClaimMapper;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.*;
import com.utfinancing.financehub.engine.finance.mapper.PayVatMapper;
import com.utfinancing.financehub.engine.finance.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.hthx.common.enums.FinanceEngineEnum;
import com.utfinancing.financehub.engine.hthx.utils.HthxDateUtils;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import com.utfinancing.financehub.engine.rule.model.dto.DataExecutionTaskDTO;
import com.utfinancing.financehub.engine.rule.model.dto.ExecuteCommonDTO;
import com.utfinancing.financehub.engine.rule.model.vo.VoucherInfoVO;
import com.utfinancing.financehub.engine.rule.service.IDataExecutionTaskService;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import com.utfinancing.financehub.engine.scene.entity.TaxRateEntity;
import com.utfinancing.financehub.engine.scene.service.ITaxRateService;
import com.utfinancing.financehub.etl.api.InvoiceClaimFacade;
import com.utfinancing.financehub.etl.model.dto.ContractQueryInfoDTO;
import com.utfinancing.financehub.etl.model.vo.ContractInvoiceClaimVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @Author : bruyang
 * @Date : Create in 2024-03-20
 * @Description :  PayVat服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class PayVatServiceImpl extends ServiceImpl<PayVatMapper, PayVatEntity> implements IPayVatService {

    private final PayVatMapper payVatMapper;
    private final ContractMapper contractMapper;
    private final ContractBalanceMapper contractBalanceMapper;
    private final IRuleService iRuleService;
    private final IVoucherService iVoucherService;
    private final IInvoiceClaimService iInvoiceClaimService;
    private final ITaxRateService iTaxRateService;
    private final IDataExecutionTaskService iDataExecutionTaskService;
    private final IContractService iContractService;

    private final InvoiceClaimMapper invoiceClaimMapper;
    private final IOrgCompanyService iOrgCompanyService;

    @Value("${approve.url.payVat-url:null}")
    private String approveUrl;

    @Resource
    private IApproveService iApproveService;
    @Resource
    private InvoiceClaimFacade invoiceClaimFacade;

    @Autowired
    @Qualifier("asyncTaskExecutor")
    private ThreadPoolTaskExecutor asyncTaskExecutor;

    @Override
    public Long savePayVat(PayVatDTO dto) {
        PayVatEntity entity = BeanUtil.copyProperties(dto, PayVatEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updatePayVat(Long id, PayVatDTO dto) {
        PayVatEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public PayVatDTO getPayVatDTOById(Long id) {
        PayVatEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, PayVatDTO.class);
    }

    @Override
    public IPage<PayVatVO> selectPage(PayVatQueryDTO queryDTO) {
        Page page = new Page(queryDTO.getPageNum(), queryDTO.getPageSize());
        if ("1".equals(queryDTO.getInvoicingFlag())) {
            queryDTO.setInvoicingFlag("开票");
        } else if ("2".equals(queryDTO.getInvoicingFlag())) {
            queryDTO.setInvoicingFlag("计提");
        }
        IPage<PayVatVO> payVatVOIPage = payVatMapper.selectPageByMapper(page, queryDTO);
        List<PayVatVO> payVatVOList = payVatVOIPage.getRecords();
        // 封装数据
        // setPayVatDate(payVatVOList);
        return payVatVOIPage;
    }

    /**
     * 查询数据
     *
     * @param queryDTO
     * @return
     */
    @Override
    public List<PayVatVO> selectList(PayVatQueryDTO queryDTO) {
        List<PayVatVO> payVatVOList = payVatMapper.selectPageByMapper(queryDTO);
        // 封装数据
        // setPayVatDate(payVatVOList);
        return payVatVOList;
    }

    /**
     * 批量生成凭证
     *
     * @param idList
     * @param isSubmit
     * @return
     */
    @Override
    public Boolean generateVoucher(List<Long> idList, String isSubmit) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请至少选择一条数据生成凭证");
        }
        List<PayVatEntity> payVatEntityList = this.listByIds(idList);
        payVatEntityList.stream().forEach(v -> {
            if (!(ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus()) || ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus()))) {
                throw new ServiceException("处理状态为已录入或者已拒绝的才可以生成凭证");
            }
        });
        // 生成凭证前先删除之前的凭证
        batchDeleteVoucher(idList);

        PayVatQueryDTO queryDTO = new PayVatQueryDTO();
        queryDTO.setIdList(idList);
        List<PayVatVO> payVatVOList = selectList(queryDTO);
        List<Map<String, Object>> voucherMapList = Lists.newArrayList();
        payVatVOList.stream().forEach(v -> {
            ExecuteCommonDTO executeCommonDTO = new ExecuteCommonDTO();
            executeCommonDTO.setSystemCode(SystemEnum.CWZT.getCode());
            executeCommonDTO.setSystemName(SystemEnum.CWZT.getDesc());
            executeCommonDTO.setSceneCode(SceneEnum.WCTZ.getCode());
            executeCommonDTO.setSceneName(SceneEnum.WCTZ.name());
            executeCommonDTO.setOrderId(v.getId().toString());
            executeCommonDTO.setBusinessDate(new Date());
            executeCommonDTO.setContractCode(v.getContractCode());
            executeCommonDTO.setOrgId(v.getOrgId());
            executeCommonDTO.setAccountDate(new Date());
            executeCommonDTO.setClientCode(v.getClientCode());
            executeCommonDTO.setContractStatus(v.getContractStatus());
            executeCommonDTO.setBatchId(v.getId());
            executeCommonDTO.setBatchType(BatchTypeEnum.YJZZS.getCode());
            executeCommonDTO.setIsSubmit(isSubmit);

            Map<String, Object> dataMap = BeanUtil.beanToMap(executeCommonDTO);
            dataMap.put("contractType", v.getContractType());
            dataMap.put("receivableOuttaxBalance", v.getAccountBalance());
            dataMap.put("reportBalance", v.getReportBalance());

            voucherMapList.add(dataMap);
        });

        List<VoucherInfoVO> voucherResultList = iRuleService.batchExecuteRule(voucherMapList);
        Boolean isExistVoucherError = voucherResultList.stream().allMatch(v -> StringUtils.isNotEmpty(v.getErrorInfo()));
        if (YesOrNoEnum.YES.getCode().equals(isSubmit) && isExistVoucherError) {
            // 异步删除已生成的凭证
            List<Long> voucherIdList = Lists.newArrayList();
            voucherResultList.stream().forEach(voucherInfoVO -> {
                if (CollectionUtils.isNotEmpty(voucherInfoVO.getVoucherDTOList())) {
                    voucherIdList.addAll(voucherInfoVO.getVoucherDTOList().stream().map(VoucherDTO::getId).collect(Collectors.toList()));
                }
            });
            asnyDeleteVoucher(voucherIdList);
            return Boolean.FALSE;
        }
        for (VoucherInfoVO infoVO : voucherResultList) {
            String voucherIds = "";
            String errorInfo = "";
            String isGenerateVoucher = YesOrNoEnum.YES.getCode();
            if (StringUtils.isNotEmpty(infoVO.getErrorInfo())) {
                errorInfo = infoVO.getErrorInfo();
                if (infoVO.getErrorInfo().length() > 2000) {
                    errorInfo = infoVO.getErrorInfo().substring(0, 2000);
                }
                isGenerateVoucher = YesOrNoEnum.NO.getCode();
            }
            if (CollectionUtils.isNotEmpty(infoVO.getVoucherDTOList())) {
                voucherIds = infoVO.getVoucherDTOList().stream().map(VoucherDTO::getId).map(String::valueOf).collect(Collectors.toList()).stream().collect(Collectors.joining(","));
            }
            this.lambdaUpdate().set(PayVatEntity::getVoucherId, voucherIds)
                    .set(PayVatEntity::getErrorInfo, errorInfo)
                    .set(PayVatEntity::getAccountDate, LocalDateTime.now())
                    .set(PayVatEntity::getIsGenerateVoucher, isGenerateVoucher)
                    .eq(PayVatEntity::getId, Long.parseLong(infoVO.getOrderId()))
                    .update();
        }
        return Boolean.TRUE;
    }

    /**
     * 提交
     *
     * @param idList
     * @return
     */
    @Override
    public Boolean submit(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请至少勾选一条数据提交");
        }
        List<PayVatEntity> payVatEntityList = this.listByIds(idList);
        List<ApproveDTO> approveDTOList = Lists.newArrayList();
        payVatEntityList.stream().forEach(v -> {
            if (!(ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus()) || ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus()))) {
                throw new ServiceException("只有处理状态为已录入或者已拒绝的才可以提交");
            }
            if (ObjectUtil.equal(v.getIsGenerateVoucher(), YesOrNoEnum.NO.getCode())) {
                throw new ServiceException("生成凭证后才可以提交");
            }
            ApproveDTO approveDTO = new ApproveDTO();
            approveDTO.setDocumentId(v.getId());
            approveDTO.setDocumentType(BatchTypeEnum.YJZZS.getCode());
            approveDTO.setUrl(approveUrl + v.getId());
            approveDTOList.add(approveDTO);
        });

        // 生成凭证
        Boolean generateVoucherFlag = generateVoucher(idList, YesOrNoEnum.YES.getCode());
        if (generateVoucherFlag) {
            // 发送审核
            Map<Long, Long> processInstantIdMap = iApproveService.submit(approveDTOList);

            List<PayVatEntity> newEntityList = this.listByIds(idList);
            newEntityList.stream().forEach(v -> {
                v.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
                if (null != processInstantIdMap && processInstantIdMap.containsKey(v.getId())) {
                    v.setProcessInstanceId(processInstantIdMap.get(v.getId()));
                }
            });
            // 凭证生成成功
            return this.updateBatchById(newEntityList);
        } else {
            // 凭证生成失败
            return Boolean.FALSE;
        }
    }

    /**
     * 撤回
     *
     * @param idList
     * @return
     */
    @Override
    public Boolean withdraw(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请至少勾选一条数据撤回");
        }
        List<PayVatEntity> payVatEntityList = this.listByIds(idList);
        payVatEntityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.SUBMITTED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("只有处理状态为已提交的才可以撤回");
            }
            v.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
        });
        iApproveService.withdraw(payVatEntityList.stream().map(PayVatEntity::getProcessInstanceId).collect(Collectors.toList()));
        return this.updateBatchById(payVatEntityList);
    }

    /**
     * 修改备注
     *
     * @param dto
     * @return
     */
    @Override
    public void updateComments(InvoiceClaimDTO dto) {
        if (ObjectUtil.isEmpty(dto)) {
            throw new ServiceException("无有效内容");
        }

        InvoiceClaimEntity invoiceClaimEntity = iInvoiceClaimService.getById(dto.getId());
        if (ObjectUtil.isEmpty(invoiceClaimEntity)) {
            throw new ServiceException("根据id[" + dto.getId() + "]未查询到数据");
        }
        iInvoiceClaimService.lambdaUpdate()
                .set(InvoiceClaimEntity::getComments, dto.getComments())
                .eq(InvoiceClaimEntity::getId, dto.getId())
                .update();
    }

    /**
     * 删除凭证
     *
     * @param idList
     */
    @Override
    public void batchDeleteVoucher(List<Long> idList) {
        // 获取所有的凭证Id
        List<PayVatEntity> payVatEntityList = this.listByIds(idList);
        // 逗号拆分
        List<Long> voucherIdList = Lists.newArrayList();
        payVatEntityList.stream().forEach(v -> {
            if (StringUtils.isNotEmpty(v.getVoucherId())) {
                voucherIdList.addAll(Arrays.stream(v.getVoucherId().split(",")).map(Long::parseLong).collect(Collectors.toList()));
            }
        });
        if (CollectionUtils.isNotEmpty(voucherIdList)) {
            iVoucherService.deleteByIdList(voucherIdList);
        }
    }

    /**
     * 异步删除凭证
     *
     * @param voucherIdList
     */
    public void asnyDeleteVoucher(List<Long> voucherIdList) {
        if (CollectionUtils.isEmpty(voucherIdList)) {
            return;
        }
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            // 异步任务的代码
            iVoucherService.deleteByIdList(voucherIdList);
        });
    }


    /**
     * @description:增值税-应交增值税对账-数据同步-获取合同数据放入应交增值税表
     **/
    @Override
    public String payVatGetContract(Integer period) {
        // 更新报表金额
        // 创建任务，防止同时刷新数据
        Long taskId = createTask();
        // 异步生成凭证
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            Integer number = getData(period);
            return number;
        }).whenComplete((v, e) -> {
            // 执行成功，更新任务状态
            iDataExecutionTaskService.finishedTask(taskId, DataExecutionTaskStatusEnum.SUCCESS.getCode(), v, 0);
        }).exceptionally(e -> {
            log.info("应交增值税更新数据 异步执行异常：", e);
            iDataExecutionTaskService.errorTask(taskId, DataExecutionTaskStatusEnum.FAILED.getCode(), 0, 0, e.getMessage());
            // 执行失败
            return null;
        });

        return "正在更新数据，请稍后查看结果";
    }

    private Integer getData(Integer period) {
        // 查询税率
        List<TaxRateEntity> taxRateEntityList = iTaxRateService.list(new LambdaQueryWrapper<TaxRateEntity>().eq(TaxRateEntity::getBusinessCode, BusinessEnum.ZLYW.getCode()));
        PayVatTaxRateDTO taxRateDTO = getPayVatTaxRateDTO(taxRateEntityList);
        // 1。查询数据
        /******modify by zhangli.chen for 新增报表余额查询日期，查询月份为当月则指定日期为当天，查询月份不为当月则指定日期为查询月份的最后一天 on 20250805 ******/
        String reportDate = null;
        if(period!=null){
            // 判断查询月份与当前日期是否是同一个月
            boolean isSameMonth = StringUtils.equals(String.valueOf(period.intValue()), DateUtil.format(DateUtil.date(),
                    HthxDateUtils.YYYYMM));
            if(isSameMonth){
                reportDate = DateUtil.format(DateUtil.toLocalDateTime(new Date()), HthxDateUtils.YYYY_MM_DD);
            }else{
                reportDate = DateUtil.format(DateUtil.endOfDay(DateUtil.endOfMonth(DateUtil.parse(String.valueOf(period.intValue()),
                        HthxDateUtils.YYYYMM))), HthxDateUtils.YYYY_MM_DD);
            }
        }
        if(StringUtils.isEmpty(reportDate)){
            reportDate = DateUtil.format(DateUtil.toLocalDateTime(new Date()),"yyyy-MM-dd");
        }
        log.info("====>>PayVatServiceImpl.getData==>>00==>>taxRateDTO:{},period:{},reportDate:{}",taxRateDTO,period,reportDate);
        List<PayVatDTO> payVatDTOList = payVatMapper.payVatGetContract(taxRateDTO,period,reportDate);
        if (CollectionUtils.isEmpty(payVatDTOList)) {
            return 0;
        }
        List<CompletableFuture<Void>> completableFutures = new ArrayList<>();
//        R<List<ContractInvoiceClaimVO>> invoiceClaimR = invoiceClaimFacade.selectContractInvoiceList(new ContractQueryInfoDTO());
//        log.info("====>>PayVatServiceImpl.getData==05==>>invoiceClaimR==null:{}",invoiceClaimR==null);
//        List<ContractInvoiceClaimVO> invoiceClaimVOList = invoiceClaimR.getData();
        // modify by zhangli.chen for 调整为查询engine服务获取合同开票认领数据 on 20250619
        List<ContractInvoiceClaimVO> invoiceClaimVOList = getContractInvoiceClaimDataForSyncData(new ContractQueryInfoDTO());
        if(invoiceClaimVOList==null){
            invoiceClaimVOList = new ArrayList<>();
        }
        // 清空数据，然后重新插入数据
        remove(new LambdaQueryWrapper<PayVatEntity>());
        // 分组
        int chunkSize = 1000;
        for (int i = 0; i < payVatDTOList.size(); i += chunkSize) {
            int end = Math.min(i + chunkSize, payVatDTOList.size());
            List<PayVatDTO> chunk = payVatDTOList.subList(i, end);
            List<Long> contractIds = chunk.stream().map(PayVatDTO::getContractId).collect(Collectors.toList());
            List<ContractEntity> filteredContractList = iContractService.listByIds(contractIds);
            List<ContractInvoiceClaimVO> filteredInvoiceClaimList = invoiceClaimVOList.stream()
                    .filter(item -> filteredContractList.stream()
                            .anyMatch(criterion -> item != null && criterion != null
                                    && StringUtils.equals(item.getContractCode(), criterion.getContractCode())
                                    && StringUtils.equals(item.getOrgId(), criterion.getOrgId())))
                    .collect(Collectors.toList());
            CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
                // 在这里处理每一组数据
                calculatePayVat(chunk, taxRateEntityList, filteredInvoiceClaimList);
                return null;
            }, asyncTaskExecutor);
            completableFutures.add(future);
        }
        // 将所有CompletableFuture组合成一个新的CompletableFuture，并等待所有线程任务完成
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0]));
        // 等待所有线程任务完成
        allFutures.join();
        return payVatDTOList.size();
    }

    private void calculatePayVat(List<PayVatDTO> payVatDTOList, List<TaxRateEntity> taxRateEntityList,
                                 List<ContractInvoiceClaimVO> filteredInvoiceClaimList) {
        //        增值税报表字段取值修改：【应开票税额】取合同表【应开票/计提税额】，【已开票金额】取invoice表，【已计提金额】取计提表
        //        tax_payable tax_accrued tax_invoiced
        List<Long> payVatIdList = payVatDTOList.stream().filter(a -> ObjectUtil.isNotEmpty(a.getId())).map(PayVatDTO::getId).collect(Collectors.toList());
        List<ContractBalanceLatestVO> contractBalanceLatestVOList = new ArrayList<>();
        // modify by zhangli.chen for 支持分批次in查询，避免in查询参数超长 on 20250619
        if (payVatIdList != null && !payVatIdList.isEmpty()) {
            List<List<Long>> partitions = Lists.partition(payVatIdList, FinanceEngineEnum.Numbers.THOUSAND.getKey());
            for (List<Long> batchIds : partitions) {
                List<ContractBalanceLatestVO> partitionVOList =
                        payVatMapper.selectContractBalanceLatestByContractCodeAndOrgId(batchIds);
                contractBalanceLatestVOList.addAll(partitionVOList);
            }
        }
        // 2.计算金额
        payVatDTOList.stream().forEach(a -> {
            // 取税率表的数据，business_code=ZLYW,fund_type=tax_general,lease_type =租赁类型
            BigDecimal taxRate = taxRateEntityList.stream().filter(b -> ObjectUtil.equals(a.getLeaseType(), b.getLeaseType())
                    && ObjectUtil.equals(b.getFundType(), TaxFundTypeEnum.TAX_GENERAL.getCode())
            ).map(TaxRateEntity::getTaxRate).findFirst().orElse(BigDecimal.ZERO);
            a.setTaxRate(taxRate);
            a.setProcessStatus(ProcessStatusEnum.NOT_ENTERED.getCode());

            StringBuffer exception = new StringBuffer();

//            ContractQueryInfoDTO queryDTO = new ContractQueryInfoDTO();
//            queryDTO.setId(a.getContractId());
//            R<List<ContractInvoiceClaimVO>> invoiceClaimR = invoiceClaimFacade.selectContractInvoiceList(queryDTO);
            if (ObjectUtil.equals(a.getSpecialFlag(), "邮储")) {
                // 已开票税额，若合同的特殊标识（合同表的special_flag）等于“邮储”时，取值改为=单合同增值税查询页的 “实际开票/计提税额”汇总-“开票/计提项目”为手续费时的“实际开票/计提税额”；
                // 先设为0,
                a.setTaxInvoiced(BigDecimal.ZERO);
            }

//            if (invoiceClaimR.getData() != null && !invoiceClaimR.getData().isEmpty()) {
            if (CollectionUtils.isNotEmpty(filteredInvoiceClaimList)) {
//                List<ContractInvoiceClaimVO> invoiceClaimVOList = invoiceClaimR.getData();
                BigDecimal shouldTaxBalance = filteredInvoiceClaimList.stream().map(ContractInvoiceClaimVO::getBackTaxValue).reduce(BigDecimal.ZERO, BigDecimal::add);
                a.setShouldTaxBalance(shouldTaxBalance);
                String exceptionStr = filteredInvoiceClaimList.stream().map(ContractInvoiceClaimVO::getExceptionType).collect(Collectors.toSet()).stream().collect(Collectors.joining());
                exception.append(exceptionStr);
                if (ObjectUtil.equals(a.getSpecialFlag(), "邮储")) {
                    // 已开票税额，若合同的特殊标识（合同表的special_flag）等于“邮储”时，取值改为=单合同增值税查询页的 “实际开票/计提税额”汇总-“开票/计提项目”为手续费时的“实际开票/计提税额”；
                    BigDecimal taxValueTotal = filteredInvoiceClaimList.stream().map(ContractInvoiceClaimVO::getTaxValue).reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal sxfTaxValueTotal = filteredInvoiceClaimList.stream().filter(b -> ObjectUtil.equals(b.getProductName(), "手续费")).map(ContractInvoiceClaimVO::getTaxValue).reduce(BigDecimal.ZERO, BigDecimal::add);
                    a.setTaxInvoiced(NumberUtil.sub(taxValueTotal, sxfTaxValueTotal));
                }
            }

            // 此模块异常类型如T5有六种
            // 1、2：单合同增值税查询页该合同只要存在异常都需要在这里提示；
            // 3.当合同类型为租赁时，按合同查最新余额表该合同receivable_outtax_balance!=0的数据查出签约主体数量>=2时需提示；
            // 合同类型为服务费时，按合同查最新余额表receivable_service_outtax_balance!=0的数据查出签约主体数量>=2时需提示；
            if (StringUtils.equals(a.getContractType(), "租赁")) {
                if (contractBalanceLatestVOList.stream().filter(o -> StringUtils.equals(a.getContractCode(), o.getContractCode()) && BigDecimal.ZERO.compareTo(o.getReceivableOuttaxBalance()) != 0)
                        .map(ContractBalanceLatestVO::getOrgId).collect(Collectors.toSet()).size() > 1) {
                    exception.append(ExceptionConstant.EXCEPTION_3);
                }
            } else if (StringUtils.equals(a.getContractType(), "服务费")) {
                if (contractBalanceLatestVOList.stream().filter(o -> StringUtils.equals(a.getContractCode(), o.getContractCode()) && BigDecimal.ZERO.compareTo(o.getReceivableServiceOuttaxBalance()) != 0)
                        .map(ContractBalanceLatestVO::getOrgId).collect(Collectors.toSet()).size() > 1) {
                    exception.append(ExceptionConstant.EXCEPTION_3);
                }
            }
            // 4.财务合同状态为某些值且科目余额不为0时需提示；
            if (FinancialContractStatusForExceptionEnum.containsValue(a.getFinancialContractStatus()) && BigDecimal.ZERO.compareTo(NumberUtil.toBigDecimal(a.getAccountBalance())) != 0) {
                exception.append(ExceptionConstant.EXCEPTION_4);
            }

            // 5.实际剩余!=科目余额时需提示；
//            if (!NumberUtil.equals(NumberUtil.toBigDecimal(a.getActualTaxBalance()), NumberUtil.toBigDecimal(a.getAccountBalance()))) {
//                exception.append(ExceptionConstant.EXCEPTION_5);
//            }
            // 6.科目余额!=报表余额时需提示
//            if (!NumberUtil.equals(NumberUtil.toBigDecimal(a.getAccountBalance()), NumberUtil.toBigDecimal(a.getReportBalance()))) {
//                exception.append(ExceptionConstant.EXCEPTION_6);
//            }
            a.setExceptionType(exception.toString());
        });
        // 3.保存数据
        saveBatch(BeanUtil.copyToList(payVatDTOList, PayVatEntity.class));
    }

    /**
     * 创建任务
     *
     * @return
     */
    private Long createTask() {
        String systemCode = BatchTypeEnum.YJZZS.getCode();
        // 查询是否有正在进行的任务
        DataExecutionTaskDTO taskDTO = iDataExecutionTaskService.getRunningTaskBySystem(systemCode);
        if (taskDTO != null) {
            throw new ServiceException("存在正在运行的任务(任务开始时间" + taskDTO.getTaskStartTime() + ")，请稍后再试");
        }
        // 生成任务
        Long taskId = iDataExecutionTaskService.createNewTask(systemCode, DataExecutionTaskStatusEnum.RUNNING.getCode(), 1, null, null);
        return taskId;
    }

    /**
     * 获取查询税率
     *
     * @param taxRateEntityList
     * @return
     */
    private PayVatTaxRateDTO getPayVatTaxRateDTO(List<TaxRateEntity> taxRateEntityList) {
        PayVatTaxRateDTO taxRateDTO = new PayVatTaxRateDTO();
        BigDecimal hzGeneralTaxRate = NumberUtil.div(taxRateEntityList.stream().filter(b -> ObjectUtil.equals(LeaseTypeEnum.LEASEBACK.getCode(), b.getLeaseType())
                && ObjectUtil.equals(b.getFundType(), TaxFundTypeEnum.TAX_GENERAL.getCode())
        ).map(TaxRateEntity::getTaxRate).findFirst().orElse(BigDecimal.ZERO), 100, 2);
        taxRateDTO.setHzGeneralTaxRate(hzGeneralTaxRate);
        taxRateDTO.setHzCommissionTaxRate(NumberUtil.div(taxRateEntityList.stream().filter(b -> ObjectUtil.equals(LeaseTypeEnum.LEASEBACK.getCode(), b.getLeaseType())
                && ObjectUtil.equals(b.getFundType(), TaxFundTypeEnum.RECEIVABLE_COMMISSION.getCode())
        ).map(TaxRateEntity::getTaxRate).findFirst().orElse(hzGeneralTaxRate), 100, 2));
        taxRateDTO.setHzInsuranceTaxRate(NumberUtil.div(taxRateEntityList.stream().filter(b -> ObjectUtil.equals(LeaseTypeEnum.LEASEBACK.getCode(), b.getLeaseType())
                && ObjectUtil.equals(b.getFundType(), TaxFundTypeEnum.RECEIVABLE_INSURANCE.getCode())
        ).map(TaxRateEntity::getTaxRate).findFirst().orElse(hzGeneralTaxRate), 100, 2));

        BigDecimal zzGeneralTaxRate = NumberUtil.div(taxRateEntityList.stream().filter(b -> ObjectUtil.equals(LeaseTypeEnum.DIRECT.getCode(), b.getLeaseType())
                && ObjectUtil.equals(b.getFundType(), TaxFundTypeEnum.TAX_GENERAL.getCode())
        ).map(TaxRateEntity::getTaxRate).findFirst().orElse(BigDecimal.ZERO), 100, 2);
        taxRateDTO.setZzGeneralTaxRate(zzGeneralTaxRate);
        taxRateDTO.setZzCommissionTaxRate(NumberUtil.div(taxRateEntityList.stream().filter(b -> ObjectUtil.equals(LeaseTypeEnum.DIRECT.getCode(), b.getLeaseType())
                && ObjectUtil.equals(b.getFundType(), TaxFundTypeEnum.RECEIVABLE_COMMISSION.getCode())
        ).map(TaxRateEntity::getTaxRate).findFirst().orElse(zzGeneralTaxRate), 100, 2));
        taxRateDTO.setZzInsuranceTaxRate(NumberUtil.div(taxRateEntityList.stream().filter(b -> ObjectUtil.equals(LeaseTypeEnum.DIRECT.getCode(), b.getLeaseType())
                && ObjectUtil.equals(b.getFundType(), TaxFundTypeEnum.RECEIVABLE_INSURANCE.getCode())
        ).map(TaxRateEntity::getTaxRate).findFirst().orElse(zzGeneralTaxRate), 100, 2));

        return taxRateDTO;
    }


    /**
     * 审批修改单据状态
     *
     * @param approveDTO
     */
    @Override
    public void updateProcessStatus(CommonApproveDTO approveDTO) {
        if (StringUtils.isEmpty(approveDTO.getDocumentStatus())) {
            throw new ServiceException("处理状态不可以为空");
        }
        PayVatEntity entity = this.getById(approveDTO.getDocumentId());
        if (ObjectUtil.isEmpty(entity)) {
            throw new ServiceException("应交增值税数据不存在");
        }
        // 修改凭证状态，通过和驳回都修改
        updateVoucherStatus(Lists.newArrayList(approveDTO.getDocumentId()), approveDTO);
        // 通过，直接修改状态
        entity.setProcessStatus(approveDTO.getDocumentStatus());
        this.updateById(entity);
    }

    /**
     * 更新凭证状态
     *
     * @param ids
     * @param approveDTO
     */
    public void updateVoucherStatus(List<Long> ids, CommonApproveDTO approveDTO) {
        // 获取所有的凭证Id
        List<PayVatEntity> entityList = this.listByIds(ids);
        // 逗号拆分
        List<String> voucherIdList = Lists.newArrayList();
        entityList.stream().forEach(v -> {
            if (StringUtils.isNotEmpty(v.getVoucherId())) {
                voucherIdList.addAll(Arrays.stream(v.getVoucherId().split(",")).collect(Collectors.toList()));
            }
        });
        iVoucherService.updateStatusByids(voucherIdList, approveDTO.getDocumentStatus(),
                approveDTO.getApproverNum(), approveDTO.getApproverName());
    }


    /**
     * @description: 增值税-应交增值税对账-数据同步-获取合同开票认领数据
     * @author: zhangli.chen
     **/
    List<ContractInvoiceClaimVO> getContractInvoiceClaimDataForSyncData(ContractQueryInfoDTO queryDTO){
        InvoiceClaimQueryDTO params = new InvoiceClaimQueryDTO();
        List<ContractInvoiceClaimVO> invoiceClaimVOList = invoiceClaimMapper.queryContractInvoiceClaimForSyncData(params);
        setInvoiceClaimDataForSyncData(invoiceClaimVOList);
        return invoiceClaimVOList;
    }

    /**
     * @description: 增值税-应交增值税对账-数据同步-获取合同开票认领数据-设置相关值
     * @author: zhangli.chen
     **/
    public void setInvoiceClaimDataForSyncData(List<ContractInvoiceClaimVO> invoiceClaimVOList) {
        // 机构列表
        Map<String,String> finalOrgIdMap = iOrgCompanyService.list().stream().collect(HashMap::new,(h, o)->
                h.put(o.getOrgId(),o.getOrgName()),HashMap::putAll);
        invoiceClaimVOList.stream().forEach(v -> {
            //  租赁类型是回租，应开票金额取应收利息，其他的取应收租金
            if ("回租".equals(v.getLeaseType())) {
                v.setBackTaxAmount(v.getInterestAmount());
            } else {
                v.setBackTaxAmount(v.getRentReceivableAmount());
            }
            // 获取开票税率 除100 税率*100
            if (StringUtils.isNotEmpty(v.getTaxRate())) {
                v.setTaxRate((new BigDecimal(v.getTaxRate()).multiply(new BigDecimal(100)).setScale(0, RoundingMode.HALF_UP)).toString());
            }
            // backTaxValue应开票税额=应开票金额/(1+税率)*税率
            BigDecimal backTaxValue = null == v.getBackTaxAmount() ? BigDecimal.ZERO : v.getBackTaxAmount();
            //开票税率
            BigDecimal backTaxRate = null == v.getBackTaxRate() ? BigDecimal.ZERO : v.getBackTaxRate().divide(new BigDecimal("100").setScale(2, RoundingMode.HALF_UP));
            if (BigDecimal.ZERO.compareTo(backTaxRate)!=0) {
                BigDecimal backTaxValueBigdecimal = backTaxValue.divide((new BigDecimal("1").add(backTaxRate)).multiply(backTaxRate));
                v.setBackTaxValue(backTaxValueBigdecimal.setScale(2, RoundingMode.HALF_UP));
            }
            v.setPrincipalAmount(null==v.getPrincipalAmount()?BigDecimal.ZERO:v.getPrincipalAmount());
            v.setTaxAmount(null==v.getTaxAmount()?BigDecimal.ZERO:v.getTaxAmount());
            v.setBackTaxRate(null==v.getBackTaxRate()?BigDecimal.ZERO:v.getBackTaxRate());
            v.setTaxRate(null==v.getTaxRate()?"0":v.getTaxRate());
            v.setBackTaxAmount(null==v.getBackTaxAmount()?BigDecimal.ZERO:v.getBackTaxAmount());
            // 设置机构名称
            if (StringUtils.isNotEmpty(v.getOrgId())) {
                v.setOrgIdName(finalOrgIdMap.get(v.getOrgId()));
            }
            //  异常类型1.开票主体、开票金额、开票税率、开票对象不一致 2.累计已开金额大于已收款金额
            String exceptionType = "";
            if (StringUtils.isEmpty(v.getOrgId())||!v.getOrgId().equals(v.getSellerTaxCode())
                    || v.getPrincipalAmount().compareTo(v.getTaxAmount())!=0
                    || v.getBackTaxRate().compareTo(new BigDecimal(v.getTaxRate()))!=0
                    || !v.getClientName().equals(v.getPayer())) {
                exceptionType = Constants.INVOICE_ERROR_COMMENT;
            }
            if (v.getBackTaxAmount().compareTo(v.getTaxAmount())!=0) {
                exceptionType = exceptionType + Constants.INVOICE_AMOUNT_ERROR_COMMENT;
            }
            v.setExceptionType(exceptionType);
        });
    }


}

