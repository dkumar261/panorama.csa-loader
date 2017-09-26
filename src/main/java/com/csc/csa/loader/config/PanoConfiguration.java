package com.csc.csa.loader.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.csc.csa.loader.util.Constants;

/**
 * 
 * @author dkumar261
 *
 */

@Configuration
public class PanoConfiguration {

	@Autowired
	private Environment environment;

	@Bean(name = "dataSource", destroyMethod = "close")
	public BasicDataSource dataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(environment.getProperty(Constants.DB_DRIVER));
		dataSource.setUrl(environment.getProperty(Constants.DB_URL));
		dataSource.setUsername(environment.getProperty(Constants.DB_USERNAME));
		dataSource.setPassword(environment.getProperty(Constants.DB_PASSWORD));
		dataSource.setMaxIdle(10);
		dataSource.setMaxActive(100);
		dataSource.setMaxWait(1000);
		dataSource.setDefaultAutoCommit(false);
		return dataSource;
	}

	@Bean
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
		return new NamedParameterJdbcTemplate(dataSource());
	}
}
