package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.nacos.common.utils.MapUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.utfinancing.financehub.admin.api.RemoteDictService;
import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.approve.model.dto.ApproveDTO;
import com.utfinancing.financehub.engine.approve.service.IApproveService;
import com.utfinancing.financehub.engine.claim.model.vo.ClaimOrderSpecialVO;
import com.utfinancing.financehub.engine.claim.model.vo.ClaimOrderVoucherVO;
import com.utfinancing.financehub.engine.claim.service.IClaimOrderSpecialService;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.entity.ContractEntity;
import com.utfinancing.financehub.engine.finance.model.dto.CostChannelFeeQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.CostChannelFeeDTO;
import com.utfinancing.financehub.engine.finance.model.dto.OrgCompanyQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherDTO;
import com.utfinancing.financehub.engine.finance.model.vo.*;
import com.utfinancing.financehub.engine.finance.entity.CostChannelFeeEntity;
import com.utfinancing.financehub.engine.finance.mapper.CostChannelFeeMapper;
import com.utfinancing.financehub.engine.finance.service.IContractService;
import com.utfinancing.financehub.engine.finance.service.ICostChannelFeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.finance.service.IOrgCompanyService;
import com.utfinancing.financehub.engine.finance.service.IVoucherService;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import com.utfinancing.financehub.engine.rule.model.vo.VoucherInfoVO;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import com.utfinancing.financehub.engine.utils.CommonDateUtils;
import com.utfinancing.financehub.engine.verification.entity.CourtCostDetailsEntity;
import com.utfinancing.financehub.engine.verification.entity.VerificationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.elasticsearch.ElasticSearchReactiveHealthContributorAutoConfiguration;
import org.springframework.expression.spel.ast.NullLiteral;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author : bruyang
 * @Date : Create in 2023-12-13
 * @Description :  CostChannelFee服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class CostChannelFeeServiceImpl extends ServiceImpl<CostChannelFeeMapper, CostChannelFeeEntity> implements ICostChannelFeeService {

    private final CostChannelFeeMapper costChannelFeeMapper;
    
    private final IRuleService iRuleService;

    private final IContractService iContractService;

    private final RemoteDictService remoteDictService;

    @Resource
    private final IApproveService iApproveService;

    @Value("${approve.url.costCategory-url:null}")
    private String approveUrl;

    @Resource
    private IVoucherService iVoucherService;

    @Resource
    private IClaimOrderSpecialService iClaimOrderSpecialService;

    @Resource
    private IOrgCompanyService iOrgCompanyService;


    @Override
    public Long saveCostChannelFee(CostChannelFeeDTO dto) {
        CostChannelFeeEntity entity = BeanUtil.copyProperties(dto, CostChannelFeeEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateCostChannelFee(Long id, CostChannelFeeDTO dto) {
        CostChannelFeeEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public CostChannelFeeDTO getCostChannelFeeDTOById(Long id) {
        CostChannelFeeEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, CostChannelFeeDTO.class);
    }

    @Override
    public IPage<CostChannelFeeVO> selectPage(CostChannelFeeQueryDTO queryDTO) {
//        LambdaQueryWrapper<CostChannelFeeEntity> queryWrapper =getQueryWrapper(queryDTO);
//        IPage<CostChannelFeeEntity> entityIPage = costChannelFeeMapper.selectPage(new Page<CostChannelFeeEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
//        IPage<CostChannelFeeVO> costChannelFeeVOIPage = ListBeanUtil.copyPage(entityIPage, CostChannelFeeVO.class);
        Page page = new Page(queryDTO.getPageNum(),queryDTO.getPageSize());
        IPage<CostChannelFeeVO> costChannelFeeVOIPage = costChannelFeeMapper.selectPageByCondition(page,queryDTO);
        costChannelFeeVOIPage.getRecords().forEach(v -> {
            if (CostMainCategoryExpenseTypeEnum.GPS.getCode().equals(v.getExpenseMainCategoryType())) {
                v.setBatchType(BatchTypeEnum.GPS.getCode());
            } else if (CostMainCategoryExpenseTypeEnum.GRACELETE.getCode().equals(v.getExpenseMainCategoryType())) {
                v.setBatchType(BatchTypeEnum.SHSBK.getCode());
            } else if (CostMainCategoryExpenseTypeEnum.SERVICE.getCode().equals(v.getExpenseMainCategoryType())) {
                v.setBatchType(BatchTypeEnum.QDF.getCode());
            } else if (CostMainCategoryExpenseTypeEnum.COLLECT_FEE.getCode().equals(v.getExpenseMainCategoryType())) {
                v.setBatchType(BatchTypeEnum.SCF.getCode());
            }
        });
        return costChannelFeeVOIPage;
    }

    private LambdaQueryWrapper<CostChannelFeeEntity> getQueryWrapper(CostChannelFeeQueryDTO queryDTO){
        LambdaQueryWrapper<CostChannelFeeEntity> queryWrapper = Wrappers.<CostChannelFeeEntity>lambdaQuery();
        //这里注入查询条件
        if (StringUtils.isNotEmpty(queryDTO.getExpenseMainCategoryType())) {
            queryWrapper.eq(CostChannelFeeEntity::getExpenseMainCategoryType,queryDTO.getExpenseMainCategoryType());
        }
        if (StringUtils.isNotEmpty(queryDTO.getChannelCode())) {
            queryWrapper.like(CostChannelFeeEntity::getChannelCode,queryDTO.getChannelCode());
        }
        if (StringUtils.isNotEmpty(queryDTO.getChannelType())) {
            queryWrapper.eq(CostChannelFeeEntity::getChannelType,queryDTO.getChannelType());
        }
        if (StringUtils.isNotEmpty(queryDTO.getChannelName())) {
            queryWrapper.like(CostChannelFeeEntity::getChannelName,queryDTO.getChannelName());
        }
        if (StringUtils.isNotEmpty(queryDTO.getContractCode())) {
            queryWrapper.like(CostChannelFeeEntity::getContractCode,queryDTO.getContractCode());
        }
        return queryWrapper;
    }

    @Override
    public Boolean importTemplate(MultipartFile file, String expenseType,String fileType) {
        try {
            //调用字典获取是否区分合同状态
            //获取字典是否区分合同状态
            R<List<SysDictData>> sysDictR = remoteDictService.listDictData(DictTypeEnum.IS_DIFFER_CONTRACT_STATUS.getCode());
            if (ObjectUtil.isNull(sysDictR)) {
                throw new ServiceException("调用字典服务失败");
            }
            Map<String,String> differContractStatusMap = sysDictR.getData().stream().collect(Collectors.toMap(SysDictData::getDictLabel,SysDictData::getRemark, (k1, k2) ->k2));
            List<CostChannelFeeEntity> saveUpdateEntityList = Lists.newArrayList();
            List<CostChannelFeeEntity> channelFeeEntityList = Lists.newArrayList();
            if (CostMainCategoryExpenseTypeEnum.SERVICE.getCode().equals(expenseType)) {
                //支付EXCEl
                if ("1".equals(fileType)) {
                    ExcelUtil<CostChannelFeeExcelVO> util = new ExcelUtil<CostChannelFeeExcelVO>(CostChannelFeeExcelVO.class);
                    List<CostChannelFeeExcelVO> costChannelFeeExcelVOList = util.importExcel(file.getInputStream());
                    channelFeeEntityList = BeanUtil.copyToList(costChannelFeeExcelVOList, CostChannelFeeEntity.class);
                } else if ("2".equals(fileType)) {
                    ExcelUtil<CostChannelFeeTaxExcelVO> util = new ExcelUtil<CostChannelFeeTaxExcelVO>(CostChannelFeeTaxExcelVO.class);
                    List<CostChannelFeeTaxExcelVO> costChannelFeeTaxExcelVOList = util.importExcel(file.getInputStream());
                    channelFeeEntityList = BeanUtil.copyToList(costChannelFeeTaxExcelVOList, CostChannelFeeEntity.class);
                }
                checkData(channelFeeEntityList,expenseType);
                //同一个渠道类型+渠道号+合同编码+业务日期如果存在，直接更新
                saveUpdateEntityList = setChannelFeeEntity(channelFeeEntityList, fileType);
            } else if (CostMainCategoryExpenseTypeEnum.GPS.getCode().equals(expenseType)
                    || CostMainCategoryExpenseTypeEnum.GRACELETE.getCode().equals(expenseType)
                    || CostMainCategoryExpenseTypeEnum.COLLECT_FEE.getCode().equals(expenseType)) {
                ExcelUtil<CostGpsAndBraceleteFeeExcelVO> util = new ExcelUtil<CostGpsAndBraceleteFeeExcelVO>(CostGpsAndBraceleteFeeExcelVO.class);
                List<CostGpsAndBraceleteFeeExcelVO> gpsAndBraceleteFeeExcelVOList = util.importExcel(file.getInputStream());
                channelFeeEntityList = BeanUtil.copyToList(gpsAndBraceleteFeeExcelVOList, CostChannelFeeEntity.class);
                checkData(channelFeeEntityList,expenseType);
                saveUpdateEntityList = channelFeeEntityList;
            }

            saveUpdateEntityList.stream().forEach(v -> {
                v.setExpenseMainCategoryType(expenseType);
                String isContractStatus = "是".equals(differContractStatusMap.get(CostChannelTypeEnum.getDescByCode(v.getChannelType()))) ? "1" : "0";
                v.setIsContractStatus(isContractStatus);
            });
            return saveOrUpdateBatch(saveUpdateEntityList);
        } catch (Exception exception) {
            throw new ServiceException("导入数据失败，失败原因："+exception.getMessage());
        }
    }

    public void checkData(List<CostChannelFeeEntity> channelFeeEntityList,String expenseType) {
        if (CollectionUtils.isEmpty(channelFeeEntityList)) {
              throw new ServiceException("导入文件数据为空");
        }
        //查询所有的数据
        Map<String, CostChannelFeeEntity> feeEntityMap = Maps.newHashMap();
        List<CostChannelFeeEntity> feeEntityList = this.lambdaQuery().eq(CostChannelFeeEntity::getIsAutoGenerate,"0").list();
        if (CollectionUtils.isNotEmpty(feeEntityList)) {
            feeEntityMap = feeEntityList.stream().collect(Collectors.toMap(v -> v.getChannelType()+"-"+v.getContractCode()+"-"+DateUtil.format(v.getBusinessDate(),"yyyy-MM-dd"),v->v,(k1,k2)->k2));
        }
        //合同号是否存在系统中
        List<String> contractCodeList = channelFeeEntityList.stream().map(CostChannelFeeEntity::getContractCode).collect(Collectors.toList());
        List<String> contractList =Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(contractCodeList)) {
            contractList = iContractService.lambdaQuery().in(ContractEntity::getContractCode, contractCodeList).list().stream().map(ContractEntity::getContractCode).collect(Collectors.toList());
        }
        List<String> finalContractList = contractList;
        Map<String, CostChannelFeeEntity> finalFeeEntityMap = feeEntityMap;
        //转换签约主体
        Map<String,String> orgIdNameMap =  getOrgIdOrgName();
        channelFeeEntityList.stream().forEach(v -> {
            String key = v.getChannelType()+"-"+v.getContractCode()+"-"+v.getOrgId()+"-"+DateUtil.format(v.getBusinessDate(),"yyyy-MM-dd");
            if (CostMainCategoryExpenseTypeEnum.SERVICE.getCode().equals(expenseType)
               && StringUtils.isEmpty(v.getChannelCode())) {
                throw new ServiceException("渠道编码不可以为空");
            }
            if (StringUtils.isEmpty(v.getChannelType())) {
                throw new ServiceException("渠道类型不可以为空");
            }
            if (StringUtils.isEmpty(v.getContractCode())) {
                throw new ServiceException("合同编号不可以为空");
            }
            if (StringUtils.isEmpty(v.getOrgId())) {
                throw new ServiceException("签约主体不可以为空");
            }
            if (!orgIdNameMap.containsKey(v.getOrgId())) {
                throw new ServiceException("签约主体不在系统中存在");
            }
            v.setOrgId(orgIdNameMap.get(v.getOrgId()));
            if (ObjectUtil.isNull(v.getBusinessDate())){
                throw new ServiceException("业务时间不可以为空");
            }
            if (!finalContractList.contains(v.getContractCode())) {
                throw new ServiceException("合同编码不在系统中存在");
            }
            if ((CostMainCategoryExpenseTypeEnum.GPS.getCode().equals(expenseType)
                    || CostMainCategoryExpenseTypeEnum.GRACELETE.getCode().equals(expenseType)
                    )
                    && finalFeeEntityMap.containsKey(key)) {
                throw new ServiceException(String.format("费用类型：%s,合同编码：%s,签约主体：%s,业务日期：%s 已经存在不可导入", v.getChannelType(),v.getContractCode(),v.getOrgId(), DateUtil.format(v.getBusinessDate(),"yyyy-MM-dd")));
            }
            //转换渠道编码
            //v.setChannelType(CostChannelTypeEnum.getCodeByDesc(v.getChannelType()));
        });
    }

    @Override
    public Boolean generateVoucher(List<Long> idList, String isSubmit) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("至少勾选一条数据生成凭证");
        }
        List<CostChannelFeeEntity> channelFeeEntityList = lambdaQuery().in(CostChannelFeeEntity::getId, idList).list();
        channelFeeEntityList.forEach(v -> {
            if (!ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("只有状态为已录入的才可以生成凭证");
            }
        });
        //删除凭证
        Map<String,List<CostChannelFeeEntity>> feeEntityMap = channelFeeEntityList.stream().collect(Collectors.groupingBy(CostChannelFeeEntity::getExpenseMainCategoryType));
        for(Map.Entry<String, List<CostChannelFeeEntity>> entry : feeEntityMap.entrySet()) {
            String batchType="";
            if (CostMainCategoryExpenseTypeEnum.SERVICE.getCode().equals(entry.getKey())) {
                batchType = BatchTypeEnum.QDF.getCode();
            } else if (CostMainCategoryExpenseTypeEnum.GPS.getCode().equals(entry.getKey())) {
                batchType = BatchTypeEnum.GPS.getCode();
            } else if (CostMainCategoryExpenseTypeEnum.GRACELETE.getCode().equals(entry.getKey())) {
                batchType = BatchTypeEnum.SHSBK.getCode();
            } else if (CostMainCategoryExpenseTypeEnum.COLLECT_FEE.getCode().equals(entry.getKey())) {
                batchType = BatchTypeEnum.SCF.getCode();
            }
            iVoucherService.deleteByBatchIdList(entry.getValue().stream().map(CostChannelFeeEntity::getId).collect(Collectors.toList()), batchType);
        }
        String expenseType = channelFeeEntityList.get(0).getExpenseMainCategoryType();
        List<Map<String,Object>> voucherMap = Lists.newArrayList();
        //获取税额
        List<String> contractCodeList = channelFeeEntityList.stream().map(CostChannelFeeEntity::getContractCode).collect(Collectors.toList());
        //获取起租不含税金额
        List<ContractEntity> contractEntityList = iContractService.lambdaQuery().in(ContractEntity::getContractCode,contractCodeList).list();
        Map<String,List<ContractEntity>> balanceMap = contractEntityList.stream().collect(Collectors.groupingBy(v->v.getContractCode()));
        //根据合同号获取费用单号和内容摘要
        Map<String, ClaimOrderSpecialVO> orderMap = getOrderMap(contractCodeList);
        channelFeeEntityList.forEach(v -> {
            BigDecimal settlementAmount = BigDecimal.ZERO;
            BigDecimal taxAmount = null == v.getTaxAmount() ? BigDecimal.ZERO : v.getTaxAmount();
            BigDecimal noTaxAmount = null == v.getNoTaxAmount() ? BigDecimal.ZERO : v.getNoTaxAmount();
            SceneEnum sceneEnum = null;
            BatchTypeEnum batchTypeEnum = null;
            if (CostMainCategoryExpenseTypeEnum.COLLECT_FEE.getCode().equals(v.getExpenseMainCategoryType())) {
                //只有收车费交易结构金额 减 支付不含税金额
                settlementAmount = v.getNoTaxTransactionAmount().subtract(v.getNoTaxAmount());
            } else if (balanceMap.containsKey(v.getContractCode())){
                    BigDecimal rentAmount = BigDecimal.ZERO;
                    ContractEntity contractEntity = balanceMap.get(v.getContractCode()).get(0);
                    if (CostChannelTypeEnum.SERVER_FEE.getCode().equals(v.getChannelType())) {
                        rentAmount = null == contractEntity.getPayableService() ? BigDecimal.ZERO : contractEntity.getPayableService();
                    } else if (CostChannelTypeEnum.EXTERNAL_CHANNEL_FEE.getCode().equals(v.getChannelType())) {
                        rentAmount = null == contractEntity.getPayableChannelExpense() ? BigDecimal.ZERO : contractEntity.getPayableChannelExpense();
                    } else if (CostChannelTypeEnum.HAITONG_CHANNEL_FEE.getCode().equals(v.getChannelType())) {
                        rentAmount = null == contractEntity.getPayableInnerExpense() ? BigDecimal.ZERO : contractEntity.getPayableInnerExpense();
                    } else if (CostMainCategoryExpenseTypeEnum.GPS.getCode().equals(v.getExpenseMainCategoryType())) {
                        rentAmount = null == contractEntity.getGpsUnitPrice() ? BigDecimal.ZERO : contractEntity.getGpsUnitPrice();
                    } else if (CostMainCategoryExpenseTypeEnum.GRACELETE.getCode().equals(v.getExpenseMainCategoryType())) {
                        rentAmount = null == contractEntity.getPayableDeviceAmount() ? BigDecimal.ZERO : contractEntity.getPayableDeviceAmount();
                    }
                    if (taxAmount.compareTo(BigDecimal.ZERO)==0){
                        settlementAmount = BigDecimal.ZERO.subtract(noTaxAmount);
                    }else {
                        settlementAmount = rentAmount.divide(taxAmount,2, RoundingMode.HALF_UP).subtract(noTaxAmount);
                    }
            }
            if (CostMainCategoryExpenseTypeEnum.SERVICE.getCode().equals(expenseType)) {
                sceneEnum = SceneEnum.QDF;
                batchTypeEnum = BatchTypeEnum.QDF;
            } else if (CostMainCategoryExpenseTypeEnum.GPS.getCode().equals(expenseType)) {
                sceneEnum = SceneEnum.GPS;
                batchTypeEnum = BatchTypeEnum.GPS;
            } else if (CostMainCategoryExpenseTypeEnum.GRACELETE.getCode().equals(expenseType)) {
                sceneEnum = SceneEnum.SHSBK;
                batchTypeEnum = BatchTypeEnum.SHSBK;
            } else if (CostMainCategoryExpenseTypeEnum.COLLECT_FEE.getCode().equals(expenseType)) {
                sceneEnum = SceneEnum.SCF;
                batchTypeEnum = BatchTypeEnum.SCF;
            }
            ClaimOrderVoucherVO claimOrderVoucherVO = new ClaimOrderVoucherVO();
            claimOrderVoucherVO.setSystemCode(SystemEnum.CWZT.getCode());
            claimOrderVoucherVO.setSystemName(SystemEnum.CWZT.getDesc());
            claimOrderVoucherVO.setBusinessCode(BusinessEnum.ZLYW.getCode());
            claimOrderVoucherVO.setBusinessName(BusinessEnum.ZLYW.getDesc());
            claimOrderVoucherVO.setOrderId(v.getId().toString());
            claimOrderVoucherVO.setSceneCode(sceneEnum.getCode());
            claimOrderVoucherVO.setSceneName(sceneEnum.getDesc());
            claimOrderVoucherVO.setBusinessDate(CommonDateUtils.parseLocalDateTimeToDate(v.getBusinessDate()));
            claimOrderVoucherVO.setFinanceDate(CommonDateUtils.parseDateToLocalDateTime(new Date()));
            claimOrderVoucherVO.setContractCode(v.getContractCode());
            claimOrderVoucherVO.setExpenseType(CostChannelTypeEnum.getDescByCode(v.getChannelType()));
            claimOrderVoucherVO.setBatchId(v.getId());
            claimOrderVoucherVO.setBatchType(batchTypeEnum.getCode());
            claimOrderVoucherVO.setNoTaxAmount(v.getNoTaxTransactionAmount());
            claimOrderVoucherVO.setTaxAmount(taxAmount);
            claimOrderVoucherVO.setSettlementAmount(settlementAmount);//起租不含税值（余额表中对应的余额/税额）-支付不含税值
            claimOrderVoucherVO.setTransactionStructureAdjustType(v.getStructureType());
            claimOrderVoucherVO.setDifferContractStatus("1".equals(v.getIsContractStatus()) ? "是" : "否");
            claimOrderVoucherVO.setOrgId(v.getOrgId());
            String orderNo = "";
            String contentAbstract = "";
            if (orderMap.containsKey(v.getContractCode())) {
                orderNo = orderMap.get(v.getContractCode()).getOrderNo();
                contentAbstract = orderMap.get(v.getContractCode()).getContentAbstract();
            }
            claimOrderVoucherVO.setOrderNo(orderNo);
            claimOrderVoucherVO.setContentAbstract(contentAbstract);
            claimOrderVoucherVO.setIsSubmit(isSubmit);
            Map<String, Object> dataMap = BeanUtil.beanToMap(claimOrderVoucherVO);
            voucherMap.add(dataMap);
        });
        List<VoucherInfoVO> voucherResultList = iRuleService.batchExecuteRule(voucherMap);
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
            this.lambdaUpdate().set(CostChannelFeeEntity::getFinancialDate,CommonDateUtils.parseDateToLocalDateTime(new Date())).set(CostChannelFeeEntity::getVoucherIds, voucherIds).eq(CostChannelFeeEntity::getId,Long.parseLong(infoVO.getOrderId())).update();
        }
        return Boolean.TRUE;
    }

    @Override
    public List<CostChannelFeeVO> listByCondition(CostChannelFeeQueryDTO queryDTO) {
        List<CostChannelFeeVO> channelFeeEntityList = costChannelFeeMapper.selectByCondition(queryDTO);
        return channelFeeEntityList;
    }

    @Override
    public Boolean deleteByIds(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请至少选择一条数据删除");
        }
        List<CostChannelFeeEntity> channelFeeEntityList = this.listByIds(idList);
        channelFeeEntityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("只有状态为已录入的才可以删除");
            }
        });
        //获取所有的凭证Id
        batchDeleteVoucher(idList);
        return this.removeBatchByIds(idList);
    }

    @Override
    public Boolean submit(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请至少选择一条数据提交");
        }
        List<ApproveDTO> approveDTOList = Lists.newArrayList();

        List<CostChannelFeeEntity> channelFeeEntityList = this.listByIds(idList);
        channelFeeEntityList.forEach(v -> {
            if (!ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("只有状态为已录入的才可以提交");
            }
            ApproveDTO approveDTO = new ApproveDTO();
            approveDTO.setDocumentId(v.getId());
            String doucumentType = "";
            if (CostMainCategoryExpenseTypeEnum.SERVICE.getCode().equals(v.getExpenseMainCategoryType())) {
                doucumentType = BatchTypeEnum.QDF.getCode();
            } else if (CostMainCategoryExpenseTypeEnum.GPS.getCode().equals(v.getExpenseMainCategoryType())) {
                doucumentType = BatchTypeEnum.GPS.getCode();
            } else if (CostMainCategoryExpenseTypeEnum.GRACELETE.getCode().equals(v.getExpenseMainCategoryType())) {
                doucumentType = BatchTypeEnum.SHSBK.getCode();
            } else if (CostMainCategoryExpenseTypeEnum.COLLECT_FEE.getCode().equals(v.getExpenseMainCategoryType())) {
                doucumentType = BatchTypeEnum.SCF.getCode();
            }
            approveDTO.setDocumentType(doucumentType);
            approveDTO.setUrl(approveUrl+v.getId());
            approveDTOList.add(approveDTO);
        });
        //发送审核
        Map<Long,Long> processInstantIdMap = iApproveService.submit(approveDTOList);
        channelFeeEntityList.forEach(v -> {
            if (null!=processInstantIdMap && processInstantIdMap.containsKey(v.getId())) {
                v.setProcessInstanceId(processInstantIdMap.get(v.getId()));
            }
        });
        this.updateBatchById(channelFeeEntityList);
        //提交生成凭证
        generateVoucher(idList,YesOrNoEnum.YES.getCode());
        return this.lambdaUpdate().set(CostChannelFeeEntity::getProcessStatus,ProcessStatusEnum.SUBMITTED.getCode()).in(CostChannelFeeEntity::getId,idList).update();
    }

    @Override
    public Boolean withdraw(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请至少选择一条数据撤回");
        }
        List<CostChannelFeeEntity> channelFeeEntityList = this.listByIds(idList);
        channelFeeEntityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.SUBMITTED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("只有状态为已提交的才可以撤回");
            }
            v.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
            v.setVoucherIds("");
        });
        iApproveService.withdraw(channelFeeEntityList.stream().map(CostChannelFeeEntity::getProcessInstanceId).collect(Collectors.toList()));
