package con.gao.shop.error.handler;

import com.gao.shop.result.ResultResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.springframework.web.reactive.function.server.RequestPredicates.all;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * 继承AbstractErrorWebExceptionHandler类定制化返回错误信息属性
 *
 * @author gaopenglin
 * @date 2023/02/15
 */
@Log4j2
public class MyErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {
    private final ErrorProperties errorProperties;

    /**
     * Create a new {@code AbstractErrorWebExceptionHandler}.
     *
     * @param errorAttributes    the error attributes
     * @param resources          the resources configuration properties
     * @param applicationContext the application context
     * @since 2.4.0
     */
    public MyErrorWebExceptionHandler(ErrorAttributes errorAttributes, WebProperties.Resources resources, ErrorProperties errorProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resources, applicationContext);
        this.errorProperties = errorProperties;
    }

    /**
     * 转换为JSON数据返回
     * Create a {@link RouterFunction} that can route and handle errors as JSON responses
     * or HTML views.
     * <p>
     * If the returned {@link RouterFunction} doesn't route to a {@code HandlerFunction},
     * the original exception is propagated in the pipeline and can be processed by other
     * {@link WebExceptionHandler}s.
     *
     * @param errorAttributes the {@code ErrorAttributes} instance to use to extract error
     *                        information
     * @return a {@link RouterFunction} that routes and handles errors
     */
    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return route(all(), this::renderErrorResponse);
    }

    /**
     * 自定义返回数据格式
     * Render the error information as a JSON payload.
     *
     * @param request the current request
     * @return a {@code Publisher} of the HTTP response
     */
    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        //获取原始的异常信息
        Map<String, Object> error = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));
        log.info("Gateway网关获取到原始错误信息：[{}]", error);
        Throwable throwable = getError(request);
        //构建响应信息
        BodyInserter<ResultResponse, ReactiveHttpOutputMessage> responseResult = BodyInserters.fromValue(ResultResponse.error(getHttpStatus(error) + "", throwable.getMessage()));
        return ServerResponse
                // http返回码
                .status(200)
                // 类型和以前一样（定义返回类型）
                .contentType(MediaType.APPLICATION_JSON)
                // 响应body的内容
                .body(responseResult);
    }

    protected ErrorAttributeOptions getErrorAttributeOptions(ServerRequest request, MediaType mediaType) {
        ErrorAttributeOptions options = ErrorAttributeOptions.defaults();
        if (this.errorProperties.isIncludeException()) {
            options = options.including(ErrorAttributeOptions.Include.EXCEPTION);
        }
        if (isIncludeStackTrace(request)) {
            options = options.including(ErrorAttributeOptions.Include.STACK_TRACE);
        }
        if (isIncludeMessage(request)) {
            options = options.including(ErrorAttributeOptions.Include.MESSAGE);
        }
        if (isIncludeBindingErrors(request)) {
            options = options.including(ErrorAttributeOptions.Include.BINDING_ERRORS);
        }
        return options;
    }

    /**
     * Get the HTTP error status information from the error map.
     *
     * @param errorAttributes the current error information
     * @return the error HTTP status
     */
    protected int getHttpStatus(Map<String, Object> errorAttributes) {
        return (int) errorAttributes.get("status");
    }

    /**
     * 是否包含stacktrace属性
     * Determine if the stacktrace attribute should be included.
     *
     * @param request the source request
     * @return if the stacktrace attribute should be included
     */
    protected boolean isIncludeStackTrace(ServerRequest request) {
        return switch (this.errorProperties.getIncludeStacktrace()) {
            case ALWAYS -> true;
            case ON_PARAM -> isTraceEnabled(request);
            default -> false;
        };
    }

    /**
     * 是否包含Message属性
     * Determine if the message attribute should be included.
     *
     * @param request the source request
     * @return if the message attribute should be included
     */
    protected boolean isIncludeMessage(ServerRequest request) {
        return switch (this.errorProperties.getIncludeMessage()) {
            case ALWAYS -> true;
            case ON_PARAM -> isMessageEnabled(request);
            default -> false;
        };
    }

    /**
     * 是否包含Errors属性
     * Determine if the errors attribute should be included.
     *
     * @param request the source request
     * @return if the errors attribute should be included
     */
    protected boolean isIncludeBindingErrors(ServerRequest request) {
        return switch (this.errorProperties.getIncludeBindingErrors()) {
            case ALWAYS -> true;
            case ON_PARAM -> isBindingErrorsEnabled(request);
            default -> false;
        };
    }

//    private static final String ERROR_INTERNAL_ATTRIBUTE = MyErrorWebExceptionHandler.class.getName() + ".ERROR";

//    public Throwable getError(ServerRequest request) {
//        Optional<Object> error = request.attribute(ERROR_INTERNAL_ATTRIBUTE);
//        error.ifPresent((value) -> {
//            request.attributes().putIfAbsent(ErrorAttributes.ERROR_ATTRIBUTE, value);
//        });
//        return (Throwable)error.orElseThrow(() -> {
//            return new IllegalStateException("Missing exception attribute in ServerWebExchange");
//        });
//    }

}
