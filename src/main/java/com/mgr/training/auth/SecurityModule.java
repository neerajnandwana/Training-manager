package com.mgr.training.auth;

import com.google.inject.AbstractModule;

public class SecurityModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(PasswordDigest.class);
	}

}
