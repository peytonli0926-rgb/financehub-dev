package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.util.DateUtils;
import com.alibaba.nacos.shaded.com.google.gson.JsonObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.utfinancing.financehub.admin.api.RemoteDictService;
import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.enums.ClaimOperationTypeEnum;
import com.utfinancing.financehub.engine.enums.DictTypeEnum;
import com.utfinancing.financehub.engine.enums.ProcessStatusEnum;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.entity.NonConfirmCollectionSumEntity;
import com.utfinancing.financehub.engine.finance.entity.VoucherEntity;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.BusinessClaimRepaymentRecordVO;
import com.utfinancing.financehub.engine.finance.entity.BusinessClaimRepaymentRecordEntity;
import com.utfinancing.financehub.engine.finance.mapper.BusinessClaimRepaymentRecordMapper;
import com.utfinancing.financehub.engine.finance.service.IBusinessClaimRepaymentRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.finance.service.INonConfirmCollectionSumService;
import com.utfinancing.financehub.engine.finance.service.IVoucherService;
import com.utfinancing.financehub.engine.rule.entity.InterfaceDataEntity;
import com.utfinancing.financehub.engine.rule.service.IInterfaceDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.lang.invoke.LambdaMetafactory;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author : robjiang
 * @Date : Create in 2024-03-21
 * @Description :  BusinessClaimRepaymentRecord服务实现类
 * @Modified :
 */
