package com.utfinancing.financehub.etl.financial.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.etl.easold.model.dto.EasVoucherDTO;
import com.utfinancing.financehub.etl.enums.Eas2SystemCodeSystemEnum;
import com.utfinancing.financehub.etl.financial.entity.VoucherDimensionValueEntity;
import com.utfinancing.financehub.etl.financial.entity.VoucherEntryDimensionValueEntity;
import com.utfinancing.financehub.etl.financial.service.IEasIntegrationService;
import com.utfinancing.financehub.etl.financial.service.IVoucherDimensionValueService;
import com.utfinancing.financehub.etl.financial.service.IVoucherEntryDimensionValueService;
import com.utfinancing.financehub.etl.financial.service.VoucherTransactionService;
import com.utfinancing.financehub.etl.easold.model.dto.QueryEas1VoucherInputDTO;
import com.utfinancing.financehub.etl.easold.service.OldEasVoucherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class VoucherTransactionServiceImpl implements VoucherTransactionService {


    private final OldEasVoucherService voucherService;

    private final IEasIntegrationService easIntegrationService;

    private final IVoucherDimensionValueService voucherDimensionValueService;

    private final IVoucherEntryDimensionValueService voucherEntryDimensionValueService;

    /**
     * 传递凭证到EAS系统
     */
    public R<Boolean> transferVoucherToEAS(QueryEas1VoucherInputDTO params) throws Exception {
        log.info("transferVoucherToEAS params:" + JSON.toJSONString(params));
        List<EasVoucherDTO> sourceVoucherList = voucherService.selectEas1Voucher(params);
        if (sourceVoucherList == null || sourceVoucherList.isEmpty()) {
            return R.fail("源凭证数据为空!");
        }
        //发送数据到Eas2
        sendDataToEas2(sourceVoucherList, Eas2SystemCodeSystemEnum.EAS1.getCode());
        log.info("TransferVoucherToEAS end; 数据量："+sourceVoucherList.size());
        return R.ok(true);
    }

    private void setDimensionValue(String[] dimArray, String[] dimValueArray, EasVoucherDTO dto) {

        for (int i = 0; i < dimArray.length; i++) {
            String dim = dimArray[i];
            if (StringUtils.equals(dim, dto.getAsstActType1()) && StringUtils.isNotEmpty(dto.getAsstActNumber1())
            && StringUtils.isNotEmpty(dto.getAsstActName1())) {
                dto.setAsstActNumber1(dimValueArray[i]);
                dto.setAsstActName1(dimValueArray[i]);
            }
            if (StringUtils.equals(dim, dto.getAsstActType2()) && StringUtils.isNotEmpty(dto.getAsstActNumber2())
                    && StringUtils.isNotEmpty(dto.getAsstActName2())) {
                dto.setAsstActNumber2(dimValueArray[i]);
                dto.setAsstActName2(dimValueArray[i]);
            }
            if (StringUtils.equals(dim, dto.getAsstActType3()) && StringUtils.isNotEmpty(dto.getAsstActNumber3())
                    && StringUtils.isNotEmpty(dto.getAsstActName3())) {
                dto.setAsstActNumber3(dimValueArray[i]);
                dto.setAsstActName3(dimValueArray[i]);
            }
            if (StringUtils.equals(dim, dto.getAsstActType4()) && StringUtils.isNotEmpty(dto.getAsstActNumber4())
                    && StringUtils.isNotEmpty(dto.getAsstActName4())) {
                dto.setAsstActNumber4(dimValueArray[i]);
                dto.setAsstActName4(dimValueArray[i]);
            }
            if (StringUtils.equals(dim, dto.getAsstActType5()) && StringUtils.isNotEmpty(dto.getAsstActNumber5())
                    && StringUtils.isNotEmpty(dto.getAsstActName5())) {
                dto.setAsstActNumber5(dimValueArray[i]);
                dto.setAsstActName5(dimValueArray[i]);
            }
            if (StringUtils.equals(dim, dto.getAsstActType6()) && StringUtils.isNotEmpty(dto.getAsstActNumber6())
                    && StringUtils.isNotEmpty(dto.getAsstActName6())) {
                dto.setAsstActNumber6(dimValueArray[i]);
                dto.setAsstActName6(dimValueArray[i]);
            }
            if (StringUtils.equals(dim, dto.getAsstActType7()) && StringUtils.isNotEmpty(dto.getAsstActNumber7())
                    && StringUtils.isNotEmpty(dto.getAsstActName7())) {
                dto.setAsstActNumber7(dimValueArray[i]);
                dto.setAsstActName7(dimValueArray[i]);
            }
            if (StringUtils.equals(dim, dto.getAsstActType8()) && StringUtils.isNotEmpty(dto.getAsstActNumber8())
                    && StringUtils.isNotEmpty(dto.getAsstActName8())) {
                dto.setAsstActNumber8(dimValueArray[i]);
                dto.setAsstActName8(dimValueArray[i]);
            }
            if (StringUtils.equals(dim, dto.getAsstActType9()) && StringUtils.isNotEmpty(dto.getAsstActNumber9())
                    && StringUtils.isNotEmpty(dto.getAsstActName9())) {
                dto.setAsstActNumber9(dimValueArray[i]);
                dto.setAsstActName9(dimValueArray[i]);
            }
            if (StringUtils.equals(dim, dto.getAsstActType10()) && StringUtils.isNotEmpty(dto.getAsstActNumber10())
                    && StringUtils.isNotEmpty(dto.getAsstActName10())) {
                dto.setAsstActNumber10(dimValueArray[i]);
                dto.setAsstActName10(dimValueArray[i]);
            }
            if (StringUtils.equals(dim, dto.getAsstActType11()) && StringUtils.isNotEmpty(dto.getAsstActNumber11())
                    && StringUtils.isNotEmpty(dto.getAsstActName11())) {
                dto.setAsstActNumber11(dimValueArray[i]);
                dto.setAsstActName11(dimValueArray[i]);
            }
            if (StringUtils.equals(dim, dto.getAsstActType12()) && StringUtils.isNotEmpty(dto.getAsstActNumber12())
                    && StringUtils.isNotEmpty(dto.getAsstActName12())) {
                dto.setAsstActNumber12(dimValueArray[i]);
                dto.setAsstActName12(dimValueArray[i]);
            }
            if (StringUtils.equals(dim, dto.getAsstActType13()) && StringUtils.isNotEmpty(dto.getAsstActNumber13())
                    && StringUtils.isNotEmpty(dto.getAsstActName13())) {
                dto.setAsstActNumber13(dimValueArray[i]);
                dto.setAsstActName13(dimValueArray[i]);
            }
            if (StringUtils.equals(dim, dto.getAsstActType14()) && StringUtils.isNotEmpty(dto.getAsstActNumber14())
                    && StringUtils.isNotEmpty(dto.getAsstActName14())) {
                dto.setAsstActNumber14(dimValueArray[i]);
                dto.setAsstActName14(dimValueArray[i]);
            }
        }
    }

    /**
     * 取得Key
     */
    private String getKey(EasVoucherDTO dto) {
        VoucherDimensionValueEntity params = new VoucherDimensionValueEntity();
        params.setOrgId(dto.getCompanyNumber());
        params.setAccountCode(dto.getAccountNumber());
        String sourceSystem = "EAS";
        if (StringUtils.isNotEmpty(dto.getSystemCode())) {
            sourceSystem = dto.getSystemCode();
        }
        params.setSourceSystem(sourceSystem);
        return voucherDimensionValueService.getBusinessKey(params);
    }

    private String getEntryKey(EasVoucherDTO dto) {
        VoucherEntryDimensionValueEntity params = new VoucherEntryDimensionValueEntity();
        params.setOrgId(dto.getCompanyNumber());
        params.setAccountCode(dto.getAccountNumber());
        return voucherEntryDimensionValueService.getBusinessKey(params);
    }

    public void sendDataToEas2 (List<EasVoucherDTO> sourceVoucherList,String systemCode) throws Exception {
        if (Eas2SystemCodeSystemEnum.FINHUB_ENTRY.getCode().equals(systemCode)) {
            voucherEntryDimensionValue(sourceVoucherList);
        } else  {
            // 传递参数维度数值转换
            List<VoucherDimensionValueEntity> voucherDimensionValueList = voucherDimensionValueService.
                    getVoucherDimensionValueListByCondition(new VoucherDimensionValueEntity());
            Map<String, VoucherDimensionValueEntity> voucherDimensionValueMap = new HashMap<>();
            if (voucherDimensionValueList != null && !voucherDimensionValueList.isEmpty()) {
                voucherDimensionValueMap = voucherDimensionValueList.stream().collect(Collectors.toMap(
                        e -> voucherDimensionValueService.getBusinessKey(e), e -> e, (a, b) -> b));
            }

            // 设置维度值
            for (EasVoucherDTO dto : sourceVoucherList) {

                // 设置会计期间
//            dto.setBookedDate(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.getNowDate()));
//            dto.setBizDate(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.getNowDate()));
//            dto.setPeriodYear(2024);
//            dto.setPeriodNumber(1);

                if (StringUtils.endsWith(dto.getCompanyNumber(), "old")) {
                    dto.setCompanyNumber(dto.getCompanyNumber().replace("-old", ""));
                }

                // 转换ischeck参数到true false
                if ("1".equals(dto.getIsCheck())) {
                    dto.setIsCheck("true");
                } else if ("0".equals(dto.getIsCheck())) {
                    dto.setIsCheck("false");
                }

                // 取得维度值
                VoucherDimensionValueEntity entity = voucherDimensionValueMap.get(getKey(dto));
                if (entity == null) {
                    continue;
                }

                String[] dimArray = entity.getDim().split("\\|");
                String[] dimValueArray = entity.getDimValue().split("\\|");
                if (dimArray.length != dimValueArray.length || dimArray.length == 0) {
                    log.error("VoucherDimensionValueEntity:id=" + entity.getId() + "数据维度数据错误!");
                    continue;
                }
                setDimensionValue(dimArray, dimValueArray, dto);
            }
        }

        // 同步到EAS
        Map<String, List<EasVoucherDTO>> sourceVoucherMap = sourceVoucherList.stream().collect(
                Collectors.groupingBy(e->e.getVoucherNumber()));
        List<EasVoucherDTO> batchAllEasVoucherDTO = new ArrayList<>();
        List<EasVoucherDTO> batchIsAuditEasVoucherDTO = new ArrayList<>();
        List<EasVoucherDTO> batchIsPostEasVoucherDTO = new ArrayList<>();
        List<EasVoucherDTO> batchAllNotEasVoucherDTO = new ArrayList<>();
        List<EasVoucherDTO> batchAllStageVoucherDTO = new ArrayList<>(); //暂存状态
        List<EasVoucherDTO> tempList = null;

        for (String key : sourceVoucherMap.keySet()) {
            tempList = sourceVoucherMap.get(key);
//            log.info("前："+JSON.toJSONString(tempList));
            //entrySeq按照从1排序
            if (!systemCode.equals(Eas2SystemCodeSystemEnum.EAS1.getCode())) {
                int i = 1;
                for (EasVoucherDTO v : tempList) {
                    v.setEntrySeq(i);
                    v.setAsstSeq(null);
                    i++;
                    if (!systemCode.equals(Eas2SystemCodeSystemEnum.FINHUB_ENTRY.getCode()) && StringUtils.isNotEmpty(v.getAccountNumber()) && v.getAccountNumber().startsWith("1002")) {
                        v.setAsstActName6("888");
                        v.setAsstActNumber6("888");
                    }
                }
            }
//            log.info("后:"+JSON.toJSONString(tempList));
            if (StringUtils.isNotEmpty(tempList.get(0).getAuditor()) && StringUtils.isNotEmpty(tempList.get(0).getPoster())) {
                batchAllEasVoucherDTO.addAll(tempList);
            } else if (StringUtils.isNotEmpty(tempList.get(0).getAuditor()) && StringUtils.isEmpty(tempList.get(0).getPoster())) {
                batchIsAuditEasVoucherDTO.addAll(tempList);
            }  else if (StringUtils.isEmpty(tempList.get(0).getAuditor()) && StringUtils.isNotEmpty(tempList.get(0).getPoster())) {
                batchIsPostEasVoucherDTO.addAll(tempList);
            }  else if (StringUtils.isEmpty(tempList.get(0).getAuditor()) && StringUtils.isEmpty(tempList.get(0).getPoster())) {
                batchAllNotEasVoucherDTO.addAll(tempList);
            } else if (StringUtils.isNotEmpty(tempList.get(0).getIsStage()) && "1".equals(tempList.get(0).getIsStage())) {
                batchAllStageVoucherDTO.addAll(tempList);
            }

            if (batchAllEasVoucherDTO.size() > 2000) {
                easIntegrationService.importVoucherToEAS(batchAllEasVoucherDTO,systemCode);
                batchAllEasVoucherDTO = new ArrayList<>();
            }

            if (batchIsAuditEasVoucherDTO.size() > 2000) {
                easIntegrationService.importVoucherToEAS(batchIsAuditEasVoucherDTO,systemCode);
                batchIsAuditEasVoucherDTO = new ArrayList<>();
            }

            if (batchIsPostEasVoucherDTO.size() > 2000) {
                easIntegrationService.importVoucherToEAS(batchIsPostEasVoucherDTO,systemCode);
                batchIsPostEasVoucherDTO = new ArrayList<>();
            }

            if (batchAllNotEasVoucherDTO.size() > 2000) {
                easIntegrationService.importVoucherToEAS(batchAllNotEasVoucherDTO,systemCode);
                batchAllNotEasVoucherDTO = new ArrayList<>();
            }

            if (batchAllStageVoucherDTO.size() > 2000) {
                easIntegrationService.importVoucherToEAS(batchAllStageVoucherDTO,systemCode);
                batchAllStageVoucherDTO = new ArrayList<>();
            }

        }

        if (batchAllEasVoucherDTO.size() > 0) {
            easIntegrationService.importVoucherToEAS(batchAllEasVoucherDTO,systemCode);
        }

        if (batchIsAuditEasVoucherDTO.size() > 0) {
            easIntegrationService.importVoucherToEAS(batchIsAuditEasVoucherDTO,systemCode);
        }

        if (batchIsPostEasVoucherDTO.size() > 0) {
            easIntegrationService.importVoucherToEAS(batchIsPostEasVoucherDTO,systemCode);
        }

        if (batchAllNotEasVoucherDTO.size() > 0) {
            easIntegrationService.importVoucherToEAS(batchAllNotEasVoucherDTO,systemCode);
        }
        if (batchAllStageVoucherDTO.size() > 0 ) {
            easIntegrationService.importVoucherToEAS(batchAllStageVoucherDTO,systemCode);
        }

    }

    @Async
    @Override
    public void middleVoucherToEas2(List<EasVoucherDTO> sourceVoucherList,String systemCode) throws Exception {
         if (CollectionUtils.isEmpty(sourceVoucherList)) {
             return;
         }
         sendDataToEas2(sourceVoucherList,systemCode);
    }

    public void voucherEntryDimensionValue(List<EasVoucherDTO> sourceVoucherList) {
        // 传递参数维度数值转换
        List<VoucherEntryDimensionValueEntity> voucherDimensionValueList = voucherEntryDimensionValueService.list();
        Map<String, VoucherEntryDimensionValueEntity> voucherDimensionValueMap = new HashMap<>();
        if (voucherDimensionValueList != null && !voucherDimensionValueList.isEmpty()) {
            voucherDimensionValueMap = voucherDimensionValueList.stream().collect(Collectors.toMap(
                    e -> voucherEntryDimensionValueService.getBusinessKey(e), e -> e, (a, b) -> b));
        }
        // 设置维度值
        for (EasVoucherDTO dto : sourceVoucherList) {
            if (StringUtils.endsWith(dto.getCompanyNumber(), "old")) {
                dto.setCompanyNumber(dto.getCompanyNumber().replace("-old", ""));
            }
            // 转换ischeck参数到true false
            if ("1".equals(dto.getIsCheck())) {
                dto.setIsCheck("true");
            } else if ("0".equals(dto.getIsCheck())) {
                dto.setIsCheck("false");
            }

            // 取得维度值
            VoucherEntryDimensionValueEntity entity = voucherDimensionValueMap.get(getEntryKey(dto));
            if (entity == null) {
                continue;
            }
            String[] dimArray = entity.getDim().split("\\|");
            String[] dimValueArray = entity.getDimValue().split("\\|");
            if (dimArray.length != dimValueArray.length || dimArray.length == 0) {
                log.error("VoucherEntryDimensionValueEntity:id=" + entity.getId() + "数据维度数据错误!");
                continue;
            }
            setDimensionValue(dimArray, dimValueArray, dto);
        }
    }
}
