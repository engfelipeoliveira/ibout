package br.com.smktbatch.config;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(basePackages = "br.com.smktbatch.repository.remote", entityManagerFactoryRef = "remoteEntityManager", transactionManagerRef = "remoteTransactionManager")
public class RemoteConfig {

	@Value("${remote.hibernate.hbm2ddl.auto}")
	private String hibernateHbm2ddlAuto;
	
	@Value("${remote.jdbc.driverClassName}")
	private String driverClassName;
	
	@Value("${remote.jdbc.url}")
	private String jdbcUrl;
	
	@Value("${remote.jdbc.user}")
	private String jdbcUser;
	
	@Value("${remote.jdbc.pass}")
	private String jdbcPass;
	
	@Bean
	@Primary
	public LocalContainerEntityManagerFactoryBean remoteEntityManager() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(remoteDataSource());
		em.setPackagesToScan(new String[] { "br.com.smktbatch.model.remote" });

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		HashMap<String, Object> properties = new HashMap<>();
		properties.put("hibernate.hbm2ddl.auto", hibernateHbm2ddlAuto);
		em.setJpaPropertyMap(properties);

		return em;
	}

	@Primary
	@Bean
	public DataSource remoteDataSource() {

		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(driverClassName);
		dataSource.setUrl(jdbcUrl);
		dataSource.setUsername(jdbcUser);
		dataSource.setPassword(jdbcPass);

		return dataSource;
	}

	@Primary
	@Bean
	public PlatformTransactionManager remoteTransactionManager() {

		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(remoteEntityManager().getObject());
		return transactionManager;
	}
}
