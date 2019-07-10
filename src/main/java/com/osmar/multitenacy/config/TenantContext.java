package com.osmar.multitenacy.config;

public class TenantContext {

	private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();
	private static final ThreadLocal<String> CONTEXT_DIALECT = new ThreadLocal<>();

	public static void setTenantId(String tenantId, String dialect) {
		CONTEXT.set(tenantId);
		CONTEXT_DIALECT.set(dialect);
	}

	public static String getTenantId() {
		return CONTEXT.get();
	}

	public static String getContextDialect() {
		return CONTEXT_DIALECT.get();
	}
}