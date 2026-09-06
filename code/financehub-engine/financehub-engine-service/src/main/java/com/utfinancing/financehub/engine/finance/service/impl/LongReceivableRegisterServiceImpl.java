package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.core.constant.GenConstants;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.approve.model.dto.ApproveDTO;
import com.utfinancing.financehub.engine.approve.service.IApproveService;
import com.utfinancing.financehub.engine.config.RabbitmqConfig;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.LongApportionVO;
import com.utfinancing.financehub.engine.finance.model.vo.LongReceivableRegisterVO;
import com.utfinancing.financehub.engine.finance.mapper.LongReceivableRegisterMapper;
import com.utfinancing.financehub.engine.finance.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import com.utfinancing.financehub.engine.rule.model.dto.ExecuteCommonDTO;
import com.utfinancing.financehub.engine.rule.model.vo.VoucherInfoVO;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.IOUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-11
 * @Description :  LongReceivableRegister服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class LongReceivableRegisterServiceImpl extends ServiceImpl<LongReceivableRegisterMapper, LongReceivableRegisterEntity> implements ILongReceivableRegisterService {

    private final LongReceivableRegisterMapper longReceivableRegisterMapper;
    private final IContractService iContractService;
    private final IClientService iClientService;
    private final IOrgCompanyService iOrgCompanyService;
    private final ILongRepaymentPlanService iLongRepaymentPlanService;
    private final ILongApportionService iLongApportionService;
    private final IRepaymentPlanService iRepaymentPlanService;
    private final IRepaymentPlanHisService iRepaymentPlanHisService;
    private final IRuleService iRuleService;
    private final IVoucherService iVoucherService;
    private final ILongIncomeConfirmService iLongIncomeConfirmService;

    private final RabbitTemplate rabbitTemplate;

    @Value("${approve.url.longReceivableRegister-url:null}")
    private String approveUrl;

    @Resource
    private IApproveService iApproveService;

    @Override
    public Long saveLongReceivableRegister(LongReceivableRegisterDTO dto) {
        LongReceivableRegisterEntity entity = BeanUtil.copyProperties(dto, LongReceivableRegisterEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateLongReceivableRegister(Long id, LongReceivableRegisterDTO dto) {
        LongReceivableRegisterEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public LongReceivableRegisterDTO getLongReceivableRegisterDTOById(Long id) {
        LongReceivableRegisterEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, LongReceivableRegisterDTO.class);
    }

    @Override
    public IPage<LongReceivableRegisterVO> selectPage(LongReceivableRegisterQueryDTO queryDTO) {
        IPage<LongReceivableRegisterEntity> entityIPage = longReceivableRegisterMapper.selectByMapper(new Page<LongReceivableRegisterEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryDTO);
        IPage<LongReceivableRegisterVO> registerVOIPage = ListBeanUtil.copyPage(entityIPage, LongReceivableRegisterVO.class);
        // 设置数据
        setData(registerVOIPage.getRecords());
        return registerVOIPage;
    }

    /**
     * 设置数据
     *
     * @param list
     */
    private void setData(List<LongReceivableRegisterVO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        Map<String, String> companyMap = iOrgCompanyService.selectAllOrgIdAndName().stream().collect(Collectors.toMap(e -> e.getOrgId(), e -> e.getOrgName(), (a, b) -> b));
        // 查询 偿还计划
        List<LongRepaymentPlanEntity> longRepaymentPlanEntityList = iLongRepaymentPlanService.getByLongRegisterIdList(list.stream().map(LongReceivableRegisterVO::getId).collect(Collectors.toList()));

        list.stream().forEach(a -> {
            a.setOrgName(companyMap.get(a.getOrgId()));
            List<LongRepaymentPlanEntity> rePlanList = longRepaymentPlanEntityList.stream().filter(b -> ObjectUtil.equals(a.getId(), b.getLongRegisterId())).collect(Collectors.toList());
            a.setReceivableTotal(rePlanList.stream().map(LongRepaymentPlanEntity::getReceivableTotal).reduce(BigDecimal.ZERO, BigDecimal::add));
            a.setReceivablePrincipal(rePlanList.stream().map(LongRepaymentPlanEntity::getReceivablePrincipal).reduce(BigDecimal.ZERO, BigDecimal::add));
            a.setReceivableInterest(rePlanList.stream().map(LongRepaymentPlanEntity::getReceivableInterest).reduce(BigDecimal.ZERO, BigDecimal::add));
        });

    }

    /**
     * 查询条件
     *
     * @param queryDTO
     * @param queryWrapper
     */
    private void setQueryCondition(LongReceivableRegisterQueryDTO queryDTO, LambdaQueryWrapper<LongReceivableRegisterEntity> queryWrapper) {
        if (ObjectUtil.isNotEmpty(queryDTO.getLongReceivableNumber())) {
            queryWrapper.like(LongReceivableRegisterEntity::getLongReceivableNumber, queryDTO.getLongReceivableNumber());
        }
        if (ObjectUtil.isNotEmpty(queryDTO.getContractCode())) {
            queryWrapper.like(LongReceivableRegisterEntity::getContractCode, queryDTO.getContractCode());
        }
        if (ObjectUtil.isNotEmpty(queryDTO.getClientName())) {
            queryWrapper.like(LongReceivableRegisterEntity::getClientName, queryDTO.getClientName());
        }
        if (ObjectUtil.isNotEmpty(queryDTO.getProjectName())) {
            queryWrapper.like(LongReceivableRegisterEntity::getProjectName, queryDTO.getProjectName());
        }
        if (CollectionUtils.isNotEmpty(queryDTO.getIdList())) {
            queryWrapper.in(LongReceivableRegisterEntity::getId, queryDTO.getIdList());
        }
        if (CollectionUtils.isNotEmpty(queryDTO.getProcessStatusList())) {
            queryWrapper.in(LongReceivableRegisterEntity::getProcessStatus, queryDTO.getProcessStatusList());
        }
        queryWrapper.orderByDesc(LongReceivableRegisterEntity::getId);
    }

    @Override
    public List<LongReceivableRegisterVO> selectList(LongReceivableRegisterQueryDTO queryDTO) {
        List<LongReceivableRegisterEntity> list = longReceivableRegisterMapper.selectByMapper(queryDTO);
        List<LongReceivableRegisterVO> registerVOList = ListBeanUtil.copyList(list, LongReceivableRegisterVO.class);
        // 设置数据
        setData(registerVOList);
        return registerVOList;
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
        InputStream inputStream2 = null;
        InputStream inputStream3 = null;
        try {
            ExcelUtil<LongReceivableRegisterExcelDTO> util = new ExcelUtil<LongReceivableRegisterExcelDTO>(LongReceivableRegisterExcelDTO.class);
            ExcelUtil<LongRepaymentPlanExcelDTO> util2 = new ExcelUtil<LongRepaymentPlanExcelDTO>(LongRepaymentPlanExcelDTO.class);
            ExcelUtil<LongApportionExcelDTO> util3 = new ExcelUtil<LongApportionExcelDTO>(LongApportionExcelDTO.class);
            inputStream = file.getInputStream();
            inputStream2 = file.getInputStream();
            inputStream3 = file.getInputStream();

            List<LongReceivableRegisterExcelDTO> list = util.importExcel("基础信息", inputStream, 0);
            List<LongRepaymentPlanExcelDTO> list2 = util2.importExcel("偿还计划", inputStream2, 0);
            List<LongApportionExcelDTO> list3 = util3.importExcel("分摊表", inputStream3, 0);
            save1(list);
            save2(list2);
            save3(list3);
        } catch (Exception e) {
            throw new ServiceException("上传文件失败，失败原因:" + e.getMessage());
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(inputStream2);
            IOUtils.closeQuietly(inputStream3);
        }
        return Boolean.TRUE;
    }

    /**
     * 保存分摊表
     *
     * @param list
     */
    private void save3(List<LongApportionExcelDTO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        // 校验数据
        Set<String> unionSet = new HashSet<>();
        list.stream().forEach(a -> {
            if (ObjectUtil.isEmpty(a.getLongReceivableNumber())) {
                throw new ServiceException("长期应收款编号为空");
            }
            if (ObjectUtil.isEmpty(a.getContractCode())) {
                throw new ServiceException("合同编号不能为空");
            }
            if (ObjectUtil.isEmpty(a.getReceivableDate())) {
                throw new ServiceException("日期不能为空");
            }
            if (ObjectUtil.isEmpty(a.getReceivableTotal())) {
                throw new ServiceException("应收总额不能为空");
            }
            if (ObjectUtil.isEmpty(a.getReceivablePrincipal())) {
                throw new ServiceException("应收本金不能为空");
            }
            if (ObjectUtil.isEmpty(a.getReceivableInterest())) {
                throw new ServiceException("应收利息不能为空");
            }
            if (ObjectUtil.isEmpty(a.getResidualPrincipal())) {
                throw new ServiceException("剩余本金不能为空");
            }
            if (ObjectUtil.isEmpty(a.getAmortizedCost())) {
                throw new ServiceException("摊余成本不能为空");
            }
            if (ObjectUtil.isEmpty(a.getConfirmIncome())) {
                throw new ServiceException("确认收入不能为空");
            }
            // 校验长期应收款编号+合同编号+应收日期重复
            if (!unionSet.add(a.getLongReceivableNumber() + a.getContractCode() + a.getReceivableDate())) {
                throw new ServiceException("文件存在重复的长期应收款编号[" + a.getLongReceivableNumber() + "]+合同编号[" + a.getContractCode() + "]+应收日期[" + a.getReceivableDate() + "],请检查");
            }
            // 校验长期应收款编号+合同编号是否存在，不存在则报错
            List<LongReceivableRegisterEntity> exsitList = list(new LambdaQueryWrapper<LongReceivableRegisterEntity>().eq(LongReceivableRegisterEntity::getLongReceivableNumber, a.getLongReceivableNumber())
                    .eq(LongReceivableRegisterEntity::getContractCode, a.getContractCode()));
            if (CollectionUtils.isEmpty(exsitList)) {
                throw new ServiceException("根据长期应收款编号[" + a.getLongReceivableNumber() + "]+合同编号[" + a.getContractCode() + "]未查询到长期应收款登记-基础信息");
            }
        });
        List<LongApportionEntity> entityList = ListBeanUtil.copyList(list, LongApportionEntity.class);
        List<LongReceivableRegisterEntity> exsitList = list(new LambdaQueryWrapper<LongReceivableRegisterEntity>()
                .in(LongReceivableRegisterEntity::getLongReceivableNumber, entityList.stream().map(LongApportionEntity::getLongReceivableNumber).collect(Collectors.toList()))
                .in(LongReceivableRegisterEntity::getContractCode, entityList.stream().map(LongApportionEntity::getContractCode).collect(Collectors.toList())));
        Map<String, List<LongApportionEntity>> listMap = entityList.stream().collect(Collectors.groupingBy(a -> a.getLongReceivableNumber() + "-" + a.getContractCode()));
        for (String key : listMap.keySet()) {
            List<LongApportionEntity> apportionEntityList = listMap.get(key);
            String longReceivableNumber = apportionEntityList.get(0).getLongReceivableNumber();
            String contractCode = apportionEntityList.get(0).getContractCode();
            LongReceivableRegisterEntity registerEntity = exsitList.stream().filter(a -> ObjectUtil.equals(a.getLongReceivableNumber(), longReceivableNumber)
                            && ObjectUtil.equals(a.getContractCode(), contractCode))
                    .sorted(Comparator.comparingInt(LongReceivableRegisterEntity::getVersionNum).reversed()).findFirst().orElse(null);
            if (ObjectUtil.isEmpty(registerEntity)) {
                throw new ServiceException("根据长期应收款编号[" + longReceivableNumber + "]+合同编号[" + contractCode + "]未查询到长期应收款登记-基础信息");
            }
            // 存在，判断状态
            if (ObjectUtil.equals(registerEntity.getProcessStatus(), ProcessStatusEnum.ENTERED.getCode()) ||
                    ObjectUtil.equals(registerEntity.getProcessStatus(), ProcessStatusEnum.REJECTED.getCode())) {
                // 状态为已录入，已拒绝 则删除原来的分摊表，写入新的
                iLongApportionService.removeByLongRegisterIdList(Lists.newArrayList(registerEntity.getId()));
                apportionEntityList.stream().forEach(a -> {
                    // 设置长期应收款id
                    a.setLongRegisterId(registerEntity.getId());
                });
                // 保存分摊表
                iLongApportionService.saveBatch(apportionEntityList);
            } else {
                // 否则新增一条长期应收款
                LongReceivableRegisterEntity newRegisterEntity = BeanUtil.copyProperties(registerEntity, LongReceivableRegisterEntity.class, GenConstants.BASE_ENTITY);
                // 版本号+1
                newRegisterEntity.setVersionNum(registerEntity.getVersionNum() + 1);
                newRegisterEntity.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
                newRegisterEntity.setIsGenerateVoucher(null);
                newRegisterEntity.setVoucherId(null);
                newRegisterEntity.setErrorInfo(null);
                save(newRegisterEntity);
                apportionEntityList.stream().forEach(a -> {
                    // 设置长期应收款id
                    a.setLongRegisterId(newRegisterEntity.getId());
                });
                // 保存分摊表
                iLongApportionService.saveBatch(apportionEntityList);
            }
        }
    }

    /**
     * 保存偿还计划
     *
     * @param list
     */
    private void save2(List<LongRepaymentPlanExcelDTO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        // 校验数据
        Set<String> unionSet = new HashSet<>();
        list.stream().forEach(a -> {
            if (ObjectUtil.isEmpty(a.getLongReceivableNumber())) {
                throw new ServiceException("长期应收款编号为空");
            }
            if (ObjectUtil.isEmpty(a.getContractCode())) {
                throw new ServiceException("合同编号不能为空");
            }
            if (ObjectUtil.isEmpty(a.getReceivableDate())) {
                throw new ServiceException("应收日期不能为空");
            }
            if (ObjectUtil.isEmpty(a.getReceivableTotal())) {
                throw new ServiceException("应收总额不能为空");
            }
            if (ObjectUtil.isEmpty(a.getReceivablePrincipal())) {
                throw new ServiceException("应收本金不能为空");
            }
            if (ObjectUtil.isEmpty(a.getReceivableInterest())) {
                throw new ServiceException("应收利息不能为空");
            }
            // 校验长期应收款编号+合同编号+应收日期重复
            if (!unionSet.add(a.getLongReceivableNumber() + a.getContractCode() + a.getReceivableDate())) {
                throw new ServiceException("文件存在重复的长期应收款编号[" + a.getLongReceivableNumber() + "]+合同编号[" + a.getContractCode() + "]+应收日期[" + a.getReceivableDate() + "],请检查");
            }
            // 校验长期应收款编号+合同编号是否存在，不存在则报错
            List<LongReceivableRegisterEntity> exsitList = list(new LambdaQueryWrapper<LongReceivableRegisterEntity>().eq(LongReceivableRegisterEntity::getLongReceivableNumber, a.getLongReceivableNumber())
                    .eq(LongReceivableRegisterEntity::getContractCode, a.getContractCode()));
            if (CollectionUtils.isEmpty(exsitList)) {
                throw new ServiceException("根据长期应收款编号[" + a.getLongReceivableNumber() + "]+合同编号[" + a.getContractCode() + "]未查询到长期应收款登记-基础信息");
            }
        });
        List<LongRepaymentPlanEntity> entityList = ListBeanUtil.copyList(list, LongRepaymentPlanEntity.class);
        List<LongReceivableRegisterEntity> exsitList = list(new LambdaQueryWrapper<LongReceivableRegisterEntity>()
                .in(LongReceivableRegisterEntity::getLongReceivableNumber, entityList.stream().map(LongRepaymentPlanEntity::getLongReceivableNumber).collect(Collectors.toList()))
                .in(LongReceivableRegisterEntity::getContractCode, entityList.stream().map(LongRepaymentPlanEntity::getContractCode).collect(Collectors.toList())));
        Map<String, List<LongRepaymentPlanEntity>> listMap = entityList.stream().collect(Collectors.groupingBy(a -> a.getLongReceivableNumber() + "-" + a.getContractCode()));
        for (String key : listMap.keySet()) {
            List<LongRepaymentPlanEntity> planEntityList = listMap.get(key);
            String longReceivableNumber = planEntityList.get(0).getLongReceivableNumber();
            String contractCode = planEntityList.get(0).getContractCode();
            LongReceivableRegisterEntity registerEntity = exsitList.stream().filter(a -> ObjectUtil.equals(a.getLongReceivableNumber(), longReceivableNumber)
                            && ObjectUtil.equals(a.getContractCode(), contractCode))
                    .sorted(Comparator.comparingInt(LongReceivableRegisterEntity::getVersionNum).reversed()).findFirst().orElse(null);
            if (ObjectUtil.isEmpty(registerEntity)) {
                throw new ServiceException("根据长期应收款编号[" + longReceivableNumber + "]+合同编号[" + contractCode + "]未查询到长期应收款登记-基础信息");
            }
            // 存在，判断状态
            if (ObjectUtil.equals(registerEntity.getProcessStatus(), ProcessStatusEnum.ENTERED.getCode()) ||
                    ObjectUtil.equals(registerEntity.getProcessStatus(), ProcessStatusEnum.REJECTED.getCode())) {
                // 状态为已录入，已拒绝 则删除原来的租金计划，写入新的
                iLongRepaymentPlanService.removeByLongRegisterIdList(Lists.newArrayList(registerEntity.getId()));
                planEntityList.stream().forEach(a -> {
                    // 设置出租登记id
                    a.setLongRegisterId(registerEntity.getId());
                });
                // 保存租金计划
                iLongRepaymentPlanService.saveBatch(planEntityList);
            } else {
                // 否则新增一条长期应收款
                LongReceivableRegisterEntity newRegisterEntity = BeanUtil.copyProperties(registerEntity, LongReceivableRegisterEntity.class, GenConstants.BASE_ENTITY);
                // 版本号+1
                newRegisterEntity.setVersionNum(registerEntity.getVersionNum() + 1);
                newRegisterEntity.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
                newRegisterEntity.setIsGenerateVoucher(null);
                newRegisterEntity.setVoucherId(null);
                newRegisterEntity.setErrorInfo(null);
                save(newRegisterEntity);
                planEntityList.stream().forEach(a -> {
                    // 设置长期应收款id
                    a.setLongRegisterId(newRegisterEntity.getId());
                });
                // 保存租金计划
                iLongRepaymentPlanService.saveBatch(planEntityList);
            }
        }
    }

    /**
     * 推送偿还计划
     *
     * @param ids
     */
    private void pushRepaymentPlan(List<Long> ids) {
        List<LongReceivableRegisterEntity> registerEntityList = listByIds(ids);
        List<LongRepaymentPlanEntity> entityList = Lists.newArrayList();
        for (LongReceivableRegisterEntity entity : registerEntityList) {
            // 查询偿还计划明细
            List<LongRepaymentPlanEntity> rePlanList = iLongRepaymentPlanService.list(new LambdaQueryWrapper<LongRepaymentPlanEntity>()
                    .eq(LongRepaymentPlanEntity::getLongReceivableNumber, entity.getLongReceivableNumber())
                    .eq(LongRepaymentPlanEntity::getContractCode, entity.getContractCode()));
            entityList.addAll(rePlanList);
        }
        Map<String, List<LongRepaymentPlanEntity>> listMap = entityList.stream().collect(Collectors.groupingBy(a -> a.getContractCode() + a.getLongReceivableNumber()));
        List<String> contractCodeList = entityList.stream().map(LongRepaymentPlanEntity::getContractCode).distinct().collect(Collectors.toList());
        List<ContractEntity> contractEntityList = iContractService.list(new LambdaQueryWrapper<ContractEntity>().in(ContractEntity::getContractCode, contractCodeList));

        for (String contractCode : listMap.keySet()) {
            // 按照日期排序
            List<LongRepaymentPlanEntity> list = listMap.get(contractCode).stream().sorted(Comparator.comparing(LongRepaymentPlanEntity::getReceivableDate)).collect(Collectors.toList());
            ContractEntity contractEntity = contractEntityList.stream().filter(a -> ObjectUtil.equals(a.getContractCode(), list.get(0).getContractCode())).findFirst().orElse(null);
            List<RepaymentPlanSaveDTO> planSaveDTOList = Lists.newArrayList();
            for (int i = 0; i < list.size(); i++) {
                LongRepaymentPlanEntity vo = list.get(i);
                RepaymentPlanSaveDTO planSaveDTO = new RepaymentPlanSaveDTO();
                planSaveDTO.setPeriods(i + 1);
                planSaveDTO.setPlanDate(DateUtils.toDate(vo.getReceivableDate()));
                planSaveDTO.setInterestAmount(vo.getReceivableInterest());
                planSaveDTO.setPrincipalAmount(vo.getReceivablePrincipal());
                planSaveDTO.setRentAmount(vo.getReceivableTotal());
                planSaveDTOList.add(planSaveDTO);
            }

            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("businessDate", LocalDate.now());
            dataMap.put("systemCode", SystemEnum.CWZT.getCode());
            dataMap.put("orgId", contractEntity.getOrgId());
            dataMap.put("contractCode", contractCode);
            dataMap.put("sceneCode", "长期应收款偿还计划修改");
            dataMap.put("orderId", list.stream().min(Comparator.comparingLong(LongRepaymentPlanEntity::getId)).map(LongRepaymentPlanEntity::getId).orElse(null));
            dataMap.put("repaymentPlanList", planSaveDTOList);
            log.info("长期应收款推送偿还计划发送mq数据：" + JSONObject.toJSONString(dataMap));
            // 发送mq 推送偿还计划
            rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_DIRECT_TRANSACTION_DATA, RabbitmqConfig.ROUTINGKEY_REPAYMENT_PLAN_DATA, JSONObject.toJSONString(dataMap));
            // iRepaymentPlanService.saveRawData("1", JSONObject.parseObject(JSONObject.toJSONString(dataMap)));
        }
    }

    /**
     * 保存基础信息
     *
     * @param list
     */
    private void save1(List<LongReceivableRegisterExcelDTO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        // 校验数据
        checkDate(list);
        // 转换签约主体
        Map<String, String> companyMap = iOrgCompanyService.selectAllOrgIdAndName().stream().collect(Collectors.toMap(e -> e.getOrgName(), e -> e.getOrgId(), (a, b) -> b));
        list.stream().forEach(a -> {
            String orgId = companyMap.get(a.getOrgId());
            if (ObjectUtil.isEmpty(orgId)) {
                throw new ServiceException("根据签约主体名称[" + a.getOrgId() + "]未查询到对应的签约主体");
            }
            a.setOrgId(orgId);
        });
        List<LongReceivableRegisterEntity> entityList = ListBeanUtil.copyList(list, LongReceivableRegisterEntity.class);
        List<LongReceivableRegisterEntity> exsitList = list(new LambdaQueryWrapper<LongReceivableRegisterEntity>()
                .in(LongReceivableRegisterEntity::getLongReceivableNumber, entityList.stream().map(LongReceivableRegisterEntity::getLongReceivableNumber).collect(Collectors.toList()))
                .in(LongReceivableRegisterEntity::getContractCode, entityList.stream().map(LongReceivableRegisterEntity::getContractCode).collect(Collectors.toList())));
        for (LongReceivableRegisterEntity entity : entityList) {
            // 找到版本号最大的数据
            LongReceivableRegisterEntity registerEntity = exsitList.stream().filter(a -> ObjectUtil.equals(a.getLongReceivableNumber(), entity.getLongReceivableNumber())
                            && ObjectUtil.equals(a.getContractCode(), entity.getContractCode()))
                    .sorted(Comparator.comparingInt(LongReceivableRegisterEntity::getVersionNum)).findFirst().orElse(null);
            if (ObjectUtil.isEmpty(registerEntity)) {
                // 不存在，新增
                save(entity);
            } else {
                // 存在，判断状态
                if (ObjectUtil.equals(registerEntity.getProcessStatus(), ProcessStatusEnum.ENTERED.getCode()) ||
                        ObjectUtil.equals(registerEntity.getProcessStatus(), ProcessStatusEnum.REJECTED.getCode())) {
                    // 状态为已录入，已拒绝 则更新
                    BeanUtil.copyProperties(entity, registerEntity, GenConstants.BASE_ENTITY);
                    registerEntity.setUpdateTime(null);
                    updateById(registerEntity);
                } else {
                    Integer versionNum = registerEntity.getVersionNum();
                    // 否则增加版本号
                    entity.setVersionNum(++versionNum);
                    save(entity);
                }
            }
        }
    }

    /**
     * 校验数据
     *
     * @param list
     */
    private void checkDate(List<LongReceivableRegisterExcelDTO> list) {
        Set<String> unionSet = new HashSet<>();
        list.stream().forEach(a -> {
            if (ObjectUtil.isEmpty(a.getLongReceivableNumber())) {
                throw new ServiceException("长期应收款编号为空");
            }
            if (ObjectUtil.isEmpty(a.getContractCode())) {
                throw new ServiceException("合同编号不能为空");
            }
            if (ObjectUtil.isEmpty(a.getProjectName())) {
                throw new ServiceException("项目名称不能为空");
            }
            if (ObjectUtil.isEmpty(a.getOrgId())) {
                throw new ServiceException("签约主体不能为空");
            }
            if (ObjectUtil.isEmpty(a.getOtherReceivable())) {
                throw new ServiceException("其他应收款不能为空");
            }
            // 校验长期应收款编号+合同编号重复
            if (!unionSet.add(a.getLongReceivableNumber() + a.getContractCode())) {
                throw new ServiceException("文件存在重复的长期应收款编号[" + a.getLongReceivableNumber() + "]+合同编号[" + a.getContractCode() + "],请检查");
            }
            // 校验合同号
            List<ContractEntity> contractEntityList = iContractService.list(new LambdaQueryWrapper<ContractEntity>().eq(ContractEntity::getContractCode, a.getContractCode()));
            if (CollectionUtils.isEmpty(contractEntityList)) {
                throw new ServiceException("根据合同编号[" + a.getContractCode() + "]未查询到对应的合同");
            }
            // 校验客户是否存在
            if (ObjectUtil.isNotEmpty(a.getClientCode())) {
                List<ClientEntity> clientEntityList = iClientService.list(new LambdaQueryWrapper<ClientEntity>().eq(ClientEntity::getClientCode, a.getClientCode()));
                if (CollectionUtils.isEmpty(clientEntityList)) {
                    throw new ServiceException("根据客户编号[" + a.getClientCode() + "]未查询到客户");
                }
            }

        });
    }

    /**
     * 批量生成凭证
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
        List<LongReceivableRegisterEntity> entityList = this.listByIds(ids);
        entityList.stream().forEach(v -> {
            if (!(ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus()) || ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus()))) {
                throw new ServiceException("处理状态为已录入或者已拒绝的才可以生成凭证");
            }
        });
        // 生成凭证前先删除之前的凭证
        batchDeleteVoucher(ids);

        List<Map<String, Object>> voucherMapList = Lists.newArrayList();
        for (LongReceivableRegisterEntity v : entityList) {
            // 查询偿还计划明细
            List<LongRepaymentPlanEntity> rePlanList = iLongRepaymentPlanService.list(new LambdaQueryWrapper<LongRepaymentPlanEntity>()
                    .eq(LongRepaymentPlanEntity::getLongReceivableNumber, v.getLongReceivableNumber())
                    .eq(LongRepaymentPlanEntity::getContractCode, v.getContractCode()));
            // if (CollectionUtils.isEmpty(rePlanList)) {
            //     continue;
            // }

            ExecuteCommonDTO executeCommonDTO = new ExecuteCommonDTO();
            executeCommonDTO.setSystemCode(SystemEnum.CWZT.getCode());
            executeCommonDTO.setSystemName(SystemEnum.CWZT.getDesc());
            executeCommonDTO.setSceneCode(SceneEnum.CQYSKXG.getCode());
            executeCommonDTO.setSceneName(SceneEnum.CQYSKXG.name());
            executeCommonDTO.setOrderId(v.getId().toString());
            executeCommonDTO.setOrgId(v.getOrgId());
            executeCommonDTO.setBusinessDate(new Date());
            executeCommonDTO.setContractCode(v.getContractCode());
            executeCommonDTO.setClientCode(v.getClientCode());
            executeCommonDTO.setAccountDate(new Date());
            executeCommonDTO.setBatchId(v.getId());
            executeCommonDTO.setBatchType(BatchTypeEnum.CQYSK.getCode());
            executeCommonDTO.setIsSubmit(isSubmit);

            Map<String, Object> dataMap = BeanUtil.beanToMap(executeCommonDTO);
            dataMap.put("receivablelongTermCode", v.getLongReceivableNumber());
            // 1.按长期应收款编号+合同编号维度汇总偿还计划上传的应收总额；
            BigDecimal receivableTotal = rePlanList.stream().map(LongRepaymentPlanEntity::getReceivableTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
            // 2.按合同编号+签约主体维度查询偿还计划历史表中系统来源!='FINHUB'，version最大时rent_amount的汇总额； 1-2
            List<RepaymentPlanHisEntity> repaymentPlanHisEntityList = iRepaymentPlanHisService.list(new LambdaQueryWrapper<RepaymentPlanHisEntity>()
                    .eq(RepaymentPlanHisEntity::getContractCode, v.getContractCode())
                    .eq(RepaymentPlanHisEntity::getOrgId, v.getOrgId())
                    .ne(RepaymentPlanHisEntity::getSystemCode, SystemEnum.CWZT.getCode())
            );
            BigDecimal rentAmountSum = BigDecimal.ZERO;
            if (CollectionUtils.isNotEmpty(repaymentPlanHisEntityList)) {
                Integer maxVersion = repaymentPlanHisEntityList.stream().map(RepaymentPlanHisEntity::getVersion).max(Integer::compareTo).orElse(1);
                rentAmountSum = repaymentPlanHisEntityList.stream().filter(a -> ObjectUtil.equals(a.getVersion(), maxVersion) && ObjectUtil.isNotEmpty(a.getRentAmount()))
                        .map(RepaymentPlanHisEntity::getRentAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            }

            dataMap.put("receivableAdjustAmount", NumberUtil.sub(receivableTotal, rentAmountSum));
            // 1.推送上传的偿还计划后（调推送偿还计划接口后）偿还计划表重新计算后的unrealized_revenue（会查出多个，任意取一个）；
            List<RepaymentPlanEntity> repaymentPlanEntityList = iRepaymentPlanService.list(new LambdaQueryWrapper<RepaymentPlanEntity>()
                    .eq(RepaymentPlanEntity::getContractCode, v.getContractCode())
                    .eq(RepaymentPlanEntity::getOrgId, v.getOrgId())
            );
            BigDecimal unrealizedRevenue = repaymentPlanEntityList.stream().filter(a -> ObjectUtil.isNotEmpty(a.getUnrealizedRevenue()))
                    .map(RepaymentPlanEntity::getUnrealizedRevenue).findFirst().orElse(BigDecimal.ZERO);

            // 2.按合同编号+签约主体维度查询偿还计划历史表中系统来源!='FINHUB'，version最大时的unrealized_revenue；1-2
            BigDecimal oldUnrealizedRevenue = BigDecimal.ZERO;
            if (CollectionUtils.isNotEmpty(repaymentPlanHisEntityList)) {
                Integer maxVersion = repaymentPlanHisEntityList.stream().map(RepaymentPlanHisEntity::getVersion).max(Integer::compareTo).orElse(1);
                oldUnrealizedRevenue = repaymentPlanHisEntityList.stream().filter(a -> ObjectUtil.equals(a.getVersion(), maxVersion) && ObjectUtil.isNotEmpty(a.getUnrealizedRevenue()))
                        .map(RepaymentPlanHisEntity::getUnrealizedRevenue).reduce(BigDecimal.ZERO, BigDecimal::add);
            }
            dataMap.put("unrealizedRevenueAdjustAmount", NumberUtil.sub(unrealizedRevenue, oldUnrealizedRevenue));
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
            }
            if (CollectionUtils.isNotEmpty(infoVO.getVoucherDTOList())) {
                voucherIds = infoVO.getVoucherDTOList().stream().map(VoucherDTO::getId).map(String::valueOf).collect(Collectors.toList()).stream().collect(Collectors.joining(","));
            }

            LongReceivableRegisterEntity receivableRegisterEntity = this.getById(Long.parseLong(infoVO.getOrderId()));
            receivableRegisterEntity.setAccountDate(LocalDateTime.now());
            receivableRegisterEntity.setIsGenerateVoucher(isGenerateVoucher);
            receivableRegisterEntity.setErrorInfo(errorInfo);
            receivableRegisterEntity.setVoucherId(voucherIds);
            receivableRegisterEntity.setUpdateTime(null);
            this.updateById(receivableRegisterEntity);
        }
        return Boolean.TRUE;
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
        List<LongReceivableRegisterEntity> entityList = this.listByIds(ids);
        List<ApproveDTO> approveDTOList = Lists.newArrayList();
        entityList.stream().forEach(v -> {
            if (!(ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus()) || ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus()))) {
                throw new ServiceException("只有处理状态为已录入或者已拒绝的才可以提交");
            }
            ApproveDTO approveDTO = new ApproveDTO();
            approveDTO.setDocumentId(v.getId());
            approveDTO.setDocumentType(BatchTypeEnum.CQYSK.getCode());
            approveDTO.setUrl(approveUrl + v.getId());
            approveDTOList.add(approveDTO);
        });
        // 生成凭证
        Boolean generateVoucherFlag = generateVoucher(ids, YesOrNoEnum.YES.getCode());
        if (generateVoucherFlag) {
            // 发送审核
            Map<Long, Long> processInstantIdMap = iApproveService.submit(approveDTOList);

            List<LongReceivableRegisterEntity> newEntityList = this.listByIds(ids);
            newEntityList.stream().forEach(v -> {
                v.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
                if (null != processInstantIdMap && processInstantIdMap.containsKey(v.getId())) {
                    v.setProcessInstanceId(processInstantIdMap.get(v.getId()));
                }
            });

            // 推送偿还计划
            pushRepaymentPlan(ids);
            // 凭证生成成功
            return this.updateBatchById(newEntityList);
        } else {
            // 凭证生成失败
            throw new ServiceException("生成凭证失败，提交失败");
        }
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
        List<LongReceivableRegisterEntity> entityList = this.listByIds(ids);
        entityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.SUBMITTED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("只有处理状态为已提交的才可以撤回");
            }
            v.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
        });
        iApproveService.withdraw(entityList.stream().map(LongReceivableRegisterEntity::getProcessInstanceId).collect(Collectors.toList()));
        return this.updateBatchById(entityList);
    }

    /**
     * 删除凭证
     *
     * @param ids
     */
    @Override
    public void batchDeleteVoucher(List<Long> ids) {
        // 获取所有的凭证Id
        List<LongReceivableRegisterEntity> entityList = this.listByIds(ids);
        // 逗号拆分
        List<Long> voucherIdList = Lists.newArrayList();
        entityList.stream().forEach(v -> {
            if (StringUtils.isNotEmpty(v.getVoucherId())) {
                voucherIdList.addAll(Arrays.stream(v.getVoucherId().split(",")).map(Long::parseLong).collect(Collectors.toList()));
            }
        });
        if (CollectionUtils.isNotEmpty(voucherIdList)) {
            iVoucherService.deleteByIdList(voucherIdList);
        }
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
        List<LongReceivableRegisterEntity> transferRegisterEntityList = this.listByIds(ids);
        transferRegisterEntityList.stream().forEach(v -> {
            if (!(ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus()) || ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus()))) {
                throw new ServiceException("只有处理状态为已录入或者已拒绝的才可以删除");
            }
        });
        // 删除凭证
        batchDeleteVoucher(ids);
        // 删除租金计划
        iLongRepaymentPlanService.removeByLongRegisterIdList(ids);
        // 删除分摊表
        iLongApportionService.removeByLongRegisterIdList(ids);
        return removeBatchByIds(ids);

    }

    /**
     * 分摊
     *
     * @param ids
     * @return
     */
    @Override
    public Boolean apportion(List<Long> ids) {
        List<LongReceivableRegisterEntity> entityList = this.listByIds(ids);
        if (CollectionUtils.isEmpty(entityList)) {
            return Boolean.TRUE;
        }
        List<LongIncomeConfirmEntity> saveList = Lists.newArrayList();
        for (LongReceivableRegisterEntity longEntity : entityList) {
            LongApportionQueryDTO queryDTO = new LongApportionQueryDTO();
            queryDTO.setLongRegisterIdList(Lists.newArrayList(longEntity.getId()));
            List<LongApportionVO> longApportionVOList = iLongApportionService.selectList(queryDTO);
            if (CollectionUtils.isEmpty(longApportionVOList)) {
                throw new ServiceException("长期应收款编号[" + longEntity.getLongReceivableNumber() + "]+合同编号[" + longEntity.getContractCode() + "]没有分摊表数据，不能分摊");
            }

            // 查询收入确认数据
            List<LongIncomeConfirmEntity> longIncomeConfirmEntityList = iLongIncomeConfirmService.list(new LambdaQueryWrapper<LongIncomeConfirmEntity>()
                    .eq(LongIncomeConfirmEntity::getLongReceivableNumber, longEntity.getLongReceivableNumber())
                    .eq(LongIncomeConfirmEntity::getContractCode, longEntity.getContractCode()));

            // 是否生成收入确认 标识
            boolean generateFlag = true;
            if (CollectionUtils.isNotEmpty(longIncomeConfirmEntityList)) {
                for (LongIncomeConfirmEntity entity : longIncomeConfirmEntityList) {
                    if (!(ProcessStatusEnum.ENTERED.getCode().equals(entity.getProcessStatus()) || ProcessStatusEnum.REJECTED.getCode().equals(entity.getProcessStatus()))) {
                        // 处理状态不为已录入或者已拒绝 不重新生成
                        generateFlag = false;
                    }
                }
            }
            if (generateFlag) {
                // 1.先删除此合同的收入确认数据
                iLongIncomeConfirmService.deleteByLongNumberAndContractCode(longEntity.getLongReceivableNumber(), longEntity.getContractCode());

                // 2.生成收入确认数据
                // 分摊表按照年月分组
                Map<String, List<LongApportionVO>> monthListMap = longApportionVOList.stream().collect(Collectors.groupingBy(a -> a.getReceivableDate().format(DateTimeFormatter.ofPattern("yyyy-MM"))));
                for (String accountMonth : monthListMap.keySet()) {
                    List<LongApportionVO> list = monthListMap.get(accountMonth);
                    LongIncomeConfirmEntity saveDTO = new LongIncomeConfirmEntity();
                    saveDTO.setLongReceivableNumber(longEntity.getLongReceivableNumber());
                    saveDTO.setContractCode(longEntity.getContractCode());
                    saveDTO.setProjectName(longEntity.getProjectName());
                    saveDTO.setOrgId(longEntity.getOrgId());
                    saveDTO.setClientName(longEntity.getClientName());
                    saveDTO.setClientCode(longEntity.getClientCode());
                    saveDTO.setAccountMonth(DateUtils.parseDate(accountMonth));
                    saveDTO.setConfirmIncomeAmount(list.stream().map(LongApportionVO::getConfirmIncome).reduce(BigDecimal.ZERO, BigDecimal::add));
                    saveList.add(saveDTO);
                }
            }
        }
        // 保存租金收入确认数据
        if (CollectionUtils.isNotEmpty(saveList)) {
            iLongIncomeConfirmService.saveBatch(saveList);
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
        LongReceivableRegisterEntity entity = this.getById(approveDTO.getDocumentId());
        if (ObjectUtil.isEmpty(entity)) {
            throw new ServiceException("转入登记数据不存在");
        }
        // 修改凭证状态，通过和驳回都修改
        updateVoucherStatus(Lists.newArrayList(approveDTO.getDocumentId()), approveDTO);
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
        List<LongReceivableRegisterEntity> entityList = this.listByIds(ids);
        // 逗号拆分
        List<String> voucherIdList = Lists.newArrayList();
        entityList.stream().forEach(v -> {
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(v.getVoucherId())) {
                voucherIdList.addAll(Arrays.stream(v.getVoucherId().split(",")).collect(Collectors.toList()));
            }
        });
        iVoucherService.updateStatusByids(voucherIdList, approveDTO.getDocumentStatus(),
                approveDTO.getApproverNum(), approveDTO.getApproverName());
    }

}

