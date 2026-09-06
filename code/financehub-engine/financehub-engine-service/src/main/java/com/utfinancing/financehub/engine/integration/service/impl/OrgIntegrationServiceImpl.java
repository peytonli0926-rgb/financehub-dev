package com.utfinancing.financehub.engine.integration.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.engine.integration.model.dto.*;
import com.utfinancing.financehub.engine.integration.service.IOrgIntegrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author : lixin
 * @Date : Create in 08/10/2023
 */
@Service
@Slf4j
public class OrgIntegrationServiceImpl implements IOrgIntegrationService {

    @Value("${hthx.org.url}")
    private String url;

    @Value("${hthx.org.tenant-code}")
    private String tenantCode;

    @Value("${hthx.org.tenant-secret}")
    private String tenantSecret;

    private static Map<String, String> ORG_SERVICE_HEADERS = new HashMap<>();
    private static final int TIME_OUT = 60000;

    @PostConstruct
    private void buildOrgServiceHeader(){
        ORG_SERVICE_HEADERS.put("tenant-code", tenantCode);
        ORG_SERVICE_HEADERS.put("tenant-secret", tenantSecret);
    }


    @Override
    public List<OrgDTO> queryAll() {
        String endPoint = "/org/queryAll";
        HttpResponse response = HttpUtil.createPost(url + endPoint).addHeaders(ORG_SERVICE_HEADERS).timeout(TIME_OUT).execute();
        String resultStr = response.body();
        log.info("orgService.org.queryAll, response:{}", resultStr);
        R<OrgQueryRespDTO> resultR = JSONObject.parseObject(resultStr, new TypeReference<R<OrgQueryRespDTO>>() {});
        if (!ObjectUtil.equal(resultR.getCode(), 200)){
            throw new ServiceException(resultR.getMsg());
        }
        if (resultR.getData() != null){
            return resultR.getData().getOrgList();
        }
        return null;
    }

    @Override
    public List<OrgDTO> queryFirst() {
        String endPoint = "/org/first";
        HttpResponse response = HttpUtil.createPost(url + endPoint).addHeaders(ORG_SERVICE_HEADERS).timeout(TIME_OUT).execute();
        String resultStr = response.body();
        log.info("orgService.org.first, response:{}", resultStr);
        R<OrgQueryRespDTO> resultR = JSONObject.parseObject(resultStr, new TypeReference<R<OrgQueryRespDTO>>() {});
        if (!ObjectUtil.equal(resultR.getCode(), 200)){
            throw new ServiceException(resultR.getMsg());
        }
        if (resultR.getData() != null){
            return resultR.getData().getOrgList();
        }
        return null;
    }

    @Override
    public List<OrgDTO> queryOrgList(OrgQueryDTO queryDTO) {
        String endPoint = "/org/query";

        OrgQueryReqDTO reqDTO = new OrgQueryReqDTO();
        reqDTO.setOrgList(ListUtil.toList(queryDTO));

        HttpResponse response = HttpUtil.createPost(url + endPoint).addHeaders(ORG_SERVICE_HEADERS).timeout(TIME_OUT).body(JSONObject.toJSONString(reqDTO)).execute();
        String resultStr = response.body();
        log.info("orgService.org.query, response:{}", resultStr);
        R<OrgQueryRespDTO> resultR = JSONObject.parseObject(resultStr, new TypeReference<R<OrgQueryRespDTO>>() {});
        if (!ObjectUtil.equal(resultR.getCode(), 200)){
            throw new ServiceException(resultR.getMsg());
        }
        if (resultR.getData() != null){
            return resultR.getData().getOrgList();
        }
        return null;
    }

