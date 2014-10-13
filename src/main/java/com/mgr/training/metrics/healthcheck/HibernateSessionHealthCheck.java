package com.mgr.training.metrics.healthcheck;

import org.hibernate.Session;

import com.codahale.metrics.health.HealthCheck;
import com.google.common.base.Throwables;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.mgr.training.util.Const;
import com.mgr.training.util.Prop;

@Singleton
public class HibernateSessionHealthCheck extends HealthCheck {
	private final String validationQuery;
	private final Provider<Session> sessionProvider;

	@Inject
	public HibernateSessionHealthCheck(Provider<Session> sessionProvider) {
		this.validationQuery = Prop.applicationConfig.getString(Const.DB_QUERY_KEY, "SELECT 1");
		this.sessionProvider = sessionProvider;
	}

	@Override
	protected Result check() {
		final Session session = sessionProvider.get();
		try {
			session.createSQLQuery(validationQuery).list();
		} catch (Exception e) {
			throw Throwables.propagate(e);
		}
		return Result.healthy();
	}
}