package com.example.ytspringbatch.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class MetaDBConfig {

    @Primary // DataSource가 여러개일 경우 어떤 DataSource를 사용할지 모르기 때문에 우선순위를 정해줌
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource-meta") // application.properties에 설정한 값을 가져옴
    public DataSource metaDBDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager metaDBTransactionManager() {
        return new DataSourceTransactionManager(metaDBDataSource());
    }




}
