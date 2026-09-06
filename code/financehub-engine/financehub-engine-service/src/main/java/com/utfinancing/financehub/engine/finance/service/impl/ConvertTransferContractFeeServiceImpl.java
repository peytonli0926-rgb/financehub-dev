package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.admin.api.RemoteDictService;
import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.engine.approve.model.dto.ApproveDTO;
import com.utfinancing.financehub.engine.approve.service.IApproveService;
import com.utfinancing.financehub.engine.enums.DictTypeEnum;
import com.utfinancing.financehub.engine.enums.ProcessStatusEnum;
import com.utfinancing.financehub.engine.enums.SceneEnum;
import com.utfinancing.financehub.engine.enums.SystemEnum;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.entity.ContractEntity;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferContractFeeDetailEntity;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferContractFeeEntity;
import com.utfinancing.financehub.engine.finance.entity.OrgCompanyEntity;
import com.utfinancing.financehub.engine.finance.mapper.ConvertTransferContractFeeMapper;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferContractFeeDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferContractFeeQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferContractFeeExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferContractFeeVO;
import com.utfinancing.financehub.engine.finance.model.vo.OrgCompanyVO;
import com.utfinancing.financehub.engine.finance.service.IContractService;
import com.utfinancing.financehub.engine.finance.service.IConvertTransferContractFeeDetailService;
import com.utfinancing.financehub.engine.finance.service.IConvertTransferContractFeeService;
import com.utfinancing.financehub.engine.finance.service.IOrgCompanyService;
import com.utfinancing.financehub.engine.finance.service.IVoucherService;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import com.utfinancing.financehub.engine.rule.model.dto.ExecuteCommonDTO;
import com.utfinancing.financehub.engine.rule.model.vo.VoucherInfoVO;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
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

@Service
public class ConvertTransferContractFeeServiceImpl extends ServiceImpl<ConvertTransferContractFeeMapper, ConvertTransferContractFeeEntity> implements IConvertTransferContractFeeService {

    private final IContractService contractService;
    private final IOrgCompanyService orgCompanyService;
    private final IConvertTransferContractFeeDetailService contractFeeDetailService;
    private final IApproveService approveService;
    private final IRuleService ruleService;
    private final IVoucherService voucherService;
    private final RemoteDictService remoteDictService;
    private final AsyncTaskExecutor asyncTaskExecutor;
    private final Logger logger = LoggerFactory.getLogger(ConvertTransferContractFeeServiceImpl.class);


    @Value("${approve.url.convert-transfer-contract-fee-url:null}")
    private String approveUrl;


    public ConvertTransferContractFeeServiceImpl(IContractService contractService, IOrgCompanyService orgCompanyService, IConvertTransferContractFeeDetailService contractFeeDetailService, IApproveService approveService, IRuleService ruleService, IVoucherService voucherService, RemoteDictService remoteDictService, AsyncTaskExecutor asyncTaskExecutor) {
        this.contractService = contractService;
        this.orgCompanyService = orgCompanyService;
        this.contractFeeDetailService = contractFeeDetailService;
        this.approveService = approveService;
        this.ruleService = ruleService;
        this.voucherService = voucherService;
        this.remoteDictService = remoteDictService;
        this.asyncTaskExecutor = asyncTaskExecutor;
    }

    @Override
    public IPage<ConvertTransferContractFeeVO> pageQuery(ConvertTransferContractFeeQueryDTO dto) {
        IPage<ConvertTransferContractFeeVO> page = lambdaQuery()
                .eq(Objects.nonNull(dto.getAccountDate()), ConvertTransferContractFeeEntity::getAccountDate, dto.getAccountDate())
                .in(CollectionUtil.isNotEmpty(dto.getOrgIdList()), ConvertTransferContractFeeEntity::getOrgId, dto.getOrgIdList())
                .page(new Page<>(dto.getPageNum(), dto.getPageSize()))
                .convert(ConvertTransferContractFeeVO::new);
        Map<String, String> orgMap = orgCompanyService.lambdaQuery()
                .list()
                .stream()
                .collect(Collectors.toMap(OrgCompanyEntity::getOrgId, OrgCompanyEntity::getOrgName, (left, right) -> right));
        for (ConvertTransferContractFeeVO vo : page.getRecords()) {
            vo.setOrgId(orgMap.get(vo.getOrgId()));
        }

        return page;
    }

