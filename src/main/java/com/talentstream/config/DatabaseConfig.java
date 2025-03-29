package com.talentstream.config;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.talentstream.AwsSecretsManagerUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DatabaseConfig {


  @Autowired
  private AwsSecretsManagerUtil awsSecretsManagerUtil;

   @Bean
   public DataSource dataSource() {
       HikariConfig hikariConfig = new HikariConfig();
       hikariConfig.setJdbcUrl("jdbc:postgresql://aws-0-ap-south-1.pooler.supabase.com:5432/postgres");
       hikariConfig.setUsername(awsSecretsManagerUtil.getDbUsername());
       hikariConfig.setPassword(awsSecretsManagerUtil.getDbPassword());
       hikariConfig.setDriverClassName("org.postgresql.Driver");
       hikariConfig.setMaximumPoolSize(20);  
       hikariConfig.setMinimumIdle(3);      
       hikariConfig.setIdleTimeout(60000);   
       hikariConfig.setMaxLifetime(900000); 
       hikariConfig.setConnectionTimeout(30000);
       hikariConfig.setValidationTimeout(3000); 
       
       return new HikariDataSource(hikariConfig);
   }
}

