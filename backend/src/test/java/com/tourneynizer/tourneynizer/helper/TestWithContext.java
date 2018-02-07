package com.tourneynizer.tourneynizer.helper;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class TestWithContext {

    protected final ApplicationContext context;
    protected final JdbcTemplate jdbcTemplate;

    public TestWithContext() {
        context = new ClassPathXmlApplicationContext("TestSpringModule.xml");
        jdbcTemplate = new JdbcTemplate(context.getBean("dataSource", DriverManagerDataSource.class));
    }
}
