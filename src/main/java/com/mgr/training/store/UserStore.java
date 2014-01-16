package com.mgr.training.store;

import org.hibernate.Session;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.mgr.training.data.User;

public class UserStore extends BaseStore<User, String> {

	@Inject
	public UserStore(Provider<Session> session) {
		super(session);
	}
}
