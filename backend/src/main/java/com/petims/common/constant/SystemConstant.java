package com.petims.common.constant;

public interface SystemConstant {

    String TENANT_ID_HEADER = "Tenant-Id";

    String DEFAULT_TENANT_ID = "1";

    String TOKEN_HEADER = "Authorization";

    String TOKEN_PREFIX = "Bearer ";

    Long JWT_EXPIRATION = 86400000L;

    String JWT_SECRET = "petims-secret-key-2024";
}