    @Override
    public List<ConvertTransferContractFeeExcelVO> export(LocalDate accountDate, List<String> orgIdList) {
        List<ConvertTransferContractFeeEntity> entities = lambdaQuery()
                .eq(Objects.nonNull(accountDate), ConvertTransferContractFeeEntity::getAccountDate, accountDate)
                .in(CollectionUtil.isNotEmpty(orgIdList), ConvertTransferContractFeeEntity::getOrgId, orgIdList)
                .list();
        Set<String> orgIds = new HashSet<>();
        List<ConvertTransferContractFeeExcelVO> vos = new ArrayList<>();
        for (ConvertTransferContractFeeEntity entity : entities) {
            vos.add(new ConvertTransferContractFeeExcelVO(entity));
            orgIds.add(entity.getOrgId());
        }
        Map<String, String> orgMap = orgCompanyService.lambdaQuery()
                .in(OrgCompanyEntity::getOrgId, orgIds)
                .list()
                .stream()
                .collect(Collectors.toMap(OrgCompanyEntity::getOrgId, OrgCompanyEntity::getOrgName, (left, right) -> right));
        for (ConvertTransferContractFeeExcelVO vo : vos) {
            vo.setOrgId(orgMap.get(vo.getOrgId()));
        }

        return vos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean importFile(List<ConvertTransferContractFeeDTO> dtos) {
        Set<String> orgIds = new HashSet<>();
        R<List<SysDictData>> chargeTypeResult = remoteDictService.listDictData("charge_type");
        Map<String,String> dictLabelValue = Optional.ofNullable(chargeTypeResult.getData()).orElse(Collections.emptyList())
                .stream()
                .collect(Collectors.toMap(SysDictData::getDictLabel, SysDictData::getDictValue));
        if (dictLabelValue.isEmpty()) {
            throw new ServiceException("费用类型查询为空");
        }
        Set<String> contractCodes = new HashSet<>();
        List<ConvertTransferContractFeeDetailEntity> details = new ArrayList<>();
        for (ConvertTransferContractFeeDTO dto : dtos) {
            ConvertTransferContractFeeDetailEntity entity = dto.toDetailEntity();
            orgIds.add(entity.getOrgId());
            contractCodes.add(entity.getContractCode());
            if (!dictLabelValue.containsKey(entity.getTransferFeeType())) {
                throw new ServiceException("费用类型【" + entity.getTransferFeeType() + "】异常");
            }
            entity.setTransferFeeType(dictLabelValue.get(entity.getTransferFeeType()));
            details.add(entity);
        }
        List<OrgCompanyEntity> orgCompanyVOS = orgCompanyService.lambdaQuery()
                .in(OrgCompanyEntity::getOrgName, orgIds)
                .select(Arrays.asList(OrgCompanyEntity::getOrgId, OrgCompanyEntity::getOrgName))
                .list();

        Map<String, ContractEntity> contractEntityMap = contractService.lambdaQuery()
                .in(ContractEntity::getContractCode, contractCodes)
                .list()
                .stream()
                .collect(Collectors.toMap(ContractEntity::getContractCode, Function.identity(), (left, right) -> right));

        for (ConvertTransferContractFeeDetailEntity detail : details) {
            String orgId = detail.getOrgId();
            for (OrgCompanyEntity vo : orgCompanyVOS) {
                if (vo.getOrgName().equals(orgId)) {
                    detail.setOrgId(vo.getOrgId());
                    break;
                }
            }
            if (orgId.equals(detail.getOrgId())) {
                throw new ServiceException("签约主体[" + orgId + "]未找到");
            }
            ContractEntity contract = Optional.ofNullable(contractEntityMap.get(detail.getContractCode())).orElseThrow(() -> new ServiceException("合同编号[" + detail.getContractCode() + "]未找到"));
            if (Objects.isNull(contract.getBusinessCode())) {
                throw new ServiceException("合同业务类型编码为空，不能转让");
            }

        }
        Collection<List<ConvertTransferContractFeeDetailEntity>> groupDetails = details.stream()
                .collect(Collectors.groupingBy(detail -> detail.getOrgId() + "_" + detail.getAccountDate()))
                .values();
        for (List<ConvertTransferContractFeeDetailEntity> entities : groupDetails) {
            ConvertTransferContractFeeDetailEntity detail = entities.get(0);
            ConvertTransferContractFeeEntity feeEntity = new ConvertTransferContractFeeEntity();
            feeEntity.setUploadDate(detail.getUploadDate());
            feeEntity.setAccountDate(detail.getAccountDate());
            feeEntity.setOrgId(detail.getOrgId());
            BigDecimal transferFeeAmount = entities.stream()
                    .map(ConvertTransferContractFeeDetailEntity::getTransferFee)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            feeEntity.setTransferFeeAmount(transferFeeAmount);
            feeEntity.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
            save(feeEntity);
            for (ConvertTransferContractFeeDetailEntity entity : entities) {
                entity.setTransferId(feeEntity.getId());
            }
            contractFeeDetailService.saveBatch(entities);
        }

        return true;
    }

    @Override
    @Transactional
    public Boolean generateVoucher(List<Long> ids) {
        return generateVoucher(ids, YesOrNoEnum.NO);
    }

    @Override
    @Transactional
    public Boolean submit(List<Long> ids) {
        return generateVoucher(ids, YesOrNoEnum.YES);
    }

    private boolean generateVoucher(List<Long> ids, YesOrNoEnum type) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请选中一行");
        }
        List<ConvertTransferContractFeeEntity> entities = listByIds(ids);
        for (ConvertTransferContractFeeEntity entity : entities) {
            if (!Objects.equals(ProcessStatusEnum.ENTERED.getCode(), entity.getProcessStatus())
                    && !Objects.equals(ProcessStatusEnum.REJECTED.getCode(), entity.getProcessStatus())) {
                throw new ServiceException("只有拒绝/已录入状态下可以提交/生成凭证");
            }
        }
        List<ConvertTransferContractFeeDetailEntity> details = contractFeeDetailService.lambdaQuery()
                .in(ConvertTransferContractFeeDetailEntity::getTransferId, ids)
                .list();
        Date date = new Date();
        List<Map<String, Object>> vourcerDateList = new ArrayList<>();
        Map<String, ConvertTransferContractFeeDetailEntity> orderIdMap = new HashMap<>();
        for (ConvertTransferContractFeeDetailEntity detail : details) {
            String orderId = detail.getId().toString();
            orderIdMap.put(orderId, detail);
            ExecuteCommonDTO executeCommonDTO = new ExecuteCommonDTO();
            executeCommonDTO.setSystemCode(SystemEnum.CWZT.getCode());
            executeCommonDTO.setSystemName(SystemEnum.CWZT.getDesc());
            executeCommonDTO.setSceneCode(SceneEnum.ZRFY.getCode());
            executeCommonDTO.setSceneName(SceneEnum.ZRFY.getDesc());
            executeCommonDTO.setBatchId(detail.getTransferId());
            executeCommonDTO.setBatchType(SceneEnum.ZRFY.getCode());
            executeCommonDTO.setOrderId(orderId);
            executeCommonDTO.setAccountDate(date);
            executeCommonDTO.setIsSubmit(type.getCode());

            executeCommonDTO.setBusinessDate(DateUtils.toDate(detail.getAccountDate()));
            executeCommonDTO.setContractCode(detail.getContractCode());

            Map<String, Object> dataMap = BeanUtil.beanToMap(executeCommonDTO);
            dataMap.put("chargeType", detail.getTransferFeeType());
            dataMap.put("chargeAmount", detail.getTransferFee());
            vourcerDateList.add(dataMap);
        }

