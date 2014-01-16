package com.mgr.training.store;

import org.hibernate.Session;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.mgr.training.data.Employee;


public class EmployeeStore extends BaseStore<Employee, String> {
	@Inject
	public EmployeeStore(Provider<Session> session) {
		super(session);
	}
	
}
