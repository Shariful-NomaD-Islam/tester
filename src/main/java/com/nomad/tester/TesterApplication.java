package com.nomad.tester;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;

@SpringBootApplication
public class TesterApplication {
    private static final Logger log = LoggerFactory.getLogger(TesterApplication.class);

    @Autowired
    private Environment env;

    public static void main(String[] args) {
        var applicationHome = new ApplicationHome(TesterApplication.class);
        System.setProperty("app.home", applicationHome.getDir().getParent());
        System.setProperty("spring.config.location", "classpath:application.yml");
        System.setProperty("spring.config.additional-location", "${app.home}/conf/application.yml");
        SpringApplication application = new SpringApplication(TesterApplication.class);
        application.run(args);
    }

//    @Bean(name = "source-database")
//    public DataSource sourceDataSource() {
//        HikariDataSource dataSource = new HikariDataSource();
//        dataSource.setDriverClassName(env.getRequiredProperty("spring.datasource.driver"));
//        dataSource.setJdbcUrl(env.getRequiredProperty("spring.datasource.url"));
//        dataSource.setUsername(env.getRequiredProperty("spring.datasource.username"));
//        dataSource.setPassword(env.getRequiredProperty("spring.datasource.password"));
//        dataSource.setAutoCommit(true);
//        dataSource.setMaximumPoolSize(env.getRequiredProperty("spring.datasource.concurrency", Integer.class) * 2);
//        return dataSource;
//    }
//
//    @Bean(name = "source-jdbc")
//    public JdbcTemplate sourceJdbcTemplate(@Qualifier("source-database") DataSource dataSource) {
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
//        jdbcTemplate.setFetchSize(env.getRequiredProperty("spring.datasource.fetchSize", Integer.class));
//        return jdbcTemplate;
//    }
//

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
