package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.admin.api.RemoteDictService;
import com.utfinancing.financehub.admin.api.model.SysDictData;
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
import com.utfinancing.financehub.engine.finance.model.vo.TaOtherPayableDetailVO;
import com.utfinancing.financehub.engine.finance.model.vo.TaOtherPayableVO;
import com.utfinancing.financehub.engine.finance.mapper.TaOtherPayableMapper;
import com.utfinancing.financehub.engine.finance.model.vo.VoucherVO;
import com.utfinancing.financehub.engine.finance.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import com.utfinancing.financehub.engine.rule.model.dto.ExecuteCommonDTO;
import com.utfinancing.financehub.engine.rule.model.vo.VoucherInfoVO;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.util.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import javax.annotation.Resource;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author : wenbin
 * @Date : Create in 2024-05-22
 * @Description :  TaOtherPayable服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class TaOtherPayableServiceImpl extends ServiceImpl<TaOtherPayableMapper, TaOtherPayableEntity> implements ITaOtherPayableService {

    private final TaOtherPayableMapper taOtherPayableMapper;
    private final IOrgCompanyService iOrgCompanyService;
    private final ITaOtherPayableDetailService iTaOtherPayableDetailService;
    private final INonConfirmCollectionAccountCheckingService iNonConfirmCollectionAccountCheckingService;
    private final IRuleService iRuleService;
    private final IVoucherService iVoucherService;
    @Autowired
    private RemoteDictService remoteDictService;

    @Value("${approve.url.taOtherPayable-url:null}")
    private String approveUrl;

    @Resource
    private IApproveService iApproveService;

    @Override
    public Long saveTaOtherPayable(TaOtherPayableDTO dto) {
        TaOtherPayableEntity entity = BeanUtil.copyProperties(dto, TaOtherPayableEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateTaOtherPayable(Long id, TaOtherPayableDTO dto) {
        TaOtherPayableEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public TaOtherPayableDTO getTaOtherPayableDTOById(Long id) {
        TaOtherPayableEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, TaOtherPayableDTO.class);
    }

    @Override
    public IPage<TaOtherPayableVO> selectPage(TaOtherPayableQueryDTO queryDTO) {
        LambdaQueryWrapper<TaOtherPayableEntity> queryWrapper = Wrappers.<TaOtherPayableEntity>lambdaQuery();
        // 这里注入查询条件
        setQueryCondition(queryWrapper, queryDTO);
        IPage<TaOtherPayableEntity> entityIPage = taOtherPayableMapper.selectPage(new Page<TaOtherPayableEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        IPage<TaOtherPayableVO> page = ListBeanUtil.copyPage(entityIPage, TaOtherPayableVO.class);

        List<TaOtherPayableVO> list = page.getRecords();
        if (CollectionUtils.isNotEmpty(list)) {
            list.stream().forEach(i -> {
                i.setReclassificationMonthStr(LocalDateTimeUtil.format(i.getReclassificationMonth(), "yyyy-MM"));
            });
        }
        return page;
    }

    /**
     * 查询条件
     *
     * @param queryWrapper
     * @param queryDTO
     */
    private void setQueryCondition(LambdaQueryWrapper<TaOtherPayableEntity> queryWrapper, TaOtherPayableQueryDTO queryDTO) {
        if (ObjectUtil.isNotEmpty(queryDTO.getReclassificationMonth())) {
            queryWrapper.apply("to_char(reclassification_month, 'YYYY-MM') = {0}", queryDTO.getReclassificationMonth());
        }

        if (ObjectUtil.isNotEmpty(queryDTO.getBatchNo())) {
            queryWrapper.like(TaOtherPayableEntity::getBatchNo, queryDTO.getBatchNo());
        }
        if (CollUtil.isNotEmpty(queryDTO.getProcessStatusList())) {
            queryWrapper.in(TaOtherPayableEntity::getProcessStatus, queryDTO.getProcessStatusList());
        }
        if (CollUtil.isNotEmpty(queryDTO.getTaOtherPayableIdList())) {
            queryWrapper.in(TaOtherPayableEntity::getId, queryDTO.getTaOtherPayableIdList());
        }
        if (ObjectUtil.isNotEmpty(queryDTO.getId())) {
            queryWrapper.eq(TaOtherPayableEntity::getId, queryDTO.getId());
        }
        queryWrapper.orderByDesc(TaOtherPayableEntity::getId);
    }

    @Override
    public List<TaOtherPayableVO> selectList(TaOtherPayableQueryDTO queryDTO) {
        LambdaQueryWrapper<TaOtherPayableEntity> queryWrapper = Wrappers.<TaOtherPayableEntity>lambdaQuery();
        // 这里注入查询条件
        setQueryCondition(queryWrapper, queryDTO);
        List<TaOtherPayableEntity> entityIPage = taOtherPayableMapper.selectList(queryWrapper);
        return ListBeanUtil.copyList(entityIPage, TaOtherPayableVO.class);
    }

    /**
     * 生成凭证
     *
     * @param ids
     * @param isSubmit
     * @return
     */
    @Override
    public Boolean generateVoucher(List<Long> ids, String isSubmit) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少选择一条数据生成凭证");
        }
        List<TaOtherPayableEntity> entityList = this.listByIds(ids);
        entityList.stream().forEach(v -> {
            if (!(ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus()) || ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus()))) {
                throw new ServiceException("处理状态为已录入或者已拒绝的才可以生成凭证");
            }
        });
        // 生成凭证前先删除之前的凭证
        for (TaOtherPayableEntity taOtherPayableEntity : entityList) {
            if (StringUtils.isNotEmpty(taOtherPayableEntity.getVoucherId())) {
                List<Long> voucherIds = Arrays.stream(taOtherPayableEntity.getVoucherId().split(",")).map(Long::parseLong)
                        .collect(Collectors.toList());
                batchDeleteVoucher(voucherIds);
            }
        }

        //获取入账主体和客户编码映射
        List<SysDictData> bankOrgIdAndClientCodeMapping = remoteDictService.listDictData(DictTypeEnum.TA_VOUCHER_CLIENTCODE_ORGID_MAPPING.getCode()).getData();
        Map<String, SysDictData> bankOrgIdAndClientCodeMap = bankOrgIdAndClientCodeMapping.stream().collect(Collectors.toMap(SysDictData::getDictLabel, b -> b));
        Date businessCode = new Date();
        Map<String, Long> batchIdMap = new HashMap<>();
        Map<String, Long> lastBatchIdMap = new HashMap<>();
        for (TaOtherPayableEntity taOtherPayableEntity : entityList) {
            // 查询明细
            TaOtherPayableDetailQueryDTO taOtherPayableDetailQueryDTO = new TaOtherPayableDetailQueryDTO();
            taOtherPayableDetailQueryDTO.setTaOtherPayableIdList(Lists.newArrayList(taOtherPayableEntity.getId()));
            List<TaOtherPayableDetailVO> list = iTaOtherPayableDetailService.selectList(taOtherPayableDetailQueryDTO);

            LocalDateTime reclassMonth = taOtherPayableEntity.getReclassificationMonth();
            String reclassLastMonth = LocalDateTimeUtil.format(reclassMonth.plusMonths(-1), "yyyy-MM");
            TaOtherPayableEntity one = this.getOne(new LambdaQueryWrapper<TaOtherPayableEntity>().apply("to_char(reclassification_month, 'YYYY-MM') = {0}", reclassLastMonth));
            taOtherPayableDetailQueryDTO.setTaOtherPayableIdList(Lists.newArrayList(one.getId()));
            List<TaOtherPayableDetailVO> lastMonthDetailList = iTaOtherPayableDetailService.selectList(taOtherPayableDetailQueryDTO);
            List<Map<String, Object>> voucherMapList = Lists.newArrayList();
            for (TaOtherPayableDetailVO detailVO : list) {
                Map<String, Object> dataMap = getStringObjectMap(isSubmit, taOtherPayableEntity, detailVO, businessCode, batchIdMap, bankOrgIdAndClientCodeMap);
                dataMap.put("currentTa", "1");

                voucherMapList.add(dataMap);
            }
            for (TaOtherPayableDetailVO lastMonthDetail : lastMonthDetailList) {
                Map<String, Object> dataMap = getStringObjectMap(isSubmit, taOtherPayableEntity, lastMonthDetail, businessCode, lastBatchIdMap, bankOrgIdAndClientCodeMap);
                dataMap.put("currentTa", "0");
                //冲销的凭证不影响上个月数据
                dataMap.put("orderId", "1");
                dataMap.put("accountingMonth", reclassLastMonth);
                dataMap.put("lastMonthTaAmount", lastMonthDetail.getReclassificationAmount());
                dataMap.put("lastMonthTaAccount", lastMonthDetail.getAccountCode());
                voucherMapList.add(dataMap);
            }

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
            StringBuffer errorInfoHead = new StringBuffer();
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
                    errorInfoHead.append(errorInfo + ";");
                }
                if (CollectionUtils.isNotEmpty(infoVO.getVoucherDTOList())) {
                    voucherIds = infoVO.getVoucherDTOList().stream().map(VoucherDTO::getId).map(String::valueOf).collect(Collectors.toList()).stream().collect(Collectors.joining(","));
                    voucherIdsList.add(voucherIds);
                }
                if (StringUtils.isNotEmpty(infoVO.getOrderId()) && !StringUtils.equals(infoVO.getOrderId(), "1")) {
                    TaOtherPayableDetailEntity taOtherPayableDetailEntity = iTaOtherPayableDetailService.getById(Long.parseLong(infoVO.getOrderId()));
                    taOtherPayableDetailEntity.setAccountDate(LocalDateTime.now());
                    taOtherPayableDetailEntity.setIsGenerateVoucher(isGenerateVoucher);
                    taOtherPayableDetailEntity.setErrorInfo(errorInfo);
                    taOtherPayableDetailEntity.setVoucherId(voucherIds);
                    taOtherPayableDetailEntity.setUpdateTime(null);
                    iTaOtherPayableDetailService.updateById(taOtherPayableDetailEntity);
                }
            }
            String voucherIdHead = voucherIdsList.stream().collect(Collectors.joining(","));
            // 更新头上的生成凭证状态
            lambdaUpdate().set(TaOtherPayableEntity::getIsGenerateVoucher, isGenerateVoucherHead)
                    .set(TaOtherPayableEntity::getErrorInfo, errorInfoHead.toString())
                    .set(TaOtherPayableEntity::getVoucherId, voucherIdHead)
                    .eq(TaOtherPayableEntity::getId, taOtherPayableEntity.getId()).update();
        }
        return Boolean.TRUE;
    }

    private @NotNull Map<String, Object> getStringObjectMap(String isSubmit, TaOtherPayableEntity taOtherPayableEntity, TaOtherPayableDetailVO detailVO, Date businessCode, Map<String, Long> batchIdMap, Map<String, SysDictData> bankOrgIdAndClientCodeMap) {
        ExecuteCommonDTO executeCommonDTO = new ExecuteCommonDTO();
        executeCommonDTO.setSystemCode(SystemEnum.CWZT.getCode());
        executeCommonDTO.setSystemName(SystemEnum.CWZT.getDesc());
        executeCommonDTO.setSceneCode(SceneEnum.TACFL.getCode());
        executeCommonDTO.setSceneName(SceneEnum.TACFL.name());
        executeCommonDTO.setBusinessCode(BusinessEnum.ZLYW.getCode());
        executeCommonDTO.setOrderId(detailVO.getId().toString());
        executeCommonDTO.setOrgId(detailVO.getBankOrgId());
        executeCommonDTO.setBusinessDate(businessCode);
        executeCommonDTO.setContractCode(null);
        executeCommonDTO.setBatchId(getBatchNo(detailVO.getBankOrgId(), batchIdMap));

        //bank_org_id为01-C0001时，传01-02-000102；bank_org_id为02-C0001时，默认传01-02-000521；bank_org_id为30001时，默认传01-03-000006
        String tmpContractCode = "";
        if (StringUtils.isEmpty(executeCommonDTO.getBusinessCode())) {
            tmpContractCode = "";
        } else if (StringUtils.equals("ZLYW", executeCommonDTO.getBusinessCode())) {
            tmpContractCode = "VL05Z0001";
        } else if (StringUtils.equals("BLYW", executeCommonDTO.getBusinessCode())) {
            tmpContractCode = "VL05Z0003";
        } else if (StringUtils.equals("WDYW", executeCommonDTO.getBusinessCode())) {
            tmpContractCode = "VL05Z0004";
        }
        executeCommonDTO.setContractCode(tmpContractCode);

        String bankOrgId = detailVO.getBankOrgId();
        String clientCode = "";
        SysDictData dictData = bankOrgIdAndClientCodeMap.get(bankOrgId);
        if (dictData != null) {
            if (JSON.parseArray(dictData.getRemark()).contains(detailVO.getAccountCode())) {
                clientCode = dictData.getDictValue();
            }
        }

        executeCommonDTO.setClientCode(clientCode);
        executeCommonDTO.setAccountDate(new Date());
//        executeCommonDTO.setBatchId(taOtherPayableEntity.getId());
        executeCommonDTO.setBatchType(BatchTypeEnum.TAQTYFK.getCode());
        executeCommonDTO.setIsSubmit(isSubmit);

        Map<String, Object> dataMap = BeanUtil.beanToMap(executeCommonDTO);
        dataMap.put("receivablelongTermCode", "");
        dataMap.put("isOtherPayment", YesOrNoEnum.YES.getCode());
//                dataMap.put("billContractCode", "XN001");
        String reclassificationMonthFormat = LocalDateTimeUtil.format(detailVO.getReclassificationMonth(), "yyyy-MM");
        dataMap.put("accountingMonth", reclassificationMonthFormat);
        dataMap.put("thisMonthTaAmount", detailVO.getReclassificationAmount());
        dataMap.put("thisMonthTaAccount", detailVO.getAccountCode());
        return dataMap;
    }

    private Long getBatchNo(String bankOrgId, Map<String, Long> batchIdMap) {
        Long l = batchIdMap.get(bankOrgId);
        if (l != null) {
            return l;
        }
        Long batchId = IdWorker.getId();
        batchIdMap.put(bankOrgId, batchId);
        return batchId;
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
     * 删除凭证
     *
     * @param idList
     */
    @Override
    public void batchDeleteVoucher(List<Long> idList) {
        //根据批次号删除凭证
        iVoucherService.deleteByIdList(idList);
    }

    /**
     * 上传
     *
     * @param file
     * @return
     */
    @Override
    public Boolean importFile(MultipartFile file) {
        InputStream inputStream = null;
        try {
            ExcelUtil<TaOtherPayableDetailExcelDTO> util = new ExcelUtil<TaOtherPayableDetailExcelDTO>(TaOtherPayableDetailExcelDTO.class);
            inputStream = file.getInputStream();
            List<TaOtherPayableDetailExcelDTO> list = util.importExcel(file.getInputStream());
            checkData(list);
            // 保存数据
            List<TaOtherPayableDetailEntity> entityList = BeanUtil.copyToList(list, TaOtherPayableDetailEntity.class);

            LocalDateTime reclassificationMonth = entityList.get(0).getReclassificationMonth();

            TaOtherPayableEntity taOtherPayableEntity = new TaOtherPayableEntity();
            taOtherPayableEntity.setId(IdWorker.getId());
            taOtherPayableEntity.setReclassificationMonth(reclassificationMonth);
            // 生成批次
            String batchNo = DateUtil.format(new Date(), "yyyyMMddHHmmssSSS");
            taOtherPayableEntity.setBatchNo(batchNo);
            taOtherPayableEntity.setReclassificationAmount(entityList.stream().map(TaOtherPayableDetailEntity::getReclassificationAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
            // 保存汇总信息
            save(taOtherPayableEntity);

            entityList.stream().forEach(a -> {
                a.setTaOtherPayableId(taOtherPayableEntity.getId());
            });
            // 保存明细数据
            iTaOtherPayableDetailService.saveBatch(entityList);

            //更新明细数据字段
            List<TaOtherPayableDetailEntity> detailList = iTaOtherPayableDetailService.selectDetailInfo(taOtherPayableEntity.getId());

            iTaOtherPayableDetailService.updateBatchById(detailList);
        } catch (Exception e) {
            throw new ServiceException("上传文件失败，失败原因:" + e.getMessage());
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return Boolean.TRUE;
    }

    /**
     * 校验上传数据
     *
     * @param list
     */
    private void checkData(List<TaOtherPayableDetailExcelDTO> list) {
        Set<String> unionSet = new HashSet<>();
        Date reclassificationMonth = list.get(0).getReclassificationMonth();
        list.stream().forEach(a -> {
            if (ObjectUtil.isEmpty(a.getReclassificationMonth())) {
                throw new ServiceException("重分类月份不能为空");
            }
            if (reclassificationMonth.compareTo(a.getReclassificationMonth()) != 0) {
                throw new ServiceException("重分类月份必须一致");
            }
            if (ObjectUtil.isEmpty(a.getBankOrgId())) {
                throw new ServiceException("网银到账主体不能为空");
            }
            if (ObjectUtil.isEmpty(a.getEbankBatchNo())) {
                throw new ServiceException("业务系统批扣流水号不能为空");
            }
            if (ObjectUtil.isEmpty(a.getFinancialPrimaryClassic())) {
                throw new ServiceException("财务初分类不能为空");
            }
//            if (ObjectUtil.isEmpty(a.getConfirmAccountProperty())) {
//                throw new ServiceException("运营部确认款项性质不能为空");
//            }
            if (ObjectUtil.isEmpty(a.getReclassificationAmount())) {
                throw new ServiceException("重分类金额不能为空");
            }
            if (ObjectUtil.isEmpty(a.getAccountCode())) {
                throw new ServiceException("重分类科目编码不能为空");
            }
            // 校验长期应收款编号+合同编号重复
            if (!unionSet.add(a.getEbankBatchNo())) {
                throw new ServiceException("文件存在重复的业务系统批扣流水号[" + a.getEbankBatchNo() + "],请检查");
            }
        });

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        List<TaOtherPayableEntity> entityList = this.list(new LambdaQueryWrapper<TaOtherPayableEntity>().apply("to_char(reclassification_month, 'YYYY-MM') = {0}", sdf.format(reclassificationMonth)));
        if (CollectionUtils.isNotEmpty(entityList)) {
            throw new ServiceException("已经上传该重分类月份的TA其他应收款数据");
        }

        // 转换签约主体
        // 签约主体
        Map<String, String> companyMap = iOrgCompanyService.selectAllOrgIdAndName().stream().collect(Collectors.toMap(e -> e.getOrgName(), e -> e.getOrgId(), (a, b) -> b));
        list.stream().forEach(a -> {
            String orgId = companyMap.get(a.getBankOrgId());
            if (ObjectUtil.isEmpty(orgId)) {
                throw new ServiceException("根据网银到账主体名称[" + a.getBankOrgId() + "]未查询到对应的签约主体");
            }
            a.setBankOrgId(orgId);
        });
    }

    /**
     * 提交
     *
     * @param ids
     * @return
     */
    @Override
    public Boolean submit(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条数据提交");
        }
        List<TaOtherPayableEntity> entityList = this.listByIds(ids);
        List<ApproveDTO> approveDTOList = Lists.newArrayList();
        entityList.stream().forEach(v -> {
            if (!(ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus()) || ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus()))) {
                throw new ServiceException("只有处理状态为已录入或者已拒绝的才可以提交");
            }
            ApproveDTO approveDTO = new ApproveDTO();
            approveDTO.setDocumentId(v.getId());
            approveDTO.setDocumentType(BatchTypeEnum.TAQTYFK.getCode());
            approveDTO.setUrl(approveUrl + v.getId());
            approveDTOList.add(approveDTO);
        });
        // 生成凭证
        Boolean generateVoucherFlag = generateVoucher(ids, YesOrNoEnum.YES.getCode());
        if (generateVoucherFlag) {
            // 发送审核
            Map<Long, Long> processInstantIdMap = iApproveService.submit(approveDTOList);

            List<TaOtherPayableEntity> newEntityList = this.listByIds(ids);
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
            throw new ServiceException("凭证生成失败，提交失败");
        }
    }

    /**
     * 撤回
     *
     * @param ids
     * @return
     */
    @Override
    public Boolean withdraw(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条数据撤回");
        }
        List<TaOtherPayableEntity> entityList = this.listByIds(ids);
        entityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.SUBMITTED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("只有处理状态为已提交的才可以撤回");
            }
            v.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
        });
        iApproveService.withdraw(entityList.stream().map(TaOtherPayableEntity::getProcessInstanceId).collect(Collectors.toList()));

        //撤回之后需要将凭证状态改为已录入状态
        iVoucherService.updateStatusByBatch(ids, BatchTypeEnum.TAQTYFK.getCode(), ProcessStatusEnum.ENTERED.getCode(), "", "");
        return this.updateBatchById(entityList);
    }

    /**
     * 删除
     *
     * @param ids
     * @return
     */
    @Override
    public Boolean delete(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条数据删除");
        }
        List<TaOtherPayableEntity> transferRegisterEntityList = this.listByIds(ids);
        transferRegisterEntityList.stream().forEach(v -> {
            if (!(ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus()) || ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus()))) {
                throw new ServiceException("只有处理状态为已录入或者已拒绝的才可以删除");
            }
        });
        // 删除凭证
        batchDeleteVoucher(ids);
        // 删除明细
        iTaOtherPayableDetailService.removeByTaOtherPayableIdList(ids);
        // 删除汇总信息
        return removeBatchByIds(ids);
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
        TaOtherPayableEntity entity = this.getById(approveDTO.getDocumentId());
        if (ObjectUtil.isEmpty(entity)) {
            throw new ServiceException("ta其他应付款数据不存在");
        }
        // 修改凭证状态，通过和驳回都修改
        List<Long> voucherIds = Arrays.stream(entity.getVoucherId().split(",")).map(Long::parseLong)
                .collect(Collectors.toList());
        updateVoucherStatus(voucherIds, approveDTO);
        // 修改状态
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
        List<String> voucherIdList = ids.stream().map(Objects::toString).collect(Collectors.toList());
        // 更新凭证状态
        iVoucherService.updateStatusByids(voucherIdList, approveDTO.getDocumentStatus(),
                approveDTO.getApproverNum(), approveDTO.getApproverName());
    }

}

