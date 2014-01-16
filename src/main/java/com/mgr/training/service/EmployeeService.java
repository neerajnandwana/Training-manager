package com.mgr.training.service;

import java.util.List;
import java.util.concurrent.Callable;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import com.mgr.training.data.Employee;
import com.mgr.training.store.EmployeeStore;

public class EmployeeService {
	private final ListeningExecutorService  executor;
	private final EmployeeStore store;

	@Inject
	public EmployeeService(EmployeeStore store, ListeningExecutorService  executor) {
		this.store = store;
		this.executor = executor;
	}

	public ListenableFuture<List<Employee>> add(final List<Employee> employees) {
		return executor.submit(new Callable<List<Employee>>() {
			@Override
			public List<Employee> call() throws Exception {
				return store.add(employees);
			}
		});
	}

	public ListenableFuture<List<Employee>> find(final List<String> ids) {
		return executor.submit(new Callable<List<Employee>>() {
			@Override
			public List<Employee> call() throws Exception {
				return store.find(ids);				
			}			
		});
	}

	public ListenableFuture<List<Employee>> all() {
		return executor.submit(new Callable<List<Employee>>() {
			@Override
			public List<Employee> call() throws Exception {
				return store.all();				
			}			
		});
	}

	public ListenableFuture<Employee> find(final String empId) {
		return executor.submit(new Callable<Employee>() {
			@Override
			public Employee call() throws Exception {
				return store.find(empId);				
			}			
		});
	}

	public ListenableFuture<Employee> delete(final String id) {
		return executor.submit(new Callable<Employee>() {
			@Override
			public Employee call() throws Exception {
				return store.remove(id);				
			}			
		});
	}

}
