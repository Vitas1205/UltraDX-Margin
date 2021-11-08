package com.fota.fotamargin.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author Yuanming Tao
 * Created on 2018/11/15
 * Description
 */
@Configuration
public class MyBatisDataSourceConfig {
    private static final Logger log = LoggerFactory.getLogger(MyBatisDataSourceConfig.class);

    private final MybatisProperties properties;

    private final Interceptor[] interceptors;

    private final ResourceLoader resourceLoader;

    private final DatabaseIdProvider databaseIdProvider;

    private final List<ConfigurationCustomizer> configurationCustomizers;

    public MyBatisDataSourceConfig(MybatisProperties properties,
                                   ObjectProvider<Interceptor[]> interceptorsProvider,
                                   ResourceLoader resourceLoader,
                                   ObjectProvider<DatabaseIdProvider> databaseIdProvider,
                                   ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider) {
        this.properties = properties;
        this.interceptors = interceptorsProvider.getIfAvailable();
        this.resourceLoader = resourceLoader;
        this.databaseIdProvider = databaseIdProvider.getIfAvailable();
        this.configurationCustomizers = configurationCustomizersProvider.getIfAvailable();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.druid.fota")
    public DataSource fota() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.druid.trade")
    public DataSource trade() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    public SqlSessionFactory fotaSqlSessionFactory(@Qualifier("fota") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factory = createSqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        DruidDataSource druidDataSource = (DruidDataSource) dataSource;
        log.info("fotaSqlSessionFactory>>jdbcUrl:{}, username:{}, password:{}", druidDataSource.getUrl(), druidDataSource.getUsername(), druidDataSource.getPassword());
        return factory.getObject();
    }

    @Bean
    public SqlSessionFactory tradeSqlSessionFactory(@Qualifier("trade") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factory = createSqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        DruidDataSource druidDataSource = (DruidDataSource) dataSource;
        log.info("tradeSqlSessionFactory>>jdbcUrl:{}, username:{}, password:{}", druidDataSource.getUrl(), druidDataSource.getUsername(), druidDataSource.getPassword());
        return factory.getObject();
    }

    @Bean
    public SqlSessionTemplate fotaSqlSessionTemplate(@Qualifier("fotaSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        ExecutorType executorType = this.properties.getExecutorType();
        DataSource dataSource = sqlSessionFactory.getConfiguration().getEnvironment().getDataSource();
        if (dataSource instanceof DruidDataSource) {
            DruidDataSource druidDataSource = (DruidDataSource) dataSource;
            log.info("fotaSqlSessionTemplate>>jdbcUrl:{}, username:{}, password:{}", druidDataSource.getUrl(), druidDataSource.getUsername(), druidDataSource.getPassword());
        } else {
            log.info("fotaSqlSessionTemplate>>dataSource is not instanceof DruidDataSource");
        }
        if (executorType != null) {
            return new SqlSessionTemplate(sqlSessionFactory, executorType);
        } else {
            return new SqlSessionTemplate(sqlSessionFactory);
        }
    }

    @Bean
    public SqlSessionTemplate tradeSqlSessionTemplate(@Qualifier("tradeSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        ExecutorType executorType = this.properties.getExecutorType();
        DataSource dataSource = sqlSessionFactory.getConfiguration().getEnvironment().getDataSource();
        if (dataSource instanceof DruidDataSource) {
            DruidDataSource druidDataSource = (DruidDataSource) dataSource;
            log.info("tradeSqlSessionTemplate>>jdbcUrl:{}, username:{}, password:{}", druidDataSource.getUrl(), druidDataSource.getUsername(), druidDataSource.getPassword());
        } else {
            log.info("tradeSqlSessionTemplate>>dataSource is not instanceof DruidDataSource");
        }
        if (executorType != null) {
            return new SqlSessionTemplate(sqlSessionFactory, executorType);
        } else {
            return new SqlSessionTemplate(sqlSessionFactory);
        }
    }

    private SqlSessionFactoryBean createSqlSessionFactoryBean() {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setVfs(SpringBootVFS.class);
        if (StringUtils.hasText(this.properties.getConfigLocation())) {
            factory.setConfigLocation(this.resourceLoader.getResource(this.properties.getConfigLocation()));
        }
        org.apache.ibatis.session.Configuration configuration = this.properties.getConfiguration();
        if (configuration == null && !StringUtils.hasText(this.properties.getConfigLocation())) {
            configuration = new org.apache.ibatis.session.Configuration();
        }
        if (configuration != null && !CollectionUtils.isEmpty(this.configurationCustomizers)) {
            for (ConfigurationCustomizer customizer : this.configurationCustomizers) {
                customizer.customize(configuration);
            }
        }
        org.apache.ibatis.session.Configuration configuration2 = new org.apache.ibatis.session.Configuration();
        BeanUtils.copyProperties(configuration, configuration2);
        factory.setConfiguration(configuration2);
        if (this.properties.getConfigurationProperties() != null) {
            factory.setConfigurationProperties(this.properties.getConfigurationProperties());
        }
        if (!ObjectUtils.isEmpty(this.interceptors)) {
            factory.setPlugins(this.interceptors);
        }
        if (this.databaseIdProvider != null) {
            factory.setDatabaseIdProvider(this.databaseIdProvider);
        }
        if (StringUtils.hasLength(this.properties.getTypeAliasesPackage())) {
            factory.setTypeAliasesPackage(this.properties.getTypeAliasesPackage());
        }
        if (StringUtils.hasLength(this.properties.getTypeHandlersPackage())) {
            factory.setTypeHandlersPackage(this.properties.getTypeHandlersPackage());
        }
        if (!ObjectUtils.isEmpty(this.properties.resolveMapperLocations())) {
            factory.setMapperLocations(this.properties.resolveMapperLocations());
        }
        return factory;
    }

    @Configuration
    @MapperScan(value = "com.fota.fotamargin.dao.mapper", sqlSessionTemplateRef = "fotaSqlSessionTemplate")
    public static class FotaMapperConfig {

    }

    @Configuration
    @MapperScan(value = "com.fota.fotamargin.dao.trade.mapper", sqlSessionTemplateRef = "tradeSqlSessionTemplate")
    public static class TradeMapperConfig {

    }
}