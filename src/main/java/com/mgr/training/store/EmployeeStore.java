package com.mgr.training.store;

import javax.ws.rs.core.MultivaluedMap;

import org.hibernate.Session;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.mgr.training.data.Employee;


public class EmployeeStore extends BaseStore<Employee, String> {
	@Inject
	public EmployeeStore(Provider<Session> session, ListeningExecutorService  executor) {
		super(session, executor);
	}
	
}
