package com.sam.framework.enums;

import com.sam.common.exception.UnknownEnumException;
import com.sam.framework.model.ErrorCode;
import com.sam.framework.responses.HttpCode;

/**
 * 业务异常枚举
 *
 * @author Caratacus
 */
public enum ErrorCodeEnum {

    /**
     * 400
     */
    BAD_REQUEST(HttpCode.SC_BAD_REQUEST, true, "请求参数错误或不完整"),
    /**
     * JSON格式错误
     */
    JSON_FORMAT_ERROR(HttpCode.SC_BAD_REQUEST, true, "JSON格式错误"),
    /**
     * 401
     */
    UNAUTHORIZED(HttpCode.SC_UNAUTHORIZED, true, "请先进行认证"),
    /**
     * 403
     */
    FORBIDDEN(HttpCode.SC_FORBIDDEN, true, "无权查看"),
    /**
     * 404
     */
    NOT_FOUND(HttpCode.SC_NOT_FOUND, true, "未找到该路径"),
    /**
     * 405
     */
    METHOD_NOT_ALLOWED(HttpCode.SC_METHOD_NOT_ALLOWED, true, "请求方式不支持"),
    /**
     * 406
     */
    NOT_ACCEPTABLE(HttpCode.SC_NOT_ACCEPTABLE, true, "不可接受该请求"),
    /**
     * 411
     */
    LENGTH_REQUIRED(HttpCode.SC_LENGTH_REQUIRED, true, "长度受限制"),
    /**
     * 415
     */
    UNSUPPORTED_MEDIA_TYPE(HttpCode.SC_UNSUPPORTED_MEDIA_TYPE, true, "不支持的媒体类型"),
    /**
     * 416
     */
    REQUESTED_RANGE_NOT_SATISFIABLE(HttpCode.SC_REQUESTED_RANGE_NOT_SATISFIABLE, true, "不能满足请求的范围"),
    /**
     * 500
     */
    INTERNAL_SERVER_ERROR(HttpCode.SC_INTERNAL_SERVER_ERROR, true, "服务器正在升级，请耐心等待"),
    /**
     * 503
     */
    SERVICE_UNAVAILABLE(HttpCode.SC_SERVICE_UNAVAILABLE, true, "请求超时"),


    //-----------------------------------------------图片上传自定义异常------------------------------------------------
    /**
     * 601 秘钥不正确/失效/冻结
     */
    PHO_SECRET_ERROR(HttpCode.PHO_SECRET_ERROR,true,"秘钥不正确/失效/冻结"),
    /**
     * 602 指定空间不存在
     */
    PHO_BUCKET_NOT_EXIST(HttpCode.PHO_BUCKET_NOT_EXIST,true,"指定空间不存在"),
    /**
     * 603 指定资源不存在或已被删除
     */
    PHO_RESOURCE_NOT_EXIST(HttpCode.PHO_RESOURCE_NOT_EXIST,true,"指定资源不存在或已被删除"),
    /**
     * 604 指定资源不存在或已被删除
     */
    PHO_0000(HttpCode.PHO_RESOURCE_NOT_EXIST,true,"指定资源不存在或已被删除"),
    /**
     * 605 服务器异常/回调异常
     */
    PHO_INTERNAL_SERVER_ERROR(HttpCode.PHO_INTERNAL_SERVER_ERROR,true,"服务器异常/回调异常"),

    //----------------------------------------------------业务异常----------------------------------------------------
    /**
     * 添加失败
     */
    BAD_ADD_FAILURE(HttpCode.SC_INTERNAL_SERVER_ERROR, true, "添加失败"),
    /**
     * 删除失败
     */
    BAD_DELETE_FAILURE(HttpCode.SC_INTERNAL_SERVER_ERROR, true, "删除失败"),
    /**
     * 没有任何信息修改
     */
    BAD_UPDATE_FAILURE(HttpCode.SC_INTERNAL_SERVER_ERROR, true, "修改失败"),

    ;

    private final int httpCode;
    private final boolean show;
    private final String msg;

    ErrorCodeEnum(int httpCode, boolean show, String msg) {
        this.httpCode = httpCode;
        this.msg = msg;
        this.show = show;
    }

    /**
     * 转换为ErrorCode(自定义返回消息)
     *
     * @param msg
     * @return
     */
    public ErrorCode convert(String msg) {
        return ErrorCode.builder().httpCode(httpCode()).show(show()).error(name()).msg(msg).build();
    }

    /**
     * 转换为ErrorCode
     *
     * @return
     */
    public ErrorCode convert() {
        return ErrorCode.builder().httpCode(httpCode()).show(show()).error(name()).msg(msg()).build();
    }

    public static ErrorCodeEnum getErrorCode(String errorCode) {
        ErrorCodeEnum[] enums = ErrorCodeEnum.values();
        for (ErrorCodeEnum errorCodeEnum : enums) {
            if (errorCodeEnum.name().equalsIgnoreCase(errorCode)) {
                return errorCodeEnum;
            }
        }
        throw new UnknownEnumException("Error: Unknown errorCode, or do not support changing errorCode!\n");
    }

    public int httpCode() {
        return this.httpCode;
    }

    public String msg() {
        return this.msg;
    }

    public boolean show() {
        return this.show;
    }

}