        List<VoucherInfoVO> voucherResultList = ruleService.batchExecuteRule(vourcerDateList);


        // 查验数据结果
        boolean isExistVoucherError = voucherResultList.stream().allMatch(v -> StringUtils.isNotEmpty(v.getErrorInfo()));
        if (isExistVoucherError) {
            List<Long> voucherIdList = Lists.newArrayList();
            for (VoucherInfoVO voucherInfoVO : voucherResultList) {
                if (CollectionUtils.isNotEmpty(voucherInfoVO.getVoucherDTOList())) {
                    voucherInfoVO.getVoucherDTOList().stream().map(VoucherDTO::getId).forEach(voucherIdList::add);
                }
            }
            // 异步删除已生成的凭证
            if (CollectionUtils.isNotEmpty(voucherIdList))
                CompletableFuture.runAsync(() -> voucherService.deleteByIdList(voucherIdList), asyncTaskExecutor);
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

        for (VoucherInfoVO voucherInfo : voucherResultList) {
            ConvertTransferContractFeeDetailEntity detail = orderIdMap.get(voucherInfo.getOrderId());
            String voucherId = voucherInfo.getVoucherDTOList()
                    .stream()
                    .map(VoucherDTO::getId)
                    .filter(Objects::nonNull)
                    .map(Objects::toString)
                    .collect(Collectors.joining(","));
            detail.setVoucherId(voucherId);
        }
        Map<Long, String> transferIdVoucherId = details.stream()
                .collect(Collectors.groupingBy(
                                ConvertTransferContractFeeDetailEntity::getTransferId,
                                Collectors.mapping(
                                        ConvertTransferContractFeeDetailEntity::getVoucherId,
                                        Collectors.reducing("", (l, r) -> r == null ? l : (l.isEmpty() ? r : l + "," + r))
                                )
                        )
                );

        List<ApproveDTO> dtos = new ArrayList<>();
        for (ConvertTransferContractFeeEntity entity : entities) {
            String voucherId = transferIdVoucherId.get(entity.getId());
            if (StringUtils.isNotEmpty(voucherId)) {
                entity.setVoucherId(voucherId);
                if (YesOrNoEnum.YES == type) {
                    ApproveDTO dto = new ApproveDTO();
                    dto.setDocumentId(entity.getId());
                    dto.setDocumentType(SceneEnum.ZRFY.getCode());
                    dto.setUrl(approveUrl + entity.getId());
                    dtos.add(dto);
                }
            }
        }

        if (CollectionUtils.isNotEmpty(dtos)) {
            Map<Long, Long> submit = approveService.submit(dtos);
            for (ConvertTransferContractFeeEntity entity : entities) {
                entity.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
                entity.setProcessInstanceId(submit.get(entity.getId()));
            }
        }

        updateBatchById(entities);
        contractFeeDetailService.updateBatchById(details);

        return true;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean withdraw(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)){
            throw new ServiceException("请选中一行");
        }
        List<ConvertTransferContractFeeEntity> entities = listByIds(ids);
        List<Long> processIdList = new ArrayList<>();
        for (ConvertTransferContractFeeEntity entity : entities) {
            if (!Objects.equals(ProcessStatusEnum.SUBMITTED.getCode(), entity.getProcessStatus())) {
                throw new ServiceException("只有提交状态下可以撤回");
            }
            processIdList.add(entity.getProcessInstanceId());
        }

