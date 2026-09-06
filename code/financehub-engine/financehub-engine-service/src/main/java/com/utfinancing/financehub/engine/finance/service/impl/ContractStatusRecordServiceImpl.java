package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.xiaoymin.knife4j.core.util.CommonUtils;
import com.google.common.collect.Maps;
import com.utfinancing.financehub.admin.api.RemoteDictService;
import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.common.core.constant.HttpStatus;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.utfinancing.financehub.common.security.utils.SecurityUtils;
import com.utfinancing.financehub.engine.approve.model.dto.ApproveDTO;
import com.utfinancing.financehub.engine.approve.service.IApproveService;
import com.utfinancing.financehub.engine.contractstatusupdate.service.IContractStatusUpdateService;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.mapper.ContractMapper;
import com.utfinancing.financehub.engine.finance.mapper.ContractStatusRecordMapper;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.ContractStatusRecordVO;
import com.utfinancing.financehub.engine.finance.model.vo.ReportLeaseTableExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.ReportLeaseTableVO;
import com.utfinancing.financehub.engine.finance.service.*;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import com.utfinancing.financehub.engine.utils.CommonDateUtils;
import com.utfinancing.financehub.engine.utils.ContractStatusUpdateUtils;
import com.utfinancing.financehub.engine.utils.UserUtils;
import com.utfinancing.financehub.engine.verification.entity.VerificationDetailsEntity;
import com.utfinancing.financehub.engine.verification.entity.VerificationEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-09
 * @Description :  ContractStatusRecord服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class ContractStatusRecordServiceImpl extends ServiceImpl<ContractStatusRecordMapper, ContractStatusRecordEntity> implements IContractStatusRecordService {

    private final ContractStatusRecordMapper contractStatusRecordMapper;
    private final ContractMapper contractMapper;
    private final IContractService contractService;
    private final RemoteDictService remoteDictService;
    private final IOrgCompanyService orgCompanyService;

    private final IContractStatusRecordTempService contractStatusRecordTempService;

    //服务器真实文件路径
//    private final String FILE_PATH_LOCAL = "/home/admin/financehub/service/financehub-engine/export/";
    //nginx配置路径
    private final IFileRecordService fileRecordService;

    @Value("${service.parth:null}")
    private String servicePath;

    @Autowired
    @Qualifier("asyncTaskExecutor")
    private ThreadPoolTaskExecutor asyncTaskExecutor;

    @Value("${approve.url.specialContract-url:null}")
    private String approveUrl;

    private final IApproveService iApproveService;

    @Value("${file.storage.basicpath.windows:null}")
    private String basicPathWindows;

    @Value("${file.storage.basicpath.linux:null}")
    private String basicPathLinux;

//    @Resource
//    private IContractService iContractService;
//    @Resource
//    private IContractMonthService iContractMonthService;
    @Resource
    private IContractStatusUpdateService contractStatusUpdateService;

    @Override
    public Long saveContractStatusRecord(ContractStatusRecordSaveDTO dto) {
        // todo 其他功能有特殊合同状态的，审批过后，这里需要新增数据 后续各种情况-资产转让-内部转让
        ContractStatusRecordEntity entity = BeanUtil.copyProperties(dto, ContractStatusRecordEntity.class);
        //校验合同信息是否存在，是否正确
        ContractDTO contract = contractService.getContractDTOByCode(dto.getContractCode(), dto.getOrgId());
        if (null == contract) {
            throw new ServiceException("合同信息不存在");
        }
        if (!StringUtils.equals(contract.getOrgId(), dto.getOrgId())) {
            throw new ServiceException("合同与签约主体关系错误");
        }
        entity.setContractName(contract.getContractName());
        entity.setClientCode(contract.getClientCode());
        entity.setClientName(contract.getClientName());
        entity.setContractStatus(contract.getContractStatus());
        entity.setOperator(SecurityUtils.getUsername());
        entity.setRecordStatus(ContractStatusRecordStatus.ENTERED.getCode());
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateContractStatusRecord(Long id, ContractStatusRecordSaveDTO dto) {
        ContractStatusRecordEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public ContractStatusRecordDTO getContractStatusRecordDTOById(Long id) {
        ContractStatusRecordEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, ContractStatusRecordDTO.class);
    }

    /**
     * @description:特殊合同状态-列表-查询
     **/
    @Override
    public IPage<ContractStatusRecordVO> selectPage(ContractStatusRecordQueryDTO queryDTO) {
        // 为空默认查询当前时间最新数据
        if (null == queryDTO.getEndDate()) {
            queryDTO.setEndDate(new Date());
        }
        Page<ContractStatusRecordVO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        List<ContractStatusRecordEntity> pageRecordList = contractStatusRecordMapper.selectContractInfo(page, queryDTO);
//        if (null == queryDTO.getEndFinancialContractStatusUpdateTime() || DateUtil.isSameDay(DateUtil.date(), queryDTO.getEndFinancialContractStatusUpdateTime())) {
//            setContractStatusByCode(contractBalanceEntities);
//        }
        if(pageRecordList!=null && pageRecordList.size()>0){
            setPageData(pageRecordList);
        }
        page.setRecords(BeanUtil.copyToList(pageRecordList, ContractStatusRecordVO.class));
        return page;
    }

    /**
     * @description: 特殊合同状态-列表-查询-设置列表查询数据
     **/
    private void setPageData(List<ContractStatusRecordEntity> pageDataList) {
        Map<String, String> companyMap = orgCompanyService.selectAllOrgIdAndName().stream().collect(Collectors.toMap(e -> e.getOrgId(), e -> e.getOrgName(), (a, b) -> b));
        pageDataList.forEach(e -> {
            // 转入公司(针对内部转让)
            if(StringUtils.isNotEmpty(e.getTransferOrgId())){
                e.setTransferOrgId(companyMap.get(e.getTransferOrgId()));
            }
        });
    }



    @Override
    public List<ContractStatusRecordVO> selectList(ContractStatusRecordQueryDTO queryDTO) {
//        if (null != queryDTO.getFinancialContractStatusUpdateTime()) {
//            queryDTO.setFinancialContractStatusUpdateTime(DateUtil.endOfDay(queryDTO.getFinancialContractStatusUpdateTime()));
//        }
        List<ContractStatusRecordEntity> contractBalanceEntities = contractStatusRecordMapper.selectContractInfo(queryDTO);
        if (null == queryDTO.getEndFinancialContractStatusUpdateTime() || DateUtil.isSameDay(DateUtil.date(), queryDTO.getEndFinancialContractStatusUpdateTime())) {
            setContractStatusByCode(contractBalanceEntities);
        }
        List<ContractStatusRecordVO> list = BeanUtil.copyToList(contractBalanceEntities, ContractStatusRecordVO.class);
        translateDict(list);
        return list;
    }

    private void translateDict(List<ContractStatusRecordVO> list) {
        // 业务合同状态
        R<List<SysDictData>> contractStatusR = remoteDictService.listDictData(DictTypeEnum.CONTRACT_STATUS.getCode());
        Map<String, String> contractStatusMap = contractStatusR.getData().stream().collect(Collectors.toMap(e -> e.getDictValue(), e -> e.getDictLabel()));
        // 财务合同状态
        R<List<SysDictData>> financialContractStatusR = remoteDictService.listDictData(DictTypeEnum.FINANCIAL_CONTRACT_STATUS.getCode());
        Map<String, String> financialContractStatusMap = financialContractStatusR.getData().stream().collect(Collectors.toMap(e -> e.getDictValue(), e -> e.getDictLabel()));
        // 签约主体
        Map<String, String> companyMap = orgCompanyService.selectAllOrgIdAndName().stream().collect(Collectors.toMap(e -> e.getOrgId(), e -> e.getOrgName(), (a, b) -> b));
        list.forEach(e -> {
            // 签约主体
            e.setOrgId(companyMap.get(e.getOrgId()));
            // 财务合同状态
            e.setFinancialContractStatus(financialContractStatusMap.get(e.getFinancialContractStatus()));
            // 业务合同状态
            e.setContractStatus(contractStatusMap.get(e.getContractStatus()));
            // 转入公司
            e.setTransferOrgId(companyMap.get(e.getTransferOrgId()));
            // 转入合同系统合同状态
            e.setTransferContractStatus(contractStatusMap.get(e.getTransferContractStatus()));
        });
    }

    private void setContractStatusByCode(List<ContractStatusRecordEntity> contractBalanceEntities) {
        List<String> contractCodeList = contractBalanceEntities.stream().map(ContractStatusRecordEntity::getContractCode).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(contractCodeList)) {
            List<ContractDTO> contractDTOS = contractService.listContractDTOByCodeList(contractCodeList);
            contractDTOS = contractDTOS.stream().filter(e -> StringUtils.isNotBlank(e.getContractStatus())).collect(Collectors.toList());
            Map<String, String> contractStatusMap = contractDTOS.stream().collect(Collectors.toMap(ContractDTO::getContractCode, ContractDTO::getContractStatus, (k1, k2) -> k2));
            contractBalanceEntities.forEach(e -> e.setContractStatus(contractStatusMap.get(e.getContractCode())));
        }
    }

    @Override
    public IPage<ContractStatusRecordVO> pageDetail(ContractStatusRecordQueryDTO queryDTO) {
        LambdaQueryWrapper<ContractStatusRecordEntity> queryWrapper = Wrappers.<ContractStatusRecordEntity>lambdaQuery();
        //这里注入查询条件
        queryWrapper.eq(StringUtils.isNotBlank(queryDTO.getContractCode()), ContractStatusRecordEntity::getContractCode, queryDTO.getContractCode());
        queryWrapper.in(CollectionUtils.isNotEmpty(queryDTO.getOrgIdList()), ContractStatusRecordEntity::getOrgId, queryDTO.getOrgIdList());
        queryWrapper.orderByDesc(ContractStatusRecordEntity::getUpdateTime);
        IPage<ContractStatusRecordEntity> entityIPage = contractStatusRecordMapper.selectPage(new Page<ContractStatusRecordEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, ContractStatusRecordVO.class);
    }

    @Override
    public List<ContractStatusRecordVO> listDetail(ContractStatusRecordQueryDTO queryDTO) {
        LambdaQueryWrapper<ContractStatusRecordEntity> queryWrapper = Wrappers.<ContractStatusRecordEntity>lambdaQuery();
        //这里注入查询条件
        queryWrapper.eq(StringUtils.isNotBlank(queryDTO.getContractCode()), ContractStatusRecordEntity::getContractCode, queryDTO.getContractCode());
        queryWrapper.in(CollectionUtils.isNotEmpty(queryDTO.getOrgIdList()), ContractStatusRecordEntity::getOrgId, queryDTO.getOrgIdList());
        queryWrapper.in(CollectionUtils.isNotEmpty(queryDTO.getIdList()), ContractStatusRecordEntity::getId, queryDTO.getIdList());
        queryWrapper.orderByDesc(ContractStatusRecordEntity::getUpdateTime);
        List<ContractStatusRecordEntity> entityIPage = contractStatusRecordMapper.selectList(queryWrapper);
        List<ContractStatusRecordVO> list = ListBeanUtil.copyList(entityIPage, ContractStatusRecordVO.class);
        translateDict(list);
        return list;
    }

    /**
     * @description:特殊合同状态-上传-确定按钮
     **/
    @Override
    public String importData(List<ContractStatusRecordSaveDTO> list, String operName) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        // 删除临时表数据
        contractStatusRecordTempService.getBaseMapper().delete(new LambdaQueryWrapper<>());
        // excel数据转换为临时表实体数据
        List<ContractStatusRecordTempEntity> contractStatusRecordTempEntityList = BeanUtil.copyToList(list, ContractStatusRecordTempEntity.class);
        // 将excel数据保存至临时表
        contractStatusRecordTempService.saveBatch(contractStatusRecordTempEntityList);
        // 关联合同表和临时表查询合同信息
        List<ContractEntity> contracts = contractMapper.selectContractInfoByTempTable();
        // 以合同代码+机构ID为唯一标识的合同对象映射表，并在标识重复时保留最后一次出现的合同对象
        Map<String, ContractEntity> contractMap = contracts.stream().collect(
                Collectors.toMap(e -> e.getContractCode().concat(e.getOrgId()), Function.identity(), (k1, k2) -> k2));
        List<String> contractCodeList = list.stream().map(ContractStatusRecordSaveDTO::getContractCode).
                collect(Collectors.toList());
        // 查询正式表-查询所有特殊合同状态信息
        LambdaQueryWrapper<ContractStatusRecordEntity> lambdaQueryWrapper = new LambdaQueryWrapper();
//        lambdaQueryWrapper.in(ContractStatusRecordEntity::getContractCode, contractCodeList);
        List<ContractStatusRecordEntity> contractStatusRecordList = contractStatusRecordMapper.selectList(lambdaQueryWrapper);
        // 查询库中正式表中的所有数据并标识唯一性：合同编号+主体编号+财务合同状态更新时间+财务合同状态
        Map<String, ContractStatusRecordEntity> contractStatusRecordMap = contractStatusRecordList.stream().
                collect(Collectors.toMap(e -> this.getContractStatusRecordKey(e), e -> e, (a, b) -> b));
        // 查询财务合同状态数据字典
        R<List<SysDictData>> financialContractStatusR = remoteDictService.listDictData(
                DictTypeEnum.FINANCIAL_CONTRACT_STATUS.getCode());
        Map<String, String> financialContractStatusMap = financialContractStatusR.getData().stream().
                collect(Collectors.toMap(e -> e.getDictLabel(), e -> e.getDictValue()));
        // 查询签约主体数据字典
        Map<String, String> companyMap = orgCompanyService.selectAllOrgIdAndName().stream().collect(Collectors.toMap(e -> e.getOrgName(), e -> e.getOrgId(), (a, b) -> b));
        // 本次导入结果信息
        List<String> failureMsg = new ArrayList<>();
        // 本次新增数据列表
        List<ContractStatusRecordEntity> contractStatusRecordEntities = new ArrayList<>();
        // 循环处理导入的excel数据
        for (ContractStatusRecordSaveDTO contractStatusRecordSaveDTO : list) {
            // 设置主体id
            contractStatusRecordSaveDTO.setOrgId(companyMap.get(contractStatusRecordSaveDTO.getOrgId()));
            // 设置财务合同状态
            String financialContractStatus = financialContractStatusMap.get(contractStatusRecordSaveDTO.getFinancialContractStatus());
            if (null == financialContractStatus) {
                failureMsg.add(contractStatusRecordSaveDTO.getContractCode() + "财务合同状态错误");
                continue;
            }
            contractStatusRecordSaveDTO.setFinancialContractStatus(financialContractStatus);
            // 拷贝至正式表实体类
            ContractStatusRecordEntity entity = BeanUtil.copyProperties(contractStatusRecordSaveDTO, ContractStatusRecordEntity.class);
            // 校验合同表是否存在导入的合同数据
            ContractEntity contract = contractMap.get(entity.getContractCode().concat(StringUtils.isEmpty(contractStatusRecordSaveDTO.getOrgId()) ? "" : contractStatusRecordSaveDTO.getOrgId()));
            if (null == contract) {
                failureMsg.add(entity.getContractCode() + "合同信息不存在");
            } else {
                // 如果db正式表中已经存在该记录，则直接跳过不进行保存
                // 总结：正式表中数据唯一性为：合同编号+主体编号+财务合同状态更新时间+财务合同状态 ，一个合同可能会存在财务合同状态更新时间不一样的多条数据
                ContractStatusRecordEntity contractStatusRecord = contractStatusRecordMap.get(this.getContractStatusRecordKey(entity));
                if (contractStatusRecord != null) {
                    // 转入公司
                    contractStatusRecord.setTransferOrgId(companyMap.get(contractStatusRecordSaveDTO.getTransferOrgId()));
                    // 转入合同号
                    contractStatusRecord.setTransferContractCode(contractStatusRecordSaveDTO.getTransferContractCode());
                    // 转入合同系统合同状态
                    contractStatusRecord.setTransferContractStatus(contractStatusRecordSaveDTO.getTransferContractStatus());
                    this.updateById(contractStatusRecord);
                    continue;
                }
                entity.setContractName(contract.getContractName());
                entity.setClientCode(contract.getClientCode());
                entity.setClientName(contract.getClientName());
                entity.setContractStatus(contract.getContractStatus());
                entity.setOperator(operName);
                // 转入公司
                if(StringUtils.isNotEmpty(contractStatusRecordSaveDTO.getTransferOrgId())){
                    entity.setTransferOrgId(companyMap.get(contractStatusRecordSaveDTO.getTransferOrgId()));
                }
                // 1-已录入
                entity.setRecordStatus(ContractStatusRecordStatus.ENTERED.getCode());
                contractStatusRecordEntities.add(entity);
            }
        }
        //记录表新增数据
        this.saveBatch(contractStatusRecordEntities);
        if (CollectionUtils.isEmpty(failureMsg)) {
            return "";
        }
        return failureMsg.toString();
    }

    /**
     * 取得ContractStatusRecordEntity的业务主键
     * 合同编号+主体编号+财务合同状态更新时间+财务合同状态
     */
    private String getContractStatusRecordKey(ContractStatusRecordEntity entity) {
        StringBuffer result = new StringBuffer();
        String financialContractStatusUpdateTime = "";
        if (ObjectUtil.isNotNull(entity.getFinancialContractStatusUpdateTime())) {
            // 财务合同状态更新时间
            financialContractStatusUpdateTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,
                    entity.getFinancialContractStatusUpdateTime());
        }
        result.append(entity.getContractCode()).append(entity.getOrgId()).
                append(financialContractStatusUpdateTime).append(entity.getFinancialContractStatus());
        return result.toString();
    }

    @Override
    public String importData(List<ContractStatusRecordSaveDTO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        Map<String, String> financialStatusMap = new HashMap<>();
        R<List<SysDictData>> dictList2 = remoteDictService.listDictData(DictTypeEnum.FINANCIAL_CONTRACT_STATUS.getCode());
        if (dictList2.getCode() == HttpStatus.SUCCESS && CollectionUtil.isNotEmpty(dictList2.getData())) {
            for (SysDictData dictData : dictList2.getData()) {
                financialStatusMap.put(dictData.getDictLabel(), dictData.getDictValue());
            }
        }
        List<ContractEntity> contracts = contractService.list(Wrappers.<ContractEntity>lambdaQuery().in(ContractEntity::getContractCode, list.stream().map(ContractStatusRecordSaveDTO::getContractCode).collect(Collectors.toList())));
        Map<String, ContractEntity> contractMap = contracts.stream().collect(Collectors.toMap(ContractEntity::getContractCode, e -> e));
        List<ContractEntity> saveContracts = new ArrayList<>();
        for (ContractStatusRecordSaveDTO contractStatusRecordSaveDTO : list) {
            ContractEntity contract = contractMap.get(contractStatusRecordSaveDTO.getContractCode());
            if (null != contract) {
                contract.setFinancialContractStatus(financialStatusMap.get(contractStatusRecordSaveDTO.getFinancialContractStatus()));
                contract.setFinancialContractStatusUpdateTime(contractStatusRecordSaveDTO.getFinancialContractStatusUpdateTime());
                saveContracts.add(contract);
            }
        }
        log.info("更新了合同size:{},合同号:{}", saveContracts.size(), saveContracts.stream().map(e -> e.getContractCode()).collect(Collectors.toList()));
        contractService.updateBatchById(saveContracts);
        return null;
    }

    @Override
    public Void submit(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少选择一条记录删除数据");
        }
        List<ContractStatusRecordEntity> recordEntityList = this.lambdaQuery().in(ContractStatusRecordEntity::getId, ids).list();
        List<ApproveDTO> approveDTOList = Lists.newArrayList();
        recordEntityList.forEach(v -> {
            if (!ProcessStatusEnum.ENTERED.getCode().equals(v.getRecordStatus())) {
                throw new ServiceException("只有处理状态为已录入才可以提交");
            }
            v.setRecordStatus(ProcessStatusEnum.SUBMITTED.getCode());
            v.setUpdateBy(UserUtils.getStaffCode());
            v.setSubmitBy(UserUtils.getStaffCode());
            ApproveDTO approveDTO = new ApproveDTO();
            approveDTO.setDocumentId(v.getId());
            approveDTO.setDocumentType(BatchTypeEnum.TSHT.getCode());
            approveDTO.setUrl(approveUrl + "contractCode=" + v.getContractCode() + "&orgIdList=" + v.getOrgId() + "&id=" + v.getId());
            approveDTOList.add(approveDTO);
        });
        //发送审核
        Map<Long, Long> processInstantIdMap = iApproveService.submit(approveDTOList);
        recordEntityList.stream().forEach(v -> {
            if (null != processInstantIdMap && processInstantIdMap.containsKey(v.getId())) {
                v.setProcessInstanceId(processInstantIdMap.get(v.getId()));
            }
        });
        //更新合同表财务合同状态和财务合同状态更新时间
        updateContract(recordEntityList);
        this.updateBatchById(recordEntityList);
        return null;
    }

    public void updateContract(List<ContractStatusRecordEntity> recordEntityList) {
        // 更新特殊合同状态
        contractStatusUpdateService.updateTSZTContractStatus(recordEntityList);

//        recordEntityList.forEach(v -> {
//            CompletableFuture.runAsync(() -> {
//                iContractService.lambdaUpdate().set(ContractEntity::getFinancialContractStatus, v.getFinancialContractStatus()).set(ContractEntity::getFinancialContractStatusUpdateTime, v.getFinancialContractStatusUpdateTime())
//                        .eq(ContractEntity::getContractCode, v.getContractCode()).eq(ContractEntity::getOrgId, v.getOrgId()).update();
//            });
//        });
//        // 是否更新合同月表合同财务状态
//        if(ContractStatusUpdateUtils.isUpdateContractStatusEnable(remoteDictService, "TS")) {
//            recordEntityList.forEach(v -> {
//                CompletableFuture.runAsync(() -> {
//                    iContractMonthService.lambdaUpdate().set(ContractMonthEntity::getFinancialContractStatus, v.getFinancialContractStatus()).set(ContractMonthEntity::getFinancialContractStatusUpdateTime, v.getFinancialContractStatusUpdateTime())
//                            .eq(ContractMonthEntity::getContractCode, v.getContractCode()).eq(ContractMonthEntity::getOrgId, v.getOrgId()).update();
//                });
//            });
//        }
    }

    @Override
    public Void pass(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return null;
        }
        List<ContractStatusRecordEntity> entities = this.getBaseMapper().selectList(Wrappers.<ContractStatusRecordEntity>lambdaQuery().in(ContractStatusRecordEntity::getId, ids));
        if (entities.stream().anyMatch(e -> !ContractStatusRecordStatus.SUBMITTED.getCode().equals(e.getRecordStatus()))) {
            throw new ServiceException("存在错误状态数据");
        }
        entities.forEach(e -> e.setRecordStatus(ContractStatusRecordStatus.PASS.getCode()));
        updateBatchById(entities);
        // 不是最新的那一条,则不覆盖合同表数据
