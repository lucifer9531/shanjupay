package com.google.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 自定义的异常类型
 * @author Administrator
 * @version 1.0
 **/
@Getter
@Setter
public class BusinessException extends RuntimeException {

    private ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }
    public BusinessException() {
        super();
    }
}
