package com.mgr.training.store;

import org.hibernate.Session;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.mgr.training.data.Training;

public class TrainingStore extends BaseStore<Training, String> {

	@Inject
	public TrainingStore(Provider<Session> session, ListeningExecutorService executor) {
		super(session, executor);
	}
}
