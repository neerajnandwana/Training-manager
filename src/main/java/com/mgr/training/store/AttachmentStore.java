package com.mgr.training.store;

import org.hibernate.Session;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.mgr.training.data.Attachment;

public class AttachmentStore extends BaseStore<Attachment, String> {

	@Inject
	public AttachmentStore(Provider<Session> session, ListeningExecutorService executor) {
		super(session, executor);
	}
}
