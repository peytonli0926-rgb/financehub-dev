package com.utfinancing.financehub.auth.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson2.JSONObject;
import com.utfinancing.financehub.admin.api.model.AccessToken;
import com.utfinancing.financehub.admin.api.model.LoginUser;
import com.utfinancing.financehub.auth.form.LoginBody;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.common.security.auth.AuthUtil;
import com.utfinancing.financehub.common.security.component.CommonPermissionComponent;
import com.utfinancing.financehub.common.security.component.SSOComponent;
import com.utfinancing.financehub.common.security.service.TokenService;
import com.utfinancing.financehub.common.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * sso token 控制
 *
 * @author hzhao
 */
@RestController
@RequestMapping("/sso")
@Slf4j
public class SSOController {

    @Value("${financehub.auth.token}")
    private String ADMINTOKEN;

    @Value("${financehub.auth.adminCode}")
    public String ADMIN_CODE;

    @Value("${financehub.auth.adminName}")
    public String ADMIN_NAME;

    @Value("${financehub.auth.adminPass}")
    public String AMDIN_PASS;

    @Value("${financehub.auth1.token}")
    private String ADMINTOKEN1;

    @Value("${financehub.auth1.adminCode}")
    public String ADMIN_CODE1;

    @Value("${financehub.auth1.adminName}")
    public String ADMIN_NAME1;

    @Value("${financehub.auth1.adminPass}")
    public String AMDIN_PASS1;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private CommonPermissionComponent permissionComponent;
    @Autowired
    private SSOComponent ssoComponent;

    @PostMapping("login")
    public R<?> login(@RequestBody LoginBody form) {
        // 用户登录
        String username = form.getUsername();
        String password = form.getPassword();
        if (StringUtils.isAnyBlank(username, password)) {
            throw new ServiceException("用户/密码必须填写");
        }
        if (!StringUtils.equals(username, ADMIN_CODE) && !StringUtils.equals(username, ADMIN_CODE1)) {
            throw new ServiceException("用户名错误");
        }
        if (!StringUtils.equals(password, AMDIN_PASS) && !StringUtils.equals(password, AMDIN_PASS1)) {
            throw new ServiceException("密码错误");
        }
        // 获取登录token
        if (StringUtils.equals(username, ADMIN_CODE)) {
            return R.ok(ADMINTOKEN);
        } else if (StringUtils.equals(username, ADMIN_CODE1)) {
            return R.ok(ADMINTOKEN1);
        }
        return R.ok(ADMINTOKEN);
    }

    @GetMapping("/getUserInfo")
    public R<LoginUser> getUserInfo(HttpServletRequest request) {

        String token = SecurityUtils.getToken(request);
        if (StringUtils.equals(ADMINTOKEN, token)) {
            LoginUser users = new LoginUser();
            users.setAccess_token(ADMINTOKEN);
            users.setExpires_in(5000000);
            users.setStaffCode(ADMIN_CODE);
            users.setStaffName(ADMIN_NAME);
            users.setUsername(ADMIN_CODE);
            //存到redis中
            tokenService.saveTokenAndUserInfoToRedis(token, users, users.getExpires_in());
            return R.ok(users);
        }
        if (StringUtils.equals(ADMINTOKEN1, token)) {
            LoginUser users = new LoginUser();
            users.setAccess_token(ADMINTOKEN1);
            users.setExpires_in(5000000);
            users.setStaffCode(ADMIN_CODE1);
            users.setStaffName(ADMIN_NAME1);
            users.setUsername(ADMIN_CODE1);
            //存到redis中
            tokenService.saveTokenAndUserInfoToRedis(token, users, users.getExpires_in());
            return R.ok(users);
        }
        log.info("token参数：{}",token);
        R<LoginUser> userR = ssoComponent.verifyToken(token);
        if (R.SUCCESS == userR.getCode()) {
            LoginUser users = userR.getData();
            //默认失效时间,
            long expires_in = 1800L;
            R<AccessToken> accessTokenR = ssoComponent.accessToken(token);
            if (R.SUCCESS == accessTokenR.getCode()) {
                AccessToken accessToken = accessTokenR.getData();
                expires_in = accessToken.getExpires_in();
                BeanUtil.copyProperties(accessToken, users);
            }
            //存到redis中
            tokenService.saveTokenAndUserInfoToRedis(token, users, expires_in);
        }
        return userR;
    }

    @PostMapping("/logout")
    public R logout(HttpServletRequest request) {
        String token = SecurityUtils.getToken(request);
        if (StringUtils.isEmpty(token)) {
            return R.fail();
        }
        // 从SSO登出
        if (!StringUtils.equals(ADMINTOKEN, token) && !StringUtils.equals(ADMINTOKEN1, token)) {
            R logoutR = ssoComponent.logout(token);
        }
        // 财务中台本地处理
        LoginUser ssoLoginUser = tokenService.getSSOLoginUser(token);
        // 删除用户缓存记录
        AuthUtil.logoutBySSOToken(token);
        // 记录用户退出日志
//        sysLoginService.logout(ssoLoginUser.getStaffName());

        return R.ok();
    }

    @GetMapping("/getAuthInfo")
    @ResponseBody
    public R getAuthInfo(HttpServletRequest request) throws FileNotFoundException {
        String token = SecurityUtils.getToken(request);

        if (ADMINTOKEN.equals(token)) {
            InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("permission-all.txt");
            String fileContent = IoUtil.readUtf8(resourceAsStream);
            JSONObject jsonObject = JSONObject.parseObject(fileContent);
            return R.ok(jsonObject);
        }

        if (ADMINTOKEN1.equals(token)) {
            InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("permission-moji.txt");
            String fileContent = IoUtil.readUtf8(resourceAsStream);
            JSONObject jsonObject = JSONObject.parseObject(fileContent);
            return R.ok(jsonObject);
        }
        R<LoginUser> userR = ssoComponent.verifyToken(token);
        if (R.SUCCESS == userR.getCode()) {
            JSONObject webPermission = permissionComponent.getWebPermission(token);
            return R.ok(webPermission);
        } else {
            return R.fail();
        }
    }

}
