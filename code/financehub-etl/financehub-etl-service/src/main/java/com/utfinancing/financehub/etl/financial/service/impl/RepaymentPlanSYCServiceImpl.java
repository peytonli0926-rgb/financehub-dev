package com.utfinancing.financehub.etl.financial.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.admin.api.RemoteDictService;
import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.redis.service.RedisService;
import com.utfinancing.financehub.etl.commveh.model.SelectContractCodeByPageDTO;
import com.utfinancing.financehub.etl.commveh.model.SelectReceiveRepaymentDTO;
import com.utfinancing.financehub.etl.commveh.model.SelectRepaymentFromSYCXTDTO;
import com.utfinancing.financehub.etl.commveh.model.SelectRepaymentFromSYCXTInputDTO;
import com.utfinancing.financehub.etl.commveh.service.ICommercialVehicleDataService;
import com.utfinancing.financehub.etl.commvehat.service.ICommercialVehicleAssetTransferDataService;
import com.utfinancing.financehub.etl.constant.RedisConstant;
import com.utfinancing.financehub.etl.enums.DictTypeEnum;
import com.utfinancing.financehub.etl.enums.SystemEnum;
import com.utfinancing.financehub.etl.financial.entity.RepaymentPlanExceldataEntity;
import com.utfinancing.financehub.etl.financial.entity.RepaymentPlanHYEntity;
import com.utfinancing.financehub.etl.financial.enums.YesOrNoEnum;
import com.utfinancing.financehub.etl.financial.mapper.RepaymentPlanHYMapper;
import com.utfinancing.financehub.etl.financial.model.dto.ContractDTO;
import com.utfinancing.financehub.etl.financial.model.dto.DataInitDTO;
import com.utfinancing.financehub.etl.financial.service.IContractService;
import com.utfinancing.financehub.etl.financial.service.IRepaymentPlanSYCService;
import com.utfinancing.financehub.etl.financial.service.IRepaymentPlanExceldataService;
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
public class RepaymentPlanSYCServiceImpl extends ServiceImpl<RepaymentPlanHYMapper, RepaymentPlanHYEntity>
        implements IRepaymentPlanSYCService {

    @Resource
    private RepaymentPlanHYMapper repaymentPlanHYMapper;

    @Resource
    private IRepaymentPlanExceldataService repaymentPlanExceldataService;

    @Resource
    private ICommercialVehicleDataService commercialVehicleDataService;

    @Resource
    private ICommercialVehicleAssetTransferDataService commercialVehicleAssetTransferDataService;

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
        log.info("商用车期初数据同步 Start--------------");
        int start = 0;
        int eachQueryNum = 1000;
        while (true) {
            SelectContractCodeByPageDTO pageInfo = new SelectContractCodeByPageDTO();
            pageInfo.setStart(start);
            pageInfo.setEnd(start + eachQueryNum);
            List<String> contractCodeList = commercialVehicleDataService.selectContractCodeByPage(pageInfo);
            if (contractCodeList == null || contractCodeList.isEmpty()) {
                break;
            } else {
                start = start + eachQueryNum;
            }

            SelectRepaymentFromSYCXTInputDTO dto = new SelectRepaymentFromSYCXTInputDTO();
            dto.setContractCodeList(contractCodeList);
            List<SelectRepaymentFromSYCXTDTO> selectRepaymentFromSYCXTDTOList = commercialVehicleDataService.
                    selectRepaymentFromSYCXTGroupInit(dto);
            log.info("商用车期初数据同步 数据量：" + selectRepaymentFromSYCXTDTOList.size());
            if (selectRepaymentFromSYCXTDTOList == null || selectRepaymentFromSYCXTDTOList.isEmpty()) {
                continue;
            }

            this.saveRepaymentPlanForSYC(selectRepaymentFromSYCXTDTOList);

            // 同步回笼数据
            log.info("商用车回笼数据同步 Start--------------");
            List<SelectReceiveRepaymentDTO> receiveRepaymentDTOList = commercialVehicleDataService.
                    selectReceiveRepayment(dto);
            if (receiveRepaymentDTOList != null && !receiveRepaymentDTOList.isEmpty()) {
                this.saveReceiveRepayment(receiveRepaymentDTOList);
            }
            log.info("商用车回笼数据同步 End--------------");
        }
        log.info("商用车期初数据同步 End");

        start = 0;
        while (true) {
            com.utfinancing.financehub.etl.commvehat.model.SelectContractCodeByPageDTO pageInfo =
                    new com.utfinancing.financehub.etl.commvehat.model.SelectContractCodeByPageDTO();
            pageInfo.setStart(start);
            pageInfo.setEnd(start + eachQueryNum);
            List<String> contractCodeList = commercialVehicleAssetTransferDataService.selectContractCodeByPage(pageInfo);
            if (contractCodeList == null || contractCodeList.isEmpty()) {
                break;
            } else {
                start = start + eachQueryNum;
            }

            // 乘用车资产转让期初数据同步
            com.utfinancing.financehub.etl.commvehat.model.SelectRepaymentFromSYCXTInputDTO dto1 =
                    new com.utfinancing.financehub.etl.commvehat.model.SelectRepaymentFromSYCXTInputDTO();
            dto1.setContractCodeList(contractCodeList);
            List<com.utfinancing.financehub.etl.commvehat.model.SelectRepaymentFromSYCXTDTO> selectRepaymentFromSYCXTDTOList1 =
                    commercialVehicleAssetTransferDataService.selectRepaymentFromSYCXTGroupInit(dto1);
            log.info("乘用车资产转让期初数据同步 数据量：" + selectRepaymentFromSYCXTDTOList1.size());
            if (selectRepaymentFromSYCXTDTOList1 == null || selectRepaymentFromSYCXTDTOList1.isEmpty()) {
                continue;
            }

            List<SelectRepaymentFromSYCXTDTO> selectRepaymentFromSYCXTDTOList = BeanUtil.
                    copyToList(selectRepaymentFromSYCXTDTOList1,
                    SelectRepaymentFromSYCXTDTO.class);
            this.saveRepaymentPlanForSYC(selectRepaymentFromSYCXTDTOList);

            // 同步回笼数据
            log.info("商用车资产转让回笼数据同步 Start--------------");
            List<com.utfinancing.financehub.etl.commvehat.model.SelectReceiveRepaymentDTO> receiveRepaymentDTOList1 =
                    commercialVehicleAssetTransferDataService.selectReceiveRepayment(dto1);
            if (receiveRepaymentDTOList1 != null && !receiveRepaymentDTOList1.isEmpty()) {
                List<SelectReceiveRepaymentDTO> receiveRepaymentDTOList = BeanUtil.
                        copyToList(receiveRepaymentDTOList1, SelectReceiveRepaymentDTO.class);
                this.saveReceiveRepayment(receiveRepaymentDTOList);
            }
            log.info("商用车资产转让回笼数据同步 End--------------");
        }
        log.info("商用车资产转让期初数据同步 End");

        return "商用车数据同步完成";
    }


    /**
     * 回笼数据同步
     */
    public String receivedAmountSync(DataInitDTO params) {
        int start = 0;
        int eachQueryNum = 1000;
        log.info("商用车回笼数据同步 Start--------------");
        while (true) {
            log.info("商用车回笼数据同步, StartIndex={}", start);
            SelectContractCodeByPageDTO pageInfo = new SelectContractCodeByPageDTO();
            pageInfo.setStart(start);
            pageInfo.setEnd(start + eachQueryNum);
            List<String> contractCodeList = commercialVehicleDataService.selectContractCodeByPage(pageInfo);
            if (contractCodeList == null || contractCodeList.isEmpty()) {
                break;
            } else {
                start = start + eachQueryNum;
            }

            SelectRepaymentFromSYCXTInputDTO dto = new SelectRepaymentFromSYCXTInputDTO();
            dto.setContractCodeList(contractCodeList);
            // 同步回笼数据
            List<SelectReceiveRepaymentDTO> receiveRepaymentDTOList = commercialVehicleDataService.
                    selectReceiveRepayment(dto);
            if (receiveRepaymentDTOList != null && !receiveRepaymentDTOList.isEmpty()) {
                this.saveReceiveRepayment(receiveRepaymentDTOList);
            }
        }
        log.info("商用车回笼数据同步 End--------------");

        start = 0;
        log.info("商用车资产转让回笼数据同步 Start--------------");
        while (true) {
            log.info("商用车资产转让回笼数据同步, StartIndex={}", start);
            com.utfinancing.financehub.etl.commvehat.model.SelectContractCodeByPageDTO pageInfo =
                    new com.utfinancing.financehub.etl.commvehat.model.SelectContractCodeByPageDTO();
            pageInfo.setStart(start);
            pageInfo.setEnd(start + eachQueryNum);
            List<String> contractCodeList = commercialVehicleAssetTransferDataService.selectContractCodeByPage(pageInfo);
            if (contractCodeList == null || contractCodeList.isEmpty()) {
                break;
            } else {
                start = start + eachQueryNum;
            }

            // 乘用车资产转让期初数据同步
            com.utfinancing.financehub.etl.commvehat.model.SelectRepaymentFromSYCXTInputDTO dto1 =
                    new com.utfinancing.financehub.etl.commvehat.model.SelectRepaymentFromSYCXTInputDTO();
            dto1.setContractCodeList(contractCodeList);

            // 同步回笼数据
            List<com.utfinancing.financehub.etl.commvehat.model.SelectReceiveRepaymentDTO> receiveRepaymentDTOList1 =
                    commercialVehicleAssetTransferDataService.selectReceiveRepayment(dto1);
            if (receiveRepaymentDTOList1 != null && !receiveRepaymentDTOList1.isEmpty()) {
                List<SelectReceiveRepaymentDTO> receiveRepaymentDTOList = BeanUtil.
                        copyToList(receiveRepaymentDTOList1, SelectReceiveRepaymentDTO.class);
                this.saveReceiveRepayment(receiveRepaymentDTOList);
            }
        }
        log.info("商用车资产转让回笼数据同步 End--------------");

        return "商用车回笼数据同步完成";
    }

    /**
     * 保存业务系统过来的偿还计划
     */
    @Override
    @Transactional
    public void saveRepaymentPlanForSYC(List<SelectRepaymentFromSYCXTDTO> repaymentList) {
        if (repaymentList == null || repaymentList.isEmpty()) {
            log.info("未查询到偿还计划数据");
            return ;
        }

//        R<List<SysDictData>> list = listDictTypeData(DictTypeEnum.IS_CALCULATE_REVENUE.getCode());
//        List<String> financeContractStatusList = Optional.ofNullable(list.getData().stream().
//                filter(e->StringUtils.isNotEmpty(e.getRemark()) && YesOrNoEnum.YES.getDesc().equals(e.getRemark()))).
//                orElse(null).map(SysDictData::getDictLabel).collect(Collectors.toList());

        // 按照合同分组
        Map<String, List<SelectRepaymentFromSYCXTDTO>> repaymentMap = repaymentList.stream().
                collect(Collectors.groupingBy(e->e.getContractCode().concat("|").concat(e.getOrgId())));

        // 查詢合同信息
        List<String> contractCodeList = repaymentList.stream().map(SelectRepaymentFromSYCXTDTO::getContractCode).
                distinct().collect(Collectors.toList());
//        List<ContractDTO> contractDTOList = contractService.listContractDTOByCodeList(contractCodeList);
//        Map<String, ContractDTO> contractDTOMap = contractDTOList.stream().collect(Collectors.
//                toMap(e->e.getContractCode().concat("|").concat(e.getOrgId()), (e)->e, (a, b)->b));

        List<SelectRepaymentFromSYCXTDTO> newRepaymentList = new ArrayList<>();
        for (String key : repaymentMap.keySet()) {

            List<SelectRepaymentFromSYCXTDTO> repaymentGroupList = repaymentMap.get(key);
//            ContractDTO contractDTO = contractDTOMap.get(key);
//            // 部分财务合同状态的数据，不进行计提操作
//            if (contractDTO == null ||  (StringUtils.isNotEmpty(contractDTO.getFinancialContractStatus()) &&
//                    !financeContractStatusList.contains(contractDTO.getFinancialContractStatus()))) {
//                continue;
//            }

            // 批量删除偿还计划
            newRepaymentList.addAll(repaymentGroupList);
        }

        repaymentPlanHYMapper.delRepaymentPlan(contractCodeList);

        // 保存偿还计划
        List<RepaymentPlanHYEntity> repaymentPlanHYEntityList = BeanUtil.copyToList(
                newRepaymentList, RepaymentPlanHYEntity.class);
        this.saveBatch(repaymentPlanHYEntityList);
        repaymentPlanHYEntityList.clear();
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
        wrapper.eq(RepaymentPlanExceldataEntity::getSystemCode, SystemEnum.SYCXT.getCode());
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
