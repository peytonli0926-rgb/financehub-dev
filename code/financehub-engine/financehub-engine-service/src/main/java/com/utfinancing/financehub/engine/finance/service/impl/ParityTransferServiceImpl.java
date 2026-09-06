package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.approve.model.dto.ApproveDTO;
import com.utfinancing.financehub.engine.approve.service.IApproveService;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.*;
import com.utfinancing.financehub.engine.finance.mapper.ParityTransferMapper;
import com.utfinancing.financehub.engine.finance.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import com.utfinancing.financehub.engine.rule.model.dto.ExecuteCommonDTO;
import com.utfinancing.financehub.engine.rule.model.vo.VoucherInfoVO;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.rmi.ServerException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @Author : bruyang
 * @Date : Create in 2024-04-02
 * @Description :  ParityTransfer服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class ParityTransferServiceImpl extends ServiceImpl<ParityTransferMapper, ParityTransferEntity> implements IParityTransferService {

    private final ParityTransferMapper parityTransferMapper;
    private final IContractBalanceLatestService iContractBalanceLatestService;
    private final IContractBalanceTempService iContractBalanceTempService;
    private final IClientService iClientService;
    private final IBankAccountService iBankAccountService;
    @Resource
    private IParityTransferDetailService iParityTransferDetailService;
    @Resource
    private IOrgCompanyService iOrgCompanyService;

    @Resource
    private IContractService iContractService;

    @Resource
    private IContractBalanceService iContractBalanceService;

    @Resource
    private IVoucherService iVoucherService;

    @Value("${approve.url.parityTransfer-url:null}")
    private String approveUrl;
    @Resource
    private IApproveService iApproveService;
    @Resource
    private final IRuleService iRuleService;

    @Override
    public Long saveParityTransfer(ParityTransferDTO dto) {
        ParityTransferEntity entity = BeanUtil.copyProperties(dto, ParityTransferEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateParityTransfer(Long id, ParityTransferDTO dto) {
        ParityTransferEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public ParityTransferDTO getParityTransferDTOById(Long id) {
        ParityTransferEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, ParityTransferDTO.class);
    }

    @Override
    public IPage<ParityTransferVO> selectPage(ParityTransferQueryDTO queryDTO) {
        LambdaQueryWrapper<ParityTransferEntity> queryWrapper = getQueryWrapper(queryDTO);
        // 这里注入查询条件
        IPage<ParityTransferEntity> entityIPage = parityTransferMapper.selectPage(new Page<ParityTransferEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        IPage<ParityTransferVO> parityTransferVOIPage = ListBeanUtil.copyPage(entityIPage, ParityTransferVO.class);
        setData(parityTransferVOIPage.getRecords());
        return parityTransferVOIPage;
    }

    /**
     * 设置数据
     * @param list
     */
    private void setData(List<ParityTransferVO> list) {
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        Map<String, String> companyMap = iOrgCompanyService.selectAllOrgIdAndName().stream().collect(Collectors.toMap(e -> e.getOrgId(), e -> e.getOrgName(), (a, b) -> b));
        list.stream().forEach(a->{
            // 翻译签约主体
            a.setTransferPartyName(companyMap.get(a.getTransferParty()));
            a.setTransfereePartyName(companyMap.get(a.getTransfereeParty()));
        });

    }

    @Override
    public List<String> batchList() {
        return this.list().stream().map(ParityTransferEntity::getBatch).distinct().collect(Collectors.toList());
    }

    @Override
    public List<ParityTransferExportVO> selectPartityTransferList(List<Long> idList) {
        List<ParityTransferEntity> parityTransferEntityList = this.listByIds(idList);
        List<ParityTransferVO> list = BeanUtil.copyToList(parityTransferEntityList, ParityTransferVO.class);
        setData(list);
        return BeanUtil.copyToList(list, ParityTransferExportVO.class);
    }

    @Override
    public List<ParityTransferDetailExportVO> selectPartityTransferDetailList(List<Long> idList) {
        List<ParityTransferDetailEntity> detailEntityList = iParityTransferDetailService.lambdaQuery().in(ParityTransferDetailEntity::getParityTransferId, idList).orderByDesc(ParityTransferDetailEntity::getCreateTime).list();
        return BeanUtil.copyToList(detailEntityList, ParityTransferDetailExportVO.class);
    }

    public LambdaQueryWrapper<ParityTransferEntity> getQueryWrapper(ParityTransferQueryDTO queryDTO) {
        LambdaQueryWrapper<ParityTransferEntity> queryWrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotEmpty(queryDTO.getBatch())) {
            queryWrapper.eq(ParityTransferEntity::getBatch, queryDTO.getBatch());
        }
        if (StringUtils.isNotEmpty(queryDTO.getTransferParty())) {
            queryWrapper.eq(ParityTransferEntity::getTransferParty, queryDTO.getTransferParty());
        }
        if (StringUtils.isNotEmpty(queryDTO.getTransfereeParty())) {
            queryWrapper.eq(ParityTransferEntity::getTransfereeParty, queryDTO.getTransfereeParty());
        }
        if (CollectionUtils.isNotEmpty(queryDTO.getBatchList())) {
            queryWrapper.in(ParityTransferEntity::getBatch, queryDTO.getBatchList());
        }
        if (CollectionUtils.isNotEmpty(queryDTO.getTransferPartyList())) {
            queryWrapper.in(ParityTransferEntity::getTransferParty, queryDTO.getTransferPartyList());
        }
        if (CollectionUtils.isNotEmpty(queryDTO.getTransfereePartyList())) {
            queryWrapper.in(ParityTransferEntity::getTransfereeParty, queryDTO.getTransfereePartyList());
        }
        if (ObjectUtils.isNotNull(queryDTO.getStartAccountDate())) {
            queryWrapper.ge(ParityTransferEntity::getAccountDate, queryDTO.getStartAccountDate());
        }
        if (ObjectUtils.isNotNull(queryDTO.getEndAccountDate())) {
            queryWrapper.le(ParityTransferEntity::getAccountDate, queryDTO.getEndAccountDate());
        }
        if (ObjectUtils.isNotNull(queryDTO.getId())) {
            queryWrapper.eq(ParityTransferEntity::getId, queryDTO.getId());
        }
        queryWrapper.orderByDesc(ParityTransferEntity::getId);
        return queryWrapper;
    }

    @Override
    public Boolean importTemplate(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        InputStream inputStream2 = file.getInputStream();
        try {
            ExcelUtil<ParityTransferExcelVO> util = new ExcelUtil<ParityTransferExcelVO>(ParityTransferExcelVO.class);
            ExcelUtil<ParityTransferDetailExcelVO> util2 = new ExcelUtil<ParityTransferDetailExcelVO>(ParityTransferDetailExcelVO.class);
            List<ParityTransferExcelVO> transferExcelVOList = util.importExcel("平价转让基本信息", inputStream, 0);
            List<ParityTransferDetailExcelVO> detailExcelVOList = util2.importExcel("平价转让详情", inputStream2, 0);
            List<ParityTransferDetailVO> detailVOList = checkData(transferExcelVOList, detailExcelVOList);
            List<ParityTransferEntity> parityTransferEntityList = BeanUtil.copyToList(transferExcelVOList, ParityTransferEntity.class);
            // 填充余额表信息
            parityTransferEntityList.stream().forEach(v -> {
                v.setBusinessDate(LocalDateTime.now());
                v.setAccountDate(v.getTradeDate());//交易日就是记账日期
                this.save(v);
                List<ParityTransferDetailEntity> detailEntityList = BeanUtil.copyToList(detailVOList, ParityTransferDetailEntity.class);
                // 保存详情信息
                saveDetail(detailEntityList, v);
            });
            return Boolean.TRUE;
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(inputStream2);
        }
    }

    /**
     * 上传支付信息
     *
     * @param file
     * @return
     */
    @Override
    public Boolean importPaymentInfo(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        try {
            ExcelUtil<ParityTransferPaymentInfoExcelDTO> util = new ExcelUtil<ParityTransferPaymentInfoExcelDTO>(ParityTransferPaymentInfoExcelDTO.class);
            List<ParityTransferPaymentInfoExcelDTO> list = util.importExcel("平价转让支付信息", inputStream, 0);
            checkPaymentInfoData(list);
            list.stream().forEach(a->{
                List<ParityTransferEntity> entityList = list(new LambdaQueryWrapper<ParityTransferEntity>().eq(ParityTransferEntity::getBatch, a.getBatch()));
                if (CollectionUtils.isEmpty(entityList)) {
                    throw new ServiceException("根据批次号[" + a.getBatch() + "]未查询到平价转让数据");
                }
                entityList.stream().forEach(b -> {
                    b.setPaymentDate(DateUtil.toLocalDateTime(a.getPaymentDate()));
                    b.setBankAccountCode(a.getBankAccountCode());
                });
                // 更新数据
                updateBatchById(entityList);
            });
            return Boolean.TRUE;
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    /**
     * 校验上传的支付信息
     * @param list
     */
    private void checkPaymentInfoData(List<ParityTransferPaymentInfoExcelDTO> list) {
        Set<String> unionSet = new HashSet<>();
        list.stream().forEach(a -> {
            if (ObjectUtil.isEmpty(a.getBatch())) {
                throw new ServiceException("转让批次不能为空");
            }
            if (ObjectUtil.isEmpty(a.getPaymentDate())) {
                throw new ServiceException("支付日期不能为空");
            }
            if (ObjectUtil.isEmpty(a.getBankAccountCode())) {
                throw new ServiceException("银行账号编码不能为空");
            }
            // 校验批次号重复
            if (!unionSet.add(a.getBatch())) {
                throw new ServiceException("文件存在重复的批次号[" + a.getBatch() + "],请检查");
            }
            // 校验 银行账号
            List<BankAccountEntity> bankAccountEntityList = iBankAccountService.selectByBankAccountCode(a.getBankAccountCode());
            if (CollectionUtils.isEmpty(bankAccountEntityList)) {
                throw new ServiceException("根据银行账号编码[" + a.getBankAccountCode() + "]未查询到银行账号数据");
            }
        });
    }

    @Override
    public Boolean deleteByIds(List<Long> idList) {
        if (CollectionUtil.isEmpty(idList)) {
            throw new ServiceException("请至少勾选一条数据删除");
        }
        List<ParityTransferEntity> parityTransferEntityList = this.listByIds(idList);
        parityTransferEntityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("处理状态为已录入的才可以删除");
            }
        });
        this.removeBatchByIds(idList);
        batchDeleteVoucher(idList);
        return iParityTransferDetailService.removeBatcheByDetailId(idList);
    }

    public void batchDeleteVoucher(List<Long> ids) {
        // 获取所有的凭证Id
        List<ParityTransferDetailEntity> detailsEntityList = iParityTransferDetailService.lambdaQuery().in(ParityTransferDetailEntity::getParityTransferId, ids).list();
        // 逗号拆分
        List<Long> voucherIdList = Lists.newArrayList();
        detailsEntityList.forEach(v -> {
            if (StringUtils.isNotEmpty(v.getVoucherIds())) {
                voucherIdList.addAll(Arrays.stream(v.getVoucherIds().split(",")).map(Long::parseLong).collect(Collectors.toList()));
            }
            v.setVoucherIds("");
        });
        if (CollectionUtil.isNotEmpty(voucherIdList)) {
            iVoucherService.deleteByIdList(voucherIdList);
        }
        iParityTransferDetailService.updateBatchById(detailsEntityList);
    }

    @Override
    public Boolean submit(List<Long> idList) {
        if (com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请至少勾选一条数据提交");
        }
        List<ParityTransferEntity> parityTransferEntityList = this.listByIds(idList);
        List<ApproveDTO> approveDTOList = Lists.newArrayList();
        List<Long> voucherIdList = Lists.newArrayList();
        parityTransferEntityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("只有处理状态为已录入的才可以提交");
            }
            if(ObjectUtil.notEqual(v.getIsGenerateVoucher(),YesOrNoEnum.YES.getCode())){
                throw new ServiceException("未生成凭证，不能提交");
            }
            ApproveDTO approveDTO = new ApproveDTO();
            approveDTO.setDocumentId(v.getId());
            approveDTO.setDocumentType(BatchTypeEnum.NBZR.getCode());
            approveDTO.setUrl(approveUrl + v.getId());
            approveDTOList.add(approveDTO);

            if (StringUtils.isNotEmpty(v.getVoucherIds())) {
                voucherIdList.addAll(Arrays.stream(v.getVoucherIds().split(",")).map(Long::parseLong).collect(Collectors.toList()));
            }
        });

        //不重新生成凭证，用原来的凭证提交
        iVoucherService.commitVoucherList(voucherIdList);

        // 更新合同表财务合同状态
        updateContractStatus(idList);
        // 生成新的合同数据
        generateContract(idList);
        List<ParityTransferEntity> newEntityList = this.listByIds(idList);
        // 发送审核
        Map<Long, Long> processInstantIdMap = iApproveService.submit(approveDTOList);
        newEntityList.forEach(v -> {
            if (null != processInstantIdMap && processInstantIdMap.containsKey(v.getId())) {
                v.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
                v.setProcessInstanceId(processInstantIdMap.get(v.getId()));
            }
        });
        return this.updateBatchById(newEntityList);
    }

    /**
     * 生成新的合同数据
     *
     * @param idList
     */
    private void generateContract(List<Long> idList) {
        List<ParityTransferEntity> parityTransferEntityList = listByIds(idList);
        List<ParityTransferDetailEntity> list = iParityTransferDetailService.list(new LambdaQueryWrapper<ParityTransferDetailEntity>()
                .in(ParityTransferDetailEntity::getParityTransferId, idList));
        for (ParityTransferDetailEntity detail : list) {
            ParityTransferEntity parityTransferEntity = parityTransferEntityList.stream().filter(a -> ObjectUtil.equals(a.getId(), detail.getParityTransferId())).findFirst().get();
            iContractService.copyContract(detail.getContractCode(), detail.getOrgId(), detail.getContractCode(), parityTransferEntity.getTransfereeParty());
        }
    }

    /**
     * 更新合同表财务合同状态
     *
     * @param idList
     */
    private void updateContractStatus(List<Long> idList) {
        List<ParityTransferDetailEntity> list = iParityTransferDetailService.list(new LambdaQueryWrapper<ParityTransferDetailEntity>()
                .in(ParityTransferDetailEntity::getParityTransferId, idList));
        for (ParityTransferDetailEntity detail : list) {
            // 更新财务合同状态
            iContractService.updateContractFinancialStatus(detail.getContractCode(), detail.getOrgId(), detail.getFinancialContractStatus());
        }
    }

    @Override
    public Boolean withdraw(List<Long> idList) {
        if (CollectionUtil.isEmpty(idList)) {
            throw new ServiceException("请至少勾选一条数据撤回");
        }
        List<ParityTransferEntity> parityTransferEntityList = this.listByIds(idList);
        parityTransferEntityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.SUBMITTED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("只有处理状态为已提交的才可以撤回");
            }
            v.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
            v.setVoucherIds("");
            v.setIsGenerateVoucher(YesOrNoEnum.NO.getCode());
        });
        iApproveService.withdraw(parityTransferEntityList.stream().map(ParityTransferEntity::getProcessInstanceId).collect(Collectors.toList()));
        // 删除凭证
        batchDeleteVoucher(idList);
        return this.updateBatchById(parityTransferEntityList);
    }

    @Override
    public Boolean generateVoucher(List<Long> ids, String isSubmit) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少选择一条数据生成凭证");
        }
        List<ParityTransferEntity> parityTransferEntityList = this.listByIds(ids);
        parityTransferEntityList.stream().forEach(v -> {
            if (!(ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus()) || ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus()))) {
                throw new ServiceException("处理状态为已录入或者已拒绝的才可以生成凭证");
            }
        });

        //生成凭证前先删除之前的凭证
        batchDeleteVoucher(ids);

        Boolean isSuccess = Boolean.TRUE;
        StringBuffer errorInfoStr = new StringBuffer();
        for (ParityTransferEntity parityTransferEntity : parityTransferEntityList) {
            List<ParityTransferDetailEntity> entityList = iParityTransferDetailService.getByParityTransferId(parityTransferEntity.getId());
            // 合同编码+签约主体+凭证日期大于基准日期获取数据
            List<String> contractList = entityList.stream().map(ParityTransferDetailEntity::getContractCode).collect(Collectors.toList());
            List<String> orgIdList = entityList.stream().map(ParityTransferDetailEntity::getOrgId).distinct().collect(Collectors.toList());
            List<String> clientCodeList = entityList.stream().filter(s -> StringUtils.isNotEmpty(s.getClientCode())).map(ParityTransferDetailEntity::getClientCode).distinct().collect(Collectors.toList());
            List<ContractBalanceVO> contractBalanceVOList = ListBeanUtil.copyList(iContractBalanceService.list(new LambdaQueryWrapper<ContractBalanceEntity>()
                            .in(ContractBalanceEntity::getContractCode, contractList)
                            .in(ContractBalanceEntity::getOrgId, orgIdList)
                            .apply("to_char(voucher_date,'YYYY-MM-DD')>={0}", DateUtil.format(parityTransferEntity.getReferenceDate(), "yyyy-MM-dd")))
                    , ContractBalanceVO.class);

            List<Map<String, Object>> voucherMapList = Lists.newArrayList();
            entityList.stream().forEach(v -> {
                ExecuteCommonDTO executeCommonDTO = new ExecuteCommonDTO();
                executeCommonDTO.setSystemCode(SystemEnum.CWZT.getCode());
                executeCommonDTO.setSystemName(SystemEnum.CWZT.getDesc());
                executeCommonDTO.setSceneCode(SceneEnum.PJZR.getCode());
                executeCommonDTO.setSceneName(SceneEnum.PJZR.getDesc());
                executeCommonDTO.setOrderId(v.getId().toString());
                executeCommonDTO.setOrgId(v.getOrgId());
                // businessDate改为取页面记账日期，即上传的交易日
                executeCommonDTO.setBusinessDate(DateUtils.toDate(parityTransferEntity.getTradeDate()));
                executeCommonDTO.setContractCode(v.getContractCode());
                executeCommonDTO.setClientCode(v.getClientCode());// 转让详情客户名称对应的科目编号
                executeCommonDTO.setAccountDate(DateUtils.toDate(parityTransferEntity.getAccountDate()));
                executeCommonDTO.setBatchId(v.getId());
                executeCommonDTO.setBatchType(BatchTypeEnum.NBZR.getCode());
                executeCommonDTO.setIsSubmit(isSubmit);

                Map<String, Object> dataMap = BeanUtil.beanToMap(executeCommonDTO);
                dataMap.put("transferType", SceneEnum.PJZR.getDesc());
                dataMap.put("transferor", parityTransferEntity.getTransferParty());
                dataMap.put("transferee", parityTransferEntity.getTransfereeParty());
                dataMap.put("transferBatch", v.getBatch());
                dataMap.put("estimatePrice", v.getAppraisedValue());
                dataMap.put("receivableLeaseAmount", v.getReceivableRent());
                dataMap.put("retainedPrice", v.getReceivableResidualValue());
                dataMap.put("receivableOuttaxAmount", v.getReceivableOuttax());
                dataMap.put("unrealizedRevenueAmount", v.getUnrealizedRevenue());
                dataMap.put("receivableMarginAmount", v.getLesseeMargin());
                dataMap.put("payableAgencyEstimateAmount", v.getPayableAgencyEstimate());
                dataMap.put("payableVehicleEstimateAmount", v.getPayableVehicleEstimate());
                dataMap.put("payableBandCostEstimateAmount", v.getPayableBandCostEstimate());
                dataMap.put("payablePledgeEstimateAmount", v.getPayablePledgeEstimate());
                dataMap.put("payableUnpledgeEstimateAmount", v.getPayableUnpledgeEstimate());
                dataMap.put("payableOtherCostEstimateAmount", v.getPayableOtherCostEstimate());
                dataMap.put("depreciationReserves", v.getDepreciationReserves());

                List<ContractBalanceVO> jyjgbgBalanceVOList = contractBalanceVOList.stream().filter(a -> ObjectUtil.equals(a.getContractCode(), v.getContractCode()) && ObjectUtil.equals(a.getOrgId(), v.getOrgId())
                        && ObjectUtil.equals(a.getSceneCode(), SceneEnum.JYJGBG.getCode())).collect(Collectors.toList());

                dataMap.put("receivableLeaseAdjustAmount", jyjgbgBalanceVOList.stream().map(ContractBalanceVO::getReceivableRentAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("firstAdjustAmount", jyjgbgBalanceVOList.stream().map(ContractBalanceVO::getReceivableDownpaymentAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("procedureAdjustRevenues", jyjgbgBalanceVOList.stream().map(ContractBalanceVO::getReceivableCommissionAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("insuranceAdjustAmount", jyjgbgBalanceVOList.stream().map(ContractBalanceVO::getReceivableInsuranceAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("payableInsuranceEstimateAdjustAmount", jyjgbgBalanceVOList.stream().map(ContractBalanceVO::getPayableInsuranceEstimateAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("residualAdjustAmount", jyjgbgBalanceVOList.stream().map(ContractBalanceVO::getReceivableResidualValueAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("otherAdjustRevenues", jyjgbgBalanceVOList.stream().map(ContractBalanceVO::getReceivableOtherincomeAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("payableDeviceAdjustAmount", jyjgbgBalanceVOList.stream().map(ContractBalanceVO::getPayableDeviceEstimateAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("otherCostAdjustAmount", jyjgbgBalanceVOList.stream().map(ContractBalanceVO::getPayableOtherCostEstimateAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("payableServiceAdjustAmount", jyjgbgBalanceVOList.stream().map(ContractBalanceVO::getPayableAgencyEstimateAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("payableBraceletAdjustAmount", jyjgbgBalanceVOList.stream().map(ContractBalanceVO::getPayableBandCostEstimateAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("receivableOuttaxAdjustAmount", jyjgbgBalanceVOList.stream().map(ContractBalanceVO::getReceivableOuttaxAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("unrealizedRevenueAdjustAmount", jyjgbgBalanceVOList.stream().map(ContractBalanceVO::getUnrealizedRevenueAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("serviceAdjustAmount", jyjgbgBalanceVOList.stream().map(ContractBalanceVO::getReceivableServiceAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("receivableServiceOuttaxAdjustAmount", jyjgbgBalanceVOList.stream().map(ContractBalanceVO::getReceivableServiceOuttaxAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("serviceEevenueAdjustAmount", jyjgbgBalanceVOList.stream().map(ContractBalanceVO::getServiceRevenueAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("outtaxAdjustAmount", jyjgbgBalanceVOList.stream().map(ContractBalanceVO::getOuttaxAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("receiveSumAdjustAmount", jyjgbgBalanceVOList.stream().map(ContractBalanceVO::getReceiveSumAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("receiveSumOuttaxAdjustAmount", jyjgbgBalanceVOList.stream().map(ContractBalanceVO::getReceiveSumOuttaxAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("payableOtherEstimateAdjustAmount", jyjgbgBalanceVOList.stream().map(ContractBalanceVO::getPayableOtherEstimateAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("receiveUnrealizedRevenueAdjustAmount", jyjgbgBalanceVOList.stream().map(ContractBalanceVO::getReceiveUnrealizedRevenueAmount).reduce(BigDecimal.ZERO, BigDecimal::add));

                List<ContractBalanceVO> zlskBalanceVOList = contractBalanceVOList.stream().filter(a -> ObjectUtil.equals(a.getContractCode(), v.getContractCode()) && ObjectUtil.equals(a.getOrgId(), v.getOrgId())
                        && ObjectUtil.equals(a.getSceneCode(), SceneEnum.ZLSK.getCode())).collect(Collectors.toList());
                dataMap.put("receiveUnconfirmed", zlskBalanceVOList.stream().map(ContractBalanceVO::getReceivableUnconfirmReceiptAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                // 场景为ZLSK，余额表凭证日期>=基准日，lessee_margin_amount<=0时，receivable_rent_amount的汇总金额
                dataMap.put("receiveLeaseAmount", zlskBalanceVOList.stream().filter(a -> NumberUtil.toBigDecimal(a.getLesseeMarginAmount()).compareTo(BigDecimal.ZERO) < 1)
                        .map(ContractBalanceVO::getReceivableUnconfirmReceiptAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("receiveRetainedPrice", zlskBalanceVOList.stream().filter(a -> NumberUtil.toBigDecimal(a.getLesseeMarginAmount()).compareTo(BigDecimal.ZERO) < 1)
                        .map(ContractBalanceVO::getReceivableResidualValueAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("receiveMarginAmount", zlskBalanceVOList.stream().filter(a -> NumberUtil.toBigDecimal(a.getLesseeMarginAmount()).compareTo(BigDecimal.ZERO) < 1)
                        .map(ContractBalanceVO::getLesseeMarginAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("receiveSupplierMarginAmount", zlskBalanceVOList.stream().filter(a -> NumberUtil.toBigDecimal(a.getLesseeMarginAmount()).compareTo(BigDecimal.ZERO) < 1)
                        .map(ContractBalanceVO::getSupplierMarginAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("receiveDownpaymentAmount", zlskBalanceVOList.stream().filter(a -> NumberUtil.toBigDecimal(a.getLesseeMarginAmount()).compareTo(BigDecimal.ZERO) < 1)
                        .map(ContractBalanceVO::getReceivableDownpaymentAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("receiveCommissionAmount", zlskBalanceVOList.stream().filter(a -> NumberUtil.toBigDecimal(a.getLesseeMarginAmount()).compareTo(BigDecimal.ZERO) < 1)
                        .map(ContractBalanceVO::getReceivableCommissionAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("receivesServiceAmount", zlskBalanceVOList.stream().filter(a -> NumberUtil.toBigDecimal(a.getLesseeMarginAmount()).compareTo(BigDecimal.ZERO) < 1)
                        .map(ContractBalanceVO::getReceivableServiceAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("receiveInsuranceAmount", zlskBalanceVOList.stream().filter(a -> NumberUtil.toBigDecimal(a.getLesseeMarginAmount()).compareTo(BigDecimal.ZERO) < 1)
                        .map(ContractBalanceVO::getReceivableInsuranceAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("receiveInsuranceDifferAmount", zlskBalanceVOList.stream().filter(a -> NumberUtil.toBigDecimal(a.getLesseeMarginAmount()).compareTo(BigDecimal.ZERO) < 1)
                        .map(ContractBalanceVO::getInsuranceDifferAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("receiveOtherincomeAmount", zlskBalanceVOList.stream().filter(a -> NumberUtil.toBigDecimal(a.getLesseeMarginAmount()).compareTo(BigDecimal.ZERO) < 1)
                        .map(ContractBalanceVO::getReceivableOtherincomeAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("receiveDamagesRevenueAmount", zlskBalanceVOList.stream().filter(a -> NumberUtil.toBigDecimal(a.getLesseeMarginAmount()).compareTo(BigDecimal.ZERO) < 1)
                        .map(ContractBalanceVO::getReceivableDamagesRevenueAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("receiveOtherRevenueAmount", zlskBalanceVOList.stream().filter(a -> NumberUtil.toBigDecimal(a.getLesseeMarginAmount()).compareTo(BigDecimal.ZERO) < 1)
                        .map(ContractBalanceVO::getReceivableOtherRevenueAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("receiveDefaultInterestAmount", zlskBalanceVOList.stream().filter(a -> NumberUtil.toBigDecimal(a.getLesseeMarginAmount()).compareTo(BigDecimal.ZERO) < 1)
                        .map(ContractBalanceVO::getReceivableDefaultInterestAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("receiveTerminateProcedureAmount", zlskBalanceVOList.stream().filter(a -> NumberUtil.toBigDecimal(a.getLesseeMarginAmount()).compareTo(BigDecimal.ZERO) < 1)
                        .map(ContractBalanceVO::getReceivableTerminateAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("dinterestRevenueAmount", zlskBalanceVOList.stream().filter(a -> NumberUtil.toBigDecimal(a.getLesseeMarginAmount()).compareTo(BigDecimal.ZERO) < 1)
                        .map(ContractBalanceVO::getDinterestRevenueAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("terminateAmount", zlskBalanceVOList.stream().filter(a -> NumberUtil.toBigDecimal(a.getLesseeMarginAmount()).compareTo(BigDecimal.ZERO) < 1)
                        .map(ContractBalanceVO::getTerminateAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("damagesRevenueAmount", zlskBalanceVOList.stream().filter(a -> NumberUtil.toBigDecimal(a.getLesseeMarginAmount()).compareTo(BigDecimal.ZERO) < 1)
                        .map(ContractBalanceVO::getDamagesRevenueAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("otherRevenueAmount", zlskBalanceVOList.stream().filter(a -> NumberUtil.toBigDecimal(a.getLesseeMarginAmount()).compareTo(BigDecimal.ZERO) < 1)
                        .map(ContractBalanceVO::getOtherRevenueAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("receivableOuttaxAmountJT", zlskBalanceVOList.stream().filter(a -> NumberUtil.toBigDecimal(a.getLesseeMarginAmount()).compareTo(BigDecimal.ZERO) < 1)
                        .map(ContractBalanceVO::getReceivableOuttaxAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("deductionMarginAmount", zlskBalanceVOList.stream().filter(a -> NumberUtil.toBigDecimal(a.getLesseeMarginAmount()).compareTo(BigDecimal.ZERO) == 1)
                        .map(ContractBalanceVO::getLesseeMarginAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("deductionLeaseAmount", zlskBalanceVOList.stream().filter(a -> NumberUtil.toBigDecimal(a.getLesseeMarginAmount()).compareTo(BigDecimal.ZERO) == 1)
                        .map(ContractBalanceVO::getReceivableRentAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("deductionRetainedPrice", zlskBalanceVOList.stream().filter(a -> NumberUtil.toBigDecimal(a.getLesseeMarginAmount()).compareTo(BigDecimal.ZERO) == 1)
                        .map(ContractBalanceVO::getReceivableResidualValueAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("deductionDefaultInterestAmount", zlskBalanceVOList.stream().filter(a -> NumberUtil.toBigDecimal(a.getLesseeMarginAmount()).compareTo(BigDecimal.ZERO) == 1)
                        .map(ContractBalanceVO::getReceivableDefaultInterestAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("deductionTerminateProcedureAmount", zlskBalanceVOList.stream().filter(a -> NumberUtil.toBigDecimal(a.getLesseeMarginAmount()).compareTo(BigDecimal.ZERO) == 1)
                        .map(ContractBalanceVO::getReceivableTerminateAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("deductionDefaultNoTaxAmount", zlskBalanceVOList.stream().filter(a -> NumberUtil.toBigDecimal(a.getLesseeMarginAmount()).compareTo(BigDecimal.ZERO) == 1)
                        .map(ContractBalanceVO::getDinterestRevenueAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("deductionTerminateNoTaxAmount", zlskBalanceVOList.stream().filter(a -> NumberUtil.toBigDecimal(a.getLesseeMarginAmount()).compareTo(BigDecimal.ZERO) == 1)
                        .map(ContractBalanceVO::getTerminateAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("deductionreceivableOuttaxAmountJT", zlskBalanceVOList.stream().filter(a -> NumberUtil.toBigDecimal(a.getLesseeMarginAmount()).compareTo(BigDecimal.ZERO) == 1)
                        .map(ContractBalanceVO::getReceivableOuttaxAmount).reduce(BigDecimal.ZERO, BigDecimal::add));

                List<ContractBalanceVO> kjfpBalanceVOList = contractBalanceVOList.stream().filter(a -> ObjectUtil.equals(a.getContractCode(), v.getContractCode()) && ObjectUtil.equals(a.getOrgId(), v.getOrgId())
                        && ObjectUtil.equals(a.getSceneCode(), SceneEnum.KJFP.getCode())).collect(Collectors.toList());
                dataMap.put("receivableOuttaxAmountKP", kjfpBalanceVOList.stream().map(ContractBalanceVO::getReceivableOuttaxAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("receivableServiceOuttaxAmountKP", kjfpBalanceVOList.stream().map(ContractBalanceVO::getReceivableServiceOuttaxAmount).reduce(BigDecimal.ZERO, BigDecimal::add));

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
            List<String> voucherIdsList = Lists.newArrayList();
            String isGenerateVoucherHead = YesOrNoEnum.YES.getCode();
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
                    isGenerateVoucherHead = YesOrNoEnum.NO.getCode();
                    isSuccess = Boolean.FALSE;
                }
                errorInfoStr.append(errorInfo);
                if (CollectionUtils.isNotEmpty(infoVO.getVoucherDTOList())) {
                    voucherIds = infoVO.getVoucherDTOList().stream().map(VoucherDTO::getId).map(String::valueOf).collect(Collectors.toList()).stream().collect(Collectors.joining(","));
                    voucherIdsList.add(voucherIds);
                }

                ParityTransferDetailEntity parityTransferDetailEntity = iParityTransferDetailService.getById(Long.parseLong(infoVO.getOrderId()));
                parityTransferDetailEntity.setErrorInfo(errorInfo);
                parityTransferDetailEntity.setVoucherIds(voucherIds);
                parityTransferDetailEntity.setUpdateTime(null);
                iParityTransferDetailService.updateById(parityTransferDetailEntity);
            }
            String voucherIdHead = voucherIdsList.stream().collect(Collectors.joining(","));

            // 更新头上的凭证
            lambdaUpdate().set(ParityTransferEntity::getIsGenerateVoucher, isGenerateVoucherHead)
                    .set(ParityTransferEntity::getVoucherIds, voucherIdHead)
                    .eq(ParityTransferEntity::getId, parityTransferEntity.getId()).update();
        }


        if (!isSuccess) {
            log.info("平价转让生成凭证错误：{}", errorInfoStr);
        }
        return isSuccess;
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

    @Override
    public Boolean updateProcessStatus(CommonApproveDTO approveDTO) {
        if (StringUtils.isEmpty(approveDTO.getDocumentStatus())) {
            throw new ServiceException("处理状态不可以为空");
        }
        ParityTransferEntity parityTransferEntity = this.getById(approveDTO.getDocumentId());
        if (null == parityTransferEntity) {
            throw new ServiceException("平价转让数据不存在");
        }
        String processStatus = parityTransferEntity.getProcessStatus();
        try {
            if (ProcessStatusEnum.REVIEWED.getCode().equals(approveDTO.getDocumentStatus())) {
                processStatus = ProcessStatusEnum.REVIEWED.getCode();
            } else if (ProcessStatusEnum.REJECTED.getCode().equals(approveDTO.getDocumentStatus())) {
                processStatus = ProcessStatusEnum.REJECTED.getCode();
            }
            // 修改凭证状态，通过和驳回都修改
            updateVoucherStatus(Lists.newArrayList(approveDTO.getDocumentId()), approveDTO);
            parityTransferEntity.setProcessStatus(processStatus);
            parityTransferEntity.setApproveErrorInfo("");
        } catch (Exception e) {
            parityTransferEntity.setApproveErrorInfo(e.getMessage());
        }
        return this.updateById(parityTransferEntity);
    }

    /**
     * 更新凭证状态
     *
     * @param ids
     * @param approveDTO
     */
    public void updateVoucherStatus(List<Long> ids, CommonApproveDTO approveDTO){
        // 获取所有的凭证Id
        List<ParityTransferEntity> entityList = this.listByIds(ids);
        // 逗号拆分
        List<String> voucherIdList = Lists.newArrayList();
        entityList.stream().forEach(v -> {
            if (StringUtils.isNotEmpty(v.getVoucherIds())) {
                voucherIdList.addAll(Arrays.stream(v.getVoucherIds().split(",")).collect(Collectors.toList()));
            }
        });
        iVoucherService.updateStatusByids(voucherIdList, approveDTO.getDocumentStatus(),
                approveDTO.getApproverNum(), approveDTO.getApproverName());
    }

    /**
     * 校验
     *
     * @param queryDTO
     * @return
     */
    @Override
    public IPage<ParityTransferCheckVO> selectCheckPage(CheckPageQueryDTO queryDTO) {
        ParityTransferDetailQueryDTO parityTransferDetailQueryDTO = new ParityTransferDetailQueryDTO();
        parityTransferDetailQueryDTO.setParityTransferId(queryDTO.getId());
        IPage<ParityTransferDetailVO> parityTransferDetailVOIPage = iParityTransferDetailService.selectPage(parityTransferDetailQueryDTO);
        List<ParityTransferDetailVO> parityTransferDetailVOList = parityTransferDetailVOIPage.getRecords();
        if (CollectionUtils.isEmpty(parityTransferDetailVOList)) {
            throw new ServiceException("没有平价转让详情数据");
        }
        List<String> orgIdList = parityTransferDetailVOList.stream().map(ParityTransferDetailVO::getOrgId).filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
        List<String> contractCodeList = parityTransferDetailVOList.stream().map(ParityTransferDetailVO::getContractCode).filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
        List<String> clientCodeList = parityTransferDetailVOList.stream().map(ParityTransferDetailVO::getClientCode).filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());


        List<ContractBalanceLatestEntity> contractBalanceLatestEntityList = iContractBalanceLatestService.list(new LambdaQueryWrapper<ContractBalanceLatestEntity>()
                .in(ContractBalanceLatestEntity::getContractCode, contractCodeList)
                .in(ContractBalanceLatestEntity::getOrgId, orgIdList)
        );
        List<ContractBalanceTempEntity> contractBalanceTempEntityList = iContractBalanceTempService.list(new LambdaQueryWrapper<ContractBalanceTempEntity>()
                .in(ContractBalanceTempEntity::getContractCode, contractCodeList)
                .in(ContractBalanceTempEntity::getOrgId, orgIdList)
                .eq(ContractBalanceTempEntity::getSceneCode, SceneEnum.PJZR.getCode())
        );
        // 查询客户
        List<ClientEntity> clientEntityList = iClientService.selectByClientCodeList(clientCodeList);
        List<ParityTransferCheckVO> list = Lists.newArrayList();
        parityTransferDetailVOList.stream().forEach(a -> {
            ParityTransferCheckVO checkVO = new ParityTransferCheckVO();
            checkVO.setContractCode(a.getContractCode());
            checkVO.setClientCode(a.getClientCode());
            checkVO.setOrgId(a.getOrgId());
            ClientEntity clientEntity = clientEntityList.stream().filter(b -> ObjectUtil.equals(a.getClientCode(), b.getClientCode())).findFirst().orElse(new ClientEntity());
            checkVO.setClientName(clientEntity.getClientName());

            // 1.合同+签约主体+客户查最新余额表对应balance
            ContractBalanceLatestEntity contractBalanceLatestEntity = contractBalanceLatestEntityList.stream().filter(b -> ObjectUtil.equals(a.getContractCode(), b.getContractCode())
                            && ObjectUtil.equals(a.getOrgId(), b.getOrgId()) && ObjectUtil.equals(a.getClientCode(), b.getClientCode()))
                    .max(Comparator.comparing(ContractBalanceLatestEntity::getId)).orElse(new ContractBalanceLatestEntity());
            // 2.合同+签约主体+客户查临时余额表场景为NBZR，interfaceid最大时对应amount
            ContractBalanceTempEntity contractBalanceTempEntity = contractBalanceTempEntityList.stream().filter(b -> ObjectUtil.equals(a.getContractCode(), b.getContractCode())
                            && ObjectUtil.equals(a.getOrgId(), b.getOrgId()) && ObjectUtil.equals(a.getClientCode(), b.getClientCode()))
                    .max(Comparator.comparing(ContractBalanceTempEntity::getInterfaceDataId)).orElse(new ContractBalanceTempEntity());
            // 1+2
            checkVO.setReceivableRent(NumberUtil.add(contractBalanceLatestEntity.getReceivableRentBalance(), NumberUtil.toBigDecimal(contractBalanceTempEntity.getReceivableRentAmount())));
            checkVO.setReceivableResidualValue(NumberUtil.add(contractBalanceLatestEntity.getReceivableResidualValueBalance(), NumberUtil.toBigDecimal(contractBalanceTempEntity.getReceivableResidualValueAmount())));
            checkVO.setReceivableOuttax(NumberUtil.add(contractBalanceLatestEntity.getReceivableOuttaxBalance(), NumberUtil.toBigDecimal(contractBalanceTempEntity.getReceivableOuttaxAmount())));
            checkVO.setLesseeMargin(NumberUtil.add(contractBalanceLatestEntity.getReceivableOuttaxBalance(), NumberUtil.toBigDecimal(contractBalanceTempEntity.getReceivableOuttaxAmount())));

            // 1.合同+签约主体查最新余额表对应balance
            ContractBalanceLatestEntity contractBalanceLatestEntity2 = contractBalanceLatestEntityList.stream().filter(b -> ObjectUtil.equals(a.getContractCode(), b.getContractCode())
                            && ObjectUtil.equals(a.getOrgId(), b.getOrgId()))
                    .max(Comparator.comparing(ContractBalanceLatestEntity::getId)).orElse(new ContractBalanceLatestEntity());
            // 2.合同+签约主体查临时余额表场景为NBZR，interfaceid最大时对应amount
            ContractBalanceTempEntity contractBalanceTempEntity2 = contractBalanceTempEntityList.stream().filter(b -> ObjectUtil.equals(a.getContractCode(), b.getContractCode())
                            && ObjectUtil.equals(a.getOrgId(), b.getOrgId()))
                    .max(Comparator.comparing(ContractBalanceTempEntity::getInterfaceDataId)).orElse(new ContractBalanceTempEntity());
            checkVO.setUnrealizedRevenue(NumberUtil.add(contractBalanceLatestEntity2.getUnrealizedRevenueBalance(), NumberUtil.toBigDecimal(contractBalanceTempEntity2.getUnrealizedRevenueAmount())));


            list.add(checkVO);

        });
        IPage<ParityTransferCheckVO> parityTransferVOIPage = ListBeanUtil.copyPage(parityTransferDetailVOIPage, ParityTransferCheckVO.class);
        parityTransferVOIPage.setRecords(list);
        return parityTransferVOIPage;
    }

    @Override
    public IPage<ParityTransferDetailVO> detailPage(ParityTransferDetailQueryDTO queryDTO) {
        IPage<ParityTransferDetailVO> resultPage = iParityTransferDetailService.selectPage(queryDTO);
        Map<String, String> orgIdMap = iOrgCompanyService.list().stream().collect(HashMap::new, (h, v) -> h.put(v.getOrgId(), v.getOrgName()), HashMap::putAll);
        resultPage.getRecords().stream().forEach(v -> {
            if (orgIdMap.containsKey(v.getOrgId())) {
                v.setOrgIdName(orgIdMap.get(v.getOrgId()));
            }
        });
        return resultPage;
    }

    public void saveDetail(List<ParityTransferDetailEntity> detailEntityList, ParityTransferEntity entity) {
        // 合同编码+签约主体+凭证日期小于基准日期获取数据
        List<String> contractList = detailEntityList.stream().map(ParityTransferDetailEntity::getContractCode).collect(Collectors.toList());
        List<String> orgIdList = detailEntityList.stream().map(ParityTransferDetailEntity::getOrgId).distinct().collect(Collectors.toList());
        List<String> clientCodeList = detailEntityList.stream().filter(s -> StringUtils.isNotEmpty(s.getClientCode())).map(ParityTransferDetailEntity::getClientCode).distinct().collect(Collectors.toList());
        ContractBalanceQueryDTO contractBalanceQueryDTO = new ContractBalanceQueryDTO();
        contractBalanceQueryDTO.setContractCodeList(contractList);
        contractBalanceQueryDTO.setClientCodeList(clientCodeList);
        contractBalanceQueryDTO.setOrgIdList(orgIdList);
        contractBalanceQueryDTO.setVoucherDate(entity.getReferenceDate());
        List<ContractBalanceVO> contractBalanceVOList = iContractBalanceService.selectLatestBalanceByCondition(contractBalanceQueryDTO);
        contractBalanceQueryDTO.setVoucherDate(entity.getTradeDate());
        List<ContractBalanceVO> tradeBalanceVOList = iContractBalanceService.selectLatestBalanceByCondition(contractBalanceQueryDTO);
        // 按照合同编码+签约主体+客户编码分组
        Map<String, List<ContractBalanceVO>> balanceGroupMap = contractBalanceVOList.stream().collect(Collectors.groupingBy(c -> c.getContractCode() + "-" + c.getOrgId() + "-" + c.getClientCode()));
        Map<String, List<ContractBalanceVO>> tradeBalanceGroupMap = tradeBalanceVOList.stream().collect(Collectors.groupingBy(c -> c.getContractCode() + "-" + c.getOrgId() + "-" + c.getClientCode()));
        detailEntityList.stream().forEach(v -> {
            v.setParityTransferId(entity.getId());
            String key = v.getContractCode() + "-" + v.getOrgId() + "-" + v.getClientCode();
            if (balanceGroupMap.containsKey(key)) {
                v.setReceivableRent(balanceGroupMap.get(key).get(0).getReceivableRentBalance());
                v.setReceivableResidualValue(balanceGroupMap.get(key).get(0).getReceivableResidualValueBalance());
                v.setReceivableOuttax(balanceGroupMap.get(key).get(0).getReceivableOuttaxBalance());
                v.setUnrealizedRevenue(balanceGroupMap.get(key).get(0).getUnrealizedRevenueBalance());
                v.setLesseeMargin(balanceGroupMap.get(key).get(0).getLesseeMarginBalance());
            }
            if (tradeBalanceGroupMap.containsKey(key)) {
                v.setPayableAgencyEstimate(tradeBalanceGroupMap.get(key).get(0).getPayableAgencyEstimateBalance());
                v.setPayableVehicleEstimate(tradeBalanceGroupMap.get(key).get(0).getPayableVehicleEstimateBalance());
                v.setPayableBandCostEstimate(tradeBalanceGroupMap.get(key).get(0).getPayableBandCostEstimateBalance());
                v.setPayablePledgeEstimate(tradeBalanceGroupMap.get(key).get(0).getPayablePledgeEstimateBalance());
                v.setPayableUnpledgeEstimate(tradeBalanceGroupMap.get(key).get(0).getPayableUnpledgeEstimateBalance());
                v.setPayableOtherCostEstimate(tradeBalanceGroupMap.get(key).get(0).getPayableOtherCostEstimateBalance());
                v.setDepreciationReserves(tradeBalanceGroupMap.get(key).get(0).getDepreciationReservesBalance());
            }
        });
        iParityTransferDetailService.saveBatch(detailEntityList);
    }

    public List<ParityTransferDetailVO> checkData(List<ParityTransferExcelVO> transferExcelVOList, List<ParityTransferDetailExcelVO> detailExcelVOList) {
        List<ParityTransferDetailVO> detailVOList = Lists.newArrayList();
        Map<String, String> orgIdMap = iOrgCompanyService.list().stream().collect(HashMap::new, (h, v) -> h.put(v.getOrgName(), v.getOrgId()), HashMap::putAll);
        if (CollectionUtils.isEmpty(transferExcelVOList)) {
            throw new ServiceException("导入模板平价转让基础数据为空");
        }
        if (CollectionUtils.isEmpty(detailExcelVOList)) {
            throw new ServiceException("导入模板平价转让详情数据为空");
        }
        String batch = transferExcelVOList.get(0).getBatch();
        String key = transferExcelVOList.get(0).getBatch() + "-" + transferExcelVOList.get(0).getTransferParty() + "-" + transferExcelVOList.get(0).getTransfereeParty();
        AtomicInteger i = new AtomicInteger();
        transferExcelVOList.stream().forEach(v -> {
            String batchKey = v.getBatch() + "-" + v.getTransferParty() + "-" + v.getTransfereeParty();
            if (StringUtils.isEmpty(v.getBatch())) {
                throw new ServiceException("平价转让基础数据转让批次不可以为空");
            }
            if (StringUtils.isEmpty(v.getTransferParty())) {
                throw new ServiceException("平价转让基础数据转让方不可以为空");
            }
            if (StringUtils.isEmpty(v.getTransfereeParty())) {
                throw new ServiceException("平价转让基础数据受让方不可以为空");
            }
            if (!batch.equals(v.getBatch())) {
                throw new ServiceException("平价转让基础数据转让批次应该唯一");
            }
            if (!orgIdMap.containsKey(v.getTransferParty())) {
                throw new ServiceException("平价转让基础数据转让方在系统中不存在");
            }
            if (!orgIdMap.containsKey(v.getTransfereeParty())) {
                throw new ServiceException("平价转让基础数据受让方在系统中不存在");
            }
            if (i.get() != 0 && key.equals(batchKey)) {
                throw new ServiceException("平价转让基础数据批次号+转让方+受让方存在重复");
            }
            if (ObjectUtils.isNull(v.getReferenceDate())) {
                throw new ServiceException("平价转让基础数据基准日不可以为空");
            }
            if (ObjectUtils.isNull(v.getTradeDate())) {
                throw new ServiceException("平价转让基础数据交易日不可以为空");
            }
            v.setTransferParty(orgIdMap.get(v.getTransferParty()));
            v.setTransfereeParty(orgIdMap.get(v.getTransfereeParty()));
            i.getAndIncrement();
        });
        String contractCode = detailExcelVOList.get(0).getContractCode();
        AtomicInteger d = new AtomicInteger();
        detailExcelVOList.stream().forEach(v -> {
            if (!batch.equals(v.getBatch())) {
                throw new ServiceException("平价转让详情批次和基础数据批次不同");
            }
            if (StringUtils.isEmpty(v.getContractCode())) {
                throw new ServiceException("平价转让详情合同编码不能为空");
            }
            if (StringUtils.isEmpty(v.getFinancialContractStatus())) {
                throw new ServiceException("平价转让详情财务合同状态不能为空");
            }
            if (d.get() != 0 && contractCode.equals(v.getContractCode())) {
                throw new ServiceException("平价转让详情合同编码不可以重复");
            }
            d.getAndIncrement();
        });
        List<String> contractList = detailExcelVOList.stream().map(ParityTransferDetailExcelVO::getContractCode).collect(Collectors.toList());
        Map<String, List<ContractEntity>> contractMap = iContractService.lambdaQuery().in(ContractEntity::getContractCode, contractList).list().stream().collect(Collectors.groupingBy(ContractEntity::getContractCode));
        detailExcelVOList.stream().forEach(v -> {
            if (!contractMap.containsKey(v.getContractCode())) {
                throw new ServiceException(String.format("合同编码：%s不在系统中存在", v.getContractCode()));
            }
            for (ContractEntity entity : contractMap.get(v.getContractCode())) {
                ParityTransferDetailVO detailVO = BeanUtil.copyProperties(v, ParityTransferDetailVO.class);
                detailVO.setOrgId(entity.getOrgId());
                detailVO.setClientCode(entity.getClientCode());
                detailVO.setClientName(entity.getClientName());
                detailVO.setTaxRate(null == entity.getTaxRate() ? "" : entity.getTaxRate().toString());
                detailVOList.add(detailVO);
            }
        });
        return detailVOList;
    }
}

