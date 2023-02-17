package con.gao.shop.filter;

import cn.hutool.core.text.AntPathMatcher;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.gao.shop.constant.TokenAuthorityServerConstant;
import com.gao.shop.enumclass.ExceptionEnum;
import com.gao.shop.result.ResultResponse;
import con.gao.shop.util.JwtUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;

/**
 * @author zhangran
 * @date 2021/7/13
 * <p>
 * 全局拦截器，作用所有的微服务
 * <p>
 * 1. 对请求的API调用过滤，记录接口的请求时间，方便日志审计、告警、分析等运维操作 2. 后期可以扩展对接其他日志系统
 * <p>
 */
@Log4j2
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {

    private static final String START_TIME = "startTime";
    /**
     *
     */
    private static final String X_REAL_IP = "X-Real-IP";
    private static final String AUTHORIZE_TOKEN = "token";
    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取请求
        ServerHttpRequest request = exchange.getRequest();
        //获取响应
        ServerHttpResponse response = exchange.getResponse();
        //获取请求接口路径
        String path = request.getURI().getPath();
        //获取命中的服务名称
        Route route = (Route) exchange.getAttributes().get(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        URI uri = route.getUri();
        String authority = uri.getAuthority();
        //判断命中服务是否需要Token权限
        if (authority.equals(TokenAuthorityServerConstant.ADMIN)) {
            String loginPath = "/**/login";
            if (antPathMatcher.match(loginPath, path)) {
                //登录接口直接放行
                return chain.filter(exchange);
            }
            //获取请求头中的Token
            HttpHeaders headers = request.getHeaders();
            String token = headers.getFirst(AUTHORIZE_TOKEN);
            //判断Token是否为空
            if (StrUtil.isEmpty(token)) {
                response.setStatusCode(HttpStatus.OK);
                byte[] bytes = JSON.toJSONString(ResultResponse.error(ExceptionEnum.TOKEN_IS_NULL)).getBytes(StandardCharsets.UTF_8);
                DataBuffer buffer = response.bufferFactory().wrap(bytes);
                return response.writeWith(Mono.just(buffer));
            }
            //验证Token
            try {
                JwtUtil.parseJWT(token);
            } catch (Exception e) {
                log.info("Token解析失败：", e);
                response.setStatusCode(HttpStatus.OK);
                byte[] bytes = JSON.toJSONString(ResultResponse.error(ExceptionEnum.TOKEN_ANALYSIS_ERROR)).getBytes(StandardCharsets.UTF_8);
                DataBuffer buffer = response.bufferFactory().wrap(bytes);
                return response.writeWith(Mono.just(buffer));
            }
        }
//        exchange.getAttributes().put(START_TIME, System.currentTimeMillis());
        return chain.filter(exchange);
//        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
//            Long startTime = exchange.getAttribute(START_TIME);
//            if (startTime != null) {
//                Long executeTime = (System.currentTimeMillis() - startTime);
//                List<String> ips = exchange.getRequest().getHeaders().get(X_REAL_IP);
//                String ip = ips != null ? ips.get(0) : null;
//                String api = exchange.getRequest().getURI().getRawPath();
//                int code = 500;
//                if (exchange.getResponse().getStatusCode() != null) {
//                    code = exchange.getResponse().getStatusCode().value();
//                }
//                // 当前仅记录日志，后续可以添加日志队列，来过滤请求慢的接口
//                if (log.isDebugEnabled()) {
//                    log.debug("来自IP地址：{}的请求接口：{}，响应状态码：{}，请求耗时：{}ms", ip, api, code, executeTime);
//                }
//            }
//        }));
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
