package com.utfinancing.financehub.gateway.filter;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.utfinancing.financehub.admin.api.model.LoginUser;
import com.utfinancing.financehub.common.core.constant.CacheConstants;
import com.utfinancing.financehub.common.core.constant.HttpStatus;
import com.utfinancing.financehub.common.core.constant.SecurityConstants;
import com.utfinancing.financehub.common.core.constant.TokenConstants;
import com.utfinancing.financehub.common.core.utils.ServletUtils;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.common.redis.service.RedisService;
import com.utfinancing.financehub.gateway.config.properties.IgnoreWhiteProperties;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 网关鉴权
 *
 * @author ruoyi
 */
@Component
public class AuthFilter implements GlobalFilter, Ordered {
    private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);

    @Value("${financehub.gateway.adminToken}")
    public String ADMIN_TOKEN;
    @Value("${financehub.gateway.adminToken1}")
    public String ADMIN_TOKEN1;

    private static final String CONTENT_TYPE = "application/json";
    // 排除过滤的 uri 地址，nacos自行添加
    @Autowired
    private IgnoreWhiteProperties ignoreWhite;

    @Autowired
    private RedisService redisService;

    @Value("${sso.client.access-token-url}")
    private String accessTokenUrl;
    @Value("${sso.client.clientId}")
    private String clientId;
    @Value("${sso.client.clientSecret}")
    private String clientSecret;


