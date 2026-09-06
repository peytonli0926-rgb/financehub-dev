package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.engine.approve.model.dto.ApproveDTO;
import com.utfinancing.financehub.engine.approve.service.IApproveService;
import com.utfinancing.financehub.engine.enums.ProcessStatusEnum;
import com.utfinancing.financehub.engine.enums.SceneEnum;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.entity.ContractEntity;
import com.utfinancing.financehub.engine.finance.entity.ContractStatusRecordEntity;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferOtherEntity;
import com.utfinancing.financehub.engine.finance.entity.OrgCompanyEntity;
import com.utfinancing.financehub.engine.finance.mapper.ContractMapper;
import com.utfinancing.financehub.engine.finance.mapper.ConvertTransferOtherMapper;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferOtherDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferOtherPlanQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferOtherQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ContractVO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferOtherExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferOtherPlanVO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferOtherVO;
import com.utfinancing.financehub.engine.finance.model.vo.OrgCompanyVO;
import com.utfinancing.financehub.engine.finance.service.IContractService;
import com.utfinancing.financehub.engine.finance.service.IContractStatusRecordService;
import com.utfinancing.financehub.engine.finance.service.IConvertTransferOtherService;
import com.utfinancing.financehub.engine.finance.service.IOrgCompanyService;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class ConvertTransferOtherServiceImpl extends ServiceImpl<ConvertTransferOtherMapper, ConvertTransferOtherEntity> implements IConvertTransferOtherService {

    private final IContractService contractService;
    private final IOrgCompanyService orgCompanyService;
    private final IApproveService approveService;
    private final IContractStatusRecordService contractStatusRecordService;

    public ConvertTransferOtherServiceImpl(IContractService contractService, IOrgCompanyService orgCompanyService, IApproveService approveService, IContractStatusRecordService contractStatusRecordService) {
        this.contractService = contractService;
        this.orgCompanyService = orgCompanyService;
        this.approveService = approveService;
        this.contractStatusRecordService = contractStatusRecordService;
    }

    @Override
    @Transactional
    public void importExcel(List<ConvertTransferOtherDTO> dtos) {
        List<ConvertTransferOtherEntity> entities = new ArrayList<>();
        Set<String> orgNames = new HashSet<>();
        for (ConvertTransferOtherDTO dto : dtos) {
            entities.add(dto.toEntity());
            orgNames.add(dto.getTransferParty());
            orgNames.add(dto.getTransfereeParty());
        }

        Map<String, String> orgNameIdMap = orgCompanyService.lambdaQuery()
                .in(OrgCompanyEntity::getOrgName, orgNames)
                .select(Arrays.asList(OrgCompanyEntity::getOrgName, OrgCompanyEntity::getOrgId))
                .list()
                .stream()
                .collect(Collectors.toMap(OrgCompanyEntity::getOrgName, OrgCompanyEntity::getOrgId));
        Map<String, String> orgIdNameMap = new HashMap<>();
        orgNameIdMap.forEach((k, v) -> orgIdNameMap.put(v, k));
        Map<String, Set<String>> orgContractMap = new TreeMap<>();
        for (ConvertTransferOtherEntity entity : entities) {
            String transferParty = entity.getTransferParty();
            String orgId = orgNameIdMap.get(transferParty);
            if (orgId == null) {
                throw new ServiceException("转让方【】未找到");
            }
            entity.setTransferParty(orgId);
            orgContractMap.compute(orgId, (o, n) -> n == null ? new HashSet<>() : n)
                    .add(entity.getContractCode());

            orgId = orgNameIdMap.get(entity.getTransfereeParty());
            if (orgId == null) {
                throw new ServiceException("受让方【" + entity.getTransfereeParty() + "】未找到");
            }
            entity.setTransfereeParty(orgId);
        }

        List<ContractEntity> contracts = ((ContractMapper) contractService.getBaseMapper()).listClientInfoByOrgContractCodeMap(orgContractMap);

        for (ConvertTransferOtherEntity entity : entities) {
            String transferParty = entity.getTransferParty();
            String contractCode = entity.getContractCode();
            for (ContractEntity contract : contracts) {
                if (contract.getContractCode().equals(contractCode)
                        && transferParty.equals(contract.getOrgId())) {
                    entity.setClientCode(contract.getClientCode());
                    entity.setClientName(contract.getClientName());
                    entity.setContractName(contract.getContractName());
                    break;
                }
            }
            if (Objects.isNull(entity.getClientCode())) {
                throw new ServiceException("通过转让方【" + orgIdNameMap.get(entity.getTransferParty()) + "】，合同编号【" + entity.getContractCode() + "】未查询到客户信息");
            }
        }

        saveBatch(entities);
    }

    @Override
    @Transactional
    public Boolean updateProcessStatus(CommonApproveDTO approveDTO) {
        if (ProcessStatusEnum.REJECTED.getCode().equals(approveDTO.getDocumentStatus())) {
            clearContractStatusRecord(Collections.singletonList(approveDTO.getDocumentId()));
        }

        return lambdaUpdate()
                .set(ConvertTransferOtherEntity::getProcessStatus, approveDTO.getDocumentStatus())
                .eq(ConvertTransferOtherEntity::getId, approveDTO.getDocumentId())
                .update(new ConvertTransferOtherEntity());
    }

    @Override
    @Transactional
    public Boolean submit(List<Long> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            throw new ServiceException("请选择一行记录");
        }
        List<ApproveDTO> dtos = new ArrayList<>();
        List<ConvertTransferOtherEntity> entities = listByIds(ids);
        Map<String, List<ContractVO>> contractMap = new HashMap<>();
        List<String> contractCodeList = new ArrayList<>();
        for (ConvertTransferOtherEntity entity : entities) {
            if (!(Objects.equals(entity.getProcessStatus(), ProcessStatusEnum.ENTERED.getCode())
                    || Objects.equals(entity.getProcessStatus(), ProcessStatusEnum.REJECTED.getCode()))) {
                throw new ServiceException("只有已录入/已拒绝的状态可以提交");
            }
            String orgId = entity.getTransferParty();
            ApproveDTO dto = new ApproveDTO();
            dto.setDocumentId(entity.getId());
            dto.setDocumentType(SceneEnum.QTZR.getCode());
            dto.setUrl("");
            dtos.add(dto);
            ContractVO vo = new ContractVO();
            vo.setContractCode(entity.getContractCode());
            vo.setContractName(entity.getContractName());
            vo.setClientCode(entity.getClientCode());
            vo.setClientName(entity.getClientName());
            vo.setOrgId(orgId);
            vo.setContractStatus(entity.getFinancialContractStatus());
            vo.setFinancialContractStatusUpdateTime(DateUtils.toDate(entity.getAccountDate()));
            vo.setFinancialContractStatus(entity.getFinancialContractStatus());
            vo.setSourceFromId(entity.getId());
            vo.setSourceFromType(dto.getDocumentType());
            contractMap.compute(orgId, (key, v) -> v == null ? new ArrayList<>() : v)
                    .add(vo);
            contractCodeList.add(entity.getContractCode());
        }

        Map<Long, Long> submit = approveService.submit(dtos);

        for (ConvertTransferOtherEntity entity : entities) {
            entity.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
            entity.setProcessInstanceId(submit.get(entity.getId()));
        }

        updateBatchById(entities);

        List<ContractEntity> contracts = contractService.lambdaQuery()
                .in(ContractEntity::getContractCode, contractCodeList)
                .list();
        // 更新合同
        List<ContractEntity> updatedContracts = new ArrayList<>();
        for (ConvertTransferOtherEntity entity : entities) {
            for (ContractEntity contract : contracts) {
                if (contract.getContractCode().equals(entity.getContractCode())
                        && entity.getTransferParty().equals(contract.getOrgId())
                        && entity.getClientCode().equals(contract.getClientCode())) {
                    contract.setFinancialContractStatus(entity.getFinancialContractStatus());
                    contract.setFinancialContractStatusUpdateTime(DateUtils.toDate(entity.getAccountDate()));
                    updatedContracts.add(contract);
                }
            }
        }

        contractService.updateBatchById(updatedContracts);
        // 保存特殊合同状态
        contractMap.values().forEach(contractService::saveRecordList);

        return Boolean.TRUE;
    }

    @Override
    @Transactional
    public Boolean withdraw(List<Long> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            throw new ServiceException("请选择一行记录");
        }
        List<Long> processIds = new ArrayList<>();
        for (ConvertTransferOtherEntity entity : lambdaQuery()
                .in(ConvertTransferOtherEntity::getId, ids)
                .select(Arrays.asList(ConvertTransferOtherEntity::getProcessStatus, ConvertTransferOtherEntity::getProcessInstanceId))
                .list()) {
            if (!Objects.equals(entity.getProcessStatus(), ProcessStatusEnum.SUBMITTED.getCode())) {
                throw new ServiceException("只有已提交的状态才能撤回");
            }
            processIds.add(entity.getProcessInstanceId());
        }
        approveService.withdraw(processIds);
        lambdaUpdate()
                .set(ConvertTransferOtherEntity::getProcessStatus, ProcessStatusEnum.ENTERED.getCode())
                .in(ConvertTransferOtherEntity::getId, ids)
                .update(new ConvertTransferOtherEntity());
        clearContractStatusRecord(ids);

        return true;
    }

    @Override
    @Transactional
    public Boolean delete(List<Long> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            throw new ServiceException("请选择一行记录");
        }
        Long count = lambdaQuery()
                .in(ConvertTransferOtherEntity::getId, ids)
                .in(ConvertTransferOtherEntity::getProcessStatus, ProcessStatusEnum.ENTERED.getCode(), ProcessStatusEnum.REJECTED.getCode())
                .count();
        if (count != ids.size()) {
            throw new ServiceException("只有已录入/已拒绝的状态可以删除");
        }
        removeBatchByIds(ids);
        return Boolean.TRUE;
    }

    @Override
    public IPage<ConvertTransferOtherPlanVO> planPage(ConvertTransferOtherPlanQueryDTO dto) {
        LambdaQueryChainWrapper<ConvertTransferOtherEntity> query = lambdaQuery()
                .in(CollectionUtil.isNotEmpty(dto.getContractCodes()), ConvertTransferOtherEntity::getContractCode, dto.getContractCodes());
        List<ConvertTransferOtherEntity> entities = query.select(Arrays.asList(ConvertTransferOtherEntity::getContractCode, ConvertTransferOtherEntity::getTransferParty, ConvertTransferOtherEntity::getInvoiceFlag))
                .list();
        Map<String, Set<String>> orgContractMap = entities
                .stream()
                .collect(Collectors.groupingBy(ConvertTransferOtherEntity::getTransferParty, Collectors.mapping(ConvertTransferOtherEntity::getContractCode, Collectors.toSet())));
        Map<String, Set<String>> invoceOrgMap = entities.stream()
                .filter(entity -> YesOrNoEnum.YES.getCode().equals(entity.getInvoiceFlag()))
                .collect(Collectors.groupingBy(ConvertTransferOtherEntity::getTransferParty, Collectors.mapping(ConvertTransferOtherEntity::getContractCode, Collectors.toSet())));

        if (orgContractMap.isEmpty()) {
            return new Page<>();
        }
        Page<Object> page = dto.getPageNum() != -1 ?
                Page.of(dto.getPageNum(), dto.getPageSize())
                : Page.of(1, Long.MAX_VALUE, false);
        if (invoceOrgMap.isEmpty()) {
            return getBaseMapper().planPage(page, orgContractMap, dto.getStartDate(), dto.getEndDate());
        } else {
            return getBaseMapper().planWithInvoicePage(page, orgContractMap, invoceOrgMap, dto.getStartDate(), dto.getEndDate());
        }
    }

    @Override
    public IPage<ConvertTransferOtherVO> pageQuery(ConvertTransferOtherQueryDTO dto) {
        IPage<ConvertTransferOtherVO> page = lambdaQuery()
                .like(StringUtils.isNotEmpty(dto.getContractCode()), ConvertTransferOtherEntity::getContractCode, dto.getContractCode())
                .in(CollectionUtil.isNotEmpty(dto.getTransferPartyList()), ConvertTransferOtherEntity::getTransferParty, dto.getTransferPartyList())
                .in(CollectionUtil.isNotEmpty(dto.getTransfereePartyList()), ConvertTransferOtherEntity::getTransfereeParty, dto.getTransfereePartyList())
                .page(new Page<>(dto.getPageNum(), dto.getPageSize()))
                .convert(ConvertTransferOtherVO::new);
        Map<String, String> orgMap = orgCompanyService.selectAllOrgIdAndName()
                .stream()
                .collect(Collectors.toMap(OrgCompanyVO::getOrgId, OrgCompanyVO::getOrgName, (left, right) -> right));
        for (ConvertTransferOtherVO record : page.getRecords()) {
            record.setTransferParty(orgMap.getOrDefault(record.getTransferParty(), record.getTransferParty()));
            record.setTransfereeParty(orgMap.getOrDefault(record.getTransfereeParty(), record.getTransfereeParty()));
        }

        return page;
    }

    @Override
    public List<ConvertTransferOtherExcelVO> listQuery(ConvertTransferOtherQueryDTO dto) {
        List<ConvertTransferOtherExcelVO> list = lambdaQuery()
                .like(StringUtils.isNotEmpty(dto.getContractCode()), ConvertTransferOtherEntity::getContractCode, dto.getContractCode())
                .in(CollectionUtil.isNotEmpty(dto.getTransferPartyList()), ConvertTransferOtherEntity::getTransferParty, dto.getTransferPartyList())
                .in(CollectionUtil.isNotEmpty(dto.getTransfereePartyList()), ConvertTransferOtherEntity::getTransfereeParty, dto.getTransfereePartyList())
                .list()
                .stream()
                .map(ConvertTransferOtherExcelVO::new)
                .collect(Collectors.toList());
        Map<String, String> orgMap = orgCompanyService.selectAllOrgIdAndName()
                .stream()
                .collect(Collectors.toMap(OrgCompanyVO::getOrgId, OrgCompanyVO::getOrgName, (left, right) -> right));
        for (ConvertTransferOtherExcelVO record : list) {
            record.setTransferParty(orgMap.getOrDefault(record.getTransferParty(), record.getTransferParty()));
            record.setTransfereeParty(orgMap.getOrDefault(record.getTransfereeParty(), record.getTransfereeParty()));
        }

        return list;

    }


    private void clearContractStatusRecord(List<Long> transferIdList) {
        contractStatusRecordService.lambdaUpdate()
                .in(ContractStatusRecordEntity::getSourceFromId, transferIdList)
                .eq(ContractStatusRecordEntity::getSourceFromType, SceneEnum.QTZR.getCode())
                .remove();
    }
}
