package com.utfinancing.financehub.common.security.interceptor;

import cn.hutool.core.lang.UUID;
import com.utfinancing.financehub.common.core.constant.SecurityConstants;
import com.utfinancing.financehub.common.core.context.SecurityContextHolder;
import com.utfinancing.financehub.common.core.utils.ServletUtils;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.common.security.auth.AuthUtil;
import com.utfinancing.financehub.common.security.utils.SecurityUtils;
import com.utfinancing.financehub.admin.api.model.LoginUser;
import org.slf4j.MDC;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义请求头拦截器，将Header数据封装到线程变量中方便获取
 * 注意：此拦截器会同时验证当前用户有效期自动刷新有效期
 *
 * @author ruoyi
 */
public class HeaderInterceptor implements AsyncHandlerInterceptor {
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        if (!(handler instanceof HandlerMethod)) {
//            return true;
//        }
//
//        SecurityContextHolder.setUserId(ServletUtils.getHeader(request, SecurityConstants.DETAILS_USER_ID));
//        SecurityContextHolder.setUserName(ServletUtils.getHeader(request, SecurityConstants.DETAILS_USERNAME));
//        SecurityContextHolder.setUserKey(ServletUtils.getHeader(request, SecurityConstants.USER_KEY));
//
//        String token = SecurityUtils.getToken();
//        if (StringUtils.isNotEmpty(token)) {
//            LoginUser loginUser = AuthUtil.getLoginUser(token);
//            if (StringUtils.isNotNull(loginUser)) {
//                AuthUtil.verifyLoginUserExpire(loginUser);
//                SecurityContextHolder.set(SecurityConstants.LOGIN_USER, loginUser);
//            }
//        }
//        return true;
//    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        //添加全局ID
        MDC.put("PID", UUID.fastUUID().toString(true));
        SecurityContextHolder.setUserId(ServletUtils.getHeader(request, SecurityConstants.DETAILS_USER_ID));
        SecurityContextHolder.setUserName(ServletUtils.getHeader(request, SecurityConstants.DETAILS_USERNAME));
        SecurityContextHolder.setUserKey(ServletUtils.getHeader(request, SecurityConstants.USER_KEY));

        String token = SecurityUtils.getToken();//取request中的token
        if (StringUtils.isNotEmpty(token)) {
            LoginUser users = AuthUtil.getSSOLoginUser(token);
            if (StringUtils.isNotNull(users)) {
                AuthUtil.verifySSOLoginUserExpire(users);
                SecurityContextHolder.set(SecurityConstants.LOGIN_USER, users);
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        SecurityContextHolder.remove();
    }
}
