package com.utfinancing.financehub.etl.financial.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
/*import com.kingdee.eas.fi.gl.ZipUtils;*/
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.etl.easold.model.dto.EasVoucherDTO;
import com.utfinancing.financehub.etl.enums.Eas2SystemCodeSystemEnum;
import com.utfinancing.financehub.etl.financial.entity.VoucherEntryEntity;
import com.utfinancing.financehub.etl.financial.mapper.VoucherEntryMapper;
import com.utfinancing.financehub.etl.financial.service.*;
import com.utfinancing.financehub.etl.financial.util.EasWebServiceUtil;
import com.utfinancing.financehub.etl.kingdee.model.dto.EasVoucherRespDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Node;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * @Author : lixin
 * @Date : Create in 30/10/2023
 */
@Service
@Slf4j
@Transactional
public class EasIntegrationServiceImpl implements IEasIntegrationService {


    @Resource
    private KingdeeEASDataService kingdeeEASDataService;

    @Value("${eas.service.login.url}")
    private String LOGIN_URL;

    @Value("${eas.service.importVoucher.url}")
    private String IMPORT_VOUCHER_URL;

    @Value("${eas.service.login.user}")
    private String USER;

    @Value("${eas.service.login.password}")
    private String PASSWORD;

    @Value("${eas.service.login.slnName}")
    private String SLN_NAME;

    @Value("${eas.service.login.dcName}")
    private String DC_NAME;

    @Value("${eas.service.login.language}")
    private String LANGUAGE;

    @Value("${eas.service.login.dbType}")
    private Integer DB_TYPE;

    @Value("${eas.service.addVoucher.url}")
    private String ADD_VOUCHER_URL;

    @Value("${eas.service.addVoucher.system}")
    private String ADD_VOUCHER_SYSTEM;

    @Value("${eas.service.addVoucher.type}")
    private String ADD_VOUCHER_TYPE;

//    @Value("${eas.service.login2.url}")
//    private String LOGIN_URL2;

    @Value("${eas.service.importVoucher2.url}")
    private String IMPORT_VOUCHER_URL2;

//    @Value("${eas.service.login2.user}")
//    private String USER2;
//
//    @Value("${eas.service.login2.password}")
//    private String PASSWORD2;
//
//    @Value("${eas.service.login2.slnName}")
//    private String SLN_NAME2;
//
//    @Value("${eas.service.login2.dcName}")
//    private String DC_NAME2;
//
//    @Value("${eas.service.login2.language}")
//    private String LANGUAGE2;
//
//    @Value("${eas.service.login2.dbType}")
//    private Integer DB_TYPE2;

    @Value("${eas.service.addVoucher2.url}")
    private String ADD_VOUCHER_URL2;

    @Value("${eas.service.addVoucher2.system}")
    private String ADD_VOUCHER_SYSTEM2;

    @Value("${eas.service.addVoucher2.type}")
    private String ADD_VOUCHER_TYPE2;

    @Resource
    ISendEas2ResultService iSendEas2ResultService;
    @Resource
    VoucherEntryMapper voucherEntryMapper;

