package com.utfinancing.financehub.etl.financial.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.utfinancing.financehub.admin.api.RemoteDictService;
import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.etl.constant.CommonConstant;
import com.utfinancing.financehub.etl.financial.entity.ContractBalanceEntity;
import com.utfinancing.financehub.etl.financial.entity.ContractEntity;
import com.utfinancing.financehub.etl.financial.model.dto.ContractQueryInfoDTO;
import com.utfinancing.financehub.etl.financial.model.dto.InvoiceClaimQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.InvoiceClaimDTO;
import com.utfinancing.financehub.etl.financial.model.vo.ContractInvoiceClaimVO;
import com.utfinancing.financehub.etl.financial.model.vo.InvoiceClaimExcelVO;
import com.utfinancing.financehub.etl.financial.model.vo.InvoiceClaimVO;
import com.utfinancing.financehub.etl.financial.entity.InvoiceClaimEntity;
import com.utfinancing.financehub.etl.financial.mapper.InvoiceClaimMapper;
import com.utfinancing.financehub.etl.financial.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.etl.invoicing.service.ITaxicOiInvoiceMiddleService;
import com.utfinancing.financehub.etl.invoicing.service.ITyOiInvoiceMiddleService;
import groovyjarjarpicocli.CommandLine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author : bruyang
 * @Date : Create in 2023-11-10
 * @Description :  InvoiceClaim服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class InvoiceClaimServiceImpl extends ServiceImpl<InvoiceClaimMapper, InvoiceClaimEntity> implements IInvoiceClaimService {

    private final InvoiceClaimMapper invoiceClaimMapper;
    private final IContractService iContractService;
    private final IOrgCompanyService iOrgCompanyService;
    private final IContractBalanceService iContractBalanceService;
    private final IClientService iClientService;
    private final RemoteDictService remoteDictService;

    @Override
    public Long saveInvoiceClaim(InvoiceClaimDTO dto) {
        InvoiceClaimEntity entity = BeanUtil.copyProperties(dto, InvoiceClaimEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateInvoiceClaim(Long id, InvoiceClaimDTO dto) {
        InvoiceClaimEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public InvoiceClaimDTO getInvoiceClaimDTOById(Long id) {
        InvoiceClaimEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, InvoiceClaimDTO.class);
    }

    @Override
    public IPage<InvoiceClaimVO> selectPage(InvoiceClaimQueryDTO queryDTO) {
        LambdaQueryWrapper<InvoiceClaimEntity> queryWrapper = getQueryWrapper(queryDTO);

        //这里注入查询条件
        IPage<InvoiceClaimEntity> entityIPage = invoiceClaimMapper.selectPage(new Page<InvoiceClaimEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        IPage<InvoiceClaimVO> invoiceClaimVOIPage = ListBeanUtil.copyPage(entityIPage, InvoiceClaimVO.class);
        invoiceClaimVOIPage.getRecords().forEach(v -> {
           //税率*100
           if (StringUtils.isNotEmpty(v.getTaxRate())) {
              v.setTaxRate((new BigDecimal(v.getTaxRate()).multiply(new BigDecimal(100)).setScale(0, RoundingMode.HALF_UP)).toString());
           }
           if (ObjectUtil.isNotEmpty(v.getVoucherId())) {
                v.setClaimStatus("1");
           } else {
                v.setClaimStatus("0");
           }
        });
        return invoiceClaimVOIPage;
    }

    @Override
    public List<InvoiceClaimVO> listByCondition(InvoiceClaimQueryDTO queryDTO) {
        LambdaQueryWrapper<InvoiceClaimEntity> queryWrapper = getQueryWrapper(queryDTO);
        List<InvoiceClaimEntity> claimEntityList = list(queryWrapper);
        List<InvoiceClaimVO> claimVOList = BeanUtil.copyToList(claimEntityList, InvoiceClaimVO.class);
        //获取业务来源字典
        R<List<SysDictData>> businessSoucerDict = remoteDictService.listDictData("invoice_business_source");
        if (null == businessSoucerDict) {
            throw new ServiceException("调用字典接口失败");
        }
        Map<String, String> businessSoucerMap = businessSoucerDict.getData().stream().collect(HashMap::new,(map,item)->map.put(item.getDictValue(),item.getDictLabel()),HashMap::putAll);
        claimVOList.forEach(v -> {
            //税率*100
            if (StringUtils.isNotEmpty(v.getTaxRate())) {
                v.setTaxRate((new BigDecimal(v.getTaxRate()).multiply(new BigDecimal(100)).setScale(0, RoundingMode.HALF_UP)).toString());
            }
            if (ObjectUtil.isNotEmpty(v.getVoucherId())) {
                v.setClaimStatus("1");
            } else {
                v.setClaimStatus("0");
            }
            if (businessSoucerMap.containsKey(v.getBusinessSource())) {
                v.setBusinessSource(businessSoucerMap.get(v.getBusinessSource()));
            }
        });
        return claimVOList;
    }

    @Override
    public IPage<ContractInvoiceClaimVO> selectContractInvoiceByPage(ContractQueryInfoDTO queryDTO) {
        InvoiceClaimQueryDTO params = new InvoiceClaimQueryDTO();
        ContractEntity contractEntity = iContractService.getById(queryDTO.getId());
        if (ObjectUtil.isNull(contractEntity)) {
            throw new ServiceException("合同信息不存在");
        }
        params.setContractCode(contractEntity.getContractCode());
        params.setOrgId(contractEntity.getOrgId());
        Page page = new Page(queryDTO.getPageNum(), queryDTO.getPageSize());
        if (StringUtils.isEmpty(params.getContractCode())) {
            throw new ServiceException("合同编码不能为空");
        }
        IPage<ContractInvoiceClaimVO> resultPage = invoiceClaimMapper.selectContractInvoiceClaimByPage(page, params);
        List<ContractInvoiceClaimVO> invoiceClaimVOList = resultPage.getRecords();
        setInvoiceClaimData(invoiceClaimVOList);
        return resultPage;
    }

    /**
     * 合同查询开票认领List
     * @param queryDTO
     * @return
     */
    @Override
    public List<ContractInvoiceClaimVO> selectContractInvoiceList(ContractQueryInfoDTO queryDTO) {
        InvoiceClaimQueryDTO params = new InvoiceClaimQueryDTO();
        if(ObjectUtil.isNotEmpty(queryDTO.getId())){
            ContractEntity contractEntity = iContractService.getById(queryDTO.getId());
            if (ObjectUtil.isNull(contractEntity)) {
                throw new ServiceException("合同信息不存在");
            }
            params.setContractCode(contractEntity.getContractCode());
            params.setOrgId(contractEntity.getOrgId());
            if (StringUtils.isEmpty(params.getContractCode())) {
                throw new ServiceException("合同编码不能为空");
            }
        }


        List<ContractInvoiceClaimVO> invoiceClaimVOList = invoiceClaimMapper.selectContractInvoiceClaimByPage(params);
        setInvoiceClaimData(invoiceClaimVOList);
        return invoiceClaimVOList;
    }

    @Override
    public List<ContractInvoiceClaimVO> selectContractInvoiceClaimByCondition(ContractQueryInfoDTO queryDTO) {
        InvoiceClaimQueryDTO params = new InvoiceClaimQueryDTO();
        ContractEntity contractEntity = iContractService.getById(queryDTO.getId());
        if (ObjectUtil.isNull(contractEntity)) {
            throw new ServiceException("合同信息不存在");
        }
        params.setContractCode(contractEntity.getContractCode());
        params.setOrgId(contractEntity.getOrgId());
        if (StringUtils.isEmpty(params.getContractCode())) {
            throw new ServiceException("合同编码不能为空");
        }
        List<ContractInvoiceClaimVO> invoiceClaimVOList =  invoiceClaimMapper.selectContractInvoiceClaimByCondition(params);
        setInvoiceClaimData(invoiceClaimVOList);
        return invoiceClaimVOList;
    }

    public void setInvoiceClaimData(List<ContractInvoiceClaimVO> invoiceClaimVOList) {
        List<String> contractList = Lists.newArrayList();
        Map<String,List<ContractBalanceEntity>> contractEntityMap = Maps.newHashMap();
        invoiceClaimVOList.stream().forEach(v -> {
            //系统来源为CYCXT/SYCXT，开票项目为咨询服务费的取主合同有值对应的签约主体
            if ((CommonConstant.CYCXT.equals(v.getSystemCode())
                    || CommonConstant.SYCXT.equals(v.getSystemCode())) && ("咨询服务费".equals(v.getProductName()) || "手续费".equals(v.getProductName()))) {
                contractList.add(v.getContractCode());
            }
            //租赁类型是回租，应开票金额取应收利息，其他的取应收租金
            if ("回租".equals(v.getLeaseType())) {
                v.setBackTaxAmount(v.getInterestAmount());
            } else {
                v.setBackTaxAmount(v.getRentReceivableAmount());
            }
            //获取开票税率 除100
            //税率*100
            if (StringUtils.isNotEmpty(v.getTaxRate())) {
                v.setTaxRate((new BigDecimal(v.getTaxRate()).multiply(new BigDecimal(100)).setScale(0, RoundingMode.HALF_UP)).toString());
            }
            // backTaxValue应开票税额=应开票金额/(1+税率)*税率
            BigDecimal backTaxValue = null == v.getBackTaxAmount() ? BigDecimal.ZERO : v.getBackTaxAmount();
            //开票税率
            BigDecimal backTaxRate = null == v.getBackTaxRate() ? BigDecimal.ZERO : v.getBackTaxRate().divide(new BigDecimal("100").setScale(2, RoundingMode.HALF_UP));
            if (BigDecimal.ZERO.compareTo(backTaxRate)!=0) {
                BigDecimal backTaxValueBigdecimal = backTaxValue.divide((new BigDecimal("1").add(backTaxRate)).multiply(backTaxRate));
                v.setBackTaxValue(backTaxValueBigdecimal.setScale(2, RoundingMode.HALF_UP));
            }
            v.setPrincipalAmount(null==v.getPrincipalAmount()?BigDecimal.ZERO:v.getPrincipalAmount());
            v.setTaxAmount(null==v.getTaxAmount()?BigDecimal.ZERO:v.getTaxAmount());
            v.setBackTaxRate(null==v.getBackTaxRate()?BigDecimal.ZERO:v.getBackTaxRate());
            v.setTaxRate(null==v.getTaxRate()?"0":v.getTaxRate());
            v.setBackTaxAmount(null==v.getBackTaxAmount()?BigDecimal.ZERO:v.getBackTaxAmount());
        });
        //获取所有的客户列表
        Map<String,String> clientMap = Maps.newHashMap();
        Map<String,String> orgIdMap = iOrgCompanyService.list().stream().collect(HashMap::new,(h, o)->h.put(o.getOrgId(),o.getOrgName()),HashMap::putAll);

        if (CollectionUtils.isNotEmpty(contractList)) {
            //获取所有的客户列表
            clientMap = iClientService.list().stream().collect(HashMap::new,(h, o)->h.put(o.getClientCode(),o.getClientName()),HashMap::putAll);
            contractEntityMap = iContractBalanceService.lambdaQuery().in(ContractBalanceEntity::getContractCode,contractList).list().stream().collect(Collectors.groupingBy(ContractBalanceEntity::getContractCode));
        }
        Map<String, List<ContractBalanceEntity>> finalContractEntityMap = contractEntityMap;
        //手续费的应开票主体和客户需要：查eg_contract_balance该合同的receivable_commission_amount不为0的最初发生额对应的客户
        //咨询服务费的应开票主体和客户需要：查eg_contract_balance该合同的receivable_service_amount不为0的最初发生额对应的客户
        Map<String, String> finalOrgIdMap = orgIdMap;
        Map<String, String> finalClientMap = clientMap;
        invoiceClaimVOList.stream().forEach(v -> {
            if (StringUtils.isNotEmpty(v.getOrgId())) {
                v.setOrgIdName(finalOrgIdMap.get(v.getOrgId()));
            }
            if (finalContractEntityMap.containsKey(v.getContractCode())) {
                List<ContractBalanceEntity> contractEntityMList = Lists.newArrayList();
                if ("手续费".equals(v.getProductName())) {
                    contractEntityMList = finalContractEntityMap.get(v.getContractCode()).stream().filter(s -> BigDecimal.ZERO.compareTo(s.getReceivableCommissionAmount())!=0).collect(Collectors.toList());
                } else if ("咨询服务费".equals(v.getProductName())) {
                    contractEntityMList = finalContractEntityMap.get(v.getContractCode()).stream().filter(s -> BigDecimal.ZERO.compareTo(s.getReceivableServiceAmount())!=0).collect(Collectors.toList());
                }
                if (CollectionUtils.isNotEmpty(contractEntityMList)) {
                    //按照时间排序获取创建时间最小的那条数据对应的签约主体和客户名称
                    ContractBalanceEntity balanceEntity = contractEntityMList.stream().sorted(Comparator.comparing(ContractBalanceEntity::getCreateTime)).collect(Collectors.toList()).get(0);
                    v.setOrgId(balanceEntity.getOrgId());
                    v.setOrgIdName(finalOrgIdMap.get(balanceEntity.getOrgId()));
                    v.setClientName(finalClientMap.get(balanceEntity.getClientCode()));
                }
            }
            //异常类型1.开票主体、开票金额、开票税率、开票对象不一致 2.累计已开金额大于已收款金额
            String exceptionType = "";
//            log.info("开票数据:{}",v);
            if (StringUtils.isEmpty(v.getOrgId())||!v.getOrgId().equals(v.getSellerTaxCode())
                    || v.getPrincipalAmount().compareTo(v.getTaxAmount())!=0
                    || v.getBackTaxRate().compareTo(new BigDecimal(v.getTaxRate()))!=0
                    || !v.getClientName().equals(v.getPayer())) {
                exceptionType = CommonConstant.INVOICE_ERROR_COMMENT;
            }
            if (v.getBackTaxAmount().compareTo(v.getTaxAmount())!=0) {
                exceptionType = exceptionType + CommonConstant.INVOICE_AMOUNT_ERROR_COMMENT;
            }
            v.setExceptionType(exceptionType);
        });
    }

    public LambdaQueryWrapper<InvoiceClaimEntity> getQueryWrapper(InvoiceClaimQueryDTO queryDTO){
        LambdaQueryWrapper<InvoiceClaimEntity> queryWrapper = Wrappers.<InvoiceClaimEntity>lambdaQuery();
        queryWrapper.eq(InvoiceClaimEntity::getIsAutoGeneration,"0");
        queryWrapper.eq(InvoiceClaimEntity::getDelFlag, "0");
        if (StringUtils.isNotEmpty(queryDTO.getBusinessSource())) {
            queryWrapper.eq(InvoiceClaimEntity::getBusinessSource,queryDTO.getBusinessSource());
        }
        if (StringUtils.isNotEmpty(queryDTO.getSellerName())) {
            queryWrapper.like(InvoiceClaimEntity::getSellerName,queryDTO.getSellerName());
        }
        if (StringUtils.isNotEmpty(queryDTO.getPurchaserName())) {
            queryWrapper.like(InvoiceClaimEntity::getPurchaserName,queryDTO.getPurchaserName());
        }
        if (StringUtils.isNotEmpty(queryDTO.getClaimStatus())) {
            if ("0".equals(queryDTO.getClaimStatus())) {
                queryWrapper.isNull(InvoiceClaimEntity::getVoucherId);
            } else {
                queryWrapper.isNotNull(InvoiceClaimEntity::getVoucherId);
            }
        }
        if (StringUtils.isNotEmpty(queryDTO.getDocumentNum())) {
            queryWrapper.like(InvoiceClaimEntity::getDocumentNum, queryDTO.getDocumentNum());
        }
        if (StringUtils.isNotEmpty(queryDTO.getEnterpriseNum())) {
            queryWrapper.like(InvoiceClaimEntity::getEnterpriseNum, queryDTO.getEnterpriseNum());
        }
        if (StringUtils.isNotEmpty(queryDTO.getSellerTaxCode())) {
            queryWrapper.like(InvoiceClaimEntity::getSellerTaxCode, queryDTO.getSellerTaxCode());
        }
        if (StringUtils.isNotEmpty(queryDTO.getInvoiceType())) {
            queryWrapper.eq(InvoiceClaimEntity::getInvoiceType, queryDTO.getInvoiceType());
        }
        if (StringUtils.isNotEmpty(queryDTO.getContractStatus())) {
            queryWrapper.eq(InvoiceClaimEntity::getContractStatus, queryDTO.getContractStatus());
        }
        if (StringUtils.isNotEmpty(queryDTO.getTaxRate())) {
            queryWrapper.apply("cast(coalesce(tax_rate,'0') as decimal) * 100 = {0}", queryDTO.getTaxRate());
        }
        if (ObjectUtil.isNotEmpty(queryDTO.getStartDocumentDate())) {
            queryWrapper.apply("to_char(document_date,'YYYY-MM-DD')>={0}",DateUtil.format(queryDTO.getStartDocumentDate(),"yyyy-MM-dd"));
        }
        if (ObjectUtil.isNotEmpty(queryDTO.getEndDocumentDate())) {
            queryWrapper.apply("to_char(document_date,'YYYY-MM-DD')<={0}",DateUtil.format(queryDTO.getEndDocumentDate(),"yyyy-MM-dd"));
        }
        if (StringUtils.isNotEmpty(queryDTO.getProcessStatus())) {
            queryWrapper.eq(InvoiceClaimEntity::getProcessStatus,queryDTO.getProcessStatus());
        }
        //过滤发票类型数据
        queryWrapper.notIn(InvoiceClaimEntity::getInvoiceType,Lists.newArrayList("222","收据"));
        return queryWrapper;
    }


}

