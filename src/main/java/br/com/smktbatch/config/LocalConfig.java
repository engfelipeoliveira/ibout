package br.com.smktbatch.config;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@PropertySource({ "classpath:persistence-multiple-db.properties" })
@EnableJpaRepositories(basePackages = "br.com.smktbatch.repository.local", entityManagerFactoryRef = "localEntityManager", transactionManagerRef = "localTransactionManager")
public class LocalConfig {

	@Autowired
	private Environment env;

	@Bean
	public LocalContainerEntityManagerFactoryBean localEntityManager() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(localDataSource());
		em.setPackagesToScan(new String[] { "br.com.smktbatch.model.local" });

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		HashMap<String, Object> properties = new HashMap<>();
		properties.put("hibernate.hbm2ddl.auto", env.getProperty("local.hibernate.hbm2ddl.auto"));
		em.setJpaPropertyMap(properties);

		return em;
	}

	@Bean
	public DataSource localDataSource() {

		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty("local.jdbc.driverClassName"));
		dataSource.setUrl(env.getProperty("local.jdbc.url"));
		dataSource.setUsername(env.getProperty("local.jdbc.user"));
		dataSource.setPassword(env.getProperty("local.jdbc.pass"));

		return dataSource;
	}

	@Primary
	@Bean
	public PlatformTransactionManager localTransactionManager() {

		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(localEntityManager().getObject());
		return transactionManager;
	}
}
