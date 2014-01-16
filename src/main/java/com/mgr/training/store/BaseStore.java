package com.mgr.training.store;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.google.common.reflect.TypeToken;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import com.mgr.training.util.Prop;

public class BaseStore<T, ID extends Serializable> {
	protected Provider<Session> sessionProvider = null;
	
	public BaseStore(Provider<Session> session) {
		this.sessionProvider = session;
	}

	private final TypeToken<T> typeToken = new TypeToken<T>(getClass()) {};
	public final Class<T> entityClass = (Class<T>) typeToken.getRawType();

	public T find(ID id) {
		return (T) sessionProvider.get().get(entityClass, id);
	}

	public List<T> find(List<ID> ids) throws Exception {
		Session session = sessionProvider.get();
		String idProperty = session.getSessionFactory().getClassMetadata(entityClass).getIdentifierPropertyName();
		return session.createCriteria(entityClass).add(Restrictions.in(idProperty, ids)).list();
	}

	public List<T> all() {
		return sessionProvider.get().createCriteria(entityClass).list();
	}

	@Transactional
	public void add(T entity) {
		sessionProvider.get().save(entity);
	}

	@Transactional
	public List<T> add(List<T> entities) {
		Session session = sessionProvider.get();
		for (int count = 0, size = entities.size(); count < size; count++) {
			session.save(entities.get(count));
			flushClearSession(count, session);
		}
		return entities;
	}

	@Transactional
	public void remove(T entity) {
		sessionProvider.get().delete(entity);
	}

	@Transactional
	public T remove(ID id) {
		T entity = find(id);
		sessionProvider.get().delete(entity);
		return entity;
	}

	@Transactional
	public void remove(List<T> entities) {
		Session session = sessionProvider.get();
		for (T entity : entities) {
			session.delete(entity);
		}
	}

	@Transactional
	public void update(T entity) {
		sessionProvider.get().update(entity);
	}

	@Transactional
	public void update(List<T> entities) {
		Session session = sessionProvider.get();
		for (int count = 0, size = entities.size(); count < size; count++) {
			session.update(entities.get(count));
			flushClearSession(count, session);
		}
	}

	public void flushClearSession(int counter, Session session) {
		int batchSize = Prop.persistenceConfig.getInt("hibernate.jdbc.batch_size");
		if(batchSize == 0){
			return;
		}
		if (counter % batchSize == 0) {
			session.clear();
			session.flush();
		}
	}
}
