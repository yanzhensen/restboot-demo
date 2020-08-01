package com.sam.framework.utils;

import com.sam.common.spring.ApplicationUtils;
import com.sam.common.utils.JacksonUtils;
import com.sam.framework.model.Log;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

/**
 * 请求日志工具类
 *
 * @author Caratacus
 */
@Slf4j
public abstract class LogUtils {

    /**
     * 获取日志对象
     *
     * @param beiginTime
     * @param parameterMap
     * @param requestBody
     * @param url
     * @param mapping
     * @param method
     * @param ip
     * @param object
     * @return
     */
    public static void printLog(Long beiginTime, String uid, Map<String, String[]> parameterMap, String requestBody, String url, String mapping, String method, String ip, Object object) {
        Log logInfo = Log.builder()
                //查询参数
                .parameterMap(parameterMap)
                .uid(uid)
                //请求体
                .requestBody(Optional.ofNullable(JacksonUtils.parse(requestBody)).orElse(requestBody))
                //请求路径
                .url(url)
                //请求mapping
                .mapping(mapping)
                //请求方法
                .method(method)
                .runTime((beiginTime != null ? System.currentTimeMillis() - beiginTime : 0) + "ms")
                .result(object)
                .ip(ip)
                .build();
        log.info(JacksonUtils.toJson(logInfo));
    }

    public static void doAfterReturning(Object ret) {
        ResponseUtils.writeValAsJson(ApplicationUtils.getRequest(), ret);
    }

    /**
     * 第三方设备异常Log
     * 如 msg:redis挂掉了 component redis
     * 输出: redis挂掉了(redis)
     *
     * @param msg
     * @param component
     */
    public static void errorLog(String msg, String component) {
        log.error(msg + "(" + component + ")");
    }

    //默认文件路径
    private final static String PATH = System.getProperty("user.dir") + "/logs/demo";
    public final static String MESSAGE_LOG = "/runBroken.log";

    /**
     * 写日志文件 默认路径
     *
     * @param str 日志内容
     */
    public static void writeLog(String str) {
        try {
            File file = new File(PATH + MESSAGE_LOG);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(file, true);
            StringBuffer sb = new StringBuffer();
            sb.append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            sb.append("\t");
            sb.append(str);
            sb.append("\r\n");
            out.write(sb.toString().getBytes("UTF-8"));
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 写日志文件 指定路径
     *
     * @param str  日志内容
     * @param path 日志文件路径
     */
    public static void writeLog(String str, String path) {
        try {
            File file = new File(PATH + path);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(file, true);
            StringBuffer sb = new StringBuffer();
            sb.append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            sb.append("\t");
            sb.append(str);
            sb.append("\r\n");
            out.write(sb.toString().getBytes("UTF-8"));
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
