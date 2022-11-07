package com.yxc.imapi.config;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.yxc.imapi.model._MappingKit;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class DataSourceConfig {

    /**
     * 初始化连接池
     *
     * @return
     */
    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druidDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setProxyFilters(getDuridFilters());
        return dataSource;
    }

    /**
     * 设置数据源代理
     */
    @Bean
    public TransactionAwareDataSourceProxy transactionAwareDataSourceProxy() {
        TransactionAwareDataSourceProxy transactionAwareDataSourceProxy = new TransactionAwareDataSourceProxy();
        transactionAwareDataSourceProxy.setTargetDataSource(druidDataSource());
        return transactionAwareDataSourceProxy;
    }

    /**
     * 设置ActiveRecord
     */
    @Bean
    public ActiveRecordPlugin activeRecordPlugin() {
        ActiveRecordPlugin arp = new ActiveRecordPlugin(transactionAwareDataSourceProxy());
        arp.setDialect(new MysqlDialect());
        arp.setContainerFactory(new CaseInsensitiveContainerFactory(true));
        arp.setShowSql(true);
        arp.getEngine().setToClassPathSourceFactory();
        //arp.addSqlTemplate("sql/all.sql");
        _MappingKit.mapping(arp);
//        _MappingKitManual.mapping(arp);
        arp.start();
        System.out.println("调用Jfinal ActiveRecordPlugin 成功");
        return arp;
    }

    /**
     * 设置事务管理
     */
    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager() {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(transactionAwareDataSourceProxy());
        return dataSourceTransactionManager;
    }

    public Slf4jLogFilter slf4jLogFilter() {
        Slf4jLogFilter slFilter = new Slf4jLogFilter();
        slFilter.setStatementExecutableSqlLogEnable(true);
        slFilter.setStatementLogErrorEnabled(true);
        slFilter.setStatementSqlPrettyFormat(true);
        return slFilter;
    }

    private List<Filter> getDuridFilters() {
        List<Filter> filters = new ArrayList<>(2);
        filters.add(slf4jLogFilter());
        return filters;
    }
}
