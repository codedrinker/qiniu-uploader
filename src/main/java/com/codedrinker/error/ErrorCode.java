package com.codedrinker.error;

/**
 * Created by codedrinker on 30/04/2018.
 */
public enum ErrorCode implements IErrorCode {
    UPLOAD_FAIL(1009, "上传失败，请检查文件名并重试"),;

    private Integer code;
    private String message;


    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
