package com.petims.config;

import com.petims.common.constant.SystemConstant;
import com.petims.common.util.TenantContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TenantInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(TenantInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String tenantId = request.getHeader(SystemConstant.TENANT_ID_HEADER);
        if (tenantId == null || tenantId.isEmpty()) {
            tenantId = SystemConstant.DEFAULT_TENANT_ID;
        }
        TenantContextHolder.setTenantId(tenantId);
        log.debug("当前租户ID: {}", tenantId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        TenantContextHolder.clear();
    }
}
