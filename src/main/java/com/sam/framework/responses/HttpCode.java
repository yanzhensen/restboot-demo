package com.sam.framework.responses;

import javax.servlet.http.HttpServletResponse;

public interface HttpCode extends HttpServletResponse {
    /**
     * 秘钥不正确/失效/冻结
     */
    int PHO_SECRET_ERROR = 601;
    /**
     * 指定空间不存在
     */
    int PHO_BUCKET_NOT_EXIST = 602;
    /**
     * 指定资源不存在或已被删除
     */
    int PHO_RESOURCE_NOT_EXIST = 603;


    /**
     * 服务器异常/回调异常
     */
    int PHO_INTERNAL_SERVER_ERROR = 605;
}
