package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.approve.model.dto.ApproveDTO;
import com.utfinancing.financehub.engine.approve.service.IApproveService;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.entity.AssetAbsRedeemEntity;
import com.utfinancing.financehub.engine.finance.entity.AssetAbsTransferPaymentDetailEntity;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.*;
import com.utfinancing.financehub.engine.finance.entity.AssetAbsTransferPaymentEntity;
import com.utfinancing.financehub.engine.finance.mapper.AssetAbsTransferPaymentMapper;
import com.utfinancing.financehub.engine.finance.service.IAssetAbsTransferPaymentDetailService;
import com.utfinancing.financehub.engine.finance.service.IAssetAbsTransferPaymentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.finance.service.IOutTableAbsService;
import com.utfinancing.financehub.engine.finance.service.IVoucherService;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import com.utfinancing.financehub.engine.rule.model.vo.VoucherInfoVO;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import com.utfinancing.financehub.engine.utils.CommonDateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author : bruyang
 * @Date : Create in 2024-03-20
 * @Description :  AssetAbsTransferPayment服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class AssetAbsTransferPaymentServiceImpl extends ServiceImpl<AssetAbsTransferPaymentMapper, AssetAbsTransferPaymentEntity> implements IAssetAbsTransferPaymentService {

    private final AssetAbsTransferPaymentMapper assetAbsTransferPaymentMapper;
    @Resource
    private final IAssetAbsTransferPaymentDetailService iAssetAbsTransferPaymentDetailService;
    @Resource
    private final IVoucherService iVoucherService;
    @Resource
    private final IApproveService iApproveService;
    @Resource
    private final IOutTableAbsService iOutTableAbsService;
    @Value("${approve.url.assetAbsTransferPayment-url:null}")
    private String approveUrl;
    @Resource
    private IRuleService iRuleService;

    @Override
    public Long saveAssetAbsTransferPayment(AssetAbsTransferPaymentDTO dto) {
        AssetAbsTransferPaymentEntity entity = BeanUtil.copyProperties(dto, AssetAbsTransferPaymentEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateAssetAbsTransferPayment(Long id, AssetAbsTransferPaymentDTO dto) {
        AssetAbsTransferPaymentEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public AssetAbsTransferPaymentDTO getAssetAbsTransferPaymentDTOById(Long id) {
        AssetAbsTransferPaymentEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, AssetAbsTransferPaymentDTO.class);
    }

    @Override
    public IPage<AssetAbsTransferPaymentVO> selectPage(AssetAbsTransferPaymentQueryDTO queryDTO) {
        IPage<AssetAbsTransferPaymentVO> entityIPage = assetAbsTransferPaymentMapper.selectPageByCondition(new Page<AssetAbsTransferPaymentEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryDTO);
        entityIPage.getRecords().stream().forEach(v -> {
            v.setBatchType(BatchTypeEnum.ABSZF.getCode());
        });
        return entityIPage;
    }

    @Override
    public Boolean deleteByIds(List<Long> idList) {
        if (CollectionUtil.isEmpty(idList)) {
            throw new ServiceException("请至少勾选一条数据删除");
        }
        List<AssetAbsTransferPaymentEntity> assetAbsTransferPaymentEntityList = this.listByIds(idList);
        assetAbsTransferPaymentEntityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("处理状态为已录入的才可以删除");
            }
        });
        this.removeBatchByIds(idList);
        batchDeleteVoucher(idList);
        return iAssetAbsTransferPaymentDetailService.removeBatcheByDetailId(idList);
    }

    @Override
    public Boolean withdraw(List<Long> idList) {
        if (CollectionUtil.isEmpty(idList)) {
            throw new ServiceException("请至少勾选一条数据撤回");
        }
        List<AssetAbsTransferPaymentEntity> assetAbsTransferPaymentEntityList = this.listByIds(idList);
        assetAbsTransferPaymentEntityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.SUBMITTED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("只有处理状态为已提交的才可以撤回");
            }
            v.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
        });
        iApproveService.withdraw(assetAbsTransferPaymentEntityList.stream().map(AssetAbsTransferPaymentEntity::getProcessInstanceId).collect(Collectors.toList()));
        //删除凭证
        batchDeleteVoucher(idList);
        return this.updateBatchById(assetAbsTransferPaymentEntityList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean importTemplate(MultipartFile file) {
        try {
            ExcelUtil<AssetAbsTransferPaymentExcelVO> util = new ExcelUtil<AssetAbsTransferPaymentExcelVO>(AssetAbsTransferPaymentExcelVO.class);
            List<AssetAbsTransferPaymentExcelVO> assetAbsTransferPaymentExcelVOList = util.importExcel(file.getInputStream());
            checkData(assetAbsTransferPaymentExcelVOList);
            //根据合同编号+期数获取已经复核，传送金蝶状态的出表ABS数据
            OutTableContractDetailQueryDTO queryDTO = new OutTableContractDetailQueryDTO();
            List<String> contractCodeList = assetAbsTransferPaymentExcelVOList.stream().map(AssetAbsTransferPaymentExcelVO::getContractCode).collect(Collectors.toList());
            String periods = assetAbsTransferPaymentExcelVOList.get(0).getPeriods();
            Date accountDate = assetAbsTransferPaymentExcelVOList.get(0).getAccountDate();
            Map<String,AssetAbsTransferPaymentExcelVO> contractMap = assetAbsTransferPaymentExcelVOList.stream().collect(Collectors.toMap(AssetAbsTransferPaymentExcelVO::getContractCode, Function.identity(),(k1,k2)->k2));
            queryDTO.setContractCodeList(contractCodeList);
            queryDTO.setPeriods(periods);
            queryDTO.setProcessStatusList(Lists.newArrayList(ProcessStatusEnum.REVIEWED.getCode(),ProcessStatusEnum.TO_KINGDEE.getCode()));
            List<OutTableContractDetailVO> detailVOList = iOutTableAbsService.selectByCondition(queryDTO);
            if (CollectionUtil.isEmpty(detailVOList)) {
                throw new ServiceException("导入的合同在出表ABS中不存在已复核或已发送金蝶的数据");
            }
            //按照借款合同+出表期数分组
            Map<String,List<OutTableContractDetailVO>> detailVoList = detailVOList.stream().collect(Collectors.groupingBy(v->v.getLoanContractCode()+"-"+v.getPeriods()));
            for (Map.Entry<String, List<OutTableContractDetailVO>> entry : detailVoList.entrySet()){
                OutTableContractDetailVO detailVO = entry.getValue().get(0);
                AssetAbsTransferPaymentEntity entity = new AssetAbsTransferPaymentEntity();
                entity.setAccountDate(CommonDateUtils.parseDateToLocalDateTime(accountDate));
                entity.setBusinessDate(LocalDateTime.now());
                entity.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
                entity.setPeriods(periods);
                entity.setLoanContractCode(detailVO.getLoanContractCode());
                if(isPaymentExistFlag(entity.getLoanContractCode(),entity.getPeriods())){
                    throw new ServiceException(String.format("借款合同编号：%s,出表期数：%s,在系统中已存在不可重复导入",entity.getLoanContractCode(),entity.getPeriods()));
                };
                this.save(entity);
                List<OutTableContractDetailVO> detailVOS = entry.getValue().stream().collect(
                        Collectors.collectingAndThen(
                                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(OutTableContractDetailVO::getContractCode))), ArrayList::new));
                List<AssetAbsTransferPaymentDetailEntity> detailEntityList = Lists.newArrayList();
                detailVOS.stream().forEach(c -> {
                    AssetAbsTransferPaymentExcelVO excelVO = contractMap.get(c.getContractCode());
                    AssetAbsTransferPaymentDetailEntity detailEntity = BeanUtil.copyProperties(excelVO,AssetAbsTransferPaymentDetailEntity.class);
                    detailEntity.setAssetAbsTransferPaymentId(entity.getId());
                    detailEntity.setClientCode(c.getClientCode());
                    detailEntity.setClientName(c.getClientName());
                    detailEntity.setOrgId(c.getOrgId());
                    //借款合同编码+期数+合同编码已经存在则不可以新增
                    if (isExistFlag(entity.getLoanContractCode(),entity.getPeriods(),detailEntity.getContractCode())) {
                        throw new ServiceException(String.format("借款合同号：%s,出表期数：%s,合同编码：%s,在系统中已经存在，不可重复导入",entity.getLoanContractCode(),entity.getPeriods(),detailEntity.getContractCode()));
                    }
                    detailEntityList.add(detailEntity);
                });
                iAssetAbsTransferPaymentDetailService.saveBatch(detailEntityList);
            }
        } catch (Exception e) {
            throw new ServiceException("导入数据失败，失败原因:"+e.getMessage());
        }
        return Boolean.TRUE;
    }

    @Override
    public IPage<AssetAbsTransferPaymentDetailVO> detailPage(AssetAbsTransferPaymentDetailQueryDTO queryDTO) {
        IPage<AssetAbsTransferPaymentDetailVO> detailVOIPage = iAssetAbsTransferPaymentDetailService.selectPage(queryDTO);
        AssetAbsTransferPaymentEntity entity = this.getById(queryDTO.getAssetAbsTransferPaymentId());
        detailVOIPage.getRecords().stream().forEach(v -> {
            v.setLoanContractCode(entity.getLoanContractCode());
            v.setPeriods(entity.getPeriods());
        });
        return detailVOIPage;
    }

    @Override
    public Boolean submit(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请至少勾选一条数据提交");
        }
        List<AssetAbsTransferPaymentEntity> assetAbsTransferPaymentEntityList = this.listByIds(idList);
        List<ApproveDTO> approveDTOList = Lists.newArrayList();
        assetAbsTransferPaymentEntityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("只有处理状态为已录入的才可以提交");
            }
            v.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
            ApproveDTO approveDTO = new ApproveDTO();
            approveDTO.setDocumentId(v.getId());
            approveDTO.setDocumentType(BatchTypeEnum.ABSZF.getCode());
            approveDTO.setUrl(approveUrl+v.getId());
            approveDTOList.add(approveDTO);
        });
        //发送审核
        Map<Long,Long> processInstantIdMap = iApproveService.submit(approveDTOList);
        assetAbsTransferPaymentEntityList.stream().forEach(v -> {
            if (null!=processInstantIdMap && processInstantIdMap.containsKey(v.getId())) {
                v.setProcessInstanceId(processInstantIdMap.get(v.getId()));
            }
        });
        Boolean isGenerateVoucher = generateVoucher(idList, YesOrNoEnum.YES.getCode());
        if (!isGenerateVoucher) {
            throw new ServiceException("凭证存在未生成，不可以提交");
        }
        return this.updateBatchById(assetAbsTransferPaymentEntityList);
    }

    @Override
    public Boolean generateVoucher(List<Long> idList, String isSubmit) {
        if (CollectionUtil.isEmpty(idList)) {
            throw new ServiceException("请至少选择一条数据生成凭证");
        }
        List<AssetAbsTransferPaymentEntity> assetAbsTransferPaymentEntityList = this.listByIds(idList);
        assetAbsTransferPaymentEntityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("处理状态为已录入的才可以生成凭证");
            }
        });
        //生成凭证前先删除凭证
        iVoucherService.deleteByBatchIdList(idList,BatchTypeEnum.ABSZF.getCode());
        Map<Long,AssetAbsTransferPaymentEntity> absEntityMap = assetAbsTransferPaymentEntityList.stream().collect(Collectors.toMap(e->e.getId(),e->e,(k1,k2)->k2));
        List<AssetAbsTransferPaymentDetailEntity> detailEntityList = iAssetAbsTransferPaymentDetailService.lambdaQuery().in(AssetAbsTransferPaymentDetailEntity::getAssetAbsTransferPaymentId,idList).list();
        List<Map<String,Object>> voucherMapList = Lists.newArrayList();
        detailEntityList.stream().forEach(v -> {
            AssetAbsTransferPaymentEntity entity = absEntityMap.get(v.getAssetAbsTransferPaymentId());
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
            iAssetAbsTransferPaymentDetailService.lambdaUpdate().set(AssetAbsTransferPaymentDetailEntity::getVoucherIds, voucherIds).set(AssetAbsTransferPaymentDetailEntity::getErrorInfo,errorInfo).set(AssetAbsTransferPaymentDetailEntity::getPeriodCode,periodCode).eq(AssetAbsTransferPaymentDetailEntity::getId,Long.parseLong(infoVO.getOrderId())).update();
        }
        if (YesOrNoEnum.NO.getCode().equals(isSubmit) && !isExistVoucherError) {
            updateIsGenerateVoucher(idList,isSubmit);
        }
        return Boolean.TRUE;
    }

    @Override
    public List<AssetAbsTransferPaymentSummarExcelVO> selectPaymentList(List<Long> idList) {
        AssetAbsTransferPaymentQueryDTO queryDTO = new AssetAbsTransferPaymentQueryDTO();
        queryDTO.setIdList(idList);
        return BeanUtil.copyToList(assetAbsTransferPaymentMapper.selectPaymentList(queryDTO),AssetAbsTransferPaymentSummarExcelVO.class);
    }

    @Override
    public List<AssetAbsTransferPaymentDetailExcelVO> selectPaymentDetailList(List<Long> idList) {
        AssetAbsTransferPaymentDetailQueryDTO queryDTO = new AssetAbsTransferPaymentDetailQueryDTO();
        queryDTO.setAssetAbsTransferPaymentIdList(idList);
        return BeanUtil.copyToList(iAssetAbsTransferPaymentDetailService.selectPaymentDetailList(queryDTO),AssetAbsTransferPaymentDetailExcelVO.class);
    }

    @Override
    public Boolean updateProcessStatus(CommonApproveDTO approveDTO) {
        if (StringUtils.isEmpty(approveDTO.getDocumentStatus())) {
            throw new ServiceException("处理状态不可以为空");
        }
        AssetAbsTransferPaymentEntity assetAbsTransferPaymentEntity = this.getById(approveDTO.getDocumentId());
        if (null == assetAbsTransferPaymentEntity) {
            throw new ServiceException("资产赎回数据不存在");
        }
        String processStatus = assetAbsTransferPaymentEntity.getProcessStatus();
        try {
            if (ProcessStatusEnum.REVIEWED.getCode().equals(approveDTO.getDocumentStatus())) {
                processStatus = ProcessStatusEnum.REVIEWED.getCode();
            } else if (ProcessStatusEnum.REJECTED.getCode().equals(approveDTO.getDocumentStatus())) {
                processStatus = ProcessStatusEnum.REJECTED.getCode();
            }
            assetAbsTransferPaymentEntity.setProcessStatus(processStatus);
            assetAbsTransferPaymentEntity.setApproveErrorInfo("");
        } catch (Exception e) {
            assetAbsTransferPaymentEntity.setApproveErrorInfo(e.getMessage());
        }
        return this.updateById(assetAbsTransferPaymentEntity);
    }

    public void updateIsGenerateVoucher(List<Long> idList,String isSubmit){
        if (YesOrNoEnum.YES.getCode().equals(isSubmit)) {
            return;
        }
        //获取详情信息
        Map<Long,List<AssetAbsTransferPaymentDetailEntity>> detailEntityMap = iAssetAbsTransferPaymentDetailService.lambdaQuery()
                .in(AssetAbsTransferPaymentDetailEntity::getAssetAbsTransferPaymentId,idList).list().stream().collect(Collectors.groupingBy(AssetAbsTransferPaymentDetailEntity::getAssetAbsTransferPaymentId));
        for (Map.Entry<Long, List<AssetAbsTransferPaymentDetailEntity>> entry : detailEntityMap.entrySet()) {
            Boolean isExistEmpty = entry.getValue().stream().anyMatch(v -> StringUtils.isEmpty(v.getVoucherIds()));
            String isGenerateVoucher = YesOrNoEnum.NO.getCode();
            Integer periodCode=null;
            if (!isExistEmpty) {
                isGenerateVoucher = YesOrNoEnum.YES.getCode();
                periodCode = entry.getValue().get(0).getPeriodCode();
            }
            this.lambdaUpdate().set(AssetAbsTransferPaymentEntity::getIsGenerateVoucher, isGenerateVoucher).set(AssetAbsTransferPaymentEntity::getPeriodCode,periodCode).eq(AssetAbsTransferPaymentEntity::getId,entry.getKey()).update();
        }
    }

    public Boolean isPaymentExistFlag(String loanContractCode,String periods) {
        AssetAbsTransferPaymentEntity entity = this.getOne(Wrappers.<AssetAbsTransferPaymentEntity>lambdaQuery().eq(AssetAbsTransferPaymentEntity::getLoanContractCode,loanContractCode).eq(AssetAbsTransferPaymentEntity::getPeriods,periods));
        if (null != entity) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public Boolean isExistFlag(String loanContractCode,String periods,String contractCode) {
        AssetAbsTransferPaymentQueryDTO queryDTO = new AssetAbsTransferPaymentQueryDTO();
        queryDTO.setLoanContractCode(loanContractCode);
        queryDTO.setContractCode(contractCode);
        queryDTO.setPeriods(periods);
        List<AssetAbsTransferPaymentDetailVO> detailVOS =  assetAbsTransferPaymentMapper.selectListByCondition(queryDTO);
        if (CollectionUtil.isNotEmpty(detailVOS)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public void checkData(List<AssetAbsTransferPaymentExcelVO> assetAbsTransferPaymentExcelVOList) {
        if (CollectionUtil.isEmpty(assetAbsTransferPaymentExcelVOList)) {
            throw new ServiceException("导入数据为空");
        }
        AssetAbsTransferPaymentExcelVO excelVO = assetAbsTransferPaymentExcelVOList.get(0);
        Date accountDate = excelVO.getAccountDate();
        String period = excelVO.getPeriods();
        assetAbsTransferPaymentExcelVOList.stream().forEach(v -> {
              if (ObjectUtil.isNull(v.getAccountDate())) {
                  throw new ServiceException("记账日期不可以为空");
              }
              if (StringUtils.isEmpty(v.getPeriods())) {
                  throw new ServiceException("出表期数不可以为空");
              }
              if (StringUtils.isEmpty(v.getContractCode())) {
                  throw new ServiceException("合同编码不可以为空");
              }
              if (excelVO.getAccountDate().compareTo(accountDate)!=0) {
                  throw new ServiceException("记账日期存在不一致");
              }
              if (!period.equals(v.getPeriods())) {
                  throw new ServiceException("出表期数存在不一致");
              }
        });
        long num = assetAbsTransferPaymentExcelVOList.stream().map(AssetAbsTransferPaymentExcelVO::getContractCode).distinct().count();
        if (num < assetAbsTransferPaymentExcelVOList.size()) {
            throw new ServiceException("合同编码存在重复，不可导入");
        }

    }


    public LambdaQueryWrapper<AssetAbsTransferPaymentEntity> getQueryWrapper(AssetAbsTransferPaymentQueryDTO queryDTO){
        LambdaQueryWrapper<AssetAbsTransferPaymentEntity> queryWrapper = Wrappers.<AssetAbsTransferPaymentEntity>lambdaQuery();
        if (StringUtils.isNotEmpty(queryDTO.getLoanContractCode())) {
            queryWrapper.like(AssetAbsTransferPaymentEntity::getLoanContractCode,queryDTO.getLoanContractCode());
        }
        if (ObjectUtil.isNotNull(queryDTO.getStartAccountDate())) {
            queryWrapper.ge(AssetAbsTransferPaymentEntity::getAccountDate, queryDTO.getStartAccountDate());
        }
        if (ObjectUtil.isNotNull(queryDTO.getEndAccountDate())) {
            queryWrapper.le(AssetAbsTransferPaymentEntity::getAccountDate, queryDTO.getEndAccountDate());
        }
        if (CollectionUtil.isNotEmpty(queryDTO.getPeriodsList())) {
            queryWrapper.in(AssetAbsTransferPaymentEntity::getPeriods, queryDTO.getPeriodsList());
        }
        if (CollectionUtil.isNotEmpty(queryDTO.getLoanContractCodeList())) {
            queryWrapper.in(AssetAbsTransferPaymentEntity::getLoanContractCode, queryDTO.getLoanContractCodeList());
        }
        if (ObjectUtil.isNotNull(queryDTO.getId())) {
            queryWrapper.eq(AssetAbsTransferPaymentEntity::getId, queryDTO.getId());
        }
        return queryWrapper;
    }

    public void batchDeleteVoucher(List<Long> ids){
        //获取所有的凭证Id
        List<AssetAbsTransferPaymentDetailEntity> detailsEntityList = iAssetAbsTransferPaymentDetailService.lambdaQuery().in(AssetAbsTransferPaymentDetailEntity::getAssetAbsTransferPaymentId, ids).list();
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
        iAssetAbsTransferPaymentDetailService.updateBatchById(detailsEntityList);
    }

    public Map<String,Object> getVoucherMap(AssetAbsTransferPaymentEntity entity, AssetAbsTransferPaymentDetailEntity v, String isSubmit) {
        AssetAbsTransferPaymentVoucherDTO voucherDTO = BeanUtil.copyProperties(v, AssetAbsTransferPaymentVoucherDTO.class);
        voucherDTO.setSystemCode(SystemEnum.CWZT.getCode());
        voucherDTO.setSystemName(SystemEnum.CWZT.getDesc());
        voucherDTO.setOrderId(v.getId().toString());
        voucherDTO.setSceneCode(SceneEnum.ABSZF.getCode());
        voucherDTO.setSceneName(SceneEnum.ABSZF.getDesc());
        voucherDTO.setBusinessCode(BusinessEnum.ZLYW.getCode());
        voucherDTO.setBusinessName(BusinessEnum.ZLYW.getDesc());
        voucherDTO.setBusinessDate(new Date());
        voucherDTO.setBatchId(entity.getId());
        voucherDTO.setBatchType(BatchTypeEnum.ABSZF.getCode());
        voucherDTO.setIsSubmit(isSubmit);
        voucherDTO.setOrgId(v.getOrgId());
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
}

