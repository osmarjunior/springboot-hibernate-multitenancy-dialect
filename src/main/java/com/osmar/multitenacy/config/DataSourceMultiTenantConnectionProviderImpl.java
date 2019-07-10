package com.osmar.multitenacy.config;

import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;

public class DataSourceMultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

	private static final long serialVersionUID = 1L;

	@Autowired
	private Map<String, DataSource> dataSources;

	@Override
	protected DataSource selectAnyDataSource() {
		return this.dataSources.values().iterator().next();
	}

	@Override
	protected DataSource selectDataSource(String tenantIdentifier) {
		return this.dataSources.get(tenantIdentifier);
	}
}