    @Override
    public String login() {
        Map<String, Object> param = new LinkedHashMap<>(10);
        param.put("user", USER);
        param.put("password", PASSWORD);
        param.put("slnName", SLN_NAME);
        param.put("dcName", DC_NAME);
        param.put("language", LANGUAGE);
        param.put("dbType", DB_TYPE);
        String response = EasWebServiceUtil.send(LOGIN_URL, "login", null, param);
        //解析xml,获取响应
        Node sessionNode = XmlUtil.getNodeByXPath("//sessionId", XmlUtil.parseXml(response));
        if (sessionNode == null) {
            throw new ServiceException("获取金蝶sessionId失败");
        }
        String sessionId = sessionNode.getTextContent();
        if (StrUtil.isBlank(sessionId)) {
            throw new ServiceException("获取金蝶sessionId失败, sessionId为空");
        }
        return sessionId;
    }

//    @Override
//    public String login2() {
//        Map<String, Object> param = new LinkedHashMap<>(10);
//        param.put("user", USER2);
//        param.put("password", PASSWORD2);
//        param.put("slnName", SLN_NAME2);
//        param.put("dcName", DC_NAME2);
//        param.put("language", LANGUAGE2);
//        param.put("dbType", DB_TYPE2);
//        String response = EasWebServiceUtil.send(LOGIN_URL2, "login", null, param);
//        //解析xml,获取响应
//        Node sessionNode = XmlUtil.getNodeByXPath("//sessionId", XmlUtil.parseXml(response));
//        if (sessionNode == null){
//            throw new ServiceException("获取金蝶sessionId失败");
//        }
//        String sessionId = sessionNode.getTextContent();
//        if (StrUtil.isBlank(sessionId)){
//            throw new ServiceException("获取金蝶sessionId失败, sessionId为空");
//        }
//        return sessionId;
//    }

//    @Override
//    public void logout2(String sessionId) {
//        Map<String, Object> param = new LinkedHashMap<>(10);
//        param.put("user", USER2);
//        param.put("slnName", SLN_NAME2);
//        param.put("dcName", DC_NAME2);
//        param.put("language", LANGUAGE2);
//
//        Map<String, String> headers = new LinkedHashMap<>();
//        headers.put("sessionId", sessionId);
//
//        EasWebServiceUtil.send(LOGIN_URL2, "logout", headers, param);
//    }


