package com.mgr.training.metrics.healthcheck;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.jdbc.ReturningWork;

import com.codahale.metrics.health.HealthCheck;
import com.google.common.base.MoreObjects;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.mgr.training.util.Const;
import com.mgr.training.util.Prop;

@Singleton
public class HibernateSessionHealthCheck extends HealthCheck {
	private static final Log LOG = LogFactory.getLog(HibernateSessionHealthCheck.class);
	
	private final Provider<Session> sessionProvider;
	private final String validationQuery;

	@Inject
	public HibernateSessionHealthCheck(Provider<Session> sessionProvider) {
		this.validationQuery = Prop.applicationConfig.getString(Const.DB_QUERY_KEY);
		this.sessionProvider = sessionProvider;
	}

	@Override
	protected Result check() {
		final Session session = sessionProvider.get();
		try {
			session.createSQLQuery(getValidationQuery()).list();
		} catch (Exception e) {
			throw Throwables.propagate(e);
		}
		return Result.healthy();
	}

	private String getProductValidationQuery() {
		return sessionProvider.get().doReturningWork(GET_CURRENT_DB_VALIDATION_QUERY);
	}

	private String getValidationQuery() {
		return MoreObjects.firstNonNull(validationQuery, getProductValidationQuery());
	}
	
	private static final ReturningWork<String> GET_CURRENT_DB_VALIDATION_QUERY = new ReturningWork<String>() {
		private final String DEFAULT_QUERY = "SELECT 1";
		private final Map<String, String> PRODUCT_SPECIFIC_QUERIES = ImmutableMap.<String, String>builder()
				.put("HSQL Database Engine", "SELECT COUNT(*) FROM INFORMATION_SCHEMA.SYSTEM_USERS")
				.put("Oracle", "SELECT 'Hello' from DUAL")
				.put("Apache Derby", "SELECT 1 FROM SYSIBM.SYSDUMMY1")
				.build();
		
		@Override
		public String execute(Connection connection) throws SQLException {
			LOG.info("Detecting validation query...");
			final String productName = connection.getMetaData().getDatabaseProductName();
			final String query = MoreObjects.firstNonNull(PRODUCT_SPECIFIC_QUERIES.get(productName), DEFAULT_QUERY);
			LOG.info("Database Product Name: " + productName + "; Validation Query: " + query);
			return query;
		}
	};
}