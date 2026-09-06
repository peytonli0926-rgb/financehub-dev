package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.google.common.collect.Maps;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.StringUtils;
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
import com.utfinancing.financehub.engine.finance.model.vo.OrgCompanyVO;
import com.utfinancing.financehub.engine.finance.model.vo.RecyclingEquipmentInVO;
import com.utfinancing.financehub.engine.finance.model.vo.RecyclingEquipmentOutDetailVO;
import com.utfinancing.financehub.engine.finance.mapper.RecyclingEquipmentOutDetailMapper;
import com.utfinancing.financehub.engine.finance.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import com.utfinancing.financehub.engine.rule.model.dto.ExecuteCommonDTO;
import com.utfinancing.financehub.engine.rule.model.vo.VoucherInfoVO;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import com.utfinancing.financehub.engine.utils.CommonDateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author : jnc
 * @Date : Create in 2024-03-07
 * @Description :  RecyclingEquipmentOutDetail服务实现类
 * @Modified :
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class RecyclingEquipmentOutDetailServiceImpl extends ServiceImpl<RecyclingEquipmentOutDetailMapper, RecyclingEquipmentOutDetailEntity> implements IRecyclingEquipmentOutDetailService {

    private final RecyclingEquipmentOutDetailMapper recyclingEquipmentOutDetailMapper;

    @Resource
    private IOrgCompanyService iOrgCompanyService;

    @Resource
    private IContractBalanceService iContractBalanceService;

    @Resource
    private IContractService iContractService;

    // @Resource
    private final IRuleService iRuleService;

    @Value("${approve.url.recyclingEquipOut-url:null}")
    private String approveUrl;

    @Resource
    private IVoucherService iVoucherService;

    @Resource
    private IApproveService iApproveService;

    @Override
    public Long saveRecyclingEquipmentOutDetail(RecyclingEquipmentOutDetailDTO dto) {
        RecyclingEquipmentOutDetailEntity entity = BeanUtil.copyProperties(dto, RecyclingEquipmentOutDetailEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateRecyclingEquipmentOutDetail(Long id, RecyclingEquipmentOutDetailDTO dto) {
        RecyclingEquipmentOutDetailEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public RecyclingEquipmentOutDetailDTO getRecyclingEquipmentOutDetailDTOById(Long id) {
        RecyclingEquipmentOutDetailEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, RecyclingEquipmentOutDetailDTO.class);
    }

    @Override
    public IPage<RecyclingEquipmentOutDetailVO> selectPage(RecyclingEquipmentOutDetailQueryDTO queryDTO) {
        LambdaQueryWrapper<RecyclingEquipmentOutDetailEntity> queryWrapper = Wrappers.<RecyclingEquipmentOutDetailEntity>lambdaQuery();
        if(StringUtils.isNotEmpty(queryDTO.getOutboundDateStart())){
            queryWrapper.apply(" outbound_date >= {0}", queryDTO.getOutboundDateStart());
        }
        if(StringUtils.isNotEmpty(queryDTO.getOutboundDateEnd())){
            queryWrapper.apply(" outbound_date <= {0}", queryDTO.getOutboundDateEnd());
        }
        if(CollectionUtil.isNotEmpty(queryDTO.getOrgIds())){
            queryWrapper.in(RecyclingEquipmentOutDetailEntity::getOrgId, queryDTO.getOrgIds());
        }
        if(StringUtils.isNotEmpty(queryDTO.getContractCode())){
            queryWrapper.like(RecyclingEquipmentOutDetailEntity::getContractCode, queryDTO.getContractCode());
        }
        if(StringUtils.isNotEmpty(queryDTO.getClientName())){
            queryWrapper.like(RecyclingEquipmentOutDetailEntity::getClientName, queryDTO.getClientName());
        }
        queryWrapper.orderByDesc(RecyclingEquipmentOutDetailEntity::getOutboundDate);

        //这里注入查询条件
        IPage<RecyclingEquipmentOutDetailEntity> entityIPage = recyclingEquipmentOutDetailMapper.selectPage(new Page<RecyclingEquipmentOutDetailEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        IPage<RecyclingEquipmentOutDetailVO> outVoPage =  ListBeanUtil.copyPage(entityIPage, RecyclingEquipmentOutDetailVO.class);
        fillVoList(outVoPage.getRecords());
        return outVoPage;
    }

    @Override
    public Boolean importTemplate(MultipartFile file) {
        try {
            ExcelUtil<RecyclingEquipmentOutDetailExcelDTO> util = new ExcelUtil<RecyclingEquipmentOutDetailExcelDTO>(RecyclingEquipmentOutDetailExcelDTO.class);
            List<RecyclingEquipmentOutDetailExcelDTO> recyclingEquipmentOutDetailExcelDTOS = util.importExcel(file.getInputStream());
            /**
             * 1.校验文件数据
             * 2.判断数据是做insert还是update
             * 3.重新生成凭证??
             */
            /**
             * 1.校验文件数据
             */
            checkImportData(recyclingEquipmentOutDetailExcelDTOS);

            /**
             * 2.判断数据是做insert还是update，并根据contract和contract_balance表填入对应金额
             */
            List<RecyclingEquipmentOutDetailEntity> entityList = getDetailData(recyclingEquipmentOutDetailExcelDTOS);

            if(CollectionUtils.isEmpty(entityList)){
                throw new ServiceException("找不到需要修改或更新的数据");
            }
            this.saveOrUpdateBatch(entityList);

        } catch (ServiceException serviceException) {
            throw new ServiceException("导入回收设备出库数据失败，失败原因："+serviceException.getMessage());
        } catch (Exception exception) {
            throw new ServiceException("导入回收设备出库数据失败，失败原因："+exception.getMessage());
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean submit(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条数据提交");
        }
        List<RecyclingEquipmentOutDetailEntity> outDetailEntityList = this.listByIds(ids);
        List<ApproveDTO> approveDTOList = Lists.newArrayList();
        outDetailEntityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus()) && !ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("只有处理状态为已录入和已拒绝的才可以提交");
            }
            v.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
            ApproveDTO approveDTO = new ApproveDTO();
            approveDTO.setDocumentId(v.getId());
            approveDTO.setDocumentType(BatchTypeEnum.HSSBCWCK.getCode());
            approveDTO.setUrl(approveUrl+"id="+v.getId()+"&tab=outbound");
            approveDTOList.add(approveDTO);
        });
        //发送审核
        Map<Long,Long> processInstantIdMap = iApproveService.submit(approveDTOList);
        outDetailEntityList.stream().forEach(v -> {
            if (null!=processInstantIdMap && processInstantIdMap.containsKey(v.getId())) {
                v.setProcessInstanceId(processInstantIdMap.get(v.getId()));
            }
        });
        generateVoucher(ids, YesOrNoEnum.YES.getCode());
        return this.updateBatchById(outDetailEntityList);
    }

    @Override
    public Boolean generateVoucher(List<Long> ids, String code) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条数据");
        }
        List<RecyclingEquipmentOutDetailEntity> outDetailEntityList = this.listByIds(ids);
        if(CollectionUtils.isEmpty(outDetailEntityList)){
            throw new ServiceException("没有找到可以生成凭证的数据");
        }
        List<Map<String,Object>> voucherMapList = Lists.newArrayList();

        outDetailEntityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus()) && !ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("只有处理状态为已录入和已拒绝才可以生成凭证");
            }
            /**
             * 组装凭证dto
             * TODO
             */

            ExecuteCommonDTO executeCommonDTO = new ExecuteCommonDTO();
            executeCommonDTO.setSceneCode(SceneEnum.CWCK.getCode());
            executeCommonDTO.setSceneName(SceneEnum.CWCK.getDesc());
            executeCommonDTO.setOrderId(v.getId().toString());
            executeCommonDTO.setSystemCode(SystemEnum.CWZT.getCode());
            executeCommonDTO.setSystemName(SystemEnum.CWZT.getDesc());
            executeCommonDTO.setOrgId(v.getOrgId());
            executeCommonDTO.setBusinessDate(DateUtil.parseDate(v.getOutboundDate()));
            executeCommonDTO.setContractCode(v.getContractCode());
            executeCommonDTO.setClientCode(v.getClientCode());
            executeCommonDTO.setClientName(v.getClientName());

            Map<String, Object> dataMap = BeanUtil.beanToMap(executeCommonDTO);
            dataMap.put("receiveCost", v.getRecyclingEquipmentCost());
            dataMap.put("equipmentDepreciationReserves", v.getProvisionForImpairment());

            log.info("生成凭证参数：{}", JSON.toJSONString(dataMap));
            voucherMapList.add(dataMap);
        });

        //20240426 生成凭证前先删除凭证
        batchDeleteVoucher(ids);

        List<VoucherInfoVO> voucherResultList = iRuleService.batchExecuteRule(voucherMapList);
        for (VoucherInfoVO infoVO : voucherResultList) {
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

            //详细表设置凭证Id
            this.lambdaUpdate()
                    .set(RecyclingEquipmentOutDetailEntity::getVoucherId, voucherIds)
                    .set(RecyclingEquipmentOutDetailEntity::getErrorInfo, errorInfo)
                    .eq(RecyclingEquipmentOutDetailEntity::getId,Long.parseLong(infoVO.getOrderId())).update();
        }
        Boolean isExistVoucherError = voucherResultList.stream().allMatch(v -> StringUtils.isNotEmpty(v.getErrorInfo()));
        if(!isExistVoucherError){
            LambdaUpdateChainWrapper<RecyclingEquipmentOutDetailEntity> recyclingEquipmentOutDetailEntityLambdaUpdateChainWrapper =
                    this.lambdaUpdate().set(RecyclingEquipmentOutDetailEntity::getIsGenerateVoucher, "1").in(RecyclingEquipmentOutDetailEntity::getId, ids);
            return recyclingEquipmentOutDetailEntityLambdaUpdateChainWrapper.update();
        }
        return false;
    }

    @Override
    public Boolean withdraw(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条数据撤回");
        }
        List<RecyclingEquipmentOutDetailEntity> outDetailEntityList = this.listByIds(ids);
        outDetailEntityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.SUBMITTED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("只有处理状态为已提交的才可以撤回");
            }
            v.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
            v.setIsGenerateVoucher("0");
        });
        iApproveService.withdraw(outDetailEntityList.stream().map(RecyclingEquipmentOutDetailEntity::getProcessInstanceId).collect(Collectors.toList()));
        //删除凭证
        batchDeleteVoucher(ids);

        //撤回之后需要将凭证状态改为已录入状态
        iVoucherService.updateStatusByBatch(ids,BatchTypeEnum.HSSBCWCK.getCode(),ProcessStatusEnum.ENTERED.getCode(),"","");
        return this.updateBatchById(outDetailEntityList);
    }

    @Override
    public List<RecyclingEquipmentOutDetailVO> listByCondition(RecyclingEquipmentOutDetailQueryDTO queryDTO) {
//        List<Long> ids = queryDTO.getRecyclingEquipmentOutIdList();
//        if (CollectionUtils.isEmpty(ids)) {
//            throw new ServiceException("请至少勾选一条数据下载");
//        }
        LambdaQueryWrapper<RecyclingEquipmentOutDetailEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RecyclingEquipmentOutDetailEntity::getDelFlag, "0");
        wrapper.eq(RecyclingEquipmentOutDetailEntity::getOutboundDate, queryDTO.getOutboundDate());
        wrapper.eq(RecyclingEquipmentOutDetailEntity::getOrgId, queryDTO.getOrgId());
        if (StringUtils.isNotEmpty(queryDTO.getContractCode())) {
            wrapper.like(RecyclingEquipmentOutDetailEntity::getContractCode, queryDTO.getContractCode());
        }
        if (StringUtils.isNotEmpty(queryDTO.getClientName())) {
            wrapper.like(RecyclingEquipmentOutDetailEntity::getClientName, queryDTO.getClientName());
        }
        List<RecyclingEquipmentOutDetailVO> outVOList = BeanUtil.copyToList(this.list(wrapper), RecyclingEquipmentOutDetailVO.class);
        fillVoList(outVOList);
        return outVOList;
    }

    @Override
    public Boolean removeDetail(List<Long> ids) {
        List<RecyclingEquipmentOutDetailEntity> outDetailList = this.listByIds(ids);
        
        if(CollectionUtil.isEmpty(outDetailList)){
            throw new ServiceException("没有找到对应的出库明细数据");
        }
        Optional<RecyclingEquipmentOutDetailEntity> op = outDetailList.stream().filter(v->!StringUtils.equals(ProcessStatusEnum.ENTERED.getCode() ,v.getProcessStatus())
                &&StringUtils.equals(ProcessStatusEnum.REJECTED.getCode() ,v.getProcessStatus())).findAny();
        if(op.isPresent()){
            throw new ServiceException("仅能删除状态为 已录入和已拒绝 的数据");
        }
        return this.removeBatchByIds(ids);
    }

    private void fillVoList(List<RecyclingEquipmentOutDetailVO> outVOList) {
        if(CollectionUtils.isEmpty(outVOList)){
            return;
        }
        Map<String,String> orgNameByOrgIdMap = getOrgNameOrgId();
        outVOList.stream().forEach(v -> {
            if (orgNameByOrgIdMap.containsKey(v.getOrgId())) {
                v.setOrgName(orgNameByOrgIdMap.get(v.getOrgId()));
            }
            if(StringUtils.isNotEmpty(v.getProcessStatus())){
                v.setProcessStatusDesc(ProcessStatusEnum.getDescByCode(v.getProcessStatus()));
            }
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

    public void batchDeleteVoucher(List<Long> ids){
        //获取所有的凭证Id
        List<RecyclingEquipmentOutDetailEntity> detailsEntityList = this.listByIds(ids);
        //逗号拆分
        List<Long> voucherIdList = Lists.newArrayList();
        detailsEntityList.stream().forEach(v -> {
            if (StringUtils.isNotEmpty(v.getVoucherId())) {
                voucherIdList.addAll(Arrays.stream(v.getVoucherId().split(",")).map(Long::parseLong).collect(Collectors.toList()));
            }
            v.setVoucherId("");
        });
        if (CollectionUtils.isNotEmpty(voucherIdList)) {
            iVoucherService.deleteByIdList(voucherIdList);
        }
        this.updateBatchById(detailsEntityList);
    }

    private List<RecyclingEquipmentOutDetailEntity> getDetailData(List<RecyclingEquipmentOutDetailExcelDTO> recyclingEquipmentOutDetailExcelDTOS) {

        List<RecyclingEquipmentOutDetailEntity> outDetailEntityList = Lists.newArrayList();
        List<RecyclingEquipmentOutDetailDTO> recyclingEquipmentOutDetailDTOS = BeanUtil.copyToList(recyclingEquipmentOutDetailExcelDTOS, RecyclingEquipmentOutDetailDTO.class);


//        List<ContractBalanceEntity> contractBalanceEntityList = iContractBalanceService.list(new LambdaQueryWrapper<ContractBalanceEntity>()
//                .eq(ContractBalanceEntity::getOrgId, v.getOrgId())
//                .eq(ContractBalanceEntity::getContractCode, v.getContractCode())
//                .apply("to_char(voucher_date, 'YYYY-MM-dd') = '"+v.getOutboundDate()+"'"));
        String outboundDate = recyclingEquipmentOutDetailDTOS.get(0).getOutboundDate();
        List<String> orgIdList = recyclingEquipmentOutDetailDTOS.stream().map(RecyclingEquipmentOutDetailDTO::getOrgId).distinct().collect(Collectors.toList());
        List<String> contractCodeList = recyclingEquipmentOutDetailDTOS.stream().map(RecyclingEquipmentOutDetailDTO::getContractCode).distinct().collect(Collectors.toList());
        ContractBalanceLastQueryDTO param = new ContractBalanceLastQueryDTO();
        param.setOrgIdList(orgIdList);
        param.setVoucherDate(outboundDate);
        param.setContractCodeList(contractCodeList);
        param.setPeriodCode(Integer.valueOf(outboundDate.replaceAll("-","").substring(0,6)));
        List<ContractBalanceEntity> contractBalanceEntityList = iContractBalanceService.getLastContract(param);


        List<RecyclingEquipmentOutDetailEntity> oldDetailList = this.list(new LambdaQueryWrapper<RecyclingEquipmentOutDetailEntity>()
                .in(RecyclingEquipmentOutDetailEntity::getOrgId, orgIdList)
                .eq(RecyclingEquipmentOutDetailEntity::getOutboundDate, outboundDate)
                .in(RecyclingEquipmentOutDetailEntity::getContractCode, contractCodeList));

        List<ContractEntity> contractList = iContractService.list(new LambdaQueryWrapper<ContractEntity>()
                .in(ContractEntity::getOrgId, orgIdList)
                .in(ContractEntity::getContractCode, contractCodeList));
        recyclingEquipmentOutDetailDTOS.stream().forEach(v -> {
//            ContractEntity contractEntity = iContractService.getOne(new LambdaQueryWrapper<ContractEntity>()
//                    .eq(ContractEntity::getOrgId, v.getOrgId())
//                    .eq(ContractEntity::getContractCode, v.getContractCode()));

//            RecyclingEquipmentOutDetailEntity detailEntity = this.getOne(new LambdaQueryWrapper<RecyclingEquipmentOutDetailEntity>()
//                    .eq(RecyclingEquipmentOutDetailEntity::getOrgId, v.getOrgId())
//                    .eq(RecyclingEquipmentOutDetailEntity::getContractCode, v.getContractCode())
//                    .eq(RecyclingEquipmentOutDetailEntity::getOutboundDate, v.getOutboundDate()));

//            List<ContractBalanceEntity> contractBalanceEntityList = iContractBalanceService.list(new LambdaQueryWrapper<ContractBalanceEntity>()
//                    .eq(ContractBalanceEntity::getOrgId, v.getOrgId())
//                    .eq(ContractBalanceEntity::getContractCode, v.getContractCode())
//                    .apply("to_char(voucher_date, 'YYYY-MM-dd') = '"+v.getOutboundDate()+"'"));
            ContractEntity contractEntity = contractList.stream().filter(i->StringUtils.equals(i.getOrgId(), v.getOrgId())&&StringUtils.equals(i.getContractCode(), v.getContractCode())).findAny().get();

            RecyclingEquipmentOutDetailEntity oldDetailEntity = null;
            Optional<RecyclingEquipmentOutDetailEntity> oldOp = oldDetailList.stream()
                    .filter(i->StringUtils.equals(i.getContractCode(), v.getContractCode())&&StringUtils.equals(i.getOrgId(), v.getOrgId())).findFirst();
            if(oldOp.isPresent()){
                oldDetailEntity = oldOp.get();
            }

            List<ContractBalanceEntity> tmpList = Lists.newArrayList();
            if(CollectionUtil.isNotEmpty(contractBalanceEntityList)){
                tmpList = contractBalanceEntityList.stream().filter(i->StringUtils.equals(i.getOrgId(), v.getOrgId())&&StringUtils.equals(i.getContractCode(), v.getContractCode())).collect(Collectors.toList());
            }            RecyclingEquipmentOutDetailEntity tmp = calDetailData(v, tmpList, oldDetailEntity, contractEntity);
            outDetailEntityList.add(tmp);
        });
        return outDetailEntityList;
    }

    private static RecyclingEquipmentOutDetailEntity calDetailData(RecyclingEquipmentOutDetailDTO v, List<ContractBalanceEntity> contractBalanceEntityList, RecyclingEquipmentOutDetailEntity detailEntity, ContractEntity contractEntity) {
        //回收设备成本
        BigDecimal receiveCostBalance = new BigDecimal(0);
        //回收设备减值
        BigDecimal equipmentDepreciationReservesBalance = new BigDecimal(0);

        if(CollectionUtils.isNotEmpty(contractBalanceEntityList)){
            Optional<ContractBalanceEntity> sumOp = contractBalanceEntityList.stream().reduce(
                    (x,y)->{
                        ContractBalanceEntity tmp = new ContractBalanceEntity();
                        tmp.setReceiveCostBalance(x.getReceiveCostBalance().add(y.getReceiveCostBalance()));
                        tmp.setEquipmentDepreciationReservesBalance(x.getEquipmentDepreciationReservesBalance().add(y.getEquipmentDepreciationReservesBalance()));
                        return tmp;
                    }
            );

            if(sumOp.isPresent()){
                ContractBalanceEntity sum = sumOp.get();
                receiveCostBalance = sum.getReceiveCostBalance();
                equipmentDepreciationReservesBalance = sum.getEquipmentDepreciationReservesBalance();
            }
        }

        RecyclingEquipmentOutDetailEntity tmp = null;
        if(detailEntity == null){
            //insert
            tmp = new RecyclingEquipmentOutDetailEntity();
            tmp.setOrgId(v.getOrgId());
            tmp.setContractCode(v.getContractCode());
            tmp.setOutboundDate(v.getOutboundDate());
            tmp.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
            tmp.setIsGenerateVoucher("0");
        }else{
            //update
            tmp = detailEntity;
        }
        tmp.setClientCode(contractEntity.getClientCode());
        tmp.setClientName(contractEntity.getClientName());
        tmp.setRecyclingEquipmentCost(receiveCostBalance);
        tmp.setProvisionForImpairment(equipmentDepreciationReservesBalance);
        return tmp;
    }

    private void checkImportData(List<RecyclingEquipmentOutDetailExcelDTO> recyclingEquipmentOutDetailExcelDTOS) {
        if (CollectionUtils.isEmpty(recyclingEquipmentOutDetailExcelDTOS)) {
            throw new ServiceException("没有数据需要上传");
        }
        //查询系统签约主体
        Map<String,String> orgIdMap = iOrgCompanyService.list().stream().collect(HashMap::new,(h, v)->h.put(v.getOrgName(),v.getOrgId()),HashMap::putAll);

        List<String> orgIds = Lists.newArrayList();
        recyclingEquipmentOutDetailExcelDTOS.stream().forEach(v -> {

            if(StringUtils.isEmpty(v.getOrgName())){
                throw new ServiceException("签约主体不可以为空");
            }

            if (!orgIdMap.containsKey(v.getOrgName())) {
                throw new ServiceException("签约主体在系统中不存在");
            }else{
                v.setOrgId(orgIdMap.get(v.getOrgName()));
            }

            if(ObjectUtil.isEmpty(v.getOutboundDate())){
                throw new ServiceException("出库日期不可以为空");
            }
            if(StringUtils.isEmpty(v.getContractCode())){
                throw new ServiceException("合同编号不可以为空");
            }

            List<RecyclingEquipmentOutDetailEntity> outEntityList =
                    this.list(new LambdaQueryWrapper<RecyclingEquipmentOutDetailEntity>()
                            .eq(RecyclingEquipmentOutDetailEntity::getOutboundDate, v.getOutboundDate())
                            .eq(RecyclingEquipmentOutDetailEntity::getOrgId, v.getOrgId())
                            .eq(RecyclingEquipmentOutDetailEntity::getContractCode, v.getContractCode()));
            Optional<RecyclingEquipmentOutDetailEntity> op = outEntityList.stream().filter(i-> !StringUtils.equals(i.getProcessStatus(), ProcessStatusEnum.ENTERED.getCode())).findAny();
            if(op.isPresent()){
                throw new ServiceException("导入的回收设备出库数据包括状态不是已录入的数据");
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
        RecyclingEquipmentOutDetailEntity entity = this.getById(approveDTO.getDocumentId());
        if (ObjectUtil.isEmpty(entity)) {
            throw new ServiceException("转入登记数据不存在");
        }
        if (ProcessStatusEnum.REJECTED.getCode().equals(approveDTO.getDocumentStatus())) {
            // 驳回 删除凭证
            batchDeleteVoucher(Arrays.asList(entity.getId()));
            entity.setVoucherId("");
            entity.setIsGenerateVoucher(YesOrNoEnum.NO.getCode());
        }

        //修改凭证状态
        String processStatus = entity.getProcessStatus();
        if (ProcessStatusEnum.REVIEWED.getCode().equals(approveDTO.getDocumentStatus())) {
            processStatus = ProcessStatusEnum.REVIEWED.getCode();
        } else if (ProcessStatusEnum.REJECTED.getCode().equals(approveDTO.getDocumentStatus())) {
            processStatus = ProcessStatusEnum.REJECTED.getCode();
        }
        iVoucherService.updateStatusByBatch(com.google.common.collect.Lists.newArrayList(entity.getId()),BatchTypeEnum.HSSBCWCK.getCode(),processStatus,approveDTO.getApproverNum(),approveDTO.getApproverName());


        // 通过，直接修改状态
        entity.setProcessStatus(approveDTO.getDocumentStatus());

        this.updateById(entity);
    }

}

