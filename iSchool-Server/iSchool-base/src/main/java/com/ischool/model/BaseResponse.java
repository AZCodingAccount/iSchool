package com.ischool.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Schema(description = "通用返回结构")
public class BaseResponse<T> implements Serializable {

    @Schema(description = "响应码，成功为0其他（4xxxx客户端错误，5xxxx服务端错误）", example = "50010")
    private int code;

    @Schema(description = "响应数据", example = "null")
    private T data;

    @Schema(description = "响应消息，成功为success，错误返回失败信息", example = "接口调用失败")
    private String msg;

    public BaseResponse(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }
}