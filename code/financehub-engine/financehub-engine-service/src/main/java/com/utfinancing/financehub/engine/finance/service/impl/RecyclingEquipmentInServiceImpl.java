package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.google.common.collect.Maps;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.approve.model.dto.ApproveDTO;
import com.utfinancing.financehub.engine.approve.service.IApproveService;
import com.utfinancing.financehub.engine.contractstatusupdate.service.IContractStatusUpdateService;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.ContractVO;
import com.utfinancing.financehub.engine.finance.model.vo.OrgCompanyVO;
import com.utfinancing.financehub.engine.finance.model.vo.RecyclingEquipmentInCheckVO;
import com.utfinancing.financehub.engine.finance.mapper.RecyclingEquipmentInMapper;
import com.utfinancing.financehub.engine.finance.model.vo.RecyclingEquipmentInVO;
import com.utfinancing.financehub.engine.finance.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.hthx.common.enums.FinanceEngineEnum;
import com.utfinancing.financehub.engine.hthx.common.enums.ResultEnum;
import com.utfinancing.financehub.engine.hthx.utils.PromptMessageUtil;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import com.utfinancing.financehub.engine.rule.model.dto.ExecuteCommonDTO;
import com.utfinancing.financehub.engine.rule.model.vo.VoucherInfoVO;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import com.utfinancing.financehub.engine.scene.entity.SceneVoucherEntryEntity;
import com.utfinancing.financehub.engine.utils.CommonDateUtils;
import com.utfinancing.financehub.engine.verification.model.vo.CourtCostDetailsVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @Author : jnc
 * @Date : Create in 2024-03-07
 * @Description :  RecyclingEquipmentIn服务实现类
 * @Modified :
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class RecyclingEquipmentInServiceImpl extends ServiceImpl<RecyclingEquipmentInMapper, RecyclingEquipmentInEntity> implements IRecyclingEquipmentInService {

    private final RecyclingEquipmentInMapper recyclingEquipmentInMapper;

    @Resource
    private IOrgCompanyService iOrgCompanyService;

    @Resource
    private IRecyclingEquipmentInDetailService recyclingEquipmentInDetailService;

    @Resource
    private IContractService iContractService;

    @Resource
    private IContractBalanceService iContractBalanceService;

    @Resource
    private IRuleService iRuleService;

    @Resource
    private IApproveService iApproveService;

    @Value("${approve.url.recyclingEquipIn-url:null}")
    private String approveUrl;

    @Resource
    private IVoucherService iVoucherService;

    @Autowired
    @Qualifier("asyncTaskExecutor")
    private ThreadPoolTaskExecutor asyncTaskExecutor;

    @Resource
    private IContractStatusRecordService contractStatusRecordService;

    @Resource
    private IContractStatusUpdateService iContractStatusUpdateService;

    @Override
    public Long saveRecyclingEquipmentIn(RecyclingEquipmentInDTO dto) {
        RecyclingEquipmentInEntity entity = BeanUtil.copyProperties(dto, RecyclingEquipmentInEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateRecyclingEquipmentIn(Long id, RecyclingEquipmentInDTO dto) {
        RecyclingEquipmentInEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public RecyclingEquipmentInDTO getRecyclingEquipmentInDTOById(Long id) {
        RecyclingEquipmentInEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, RecyclingEquipmentInDTO.class);
    }

    /**
     * @description: 债务重组业务-回收设备-财务入库-首页列表查询
     * @author: zhangli.chen
     **/
    @Override
    public IPage<RecyclingEquipmentInVO> selectPage(RecyclingEquipmentInQueryDTO queryDTO) {
        LambdaQueryWrapper<RecyclingEquipmentInEntity> queryWrapper = Wrappers.<RecyclingEquipmentInEntity>lambdaQuery();
        if(StringUtils.isNotEmpty(queryDTO.getInboundDateStart())){
            queryWrapper.apply(" inbound_date >= {0}", queryDTO.getInboundDateStart());
        }
        if(StringUtils.isNotEmpty(queryDTO.getInboundDateEnd())){
            queryWrapper.apply(" inbound_date <= {0}", queryDTO.getInboundDateEnd());
        }
        if(CollectionUtil.isNotEmpty(queryDTO.getOrgIds())){
            queryWrapper.in(RecyclingEquipmentInEntity::getOrgId, queryDTO.getOrgIds());
        }
        queryWrapper.orderByDesc(RecyclingEquipmentInEntity::getInboundDate);
        queryWrapper.orderByDesc(RecyclingEquipmentInEntity::getCreateTime);
        //这里注入查询条件
        IPage<RecyclingEquipmentInEntity> entityIPage = recyclingEquipmentInMapper.selectPage(new Page<RecyclingEquipmentInEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        IPage<RecyclingEquipmentInVO> page = ListBeanUtil.copyPage(entityIPage, RecyclingEquipmentInVO.class);
        fillVoList(page.getRecords());
        return page;
    }

    /**
     * @description: 债务重组业务-回收设备-财务入库-上传-导入
     * 1.校验文件数据
     * 2.根据本次导入合同，获取从contract_balance表计算对应余额信息
     * 3.根据入库日期-签约主体分组，并分批插入入库主表和明细表
     * @author: zhangli.chen
     **/
    @Override
    public Boolean importTemplate(MultipartFile file) {
        try {
            ExcelUtil<RecyclingEquipmentInDetailExcelDTO> util = new ExcelUtil<RecyclingEquipmentInDetailExcelDTO>(RecyclingEquipmentInDetailExcelDTO.class);
            List<RecyclingEquipmentInDetailExcelDTO> importExcelDataList = util.importExcel(file.getInputStream());
            /**
             * 1.校验文件数据
             */
            checkImportData(importExcelDataList);
            /**
             * 2.根据本次导入合同，获取从contract_balance表计算对应余额信息
             */
            List<RecyclingEquipmentInDetailEntity> rcDetailEntityList = getBalanceInfoForImportContract(importExcelDataList);
            /**
             * 3.根据入库日期-签约主体分组，并分批插入入库主表和明细表
             */
            if(CollectionUtils.isNotEmpty(rcDetailEntityList)){
                // 详细表数据根据入库日期-签约主体分组
                Map<String,List<RecyclingEquipmentInDetailEntity>> detailEntityMap = rcDetailEntityList
                        .stream().collect(Collectors.groupingBy(v->v.getInboundDate() + FinanceEngineEnum.Symbol.UNDERLINE.getValue() + v.getOrgId()));
                RecyclingEquipmentInDetailEntity importDetailEntity = rcDetailEntityList.stream().findFirst().orElse(null);
                String importInboundDateYearAndMonth = null;
                String importInboundDate = null;
                if(importDetailEntity!=null && StringUtils.isNotEmpty(importDetailEntity.getInboundDate())){
                    importInboundDateYearAndMonth = importDetailEntity.getInboundDate().substring(0, 7);
                    importInboundDate = importDetailEntity.getInboundDate();
                }
                log.info("====>>RecyclingEquipmentInServiceImpl.importTemplate==00==>>importDetailEntity:{}" +
                        ",importInboundDateYearAndMonth:{},importInboundDate:{}",importDetailEntity,importInboundDateYearAndMonth,importInboundDate);
                // 获取各个月份的最后一次上传批次号
                Map<String,String> historyBatchNumberMap = new HashMap<>();
                // 获取各个月份的最后一次上传批次号
                RecyclingEquipmentInEntity historyEquipmentInEntity =  this.baseMapper.getLastImportedRecyclingEquipmentByYearMonth(importInboundDateYearAndMonth);
                if(historyEquipmentInEntity!=null){
                    historyBatchNumberMap.put(importInboundDate,historyEquipmentInEntity.getBatchNumber());
                }
                log.info("====>>RecyclingEquipmentInServiceImpl.importTemplate==01==>>historyBatchNumberMap:{}",historyBatchNumberMap);
                // 循环处理不同主体+同一入库日期的财务入库数据
                for (Map.Entry<String, List<RecyclingEquipmentInDetailEntity>> entry : detailEntityMap.entrySet()) {
                    String inboundDate = null;
                    String orgId = null;
                    if(StringUtils.isNotEmpty(entry.getKey())){
                        String[] parts = entry.getKey().split(FinanceEngineEnum.Symbol.UNDERLINE.getValue(), FinanceEngineEnum.Numbers.TWO.getKey());
                        // 下划线前的部分
                        if (parts.length >= FinanceEngineEnum.Numbers.ONE.getKey()) {
                            inboundDate = parts[FinanceEngineEnum.Numbers.ZERO.getKey()];
                        }
                        // 下划线后的部分
                        if (parts.length == FinanceEngineEnum.Numbers.TWO.getKey()) {
                            orgId = parts[FinanceEngineEnum.Numbers.ONE.getKey()];
                        }
                    }
                    List<RecyclingEquipmentInDetailEntity> detailListByKey = entry.getValue();
                    if(CollectionUtils.isEmpty(detailListByKey)){
                        continue;
                    }
                    Optional<RecyclingEquipmentInDetailEntity> sumDetailListByKeyOptional = detailListByKey.stream().reduce((x,y) -> {
                        RecyclingEquipmentInDetailEntity tmp = new RecyclingEquipmentInDetailEntity();
                        tmp.setFinancialExposure(x.getFinancialExposure().add(y.getFinancialExposure()));
                        tmp.setProvisionForImpairment(x.getProvisionForImpairment().add(y.getProvisionForImpairment()));
                        tmp.setRecyclingEquipmentCost(x.getRecyclingEquipmentCost().add(y.getRecyclingEquipmentCost()));
                        return tmp;
                    });
                    RecyclingEquipmentInDetailEntity sumDetailListByKey = new RecyclingEquipmentInDetailEntity();
                    if(sumDetailListByKeyOptional.isPresent()){
                        sumDetailListByKey = sumDetailListByKeyOptional.get();
                    }
                    RecyclingEquipmentInEntity recyclingEquipmentInEntity = new RecyclingEquipmentInEntity();
                    recyclingEquipmentInEntity.setInboundDate(inboundDate);
                    recyclingEquipmentInEntity.setOrgId(orgId);
                    recyclingEquipmentInEntity.setFinancialExposure(sumDetailListByKey.getFinancialExposure());
                    recyclingEquipmentInEntity.setRecyclingEquipmentCost(sumDetailListByKey.getRecyclingEquipmentCost());
                    recyclingEquipmentInEntity.setProvisionForImpairment(sumDetailListByKey.getProvisionForImpairment());
                    recyclingEquipmentInEntity.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
                    recyclingEquipmentInEntity.setIsGenerateVoucher(FinanceEngineEnum.Numbers.ZERO.getValue());
                    String importBatchNumber =  null;
                    // 设置上次批次号
                    if(historyBatchNumberMap!=null && historyBatchNumberMap.containsKey(inboundDate)){
                        String preBatchNumber = historyBatchNumberMap.get(inboundDate);
                        if(StringUtils.isNotEmpty(preBatchNumber)){
                            String[] parts = preBatchNumber.split(FinanceEngineEnum.Symbol.LINE.getValue());
                            int day = Integer.parseInt(parts[1]);
                            importBatchNumber =  String.format("%s-%02d", parts[0], day + 1);
                        }
                    }
                    log.info("====>>RecyclingEquipmentInServiceImpl.importTemplate==02==>>historyBatchNumberMap:{},inboundDate:{},importBatchNumber:{}"
                            ,historyBatchNumberMap,inboundDate,importBatchNumber);
                    if(StringUtils.isEmpty(importBatchNumber)){
                        LocalDate date = LocalDate.parse(inboundDate, DateTimeFormatter.ISO_DATE);
                        importBatchNumber = String.format("%d%02d-01", date.getYear(), date.getMonthValue());
                    }
                    log.info("====>>RecyclingEquipmentInServiceImpl.importTemplate==03==>>importBatchNumber:{}",importBatchNumber);
                    recyclingEquipmentInEntity.setBatchNumber(importBatchNumber);
                    this.save(recyclingEquipmentInEntity);
                    // 设置财务入口头表ID
                    detailListByKey.stream().forEach(v->{
                        v.setRecycleId(recyclingEquipmentInEntity.getId());
                    });
                    recyclingEquipmentInDetailService.saveBatch(detailListByKey);
                }
            }else{
                throw new ServiceException("没有需要更新的设备回收入库数据");
            }
        } catch (ServiceException serviceException) {
            throw new ServiceException("导入设备回收入库数据失败，失败原因："+serviceException.getMessage());
        } catch (Exception exception) {
            throw new ServiceException("导入设备回收入库数据失败，失败原因："+exception.getMessage());
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean submit(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条数据提交");
        }
        List<RecyclingEquipmentInEntity> inEntityList = this.listByIds(ids);
        List<ApproveDTO> approveDTOList = Lists.newArrayList();
        inEntityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus()) && !ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("只有处理状态为已录入和已拒绝的才可以提交");
            }
            v.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
            ApproveDTO approveDTO = new ApproveDTO();
            approveDTO.setDocumentId(v.getId());
            approveDTO.setDocumentType(BatchTypeEnum.HSSBCWRK.getCode());
            approveDTO.setUrl(approveUrl+"orgId="+v.getOrgId()+"&inboundDate="+v.getInboundDate());
            approveDTOList.add(approveDTO);

        });
        //发送审核
        Map<Long,Long> processInstantIdMap = iApproveService.submit(approveDTOList);
        inEntityList.stream().forEach(v -> {
            if (null!=processInstantIdMap && processInstantIdMap.containsKey(v.getId())) {
                v.setProcessInstanceId(processInstantIdMap.get(v.getId()));
            }
        });
        // 生成凭证
        generateVoucher(ids, YesOrNoEnum.YES.getCode());
        // 更新合同状态
        iContractStatusUpdateService.updateHSSBCWRKContractStatus(inEntityList);

        return this.updateBatchById(inEntityList);
    }


    @Override
    public Boolean withdraw(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条数据撤回");
        }
        List<RecyclingEquipmentInEntity> inEntityList = this.listByIds(ids);
        List<RecyclingEquipmentInDetailEntity> inDetailEntityList = Lists.newArrayList();
        inEntityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.SUBMITTED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("只有处理状态为已提交的才可以撤回");
            }
            v.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
            v.setIsGenerateVoucher("0");
            List<RecyclingEquipmentInDetailEntity> tmpList = recyclingEquipmentInDetailService
                    .list(new LambdaQueryWrapper<RecyclingEquipmentInDetailEntity>()
                            .eq(RecyclingEquipmentInDetailEntity::getInboundDate, v.getInboundDate())
                            .eq(RecyclingEquipmentInDetailEntity::getRecycleId,v.getId())
                            .eq(RecyclingEquipmentInDetailEntity::getOrgId, v.getOrgId()));
            inDetailEntityList.addAll(tmpList);
        });
        iApproveService.withdraw(inEntityList.stream().map(RecyclingEquipmentInEntity::getProcessInstanceId).collect(Collectors.toList()));
        //删除凭证
        batchDeleteVoucher(ids);
        //撤回时要删除合同变更表的数据
        if(CollectionUtils.isNotEmpty(inDetailEntityList)){
            List<Long> idList = inDetailEntityList.stream().map(RecyclingEquipmentInDetailEntity::getId).collect(Collectors.toList());
            contractStatusRecordService.remove(
                    new LambdaQueryWrapper<ContractStatusRecordEntity>()
                            .eq(ContractStatusRecordEntity::getSourceFromType, BatchTypeEnum.HSSBCWRK)
                            .in(ContractStatusRecordEntity::getSourceFromId, idList));
        }
        //撤回之后需要将凭证状态改为已录入状态
        iVoucherService.updateStatusByBatch(ids,BatchTypeEnum.HSSBCWRK.getCode(),ProcessStatusEnum.ENTERED.getCode(),"","");
        return this.updateBatchById(inEntityList);
    }

    @Override
    public List<RecyclingEquipmentInVO> listByCondition(RecyclingEquipmentInQueryDTO queryDTO) {
        List<Long> ids = queryDTO.getRecyclingEquipmentInIdList();
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条数据下载");
        }
        LambdaQueryWrapper<RecyclingEquipmentInEntity> queryWrapper = Wrappers.<RecyclingEquipmentInEntity>lambdaQuery();
        queryWrapper.in(RecyclingEquipmentInEntity::getId, ids);
        queryWrapper.orderByDesc(RecyclingEquipmentInEntity::getInboundDate);
        queryWrapper.orderByDesc(RecyclingEquipmentInEntity::getCreateTime);
        List<RecyclingEquipmentInEntity> exportDataList = recyclingEquipmentInMapper.selectList(queryWrapper);
        List<RecyclingEquipmentInVO> inVOList = BeanUtil.copyToList(exportDataList, RecyclingEquipmentInVO.class);
        fillVoList(inVOList);
        return inVOList;
    }

    @Override
    public Boolean removeSummaryAndDetail(List<Long> ids) {
        List<RecyclingEquipmentInEntity> inEntityList = this.listByIds(ids);
        if(CollectionUtil.isEmpty(inEntityList)){
            throw new ServiceException("没有找到对应的入库汇总数据");
        }
        Optional<RecyclingEquipmentInEntity> op = inEntityList.stream().filter(v->!StringUtils.equals(ProcessStatusEnum.ENTERED.getCode() ,v.getProcessStatus())&&StringUtils.equals(ProcessStatusEnum.REJECTED.getCode() ,v.getProcessStatus())).findAny();
        if(op.isPresent()){
            throw new ServiceException("仅能删除状态为 已录入和已拒绝 的数据");
        }
        inEntityList.forEach(inEntity->{
            // 逻辑删除详情表
            recyclingEquipmentInDetailService.remove(new LambdaQueryWrapper<RecyclingEquipmentInDetailEntity>()
                    .eq(RecyclingEquipmentInDetailEntity::getOrgId, inEntity.getOrgId())
                    .in(RecyclingEquipmentInDetailEntity::getRecycleId,ids)
                    .eq(RecyclingEquipmentInDetailEntity::getInboundDate, inEntity.getInboundDate()));
            // 逻辑删除汇总表
            this.removeById(inEntity.getId());
        });
        return true;
    }

    @Override
    public List<RecyclingEquipmentInCheckVO> checkRemainBalance(Long id) {
        RecyclingEquipmentInEntity inEntity = this.getById(id);
        if(inEntity==null){
            throw new ServiceException("没有找到对应的入库汇总数据");
        }
        return recyclingEquipmentInDetailService.getCheckData(inEntity.getInboundDate(), inEntity.getOrgId(),inEntity.getId());


//        List<RecyclingEquipmentInDetailEntity> inDetailEntityList = recyclingEquipmentInDetailService.list(new LambdaQueryWrapper<RecyclingEquipmentInDetailEntity>()
//                .eq(RecyclingEquipmentInDetailEntity::getOrgId, inEntity.getOrgId())
//                .eq(RecyclingEquipmentInDetailEntity::getInboundDate, inEntity.getInboundDate()));
//
//        List<RecyclingEquipmentInCheckVO> checkVOList = Lists.newArrayList();
//        for(RecyclingEquipmentInDetailEntity in : inDetailEntityList){
//            RecyclingEquipmentInCheckVO checkVO = new RecyclingEquipmentInCheckVO();
//            checkVO.setClientName(in.getClientName());
//            checkVO.setOrgId(in.getOrgId());
//            checkVO.setInboundDate(in.getInboundDate());
//            checkVO.setContractCode(in.getContractCode());
//            checkVO.setContractStatus(in.getContractStatus());
//            checkVO.setReceivableRentBalance(in.getReceivableRentBalance());
//            checkVO.setReceivableOuttaxBalance(in.getReceivableOuttaxBalance());
//            checkVO.setUnrealizedRevenueBalance(in.getUnrealizedRevenueBalance());
//            checkVO.setReceivableResidualValueBalance(in.getReceivableResidualValueBalance());
//            checkVO.setLesseeMarginBalance(in.getLesseeMarginBalance());
//            checkVOList.add(checkVO);
//        }
//        return checkVOList;
    }

    private void fillVoList(List<RecyclingEquipmentInVO> inVOList) {
        if(CollectionUtils.isEmpty(inVOList)){
            return;
        }
        Map<String,String> orgNameByOrgIdMap = getOrgNameOrgId();
        Map<String, List<RecyclingEquipmentInVO>> inboundDateMap = new HashMap<>();
        inVOList.stream().forEach(v -> {
            // 签约主体
            if (orgNameByOrgIdMap.containsKey(v.getOrgId())) {
                v.setOrgName(orgNameByOrgIdMap.get(v.getOrgId()));
            }
            // 处理状态
            if(StringUtils.isNotEmpty(v.getProcessStatus())){
                v.setProcessStatusDesc(ProcessStatusEnum.getDescByCode(v.getProcessStatus()));
            }
            // 批次ID
            v.setBatchId(v.getId());
            // 批次类型
            v.setBatchType(BatchTypeEnum.HSSBCWRK.getCode());
        });
     }

    private Map<String,String> getOrgNameOrgId(){
        Map<String,String> orgNameAndIdMap = Maps.newHashMap();
        List<OrgCompanyVO> orgCompanyVOList =  iOrgCompanyService.selectByCondition(new OrgCompanyQueryDTO());
        if (CollectionUtils.isNotEmpty(orgCompanyVOList)) {
            orgNameAndIdMap = orgCompanyVOList.stream().collect(Collectors.toMap(OrgCompanyVO::getOrgId,OrgCompanyVO::getOrgName, (k1,k2)->k2));
        }
        return orgNameAndIdMap;
    }

    /**
     * @description: 更新详情表+删除详情数据生成的凭证
     * @author: zhangli.chen
     **/
    public void batchDeleteVoucher(List<Long> ids){
        //获取所有的凭证Id
        List<RecyclingEquipmentInDetailEntity> detailsEntityList = Lists.newArrayList();
        List<RecyclingEquipmentInEntity> inEntityList = this.listByIds(ids);
        inEntityList.stream().forEach(v -> {
            List<RecyclingEquipmentInDetailEntity> tmpList = recyclingEquipmentInDetailService
                    .list(new LambdaQueryWrapper<RecyclingEquipmentInDetailEntity>()
                            .eq(RecyclingEquipmentInDetailEntity::getInboundDate, v.getInboundDate())
                            .eq(RecyclingEquipmentInDetailEntity::getRecycleId,v.getId())
                            .eq(RecyclingEquipmentInDetailEntity::getOrgId, v.getOrgId()));
            detailsEntityList.addAll(tmpList);
        });
        //逗号拆分
        List<Long> voucherIdList = Lists.newArrayList();
        List<Long> detailIdList = Lists.newArrayList();
        // 详情数据
        detailsEntityList.stream().forEach(v -> {
            if (StringUtils.isNotEmpty(v.getVoucherId())) {
                voucherIdList.addAll(Arrays.stream(v.getVoucherId().split(",")).map(Long::parseLong).collect(Collectors.toList()));
            }
            detailIdList.add(v.getId());
        });
        if (CollectionUtils.isNotEmpty(voucherIdList)) {
            iVoucherService.deleteByIdList(voucherIdList);
        }
        LambdaUpdateWrapper<RecyclingEquipmentInDetailEntity> updateChainWrapper = new LambdaUpdateWrapper<>();
        updateChainWrapper.in(RecyclingEquipmentInDetailEntity::getId, detailIdList)
                .set(RecyclingEquipmentInDetailEntity::getVoucherId, "")
                .set(RecyclingEquipmentInDetailEntity::getErrorInfo,null);
        recyclingEquipmentInDetailService.update(updateChainWrapper);
    }

    /**
     * @description: 债务重组业务-回收设备-财务入库-多行勾选-批量生成凭证
     * @author: zhangli.chen
     **/
    @Override
    public Boolean generateVoucher(List<Long> ids, String isSubmit) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条数据");
        }
        List<Long> inIdList = Lists.newArrayList();
        List<Map<String,Object>> voucherMapList = Lists.newArrayList();
        List<RecyclingEquipmentInDetailEntity> totalInDetailList = Lists.newArrayList();
        List<RecyclingEquipmentInEntity> inEntityList = this.listByIds(ids);
        // 查询汇总表
        inEntityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus()) && !ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("只有处理状态为已录入和已拒绝的才可以生成凭证");
            }
            List<RecyclingEquipmentInDetailEntity> tmpList = recyclingEquipmentInDetailService
                    .list(new LambdaQueryWrapper<RecyclingEquipmentInDetailEntity>()
                            .eq(RecyclingEquipmentInDetailEntity::getInboundDate, v.getInboundDate())
                            .eq(RecyclingEquipmentInDetailEntity::getRecycleId,v.getId())
                            .eq(RecyclingEquipmentInDetailEntity::getOrgId, v.getOrgId()));
            totalInDetailList.addAll(tmpList);
            inIdList.add(v.getId());
            tmpList.stream().forEach(detailEntity->{
                Map<String, Object> dataMap = getVoucherMap(detailEntity, v.getId(), isSubmit);
                voucherMapList.add(dataMap);
            });
        });

        if(CollectionUtils.isEmpty(voucherMapList)){
            throw new ServiceException("没有找到可以生成凭证的数据");
        }
        //提交时修改合同表字段，且往合同历史表变更表里插入数据
        if(StringUtils.equals(YesOrNoEnum.YES.getCode(), isSubmit)){
            List<ContractEntity> contractList = Lists.newArrayList();
            totalInDetailList.stream().forEach(i->{
                ContractDTO dto = iContractService.getContractDTOByCode(i.getContractCode(), i.getOrgId());
                ContractEntity tmpContract = new ContractEntity();
                tmpContract.setId(dto.getId());
                tmpContract.setFinancialContractStatus(i.getFinancialContractStatus());
                tmpContract.setFinancialContractStatusUpdateTime(DateUtils.dateTime("yyyy-MM-dd", i.getInboundDate()));
                contractList.add(tmpContract);
            });
            iContractService.updateBatchById(contractList);
            List<ContractVO> contractVOList = Lists.newArrayList();
            totalInDetailList.stream().forEach(i->{
                ContractDTO dto = iContractService.getContractDTOByCode(i.getContractCode(), i.getOrgId());
                ContractVO vo = new ContractVO();
                BeanUtil.copyProperties(dto, vo);
                vo.setSourceFromId(i.getId());
                vo.setSourceFromType(BatchTypeEnum.HSSBCWRK.getCode());
                contractVOList.add(vo);
            });
            iContractService.saveRecordList(contractVOList);
        }
        //20240426 生成凭证前先删除凭证
        batchDeleteVoucher(ids);
        CompletableFuture<Void> completableFuture = CompletableFuture.supplyAsync(() -> {
            // 异步执行的任务
            log.info("回收设备生成凭证 开始");
            List<VoucherInfoVO> voucherResultList = iRuleService.batchExecuteRule(voucherMapList);
            log.info("回收设备生成凭证 结束");
            log.info("voucherResultList size:{}", voucherResultList.size());
            return voucherResultList;
        }, asyncTaskExecutor).thenAccept((result) -> {
            List<RecyclingEquipmentInDetailEntity> detailList = Lists.newArrayList();
            for (VoucherInfoVO infoVO : result) {
                String voucherIds = "";
                String errorInfo = "";
                if (StringUtils.isNotEmpty(infoVO.getErrorInfo())) {
                    errorInfo = infoVO.getErrorInfo();
                    if (infoVO.getErrorInfo().length()>2000) {
                        errorInfo = infoVO.getErrorInfo().substring(0, 2000);
                    }
                }
                if (CollectionUtils.isNotEmpty(infoVO.getVoucherDTOList())) {
                    voucherIds = infoVO.getVoucherDTOList().stream().map(VoucherDTO::getId).map(String::valueOf).collect(Collectors.toList()).stream().collect(Collectors.joining(","));
                }
                RecyclingEquipmentInDetailEntity detail = new RecyclingEquipmentInDetailEntity();
                detail.setId(Long.parseLong(infoVO.getOrderId()));
                detail.setVoucherId(voucherIds);
                detail.setErrorInfo(errorInfo);
                detailList.add(detail);
                //详细表设置凭证Id
//                recyclingEquipmentInDetailService.lambdaUpdate()
//                        .set(RecyclingEquipmentInDetailEntity::getVoucherId, voucherIds)
//                        .set(RecyclingEquipmentInDetailEntity::getErrorInfo, errorInfo)
//                        .eq(RecyclingEquipmentInDetailEntity::getId,Long.parseLong(infoVO.getOrderId())).update();
            }
            recyclingEquipmentInDetailService.saveOrUpdateBatch(detailList);
            Boolean isExistVoucherError = result.stream().allMatch(v -> StringUtils.isNotEmpty(v.getErrorInfo()));
            if(!isExistVoucherError){
                //设置汇总表是否生成凭证字段
                LambdaUpdateChainWrapper<RecyclingEquipmentInEntity> recyclingEquipmentInEntityLambdaUpdateChainWrapper =
                        this.lambdaUpdate().set(RecyclingEquipmentInEntity::getIsGenerateVoucher, "1").in(RecyclingEquipmentInEntity::getId, inIdList);
                recyclingEquipmentInEntityLambdaUpdateChainWrapper.update();
            }
        });
        return true;
    }

    public static void main(String[] args) {
        String dt = "2024-05-03";
        Date date = DateUtils.dateTime("yyyy-MM-dd", dt);
        System.out.println(date);
    }

    /**
     * 组装凭证dto
     */
    private static Map<String, Object> getVoucherMap(RecyclingEquipmentInDetailEntity v, Long inEntityId, String isSubmit) {
        ExecuteCommonDTO executeCommonDTO = new ExecuteCommonDTO();
        executeCommonDTO.setBusinessCode(BusinessEnum.ZLYW.getCode());
        executeCommonDTO.setBusinessName(BusinessEnum.ZLYW.getDesc());
        executeCommonDTO.setSceneCode(SceneEnum.CWRK.getCode());
        executeCommonDTO.setSceneName(SceneEnum.CWRK.getDesc());
        executeCommonDTO.setOrderId(v.getId().toString());
        executeCommonDTO.setSystemCode(SystemEnum.CWZT.getCode());
        executeCommonDTO.setSystemName(SystemEnum.CWZT.getDesc());
        executeCommonDTO.setOrgId(v.getOrgId());
        executeCommonDTO.setBusinessDate(DateUtil.parseDate(v.getInboundDate()));
        executeCommonDTO.setContractCode(v.getContractCode());
        executeCommonDTO.setClientCode(v.getClientCode());
        executeCommonDTO.setClientName(v.getClientName());
        executeCommonDTO.setFinancialContractStatus(v.getFinancialContractStatus());
        executeCommonDTO.setBatchId(inEntityId);
        executeCommonDTO.setBatchType(BatchTypeEnum.HSSBCWRK.getCode());
        executeCommonDTO.setIsSubmit(isSubmit);
        Map<String, Object> dataMap = BeanUtil.beanToMap(executeCommonDTO);
        dataMap.put("receiveCost", v.getRecyclingEquipmentCost());
        dataMap.put("receivableLeaseBalance", v.getReceivableRentBalance());
        dataMap.put("residualBalance", v.getReceivableResidualValueBalance());
        dataMap.put("receivableOuttaxBalance", v.getReceivableOuttaxBalance());
        dataMap.put("unrealizedRevenueBalance", v.getUnrealizedRevenueBalance());
        dataMap.put("receivableMarginBalance", v.getLesseeMarginBalance());
        dataMap.put("provisionBalance", v.getProvisionForImpairment());
        //例子：入库时间2024-05-13 inboundPeriod 2024.05
        String inboundDate = v.getInboundDate();
        LocalDateTime dt = LocalDateTimeUtil.parse(inboundDate, "yyyy-MM-dd");
        String dtFormat = LocalDateTimeUtil.format(dt, "yyyy.MM");
        dataMap.put("inboundPeriod", dtFormat);
        log.info("生成凭证参数：{}", JSON.toJSONString(dataMap));
        return dataMap;
    }


    /**
     * @description: 获取本次导入合同的各类余额信息
     * @author: zhangli.chen
     **/
    private List<RecyclingEquipmentInDetailEntity> getBalanceInfoForImportContract(List<RecyclingEquipmentInDetailExcelDTO> importExcelDataList) {
        log.info("====>>RecyclingEquipmentInServiceImpl.getBalanceInfoForImportContract==>>00==>>importExcelDataList:{}"+importExcelDataList);
        if(CollectionUtil.isEmpty(importExcelDataList)){
            return Lists.newArrayList();
        }
        List<RecyclingEquipmentInDetailDTO> recyclingEquipmentDetailList = BeanUtil.copyToList(importExcelDataList, RecyclingEquipmentInDetailDTO.class);
        List<RecyclingEquipmentInDetailDTO> insertList = Lists.newArrayList();
        // 查询参数-入库日期
        String inboundDate = null;
        if(CollectionUtil.isEmpty(recyclingEquipmentDetailList)){
            return Lists.newArrayList();
        }else{
            inboundDate = recyclingEquipmentDetailList.get(0).getInboundDate();
        }
        // 查询参数-本次导入签约主体ID列表
        List<String> orgIdList = recyclingEquipmentDetailList.stream().map(RecyclingEquipmentInDetailDTO::getOrgId).distinct().collect(Collectors.toList());
        // 查询参数-本次导入合同编号列表
        List<String> contractCodeList = recyclingEquipmentDetailList.stream().map(RecyclingEquipmentInDetailDTO::getContractCode).distinct().collect(Collectors.toList());
        // 查询余额表信息
        ContractBalanceLastQueryDTO param = new ContractBalanceLastQueryDTO();
        param.setOrgIdList(orgIdList);
        param.setVoucherDate(inboundDate);
        param.setContractCodeList(contractCodeList);
        param.setPeriodCode(Integer.valueOf(inboundDate.replaceAll("-","").substring(0,6)));
        List<ContractBalanceEntity> contractBalanceEntityList = iContractBalanceService.getLastContract(param);
        log.info("====>>RecyclingEquipmentInServiceImpl.getBalanceInfoForImportContract==>>01==>>contractBalanceEntityList:{}"+contractBalanceEntityList);
        // 查询本次导入的合同信息
        List<ContractEntity> contractList = iContractService.list(new LambdaQueryWrapper<ContractEntity>()
                .in(ContractEntity::getOrgId, orgIdList)
                .in(ContractEntity::getContractCode, contractCodeList));
        recyclingEquipmentDetailList.stream().forEach(v -> {
            // 本次导入合同的余额信息
            List<ContractBalanceEntity> importContractBalanceList = Lists.newArrayList();
            if(CollectionUtil.isNotEmpty(contractBalanceEntityList)){
                importContractBalanceList = contractBalanceEntityList.stream().filter(i->StringUtils.equals(i.getOrgId(), v.getOrgId()) && StringUtils.equals(i.getContractCode(), v.getContractCode())).collect(Collectors.toList());
            }
            // 本次导入合同的合同信息
            if(CollectionUtil.isNotEmpty(contractList)){
                ContractEntity contractEntity = contractList.stream().filter(i->StringUtils.equals(i.getOrgId(), v.getOrgId()) && StringUtils.equals(i.getContractCode(), v.getContractCode())).findAny().get();
                //设置客户名称
                if(contractEntity!=null){
                    v.setClientName(contractEntity.getClientName());
                    v.setClientCode(contractEntity.getClientCode());
                }
            }
            // 从contractBalance表查询并计算本次导入合同的各类余额金额
            calDataFromContractBalance(v, importContractBalanceList);
            insertList.add(v);
        });
        // 封装本次导入合同信息
        List<RecyclingEquipmentInDetailEntity> entityList = Lists.newArrayList();
        entityList.addAll(BeanUtil.copyToList(insertList, RecyclingEquipmentInDetailEntity.class));
        return entityList;
    }


    private List<RecyclingEquipmentInDetailEntity> getDetailData(List<RecyclingEquipmentInDetailExcelDTO> importExcelDataList) {
        List<RecyclingEquipmentInDetailDTO> recyclingEquipmentInDetailDTOS = BeanUtil.copyToList(importExcelDataList, RecyclingEquipmentInDetailDTO.class);
        List<RecyclingEquipmentInDetailDTO> insertList = Lists.newArrayList();
        List<RecyclingEquipmentInDetailDTO> updateList = Lists.newArrayList();
        if(CollectionUtil.isEmpty(importExcelDataList)){
            return Lists.newArrayList();
        }
        String inboundDate = recyclingEquipmentInDetailDTOS.get(0).getInboundDate();
        // 本次导入签约主体ID列表
        List<String> orgIdList = recyclingEquipmentInDetailDTOS.stream().map(RecyclingEquipmentInDetailDTO::getOrgId).distinct().collect(Collectors.toList());
        // 本次导入合同编号列表
        List<String> contractCodeList = recyclingEquipmentInDetailDTOS.stream().map(RecyclingEquipmentInDetailDTO::getContractCode).distinct().collect(Collectors.toList());

        ContractBalanceLastQueryDTO param = new ContractBalanceLastQueryDTO();
        param.setOrgIdList(orgIdList);
        param.setVoucherDate(inboundDate);
        param.setContractCodeList(contractCodeList);
        param.setPeriodCode(Integer.valueOf(inboundDate.replaceAll("-","").substring(0,6)));
        List<ContractBalanceEntity> contractBalanceEntityList = iContractBalanceService.getLastContract(param);

        log.info("债务重组业务-回收设备入库上传文件 contractBalanceList:"+contractBalanceEntityList.size());
        if(CollectionUtils.isNotEmpty(contractBalanceEntityList)){
            contractBalanceEntityList.stream().forEach(i->{
                log.info("债务重组业务-回收设备入库上传文件 获取最新的合同余额数据: orgId:{} contractCode:{} receivable_rent_balance:{} receivable_residual_value_balance:{} receivable_outtax_balance:{} unrealized_revenue_balance:{} lessee_margin_balance:{} receive_cost_balance:{} equipment_depreciation_reserves_balance:{}"
                        ,i.getOrgId(),i.getContractCode(),i.getReceivableRentBalance(), i.getReceivableResidualValueBalance(), i.getReceivableOuttaxBalance(), i.getUnrealizedRevenueBalance(), i.getLesseeMarginBalance(), i.getReceiveCostBalance(), i.getEquipmentDepreciationReservesBalance());
            });
        }
        // 查询已完成导入的合同导入信息
        List<RecyclingEquipmentInDetailEntity> oldDetailList = recyclingEquipmentInDetailService.list(new LambdaQueryWrapper<RecyclingEquipmentInDetailEntity>()
                .in(RecyclingEquipmentInDetailEntity::getOrgId, orgIdList)
                .eq(RecyclingEquipmentInDetailEntity::getInboundDate, inboundDate)
                .in(RecyclingEquipmentInDetailEntity::getContractCode, contractCodeList));
        // 查询本次导入的合同信息
        List<ContractEntity> contractList = iContractService.list(new LambdaQueryWrapper<ContractEntity>()
                .in(ContractEntity::getOrgId, orgIdList)
                .in(ContractEntity::getContractCode, contractCodeList));
        recyclingEquipmentInDetailDTOS.stream().forEach(v -> {
            // 先前导入合同的导入信息
            RecyclingEquipmentInDetailEntity oldDetailEntity = null;
            if(CollectionUtil.isNotEmpty(oldDetailList)){
                Optional<RecyclingEquipmentInDetailEntity>  preImportOptional= oldDetailList.stream()
                        .filter(i->StringUtils.equals(i.getContractCode(), v.getContractCode()) && StringUtils.equals(i.getOrgId(), v.getOrgId())).findFirst();
                if(preImportOptional.isPresent()){
                    oldDetailEntity = preImportOptional.get();
                }
            }
            // 本次导入合同的余额信息
            List<ContractBalanceEntity> importContractBalanceList = Lists.newArrayList();
            if(CollectionUtil.isNotEmpty(contractBalanceEntityList)){
                importContractBalanceList = contractBalanceEntityList.stream().filter(i->StringUtils.equals(i.getOrgId(), v.getOrgId()) && StringUtils.equals(i.getContractCode(), v.getContractCode())).collect(Collectors.toList());
            }
            // 本次导入合同的合同信息
            if(CollectionUtil.isNotEmpty(contractList)){
                ContractEntity contractEntity = contractList.stream().filter(i->StringUtils.equals(i.getOrgId(), v.getOrgId()) && StringUtils.equals(i.getContractCode(), v.getContractCode())).findAny().get();
                //设置客户名称
                if(contractEntity!=null){
                    v.setClientName(contractEntity.getClientName());
                    v.setClientCode(contractEntity.getClientCode());
                }
            }
            // 从contractBalance表查询并计算本次导入合同的各类余额金额
            calDataFromContractBalance(v, importContractBalanceList);

            if(oldDetailEntity == null){
                // insert
                insertList.add(v);
            }else{
                // update
                v.setId(oldDetailEntity.getId());
                updateList.add(v);
            }
        });
        // 封装本次导入合同信息
        List<RecyclingEquipmentInDetailEntity> entityList = Lists.newArrayList();
        entityList.addAll(BeanUtil.copyToList(insertList, RecyclingEquipmentInDetailEntity.class));
        entityList.addAll(BeanUtil.copyToList(updateList, RecyclingEquipmentInDetailEntity.class));
        return entityList;
    }

    /**
     * @description: 根据detail对象的入库日期和签约主体刷新对应的汇总表数据
     * @author: zhangli.chen
     **/
    private void freshInData(String inboundDate, List<String> orgIds) {
        // 查询本次导入数据相关的汇总表
        List<RecyclingEquipmentInEntity> sumEntityList = this.list(new LambdaQueryWrapper<RecyclingEquipmentInEntity>()
                .eq(RecyclingEquipmentInEntity::getInboundDate, inboundDate)
                .in(RecyclingEquipmentInEntity::getOrgId, orgIds));
        // 查询本次导入数据相关的明细表
        List<RecyclingEquipmentInDetailEntity> recyclingEquipmentInDetailEntityList =
                recyclingEquipmentInDetailService.list(new LambdaQueryWrapper<RecyclingEquipmentInDetailEntity>()
                        .eq(RecyclingEquipmentInDetailEntity::getInboundDate, inboundDate)
                        .in(RecyclingEquipmentInDetailEntity::getOrgId, orgIds));

        // 详细表数据根据入库日期-签约主体分组
        Map<String,List<RecyclingEquipmentInDetailEntity>> detailEntityMap = recyclingEquipmentInDetailEntityList
                .stream().collect(Collectors.groupingBy(v->v.getInboundDate()+"_"+v.getOrgId()));

        // 汇总表中的签约主体
        List<String> sumOrgIds = sumEntityList.stream().map(RecyclingEquipmentInEntity::getOrgId).distinct().collect(Collectors.toList());

        List<RecyclingEquipmentInEntity> inEntityList = Lists.newArrayList();
        // 判断之前是否有导入-有导入
        if(CollectionUtils.isNotEmpty(sumEntityList)){
            if(!StringUtils.equals(inboundDate, sumEntityList.get(0).getInboundDate())){
                throw new ServiceException("入库日期存在不一致，不能导入");
            }
            RecyclingEquipmentInEntity recyclingEquipmentInEntity = null;
            // 循环本次导入的主体
            for(String orgId: orgIds){
                String key = inboundDate+"_"+orgId;
                List<RecyclingEquipmentInDetailEntity> detailEntityList = detailEntityMap.get(key);
                if(!detailEntityMap.containsKey(key)||CollectionUtils.isEmpty(detailEntityList)){
                    continue;
                }
                RecyclingEquipmentInDetailEntity sumDetailEntity = detailEntityList.stream().reduce((x,y) -> {
                    RecyclingEquipmentInDetailEntity tmp = new RecyclingEquipmentInDetailEntity();
                    tmp.setFinancialExposure(x.getFinancialExposure().add(y.getFinancialExposure()));
                    tmp.setProvisionForImpairment(x.getProvisionForImpairment().add(y.getProvisionForImpairment()));
                    tmp.setRecyclingEquipmentCost(x.getRecyclingEquipmentCost().add(y.getRecyclingEquipmentCost()));
                    return tmp;
                }).get();
                // 会存在本次导入会有新的主体信息导入。--如果是新的主体
                if(!sumOrgIds.contains(orgId)){
                    //insert
                    recyclingEquipmentInEntity = new RecyclingEquipmentInEntity();
                    recyclingEquipmentInEntity.setInboundDate(inboundDate);
                    recyclingEquipmentInEntity.setOrgId(orgId);
                    recyclingEquipmentInEntity.setFinancialExposure(sumDetailEntity.getFinancialExposure());
                    recyclingEquipmentInEntity.setRecyclingEquipmentCost(sumDetailEntity.getRecyclingEquipmentCost());
                    recyclingEquipmentInEntity.setProvisionForImpairment(sumDetailEntity.getProvisionForImpairment());
                    recyclingEquipmentInEntity.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
                    recyclingEquipmentInEntity.setIsGenerateVoucher("0");
                }else{
                    //update
                    recyclingEquipmentInEntity = sumEntityList.stream()
                            .filter(v -> v.getInboundDate().compareTo(inboundDate)==0&&StringUtils.equals(v.getOrgId(), orgId)).findFirst().get();
                    recyclingEquipmentInEntity.setFinancialExposure(sumDetailEntity.getFinancialExposure());
                    recyclingEquipmentInEntity.setRecyclingEquipmentCost(sumDetailEntity.getRecyclingEquipmentCost());
                    recyclingEquipmentInEntity.setProvisionForImpairment(sumDetailEntity.getProvisionForImpairment());
                }
                inEntityList.add(recyclingEquipmentInEntity);
            }
        }else{
            // 判断之前是否有导入-第一次导入
            for(String orgId : orgIds){
                String key = inboundDate+"_"+orgId;
                List<RecyclingEquipmentInDetailEntity> detailEntityList = detailEntityMap.get(key);
                if(!detailEntityMap.containsKey(key)||CollectionUtils.isEmpty(detailEntityList)){
                    continue;
                }
                RecyclingEquipmentInDetailEntity sumDetailEntity = detailEntityList.stream().reduce((x,y) -> {
                    RecyclingEquipmentInDetailEntity tmp = new RecyclingEquipmentInDetailEntity();
                    tmp.setFinancialExposure(x.getFinancialExposure().add(y.getFinancialExposure()));
                    tmp.setProvisionForImpairment(x.getProvisionForImpairment().add(y.getProvisionForImpairment()));
                    tmp.setRecyclingEquipmentCost(x.getRecyclingEquipmentCost().add(y.getRecyclingEquipmentCost()));
                    return tmp;
                }).get();
                RecyclingEquipmentInEntity recyclingEquipmentInEntity = new RecyclingEquipmentInEntity();
                recyclingEquipmentInEntity.setInboundDate(inboundDate);
                recyclingEquipmentInEntity.setOrgId(orgId);
                recyclingEquipmentInEntity.setFinancialExposure(sumDetailEntity.getFinancialExposure());
                recyclingEquipmentInEntity.setRecyclingEquipmentCost(sumDetailEntity.getRecyclingEquipmentCost());
                recyclingEquipmentInEntity.setProvisionForImpairment(sumDetailEntity.getProvisionForImpairment());
                recyclingEquipmentInEntity.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
                recyclingEquipmentInEntity.setIsGenerateVoucher("0");
                inEntityList.add(recyclingEquipmentInEntity);
            }
        }
        this.saveOrUpdateBatch(inEntityList);
    }

    /**
     * @description: 从contractBalance表查询并计算本次导入合同的各类余额金额
     * @author: zhangli.chen
     **/
    private static void calDataFromContractBalance(RecyclingEquipmentInDetailDTO v, List<ContractBalanceEntity> contractBalanceEntityList) {
        if(CollectionUtils.isNotEmpty(contractBalanceEntityList)){
            contractBalanceEntityList.stream().forEach(i->{
                log.info("债务重组业务-回收设备入库上传文件 计算合同余额表数据: orgId:{} contractCode:{} receivable_rent_balance:{} receivable_residual_value_balance:{} receivable_outtax_balance:{} unrealized_revenue_balance:{} lessee_margin_balance:{} receive_cost_balance:{} equipment_depreciation_reserves_balance:{}"
                        ,i.getOrgId(),i.getContractCode(),i.getReceivableRentBalance(), i.getReceivableResidualValueBalance(), i.getReceivableOuttaxBalance(), i.getUnrealizedRevenueBalance(), i.getLesseeMarginBalance(), i.getReceiveCostBalance(), i.getEquipmentDepreciationReservesBalance());
            });
        }
        //应收租金
        BigDecimal receivableRentBalance = new BigDecimal(0);
        //应收期末残值
        BigDecimal receivableResidualValueBalance = new BigDecimal(0);
        //应收销项税
        BigDecimal receivableOuttaxBalance = new BigDecimal(0);
        //未实现收益
        BigDecimal unrealizedRevenueBalance = new BigDecimal(0);
        //承租人保证金
        BigDecimal lesseeMarginBalance = new BigDecimal(0);
        //财务敞口
        BigDecimal financialExposure = new BigDecimal(0);
        //入库时计提减值
        BigDecimal provisionForImpairment = new BigDecimal(0);
        if(CollectionUtils.isNotEmpty(contractBalanceEntityList)){
            Optional<ContractBalanceEntity> sumOp = contractBalanceEntityList.stream().reduce(
                    (x,y)->{
                        ContractBalanceEntity tmp = new ContractBalanceEntity();
                        tmp.setReceivableRentBalance(x.getReceivableRentBalance().add(y.getReceivableRentBalance()));
                        tmp.setReceivableResidualValueBalance(x.getReceivableResidualValueBalance().add(y.getReceivableResidualValueBalance()));
                        tmp.setReceivableOuttaxBalance(x.getReceivableOuttaxBalance().add(y.getReceivableOuttaxBalance()));
                        tmp.setUnrealizedRevenueBalance(x.getUnrealizedRevenueBalance().add(y.getUnrealizedRevenueBalance()));
                        tmp.setLesseeMarginBalance(x.getLesseeMarginBalance().add(y.getLesseeMarginBalance()));
                        return tmp;
                    }
            );
            if(sumOp.isPresent()){
                ContractBalanceEntity sumEntity = sumOp.get();
                // 应收租金余额
                receivableRentBalance = sumEntity.getReceivableRentBalance();
                // 应收期末残值余额
                receivableResidualValueBalance = sumEntity.getReceivableResidualValueBalance();
                // 应收销项税余额
                receivableOuttaxBalance = sumEntity.getReceivableOuttaxBalance();
                // 未实现收益余额
                unrealizedRevenueBalance = sumEntity.getUnrealizedRevenueBalance();
                // 承租人保证金余额
                lesseeMarginBalance = sumEntity.getLesseeMarginBalance();
                // 财务敞口 = (应收租金余额+应收期末残值余额+应收销项税余额) - 未实现收益余额 - 承租人保证金余额
                financialExposure = NumberUtil.sub(NumberUtil.add(receivableRentBalance, receivableResidualValueBalance, receivableOuttaxBalance), unrealizedRevenueBalance, lesseeMarginBalance);
                // 入库时计提减值 = 财务敞口 - 本次回收设备成本
                provisionForImpairment = NumberUtil.sub(financialExposure, v.getRecyclingEquipmentCost());
                v.setReceivableRentBalance(receivableRentBalance);
                v.setReceivableResidualValueBalance(receivableResidualValueBalance);
                v.setReceivableOuttaxBalance(receivableOuttaxBalance);
                v.setUnrealizedRevenueBalance(unrealizedRevenueBalance);
                v.setLesseeMarginBalance(lesseeMarginBalance);
                // 财务敞口
                v.setFinancialExposure(financialExposure);
                // 入库时计提减值
                v.setProvisionForImpairment(provisionForImpairment);
            }
        }else{
            v.setReceivableRentBalance(receivableRentBalance);
            v.setReceivableResidualValueBalance(receivableResidualValueBalance);
            v.setReceivableOuttaxBalance(receivableOuttaxBalance);
            v.setUnrealizedRevenueBalance(unrealizedRevenueBalance);
            v.setLesseeMarginBalance(lesseeMarginBalance);
            v.setFinancialExposure(financialExposure);
            v.setProvisionForImpairment(financialExposure.subtract(v.getRecyclingEquipmentCost()));
        }
        log.info("债务重组业务-回收设备入库上传文件设置余额完毕, detailVo:{}",v.toString());
    }

    /**
     * @description: 校验导入的数据是否合规-支持多签约主体+单入库日期
     * @author: zhangli.chen
     **/
    private void checkImportData(List<RecyclingEquipmentInDetailExcelDTO> importExcelDataList) {
        // 校验导入数据不能为空
        if (CollectionUtils.isEmpty(importExcelDataList)) {
            throw new ServiceException(ResultEnum.DR_RE_NO_DATA_UPLOAD.getMessage());
        }
        //  查询系统签约主体
        Map<String,String> orgIdMap = iOrgCompanyService.list().stream().collect(HashMap::new,(h, v)->h.put(v.getOrgName(),v.getOrgId()),HashMap::putAll);
        // 导入签约主体ID数组
        List<String> orgIds = Lists.newArrayList();
        importExcelDataList.stream().forEach(v -> {
            if (orgIdMap.containsKey(v.getOrgName())) {
                v.setOrgId(orgIdMap.get(v.getOrgName()));
            }
            if(!orgIds.contains(v.getOrgId())){
                orgIds.add(v.getOrgId());
            }
        });
        // 入库日期
        String inboundDate = importExcelDataList.get(0).getInboundDate();
        // 校验同一个入库日期下是否有非已录入状态下的数据，若有则不能再次导入
//        List<RecyclingEquipmentInEntity> inEntityList =
//                this.list(new LambdaQueryWrapper<RecyclingEquipmentInEntity>()
//                        .eq(RecyclingEquipmentInEntity::getInboundDate, inboundDate)
//                        .in(RecyclingEquipmentInEntity::getOrgId, orgIds));
//        Optional<RecyclingEquipmentInEntity> op = inEntityList.stream().filter(i->
//                !StringUtils.equals(i.getProcessStatus(), ProcessStatusEnum.ENTERED.getCode())).findAny();
//        if(op.isPresent()){
//            throw new ServiceException("导入的回收设备入库数据包括状态不是已录入的数据");
//        }
        // add by zhangli.chen for 新增校验若本次上传的数据系统中已经存同一财务入库日期+同一主体的数据+同一合同号不论其是否已经提交凭证，则系统校验不允许此次上传操作
        List<RecyclingEquipmentInDetailEntity> detailEntityList =
                recyclingEquipmentInDetailService.list(new LambdaQueryWrapper<RecyclingEquipmentInDetailEntity>()
                        .eq(RecyclingEquipmentInDetailEntity::getInboundDate, inboundDate)
                        .in(RecyclingEquipmentInDetailEntity::getOrgId, orgIds));
        importExcelDataList.stream().forEach(v -> {
            if(StringUtils.isEmpty(v.getOrgName())){
                throw new ServiceException("签约主体不可以为空");
            }
            if(ObjectUtil.isEmpty(v.getInboundDate())){
                throw new ServiceException("入库日期不可以为空");
            }
            if(StringUtils.isEmpty(v.getContractCode())){
                throw new ServiceException("合同编号不可以为空");
            }
            if(StringUtils.isEmpty(v.getFinancialContractStatus())){
                throw new ServiceException("财务合同状态不可以为空");
            }
            if(ObjectUtil.isEmpty(v.getRecyclingEquipmentCost())){
                throw new ServiceException("回收设备成本不可以为空");
            }
            if(StringUtils.isEmpty(v.getOrgId())){
                throw new ServiceException("签约主体在系统中不存在");
            }
            // 强限制每次导入只能导入同一个入库日期的数据
            if(inboundDate.compareTo(v.getInboundDate())!=0){
                throw new ServiceException("入库日期存在不一致，不能导入");
            }
            if(detailEntityList!=null && detailEntityList.size()>0){
                List<RecyclingEquipmentInDetailEntity> filteredList = detailEntityList.stream()
                        .filter(entity -> v.getContractCode().equals(entity.getContractCode()))
                        .collect(Collectors.toList());
                if(!filteredList.isEmpty()){
                    throw new ServiceException(PromptMessageUtil.promptMessageFormat(ResultEnum.DR_RE_CONTRACT_HAS_BEEN_UPLOADED,v.getContractCode(),v.getInboundDate()));
                }
            }
        });
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
        RecyclingEquipmentInEntity entity = this.getById(approveDTO.getDocumentId());
        if (ObjectUtil.isEmpty(entity)) {
            throw new ServiceException("转入登记数据不存在");
        }
        if (ProcessStatusEnum.REJECTED.getCode().equals(approveDTO.getDocumentStatus())) {
            // 驳回 删除凭证
            batchDeleteVoucher(Arrays.asList(entity.getId()));
//            entity.setVoucherId("");
            entity.setIsGenerateVoucher(YesOrNoEnum.NO.getCode());
            List<RecyclingEquipmentInDetailEntity> detailEntityList =
                    recyclingEquipmentInDetailService.list(new LambdaQueryWrapper<RecyclingEquipmentInDetailEntity>()
                            .eq(RecyclingEquipmentInDetailEntity::getInboundDate, entity.getInboundDate())
                            .eq(RecyclingEquipmentInDetailEntity::getRecycleId,entity.getId())
                            .eq(RecyclingEquipmentInDetailEntity::getOrgId, entity.getOrgId()));
            if(CollectionUtil.isNotEmpty(detailEntityList)){
                detailEntityList.stream().forEach(i->i.setVoucherId(""));
                recyclingEquipmentInDetailService.saveOrUpdateBatch(detailEntityList);
                //拒绝时要删除合同变更表的数据
                List<Long> idList = detailEntityList.stream().map(RecyclingEquipmentInDetailEntity::getId).collect(Collectors.toList());
                contractStatusRecordService.remove(
                        new LambdaQueryWrapper<ContractStatusRecordEntity>()
                                .eq(ContractStatusRecordEntity::getSourceFromType, BatchTypeEnum.HSSBCWRK)
                                .in(ContractStatusRecordEntity::getSourceFromId, idList));
            }
        }
        //修改凭证状态
        String processStatus = entity.getProcessStatus();
        if (ProcessStatusEnum.REVIEWED.getCode().equals(approveDTO.getDocumentStatus())) {
            processStatus = ProcessStatusEnum.REVIEWED.getCode();
        } else if (ProcessStatusEnum.REJECTED.getCode().equals(approveDTO.getDocumentStatus())) {
            processStatus = ProcessStatusEnum.REJECTED.getCode();
        }
        iVoucherService.updateStatusByBatch(com.google.common.collect.Lists.newArrayList(entity.getId()),BatchTypeEnum.HSSBCWRK.getCode(),processStatus,approveDTO.getApproverNum(),approveDTO.getApproverName());
        // 通过，直接修改状态
        entity.setProcessStatus(approveDTO.getDocumentStatus());
        this.updateById(entity);
    }

}

