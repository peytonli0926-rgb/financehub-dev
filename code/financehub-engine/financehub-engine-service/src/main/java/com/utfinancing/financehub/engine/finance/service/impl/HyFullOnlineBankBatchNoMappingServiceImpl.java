package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.model.dto.HyFullOnlineBankBatchNoMappingQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.HyFullOnlineBankBatchNoMappingDTO;
import com.utfinancing.financehub.engine.finance.model.vo.HyFullOnlineBankBatchNoMappingVO;
import com.utfinancing.financehub.engine.finance.entity.HyFullOnlineBankBatchNoMappingEntity;
import com.utfinancing.financehub.engine.finance.mapper.HyFullOnlineBankBatchNoMappingMapper;
import com.utfinancing.financehub.engine.finance.service.IHyFullOnlineBankBatchNoMappingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author : robjiang
 * @Date : Create in 2024-03-21
 * @Description :  HyFullOnlineBankBatchNoMapping服务实现类
 * @Modified :
 */
@Service
@Transactional
@Slf4j
public class HyFullOnlineBankBatchNoMappingServiceImpl extends ServiceImpl<HyFullOnlineBankBatchNoMappingMapper,
        HyFullOnlineBankBatchNoMappingEntity> implements IHyFullOnlineBankBatchNoMappingService {

    @Resource
    private HyFullOnlineBankBatchNoMappingMapper hyFullOnlineBankBatchNoMappingMapper;

    @Override
    public Long saveHyFullOnlineBankBatchNoMapping(HyFullOnlineBankBatchNoMappingDTO dto) {
        HyFullOnlineBankBatchNoMappingEntity entity = BeanUtil.copyProperties(dto, HyFullOnlineBankBatchNoMappingEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateHyFullOnlineBankBatchNoMapping(Long id, HyFullOnlineBankBatchNoMappingDTO dto) {
        HyFullOnlineBankBatchNoMappingEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public HyFullOnlineBankBatchNoMappingDTO getHyFullOnlineBankBatchNoMappingDTOById(Long id) {
        HyFullOnlineBankBatchNoMappingEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, HyFullOnlineBankBatchNoMappingDTO.class);
    }

    @Override
    public IPage<HyFullOnlineBankBatchNoMappingVO> selectPage(HyFullOnlineBankBatchNoMappingQueryDTO queryDTO) {
        LambdaQueryWrapper<HyFullOnlineBankBatchNoMappingEntity> queryWrapper = Wrappers.<HyFullOnlineBankBatchNoMappingEntity>lambdaQuery();
        //这里注入查询条件
        IPage<HyFullOnlineBankBatchNoMappingEntity> entityIPage = hyFullOnlineBankBatchNoMappingMapper.selectPage(new Page<HyFullOnlineBankBatchNoMappingEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, HyFullOnlineBankBatchNoMappingVO.class);
    }

    /**
     * 接收业务系统推送的网银编号和批扣号的映射关系数据
     */
    public void receiveBusinessDataFromMQ(String messageId, JSONObject jsonObject) {
        HyFullOnlineBankBatchNoMappingEntity entity = new HyFullOnlineBankBatchNoMappingEntity();
        entity.setId(IdWorker.getId());
        String deductBatchNo = jsonObject.getString("VC_WANGYBH");
        if (StringUtils.isNotEmpty(deductBatchNo)) {
            entity.setDeductBatchNo(deductBatchNo);
        }

        String deductCategory = jsonObject.getString("VC_ZHONGL");
        if (StringUtils.isNotEmpty(deductCategory)) {
            entity.setDeductCategory(deductCategory);
        }

        String businessLine = jsonObject.getString("VC_ZIXTZL");
        if (StringUtils.isNotEmpty(businessLine)) {
            entity.setBusinessLine(businessLine);
        }
        String bankReceiptNo = jsonObject.getString("VC_YINHHDH");
        if (StringUtils.isNotEmpty(bankReceiptNo)) {
            entity.setBankReceiptNo(bankReceiptNo);
        }
        String collectAmountTime = jsonObject.getString("DT_DAODSJ");
        if (collectAmountTime != null) {
            entity.setCollectAmountTime(LocalDateTime.parse(collectAmountTime));
        }
        String collectAmount = jsonObject.getString("DEC_DAOZJE");
        if (StringUtils.isNotEmpty(collectAmount)) {
            entity.setCollectAmount(new BigDecimal(collectAmount));
        } else {
            entity.setCollectAmount(new BigDecimal(0));
        }
        String collectBank = jsonObject.getString("VC_DAOZYH");
        if (StringUtils.isNotEmpty(collectBank)) {
            entity.setCollectBank(collectBank);
        }
        String collectBankAccount = jsonObject.getString("VC_DAOZYHZH");
        if (StringUtils.isNotEmpty(collectBankAccount)) {
            entity.setCollectBankAccount(collectBankAccount);
        }
        String comments = jsonObject.getString("VC_ZHAIY");
        if (StringUtils.isNotEmpty(comments)) {
            entity.setComments(comments);
        }
        String payBankAccountName = jsonObject.getString("VC_FUKZH");
        if (StringUtils.isNotEmpty(payBankAccountName)) {
            entity.setPayBankAccountName(payBankAccountName);
        }
        String payBank = jsonObject.getString("VC_FUKYH");
        if (StringUtils.isNotEmpty(payBank)) {
            entity.setPayBank(payBank);
        }
        String payBankAccount = jsonObject.getString("VC_FUKYHZH");
        if (StringUtils.isNotEmpty(payBankAccount)) {
            entity.setPayBankAccount(payBankAccount);
        }
        String canChargeOffAmount = jsonObject.getString("DEC_KEHXJE");
        if (StringUtils.isNotEmpty(canChargeOffAmount)) {
            entity.setCanChargeOffAmount(new BigDecimal(canChargeOffAmount));
        } else {
            entity.setCanChargeOffAmount(new BigDecimal(0));
        }
        String alreadyChargeOffAmount = jsonObject.getString("VC_YINHKM");
        if (StringUtils.isNotEmpty(alreadyChargeOffAmount)) {
            entity.setAlreadyChargeOffAmount(new BigDecimal(alreadyChargeOffAmount));
        } else {
            entity.setAlreadyChargeOffAmount(new BigDecimal(0));
        }
        String createBy = jsonObject.getString("VC_CHUANGJR");
        if (StringUtils.isNotEmpty(createBy)) {
            entity.setCreateBy(createBy);
        }
//        String createTime = jsonObject.getString("DT_CHUANGJSJ");
//        if (StringUtils.isNotEmpty(createTime)) {
//
//        }
        entity.setCreateTime(LocalDateTime.now());
        String updateBy = jsonObject.getString("VC_XIUGR");
        if (StringUtils.isNotEmpty(updateBy)) {
            entity.setUpdateBy(updateBy);
        }
//        String updateTime = jsonObject.getString("DT_XIUGSJ");
//        if (StringUtils.isNotEmpty(updateTime)) {
//            entity.setUpdateTime(LocalDateTime.parse(updateTime));
//        }
        entity.setUpdateTime(LocalDateTime.now());
        String decDongjje = jsonObject.getString("DEC_DONGJJE");
        if (StringUtils.isNotEmpty(decDongjje)) {
            entity.setDecDongjje(new BigDecimal(decDongjje));
        } else {
            entity.setDecDongjje(new BigDecimal(0));
        }
        String refundAmount = jsonObject.getString("DEC_TUIKJE");
        if (StringUtils.isNotEmpty(refundAmount)) {
            entity.setRefundAmount(new BigDecimal(refundAmount));
        } else {
            entity.setRefundAmount(new BigDecimal(0));
        }
        String collectAmountAccountName = jsonObject.getString("VC_DAOZZH");
        if (StringUtils.isNotEmpty(collectAmountAccountName)) {
            entity.setCollectAmountAccountName(collectAmountAccountName);
        }
        String refundFlag = jsonObject.getString("VC_TUIKBZ");
        if (StringUtils.isNotEmpty(refundFlag)) {
            entity.setRefundFlag(refundFlag);
        }
        String onlineBankBelong = jsonObject.getString("VC_WANGYGS");
        if (StringUtils.isNotEmpty(onlineBankBelong)) {
            entity.setOnlineBankBelong(onlineBankBelong);
        }
        String onlineBankNo = jsonObject.getString("VC_PINGZZY");
        if (StringUtils.isNotEmpty(onlineBankNo)) {
            entity.setOnlineBankNo(onlineBankNo);
        }
        this.save(entity);
    }

    /**
     * 根据条件查询业务网银编号与批扣号得映射关系
     */
    public List<HyFullOnlineBankBatchNoMappingEntity> selectMappingDataByCon(HyFullOnlineBankBatchNoMappingEntity params) {
        LambdaQueryWrapper<HyFullOnlineBankBatchNoMappingEntity> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(params.getOnlineBankNo())) {
            List<String> onlineBankNoList = Arrays.asList(params.getOnlineBankNo().split(","));
            wrapper.in(HyFullOnlineBankBatchNoMappingEntity::getOnlineBankNo, onlineBankNoList);
        }
        if (StringUtils.isNotEmpty(params.getDeductBatchNo())) {
            List<String> deductBatchNoList = Arrays.asList(params.getDeductBatchNo().split(","));
            wrapper.in(HyFullOnlineBankBatchNoMappingEntity::getDeductBatchNo, deductBatchNoList);
        }
        wrapper.eq(HyFullOnlineBankBatchNoMappingEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        return hyFullOnlineBankBatchNoMappingMapper.selectList(wrapper);
    }

    /**
     * 根据网银编号查询批扣流水号
     */
    public List<HyFullOnlineBankBatchNoMappingEntity> selectBusinessOnlineBankNoBatchNoMapping(String onlineBankNo) {
        HyFullOnlineBankBatchNoMappingEntity params = new HyFullOnlineBankBatchNoMappingEntity();
        params.setOnlineBankNo(onlineBankNo);
        return  this.selectMappingDataByCon(params);
    }
}

