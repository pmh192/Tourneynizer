package com.tourneynizer.tourneynizer.helper;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.jdbc.JdbcTestUtils;

public class TestWithContext {

    protected final ApplicationContext context;
    protected final JdbcTemplate jdbcTemplate;

    public TestWithContext() {
        context = new ClassPathXmlApplicationContext("TestSpringModule.xml");
        jdbcTemplate = new JdbcTemplate(context.getBean("dataSource", DriverManagerDataSource.class));
    }

    protected void clearDB() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "user_participation");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "sessions");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "teamRequest");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "tournamentRequest");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "roster");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "matches");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "teams");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "tournaments");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "users");
    }
}