    @Override
    public void logout(String sessionId) {
        Map<String, Object> param = new LinkedHashMap<>(10);
        param.put("user", USER);
        param.put("slnName", SLN_NAME);
        param.put("dcName", DC_NAME);
        param.put("language", LANGUAGE);

        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("sessionId", sessionId);

        EasWebServiceUtil.send(LOGIN_URL, "logout", headers, param);
    }

//    public static void main(String[] args) {
//        try {
//            String result = ZipUtils.gunzip("H4sIAAAAAAAAAO2U0UsUQRzH/5W4Z0dmdmf3dnvzlEgSHzIfQnyYm53LwbvbdWYvuJ6uwMgkTbJAiCQjMqigCBS0/8a9Pf+LZnavvdvNyELFB2GO+31nPvf73Xx/P2ZurkT9RkCa7elWo8pEaaR032/RBSYyHTDBfe8uIwORnVV9f5F5EyRkWvAH/Uj6LUHZnXbABvn6ymOSCh6E3G8OzsaqMhSEhmqHy/EFRhdVRAUjoa+rkJbH0yjwZZgU5o3AF+Et1tYgkQs82U3rVni9PullcqYtdaXRTE37ea35WVFXm6wZivYMW9I1KfVbzTC7KW0JwZp0YFPCToyrqMGIbAnWUDsJKGUa1X1K6rdTR3zB7/EmqY81dNbEhyoPM6Xu6g3JpVDfKxCc6t8SKcP+n5KSy3DILX00RkPtLRrI9D8Ob5DG8LnGjSJu5HEjj5tF3MzjZh7HRRzncZzHrSJu5XErj9tF3M7jdh4vF/FyHi/ncaeIO3ncKc2PzJUgAuMQQu0qhsQhVhmCMqIYYFaugSqzCHA9hglEHqo5VGEGNPS1EerHACGA8DUIryfrj7tqQW1AdNiJ3+/q4PG3483OLxhi4ELTccGUASsIYtvCwEQQGiZCJgTQtR2IsQuO9l/Em7vR+oZKAuKXW73vn48O9oDKGi3vRTtvos5hvLuWnqsiNVKXevhUJhPr7sZrq71n77o7y4X4xuT0zdnKKT0oLG0Fskw0CtVylahUEkdR/9rpl7rlqAWHg+HPRRkSr3zsvtpSwSBtUURfdrpP9nQjkYuQi21bj1b363ZvdT0+2O493Iw/bHSfdnqP3h4d/Oi+Xjneeh4tf4rW93+35h/W1Tie0TjqN88wjHQcrb+O44kjefar0F4IHQiZbQKM7BrAFLnAMS0PIItYruNWiemY595eC0whV7XXQZZxQnudc27vKT24wNfmrA0ZvDZZ2qK4HK/N1Tj+7zhe0tdm/idoVEFoEwwAAA==");
//            System.out.println(result);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    /**
     * 导入凭证到EAS
     */
    @Async
    public void importVoucherToEAS(List<EasVoucherDTO> voucherEntryList, String systemCode) throws Exception {
        // 按照entry no进行排序
//        voucherEntryList = voucherEntryList.stream().sorted(Comparator.comparingInt(EasVoucherDTO::getEntrySeq)).
//                collect(Collectors.toList());
        AtomicInteger seq = new AtomicInteger(1);
        List<String> oriEntryIds = voucherEntryList.stream().map(EasVoucherDTO::getEntryId).collect(Collectors.toList());
        voucherEntryList = voucherEntryList.stream()
                //过滤凭证为零的数据
                .filter(this::filterZoreData)
                //重置业务日期大于记账日期的数据，
                .peek(b -> {
                    b.setEntrySeq(seq.getAndIncrement());
                    if (b.getBizDate().compareTo(b.getBookedDate()) > 0) {
                        b.setBizDate(b.getBookedDate());
                    }
                }).collect(Collectors.toList());
        resetEntrySeq(voucherEntryList, systemCode);
        if (CollectionUtils.isEmpty(voucherEntryList)) {
            return;
        }
        List<String> handlerEntryIds = voucherEntryList.stream().map(EasVoucherDTO::getEntryId).collect(Collectors.toList());
        List<String> setInvalidIds = oriEntryIds.stream().filter(id -> !handlerEntryIds.contains(id)).collect(Collectors.toList());

        log.info("Thread:" + Thread.currentThread().getName());
        //获取sessionID
        String sessionId = null;
        String importVoucherUrl = IMPORT_VOUCHER_URL;
        sessionId = login();
//        if (systemCode.equals(Eas2SystemCodeSystemEnum.FINHUB_ENTRY.getCode())) {
//            sessionId = login2();
//            importVoucherUrl = IMPORT_VOUCHER_URL2;
//        } else {
//            sessionId = login();
//        }

        LocalDateTime now = LocalDateTime.now();

        //封装header
        Map<String, String> headers = new HashMap<>();
        headers.put("sessionId", sessionId);

        //封装参数
        boolean isCompress = true;
        List<List<String>> rows = createRowsParams(voucherEntryList, systemCode);
        String rowParams = "";
        if (isCompress) {
            log.info("数据压缩开始");
//            rowParams = ZipUtils.gzip(JSON.toJSONString(rows));
            log.info("数据压缩结束");
        }
        String metaParams = createMetaParams(isCompress, voucherEntryList);

        Map<String, Object> param = new HashMap<>();
        param.put("params", metaParams);
        param.put("rows", rowParams);

        String batchUuid = UUID.randomUUID().toString();
        log.info("批次号：" + batchUuid);
        kingdeeEASDataService.saveParams(voucherEntryList, now, rowParams, systemCode, batchUuid, metaParams);
        log.info("传递参数保存成功，保存数据量:" + voucherEntryList.size());

        log.info("调用金蝶接口开始");
        //传过去的索引号和fid匹配
        String response = EasWebServiceUtil.send1(importVoucherUrl.concat("?wsdl"), "web:nImportVoucher", sessionId, param);
        log.info("调用金蝶接口结束");
        log.info("调用金蝶接口返回结果：{}", response);

        // 返回结果解析
        Node returnNode = XmlUtil.getNodeByXPath("//nImportVoucherReturn", XmlUtil.parseXml(response));
        Map result = new HashMap();
        if (isCompress) {
//            result = JSON.parseObject(ZipUtils.gunzip(returnNode.getTextContent()), Map.class);
        } else {
            result = JSON.parseObject(returnNode.getTextContent(), Map.class);
        }

        if (result == null || result.keySet().size() == 0) {
            throw new ServiceException(StrUtil.format("EAS返回数据为空，完整响应内容：{}", response));
        }


        //保存fid结果是否成功
        iSendEas2ResultService.saveFidSendEas2Result(voucherEntryList, result, systemCode, batchUuid);

        //更新过滤不传金蝶的数据
        if (!setInvalidIds.isEmpty()) {
            int update = voucherEntryMapper.removeByIds(setInvalidIds);
            log.info("voucherEntryMapper.update count:{}",update);
        }

        Map sucs = (Map) result.get("sucs");
        kingdeeEASDataService.createVoucherToEasResultEntity(sucs, "sucs", now, batchUuid);

        Map errs = (Map) result.get("errs");
        kingdeeEASDataService.createVoucherToEasResultEntity(errs, "errs", now, batchUuid);
        log.info("Transfer Data end...");

    }

