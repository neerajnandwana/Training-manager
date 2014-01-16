package com.mgr.training.service;

import java.util.List;
import java.util.concurrent.Callable;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import com.mgr.training.auth.PasswordDigest;
import com.mgr.training.data.User;
import com.mgr.training.store.UserStore;

public class UserService {
	private final ListeningExecutorService executor;
	private final UserStore store;
	private final PasswordDigest password;

	@Inject
	public UserService(UserStore store, PasswordDigest password, ListeningExecutorService executor) {
		this.store = store;
		this.password = password;
		this.executor = executor;
	}

	public ListenableFuture<User> getByCredential(final String userId, final String userPass) {
		return executor.submit(new Callable<User>() {
			@Override
			public User call() throws Exception {
				User user = store.find(userId);
				if (user == null) {
					return null;
				}
				boolean isAuthorize = password.verify(user.getPassword(), userPass);
				return isAuthorize ? user : null;
			}
		});
	}

	public ListenableFuture<List<User>> add(final List<User> users) {
		return executor.submit(new Callable<List<User>>() {
			@Override
			public List<User> call() throws Exception {
				return store.add(users);
			}
		});
	}


	public ListenableFuture<List<User>> find(final List<String> ids) {
		return executor.submit(new Callable<List<User>>() {
			@Override
			public List<User> call() throws Exception {
				return store.find(ids);				
			}			
		});
	}

	public ListenableFuture<List<User>> all() {
		return executor.submit(new Callable<List<User>>() {
			@Override
			public List<User> call() throws Exception {
				return store.all();				
			}			
		});
	}

	public ListenableFuture<User> find(final String empId) {
		return executor.submit(new Callable<User>() {
			@Override
			public User call() throws Exception {
				return store.find(empId);				
			}			
		});
	}

	public ListenableFuture<User> delete(final String id) {
		return executor.submit(new Callable<User>() {
			@Override
			public User call() throws Exception {
				return store.remove(id);				
			}			
		});
	}
}
