package com.osmar.multitenacy.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.osmar.multitenacy.config.MultiTenantProperties.DataSourceProperties;

@Configuration
@EnableConfigurationProperties({ MultiTenantProperties.class, JpaProperties.class })
@EnableJpaRepositories(basePackages = { "com.osmar.multitenacy.persistence" }, transactionManagerRef = "txManager")
@EnableTransactionManagement
public class MultiTenantJpaConfiguration {

	@Autowired
	private JpaProperties jpaProperties;

	@Autowired
	private MultiTenantProperties multiTenantProperties;

	@Bean(name = "dataSources" )
	public Map<String, DataSource> dataSources() {
		Map<String, DataSource> result = new HashMap<>();
		for (DataSourceProperties dsProperties : this.multiTenantProperties.getDataSources()) {
			DataSourceBuilder<?> factory = DataSourceBuilder
				.create()
				.url(dsProperties.getUrl())
				.username(dsProperties.getUsername())
				.password(dsProperties.getPassword())
				.driverClassName(dsProperties.getDriverClassName());
			result.put(dsProperties.getTenantId(), factory.build());
		}
		return result;
	}

	@Bean
	public MultiTenantConnectionProvider multiTenantConnectionProvider() {
		return new DataSourceMultiTenantConnectionProviderImpl();
	}

	@Bean
	public CurrentTenantIdentifierResolver currentTenantIdentifierResolver() {
		return new TenantIdentifierResolverImpl();
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(MultiTenantConnectionProvider multiTenantConnectionProvider,
		CurrentTenantIdentifierResolver currentTenantIdentifierResolver) {

		Map<String, Object> hibernateProps = new LinkedHashMap<>();
		hibernateProps.putAll(this.jpaProperties.getProperties());
		hibernateProps.put(Environment.MULTI_TENANT, MultiTenancyStrategy.DATABASE);
		hibernateProps.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
		hibernateProps.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolver);
		hibernateProps.put(Environment.DIALECT, "com.osmar.multitenacy.config.MultiTenantDialect");
		hibernateProps.put(Environment.SHOW_SQL, "true");

		LocalContainerEntityManagerFactoryBean result = new LocalContainerEntityManagerFactoryBean();
		result.setPackagesToScan("com.osmar.multitenacy.model");
		result.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		result.setJpaPropertyMap(hibernateProps);

		return result;
	}

	@Bean
	public EntityManagerFactory entityManagerFactory(LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
		return entityManagerFactoryBean.getObject();
	}

	@Bean
	public PlatformTransactionManager txManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory);
		return transactionManager;
	}
}