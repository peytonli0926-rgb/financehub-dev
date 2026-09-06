package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.google.common.collect.Maps;
import com.utfinancing.financehub.admin.api.RemoteDictService;
import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.common.core.utils.uuid.UUID;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.common.security.utils.SecurityUtils;
import com.utfinancing.financehub.engine.approve.model.dto.ApproveDTO;
import com.utfinancing.financehub.engine.approve.service.IApproveService;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.OrgCompanyVO;
import com.utfinancing.financehub.engine.finance.model.vo.TaReclassificationDetailVO;
import com.utfinancing.financehub.engine.finance.model.vo.TaReclassificationVO;
import com.utfinancing.financehub.engine.finance.mapper.TaReclassificationMapper;
import com.utfinancing.financehub.engine.finance.model.vo.VoucherVO;
import com.utfinancing.financehub.engine.finance.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import com.utfinancing.financehub.engine.rule.constant.RuleConstant;
import com.utfinancing.financehub.engine.rule.model.dto.ExecuteCommonDTO;
import com.utfinancing.financehub.engine.rule.model.vo.VoucherInfoVO;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
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
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @Author : wenbin
 * @Date : Create in 2024-05-24
 * @Description :  TaReclassification服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class TaReclassificationServiceImpl extends ServiceImpl<TaReclassificationMapper, TaReclassificationEntity> implements ITaReclassificationService {

    private final TaReclassificationMapper taReclassificationMapper;

    @Resource
    private ITaReclassificationUploadRecordService taReclassificationUploadRecordService;

    @Resource
    private ITaReclassificationDetailService taReclassificationDetailService;

    @Resource
    private IVoucherService iVoucherService;

    @Resource
    private IRuleService iRuleService;

    @Value("${approve.url.taReclassification-url:null}")
    private String approveUrl;

    @Resource
    private IApproveService iApproveService;

    @Resource
    private IBatchTaskService iBatchTaskService;

    @Resource
    private IOutTableContractDetailService outTableContractDetailService;
    @Resource
    private IOrgCompanyService orgCompanyService;
    @Autowired
    private RemoteDictService remoteDictService;


    @Override
    public Long saveTaReclassification(TaReclassificationDTO dto) {
        TaReclassificationEntity entity = BeanUtil.copyProperties(dto, TaReclassificationEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateTaReclassification(Long id, TaReclassificationDTO dto) {
        TaReclassificationEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public TaReclassificationDTO getTaReclassificationDTOById(Long id) {
        TaReclassificationEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, TaReclassificationDTO.class);
    }

    @Override
    public IPage<TaReclassificationVO> selectPage(TaReclassificationQueryDTO queryDTO) {
        LambdaQueryWrapper<TaReclassificationEntity> queryWrapper = Wrappers.<TaReclassificationEntity>lambdaQuery();
        //这里注入查询条件
        if (StringUtils.isNotEmpty(queryDTO.getReclassificationMonth())) {
            queryWrapper.apply("to_char(reclassification_month, 'YYYY-MM') = {0}", queryDTO.getReclassificationMonth());
        }

        String amount = queryDTO.getTaReclassificationAmount();
        if (StringUtils.isNotEmpty(amount) && StringUtils.equals(amount, "0")) {
            queryWrapper.apply("coalesce(ta_reclassification_amount, 0) = 0");
        }

        IPage<TaReclassificationEntity> entityIPage = taReclassificationMapper.selectPage(new Page<TaReclassificationEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, TaReclassificationVO.class);
    }

    @Override
    public List<TaReclassificationVO> selectList(TaReclassificationQueryDTO queryDTO) {
        String reclassificationMonth = queryDTO.getReclassificationMonth();
        if (StringUtils.isEmpty(reclassificationMonth)) {
            throw new ServiceException("重分类月份不能为空");
        }
        LocalDateTime now = LocalDateTime.now();
        String currentMonth = now.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        if (StringUtils.equals(reclassificationMonth, currentMonth)) {
            reclassificationMonth = now.minusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM"));
        }

        String taAmount = queryDTO.getTaReclassificationAmount();

        LambdaQueryWrapper<TaReclassificationEntity> wrapper = new LambdaQueryWrapper<TaReclassificationEntity>();
        wrapper.apply("to_char(reclassification_month, 'YYYY-MM') = {0}", reclassificationMonth);
        if (StringUtils.isNotEmpty(taAmount)) {
            if (StringUtils.equals("0", taAmount)) {
                wrapper.apply("ta_reclassification_amount = 0");
            } else {
                wrapper.apply("ta_reclassification_amount != 0");
            }
        }

        List<TaReclassificationEntity> list = this.list(wrapper);
        return BeanUtil.copyToList(list, TaReclassificationVO.class);
    }

    @Override
    public Boolean generateVoucher(List<Long> ids, String isSubmit) {
//        if (CollectionUtils.isEmpty(ids)) {
//            throw new ServiceException("请至少选择一条数据生成凭证");
//        }
        List<TaReclassificationEntity> entityList = this.listByIds(ids);
//        entityList.stream().forEach(v -> {
//            if (!(ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus()) || ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus()))) {
//                throw new ServiceException("处理状态为已录入或者已拒绝的才可以生成凭证");
//            }
//        });

        // 生成凭证前先删除之前的凭证
        for (TaReclassificationEntity taReclassificationEntity : entityList) {
            if (StringUtils.isNotEmpty(taReclassificationEntity.getVoucherId())) {
                List<Long> voucherIds = Arrays.stream(taReclassificationEntity.getVoucherId().split(",")).map(Long::parseLong)
                        .collect(Collectors.toList());
                batchDeleteVoucher(voucherIds);
            }
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");

        //获取入账主体和客户编码映射
        List<SysDictData> bankOrgIdAndClientCodeMapping = remoteDictService.listDictData(DictTypeEnum.TA_VOUCHER_CLIENTCODE_ORGID_MAPPING.getCode()).getData();
        List<SysDictData> orgDic = remoteDictService.listDictData(DictTypeEnum.COMPANY.getCode()).getData();
        Map<String, SysDictData> bankOrgIdAndClientCodeMap = bankOrgIdAndClientCodeMapping.stream().collect(Collectors.toMap(SysDictData::getDictLabel, b -> b));
        Map<String, String> orgMap = orgDic.stream().collect(Collectors.toMap(SysDictData::getDictValue,
                SysDictData::getDictLabel));
        Date businessCode = new Date();
        Map<String, Long> batchIdMap = new HashMap<>();
        Map<String, Long> lastBatchIdMap = new HashMap<>();
        for (TaReclassificationEntity entity : entityList) {


            String reclassificationMonthFormat = LocalDateTimeUtil.format(entity.getReclassificationMonth(), "yyyy-MM");
            String reclassificationLastMonthFormat = LocalDateTimeUtil.format(entity.getReclassificationMonth().minusMonths(1), "yyyy-MM");

            List<TaReclassificationDetailEntity> currentMonthDetailList = taReclassificationDetailService.list(new LambdaQueryWrapper<TaReclassificationDetailEntity>()
                    .apply("to_char(reclassification_month, 'YYYY-MM') = {0}", reclassificationMonthFormat));

            List<TaReclassificationDetailEntity> lastMonthDetailList = taReclassificationDetailService.list(new LambdaQueryWrapper<TaReclassificationDetailEntity>()
                    .apply("to_char(reclassification_month, 'YYYY-MM') = {0}", reclassificationLastMonthFormat));


            List<OutTableContractDetailEntity> absList = outTableContractDetailService.getOutTableContractDetailInfoByTaId(entity.getId());
            List<Map<String, Object>> voucherMapList = Lists.newArrayList();
            for (TaReclassificationDetailEntity detail : currentMonthDetailList) {
                Map<String, Object> dataMap = getStringObjectMap(isSubmit, detail, orgMap, businessCode, batchIdMap, bankOrgIdAndClientCodeMap, reclassificationMonthFormat, absList);
                dataMap.put("currentTa", "1");
                voucherMapList.add(dataMap);
            }

            for (TaReclassificationDetailEntity lastMonthDetail : lastMonthDetailList) {
                Map<String, Object> dataMap = getStringObjectMap(isSubmit, lastMonthDetail, orgMap, businessCode, lastBatchIdMap, bankOrgIdAndClientCodeMap, reclassificationMonthFormat, absList);
                dataMap.put("currentTa", "0");
                //冲销的凭证不影响上个月数据
                dataMap.put("orderId", "1");
                dataMap.put("accountingMonth", reclassificationLastMonthFormat);
                dataMap.put("lastMonthTaAmount", lastMonthDetail.getTaReclassificationAmount());
                dataMap.put("lastMonthTaAccount", lastMonthDetail.getTaAccountCode());
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
                asyncDeleteVoucher(voucherIdList);
                return Boolean.FALSE;
            }

            //更新凭证批次号,统一条数据会生成2个主体的凭证，故需重新分配批次号
            List<VoucherEntity> updateBatchIds = new ArrayList<>();
            for (VoucherInfoVO voucherInfoVO : voucherResultList) {
                for (VoucherDTO voucherDTO : voucherInfoVO.getVoucherDTOList()) {
                    VoucherEntity voucherEntity = new VoucherEntity();
                    voucherEntity.setId(voucherDTO.getId());
                    voucherEntity.setPeriodCode(voucherDTO.getPeriodCode());
                    if (StringUtils.equals(voucherInfoVO.getOrderId(), "1")){
                        voucherEntity.setBatchId(lastBatchIdMap.get(voucherDTO.getOrgId()));
                    }else {
                        voucherEntity.setBatchId(batchIdMap.get(voucherDTO.getOrgId()));
                    }

                    updateBatchIds.add(voucherEntity);
                }
            }
            iVoucherService.updateBatchById(updateBatchIds);

            //提交时若凭证存在借贷不平的情况，应该报错不能提交 暂时注释掉
//            if(YesOrNoEnum.YES.getCode().equals(isSubmit) && !isExistVoucherError){
//                boolean validFlag = true;
//                for (VoucherInfoVO infoVO : voucherResultList) {
//                    if(!validFlag){
//                        break;
//                    }
//                   List<VoucherDTO> voucherDTOList = infoVO.getVoucherDTOList();
//                   if(CollectionUtils.isNotEmpty(voucherDTOList)){
//                       for(VoucherDTO dto : voucherDTOList){
//                           if(!StringUtils.equals(VoucherValidFlagEnum.VALID.getCode(), dto.getValidFlag())&&
//                                   !StringUtils.equals(VoucherValidFlagEnum.ENTRY_EMPTY.getCode(), dto.getValidFlag())){
//                               validFlag = false;
//                               break;
//                           }
//                       }
//                   }
//                }
//                if(!validFlag){
//                    List<Long> voucherIdList = Lists.newArrayList();
//                    voucherResultList.stream().forEach(voucherInfoVO -> {
//                        if (CollectionUtils.isNotEmpty(voucherInfoVO.getVoucherDTOList())) {
//                            voucherIdList.addAll(voucherInfoVO.getVoucherDTOList().stream().map(VoucherDTO::getId).collect(Collectors.toList()));
//                        }
//                    });
//                    asyncDeleteVoucher(voucherIdList);
//                    return Boolean.FALSE;
//                }
//            }

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
                if (StringUtils.isNotEmpty(infoVO.getOrderId()) && !StringUtils.equals(infoVO.getOrderId(), "1")){
                    TaReclassificationDetailEntity taReclassificationDetailEntity = taReclassificationDetailService.getById(Long.parseLong(infoVO.getOrderId()));
                    taReclassificationDetailEntity.setAccountDate(LocalDateTime.now());
                    taReclassificationDetailEntity.setIsGenerateVoucher(isGenerateVoucher);
                    taReclassificationDetailEntity.setErrorInfo(errorInfo);
                    taReclassificationDetailEntity.setVoucherId(voucherIds);
                    taReclassificationDetailEntity.setUpdateTime(LocalDateTime.now());
                    taReclassificationDetailService.updateById(taReclassificationDetailEntity);
                }
            }
            String voucherIdHead = voucherIdsList.stream().collect(Collectors.joining(","));
            // 更新头上的生成凭证状态
            lambdaUpdate().set(TaReclassificationEntity::getIsGenerateVoucher, isGenerateVoucherHead)
                    .set(TaReclassificationEntity::getErrorInfo, errorInfoHead.toString())
                    .set(TaReclassificationEntity::getVoucherId, voucherIdHead)
                    .eq(TaReclassificationEntity::getId, entity.getId()).update();
        }
        return Boolean.TRUE;
    }

    private @NotNull Map<String, Object> getStringObjectMap(String isSubmit, TaReclassificationDetailEntity detail, Map<String, String> orgMap, Date businessCode, Map<String, Long> batchIdMap, Map<String, SysDictData> bankOrgIdAndClientCodeMap, String reclassificationMonthFormat, List<OutTableContractDetailEntity> absList) {
        ExecuteCommonDTO executeCommonDTO = new ExecuteCommonDTO();
        executeCommonDTO.setSceneCode(SceneEnum.TACFL.getCode());
        executeCommonDTO.setSceneName(SceneEnum.TACFL.getDesc());
        executeCommonDTO.setOrderId(detail.getId().toString());
        executeCommonDTO.setSystemName(SystemEnum.getDescByCode(detail.getSystemCode()));
        executeCommonDTO.setSystemCode(SystemEnum.CWZT.getCode());
        executeCommonDTO.setBusinessCode(detail.getBusinessCode());
        executeCommonDTO.setBusinessName(BusinessEnum.getDescByCode(detail.getBusinessCode()));
        executeCommonDTO.setOrgId(detail.getOrgId());
        executeCommonDTO.setOrgName(orgMap.get(detail.getOrgId()));
        executeCommonDTO.setBusinessDate(businessCode);
        executeCommonDTO.setIsSubmit(isSubmit);
        executeCommonDTO.setBatchId(getBatchNo(detail.getOrgId(), batchIdMap));
//                executeCommonDTO.setContractCode(detail.getContractCode());
        //business_code=’ZLYW'时推VL05Z0001，business_code=’BLYW'时推VL05Z0003，business_code=’WDYW'时推VL05Z0004
        String tmpContractCode = "";
        if (StringUtils.isEmpty(detail.getBusinessCode())) {
            tmpContractCode = "";
        } else if (StringUtils.equals("ZLYW", detail.getBusinessCode())) {
            tmpContractCode = "VL05Z0001";
        } else if (StringUtils.equals("BLYW", detail.getBusinessCode())) {
            tmpContractCode = "VL05Z0003";
        } else if (StringUtils.equals("WDYW", detail.getBusinessCode())) {
            tmpContractCode = "VL05Z0004";
        }

        //bank_org_id为01-C0001时，传01-02-000102；bank_org_id为02-C0001时，默认传01-02-000521；bank_org_id为30001时，默认传01-03-000006
        String orgId = detail.getOrgId();
        String clientCode = detail.getClientCode();
        SysDictData dictData = bankOrgIdAndClientCodeMap.get(orgId);
        if (dictData != null) {
            if (JSON.parseArray(dictData.getRemark()).contains(detail.getTaAccountCode())) {
                clientCode = dictData.getDictValue();
            }
        }
        executeCommonDTO.setContractCode(tmpContractCode);
        executeCommonDTO.setClientCode(clientCode);

        Map<String, Object> dataMap = BeanUtil.beanToMap(executeCommonDTO);
        dataMap.put("bankOrgId", detail.getBankOrgId());
        dataMap.put("bankOrgName", orgMap.get(detail.getBankOrgId()));
        dataMap.put("accountingMonth", reclassificationMonthFormat);
        dataMap.put("thisMonthTaAmount", detail.getTaReclassificationAmount());
        dataMap.put("thisMonthTaAccount", detail.getTaAccountCode());
        dataMap.put("isOtherPayment", YesOrNoEnum.NO.getCode());
        // modify for 修复ta重分类因缺少ID排序字段而凭证生成报错问题 on 20250406 by  zhangli.chen
        dataMap.put(RuleConstant.FIELD_ID, detail.getId());
        SysDictData bankOrgDictData = bankOrgIdAndClientCodeMap.get(detail.getBankOrgId());
        if (bankOrgDictData != null) {
            if (JSON.parseArray(bankOrgDictData.getRemark()).contains(detail.getTaAccountCode())) {
                dataMap.put("bankOrgIdClientCode", bankOrgDictData.getDictValue());
            }
        }

        if (CollectionUtil.isNotEmpty(absList)) {
            Optional<OutTableContractDetailEntity> absOp = absList.stream()
                    .filter(i -> StringUtils.equals(i.getOrgId(), detail.getOrgId()) && StringUtils.equals(i.getContractCode(), detail.getContractCode())).findFirst();
            if (absOp.isPresent()) {
                OutTableContractDetailEntity absDetail = absOp.get();
                dataMap.put("billContractCode", absDetail.getLoanContractCode());
            } else {
                dataMap.put("billContractCode", "XN001");
            }
        } else {
            dataMap.put("billContractCode", "XN001");
        }
        return dataMap;
    }

    private Long getBatchNo(String orgId, Map<String, Long> batchIdMap) {
        Long l = batchIdMap.get(orgId);
        if (l != null) {
            return l;
        }
        Long batchId = IdWorker.getId();
        batchIdMap.put(orgId, batchId);
        return batchId;
    }

    public void asyncDeleteVoucher(List<Long> voucherIdList) {
        if (CollectionUtils.isEmpty(voucherIdList)) {
            return;
        }
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            // 异步任务的代码
            iVoucherService.deleteByIdList(voucherIdList);
        });
    }

    @Override
    public Boolean importFile(MultipartFile file) {

        InputStream inputStream = null;
        try {
            ExcelUtil<TaReclassificationExcelDTO> util = new ExcelUtil<TaReclassificationExcelDTO>(TaReclassificationExcelDTO.class);
            inputStream = file.getInputStream();
            List<TaReclassificationExcelDTO> list = util.importExcel(file.getInputStream());

            Date reclassificationMonth = list.get(0).getReclassificationMonth();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
            String reclassificationMonthFormat = formatter.format(reclassificationMonth);

            TaReclassificationEntity sumEntity = checkData(reclassificationMonth, list, reclassificationMonthFormat);

            String batchId = UUID.fastUUID().toString(true);

            List<TaReclassificationUploadRecordEntity> records = saveRecords(list, batchId);

            freshDetail(reclassificationMonthFormat, batchId, records, reclassificationMonth);

            freshSummary(reclassificationMonthFormat, sumEntity);

        } catch (Exception e) {
            throw new ServiceException("上传文件失败，失败原因:" + e.getMessage());
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return null;
    }

    private void freshSummary(String reclassificationMonthFormat, TaReclassificationEntity sumEntity) {
        List<TaReclassificationDetailEntity> monthDetailList = taReclassificationDetailService.list(new LambdaQueryWrapper<TaReclassificationDetailEntity>()
                .apply("to_char(reclassification_month, 'YYYY-MM') = {0}", reclassificationMonthFormat));

        BigDecimal sumTaAmount = BigDecimal.ZERO;
        for (TaReclassificationDetailEntity detail : monthDetailList) {
            sumTaAmount = NumberUtil.add(detail.getTaReclassificationAmount(), sumTaAmount);
        }
        sumEntity.setTaReclassificationAmount(sumTaAmount);
        sumEntity.setUpdateTime(LocalDateTime.now());
        sumEntity.setUpdateBy(SecurityUtils.getUserId() + "");
        this.updateById(sumEntity);
    }

    private void freshDetail(String reclassificationMonthFormat, String batchId, List<TaReclassificationUploadRecordEntity> records, Date reclassificationMonth) {
        Map<String, String> param = Maps.newHashMap();
        param.put("reclassificationMonth", reclassificationMonthFormat);
        param.put("batchId", batchId);
        List<TaReclassificationDetailEntity> detailEntityList = taReclassificationDetailService.getByParams(param);
        if (CollectionUtil.isNotEmpty(detailEntityList)) {
            List<TaReclassificationDetailEntity> tmpList = Lists.newArrayList();
//            List<TaReclassificationUploadRecordEntity> records = taReclassificationUploadRecordService.list(new LambdaQueryWrapper<TaReclassificationUploadRecordEntity>().eq(TaReclassificationUploadRecordEntity::getBatchId, batchId));
            for (TaReclassificationDetailEntity detailEntity : detailEntityList) {
                Optional<TaReclassificationUploadRecordEntity> opRecord = records.stream().filter(t ->
                        LocalDateTimeUtil.of(reclassificationMonth).plusMonths(1).minusDays(1).isEqual(t.getReclassificationMonth())
                                && StringUtils.equals(t.getContractCode(), detailEntity.getContractCode())
                                && StringUtils.equals(t.getEbankBatchNo(), detailEntity.getEbankBatchNo())).findFirst();
                if (opRecord.isPresent()) {
                    TaReclassificationUploadRecordEntity record = opRecord.get();
                    TaReclassificationDetailEntity tmp = new TaReclassificationDetailEntity();
                    tmp.setTaReclassificationAmount(record.getTaReclassificationAmount());
                    tmp.setTaAccountCode(record.getAccountCode());
                    tmp.setRemark(record.getRemark());
                    tmp.setId(detailEntity.getId());
                    tmpList.add(tmp);
                }
            }
            taReclassificationDetailService.updateBatchById(tmpList);
        }
    }

    private List<TaReclassificationUploadRecordEntity> saveRecords(List<TaReclassificationExcelDTO> list, String batchId) {
        List<TaReclassificationUploadRecordEntity> records = Lists.newArrayList();
        for (TaReclassificationExcelDTO dto : list) {
//            Date reclassMonth = dto.getReclassificationMonth();
//            LocalDateTime dt = LocalDateTimeUtil.of(reclassMonth).plusMonths(1).minusDays(1);

            TaReclassificationUploadRecordEntity record = new TaReclassificationUploadRecordEntity();
            record.setBatchId(batchId);
            record.setReclassificationMonth(LocalDateTimeUtil.of(dto.getReclassificationMonth()).plusMonths(1).minusDays(1));
            record.setContractCode(dto.getContractCode());
            record.setEbankBatchNo(dto.getEbankBatchNo());
            record.setTaReclassificationAmount(dto.getTaReclassificationAmount());
            record.setAccountCode(dto.getTaAccountCode());
            record.setRemark(dto.getRemark());
            record.setUploadBy(SecurityUtils.getUserId() + "");
            record.setUploadStatus(CheckExecuteStatusEnum.INPROGRESS.getCode());
            records.add(record);
        }
        taReclassificationUploadRecordService.saveBatch(records);
        return records;
    }

    private TaReclassificationEntity checkData(Date reclassificationMonth, List<TaReclassificationExcelDTO> list, String reclassificationMonthFormat) {
        if (CollectionUtil.isEmpty(list)) {
            throw new ServiceException("上传文件内容为空");
        }

        for (TaReclassificationExcelDTO dto : list) {
            if (ObjectUtil.isEmpty(dto.getReclassificationMonth()) ||
//                    ObjectUtil.isEmpty(dto.getContractCode())||
                    ObjectUtil.isEmpty(dto.getEbankBatchNo()) ||
                    (ObjectUtil.isEmpty(dto.getTaAccountCode()) && ObjectUtil.isEmpty(dto.getTaReclassificationAmount()))
            ) {
                throw new ServiceException("上传文件必填项不能为空");
            }
            if (reclassificationMonth.compareTo(dto.getReclassificationMonth()) != 0) {
                throw new ServiceException("上传的重分类月份必填一致");
            }
        }

        TaReclassificationEntity sumEntity = this.getOne(new LambdaQueryWrapper<TaReclassificationEntity>().apply("to_char(reclassification_month, 'YYYY-MM') = {0}", reclassificationMonthFormat));
        if (ObjectUtil.isEmpty(sumEntity)) {
            throw new ServiceException("上传月份的业务报表还未同步");
        }
        if (!StringUtils.equals(sumEntity.getProcessStatus(), ProcessStatusEnum.NOT_ENTERED.getCode()) && !StringUtils.equals(sumEntity.getProcessStatus(), ProcessStatusEnum.ENTERED.getCode())) {
            throw new ServiceException("该月份处理状态不支持上传文件");
        }

        return sumEntity;
    }

    @Override
    public void batchDeleteVoucher(List<Long> ids) {
        //根据批次号删除凭证
        iVoucherService.deleteByIdList(ids);
    }

    @Override
    public Boolean submit(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条数据提交");
        }
        List<TaReclassificationEntity> entityList = this.listByIds(ids);
        List<ApproveDTO> approveDTOList = Lists.newArrayList();
        entityList.stream().forEach(v -> {
            if (!(ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus()) || ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus()))) {
                throw new ServiceException("只有处理状态为已录入或者已拒绝的才可以提交");
            }
            ApproveDTO approveDTO = new ApproveDTO();
            approveDTO.setDocumentId(v.getId());
            approveDTO.setDocumentType(BatchTypeEnum.TACFL.getCode());
            approveDTO.setUrl(approveUrl + v.getId());
            approveDTOList.add(approveDTO);
        });
        // 生成凭证
        Boolean generateVoucherFlag = generateVoucher(ids, YesOrNoEnum.YES.getCode());
        if (generateVoucherFlag) {
            // 发送审核
            Map<Long, Long> processInstantIdMap = iApproveService.submit(approveDTOList);

            List<TaReclassificationEntity> newEntityList = this.listByIds(ids);
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

    @Override
    public Boolean withdraw(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条数据撤回");
        }
        List<TaReclassificationEntity> entityList = this.listByIds(ids);
        entityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.SUBMITTED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("只有处理状态为已提交的才可以撤回");
            }
            v.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
        });
        iApproveService.withdraw(entityList.stream().map(TaReclassificationEntity::getProcessInstanceId).collect(Collectors.toList()));
        //撤回之后需要将凭证状态改为已录入状态
        iVoucherService.updateStatusByBatch(ids, BatchTypeEnum.TACFL.getCode(), ProcessStatusEnum.ENTERED.getCode(), "", "");
        return this.updateBatchById(entityList);
    }

    @Override
    public Boolean delete(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条数据删除");
        }
        List<TaReclassificationEntity> transferRegisterEntityList = this.listByIds(ids);
        transferRegisterEntityList.stream().forEach(v -> {
            if (!(ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus()) || ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus()))) {
                throw new ServiceException("只有处理状态为已录入或者已拒绝的才可以删除");
            }
        });
        // 删除凭证
        batchDeleteVoucher(ids);
        // 删除明细
        taReclassificationDetailService.removeByTaReclassificationIdList(ids);
        // 删除汇总信息
        return removeBatchByIds(ids);
    }

    @Override
    public Boolean generateVoucherBatch(List<Long> ids, String isSubmit) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少选择一条数据生成凭证");
        }
        List<TaReclassificationEntity> entityList = this.listByIds(ids);
        entityList.stream().forEach(v -> {
            if (!(ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus()) || ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus()))) {
                throw new ServiceException("处理状态为已录入或者已拒绝的才可以生成凭证");
            }
        });

        //校验任务
        ids.forEach(v -> {
            Boolean isExistFlag = iBatchTaskService.isExistTask(v, BatchTypeEnum.TACFL.getCode());
            if (isExistFlag) {
                throw new ServiceException("存在任务正在执行，请稍后重试");
            }
        });
        //保存任务
        List<Long> taskIdList = Lists.newArrayList();
        ids.forEach(v -> {
            taskIdList.add(iBatchTaskService.saveBatchTask(BatchTaskDTO.builder().businessId(v).status("1").businessType(BatchTypeEnum.TACFL.getCode()).build()));
        });

        CompletableFuture.runAsync(() -> {
            generateVoucher(ids, isSubmit);
        }).whenComplete((v, e) -> {
            // 执行成功，更新任务状态
            iBatchTaskService.updateBatchTask(taskIdList, "2");
        }).exceptionally(e -> {
            log.info("TA重分类生成凭证批量处理数据失败", e);
            iBatchTaskService.updateBatchTask(taskIdList, "3");
            return null;
        });
        return Boolean.TRUE;
    }

    @Override
    public Boolean submitBatch(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条数据提交");
        }
        List<TaReclassificationEntity> entityList = this.listByIds(ids);
        entityList.stream().forEach(v -> {
            if (!(ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus()) || ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus()))) {
                throw new ServiceException("只有处理状态为已录入或者已拒绝的才可以提交");
            }
        });

        //校验任务
        ids.forEach(v -> {
            Boolean isExistFlag = iBatchTaskService.isExistTask(v, BatchTypeEnum.TACFL.getCode());
            if (isExistFlag) {
                throw new ServiceException("存在任务正在执行，请稍后重试");
            }
        });

        //保存任务
        List<Long> taskIdList = Lists.newArrayList();
        ids.forEach(v -> {
            taskIdList.add(iBatchTaskService.saveBatchTask(BatchTaskDTO.builder().businessId(v).status("1").businessType(BatchTypeEnum.TACFL.getCode()).build()));
        });

        CompletableFuture.runAsync(() -> {
            iVoucherService.deleteByBatchIdList(ids, BatchTypeEnum.TACFL.getCode());
            List<TaReclassificationEntity> tmpEntityList = this.listByIds(ids);
            List<ApproveDTO> approveDTOList = Lists.newArrayList();
            tmpEntityList.stream().forEach(v -> {

                ApproveDTO approveDTO = new ApproveDTO();
                approveDTO.setDocumentId(v.getId());
                approveDTO.setDocumentType(BatchTypeEnum.TACFL.getCode());
                approveDTO.setUrl(approveUrl + v.getId());
                approveDTOList.add(approveDTO);
            });
            // 生成凭证
            Boolean generateVoucherFlag = generateVoucher(ids, YesOrNoEnum.YES.getCode());
            if (generateVoucherFlag) {
                // 发送审核
                Map<Long, Long> processInstantIdMap = iApproveService.submit(approveDTOList);

                List<TaReclassificationEntity> newEntityList = this.listByIds(ids);
                newEntityList.stream().forEach(v -> {
                    v.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
                    if (null != processInstantIdMap && processInstantIdMap.containsKey(v.getId())) {
                        v.setProcessInstanceId(processInstantIdMap.get(v.getId()));
                        v.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
                    }
                });
                // 凭证生成成功
                this.updateBatchById(newEntityList);
            } else {
                // 凭证生成失败
                throw new ServiceException("凭证生成失败或存在无效和借贷金额未平的凭证，无法提交");
            }
        }).whenComplete((v, e) -> {
            // 执行成功，更新任务状态
            iBatchTaskService.updateBatchTask(taskIdList, "2");
        }).exceptionally(e -> {
            log.info("TA重分类提交处理数据失败", e);
            iBatchTaskService.updateBatchTask(taskIdList, "3");
            return null;
        });
        return Boolean.TRUE;
    }

    @Override
    public void updateProcessStatus(CommonApproveDTO approveDTO) {
        if (StringUtils.isEmpty(approveDTO.getDocumentStatus())) {
            throw new ServiceException("处理状态不可以为空");
        }
        TaReclassificationEntity entity = this.getById(approveDTO.getDocumentId());
        if (ObjectUtil.isEmpty(entity)) {
            throw new ServiceException("ta重分类明细表数据不存在");
        }
        List<Long> voucherIds = Arrays.stream(entity.getVoucherId().split(",")).map(Long::parseLong)
                .collect(Collectors.toList());
        // 修改凭证状态，通过和驳回都修改
        updateVoucherStatus(voucherIds, approveDTO);
        // 修改状态
        entity.setProcessStatus(approveDTO.getDocumentStatus());

        this.updateById(entity);
    }

    @Override
    public Boolean syncData(String queryDate) {
        if (StringUtils.isEmpty(queryDate)) {
            throw new ServiceException("同步时间queryDate不能为空");
        }
        queryDate = DateUtil.format(DateUtil.endOfMonth(DateUtil.parse(queryDate, "yyyy-MM")), "yyyy-MM-dd");

        LocalDateTime dateTime = LocalDateTimeUtil.parse(queryDate, "yyyy-MM-dd");
        String reclassificationMonth = LocalDateTimeUtil.format(dateTime, "yyyy-MM");

        LocalDateTime queryDateNextDay = dateTime.plusDays(1);
        String reclassificationMonthNextDay = LocalDateTimeUtil.format(queryDateNextDay, "yyyy-MM-dd");

        List<TaReclassificationEntity> tmpList = this.list(new LambdaQueryWrapper<TaReclassificationEntity>().apply("to_char(reclassification_month,'YYYY-MM') = {0} ", reclassificationMonth));
        if (CollectionUtil.isNotEmpty(tmpList)) {
            throw new ServiceException("该月的业务系统报表已同步:" + reclassificationMonth);
        }
        Map<String, String> orgMap = getOrgNameOrgId();

        List<TaReclassificationDetailEntity> totalList = taReclassificationDetailService.getTaReclassificationDetailList();

        if (CollectionUtil.isEmpty(totalList)) {
            throw new ServiceException("同步到的业务系统报表数量为0");
        }
        for (TaReclassificationDetailEntity taReclassificationDetailEntity : totalList) {
            //小微
            if (SystemEnum.SYCXT.getCode().equals(taReclassificationDetailEntity.getSystemCode()) ||
                    SystemEnum.CYCXT.getCode().equals(taReclassificationDetailEntity.getSystemCode()) ||
                    SystemEnum.XWXT.getCode().equals(taReclassificationDetailEntity.getSystemCode())) {
                if (SystemEnum.XWXT.getCode().equals(taReclassificationDetailEntity.getSystemCode())) {
                    if (StringUtils.isNotEmpty(taReclassificationDetailEntity.getOrgId()) && orgMap.containsKey(taReclassificationDetailEntity.getOrgId())) {
                        taReclassificationDetailEntity.setOrgId(orgMap.get(taReclassificationDetailEntity.getOrgId()));
                    }
                    if (StringUtils.isNotEmpty(taReclassificationDetailEntity.getEbankSerialNumber())) {
                        taReclassificationDetailEntity.setEbankBatchNo(taReclassificationDetailEntity.getEbankSerialNumber());
                    }
                }
                if (StringUtils.isNotEmpty(taReclassificationDetailEntity.getBankOrgId()) && orgMap.containsKey(taReclassificationDetailEntity.getBankOrgId())) {
                    taReclassificationDetailEntity.setBankOrgId(orgMap.get(taReclassificationDetailEntity.getBankOrgId()));
                }
            }
            taReclassificationDetailEntity.setReclassificationMonth(dateTime);
        }

        TaReclassificationEntity summary = new TaReclassificationEntity();
        summary.setReclassificationMonth(dateTime);
        summary.setProcessStatus(MarginStatusEnum.ENTERED.getCode());
        summary.setIsGenerateVoucher(YesOrNoEnum.NO.getCode());
        summary.setAccountDate(dateTime);
        this.save(summary);
        log.info("TA重分类 汇总数据 保存完成");

        log.info("TA重分类 详细数据 保存 开始");
        taReclassificationDetailService.saveBatch(totalList);
        log.info("TA重分类 详细数据 保存 完成，保存数量:{}", totalList.size());

        List<TaReclassificationDetailEntity> typtList = taReclassificationDetailService.selectTYPTInfo(queryDate, reclassificationMonthNextDay, SystemEnum.TYPT.getCode());
        if (CollectionUtil.isNotEmpty(typtList)) {
            log.info("TA重分类 特殊处理统一平台数据 开始");
            typtList.forEach(i -> {
                if (StringUtils.isNotEmpty(i.getBankOrgId()) && orgMap.containsKey(i.getBankOrgId())) {
                    i.setBankOrgId(orgMap.get(i.getBankOrgId()));
                }
            });
            taReclassificationDetailService.updateBatchById(typtList);
            log.info("TA重分类 特殊处理统一平台数据 结束，处理数据量：{}", typtList.size());
        }

        String queryDateStr = queryDate + " 00:00:00.000";

        log.info("TA重分类 加载明细数据字段 开始");
        List<TaReclassificationDetailEntity> entityList = taReclassificationDetailService.selectDetailDataAndContractInfo(queryDate);
        log.info("TA重分类 加载明细数据字段 结束，加载数量：{}", entityList.stream());
        if (CollectionUtil.isNotEmpty(entityList)) {
            log.info("TA重分类 更新明细数据字段 开始");
            entityList.forEach(i -> i.setTaReclassificationId(summary.getId()));
            taReclassificationDetailService.updateBatchById(entityList);
            log.info("TA重分类 更新明细数据字段 结束，更新数量：{}", entityList.size());
        }


        BigDecimal taReclassificationAmount = entityList.stream().map(TaReclassificationDetailEntity::getTaReclassificationAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        summary.setTaReclassificationAmount(taReclassificationAmount);
        this.updateById(summary);
        log.info("TA重分类 刷新汇总数据重分类金额 完成，任务结束");
        return true;
    }

    private Map<String, String> getOrgNameOrgId() {
        Map<String, String> orgNameAndIdMap = Maps.newHashMap();

        LambdaQueryWrapper<OrgCompanyEntity> queryWrapper = Wrappers.<OrgCompanyEntity>lambdaQuery();
        List<OrgCompanyEntity> orgCompanyEntityList = orgCompanyService.list(queryWrapper).stream().distinct().collect(Collectors.toList());
        List<OrgCompanyVO> orgCompanyVOList = ListBeanUtil.copyList(orgCompanyEntityList, OrgCompanyVO.class);
        if (com.alibaba.nacos.common.utils.CollectionUtils.isNotEmpty(orgCompanyVOList)) {
            orgNameAndIdMap = orgCompanyVOList.stream().collect(Collectors.toMap(OrgCompanyVO::getOrgName, OrgCompanyVO::getOrgId, (k1, k2) -> k2));
        }
        return orgNameAndIdMap;
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

