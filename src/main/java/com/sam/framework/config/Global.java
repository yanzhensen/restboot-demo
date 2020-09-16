package com.sam.framework.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 全局配置类
 * 使用 @ConfigurationProperties注入对象 普通方法调用可以  在静态方法中调用的时候读取不到参数
 * 需要导入spring-boot-configuration-processor包
 *
 * @author Sam
 */

@Component
@ConfigurationProperties(prefix = "sam")
public class Global {
    /**
     * 项目名称
     */
    private static String name;

    /**
     * 版本
     */
    private static String version;

    /**
     * 版权年份
     */
    private static String copyrightYear;

    /**
     * 上传路径
     */
    private static String profile;

    /**
     * 密码盐
     */
    private static String pwdSalt;


    public static String getName() {
        return name;
    }

    public void setName(String name) {
        Global.name = name;
    }

    public static String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        Global.version = version;
    }

    public static String getCopyrightYear() {
        return copyrightYear;
    }

    public void setCopyrightYear(String copyrightYear) {
        Global.copyrightYear = copyrightYear;
    }

    public static String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        Global.profile = profile;
    }

    public static String getPwdSalt() {
        return pwdSalt;
    }

    public void setPwdSalt(String pwdSalt) {
        Global.pwdSalt = pwdSalt;
    }

    /**
     * 获取头像上传路径
     */
    public static String getAvatarPath() {
        return getProfile() + "/avatar";
    }

    /**
     * 获取下载路径
     */
    public static String getDownloadPath() {
        return getProfile() + "/download/";
    }

}
