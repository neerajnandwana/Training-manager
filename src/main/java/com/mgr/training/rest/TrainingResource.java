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
import com.mgr.training.data.Training;
import com.mgr.training.store.TrainingStore;

@Singleton
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
@Path("training")
public class TrainingResource {
	private final TrainingStore trainingStore;

	@Inject
	public TrainingResource(TrainingStore trainingStore) {
		this.trainingStore = trainingStore;
	}

	@GET
	@Timed
	public Object all(@Context UriInfo ui) {
		MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
		return trainingStore.query(queryParams);
	}

	@GET
	@Timed
	@Path("{id}")
	public Object getById(@PathParam("id") String id) {
		return trainingStore.findById(id);
	}

	@POST
	@Timed
	@Path("{id}")
	public Object update(@PathParam("id") String id, Training training) {
		training.setTrainingId(id);
		return trainingStore.update(training);
	}

	@DELETE
	@Timed
	@Path("{id}")
	public Object deleteById(@PathParam("id") String id) {
		return trainingStore.deleteById(id);
	}
}