//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        ServerHttpRequest request = exchange.getRequest();
//        ServerHttpRequest.Builder mutate = request.mutate();
//
//        String url = request.getURI().getPath();
//        // 跳过不需要验证的路径
//        if (StringUtils.matches(url, ignoreWhite.getWhites())) {
//            return chain.filter(exchange);
//        }
//        String token = getToken(request);
//        if (StringUtils.isEmpty(token)) {
//            return unauthorizedResponse(exchange, "令牌不能为空");
//        }
//        Claims claims = JwtUtils.parseToken(token);//修改为???
//        if (claims == null) {
//            return unauthorizedResponse(exchange, "令牌已过期或验证不正确！");
//        }
//        String userkey = JwtUtils.getUserKey(claims);
//        boolean islogin = redisService.hasKey(getTokenKey(userkey));
//        if (!islogin) {
//            return unauthorizedResponse(exchange, "登录状态已过期");
//        }
//        String userid = JwtUtils.getUserId(claims);
//        String username = JwtUtils.getUserName(claims);
//        if (StringUtils.isEmpty(userid) || StringUtils.isEmpty(username)) {
//            return unauthorizedResponse(exchange, "令牌验证失败");
//        }
//
//        // 设置用户信息到请求
//        addHeader(mutate, SecurityConstants.USER_KEY, userkey);
//        addHeader(mutate, SecurityConstants.DETAILS_USER_ID, userid);
//        addHeader(mutate, SecurityConstants.DETAILS_USERNAME, username);
//        // 内部请求来源参数清除
//        removeHeader(mutate, SecurityConstants.FROM_SOURCE);
//        return chain.filter(exchange.mutate().request(mutate.build()).build());
//    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest.Builder mutate = request.mutate();
        log.info("requestInfo:{}", JSON.toJSONString(request));

        String url = request.getURI().getPath();
        // 跳过不需要验证的路径
        if (StringUtils.matches(url, ignoreWhite.getWhites())) {
            return chain.filter(exchange);
        }
        String token = getToken(request);
        if (StringUtils.isEmpty(token)) {
            return unauthorizedResponse(exchange, "令牌不能为空");
        }
        if (!ADMIN_TOKEN.equals(token) && !ADMIN_TOKEN1.equals(token)) {

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", token);
            headers.add("client_id", this.clientId);
            headers.add("client_secret", this.clientSecret);
            HttpRequest body = HttpUtil.createGet(accessTokenUrl).header(headers);

            HttpResponse execute = body.execute();
            JSONObject jsonObject = JSON.parseObject(execute.body());
            log.info("accessToken response:{}", jsonObject.toJSONString());
            Integer code = jsonObject.getInteger("code");
            if (code != 200) {
                return unauthorizedResponse(exchange, "令牌已过期或验证不正确！");
            }
        }

        boolean islogin = redisService.hasKey(getTokenKey(token));
        if (!islogin) {
            return unauthorizedResponse(exchange, "登录状态已过期");
        }
        LoginUser loginUser = redisService.getCacheObject(getTokenKey(token));
        String userid = loginUser.getStaffCode();
        String username = loginUser.getStaffName();
        if (StringUtils.isEmpty(userid) || StringUtils.isEmpty(username)) {
            return unauthorizedResponse(exchange, "令牌验证失败");
        }

        // 设置用户信息到请求
        addHeader(mutate, SecurityConstants.USER_KEY, token);
        addHeader(mutate, SecurityConstants.DETAILS_USER_ID, userid);
        addHeader(mutate, SecurityConstants.DETAILS_USERNAME, username);
        // 内部请求来源参数清除
        removeHeader(mutate, SecurityConstants.FROM_SOURCE);

        ServerHttpResponseDecorator decoratedResponse = recordResponseLog(exchange);
        return chain.filter(exchange.mutate().request(mutate.build()).response(decoratedResponse).build());
    }

    /**
     * 记录响应日志
     * 通过 DataBufferFactory 解决响应体分段传输问题。
     */
    private ServerHttpResponseDecorator recordResponseLog(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        DataBufferFactory bufferFactory = response.bufferFactory();

        return new ServerHttpResponseDecorator(response) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {

                    // 获取响应类型，如果是 json 就打印
                    String originalResponseContentType = exchange.getAttribute(ServerWebExchangeUtils.ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR);

                    if (Objects.equals(this.getStatusCode(), org.springframework.http.HttpStatus.OK)
                            && org.apache.commons.lang3.StringUtils.isNotBlank(originalResponseContentType)
                            && originalResponseContentType.contains(CONTENT_TYPE)) {

                        Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                        return super.writeWith(fluxBody.buffer().map(dataBuffers -> {

                            // 合并多个流集合，解决返回体分段传输
                            DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                            DataBuffer join = dataBufferFactory.join(dataBuffers);
                            byte[] content = new byte[join.readableByteCount()];
                            join.read(content);

                            // 释放掉内存
                            DataBufferUtils.release(join);
                            String responseResult = new String(content, StandardCharsets.UTF_8);

                            log.info("responseResult:{}", responseResult);

                            return bufferFactory.wrap(content);
                        }));
                    }
                }
                // if body is not a flux. never got there.
                return super.writeWith(body);
            }
        };
    }

    private void addHeader(ServerHttpRequest.Builder mutate, String name, Object value) {
        if (value == null) {
            return;
        }
        String valueStr = value.toString();
        String valueEncode = ServletUtils.urlEncode(valueStr);
        mutate.header(name, valueEncode);
    }

    private void removeHeader(ServerHttpRequest.Builder mutate, String name) {
        mutate.headers(httpHeaders -> httpHeaders.remove(name)).build();
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String msg) {
        log.error("[鉴权异常处理]请求路径:{}", exchange.getRequest().getPath());
        return ServletUtils.webFluxResponseWriter(exchange.getResponse(), msg, HttpStatus.UNAUTHORIZED);
    }

    /**
     * 获取缓存key
     */
    private String getTokenKey(String token) {
        return CacheConstants.LOGIN_TOKEN_KEY + token;
    }

    /**
     * 获取请求token
     */
    private String getToken(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst(TokenConstants.AUTHENTICATION);
        // 如果前端设置了令牌前缀，则裁剪掉前缀
        if (StringUtils.isNotEmpty(token) && CharSequenceUtil.startWith(token, TokenConstants.PREFIX)) {
            token = CharSequenceUtil.replaceFirst(token, TokenConstants.PREFIX, StringUtils.EMPTY);
        }
        return token;
    }

    @Override
    public int getOrder() {
        return -200;
    }
}