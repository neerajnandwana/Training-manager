package com.mgr.training.data;

import java.util.Properties;

import javax.persistence.EntityManager;

import org.apache.commons.configuration.ConfigurationConverter;
import org.hibernate.Session;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.mgr.training.store.EmployeeStore;
import com.mgr.training.store.TrainingStore;
import com.mgr.training.store.UserStore;
import com.mgr.training.util.Const;
import com.mgr.training.util.Prop;

public class PersistenceModule extends AbstractModule {

	@Override
	protected void configure() {
		Properties prop = ConfigurationConverter.getProperties(Prop.persistenceConfig);
		// hibernate config module
		install(new JpaPersistModule(Const.PERSISTENCE_UNIT_NAME).properties(prop));

		// configure data service
		bind(EmployeeStore.class);
		bind(TrainingStore.class);
		bind(UserStore.class);
	}

	@Provides
	@Inject
	protected Session providesHibernateSession(EntityManager entityManager) {
		return (Session) entityManager.getDelegate();
	}
}