//        batchDeleteVoucher(idList);
        return this.updateBatchById(channelFeeEntityList);
    }

    public List<CostChannelFeeEntity> setChannelFeeEntity(List<CostChannelFeeEntity> channelFeeEntityList, String fileType){
        List<CostChannelFeeEntity> newChannelFeeEntityList = Lists.newArrayList();
        //查询所有的数据
        Map<String, CostChannelFeeEntity> feeEntityMap = Maps.newHashMap();
        List<CostChannelFeeEntity> feeEntityList = this.list();
        if (CollectionUtils.isNotEmpty(feeEntityList)) {
            feeEntityMap = feeEntityList.stream().collect(Collectors.toMap(v -> v.getChannelType()+"-"+v.getChannelCode()+"-"+v.getContractCode()+"-"+v.getOrgId()+"-"+DateUtil.format(v.getBusinessDate(),"yyyy-MM-dd"),v->v,(k1,k2)->k2));
        }
        Map<String, CostChannelFeeEntity> finalFeeEntityMap = feeEntityMap;
        channelFeeEntityList.forEach(v -> {
            String key = v.getChannelType()+"-"+v.getChannelCode()+"-"+v.getContractCode()+"-"+v.getOrgId()+"-"+DateUtil.format(v.getBusinessDate(),"yyyy-MM-dd");
            if (finalFeeEntityMap.containsKey(key)) {
                CostChannelFeeEntity oldFeeEntity = finalFeeEntityMap.get(key);
                if ("1".equals(fileType)) {
                    oldFeeEntity.setActualAmount(v.getActualAmount());
                    oldFeeEntity.setHostFactory(v.getHostFactory());
                    oldFeeEntity.setStructureType(v.getStructureType());
                    oldFeeEntity.setNoTaxTransactionAmount(v.getNoTaxTransactionAmount());
                } else if ("2".equals(fileType)) {
                    oldFeeEntity.setNoTaxAmount(v.getNoTaxAmount());
                    oldFeeEntity.setTaxAmount(v.getTaxAmount());
                }
                newChannelFeeEntityList.add(oldFeeEntity);
            } else {
                newChannelFeeEntityList.add(v);
            }
        });
        return newChannelFeeEntityList;
    }

    @Override
    public Boolean updateProcessStatus(CommonApproveDTO commonApproveDTO) {
        if (StringUtils.isEmpty(commonApproveDTO.getDocumentStatus())) {
            throw new ServiceException("成本状态不可以为空");
        }
        CostChannelFeeEntity costChannelFeeEntity = this.getById(commonApproveDTO.getDocumentId());
        if (null == costChannelFeeEntity) {
            throw new ServiceException("成本数据不存在");
        }
        String processStatus = costChannelFeeEntity.getProcessStatus();
        if (ProcessStatusEnum.REVIEWED.getCode().equals(commonApproveDTO.getDocumentStatus())) {
            processStatus = ProcessStatusEnum.REVIEWED.getCode();
            // TODO: 11/01/2024  待传送金蝶
        } else if (ProcessStatusEnum.REJECTED.getCode().equals(commonApproveDTO.getDocumentStatus())) {
            processStatus = ProcessStatusEnum.REJECTED.getCode();
        }
        costChannelFeeEntity.setProcessStatus(processStatus);
        return this.updateById(costChannelFeeEntity);
    }

    public void batchDeleteVoucher(List<Long> ids){
        //获取所有的凭证Id
        List<CostChannelFeeEntity> detailsEntityList = this.listByIds(ids);
        //逗号拆分
        List<Long> voucherIdList = Lists.newArrayList();
        detailsEntityList.stream().forEach(v -> {
            if (StringUtils.isNotEmpty(v.getVoucherIds())) {
                voucherIdList.addAll(Arrays.stream(v.getVoucherIds().split(",")).map(Long::parseLong).collect(Collectors.toList()));
            }
        });
        if (CollectionUtils.isNotEmpty(voucherIdList)) {
            iVoucherService.deleteByIdList(voucherIdList);
        }
    }

    public Map<String,ClaimOrderSpecialVO> getOrderMap(List<String> contractCodeList){
        Map<String,ClaimOrderSpecialVO> resultMap = new HashMap<>();
        if (CollectionUtils.isEmpty(contractCodeList)) {
            return resultMap;
        }
        List<ClaimOrderSpecialVO> specialVOList = iClaimOrderSpecialService.getOrderByContractCodeList(contractCodeList);
        if (CollectionUtils.isEmpty(specialVOList)) {
            return resultMap;
        }
        return specialVOList.stream().collect(Collectors.groupingBy(ClaimOrderSpecialVO::getContractNum,Collectors.collectingAndThen(Collectors.toList(),value->value.get(0))));
    }

    private Map<String,String> getOrgIdOrgName(){
        Map<String,String> orgNameAndIdMap = Maps.newHashMap();
        List<OrgCompanyVO> orgCompanyVOList =  iOrgCompanyService.selectByCondition(new OrgCompanyQueryDTO());
        if (CollectionUtils.isNotEmpty(orgCompanyVOList)) {
            orgNameAndIdMap = orgCompanyVOList.stream().collect(Collectors.toMap(OrgCompanyVO::getOrgName,OrgCompanyVO::getOrgId, (k1,k2)->k2));
        }
        return orgNameAndIdMap;
    }

}