        approveService.withdraw(processIdList);

        voucherService.deleteByBatchIdList(ids, SceneEnum.ZRFY.getCode());
        lambdaUpdate()
                .set(ConvertTransferContractFeeEntity::getProcessStatus, ProcessStatusEnum.ENTERED.getCode())
                .set(ConvertTransferContractFeeEntity::getProcessInstanceId, null)
                .in(ConvertTransferContractFeeEntity::getId, ids)
                .update(new ConvertTransferContractFeeEntity());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)){
            throw new ServiceException("请选中一行");
        }
        for (ConvertTransferContractFeeEntity entity : listByIds(ids)) {
            if (!Objects.equals(ProcessStatusEnum.ENTERED.getCode(), entity.getProcessStatus())
                    && !Objects.equals(ProcessStatusEnum.REJECTED.getCode(), entity.getProcessStatus())) {
                throw new ServiceException("只有拒绝/已录入状态下可以删除");
            }
        }
        removeByIds(ids);
        return true;
    }

    @Override
    @Transactional
    public void updateProcessStatus(CommonApproveDTO dto) {
        Long documentId = dto.getDocumentId();
        String documentType = dto.getDocumentType();
        String status = dto.getDocumentStatus();
        if (SceneEnum.ZRFY.getCode().equals(documentType)) {
            ConvertTransferContractFeeEntity entity = getById(documentId);
            String voucherId = entity.getVoucherId();
            voucherService.updateStatusByids(Arrays.asList(voucherId.split(",")), status, dto.getApproverNum(), dto.getApproverName());

            entity.setProcessStatus(status);
            entity.setErrorInfo(dto.getRemark());
            updateById(entity);

        } else {
            logger.error("资产转让 - 转让合同费 消费到其他场景审批消息：{}", JSON.toJSONString(dto));
        }
    }
}
