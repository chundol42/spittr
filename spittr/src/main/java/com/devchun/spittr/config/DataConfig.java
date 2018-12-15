package com.devchun.spittr.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import net.sf.log4jdbc.Log4jdbcProxyDataSource;
import net.sf.log4jdbc.tools.Log4JdbcCustomFormatter;
import net.sf.log4jdbc.tools.LoggingType;

@Configuration
@EnableTransactionManagement(proxyTargetClass=true)
@PropertySource("classpath:db.properties")
public class DataConfig {
  private static final Logger logger = LoggerFactory.getLogger(DataConfig.class);

  @Autowired
  private Environment env;

  @Bean
  public DriverManagerDataSource dataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    logger.info(env.getProperty("jdbc.driver"));
    logger.info(env.getProperty("jdbc.url"));
    logger.info(env.getProperty("jdbc.username"));
    logger.info(env.getProperty("jdbc.password"));

    dataSource.setDriverClassName(env.getProperty("jdbc.driver"));
    dataSource.setUrl(env.getProperty("jdbc.url"));
    dataSource.setUsername(env.getProperty("jdbc.username"));
    dataSource.setPassword(env.getProperty("jdbc.password"));

    return dataSource;
  }

  public DataSource dataSourceSpied() {
    Log4jdbcProxyDataSource dataSourceSpied = new Log4jdbcProxyDataSource(dataSource());
    Log4JdbcCustomFormatter formatter = new Log4JdbcCustomFormatter();
    formatter.setLoggingType(LoggingType.MULTI_LINE);
    formatter.setSqlPrefix("");

    dataSourceSpied.setLogFormatter(formatter);

    return dataSourceSpied;
  }

  /* Hibernate configuration */
  
  @Bean
  @Qualifier("hibernateSessionFactory")
  public LocalSessionFactoryBean hibernateSessionFactory() {
    LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
    sessionFactory.setDataSource(dataSource());
    sessionFactory.setPackagesToScan(new String[] { "com.devchun.spittr.domain" });

    Properties properties = new Properties();
    properties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
    properties.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
    properties.setProperty("hibernate.format_sql", env.getProperty("hibernate.format_sql"));

    sessionFactory.setHibernateProperties(properties);

    return sessionFactory;
  }

  @Bean
  @Qualifier("hibernateTxManager")
  public HibernateTransactionManager hibernateTxManager(@Qualifier("hibernateSessionFactory") SessionFactory sessionFactory) {
    HibernateTransactionManager txManager = new HibernateTransactionManager();
    txManager.setSessionFactory(sessionFactory);

    return txManager;
  }

  @Bean
  public BeanPostProcessor exceptionTranslation() {
    return new PersistenceExceptionTranslationPostProcessor();
  }
}