@Service
@Transactional
@Slf4j
public class BusinessClaimRepaymentRecordServiceImpl extends ServiceImpl<BusinessClaimRepaymentRecordMapper,
        BusinessClaimRepaymentRecordEntity> implements IBusinessClaimRepaymentRecordService {

    @Lazy
    @Resource
    private BusinessClaimRepaymentRecordMapper businessClaimRepaymentRecordMapper;

    @Lazy
    @Resource
    private INonConfirmCollectionSumService nonConfirmCollectionSumService;

    @Resource
    private IInterfaceDataService interfaceDataService;

    @Resource
    private IVoucherService voucherService;

    @Resource
    private RemoteDictService remoteDictService;

    @Override
    public Long saveBusinessClaimRepaymentRecord(BusinessClaimRepaymentRecordDTO dto) {
        BusinessClaimRepaymentRecordEntity entity = BeanUtil.copyProperties(dto, BusinessClaimRepaymentRecordEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateBusinessClaimRepaymentRecord(Long id, BusinessClaimRepaymentRecordDTO dto) {
        BusinessClaimRepaymentRecordEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public BusinessClaimRepaymentRecordDTO getBusinessClaimRepaymentRecordDTOById(Long id) {
        BusinessClaimRepaymentRecordEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, BusinessClaimRepaymentRecordDTO.class);
    }

    @Override
    public IPage<BusinessClaimRepaymentRecordVO> selectPage(BusinessClaimRepaymentRecordQueryDTO queryDTO) {
        LambdaQueryWrapper<BusinessClaimRepaymentRecordEntity> queryWrapper = Wrappers.<BusinessClaimRepaymentRecordEntity>lambdaQuery();
        //这里注入查询条件
        IPage<BusinessClaimRepaymentRecordEntity> entityIPage = businessClaimRepaymentRecordMapper.selectPage(new Page<BusinessClaimRepaymentRecordEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, BusinessClaimRepaymentRecordVO.class);
    }

    /**
     * 抽取业务系统已经认领的记录
     */
    public void extractClaimPaymentRecord() {
        log.info("抽取认领记录 Start...");
        List<InterfaceDataEntity> interfaceDataEntityList = interfaceDataService.selectEbankSerialNumberNotNullData();
        if (interfaceDataEntityList == null || interfaceDataEntityList.isEmpty()) {
            return;
        }

        log.info("抽取认领记录 数量："+interfaceDataEntityList.size());
        R<List<SysDictData>> orgR = remoteDictService.listDictData(DictTypeEnum.COMPANY.getCode());
        Map<String, String> orgMap = orgR.getData().stream().collect(Collectors.toMap(e -> e.getDictValue(),
                e -> e.getDictLabel()));


        // 更新未确认收款汇总表的认领记录
        // 每1000条批处理一次
        List<InterfaceDataEntity> interfaceDataEntitySubList = new ArrayList<>();
        for (int i = 0; i < interfaceDataEntityList.size(); i++) {
            interfaceDataEntitySubList.add(interfaceDataEntityList.get(i));

            if (interfaceDataEntitySubList.size() > 1000 || i == interfaceDataEntityList.size() - 1) {
                this.businessClaimRecordBatchProcess(interfaceDataEntitySubList, orgMap);
                interfaceDataEntitySubList.clear();
            }
        }
        log.info("抽取认领记录 End");
    }

    private void businessClaimRecordBatchProcess(List<InterfaceDataEntity> interfaceDataEntityList,
                                                 Map<String, String> orgMap) {
        if (interfaceDataEntityList == null || interfaceDataEntityList.size() == 0) {
            return ;
        }

        // 认领记录保存
        List<BusinessClaimRepaymentRecordEntity> businessClaimRepaymentRecordEntityList = new ArrayList<>();

        LambdaQueryWrapper<VoucherEntity> voucherEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 取得凭证信息
        List<Long> interfaceIds = interfaceDataEntityList.stream().map(InterfaceDataEntity::getId).collect(Collectors.toList());
        voucherEntityLambdaQueryWrapper.in(VoucherEntity::getInterfaceDataId, interfaceIds);
        List<VoucherEntity> voucherEntityList = voucherService.list(voucherEntityLambdaQueryWrapper);
        Map<Long, List<VoucherEntity>> voucherEntityMap = voucherEntityList.stream().collect(Collectors.groupingBy(
                e->e.getInterfaceDataId()));

        for (InterfaceDataEntity interfaceDataEntity : interfaceDataEntityList) {

            log.info(String.format("interfaceDataId=%s", interfaceDataEntity.getId()));

            BusinessClaimRepaymentRecordEntity entity = new BusinessClaimRepaymentRecordEntity();
            entity.setId(IdWorker.getId());
            // 已认领金额
            Object receiveUnconfirmed = interfaceDataEntity.getInterfaceData().get("receiveUnconfirmed");
            BigDecimal claimAmount = new BigDecimal(0);
            if (receiveUnconfirmed != null) {
                claimAmount = claimAmount.add(new BigDecimal(receiveUnconfirmed.toString()));
            }
            Object payableReceivableUnconfirmReceip = interfaceDataEntity.getInterfaceData().get("payableReceivableUnconfirmReceipt");
            if (payableReceivableUnconfirmReceip != null) {
                claimAmount = claimAmount.add(new BigDecimal(payableReceivableUnconfirmReceip.toString()));
            }
            Object payableOverdeposit = interfaceDataEntity.getInterfaceData().get("payableOverdeposit");
            if (payableOverdeposit != null) {
                claimAmount = claimAmount.add(new BigDecimal(payableOverdeposit.toString()));
            }
            Object receivableUnconfirm = interfaceDataEntity.getInterfaceData().get("receivableUnconfirm");
            if (receivableUnconfirm != null) {
                claimAmount = claimAmount.add(new BigDecimal(receivableUnconfirm.toString()));
            }
            entity.setClaimAmount(claimAmount);
            entity.setBusinessDate(interfaceDataEntity.getVoucherDate());
            entity.setCreateBy("system");
            entity.setCreateTime(LocalDateTime.now());
            entity.setUpdateBy("system");
            entity.setUpdateTime(LocalDateTime.now());
            entity.setClientCode(interfaceDataEntity.getClientCode());
            entity.setContractCode(interfaceDataEntity.getContractCode());
            Object currencyType = interfaceDataEntity.getInterfaceData().get("currencyType");
            if (currencyType != null) {
                entity.setCurrencyType(currencyType.toString());
            }
            entity.setSystemCode(interfaceDataEntity.getSystemCode());
            entity.setEbankSerialNumber(interfaceDataEntity.getEbankBatchNo());
            entity.setOrderId(interfaceDataEntity.getOrderId());
            if (StringUtils.isNotEmpty(interfaceDataEntity.getOrgId())) {
                entity.setOrgId(interfaceDataEntity.getOrgId());
            } else {
                entity.setOrgId(interfaceDataEntity.getBankOrgId());
            }
            entity.setOrgName(orgMap.get(interfaceDataEntity.getOrgId()));
            entity.setSceneCode(interfaceDataEntity.getSceneCode());
            entity.setSceneName(interfaceDataEntity.getSceneName());
            entity.setBatchNo(new BigDecimal(1));
            entity.setOperationType(ClaimOperationTypeEnum.BUSINESS_AUTO_CLAIM.getCode());
            entity.setProcessStatus(ProcessStatusEnum.REVIEWED.getCode());

            List<VoucherEntity> voucherEntityTemp = voucherEntityMap.get(interfaceDataEntity.getId());
            if (voucherEntityTemp != null && !voucherEntityTemp.isEmpty()) {
                entity.setVoucherIds(voucherEntityTemp.stream().map(e->String.valueOf(e.getId())).collect(Collectors.joining(",")));
            }
            businessClaimRepaymentRecordEntityList.add(entity);

            interfaceDataEntity.setIsExtractData(YesOrNoEnum.YES.getCode());
        }
        this.saveBatch(businessClaimRepaymentRecordEntityList);
        interfaceDataService.updateBatchById(interfaceDataEntityList);
    }

    public boolean processStatusUpdate(Long detailId, String status) {
        if (detailId == null) {
            return true;
        }
        LambdaUpdateWrapper<BusinessClaimRepaymentRecordEntity> wrapper = new LambdaUpdateWrapper();
        wrapper.eq(BusinessClaimRepaymentRecordEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        wrapper.eq(BusinessClaimRepaymentRecordEntity::getNonConfirmSecondDetailId, detailId);
        wrapper.set(BusinessClaimRepaymentRecordEntity::getProcessStatus, status);
        return this.update(wrapper);
    }

    public boolean processStatusBatchUpdate(List<Long> detailIds, String status) {
        if (detailIds == null || detailIds.isEmpty()) {
            return true;
        }
        LambdaUpdateWrapper<BusinessClaimRepaymentRecordEntity> wrapper = new LambdaUpdateWrapper();
        wrapper.eq(BusinessClaimRepaymentRecordEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        wrapper.in(BusinessClaimRepaymentRecordEntity::getNonConfirmSecondDetailId, detailIds);
        wrapper.set(BusinessClaimRepaymentRecordEntity::getProcessStatus, status);
        return this.update(wrapper);
    }

    /**
     * 根据批扣流水号查询认领记录
     */
    public List<BusinessClaimRepaymentRecordEntity> selectClaimRecordByDeductBatchNo(String deductBatchNo,
               List<String> processStatusList, String contractCode, List<String> operationTypeList) {
        if (StringUtils.isEmpty(deductBatchNo)) {
            return new ArrayList<>();
        }

        List<String> deductBatchNoList = Arrays.asList(deductBatchNo.split(","));
        LambdaQueryWrapper<BusinessClaimRepaymentRecordEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(BusinessClaimRepaymentRecordEntity::getEbankSerialNumber, deductBatchNoList);
        if (processStatusList != null && !processStatusList.isEmpty()) {
            wrapper.in(BusinessClaimRepaymentRecordEntity::getProcessStatus, processStatusList);
        }
        if (StringUtils.isNotEmpty(contractCode)) {
            wrapper.eq(BusinessClaimRepaymentRecordEntity::getContractCode, contractCode);
        }
        if (operationTypeList != null && !operationTypeList.isEmpty()) {
            wrapper.in(BusinessClaimRepaymentRecordEntity::getOperationType, operationTypeList);
        }
        wrapper.eq(BusinessClaimRepaymentRecordEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        wrapper.orderByAsc(BusinessClaimRepaymentRecordEntity::getBusinessDate);
        return businessClaimRepaymentRecordMapper.selectList(wrapper);
    }


    /**
     * 根据未确认收款明细表id查询认领记录
     */
    public List<BusinessClaimRepaymentRecordEntity> selectClaimRecordBySecondDetailId(String nonConfirmSecondDetailId,
                                                                                      List<String> processStatusList) {
        if (StringUtils.isEmpty(nonConfirmSecondDetailId)) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<BusinessClaimRepaymentRecordEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(BusinessClaimRepaymentRecordEntity::getNonConfirmSecondDetailId, nonConfirmSecondDetailId);
        if (processStatusList != null && processStatusList.isEmpty()) {
            wrapper.in(BusinessClaimRepaymentRecordEntity::getProcessStatus, processStatusList);
        }
        wrapper.eq(BusinessClaimRepaymentRecordEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        return businessClaimRepaymentRecordMapper.selectList(wrapper);
    }

    /**
     * 取最大batch no值
     */
    public int getMaxBatchNo(String ebankSerialNumber) {
        if (StringUtils.isEmpty(ebankSerialNumber)) {
            throw new ServiceException("批扣流水号不能为空!");
        }

        List<String> deductBatchNoList = Arrays.asList(ebankSerialNumber.split(","));
        LambdaQueryWrapper<BusinessClaimRepaymentRecordEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(BusinessClaimRepaymentRecordEntity::getEbankSerialNumber, deductBatchNoList);
        wrapper.eq(BusinessClaimRepaymentRecordEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        List<BusinessClaimRepaymentRecordEntity> businessClaimRepaymentRecordEntityList = businessClaimRepaymentRecordMapper.selectList(wrapper);
        if (businessClaimRepaymentRecordEntityList == null || businessClaimRepaymentRecordEntityList.isEmpty()) {
            return 1;
        } else {
//            Optional<BigDecimal> maxValue = businessClaimRepaymentRecordEntityList.stream().map(
//                    BusinessClaimRepaymentRecordEntity::getBatchNo).max(BigDecimal::compareTo);
//            return maxValue.get().intValue();
            // 将null替换为0并在空集合时返回默认值0
            int maxBatchNo = businessClaimRepaymentRecordEntityList.stream()
                    .map(record -> Optional.ofNullable(record.getBatchNo()).orElse(BigDecimal.ZERO))
                    .max(BigDecimal::compareTo)
                    .map(BigDecimal::intValue)
                    .orElse(0);
            return maxBatchNo;
        }
    }

    /**
     * 取得已经认领金额
     */
    public BigDecimal getReadyClaimAmount(List<BusinessClaimRepaymentRecordEntity> businessClaimRepaymentRecordEntityList) {
        if (businessClaimRepaymentRecordEntityList == null || businessClaimRepaymentRecordEntityList.isEmpty()) {
            return new BigDecimal(0);
        } else {
            BigDecimal readyClaimAmount = businessClaimRepaymentRecordEntityList.stream().map(
                    BusinessClaimRepaymentRecordEntity::getClaimAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            return readyClaimAmount;
        }
    }

    /**
     * 根据客户编码查询已经认领金额和到账金额总和
     */
    public SelectClaimByClientCodeDTO selectClaimByClientCode(String clientCode) {
        SelectClaimByClientCodeDTO params = new SelectClaimByClientCodeDTO();
        params.setClientCode(clientCode);
        return businessClaimRepaymentRecordMapper.selectClaimByClientCode(params);
    }

    /**
     * 修改入账日期查询
     */
    public List<SelectIncomeDateInfoOutputDTO> selectIncomeDateInfo(SelectIncomeDateInfoInputDTO params) {
        return businessClaimRepaymentRecordMapper.selectIncomeDateInfo(params);
    }


    /**
     * 根据合同及日期，在参数日期之后取得合同认领的数据
     */
    public BusinessClaimRepaymentRecordEntity queryClaimAmountByContract(String contractCode, Date date) {
        SelectClaimDataByConstractInputDTO inputDTO = new SelectClaimDataByConstractInputDTO();
        inputDTO.setContractCode(contractCode);
        inputDTO.setBusinessDate(date);
        BusinessClaimRepaymentRecordEntity result = businessClaimRepaymentRecordMapper.selectClaimDataByContract(inputDTO);
        if (result == null) {
            result = new BusinessClaimRepaymentRecordEntity();
            result.setContractCode(contractCode);
            result.setClaimAmount(BigDecimal.ZERO);
        }
        return result;
    }
}

