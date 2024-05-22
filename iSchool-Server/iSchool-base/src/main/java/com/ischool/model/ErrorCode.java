package com.ischool.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "错误响应码")
public enum ErrorCode {

    @Schema(description = "成功", example = "0")
    SUCCESS(0, "ok"),

    @Schema(description = "请求参数错误", example = "40000")
    PARAMS_ERROR(40000, "请求参数错误"),

    @Schema(description = "未登录", example = "40100")
    NOT_LOGIN_ERROR(40100, "未登录"),

    @Schema(description = "无权限", example = "40101")
    NO_AUTH_ERROR(40101, "无权限"),

    @Schema(description = "请求数据不存在", example = "40400")
    NOT_FOUND_ERROR(40400, "请求数据不存在"),

    @Schema(description = "禁止访问", example = "40300")
    FORBIDDEN_ERROR(40300, "禁止访问"),

    @Schema(description = "系统内部异常", example = "50000")
    SYSTEM_ERROR(50000, "系统内部异常"),

    @Schema(description = "操作失败", example = "50001")
    OPERATION_ERROR(50001, "操作失败"),

    @Schema(description = "接口调用失败", example = "50010")
    API_REQUEST_ERROR(50010, "接口调用失败");

    /**
     * 状态码
     */
    private final int code;

    /**
     * 信息
     */
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}