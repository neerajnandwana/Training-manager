package com.mgr.training.service;

import java.util.List;
import java.util.concurrent.Callable;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import com.mgr.training.data.Training;
import com.mgr.training.store.TrainingStore;

public class TrainingService {
	private final ListeningExecutorService  executor;
	private final TrainingStore store;

	@Inject
	public TrainingService(TrainingStore store, ListeningExecutorService executor) {
		this.store = store;
		this.executor = executor;
	}

	public ListenableFuture<List<Training>> add(final List<Training> training) {
		return executor.submit(new Callable<List<Training>>() {
			@Override
			public List<Training> call() throws Exception {
				return store.add(training);				
			}			
		});		
	}

	public ListenableFuture<List<Training>> all() {
		return executor.submit(new Callable<List<Training>>() {
			@Override
			public List<Training> call() throws Exception {
				return store.all();				
			}			
		});		
	}

	public ListenableFuture<Training> find(final String id) {
		return executor.submit(new Callable<Training>() {
			@Override
			public Training call() throws Exception {
				return store.find(id);				
			}			
		});		
	}

	public ListenableFuture<Training> delete(final String id) {
		return executor.submit(new Callable<Training>() {
			@Override
			public Training call() throws Exception {
				return store.remove(id);				
			}			
		});
	}
}
