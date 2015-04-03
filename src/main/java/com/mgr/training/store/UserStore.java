package com.mgr.training.store;

import java.io.IOException;
import java.util.concurrent.Callable;

import org.hibernate.Session;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.mgr.training.data.User;

public class UserStore extends BaseStore<User, String> {

	@Inject
	public UserStore(Provider<Session> session, ListeningExecutorService executor) {
		super(session, executor);
	}

	public User getByCredential(final String userId, final String userPass) throws ClassNotFoundException, IOException {
		User user = findById(userId);
		if (user == null) {
			return null;
		}
		boolean isAuthorize = user.getPassword().verify(userPass.toCharArray());//password.verify(user.getPassword(), userPass);
		return isAuthorize ? user : null;
	}

	protected AsyncUserStoreWrapper buildAsyncStore(UserStore store, ListeningExecutorService executor) {
		return new AsyncUserStoreWrapper(store, executor);
	}

	public class AsyncUserStoreWrapper extends AsyncStoreWrapper {

		public AsyncUserStoreWrapper(BaseStore<User, String> store, ListeningExecutorService executor) {
			super(store, executor);
		}

		public ListenableFuture<User> getByCredential(final String userId, final String userPass) {
			return executor.submit(new Callable<User>() {
				@Override
				public User call() throws Exception {
					return ((UserStore) store).getByCredential(userId, userPass);
				}
			});
		}

	}
}
