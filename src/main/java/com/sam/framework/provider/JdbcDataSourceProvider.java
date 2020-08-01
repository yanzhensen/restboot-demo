package com.sam.framework.provider;

import com.baomidou.dynamic.datasource.provider.AbstractJdbcDataSourceProvider;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * JDBC数据源提供者
 *
 * @author wangchong
 * @since 2020-01-16
 */
public class JdbcDataSourceProvider extends AbstractJdbcDataSourceProvider implements DynamicDataSourceProvider {

    /**
     * JDBC driver
     */
    private String driverClassName;
    /**
     * JDBC url 地址
     */
    private String url;
    /**
     * JDBC 用户名
     */
    private String username;
    /**
     * JDBC 密码
     */
    private String password;
    /**
     * 查询数据源sql
     */
//    private String sql;

    public JdbcDataSourceProvider(String driverClassName, String url, String username, String password) {
        super(driverClassName, url, username, password);
        this.driverClassName = driverClassName;
        this.url = url;
        this.username = username;
        this.password = password;
//        this.sql = sql;
    }

    @Override
    protected Map<String, DataSourceProperty> executeStmt(Statement statement) throws SQLException {
        Map<String, DataSourceProperty> map = new LinkedHashMap<>();
        //动态加载数据库
//        ResultSet rs = statement.executeQuery(sql);
        DataSourceProperty master = new DataSourceProperty();
        master.setPollName("master");
        master.setDriverClassName(driverClassName);
        master.setUrl(url);
        master.setUsername(username);
        master.setPassword(password);
        map.put(master.getPollName(), master);
//        while (rs.next()) {
//            DataSourceProperty dataSourceProperty = new DataSourceProperty();
//            dataSourceProperty.setPollName(rs.getString("name"));
//            dataSourceProperty.setDriverClassName(rs.getString("driver"));
//            dataSourceProperty.setUrl(rs.getString("url"));
//            dataSourceProperty.setUsername(rs.getString("username"));
//            dataSourceProperty.setPassword(rs.getString("password"));
//            map.put(dataSourceProperty.getPollName(), dataSourceProperty);
//        }
        return map;
    }
}
