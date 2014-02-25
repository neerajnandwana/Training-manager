//package com.mgr.training.store;
//
//import java.io.Serializable;
//import java.util.List;
//import java.util.concurrent.Callable;
//
//import org.hibernate.Session;
//
//import com.google.common.util.concurrent.ListenableFuture;
//import com.google.common.util.concurrent.ListeningExecutorService;
//import com.google.inject.Provider;
//
//public class AsyncBaseStore<T, ID extends Serializable> extends BaseStore<T, ID> {
//	private ListeningExecutorService executor;
//
//	public AsyncBaseStore(Provider<Session> session, ListeningExecutorService executor) {
//		super(session);
//		this.executor = executor;
//	}
//
//	public ListenableFuture<T> findAsync(final ID id) {
//		return executor.submit(new Callable<T>() {
//			@Override
//			public T call() throws Exception {
//				return store.find(id);
//			}
//		});
//	}
//
//	public ListenableFuture<List<T>> find(final List<ID> ids) {
//		return executor.submit(new Callable<List<T>>() {
//			@Override
//			public List<T> call() throws Exception {
//				return store.find(ids);
//			}
//		});
//	}
//
//	public ListenableFuture<List<T>> all() {
//		return executor.submit(new Callable<List<T>>() {
//			@Override
//			public List<T> call() throws Exception {
//				return store.all();
//			}
//		});
//	}
//
//	public ListenableFuture<T> add(final T entity) {
//		return executor.submit(new Callable<T>() {
//			@Override
//			public T call() throws Exception {
//				return store.add(entity);
//			}
//		});
//	}
//
//	public ListenableFuture<List<T>> add(final List<T> entities) {
//		return executor.submit(new Callable<List<T>>() {
//			@Override
//			public List<T> call() throws Exception {
//				return store.add(entities);
//			}
//		});
//	}
//
//	public ListenableFuture<T> remove(final T entity) {
//		return executor.submit(new Callable<T>() {
//			@Override
//			public T call() throws Exception {
//				return store.remove(entity);
//			}
//		});
//	}
//
//	public ListenableFuture<T> remove(final ID id) {
//		return executor.submit(new Callable<T>() {
//			@Override
//			public T call() throws Exception {
//				return store.remove(id);
//			}
//		});
//	}
//
//	public ListenableFuture<List<T>> remove(final List<T> entities) {
//		return executor.submit(new Callable<List<T>>() {
//			@Override
//			public List<T> call() throws Exception {
//				return store.remove(entities);
//			}
//		});
//	}
//
//	public ListenableFuture<T> update(final T entity) {
//		return executor.submit(new Callable<T>() {
//			@Override
//			public T call() throws Exception {
//				return store.update(entity);
//			}
//		});
//	}
//
//	public ListenableFuture<List<T>> update(final List<T> entities) {
//		return executor.submit(new Callable<List<T>>() {
//			@Override
//			public List<T> call() throws Exception {
//				return store.update(entities);
//			}
//		});
//	}
//
//}
