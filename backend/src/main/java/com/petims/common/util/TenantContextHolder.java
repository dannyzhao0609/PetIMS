package com.petims.common.util;

import com.petims.common.constant.SystemConstant;

public class TenantContextHolder {

    private static final ThreadLocal<String> TENANT_ID_HOLDER = new ThreadLocal<>();

    public static void setTenantId(String tenantId) {
        TENANT_ID_HOLDER.set(tenantId);
    }

    public static String getTenantId() {
        String tenantId = TENANT_ID_HOLDER.get();
        return tenantId != null ? tenantId : SystemConstant.DEFAULT_TENANT_ID;
    }

    public static void clear() {
        TENANT_ID_HOLDER.remove();
    }
}
