package com.osmar.multitenacy.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "multitenancy.products")
public class MultiTenantProperties {

	private List<DataSourceProperties> dataSourcesProps;

	public List<DataSourceProperties> getDataSources() {
		return this.dataSourcesProps;
	}

	public void setDataSources(List<DataSourceProperties> dataSourcesProps) {
		this.dataSourcesProps = dataSourcesProps;
	}

	public static class DataSourceProperties extends org.springframework.boot.autoconfigure.jdbc.DataSourceProperties {

		private String tenantId;

		public String getTenantId() {
			return tenantId;
		}

		public void setTenantId(String tenantId) {
			this.tenantId = tenantId;
		}
	}
}