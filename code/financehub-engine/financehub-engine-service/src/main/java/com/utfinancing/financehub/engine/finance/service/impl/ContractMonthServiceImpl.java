package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.utfinancing.financehub.engine.enums.ContractStatusTransferEnum;
import com.utfinancing.financehub.engine.enums.SystemEnum;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.entity.CloseAccountEntity;
import com.utfinancing.financehub.engine.finance.entity.ContractEntity;
import com.utfinancing.financehub.engine.finance.entity.ContractMonthEntity;
import com.utfinancing.financehub.engine.finance.mapper.ContractMonthMapper;
import com.utfinancing.financehub.engine.finance.model.dto.ContractDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ContractMonthDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ContractMonthQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ContractMonthVO;
import com.utfinancing.financehub.engine.finance.service.ICloseAccountService;
import com.utfinancing.financehub.engine.finance.service.IContractMonthService;
import com.utfinancing.financehub.engine.verification.service.IVerificationService;
import com.utfinancing.financehub.etl.api.KingdeeDataSyncFacade;
import com.utfinancing.financehub.etl.api.TyptBusinessFacade;
import com.utfinancing.financehub.etl.model.dto.OrgPeriodDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
/**
 * @Author : robjiang
 * @Date : Create in 2025-04-21
 * @Description :  ContractMonth服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class ContractMonthServiceImpl extends ServiceImpl<ContractMonthMapper, ContractMonthEntity> implements IContractMonthService {

    private final ContractMonthMapper contractMonthMapper;

    private final KingdeeDataSyncFacade kingdeeDataSyncFacade;

    private final ICloseAccountService closeAccountService;

    private final TyptBusinessFacade typtBusinessFacade;

    @Lazy
    @Resource
    private IVerificationService iVerificationService;
    @Override
    public Long saveContractMonth(ContractMonthDTO dto) {
        ContractMonthEntity entity = BeanUtil.copyProperties(dto, ContractMonthEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateContractMonth(Long id, ContractMonthDTO dto) {
        ContractMonthEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public ContractMonthDTO getContractMonthDTOById(Long id) {
        ContractMonthEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, ContractMonthDTO.class);
    }

    @Override
    public IPage<ContractMonthVO> selectPage(ContractMonthQueryDTO queryDTO) {
        LambdaQueryWrapper<ContractMonthEntity> queryWrapper = Wrappers.<ContractMonthEntity>lambdaQuery();
        //这里注入查询条件
        IPage<ContractMonthEntity> entityIPage = contractMonthMapper.selectPage(new Page<ContractMonthEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, ContractMonthVO.class);
    }


    @Override
    public ContractMonthDTO getContractDTOByCode(String contractCode, String orgId) {
        QueryWrapper<ContractMonthEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ContractMonthEntity::getContractCode, contractCode);
        if (StringUtils.isNotEmpty(orgId)) {
            queryWrapper.lambda().eq(ContractMonthEntity::getOrgId, orgId);
        }
        queryWrapper.lambda().orderByDesc(ContractMonthEntity::getId);
        List<ContractMonthEntity> entityList = this.list(queryWrapper);
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        ContractMonthDTO contractDTO = BeanUtil.copyProperties(entityList.get(0), ContractMonthDTO.class);
        contractDTO.setContractStatus(ContractStatusTransferEnum.transferContractStatus(contractDTO.getContractStatus()));
        return contractDTO;
    }

    @Override
    public ContractMonthDTO getTranStatusDTOByCode(String contractCode, String orgId) {
        QueryWrapper<ContractMonthEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ContractMonthEntity::getContractCode, contractCode);
        if (StringUtils.isNotEmpty(orgId)) {
            queryWrapper.lambda().eq(ContractMonthEntity::getOrgId, orgId);
        }
        queryWrapper.lambda().orderByDesc(ContractMonthEntity::getId);
        List<ContractMonthEntity> entityList = this.list(queryWrapper);
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        return BeanUtil.copyProperties(entityList.get(0), ContractMonthDTO.class);
    }

    @Override
    public List<ContractMonthEntity> getContractDTOByCode(String contractCode) {
        QueryWrapper<ContractMonthEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ContractMonthEntity::getContractCode, contractCode);
        queryWrapper.lambda().eq(ContractMonthEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        queryWrapper.lambda().orderByDesc(ContractMonthEntity::getId);
        List<ContractMonthEntity> entityList = this.list(queryWrapper);
        return entityList;
    }

    /**
     * 数据同步
     */
    public void dataSync() {
        log.info("合同月表数据同步开始----------------------------");
        boolean dataSyncCheckResult = dataSyncCheck();
        if (dataSyncCheckResult) {
            log.info("经过校验，数仓的关账日期和金蝶的关账日期已经同步，开始数据合同月表同步-------------------------");
            // 同步基础信息
            LambdaUpdateWrapper<ContractMonthEntity> wrapper = new LambdaUpdateWrapper<>();
            contractMonthMapper.delete(wrapper);
            contractMonthMapper.dataSync();

            // 同步合同状态、到期日、起租日
            contractMonthMapper.updateColumn();

            // 同步TA金额
            typtBusinessFacade.taAmountSync();
        } else {
            log.info("合同月表数据不能同步,原因: 数仓的关账日期和金蝶的关账日期不同步!");
        }
        log.info("合同月表数据同步结束----------------------------");
    }

    @Override
    public void updateByContractCodeM(ContractMonthEntity updateContractVo) {
        if (StringUtils.isNotEmpty(updateContractVo.getContractCodeM()))
        this.update(updateContractVo, new LambdaUpdateWrapper<ContractMonthEntity>()
                .eq(ContractMonthEntity::getContractCodeM, updateContractVo.getContractCodeM()));
    }

    @Override
    public void updateBatchByContractCode(List<ContractMonthEntity> updateContractMonthEntities) {
        // 使用 BATCH 执行器类型
        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
            ContractMonthMapper contractMapper = sqlSession.getMapper(ContractMonthMapper.class);

            for (ContractMonthEntity contract : updateContractMonthEntities) {
                contractMapper.updateBatchByContractCode(contract);
            }

            // 提交事务
            sqlSession.commit();
        }
    }

    @Override
    public List<ContractMonthEntity> getTranStatusList(List<String> contractCodeList) {
        List<ContractMonthEntity> contractMonthEntities = this.getBaseMapper().selectList(Wrappers.<ContractMonthEntity>lambdaQuery()
                .in(ContractMonthEntity::getContractCode, contractCodeList)
                .eq(ContractMonthEntity::getDelFlag, YesOrNoEnum.NO.getCode())
        );
        if (CollectionUtils.isEmpty(contractMonthEntities)){
            return Collections.emptyList();
        }
        for (ContractMonthEntity contractMonthEntity : contractMonthEntities) {
            contractMonthEntity.setContractStatus(ContractStatusTransferEnum.transferContractStatus(contractMonthEntity.getContractStatus()));
        }
        return contractMonthEntities;
    }

    /**
     * 是否进行合同数据同步的判定
     */
    private boolean dataSyncCheck() {
        // 查询金蝶最新账期
        R<List<OrgPeriodDTO>> r = kingdeeDataSyncFacade.queryKingdeeAccountingPeriod();
        if (r.getData() == null ||  r.getData().isEmpty()) {
            log.error("未查询到金蝶最新账期数据!");
            throw new ServiceException("未查询到金蝶最新账期数据!");
        }
        String kingdeePeriod = r.getData().get(0).getPeriodCode();
        log.info("金蝶的最新账期：" + kingdeePeriod);
        Integer kingdeeYM = Integer.parseInt(kingdeePeriod);

        // 查询数仓最新账期
        CloseAccountEntity closeAccountEntity = closeAccountService.queryCloseDate(SystemEnum.XWXT.getCode());
        if (closeAccountEntity == null) {
            log.error("未查询到数仓最新账期数据!");
            throw new ServiceException("未查询到数仓最新账期数据!");
        }
        String dataWarehousePeriod = closeAccountEntity.getCloseDate();
        Integer day = Integer.parseInt(dataWarehousePeriod.split("-")[2]);
        Integer dataWarehouseCloseYM = 0;
        if (day >= 20 && day < 32) {
            dataWarehouseCloseYM = Integer.parseInt(DateUtils.parseDateToStr("YYYYMM", DateUtils.parseDate(dataWarehousePeriod)));
        } else if (day > 0 && day < 10) {
            Date closeDate = DateUtils.addMonths(DateUtils.parseDate(dataWarehousePeriod), -1);
            dataWarehouseCloseYM = Integer.parseInt(DateUtils.parseDateToStr("YYYYMM", closeDate));
        } else {
            throw new ServiceException("数仓的关账日期异常!");
        }

        // 数仓关账、金蝶未关账不进行同步-相反两个账期日期不等(即表示在同一个账期内)，需要进行数据同步
        if (kingdeeYM.intValue() != dataWarehouseCloseYM.intValue()) {
            return true;
        } else if (StringUtils.equals(dataWarehousePeriod, DateUtils.getDate())) {
            // 数仓关账第一天要进行数据同步
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Map<String, Object> getContractMap(String contractCode, String orgId) {
        ContractMonthDTO contractEntity = this.getContractDTOByCode(contractCode,orgId);
        if (contractEntity == null) {
            return null;
        }
        Map<String, Object> oldMap = JSONObject.parseObject(JSONObject.toJSONString(contractEntity), new TypeReference<Map<String, Object>>() {
        });
        //修改拨备转回金额depreciation_reserves_balance
        oldMap.put("depreciationReservesBalance",getDepreciationReservesBalance(contractCode,orgId));
        Map<String, Object> newMap = new HashMap<>();
        Iterator<Map.Entry<String, Object>> it = oldMap.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String, Object> entry = it.next();
            newMap.put("contract_month_" + entry.getKey(), entry.getValue());
        }
        return newMap;
    }

    public BigDecimal getDepreciationReservesBalance(String contractCode, String orgId){
        BigDecimal maxReserves = BigDecimal.ZERO;
        if (StringUtils.isEmpty(contractCode) || StringUtils.isEmpty(orgId)) {
            return maxReserves;
        }
        maxReserves = iVerificationService.getMaxDepreciationReserves(contractCode,orgId);
        return null==maxReserves ? BigDecimal.ZERO : maxReserves;
    }


}