    private static void resetEntrySeq(List<EasVoucherDTO> voucherEntryList, String systemCode) {
        Map<String, List<EasVoucherDTO>> sourceVoucherMap = voucherEntryList.stream().collect(
                Collectors.groupingBy(e->e.getVoucherNumber()));
        for (String key : sourceVoucherMap.keySet()) {
            List<EasVoucherDTO> tempList = sourceVoucherMap.get(key);
            //entrySeq按照从1排序
            if (!systemCode.equals(Eas2SystemCodeSystemEnum.EAS1.getCode())) {
                int i = 1;
                for (EasVoucherDTO v : tempList) {
                    v.setEntrySeq(i);
                    i++;
                }
            }
        }
    }

    private boolean filterZoreData(EasVoucherDTO easVoucherDTO) {
        if (easVoucherDTO.getDebitAmount() == null && easVoucherDTO.getCreditAmount() == null) {
            return Boolean.FALSE;
        } else if (easVoucherDTO.getDebitAmount() != null && BigDecimal.ZERO.compareTo(easVoucherDTO.getDebitAmount()) != 0) {
            return easVoucherDTO.getCreditAmount() == null || BigDecimal.ZERO.compareTo(easVoucherDTO.getCreditAmount()) == 0;
        } else if (easVoucherDTO.getCreditAmount() != null && BigDecimal.ZERO.compareTo(easVoucherDTO.getCreditAmount()) != 0) {
            return easVoucherDTO.getDebitAmount() == null || BigDecimal.ZERO.compareTo(easVoucherDTO.getDebitAmount()) == 0;
        }
        return Boolean.FALSE;
    }


    private String createMetaParams(boolean isCompress, List<EasVoucherDTO> voucherEntryList) {
        Map<String, Object> params = new HashMap();
        if (StringUtils.isNotEmpty(voucherEntryList.get(0).getIsStage()) && "1".equals(voucherEntryList.get(0).getIsStage())) {
            params.put("isSubmit", "false");
        } else {
            params.put("isSubmit", "true");
        }
        if (StringUtils.isNotEmpty(voucherEntryList.get(0).getAuditor())) {
            params.put("isAudit", "true");
        } else {
            params.put("isAudit", "false");
        }
        if (StringUtils.isNotEmpty(voucherEntryList.get(0).getPoster())) {
            params.put("isPost", "true");
        } else {
            params.put("isPost", "false");
        }
        params.put("isImpCashflow", "false");
        params.put("isCompress", String.valueOf(isCompress));
        params.put("isVoucherTypeIdField", "true");
        params.put("overwrite", "true");
        params.put("useGlobalCache", "true");
        params.put("clearGlobalCache", "true");
        params.put("extend", "true");
        String jsonParams = JSON.toJSONString(params);
        return jsonParams;
    }

