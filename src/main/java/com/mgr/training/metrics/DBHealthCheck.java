package com.mgr.training.metrics;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.codahale.metrics.health.HealthCheck;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class DBHealthCheck extends HealthCheck {
	@Inject
	Provider<Session> sessionProvider;
	private final String validationQuery = "select 1 from DUAL";

	@Override
	protected Result check() throws Exception {
		System.out.println("in DB Health check  >>"+sessionProvider);
		final Session session = sessionProvider.get();
		System.out.println("in DB session found");
		final Transaction txn = session.beginTransaction();
		System.out.println("in DB transaction started");
		try {
			session.createSQLQuery(validationQuery).list();
			System.out.println("in DB query executed");
			txn.commit();
			System.out.println("in DB cmminted");
		} catch (Exception e) {
			if (txn.isActive()) {
				txn.rollback();
			}
			e.printStackTrace();
			throw e;
		}
		return Result.healthy();
	}
}