package com.petims.common.result;

public enum ResultCode {

    SUCCESS(200, "操作成功"),
    
    ERROR(500, "操作失败"),
    
    PARAM_ERROR(400, "参数错误"),
    
    UNAUTHORIZED(401, "未授权"),
    
    FORBIDDEN(403, "禁止访问"),
    
    NOT_FOUND(404, "资源不存在"),
    
    USER_NOT_EXIST(1001, "用户不存在"),
    
    USER_ALREADY_EXIST(1002, "用户已存在"),
    
    PASSWORD_ERROR(1003, "密码错误"),
    
    TENANT_NOT_EXIST(2001, "租户不存在"),
    
    PET_NOT_EXIST(3001, "宠物不存在");

    private final Integer code;

    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