    /**
     * 创建行参数
     */
    private List<List<String>> createRowsParams(List<EasVoucherDTO> voucherEntryList, String systemCode) {
        int colsNum = 58;
        int assistAbstractColsNum = 31;
        List<List<String>> rows = new ArrayList<List<String>>();
        //列名
        List<String> cols = new ArrayList<String>();
        cols.add("companyNumber");
        cols.add("voucherNumber");
        cols.add("periodYear");
        cols.add("periodNumber");
        cols.add("bookedDate");
        cols.add("bizDate");
        cols.add("sourceType");
        cols.add("voucherType");
        cols.add("description");
        cols.add("voucherAbstract");
        cols.add("isCheck");
        cols.add("creator");
        if (StringUtils.isNotEmpty(voucherEntryList.get(0).getAuditor())) {
            cols.add("auditor");
            colsNum++;
            assistAbstractColsNum++;
        }
        if (StringUtils.isNotEmpty(voucherEntryList.get(0).getPoster())) {
            cols.add("poster");
            colsNum++;
            assistAbstractColsNum++;
        }
        cols.add("importKey");
        cols.add("cashier");
        cols.add("sourceBillId");
        cols.add("sourceSys");
        cols.add("v.sourceSysNo");
        cols.add("v.sourceSysBillUrl");

        // 凭证分录行
        cols.add("entrySeq");
//        cols.add("profitCenterNumber");
        cols.add("accountNumber");
        cols.add("currencyNumber");
        cols.add("entryDC");
        cols.add("measurement");
        cols.add("cussent");
        cols.add("localRate");

        cols.add("originalAmount");
        cols.add("debitAmount");
        cols.add("creditAmount");
        cols.add("qty");
        cols.add("price");

        cols.add("asstSeq");
        cols.add("assistAbstract");
        cols.add("asstActType1");
        cols.add("asstActNumber1");
        cols.add("asstActName1");
        cols.add("asstActType2");
        cols.add("asstActNumber2");
        cols.add("asstActName2");
        cols.add("asstActType3");
        cols.add("asstActNumber3");
        cols.add("asstActName3");
        cols.add("asstActType4");
        cols.add("asstActNumber4");
        cols.add("asstActName4");
        cols.add("asstActType5");
        cols.add("asstActNumber5");
        cols.add("asstActName5");
        cols.add("asstActType6");
        cols.add("asstActNumber6");
        cols.add("asstActName6");
        cols.add("asstActType7");
        cols.add("asstActNumber7");
        cols.add("asstActName7");
        cols.add("asstActType8");
        cols.add("asstActNumber8");
        cols.add("asstActName8");

//        cols.add("itemFlag");
//        cols.add("oppAccountSeq");
//        cols.add("primaryItem");
//        cols.add("cashflowAmountOriginal");
//        cols.add("cashflowAmountLocal");
//        cols.add("cashflowAmountRpt");
//        cols.add("type");
        rows.add(cols);

        List<String> row = new ArrayList<String>();
        for (int k = 0; k < voucherEntryList.size(); k++) {
            EasVoucherDTO dto = voucherEntryList.get(k);
            dto.setLineNo(k + 1);
            row = new ArrayList<String>();
            row.add(dto.getCompanyNumber());
            row.add(dto.getVoucherNumber());
            row.add(dto.getPeriodYear() == null ? "" : String.valueOf(dto.getPeriodYear()));
            row.add(dto.getPeriodNumber() == null ? "" : String.valueOf(dto.getPeriodNumber()));
            row.add(dto.getBookedDate());
            row.add(dto.getBizDate());
            row.add(StringUtils.isEmpty(dto.getSourceType()) ? "" : dto.getSourceType());
            row.add(StringUtils.isEmpty(dto.getVoucherType()) ? "" : dto.getVoucherType());
            row.add(StringUtils.isEmpty(dto.getDescription()) ? "" : dto.getDescription());
            row.add(StringUtils.isEmpty(dto.getVoucherAbstract()) ? "" : dto.getVoucherAbstract());
            row.add(StringUtils.isEmpty(dto.getIsCheck()) ? "" : dto.getIsCheck());
            row.add(StringUtils.isEmpty(dto.getCreator()) ? "" : dto.getCreator());
            if (StringUtils.isNotEmpty(voucherEntryList.get(0).getAuditor())) {
                row.add(StringUtils.isEmpty(dto.getAuditor()) ? "" : dto.getAuditor());
            }
            if (StringUtils.isNotEmpty(voucherEntryList.get(0).getPoster())) {
                row.add(StringUtils.isEmpty(dto.getPoster()) ? "" : dto.getPoster());
            }
            if (Eas2SystemCodeSystemEnum.EAS1.getCode().equals(systemCode)) {
                row.add(dto.getFid());
            } else {
                row.add(dto.getImportKey());
            }
            if (StringUtils.isNotEmpty(dto.getCashier())) {
                row.add(StringUtils.isEmpty(dto.getCashier()) ? "" : dto.getCashier());
            } else {
                row.add("");
            }
            row.add(StringUtils.isEmpty(dto.getSourceBillId()) ? "" : dto.getSourceBillId());
            if (Eas2SystemCodeSystemEnum.EAS1.getCode().equals(systemCode)) {
                row.add(dto.getSourceSysNo());
            } else {
                row.add("");
            }
            row.add(StringUtils.isEmpty(dto.getSourceSysNo()) ? "" : dto.getSourceSysNo());
            row.add(StringUtils.isEmpty(dto.getSourceSysBillUrl()) ? "" : dto.getSourceSysBillUrl());

            // 凭证分录行
            row.add(dto.getEntrySeq() == null ? "" : String.valueOf(dto.getEntrySeq()));
//            row.add(dto.getProfitCenterNumber() == null ? "" : String.valueOf(dto.getProfitCenterNumber()));
            row.add(dto.getAccountNumber() == null ? "" : dto.getAccountNumber());
            row.add(dto.getCurrencyNumber() == null ? "" : dto.getCurrencyNumber());
//            row.add(String.valueOf(dto.getEntryDC()));
//            row.add(dto.getEntryDC() == null ? "" : String.valueOf(dto.getEntryDC()));
            if (dto.getDebitAmount() != null && BigDecimal.ZERO.compareTo(dto.getDebitAmount()) != 0) {
                row.add("1");
            } else {
                row.add("0");
            }

            row.add(dto.getMeasurement() == null ? "" : String.valueOf(dto.getMeasurement()));
            row.add(dto.getCussent() == null ? "" : String.valueOf(dto.getCussent()));
            row.add(dto.getLocalRate() == null ? "" : String.valueOf(dto.getLocalRate()));

            row.add(dto.getOriginalAmount() == null ? "0" : dto.getOriginalAmount().toString());
            row.add(dto.getDebitAmount() == null ? "0" : dto.getDebitAmount().toString());
            row.add(dto.getCreditAmount() == null ? "0" : dto.getCreditAmount().toString());
            row.add(dto.getQty() == null ? "0" : String.valueOf(dto.getQty()));
            row.add(dto.getPrice() == null ? "0" : String.valueOf(dto.getPrice()));

            row.add(dto.getAsstSeq() == null ? "" : String.valueOf(dto.getAsstSeq()));
            row.add(dto.getAssistAbstract() == null ? "" : String.valueOf(dto.getAssistAbstract()));
            Boolean isHasAssistFlag = Boolean.FALSE;
            if (StringUtils.isNotEmpty(dto.getAsstActNumber1())) {
                row.add(dto.getAsstActType1());
                row.add(dto.getAsstActNumber1());
                row.add(dto.getAsstActName1());
                isHasAssistFlag = Boolean.TRUE;
            }
            if (StringUtils.isNotEmpty(dto.getAsstActNumber2())) {
                row.add(dto.getAsstActType2());
                row.add(dto.getAsstActNumber2());
                row.add(StringUtils.isEmpty(dto.getAsstActName2()) ? "" : dto.getAsstActName2());
                isHasAssistFlag = Boolean.TRUE;
            }
            if (StringUtils.isNotEmpty(dto.getAsstActNumber3())) {
                row.add(dto.getAsstActType3());
                row.add(dto.getAsstActNumber3());
                row.add(dto.getAsstActName3());
                isHasAssistFlag = Boolean.TRUE;
            }
            if (StringUtils.isNotEmpty(dto.getAsstActNumber4())) {
                row.add(dto.getAsstActType4());
                row.add(dto.getAsstActNumber4());
                row.add(dto.getAsstActName4());
                isHasAssistFlag = Boolean.TRUE;
            }
            if (StringUtils.isNotEmpty(dto.getAsstActNumber5())) {
                row.add(dto.getAsstActType5());
                row.add(dto.getAsstActNumber5());
                row.add(dto.getAsstActName5());
                isHasAssistFlag = Boolean.TRUE;
            }
            if (StringUtils.isNotEmpty(dto.getAsstActNumber6())) {
                row.add(dto.getAsstActType6());
                row.add(dto.getAsstActNumber6());
                row.add(dto.getAsstActName6());
                isHasAssistFlag = Boolean.TRUE;
            }
            if (StringUtils.isNotEmpty(dto.getAsstActNumber7())) {
                row.add(dto.getAsstActType7());
                row.add(dto.getAsstActNumber7());
                row.add(dto.getAsstActName7());
                isHasAssistFlag = Boolean.TRUE;
            }
            if (StringUtils.isNotEmpty(dto.getAsstActNumber8())) {
                row.add(dto.getAsstActType8());
                row.add(dto.getAsstActNumber8());
                row.add(dto.getAsstActName8());
                isHasAssistFlag = Boolean.TRUE;
            }
            if (StringUtils.isNotEmpty(dto.getAsstActNumber9()) && StringUtils.isNotEmpty(dto.getAsstActName9())) {
                row.add(dto.getAsstActType9());
                row.add(dto.getAsstActNumber9());
                row.add(dto.getAsstActName9());
                isHasAssistFlag = Boolean.TRUE;
            }
            if (StringUtils.isNotEmpty(dto.getAsstActNumber10()) && StringUtils.isNotEmpty(dto.getAsstActName10())) {
                row.add(dto.getAsstActType10());
                row.add(dto.getAsstActNumber10());
                row.add(dto.getAsstActName10());
                isHasAssistFlag = Boolean.TRUE;
            }
            if (StringUtils.isNotEmpty(dto.getAsstActNumber11()) && StringUtils.isNotEmpty(dto.getAsstActName11())) {
                row.add(dto.getAsstActType11());
                row.add(dto.getAsstActNumber11());
                row.add(dto.getAsstActName11());
                isHasAssistFlag = Boolean.TRUE;
            }
            if (StringUtils.isNotEmpty(dto.getAsstActNumber12()) && StringUtils.isNotEmpty(dto.getAsstActName12())) {
                row.add(dto.getAsstActType12());
                row.add(dto.getAsstActNumber12());
                row.add(dto.getAsstActName12());
                isHasAssistFlag = Boolean.TRUE;
            }
            if (StringUtils.isNotEmpty(dto.getAsstActNumber13()) && StringUtils.isNotEmpty(dto.getAsstActName13())) {
                row.add(dto.getAsstActType13());
                row.add(dto.getAsstActNumber13());
                row.add(dto.getAsstActName13());
                isHasAssistFlag = Boolean.TRUE;
            }
            if (StringUtils.isNotEmpty(dto.getAsstActNumber14()) && StringUtils.isNotEmpty(dto.getAsstActName14())) {
                row.add(dto.getAsstActType14());
                row.add(dto.getAsstActNumber14());
                row.add(dto.getAsstActName14());
                isHasAssistFlag = Boolean.TRUE;
            }
            if (!isHasAssistFlag) {
                row.set(assistAbstractColsNum, "");
            }
            // 补齐row的title数量
            if (row.size() < colsNum) {
                int loopSize = colsNum - row.size();
                for (int i = 0; i < loopSize; i++) {
                    row.add("");
                }
            }
            rows.add(row);
        }
        return rows;
    }

