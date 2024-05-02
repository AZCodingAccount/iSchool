package com.ischool.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {
    private Integer code; // 编码：1成功，0和其它数字为失败
    private String msg; // 错误信息
    private T data; // 数据

    // 返回数据为空
    public static <T> BaseResponse<T> success() {
        return new BaseResponse<>(1, null, "success");
    }

    // 返回带数据的请求
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<T>(1, data, "success");
    }

    // 使用默认的失败的信息
    public static <T> BaseResponse<T> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode.getCode(), null, errorCode.getMessage());
    }

    // 兼容全局异常处理器的错误码
    public static BaseResponse error(int code, String msg) {
        return new BaseResponse(code, null, msg);
    }


    // 自定义失败的信息
    public static <T> BaseResponse<T> error(ErrorCode errorCode, String msg) {
        return new BaseResponse<>(errorCode.getCode(), null, msg);
    }

}