    @Override
    public List<PositionDTO> queryOrgPositionList(String orgCode) {
        String endPoint = "/org/position/query";

        OrgPositionQueryReqDTO reqDTO = new OrgPositionQueryReqDTO();
        OrgPositionOrgReqDTO orgPositionOrgReqDTO = new OrgPositionOrgReqDTO();
        orgPositionOrgReqDTO.setOrgCode(orgCode);
        orgPositionOrgReqDTO.setIsNeedPositionEmp(0);//不看员工
        reqDTO.setOrgList(ListUtil.toList(orgPositionOrgReqDTO));

        HttpResponse response = HttpUtil.createPost(url + endPoint).addHeaders(ORG_SERVICE_HEADERS).timeout(TIME_OUT).body(JSONObject.toJSONString(reqDTO)).execute();
        String resultStr = response.body();
        log.info("orgService.org.position.query, response:{}", resultStr);
        R<OrgQueryRespDTO> resultR = JSONObject.parseObject(resultStr, new TypeReference<R<OrgQueryRespDTO>>() {});
        if (!ObjectUtil.equal(resultR.getCode(), 200)){
            throw new ServiceException(resultR.getMsg());
        }
        if (resultR.getData() != null && resultR.getData().getOrgList() !=null && resultR.getData().getOrgList().get(0) != null){
            return resultR.getData().getOrgList().get(0).getPositionList();
        }
        return null;
    }

    @Override
    public List<StaffDTO> queryOrgStaffList(String orgCode) {
        String endPoint = "/org/staff/query";

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("orgCodeList", ListUtil.toList(orgCode));

        HttpResponse response = HttpUtil.createPost(url + endPoint).addHeaders(ORG_SERVICE_HEADERS).timeout(TIME_OUT).body(JSONObject.toJSONString(paramMap)).execute();
        String resultStr = response.body();
        log.info("orgService.org.staff.query, response:{}", resultStr);
        R<OrgQueryRespDTO> resultR = JSONObject.parseObject(resultStr, new TypeReference<R<OrgQueryRespDTO>>() {});
        if (!ObjectUtil.equal(resultR.getCode(), 200)){
            throw new ServiceException(resultR.getMsg());
        }
        if (resultR.getData() != null && resultR.getData().getOrgList() !=null && resultR.getData().getOrgList().get(0) != null){
            return resultR.getData().getOrgList().get(0).getStaffList();
        }
        return null;
    }

    @Override
    public List<PositionDTO> queryPositionList(PositionQueryDTO queryDTO) {
        String endPoint = "/position/query";

        PositionQueryReqDTO reqDTO = new PositionQueryReqDTO();
        reqDTO.setPositionList(ListUtil.toList(queryDTO));

        HttpResponse response = HttpUtil.createPost(url + endPoint).addHeaders(ORG_SERVICE_HEADERS).timeout(TIME_OUT).body(JSONObject.toJSONString(reqDTO)).execute();
        String resultStr = response.body();
        log.info("orgService.position.query, response:{}", resultStr);
        R<PositionQueryRespDTO> resultR = JSONObject.parseObject(resultStr, new TypeReference<R<PositionQueryRespDTO>>() {});
        if (!ObjectUtil.equal(resultR.getCode(), 200)){
            throw new ServiceException(resultR.getMsg());
        }
        if (resultR.getData() != null && resultR.getData().getPositionList() !=null){
            return resultR.getData().getPositionList();
        }
        return null;
    }

    @Override
    public List<StaffDTO> queryStaffList(StaffQueryDTO queryDTO) {
        String endPoint = "/staff/query";

        StaffQueryReqDTO reqDTO = new StaffQueryReqDTO();
        reqDTO.setStaffList(ListUtil.toList(queryDTO));

        HttpResponse response = HttpUtil.createPost(url + endPoint).addHeaders(ORG_SERVICE_HEADERS).timeout(TIME_OUT).body(JSONObject.toJSONString(reqDTO)).execute();
        String resultStr = response.body();
        log.info("orgService.staff.query, response:{}", resultStr);
        R<StaffQueryRespDTO> resultR = JSONObject.parseObject(resultStr, new TypeReference<R<StaffQueryRespDTO>>() {});
        if (!ObjectUtil.equal(resultR.getCode(), 200)){
            throw new ServiceException(resultR.getMsg());
        }
        if (resultR.getData() != null && resultR.getData().getStaffList() !=null){
            return resultR.getData().getStaffList();
        }
        return null;
    }


}
