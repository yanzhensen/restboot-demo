package com.sam.framework.exception;

import com.sam.framework.enums.ErrorCodeEnum;
import com.sam.framework.model.ErrorCode;

/**
 * <p>
 * API 业务异常类
 * </p>
 *
 * @author Caratacus
 */
public class ApiException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private final ErrorCode errorCode;

    public ApiException(ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum.msg());
        this.errorCode = errorCodeEnum.convert();
    }

    public ApiException(ErrorCode errorCode) {
        super(errorCode.getError());
        this.errorCode = errorCode;

    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

}
