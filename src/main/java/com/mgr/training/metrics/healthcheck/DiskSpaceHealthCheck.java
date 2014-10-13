package com.mgr.training.metrics.healthcheck;

import com.codahale.metrics.health.HealthCheck;
import com.mgr.training.util.Const;
import com.mgr.training.util.Prop;

public class DiskSpaceHealthCheck extends HealthCheck {
	private final long threshold;

	public DiskSpaceHealthCheck() {
		//10 GB default threshold
		threshold = Prop.applicationConfig.getLong(Const.DISKSPACE_THRESHOLD_KEY, 10 * 1024 * 1024);
	}

	@Override
	protected Result check() throws Exception {
		long freeSpace = Const.OS_BEAN.getFreePhysicalMemorySize();
		if (freeSpace >= threshold) {
			return Result.healthy();
		}
		return Result.unhealthy("Free disk space below threshold. Available: %d bytes [threshold: %d bytes]", freeSpace, threshold);
	}
}