    @Override
    public List<EasVoucherRespDTO> addVouchers(List<EasVoucherDTO> voucherEntryList) {

        //获取sessionID
        String sessionId = login();
        try {
            LocalDateTime now = LocalDateTime.now();

            //封装header
            Map<String, String> headers = new LinkedHashMap<>();
            headers.put("sessionId", sessionId);
            //封装参数
            Map<String, Object> param = new LinkedHashMap<>();
            param.put("system", ADD_VOUCHER_SYSTEM);
            param.put("type", ADD_VOUCHER_TYPE);
            param.put("json", JSONObject.toJSONString(voucherEntryList));

            // 保存入参
            kingdeeEASDataService.saveParams(voucherEntryList, now, JSONObject.toJSONString(voucherEntryList), Eas2SystemCodeSystemEnum.EAS1.getCode(), UUID.randomUUID().toString(), "");

            String response = EasWebServiceUtil.send(ADD_VOUCHER_URL, "add", headers, param);
            //解析xml,获取响应
            Node returnNode = XmlUtil.getNodeByXPath("//addReturn", XmlUtil.parseXml(response));
            if (returnNode == null) {
                throw new ServiceException(StrUtil.format("EAS返回数据为空，完整响应内容：{}", response));
            }
            String returnJsonStr = returnNode.getTextContent();
            List<EasVoucherRespDTO> returnRespDTO = JSONArray.parseArray(returnJsonStr, EasVoucherRespDTO.class);

            // 保存返回结果
            kingdeeEASDataService.saveResult(returnRespDTO);
            return returnRespDTO;
        } finally {
            logout(sessionId);
        }
    }


}
