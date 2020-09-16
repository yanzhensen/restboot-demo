package com.sam.generate;

import com.sam.common.enums.SeniorityEnum;
import org.junit.jupiter.api.Test;

/**
 * mybatisPlus数据库表生成器
 *
 * @author Sam
 */
public class MysqlGeneratorTest {

    String url = "jdbc:mysql://127.0.0.1:3306/restbootdb?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false";
    String username = "root";
    String password = "root";

    @Test
    public void generator() {
        MysqlGenerator mysqlGenerator = new MysqlGenerator();
        mysqlGenerator.generator("Sam", SeniorityEnum.MOGUL, url, username, password,
                "info", "info_", "info_photo");
    }
}
