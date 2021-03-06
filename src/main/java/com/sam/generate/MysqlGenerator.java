/*
 * Copyright (c) 2018-2022 Caratacus, (caratacus@qq.com).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.sam.generate;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.sam.common.enums.SeniorityEnum;

/**
 * <p>
 * Mysql代码生成器
 * </p>
 *
 * @author Caratacus
 */
public class MysqlGenerator extends SuperGenerator {

    /**
     * MySQL generator
     *
     * @param author          作者
     * @param seniority       获取路径形式
     * @param url             数据库连接地址
     * @param username        数据库用户名
     * @param password        数据库密码
     * @param packageLocation 路径包位置
     * @param tablePrefix     表前缀  输入前缀则自动去掉表名前缀
     * @param tableName       表名
     * @return
     */
    public void generator(String author, SeniorityEnum seniority, String url, String username, String password,
                          String packageLocation, String tablePrefix, String... tableName) {
        // 代码生成器
        AutoGenerator mpg = getAutoGenerator(author, seniority, url, username, password, packageLocation, tablePrefix, tableName);
        mpg.execute();
        if (tableName == null) {
            System.err.println(" Generator Success !");
        } else {
            for (int i = 0; i < tableName.length; i++) {
                System.err.println(" TableName【 " + tableName[i] + " 】" + "Generator Success !");
            }
        }
    }


}
