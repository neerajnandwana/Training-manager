package com.mgr.training.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * This class run the task parallel without halting the current process.
 * 
 * @author Neeraj.Nandwana
 * 
 */
@Singleton
public class Tasks {
	private static final Log LOG = LogFactory.getLog(Tasks.class);
	private final ListeningExecutorService  executor;

	@Inject
	public Tasks(ListeningExecutorService  executor) {
		this.executor = executor;
	}

	public ListenableFuture<?> run(Runnable runnable) {
		LOG.debug("New task submitted.");
		return executor.submit(runnable);
	}

}
