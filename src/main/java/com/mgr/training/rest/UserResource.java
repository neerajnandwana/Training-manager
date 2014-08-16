package com.mgr.training.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mgr.training.data.User;
import com.mgr.training.store.UserStore;

@Path("user")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
@Singleton
public class UserResource {
	private final UserStore userStore;

	@Inject
	public UserResource(UserStore userStore) {
		this.userStore = userStore;
	}

	@GET
	@Timed
	public Object all(@Context UriInfo ui) {
		MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
		return userStore.query(queryParams);
	}

	@GET
	@Timed
	@Path("{id}")
	public Object getById(@PathParam("id") String id) {
		return userStore.findById(id);
	}

	@POST
	@Timed
	@Path("{id}")
	public Object update(@PathParam("id") String id, User user) {
		user.setUserId(id);
		return userStore.update(user);
	}

	@DELETE
	@Timed
	@Path("{id}")
	public Object deleteById(@PathParam("id") String id) {
		return userStore.deleteById(id);
	}
}
