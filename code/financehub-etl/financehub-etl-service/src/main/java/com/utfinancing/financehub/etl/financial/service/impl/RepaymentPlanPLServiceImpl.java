package com.utfinancing.financehub.etl.financial.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.admin.api.RemoteDictService;
import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.common.redis.service.RedisService;
import com.utfinancing.financehub.etl.constant.RedisConstant;
import com.utfinancing.financehub.etl.enums.DictTypeEnum;
import com.utfinancing.financehub.etl.enums.SystemEnum;
import com.utfinancing.financehub.etl.financial.entity.ContractTaAmountEntity;
import com.utfinancing.financehub.etl.financial.entity.RepaymentPlanExceldataEntity;
import com.utfinancing.financehub.etl.financial.entity.RepaymentPlanPLEntity;
import com.utfinancing.financehub.etl.financial.enums.YesOrNoEnum;
import com.utfinancing.financehub.etl.financial.mapper.ContractTaAmountMapper;
import com.utfinancing.financehub.etl.financial.mapper.RepaymentPlanPLMapper;
import com.utfinancing.financehub.etl.financial.model.dto.ContractDTO;
import com.utfinancing.financehub.etl.financial.model.dto.DataInitDTO;
import com.utfinancing.financehub.etl.financial.service.*;
import com.utfinancing.financehub.etl.platform.model.SelectReceiveRepaymentDTO;
import com.utfinancing.financehub.etl.platform.model.SelectRepaymentFromTYPTDTO;
import com.utfinancing.financehub.etl.platform.model.SelectRepaymentFromTYPTInputDTO;
import com.utfinancing.financehub.etl.platform.model.SelectTaAmountDTO;
import com.utfinancing.financehub.etl.platform.service.IPlatformDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Slf4j
@Service
public class RepaymentPlanPLServiceImpl extends ServiceImpl<RepaymentPlanPLMapper, RepaymentPlanPLEntity>
        implements IRepaymentPlanPLService {

    @Resource
    private RepaymentPlanPLMapper repaymentPlanPLMapper;

    @Resource
    private IRepaymentPlanExceldataService repaymentPlanExceldataService;

    @Resource
    private IPlatformDataService platformDataService;

    @Resource
    private IContractService contractService;

    @Resource
    private RedisService redisService;

    @Resource
    private RemoteDictService remoteDictService;

    @Resource
    private IContractTaAmountService contractTaAmountService;

    /**
     * 期初数据同步
     */
    public String dataInit(DataInitDTO params) {
        log.info("统一平台期初数据同步 Start--------------");
        SelectRepaymentFromTYPTInputDTO dto = new SelectRepaymentFromTYPTInputDTO();
        if (StringUtils.isNotEmpty(params.getContractCode())) {
            dto.setContractCode(params.getContractCode());
        }
        List<SelectRepaymentFromTYPTDTO> selectRepaymentFromTYPTDTOList = platformDataService.
                selectRepaymentFromTYPTGroupInit(dto);
        log.info("统一平台期初数据同步 数据量：" + selectRepaymentFromTYPTDTOList.size());
        if (selectRepaymentFromTYPTDTOList == null || selectRepaymentFromTYPTDTOList.isEmpty()) {
            log.info("统一平台期初数据同步 End");
            return "统一平台期初数据同步完成";
        }

        this.saveRepaymentPlanForPL(selectRepaymentFromTYPTDTOList);
        log.info("统一平台期初数据同步 End");

        // 同步回笼数据
        log.info("统一平台回笼数据同步 Start--------------");
        List<SelectReceiveRepaymentDTO> receiveRepaymentDTOList = platformDataService.selectReceiveRepayment(dto);
        if (receiveRepaymentDTOList != null && !receiveRepaymentDTOList.isEmpty()) {
            this.saveReceiveRepayment(receiveRepaymentDTOList);
        }
        log.info("统一平台回笼数据同步 End--------------");

        // 同步TA余额
        log.info("统一平台TA余额同步 Start--------------");
        this.taAmountSync();
        log.info("统一平台TA余额同步 End--------------");
        return "统一平台数据同步完成";
    }

    /**
     * 回笼数据同步
     */
    public String receivedAmountSync(DataInitDTO params) {
        SelectRepaymentFromTYPTInputDTO dto = new SelectRepaymentFromTYPTInputDTO();
        if (StringUtils.isNotEmpty(params.getContractCode())) {
            dto.setContractCode(params.getContractCode());
        }
        // 同步回笼数据
        log.info("统一平台回笼数据同步 Start--------------");
        List<SelectReceiveRepaymentDTO> receiveRepaymentDTOList = platformDataService.selectReceiveRepayment(dto);
        if (receiveRepaymentDTOList != null && !receiveRepaymentDTOList.isEmpty()) {
            this.saveReceiveRepayment(receiveRepaymentDTOList);
        }
        log.info("统一平台回笼数据同步 End--------------");
        return "统一平台回笼数据同步完成";
    }

    /**
     * 保存业务系统过来的偿还计划
     */
    @Override
    @Transactional
    public void saveRepaymentPlanForPL(List<SelectRepaymentFromTYPTDTO> repaymentList) {
        if (repaymentList == null || repaymentList.isEmpty()) {
            log.info("未查询到偿还计划数据");
            return ;
        }

//        R<List<SysDictData>> list = listDictTypeData(DictTypeEnum.IS_CALCULATE_REVENUE.getCode());
//        List<String> financeContractStatusList = Optional.ofNullable(list.getData().stream().
//                filter(e->StringUtils.isNotEmpty(e.getRemark()) && YesOrNoEnum.YES.getDesc().equals(e.getRemark()))).
//                orElse(null).map(SysDictData::getDictLabel).collect(Collectors.toList());

        // 按照合同分组
        Map<String, List<SelectRepaymentFromTYPTDTO>> repaymentMap = repaymentList.stream().
                filter(e->StringUtils.isNotEmpty(e.getContractCode()) && StringUtils.isNotEmpty(e.getOrgId())).
                collect(Collectors.groupingBy(e->e.getContractCode().concat("|").concat(e.getOrgId())));

        List<String> contractCodeList = new ArrayList<>();
        List<SelectRepaymentFromTYPTDTO> newRepaymentList = new ArrayList<>();
        for (String key : repaymentMap.keySet()) {
            String contractCode = key.split("\\|")[0];
//            String orgId = key.split("\\|")[1];

            List<SelectRepaymentFromTYPTDTO> repaymentGroupList = repaymentMap.get(key);
//            ContractDTO contractDTO = contractService.getContractDTOByCode(contractCode, orgId);
//            // 部分财务合同状态的数据，不进行计提操作
//            if (contractDTO == null || (StringUtils.isNotEmpty(contractDTO.getFinancialContractStatus()) &&
//                    !financeContractStatusList.contains(contractDTO.getFinancialContractStatus()))) {
//                continue;
//            }

            // 批量删除偿还计划
            contractCodeList.add(contractCode);
            newRepaymentList.addAll(repaymentGroupList);
            if (contractCodeList.size() >= 500) {
                repaymentPlanPLMapper.delRepaymentPlan(contractCodeList);
                contractCodeList.clear();
            }
        }
        if (contractCodeList.size() > 0) {
            repaymentPlanPLMapper.delRepaymentPlan(contractCodeList);
            contractCodeList.clear();
        }

        // 保存偿还计划
        List<RepaymentPlanPLEntity> repaymentPlanPLEntityList = BeanUtil.copyToList(
                newRepaymentList, RepaymentPlanPLEntity.class);
        this.saveBatch(repaymentPlanPLEntityList);
        repaymentPlanPLEntityList.clear();
    }

    /**
     * 保存收到的回笼数据
     */
    @Transactional
    public void saveReceiveRepayment(List<SelectReceiveRepaymentDTO> receiveRepaymentDTOList) {
        if (receiveRepaymentDTOList == null || receiveRepaymentDTOList.isEmpty()) {
            return;
        }

        List<String> contractCodeList = receiveRepaymentDTOList.stream().map(e->e.getContractCode()).
                distinct().collect(Collectors.toList());

        LambdaUpdateWrapper<RepaymentPlanExceldataEntity> wrapper = new LambdaUpdateWrapper();
        wrapper.eq(RepaymentPlanExceldataEntity::getSystemCode, SystemEnum.TYPT.getCode());
        wrapper.in(RepaymentPlanExceldataEntity::getContractCode, contractCodeList);
        repaymentPlanExceldataService.remove(wrapper);

        List<RepaymentPlanExceldataEntity> repaymentPlanExceldataEntityList =
                BeanUtil.copyToList(receiveRepaymentDTOList, RepaymentPlanExceldataEntity.class);
        repaymentPlanExceldataService.saveBatch(repaymentPlanExceldataEntityList);
    }


    /**
     * 字典数据
     * @return
     */
    public R<List<SysDictData>> listDictTypeData (String dictType) {
        R<List<SysDictData>> sysDictR = redisService.getCacheObject(
                String.format(RedisConstant.V_DICT_SYS_CASH_TYPE,dictType));
        if (ObjectUtil.isNull(sysDictR)) {
            sysDictR =  remoteDictService.listDictData(dictType);
            if (ObjectUtil.isNotNull(sysDictR)) {
                redisService.setCacheObject(String.format(RedisConstant.V_DICT_SYS_CASH_TYPE,dictType),
                        sysDictR, RedisConstant.TIME_OUT, TimeUnit.MINUTES);
            }
        }
        return sysDictR;
    }

    /**
     * TA余额同步
     */
    public R<String> taAmountSync() {
        List<SelectTaAmountDTO> taAmountDTOList = platformDataService.queryTaAmount();
        if (taAmountDTOList == null || taAmountDTOList.isEmpty()) {
            return null;
        }

        // 清空表数据
        contractTaAmountService.remove(new LambdaQueryWrapper<>());

        R<List<SysDictData>> orgR = this.listDictTypeData(DictTypeEnum.COMPANY.getCode());
        Map<String, String> orgMap = orgR.getData().stream().collect(Collectors.toMap(e -> e.getDictLabel(),
                e -> e.getDictValue()));

        List<ContractTaAmountEntity> contractTaAmountEntityList = BeanUtil.copyToList(
                taAmountDTOList, ContractTaAmountEntity.class);
        contractTaAmountEntityList.stream().forEach(e-> {
            e.setId(IdWorker.getId());
            e.setOrgId(orgMap.get(e.getOrgId()));
            e.setTransactionDate(DateUtils.getNowDate());
        });

        contractTaAmountService.saveBatch(contractTaAmountEntityList);
        return R.ok();
    }
}
