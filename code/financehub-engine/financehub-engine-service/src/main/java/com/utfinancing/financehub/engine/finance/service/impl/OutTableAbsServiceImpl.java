package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.hash.Hash;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.nacos.api.common.Constants;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.utfinancing.financehub.admin.api.RemoteDictService;
import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.common.core.dto.R;
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
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.*;
import com.utfinancing.financehub.engine.finance.mapper.OutTableAbsMapper;
import com.utfinancing.financehub.engine.finance.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import com.utfinancing.financehub.engine.rule.model.dto.ExecuteCommonDTO;
import com.utfinancing.financehub.engine.rule.model.vo.VoucherInfoVO;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import com.utfinancing.financehub.engine.utils.CommonDateUtils;
import com.utfinancing.financehub.engine.utils.PeriodCodeUtil;
import com.utfinancing.financehub.engine.verification.entity.VerificationDetailsEntity;
import com.utfinancing.financehub.engine.verification.model.dto.VerificationDTO;
import com.utfinancing.financehub.engine.verification.model.dto.VerificationDetailsQueryDTO;
import com.utfinancing.financehub.engine.verification.model.vo.VerificationDetailsVO;
import feign.Contract;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.util.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.rmi.ServerException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author : bruyang
 * @Date : Create in 2024-03-11
 * @Description :  OutTableAbs服务实现类
 * @Modified :
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class OutTableAbsServiceImpl extends ServiceImpl<OutTableAbsMapper, OutTableAbsEntity> implements IOutTableAbsService {

    private final OutTableAbsMapper outTableAbsMapper;
    private final IContractService iContractService;
    private final IOutTableContractDetailService iOutTableContractDetailService;
    private final IContractBalanceService iContractBalanceService;
    private final IVoucherService iVoucherService;
    @Value("${approve.url.tailAdjust-url:null}")
    private String approveUrl;
    private final IApproveService iApproveService;
    private final IRuleService iRuleService;
    private final IClientService iClientService;
    private final IOrgCompanyService iOrgCompanyService;
    private final RemoteDictService remoteDictService;
    private final IContractStatusRecordService iContractStatusRecordService;

    @Override
    public Long saveOutTableAbs(OutTableAbsDTO dto) {
        OutTableAbsEntity entity = BeanUtil.copyProperties(dto, OutTableAbsEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateOutTableAbs(Long id, OutTableAbsDTO dto) {
        OutTableAbsEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public OutTableAbsDTO getOutTableAbsDTOById(Long id) {
        OutTableAbsEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, OutTableAbsDTO.class);
    }

    @Override
    public IPage<OutTableAbsVO> selectPage(OutTableAbsQueryDTO queryDTO) {
        LambdaQueryWrapper<OutTableAbsEntity> queryWrapper = getQueryWrapper(queryDTO);
        //这里注入查询条件
        IPage<OutTableAbsEntity> entityIPage = outTableAbsMapper.selectPage(new Page<OutTableAbsEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        IPage<OutTableAbsVO> resultPage = ListBeanUtil.copyPage(entityIPage, OutTableAbsVO.class);
        resultPage.getRecords().stream().forEach(v -> {
            v.setBatchType(BatchTypeEnum.CBABS.getCode());
        });
        return resultPage;
    }

    public LambdaQueryWrapper<OutTableAbsEntity> getQueryWrapper(OutTableAbsQueryDTO queryDTO){
        LambdaQueryWrapper<OutTableAbsEntity> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(queryDTO.getLoanContractCode())) {
            queryWrapper.like(OutTableAbsEntity::getLoanContractCode,queryDTO.getLoanContractCode());
        }
        if (ObjectUtil.isNotNull(queryDTO.getStartAccountDate())) {
            queryWrapper.ge(OutTableAbsEntity::getAccountDate, queryDTO.getStartAccountDate());
        }
        if (ObjectUtil.isNotNull(queryDTO.getEndAccountDate())) {
            queryWrapper.le(OutTableAbsEntity::getAccountDate, queryDTO.getEndAccountDate());
        }
        if (CollectionUtil.isNotEmpty(queryDTO.getPeriodsList())) {
            queryWrapper.in(OutTableAbsEntity::getPeriods, queryDTO.getPeriodsList());
        }
        if (CollectionUtil.isNotEmpty(queryDTO.getLoanContractCodeList())) {
            queryWrapper.in(OutTableAbsEntity::getLoanContractCode, queryDTO.getLoanContractCodeList());
        }
        if (ObjectUtil.isNotNull(queryDTO.getId())) {
            queryWrapper.eq(OutTableAbsEntity::getId, queryDTO.getId());
        }
        return queryWrapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean importTemplate(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        InputStream inputStream2 = file.getInputStream();
        try {
            ExcelUtil<OutTableAbsExcelVO> util = new ExcelUtil<OutTableAbsExcelVO>(OutTableAbsExcelVO.class);
            ExcelUtil<OutTableContractDetailExcelVO> util2 = new ExcelUtil<OutTableContractDetailExcelVO>(OutTableContractDetailExcelVO.class);
            List<OutTableAbsExcelVO> contractExcels = util.importExcel("出表abs基本信息", inputStream, 0);
            List<OutTableContractDetailExcelVO> contractStructureExcels = util2.importExcel("出表合同详情", inputStream2, 0);
            List<OutTableAbsDTO> outTableAbsDTOList = BeanUtil.copyToList(contractExcels,OutTableAbsDTO.class);
            List<OutTableContractDetailDTO> detailDTOList = BeanUtil.copyToList(contractStructureExcels,OutTableContractDetailDTO.class);
            detailDTOList = checkDate(outTableAbsDTOList,detailDTOList);
            //应收融资租赁款汇总
            final BigDecimal[] financeLeaseReceivablesAmountTotal = {BigDecimal.ZERO};
            //封包日应收租金+封包日应收残值+封包日应收销项税-封包日未实现收益
            detailDTOList.stream().forEach(v -> {
                BigDecimal financeLeaseReceivablesAmount =  (null==v.getReceivableRent()?BigDecimal.ZERO:v.getReceivableRent())
                        .add(null == v.getReceivableResidualValue()?BigDecimal.ZERO:v.getReceivableResidualValue())
                        .add(null == v.getReceivableOuttax()?BigDecimal.ZERO:v.getReceivableOuttax())
                        .subtract(null == v.getUnrealizedRevenue()?BigDecimal.ZERO:v.getUnrealizedRevenue());
                financeLeaseReceivablesAmountTotal[0] = financeLeaseReceivablesAmountTotal[0].add(financeLeaseReceivablesAmount);
            });
            //合同详情表按照借款合同编号分组
            Map<String,List<OutTableContractDetailDTO>> detailMap = detailDTOList.stream().collect(Collectors.groupingBy(OutTableContractDetailDTO::getLoanContractCode));
            outTableAbsDTOList.stream().forEach(v -> {
                OutTableAbsEntity entity = BeanUtil.copyProperties(v, OutTableAbsEntity.class);
                entity.setBusinessDate(LocalDateTime.now());
                entity.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
                entity.setAccountDate(CommonDateUtils.parseDateToLocalDateTime(v.getReleaseDate()));
                this.save(entity);
                if (detailMap.containsKey(v.getLoanContractCode())) {
                    List<OutTableContractDetailEntity> detailEntityList = BeanUtil.copyToList(detailMap.get(v.getLoanContractCode()),OutTableContractDetailEntity.class);
                    detailEntityList.stream().forEach(d -> {
                        d.setOutTableAbsId(entity.getId());
                        setTransferPrice(d,entity,financeLeaseReceivablesAmountTotal[0]);
                        //校验借款合同+出表期数+合同编号+签约主体在系统中是否存在
                        if (isExistFlag(entity.getLoanContractCode(),entity.getPeriods(),d.getContractCode(),d.getOrgId())) {
                            throw new ServiceException(String.format("借款合同编号：%s,出表期数：%s,合同编号：%s,签约主体：%s已经存在，不可导入",entity.getLoanContractCode(),
                                    d.getContractCode(),
                                    d.getOrgId(),
                                    entity.getPeriods()));
                        }
                    });
                    iOutTableContractDetailService.saveBatch(detailEntityList);
                }
            });
            return Boolean.TRUE;
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(inputStream2);
        }
    }

    public Boolean isExistFlag(String loanContractCode,String periods,String contractCode,String orgId){
        OutTableContractDetailQueryDTO queryDTO = new OutTableContractDetailQueryDTO();
        queryDTO.setLoanContractCode(loanContractCode);
        queryDTO.setPeriods(periods);
        queryDTO.setContractCode(contractCode);
        queryDTO.setOrgId(orgId);
        List<OutTableContractDetailVO> detailVOS = outTableAbsMapper.selectByCondition(queryDTO);
        if (CollectionUtil.isNotEmpty(detailVOS)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public List<OutTableContractDetailDTO> checkDate(List<OutTableAbsDTO> outTableAbsDTOList,List<OutTableContractDetailDTO> detailDTOList) {
        if (CollectionUtils.isEmpty(outTableAbsDTOList)) {
            throw new ServiceException("导入文件出表abs基本信息是空的");
        }
        List<String> clientCodeList = detailDTOList.stream().filter(v -> StringUtils.isNotEmpty(v.getClientCode())).map(OutTableContractDetailDTO::getClientCode).collect(Collectors.toList());
        Map<String,String> clientCodeMap = Maps.newHashMap();
        if (CollectionUtil.isNotEmpty(clientCodeList)) {
            clientCodeMap = iClientService.lambdaQuery().in(ClientEntity::getClientCode, clientCodeList).list().stream().filter(v -> StringUtils.isNotEmpty(v.getClientCode())).collect(HashMap::new, (map, item) -> map.put(item.getClientCode(), item.getClientName()), HashMap::putAll);
        }
        List<OutTableContractDetailDTO> newOutTableContractDetailList = Lists.newArrayList();
        //获取合同信息
        List<String> contractCodeList = detailDTOList.stream().filter(v->StringUtils.isNotEmpty(v.getContractCode())).map(OutTableContractDetailDTO::getContractCode).distinct().collect(Collectors.toList());
        Map<String,List<ContractEntity>> contractMap = Maps.newHashMap();
        if (CollectionUtil.isNotEmpty(contractCodeList)) {
            contractMap = iContractService.lambdaQuery().in(ContractEntity::getContractCode,contractCodeList).list().stream().collect(Collectors.groupingBy(ContractEntity::getContractCode));
        }
        //获取出表数据借款合同号+出表期数来判断
        outTableAbsDTOList.stream().forEach(v -> {
            if (StringUtils.isEmpty(v.getLoanContractCode())) {
                throw new ServiceException("基本信息借款合同编号不可以为空");
            }
            if (StringUtils.isEmpty(v.getPeriods())) {
                throw new ServiceException("基本信息出表期数不可以为空");
            }
            if (StringUtils.isEmpty(v.getAdministrator())) {
                throw new ServiceException("基本信息管理人不可以为空");
            }
            if (ObjectUtil.isNull(v.getCloseDate())) {
                throw new ServiceException("基本信息封包日不可以为空");
            }
            if (ObjectUtil.isNull(v.getReleaseDate())) {
                throw new ServiceException("基本信息发行日不可以为空");
            }
            if (ObjectUtil.isNull(v.getTransferPrice())) {
                throw new ServiceException("基本信息转让价格不可以为空");
            }
            if (StringUtils.isEmpty(v.getTransferPeriod())) {
                throw new ServiceException("基本信息转付周期不可以为空");
            }
            if (StringUtils.isEmpty(v.getCalculationPeriod())) {
                throw new ServiceException("基本信息计算周期不可以为空");
            }
            if (StringUtils.isEmpty(v.getCalculationPeriod())) {
                throw new ServiceException("基本信息兑付周期不可以为空");
            }
            if (isExist(v)) {
                throw new ServiceException(String.format("基本信息借款合同：%s,出表期数：%s已经在系统中存在",v.getLoanContractCode(),v.getPeriods()));
            }
        });
        Map<String, List<ContractEntity>> finalContractMap = contractMap;
        //详情表借款合同号+合同编号唯一不能重复
        Map<String,List<OutTableContractDetailDTO>> detailMap = detailDTOList.stream().collect(Collectors.groupingBy(v -> v.getLoanContractCode()+"-"+v.getContractCode()));
        for(Map.Entry<String, List<OutTableContractDetailDTO>> entry : detailMap.entrySet()) {
            Map<String, String> finalClientCodeMap = clientCodeMap;
            entry.getValue().stream().forEach(v -> {
                if (StringUtils.isEmpty(v.getLoanContractCode())) {
                    if (StringUtils.isEmpty(v.getLoanContractCode())) {
                        throw new ServiceException("合同详情借款合同编号不可以为空");
                    }
                }
                if (StringUtils.isEmpty(v.getContractCode())) {
                    if (StringUtils.isEmpty(v.getLoanContractCode())) {
                        throw new ServiceException("合同详情合同编号不可以为空");
                    }
                }
                if (!finalContractMap.containsKey(v.getContractCode())) {
                    throw new ServiceException("合同详情合同编号不在系统中存在");
                }
                List<ContractEntity> contractEntityList = finalContractMap.get(v.getContractCode());
                contractEntityList.stream().forEach(s -> {
                    OutTableContractDetailDTO newTableDTO = BeanUtil.copyProperties(v, OutTableContractDetailDTO.class);
                    newTableDTO.setOrgId(s.getOrgId());
                    newTableDTO.setClientCode(s.getClientCode());
                    if (StringUtils.isNotEmpty(newTableDTO.getClientCode()) && finalClientCodeMap.containsKey(newTableDTO.getClientCode())) {
                        newTableDTO.setClientName(finalClientCodeMap.get(newTableDTO.getClientCode()));
                    }
                    newOutTableContractDetailList.add(newTableDTO);
                });
            });
            if (entry.getValue().size()>1) {
                throw new ServiceException(String.format("合同详情借款合同号：{}和合同编码：{}，应该唯一",entry.getValue().get(0).getLoanContractCode(),entry.getValue().get(0).getContractCode()));
            }
        }
        return newOutTableContractDetailList;
    }

    public Boolean isExist(OutTableAbsDTO v) {
        return this.lambdaQuery().eq(OutTableAbsEntity::getLoanContractCode,v.getLoanContractCode()).eq(OutTableAbsEntity::getPeriods,v.getPeriods()).exists();
    }

    @Override
    public IPage<OutTableContractDetailVO> detailPage(OutTableContractDetailQueryDTO queryDTO) {
        IPage<OutTableContractDetailVO> resultPage = iOutTableContractDetailService.selectPage(queryDTO);
        OutTableAbsEntity entity = this.getById(queryDTO.getOutTableAbsId());
        Map<String,String> orgIdMap = iOrgCompanyService.list().stream().collect(HashMap::new,(h, v)->h.put(v.getOrgId(),v.getOrgName()),HashMap::putAll);
        resultPage.getRecords().stream().forEach(v -> {
            getBalance(v,entity);
            if (orgIdMap.containsKey(v.getOrgId())) {
                v.setOrgIdName(orgIdMap.get(v.getOrgId()));
            }
        });
        return resultPage;
    }

    public void getBalance(OutTableContractDetailVO v,OutTableAbsEntity entity){
        v.setPeriods(entity.getPeriods());
        v.setLoanContractCode(entity.getLoanContractCode());
        //查询合同类型
        ContractEntity contractEntity = iContractService.getOne(Wrappers.<ContractEntity>lambdaQuery().eq(ContractEntity::getContractCode,v.getContractCode()).eq(ContractEntity::getOrgId,v.getOrgId()));
        //查询余额表
        List<ContractBalanceEntity> contractBalanceEntityList = iContractBalanceService.lambdaQuery().eq(ContractBalanceEntity::getContractCode,v.getContractCode())
                .eq(ContractBalanceEntity::getOrgId,v.getOrgId()).orderByDesc(ContractBalanceEntity::getId).list();
        if (CollectionUtil.isEmpty(contractBalanceEntityList)) {
            return;
        }
        // 按合同编号+签约主体维度查余额表：发行日depreciation_reserves_balance
        final BigDecimal[] depreciationReservesBalance = {BigDecimal.ZERO};
        final BigDecimal[] leaseRevenueBalance = {BigDecimal.ZERO};
        final BigDecimal[] receivableUnconfirmReceiptAmount = {BigDecimal.ZERO};
        final BigDecimal[] receivableOuttaxDebtRestructureAmount = {BigDecimal.ZERO};
        //转让损益=转让价格（合同维度）-应收融资租赁款
        BigDecimal transferLossPrice = BigDecimal.ZERO;
        String releaseDate = DateUtil.format(entity.getReleaseDate(),"yyyy-MM-dd");
        String coleseDate = DateUtil.format(entity.getCloseDate(),"yyyy-MM-dd");
        BigDecimal financeLeaseReceivablesAmount = BigDecimal.ZERO;
        //最新日期的数据
        ContractBalanceEntity latestEntity = contractBalanceEntityList.get(0);
        contractBalanceEntityList.stream().forEach(e -> {
            String voucherDate = DateUtil.format(e.getVoucherDate(),"yyyy-MM-dd");
            if (StringUtils.isNotEmpty(voucherDate) && voucherDate.equals(releaseDate)) {
                depreciationReservesBalance[0] = depreciationReservesBalance[0].add(null == v.getDepreciationReservesBalance()?BigDecimal.ZERO:v.getDepreciationReservesBalance());
            }
            if (LeaseTypeEnum.DIRECT.getCode().equals(contractEntity.getLeaseType())) {
                leaseRevenueBalance[0] = leaseRevenueBalance[0].add(null==e.getLeaseRevenueBalance()?BigDecimal.ZERO:e.getLeaseRevenueBalance());
            }
            if (LeaseTypeEnum.LEASEBACK.getCode().equals(contractEntity.getLeaseType())) {
                leaseRevenueBalance[0] = leaseRevenueBalance[0].add(null==e.getLeaseRevenue6Balance()?BigDecimal.ZERO:e.getLeaseRevenue6Balance());
            }
            if (null!=e.getVoucherDate() && entity.getCloseDate().compareTo(e.getVoucherDate())<=0
                    && e.getCreateTime().compareTo(e.getVoucherDate())>=0) {
                BigDecimal receivableUnconfirmReceipt = null == e.getReceivableUnconfirmReceiptAmount() ? BigDecimal.ZERO : e.getReceivableUnconfirmReceiptAmount();
                if (receivableUnconfirmReceipt.compareTo(BigDecimal.ZERO)>=0) {
                    receivableUnconfirmReceiptAmount[0] = receivableUnconfirmReceiptAmount[0].add(receivableUnconfirmReceipt);
                }
                BigDecimal receivableOuttaxDebtRestructure = null == e.getReceivableOuttaxDebtRestructureAmount() ? BigDecimal.ZERO : e.getReceivableOuttaxDebtRestructureAmount();
                if (receivableOuttaxDebtRestructure.compareTo(BigDecimal.ZERO)>=0) {
                    receivableOuttaxDebtRestructureAmount[0] = receivableOuttaxDebtRestructureAmount[0].add(receivableOuttaxDebtRestructure);
                }
                BigDecimal interestOtherFinancialAc = null == e.getInterestOtherFinancialAcAmount() ? BigDecimal.ZERO : e.getInterestOtherFinancialAcAmount();
                if (interestOtherFinancialAc.compareTo(BigDecimal.ZERO)>=0) {
                    receivableOuttaxDebtRestructureAmount[0].add(interestOtherFinancialAc);
                }
                BigDecimal receivableOuttax = null == e.getReceivableOuttaxAmount() ? BigDecimal.ZERO : e.getReceivableOuttaxAmount();
                if (receivableOuttax.compareTo(BigDecimal.ZERO)>=0) {
                    receivableOuttaxDebtRestructureAmount[0].add(receivableOuttax);
                }
                BigDecimal receivableServiceOuttax = null == e.getReceivableServiceOuttaxAmount() ? BigDecimal.ZERO : e.getReceivableServiceOuttaxAmount();
                if (receivableServiceOuttax.compareTo(BigDecimal.ZERO)>=0) {
                    receivableOuttaxDebtRestructureAmount[0].add(receivableServiceOuttax);
                }
            }
        });
        // 按合同编号+签约主体维度查余额表：发行日depreciation_reserves_balance
        v.setDepreciationReservesBalance(depreciationReservesBalance[0]);
        //按合同编号+签约主体维度查余额表 1.直租合同：最新日期的lease_revenue_balance 减封包日的lease_revenue_balance；2.回租合同：最新日期的lease_revenue6_balance 减 封包日的lease_revenue6_balance
        v.setLeaseRevenueBalance(latestEntity.getLeaseRevenueBalance().subtract(leaseRevenueBalance[0]));
        //按签约主体+合同编号查余额表：封包日到最新日期receivable_unconfirm_receipt_amount期间正数的汇总额
        v.setReceivableOuttaxDebtRestructureAmount(receivableUnconfirmReceiptAmount[0]);
        //按签约主体+合同编号查余额表：封包日到最新日期receivable_outtax_debt_restructure_amount正数汇总
        //+interest_other_financial_ac_amount正数汇总
        //+receivable_outtax_amount正数汇总
        //+receivable_service_outtax_amount正数汇总
        v.setReceivableOuttaxDebtRestructureAmount(receivableOuttaxDebtRestructureAmount[0]);
        // Finance lease receivables =封包日应收租金+封包日应收残值+封包日应收销项税-封包日未实现收益
        financeLeaseReceivablesAmount = getFinanceLeaseReceivablesAmount(BeanUtil.copyProperties(v,OutTableContractDetailEntity.class));
        v.setFinanceLeaseReceivablesAmount(financeLeaseReceivablesAmount);
        transferLossPrice = (null==v.getTransferPrice()?BigDecimal.ZERO:v.getTransferPrice()).subtract(financeLeaseReceivablesAmount);
        v.setTransferLossPrice(transferLossPrice);
    }


    @Override
    public Boolean deleteByIds(List<Long> idList) {
        if (CollectionUtil.isEmpty(idList)) {
            throw new ServiceException("请至少勾选一条数据删除");
        }
        List<OutTableAbsEntity> outTableAbsEntityList = this.listByIds(idList);
        outTableAbsEntityList.forEach(v -> {
            if (!ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("处理状态为已录入的才可以删除");
            }
        });
        this.removeBatchByIds(idList);
        batchDeleteVoucher(idList);
        List<Long> detailList = iOutTableContractDetailService.lambdaQuery().in(OutTableContractDetailEntity::getOutTableAbsId,idList).list().stream().map(OutTableContractDetailEntity::getId).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(detailList)) {
            iContractStatusRecordService.lambdaUpdate().set(ContractStatusRecordEntity::getDelFlag,"1").in(ContractStatusRecordEntity::getSourceFromId, detailList).eq(ContractStatusRecordEntity::getSourceFromType, BatchTypeEnum.HZHX.getCode()).update();
        }
        return iOutTableContractDetailService.removeBatcheByDetailId(idList);
    }

    public void batchDeleteVoucher(List<Long> ids){
        //获取所有的凭证Id
        List<OutTableContractDetailEntity> detailsEntityList = iOutTableContractDetailService.lambdaQuery().in(OutTableContractDetailEntity::getOutTableAbsId, ids).list();
        //逗号拆分
        List<Long> voucherIdList = Lists.newArrayList();
        detailsEntityList.stream().forEach(v -> {
            if (StringUtils.isNotEmpty(v.getVoucherId())) {
                voucherIdList.addAll(Arrays.stream(v.getVoucherId().split(",")).map(Long::parseLong).collect(Collectors.toList()));
            }
            v.setVoucherId("");
        });
        if (CollectionUtil.isNotEmpty(voucherIdList)) {
            iVoucherService.deleteByIdList(voucherIdList);
        }
        iOutTableContractDetailService.updateBatchById(detailsEntityList);
    }

    @Override
    public Boolean submit(List<Long> idList) {
        if (com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请至少勾选一条数据提交");
        }
        List<OutTableAbsEntity> outTableAbsEntityList = this.listByIds(idList);
        List<ApproveDTO> approveDTOList = Lists.newArrayList();
        outTableAbsEntityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("只有处理状态为已录入的才可以提交");
            }
            v.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
            ApproveDTO approveDTO = new ApproveDTO();
            approveDTO.setDocumentId(v.getId());
            approveDTO.setDocumentType(BatchTypeEnum.CBABS.getCode());
            approveDTO.setUrl(approveUrl+v.getId());
            approveDTOList.add(approveDTO);
        });
        //发送审核
        Map<Long,Long> processInstantIdMap = iApproveService.submit(approveDTOList);
        outTableAbsEntityList.stream().forEach(v -> {
            if (null!=processInstantIdMap && processInstantIdMap.containsKey(v.getId())) {
                v.setProcessInstanceId(processInstantIdMap.get(v.getId()));
            }
        });
        Boolean isGenerateVoucher = generateVoucher(idList, YesOrNoEnum.YES.getCode());
        if (!isGenerateVoucher) {
            throw new ServiceException("凭证存在未生成，不可以提交");
        }
        //提交生成特殊合同信息
        return this.updateBatchById(outTableAbsEntityList);
    }

    public void saveSperialContract(List<Long> idList) {
        OutTableContractDetailQueryDTO queryDTO = new OutTableContractDetailQueryDTO();
        queryDTO.setOutTableAbsIdList(idList);
        List<OutTableContractDetailVO> detailEntityList = iOutTableContractDetailService.selectDetailsByParams(queryDTO);
        List<ContractVO> contractVOList = Lists.newArrayList();
        for (OutTableContractDetailVO outTableContractDetailEntity : detailEntityList) {
            ContractVO contractVO = BeanUtil.copyProperties(outTableContractDetailEntity,ContractVO.class);
            contractVO.setSourceFromType(BatchTypeEnum.CBABS.getCode());
            contractVO.setSourceFromId(outTableContractDetailEntity.getId());
            contractVO.setFinancialContractStatus(outTableContractDetailEntity.getFinancialContractStatus());
            contractVO.setFinancialContractStatusUpdateTime(CommonDateUtils.parseLocalDateTimeToDate(outTableContractDetailEntity.getAccountDate()));
            contractVOList.add(contractVO);
        }
        iContractService.verificationSaveRecordList(contractVOList);
    }

    @Override
    public Boolean withdraw(List<Long> idList) {
        if (CollectionUtil.isEmpty(idList)) {
            throw new ServiceException("请至少勾选一条数据撤回");
        }
        List<OutTableAbsEntity> outTableAbsEntityList = this.listByIds(idList);
        outTableAbsEntityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.SUBMITTED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("只有处理状态为已提交的才可以撤回");
            }
            v.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
        });
        iApproveService.withdraw(outTableAbsEntityList.stream().map(OutTableAbsEntity::getProcessInstanceId).collect(Collectors.toList()));
        //删除凭证
        iVoucherService.updateStatusByBatch(idList, BatchTypeEnum.CBABS.getCode(), ProcessStatusEnum.ENTERED.getCode(),"","");
        List<Long> detailList = iOutTableContractDetailService.lambdaQuery().in(OutTableContractDetailEntity::getOutTableAbsId,idList).list().stream().map(OutTableContractDetailEntity::getId).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(detailList)) {
            iContractStatusRecordService.lambdaUpdate().set(ContractStatusRecordEntity::getDelFlag,"1").in(ContractStatusRecordEntity::getSourceFromId, detailList).eq(ContractStatusRecordEntity::getSourceFromType, BatchTypeEnum.HZHX.getCode()).update();
        }
        return this.updateBatchById(outTableAbsEntityList);
    }

    @Override
    public Boolean generateVoucher(List<Long> idList, String isSubmit) {
        if (CollectionUtil.isEmpty(idList)) {
            throw new ServiceException("请至少选择一条数据生成凭证");
        }
        List<OutTableAbsEntity> outTableAbsEntityList = this.listByIds(idList);
        outTableAbsEntityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("处理状态为已录入的才可以生成凭证");
            }
        });
        //生成凭证前先删除凭证
        iVoucherService.deleteByBatchIdList(idList,BatchTypeEnum.CBABS.getCode());
        Map<Long,OutTableAbsEntity> absEntityMap = outTableAbsEntityList.stream().collect(Collectors.toMap(e->e.getId(),e->e,(k1,k2)->k2));
        List<OutTableContractDetailEntity> detailEntityList = iOutTableContractDetailService.lambdaQuery().in(OutTableContractDetailEntity::getOutTableAbsId,idList).list();
        List<Map<String,Object>> voucherMapList = Lists.newArrayList();
        detailEntityList.stream().forEach(v -> {
            OutTableAbsEntity entity = absEntityMap.get(v.getOutTableAbsId());
            //组装凭证参数
            voucherMapList.add(getVoucherMap(entity,v,isSubmit));
        });
        log.info("出表ABS凭证参数：{}", JSON.toJSONString(voucherMapList));
        List<VoucherInfoVO> voucherResultList = iRuleService.batchExecuteRule(voucherMapList);
        log.info("出表ABS凭证返回值：{}", JSON.toJSONString(voucherResultList));
        Boolean isExistVoucherError = voucherResultList.stream().allMatch(v -> StringUtils.isNotEmpty(v.getErrorInfo()));
        if (YesOrNoEnum.YES.getCode().equals(isSubmit) && isExistVoucherError) {
            //异步删除已生成的凭证
            List<Long> voucherIdList = Lists.newArrayList();
            voucherResultList.stream().forEach(voucherInfoVO -> {
                if (com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isNotEmpty(voucherInfoVO.getVoucherDTOList())){
                    voucherIdList.addAll(voucherInfoVO.getVoucherDTOList().stream().map(VoucherDTO::getId).collect(Collectors.toList()));
                }
            });
            asnyDeleteVoucher(voucherIdList);
            return Boolean.FALSE;
        }
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
            if (CollectionUtil.isNotEmpty(infoVO.getVoucherDTOList())) {
                voucherIds = infoVO.getVoucherDTOList().stream().map(VoucherDTO::getId).map(String::valueOf).collect(Collectors.toList()).stream().collect(Collectors.joining(","));
                periodCode = infoVO.getVoucherDTOList().get(0).getPeriodCode();
            }
            iOutTableContractDetailService.lambdaUpdate().set(OutTableContractDetailEntity::getVoucherId, voucherIds).set(OutTableContractDetailEntity::getErrorInfo,errorInfo).set(OutTableContractDetailEntity::getPeriodCode,periodCode).eq(OutTableContractDetailEntity::getId,Long.parseLong(infoVO.getOrderId())).update();
        }
        if (YesOrNoEnum.NO.getCode().equals(isSubmit) && !isExistVoucherError) {
            updateIsGenerateVoucher(idList,isSubmit);
        }
        return Boolean.TRUE;
    }

    public void updateIsGenerateVoucher(List<Long> idList,String isSubmit){
        if (YesOrNoEnum.YES.getCode().equals(isSubmit)) {
            return;
        }
        //获取详情信息
        Map<Long,List<OutTableContractDetailEntity>> detailEntityMap = iOutTableContractDetailService.lambdaQuery()
                .in(OutTableContractDetailEntity::getOutTableAbsId,idList).list().stream().collect(Collectors.groupingBy(OutTableContractDetailEntity::getOutTableAbsId));
        for (Map.Entry<Long, List<OutTableContractDetailEntity>> entry : detailEntityMap.entrySet()) {
            Boolean isExistEmpty = entry.getValue().stream().anyMatch(v -> StringUtils.isEmpty(v.getVoucherId()));
            String isGenerateVoucher = YesOrNoEnum.NO.getCode();
            Integer periodCode=null;
            if (!isExistEmpty) {
                isGenerateVoucher = YesOrNoEnum.YES.getCode();
                periodCode = entry.getValue().get(0).getPeriodCode();
            }
            this.lambdaUpdate().set(OutTableAbsEntity::getIsGenerateVoucher, isGenerateVoucher).set(OutTableAbsEntity::getPeriodCode,periodCode).eq(OutTableAbsEntity::getId,entry.getKey()).update();
        }
    }

    @Override
    public Boolean updateProcessStatus(CommonApproveDTO commonApproveDTO) {
        if (StringUtils.isEmpty(commonApproveDTO.getDocumentStatus())) {
            throw new ServiceException("处理状态不可以为空");
        }
        OutTableAbsEntity outTableAbsEntity = this.getById(commonApproveDTO.getDocumentId());
        if (null == outTableAbsEntity) {
            throw new ServiceException("尾差调整信息数据不存在");
        }
        String processStatus = outTableAbsEntity.getProcessStatus();
        try {
            if (ProcessStatusEnum.REVIEWED.getCode().equals(commonApproveDTO.getDocumentStatus())) {
                processStatus = ProcessStatusEnum.REVIEWED.getCode();
                //更新财务合同状态
                updateFinancialContractStatus(outTableAbsEntity.getId());
            } else if (ProcessStatusEnum.REJECTED.getCode().equals(commonApproveDTO.getDocumentStatus())) {
                processStatus = ProcessStatusEnum.REJECTED.getCode();
            }
            outTableAbsEntity.setProcessStatus(processStatus);
            outTableAbsEntity.setApproveErrorInfo("");
            iVoucherService.updateStatusByBatch(Lists.newArrayList(outTableAbsEntity.getId()),BatchTypeEnum.CBABS.getCode(), processStatus,commonApproveDTO.getApproverNum(),commonApproveDTO.getApproverName());
        } catch (Exception e) {
            outTableAbsEntity.setApproveErrorInfo(e.getMessage());
        }
        return this.updateById(outTableAbsEntity);
    }

    public void updateFinancialContractStatus(Long id){
            List<OutTableContractDetailEntity> detailEntityList = iOutTableContractDetailService.lambdaQuery().eq(OutTableContractDetailEntity::getOutTableAbsId,id).list();
            if (CollectionUtil.isEmpty(detailEntityList)) {
                return;
            }
            //按照合同编码+签约主体分组
            Map<String,List<OutTableContractDetailEntity>> detailMap =  detailEntityList.stream().collect(Collectors.groupingBy(v -> v.getContractCode()+"-" + v.getOrgId()));
            List<String> contractCodeList = detailEntityList.stream().map(OutTableContractDetailEntity::getContractCode).collect(Collectors.toList());
            List<String> orgIdList = detailEntityList.stream().map(OutTableContractDetailEntity::getOrgId).collect(Collectors.toList());
            //根据合同编码+签约主体查询所有的合同信息
            List<ContractEntity> contractEntityList = iContractService.lambdaQuery().in(ContractEntity::getContractCode,contractCodeList).in(ContractEntity::getOrgId,orgIdList).list();
            contractEntityList.stream().forEach(v -> {
                String key = v.getContractCode()+"-"+v.getOrgId();
                if (detailMap.containsKey(key)){
                    v.setFinancialContractStatus(detailMap.get(key).get(0).getFinancialContractStatus());
                    v.setFinancialContractStatusUpdateTime(new Date());
                }
            });
            if (CollectionUtil.isNotEmpty(contractEntityList)) {
                iContractService.updateBatchById(contractEntityList);
            }
    }

    @Override
    public List<OutTableAbsVO> selectTableAbsList(List<Long> idList) {
        if (CollectionUtil.isEmpty(idList)) {
            throw new ServiceException("请至少勾选一条数据导出");
        }
        return BeanUtil.copyToList(listByIds(idList),OutTableAbsVO.class);
    }

    @Override
    public List<OutTableContractDetailVO> selectTableContractDetailList(List<Long> idList) {
        Map<String,String> orgIdMap = iOrgCompanyService.list().stream().collect(HashMap::new,(h,v)->h.put(v.getOrgId(),v.getOrgName()),HashMap::putAll);
        List<OutTableContractDetailEntity> outTableContractDetailEntityList = iOutTableContractDetailService.lambdaQuery().in(OutTableContractDetailEntity::getOutTableAbsId,idList).list();
        List<OutTableContractDetailVO> result = Lists.newArrayList();
        Map<Long,List<OutTableContractDetailVO>> detailMap = BeanUtil.copyToList(outTableContractDetailEntityList,OutTableContractDetailVO.class).stream().collect(Collectors.groupingBy(v ->v.getOutTableAbsId()));
        for (Map.Entry<Long, List<OutTableContractDetailVO>> entry : detailMap.entrySet()) {
            OutTableAbsEntity outTableAbsEntity = this.getById(entry.getKey());
            entry.getValue().stream().forEach(v -> {
                v.setPeriods(v.getPeriods());
                if (orgIdMap.containsKey(v.getOrgId())) {
                    v.setOrgIdName(orgIdMap.get(v.getOrgId()));
                }
                getBalance(v,outTableAbsEntity);
            });
            result.addAll(entry.getValue());
        }
        return result;
    }

    @Override
    public List<OutTableContractDetailVO> selectByCondition(OutTableContractDetailQueryDTO queryDTO) {
        return outTableAbsMapper.selectByCondition(queryDTO);
    }

    @Override
    public IPage<OutTableRentPlanVO> rentPlanPage(OutTableAbsQueryDTO queryDTO) {
        Page page = new Page(queryDTO.getPageNum(),queryDTO.getPageSize());
        IPage<OutTableRentPlanVO> outTableRentPlanVOIPage = outTableAbsMapper.selectRentPlanPage(page,queryDTO);
        List<String> contractCodeList = outTableRentPlanVOIPage.getRecords().stream().map(OutTableRentPlanVO::getContractCode).distinct().collect(Collectors.toList());
        Map<String,ContractEntity> contractEntityMap = Maps.newHashMap();
        if (CollectionUtil.isNotEmpty(contractCodeList)) {
            contractEntityMap = iContractService.lambdaQuery().in(ContractEntity::getContractCode,contractCodeList).list().stream().collect(Collectors.toMap(ContractEntity::getContractCode, Function.identity(),(k1,k2)->k2));
        }
        Map<String, ContractEntity> finalContractEntityMap = contractEntityMap;
        outTableRentPlanVOIPage.getRecords().forEach(v -> {
            setRentPlanData(finalContractEntityMap,v);
        });
        return outTableRentPlanVOIPage;
    }

    @Override
    public List<OutTableRentPlanVO> rentPlanList(OutTableAbsQueryDTO queryDTO) {
        List<OutTableRentPlanVO> outTableRentPlanVOList = outTableAbsMapper.selectRentPlanList(queryDTO);
        List<String> contractCodeList = outTableRentPlanVOList.stream().map(OutTableRentPlanVO::getContractCode).distinct().collect(Collectors.toList());
        Map<String,ContractEntity> contractEntityMap = Maps.newHashMap();
        if (CollectionUtil.isNotEmpty(contractCodeList)) {
            contractEntityMap = iContractService.lambdaQuery().in(ContractEntity::getContractCode,contractCodeList).list().stream().collect(Collectors.toMap(ContractEntity::getContractCode, Function.identity(),(k1,k2)->k2));
        }
        Map<String, ContractEntity> finalContractEntityMap = contractEntityMap;
        outTableRentPlanVOList.forEach(v -> {
            setRentPlanData(finalContractEntityMap,v);
        });
        return outTableRentPlanVOList;
    }

    @Override
    public IPage<ContractBalanceVO> selectCheckPage(CheckPageQueryDTO queryDTO) {
        ContractBalanceCheckQueryDTO checkQueryDTO = new ContractBalanceCheckQueryDTO();
        OutTableAbsEntity outTableAbsEntity = this.getById(queryDTO.getId());
//        checkQueryDTO.setPeriodCode(PeriodCodeUtil.periodCodeByLocalDateTime(outTableAbsEntity.getBusinessDate()));
        List<OutTableContractDetailEntity> outTableAbsEntityList = iOutTableContractDetailService.lambdaQuery().eq(OutTableContractDetailEntity::getOutTableAbsId,queryDTO.getId()).list();
        checkQueryDTO.setOrgIdList(outTableAbsEntityList.stream().map(OutTableContractDetailEntity::getOrgId).filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList()));
        checkQueryDTO.setContractCodeList(outTableAbsEntityList.stream().map(OutTableContractDetailEntity::getContractCode).filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList()));
        return iContractBalanceService.selectCheckPage(checkQueryDTO);
    }

    public void setRentPlanData(Map<String,ContractEntity> contractEntityMap,OutTableRentPlanVO v) {
        if (contractEntityMap.containsKey(v.getContractCode())) {
            if (null!=contractEntityMap.get(v.getContractCode()).getTaxRate()) {
                v.setRate(contractEntityMap.get(v.getContractCode()).getTaxRate().toString());
            } else if (LeaseTypeEnum.DIRECT.getCode().equals(contractEntityMap.get(v.getContractCode()).getLeaseType())) {
                //取租赁类型默认值租赁类型为直租是税率13%，租赁类型为回租是税率6%
                v.setRate("13");
            } else if (LeaseTypeEnum.LEASEBACK.getCode().equals(contractEntityMap.get(v.getContractCode()).getLeaseType())) {
                v.setRate("6");
            }
        }
        //实收留够价,实收罚息及手续费 todo
        v.setReceivableResidualValueAmount(null);
        v.setReceivableTerminateAmount(null);
    }

    public void asnyDeleteVoucher(List<Long> voucherIdList){
        if (CollectionUtil.isEmpty(voucherIdList)) {
            return;
        }
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            // 异步任务的代码
            iVoucherService.deleteByIdList(voucherIdList);
        });
    }
    public void setTransferPrice(OutTableContractDetailEntity v,OutTableAbsEntity entity,BigDecimal financeLeaseReceivablesAmountTotal){
        //transferPrice=应收融资租赁款（当前行的金额）/应收融资租赁款汇总（所有行的汇总金额）*转让价格（汇总页的批次转让价格）
        BigDecimal financeLeaseReceivablesAmount =  getFinanceLeaseReceivablesAmount(v);
        BigDecimal transferPrice = (financeLeaseReceivablesAmount.divide(financeLeaseReceivablesAmountTotal,4,BigDecimal.ROUND_HALF_UP)).multiply(entity.getTransferPrice()).setScale(2, RoundingMode.HALF_UP);
        v.setTransferPrice(transferPrice);
    }

    //融资租赁收款
    public BigDecimal getFinanceLeaseReceivablesAmount(OutTableContractDetailEntity v){
        BigDecimal financeLeaseReceivablesAmount =  (null==v.getReceivableRent()?BigDecimal.ZERO:v.getReceivableRent())
                .add(null == v.getReceivableResidualValue()?BigDecimal.ZERO:v.getReceivableResidualValue())
                .add(null == v.getReceivableOuttax()?BigDecimal.ZERO:v.getReceivableOuttax())
                .subtract(null == v.getUnrealizedRevenue()?BigDecimal.ZERO:v.getUnrealizedRevenue());
        return financeLeaseReceivablesAmount;
    }

    public Map<String,Object> getVoucherMap(OutTableAbsEntity entity, OutTableContractDetailEntity v, String isSubmit) {
        //根据合同编号+签约主体获取所有余额表信息
        List<ContractBalanceEntity> balanceEntityList = iContractBalanceService.lambdaQuery()
                .eq(ContractBalanceEntity::getContractCode,v.getContractCode())
                .eq(ContractBalanceEntity::getOrgId,v.getOrgId()).list();
        Map<String,Object> voucherMap = Maps.newHashMap();
        OutTableContractVoucherDTO outTableContractVoucherDTO = new OutTableContractVoucherDTO();
        outTableContractVoucherDTO.setSystemCode(SystemEnum.CWZT.getCode());
        outTableContractVoucherDTO.setSystemName(SystemEnum.CWZT.getDesc());
        outTableContractVoucherDTO.setOrderId(v.getId().toString());
        outTableContractVoucherDTO.setBusinessCode(BusinessEnum.ZLYW.getCode());
        outTableContractVoucherDTO.setBusinessName(BusinessEnum.ZLYW.getDesc());
        outTableContractVoucherDTO.setSceneCode(SceneEnum.CBABS.getCode());
        outTableContractVoucherDTO.setSceneName(SceneEnum.CBABS.getDesc());
        outTableContractVoucherDTO.setBusinessDate(CommonDateUtils.parseLocalDateTimeToDate(entity.getBusinessDate()));
        outTableContractVoucherDTO.setContractCode(v.getContractCode());
        outTableContractVoucherDTO.setClientCode(v.getClientCode());
        outTableContractVoucherDTO.setClientName(v.getClientName());
        outTableContractVoucherDTO.setOrgId(v.getOrgId());
        outTableContractVoucherDTO.setRetainedPrice(null == v.getReceivableResidualValue() ? BigDecimal.ZERO : v.getReceivableResidualValue());
        outTableContractVoucherDTO.setReceivableLeaseAmount(null == v.getReceivableRent()? BigDecimal.ZERO :  v.getReceivableRent());
        outTableContractVoucherDTO.setReceivableOuttaxAmount(null == v.getReceivableOuttax() ? BigDecimal.ZERO : v.getReceivableOuttax());
        outTableContractVoucherDTO.setUnrealizedRevenueAmount(null == v.getUnrealizedRevenue() ? BigDecimal.ZERO : v.getUnrealizedRevenue());
        outTableContractVoucherDTO.setReceivableMarginAmount(null == v.getLesseeMargin() ? BigDecimal.ZERO : v.getLesseeMargin());
        outTableContractVoucherDTO.setTransferPrice(null == v.getTransferPrice() ? BigDecimal.ZERO : v.getTransferPrice());
        outTableContractVoucherDTO.setBatchId(v.getOutTableAbsId());
        outTableContractVoucherDTO.setBatchType(BatchTypeEnum.CBABS.getCode());
        outTableContractVoucherDTO.setBillContractCode(entity.getLoanContractCode());
        outTableContractVoucherDTO.setTransferPeriod(entity.getPeriods());
        outTableContractVoucherDTO.setIsSubmit(isSubmit);
        //未确认收款 = 场景为ZLSK，封包日<=余额表凭证日期<=发行日时，receivable_unconfirm_receipt_amount的汇总金额
        final BigDecimal[] receiveUnconfirmed = {BigDecimal.ZERO};
        //收取应收租金 = 场景为ZLSK，封包日<=余额表凭证日期<=发行日，lessee_margin_amount<=0时，receivable_rent_amount的汇总金额
        final BigDecimal[] receiveLeaseAmount = {BigDecimal.ZERO};
        //收取留购价 = 场景为ZLSK，封包日<=余额表凭证日期<=发行日，lessee_margin_amount<=0时，receivable_residual_value_amount的汇总金额;
        final BigDecimal[] receiveRetainedPrice = {BigDecimal.ZERO};
        //收取罚息收入 = 场景为ZLSK，封包日<=余额表凭证日期<=发行日，lessee_margin_amount<=0时，receivable_default_interest_amount的汇总金额
        final BigDecimal[] receiveDefaultInterestAmount = {BigDecimal.ZERO};
        //收取合同解约及更改手续费 = 场景为ZLSK，封包日<=余额表凭证日期<=发行日，lessee_margin_amount<=0时，receivable_terminate_amount的汇总金额
        final BigDecimal[] receiveTerminateProcedureAmount = {BigDecimal.ZERO};
        //收取承租人保证金 =场景为ZLSK，封包日<=余额表凭证日期<=发行日，lessee_margin_amount<=0时，lessee_margin_amount的汇总金额
        final BigDecimal[] receiveMarginAmount = {BigDecimal.ZERO};
        //应收销项税 = 场景为KJFP，封包日<=余额表凭证日期<=发行日时，receivable_outtax_amount的汇总金额
        final BigDecimal[] receivableOuttaxAmountKP = {BigDecimal.ZERO};
        //应收服务费销项税 = 场景为KJFP，封包日<=余额表凭证日期<=发行日时，receivable_service_outtax_amount的汇总金额
        final BigDecimal[] receivableServiceOuttaxAmountKP = {BigDecimal.ZERO};
        //抵扣保证金 = 场景为ZLSK，封包日<=余额表凭证日期<=发行日，lessee_margin_amount>0时，lessee_margin_amount的汇总金额
        final BigDecimal[] deductionMarginAmount = {BigDecimal.ZERO};
        //保证金抵扣应收租金 = 场景为ZLSK，封包日<=余额表凭证日期<=发行日，lessee_margin_amount>时，receivable_rent_amount的汇总金额
        final BigDecimal[] deductionLeaseAmount = {BigDecimal.ZERO};
        //保证金抵扣留购价 = 场景为ZLSK，封包日<=余额表凭证日期<=发行日，lessee_margin_amount>时，receivable_residual_value_amount的汇总金额
        final BigDecimal[] deductionRetainedPrice = {BigDecimal.ZERO};
        //保证金抵扣罚息收入 = 场景为ZLSK，封包日<=余额表凭证日期<=发行日，lessee_margin_amount>时，receivable_default_interest_amount的汇总金额
        final BigDecimal[] deductionDefaultInterestAmount = {BigDecimal.ZERO};
        //保证金抵扣合同解约及更改手续费=场景为ZLSK，封包日<=余额表凭证日期<=发行日，lessee_margin_amount>时，receivable_terminate_amount的汇总金额
        final BigDecimal[] deductionTerminateProcedureAmount = {BigDecimal.ZERO};
        //转让损益 == 转让价格（合同维度）-应收融资租赁款
        final BigDecimal transferGainsAndLosses = (null == v.getTransferPrice() ? BigDecimal.ZERO : v.getTransferPrice()).subtract(getFinanceLeaseReceivablesAmount(v));
        //发行日应收融资租赁款减值损失=余额表凭证日期<=发行日，创建日期为最新时，depreciation_loss_balance的金额
        BigDecimal depreciationLoss = BigDecimal.ZERO;
        //发行日应收租赁款组合拨备 = 余额表凭证日期<=发行日，创建日期为最新时，depreciation_reserves_balance的金额
        BigDecimal depreciationReserves = BigDecimal.ZERO;
        //租赁收益 = 场景为SYJT，封包日<=余额表凭证日期<=发行日，lease_revenue_amount汇总金额+lease_revenue6_amount汇总金额
        final BigDecimal[] leaseRevenue = {BigDecimal.ZERO};
        //封包日后租金调整=场景为JYJGBG，封包日<=余额表凭证日期<=发行日，receivable_rent_amount汇总金额
        final BigDecimal[] receivableLeaseAdjustAmount = {BigDecimal.ZERO};
        //封包日后首付款调整 = 场景为JYJGBG，封包日<=余额表凭证日期<=发行日，receivable_downpayment_amount汇总金额
        final BigDecimal[] firstAdjustAmount = {BigDecimal.ZERO};
        //封包日后手续费调整=场景为JYJGBG，封包日<=余额表凭证日期<=发行日，receivable_commission_amount汇总金额
        final BigDecimal[] procedureAdjustRevenues = {BigDecimal.ZERO};
        //封包日后应收保险费调整=场景为JYJGBG，封包日<=余额表凭证日期<=发行日，receivable_insurance_amount汇总金额
        final BigDecimal[] insuranceAdjustAmount = {BigDecimal.ZERO};
        //封包日后应付保险费调整=场景为JYJGBG，封包日<=余额表凭证日期<=发行日，payable_insurance_estimate_amount汇总金额
        final BigDecimal[] payableInsuranceEstimateAdjustAmount = {BigDecimal.ZERO};
        //封包日后留购价调整 = 场景为JYJGBG，封包日<=余额表凭证日期<=发行日，receivable_residual_value_amount汇总金额
        final BigDecimal[] residualAdjustAmount = {BigDecimal.ZERO};
        //封包日后其他收入调整 = 场景为JYJGBG，封包日<=余额表凭证日期<=发行日，receivable_otherincome_amount汇总金额
        final BigDecimal[] otherAdjustRevenues = {BigDecimal.ZERO};
        //封包日后设备款调整=场景为JYJGBG，封包日<=余额表凭证日期<=发行日，payable_device_estimate_amount汇总金额
        final BigDecimal[] payableDeviceAdjustAmount = {BigDecimal.ZERO};
        //封包日后其他成本调整=场景为JYJGBG，封包日<=余额表凭证日期<=发行日，payable_other_cost_estimate_amount汇总金额
        final BigDecimal[] otherCostAdjustAmount = {BigDecimal.ZERO};
        //封包日后经销商服务费调整=场景为JYJGBG，封包日<=余额表凭证日期<=发行日，payable_agency_estimate_amount汇总金额
        final BigDecimal[] payableServiceAdjustAmount = {BigDecimal.ZERO};
        //封包日后手环成本调整=场景为JYJGBG，封包日<=余额表凭证日期<=发行日，payable_band_cost_estimate_amount汇总金额
        final BigDecimal[] payableBraceletAdjustAmount = {BigDecimal.ZERO};
        //封包日后应收销项税调整=场景为JYJGBG，封包日<=余额表凭证日期<=发行日，receivable_outtax_amount汇总金额
        final BigDecimal[] receivableOuttaxAdjustAmount = {BigDecimal.ZERO};
        //封包日后未实现收益调整=场景为JYJGBG，封包日<=余额表凭证日期<=发行日，unrealized_revenue_amount汇总金额
        final BigDecimal[] unrealizedRevenueAdjustAmount = {BigDecimal.ZERO};
        //封包日后服务费调整=场景为JYJGBG，封包日<=余额表凭证日期<=发行日，receivable_service_amount汇总金额
        final BigDecimal[] serviceAdjustAmount = {BigDecimal.ZERO};
        //封包日后服务费销项税调整=场景为JYJGBG，封包日<=余额表凭证日期<=发行日，receivable_service_outtax_amount汇总金额
        final BigDecimal[] receivableServiceOuttaxAdjustAmount = {BigDecimal.ZERO};
        //封包日后服务收入调整=场景为JYJGBG，封包日<=余额表凭证日期<=发行日，service_revenue_amount汇总金额
        final BigDecimal[] serviceEevenueAdjustAmount = {BigDecimal.ZERO};
        //封包日后销项税额调整=场景为JYJGBG，封包日<=余额表凭证日期<=发行日，outtax_amount汇总金额
        final BigDecimal[] outtaxAdjustAmount = {BigDecimal.ZERO};
        //博远封包日后应收总额调整=场景为JYJGBG，封包日<=余额表凭证日期<=发行日，receive_sum_amount汇总金额
        final BigDecimal[] receiveSumAdjustAmount = {BigDecimal.ZERO};
        //博远封包日后应收销项税调整=场景为JYJGBG，封包日<=余额表凭证日期<=发行日，receive_sum_outtax_amount汇总金额
        final BigDecimal[] receiveSumOuttaxAdjustAmount = {BigDecimal.ZERO};
        //博远封包日后应付其他款项调整=场景为JYJGBG，封包日<=余额表凭证日期<=发行日，payable_other_estimate_amount汇总金额
        final BigDecimal[] payableOtherEstimateAdjustAmount = {BigDecimal.ZERO};
        //博远封包日后未实现收益调整=场景为JYJGBG，封包日<=余额表凭证日期<=发行日，receive_unrealized_revenue_amount汇总金额
        final BigDecimal[] receiveUnrealizedRevenueAdjustAmount = {BigDecimal.ZERO};

        //收取首付款=场景为ZLSK，封包日<=余额表凭证日期<=发行日，lessee_margin_amount<=0时，receivable_downpayment_amount的汇总金额
        final BigDecimal[] receiveDownpaymentAmount = {BigDecimal.ZERO};
        //收取手续费=场景为ZLSK，封包日<=余额表凭证日期<=发行日，lessee_margin_amount<=0时，receivable_commission_amount的汇总金额
        final BigDecimal[] receiveCommissionAmount = {BigDecimal.ZERO};
        //收取服务费=场景为ZLSK，封包日<=余额表凭证日期<=发行日，lessee_margin_amount<=0时，receivable_service_amount的汇总金额
        final BigDecimal[] receivesServiceAmount = {BigDecimal.ZERO};
        //收取保险费=场景为ZLSK，封包日<=余额表凭证日期<=发行日，lessee_margin_amount<=0时，receivable_insurance_amount的汇总金额
        final BigDecimal[] receiveInsuranceAmount = {BigDecimal.ZERO};
        //收取保险费差额=场景为ZLSK，封包日<=余额表凭证日期<=发行日，lessee_margin_amount<=0时，insurance_differ_amount的汇总金额
        final BigDecimal[] receiveInsuranceDifferAmount = {BigDecimal.ZERO};
        //收取其他收入=场景为ZLSK，封包日<=余额表凭证日期<=发行日，lessee_margin_amount<=0时，receivable_otherincome_amount的汇总金额
        final BigDecimal[] receiveOtherincomeAmount = {BigDecimal.ZERO};
        //收取违约金收入=场景为ZLSK，封包日<=余额表凭证日期<=发行日，lessee_margin_amount<=0时，receivable_damages_revenue_amount的汇总金额
        final BigDecimal[] receiveDamagesRevenueAmount = {BigDecimal.ZERO};
        //收取其他租赁收入=场景为ZLSK，封包日<=余额表凭证日期<=发行日，lessee_margin_amount<=0时，receivable_other_revenue_amount的汇总金额
        final BigDecimal[] receiveOtherRevenueAmount = {BigDecimal.ZERO};

        //罚息收入确认不含税额=场景为ZLSK，封包日<=余额表凭证日期<=发行日，lessee_margin_amount<=0时，dinterest_revenue_amount的汇总金额
        final BigDecimal[] dinterestRevenueAmount = {BigDecimal.ZERO};
        //变更手续费收入确认不含税额=场景为ZLSK，封包日<=余额表凭证日期<=发行日，lessee_margin_amount<=0时，terminate_amount的汇总金额
        final BigDecimal[] terminateAmount = {BigDecimal.ZERO};
        //违约金收入确认不含税额=场景为ZLSK，封包日<=余额表凭证日期<=发行日，lessee_margin_amount<=0时，damages_revenue_amount的汇总金额
        final BigDecimal[] damagesRevenueAmount = {BigDecimal.ZERO};
        //其他租赁收入确认不含税额=场景为ZLSK，封包日<=余额表凭证日期<=发行日，lessee_margin_amount<=0时，other_revenue_amount的汇总金额
        final BigDecimal[] otherRevenueAmount = {BigDecimal.ZERO};
        //税金计提金额=场景为ZLSK，封包日<=余额表凭证日期<=发行日，receivable_outtax_amount>0时的汇总金额
        final BigDecimal[] receivableOuttaxAmountJT = {BigDecimal.ZERO};
        //保证金抵扣罚息收入不含税额=场景为ZLSK，封包日<=余额表凭证日期<=发行日，lessee_margin_amount>0时，dinterest_revenue_amount的汇总金额
        final BigDecimal[] deductionDefaultNoTaxAmount = {BigDecimal.ZERO};
        //保证金抵扣变更手续费收入不含税额=场景为ZLSK，封包日<=余额表凭证日期<=发行日，lessee_margin_amount>0时，terminate_amount的汇总金额
        final BigDecimal[] deductionTerminateNoTaxAmount = {BigDecimal.ZERO};
        //保证金抵扣税金计提金额=场景为ZLSK，封包日<=余额表凭证日期<=发行日，lessee_margin_amount>时，receivable_outtax_amount>0时的汇总金额
        final BigDecimal[] deductionreceivableOuttaxAmountJT = {BigDecimal.ZERO};
        balanceEntityList.stream().forEach(b -> {
            LocalDateTime voucherDate = null == b.getVoucherDate() ? null : b.getVoucherDate().withHour(0).withMinute(0).withSecond(0);
            if (SceneEnum.ZLSK.getCode().equals(b.getSceneCode())
                    && null!=voucherDate
                    && voucherDate.compareTo(entity.getCloseDate())>=0
                    && voucherDate.compareTo(entity.getReleaseDate())<=0) {
                receiveUnconfirmed[0] = receiveUnconfirmed[0].add(null == b.getReceivableUnconfirmReceiptAmount() ? BigDecimal.ZERO : b.getReceivableUnconfirmReceiptAmount());
                if (null != b.getReceivableOuttaxAmount() && b.getReceivableOuttaxAmount().compareTo(BigDecimal.ZERO)>0) {
                    receivableOuttaxAmountJT[0] = receivableOuttaxAmountJT[0].add(b.getReceivableOuttaxAmount());
                }
                if (null == b.getLesseeMarginAmount() || b.getLesseeMarginAmount().compareTo(BigDecimal.ZERO)<=0) {
                    receiveLeaseAmount[0] = receiveLeaseAmount[0].add(null == b.getReceivableRentAmount() ? BigDecimal.ZERO : b.getReceivableRentAmount());
                    receiveRetainedPrice[0] = receiveRetainedPrice[0].add(null == b.getReceivableResidualValueAmount() ? BigDecimal.ZERO : b.getReceivableResidualValueAmount());
                    receiveDefaultInterestAmount[0] = receiveDefaultInterestAmount[0].add(null == b.getReceivableDefaultInterestAmount() ? BigDecimal.ZERO : b.getReceivableDefaultInterestAmount());
                    receiveTerminateProcedureAmount[0] = receiveTerminateProcedureAmount[0].add(null == b.getReceivableTerminateAmount() ? BigDecimal.ZERO : b.getReceivableTerminateAmount());
                    receiveMarginAmount[0] = receiveMarginAmount[0].add(null == b.getLesseeMarginAmount() ? BigDecimal.ZERO : b.getLesseeMarginAmount());

                    receiveDownpaymentAmount[0] = receiveDownpaymentAmount[0].add(null == b.getReceivableDownpaymentAmount() ? BigDecimal.ZERO : b.getReceivableDownpaymentAmount());

                    receiveCommissionAmount[0] = receiveCommissionAmount[0].add(null == b.getReceivableCommissionAmount() ? BigDecimal.ZERO : b.getReceivableCommissionAmount());
                    receivesServiceAmount[0] = receivesServiceAmount[0].add(null == b.getReceivableServiceAmount() ? BigDecimal.ZERO : b.getReceivableServiceAmount());

                    receiveInsuranceAmount[0] = receiveInsuranceAmount[0].add(null == b.getReceivableInsuranceAmount() ? BigDecimal.ZERO : b.getReceivableInsuranceAmount());
                    receiveInsuranceDifferAmount[0] = receiveInsuranceDifferAmount[0].add(null == b.getInsuranceDifferAmount() ? BigDecimal.ZERO : b.getInsuranceDifferAmount());
                    receiveOtherincomeAmount[0] = receiveOtherincomeAmount[0].add(null == b.getReceivableOtherincomeAmount() ? BigDecimal.ZERO : b.getReceivableOtherincomeAmount());
                    receiveDamagesRevenueAmount[0] = receiveDamagesRevenueAmount[0].add(null == b.getReceivableDamagesRevenueAmount() ? BigDecimal.ZERO : b.getReceivableDamagesRevenueAmount());
                    receiveOtherRevenueAmount[0] = receiveOtherRevenueAmount[0].add(null == b.getReceivableOtherRevenueAmount() ? BigDecimal.ZERO : b.getReceivableOtherRevenueAmount());

                    dinterestRevenueAmount[0] = dinterestRevenueAmount[0].add(null == b.getDinterestRevenueAmount() ? BigDecimal.ZERO : b.getDinterestRevenueAmount());

                    terminateAmount[0] = terminateAmount[0].add(null == b.getTerminateAmount() ? BigDecimal.ZERO : b.getTerminateAmount());

                    damagesRevenueAmount[0] = damagesRevenueAmount[0].add(null == b.getDamagesRevenueAmount() ? BigDecimal.ZERO : b.getDamagesRevenueAmount());
                    otherRevenueAmount[0] = otherRevenueAmount[0].add(null == b.getOtherRevenueAmount() ? BigDecimal.ZERO : b.getOtherRevenueAmount());
                } else {
                    deductionMarginAmount[0] = deductionMarginAmount[0].add(null == b.getLesseeMarginAmount() ? BigDecimal.ZERO : b.getLesseeMarginAmount());
                    deductionLeaseAmount[0] = deductionLeaseAmount[0].add(null == b.getReceivableRentAmount() ? BigDecimal.ZERO : b.getReceivableRentAmount());
                    deductionRetainedPrice[0] = deductionRetainedPrice[0].add(null == b.getReceivableResidualValueAmount() ? BigDecimal.ZERO : b.getReceivableResidualValueAmount());
                    deductionDefaultInterestAmount[0] = deductionDefaultInterestAmount[0].add(null == b.getReceivableDefaultInterestAmount() ? BigDecimal.ZERO : b.getReceivableDefaultInterestAmount());
                    deductionTerminateProcedureAmount[0] = deductionTerminateProcedureAmount[0].add(null == b.getReceivableTerminateAmount() ? BigDecimal.ZERO : b.getReceivableTerminateAmount());

                    deductionDefaultNoTaxAmount[0] = deductionDefaultNoTaxAmount[0].add(null == b.getDinterestRevenueAmount() ? BigDecimal.ZERO : b.getDinterestRevenueAmount());

                    deductionTerminateNoTaxAmount[0] = deductionTerminateNoTaxAmount[0].add(null == b.getTerminateAmount() ? BigDecimal.ZERO : b.getTerminateAmount());
                    deductionreceivableOuttaxAmountJT[0] = deductionreceivableOuttaxAmountJT[0].add(null == b.getReceivableOuttaxAmount() ? BigDecimal.ZERO : b.getReceivableOuttaxAmount());

                }
            } else if (SceneEnum.KJFP.getCode().equals(b.getSceneCode())
                    && null!=voucherDate
                    && voucherDate.compareTo(entity.getCloseDate())>=0
                    && voucherDate.compareTo(entity.getReleaseDate())<=0) {
                receivableOuttaxAmountKP[0] = receivableOuttaxAmountKP[0].add(null == b.getReceivableOuttaxAmount() ? BigDecimal.ZERO : b.getReceivableOuttaxAmount());
                receivableServiceOuttaxAmountKP[0] = receivableServiceOuttaxAmountKP[0].add(null == b.getReceivableServiceOuttaxAmount() ? BigDecimal.ZERO : b.getReceivableServiceOuttaxAmount());

            }else if (SceneEnum.SYJT.getCode().equals(b.getSceneCode())
                    && null!=voucherDate
                    && voucherDate.compareTo(entity.getCloseDate())>=0
                    && voucherDate.compareTo(entity.getReleaseDate())<=0) {
                leaseRevenue[0] = leaseRevenue[0].add(null == b.getLeaseRevenueAmount() ? BigDecimal.ZERO : b.getLeaseRevenueAmount())
                .add(null == b.getLeaseRevenue6Amount() ? BigDecimal.ZERO : b.getLeaseRevenue6Amount());
            } else if (SceneEnum.JYJGBG.getCode().equals(b.getSceneCode())
                    && null!=voucherDate
                    && voucherDate.compareTo(entity.getCloseDate())>=0
                    && voucherDate.compareTo(entity.getReleaseDate())<=0) {
                receivableLeaseAdjustAmount[0] = receivableLeaseAdjustAmount[0].add(null == b.getReceivableRentAmount() ? BigDecimal.ZERO : b.getReceivableRentAmount());
                firstAdjustAmount[0] = firstAdjustAmount[0].add(null == b.getReceivableDownpaymentAmount() ? BigDecimal.ZERO : b.getReceivableDownpaymentAmount());
                procedureAdjustRevenues[0] = procedureAdjustRevenues[0].add(null == b.getReceivableCommissionAmount() ? BigDecimal.ZERO : b.getReceivableCommissionAmount());
                insuranceAdjustAmount[0] = insuranceAdjustAmount[0].add(null == b.getReceivableInsuranceAmount() ? BigDecimal.ZERO : b.getReceivableInsuranceAmount());
                payableInsuranceEstimateAdjustAmount[0] = payableInsuranceEstimateAdjustAmount[0].add(null == b.getPayableInsuranceEstimateAmount() ? BigDecimal.ZERO : b.getPayableInsuranceEstimateAmount());
                residualAdjustAmount[0] = residualAdjustAmount[0].add(null == b.getReceivableResidualValueAmount() ? BigDecimal.ZERO : b.getReceivableResidualValueAmount());
                otherAdjustRevenues[0] = otherAdjustRevenues[0].add(null == b.getReceivableOtherincomeAmount() ? BigDecimal.ZERO : b.getReceivableOtherincomeAmount());
                payableDeviceAdjustAmount[0] = payableDeviceAdjustAmount[0].add(null == b.getPayableDeviceEstimateAmount() ? BigDecimal.ZERO : b.getPayableDeviceEstimateAmount());
                otherCostAdjustAmount[0] = otherCostAdjustAmount[0].add(null == b.getPayableOtherCostEstimateAmount() ? BigDecimal.ZERO : b.getPayableOtherCostEstimateAmount());
                payableServiceAdjustAmount[0] = payableServiceAdjustAmount[0].add(null == b.getPayableAgencyEstimateAmount() ? BigDecimal.ZERO : b.getPayableAgencyEstimateAmount());
                payableBraceletAdjustAmount[0] = payableBraceletAdjustAmount[0].add(null == b.getPayableBandCostEstimateAmount() ? BigDecimal.ZERO : b.getPayableBandCostEstimateAmount());
                receivableOuttaxAdjustAmount[0] = receivableOuttaxAdjustAmount[0].add(null == b.getReceivableOuttaxAmount() ? BigDecimal.ZERO : b.getReceivableOuttaxAmount());
                unrealizedRevenueAdjustAmount[0] = unrealizedRevenueAdjustAmount[0].add(null == b.getUnrealizedRevenueAmount() ? BigDecimal.ZERO : b.getUnrealizedRevenueAmount());
                serviceAdjustAmount[0] = serviceAdjustAmount[0].add(null == b.getReceivableServiceAmount() ? BigDecimal.ZERO : b.getReceivableServiceAmount());
                receivableServiceOuttaxAdjustAmount[0] = receivableServiceOuttaxAdjustAmount[0].add(null == b.getReceivableServiceOuttaxAmount() ? BigDecimal.ZERO : b.getReceivableServiceOuttaxAmount());
                serviceEevenueAdjustAmount[0] = serviceEevenueAdjustAmount[0].add(null == b.getServiceRevenueAmount() ? BigDecimal.ZERO : b.getServiceRevenueAmount());
                outtaxAdjustAmount[0] = outtaxAdjustAmount[0].add(null == b.getOuttaxAmount() ? BigDecimal.ZERO : b.getOuttaxAmount());
                receiveSumAdjustAmount[0] = receiveSumAdjustAmount[0].add(null == b.getReceiveSumAmount() ? BigDecimal.ZERO : b.getReceiveSumAmount());
                receiveSumOuttaxAdjustAmount[0] = receiveSumOuttaxAdjustAmount[0].add(null == b.getReceiveSumOuttaxAmount() ? BigDecimal.ZERO : b.getReceiveSumOuttaxAmount());
                payableOtherEstimateAdjustAmount[0] = payableOtherEstimateAdjustAmount[0].add(null == b.getPayableOtherEstimateAmount() ? BigDecimal.ZERO : b.getPayableOtherEstimateAmount());
                receiveUnrealizedRevenueAdjustAmount[0] = receiveUnrealizedRevenueAdjustAmount[0].add(null == b.getReceiveUnrealizedRevenueAmount() ? BigDecimal.ZERO : b.getReceiveUnrealizedRevenueAmount());

            }
        });
        List<ContractBalanceEntity> latestBalanceEntityList = balanceEntityList.stream().filter(e -> null!=e.getVoucherDate() &&  entity.getReleaseDate().compareTo(e.getVoucherDate().withHour(0).withMinute(0).withSecond(0))>=0).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(latestBalanceEntityList)) {
            ContractBalanceEntity latestBalanceEntity = latestBalanceEntityList.stream().max(Comparator.comparing(ContractBalanceEntity::getId)).get();
            depreciationLoss = null == latestBalanceEntity.getDepreciationLossBalance() ? BigDecimal.ZERO : latestBalanceEntity.getDepreciationLossBalance();
            depreciationReserves = null == latestBalanceEntity.getDepreciationReservesBalance() ? BigDecimal.ZERO : latestBalanceEntity.getDepreciationReservesBalance();
        }
        outTableContractVoucherDTO.setReceiveUnconfirmed(receiveUnconfirmed[0]);
        outTableContractVoucherDTO.setReceivableOuttaxAmount(receiveLeaseAmount[0]);
        outTableContractVoucherDTO.setReceiveRetainedPrice(receiveRetainedPrice[0]);
        outTableContractVoucherDTO.setReceiveDefaultInterestAmount(receiveDefaultInterestAmount[0]);
        outTableContractVoucherDTO.setReceiveTerminateProcedureAmount(receiveTerminateProcedureAmount[0]);
        outTableContractVoucherDTO.setReceiveMarginAmount(receiveMarginAmount[0]);
        outTableContractVoucherDTO.setReceivableOuttaxAmountKP(receivableOuttaxAmountKP[0]);
        outTableContractVoucherDTO.setReceivableServiceOuttaxAmountKP(receivableServiceOuttaxAmountKP[0]);
        outTableContractVoucherDTO.setDeductionMarginAmount(deductionMarginAmount[0]);
        outTableContractVoucherDTO.setDeductionLeaseAmount(deductionLeaseAmount[0]);
        outTableContractVoucherDTO.setDeductionRetainedPrice(deductionRetainedPrice[0]);
        outTableContractVoucherDTO.setDeductionDefaultInterestAmount(deductionDefaultInterestAmount[0]);
        outTableContractVoucherDTO.setDeductionTerminateProcedureAmount(deductionTerminateProcedureAmount[0]);
        outTableContractVoucherDTO.setTransferGainsAndLosses(transferGainsAndLosses);
        outTableContractVoucherDTO.setDepreciationLoss(depreciationLoss);
        outTableContractVoucherDTO.setDepreciationReserves(depreciationReserves);
        outTableContractVoucherDTO.setLeaseRevenue(leaseRevenue[0]);
        outTableContractVoucherDTO.setReceivableLeaseAdjustAmount(receivableLeaseAdjustAmount[0]);
        outTableContractVoucherDTO.setFirstAdjustAmount(firstAdjustAmount[0]);
        outTableContractVoucherDTO.setProcedureAdjustRevenues(procedureAdjustRevenues[0]);
        outTableContractVoucherDTO.setInsuranceAdjustAmount(insuranceAdjustAmount[0]);
        outTableContractVoucherDTO.setPayableInsuranceEstimateAdjustAmount(payableInsuranceEstimateAdjustAmount[0]);
        outTableContractVoucherDTO.setResidualAdjustAmount(residualAdjustAmount[0]);
        outTableContractVoucherDTO.setOtherAdjustRevenues(otherAdjustRevenues[0]);
        outTableContractVoucherDTO.setPayableDeviceAdjustAmount(payableDeviceAdjustAmount[0]);
        outTableContractVoucherDTO.setOtherCostAdjustAmount(otherCostAdjustAmount[0]);
        outTableContractVoucherDTO.setPayableServiceAdjustAmount(payableServiceAdjustAmount[0]);
        outTableContractVoucherDTO.setPayableBraceletAdjustAmount(payableBraceletAdjustAmount[0]);
        outTableContractVoucherDTO.setReceivableOuttaxAdjustAmount(receivableOuttaxAdjustAmount[0]);
        outTableContractVoucherDTO.setUnrealizedRevenueAdjustAmount(unrealizedRevenueAdjustAmount[0]);
        outTableContractVoucherDTO.setServiceAdjustAmount(serviceAdjustAmount[0]);
        outTableContractVoucherDTO.setReceivableServiceOuttaxAdjustAmount(receivableServiceOuttaxAdjustAmount[0]);
        outTableContractVoucherDTO.setServiceEevenueAdjustAmount(serviceEevenueAdjustAmount[0]);
        outTableContractVoucherDTO.setOuttaxAdjustAmount(outtaxAdjustAmount[0]);
        outTableContractVoucherDTO.setReceiveSumAdjustAmount(receiveSumAdjustAmount[0]);
        outTableContractVoucherDTO.setReceiveSumOuttaxAdjustAmount(receiveSumOuttaxAdjustAmount[0]);
        outTableContractVoucherDTO.setReceivableOuttaxAdjustAmount(receivableOuttaxAdjustAmount[0]);
        outTableContractVoucherDTO.setReceiveUnrealizedRevenueAdjustAmount(receiveUnrealizedRevenueAdjustAmount[0]);
        outTableContractVoucherDTO.setReceiveCommissionAmount(receiveCommissionAmount[0]);
        outTableContractVoucherDTO.setReceivesServiceAmount(receivesServiceAmount[0]);
        outTableContractVoucherDTO.setReceiveInsuranceAmount(receiveInsuranceAmount[0]);
        outTableContractVoucherDTO.setReceiveInsuranceDifferAmount(receiveInsuranceDifferAmount[0]);
        outTableContractVoucherDTO.setReceiveOtherincomeAmount(receiveOtherincomeAmount[0]);
        outTableContractVoucherDTO.setReceiveDamagesRevenueAmount(receiveDamagesRevenueAmount[0]);
        outTableContractVoucherDTO.setReceiveOtherRevenueAmount(receiveOtherRevenueAmount[0]);
        outTableContractVoucherDTO.setDinterestRevenueAmount(dinterestRevenueAmount[0]);
        outTableContractVoucherDTO.setTerminateAmount(terminateAmount[0]);
        outTableContractVoucherDTO.setDamagesRevenueAmount(damagesRevenueAmount[0]);
        outTableContractVoucherDTO.setOtherRevenueAmount(otherRevenueAmount[0]);
        outTableContractVoucherDTO.setReceivableOuttaxAmountJT(receivableOuttaxAmountJT[0]);

        voucherMap = BeanUtil.beanToMap(outTableContractVoucherDTO);
        return voucherMap;
    }

}

