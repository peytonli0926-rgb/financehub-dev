package com.utfinancing.financehub.engine.integration.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.engine.integration.model.eas.dto.EasVoucherDTO;
import com.utfinancing.financehub.engine.integration.model.eas.dto.EasVoucherRespDTO;
import com.utfinancing.financehub.engine.integration.service.IEasIntegrationService;
import com.utfinancing.financehub.engine.integration.util.EasWebServiceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;

import java.util.*;

/**
 * @Author : lixin
 * @Date : Create in 30/10/2023
 */
@Service
@Slf4j
public class EasIntegrationServiceImpl implements IEasIntegrationService {

    @Value("${eas.service.login.url}")
    private String LOGIN_URL;

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
        if (sessionNode == null){
            throw new ServiceException("获取金蝶sessionId失败");
        }
        String sessionId = sessionNode.getTextContent();
        if (StrUtil.isBlank(sessionId)){
            throw new ServiceException("获取金蝶sessionId失败, sessionId为空");
        }
        return sessionId;
    }

    @Override
    public List<EasVoucherRespDTO> addVouchers(List<EasVoucherDTO> voucherEntryList) {
        //获取sessionID
        String sessionId = login();
        //封装header
        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("sessionId", sessionId);
        //封装参数
        Map<String, Object> param = new LinkedHashMap<>();
        param.put("system", ADD_VOUCHER_SYSTEM);
        param.put("type", ADD_VOUCHER_TYPE);
        param.put("json", JSONObject.toJSONString(voucherEntryList));
        String response = EasWebServiceUtil.send(ADD_VOUCHER_URL, "add", headers, param);
        //解析xml,获取响应
        Node returnNode = XmlUtil.getNodeByXPath("//addReturn", XmlUtil.parseXml(response));
        if (returnNode == null){
            throw new ServiceException(StrUtil.format("EAS返回数据为空，完整响应内容：{}", response));
        }
        String returnJsonStr = returnNode.getTextContent();
        List<EasVoucherRespDTO> returnRespDTO = JSONArray.parseArray(returnJsonStr, EasVoucherRespDTO.class);
        return returnRespDTO;
    }


}
