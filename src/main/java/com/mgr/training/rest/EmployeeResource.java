package com.mgr.training.rest;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mgr.training.data.Employee;
import com.mgr.training.service.EmployeeService;

@Path("emp")
@Produces({ MediaType.APPLICATION_JSON })
@Singleton
public class EmployeeResource {
	private final EmployeeService empService;

	@Inject
	public EmployeeResource(EmployeeService empService) {
		this.empService = empService;
	}

	@GET
	public List<Employee> all() throws InterruptedException, ExecutionException {
		return empService.all().get();
	}

	@GET
	@Path("{id}")
	public Employee getById(@PathParam("id") String id) throws InterruptedException, ExecutionException {
		return empService.find(id).get();
	}

	@DELETE
	@Path("{id}")
	public Employee deleteById(@PathParam("id") String id) throws InterruptedException, ExecutionException {
		return empService.delete(id).get();
	}
}