//        LambdaQueryWrapper<ContractStatusRecordEntity> lambdaQuery = Wrappers.<ContractStatusRecordEntity>lambdaQuery();
//
//        getBaseMapper().selectList(lambdaQuery);
        entities.forEach(e -> {
            LambdaUpdateWrapper<ContractEntity> updateChainWrapper = new LambdaUpdateWrapper<>();
            updateChainWrapper
                    .eq(ContractEntity::getContractCode, e.getContractCode())
                    .eq(ContractEntity::getOrgId, e.getOrgId())
                    .set(ContractEntity::getFinancialContractStatusUpdateTime, e.getFinancialContractStatusUpdateTime())
                    .set(ContractEntity::getFinancialContractStatus, e.getFinancialContractStatus());
            contractService.update(updateChainWrapper);
        });
        return null;
    }

    @Override
    public Void fail(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return null;
        }
        changeStatus(ids, ContractStatusRecordStatus.FAILED);
        return null;
    }

    /**
     * @description:特殊合同状态-导出按钮-异步导出文件-查询数据
     **/
    @Override
    public List<ContractStatusRecordVO> exportSummaryData(ContractStatusRecordQueryDTO queryDTO) {
        // 为空默认查询当前时间最新数据
        if (null == queryDTO.getEndDate()) {
            queryDTO.setEndDate(new Date());
        }
        log.info("====>>ContractStatusRecordServiceImpl.exportSummaryData==00==>>queryDTO:{}",queryDTO);
        List<ContractStatusRecordEntity> contractBalanceEntities = contractStatusRecordMapper.selectContractInfo(queryDTO);
        if(contractBalanceEntities==null){
            contractBalanceEntities = new ArrayList<>();
        }
        log.info("====>>ContractStatusRecordServiceImpl.exportSummaryData==01==>>contractBalanceEntities.size():{}",contractBalanceEntities.size());
        List<ContractStatusRecordVO> statusRecordVOList = BeanUtil.copyToList(contractBalanceEntities, ContractStatusRecordVO.class);
        setExportSummaryData(statusRecordVOList);
        return statusRecordVOList;
    }

    /**
     * @description: 特殊合同状态-导出按钮-异步导出文件-设置导出数据
     **/
    private void setExportSummaryData(List<ContractStatusRecordVO> summaryDataList) {
        // 业务合同状态
        R<List<SysDictData>> contractStatusR = remoteDictService.listDictData(DictTypeEnum.CONTRACT_STATUS.getCode());
        Map<String, String> contractStatusMap = contractStatusR.getData().stream().collect(Collectors.toMap(e -> e.getDictValue(), e -> e.getDictLabel()));
        // 财务合同状态
        R<List<SysDictData>> financialContractStatusR = remoteDictService.listDictData(DictTypeEnum.FINANCIAL_CONTRACT_STATUS.getCode());
        Map<String, String> financialContractStatusMap = financialContractStatusR.getData().stream().collect(Collectors.toMap(e -> e.getDictValue(), e -> e.getDictLabel()));
        // 签约主体
        Map<String, String> companyMap = orgCompanyService.selectAllOrgIdAndName().stream().collect(Collectors.toMap(e -> e.getOrgId(), e -> e.getOrgName(), (a, b) -> b));
//        // 业务合同状态
//        List<String> contractCodeList = summaryDataList.stream().map(ContractStatusRecordVO::getContractCode).collect(Collectors.toList());
//        List<ContractDTO> contractDTOS = contractService.listContractDTOByCodeList(contractCodeList);
//        contractDTOS = contractDTOS.stream().filter(e -> StringUtils.isNotBlank(e.getContractStatus())).collect(Collectors.toList());
//        Map<String, String> contractStatusMapFromContract  = contractDTOS.stream().collect(Collectors.toMap(ContractDTO::getContractCode, ContractDTO::getContractStatus, (k1, k2) -> k2));
        summaryDataList.forEach(e -> {
            // 签约主体
            e.setOrgId(companyMap.get(e.getOrgId()));
            // 财务合同状态
            e.setFinancialContractStatus(financialContractStatusMap.get(e.getFinancialContractStatus()));
            // 业务合同状态
            //e.setContractStatus(contractStatusMap.get(e.getContractStatus()));
            //e.setContractStatus(contractStatusMapFromContract.get(e.getContractCode()));
            // 转入合同的系统合同状态
            //e.setTransferContractStatus(contractStatusMap.get(e.getTransferContractStatus()));
            // 处理状态(1: 已录入,2: 已提交,3: 复核通过,4: 复核失败)
            e.setRecordStatus(ProcessStatusEnum.getDescByCode(e.getRecordStatus()));
            // 财务合同状态更新时间
            if (e.getFinancialContractStatusUpdateTime()!=null) {
                String updateTime = DateUtil.format(e.getFinancialContractStatusUpdateTime(), "yyyy-MM-dd");
                e.setFinancialContractStatusUpdateTime(DateUtil.parse(updateTime,"yyyy-MM-dd"));
            }
            // 转入公司(针对内部转让)
            if(StringUtils.isNotEmpty(e.getTransferOrgId())){
                e.setTransferOrgId(companyMap.get(e.getTransferOrgId()));
            }
        });
    }

    /**
     * @description:特殊合同状态-导出按钮
     **/
    @Override
    public Map<String, String> export(ContractStatusRecordQueryDTO queryDTO) {
        String fileName = "特殊合同汇总_" + LocalDateTimeUtil.format(LocalDateTimeUtil.now(), "yyyyMMddHHmmss") + ".xlsx";
        String filePath = getFilePath();
        FileRecordEntity record = new FileRecordEntity();
        record.setModuleName(ModuleEnum.SPECIAL_CONTRACT.getCode());
        record.setBusinessScene(BusinessSceneEnum.SPECIAL_CONTRACT_SUMMARY.getCode());
        record.setFileLocation(filePath + fileName);
        record.setFileName(fileName);
        record.setExecuteStatus(CheckExecuteStatusEnum.INPROGRESS.getCode());
        record.setFileUploadBy(String.valueOf(SecurityUtils.getUserId()));
        fileRecordService.save(record);
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            // 异步执行的任务
            queryAndWriteSpecialTable(queryDTO, fileName);
        }, asyncTaskExecutor).thenRun(() -> {
            FileRecordEntity fileRecordEntity = new FileRecordEntity();
            fileRecordEntity.setId(record.getId());
            fileRecordEntity.setExecuteStatus(CheckExecuteStatusEnum.FINISH.getCode());
            fileRecordEntity.setFileUploadTime(LocalDateTime.now());
            fileRecordService.updateById(fileRecordEntity);
        });
        Map<String, String> map = Maps.newLinkedHashMap();
        map.put("fileName", fileName);
        map.put("location", filePath + fileName);
        map.put("servicePath", servicePath);
        return map;
    }

    private String getFilePath() {
        String osName = System.getProperties().getProperty("os.name");
        if (osName.toLowerCase().contains("windows")) {
            return basicPathWindows + "specialContract" + File.separator;
        } else if (osName.toLowerCase().contains("linux") || osName.toLowerCase().contains("unix")) {
            return basicPathLinux + "specialContract" + File.separator;
        }
        return com.utfinancing.financehub.common.core.utils.StringUtils.EMPTY;
    }

    @Override
    public Boolean updateProcessStatus(CommonApproveDTO approveDTO) {
        if (StringUtils.isEmpty(approveDTO.getDocumentStatus())) {
            throw new ServiceException("复核状态不可以为空");
        }
        ContractStatusRecordEntity recordEntity = this.getById(approveDTO.getDocumentId());
        if (null == recordEntity) {
            throw new ServiceException("特殊合同数据不存在");
        }
        String processStatus = recordEntity.getRecordStatus();
        if (ProcessStatusEnum.REVIEWED.getCode().equals(approveDTO.getDocumentStatus())) {
            processStatus = ProcessStatusEnum.REVIEWED.getCode();
        } else if (ProcessStatusEnum.REJECTED.getCode().equals(approveDTO.getDocumentStatus())) {
            processStatus = ProcessStatusEnum.REJECTED.getCode();
        }
        recordEntity.setRecordStatus(processStatus);
        return this.updateById(recordEntity);
    }

    @Override
    public Boolean deleteByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条数据删除");
        }
        List<ContractStatusRecordEntity> recordEntityList = this.listByIds(ids);
        recordEntityList.forEach(v -> {
            if (!ProcessStatusEnum.ENTERED.getCode().equals(v.getRecordStatus())) {
                throw new ServiceException("处理状态为已录入的才可以删除");
            }

        });
        return this.removeBatchByIds(ids);
    }

    @Override
    public IPage<FileRecordEntity> selectSpecialContractFileList(FileRecordQueryDTO queryDTO) {
        queryDTO.setExecuteStatus(Arrays.asList(CheckExecuteStatusEnum.FINISH.getCode(), CheckExecuteStatusEnum.INPROGRESS.getCode()));
        queryDTO.setModuleName(ModuleEnum.SPECIAL_CONTRACT.getCode());
        queryDTO.setBusinessScene(BusinessSceneEnum.SPECIAL_CONTRACT_SUMMARY.getCode());
        return fileRecordService.selectFileListByModuleAndBusiness(queryDTO);
    }

    @Override
    public ContractStatusRecordDTO getLastByContractCodeAndOrgId(String contractCode, String orgId) {
        return contractStatusRecordMapper.getLastByContractCodeAndOrgId(contractCode, orgId);
    }

    @Override
    public List<ContractStatusRecordEntity> listFinContractStatusByContractCodes(List<String> contractCodes, List<String> finContractStatus) {
        LambdaQueryWrapper<ContractStatusRecordEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ContractStatusRecordEntity::getContractCode, contractCodes);
        queryWrapper.in(CollectionUtils.isNotEmpty(finContractStatus),ContractStatusRecordEntity::getFinancialContractStatus, finContractStatus);
        return contractStatusRecordMapper.selectList(queryWrapper);
    }

    /**
     * @description:特殊合同状态-导出按钮-异步导出文件
     **/
    @Async
    public void queryAndWriteSpecialTable(ContractStatusRecordQueryDTO queryDTO, String fileName) {
        log.info("==>>特殊合同状态-导出按钮-异步导出文件-查询开始时间：{}",System.currentTimeMillis());
        List<ContractStatusRecordVO> summaryDataVOList = exportSummaryData(queryDTO);
        List<ContractStatusRecordExcel> excelList = Lists.newArrayList();
        if(summaryDataVOList!=null && summaryDataVOList.size()>0){
            excelList = BeanUtil.copyToList(summaryDataVOList,ContractStatusRecordExcel.class);
        }
        log.info("==>>特殊合同状态-导出按钮-异步导出文件-查询结束时间：{}",System.currentTimeMillis());
        ExcelWriter writer = ExcelUtil.getWriter(getFilePath() + fileName);
        writer.addHeaderAlias("contractCode", "合同号");
        writer.addHeaderAlias("clientName", "客户名称");
        writer.addHeaderAlias("orgId", "签约主体");
        writer.addHeaderAlias("financialContractStatusUpdateTime", "财务合同状态更新时间");
        writer.addHeaderAlias("financialContractStatus", "财务合同状态");
        writer.addHeaderAlias("contractStatus", "业务合同状态");
        writer.addHeaderAlias("transferOrgId", "转入公司(针对内部转让)");
        writer.addHeaderAlias("transferContractCode", "转入合同号(针对内部转让)");
        writer.addHeaderAlias("transferContractStatus", "转入合同系统合同状态(针对内部转让)");
        writer.addHeaderAlias("recordStatus", "处理状态");
        writer.autoSizeColumnAll();
        // 强制刷新标题头
        writer.reset();
        writer.write(excelList, true);
        writer.close();
        log.info("==>>特殊合同状态-导出按钮-异步导出文件-执行结束时间：{}",System.currentTimeMillis());
    }

    @Override
    public Void withdraw(List<Long> ids) {
        List<ContractStatusRecordEntity> recordEntityList = this.listByIds(ids);
        recordEntityList.forEach(v -> {
            if (!ProcessStatusEnum.SUBMITTED.getCode().equals(v.getRecordStatus())) {
                throw new ServiceException("只有状态为已提交的才可以撤回");
            }
            v.setRecordStatus(ProcessStatusEnum.ENTERED.getCode());
        });
        iApproveService.withdraw(recordEntityList.stream().map(ContractStatusRecordEntity::getProcessInstanceId).collect(Collectors.toList()));
        this.updateBatchById(recordEntityList);
        return null;
    }

    private void changeStatus(List<Long> ids, ContractStatusRecordStatus statusRecordStatus) {
        LambdaUpdateWrapper<ContractStatusRecordEntity> updateChainWrapper = new LambdaUpdateWrapper<>();
        updateChainWrapper
                .in(ContractStatusRecordEntity::getId, ids)
                .eq(ContractStatusRecordEntity::getRecordStatus, ContractStatusRecordStatus.SUBMITTED.getCode())
                .set(ContractStatusRecordEntity::getUpdateTime, LocalDateTime.now())
                .set(ContractStatusRecordEntity::getContractStatus, statusRecordStatus.getCode());
        this.update(updateChainWrapper);
    }

}