package com.mgr.training.store;

import org.hibernate.Session;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.mgr.training.data.Training;

public class TrainingStore extends BaseStore<Training, String> {
	
	@Inject
	public TrainingStore(Provider<Session> session) {
		super(session);
	}
}
