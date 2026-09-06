package com.utfinancing.financehub.common.security.component;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.utfinancing.financehub.common.security.config.PermissionConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class CommonPermissionComponent {


    @Autowired
    private PermissionConfig permissionConfig;

    public JSONObject getWebPermission(String token) {
        String url = permissionConfig.getBpmDomain() + permissionConfig.getBpmPermissionUrlWeb() + token;
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("systemCode", permissionConfig.getBpmSyscode());
        requestMap.put("oriSystemCode", permissionConfig.getBpmSyscode());
        requestMap.put("token", permissionConfig.getBpmToken());
        log.info("getWebPermission request:{}", JSON.toJSONString(requestMap));
        HttpRequest body = HttpUtil.createPost(url).body(JSON.toJSONString(requestMap));
        HttpResponse execute = body.execute();
        JSONObject jsonObject = JSON.parseObject(execute.body());
        log.info("getWebPermission response:{}", jsonObject.toJSONString());
        Integer code = jsonObject.getInteger("code");
        if (code == 102200000) {
            return jsonObject.getJSONObject("data");
        }
        return null;
    }

    /**
     * @param userId      登陆人
     * @param token       sso token
     * @param verifyUrl   接口url
     * @param requestType 请求类型
     * @return
     */
    public boolean haveInterfacePermission(String userId, String token, String verifyUrl, String requestType) {
        String url = permissionConfig.getBpmDomain() + permissionConfig.getBpmPermissionUrlVerify() + token;
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("systemCode", permissionConfig.getBpmSyscode());
        requestMap.put("oriSystemCode", permissionConfig.getBpmSyscode());
        requestMap.put("userId", userId);
        requestMap.put("url", verifyUrl);
        requestMap.put("requestType", requestType);
        requestMap.put("token", permissionConfig.getBpmToken());
        log.info("haveInterfacePermission request:{}", JSON.toJSONString(requestMap));
        HttpRequest body = HttpUtil.createPost(url).body(JSON.toJSONString(requestMap));
        HttpResponse execute = body.execute();
        JSONObject jsonObject = JSON.parseObject(execute.body());
        log.info("haveInterfacePermission response:{}", jsonObject.toJSONString());
        return jsonObject.getBoolean("success");

    }

    public JSONObject getInterfacePermission(String userId, String token) {
        String url = permissionConfig.getBpmDomain() + permissionConfig.getBpmPermissionUrlWeb() + token;
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("systemCode", permissionConfig.getBpmSyscode());
        requestMap.put("oriSystemCode", permissionConfig.getBpmSyscode());
        requestMap.put("userId", userId);
        HttpRequest body = HttpUtil.createPost(url).body(JSON.toJSONString(requestMap));
        HttpResponse execute = body.execute();
        JSONObject jsonObject = JSON.parseObject(execute.body());
        Integer code = jsonObject.getInteger("code");
        if (code == 102200000) {
            return jsonObject.getJSONObject("data");
        }
        return null;
    }
}
