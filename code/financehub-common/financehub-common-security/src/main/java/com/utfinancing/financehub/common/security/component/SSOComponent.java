package com.utfinancing.financehub.common.security.component;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.utfinancing.financehub.admin.api.RemoteOrgService;
import com.utfinancing.financehub.admin.api.model.AccessToken;
import com.utfinancing.financehub.admin.api.model.LoginUser;
import com.utfinancing.financehub.common.core.dto.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class SSOComponent {


    @Value("${sso.client.verify-token-url}")
    private String verifyTokenUrl;
    @Value("${sso.client.refresh-token-url}")
    private String refreshTokenUrl;
    @Value("${sso.client.access-token-url}")
    private String accessTokenUrl;
    @Value("${sso.client.logout-url}")
    private String logoutUrl;
    @Value("${sso.client.clientId}")
    private String clientId;
    @Value("${sso.client.clientSecret}")
    private String clientSecret;

    @Resource
    private RemoteOrgService remoteOrgService;


    public R<LoginUser> verifyToken(String token) {
        HttpRequest body = HttpUtil.createPost(verifyTokenUrl).header("Authorization", token);

        HttpResponse execute = body.execute();
        JSONObject jsonObject = JSON.parseObject(execute.body());
        log.info("verifyToken response:{}", jsonObject.toJSONString());
        Integer code = jsonObject.getInteger("code");
        if (code == 200) {
            LoginUser users = jsonObject.getObject("data",LoginUser.class);
            log.info("users:{}", JSON.toJSONString(users));
            log.info("调用远程接口remoteOrgService.getOrgByUserCode获取数据权限参数：{}", users.getStaffCode());
            R<List<String>> orgListR = remoteOrgService.getOrgByUserCode(users.getStaffCode());
            log.info("调用远程接口remoteOrgService.getOrgByUserCode获取数据权限返回值：{}", orgListR);
            if (null != orgListR && CollectionUtil.isNotEmpty(orgListR.getData())) {
                users.setOrgIds(orgListR.getData());
            }
            log.info("含有数据权限users:{}", JSON.toJSONString(users));
            return R.ok(users);
        } else {
            return R.fail("认证失败");
        }
    }

    public R logout(String token) {
        HttpRequest body = HttpUtil.createPost(logoutUrl).header("Authorization", token);


        HttpResponse execute = body.execute();
        JSONObject jsonObject = JSON.parseObject(execute.body());
        log.info("logout response:{}", jsonObject.toJSONString());
        Integer code = jsonObject.getInteger("code");
        if (code == 200) {
            return R.ok();
        } else {
            return R.fail("认证失败");
        }
    }

    public R<AccessToken> accessToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        headers.add("client_id", this.clientId);
        headers.add("client_secret", this.clientSecret);
        HttpRequest body = HttpUtil.createGet(accessTokenUrl).header(headers);

        HttpResponse execute = body.execute();
        JSONObject jsonObject = JSON.parseObject(execute.body());
        log.info("accessToken response:{}", jsonObject.toJSONString());
        Integer code = jsonObject.getInteger("code");
        if (code == 200) {
            AccessToken users = jsonObject.getObject("data",AccessToken.class);
            log.info("accessToken:{}", JSON.toJSONString(users));
            return R.ok(users);
        } else {
            return R.fail("认证失败");
        }
    }

    public R<AccessToken> refreshToken(String token, String refreshToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        Map<String, String> postParameters = new HashMap<>();
        postParameters.put("refresh_token", refreshToken);
        postParameters.put("grant_type", "refresh_token");
        postParameters.put("client_id", this.clientId);
        postParameters.put("client_secret", this.clientSecret);
        HttpRequest body = HttpUtil.createPost(refreshTokenUrl).header(headers).body(JSON.toJSONString(postParameters));

        HttpResponse execute = body.execute();
        JSONObject jsonObject = JSON.parseObject(execute.body());
        log.info("refreshToken response:{}", jsonObject.toJSONString());
        if (execute.getStatus() == 200) {
            AccessToken users = jsonObject.toJavaObject(AccessToken.class);
            log.info("refreshToken:{}", JSON.toJSONString(users));
            return R.ok(users);
        } else {
            return R.fail("认证失败");
        }
    }
}
