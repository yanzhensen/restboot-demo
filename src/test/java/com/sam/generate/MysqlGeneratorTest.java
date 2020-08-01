package com.sam.generate;

import com.sam.common.enums.SeniorityEnum;
import org.junit.jupiter.api.Test;

/**
 * mybatisPlus数据库表生成器
 *
 * @author Sam
 */
public class MysqlGeneratorTest {

    @Test
    public void generator() {
        MysqlGenerator mysqlGenerator = new MysqlGenerator();
        mysqlGenerator.generator("Sam", SeniorityEnum.MOGUL, "sys_",
                "sys_user","sys_user_role","sys_menu","sys_menu_resource","sys_role_menu","sys_role","sys_role_resource","sys_resource");
    }
}
