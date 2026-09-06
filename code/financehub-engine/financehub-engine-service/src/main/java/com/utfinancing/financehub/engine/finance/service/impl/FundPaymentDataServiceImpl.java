package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.FundEbankTransactionDataVO;
import com.utfinancing.financehub.engine.finance.model.vo.FundPaymentDataVO;
import com.utfinancing.financehub.engine.finance.mapper.FundPaymentDataMapper;
import com.utfinancing.financehub.engine.finance.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import com.utfinancing.financehub.engine.utils.CommonDateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-17
 * @Description :  FundPaymentData服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class FundPaymentDataServiceImpl extends ServiceImpl<FundPaymentDataMapper, FundPaymentDataEntity> implements IFundPaymentDataService {

    private final FundPaymentDataMapper fundPaymentDataMapper;

    private final IRuleService iRuleService;

    @Override
    public Long saveFundPaymentData(FundPaymentDataDTO dto) {
        FundPaymentDataEntity entity = BeanUtil.copyProperties(dto, FundPaymentDataEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public FundPaymentDataEntity saveRawData(JSONObject jsonData) {
        FundPaymentDataEntity entity = BeanUtil.copyProperties(jsonData, FundPaymentDataEntity.class);
        // 转换
        if (StringUtils.isNotBlank(entity.getCurrencyType())) {
            FundCurrencyTypeEnum enumByType = FundCurrencyTypeEnum.getEnumByType(entity.getCurrencyType());
            if (null != enumByType) {
                entity.setCurrencyType(enumByType.getCode());
            }
        }
        this.save(entity);
        //this.paymentGenerateVoucher(Lists.newArrayList(entity));
        // 关联网银收款信息入库到未确认收款汇总表
        //fundBusinessSystemEbankMappingService.selectNonConfirmCollectionFromFundSystem();
        return entity;
    }

    @Override
    public Long updateFundPaymentData(Long id, FundPaymentDataDTO dto) {
        FundPaymentDataEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public FundPaymentDataDTO getFundPaymentDataDTOById(Long id) {
        FundPaymentDataEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, FundPaymentDataDTO.class);
    }

    @Override
    public IPage<FundPaymentDataVO> selectPage(FundPaymentDataQueryDTO queryDTO) {
        LambdaQueryWrapper<FundPaymentDataEntity> queryWrapper = Wrappers.<FundPaymentDataEntity>lambdaQuery();
        //这里注入查询条件
        IPage<FundPaymentDataEntity> entityIPage = fundPaymentDataMapper.selectPage(new Page<FundPaymentDataEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, FundPaymentDataVO.class);
    }

    @Override
    public void paymentGenerateVoucher(List<FundPaymentDataEntity> fundPaymentDataEntityList) {
        fundPaymentDataEntityList.stream().forEach(v -> {
            try {
                FundSystemVoucherDTO fundSystemVoucherDTO = BeanUtil.copyProperties(v, FundSystemVoucherDTO.class);
                fundSystemVoucherDTO.setBusinessDate(StringUtils.isEmpty(v.getBusinessDate()) ? new Date() : CommonDateUtils.parseDateStringToDate(v.getBusinessDate()));
                fundSystemVoucherDTO.setOperationDate(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                fundSystemVoucherDTO.setSceneCode(SceneEnum.WYLSFK.getCode());
                fundSystemVoucherDTO.setSceneName(SceneEnum.WYLSFK.getDesc());
                fundSystemVoucherDTO.setSystemCode(SystemEnum.ZJXT.getCode());
                fundSystemVoucherDTO.setSystemName(SystemEnum.ZJXT.getDesc());
                fundSystemVoucherDTO.setBusinessCode("ZLYW");
                fundSystemVoucherDTO.setBusinessName("资金业务");
                fundSystemVoucherDTO.setTransactionType("payment");
                fundSystemVoucherDTO.setOrderId(v.getOrderId());
                fundSystemVoucherDTO.setEbankNumber(v.getEbankNumber());
                fundSystemVoucherDTO.setCollectionAccountsBank(v.getCollectionAccountsBank());
                fundSystemVoucherDTO.setCollectionAccountsBankNo(v.getCollectionAccountsBank());
                fundSystemVoucherDTO.setClientAccountsBank(v.getCollectionAccountsBank());
                fundSystemVoucherDTO.setClientAccountsBankNo(v.getCollectionAccountsBank());
                fundSystemVoucherDTO.setPaymentAccountsBankNo(v.getPaymentBankNo());
                fundSystemVoucherDTO.setBatchId(v.getId());
                fundSystemVoucherDTO.setBatchType(BatchTypeEnum.WYLSFK.getCode());
                fundSystemVoucherDTO.setInterfaceCreateTime(v.getCreateTime());
                fundSystemVoucherDTO.setActualClientName(v.getActualClientName());
                fundSystemVoucherDTO.setActualClientCode(v.getActualClientCode());
                Map<String, Object> dataMap = BeanUtil.beanToMap(fundSystemVoucherDTO);
                dataMap.put("interfaceId", v.getId());
                log.info("生成凭证参数：{}", JSON.toJSONString(dataMap));

                long startTime = System.currentTimeMillis();
                log.info("资金系统付款生成凭证-会计引擎调用Start：" + startTime);
                iRuleService.executeRule(dataMap);
                log.info("资金系统付款生成凭证-会计引擎调用End：" + (System.currentTimeMillis() - startTime));
                v.setIsGenerateVoucher("1");
            } catch (Exception e) {
                v.setIsGenerateVoucher("2");
                log.error("付款生成凭证id:{}",v.getId(),e);
            }
            long startTime = System.currentTimeMillis();
            log.info("资金系统付款生成凭证-状态更新Start：" + startTime);
            this.updateById(v);
            log.info("资金系统付款生成凭证-状态更新End：" + (System.currentTimeMillis() - startTime));
        });
    }

    @Override
    public Boolean payMentGenerateVoucher(FundPaymentDataQueryDTO queryDTO) {
        long startTime = System.currentTimeMillis();
        log.info("资金系统付款生成凭证-FundPaymentDataEntity数据查询Start：" + startTime);
        QueryWrapper<FundPaymentDataEntity> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotNull(queryDTO.getId())) {
            queryWrapper.lambda().eq(FundPaymentDataEntity::getId,queryDTO.getId());
        }
        if (StringUtils.isNotEmpty(queryDTO.getBusinessDate())) {
            queryWrapper.apply("to_char(create_time,'YYYY-MM-DD')={0}",queryDTO.getBusinessDate());
        }
        queryWrapper.lambda().isNotNull(FundPaymentDataEntity::getCollectionAccountsBank);
        queryWrapper.lambda().eq(FundPaymentDataEntity::getIsGenerateVoucher,"0");
        List<FundPaymentDataEntity> dataEntityList = this.list(queryWrapper);
        log.info("资金系统付款生成凭证-FundPaymentDataEntity数据查询End：" + (System.currentTimeMillis() - startTime));
        paymentGenerateVoucher(dataEntityList);
        return Boolean.TRUE;
    }

    @Override
    public Boolean testGenerateVoucher2ByDay(FundPaymentDataQueryDTO queryDTO) {
        Date startDate = queryDTO.getStartDocumentDate();
        Date endDate = queryDTO.getEndDocumentDate();
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        long diffInMillies = Math.abs(endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis());
        int daysBetween = (int)(diffInMillies / (24 * 60 * 60 * 1000));
        log.info("相差天数：{}",daysBetween);
        while (startCalendar.getTime().compareTo(endCalendar.getTime())<0){
            startCalendar.add(Calendar.DAY_OF_MONTH, 1);
            String date = DateUtil.format(startCalendar.getTime(),"yyyy-MM-dd");
            queryDTO.setBusinessDate(date);
            log.info("同步付款金额数据开始时间>>>>>>：{}",queryDTO.getBusinessDate());
            payMentGenerateVoucher(queryDTO);
            log.info("同步付款金额数据结束时间>>>>>>：{}",queryDTO.getBusinessDate());
        }
        log.info("同步收付款金额数据开始时间>>>>>>>>：{}，结束时间>>>>>>：{} 数据同步完成",startDate,endDate);
        return Boolean.TRUE;
    }

}

