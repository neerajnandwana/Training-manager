package com.mgr.training.metrics.healthcheck;

import org.hibernate.Session;

import com.codahale.metrics.health.HealthCheck;
import com.google.common.base.Throwables;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class HibernateSessionHealthCheck extends HealthCheck {
	private final String validationQuery = "select 1";
	private final Provider<Session> sessionProvider;

	@Inject
	public HibernateSessionHealthCheck(Provider<Session> sessionProvider) {
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