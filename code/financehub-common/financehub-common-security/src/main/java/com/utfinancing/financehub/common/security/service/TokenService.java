package com.utfinancing.financehub.common.security.service;

import com.utfinancing.financehub.admin.api.model.AccessToken;
import com.utfinancing.financehub.common.core.constant.CacheConstants;
import com.utfinancing.financehub.common.core.constant.SecurityConstants;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.utils.JwtUtils;
import com.utfinancing.financehub.common.core.utils.ServletUtils;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.common.core.utils.ip.IpUtils;
import com.utfinancing.financehub.common.core.utils.uuid.IdUtils;
import com.utfinancing.financehub.common.redis.service.RedisService;
import com.utfinancing.financehub.common.security.component.SSOComponent;
import com.utfinancing.financehub.common.security.utils.SecurityUtils;
import com.utfinancing.financehub.admin.api.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * token验证处理
 *
 * @author ruoyi
 */
@Component
public class TokenService {
    @Autowired
    private RedisService redisService;

    @Autowired
    private SSOComponent ssoComponent;

    public static final long DEFAULT_REDRESH_TIME = 1800;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private final static long expireTime = CacheConstants.EXPIRATION;

    private final static String ACCESS_TOKEN = CacheConstants.LOGIN_TOKEN_KEY;

    private final static Long MILLIS_MINUTE_TEN = CacheConstants.REFRESH_TIME * MILLIS_MINUTE;

    /**
     * 创建令牌
     */
    public Map<String, Object> createToken(LoginUser loginUser) {
        String token = IdUtils.fastUUID();
        Long userId = loginUser.getSysUser().getUserId();
        String userName = loginUser.getSysUser().getUserName();
        loginUser.setToken(token);
        loginUser.setUserid(userId);
        loginUser.setUsername(userName);
        loginUser.setIpaddr(IpUtils.getIpAddr());
        refreshToken(loginUser);

        // Jwt存储信息
        Map<String, Object> claimsMap = new HashMap<String, Object>();
        claimsMap.put(SecurityConstants.USER_KEY, token);
        claimsMap.put(SecurityConstants.DETAILS_USER_ID, userId);
        claimsMap.put(SecurityConstants.DETAILS_USERNAME, userName);

        // 接口返回信息
        Map<String, Object> rspMap = new HashMap<String, Object>();
        rspMap.put("access_token", JwtUtils.createToken(claimsMap));
        rspMap.put("expires_in", expireTime);
        return rspMap;
    }

    public void saveTokenAndUserInfoToRedis(String token, LoginUser users, Long expires_in) {
        redisService.setCacheObject(getTokenKey(token),users, expires_in, TimeUnit.SECONDS);
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser() {
        return getLoginUser(ServletUtils.getRequest());
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = SecurityUtils.getToken(request);
        return getLoginUser(token);
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(String token) {
        LoginUser user = null;
        try {
            if (StringUtils.isNotEmpty(token)) {
                String userkey = JwtUtils.getUserKey(token);
                user = redisService.getCacheObject(getTokenKey(userkey));
                return user;
            }
        } catch (Exception e) {
        }
        return user;
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getSSOLoginUser(String token) {
        LoginUser user = null;
        try {
            if (StringUtils.isNotEmpty(token)) {
                user = redisService.getCacheObject(getTokenKey(token));
                return user;
            }
        } catch (Exception e) {
        }
        return user;
    }

    /**
     * 设置用户身份信息
     */
    public void setLoginUser(LoginUser loginUser) {
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNotEmpty(loginUser.getToken())) {
            refreshToken(loginUser);
        }
    }

    /**
     * 删除用户缓存信息
     */
    public void delLoginUser(String token) {
        if (StringUtils.isNotEmpty(token)) {
            String userkey = JwtUtils.getUserKey(token);
            redisService.deleteObject(getTokenKey(userkey));
        }
    }

    /**
     * 删除用户缓存信息
     */
    public void delSSOLoginUser(String token) {
        if (StringUtils.isNotEmpty(token)) {
            redisService.deleteObject(getTokenKey(token));
        }
    }

    /**
     * 验证令牌有效期，相差不足120分钟，自动刷新缓存
     *
     * @param loginUser
     */
    public void verifyToken(LoginUser loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN) {
            refreshToken(loginUser);
        }
    }

    /**
     * 验证令牌有效期，相差不足30分钟，自动刷新缓存
     *
     * @param loginUser
     */
    public void verifySSOToken(LoginUser loginUser) {
        //这里应该取token对应在redis里剩余的时间
        String token = loginUser.getAccess_token();
        long expire = redisService.getExpire(getTokenKey(token));
        if (expire <= DEFAULT_REDRESH_TIME) {
            refreshSSOToken(loginUser);
        }
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUser.getToken());
        redisService.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshSSOToken(LoginUser loginUser) {
        // 调用客户refresh接口
        R<AccessToken> refreshTokenR = ssoComponent.refreshToken(loginUser.getAccess_token(), loginUser.getRefresh_token());
        // 成功后,refresh本地缓存
        if (R.SUCCESS == refreshTokenR.getCode()) {
            AccessToken refreshToken = refreshTokenR.getData();
            String userKey = getTokenKey(loginUser.getAccess_token());
            redisService.setCacheObject(userKey, loginUser, refreshToken.getExpires_in(), TimeUnit.SECONDS);
        }
    }

    private String getTokenKey(String token) {
        return ACCESS_TOKEN + token;
    }
}