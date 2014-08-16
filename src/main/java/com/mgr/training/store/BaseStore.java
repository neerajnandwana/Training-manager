package com.mgr.training.store;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.Callable;

import javax.ws.rs.core.MultivaluedMap;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.google.common.reflect.TypeToken;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import com.mgr.training.util.Utils;

public class BaseStore<T, ID extends Serializable> {
	protected final Provider<Session> sessionProvider;
	protected final int batchSize = 0; // 1000+ records committed in single transaction

	public final AsyncStoreWrapper async;

	public BaseStore(final Provider<Session> session, final ListeningExecutorService executor) {
		this.sessionProvider = session;
		this.async = buildAsyncStore(this, executor);
	}

	private final TypeToken<T> typeToken = new TypeToken<T>(getClass()) {};
	public final Class<T> entityClass = (Class<T>) typeToken.getRawType();

	public T findById(final ID id) {
		return (T) currentSession().get(entityClass, id);
	}

	public List<T> findById(final List<ID> ids) throws Exception {
		Session session = currentSession();
		String idProperty = session.getSessionFactory().getClassMetadata(entityClass).getIdentifierPropertyName();
		return session.createCriteria(entityClass).add(Restrictions.in(idProperty, ids)).list();
	}

	public List<T> all() {
		return currentSession().createCriteria(entityClass).list();
	}

	public List<T> query(final MultivaluedMap<String, String> queryParams) {
		// if no query params is passed
		if (queryParams == null || queryParams.isEmpty()) {
			return all();
		}

		Session session = currentSession();
		Criteria criteria = session.createCriteria(entityClass);

		List<String> sort = queryParams.get("$sort");
		String limit = queryParams.getFirst("$limit");
		String page = queryParams.getFirst("$page");

		int defaultLimit = 100;
		int defaultPage = 1;

		int limit_int = defaultLimit;
		int page_int = defaultPage;

		// sort param should be list of sortOrder and sortProperty like
		// sort=asc,name,desc,level,asc,age etc
		if (!Utils.isNullOrEmpty(sort) && sort.size() % 2 == 0) {
			for (int i = 0, size = sort.size(); i <= size; i += 2) {
				String sortOrder = sort.get(i);
				String sortAttribute = sort.get(i + 1);
				if ("asc".equalsIgnoreCase(sortOrder)) {
					criteria.addOrder(Order.asc(sortAttribute));
				} else if ("desc".equalsIgnoreCase(sortOrder)) {
					criteria.addOrder(Order.desc(sortAttribute));
				}
			}
		}

		// list pagination properties
		if (!Utils.isNullOrEmpty(page)) {
			try {
				page_int = Integer.parseInt(page.trim());
			} catch (NumberFormatException e) {
				// don't do anything
			}
		}
		criteria.setFirstResult(page_int - 1);

		if (!Utils.isNullOrEmpty(limit)) {
			try {
				limit_int = Integer.parseInt(limit);
			} catch (NumberFormatException e) {
				// don't do anything
			}
		}
		criteria.setMaxResults(limit_int);

		return criteria.list();
	}

	@Transactional
	public T create(final T entity) {
		currentSession().save(entity);
		return entity;
	}

	@Transactional
	public List<T> create(final List<T> entities) {
		Session session = currentSession();
		for (int count = 0, size = entities.size(); count < size; count++) {
			session.save(entities.get(count));
			flushClearSession(count, session);
		}
		return entities;
	}

	@Transactional
	public T delete(final T entity) {
		currentSession().delete(entity);
		return entity;
	}

	@Transactional
	public T deleteById(final ID id) {
		T entity = findById(id);
		currentSession().delete(entity);
		return entity;
	}

	@Transactional
	public List<T> delete(final List<T> entities) {
		Session session = currentSession();
		for (T entity : entities) {
			session.delete(entity);
		}
		return entities;
	}

	@Transactional
	public T update(final T entity) {
		currentSession().update(entity);
		return entity;
	}

	@Transactional
	public List<T> update(final List<T> entities) {
		Session session = currentSession();
		for (int count = 0, size = entities.size(); count < size; count++) {
			session.update(entities.get(count));
			flushClearSession(count, session);
		}
		return entities;
	}

	public void flushClearSession(final int counter, final Session session) {
//		if (batchSize == 0) {
//			return;
//		}
//		if (counter % batchSize == 0) {
//			session.clear();
//			session.flush();
//		}
	}
	
	private Session currentSession(){
		return sessionProvider.get();
	}

	protected AsyncStoreWrapper buildAsyncStore(final BaseStore store, final ListeningExecutorService executor) {
		return new AsyncStoreWrapper(store, executor);
	}

	public class AsyncStoreWrapper {
		protected final ListeningExecutorService executor;
		protected final BaseStore<T, ID> store;

		public AsyncStoreWrapper(BaseStore<T, ID> store, ListeningExecutorService executor) {
			this.executor = executor;
			this.store = store;
		}

		public ListenableFuture<T> findById(final ID id) {
			return executor.submit(new Callable<T>() {
				@Override
				public T call() throws Exception {
					return store.findById(id);
				}
			});
		}

		public ListenableFuture<List<T>> findById(final List<ID> ids) {
			return executor.submit(new Callable<List<T>>() {
				@Override
				public List<T> call() throws Exception {
					return store.findById(ids);
				}
			});
		}

		public ListenableFuture<List<T>> all() {
			return executor.submit(new Callable<List<T>>() {
				@Override
				public List<T> call() throws Exception {
					return store.all();
				}
			});
		}

		public ListenableFuture<T> create(final T entity) {
			return executor.submit(new Callable<T>() {
				@Override
				public T call() throws Exception {
					return store.create(entity);
				}
			});
		}

		public ListenableFuture<List<T>> create(final List<T> entities) {
			return executor.submit(new Callable<List<T>>() {
				@Override
				public List<T> call() throws Exception {
					return store.create(entities);
				}
			});
		}

		public ListenableFuture<T> delete(final T entity) {
			return executor.submit(new Callable<T>() {
				@Override
				public T call() throws Exception {
					return store.delete(entity);
				}
			});
		}

		public ListenableFuture<T> deleteById(final ID id) {
			return executor.submit(new Callable<T>() {
				@Override
				public T call() throws Exception {
					return store.deleteById(id);
				}
			});
		}

		public ListenableFuture<List<T>> delete(final List<T> entities) {
			return executor.submit(new Callable<List<T>>() {
				@Override
				public List<T> call() throws Exception {
					return store.delete(entities);
				}
			});
		}

		public ListenableFuture<T> update(final T entity) {
			return executor.submit(new Callable<T>() {
				@Override
				public T call() throws Exception {
					return store.update(entity);
				}
			});
		}

		public ListenableFuture<List<T>> update(final List<T> entities) {
			return executor.submit(new Callable<List<T>>() {
				@Override
				public List<T> call() throws Exception {
					return store.update(entities);
				}
			});
		}
	}
}
