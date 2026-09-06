package com.utfinancing.financehub.etl.financial.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.admin.api.RemoteDictService;
import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.redis.service.RedisService;
import com.utfinancing.financehub.etl.constant.RedisConstant;
import com.utfinancing.financehub.etl.enums.DictTypeEnum;
import com.utfinancing.financehub.etl.enums.SystemEnum;
import com.utfinancing.financehub.etl.financial.entity.RepaymentPlanExceldataEntity;
import com.utfinancing.financehub.etl.financial.entity.RepaymentPlanXWEntity;
import com.utfinancing.financehub.etl.financial.enums.YesOrNoEnum;
import com.utfinancing.financehub.etl.financial.mapper.RepaymentPlanXWMapper;
import com.utfinancing.financehub.etl.financial.model.dto.ContractDTO;
import com.utfinancing.financehub.etl.financial.model.dto.DataInitDTO;
import com.utfinancing.financehub.etl.financial.service.IContractService;
import com.utfinancing.financehub.etl.financial.service.IRepaymentPlanExceldataService;
import com.utfinancing.financehub.etl.financial.service.IRepaymentPlanXWService;
import com.utfinancing.financehub.etl.micro.model.SelectRepaymentFromXWXTDTO;
import com.utfinancing.financehub.etl.micro.model.SelectRepaymentFromXWXTInputDTO;
import com.utfinancing.financehub.etl.micro.service.IMicroDataService;
import com.utfinancing.financehub.etl.micro.model.SelectReceiveRepaymentDTO;
import com.utfinancing.financehub.etl.platform.model.SelectRepaymentFromTYPTInputDTO;
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
public class RepaymentPlanXWServiceImpl extends ServiceImpl<RepaymentPlanXWMapper, RepaymentPlanXWEntity>
        implements IRepaymentPlanXWService {

    @Resource
    private RepaymentPlanXWMapper repaymentPlanXWMapper;

    @Resource
    private IRepaymentPlanExceldataService repaymentPlanExceldataService;

    @Resource
    private IMicroDataService microDataService;

    @Resource
    private IContractService contractService;

    @Resource
    private RedisService redisService;

    @Resource
    private RemoteDictService remoteDictService;
    /**
     * 期初数据同步
     */
    public String dataInit(DataInitDTO params) {
        log.info("小微系统期初数据同步 Start--------------");
        SelectRepaymentFromXWXTInputDTO dto = new SelectRepaymentFromXWXTInputDTO();
        if (StringUtils.isNotEmpty(params.getContractCode())) {
            dto.setContractCode(params.getContractCode());
        }
        List<SelectRepaymentFromXWXTDTO> selectRepaymentFromXWXTDTOList = microDataService.selectRepaymentFromXWXTGroupInit(dto);
        log.info("小微系统期初数据同步 数据量：" + selectRepaymentFromXWXTDTOList.size());
        if (selectRepaymentFromXWXTDTOList == null || selectRepaymentFromXWXTDTOList.isEmpty()) {
            log.info("小微系统期初数据同步 End");
            return "小微系统期初数据同步完成";
        }

        this.saveRepaymentPlanForXW(selectRepaymentFromXWXTDTOList);
        log.info("小微系统期初数据同步 End");

        // 同步回笼数据
        log.info("小微系统回笼数据同步 Start--------------");
        List<SelectReceiveRepaymentDTO> receiveRepaymentDTOList = microDataService.selectReceiveRepayment(dto);
        if (receiveRepaymentDTOList != null && !receiveRepaymentDTOList.isEmpty()) {
            this.saveReceiveRepayment(receiveRepaymentDTOList);
        }
        log.info("小微系统回笼数据同步 End--------------");

        return "小微系统数据同步完成";
    }


    /**
     * 回笼数据同步
     */
    public String receivedAmountSync(DataInitDTO params) {
        SelectRepaymentFromXWXTInputDTO dto = new SelectRepaymentFromXWXTInputDTO();
        if (StringUtils.isNotEmpty(params.getContractCode())) {
            dto.setContractCode(params.getContractCode());
        }
        // 同步回笼数据
        log.info("小微系统回笼数据同步 Start--------------");
        List<SelectReceiveRepaymentDTO> receiveRepaymentDTOList = microDataService.selectReceiveRepayment(dto);
        if (receiveRepaymentDTOList != null && !receiveRepaymentDTOList.isEmpty()) {
            this.saveReceiveRepayment(receiveRepaymentDTOList);
        }
        log.info("小微系统回笼数据同步 End--------------");
        return "小微系统回笼数据同步完成";
    }

    /**
     * 保存业务系统过来的偿还计划
     */
    @Override
    @Transactional
    public void saveRepaymentPlanForXW(List<SelectRepaymentFromXWXTDTO> repaymentList) {
        if (repaymentList == null || repaymentList.isEmpty()) {
            log.info("未查询到偿还计划数据");
            return ;
        }

//        R<List<SysDictData>> list = listDictTypeData(DictTypeEnum.IS_CALCULATE_REVENUE.getCode());
//        List<String> financeContractStatusList = Optional.ofNullable(list.getData().stream().
//                        filter(e->StringUtils.isNotEmpty(e.getRemark()) && YesOrNoEnum.YES.getDesc().equals(e.getRemark()))).
//                orElse(null).map(SysDictData::getDictLabel).collect(Collectors.toList());

        // 按照合同分组
        Map<String, List<SelectRepaymentFromXWXTDTO>> repaymentMap = repaymentList.stream().
                filter(e->StringUtils.isNotEmpty(e.getContractCode()) && StringUtils.isNotEmpty(e.getOrgId())).
                collect(Collectors.groupingBy(e->e.getContractCode().concat("|").concat(e.getOrgId())));


        List<String> contractCodeList = new ArrayList<>();
        List<SelectRepaymentFromXWXTDTO> newRepaymentList = new ArrayList<>();
        for (String key : repaymentMap.keySet()) {
            String contractCode = key.split("\\|")[0];
//            String orgId = key.split("\\|")[1];

            List<SelectRepaymentFromXWXTDTO> repaymentGroupList = repaymentMap.get(key);
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
                repaymentPlanXWMapper.delRepaymentPlan(contractCodeList);
                contractCodeList.clear();
            }
        }
        if (contractCodeList.size() > 0) {
            repaymentPlanXWMapper.delRepaymentPlan(contractCodeList);
            contractCodeList.clear();
        }

        // 保存偿还计划
        List<RepaymentPlanXWEntity> repaymentPlanXWEntityList = BeanUtil.copyToList(newRepaymentList,
                RepaymentPlanXWEntity.class);
        this.saveBatch(repaymentPlanXWEntityList);
        repaymentPlanXWEntityList.clear();
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
        wrapper.eq(RepaymentPlanExceldataEntity::getSystemCode, SystemEnum.XWXT.getCode());
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
}
