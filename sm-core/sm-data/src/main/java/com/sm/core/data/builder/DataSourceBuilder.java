package com.sm.core.data.builder;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.*;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Objects;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DataSourceBuilder {

    private String driver;

    private String jdbcUrl;

    private String username;

    private String password;

    private String dataSourceName;

    private String vendor;

    public void buildDataSourceProps() {
        dataSourceName = dataSourceName.toLowerCase();
        vendor = vendor.toLowerCase();
        DataSourceProps.loadDataSourceProps(dataSourceName, vendor);
        driver = DataSourceProps.getStringProperty(dataSourceName, "mysql.datasource.driver-class-name");
        jdbcUrl = DataSourceProps.getStringProperty(dataSourceName,"mysql.datasource.jdbcUrl");
        username = DataSourceProps.getStringProperty(dataSourceName,"mysql.datasource.username");
        password = DataSourceProps.getStringProperty(dataSourceName,"mysql.datasource.password");
    }

    public DataSource buildDataSource() {
        buildDataSourceProps();
        if(Objects.isNull(jdbcUrl)) {
            throw new IllegalStateException("jdbcUrl must be set");
        }
        if(Objects.isNull(username)) {
            throw new IllegalStateException("username must be set");
        }
        if(Objects.isNull(password)) {
            throw new IllegalStateException("password must be set");
        }
        try {
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setPoolName(dataSourceName + "_POOL");
            hikariConfig.setJdbcUrl(jdbcUrl);
            hikariConfig.setUsername(username);
            hikariConfig.setPassword(password);
            hikariConfig.setDriverClassName(driver);
            hikariConfig.setMaximumPoolSize(20);
            hikariConfig.setMinimumIdle(2);
            hikariConfig.setIdleTimeout(120000); //2 minutes
            hikariConfig.setMaxLifetime(600000); //10 minutes

            hikariConfig.addDataSourceProperty("cachePrepStmts", true);
            hikariConfig.addDataSourceProperty("prepStmtCacheSize", 250);
            hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
            hikariConfig.addDataSourceProperty("useServerPrepStmts", true);
            hikariConfig.addDataSourceProperty("useLocalSessionState", true);
            hikariConfig.addDataSourceProperty("rewriteBatchedStatements", true);
            hikariConfig.addDataSourceProperty("cacheResultSetMetadata", true);
            hikariConfig.addDataSourceProperty("cacheServerConfiguration", true);
            hikariConfig.addDataSourceProperty("elideSetAutoCommits", true);
            hikariConfig.addDataSourceProperty("maintainTimeStats", false);

            return new HikariDataSource(hikariConfig);
        } catch (Exception e) {
            throw new RuntimeException("Unable to create datasource for " + dataSourceName, e);
        }
    }
}
