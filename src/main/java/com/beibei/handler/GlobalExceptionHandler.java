package com.beibei.handler;

import com.beibei.entity.RestBean;
import com.beibei.service.SystemNotificationService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @Resource
    private SystemNotificationService notificationService;

    /**
     * 处理未知异常
     */
    @ExceptionHandler(Exception.class)
    public RestBean<String> handleException(Exception e) {
        log.error("系统发生未知异常", e);

        // 记录到系统通知
        String errorMessage = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
        notificationService.addNotification(
                "系统错误",
                "系统发生未知异常: " + errorMessage,
                "ERROR");

        // 返回通用错误信息
        return RestBean.failure(500, "服务器内部错误，请联系管理员");
    }

    /**
     * 处理业务异常
     */
    @ExceptionHandler(RuntimeException.class)
    public RestBean<String> handleRuntimeException(RuntimeException e) {
        log.error("系统发生业务异常", e);

        // 如果是重要错误，记录到系统通知
        if (isImportantError(e)) {
            String errorMessage = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
            notificationService.addNotification(
                    "业务错误",
                    "系统发生业务异常: " + errorMessage,
                    "WARNING");
        }

        // 返回错误信息给前端
        return RestBean.failure(400, e.getMessage());
    }

    /**
     * 处理认证相关异常
     */
    @ExceptionHandler({ BadCredentialsException.class, UsernameNotFoundException.class })
    public RestBean<String> handleAuthenticationException(Exception e) {
        log.info("登录失败: {}", e.getMessage());
        return RestBean.failure(401, "用户名或密码错误");
    }

    /**
     * 处理未授权异常
     */
    @ExceptionHandler(InsufficientAuthenticationException.class)
    public RestBean<String> handleInsufficientAuthenticationException(InsufficientAuthenticationException e) {
        log.info("未授权访问: {}", e.getMessage());
        return RestBean.failure(401, "未登录或登录已过期");
    }

    /**
     * 判断是否是重要错误（需要记录到系统通知的错误）
     */
    private boolean isImportantError(Exception e) {
        // 以下是一些示例，实际情况可以根据项目需求定制
        String message = e.getMessage();
        if (message == null)
            return false;

        return message.contains("数据库") ||
                message.contains("连接") ||
                message.contains("权限") ||
                message.contains("database") ||
                message.contains("connection") ||
                message.contains("permission");
    }
}
