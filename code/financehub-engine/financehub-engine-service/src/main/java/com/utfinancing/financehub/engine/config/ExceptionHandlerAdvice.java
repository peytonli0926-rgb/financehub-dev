package com.utfinancing.financehub.engine.config;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.engine.file.common.UnifiedException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.ServletException;
import java.sql.SQLException;


/**
 * @author Administrator
 * 全局异常处理类
 */
@RestControllerAdvice
@Slf4j
public class ExceptionHandlerAdvice {

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public R methodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("方法请求错误", e);
        return R.fail("方法请求错误");
    }

    @ExceptionHandler(value = ServletException.class)
    public R servletException(ServletException e) {
        /*
          TODO 直接捕获NoHandlerFoundException异常gateway会报错，所以捕获ServletException
         */
        if (e instanceof NoHandlerFoundException) {
            log.error("请求路径未找到:", ((NoHandlerFoundException) e).getRequestURL());
            return R.fail("请求路径未找到");
        }
        log.error("ServletException:", e);
        return R.fail("系统内部错误");
    }


    @ExceptionHandler(value = FeignException.class)
    public R feignException(FeignException ex) {
        log.error("发生FeignException异常：", ex);
        String message = ex.getMessage();
        if (StrUtil.isNotEmpty(message) && ReUtil.contains("[\\u4e00-\\u9fa5]", message)) {
            return R.fail(ex.status(), message);
        } else {
            return R.fail("系统内部错误");
        }
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(UnifiedException.class)
    public R businessException(UnifiedException e) {
        log.error("发生UnifiedException异常：", e);
        if (StringUtils.isNull(e.getCode())) {
            return R.fail("系统内部错误");
        }
        return R.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public R handleException(Exception e) {
        log.error("发生未知异常：" + e.getMessage(), e);
        if (e instanceof DataAccessException || e instanceof SQLException) {
            return R.fail("SQL异常");
        }
        return R.fail(e.getMessage());
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object validExceptionHandler(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return R.fail(message);
    }

    /**
     * 文件上传大小异常
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public R maxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        return R.fail("附件上传异常，文件过大，请分次上传");
    }

}
