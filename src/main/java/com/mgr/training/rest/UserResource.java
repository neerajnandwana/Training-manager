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
import com.mgr.training.data.User;
import com.mgr.training.service.UserService;


@Path("user")
@Produces({ MediaType.APPLICATION_JSON })
@Singleton
public class UserResource {
	private final UserService userService;

	@Inject
	public UserResource(UserService userService) {
		this.userService = userService;
	}

	@GET
	public List<User> all() throws InterruptedException, ExecutionException {
		return userService.all().get();
	}

	@GET
	@Path("{id}")
	public User getById(@PathParam("id") String id) throws InterruptedException, ExecutionException {
		return userService.find(id).get();
	}

	@DELETE
	@Path("{id}")
	public User deleteById(@PathParam("id") String id) throws InterruptedException, ExecutionException {
		return userService.delete(id).get();
	}
}

