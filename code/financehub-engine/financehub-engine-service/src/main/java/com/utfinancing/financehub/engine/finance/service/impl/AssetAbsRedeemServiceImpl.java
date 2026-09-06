package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
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
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.*;
import com.utfinancing.financehub.engine.finance.mapper.AssetAbsRedeemMapper;
import com.utfinancing.financehub.engine.finance.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import com.utfinancing.financehub.engine.rule.model.vo.VoucherInfoVO;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import com.utfinancing.financehub.engine.utils.CommonDateUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author : bruyang
 * @Date : Create in 2024-03-15
 * @Description :  AssetAbsRedeem服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class AssetAbsRedeemServiceImpl extends ServiceImpl<AssetAbsRedeemMapper, AssetAbsRedeemEntity> implements IAssetAbsRedeemService {

    private final AssetAbsRedeemMapper assetAbsRedeemMapper;
    private final IOutTableAbsService iOutTableAbsService;
    private final IAssetAbsRedeemDetailService iAssetAbsRedeemDetailService;
    private final IContractBalanceService iContractBalanceService;
    private final IVoucherService iVoucherService;
    private final IApproveService iApproveService;
    private final IContractService iContractService;
    @Value("${approve.url.assetAbsRedeem-url:null}")
    private String approveUrl;

    private final IRuleService iRuleService;
    private final IOrgCompanyService iOrgCompanyService;
    private final IRepaymentPlanHisService iRepaymentPlanHisService;
    private final IRepaymentPlanService iRepaymentPlanService;

    @Override
    public Long saveAssetAbsRedeem(AssetAbsRedeemDTO dto) {
        AssetAbsRedeemEntity entity = BeanUtil.copyProperties(dto, AssetAbsRedeemEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateAssetAbsRedeem(Long id, AssetAbsRedeemDTO dto) {
        AssetAbsRedeemEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public AssetAbsRedeemDTO getAssetAbsRedeemDTOById(Long id) {
        AssetAbsRedeemEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, AssetAbsRedeemDTO.class);
    }

    @Override
    public IPage<AssetAbsRedeemVO> selectPage(AssetAbsRedeemQueryDTO queryDTO) {
        LambdaQueryWrapper<AssetAbsRedeemEntity> queryWrapper = getQueryWrapper(queryDTO);
        //这里注入查询条件
        IPage<AssetAbsRedeemEntity> entityIPage = assetAbsRedeemMapper.selectPage(new Page<AssetAbsRedeemEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        IPage<AssetAbsRedeemVO> redeemVOIPage = ListBeanUtil.copyPage(entityIPage, AssetAbsRedeemVO.class);
        redeemVOIPage.getRecords().stream().forEach(v -> {
            setRedeemPriceTotal(v);
        });
        return redeemVOIPage;
    }

    public void setRedeemPriceTotal(AssetAbsRedeemVO v){
        List<AssetAbsRedeemDetailEntity> detailEntityList = iAssetAbsRedeemDetailService.lambdaQuery().eq(AssetAbsRedeemDetailEntity::getAssetAbsRedeemId,v.getId()).list();
        BigDecimal redeemPrice = detailEntityList.stream().filter(d ->null!=d.getRedeemPrice()).collect(Collectors.toList()).stream().map(AssetAbsRedeemDetailEntity::getRedeemPrice).reduce(BigDecimal.ZERO, (a, b) -> NumberUtil.add(a, b)).setScale(2, RoundingMode.HALF_UP);
        v.setRedeemPriceTotal(redeemPrice);
    }

    public LambdaQueryWrapper<AssetAbsRedeemEntity> getQueryWrapper(AssetAbsRedeemQueryDTO queryDTO) {
        LambdaQueryWrapper<AssetAbsRedeemEntity> queryWrapper =  Wrappers.lambdaQuery();
        if (StringUtils.isNotEmpty(queryDTO.getLoanContractCode())) {
            queryWrapper.like(AssetAbsRedeemEntity::getLoanContractCode,queryDTO.getLoanContractCode());
        }
        if (ObjectUtil.isNotNull(queryDTO.getStartAccountDate())) {
            queryWrapper.ge(AssetAbsRedeemEntity::getAccountDate, queryDTO.getStartAccountDate());
        }
        if (ObjectUtil.isNotNull(queryDTO.getEndAccountDate())) {
            queryWrapper.le(AssetAbsRedeemEntity::getAccountDate, queryDTO.getEndAccountDate());
        }
        if (CollectionUtil.isNotEmpty(queryDTO.getPeriodsList())) {
            queryWrapper.in(AssetAbsRedeemEntity::getPeriods, queryDTO.getPeriodsList());
        }
        if (CollectionUtil.isNotEmpty(queryDTO.getLoanContractCodeList())) {
            queryWrapper.in(AssetAbsRedeemEntity::getLoanContractCode, queryDTO.getLoanContractCodeList());
        }
        if (ObjectUtil.isNotNull(queryDTO.getId())) {
            queryWrapper.eq(AssetAbsRedeemEntity::getId, queryDTO.getId());
        }
        return queryWrapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean importTemplate(MultipartFile file) {
        try {
            ExcelUtil<AssetAbsRedeemExcelVO> util = new ExcelUtil<AssetAbsRedeemExcelVO>(AssetAbsRedeemExcelVO.class);
            List<AssetAbsRedeemExcelVO> absRedeemExcelVOList = util.importExcel(file.getInputStream());
            absRedeemExcelVOList = checkData(absRedeemExcelVOList);
            List<AssetAbsRedeemDetailVO> redeemEntityList = BeanUtil.copyToList(absRedeemExcelVOList, AssetAbsRedeemDetailVO.class);
            //按照借款合同编号+签约主体+出表期数+业务日期
            Map<String,List<AssetAbsRedeemDetailVO>> redeemDetailMap = redeemEntityList.stream().collect(Collectors.groupingBy(v ->
                    v.getLoanContractCode()+"-"+v.getOrgId()+"-"+v.getPeriods()+"-"+ DateUtil.format(v.getBusinessDate(),"yyyy-MM-dd")));
            for (Map.Entry<String, List<AssetAbsRedeemDetailVO>> entry: redeemDetailMap.entrySet()) {
                List<AssetAbsRedeemDetailEntity> detailEntityList = BeanUtil.copyToList(entry.getValue(),AssetAbsRedeemDetailEntity.class);
                AssetAbsRedeemEntity redeemEntity = BeanUtil.copyProperties(entry.getValue().get(0),AssetAbsRedeemEntity.class);
                redeemEntity.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
                this.save(redeemEntity);
                detailEntityList.stream().forEach(v -> {
                    v.setAssetAbsRedeemId(redeemEntity.getId());
                    //借款合同编号+签约主体+出表期数+业务日期+合同编号 存在则报错
                    if (isExistFlag(redeemEntity.getLoanContractCode(),
                            v.getContractCode(),
                            redeemEntity.getOrgId(),
                            redeemEntity.getPeriods(),redeemEntity.getBusinessDate())) {
                       throw new ServiceException(String.format("借款合同编号：%s,签约主体：%s,出表期数：%s,合同编号：%s,业务日期：%s已经存在，不可导入",redeemEntity.getLoanContractCode(),
                               v.getContractCode(),
                               redeemEntity.getOrgId(),
                               redeemEntity.getPeriods(), DateUtil.format(redeemEntity.getBusinessDate(),"yyyy-MM-dd")));
                    }
                });
                iAssetAbsRedeemDetailService.saveBatch(detailEntityList);
            }
        } catch (Exception exception) {
            throw new ServiceException("导入文件失败，失败原因："+exception.getMessage());
        }
        return Boolean.TRUE;
    }

    public Boolean isExistFlag (String loadContractCode,String contractCode,String orgId,String periods,LocalDateTime businessDate) {
        AssetAbsRedeemDetailQueryDTO detailQueryDTO = new AssetAbsRedeemDetailQueryDTO();
        detailQueryDTO.setLoanContractCode(loadContractCode);
        detailQueryDTO.setContractCode(contractCode);
        detailQueryDTO.setOrgId(orgId);
        detailQueryDTO.setPeriods(periods);
        detailQueryDTO.setBusinessDate(businessDate);
        List<AssetAbsRedeemDetailVO> voList = assetAbsRedeemMapper.selectByCondition(detailQueryDTO);
        if (CollectionUtil.isNotEmpty(voList)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public IPage<AssetAbsRedeemDetailVO> detailPage(AssetAbsRedeemDetailQueryDTO queryDTO) {
        IPage<AssetAbsRedeemDetailVO> detailVOIPage = iAssetAbsRedeemDetailService.selectPage(queryDTO);
        AssetAbsRedeemEntity entity = this.getById(queryDTO.getAssetAbsRedeemId());
        detailVOIPage.getRecords().stream().forEach(v -> {
            getBalance(v,entity);
        });
        return detailVOIPage;
    }

    @Override
    public Boolean deleteByIds(List<Long> idList) {
        if (CollectionUtil.isEmpty(idList)) {
            throw new ServiceException("请至少勾选一条数据删除");
        }
        List<AssetAbsRedeemEntity> assetAbsRedeemEntityList = this.listByIds(idList);
        assetAbsRedeemEntityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("处理状态为已录入的才可以删除");
            }
        });
        this.removeBatchByIds(idList);
        batchDeleteVoucher(idList);
        return iAssetAbsRedeemDetailService.removeBatcheByDetailId(idList);
    }

    @Override
    public Boolean withdraw(List<Long> idList) {
        if (CollectionUtil.isEmpty(idList)) {
            throw new ServiceException("请至少勾选一条数据撤回");
        }
        List<AssetAbsRedeemEntity> assetAbsRedeemEntityList = this.listByIds(idList);
        assetAbsRedeemEntityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.SUBMITTED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("只有处理状态为已提交的才可以撤回");
            }
            v.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
        });
        iApproveService.withdraw(assetAbsRedeemEntityList.stream().map(AssetAbsRedeemEntity::getProcessInstanceId).collect(Collectors.toList()));
        //删除凭证
        batchDeleteVoucher(idList);
        return this.updateBatchById(assetAbsRedeemEntityList);
    }

    @Override
    public Boolean submit(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请至少勾选一条数据提交");
        }
        List<AssetAbsRedeemEntity> assetAbsRedeemEntityList = this.listByIds(idList);
        List<ApproveDTO> approveDTOList = Lists.newArrayList();
        assetAbsRedeemEntityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("只有处理状态为已录入的才可以提交");
            }
            v.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
            ApproveDTO approveDTO = new ApproveDTO();
            approveDTO.setDocumentId(v.getId());
            approveDTO.setDocumentType(BatchTypeEnum.ABSSH.getCode());
            approveDTO.setUrl(approveUrl+v.getId());
            approveDTOList.add(approveDTO);
        });
        //发送审核
        Map<Long,Long> processInstantIdMap = iApproveService.submit(approveDTOList);
        assetAbsRedeemEntityList.stream().forEach(v -> {
            if (null!=processInstantIdMap && processInstantIdMap.containsKey(v.getId())) {
                v.setProcessInstanceId(processInstantIdMap.get(v.getId()));
            }
        });
        Boolean isGenerateVoucher = generateVoucher(idList, YesOrNoEnum.YES.getCode());
        if (!isGenerateVoucher) {
            throw new ServiceException("凭证存在未生成，不可以提交");
        }
        return this.updateBatchById(assetAbsRedeemEntityList);
    }

    @Override
    public Boolean generateVoucher(List<Long> idList, String isSubmit) {
        if (CollectionUtil.isEmpty(idList)) {
            throw new ServiceException("请至少选择一条数据生成凭证");
        }
        List<AssetAbsRedeemEntity> assetAbsRedeemEntityList = this.listByIds(idList);
        assetAbsRedeemEntityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("处理状态为已录入的才可以生成凭证");
            }
        });
        //生成凭证前先删除凭证
        iVoucherService.deleteByBatchIdList(idList,BatchTypeEnum.ABSSH.getCode());
        Map<Long,AssetAbsRedeemEntity> absEntityMap = assetAbsRedeemEntityList.stream().collect(Collectors.toMap(e->e.getId(),e->e,(k1,k2)->k2));
        List<AssetAbsRedeemDetailEntity> detailEntityList = iAssetAbsRedeemDetailService.lambdaQuery().in(AssetAbsRedeemDetailEntity::getAssetAbsRedeemId,idList).list();
        List<Map<String,Object>> voucherMapList = Lists.newArrayList();
        detailEntityList.stream().forEach(v -> {
            AssetAbsRedeemEntity entity = absEntityMap.get(v.getAssetAbsRedeemId());
            //组装凭证参数
            voucherMapList.add(getVoucherMap(entity,v,isSubmit));
        });
        log.info("赎回凭证参数：{}", JSON.toJSONString(voucherMapList));
        List<VoucherInfoVO> voucherResultList = iRuleService.batchExecuteRule(voucherMapList);
        log.info("赎回凭证返回值：{}", JSON.toJSONString(voucherResultList));
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
            iAssetAbsRedeemDetailService.lambdaUpdate().set(AssetAbsRedeemDetailEntity::getVoucherIds, voucherIds).set(AssetAbsRedeemDetailEntity::getErrorInfo,errorInfo).set(AssetAbsRedeemDetailEntity::getPeriodCode,periodCode).eq(AssetAbsRedeemDetailEntity::getId,Long.parseLong(infoVO.getOrderId())).update();
        }
        if (YesOrNoEnum.NO.getCode().equals(isSubmit) && !isExistVoucherError) {
            updateIsGenerateVoucher(idList,isSubmit);
        }
        return Boolean.TRUE;
    }

    @Override
    public List<AssetAbsRedeemVO> selectByCondition(AssetAbsRedeemQueryDTO queryDTO) {
        LambdaQueryWrapper<AssetAbsRedeemEntity> queryWrapper = getQueryWrapper(queryDTO);
        List<AssetAbsRedeemEntity> redeemEntityList = this.list(queryWrapper);
        List<AssetAbsRedeemVO> redeemVOList = BeanUtil.copyToList(redeemEntityList, AssetAbsRedeemVO.class);
        //获取签约主体
        Map<String,String> orgIdMap = getOrgIdOrgName();
        redeemVOList.stream().forEach(v -> {
            setRedeemPriceTotal(v);
            if (orgIdMap.containsKey(v.getOrgId())) {
                v.setOrgIdName(orgIdMap.get(v.getOrgId()));
            }
        });
        return redeemVOList;
    }

    @Override
    public Boolean updateProcessStatus(CommonApproveDTO approveDTO) {
        if (StringUtils.isEmpty(approveDTO.getDocumentStatus())) {
            throw new ServiceException("处理状态不可以为空");
        }
        AssetAbsRedeemEntity assetAbsRedeemEntity = this.getById(approveDTO.getDocumentId());
        if (null == assetAbsRedeemEntity) {
            throw new ServiceException("资产赎回数据不存在");
        }
        String processStatus = assetAbsRedeemEntity.getProcessStatus();
        try {
            if (ProcessStatusEnum.REVIEWED.getCode().equals(approveDTO.getDocumentStatus())) {
                processStatus = ProcessStatusEnum.REVIEWED.getCode();
                //更新财务合同状态
                updateFinancialContractStatus(assetAbsRedeemEntity.getId());
            } else if (ProcessStatusEnum.REJECTED.getCode().equals(approveDTO.getDocumentStatus())) {
                processStatus = ProcessStatusEnum.REJECTED.getCode();
            }
            assetAbsRedeemEntity.setProcessStatus(processStatus);
            assetAbsRedeemEntity.setApproveErrorInfo("");
        } catch (Exception e) {
            assetAbsRedeemEntity.setApproveErrorInfo(e.getMessage());
        }
        return this.updateById(assetAbsRedeemEntity);
    }

    @Override
    public List<AssetAbsRedeemDetailExcelVO> selectDetailByRedeemId(Long assetAbsRedeemId) {
        AssetAbsRedeemEntity entity = this.getById(assetAbsRedeemId);
        AssetAbsRedeemDetailQueryDTO queryDTO = new AssetAbsRedeemDetailQueryDTO();
        queryDTO.setAssetAbsRedeemId(assetAbsRedeemId);
        List<AssetAbsRedeemDetailVO> detailEntityList = iAssetAbsRedeemDetailService.selectByCondition(queryDTO);
        //获取签约主体
        Map<String,String> orgIdMap = getOrgIdOrgName();
        detailEntityList.stream().forEach(v -> {
            getBalance(v,entity);
            if (orgIdMap.containsKey(v.getOrgId())) {
                v.setOrgIdName(orgIdMap.get(v.getOrgId()));
            }
        });
        return BeanUtil.copyToList(detailEntityList,AssetAbsRedeemDetailExcelVO.class);
    }

    @Override
    public List<AssetAbsRedeemDetailExcelVO> selectDetailByRedeemIdList(List<Long> assetAbsRedeemIdList) {
        AssetAbsRedeemDetailQueryDTO queryDTO = new AssetAbsRedeemDetailQueryDTO();
        queryDTO.setAssetAbsRedeemIdList(assetAbsRedeemIdList);
        List<AssetAbsRedeemDetailVO> detailEntityList = iAssetAbsRedeemDetailService.selectByCondition(queryDTO);
        //获取签约主体
        Map<String,String> orgIdMap = getOrgIdOrgName();
        detailEntityList.forEach(v -> {
            AssetAbsRedeemEntity entity = this.getById(v.getAssetAbsRedeemId());
            getBalance(v,entity);
            if (orgIdMap.containsKey(v.getOrgId())) {
                v.setOrgIdName(orgIdMap.get(v.getOrgId()));
            }
        });
        return BeanUtil.copyToList(detailEntityList,AssetAbsRedeemDetailExcelVO.class);
    }

    @Override
    public Boolean detailImport(MultipartFile file, Long assetAbsRedeemId) {
        try {
            ExcelUtil<AssetAbsRedeemDetailExcelVO> util = new ExcelUtil<AssetAbsRedeemDetailExcelVO>(AssetAbsRedeemDetailExcelVO.class);
            List<AssetAbsRedeemDetailExcelVO> absRedeemExcelVOList = util.importExcel(file.getInputStream());
            if (CollectionUtils.isEmpty(absRedeemExcelVOList)) {
                throw new ServiceException("没有数据需要上传");
            }
            //按照合同分组
            Map<String,AssetAbsRedeemDetailExcelVO> detailExcelVOMap = absRedeemExcelVOList.stream().collect(Collectors.toMap(AssetAbsRedeemDetailExcelVO::getContractCode, Function.identity(), (k1, k2)->k2));
            AssetAbsRedeemDetailQueryDTO detailVO = new AssetAbsRedeemDetailQueryDTO();
            detailVO.setAssetAbsRedeemId(assetAbsRedeemId);
            List<AssetAbsRedeemDetailEntity> assetAbsRedeemDetailVOList = iAssetAbsRedeemDetailService.lambdaQuery().eq(AssetAbsRedeemDetailEntity::getAssetAbsRedeemId,assetAbsRedeemId).list();
            AssetAbsRedeemDetailExcelVO detailExcelVO = absRedeemExcelVOList.get(0);
            Date startDate = detailExcelVO.getStartDate();
            Date actureDate = detailExcelVO.getActualDate();
            assetAbsRedeemDetailVOList.stream().forEach(v -> {
                //更新财务合同状态，赎回价格
                if (detailExcelVOMap.containsKey(v.getContractCode())) {
                    v.setFinancialContractStatus(detailExcelVOMap.get(v.getContractCode()).getFinancialContractStatus());
                    v.setRedeemPrice(detailExcelVOMap.get(v.getContractCode()).getRedeemPrice());
                }
            });
            iAssetAbsRedeemDetailService.updateBatchById(assetAbsRedeemDetailVOList);
            //更新主表起算日，赎回日
            this.lambdaUpdate().set(AssetAbsRedeemEntity::getStartDate,startDate).set(AssetAbsRedeemEntity::getActualDate,actureDate).eq(AssetAbsRedeemEntity::getId,assetAbsRedeemId).update();
        } catch (Exception e) {
            throw new ServiceException("导入详情页数据失败，失败原因："+e.getMessage());
        }
        return Boolean.TRUE;
    }

    @Override
    public IPage<ContractBalanceVO> selectCheckPage(CheckPageQueryDTO queryDTO) {
        ContractBalanceCheckQueryDTO checkQueryDTO = new ContractBalanceCheckQueryDTO();
        AssetAbsRedeemEntity assetAbsRedeemEntity = this.getById(queryDTO.getId());
        List<AssetAbsRedeemDetailEntity> outTableAbsEntityList = iAssetAbsRedeemDetailService.lambdaQuery().eq(AssetAbsRedeemDetailEntity::getAssetAbsRedeemId,queryDTO.getId()).list();
        checkQueryDTO.setOrgIdList(Lists.newArrayList(assetAbsRedeemEntity.getOrgId()));
        checkQueryDTO.setContractCodeList(outTableAbsEntityList.stream().map(AssetAbsRedeemDetailEntity::getContractCode).filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList()));
        return iContractBalanceService.selectCheckPage(checkQueryDTO);
    }

    public void getBalance(AssetAbsRedeemDetailVO v,AssetAbsRedeemEntity entity){
        v.setLoanContractCode(entity.getLoanContractCode());
        v.setPeriods(entity.getPeriods());
        v.setStartDate(entity.getStartDate());
        v.setActualDate(entity.getActualDate());
        v.setOrgId(entity.getOrgId());
        //赎回起算日租金余额=按合同编号+签约主体查余额表：凭证日期<=上传的赎回起算日，且创建日期最新时，receivable_rent_balance金额
        BigDecimal receivableRentBalance = BigDecimal.ZERO;
        //赎回起算日本金余额 =  1.按合同编号+签约主体查eg_repayment_plan_his偿还计划历史表：
        //change_date<=上传的赎回起算日，version为最大时，planned_principal汇总额；
        //2.按合同编号+签约主体查eg_repayment_plan偿还计划表：
        //actual_repayment_date<=上传的赎回起算日，actual_repayment_principal_amount的汇总额；
        //3.计算最终值：1-2
        BigDecimal principalBalance = BigDecimal.ZERO;
        //赎回起算日利息余额 = 1.按合同编号+签约主体查eg_repayment_plan_his偿还计划历史表：
        //change_date<=上传的赎回起算日，version为最大时，planned_interest汇总额；
        //2.按合同编号+签约主体查eg_repayment_plan偿还计划表：
        //actual_repayment_date<=上传的赎回起算日，actual_repayment_interes_amount的汇总额；
        //3.计算最终值：1-2
        RepaymentPlanHisQueryDTO queryDTO = new RepaymentPlanHisQueryDTO();
        queryDTO.setContractCode(v.getContractCode());
        queryDTO.setOrgId(v.getOrgId());
        queryDTO.setPlanDate(CommonDateUtils.parseLocalDateTimeToDate(v.getStartDate()));
        List<RepaymentPlanHisVO> planHisVOS = iRepaymentPlanHisService.selectPlanAmountByCondition(queryDTO);
        RepaymentPlanQueryDTO planQueryDTO = BeanUtil.copyProperties(queryDTO,RepaymentPlanQueryDTO.class);
        List<RepaymentPlanVO> planVOS = iRepaymentPlanService.selectPlanAmountBuCondition(planQueryDTO);
        BigDecimal plannedPrincipal = BigDecimal.ZERO;
        BigDecimal plannedInterest = BigDecimal.ZERO;
        BigDecimal actualRepaymentPrincipalAmount = BigDecimal.ZERO;
        BigDecimal actualRepaymentInteresAmount = BigDecimal.ZERO;
        if (CollectionUtils.isNotEmpty(planHisVOS)) {
            plannedPrincipal = planHisVOS.get(0).getPlannedPrincipal();
            plannedInterest = planHisVOS.get(0).getPlannedInterest();
        }
        if (CollectionUtils.isNotEmpty(planVOS)) {
            actualRepaymentPrincipalAmount = planVOS.get(0).getActualRepaymentPrincipalAmount();
            actualRepaymentInteresAmount = planVOS.get(0).getActualRepaymentInteresAmount();
        }
        principalBalance = plannedPrincipal.subtract(actualRepaymentPrincipalAmount);
        BigDecimal interestBalance = plannedInterest.subtract(actualRepaymentInteresAmount);
        //赎回起算日留购价余额=按合同编号+签约主体查余额表：凭证日期<=上传的赎回起算日，且创建日期最新时，receivable_residual_value_balance金额
        BigDecimal receivableResidualValueBalance = BigDecimal.ZERO;
        //赎回起算日保证金余额 = 按合同编号+签约主体查余额表：凭证日期<=上传的赎回起算日，且创建日期最新时，lessee_margin_balance金额
        BigDecimal lesseeMarginBalance = BigDecimal.ZERO;
        List<ContractBalanceEntity> contractBalanceEntityList = iContractBalanceService.lambdaQuery().eq(ContractBalanceEntity::getContractCode,v.getContractCode())
                .eq(ContractBalanceEntity::getOrgId,v.getOrgId()).le(ContractBalanceEntity::getVoucherDate,entity.getStartDate()).orderByDesc(ContractBalanceEntity::getId).list();
        if (CollectionUtils.isEmpty(contractBalanceEntityList)) {
            return;
        }
        ContractBalanceEntity latestEntity = contractBalanceEntityList.get(0);
        receivableRentBalance = null == latestEntity.getReceivableRentBalance() ? BigDecimal.ZERO : latestEntity.getReceivableRentBalance();
        receivableResidualValueBalance = null == latestEntity.getLesseeMarginBalance() ? BigDecimal.ZERO : latestEntity.getLesseeMarginBalance();
        v.setReceivableRentBalance(receivableRentBalance);
        v.setReceivableResidualValueBalance(receivableResidualValueBalance);
        v.setReceivableOuttax(null == latestEntity.getReceivableOuttaxBalance() ? BigDecimal.ZERO : latestEntity.getReceivableOuttaxBalance());
        v.setUnrealizedRevenue(null == latestEntity.getUnrealizedRevenueBalance() ? BigDecimal.ZERO : latestEntity.getUnrealizedRevenueBalance());
        v.setPrincipalBalance(principalBalance);
        v.setInterestBalance(interestBalance);
    }

    public List<AssetAbsRedeemExcelVO> checkData(List<AssetAbsRedeemExcelVO> absRedeemExcelVOList) {
        List<AssetAbsRedeemExcelVO> newAbsRedeemExcelVOList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(absRedeemExcelVOList)) {
            throw new ServiceException("导入数据为空");
        }
        Date startDate = absRedeemExcelVOList.get(0).getStartDate();
        Date actualDate = absRedeemExcelVOList.get(0).getActualDate();
        //判断导入的合同是否重复
        long num = absRedeemExcelVOList.stream().map(AssetAbsRedeemExcelVO::getContractCode).distinct().count();
        if (num < absRedeemExcelVOList.size()) {
            throw new ServiceException("导入的合同编码不可以重复");
        }
        List<String> contractList = absRedeemExcelVOList.stream().map(AssetAbsRedeemExcelVO::getContractCode).distinct().collect(Collectors.toList());
        Map<String, List<OutTableContractDetailVO>> contractCodeMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(contractList)) {
            OutTableContractDetailQueryDTO outTableContractDetailQueryDTO = new OutTableContractDetailQueryDTO();
            outTableContractDetailQueryDTO.setContractCodeList(contractList);
            outTableContractDetailQueryDTO.setProcessStatusList(Lists.newArrayList(ProcessStatusEnum.REVIEWED.getCode(),ProcessStatusEnum.TO_KINGDEE.getCode()));
            contractCodeMap = iOutTableAbsService.selectByCondition(outTableContractDetailQueryDTO).stream().collect(Collectors.groupingBy(OutTableContractDetailVO::getContractCode));
        }
        Map<String, List<OutTableContractDetailVO>> finalContractCodeMap = contractCodeMap;
        absRedeemExcelVOList.stream().forEach(v ->{
            if (StringUtils.isEmpty(v.getContractCode())) {
                throw new ServiceException("合同编码不可以为空");
            }
            if (StringUtils.isEmpty(v.getFinancialContractStatus())) {
                throw new ServiceException("财务合同状态不可以为空");
            }
            if (ObjectUtil.isNull(v.getStartDate())) {
                throw new ServiceException("赎回起算日不可以为空");
            }
            if (startDate.compareTo(v.getStartDate())!=0) {
                throw new ServiceException("所有合同的赎回起算日存在不一致，不可导入");
            }
            if ((actualDate==null && null!=v.getActualDate()) || actualDate.compareTo(v.getActualDate())!=0) {
                throw new ServiceException("所有合同的实际赎回日存在不一致，不可导入");
            }
            if (!finalContractCodeMap.containsKey(v.getContractCode())) {
                throw new ServiceException("合同编码在出表ABS中不存在，不可以导入");
            }
            List<OutTableContractDetailVO> detailVOList = finalContractCodeMap.get(v.getContractCode());
            detailVOList.stream().forEach(d -> {
                AssetAbsRedeemExcelVO excelVO = BeanUtil.copyProperties(v,AssetAbsRedeemExcelVO.class);
                excelVO.setLoanContractCode(d.getLoanContractCode());
                excelVO.setOrgId(d.getOrgId());
                excelVO.setPeriods(d.getPeriods());
                //业务日期默认创建时间
                excelVO.setBusinessDate(LocalDateTime.now());
                excelVO.setClientCode(d.getClientCode());
                excelVO.setClientName(d.getClientName());
                newAbsRedeemExcelVOList.add(excelVO);
            });
        });
        return newAbsRedeemExcelVOList;
    }

    public void batchDeleteVoucher(List<Long> ids){
        //获取所有的凭证Id
        List<AssetAbsRedeemDetailEntity> detailsEntityList = iAssetAbsRedeemDetailService.lambdaQuery().in(AssetAbsRedeemDetailEntity::getAssetAbsRedeemId, ids).list();
        //逗号拆分
        List<Long> voucherIdList = Lists.newArrayList();
        detailsEntityList.stream().forEach(v -> {
            if (StringUtils.isNotEmpty(v.getVoucherIds())) {
                voucherIdList.addAll(Arrays.stream(v.getVoucherIds().split(",")).map(Long::parseLong).collect(Collectors.toList()));
            }
            v.setVoucherIds("");
        });
        if (CollectionUtil.isNotEmpty(voucherIdList)) {
            iVoucherService.deleteByIdList(voucherIdList);
        }
        iAssetAbsRedeemDetailService.updateBatchById(detailsEntityList);
    }

    public Map<String,Object> getVoucherMap(AssetAbsRedeemEntity entity, AssetAbsRedeemDetailEntity v, String isSubmit) {
        AssetAbsRedeemVoucherDTO voucherDTO = new AssetAbsRedeemVoucherDTO();
        AssetAbsRedeemDetailVO vo = BeanUtil.copyProperties(v,AssetAbsRedeemDetailVO.class);
        getBalance(vo,entity);
        voucherDTO.setSystemCode(SystemEnum.CWZT.getCode());
        voucherDTO.setSystemName(SystemEnum.CWZT.getDesc());
        voucherDTO.setBusinessCode(BusinessEnum.ZLYW.getCode());
        voucherDTO.setBusinessName(BusinessEnum.ZLYW.getDesc());
        voucherDTO.setBusinessDate(new Date());
        voucherDTO.setOrderId(v.getId().toString());
        voucherDTO.setContractCode(v.getContractCode());
        voucherDTO.setClientCode(v.getClientCode());
        voucherDTO.setReceivableLeaseAmount(vo.getReceivableRentBalance());
        voucherDTO.setRetainedPrice(vo.getReceivableResidualValueBalance());
        voucherDTO.setReceivableMarginAmount(vo.getLesseeMarginBalance());
        voucherDTO.setRedeemPrice(vo.getRedeemPrice());
        voucherDTO.setUnrealizedRevenue(vo.getUnrealizedRevenue());
        voucherDTO.setReceivableOuttax(vo.getReceivableOuttax());
        voucherDTO.setIsSubmit(isSubmit);
        voucherDTO.setBatchId(entity.getId());
        voucherDTO.setBatchType(BatchTypeEnum.ABSSH.getCode());
        voucherDTO.setSceneCode(SceneEnum.ABSSH.getCode());
        voucherDTO.setSceneName(SceneEnum.ABSSH.getDesc());
        voucherDTO.setOrgId(vo.getOrgId());
        List<ContractBalanceEntity> contractBalanceEntityList = iContractBalanceService.lambdaQuery()
                .eq(ContractBalanceEntity::getSceneCode,SceneEnum.ZLSK.getCode())
                .eq(ContractBalanceEntity::getContractCode,v.getContractCode())
                .eq(ContractBalanceEntity::getOrgId,entity.getOrgId()).ge(ContractBalanceEntity::getVoucherDate,entity.getStartDate())
                .orderByDesc(ContractBalanceEntity::getId).list();
        final BigDecimal[] receiveUnconfirmed = {BigDecimal.ZERO};
        final BigDecimal[] receiveLeaseAmount = {BigDecimal.ZERO};
        final BigDecimal[] receiveRetainedPrice = {BigDecimal.ZERO};
        final BigDecimal[] receiveMarginAmount = {BigDecimal.ZERO};
        final BigDecimal[] receiveSupplierMarginAmount = {BigDecimal.ZERO};
        final BigDecimal[] receiveDownpaymentAmount = {BigDecimal.ZERO};
        final BigDecimal[] dinterestRevenueAmount = {BigDecimal.ZERO};
        final BigDecimal[] terminateAmount = {BigDecimal.ZERO};
        final BigDecimal[] damagesRevenueAmount = {BigDecimal.ZERO};
        final BigDecimal[] otherRevenueAmount = {BigDecimal.ZERO};
        final BigDecimal[] receivableOuttaxAmountJT = {BigDecimal.ZERO};
        final BigDecimal[] deductionMarginAmount = {BigDecimal.ZERO};
        final BigDecimal[] deductionLeaseAmount = {BigDecimal.ZERO};
        final BigDecimal[] deductionRetainedPrice = {BigDecimal.ZERO};
        final BigDecimal[] deductionDefaultInterestAmount = {BigDecimal.ZERO};
        final BigDecimal[] deductionTerminateProcedureAmount = {BigDecimal.ZERO};
        final BigDecimal[] deductionDefaultNoTaxAmount = {BigDecimal.ZERO};
        final BigDecimal[] deductionTerminateNoTaxAmount = {BigDecimal.ZERO};
        final BigDecimal[] deductionreceivableOuttaxAmountJT = {BigDecimal.ZERO};
        final BigDecimal[] receiveCommissionAmount = {BigDecimal.ZERO};
        final BigDecimal[] receivesServiceAmount = {BigDecimal.ZERO};
        final BigDecimal[] receiveInsuranceAmount = {BigDecimal.ZERO};
        final BigDecimal[] receiveInsuranceDifferAmount = {BigDecimal.ZERO};
        final BigDecimal[] receiveOtherincomeAmount = {BigDecimal.ZERO};
        final BigDecimal[] receiveDamagesRevenueAmount = {BigDecimal.ZERO};
        final BigDecimal[] receiveOtherRevenueAmount = {BigDecimal.ZERO};
        final BigDecimal[] receiveDefaultInterestAmount = {BigDecimal.ZERO};
        final BigDecimal[] receiveTerminateProcedureAmount = {BigDecimal.ZERO};
        contractBalanceEntityList.stream().forEach(e -> {
            receiveUnconfirmed[0] = receiveUnconfirmed[0].add(null == e.getReceivableUnconfirmReceiptAmount() ? BigDecimal.ZERO : e.getReceivableUnconfirmReceiptAmount());
            if (null != e.getReceivableOuttaxAmount() && e.getReceivableOuttaxAmount().compareTo(BigDecimal.ZERO)>0) {
                receivableOuttaxAmountJT[0] = receivableOuttaxAmountJT[0].add(null == e.getReceivableOuttaxAmount() ? BigDecimal.ZERO : e.getReceivableOuttaxAmount());
            }
            if (e.getLesseeMarginAmount()!=null && e.getLesseeMarginAmount().compareTo(BigDecimal.ZERO)<=0) {
                receiveLeaseAmount[0] = receiveLeaseAmount[0].add(null == e.getReceivableRentAmount() ? BigDecimal.ZERO : e.getReceivableRentAmount());
                receiveRetainedPrice[0] = receiveRetainedPrice[0].add(null == e.getReceivableResidualValueAmount() ? BigDecimal.ZERO : e.getReceivableResidualValueAmount());
                receiveMarginAmount[0] = receiveMarginAmount[0].add(null== e.getLesseeMarginAmount() ? BigDecimal.ZERO : e.getLesseeMarginAmount());
                receiveSupplierMarginAmount[0] = receiveSupplierMarginAmount[0].add(null== e.getSupplierMarginAmount() ? BigDecimal.ZERO : e.getSupplierMarginAmount());
                receiveDownpaymentAmount[0] = receiveDownpaymentAmount[0].add(null== e.getReceivableDownpaymentAmount() ? BigDecimal.ZERO : e.getReceivableDownpaymentAmount());
                otherRevenueAmount[0] = otherRevenueAmount[0].add(null== e.getOtherRevenueAmount() ? BigDecimal.ZERO : e.getOtherRevenueAmount());
                damagesRevenueAmount[0] = damagesRevenueAmount[0].add(null== e.getDamagesRevenueAmount() ? BigDecimal.ZERO : e.getDamagesRevenueAmount());
                terminateAmount[0] = terminateAmount[0].add(null== e.getTerminateAmount() ? BigDecimal.ZERO : e.getTerminateAmount());
                dinterestRevenueAmount[0] = dinterestRevenueAmount[0].add(null== e.getDinterestRevenueAmount() ? BigDecimal.ZERO : e.getDinterestRevenueAmount());
                receiveTerminateProcedureAmount[0] = receiveTerminateProcedureAmount[0].add(null== e.getReceivableTerminateAmount() ? BigDecimal.ZERO : e.getReceivableTerminateAmount());
                receiveDefaultInterestAmount[0] = receiveDefaultInterestAmount[0].add(null== e.getReceivableDefaultInterestAmount() ? BigDecimal.ZERO : e.getReceivableDefaultInterestAmount());
                receiveOtherRevenueAmount[0] = receiveOtherRevenueAmount[0].add(null== e.getReceivableOtherRevenueAmount() ? BigDecimal.ZERO : e.getReceivableOtherRevenueAmount());
                receiveDamagesRevenueAmount[0] = receiveDamagesRevenueAmount[0].add(null== e.getReceivableDamagesRevenueAmount() ? BigDecimal.ZERO : e.getReceivableDamagesRevenueAmount());
                receiveOtherincomeAmount[0] = receiveOtherincomeAmount[0].add(null== e.getReceivableOtherincomeAmount() ? BigDecimal.ZERO : e.getReceivableOtherincomeAmount());
                receiveInsuranceDifferAmount[0] = receiveInsuranceDifferAmount[0].add(null== e.getInsuranceDifferAmount() ? BigDecimal.ZERO : e.getInsuranceDifferAmount());
                receiveInsuranceAmount[0] = receiveInsuranceAmount[0].add(null== e.getReceivableInsuranceAmount() ? BigDecimal.ZERO : e.getReceivableInsuranceAmount());
                receivesServiceAmount[0] = receivesServiceAmount[0].add(null== e.getReceivableServiceAmount() ? BigDecimal.ZERO : e.getReceivableServiceAmount());
                receiveCommissionAmount[0] = receiveCommissionAmount[0].add(null== e.getReceivableCommissionAmount() ? BigDecimal.ZERO : e.getReceivableCommissionAmount());
            } else if(e.getLesseeMarginAmount()!=null && e.getLesseeMarginAmount().compareTo(BigDecimal.ZERO)>0) {
                deductionreceivableOuttaxAmountJT[0] = deductionreceivableOuttaxAmountJT[0].add(null== e.getReceivableOuttaxAmount() ? BigDecimal.ZERO : e.getReceivableOuttaxAmount());
                deductionTerminateNoTaxAmount[0] = deductionTerminateNoTaxAmount[0].add(null== e.getTerminateAmount() ? BigDecimal.ZERO : e.getTerminateAmount());
                deductionDefaultNoTaxAmount[0] = deductionDefaultNoTaxAmount[0].add(null== e.getDinterestRevenueAmount() ? BigDecimal.ZERO : e.getDinterestRevenueAmount());
                deductionTerminateProcedureAmount[0] = deductionTerminateProcedureAmount[0].add(null== e.getReceivableTerminateAmount() ? BigDecimal.ZERO : e.getReceivableTerminateAmount());
                deductionDefaultInterestAmount[0] = deductionDefaultInterestAmount[0].add(null== e.getReceivableDefaultInterestAmount() ? BigDecimal.ZERO : e.getReceivableDefaultInterestAmount());
                deductionRetainedPrice[0] = deductionRetainedPrice[0].add(null== e.getReceivableResidualValueAmount() ? BigDecimal.ZERO : e.getReceivableResidualValueAmount());
                deductionLeaseAmount[0] = deductionLeaseAmount[0].add(null== e.getReceivableRentAmount() ? BigDecimal.ZERO : e.getReceivableRentAmount());
                deductionMarginAmount[0] = deductionMarginAmount[0].add(null== e.getLesseeMarginAmount() ? BigDecimal.ZERO : e.getLesseeMarginAmount());
                receivableOuttaxAmountJT[0] = receivableOuttaxAmountJT[0].add(null== e.getLesseeMarginAmount() ? BigDecimal.ZERO : e.getLesseeMarginAmount());
            }

        });
        voucherDTO.setReceiveUnconfirmed(receiveUnconfirmed[0]);
        voucherDTO.setReceiveLeaseAmount(receiveLeaseAmount[0]);
        voucherDTO.setReceiveRetainedPrice(receiveRetainedPrice[0]);
        voucherDTO.setReceiveMarginAmount(receiveMarginAmount[0]);
        voucherDTO.setReceiveSupplierMarginAmount(receiveSupplierMarginAmount[0]);
        voucherDTO.setReceiveDownpaymentAmount(receiveDownpaymentAmount[0]);
        voucherDTO.setDeductionreceivableOuttaxAmountJT(deductionreceivableOuttaxAmountJT[0]);
        voucherDTO.setDeductionTerminateNoTaxAmount(deductionTerminateNoTaxAmount[0]);
        voucherDTO.setDeductionDefaultNoTaxAmount(deductionDefaultNoTaxAmount[0]);
        voucherDTO.setDeductionTerminateProcedureAmount(deductionTerminateProcedureAmount[0]);
        voucherDTO.setDeductionDefaultInterestAmount(deductionDefaultInterestAmount[0]);
        voucherDTO.setDeductionRetainedPrice(deductionRetainedPrice[0]);
        voucherDTO.setDeductionLeaseAmount(deductionLeaseAmount[0]);
        voucherDTO.setReceiveTerminateProcedureAmount(receiveTerminateProcedureAmount[0]);
        voucherDTO.setDeductionMarginAmount(deductionMarginAmount[0]);
        voucherDTO.setReceivableOuttaxAmountJT(receivableOuttaxAmountJT[0]);
        voucherDTO.setOtherRevenueAmount(otherRevenueAmount[0]);
        voucherDTO.setDamagesRevenueAmount(damagesRevenueAmount[0]);
        voucherDTO.setTerminateAmount(terminateAmount[0]);
        voucherDTO.setDinterestRevenueAmount(dinterestRevenueAmount[0]);
        voucherDTO.setReceiveDefaultInterestAmount(receiveDefaultInterestAmount[0]);
        voucherDTO.setReceiveOtherRevenueAmount(receiveOtherRevenueAmount[0]);
        voucherDTO.setReceiveDamagesRevenueAmount(receiveDamagesRevenueAmount[0]);
        voucherDTO.setReceiveOtherincomeAmount(receiveOtherincomeAmount[0]);
        voucherDTO.setReceiveInsuranceDifferAmount(receiveInsuranceDifferAmount[0]);
        voucherDTO.setReceiveInsuranceAmount(receiveInsuranceAmount[0]);
        voucherDTO.setReceivesServiceAmount(receivesServiceAmount[0]);
        voucherDTO.setReceiveCommissionAmount(receiveCommissionAmount[0]);
        Map<String, Object> dataMap = BeanUtil.beanToMap(voucherDTO);
        return dataMap;
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

    public void updateIsGenerateVoucher(List<Long> idList,String isSubmit){
        if (YesOrNoEnum.YES.getCode().equals(isSubmit)) {
            return;
        }
        //获取详情信息
        Map<Long,List<AssetAbsRedeemDetailEntity>> detailEntityMap = iAssetAbsRedeemDetailService.lambdaQuery()
                .in(AssetAbsRedeemDetailEntity::getAssetAbsRedeemId,idList).list().stream().collect(Collectors.groupingBy(AssetAbsRedeemDetailEntity::getAssetAbsRedeemId));
        for (Map.Entry<Long, List<AssetAbsRedeemDetailEntity>> entry : detailEntityMap.entrySet()) {
            Boolean isExistEmpty = entry.getValue().stream().anyMatch(v -> StringUtils.isEmpty(v.getVoucherIds()));
            String isGenerateVoucher = YesOrNoEnum.NO.getCode();
            Integer periodCode=null;
            if (!isExistEmpty) {
                isGenerateVoucher = YesOrNoEnum.YES.getCode();
                periodCode = entry.getValue().get(0).getPeriodCode();
            }
            this.lambdaUpdate().set(AssetAbsRedeemEntity::getIsGenerateVoucher, isGenerateVoucher).set(AssetAbsRedeemEntity::getPeriodCode,periodCode).eq(AssetAbsRedeemEntity::getId,entry.getKey()).update();
        }
    }

    private Map<String,String> getOrgIdOrgName(){
        Map<String,String> orgIdAndNameMap = Maps.newHashMap();
        List<OrgCompanyVO> orgCompanyVOList =  iOrgCompanyService.selectByCondition(new OrgCompanyQueryDTO());
        if (CollectionUtils.isNotEmpty(orgCompanyVOList)) {
            orgIdAndNameMap = orgCompanyVOList.stream().collect(Collectors.toMap(OrgCompanyVO::getOrgId,OrgCompanyVO::getOrgName, (k1,k2)->k2));
        }
        return orgIdAndNameMap;
    }

    public void updateFinancialContractStatus(Long id){
        AssetAbsRedeemEntity entity = this.getById(id);
        List<AssetAbsRedeemDetailEntity> detailEntityList = iAssetAbsRedeemDetailService.lambdaQuery().eq(AssetAbsRedeemDetailEntity::getAssetAbsRedeemId,id).list();
        if (CollectionUtil.isEmpty(detailEntityList)) {
            return;
        }
        //按照合同编码+签约主体分组
        Map<String,List<AssetAbsRedeemDetailEntity>> detailMap = detailEntityList.stream().collect(Collectors.groupingBy(v -> v.getContractCode()));
        List<String> contractCodeList = detailEntityList.stream().map(AssetAbsRedeemDetailEntity::getContractCode).collect(Collectors.toList());
        //根据合同编码+签约主体查询所有的合同信息
        List<ContractEntity> contractEntityList = iContractService.lambdaQuery().in(ContractEntity::getContractCode,contractCodeList).eq(ContractEntity::getOrgId,entity.getOrgId()).list();
        contractEntityList.stream().forEach(v -> {
            String key = v.getContractCode();
            if (detailMap.containsKey(key)){
                v.setFinancialContractStatus(detailMap.get(key).get(0).getFinancialContractStatus());
                v.setFinancialContractStatusUpdateTime(new Date());
            }
        });
        if (CollectionUtil.isNotEmpty(contractEntityList)) {
            iContractService.updateBatchById(contractEntityList);
        }
    }
}

