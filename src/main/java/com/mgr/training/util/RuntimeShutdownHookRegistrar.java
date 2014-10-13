package com.mgr.training.util;
/**
 * A {@link ShutdownHookRegistrar} that delegates to {@link Runtime}.
 */
public enum RuntimeShutdownHookRegistrar {
	INSTANCE;
	public void addShutdownHook(Thread hook) {
		Runtime.getRuntime().addShutdownHook(hook);
	}

	public boolean removeShutdownHook(Thread hook) {
		return Runtime.getRuntime().removeShutdownHook(hook);
	}
}