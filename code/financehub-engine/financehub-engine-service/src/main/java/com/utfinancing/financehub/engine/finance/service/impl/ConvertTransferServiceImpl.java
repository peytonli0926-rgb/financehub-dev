package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.utfinancing.financehub.engine.approve.model.dto.ApproveDTO;
import com.utfinancing.financehub.engine.approve.service.IApproveService;
import com.utfinancing.financehub.engine.enums.ProcessStatusEnum;
import com.utfinancing.financehub.engine.enums.SceneEnum;
import com.utfinancing.financehub.engine.enums.SystemEnum;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.entity.ContractEntity;
import com.utfinancing.financehub.engine.finance.entity.ContractStatusRecordEntity;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferDetailEntity;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferEntity;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferPlanEntity;
import com.utfinancing.financehub.engine.finance.mapper.ContractBalanceMapper;
import com.utfinancing.financehub.engine.finance.mapper.ConvertTransferMapper;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferDetailExcelDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferExcelDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferPlanExcelDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.TransferContractBalanceQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ContractBalanceVO;
import com.utfinancing.financehub.engine.finance.model.vo.ContractVO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferVO;
import com.utfinancing.financehub.engine.finance.model.vo.OrgCompanyVO;
import com.utfinancing.financehub.engine.finance.model.vo.RepaymentPlanVO;
import com.utfinancing.financehub.engine.finance.service.IContractBalanceService;
import com.utfinancing.financehub.engine.finance.service.IContractService;
import com.utfinancing.financehub.engine.finance.service.IContractStatusRecordService;
import com.utfinancing.financehub.engine.finance.service.IConvertTransferDetailService;
import com.utfinancing.financehub.engine.finance.service.IConvertTransferPlanService;
import com.utfinancing.financehub.engine.finance.service.IConvertTransferService;
import com.utfinancing.financehub.engine.finance.service.IOrgCompanyService;
import com.utfinancing.financehub.engine.finance.service.IRepaymentPlanService;
import com.utfinancing.financehub.engine.finance.service.IVoucherService;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import com.utfinancing.financehub.engine.rule.model.dto.ExecuteCommonDTO;
import com.utfinancing.financehub.engine.rule.model.vo.VoucherInfoVO;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.rmi.ServerException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-22
 * @Description :  ConvertTransfer服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class ConvertTransferServiceImpl extends ServiceImpl<ConvertTransferMapper, ConvertTransferEntity> implements IConvertTransferService {

    private final IOrgCompanyService iOrgCompanyService;
    private final IContractService iContractService;
    private final IContractBalanceService iContractBalanceService;
    private final IConvertTransferDetailService iConvertTransferDetailService;
    private final IConvertTransferPlanService iConvertTransferPlanService;
    private final IRepaymentPlanService iRepaymentPlanService;
    private final IVoucherService iVoucherService;
    private final IRuleService iRuleService;
    private final AsyncTaskExecutor asyncTaskExecutor;
    private final IApproveService approveService;
    private final IContractStatusRecordService contractStatusRecordService;

    @Value("${approve.url.convert-transfer-url:null}")
    private String approveUrl;

    @Override
    public Boolean updateProcessStatus(CommonApproveDTO approveDTO) {
        if (StringUtils.isEmpty(approveDTO.getDocumentStatus())) {
            throw new ServiceException("处理状态不可以为空");
        }
        ConvertTransferEntity transfer = getById(approveDTO.getDocumentId());
        if (null == transfer) {
            throw new ServiceException("平价转让数据不存在");
        }
        String processStatus = transfer.getProcessStatus();
        try {
            if (ProcessStatusEnum.REVIEWED.getCode().equals(approveDTO.getDocumentStatus())) {
                processStatus = ProcessStatusEnum.REVIEWED.getCode();
            } else if (ProcessStatusEnum.REJECTED.getCode().equals(approveDTO.getDocumentStatus())) {
                processStatus = ProcessStatusEnum.REJECTED.getCode();
                clearContractStatusRecord(transfer);
            }
            // 修改凭证状态，通过和驳回都修改
            // 逗号拆分
            if (com.utfinancing.financehub.common.core.utils.StringUtils.isNotEmpty(transfer.getVoucherId())) {
                iVoucherService.updateStatusByids(Arrays.asList(transfer.getVoucherId().split(",")), approveDTO.getDocumentStatus(), approveDTO.getApproverNum(), approveDTO.getApproverName());
            }
            transfer.setProcessStatus(processStatus);
            transfer.setErrorInfo("");
        } catch (Exception e) {
            transfer.setErrorInfo(e.getMessage());
        }
        return this.updateById(transfer);
    }


    @Override
    public IPage<ConvertTransferVO> selectPage(ConvertTransferQueryDTO queryDTO) {
        LambdaQueryWrapper<ConvertTransferEntity> queryWrapper = Wrappers.<ConvertTransferEntity>lambdaQuery();
        // 这里注入查询条件
        setQueryCondition(queryDTO, queryWrapper);
        IPage<ConvertTransferEntity> entityIPage = baseMapper.selectPage(new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, ConvertTransferVO.class);
    }

    @Override
    public List<ConvertTransferExcelVO> selectList(ConvertTransferQueryDTO queryDTO) {
        LambdaQueryWrapper<ConvertTransferEntity> queryWrapper = Wrappers.<ConvertTransferEntity>lambdaQuery();
        // 这里注入查询条件
        setQueryCondition(queryDTO, queryWrapper);
        List<ConvertTransferEntity> entityList = baseMapper.selectList(queryWrapper);
        return ListBeanUtil.copyList(entityList, ConvertTransferExcelVO.class);
    }

    @Override
    @Transactional
    public Boolean importFile(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        InputStream inputStream2 = file.getInputStream();
        InputStream inputStream3 = file.getInputStream();
        try {
            ExcelUtil<ConvertTransferExcelDTO> util = new ExcelUtil<>(ConvertTransferExcelDTO.class);
            ExcelUtil<ConvertTransferDetailExcelDTO> util2 = new ExcelUtil<>(ConvertTransferDetailExcelDTO.class);
            ExcelUtil<ConvertTransferPlanExcelDTO> util3 = new ExcelUtil<>(ConvertTransferPlanExcelDTO.class);
            List<ConvertTransferExcelDTO> transferExcelDTOList = util.importExcel("折价转让基本信息", inputStream, 0);
            List<ConvertTransferDetailExcelDTO> transferDetailExcelDTOList = util2.importExcel("折价转让详情", inputStream2, 0);
            List<ConvertTransferPlanExcelDTO> planExcelDTOList = util3.importExcel("折价转让租金计划", inputStream3, 0);
            checkData(transferExcelDTOList, transferDetailExcelDTOList, planExcelDTOList);
            List<ConvertTransferEntity> convertTransferEntityList = BeanUtil.copyToList(transferExcelDTOList, ConvertTransferEntity.class);
            // 填充余额表信息
            for (ConvertTransferEntity v : convertTransferEntityList) {
                v.setBusinessDate(LocalDateTime.now());
                v.setAccountDate(v.getTradeDate());// 交易日就是记账日期
                v.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
                v.setFinanceDate(v.getTradeDate());
                this.save(v);
                List<ConvertTransferDetailEntity> detailEntityList = BeanUtil.copyToList(transferDetailExcelDTOList, ConvertTransferDetailEntity.class);
                // 保存详情信息
                saveDetail(detailEntityList, v);
                List<ConvertTransferPlanEntity> planEntityList = BeanUtil.copyToList(planExcelDTOList, ConvertTransferPlanEntity.class);
                // 保存租金计划
                savePlan(planEntityList, v);
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(inputStream2);
        }
    }

    /**
     * 保存租金计划
     */
    private void savePlan(List<ConvertTransferPlanEntity> planEntityList, ConvertTransferEntity entity) {
        List<ConvertTransferPlanEntity> list = Lists.newArrayList();
        List<String> contractCodeList = planEntityList.stream().map(ConvertTransferPlanEntity::getOldContractCode).collect(Collectors.toList());
        Map<String, ContractEntity> orgContractCodeMap = iContractService.lambdaQuery()
                .in(ContractEntity::getContractCode, contractCodeList)
                .eq(ContractEntity::getOrgId, entity.getTransferParty())
                .list()
                .stream()
                .collect(Collectors.toMap(ContractEntity::getContractCode, Function.identity()));
        for (ConvertTransferPlanEntity transferPlan : planEntityList) {
            transferPlan.setConvertTransferId(entity.getId());
            transferPlan.setBatch(entity.getBatch());
            if (ObjectUtil.isEmpty(transferPlan.getPlanDate())) {
                // 未上传租金计划，查询原合同的偿还计划
                ContractEntity contractEntity = orgContractCodeMap.get(transferPlan.getOldContractCode());
                List<RepaymentPlanVO> repaymentPlanVOS = iRepaymentPlanService.selectByContractCode(transferPlan.getOldContractCode());
                if (CollectionUtil.isEmpty(repaymentPlanVOS)) {
                    throw new ServiceException("合同编号【" + transferPlan.getNewContractCode() + "】租金计划未上传，且原合同编号【" + transferPlan.getOldContractCode() + "】未查询到租金计划");
                }
                for (RepaymentPlanVO planVO : repaymentPlanVOS) {
                    ConvertTransferPlanEntity planEntity = BeanUtil.copyProperties(transferPlan, ConvertTransferPlanEntity.class);
                    planEntity.setConvertTransferId(entity.getId());
                    planEntity.setBatch(entity.getBatch());
                    planEntity.setPlanDate(DateUtil.toLocalDateTime(planVO.getPlanDate()));
                    planEntity.setPeriods(planVO.getPeriods());
                    planEntity.setReceivableRent(planVO.getRentAmount());
                    planEntity.setReceivablePrincipal(planVO.getPrincipalAmount());
                    planEntity.setReceivableInterest(planVO.getInterestAmount());
                    planEntity.setReceivableEndingSalvage(contractEntity.getRetainedPrice());// 应收期末残值 取合同的名义留购价
                    list.add(planEntity);
                }
            } else {
                list.add(transferPlan);
            }
        }
        iConvertTransferPlanService.saveBatch(list);
    }

    /**
     * 保存详情
     */
    private void saveDetail(List<ConvertTransferDetailEntity> detailEntityList, ConvertTransferEntity transfer) {
        List<String> contractCodeList = detailEntityList.stream().map(ConvertTransferDetailEntity::getContractCode).collect(Collectors.toList());
        Map<String, ContractEntity> contractEntityMap = iContractService.lambdaQuery()
                .in(ContractEntity::getContractCode, contractCodeList)
                .eq(ContractEntity::getOrgId, transfer.getTransferParty())
                .list()
                .stream()
                .collect(Collectors.toMap(ContractEntity::getContractCode, Function.identity(), (o, n) -> o));
        // 根据合同号，和签约主体 查询合同
        for (ConvertTransferDetailEntity transferDetail : detailEntityList) {
            ContractEntity contractEntity = contractEntityMap.get(transferDetail.getContractCode());
            if (Objects.isNull(contractEntity)) {
                throw new ServiceException("根据原合同编号[" + transferDetail.getContractCode() + "]+转让方[" + transfer.getTransferParty() + "]未查询到原合同");
            }
            transferDetail.setConvertTransferId(transfer.getId());
            transferDetail.setOrgId(contractEntity.getOrgId());
            transferDetail.setClientCode(contractEntity.getClientCode());
            transferDetail.setClientName(contractEntity.getClientName());
            transferDetail.setTaxRate(contractEntity.getTaxRate());
        }

        // 根据合同+签约主体  查余额表凭证日期<=上传的交易日，创建日期为最新时对应字段
        TransferContractBalanceQueryDTO contractBalanceQueryDTO = new TransferContractBalanceQueryDTO();
        contractBalanceQueryDTO.setContractCodeList(contractCodeList);
        contractBalanceQueryDTO.setOrgId(transfer.getTransferParty());
        LocalDate tradeDate = transfer.getTradeDate().toLocalDate();
        contractBalanceQueryDTO.setVoucherDateEnd(tradeDate);
        List<ContractBalanceVO> contractBalanceVOList = iContractBalanceService.selectLatestBalanceByOrgIdContractCodeList(contractBalanceQueryDTO);
        // 按照合同+签约主体分组
        Map<String, List<ContractBalanceVO>> balanceGroupMap = contractBalanceVOList.stream().collect(Collectors.groupingBy(c -> c.getContractCode() + "-" + c.getOrgId()));


        //基准日后计提收益	基准日后计提拨备	基准日后收款
        contractBalanceQueryDTO.setVoucherDateEnd(null);
        contractBalanceQueryDTO.setSceneCodeList(Arrays.asList(SceneEnum.SYJT.getCode(), SceneEnum.JZJT.getCode(), SceneEnum.ZLSK.getCode()));
        contractBalanceQueryDTO.setVoucherDateStart(tradeDate);

        Map<String, List<ContractBalanceVO>> afterBalance = iContractBalanceService.selectLatestBalanceByOrgIdContractCodeList(contractBalanceQueryDTO)
                .stream()
                .collect(Collectors.groupingBy(c -> c.getContractCode() + "-" + c.getOrgId()));
        for (ConvertTransferDetailEntity transferDetail : detailEntityList) {
            String key = transferDetail.getContractCode() + "-" + transferDetail.getOrgId();
            if (balanceGroupMap.containsKey(key)) {
                ContractBalanceVO vo = balanceGroupMap.get(key).get(0);
                transferDetail.setPayableAgencyEstimate(vo.getPayableAgencyEstimateBalance());
                transferDetail.setPayableVehicleEstimate(vo.getPayableVehicleEstimateBalance());
                transferDetail.setPayableBandCostEstimate(vo.getPayableBandCostEstimateBalance());
                transferDetail.setPayablePledgeEstimate(vo.getPayablePledgeEstimateBalance());
                transferDetail.setPayableUnpledgeEstimate(vo.getPayableUnpledgeEstimateBalance());
                transferDetail.setPayableOtherCostEstimate(vo.getPayableOtherCostEstimateBalance());
            }

            BigDecimal accruedIncome = BigDecimal.ZERO, provision = BigDecimal.ZERO, receive = BigDecimal.ZERO;
            for (ContractBalanceVO vo : afterBalance.getOrDefault(key, Collections.emptyList())) {
                // 场景为SYJT，lease_revenue6_amount+lease_revenue_amount
                if (Objects.equals(vo.getSceneCode(), SceneEnum.SYJT.getCode())) {
                    accruedIncome = NumberUtil.add(accruedIncome, vo.getLeaseRevenue6Amount(), vo.getLeaseRevenueAmount());
                }
                // 场景为JZJT，depreciation_reserves_amount汇总
                if (Objects.equals(vo.getSceneCode(), SceneEnum.JZJT.getCode())) {
                    provision = NumberUtil.add(provision, vo.getDepreciationReservesAmount());
                }
                // 场景为ZLSK，receivable_unconfirm_receipt_amount的汇总金额
                if (Objects.equals(vo.getSceneCode(), SceneEnum.ZLSK.getCode())) {
                    receive = NumberUtil.add(receive, vo.getReceivableUnconfirmReceiptAmount());
                }

            }
            transferDetail.setBaseDateAccruedIncome(accruedIncome);
            transferDetail.setBaseDateProvision(provision);
            transferDetail.setBaseDateReceive(receive);
        }
        iConvertTransferDetailService.saveBatch(detailEntityList);
    }

    /**
     * 校验折价转让基本信息
     */
    private void checkData(List<ConvertTransferExcelDTO> transferExcelDTOList, List<ConvertTransferDetailExcelDTO> detailExcelDTOList, List<ConvertTransferPlanExcelDTO> planExcelDTOList) {
        if (CollectionUtils.isEmpty(transferExcelDTOList)) {
            throw new ServiceException("导入模板折价转让基本信息为空");
        }
        if (transferExcelDTOList.size() != 1) {
            throw new ServiceException("导入模板折价转让基本信息只能有一行数据");
        }
        if (CollectionUtils.isEmpty(detailExcelDTOList)) {
            throw new ServiceException("导入模板折价转让详情为空");
        }
        if (CollectionUtils.isEmpty(planExcelDTOList)) {
            throw new ServiceException("导入模板折价转让租金计划为空");
        }
        // 签约主体
        Map<String, String> orgIdMap = iOrgCompanyService.selectAllOrgIdAndName().stream().collect(Collectors.toMap(OrgCompanyVO::getOrgName, OrgCompanyVO::getOrgId, (a, b) -> b));
        for (ConvertTransferExcelDTO convertTransferExcelDTO : transferExcelDTOList) {
            if (ObjectUtil.isEmpty(convertTransferExcelDTO.getBatch())) {
                throw new ServiceException("折价转让基本信息转让批次不能为空");
            }
            if (ObjectUtil.isEmpty(convertTransferExcelDTO.getTransferParty())) {
                throw new ServiceException("折价转让基本信息转让方不能为空");
            }
            if (ObjectUtil.isEmpty(convertTransferExcelDTO.getTransfereeParty())) {
                throw new ServiceException("折价转让基本信息受让方不能为空");
            }
            if (ObjectUtil.isEmpty(convertTransferExcelDTO.getReferenceDate())) {
                throw new ServiceException("折价转让基本信息基准日不能为空");
            }
            if (ObjectUtil.isEmpty(convertTransferExcelDTO.getTradeDate())) {
                throw new ServiceException("折价转让基本信息交易日不能为空");
            }

            String transferOrgId = orgIdMap.get(convertTransferExcelDTO.getTransferParty());
            if (ObjectUtil.isEmpty(transferOrgId)) {
                throw new ServiceException("根据转让方[" + convertTransferExcelDTO.getTransferParty() + "]未查询到对应的签约主体");
            }
            convertTransferExcelDTO.setTransferParty(transferOrgId);
            String transfereeOrgId = orgIdMap.get(convertTransferExcelDTO.getTransfereeParty());
            if (ObjectUtil.isEmpty(transfereeOrgId)) {
                throw new ServiceException("根据受让方[" + convertTransferExcelDTO.getTransfereeParty() + "]未查询到对应的签约主体");
            }
            convertTransferExcelDTO.setTransfereeParty(transfereeOrgId);
        }
        String batch = transferExcelDTOList.get(0).getBatch();
        ConvertTransferEntity convertTransferEntity = baseMapper.selectOne(new LambdaQueryWrapper<ConvertTransferEntity>().eq(ConvertTransferEntity::getBatch, batch));
        if (ObjectUtil.isNotEmpty(convertTransferEntity)) {
            throw new ServiceException("批次号[" + batch + "]已存在，不能重复上传");
        }

        // 校验详情
        Set<String> unionSet = new HashSet<>();
        for (ConvertTransferDetailExcelDTO a : detailExcelDTOList) {
            if (ObjectUtil.isEmpty(a.getContractCode())) {
                throw new ServiceException("折价转让详情原合同编码不能为空");
            }
            if (ObjectUtil.isEmpty(a.getFinancialContractStatus())) {
                throw new ServiceException("折价转让详情财务合同状态不能为空");
            }
            if (ObjectUtil.isEmpty(a.getReceivableRent())) {
                throw new ServiceException("折价转让详情应收租金不能为空");
            }
            if (ObjectUtil.isEmpty(a.getReceivableResidualValue())) {
                throw new ServiceException("折价转让详情应收期末残值不能为空");
            }
            if (ObjectUtil.isEmpty(a.getReceivableOuttax())) {
                throw new ServiceException("折价转让详情应收销项税不能为空");
            }
            if (ObjectUtil.isEmpty(a.getUnrealizedRevenue())) {
                throw new ServiceException("折价转让详情未实现融资租赁收益不能为空");
            }
            if (ObjectUtil.isEmpty(a.getLesseeMargin())) {
                throw new ServiceException("折价转让详情承租人保证金不能为空");
            }
            if (ObjectUtil.isEmpty(a.getDepreciationReserves())) {
                throw new ServiceException("折价转让详情应收租赁款组合拨备不能为空");
            }
            if (ObjectUtil.isEmpty(a.getAppraisedValue())) {
                throw new ServiceException("折价转让详情评估价不能为空");
            }
            if (ObjectUtil.isEmpty(a.getTransferOpen())) {
                throw new ServiceException("折价转让详情转让时敞口不能为空");
            }
            if (ObjectUtil.isEmpty(a.getSupplementaryProvision())) {
                throw new ServiceException("折价转让详情补提拨备不能为空");
            }
            if (ObjectUtil.isEmpty(a.getRevenueRecognition())) {
                throw new ServiceException("折价转让详情收益确认不能为空");
            }
            // 校验合同编号重复
            if (!unionSet.add(a.getContractCode())) {
                throw new ServiceException("文件折价转让详情存在重复的合同编号[" + a.getContractCode() + "],请检查");
            }
        }
        List<String> contractCodeList = detailExcelDTOList.stream().map(ConvertTransferDetailExcelDTO::getContractCode).collect(Collectors.toList());
        // 校验租金计划
        Set<String> unionPlanSet = new HashSet<>();

        Map<String, Map<Boolean, String>> date = new HashMap<>();

        for (ConvertTransferPlanExcelDTO a : planExcelDTOList) {
            if (ObjectUtil.isEmpty(a.getOldContractCode())) {
                throw new ServiceException("折价转让详情原合同编码不能为空");
            }
            if (ObjectUtil.isEmpty(a.getNewContractCode())) {
                throw new ServiceException("折价转让详情新合同编码不能为空");
            }
            if (!contractCodeList.contains(a.getOldContractCode())) {
                throw new ServiceException("租金计划的原合同编号[" + a.getOldContractCode() + "] 在折价转让详情中不存在");
            }
            // 校验原合同编号+新合同编号+计划日期重复
            if (!unionPlanSet.add(a.getOldContractCode() + a.getNewContractCode() + a.getPlanDate())) {
                throw new ServiceException("文件折价转让租金计划存在重复的原合同编号[" + a.getOldContractCode() + "]+新合同编号[" + a.getNewContractCode() + "]+计划日期[" + a.getPlanDate() + "],请检查");
            }
            date.compute(a.getOldContractCode() +"-" + a.getNewContractCode(),(k,v)-> v == null ? new HashMap<>() : v)
                    .put(ObjectUtil.isEmpty(a.getPlanDate()), a.getOldContractCode() + a.getNewContractCode() + a.getPlanDate());
        }

        date.forEach((key, value) -> {
            if (value.size() == 2) {
                throw new ServiceException("【原合同-新合同】" + key + "存在同时填写了计划日期与未填写计划日期的数据行");
            }
        });
    }

    @Override
    public Boolean generateVoucher(List<Long> ids) {
        return generateVoucher(ids, YesOrNoEnum.NO);
    }

    private Boolean generateVoucher(List<Long> ids, YesOrNoEnum yesOrNo) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请选中一行");
        }
        // 校验数据并处理数据 含 删除已经生成的凭证
        Pair<List<ConvertTransferEntity>, Map<String, ConvertTransferDetailEntity>> check = checkAndPrepareEntity(ids);
        List<ConvertTransferEntity> entityList = check.getFirst();
        Map<String, ConvertTransferDetailEntity> detailEntityMap = check.getSecond();

        // 生成凭证数据
        List<Map<String, Object>> voucherMapList = generateVoucherData(entityList, detailEntityMap.values(), yesOrNo);

        // 生成凭证
        List<VoucherInfoVO> voucherResultList = iRuleService.batchExecuteRule(voucherMapList);
        // 查验数据结果
        boolean isExistVoucherError = voucherResultList.stream().allMatch(v -> StringUtils.isNotEmpty(v.getErrorInfo()));
        if (YesOrNoEnum.YES == yesOrNo && isExistVoucherError) {
            List<Long> voucherIdList = Lists.newArrayList();
            for (VoucherInfoVO voucherInfoVO : voucherResultList) {
                if (CollectionUtils.isNotEmpty(voucherInfoVO.getVoucherDTOList())) {
                    voucherInfoVO.getVoucherDTOList().stream().map(VoucherDTO::getId).forEach(voucherIdList::add);
                }
            }
            // 异步删除已生成的凭证
            if (CollectionUtils.isNotEmpty(voucherIdList))
                CompletableFuture.runAsync(() -> iVoucherService.deleteByIdList(voucherIdList), asyncTaskExecutor);
            return Boolean.FALSE;
        }
        long count = voucherResultList.stream()
                .map(VoucherInfoVO::getVoucherDTOList)
                .filter(Objects::nonNull)
                .mapToLong(Collection::size)
                .sum();
        if (count == 0) {
            return false;
        }
        // 保存凭证信息
        saveVoucher(entityList, detailEntityMap, voucherResultList);
        if (YesOrNoEnum.YES == yesOrNo) {
            Map<Long, ConvertTransferEntity> transferEntityMap = entityList.stream().collect(Collectors.toMap(ConvertTransferEntity::getId, Function.identity()));

            List<String> batchList = entityList.stream().map(ConvertTransferEntity::getBatch).collect(Collectors.toList());
            // 提交1. 当租金计划里面的新合同和不存在的情况
            List<SFunction<ConvertTransferPlanEntity, ?>> select = Arrays.asList(ConvertTransferPlanEntity::getNewContractCode, ConvertTransferPlanEntity::getOldContractCode, ConvertTransferPlanEntity::getConvertTransferId);
            List<ConvertTransferPlanEntity> planEntities = iConvertTransferPlanService.lambdaQuery()
                    .in(ConvertTransferPlanEntity::getBatch, batchList)
                    .select(select)
                    .groupBy(select)
                    .list();
            Map<String, ConvertTransferDetailEntity> orignContractFinacialStatusMap = detailEntityMap.values().stream().collect(Collectors.toMap(ConvertTransferDetailEntity::getContractCode, Function.identity(), (l, r) -> r));
            Map<String, Set<String>> orgContractCodeMap = new HashMap<>();
            for (ConvertTransferPlanEntity planEntity : planEntities) {
                ConvertTransferEntity transferEntity = transferEntityMap.get(planEntity.getConvertTransferId());
                // 转让方 老合同
                orgContractCodeMap.compute(transferEntity.getTransferParty(), (key, old) -> CollectionUtils.isEmpty(old) ? new HashSet<>() : old)
                        .add(planEntity.getOldContractCode());
                // 受让方 新合同
                orgContractCodeMap.compute(transferEntity.getTransfereeParty(), (key, old) -> CollectionUtils.isEmpty(old) ? new HashSet<>() : old)
                        .add(planEntity.getNewContractCode());
            }
            List<ContractEntity> newContractList = new ArrayList<>();

            LambdaQueryChainWrapper<ContractEntity> wrapper = iContractService.lambdaQuery();
            wrapper.nested(w -> {
                        for (Map.Entry<String, Set<String>> entry : orgContractCodeMap.entrySet()) {
                            Set<String> contractCodeList = entry.getValue();
                            String orgId = entry.getKey();
                            if (CollectionUtils.isNotEmpty(contractCodeList)) {
                                w.or().nested(c -> c.eq(ContractEntity::getOrgId, orgId).
                                        in(ContractEntity::getContractCode, contractCodeList)
                                );
                            }
                        }
                    }
            );

            Map<String, ContractEntity> orgContractCodeKeyMap = wrapper.list().stream().collect(Collectors.toMap(contract -> contract.getOrgId() + "-" + contract.getContractCode(), Function.identity()));

            Set<String> orgContractCodeSet = new HashSet<>();

            Map<String, List<ContractVO>> records = new HashMap<>();

            for (ConvertTransferPlanEntity entity : planEntities) {
                ConvertTransferEntity transferEntity = transferEntityMap.get(entity.getConvertTransferId());
                String nowKey = transferEntity.getTransfereeParty() + "-" + entity.getNewContractCode();
                String originKey = transferEntity.getTransferParty() + "-" + entity.getOldContractCode();
                ContractEntity origin = orgContractCodeKeyMap.get(originKey);
                ContractEntity contract = orgContractCodeKeyMap.get(nowKey);
                // 单个合同只处理一次
                if (orgContractCodeSet.add(originKey)) {
                    ConvertTransferDetailEntity transferDetail = orignContractFinacialStatusMap.get(entity.getOldContractCode());
                    Instant instant = transferEntity.getFinanceDate().atZone(ZoneId.systemDefault()).toInstant();
                    // 提交2. 先更新合同表的这5个字段
                    // 修改 财务合同状态
                    origin.setFinancialContractStatusUpdateTime(Date.from(instant));
                    origin.setFinancialContractStatus(transferDetail.getFinancialContractStatus());
                    // 更新 转入公司
                    origin.setTransferOrgId(transferEntity.getTransfereeParty());
                    origin.setTransferContractCode(entity.getNewContractCode());
                    origin.setTransferContractStatus(transferDetail.getFinancialContractStatus());
                    newContractList.add(origin);

                    ContractVO vo = BeanUtil.copyProperties(origin, ContractVO.class);
                    vo.setSourceFromType(SceneEnum.ZJZR.getCode());
                    vo.setSourceFromId(entity.getConvertTransferId());
                    records.compute(transferEntity.getTransferParty(), (key, old) -> CollectionUtils.isEmpty(old) ? new ArrayList<>() : old)
                            .add(vo);
                }
                // 单个合同只处理一次
                if (orgContractCodeSet.add(nowKey)) {
                    // 提交1. 当租金计划里面的新合同和不存在的情况 copy 原合同号 + org_id 的数据 修改下面字段的信息 新增一条记录
                    if (Objects.isNull(contract)) {
                        contract = BeanUtil.copyProperties(
                                origin,
                                ContractEntity.class,
                                "id", "createBy", "updateBy", "createTime", "updateTime",
                                "financialContractStatus", "financialContractStatusUpdateTime",
                                "transferOrgId", "transferContractCode", "transferContractStatus", "clientCode", "clientName");
                        contract.setContractCode(entity.getNewContractCode());
                        contract.setOrgId(transferEntity.getTransfereeParty());

                        newContractList.add(contract);
                    }
                }
            }
            if (newContractList.isEmpty()) {
                log.warn("No contracts will be updated");
            } else {
                // 提交2. 更新合同状态
                iContractService.saveOrUpdateBatch(newContractList);
            }

            if (records.isEmpty()) {
                log.warn("No contracts records will be saved");
            } else {
                // 提交3. 添加合同状态记录
                records.values().forEach(iContractService::saveRecordList);
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean submit(List<Long> ids) {
        Boolean generated = generateVoucher(ids, YesOrNoEnum.YES);
        if (Objects.equals(generated, Boolean.FALSE)) {
            return Boolean.FALSE;
        }

        List<ApproveDTO> approveDTOList = new ArrayList<>();
        for (Long id : ids) {
            ApproveDTO approveDTO = new ApproveDTO();
            approveDTO.setDocumentId(id);
            approveDTO.setDocumentType(SceneEnum.ZJZR.getCode());
            approveDTO.setUrl(approveUrl + id);
            approveDTOList.add(approveDTO);
        }
        lambdaUpdate()
                .set(ConvertTransferEntity::getProcessStatus, ProcessStatusEnum.SUBMITTED.getCode())
                .set(ConvertTransferEntity::getErrorInfo, "")
                .in(ConvertTransferEntity::getId, ids)
                .update(new ConvertTransferEntity());
        approveService.submit(approveDTOList);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean withdraw(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条数据撤回");
        }
        List<ConvertTransferEntity> entities = lambdaQuery()
                .eq(ConvertTransferEntity::getProcessStatus, ProcessStatusEnum.SUBMITTED.getCode())
                .in(ConvertTransferEntity::getId, ids)
                .list();

        if (entities.size() < ids.size()) {
            throw new ServiceException("只有处理状态为已提交的才可以撤回");
        }
        List<Long> voucherIds = new ArrayList<>();
        List<Long> processInstanceId = new ArrayList<>();
        for (ConvertTransferEntity entity : entities) {
            Stream.of(entity.getVoucherId().split(",")).map(Long::valueOf).forEach(voucherIds::add);
            entity.setVoucherId(null);
            entity.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
            entity.setIsGenerateVoucher(YesOrNoEnum.NO.getCode());
            processInstanceId.add(entity.getProcessInstanceId());
        }
        updateBatchById(entities);
        clearContractStatusRecord(entities);
        approveService.withdraw(processInstanceId);
        iVoucherService.deleteByIdList(voucherIds);
        return true;
    }

    @Override
    @Transactional
    public Boolean delete(List<Long> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条数据删除");
        }

        List<ConvertTransferEntity> entityList = lambdaQuery()
                .in(ConvertTransferEntity::getId, ids)
                .select(Collections.singletonList(ConvertTransferEntity::getProcessStatus))
                .list();
        for (ConvertTransferEntity v : entityList) {
            if (!(ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus()) || ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus()))) {
                throw new ServiceException("只有处理状态为已录入或者已拒绝的才可以删除");
            }
        }
        removeBatchByIds(ids);
        // 删除详情行信息
        iConvertTransferDetailService.deleteByConvertTransferId(ids);
        // 删除租金计划信息
        iConvertTransferPlanService.deleteByConvertTransferId(ids);
        // 删除凭证
        batchDeleteVoucher(entityList);
        // 删除合同状态
        clearContractStatusRecord(entityList);
        return Boolean.TRUE;
    }

    /**
     * 删除凭证
     */
    private void batchDeleteVoucher(List<ConvertTransferEntity> entityList) {
        // 获取所有的凭证Id
        // 逗号拆分
        List<Long> voucherIdList = entityList.stream()
                .map(ConvertTransferEntity::getVoucherId)
                .filter(StringUtils::isNotEmpty)
                .map(v -> v.split(","))
                .flatMap(Arrays::stream)
                .map(Long::valueOf)
                .collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(voucherIdList)) {
            iVoucherService.deleteByIdList(voucherIdList);
        }
    }

    @Override
    public List<String> batchList() {
        List<SFunction<ConvertTransferEntity, ?>> select = Collections.singletonList(ConvertTransferEntity::getBatch);
        return lambdaQuery()
                .select(select)
                .groupBy(select)
                .list()
                .stream()
                .map(ConvertTransferEntity::getBatch)
                .collect(Collectors.toList());
    }

    /**
     * 设置查询条件
     */
    private void setQueryCondition(ConvertTransferQueryDTO queryDTO, LambdaQueryWrapper<ConvertTransferEntity> queryWrapper) {
        if (ObjectUtil.isNotEmpty(queryDTO.getBatchList())) {
            queryWrapper.in(ConvertTransferEntity::getBatch, queryDTO.getBatchList());
        }
        if (ObjectUtil.isNotEmpty(queryDTO.getTransferPartyList())) {
            queryWrapper.in(ConvertTransferEntity::getTransferParty, queryDTO.getTransferPartyList());
        }
        if (ObjectUtil.isNotEmpty(queryDTO.getTransfereePartyList())) {
            queryWrapper.in(ConvertTransferEntity::getTransfereeParty, queryDTO.getTransfereePartyList());
        }
        if (CollectionUtils.isNotEmpty(queryDTO.getIdList())) {
            queryWrapper.in(ConvertTransferEntity::getId, queryDTO.getIdList());
        }
        if (CollectionUtils.isNotEmpty(queryDTO.getProcessStatusList())) {
            queryWrapper.in(ConvertTransferEntity::getProcessStatus, queryDTO.getProcessStatusList());
        }
    }


    private List<Map<String, Object>> generateVoucherData(List<ConvertTransferEntity> entityList, Collection<ConvertTransferDetailEntity> detailEntities, YesOrNoEnum yesOrNoEnum) {
        Map<Long, ConvertTransferEntity> transferEntityMap = entityList.stream().collect(Collectors.toMap(ConvertTransferEntity::getId, Function.identity()));
        List<Map<String, Object>> voucherMapList = Lists.newArrayList();
        for (ConvertTransferDetailEntity entity : detailEntities) {
            String orderId = entity.getId().toString();
            ConvertTransferEntity transferEntity = transferEntityMap.get(entity.getConvertTransferId());

            ExecuteCommonDTO executeCommonDTO = new ExecuteCommonDTO();
            executeCommonDTO.setSystemCode(SystemEnum.CWZT.getCode());
            executeCommonDTO.setSystemName(SystemEnum.CWZT.getDesc());
            executeCommonDTO.setSceneCode(SceneEnum.ZJZR.getCode());
            executeCommonDTO.setSceneName(SceneEnum.ZJZR.getDesc());
            executeCommonDTO.setBatchId(transferEntity.getId());
            executeCommonDTO.setBatchType(SceneEnum.ZJZR.getCode());
            executeCommonDTO.setOrderId(orderId);
            executeCommonDTO.setBusinessDate(DateUtils.toDate(transferEntity.getBusinessDate()));
            executeCommonDTO.setContractCode(entity.getContractCode());
            executeCommonDTO.setClientCode(entity.getClientCode());
            executeCommonDTO.setAccountDate(new Date());
            executeCommonDTO.setIsSubmit(yesOrNoEnum.getCode());


            Map<String, Object> dataMap = BeanUtil.beanToMap(executeCommonDTO);
            // 默认：折价转让
            dataMap.put("transferType", SceneEnum.ZJZR.getDesc());
            // 转让方
            dataMap.put("transferor", transferEntity.getTransferParty());
            // 受让方
            dataMap.put("transferee", transferEntity.getTransfereeParty());
            // 转让批次
            dataMap.put("transferBatch", transferEntity.getBatch());

            // 评估价
            dataMap.put("estimatePrice", entity.getAppraisedValue());
            // 应收租金
            dataMap.put("receivableLeaseAmount", entity.getReceivableRent());
            // 应收期末残值
            dataMap.put("retainedPrice", entity.getReceivableResidualValue());
            // 应收销项税
            dataMap.put("receivableOuttaxAmount", entity.getReceivableOuttax());
            // 未实现融资租赁收益
            dataMap.put("unrealizedRevenueAmount", entity.getUnrealizedRevenue());
            // 承租人保证金
            dataMap.put("receivableMarginAmount", entity.getLesseeMargin());

            // 应付经销商服务费-暂估
            dataMap.put("payableAgencyEstimateAmount", entity.getPayableAgencyEstimate());
            // 应付收车费-暂估
            dataMap.put("payableVehicleEstimateAmount", entity.getPayableVehicleEstimate());
            // 应付手环成本_暂估
            dataMap.put("payableBandCostEstimateAmount", entity.getPayableBandCostEstimate());
            // 应付抵押费_暂估
            dataMap.put("payablePledgeEstimateAmount", entity.getPayablePledgeEstimate());
            // 应付解抵押费_暂估
            dataMap.put("payableUnpledgeEstimateAmount", entity.getPayableUnpledgeEstimate());
            // 应付其他租赁成本-暂估
            dataMap.put("payableOtherCostEstimateAmount", entity.getPayableOtherCostEstimate());

            // 基准日补提拨备 provisionReversalAmount
            dataMap.put("provisionReversalAmount", entity.getSupplementaryProvision());
            // 基准日后收款 receiveUnconfirmed
            dataMap.put("receiveUnconfirmed", entity.getBaseDateReceive());
            // 基准日转让时敞口 financialExpenseAmount
            dataMap.put("financialExpenseAmount", entity.getTransferOpen());

            fillControlBalanceJYJGBG(dataMap, transferEntity, entity);
            fillControlBalanceZLSK(dataMap, transferEntity, entity);
            fillControlBalanceKJFP(dataMap, transferEntity, entity);

            voucherMapList.add(dataMap);
        }
        return voucherMapList;
    }

    private void fillControlBalanceJYJGBG(Map<String, Object> data, ConvertTransferEntity entity, ConvertTransferDetailEntity detailEntity) {
        LocalDateTime businessDate = entity.getBusinessDate();
        int periodCode = businessDate.getYear() * 100 + businessDate.getMonthValue();
        Map<String, BigDecimal> sum = Optional.ofNullable(((ContractBalanceMapper) iContractBalanceService.getBaseMapper())
                        .sumContractBalanceJYJGBG(periodCode, entity.getTransferParty(), businessDate, detailEntity.getContractCode()))
                .orElse(Collections.emptyMap());
        // 封包日后租金调整
        data.put("receivableLeaseAdjustAmount", sum.getOrDefault("receivable_downpayment_amount", BigDecimal.ZERO));
        // 封包日后首付款调整
        data.put("firstAdjustAmount", sum.getOrDefault("receivable_commission_amount", BigDecimal.ZERO));
        // 封包日后手续费调整
        data.put("procedureAdjustRevenues", sum.getOrDefault("receivable_insurance_amount", BigDecimal.ZERO));
        // 封包日后应收保险费调整
        data.put("insuranceAdjustAmount", sum.getOrDefault("payable_insurance_estimate_amount", BigDecimal.ZERO));
        // 封包日后应付保险费调整
        data.put("payableInsuranceEstimateAdjustAmount", sum.getOrDefault("receivable_residual_value_amount", BigDecimal.ZERO));
        // 封包日后留购价调整
        data.put("residualAdjustAmount", sum.getOrDefault("receivable_otherincome_amount", BigDecimal.ZERO));
        // 封包日后其他收入调整
        data.put("otherAdjustRevenues", sum.getOrDefault("payable_device_estimate_amount", BigDecimal.ZERO));
        // 封包日后设备款调整
        data.put("payableDeviceAdjustAmount", sum.getOrDefault("payable_other_cost_estimate_amount", BigDecimal.ZERO));
        // 封包日后其他成本调整
        data.put("otherCostAdjustAmount", sum.getOrDefault("payable_agency_estimate_amount", BigDecimal.ZERO));
        // 封包日后经销商服务费调整
        data.put("payableServiceAdjustAmount", sum.getOrDefault("payable_band_cost_estimate_amount", BigDecimal.ZERO));
        // 封包日后手环成本调整
        data.put("payableBraceletAdjustAmount", sum.getOrDefault("receivable_outtax_amount", BigDecimal.ZERO));
        // 封包日后应收销项税调整
        data.put("receivableOuttaxAdjustAmount", sum.getOrDefault("unrealized_revenue_amount", BigDecimal.ZERO));
        // 封包日后未实现收益调整
        data.put("unrealizedRevenueAdjustAmount", sum.getOrDefault("receivable_service_amount", BigDecimal.ZERO));
        // 封包日后服务费调整
        data.put("serviceAdjustAmount", sum.getOrDefault("receivable_service_outtax_amount", BigDecimal.ZERO));
        // 封包日后服务费销项税调整
        data.put("receivableServiceOuttaxAdjustAmount", sum.getOrDefault("service_revenue_amount", BigDecimal.ZERO));
        // 封包日后服务收入调整
        data.put("serviceEevenueAdjustAmount", sum.getOrDefault("outtax_amount", BigDecimal.ZERO));
        // 封包日后销项税额调整
        data.put("outtaxAdjustAmount", sum.getOrDefault("receive_sum_amount", BigDecimal.ZERO));
        // 博远封包日后应收总额调整
        data.put("receiveSumAdjustAmount", sum.getOrDefault("receive_sum_outtax_amount", BigDecimal.ZERO));
        // 博远封包日后应收销项税调整
        data.put("receiveSumOuttaxAdjustAmount", sum.getOrDefault("payable_other_estimate_amount", BigDecimal.ZERO));
        // 博远封包日后应付其他款项调整
        data.put("payableOtherEstimateAdjustAmount", sum.getOrDefault("receive_unrealized_revenue_amount", BigDecimal.ZERO));
        // 博远封包日后未实现收益调整
        data.put("receiveUnrealizedRevenueAdjustAmount", sum.getOrDefault("", BigDecimal.ZERO));
    }

    private void fillControlBalanceZLSK(Map<String, Object> data, ConvertTransferEntity entity, ConvertTransferDetailEntity detailEntity) {
        LocalDateTime businessDate = entity.getBusinessDate();
        int periodCode = businessDate.getYear() * 100 + businessDate.getMonthValue();
        Map<String, BigDecimal> sum = Optional.ofNullable(((ContractBalanceMapper) iContractBalanceService.getBaseMapper())
                        .sumContractBalanceZLSK(periodCode, entity.getTransferParty(), businessDate, detailEntity.getContractCode()))
                .orElse(Collections.emptyMap());

        // 未确认收款
        data.put("receiveUnconfirmed", sum.getOrDefault("receivable_downpayment_amount", BigDecimal.ZERO));
        // 收取应收租金
        data.put("receiveLeaseAmount", sum.getOrDefault("receivable_rent_amount", BigDecimal.ZERO));
        // 收取留购价
        data.put("receiveRetainedPrice", sum.getOrDefault("receivable_residual_value_amount", BigDecimal.ZERO));
        // 收取承租人保证金
        data.put("receiveMarginAmount", sum.getOrDefault("lessee_margin_amount", BigDecimal.ZERO));
        // 收取供应商保证金
        data.put("receiveSupplierMarginAmount", sum.getOrDefault("supplier_margin_amount", BigDecimal.ZERO));
        // 收取首付款
        data.put("receiveDownpaymentAmount", sum.getOrDefault("receivable_downpayment_amount", BigDecimal.ZERO));
        // 收取手续费
        data.put("receiveCommissionAmount", sum.getOrDefault("receivable_commission_amount", BigDecimal.ZERO));
        // 收取服务费
        data.put("receivesServiceAmount", sum.getOrDefault("receivable_service_amount", BigDecimal.ZERO));
        // 收取保险费
        data.put("receiveInsuranceAmount", sum.getOrDefault("receivable_insurance_amount", BigDecimal.ZERO));
        // 收取保险费差额
        data.put("receiveInsuranceDifferAmount", sum.getOrDefault("insurance_differ_amount", BigDecimal.ZERO));
        // 收取其他收入
        data.put("receiveOtherincomeAmount", sum.getOrDefault("receivable_otherincome_amount", BigDecimal.ZERO));
        // 收取违约金收入
        data.put("receiveDamagesRevenueAmount", sum.getOrDefault("receivable_damages_revenue_amount", BigDecimal.ZERO));
        // 收取其他租赁收入
        data.put("receiveOtherRevenueAmount", sum.getOrDefault("receivable_other_revenue_amount", BigDecimal.ZERO));
        // 收取罚息收入
        data.put("receiveDefaultInterestAmount", sum.getOrDefault("receivable_default_interest_amount", BigDecimal.ZERO));
        // 收取合同解约及更改手续费
        data.put("receiveTerminateProcedureAmount", sum.getOrDefault("receivable_terminate_amount", BigDecimal.ZERO));
        // 罚息收入确认不含税额
        data.put("dinterestRevenueAmount", sum.getOrDefault("dinterest_revenue_amount", BigDecimal.ZERO));
        // 变更手续费收入确认不含税额
        data.put("terminateAmount", sum.getOrDefault("terminate_amount", BigDecimal.ZERO));
        // 违约金收入确认不含税额
        data.put("damagesRevenueAmount", sum.getOrDefault("damages_revenue_amount", BigDecimal.ZERO));
        // 其他租赁收入确认不含税额
        data.put("otherRevenueAmount", sum.getOrDefault("other_revenue_amount", BigDecimal.ZERO));
        // 税金计提金额
        data.put("receivableOuttaxAmountJT", sum.getOrDefault("payable_other_estimate_amount", BigDecimal.ZERO));
        // 抵扣保证金
        data.put("deductionMarginAmount", sum.getOrDefault("lessee_margin_amount", BigDecimal.ZERO));
        // 保证金抵扣应收租金
        data.put("deductionLeaseAmount", sum.getOrDefault("receivable_rent_amount", BigDecimal.ZERO));
        // 保证金抵扣留购价
        data.put("deductionRetainedPrice", sum.getOrDefault("receivable_residual_value_amount", BigDecimal.ZERO));
        // 保证金抵扣罚息收入
        data.put("deductionDefaultInterestAmount", sum.getOrDefault("receivable_default_interest_amount", BigDecimal.ZERO));
        // 保证金抵扣合同解约及更改手续费
        data.put("deductionTerminateProcedureAmount", sum.getOrDefault("receivable_terminate_amount", BigDecimal.ZERO));
        // 保证金抵扣罚息收入不含税额
        data.put("deductionDefaultNoTaxAmount", sum.getOrDefault("dinterest_revenue_amount", BigDecimal.ZERO));
        // 保证金抵扣变更手续费收入不含税额
        data.put("deductionTerminateNoTaxAmount", sum.getOrDefault("terminate_amount", BigDecimal.ZERO));
        // 保证金抵扣税金计提金额
        data.put("deductionreceivableOuttaxAmountJT", sum.getOrDefault("receivable_outtax_amount", BigDecimal.ZERO));
    }

    private void fillControlBalanceKJFP(Map<String, Object> data, ConvertTransferEntity entity, ConvertTransferDetailEntity detailEntity) {
        LocalDateTime businessDate = entity.getBusinessDate();
        int periodCode = businessDate.getYear() * 100 + businessDate.getMonthValue();
        Map<String, BigDecimal> sum = Optional.ofNullable(((ContractBalanceMapper) iContractBalanceService.getBaseMapper())
                        .sumContractBalanceKJFP(periodCode, entity.getTransferParty(), businessDate, detailEntity.getContractCode()))
                .orElse(Collections.emptyMap());
        // 应收销项税
        data.put("receivableOuttaxAmountKP", sum.getOrDefault("receivable_outtax_amount", BigDecimal.ZERO));
        // 应收服务费销项税
        data.put("receivableServiceOuttaxAmountKP", sum.getOrDefault("receivable_service_outtax_amount", BigDecimal.ZERO));

    }

    private Pair<List<ConvertTransferEntity>, Map<String, ConvertTransferDetailEntity>> checkAndPrepareEntity(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少选择一条数据生成凭证");
        }
        List<ConvertTransferEntity> entityList = listByIds(ids);
        for (ConvertTransferEntity convertTransferEntity : entityList) {
            if (!(ProcessStatusEnum.ENTERED.getCode().equals(convertTransferEntity.getProcessStatus()) || ProcessStatusEnum.REJECTED.getCode().equals(convertTransferEntity.getProcessStatus()))) {
                throw new ServiceException("处理状态为已录入或者已拒绝的才可以生成凭证");
            }
        }

        // 清理之前生成的凭证 并 更新 主数据
        List<Long> oldVoucherIds = entityList.stream().map(ConvertTransferEntity::getVoucherId)
                .filter(StringUtils::isNotBlank)
                .map(String::trim)
                .map(s -> Arrays.asList(s.split(",")))
                .flatMap(Collection::stream)
                .map(Long::valueOf)
                .collect(Collectors.toList());
        List<ConvertTransferDetailEntity> detailEntities = new ArrayList<>();
        Map<String, ConvertTransferDetailEntity> detailEntityMap = new HashMap<>();


        for (ConvertTransferEntity entity : entityList) {

            entity.setVoucherId("");
            List<ConvertTransferDetailEntity> entities = iConvertTransferDetailService.lambdaQuery()
                    .eq(ConvertTransferDetailEntity::getConvertTransferId, entity.getId())
                    .list();
            for (ConvertTransferDetailEntity detail : entities) {
                detail.setVoucherId("");
                detailEntityMap.put(detail.getId().toString(), detail);
            }
            detailEntities.addAll(entities);

        }
        // 删除凭证
        iVoucherService.deleteByIdList(oldVoucherIds);
        // 更新主表
        updateBatchById(entityList);
        // 更新详细
        iConvertTransferDetailService.updateBatchById(detailEntities);
        return Pair.of(entityList, detailEntityMap);
    }

    private void saveVoucher(List<ConvertTransferEntity> entityList, Map<String, ConvertTransferDetailEntity> detailEntityMap, List<VoucherInfoVO> voucherResultList) {
        Map<Long, String> transferVourchIdMap = new HashMap<>();

        Map<Long, String> transferYesOrNoMap = new HashMap<>();
        for (VoucherInfoVO infoVO : voucherResultList) {
            String errorInfo = Optional.ofNullable(infoVO.getErrorInfo())
                    .map(v -> v.substring(0, Math.min(v.length(), 2000)))
                    .orElse("");

            YesOrNoEnum isGenerateVoucher = errorInfo.isEmpty() ? YesOrNoEnum.YES : YesOrNoEnum.NO;

            String voucherIds = Optional.ofNullable(infoVO.getVoucherDTOList())
                    .filter(CollectionUtils::isNotEmpty)
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(VoucherDTO::getId)
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));

            ConvertTransferDetailEntity entity = detailEntityMap.get(infoVO.getOrderId());
            entity.setAccountDate(LocalDateTime.now());
            entity.setErrorInfo(errorInfo);
            entity.setVoucherId(voucherIds);
            entity.setUpdateTime(null);
            iConvertTransferDetailService.updateById(entity);
            Long convertTransferId = entity.getConvertTransferId();
            transferYesOrNoMap.compute(convertTransferId, (k, old) -> {
                if (YesOrNoEnum.YES.getCode().equals(old)) {
                    return old;
                }
                return isGenerateVoucher.getCode();
            });
            transferVourchIdMap.compute(convertTransferId, (k, old) -> old == null ? entity.getVoucherId() : old + "," + entity.getVoucherId());

        }
        // 更新汇总单据
        for (ConvertTransferEntity transferEntity : entityList) {
            transferEntity.setVoucherId(transferVourchIdMap.get(transferEntity.getId()));
            transferEntity.setAccountDate(LocalDateTime.now());
            transferEntity.setUpdateTime(null);

            transferEntity.setIsGenerateVoucher(transferYesOrNoMap.get(transferEntity.getId()));
        }
        updateBatchById(entityList);
    }

    // 删除合同状态
    private void clearContractStatusRecord(ConvertTransferEntity transfer) {
        contractStatusRecordService.lambdaUpdate()
                .eq(ContractStatusRecordEntity::getSourceFromId, transfer.getId())
                .eq(ContractStatusRecordEntity::getSourceFromType, SceneEnum.ZJZR.getCode())
                .remove();
    }

    private void clearContractStatusRecord(List<ConvertTransferEntity> entities) {
        List<Long> transferIdList = entities.stream().map(ConvertTransferEntity::getId).collect(Collectors.toList());
        contractStatusRecordService.lambdaUpdate()
                .in(ContractStatusRecordEntity::getSourceFromId, transferIdList)
                .eq(ContractStatusRecordEntity::getSourceFromType, SceneEnum.ZJZR.getCode())
                .remove();
    }
}

