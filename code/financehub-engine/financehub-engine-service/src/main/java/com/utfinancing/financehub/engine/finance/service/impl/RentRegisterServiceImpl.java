package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.admin.api.RemoteDictService;
import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.common.core.constant.GenConstants;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.approve.model.dto.ApproveDTO;
import com.utfinancing.financehub.engine.approve.service.IApproveService;
import com.utfinancing.financehub.engine.config.RabbitmqConfig;
import com.utfinancing.financehub.engine.dw.entity.DwCmFarkhxxDEntity;
import com.utfinancing.financehub.engine.dw.service.IDwCmFarkhxxDService;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.mapper.SellRegisterMapper;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.*;
import com.utfinancing.financehub.engine.finance.mapper.RentRegisterMapper;
import com.utfinancing.financehub.engine.finance.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-07
 * @Description :  RentRegister服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class RentRegisterServiceImpl extends ServiceImpl<RentRegisterMapper, RentRegisterEntity> implements IRentRegisterService {

    private final RentRegisterMapper rentRegisterMapper;
    private final IRepaymentPlanService iRepaymentPlanService;
    private final ITransferRegisterService iTransferRegisterService;
    private final SellRegisterMapper sellRegisterMapper;
    private final IClientService iClientService;
    private final IContractService iContractService;
    private final IRentRegisterDetailService iRentRegisterDetailService;
    private final IRentIncomeConfirmService iRentIncomeConfirmService;
    private final IDwCmFarkhxxDService iDwCmFarkhxxDService;
    private final RemoteDictService remoteDictService;

    private final RabbitTemplate rabbitTemplate;

    @Value("${approve.url.rentRegister-url:null}")
    private String approveUrl;

    @Resource
    private IApproveService iApproveService;

    @Override
    public Long saveRentRegister(RentRegisterDTO dto) {
        RentRegisterEntity entity = BeanUtil.copyProperties(dto, RentRegisterEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateRentRegister(Long id, RentRegisterDTO dto) {
        RentRegisterEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public RentRegisterDTO getRentRegisterDTOById(Long id) {
        RentRegisterEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, RentRegisterDTO.class);
    }

    @Override
    public IPage<RentRegisterVO> selectPage(RentRegisterQueryDTO queryDTO) {
        // 这里注入查询条件
        IPage<RentRegisterEntity> entityIPage = rentRegisterMapper.selectByMapper(new Page<RentRegisterEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryDTO);
        // 封装数据
        // setRentRegisterData(entityIPage.getRecords());
        return ListBeanUtil.copyPage(entityIPage, RentRegisterVO.class);
    }

    /**
     * 封装数据
     *
     * @param list
     */
    private void setRentRegisterData(List<RentRegisterEntity> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        List<String> contractCodeList = list.stream().map(RentRegisterEntity::getContractCode).collect(Collectors.toList());
        List<RentRegisterDetailVO> rentRegisterDetailVOList = iRentRegisterDetailService.selectByContractCodeList(contractCodeList);
        Map<String, List<RentRegisterDetailVO>> repaymentPlanMap = rentRegisterDetailVOList.stream().collect(Collectors.groupingBy(RentRegisterDetailVO::getContractCode));
        list.stream().forEach(a -> {
            List<RentRegisterDetailVO> repaymentPlanVOS = repaymentPlanMap.get(a.getContractCode());
            if (CollectionUtils.isNotEmpty(repaymentPlanVOS)) {
                if (ObjectUtil.isEmpty(a.getLeaseDateStart())) {
                    // 起租日期为空，取偿还计划最小的日期
                    a.setLeaseDateStart(DateUtils.toDate(repaymentPlanVOS.stream().min(Comparator.comparing(RentRegisterDetailVO::getPlanDate)).map(RentRegisterDetailVO::getPlanDate).orElse(null)));
                }
                if (ObjectUtil.isEmpty(a.getLeaseDateEnd())) {
                    // 到期日为空，取偿还计划最大的日期
                    a.setLeaseDateEnd(DateUtils.toDate(repaymentPlanVOS.stream().max(Comparator.comparing(RentRegisterDetailVO::getPlanDate)).map(RentRegisterDetailVO::getPlanDate).orElse(null)));
                }
                // 汇总租金
                a.setRentTotal(repaymentPlanVOS.stream().filter(o -> ObjectUtil.isNotEmpty(o.getReceivableRent())).map(RentRegisterDetailVO::getReceivableRent).reduce(BigDecimal.ZERO, BigDecimal::add));
            }
        });

    }

    @Override
    public List<RentRegisterVO> selectList(RentRegisterQueryDTO queryDTO) {
        List<RentRegisterEntity> list = rentRegisterMapper.selectByMapper(queryDTO);
        // 封装数据
        // setRentRegisterData(list);
        return ListBeanUtil.copyList(list, RentRegisterVO.class);
    }

    private void setQueryCondition(RentRegisterQueryDTO queryDTO, LambdaQueryWrapper<RentRegisterEntity> queryWrapper) {
        if (ObjectUtil.isNotEmpty(queryDTO.getContractCode())) {
            queryWrapper.like(RentRegisterEntity::getContractCode, queryDTO.getContractCode());
        }
        if (ObjectUtil.isNotEmpty(queryDTO.getClientName())) {
            queryWrapper.like(RentRegisterEntity::getClientName, queryDTO.getClientName());
        }
        if (ObjectUtil.isNotEmpty(queryDTO.getAssetNumber())) {
            queryWrapper.like(RentRegisterEntity::getAssetNumber, queryDTO.getAssetNumber());
        }
        if (CollectionUtils.isNotEmpty(queryDTO.getProcessStatusList())) {
            queryWrapper.in(RentRegisterEntity::getProcessStatus, queryDTO.getProcessStatusList());
        }
        if (CollectionUtils.isNotEmpty(queryDTO.getIdList())) {
            queryWrapper.in(RentRegisterEntity::getId, queryDTO.getIdList());
        }
        if (ObjectUtil.isNotEmpty(queryDTO.getId())) {
            queryWrapper.in(RentRegisterEntity::getId, queryDTO.getId());
        }
        queryWrapper.orderByDesc(RentRegisterEntity::getId);
    }

    /**
     * 上传基本信息
     *
     * @param file
     * @return
     */
    @Override
    public Boolean importFile(MultipartFile file) {
        try {
            ExcelUtil<RentRegisterExcelDTO> util = new ExcelUtil<RentRegisterExcelDTO>(RentRegisterExcelDTO.class);
            List<RentRegisterExcelDTO> list = util.importExcel(file.getInputStream());
            if (CollectionUtils.isEmpty(list)) {
                throw new ServiceException("没有数据需要上传");
            }
            // 校验数据
            checkDate(list);
            List<RentRegisterEntity> entityList = ListBeanUtil.copyList(list, RentRegisterEntity.class);
            List<String> contractCodeList = entityList.stream().map(RentRegisterEntity::getContractCode).distinct().collect(Collectors.toList());
            List<RentRegisterEntity> registerEntityList = list(new LambdaQueryWrapper<RentRegisterEntity>().in(RentRegisterEntity::getContractCode, contractCodeList));

            // 保存数据
            for (RentRegisterEntity rentRegisterEntity : entityList) {
                // 1、查询是否存在房产租赁合同编号 ，不存在新增， 存在 已录入/已拒绝的 则覆盖，否则新增版本号
                RentRegisterEntity registerEntity = registerEntityList.stream().filter(a -> ObjectUtil.equals(a.getContractCode(), rentRegisterEntity.getContractCode()))
                        .sorted(Comparator.comparingInt(RentRegisterEntity::getVersionNum).reversed()).findFirst().orElse(null);
                if (ObjectUtil.isEmpty(registerEntity)) {
                    // 不存在，新增
                    save(rentRegisterEntity);
                } else {
                    // 存在，判断状态
                    if (ObjectUtil.equals(registerEntity.getProcessStatus(), ProcessStatusEnum.ENTERED.getCode()) ||
                            ObjectUtil.equals(registerEntity.getProcessStatus(), ProcessStatusEnum.REJECTED.getCode())) {
                        // 状态为已录入，已拒绝 则更新
                        BeanUtil.copyProperties(rentRegisterEntity, registerEntity, GenConstants.BASE_ENTITY);
                        registerEntity.setUpdateTime(null);
                        updateById(registerEntity);
                    } else {
                        Integer versionNum = registerEntity.getVersionNum();
                        // 否则增加版本号
                        rentRegisterEntity.setVersionNum(++versionNum);
                        save(rentRegisterEntity);
                    }
                }
            }
        } catch (Exception e) {
            throw new ServiceException("上传文件失败，失败原因:" + e.getMessage());
        }
        return Boolean.TRUE;
    }

    /**
     * 生成合同信息
     *
     * @param entityList
     */
    private void generateContract(List<RentRegisterEntity> entityList) {
        // 获取税率
        BigDecimal taxRate;
        R<List<SysDictData>> taxRateR = remoteDictService.listDictData(DictTypeEnum.SYS_TAX_RATE_BONDED_ASSETS.getCode());
        if (ObjectUtil.isNotEmpty(taxRateR) && ObjectUtil.isNotEmpty(taxRateR.getData())) {
            // 取第一个税率
            taxRate = new BigDecimal(taxRateR.getData().get(0).getDictLabel());
        } else {
            taxRate = BigDecimal.ZERO;
        }

        List<ContractEntity> contractEntityList = Lists.newArrayList();
        entityList.stream().forEach(a -> {
            List<String> assetNumberList = StrUtil.split(a.getAssetNumber(), ",");
            TransferRegisterVO transferRegisterVO = iTransferRegisterService.getByAssetNumber(assetNumberList.get(0));
            if (ObjectUtil.isEmpty(transferRegisterVO)) {
                throw new ServiceException("根据资产编号[" + assetNumberList.get(0) + "]未查询到对应的抵债房产转入登记数据");
            }
            ContractEntity contractEntity = iContractService.getOne(new LambdaQueryWrapper<ContractEntity>().eq(ContractEntity::getContractCode, a.getContractCode())
                    .eq(ContractEntity::getOrgId, transferRegisterVO.getOrgId()));
            if (ObjectUtil.isEmpty(contractEntity)) {
                contractEntity = new ContractEntity();
            }
            contractEntity.setContractCode(a.getContractCode());
            contractEntity.setOrgId(transferRegisterVO.getOrgId());
            contractEntity.setClientCode(a.getClientCode());
            contractEntity.setClientName(a.getClientName());
            contractEntity.setClientType("承租人");
            contractEntity.setLeaseDateStart(a.getLeaseDateStart());
            contractEntity.setLeaseDateEnd(a.getLeaseDateEnd());
            contractEntity.setBusinessCode(BusinessEnum.ZLYW.getCode());
            contractEntity.setBusinessName(BusinessEnum.ZLYW.getDesc());
            contractEntity.setContractStatus(BusinessContractStatusEnum.CONTRACT_STATUS_1.getCode());
            contractEntity.setCurrencyType(CurrencyTypeEnum.CNY.getCode());// 默认人民币
            contractEntity.setTaxRate(taxRate);

            List<DwCmFarkhxxDEntity> dwCmFarkhxxDEntityList = iDwCmFarkhxxDService.selectByClientCode(a.getClientCode());
            // 客户存在dw_cm_farkhxx_d 为 开票 否则为计提
            String invoiceingFlag = "";
            if (CollectionUtils.isNotEmpty(dwCmFarkhxxDEntityList)) {
                invoiceingFlag = "开票";
            } else {
                invoiceingFlag = "计提";
            }
            contractEntity.setInvoicingFlag(invoiceingFlag);
            contractEntity.setSystemCode(SystemEnum.CWZT.getCode());
            contractEntity.setBusinessDate(LocalDate.now());
            contractEntity.setUpdateTime(null);
            contractEntity.setIsDzzc(YesOrNoEnum.YES.getCode());

            contractEntityList.add(contractEntity);
        });
        iContractService.saveOrUpdateBatch(contractEntityList);
    }

    /**
     * 校验数据
     *
     * @param list
     */
    private void checkDate(List<RentRegisterExcelDTO> list) {
        Set<String> unionSet = new HashSet<>();
        Set<String> unionSetContractCode = new HashSet<>();
        list.stream().forEach(a -> {
            if (ObjectUtil.isEmpty(a.getAssetNumber())) {
                throw new ServiceException("资产编号不能为空");
            }
            if (ObjectUtil.isEmpty(a.getTransferOutDate())) {
                throw new ServiceException("转出时间不能为空");
            }
            if (ObjectUtil.isEmpty(a.getContractCode())) {
                throw new ServiceException("房产租赁合同编号不能为空");
            }
            if (ObjectUtil.isEmpty(a.getRentBond())) {
                throw new ServiceException("租赁保证金不能为空");
            }
            if (ObjectUtil.isEmpty(a.getClientCode())) {
                throw new ServiceException("客户编号不能为空");
            }
            if (ObjectUtil.isEmpty(a.getClientName())) {
                throw new ServiceException("客户名称不能为空");
            }
            // 校验资产编号+房产租赁合同编号重复
            if (!unionSetContractCode.add(a.getContractCode())) {
                throw new ServiceException("文件存在重复的房产租赁合同编号[" + a.getContractCode() + "],请检查");
            }

            // 校验客户是否存在
            if (ObjectUtil.isNotEmpty(a.getClientCode())) {
                List<ClientEntity> clientEntityList = iClientService.list(new LambdaQueryWrapper<ClientEntity>().eq(ClientEntity::getClientCode, a.getClientCode()));
                if (CollectionUtils.isEmpty(clientEntityList)) {
                    // 若上传的客户编码不存在客户表eg_client，则插入新数据，client_type默认承租人
                    ClientEntity clientEntity = new ClientEntity();
                    clientEntity.setClientCode(a.getClientCode());
                    clientEntity.setClientName(a.getClientName());
                    clientEntity.setClientType("承租人");
                    iClientService.save(clientEntity);
                }
            }
            List<String> assetNumberList = StrUtil.split(a.getAssetNumber(), ",");
            for (String assetNumber : assetNumberList) {
                // 校验资产编号+房产租赁合同编号重复
                if (!unionSet.add(assetNumber + a.getContractCode())) {
                    throw new ServiceException("文件存在重复的资产编号[" + assetNumber + "]+房产租赁合同编号[" + a.getContractCode() + "],请检查");
                }

                // 1.若上传资产编号在转入登记表不存在，报错：资产编号不存在
                TransferRegisterVO transferRegisterVO = iTransferRegisterService.getByAssetNumber(assetNumber);
                if (ObjectUtil.isEmpty(transferRegisterVO)) {
                    throw new ServiceException("资产编号[" + assetNumber + "]不存在");
                }
                SellRegisterEntity sellRegisterEntity = sellRegisterMapper.selectOne(new LambdaQueryWrapper<SellRegisterEntity>().eq(SellRegisterEntity::getAssetNumber, assetNumber));
                // 2.若上传资产编号已做过转出处理（存在于转出-出售表/转出-出租表），报错：该资产编号已转出
                if (ObjectUtil.isNotEmpty(sellRegisterEntity)) {
                    throw new ServiceException("资产编号[" + assetNumber + "]已转出");
                }
            }

        });
    }

    /**
     * 上传租金计划表
     *
     * @param file
     * @return
     */
    @Override
    public Boolean importFileForDetail(MultipartFile file) {
        try {
            ExcelUtil<RentRegisterDetailExcelDTO> util = new ExcelUtil<RentRegisterDetailExcelDTO>(RentRegisterDetailExcelDTO.class);
            List<RentRegisterDetailExcelDTO> list = util.importExcel(file.getInputStream());
            if (CollectionUtils.isEmpty(list)) {
                throw new ServiceException("没有数据需要上传");
            }
            // 校验数据
            checkDetailDate(list);
            List<RentRegisterDetailEntity> entityList = ListBeanUtil.copyList(list, RentRegisterDetailEntity.class);
            List<String> contractCodeList = entityList.stream().map(RentRegisterDetailEntity::getContractCode).distinct().collect(Collectors.toList());
            List<RentRegisterEntity> registerEntityList = list(new LambdaQueryWrapper<RentRegisterEntity>().in(RentRegisterEntity::getContractCode, contractCodeList));
            Map<String, List<RentRegisterDetailEntity>> listMap = entityList.stream().collect(Collectors.groupingBy(RentRegisterDetailEntity::getContractCode));
            for (String contractCode : listMap.keySet()) {
                List<RentRegisterDetailEntity> detailEntityList = listMap.get(contractCode);
                RentRegisterEntity registerEntity = registerEntityList.stream().filter(a -> ObjectUtil.equals(a.getContractCode(), contractCode))
                        .sorted(Comparator.comparingInt(RentRegisterEntity::getVersionNum).reversed()).findFirst().get();
                if(ObjectUtil.isEmpty(registerEntity)){
                    throw new ServiceException("根据合同编号[" + contractCode + "]未在出租登记中查询到对应的出租登记数据");
                }
                // 存在，判断状态
                if (ObjectUtil.equals(registerEntity.getProcessStatus(), ProcessStatusEnum.ENTERED.getCode()) ||
                        ObjectUtil.equals(registerEntity.getProcessStatus(), ProcessStatusEnum.REJECTED.getCode())) {
                    // 状态为已录入，已拒绝 则删除原来的租金计划，写入新的
                    iRentRegisterDetailService.removeByRentRegisterId(registerEntity.getId());
                    detailEntityList.stream().forEach(a->{
                        // 设置出租登记id
                        a.setRentRegisterId(registerEntity.getId());
                    });
                    // 保存租金计划
                    iRentRegisterDetailService.saveBatch(detailEntityList);
                }else {
                    // 否则新增一条出租登记
                    RentRegisterEntity newRegisterEntity = BeanUtil.copyProperties(registerEntity, RentRegisterEntity.class, GenConstants.BASE_ENTITY);
                    // 版本号+1
                    newRegisterEntity.setVersionNum(registerEntity.getVersionNum()+1);
                    newRegisterEntity.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
                    save(newRegisterEntity);
                    detailEntityList.stream().forEach(a->{
                        // 设置出租登记id
                        a.setRentRegisterId(newRegisterEntity.getId());
                    });
                    // 保存租金计划
                    iRentRegisterDetailService.saveBatch(detailEntityList);
                }
            }

            // 计算金额
            iRentRegisterDetailService.calculateAmount(contractCodeList);

        } catch (Exception e) {
            throw new ServiceException("上传文件失败，失败原因:" + e.getMessage());
        }
        return Boolean.TRUE;
    }

    /**
     * 推送偿还计划
     *
     * @param contractCodeList
     */
    private void pushRepaymentPlan(List<String> contractCodeList) {
        List<RentRegisterDetailVO> rentRegisterDetailVOList = iRentRegisterDetailService.selectByContractCodeList(contractCodeList);
        List<ContractEntity> contractEntityList = iContractService.list(new LambdaQueryWrapper<ContractEntity>().in(ContractEntity::getContractCode, contractCodeList));
        // 根据合同编号分组
        Map<String, List<RentRegisterDetailVO>> listMap = rentRegisterDetailVOList.stream().collect(Collectors.groupingBy(RentRegisterDetailVO::getContractCode));

        for (String contractCode : listMap.keySet()) {
            List<RentRegisterDetailVO> detailVOList = listMap.get(contractCode);
            if (CollectionUtils.isEmpty(detailVOList)) {
                throw new ServiceException("房产租赁合同编号[" + contractCode + "]租金计划数据为空");
            }
            // 按照日期排序
            List<RentRegisterDetailVO> list = detailVOList.stream().sorted(Comparator.comparing(RentRegisterDetailVO::getPlanDate)).collect(Collectors.toList());
            ContractEntity contractEntity = contractEntityList.stream().filter(a -> ObjectUtil.equals(a.getContractCode(), contractCode)).findFirst().orElse(null);
            List<RepaymentPlanSaveDTO> planSaveDTOList = Lists.newArrayList();
            for (int i = 0; i < list.size(); i++) {
                RentRegisterDetailVO vo = list.get(i);
                RepaymentPlanSaveDTO planSaveDTO = new RepaymentPlanSaveDTO();
                planSaveDTO.setPeriods(i + 1);
                planSaveDTO.setPlanDate(DateUtils.toDate(vo.getPlanDate()));
                planSaveDTO.setInterestAmount(BigDecimal.ZERO);
                planSaveDTO.setPrincipalAmount(vo.getThisMonthReceivableRent());
                planSaveDTO.setRentAmount(vo.getThisMonthReceivableRent());
                planSaveDTOList.add(planSaveDTO);
            }

            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("businessDate", LocalDate.now());
            dataMap.put("systemCode", SystemEnum.CWZT.getCode());
            dataMap.put("orgId", contractEntity.getOrgId());
            dataMap.put("contractCode", contractCode);
            dataMap.put("sceneCode", "出租登记偿还计划修改");
            dataMap.put("orderId", list.stream().min(Comparator.comparingLong(RentRegisterDetailVO::getId)).map(RentRegisterDetailVO::getId).orElse(null));
            dataMap.put("repaymentPlanList", planSaveDTOList);
            log.info("出租登记推送偿还计划发送mq数据：" + JSONObject.toJSONString(dataMap));
            // 发送mq 推送偿还计划
            rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_DIRECT_TRANSACTION_DATA, RabbitmqConfig.ROUTINGKEY_REPAYMENT_PLAN_DATA, JSONObject.toJSONString(dataMap));
            // iRepaymentPlanService.saveRawData("1", JSONObject.parseObject(JSONObject.toJSONString(dataMap)));
        }
    }

    /**
     * 校验明细上传数据
     *
     * @param list
     */
    private void checkDetailDate(List<RentRegisterDetailExcelDTO> list) {
        Set<String> unionSet = new HashSet<>();
        list.stream().forEach(a -> {
            if (ObjectUtil.isEmpty(a.getContractCode())) {
                throw new ServiceException("房产租赁合同编号不能为空");
            }
            if (ObjectUtil.isEmpty(a.getPlanDate())) {
                throw new ServiceException("应付日期不能为空");
            }
            // 校验重复
            if (!unionSet.add(a.getContractCode() + a.getPlanDate())) {
                throw new ServiceException("文件存在重复的合同编号[" + a.getContractCode() + "]+应付日期[" + a.getPlanDate() + "],请检查");
            }
            // 校验合同号
            List<RentRegisterEntity> rentRegisterEntityList = list(new LambdaQueryWrapper<RentRegisterEntity>().eq(RentRegisterEntity::getContractCode, a.getContractCode()));
            if (CollectionUtils.isEmpty(rentRegisterEntityList)) {
                throw new ServiceException("根据合同编号[" + a.getContractCode() + "]未在出租登记中查询到对应的数据");
            }
        });
    }

    /**
     * 批量提交
     *
     * @param ids
     * @return
     */
    @Override
    public Boolean submit(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条数据提交");
        }
        List<RentRegisterEntity> entityList = this.listByIds(ids);
        List<ApproveDTO> approveDTOList = Lists.newArrayList();
        entityList.stream().forEach(v -> {
            if (!(ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus()) || ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus()))) {
                throw new ServiceException("只有处理状态为已录入或者已拒绝的才可以提交");
            }
            v.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
            ApproveDTO approveDTO = new ApproveDTO();
            approveDTO.setDocumentId(v.getId());
            approveDTO.setDocumentType(BatchTypeEnum.DZZCCZDJ.getCode());
            approveDTO.setUrl(approveUrl + v.getId());
            approveDTOList.add(approveDTO);
        });

        // 保存合同信息
        generateContract(entityList);
        List<String> contractCodeList = entityList.stream().map(RentRegisterEntity::getContractCode).distinct().collect(Collectors.toList());
        // 推送偿还计划
        pushRepaymentPlan(contractCodeList);

        // 发送审核
        Map<Long, Long> processInstantIdMap = iApproveService.submit(approveDTOList);
        entityList.stream().forEach(v -> {
            if (null != processInstantIdMap && processInstantIdMap.containsKey(v.getId())) {
                v.setProcessInstanceId(processInstantIdMap.get(v.getId()));
            }
        });
        return this.updateBatchById(entityList);
    }

    /**
     * 批量撤回
     *
     * @param ids
     * @return
     */
    @Override
    public Boolean withdraw(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条数据撤回");
        }
        List<RentRegisterEntity> entityList = this.listByIds(ids);
        entityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.SUBMITTED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("只有处理状态为已提交的才可以撤回");
            }
            v.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
        });
        iApproveService.withdraw(entityList.stream().map(RentRegisterEntity::getProcessInstanceId).collect(Collectors.toList()));
        return this.updateBatchById(entityList);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @Override
    public Boolean delete(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条数据删除");
        }
        List<RentRegisterEntity> transferRegisterEntityList = this.listByIds(ids);
        transferRegisterEntityList.stream().forEach(v -> {
            if (!(ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus()) || ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus()))) {
                throw new ServiceException("只有处理状态为已录入或者已拒绝的才可以删除");
            }

            // 删除出租登记信息
            removeById(v.getId());
            // 删除租金计划数据
            iRentRegisterDetailService.removeByRentRegisterId(v.getId());
            // 查询删除后，是否还存在关联的出租登记信息
            List<RentRegisterEntity> list = list(new LambdaQueryWrapper<RentRegisterEntity>().eq(RentRegisterEntity::getContractCode, v.getContractCode()));
            if (CollectionUtils.isEmpty(list)) {

                // 校验租金收入确认是否有数据
                List<RentIncomeConfirmEntity> rentIncomeConfirmEntityList = iRentIncomeConfirmService.getByContractCode(v.getContractCode());
                if (CollectionUtils.isNotEmpty(rentIncomeConfirmEntityList)) {
                    for (RentIncomeConfirmEntity entity : rentIncomeConfirmEntityList) {
                        if (!(ProcessStatusEnum.ENTERED.getCode().equals(entity.getProcessStatus()) || ProcessStatusEnum.REJECTED.getCode().equals(entity.getProcessStatus()))) {
                            // 处理状态不为已录入或者已拒绝 不能删除
                            throw new ServiceException("房产租赁合同编号[" + v.getContractCode() + "]在租金收入确认中存在状态为[" + ProcessStatusEnum.getDescByCode(entity.getProcessStatus()) + "]的数据，不能删除");
                        }
                    }
                }
                // 删除租金收入确认数据
                iRentIncomeConfirmService.deleteByContractCode(v.getContractCode());
            }

        });
        return Boolean.TRUE;

    }

    /**
     * 根据资产编号查询出租登记
     *
     * @param assetNumber
     * @param contractCode
     * @return
     */
    @Override
    public RentRegisterVO getByAssetNumber(String assetNumber, String contractCode) {
        RentRegisterEntity rentRegisterEntity = getOne(new LambdaQueryWrapper<RentRegisterEntity>()
                .like(RentRegisterEntity::getAssetNumber, assetNumber)
                .eq(ObjectUtil.isNotEmpty(contractCode), RentRegisterEntity::getContractCode, contractCode)
        );
        return BeanUtil.copyProperties(rentRegisterEntity, RentRegisterVO.class);
    }

    /**
     * 分摊
     *
     * @param ids
     * @return
     */
    @Override
    public Boolean apportion(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Boolean.TRUE;
        }
        List<RentRegisterEntity> entityList = this.listByIds(ids);
        List<String> contractCodeList = entityList.stream().map(RentRegisterEntity::getContractCode).distinct().collect(Collectors.toList());
        List<RentRegisterDetailVO> rentRegisterDetailVOList = iRentRegisterDetailService.selectByContractCodeList(contractCodeList);
        // 根据合同编号分组
        Map<String, List<RentRegisterDetailVO>> listMap = rentRegisterDetailVOList.stream().collect(Collectors.groupingBy(RentRegisterDetailVO::getContractCode));

        List<RentIncomeConfirmEntity> saveRentIncomeConfirmEntityList = Lists.newArrayList();
        for (String contractCode : contractCodeList) {
            RentRegisterEntity rentRegisterEntity = entityList.stream().filter(a -> ObjectUtil.equals(a.getContractCode(), contractCode)).findFirst().get();
            List<String> assetNumberList = StrUtil.split(rentRegisterEntity.getAssetNumber(), ",");
            TransferRegisterVO transferRegisterVO = iTransferRegisterService.getByAssetNumber(assetNumberList.get(0));
            if (ObjectUtil.isEmpty(transferRegisterVO)) {
                throw new ServiceException("根据资产编号[" + assetNumberList.get(0) + "]未查询到转入登记数据");
            }

            // 是否生成租金收入 标识
            boolean generateFlag = true;
            List<RentIncomeConfirmEntity> rentIncomeConfirmEntityList = iRentIncomeConfirmService.getByContractCode(contractCode);
            if (CollectionUtils.isNotEmpty(rentIncomeConfirmEntityList)) {
                for (RentIncomeConfirmEntity entity : rentIncomeConfirmEntityList) {
                    if (!(ProcessStatusEnum.ENTERED.getCode().equals(entity.getProcessStatus()) || ProcessStatusEnum.REJECTED.getCode().equals(entity.getProcessStatus()))) {
                        // 处理状态不为已录入或者已拒绝 不重新生成
                        generateFlag = false;
                    }
                }
            }
            if (generateFlag) {
                // 1.先删除此合同的租金收入确认数据
                iRentIncomeConfirmService.deleteByContractCode(contractCode);

                // 2.生成租金收入确认数据
                List<RentRegisterDetailVO> detailVOList = listMap.get(contractCode);
                // 租金计划按照月份分组
                Map<String, List<RentRegisterDetailVO>> detailListMap = detailVOList.stream().collect(Collectors.groupingBy(a -> a.getPlanDate().format(DateTimeFormatter.ofPattern("yyyy-MM"))));
                for (String accountMonth : detailListMap.keySet()) {
                    List<RentRegisterDetailVO> list = detailListMap.get(accountMonth).stream().sorted(Comparator.comparing(RentRegisterDetailVO::getPlanDate)).collect(Collectors.toList());
                    RentIncomeConfirmEntity saveDTO = new RentIncomeConfirmEntity();
                    saveDTO.setContractCode(contractCode);
                    saveDTO.setOrgId(transferRegisterVO.getOrgId());
                    saveDTO.setClientCode(rentRegisterEntity.getClientCode());
                    saveDTO.setClientName(rentRegisterEntity.getClientName());
                    saveDTO.setAccountMonth(DateUtils.parseDate(accountMonth));
                    saveDTO.setThisMonthReceivableRent(list.stream().map(RentRegisterDetailVO::getThisMonthReceivableRent).reduce(BigDecimal.ZERO, BigDecimal::add));
                    saveDTO.setThisMonthTax(list.stream().map(RentRegisterDetailVO::getThisMonthTax).reduce(BigDecimal.ZERO, BigDecimal::add));
                    saveDTO.setThisMonthRentIncome(list.stream().map(RentRegisterDetailVO::getThisMonthRentIncome).reduce(BigDecimal.ZERO, BigDecimal::add));
                    saveRentIncomeConfirmEntityList.add(saveDTO);
                }
            }
        }
        // 保存租金收入确认数据
        if (CollectionUtils.isNotEmpty(saveRentIncomeConfirmEntityList)) {
            iRentIncomeConfirmService.saveBatch(saveRentIncomeConfirmEntityList);
        }
        return Boolean.TRUE;
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
        RentRegisterEntity entity = this.getById(approveDTO.getDocumentId());
        if (ObjectUtil.isEmpty(entity)) {
            throw new ServiceException("出租登记数据不存在");
        }
        // 通过、驳回，直接修改状态
        entity.setProcessStatus(approveDTO.getDocumentStatus());
        this.updateById(entity);
    }

}

