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
import com.mgr.training.data.Training;
import com.mgr.training.service.TrainingService;

@Singleton
@Produces({ MediaType.APPLICATION_JSON })
@Path("training")
public class TrainingResource {
	private final TrainingService trainingService;

	@Inject
	public TrainingResource(TrainingService trainingService) {
		this.trainingService = trainingService;
	}

	@GET
	public List<Training> all() throws InterruptedException, ExecutionException {
		return trainingService.all().get();
	}

	@GET
	@Path("{id}")
	public Training getById(@PathParam("id") String id) throws InterruptedException, ExecutionException {
		return trainingService.find(id).get();
	}

	@DELETE
	@Path("{id}")
	public Training deleteById(@PathParam("id") String id) throws InterruptedException, ExecutionException {
		return trainingService.delete(id).get();
	}
}
